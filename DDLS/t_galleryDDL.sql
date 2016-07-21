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

