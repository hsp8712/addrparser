CREATE TABLE earth4j_region (
  code INT PRIMARY KEY,
  parent_code INT NOT NULL,
  name VARCHAR(100) NOT NULL,
  level TINYINT NOT NULL,
  center VARCHAR(30),
  polyline TEXT NOT NULL
)