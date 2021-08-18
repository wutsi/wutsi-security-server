CREATE TABLE T_LOGIN(
    id            SERIAL NOT NULL,
    application_fk  BIGINT REFERENCES T_APPLICATION(id),
    access_token  TEXT NOT NULL,
    active        BOOLEAN NOT NULL DEFAULT true,
    created       TIMESTAMPTZ NOT NULL DEFAULT now(),
    expires       TIMESTAMPTZ NOT NULL DEFAULT now(),

    PRIMARY KEY (id)
);
