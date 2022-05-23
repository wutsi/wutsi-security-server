UPDATE T_APPLICATION
    SET name='com.wutsi.wutsi-login'
    WHERE name = 'com.wutsi.wutsi-login-bff';

INSERT INTO T_APPLICATION(name, api_key, active, title, security_level, description)
    VALUES
        ('com.wutsi.wutsi-store-bff', uuid_generate_v1(), true, 'Wutsi Store App', 0, 'Wutsi app that manages the store')
;
