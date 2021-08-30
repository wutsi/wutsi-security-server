INSERT INTO T_SCOPE(id, name, description, security_level, active)
    VALUES
        (1, 'user-read', 'Read user information', 99, false),
        (2, 'user-read-basic', 'Read user basic information', 99, true),
        (3, 'user-read-email', 'Read user email', 0, true),
        (4, 'payment-read', 'Read payment information', 99, true)
    ;

INSERT INTO T_APPLICATION(id, name, api_key, title, active, config_url, home_url, security_level, description)
    VALUES
        (1, 'com.wutsi.application.test', '0000-1111', 'Test', true, 'https://test.herokuapp.com/config', 'https://test.herokuapp.com', 3, 'This is the description'),
        (2, 'com.wutsi.application.inactive', 'inactive-key', 'App2', false, null, null, 1, null),
        (100, 'com.wutsi.wutsi-security', uuid_generate_v1(), 'Wutsi Security', true, null, null, 0, null)
;

INSERT INTO T_APPLICATION_SCOPE(application_fk, scope_fk)
    VALUES
        (1, 1),
        (1, 2),
        (1, 3)
;
INSERT INTO T_KEY(id, algorithm, public_key, private_key, active, created)
    VALUES
        (1,
        'RSA',
        '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',
         true,
         '2011-01-01')
;

INSERT INTO T_MFA_LOGIN(id, account_id, type, token, verification_id, scopes, admin, display_name)
    VALUES
        (100, 33, 1, '0000000', 333, 'user-read,payment-read', true, 'Ray Sponsible');
