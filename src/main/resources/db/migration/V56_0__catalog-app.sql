INSERT INTO T_APPLICATION(name, api_key, active, title, security_level, description)
    VALUES
        ('com.wutsi.wutsi-catalog-server', uuid_generate_v1(), true, 'Wutsi Catalog Server', 0, 'Wutsi Catalog Manager')
;

INSERT INTO  T_APPLICATION_SCOPE(application_fk, scope_fk)
    SELECT A.id, S.id FROM T_APPLICATION A, T_SCOPE S
    WHERE A.name='com.wutsi.wutsi-catalog-server' AND S.name IN (
        'user-read'
    );
