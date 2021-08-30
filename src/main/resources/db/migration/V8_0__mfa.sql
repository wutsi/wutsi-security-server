CREATE TABLE T_MFA_LOGIN(
    id              SERIAL NOT NULL,
    account_id      BIGINT NOT NULL,
    type            INT NOT NULL DEFAULT 0,
    token           VARCHAR(36) NOT NULL,
    verification_id BIGINT NOT NULL,
    scopes          TEXT,
    created         TIMESTAMPTZ NOT NULL DEFAULT now(),

    UNIQUE (token),
    PRIMARY KEY (id)
);

ALTER TABLE T_LOGIN ADD COLUMN account_id BIGINT NULL;
