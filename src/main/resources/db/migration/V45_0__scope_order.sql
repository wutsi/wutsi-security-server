INSERT INTO T_SCOPE(name, active, security_level, description)
    VALUES
         ('order-read', true, 100, 'This scope allows the subject read Order information'),
         ('order-manage', true, 100, 'This scope allows the subject to create,update Order')
;
