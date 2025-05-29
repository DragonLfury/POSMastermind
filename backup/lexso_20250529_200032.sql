-- MySQL dump 10.13  Distrib 8.0.39, for Win64 (x86_64)
--
-- Host: localhost    Database: u821149722_lexso
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `attendance`
--

DROP TABLE IF EXISTS `attendance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `attendance` (
  `attendance_id` int NOT NULL AUTO_INCREMENT,
  `user_email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `check_in` datetime DEFAULT NULL,
  `check_out` datetime DEFAULT NULL,
  `date` date NOT NULL,
  `status` enum('Present','Absent','Sick','Vacation') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `shift_id` int DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`attendance_id`),
  KEY `fk_attendance_user1_idx` (`user_email`),
  KEY `fk_attendance_shift1_idx` (`shift_id`),
  CONSTRAINT `fk_attendance_shift1` FOREIGN KEY (`shift_id`) REFERENCES `shift` (`shift_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_attendance_user1` FOREIGN KEY (`user_email`) REFERENCES `user` (`email`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attendance`
--

LOCK TABLES `attendance` WRITE;
/*!40000 ALTER TABLE `attendance` DISABLE KEYS */;
/*!40000 ALTER TABLE `attendance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `backup_logs`
--

DROP TABLE IF EXISTS `backup_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `backup_logs` (
  `backup_id` int NOT NULL AUTO_INCREMENT,
  `backup_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `backup_file` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`backup_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `backup_logs`
--

LOCK TABLES `backup_logs` WRITE;
/*!40000 ALTER TABLE `backup_logs` DISABLE KEYS */;
/*!40000 ALTER TABLE `backup_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bank`
--

DROP TABLE IF EXISTS `bank`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bank` (
  `id` int NOT NULL AUTO_INCREMENT,
  `bank_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `country_id` int NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_bank_country1_idx` (`country_id`),
  CONSTRAINT `fk_bank_country1` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bank`
--

LOCK TABLES `bank` WRITE;
/*!40000 ALTER TABLE `bank` DISABLE KEYS */;
INSERT INTO `bank` VALUES (1,'Bank of Ceylon',1,'2025-05-23 18:13:17',NULL),(2,'People\'s Bank',1,'2025-05-23 18:13:17',NULL),(3,'Commercial Bank',1,'2025-05-23 18:13:17',NULL),(4,'Sampath Bank',1,'2025-05-23 18:13:17',NULL),(5,'HSBC',4,'2025-05-23 18:13:17',NULL);
/*!40000 ALTER TABLE `bank` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bank_details`
--

DROP TABLE IF EXISTS `bank_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bank_details` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `acnumber` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `branch` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bank_id` int NOT NULL,
  `user_email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_bank_details_bank1_idx` (`bank_id`),
  KEY `fk_bank_details_user1_idx` (`user_email`),
  CONSTRAINT `fk_bank_details_bank1` FOREIGN KEY (`bank_id`) REFERENCES `bank` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_bank_details_user1` FOREIGN KEY (`user_email`) REFERENCES `user` (`email`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bank_details`
--

LOCK TABLES `bank_details` WRITE;
/*!40000 ALTER TABLE `bank_details` DISABLE KEYS */;
INSERT INTO `bank_details` VALUES (1,'Admin Account','1234567890','Main Branch','Primary Account',1,'admin@lexso.com','2025-05-23 18:13:18',NULL),(2,'Manager Account','2345678901','City Branch','Salary Account',2,'manager@lexso.com','2025-05-23 18:13:18',NULL),(3,'Cashier Account','3456789012','North Branch','Savings Account',3,'cashier@lexso.com','2025-05-23 18:13:18',NULL),(5,'Accountant','5678901234','West Branch','Joint Account',5,'accountant@lexso.com','2025-05-23 18:13:18',NULL);
/*!40000 ALTER TABLE `bank_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `brand`
--

DROP TABLE IF EXISTS `brand`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `brand` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `brand`
--

LOCK TABLES `brand` WRITE;
/*!40000 ALTER TABLE `brand` DISABLE KEYS */;
INSERT INTO `brand` VALUES (1,'Samsung','2025-05-23 18:13:18',NULL),(2,'Apple','2025-05-23 18:13:18',NULL),(3,'Sony','2025-05-23 18:13:18',NULL),(4,'LG','2025-05-23 18:13:18',NULL),(5,'Panasonic','2025-05-23 18:13:18',NULL);
/*!40000 ALTER TABLE `brand` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `category_name` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'Electronics','2025-05-23 18:13:18',NULL),(2,'Clothing','2025-05-23 18:13:18',NULL),(3,'Groceries','2025-05-23 18:13:18',NULL),(4,'Home Appliances','2025-05-23 18:13:18',NULL),(5,'Beauty Products','2025-05-23 18:13:18',NULL);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category_has_brand`
--

DROP TABLE IF EXISTS `category_has_brand`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category_has_brand` (
  `category_has_brand_id` int NOT NULL AUTO_INCREMENT,
  `category_category_id` int NOT NULL,
  `brand_id` int NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`category_has_brand_id`),
  KEY `fk_categories_has_brand_brand1_idx` (`brand_id`),
  KEY `fk_categories_has_brand_categories1_idx` (`category_category_id`),
  CONSTRAINT `fk_categories_has_brand_brand1` FOREIGN KEY (`brand_id`) REFERENCES `brand` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_categories_has_brand_categories1` FOREIGN KEY (`category_category_id`) REFERENCES `category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category_has_brand`
--

LOCK TABLES `category_has_brand` WRITE;
/*!40000 ALTER TABLE `category_has_brand` DISABLE KEYS */;
INSERT INTO `category_has_brand` VALUES (1,1,1,'2025-05-23 18:13:18',NULL),(2,1,2,'2025-05-23 18:13:18',NULL),(3,1,3,'2025-05-23 18:13:18',NULL),(4,4,4,'2025-05-23 18:13:18',NULL),(5,4,5,'2025-05-23 18:13:18',NULL);
/*!40000 ALTER TABLE `category_has_brand` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `city`
--

DROP TABLE IF EXISTS `city`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `city` (
  `id` int NOT NULL AUTO_INCREMENT,
  `city_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `district_id` int NOT NULL,
  `status_id` int NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_city_district1_idx` (`district_id`),
  KEY `fk_city_status1_idx` (`status_id`),
  CONSTRAINT `fk_city_district1` FOREIGN KEY (`district_id`) REFERENCES `district` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_city_status1` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `city`
--

LOCK TABLES `city` WRITE;
/*!40000 ALTER TABLE `city` DISABLE KEYS */;
INSERT INTO `city` VALUES (1,'Colombo',1,1,'2025-05-23 18:13:16',NULL),(2,'Dehiwala',1,1,'2025-05-23 18:13:16',NULL),(3,'Moratuwa',1,1,'2025-05-23 18:13:16',NULL),(4,'Negombo',2,1,'2025-05-23 18:13:16',NULL),(5,'Kandy City',4,1,'2025-05-23 18:13:16',NULL);
/*!40000 ALTER TABLE `city` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company`
--

DROP TABLE IF EXISTS `company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `hotline` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company`
--

LOCK TABLES `company` WRITE;
/*!40000 ALTER TABLE `company` DISABLE KEYS */;
INSERT INTO `company` VALUES (1,'ABC Distributors','0112345678','2025-05-23 18:13:17',NULL),(2,'XYZ Enterprises','0112345679','2025-05-23 18:13:17',NULL),(3,'Global Traders','0112345680','2025-05-23 18:13:17',NULL),(4,'Island Suppliers','0112345681','2025-05-23 18:13:17',NULL),(5,'Premier Products','0112345682','2025-05-23 18:13:17',NULL);
/*!40000 ALTER TABLE `company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `country`
--

DROP TABLE IF EXISTS `country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `country` (
  `id` int NOT NULL AUTO_INCREMENT,
  `country_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `status_id` int NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_country_status1_idx` (`status_id`),
  CONSTRAINT `fk_country_status1` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `country`
--

LOCK TABLES `country` WRITE;
/*!40000 ALTER TABLE `country` DISABLE KEYS */;
INSERT INTO `country` VALUES (1,'Sri Lanka',1,'2025-05-23 18:13:16',NULL),(2,'India',1,'2025-05-23 18:13:16',NULL),(3,'United States',1,'2025-05-23 18:13:16',NULL),(4,'United Kingdom',1,'2025-05-23 18:13:16',NULL),(5,'Australia',1,'2025-05-23 18:13:16',NULL);
/*!40000 ALTER TABLE `country` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `mobile` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `first_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `point` int NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES ('0771234567','John','Doe','john.doe@example.com',195,'2025-05-23 18:13:18',NULL),('0772345678','Jane','Smith','jane.smith@example.com',1490,'2025-05-23 18:13:18',NULL),('0773456789','Robert','Johnson','robert.j@example.com',200,'2025-05-23 18:13:18',NULL),('0774567890','Sarah','Williams','sarah.w@example.com',50,'2025-05-23 18:13:18',NULL),('0775678901','Michael','Brown','michael.b@example.com',300,'2025-05-23 18:13:18',NULL);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `district`
--

DROP TABLE IF EXISTS `district`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `district` (
  `id` int NOT NULL AUTO_INCREMENT,
  `district_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `province_id` int NOT NULL,
  `status_id` int NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_district_province1_idx` (`province_id`),
  KEY `fk_district_status1_idx` (`status_id`),
  CONSTRAINT `fk_district_province1` FOREIGN KEY (`province_id`) REFERENCES `province` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_district_status1` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `district`
--

LOCK TABLES `district` WRITE;
/*!40000 ALTER TABLE `district` DISABLE KEYS */;
INSERT INTO `district` VALUES (1,'Colombo',1,1,'2025-05-23 18:13:16',NULL),(2,'Gampaha',1,1,'2025-05-23 18:13:16',NULL),(3,'Kalutara',1,1,'2025-05-23 18:13:16',NULL),(4,'aaaa',2,1,'2025-05-23 18:13:16',NULL),(5,'Matale',2,1,'2025-05-23 18:13:16',NULL);
/*!40000 ALTER TABLE `district` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `editable_receipt`
--

DROP TABLE IF EXISTS `editable_receipt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `editable_receipt` (
  `id` int NOT NULL AUTO_INCREMENT,
  `shop_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `logo` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Address` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mobile` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `thank_note` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `editable_receipt`
--

LOCK TABLES `editable_receipt` WRITE;
/*!40000 ALTER TABLE `editable_receipt` DISABLE KEYS */;
INSERT INTO `editable_receipt` VALUES (1,'LexSo POS','/images/logo.png','123 Main Street, Colombo 3','0112345678','info@lexso.com','Thank you for shopping with us!','2025-05-23 18:13:18',NULL),(2,'LexSo Market','/images/market_logo.png','456 High Street, Colombo 4','0112345679','market@lexso.com','Visit us again soon!','2025-05-23 18:13:18',NULL);
/*!40000 ALTER TABLE `editable_receipt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gender`
--

DROP TABLE IF EXISTS `gender`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gender` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gender`
--

LOCK TABLES `gender` WRITE;
/*!40000 ALTER TABLE `gender` DISABLE KEYS */;
INSERT INTO `gender` VALUES (1,'Male','2025-05-23 18:13:15',NULL),(2,'Female','2025-05-23 18:13:15',NULL),(3,'Other','2025-05-23 18:13:15',NULL),(4,'Prefer not to say','2025-05-23 18:13:15',NULL),(5,'Non-binary','2025-05-23 18:13:15',NULL);
/*!40000 ALTER TABLE `gender` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gift_cards`
--

DROP TABLE IF EXISTS `gift_cards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gift_cards` (
  `gift_card_id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `purpose` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `card_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `status` enum('Active','Inactive','Pending','Expired','Redeemed') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`gift_card_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gift_cards`
--

LOCK TABLES `gift_cards` WRITE;
/*!40000 ALTER TABLE `gift_cards` DISABLE KEYS */;
INSERT INTO `gift_cards` VALUES (1,'Birthday Gift Card','Birthday Gift','BGC-123456',5000.00,'Active','2023-07-01 04:30:00',NULL),(2,'Anniversary Gift Card','Anniversary Gift','AGC-789012',10000.00,'Active','2023-07-02 05:30:00',NULL),(3,'Thank You Gift Card','Appreciation','TGC-345678',2500.00,'Redeemed','2023-07-03 06:30:00',NULL),(4,'Welcome Gift Card','New Customer','WGC-901234',1000.00,'Active','2023-07-04 07:30:00',NULL),(5,'Christmas Gift Card','Christmas Gift','CGC-567890',7500.00,'Pending','2023-07-05 08:30:00',NULL);
/*!40000 ALTER TABLE `gift_cards` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grn`
--

DROP TABLE IF EXISTS `grn`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grn` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `supplier_mobile` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `date` date NOT NULL,
  `user_email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `paid_amount` double NOT NULL,
  `full_amount` double DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_grn_user1_idx` (`user_email`),
  KEY `fk_grn_supplier1_idx` (`supplier_mobile`),
  CONSTRAINT `fk_grn_supplier1` FOREIGN KEY (`supplier_mobile`) REFERENCES `supplier` (`mobile`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_grn_user1` FOREIGN KEY (`user_email`) REFERENCES `user` (`email`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grn`
--

LOCK TABLES `grn` WRITE;
/*!40000 ALTER TABLE `grn` DISABLE KEYS */;
INSERT INTO `grn` VALUES (1,'0711234567','2023-06-01','stock@lexso.com',800000,800000,'2025-05-23 18:13:18',NULL),(2,'0712345678','2023-06-15','stock@lexso.com',45000,45000,'2025-05-23 18:13:18',NULL),(3,'0713456789','2023-07-01','manager@lexso.com',15000,30000,'2025-05-23 18:13:18',NULL),(4,'0714567890','2023-07-15','stock@lexso.com',150000,150000,'2025-05-23 18:13:18',NULL),(5,'0715678901','2023-08-01','manager@lexso.com',475000,475000,'2025-05-23 18:13:18',NULL);
/*!40000 ALTER TABLE `grn` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grn_item`
--

DROP TABLE IF EXISTS `grn_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grn_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `grn_id` bigint NOT NULL,
  `qty` double NOT NULL,
  `price` double NOT NULL,
  `stock_id` int NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_grn_item_grn1_idx` (`grn_id`),
  KEY `fk_grn_item_stock1_idx` (`stock_id`),
  CONSTRAINT `fk_grn_item_grn1` FOREIGN KEY (`grn_id`) REFERENCES `grn` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_grn_item_stock1` FOREIGN KEY (`stock_id`) REFERENCES `stock` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grn_item`
--

LOCK TABLES `grn_item` WRITE;
/*!40000 ALTER TABLE `grn_item` DISABLE KEYS */;
INSERT INTO `grn_item` VALUES (1,1,10,80000,1,'2025-05-23 18:13:18',NULL),(2,2,100,450,3,'2025-05-23 18:13:18',NULL),(3,3,200,75,4,'2025-05-23 18:13:18',NULL),(4,4,20,7500,5,'2025-05-23 18:13:18',NULL),(5,5,5,95000,1,'2025-05-23 18:13:18',NULL);
/*!40000 ALTER TABLE `grn_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice` (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `customer_mobile` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `date` datetime NOT NULL,
  `paid_amount` decimal(10,2) NOT NULL,
  `discount` decimal(10,2) NOT NULL DEFAULT '0.00',
  `invoice_discount_id` int DEFAULT NULL,
  `payment_status` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_invoice_customer1_idx` (`customer_mobile`),
  KEY `fk_invoice_user1_idx` (`user_email`),
  KEY `fk_invoice_invoice_discount1_idx` (`invoice_discount_id`),
  CONSTRAINT `fk_invoice_customer1` FOREIGN KEY (`customer_mobile`) REFERENCES `customer` (`mobile`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_invoice_invoice_discount1` FOREIGN KEY (`invoice_discount_id`) REFERENCES `invoice_discount` (`id`),
  CONSTRAINT `fk_invoice_user1` FOREIGN KEY (`user_email`) REFERENCES `user` (`email`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice`
--

LOCK TABLES `invoice` WRITE;
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
INSERT INTO `invoice` VALUES ('I00001','0771234567','2025-05-01 02:36:59',95000.00,0.00,1,'paid','admin@lexso.com','2025-05-23 18:13:18',NULL),('I00002','0772345678','2025-05-02 02:36:59',9500.00,500.00,2,'paid','cashier@lexso.com','2025-05-23 18:13:18',NULL),('I00003','0773456789','2025-05-03 02:36:59',130000.00,5000.00,3,'paid','cashier@lexso.com','2025-05-23 18:13:18',NULL),('I00004','0774567890','2025-05-04 02:36:59',8900.00,0.00,4,'paid','cashier@lexso.com','2025-05-23 18:13:18',NULL),('I00005','0775678901','2025-05-05 02:36:59',115000.00,10000.00,5,'paid','cashier@lexso.com','2025-05-23 18:13:18',NULL),('I00006','0771234567','2025-05-05 02:36:59',9500.00,0.00,NULL,'Success','cashier@lexso.com','2025-05-25 20:55:54',NULL),('I00007','0772345678','2025-05-06 02:36:59',135000.00,0.00,NULL,'Success','cashier@lexso.com','2025-05-25 21:06:59',NULL);
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_discount`
--

DROP TABLE IF EXISTS `invoice_discount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice_discount` (
  `id` int NOT NULL AUTO_INCREMENT,
  `discount_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `discount_type` enum('percentage','fixed') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `discount_value` decimal(10,2) NOT NULL,
  `invoice_subtotal` decimal(10,2) DEFAULT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_discount`
--

LOCK TABLES `invoice_discount` WRITE;
/*!40000 ALTER TABLE `invoice_discount` DISABLE KEYS */;
INSERT INTO `invoice_discount` VALUES (1,'New Year Sale','percentage',10.00,10000.00,'2023-01-01 00:00:00','2023-01-15 23:59:59','2025-05-23 18:13:18',NULL),(2,'Summer Discount','percentage',15.00,5000.00,'2023-04-01 00:00:00','2023-05-31 23:59:59','2025-05-23 18:13:18',NULL),(3,'Holiday Special','fixed',2000.00,NULL,'2023-12-01 00:00:00','2023-12-31 23:59:59','2025-05-23 18:13:18',NULL),(4,'Loyalty Reward','percentage',5.00,NULL,'2023-01-01 00:00:00','2023-12-31 23:59:59','2025-05-23 18:13:18',NULL),(5,'Flash Sale','percentage',25.00,15000.00,'2023-06-01 00:00:00','2023-06-02 23:59:59','2025-05-23 18:13:18',NULL);
/*!40000 ALTER TABLE `invoice_discount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_item`
--

DROP TABLE IF EXISTS `invoice_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `stock_id` int NOT NULL,
  `qty` double NOT NULL,
  `invoice_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_stock_has_invoice_stock1_idx` (`stock_id`),
  KEY `fk_invoice_item_invoice1_idx` (`invoice_id`),
  CONSTRAINT `fk_invoice_item_invoice1` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_stock_has_invoice_stock1` FOREIGN KEY (`stock_id`) REFERENCES `stock` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_item`
--

LOCK TABLES `invoice_item` WRITE;
/*!40000 ALTER TABLE `invoice_item` DISABLE KEYS */;
INSERT INTO `invoice_item` VALUES (1,4,1,'I00001','2025-05-23 18:13:18',NULL),(2,3,1,'I00002','2025-05-23 18:13:18',NULL),(3,2,1,'I00003','2025-05-23 18:13:18',NULL),(4,5,1,'I00004','2025-05-23 18:13:18',NULL),(5,1,1,'I00005','2025-05-23 18:13:18',NULL),(6,3,1,'I00006','2025-05-25 20:55:54',NULL),(7,2,1,'I00007','2025-05-25 21:06:59',NULL);
/*!40000 ALTER TABLE `invoice_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `notifications_id` int NOT NULL AUTO_INCREMENT,
  `messages` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `status` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`notifications_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (1,'Welcome to LexSo POS System','2025-05-23 23:43:18',NULL,'unread'),(2,'New stock update available','2025-05-23 23:43:18',NULL,'unread'),(3,'Monthly sales report is ready','2025-05-23 23:43:18',NULL,'read'),(4,'Backup completed successfully','2025-05-23 23:43:18',NULL,'read'),(5,'System maintenance scheduled for 2023-10-10','2025-05-23 23:43:18',NULL,'unread');
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_method`
--

DROP TABLE IF EXISTS `payment_method`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_method` (
  `method_id` int NOT NULL AUTO_INCREMENT,
  `invoice_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `payment_type` enum('cash','card','cheque','loyalty_points','gift_card') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `transaction_reference` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`method_id`),
  KEY `fk_payment_method_invoice1_idx` (`invoice_id`),
  CONSTRAINT `fk_payment_method_invoice1` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_method`
--

LOCK TABLES `payment_method` WRITE;
/*!40000 ALTER TABLE `payment_method` DISABLE KEYS */;
INSERT INTO `payment_method` VALUES (1,'I00001','cash',95000.00,NULL,'2025-05-23 18:13:18',NULL),(2,'I00002','card',9500.00,'CARD-1234567890','2025-05-23 18:13:18',NULL),(3,'I00003','card',130000.00,'CARD-2345678901','2025-05-23 18:13:18',NULL),(4,'I00004','cash',8900.00,NULL,'2025-05-23 18:13:18',NULL),(5,'I00005','cheque',115000.00,'CHQ-3456789012','2025-05-23 18:13:18',NULL),(6,'I00006','cash',100.00,NULL,'2025-05-25 20:55:54',NULL),(7,'I00006','card',10000.00,'','2025-05-25 20:55:54',NULL),(8,'I00007','loyalty_points',10.00,NULL,'2025-05-25 21:07:00',NULL),(9,'I00007','cash',25000.00,NULL,'2025-05-25 21:07:00',NULL),(10,'I00007','card',100000.00,'','2025-05-25 21:07:00',NULL);
/*!40000 ALTER TABLE `payment_method` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `category` int NOT NULL,
  `product_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `unit` enum('kg','liter','piece','pack','gram','ml') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `warranty_period` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `product_image` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` enum('active','inactive') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'active',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `fk_product_categories_has_brand1_idx` (`category`),
  CONSTRAINT `fk_product_categories_has_brand1` FOREIGN KEY (`category`) REFERENCES `category_has_brand` (`category_has_brand_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES ('P001','Samsung Galaxy S21',1,'Latest Samsung smartphone','piece','12 months','/images/galaxy_s21.jpg','active','2025-05-23 18:13:18',NULL),('P002','iPhone 13 Pro',2,'Latest Apple smartphone','piece','12 months','/images/iphone13.jpg','active','2025-05-23 18:13:18',NULL),('P003','Sony 55\" LED TV',3,'4K Ultra HD Smart TV','piece','24 months','/images/sony_tv.jpg','inactive','2025-05-23 18:13:18',NULL),('P004','LG Refrigerator',4,'Double door refrigerator','piece','36 months','/images/lg_fridge.jpg','active','2025-05-23 18:13:18',NULL),('P005','Panasonic Microwave',5,'Convection Microwave Oven','piece','12 months','/images/panasonic_mw.jpg','active','2025-05-23 18:13:18',NULL);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `province`
--

DROP TABLE IF EXISTS `province`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `province` (
  `id` int NOT NULL AUTO_INCREMENT,
  `province_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `country_id` int NOT NULL,
  `status_id` int NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_province_country1_idx` (`country_id`),
  KEY `fk_province_status1_idx` (`status_id`),
  CONSTRAINT `fk_province_country1` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_province_status1` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `province`
--

LOCK TABLES `province` WRITE;
/*!40000 ALTER TABLE `province` DISABLE KEYS */;
INSERT INTO `province` VALUES (1,'Western',1,1,'2025-05-23 18:13:16',NULL),(2,'Central',1,1,'2025-05-23 18:13:16',NULL),(3,'Southern',1,1,'2025-05-23 18:13:16',NULL),(4,'Northern',1,1,'2025-05-23 18:13:16',NULL),(5,'Eastern',1,1,'2025-05-23 18:13:16',NULL);
/*!40000 ALTER TABLE `province` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'Administrator','2025-05-23 18:13:15',NULL),(2,'Manager','2025-05-23 18:13:15',NULL),(3,'Cashier','2025-05-23 18:13:15',NULL),(4,'Stock Manager','2025-05-23 18:13:15',NULL),(5,'Accountant','2025-05-23 18:13:15',NULL);
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shift`
--

DROP TABLE IF EXISTS `shift`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shift` (
  `shift_id` int NOT NULL AUTO_INCREMENT,
  `shift_start_time` time NOT NULL,
  `shift_end_time` time NOT NULL,
  `shift_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`shift_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shift`
--

LOCK TABLES `shift` WRITE;
/*!40000 ALTER TABLE `shift` DISABLE KEYS */;
INSERT INTO `shift` VALUES (1,'08:00:00','16:00:00','Morning Shift','2025-05-23 18:13:15',NULL),(2,'16:00:00','00:00:00','Evening Shift','2025-05-23 18:13:15',NULL),(3,'00:00:00','08:00:00','Night Shift','2025-05-23 18:13:15',NULL),(4,'10:00:00','18:00:00','Custom Shift','2025-05-23 18:13:15',NULL),(5,'12:00:00','20:00:00','Afternoon Shift','2025-05-23 18:13:15',NULL);
/*!40000 ALTER TABLE `shift` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` VALUES (1,'Active','2025-05-23 18:13:16',NULL),(2,'Inactive','2025-05-23 18:13:16',NULL),(3,'Pending','2025-05-23 18:13:16',NULL),(4,'Suspended','2025-05-23 18:13:16',NULL),(5,'Archived','2025-05-23 18:13:16',NULL);
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock`
--

DROP TABLE IF EXISTS `stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `barcode` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `qty` double NOT NULL,
  `buying_price` double NOT NULL,
  `selling_price` double NOT NULL,
  `mft` date NOT NULL,
  `exp` date NOT NULL,
  `stock_location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `low_stock_warning` int DEFAULT NULL,
  `exp_warning_days` int DEFAULT NULL,
  `discount_type` enum('percentage','fixed','no_discount') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `discount_value` decimal(10,2) DEFAULT NULL,
  `discount_start_date` datetime NOT NULL,
  `discount_end_date` datetime NOT NULL,
  `user_email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_stock_product1_idx` (`product_id`),
  KEY `fk_stock_user1_idx` (`user_email`),
  CONSTRAINT `fk_stock_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_stock_user1` FOREIGN KEY (`user_email`) REFERENCES `user` (`email`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock`
--

LOCK TABLES `stock` WRITE;
/*!40000 ALTER TABLE `stock` DISABLE KEYS */;
INSERT INTO `stock` VALUES (1,'P001','1234567890123',10,80000,95000,'2023-01-01','2025-01-01','Rack A1',5,30,'no_discount',NULL,'2023-01-01 00:00:00','2023-01-01 00:00:00','stock@lexso.com','2025-05-23 18:13:18',NULL),(2,'P002','2345678901234',14,110000,135000,'2023-01-15','2025-01-15','Rack A2',3,30,'no_discount',NULL,'2023-01-01 00:00:00','2023-01-01 00:00:00','stock@lexso.com','2025-05-23 18:13:18',NULL),(3,'P003','3456789012345',3,8000,9500,'2023-02-01','2025-02-01','Rack B1',2,30,'percentage',5.00,'2023-06-01 00:00:00','2023-06-30 23:59:59','stock@lexso.com','2025-05-23 18:13:18',NULL),(4,'P004','4567890123456',8,45000,55000,'2023-02-15','2025-02-15','Rack B2',3,30,'fixed',5000.00,'2023-06-01 00:00:00','2023-06-30 23:59:59','stock@lexso.com','2025-05-23 18:13:18',NULL),(5,'P005','5678901234567',12,7500,8900,'2023-03-01','2025-03-01','Rack C1',5,30,'no_discount',NULL,'2023-01-01 00:00:00','2023-01-01 00:00:00','stock@lexso.com','2025-05-23 18:13:18',NULL);
/*!40000 ALTER TABLE `stock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock_reorder`
--

DROP TABLE IF EXISTS `stock_reorder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock_reorder` (
  `id` int NOT NULL AUTO_INCREMENT,
  `reorder_date` date NOT NULL,
  `arrival_date` date NOT NULL,
  `order_amount` decimal(10,2) NOT NULL,
  `order_qty` int NOT NULL,
  `status` enum('pending','delivered','canceled') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending',
  `stock_id` int NOT NULL,
  `supplier_mobile` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_stock_reorder_stock1_idx` (`stock_id`),
  KEY `fk_stock_reorder_supplier1_idx1` (`supplier_mobile`),
  CONSTRAINT `fk_stock_reorder_stock1` FOREIGN KEY (`stock_id`) REFERENCES `stock` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_stock_reorder_supplier1` FOREIGN KEY (`supplier_mobile`) REFERENCES `supplier` (`mobile`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock_reorder`
--

LOCK TABLES `stock_reorder` WRITE;
/*!40000 ALTER TABLE `stock_reorder` DISABLE KEYS */;
INSERT INTO `stock_reorder` VALUES (1,'2023-09-15','2023-09-22',160000.00,2,'delivered',1,'0711234567','2025-05-23 18:13:18',NULL),(2,'2023-09-16','2023-09-23',220000.00,2,'delivered',2,'0711234567','2025-05-23 18:13:18',NULL),(3,'2023-09-17','2023-09-24',24000.00,3,'pending',3,'0713456789','2025-05-23 18:13:18',NULL),(4,'2023-09-18','2023-09-25',45000.00,100,'pending',4,'0713456789','2025-05-23 18:13:18',NULL),(5,'2023-09-19','2023-09-26',15000.00,200,'canceled',5,'0715678901','2025-05-23 18:13:18',NULL);
/*!40000 ALTER TABLE `stock_reorder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier`
--

DROP TABLE IF EXISTS `supplier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier` (
  `mobile` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `first_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(75) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `company_id` int NOT NULL,
  `status` enum('active','inactive') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'active',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`mobile`),
  KEY `fk_supplier_company1_idx` (`company_id`),
  CONSTRAINT `fk_supplier_company1` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier`
--

LOCK TABLES `supplier` WRITE;
/*!40000 ALTER TABLE `supplier` DISABLE KEYS */;
INSERT INTO `supplier` VALUES ('0711234567','David','Wilson','david.w@example.com',1,'active','2025-05-23 18:13:18',NULL),('0712345678','Emily','Davis','emily.d@example.com',2,'active','2025-05-23 18:13:18',NULL),('0713456789','Richard','Miller','richard.m@example.com',3,'active','2025-05-23 18:13:18',NULL),('0714567890','Jennifer','Taylor','jennifer.t@example.com',4,'active','2025-05-23 18:13:18',NULL),('0715678901','Thomas','Anderson','thomas.a@example.com',5,'active','2025-05-23 18:13:18',NULL);
/*!40000 ALTER TABLE `supplier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `theme`
--

DROP TABLE IF EXISTS `theme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `theme` (
  `id` int NOT NULL AUTO_INCREMENT,
  `theme_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `value` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `theme`
--

LOCK TABLES `theme` WRITE;
/*!40000 ALTER TABLE `theme` DISABLE KEYS */;
INSERT INTO `theme` VALUES (1,'Light','#FFFFFF,#333333,#3498DB','2025-05-23 18:13:17',NULL),(2,'Dark','#121212,#FFFFFF,#BB86FC','2025-05-23 18:13:17',NULL),(3,'Blue','#E3F2FD,#333333,#2196F3','2025-05-23 18:13:17',NULL),(4,'Green','#E8F5E9,#333333,#4CAF50','2025-05-23 18:13:17',NULL),(5,'Orange','#FFF3E0,#333333,#FF9800','2025-05-23 18:13:17',NULL);
/*!40000 ALTER TABLE `theme` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `first_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `role_id` int NOT NULL,
  `nic` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('Active','Inactive') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `date_registered` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `signature` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '/com/lexso/users/signatures/nullsignature.png',
  `profile_picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '/com/lexso/users/profilepics/man.png',
  `whatsapp_number` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mobile` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `birthday` date NOT NULL,
  `age` int DEFAULT NULL,
  `gender_id` int NOT NULL,
  `last_login` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`email`),
  UNIQUE KEY `nic_UNIQUE` (`nic`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  KEY `fk_user_role_idx` (`role_id`),
  KEY `fk_user_gender1_idx` (`gender_id`),
  CONSTRAINT `fk_user_gender1` FOREIGN KEY (`gender_id`) REFERENCES `gender` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_user_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('accountant@lexso.com','hashed_password_5','Account','Manager','ACC001','accountant',5,'567890123V','Active','2025-05-23 23:43:18','/com/lexso/users/signatures/nullsignature.png','/com/lexso/users/profilepics/man.png','94745678901','0745678901','1998-05-05',25,2,NULL,NULL),('admin@lexso.com','Admin@123','Admin','User','ADM001','admin',1,'123456789V','Active','2025-05-23 23:43:18','/com/lexso/users/signatures/nullsignature.png','/com/lexso/users/profilepics/255_kreedx my image.jpeg','94711234567','0711234567','1990-01-01',33,1,NULL,NULL),('cashier@lexso.com','hashed_password_3','Cashier','User','CSH001','cashier',3,'345678901V','Active','2025-05-23 23:43:18','/com/lexso/users/signatures/nullsignature.png','/com/lexso/users/profilepics/man.png','94723456789','0723456789','1994-03-03',29,2,NULL,NULL),('manager@lexso.com','hashed_password_2','Manager','User','MGR001','manager',2,'234567890V','Active','2025-05-23 23:43:18','/com/lexso/users/signatures/nullsignature.png','/com/lexso/users/profilepics/man.png','94712345678','0712345678','1992-02-02',31,1,NULL,NULL),('stock@lexso.com','hashed_password_4','Stock','Manager','STK001','stockmgr',4,'456789012V','Active','2025-05-23 23:43:18','/com/lexso/users/signatures/nullsignature.png','/com/lexso/users/profilepics/man.png','94734567890','0734567890','1996-04-04',29,1,NULL,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_address`
--

DROP TABLE IF EXISTS `user_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_address` (
  `id` int NOT NULL AUTO_INCREMENT,
  `line1` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `line2` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `city_id` int NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_address_user1_idx` (`user_email`),
  KEY `fk_user_address_city1_idx` (`city_id`),
  CONSTRAINT `fk_user_address_city1` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_user_address_user1` FOREIGN KEY (`user_email`) REFERENCES `user` (`email`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_address`
--

LOCK TABLES `user_address` WRITE;
/*!40000 ALTER TABLE `user_address` DISABLE KEYS */;
INSERT INTO `user_address` VALUES (1,'123 Main St','Apartment 4A','admin@lexso.com',1,'2025-05-23 18:13:18',NULL),(2,'456 Oak Ave','Suite 101','manager@lexso.com',2,'2025-05-23 18:13:18',NULL),(3,'789 Pine Rd','Unit 7','cashier@lexso.com',3,'2025-05-23 18:13:18',NULL),(4,'101 Maple St','Floor 2','stock@lexso.com',4,'2025-05-23 18:13:18',NULL),(5,'202 Cedar Ln','Room 303','accountant@lexso.com',5,'2025-05-23 18:13:18',NULL);
/*!40000 ALTER TABLE `user_address` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-29 20:00:32
