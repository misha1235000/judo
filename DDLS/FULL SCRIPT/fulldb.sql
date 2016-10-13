DROP TABLE t_chat;
DROP SEQUENCE chat_seq;
CREATE TABLE t_chat(	 id 	    INTEGER PRIMARY KEY,
			 msgfrom    INTEGER,
			 msgto	    INTEGER,
			 date	    varchar(1000),
			 time	    varchar(1000),
			 msg	    varchar(10000),
			 profilepic varchar(1000));
CREATE SEQUENCE chat_seq;
COMMIT;

DROP SEQUENCE comments_seq;
DROP TABLE t_comments;
CREATE TABLE t_comments (id INTEGER PRIMARY KEY, poster_id INTEGER, 
pic_id varchar(1000), postername varchar(100), posterlastname varchar(100), date varchar(100), time varchar(100), comment text, src varchar(1000));
CREATE SEQUENCE comments_seq;
COMMIT;

DROP SEQUENCE gallery_seq;
DROP TABLE t_gallery;
CREATE TABLE t_gallery(id varchar(1000) PRIMARY KEY,
				    src varchar(100),
				    info varchar(1000),
				    title varchar(100),
				    date varchar(100),
				    time varchar(100),
				    poster_id INTEGER,
				    poster_name varchar(100),
				    poster_lastname varchar(100),
				    isvideo INTEGER,
				    posterimg varchar(1000));
CREATE SEQUENCE gallery_seq;
COMMIT;

DROP TABLE t_msgs;
CREATE TABLE t_msgs(msgfrom INTEGER, msgto INTEGER, amount INTEGER);
COMMIT;

DROP TABLE t_news;
DROP SEQUENCE news_seq;
CREATE TABLE t_news(id INTEGER PRIMARY KEY,
				    author_name varchar(100),
				    describtion varchar(1000),
				   	date varchar(100),
				   	time varchar(100));
CREATE SEQUENCE news_seq;
COMMIT;

CREATE TABLE t_perms(id INTEGER PRIMARY KEY,
				    name varchar(100));
INSERT INTO t_perms values(1, 'User');
INSERT INTO t_perms values(2, 'Trainer');
INSERT INTO t_perms values(3, 'Operator');
COMMIT;

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
