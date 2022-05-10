INSERT INTO T_SCOPE(name, active, security_level, description)
    VALUES
         ('shipping-read', true, 100, 'This scope allows the subject to read shipping infos'),
         ('shipping-manage', true, 100, 'This scope allows the subject to manage shipping infos')
;

INSERT INTO  T_APPLICATION_SCOPE(application_fk, scope_fk)
    SELECT A.id, S.id FROM T_APPLICATION A, T_SCOPE S
    WHERE A.name='com.wutsi.wutsi-shipping' AND S.name IN (
       'shipping-read',
       'shipping-manage',
    );
