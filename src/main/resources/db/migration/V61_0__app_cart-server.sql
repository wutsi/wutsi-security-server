INSERT INTO T_APPLICATION(name, api_key, active, title, security_level, description)
    VALUES
        ('com.wutsi.wutsi-cart-service', uuid_generate_v1(), true, 'Wutsi Cart Server', 0, 'Service for managing the shopping cart')
;

INSERT INTO  T_APPLICATION_SCOPE(application_fk, scope_fk)
    SELECT currval('T_APPLICATION_id_seq'), id FROM T_SCOPE
    WHERE name IN (
        'order-read'
    );

