INSERT INTO T_SCOPE(name, description, security_level, active)
    VALUES
        ('user-read', 'Read user information', 99, false),
        ('user-read-basic', 'Read user basic information', 99, true),
        ('user-read-email', 'Read user email', 0, true),
        ('payment-read', 'Read payment information', 99, true)
    ;

INSERT INTO T_APPLICATION(name, api_key, title, active, config_url, home_url, security_level, description)
    VALUES
        ('com.wutsi.application.test', gen_random_uuid(), 'Test', true, 'https://test.herokuapp.com/config', 'https://test.herokuapp.com', 3, 'description')
;
