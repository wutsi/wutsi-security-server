INSERT INTO T_APPLICATION(name, api_key, active, title, security_level, description)
    VALUES
        ('com.wutsi.wutsi-sms-service', uuid_generate_v1(), true, 'Wutsi SMS Server', 0, 'Sends SMS')
;

INSERT INTO  T_APPLICATION_SCOPE(application_fk, scope_fk)
    SELECT A.id, S.id FROM T_APPLICATION A, T_SCOPE S
    WHERE A.name='com.wutsi.wutsi-sms-service' AND S.name IN (
        'sms-send',
        'sms-verification'
    );

DELETE FROM T_SCOPE where name='sms-delivery';
