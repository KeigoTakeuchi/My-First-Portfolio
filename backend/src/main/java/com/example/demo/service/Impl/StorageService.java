package com.example.demo.service.Impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.example.demo.exception.StorageException;

import reactor.core.publisher.Flux;

@Service
@Transactional
public class StorageService {

	//デバッグ
	private static final Logger logger = LoggerFactory.getLogger(StorageService.class);
	
	private final String bucket;
	private final WebClient webClient;
	private final String baseUrl;
	
	//レコード
	//これは画像の一覧取得をする際の取得条件(取得数など)を決めるためのrecord
	public record ListOptions(int limit) {}
	public record ListObjectRequest(String path, ListOptions options) {}
	
	public record StorageFile(String name) {}
	
	//コンストラクタ
	public StorageService(
			@Value("${supabase.url}") String supabaseUrl,
			@Value("${supabase.bucket}") String bucket,
			@Value("${supabase.service-key}") String supabaseKey)  {
		this.baseUrl = supabaseUrl + "/storage/v1";
		this.bucket = bucket;
		this.webClient = WebClient.builder()
				.baseUrl(baseUrl)
				.defaultHeader("apikey", supabaseKey)
				.defaultHeader("Authorization", "Bearer " + supabaseKey)
				.build();
	}
	
	public String upload(Integer accountId,Integer messageId,String uuid,String ext, byte[] bytes,String contentType) {
		
		//logger.info("supabase storageにupload処理を行います... Content-Typeは: {}",contentType);
		
		String objectPath = String.format("%d/%d/%s.%s",accountId,messageId, uuid, ext);
		try {
			webClient.post()
				.uri("/object/{bucket}/{objectPath}",bucket,objectPath)
				.contentType(MediaType.parseMediaType(contentType == null ? "application/octet-stream" : contentType))
				.bodyValue(bytes)
				.retrieve()
				.onStatus(status -> !status.is2xxSuccessful(),ClientResponse::createException)
				.bodyToMono(Void.class)
				.block();
			
			return this.publicUrl(objectPath);
		}catch (WebClientResponseException e) {
			throw new StorageException("Failed to upload objects" + e);
		}
	}
	
	public void delete(String publicPath) {
		
		String prefix = "/object/public/" + this.bucket + "/";
		
		int startInt = publicPath.indexOf(prefix);
		
		if(startInt == -1) {
			throw new IllegalArgumentException("保存されたURLの形式が不正です。次の形を期待しています: " + prefix);
		}
		//完全なurlから、prefixで指定した文字列の最後尾からurlの最後まで、抽出している(以下の引数は抽出開始位置の指定)
		String objectPath = publicPath.substring(startInt + prefix.length());

		try {
			webClient.delete()
				.uri(uriBuilder -> uriBuilder
						.path("/object/{bucket}/")
						.path(objectPath)
						.build(bucket))
				.retrieve()
				.onStatus(status -> !status.is2xxSuccessful(),ClientResponse::createException)
				.bodyToMono(Void.class)
				.block();
		}catch (WebClientResponseException e) {
			throw new StorageException("Failed to delete objects. Status :" + e.getStatusCode() + ", Body :" + e.getResponseBodyAsString(), e); 
		}
	}
	
	public Flux<String> getImagesUrl(Integer accountId,Integer messageId){
		
		String listPath = "messages/" + accountId + "/" + messageId;
		
		Map<String,Object> options = new HashMap<>();
		options.put("limit",50);
		
		Map<String,Object> requestBody = new HashMap<>();
		requestBody.put("path",listPath);
		requestBody.put("options",options);
		
		return webClient.post()
				.uri("object/list/{bucket}",bucket)
				.bodyValue(requestBody)
				.retrieve()
				.onStatus(status -> !status.is2xxSuccessful(),ClientResponse::createException)
				.bodyToFlux(StorageFile.class)
				.map(file -> publicUrl(listPath + "/" + file.name()))
				.onErrorMap(WebClientResponseException.class, e ->
						new StorageException("Failed to get list of images", e));
		}
	
	public String publicUrl(String objectPath) {
		return String.format("%s/object/public/%s/%s", baseUrl, bucket, objectPath);
	}
	
}
