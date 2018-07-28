--use /h2 console to import
DROP TABLE  IF EXISTS hello;
CREATE TABLE hello(ID INT PRIMARY KEY, NAME VARCHAR(255), PASSWORD VARCHAR(255));
INSERT INTO hello VALUES(1, 'Hello', 'xxxx');
INSERT INTO hello VALUES(2, 'World', 'xxxx');