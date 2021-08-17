CREATE TABLE T_SCOPE(
    id            SERIAL NOT NULL,
    name          VARCHAR(100) NOT NULL,
    description   TEXT NOT NULL,
    security_level INT NOT NULL DEFAULT 0,
    active        BOOLEAN NOT NULL DEFAULT true,
    created       TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated       TIMESTAMPTZ NOT NULL DEFAULT now(),

    UNIQUE(name),
    PRIMARY KEY (id)
);

CREATE OR REPLACE FUNCTION scope_updated()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_scope_updated
BEFORE UPDATE ON T_SCOPE
FOR EACH ROW
EXECUTE PROCEDURE scope_updated();
