INSERT INTO T_APPLICATION(name, api_key, active, title, security_level, description)
    VALUES
        ('com.wutsi.wutsi-qr-service', uuid_generate_v1(), true, 'Wutsi QR Code Server', 0, 'Service for generating QR codes')
;
