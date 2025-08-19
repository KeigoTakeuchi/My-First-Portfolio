INSERT INTO accounts (
	name,
	hashed_password,
	display_name,
	authority,
	created_at,
	updated_at,
	deleted_at
) VALUES(
	test,
	hashhash,
	tesuto,
	ADMIN,
	CURRENT_TIMESTAMP,
	CURRENT_TIMESTAMP,
	null
)
