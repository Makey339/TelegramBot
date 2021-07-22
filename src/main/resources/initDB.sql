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


    CREATE SEQUENCE IF NOT EXISTS HIBERNATE_SEQUENCE START WITH 1 INCREMENT BY 1;

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

-- ALTER TABLE "messages"
--     ADD CONSTRAINT messages_users_fk FOREIGN KEY (user_id)
--         REFERENCES public."users" (user_id) MATCH SIMPLE
--         ON UPDATE NO ACTION
--         ON DELETE NO ACTION
--     NOT VALID;


