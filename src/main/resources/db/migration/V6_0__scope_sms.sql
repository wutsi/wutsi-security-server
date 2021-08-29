INSERT INTO T_SCOPE(name, active, security_level, description)
    VALUES
         ('sms-send', true, 0, 'This scope allows the subject to send SMS')
        ,('sms-verify', true, 0, 'This scope allows the subject to verify phone number. The subject can send the verification code via SMS and verify the verification code')
;
