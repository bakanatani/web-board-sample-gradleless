CREATE TABLE message_board(
  id INT(3) NOT NULL PRIMARY KEY,
  name VARCHAR(50),
  message VARCHAR(100),
  created_at TIMESTAMP
);