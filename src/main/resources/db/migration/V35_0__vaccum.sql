UPDATE T_APPLICATION SET active=false
    WHERE name IN ('com.wutsi.wutsi-payment-job', 'com.wutsi.wutsi-security');

DELETE FROM T_APPLICATION_SCOPE WHERE application_fk IN
    (SELECT id FROM T_APPLICATION WHERE active=false);

DELETE FROM T_LOGIN WHERE application_fk IN
    (SELECT id FROM T_APPLICATION WHERE active=false);

DELETE FROM T_APPLICATION where active=false;
