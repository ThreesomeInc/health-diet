USE test;

CREATE TABLE `food_tbl` (
  `food_id` varchar(45) NOT NULL,
  `food_code` varchar(45) DEFAULT NULL,
  `sub_code` varchar(45) DEFAULT NULL,
  `sub_name` varchar(45) DEFAULT NULL,
  `food_name` varchar(256) DEFAULT NULL,
  `water` varchar(45) DEFAULT NULL,
  `energy` varchar(45) DEFAULT NULL,
  `protein` varchar(45) DEFAULT NULL,
  `fat` varchar(45) DEFAULT NULL,
  `cho` varchar(45) DEFAULT NULL,
  `p` varchar(45) DEFAULT NULL,
  `k` varchar(45) DEFAULT NULL,
  `na` varchar(45) DEFAULT NULL,
  `unit` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`food_id`),
  FULLTEXT KEY `NAME_INDEX` (`food_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4


CREATE TABLE `food_weight_tbl` (
  `food_id` varchar(45) NOT NULL,
  `food_code` varchar(45) DEFAULT NULL,
  `sub_code` varchar(45) DEFAULT NULL,
  `protein_weight` varchar(45) DEFAULT NULL,
  `fat_weight` varchar(45) DEFAULT NULL,
  `cho_weight` varchar(45) DEFAULT NULL,
  `k_weight` varchar(45) DEFAULT NULL,
  `na_weight` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`food_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4