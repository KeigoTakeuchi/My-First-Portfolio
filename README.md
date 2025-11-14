# 掲示板アプリ

ご覧いただきありがとうございます。<br>
こちらは、学習の成果として「実際に動くサービスを形にしたい」という想いから作成しました、簡易掲示板アプリケーションです。

## アプリの概要

こちらのアプリは、ユーザーがメッセージや画像を投稿して情報を共有できる掲示板サービスを想定して設計したbackend APIです。<br>
現在はAPIとして動作する形ですが、今後、frontendを実装し、ユーザーが実際に操作できるUIへと発展させる予定です。

### ▼ 実装済み機能
  - IDとパスワードを利用したユーザー登録 / ログイン機能
  - JWTを用いたセッション管理
  - ユーザー情報の変更機能
  - パスワードの変更機能
  - 退会機能
  - 投稿の取得 / 作成 / 更新 / 削除機能
  - 投稿の検索機能
  - 投稿内容のバリデーション
  - 画像の取得 / アップロード機能

### ▼ 使用技術  
| Category       | Technology Stack                                     |
| -------------- | ---------------------------------------------------- |
| Frontend       | HTML / CSS / Bootstrap / javascript                  |
| Backend        | Java(21),Spring Boot(3.5.4),Spring WebMVC(6.2.9),Spring Security(6.5.2),MyBatis(3.5.19)|
| Infrastructure | Vercel(frontend) / Render(backend)                   |
| Database       | supabase database / supabase storage                 |
| Environment    | -                                                    |
| CI/CD          | -                                                    |
| library        | Lombok, JWT                                          |
| Package Manager| Gradle                                               |
| etc.           | Git, GitHub                                          |
<br>
* Frontendは実装中、EnvironmentにはDockerを、CI/CDにはGitHub Actionsをいずれ実装予定。

### ▼ ER図

<img width="526" height="633" alt="Image" src="https://github.com/user-attachments/assets/2d2b2e4a-8a58-43fd-9036-d568c323c65e" />
<br>

### ▼ 工夫した点

<details>
  <summary>１．データベースの排他制御の実装</summary>

  - めったに他者との同時更新が起きないことを期待した楽観的排他制御(楽観ロック)を実装。
  - また、Renderにbackendプロジェクトをデプロイするために、データベースにsupabase databaseを利用しております。
  - 今回は各テーブルに更新日時(`updated_at`)カラムを持ち、比較検証することでデータの整合性を保証しております。
</details>

<details>
  <summary>２．SPA形式での実装</summary>

  - 今までSSR形式でのWebサービスを作成していたため、昨今主流なSPA形式で作成しました。
  - 現在は普遍的なRESTful Webサービスを学習しながら実装しました。
</details>

<details>
  <summary>３．セキュリティの強化</summary>

  - ログインにはSpring Securityを利用し、一度ログイン後のセッション管理・検証にJWT(JSON Web Token)を利用しております。
  - 今回はfrontend側で認証情報を扱わない目的で実装しましたが、認証情報をCookieなどで保存する機能は今後実装予定です。
  - 認可処理はJWTトークンの情報とURL内のid情報と比較し、分岐処理をしております。
  - また、学習用途に限り、JWT鍵にはオレオレ証明書(自己署名証明書)にて実装しております。
</details>

<details>
  <summary>４．画像投稿機能の実装</summary>

  - Message投稿に伴い画像を投稿できるような機能を実装しました。画像保存や取得にはsupabase storageを利用しております。
  - 画像サイズや形式にもバリデーションを設けております。また、supabaseではRLS(Row Level Security)を利用せず、backendプロジェクト内で認可処理をしております。
</details>

<br>

### ▼ 今後の課題及び追加予定機能

<details>
  <summary>１．frontendの実装</summary>

  - 上記使用技術にある通り、HTML,CSS,BootStrap,JavaScriptを利用したシンプルですが魅力的なUIを実装予定です。
</details>

<details>
  <summary>２．テストの実装</summary>

  - テスト周りの学習が進んでおらず、単体テストが作成できていなかったため、非常に苦労しました。今後は早急に学習をし単体テストから実装をする予定です。
  - 今回は、Postmanを用いてbackendプロジェクトであるREST APIを叩き、インテグレーションテストを行いました。
</details>

<details>
  <summary>３．CI/CDとDockerの実装</summary>

  - GitHub Actionsによる自動ビルド・デプロイと、Dockerによる環境構築を実装予定です。環境依存を減らし、より実務よりの仕上げを意識します。
</details>

<details>
  <summary>４．Cookieの利用</summary>

  - 現在アカウント作成 / ログイン後に発行されるJWTの保存先が未実装なので、JWTの利点を活かせておりません。そのため、今後はCookieを利用することで解決する予定です。
  - cookieには`http only`属性や`secure true`を設定することで、セキュリティの強化をするつもりです。
</details>

<details>
  <summary>５．ログの処理</summary>

  - 現状、例外が発生した際はエラーを返すカスタムを実装しておりますが、適切かつ詳細なログを出力できておりません。そのため、エラー発生時は根本の原因を追究するのに非常に時間がかかりました。
  - 今後はログ出力レベルを適切に設定し、ログファイルに書き込むなど対策を施す予定です。
</details>

### ▼ 次に活かしたい反省点

<details>
  <summary>１．スケジュールの整理</summary>

  - 締め切り日を事前に設けなかったことにより見通しが立てれませんでした。そのため、今後は最初に締め切り日を設定し、逆算してタスクを日ごとに割り振り明確にします。
</details>

<details>
  <summary>２．実装イメージの具体化</summary>

  - 要件定義の曖昧さにより機能追加やコードの複雑化・修正対応に追われました。今後は機能要件だけでなく、DTO設計や画面遷移図(またはfrontendのフレーム)を事前に整理し、実装イメージを具体化します。
</details>

<details>
  <summary>３．思考の整理</summary>

  - (２にも通じますが、)実装中に思考の整理が難しかったです。そのため、思考が複雑化した際に、「ToDo」「注意点」「疑問点」としてメモなどに書き出し、可視化することですべきことを明確にして作業への集中力を維持します。
</details>

### 最後に

ここまでご覧いただき、誠にありがとうございました。<br>
本プロジェクトを通して得た学びや反省を次の開発へ活かし、より良い設計とプロセスを目指してまいります。<br>
引き続き、技術力の向上と継続的な改善を重ねていきます。






