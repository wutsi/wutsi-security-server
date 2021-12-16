INSERT INTO  T_APPLICATION_SCOPE(application_fk, scope_fk)
SELECT A.id, S.id FROM T_APPLICATION A, T_SCOPE S
WHERE A.name='com.wutsi.wutsi-onboard' AND S.name IN (
    'payment-method-manage'
);
