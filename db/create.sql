-- CREATE DATABASE "JRMI"
--     WITH 
--     OWNER = postgres
--     ENCODING = 'UTF8'
--     CONNECTION LIMIT = -1;

CREATE SEQUENCE ACCOUNT_SEQ START WITH 3000;

CREATE TABLE JRMI_USER (
	document_id varchar UNIQUE NOT NULL,
	name varchar NOT NULL,
	username varchar UNIQUE NOT NULL,
	password varchar NOT NULL
);

CREATE TABLE JRMI_ACCOUNT (
	number integer UNIQUE NOT NULL DEFAULT nextval('ACCOUNT_SEQ'),
	current_balance float NOT NULL,
	fk_user varchar NOT NULL,
	CONSTRAINT fk_user FOREIGN KEY (fk_user) REFERENCES JRMI_USER(document_id)
);

CREATE TABLE JRMI_TRANSACTION (
	id serial primary key,
	amount float NOT NULL,
	date timestamp NOT NULL DEFAULT NOW(),
	description varchar NOT NULL,
	type varchar NOT NULL,
	fk_account_source integer,
	fk_account_destination integer,
	CONSTRAINT fk_account_source FOREIGN KEY (fk_account_source) REFERENCES JRMI_ACCOUNT(number),
	CONSTRAINT fk_account_destination FOREIGN KEY (fk_account_destination) REFERENCES JRMI_ACCOUNT(number),
	CONSTRAINT check_constraint_type CHECK(type IN ('deposit', 'withdrawal', 'transference'))
);

-- DROP TABLE JRMI_TRANSACTION;
-- DROP TABLE JRMI_ACCOUNT;
-- DROP TABLE JRMI_USER;
-- DROP SEQUENCE ACCOUNT_SEQ;