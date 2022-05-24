UPDATE T_APPLICATION
    SET name='com.wutsi.wutsi-login-bff'
    WHERE name = 'com.wutsi.wutsi-login';

INSERT INTO T_APPLICATION(name, api_key, active, title, security_level, description)
    VALUES
        ('com.wutsi.wutsi-shell-bff', uuid_generate_v1(), true, 'Wutsi Shell App', 0, 'Wutsi main app')
;
