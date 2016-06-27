CREATE TABLE t_comments (id INTEGER PRIMARY KEY, poster_id INTEGER, 
pic_id INTEGER, postername varchar(100), posterlastname varchar(100), date varchar(100), time varchar(100), comment text);
CREATE SEQUENCE comments_seq;
COMMIT;

