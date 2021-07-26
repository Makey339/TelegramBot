--liquibase formatted sql

--changeset makey:createUsers
CREATE TABLE IF NOT EXISTS "users"
(
    user_id bigint NOT NULL,
    first_name character varying COLLATE pg_catalog."default",
    last_name character varying COLLATE pg_catalog."default",
    user_name character varying COLLATE pg_catalog."default",
    last_message_at timestamp,
    CONSTRAINT "Users_pkey" PRIMARY KEY (user_id)
    )

    TABLESPACE pg_default;

ALTER TABLE public."users"
    OWNER to makey;

--changeset makey:createMessages
CREATE TABLE IF NOT EXISTS "messages"
(
    msg_id bigserial NOT NULL,
    msg_text text COLLATE pg_catalog."default",
    msg_date date,
    user_id bigint,
    chat_id bigint,
    CONSTRAINT "Messages_pkey" PRIMARY KEY (msg_id),
    CONSTRAINT messages_users_fk FOREIGN KEY (user_id)
    REFERENCES public."users" (user_id) MATCH SIMPLE
    )

    TABLESPACE pg_default;

ALTER TABLE "messages"
    OWNER to makey;

--changeset makey:createDomain
CREATE TABLE IF NOT EXISTS public.domain
(
    id bigserial NOT NULL,
    domainname character varying,
    hotness integer,
    price integer,
    x_value integer,
    yandex_tic integer,
    links integer,
    visitors integer,
    registrar character varying,
    yearsold integer,
    delete_date timestamp,
    rkn boolean,
    judicial boolean,
    block boolean,
    CONSTRAINT domain_pkey PRIMARY KEY (id)
    )

    TABLESPACE pg_default;

ALTER TABLE public.domain
    OWNER to makey;


