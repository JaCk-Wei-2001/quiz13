CREATE TABLE if not exists `quiz` (
  `quiz_id` int NOT NULL AUTO_INCREMENT,
  `quiz_name` varchar(60) NOT NULL,
  `description` varchar(200) NOT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `is_published` tinyint DEFAULT '1',
  PRIMARY KEY (`quiz_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
