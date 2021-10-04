INSERT INTO T_APPLICATION(name, api_key, active, title, security_level, description)
    VALUES
        ('com.wutsi.wutsi-onboard', uuid_generate_v1(), true, 'Wutsi Onboard App', 0, 'Wutsi Application for Mobile Onboarding')
;


INSERT INTO  T_APPLICATION_SCOPE(application_fk, scope_fk)
    SELECT currval('T_APPLICATION_id_seq'), id FROM T_SCOPE
    WHERE name IN (
        'sms-send',
        'sms-verify',
        'user-read',
        'user-phone',
        'user-manage'
    );
