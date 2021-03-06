INSERT INTO T_SCOPE(name, active, security_level, description)
    VALUES
         ('payment-manage', true, 100, 'This scope allows the subject make transactions (charges/payout etc.) on his account'),
         ('payment-read', true, 100, 'This scope allows the subject retrieve transaction information from his account')
;

INSERT INTO  T_APPLICATION_SCOPE(application_fk, scope_fk)
    SELECT A.id, S.id FROM T_APPLICATION A, T_SCOPE S
    WHERE A.name='com.wutsi.wutsi-payment' AND S.name IN (
        'payment-manage',
        'payment-read'
    );

DELETE FROM T_APPLICATION_SCOPE WHERE scope_fk IN (
    SELECT id FROM T_SCOPE WHERE name IN ('payment-payout', 'payment-transfer', 'payment-charge')
);

DELETE FROM T_SCOPE WHERE name IN ('payment-payout', 'payment-transfer', 'payment-charge');
