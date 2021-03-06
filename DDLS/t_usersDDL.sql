DROP TABLE t_users;
DROP SEQUENCE users_seq;
CREATE TABLE t_users(id INTEGER PRIMARY KEY,
					 username  varchar(100),
					 pass	   varchar(100),
					 firstname varchar(100),
					 lastname  varchar(100),
					 email	   varchar(100),
					 perm	   INTEGER,
					 profilepic varchar(500),
					 newmsg	   varchar(10000));
CREATE SEQUENCE users_seq;
COMMIT;
