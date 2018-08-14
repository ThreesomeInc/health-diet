USE health_diet;

CREATE TABLE `user_tbl` (
  `open_id` varchar(255) NOT NULL,
  `gender` varchar(45) DEFAULT NULL,
  `birthday` varchar(45) DEFAULT NULL,
  `height` varchar(45) DEFAULT NULL,
  `weight` varchar(45) DEFAULT NULL,
  `sport_rate` varchar(45) DEFAULT NULL,
  `nephrotic_period` varchar(45) DEFAULT NULL,
  `treatment_method` varchar(255) DEFAULT NULL,
  `other_diseases` varchar(255) DEFAULT NULL,
  `irritability` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`open_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `food_weight_tbl` (
  `food_id` varchar(45) NOT NULL,
  `food_code` varchar(45) DEFAULT NULL,
  `sub_code` varchar(45) DEFAULT NULL,
  `protein_weight` int DEFAULT NULL,
  `fat_weight` int DEFAULT NULL,
  `cho_weight` int DEFAULT NULL,
--   `k_weight` int DEFAULT NULL,
  `na_weight` int DEFAULT NULL,
  `cholesterol_weight` int DEFAULT NULL,
  `purine_weight` int DEFAULT NULL,
--   `p_weight` int DEFAULT NULL,
  PRIMARY KEY (`food_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4