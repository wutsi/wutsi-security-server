INSERT INTO T_SCOPE(name, active, security_level, description)
    VALUES
         ('user-email', true, 100, 'This scope allows the subject to read the user'' email')
;

INSERT INTO  T_APPLICATION_SCOPE(application_fk, scope_fk)
    SELECT A.id, S.id FROM T_APPLICATION A, T_SCOPE S
    WHERE A.name='com.wutsi.wutsi-payment-server' AND S.name IN (
        'user-email'
    );
