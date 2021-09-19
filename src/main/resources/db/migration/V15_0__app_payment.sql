CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DELETE FROM T_SCOPE where name = 'payment-transfer';

INSERT INTO T_APPLICATION(name, api_key, active, title, security_level)
    VALUES
        ('com.wutsi.wutsi-payment', uuid_generate_v1(), true, 'Wutsi Payment', 0)
;

INSERT INTO  T_APPLICATION_SCOPE(application_fk, scope_fk)
    SELECT currval('T_APPLICATION_id_seq'), id FROM T_SCOPE WHERE name IN (
        'user-read',
        'payment-method-read',
        'payment-method-details',
        'payment-charge',
        'payment-payout'
    )
