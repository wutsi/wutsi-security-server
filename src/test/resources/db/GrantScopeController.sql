INSERT INTO T_SCOPE(id, name, description, security_level, active)
    VALUES
        (1, 'user-read', 'Read user information', 99, false),
        (2, 'user-read-basic', 'Read user basic information', 99, true),
        (3, 'user-read-email', 'Read user email', 0, true),
        (4, 'payment-read', 'Read payment information', 99, true)
    ;

INSERT INTO T_APPLICATION(id, name, api_key, title, active, config_url, home_url, security_level, description)
    VALUES
        (1, 'com.wutsi.application.test', '0000-1111', 'Test', true, 'https://test.herokuapp.com/config', 'https://test.herokuapp.com', 3, 'This is the description')
;

INSERT INTO T_APPLICATION_SCOPE(application_fk, scope_fk)
    VALUES
        (1, 1)
;
