DELETE FROM T_APPLICATION_SCOPE WHERE application_fk IN
    (SELECT id FROM T_APPLICATION WHERE name IN ('com.wutsi.wutsi-paymant-job'));

DELETE FROM T_APPLICATION_SCOPE WHERE scope_fk IN
    (SELECT id FROM T_SCOPE WHERE name IN ('payment-job-manage'));

DELETE FROM T_LOGIN WHERE application_fk IN
    (SELECT id FROM T_APPLICATION WHERE name IN ('com.wutsi.wutsi-paymant-job'));

DELETE FROM T_APPLICATION where name IN ('com.wutsi.wutsi-paymant-job');

DELETE FROM T_SCOPE WHERE name = 'payment-job-manage';
