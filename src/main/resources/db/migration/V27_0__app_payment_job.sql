INSERT INTO T_SCOPE(name, active, security_level, description)
    VALUES
         ('payment-job-manage', true, 0, 'This scope allows the subject to run payment jobs')
;


INSERT INTO T_APPLICATION(name, api_key, active, title, security_level, description)
    VALUES
        ('com.wutsi.wutsi-payment-job', uuid_generate_v1(), true, 'Wutsi Payment Job', 0, 'Wutsi Application that run payment related jobs')
;

INSERT INTO  T_APPLICATION_SCOPE(application_fk, scope_fk)
    SELECT currval('T_APPLICATION_id_seq'), id FROM T_SCOPE
    WHERE name IN (
        'payment-job-manage'
    );
