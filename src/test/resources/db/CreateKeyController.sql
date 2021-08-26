INSERT INTO T_KEY(id, algorithm, public_key, private_key, active, created, expired)
    VALUES
        (100, 'RSA', 'public', 'private', true, '2011-01-01', null),
        (200, 'RSA', 'public', 'private', false, '2011-01-01', '2011-11-01')
;
