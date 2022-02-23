INSERT INTO T_APPLICATION(name, api_key, active, title, security_level)
VALUES
    ('com.wutsi.wutsi-order', uuid_generate_v1(), true, 'Wutsi Order API', 0)
;

INSERT INTO  T_APPLICATION_SCOPE(application_fk, scope_fk)
SELECT currval('T_APPLICATION_id_seq'), id FROM
    T_SCOPE WHERE name IN (
       'payment-read',
       'catalog-read',
       'tenant-read'
    );
