CREATE TABLE growdevers (
	id uuid NOT NULL,
	name varchar(100) NOT NULL,
	email varchar(50) NOT NULL,
	cpf varchar(11) NOT NULL,
	status varchar(30) NOT NULL,
	phone varchar(11) NULL,
	created_at timestamp NOT NULL DEFAULT now(),
	updated_at timestamp NULL,
	enable bool NOT NULL DEFAULT true,
	CONSTRAINT growdevers_cpf_key UNIQUE (cpf),
	CONSTRAINT growdevers_email_key UNIQUE (email),
	CONSTRAINT growdevers_pkey PRIMARY KEY (id)
);