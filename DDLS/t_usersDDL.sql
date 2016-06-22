CREATE TABLE t_users(id INTEGER PRIMARY KEY,
					 username  varchar(100),
					 pass	   varchar(100),
					 firstname varchar(100),
					 lastname  varchar(100),
					 email	   varchar(100),
					 perm	   INTEGER);
CREATE SEQUENCE users_seq;
COMMIT;
