INSERT INTO T_SCOPE(name, active, security_level, description)
    VALUES
         ('user-phone', true, 0, 'This scope allows the subject to read the phone of a user')
;

UPDATE T_SCOPE set security_level=100 WHERE name in ('user-rewad', 'user-manage');
