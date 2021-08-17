INSERT INTO T_SCOPE(name, description, security_level, active)
    VALUES
        ('user-read', 'Read user information', 99, false),
        ('user-read-basic', 'Read user basic information', 99, true),
        ('user-read-email', 'Read user email', 0, true),
        ('PAYMENT-read', 'Read payment information', 99, true)
    ;
