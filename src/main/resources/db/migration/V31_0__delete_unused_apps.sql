DELETE FROM T_APPLICATION_SCOPE WHERE application_fk IN
    (SELECT id FROM T_APPLICATION WHERE name IN ('com.wutsi.wutsi-onboard', 'wutsi-account', 'test'));

DELETE FROM T_LOGIN WHERE application_fk IN
    (SELECT id FROM T_APPLICATION WHERE name IN ('com.wutsi.wutsi-onboard', 'wutsi-account', 'test'));

DELETE FROM T_APPLICATION where name IN ('com.wutsi.wutsi-onboard', 'wutsi-account', 'test')
