INSERT INTO T_SCOPE(name, active, security_level, description)
    VALUES
         ('contact-read', true, 0, 'This scope allows the subject to read the contacts of a user'),
         ('contact-manage', true, 0, 'This scope allows the subject to create/editi/delete the contacts of a user')
;

UPDATE T_SCOPE
    SET description = 'This scope allows the subject to create/editi/delete the payment methods of a user'
    WHERE name='payment-method-manage';
