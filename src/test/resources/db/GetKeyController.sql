INSERT INTO T_KEY(id, algorithm, public_key, private_key, active, created, expired)
    VALUES
        (100, 'RSA', 'public-1', 'private-1', true, '2011-01-01', null),
        (200, 'RSA', 'public-2', 'private-2', false, '2011-01-01', '2011-11-01'),
        (300, 'RSA', 'public-3', 'private-3', true, '2011-02-01', null)
;
