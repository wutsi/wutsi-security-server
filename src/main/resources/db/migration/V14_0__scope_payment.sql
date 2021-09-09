INSERT INTO T_SCOPE(name, active, security_level, description)
    VALUES
         ('payment-charge', true, 100, 'This scope allows the subject to charge a customer'),
         ('payment-payout', true, 100, 'This scope allows the subject to receive his funds'),
         ('payment-transfer', true, 100, 'This scope allows the subject to transfer money to another user')
;
