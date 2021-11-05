INSERT INTO T_APPLICATION(name, api_key, active, title, security_level, description)
    VALUES
        ('com.wutsi.wutsi-login', uuid_generate_v1(), true, 'Wutsi Login App', 0, 'Wutsi Login application')
;


INSERT INTO  T_APPLICATION_SCOPE(application_fk, scope_fk)
    SELECT currval('T_APPLICATION_id_seq'), id FROM T_SCOPE
    WHERE name IN (
        'user-read',
        'tenant-read'
    );
