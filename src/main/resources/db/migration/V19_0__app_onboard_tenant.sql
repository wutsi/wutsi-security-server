INSERT INTO T_SCOPE(name, active, security_level, description)
    VALUES
         ('tenant-read', true, 100, 'This scope allows the subject to read tenant information')
;

INSERT INTO  T_APPLICATION_SCOPE(application_fk, scope_fk)
    SELECT A.id, S.id FROM T_APPLICATION A, T_SCOPE S
    WHERE A.name='com.wutsi.wutsi-onboard' AND S.name IN (
        'tenant-read'
    );
