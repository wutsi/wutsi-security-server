INSERT INTO T_SCOPE(name, active, security_level, description)
    VALUES
         ('payment-method-read', true, 100, 'This scope allows the subject to read the payment methods of a user'),
         ('payment-method-manage', true, 100, 'This scope allows the subject to create/editir/delete the payment methods of a user'),
         ('payment-method-details', true, 0, 'This scope allows the subject to access the details of a payment method')
;
