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
  `energy` float(10,2) DEFAULT NULL,
  `protein` float(10,2) DEFAULT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `recipe_tbl` (
  `recipe_id` varchar(45) NOT NULL,
  `recipe_name` varchar(45) DEFAULT NULL,
  `cook_method` varchar(45) DEFAULT NULL,
  `taste` varchar(45) DEFAULT NULL,
  `cuisine` varchar(45) DEFAULT NULL,
  `age_group` varchar(45) DEFAULT NULL,
  `difficulty` varchar(45) DEFAULT NULL,
  `prepare_time` int DEFAULT NULL,
  `cooking_time` int DEFAULT NULL,
  `meal_time` varchar(45) DEFAULT NULL,
  `category` varchar(45) DEFAULT NULL,
  `material` varchar(45) DEFAULT NULL,
  `main_ingredients` varchar(256) DEFAULT NULL,
  `supplementary` varchar(256) DEFAULT NULL,
  `cookingnote` TEXT,
  `energy` float(10,2) DEFAULT NULL,
  `protein` float(10,2) DEFAULT NULL,
  `protein_weight` int DEFAULT NULL,
  `fat_weight` int DEFAULT NULL,
  `cho_weight` int DEFAULT NULL,
  `na_weight` int DEFAULT NULL,
  `cholesterol_weight` int DEFAULT NULL,
  `purine_weight` int DEFAULT NULL,
  `k_weight` int DEFAULT NULL,
  `ckd_category` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`recipe_id`),
  FULLTEXT KEY `NAME_INDEX` (`recipe_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4