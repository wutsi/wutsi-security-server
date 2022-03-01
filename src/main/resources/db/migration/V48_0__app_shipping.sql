INSERT INTO T_APPLICATION(name, api_key, active, title, security_level)
VALUES
    ('com.wutsi.wutsi-shipping', uuid_generate_v1(), true, 'Wutsi Shipping API', 0)
;

INSERT INTO  T_APPLICATION_SCOPE(application_fk, scope_fk)
SELECT currval('T_APPLICATION_id_seq'), id FROM
    T_SCOPE WHERE name IN (
       'shipping-read',
       'shipping-manage',
       'order-read',
       'mail-send',
       'user-read'
    );
