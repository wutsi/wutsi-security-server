CREATE TABLE T_APPLICATION(
    id            SERIAL NOT NULL,
    api_key       VARCHAR(36) NOT NULL,
    name          VARCHAR(255) NOT NULL,
    title         VARCHAR(30) NOT NULL,
    security_level INTEGER NOT NULL DEFAULT 0,
    active        BOOLEAN NOT NULL DEFAULT true,
    created       TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated       TIMESTAMPTZ NOT NULL DEFAULT now(),
    icon_url      TEXT,
    config_url    TEXT,
    home_url      TEXT,
    description   TEXT,

    UNIQUE(name),
    UNIQUE(api_key),
    PRIMARY KEY (id)
);

CREATE TABLE T_APPLICATION_SCOPE(
    application_fk  BIGINT NOT NULL REFERENCES T_APPLICATION(id),
    scope_fk  BIGINT NOT NULL REFERENCES T_SCOPE(id),

    PRIMARY KEY (application_fk, scope_fk)
);

CREATE OR REPLACE FUNCTION application_updated()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_application_updated
BEFORE UPDATE ON T_APPLICATION
FOR EACH ROW
EXECUTE PROCEDURE application_updated();
