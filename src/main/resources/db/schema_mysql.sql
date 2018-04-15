CREATE TABLE `region_data` (
  `code` int(11) NOT NULL,
  `parent_code` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `level` tinyint(4) NOT NULL,
  `center` varchar(30) DEFAULT NULL,
  `polyline` text NOT NULL,
  PRIMARY KEY (`code`)
);