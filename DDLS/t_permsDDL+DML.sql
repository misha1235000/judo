 CREATE TABLE t_perms(id INTEGER PRIMARY KEY,
				    name varchar(100));
INSERT INTO t_perms values(1, 'User');
INSERT INTO t_perms values(2, 'Trainer');
INSERT INTO t_perms values(3, 'Operator');
COMMIT;

