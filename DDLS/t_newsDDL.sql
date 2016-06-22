CREATE TABLE t_news(id INTEGER PRIMARY KEY,
				    author_name varchar(100),
				    describtion varchar(1000),
				   	date		varchar(100),
				   	time		varchar(100));
CREATE SEQUENCE news_seq;
COMMIT;
