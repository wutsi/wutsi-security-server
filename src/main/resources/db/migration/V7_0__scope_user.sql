INSERT INTO T_SCOPE(name, active, security_level, description)
    VALUES
         ('user-read', true, 0, 'This scope allows the subject to read information of accounts')
        ,('user-manage', true, 0, 'This scope allows the subject create, update or delete account')
;
