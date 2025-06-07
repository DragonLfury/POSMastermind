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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `backup_logs`
--

LOCK TABLES `backup_logs` WRITE;
/*!40000 ALTER TABLE `backup_logs` DISABLE KEYS */;
INSERT INTO `backup_logs` VALUES (1,'2025-05-29 20:00:32','lexso_20250529_200032.sql',NULL),(2,'2025-06-05 13:13:11','lexso_20250605_131310.sql',NULL);
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
INSERT INTO `customer` VALUES ('0771234567','John','Doe','john.doe@example.com',7287,'2025-05-23 18:13:18',NULL),('0772345678','Jane','Smith','jane.smith@example.com',13602,'2025-05-23 18:13:18',NULL),('0773456789','Robert','Johnson','robert.j@example.com',2512,'2025-05-23 18:13:18',NULL),('0774567890','Sarah','Williams','sarah.w@example.com',1126,'2025-05-23 18:13:18',NULL),('0775678901','Michael','Brown','michael.b@example.com',7322,'2025-05-23 18:13:18',NULL);
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
INSERT INTO `editable_receipt` VALUES (1,'LexSo POS','src/com/lexso/settings/resources/logo.png','123 Main Street, Colombo 3','0112345678','info@lexso.com','Thank you for shopping with us!','2025-05-23 18:13:18',NULL),(2,'LexSo Market','/images/market_logo.png','456 High Street, Colombo 4','0112345679','market@lexso.com','Visit us again soon!','2025-05-23 18:13:18',NULL);
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
INSERT INTO `gift_cards` VALUES (1,'Birthday Gift Card','Birthday Gift','BGC-123456',5000.00,'Inactive','2023-07-01 04:30:00',NULL),(2,'Anniversary Gift Card','Anniversary Gift','AGC-789012',1000.00,'Inactive','2023-07-02 05:30:00',NULL),(3,'Thank You Gift Card','Appreciation','TGC-345678',2500.00,'Redeemed','2023-07-03 06:30:00',NULL),(4,'Welcome Gift Card','New Customer','WGC-901234',1000.00,'Inactive','2023-07-04 07:30:00',NULL),(5,'Christmas Gift Card','Christmas Gift','CGC-567890',7500.00,'Pending','2023-07-05 08:30:00',NULL);
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
INSERT INTO `invoice` VALUES ('I00001','0771234567','2025-06-01 02:36:59',95000.00,0.00,1,'paid','admin@lexso.com','2025-05-23 18:13:18',NULL),('I00002','0772345678','2025-06-02 02:36:59',9500.00,500.00,2,'paid','cashier@lexso.com','2025-05-23 18:13:18',NULL),('I00003','0773456789','2025-06-03 02:36:59',130000.00,5000.00,3,'paid','cashier@lexso.com','2025-05-23 18:13:18',NULL),('I00004','0774567890','2025-06-04 02:36:59',8900.00,0.00,4,'paid','cashier@lexso.com','2025-05-23 18:13:18',NULL),('I00005','0775678901','2025-06-05 02:36:59',115000.00,10000.00,5,'paid','cashier@lexso.com','2025-05-23 18:13:18',NULL),('I00006','0771234567','2025-06-05 02:36:59',9500.00,0.00,NULL,'Success','cashier@lexso.com','2025-05-25 20:55:54',NULL),('I00007','0772345678','2025-06-06 02:36:59',135000.00,0.00,NULL,'Success','cashier@lexso.com','2025-05-25 21:06:59',NULL),('I00008','0775678901','2025-06-06 14:22:10',152800.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 08:52:10',NULL),('I00009','0775678901','2025-06-06 15:07:06',9500.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 09:37:06',NULL),('I00010','0773456789','2025-06-06 15:35:30',135000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 10:05:30',NULL),('I00011','0775678901','2025-06-06 15:41:47',110000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 10:11:47',NULL),('I00012','0775678901','2025-06-06 15:43:52',55000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 10:13:52',NULL),('I00013','0772345678','2025-06-06 15:48:20',55000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 10:18:20',NULL),('I00014','0773456789','2025-06-06 15:49:59',55000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 10:19:59',NULL),('I00015','0772345678','2025-06-06 15:51:31',55000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 10:21:31',NULL),('I00016','0771234567','2025-06-06 15:53:45',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 10:23:45',NULL),('I00017','0773456789','2025-06-06 15:56:11',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 10:26:11',NULL),('I00018','0771234567','2025-06-06 15:58:53',135000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 10:28:53',NULL),('I00019','0773456789','2025-06-06 17:10:55',95000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 11:40:55',NULL),('I00020','0771234567','2025-06-06 17:13:20',135000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 11:43:20',NULL),('I00021','0775678901','2025-06-06 17:18:02',135000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 11:48:02',NULL),('I00022','0775678901','2025-06-06 17:19:06',135000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 11:49:06',NULL),('I00023','0774567890','2025-06-06 17:22:39',95000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 11:52:39',NULL),('I00024','0772345678','2025-06-06 17:27:41',135000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 11:57:41',NULL),('I00025','0772345678','2025-06-06 17:35:24',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 12:05:24',NULL),('I00026','0772345678','2025-06-06 17:37:55',95000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 12:07:55',NULL),('I00027','0771234567','2025-06-06 17:39:45',135000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 12:09:45',NULL),('I00028','0772345678','2025-06-06 17:40:35',135000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 12:10:35',NULL),('I00029','0775678901','2025-06-06 17:45:09',143900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 12:15:09',NULL),('I00030','0772345678','2025-06-06 17:51:21',95000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 12:21:21',NULL),('I00031','0772345678','2025-06-06 17:53:27',95000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 12:23:27',NULL),('I00032','0772345678','2025-06-06 17:58:25',95000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 12:28:25',NULL),('I00033','0771234567','2025-06-06 18:02:31',95000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 12:32:31',NULL),('I00034','0772345678','2025-06-06 18:05:35',135000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 12:35:35',NULL),('I00035','0771234567','2025-06-06 18:08:08',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 12:38:08',NULL),('I00036','0771234567','2025-06-06 18:26:26',95000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 12:56:26',NULL),('I00037','0771234567','2025-06-06 18:34:52',135000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 13:04:52',NULL),('I00038','0775678901','2025-06-06 18:37:18',135000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 13:07:18',NULL),('I00039','0772345678','2025-06-06 18:43:46',55000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 13:13:46',NULL),('I00040','0772345678','2025-06-06 18:46:02',9500.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 13:16:03',NULL),('I00041','0771234567','2025-06-06 18:48:24',95000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 13:18:24',NULL),('I00042','0771234567','2025-06-06 18:53:54',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 13:23:54',NULL),('I00043','0773456789','2025-06-06 19:05:05',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 13:35:05',NULL),('I00044','0771234567','2025-06-06 19:10:12',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 13:40:12',NULL),('I00045','0773456789','2025-06-06 19:12:18',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 13:42:18',NULL),('I00046','0772345678','2025-06-06 19:18:07',95000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 13:48:07',NULL),('I00047','0775678901','2025-06-06 19:20:34',135000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 13:50:34',NULL),('I00048','0771234567','2025-06-06 19:21:56',135000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 13:51:56',NULL),('I00049','0775678901','2025-06-06 19:22:46',9500.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 13:52:46',NULL),('I00050','0772345678','2025-06-06 19:37:30',95000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 14:07:30',NULL),('I00051','0772345678','2025-06-06 21:08:15',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 15:38:15',NULL),('I00052','0771234567','2025-06-06 21:11:58',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 15:41:58',NULL),('I00053','0775678901','2025-06-06 21:29:54',135000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 15:59:54',NULL),('I00054','0773456789','2025-06-06 21:32:16',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 16:02:16',NULL),('I00055','0772345678','2025-06-06 21:40:03',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 16:10:03',NULL),('I00056','0771234567','2025-06-06 21:40:37',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 16:10:37',NULL),('I00057','0773456789','2025-06-06 21:42:59',95000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 16:12:59',NULL),('I00058','0772345678','2025-06-06 21:49:36',135000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 16:19:36',NULL),('I00059','0771234567','2025-06-06 21:51:16',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 16:21:16',NULL),('I00060','0775678901','2025-06-06 22:00:26',135000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 16:30:26',NULL),('I00061','0771234567','2025-06-06 22:28:22',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 16:58:22',NULL),('I00062','0774567890','2025-06-06 22:31:23',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 17:01:23',NULL),('I00063','0773456789','2025-06-06 22:35:54',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 17:05:54',NULL),('I00064','0773456789','2025-06-06 22:41:12',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 17:11:12',NULL),('I00065','0774567890','2025-06-06 22:43:14',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 17:13:14',NULL),('I00066','0775678901','2025-06-06 22:52:21',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 17:22:21',NULL),('I00067','0775678901','2025-06-06 22:54:29',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 17:24:29',NULL),('I00068','0775678901','2025-06-06 22:57:21',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 17:27:21',NULL),('I00069','0775678901','2025-06-06 23:17:22',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 17:47:22',NULL),('I00070','0773456789','2025-06-06 23:25:10',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 17:55:10',NULL),('I00071','0771234567','2025-06-07 00:12:39',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 18:42:39',NULL),('I00072','0771234567','2025-06-07 00:16:24',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 18:46:24',NULL),('I00073','0771234567','2025-06-07 00:21:36',9500.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 18:51:36',NULL),('I00074','0773456789','2025-06-07 00:23:45',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 18:53:45',NULL),('I00075','0774567890','2025-06-07 00:26:51',55000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 18:56:51',NULL),('I00076','0774567890','2025-06-07 00:28:01',8900.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 18:58:01',NULL),('I00077','0775678901','2025-06-07 01:01:00',55000.00,0.00,NULL,'Success','admin@lexso.com','2025-06-06 19:31:00',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_item`
--

LOCK TABLES `invoice_item` WRITE;
/*!40000 ALTER TABLE `invoice_item` DISABLE KEYS */;
INSERT INTO `invoice_item` VALUES (1,4,1,'I00001','2025-05-23 18:13:18',NULL),(2,3,1,'I00002','2025-05-23 18:13:18',NULL),(3,2,1,'I00003','2025-05-23 18:13:18',NULL),(4,5,1,'I00004','2025-05-23 18:13:18',NULL),(5,1,1,'I00005','2025-05-23 18:13:18',NULL),(6,3,1,'I00006','2025-05-25 20:55:54',NULL),(7,2,1,'I00007','2025-05-25 21:06:59',NULL),(8,2,1,'I00008','2025-06-06 08:52:10',NULL),(9,5,2,'I00008','2025-06-06 08:52:10',NULL),(10,3,1,'I00009','2025-06-06 09:37:06',NULL),(11,2,1,'I00010','2025-06-06 10:05:30',NULL),(12,4,2,'I00011','2025-06-06 10:11:47',NULL),(13,4,1,'I00012','2025-06-06 10:13:52',NULL),(14,4,1,'I00013','2025-06-06 10:18:20',NULL),(15,4,1,'I00014','2025-06-06 10:19:59',NULL),(16,4,1,'I00015','2025-06-06 10:21:31',NULL),(17,5,1,'I00016','2025-06-06 10:23:45',NULL),(18,5,1,'I00017','2025-06-06 10:26:11',NULL),(19,2,1,'I00018','2025-06-06 10:28:53',NULL),(20,1,1,'I00019','2025-06-06 11:40:55',NULL),(21,2,1,'I00020','2025-06-06 11:43:20',NULL),(22,2,1,'I00021','2025-06-06 11:48:02',NULL),(23,2,1,'I00022','2025-06-06 11:49:06',NULL),(24,1,1,'I00023','2025-06-06 11:52:39',NULL),(25,2,1,'I00024','2025-06-06 11:57:41',NULL),(26,5,1,'I00025','2025-06-06 12:05:24',NULL),(27,1,1,'I00026','2025-06-06 12:07:55',NULL),(28,2,1,'I00027','2025-06-06 12:09:45',NULL),(29,2,1,'I00028','2025-06-06 12:10:35',NULL),(30,2,1,'I00029','2025-06-06 12:15:09',NULL),(31,5,1,'I00029','2025-06-06 12:15:10',NULL),(32,1,1,'I00030','2025-06-06 12:21:21',NULL),(33,1,1,'I00031','2025-06-06 12:23:27',NULL),(34,1,1,'I00032','2025-06-06 12:28:26',NULL),(35,1,1,'I00033','2025-06-06 12:32:31',NULL),(36,2,1,'I00034','2025-06-06 12:35:35',NULL),(37,5,1,'I00035','2025-06-06 12:38:08',NULL),(38,1,1,'I00036','2025-06-06 12:56:26',NULL),(39,2,1,'I00037','2025-06-06 13:04:52',NULL),(40,2,1,'I00038','2025-06-06 13:07:18',NULL),(41,4,1,'I00039','2025-06-06 13:13:46',NULL),(42,3,1,'I00040','2025-06-06 13:16:03',NULL),(43,1,1,'I00041','2025-06-06 13:18:24',NULL),(44,5,1,'I00042','2025-06-06 13:23:54',NULL),(45,5,1,'I00043','2025-06-06 13:35:05',NULL),(46,5,1,'I00044','2025-06-06 13:40:13',NULL),(47,5,1,'I00045','2025-06-06 13:42:18',NULL),(48,1,1,'I00046','2025-06-06 13:48:07',NULL),(49,2,1,'I00047','2025-06-06 13:50:34',NULL),(50,2,1,'I00048','2025-06-06 13:51:57',NULL),(51,3,1,'I00049','2025-06-06 13:52:46',NULL),(52,1,1,'I00050','2025-06-06 14:07:30',NULL),(53,5,1,'I00051','2025-06-06 15:38:16',NULL),(54,5,1,'I00052','2025-06-06 15:41:59',NULL),(55,2,1,'I00053','2025-06-06 15:59:54',NULL),(56,5,1,'I00054','2025-06-06 16:02:16',NULL),(57,5,1,'I00055','2025-06-06 16:10:03',NULL),(58,5,1,'I00056','2025-06-06 16:10:37',NULL),(59,1,1,'I00057','2025-06-06 16:13:00',NULL),(60,2,1,'I00058','2025-06-06 16:19:37',NULL),(61,5,1,'I00059','2025-06-06 16:21:16',NULL),(62,2,1,'I00060','2025-06-06 16:30:26',NULL),(63,5,1,'I00061','2025-06-06 16:58:22',NULL),(64,5,1,'I00062','2025-06-06 17:01:24',NULL),(65,5,1,'I00063','2025-06-06 17:05:55',NULL),(66,5,1,'I00064','2025-06-06 17:11:12',NULL),(67,5,1,'I00065','2025-06-06 17:13:14',NULL),(68,5,1,'I00066','2025-06-06 17:22:22',NULL),(69,5,1,'I00067','2025-06-06 17:24:29',NULL),(70,5,1,'I00068','2025-06-06 17:27:21',NULL),(71,5,1,'I00069','2025-06-06 17:47:22',NULL),(72,5,1,'I00070','2025-06-06 17:55:11',NULL),(73,5,1,'I00071','2025-06-06 18:42:39',NULL),(74,5,1,'I00072','2025-06-06 18:46:24',NULL),(75,3,1,'I00073','2025-06-06 18:51:36',NULL),(76,5,1,'I00074','2025-06-06 18:53:45',NULL),(77,4,1,'I00075','2025-06-06 18:56:51',NULL),(78,5,1,'I00076','2025-06-06 18:58:01',NULL),(79,4,1,'I00077','2025-06-06 19:31:00',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=170 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_method`
--

LOCK TABLES `payment_method` WRITE;
/*!40000 ALTER TABLE `payment_method` DISABLE KEYS */;
INSERT INTO `payment_method` VALUES (1,'I00001','cash',95000.00,NULL,'2025-05-23 18:13:18',NULL),(2,'I00002','card',9500.00,'CARD-1234567890','2025-05-23 18:13:18',NULL),(3,'I00003','card',130000.00,'CARD-2345678901','2025-05-23 18:13:18',NULL),(4,'I00004','cash',8900.00,NULL,'2025-05-23 18:13:18',NULL),(5,'I00005','cheque',115000.00,'CHQ-3456789012','2025-05-23 18:13:18',NULL),(6,'I00006','cash',100.00,NULL,'2025-05-25 20:55:54',NULL),(7,'I00006','card',10000.00,'','2025-05-25 20:55:54',NULL),(8,'I00007','loyalty_points',10.00,NULL,'2025-05-25 21:07:00',NULL),(9,'I00007','cash',25000.00,NULL,'2025-05-25 21:07:00',NULL),(10,'I00007','card',100000.00,'','2025-05-25 21:07:00',NULL),(11,'I00008','loyalty_points',100.00,NULL,'2025-06-06 08:52:11',NULL),(12,'I00008','cash',150000.00,NULL,'2025-06-06 08:52:11',NULL),(13,'I00009','loyalty_points',200.00,NULL,'2025-06-06 09:37:06',NULL),(14,'I00009','cash',500.00,NULL,'2025-06-06 09:37:06',NULL),(15,'I00010','cash',1355000.00,NULL,'2025-06-06 10:05:30',NULL),(16,'I00011','cash',10000.00,NULL,'2025-06-06 10:11:47',NULL),(17,'I00011','card',100000.00,'','2025-06-06 10:11:47',NULL),(18,'I00012','cash',550000.00,NULL,'2025-06-06 10:13:52',NULL),(19,'I00013','cash',55000.00,NULL,'2025-06-06 10:18:20',NULL),(20,'I00014','cash',60000.00,NULL,'2025-06-06 10:19:59',NULL),(21,'I00015','cash',55000.00,NULL,'2025-06-06 10:21:31',NULL),(22,'I00016','cash',8900.00,NULL,'2025-06-06 10:23:45',NULL),(23,'I00017','cash',8900.00,NULL,'2025-06-06 10:26:11',NULL),(24,'I00018','cash',135000.00,NULL,'2025-06-06 10:28:53',NULL),(25,'I00019','cash',95000.00,NULL,'2025-06-06 11:40:55',NULL),(26,'I00020','cash',135000.00,NULL,'2025-06-06 11:43:20',NULL),(27,'I00021','cash',135000.00,NULL,'2025-06-06 11:48:02',NULL),(28,'I00022','cash',135000.00,NULL,'2025-06-06 11:49:06',NULL),(29,'I00023','cash',95000.00,NULL,'2025-06-06 11:52:40',NULL),(30,'I00024','cash',135000.00,NULL,'2025-06-06 11:57:41',NULL),(31,'I00025','cash',8900.00,NULL,'2025-06-06 12:05:24',NULL),(32,'I00026','cash',95000.00,NULL,'2025-06-06 12:07:56',NULL),(33,'I00027','cash',135000.00,NULL,'2025-06-06 12:09:46',NULL),(34,'I00028','cash',14000.00,NULL,'2025-06-06 12:10:36',NULL),(35,'I00028','card',125000.00,'','2025-06-06 12:10:36',NULL),(36,'I00029','cash',143900.00,NULL,'2025-06-06 12:15:11',NULL),(37,'I00030','cash',950110.00,NULL,'2025-06-06 12:21:22',NULL),(38,'I00031','cash',95000.00,NULL,'2025-06-06 12:23:27',NULL),(39,'I00032','cash',95000.00,NULL,'2025-06-06 12:28:26',NULL),(40,'I00033','cash',95000.00,NULL,'2025-06-06 12:32:32',NULL),(41,'I00034','cash',150000.00,NULL,'2025-06-06 12:35:36',NULL),(42,'I00034','card',30000.00,'','2025-06-06 12:35:36',NULL),(43,'I00035','cash',8900.00,NULL,'2025-06-06 12:38:09',NULL),(44,'I00036','cash',95000.00,NULL,'2025-06-06 12:56:27',NULL),(45,'I00037','cash',135220.00,NULL,'2025-06-06 13:04:53',NULL),(46,'I00038','cash',135790.00,NULL,'2025-06-06 13:07:19',NULL),(47,'I00039','cash',55550.00,NULL,'2025-06-06 13:13:46',NULL),(48,'I00040','cash',323230.00,NULL,'2025-06-06 13:16:03',NULL),(49,'I00041','cash',3232232.00,NULL,'2025-06-06 13:18:25',NULL),(50,'I00042','cash',23220.00,NULL,'2025-06-06 13:23:55',NULL),(51,'I00043','cash',23120.00,NULL,'2025-06-06 13:35:05',NULL),(52,'I00044','cash',41330.00,NULL,'2025-06-06 13:40:13',NULL),(53,'I00045','cash',21120.00,NULL,'2025-06-06 13:42:19',NULL),(54,'I00046','cash',313110.00,NULL,'2025-06-06 13:48:08',NULL),(55,'I00047','cash',32232320.00,NULL,'2025-06-06 13:50:34',NULL),(56,'I00048','cash',1212121.00,NULL,'2025-06-06 13:51:58',NULL),(57,'I00049','cash',231230.00,NULL,'2025-06-06 13:52:47',NULL),(58,'I00050','cash',45353450.00,NULL,'2025-06-06 14:07:31',NULL),(59,'I00051','cash',900.00,NULL,'2025-06-06 15:38:16',NULL),(60,'I00052','cash',8000.00,NULL,'2025-06-06 15:41:59',NULL),(61,'I00053','cash',136000.00,NULL,'2025-06-06 15:59:55',NULL),(62,'I00054','cash',145000.00,NULL,'2025-06-06 16:02:17',NULL),(63,'I00055','cash',89090.00,NULL,'2025-06-06 16:10:04',NULL),(64,'I00056','cash',8900.00,NULL,'2025-06-06 16:10:38',NULL),(65,'I00057','loyalty_points',6.00,NULL,'2025-06-06 16:13:01',NULL),(66,'I00057','cash',98050.00,NULL,'2025-06-06 16:13:01',NULL),(67,'I00057','card',4000.00,'1334','2025-06-06 16:13:01',NULL),(68,'I00058','loyalty_points',1000.00,NULL,'2025-06-06 16:19:38',NULL),(69,'I00058','cash',229300.00,NULL,'2025-06-06 16:19:38',NULL),(70,'I00058','card',40000.00,'1431','2025-06-06 16:19:38',NULL),(71,'I00059','loyalty_points',1100.00,NULL,'2025-06-06 16:21:17',NULL),(72,'I00059','cash',14800.00,NULL,'2025-06-06 16:21:17',NULL),(73,'I00059','card',1500.00,'1431','2025-06-06 16:21:18',NULL),(74,'I00060','loyalty_points',1500.00,NULL,'2025-06-06 16:30:27',NULL),(75,'I00060','cash',203500.00,NULL,'2025-06-06 16:30:27',NULL),(76,'I00060','card',60000.00,'1234','2025-06-06 16:30:27',NULL),(77,'I00061','loyalty_points',400.00,NULL,'2025-06-06 16:58:24',NULL),(78,'I00061','cash',8250.00,NULL,'2025-06-06 16:58:24',NULL),(79,'I00062','loyalty_points',500.00,NULL,'2025-06-06 17:01:25',NULL),(80,'I00062','cash',8400.00,NULL,'2025-06-06 17:01:25',NULL),(81,'I00063','gift_card',1000.00,'WGC-901234','2025-06-06 17:05:56',NULL),(82,'I00063','gift_card',5000.00,'BGC-123456','2025-06-06 17:05:56',NULL),(83,'I00063','loyalty_points',500.00,'Loyalty Points Redemption','2025-06-06 17:05:56',NULL),(84,'I00063','cash',1900.00,'Cash Payment','2025-06-06 17:05:56',NULL),(85,'I00063','card',500.00,'1234','2025-06-06 17:05:56',NULL),(86,'I00063','card',500.00,'5678','2025-06-06 17:05:57',NULL),(87,'I00064','gift_card',1000.00,'WGC-901234','2025-06-06 17:11:13',NULL),(88,'I00064','gift_card',15000.00,'BGC-123456','2025-06-06 17:11:13',NULL),(89,'I00064','loyalty_points',700.00,'Loyalty Points Redemption','2025-06-06 17:11:14',NULL),(90,'I00064','cash',11700.00,'Cash Payment','2025-06-06 17:11:14',NULL),(91,'I00064','card',1000.00,'1234','2025-06-06 17:11:14',NULL),(92,'I00064','card',1000.00,'5678','2025-06-06 17:11:14',NULL),(93,'I00065','gift_card',1000.00,'WGC-901234','2025-06-06 17:13:15',NULL),(94,'I00065','loyalty_points',100.00,'Loyalty Points Redemption','2025-06-06 17:13:15',NULL),(95,'I00065','cash',5800.00,'Cash Payment','2025-06-06 17:13:15',NULL),(96,'I00065','card',2000.00,'1234','2025-06-06 17:13:15',NULL),(97,'I00066','gift_card',1000.00,'WGC-901234','2025-06-06 17:22:22',NULL),(98,'I00066','loyalty_points',1000.00,'Loyalty Points Redemption','2025-06-06 17:22:23',NULL),(99,'I00066','cash',5000.00,'Cash Payment','2025-06-06 17:22:23',NULL),(100,'I00066','card',1000.00,'1234','2025-06-06 17:22:23',NULL),(101,'I00066','card',1000.00,'5678','2025-06-06 17:22:23',NULL),(102,'I00067','gift_card',2000.00,'WGC-901234','2025-06-06 17:24:30',NULL),(103,'I00067','gift_card',1000.00,'AGC-789012','2025-06-06 17:24:31',NULL),(104,'I00067','loyalty_points',2000.00,'Loyalty Points Redemption','2025-06-06 17:24:31',NULL),(105,'I00067','cash',8900.00,'Cash Payment','2025-06-06 17:24:31',NULL),(106,'I00067','card',2000.00,'1234','2025-06-06 17:24:31',NULL),(107,'I00067','card',2000.00,'5678','2025-06-06 17:24:31',NULL),(108,'I00068','gift_card',3000.00,'WGC-901234','2025-06-06 17:27:22',NULL),(109,'I00068','gift_card',2000.00,'AGC-789012','2025-06-06 17:27:22',NULL),(110,'I00068','loyalty_points',3000.00,'Loyalty Points Redemption','2025-06-06 17:27:22',NULL),(111,'I00068','cash',12800.00,'Cash Payment','2025-06-06 17:27:23',NULL),(112,'I00068','card',3000.00,'1234','2025-06-06 17:27:23',NULL),(113,'I00068','card',3000.00,'5678','2025-06-06 17:27:23',NULL),(114,'I00069','gift_card',1000.00,'WGC-901234','2025-06-06 17:47:23',NULL),(115,'I00069','gift_card',1000.00,'AGC-789012','2025-06-06 17:47:24',NULL),(116,'I00069','loyalty_points',1000.00,'Loyalty Points Redemption','2025-06-06 17:47:24',NULL),(117,'I00069','cash',3900.00,'Cash Payment','2025-06-06 17:47:24',NULL),(118,'I00069','card',1000.00,'1234','2025-06-06 17:47:24',NULL),(119,'I00069','card',1000.00,'5678','2025-06-06 17:47:24',NULL),(120,'I00069','gift_card',1000.00,'WGC-901234','2025-06-06 17:47:28',NULL),(121,'I00069','gift_card',1000.00,'AGC-789012','2025-06-06 17:47:28',NULL),(122,'I00069','loyalty_points',1000.00,'Loyalty Points Redemption','2025-06-06 17:47:28',NULL),(123,'I00069','cash',3900.00,'Cash Payment','2025-06-06 17:47:28',NULL),(124,'I00069','card',1000.00,'1234','2025-06-06 17:47:28',NULL),(125,'I00069','card',1000.00,'5678','2025-06-06 17:47:29',NULL),(126,'I00070','gift_card',1000.00,'WGC-901234','2025-06-06 17:55:12',NULL),(127,'I00070','gift_card',1000.00,'AGC-789012','2025-06-06 17:55:12',NULL),(128,'I00070','loyalty_points',500.00,'Loyalty Points Redemption','2025-06-06 17:55:12',NULL),(129,'I00070','cash',4400.00,'Cash Payment','2025-06-06 17:55:12',NULL),(130,'I00070','card',1000.00,'1234','2025-06-06 17:55:12',NULL),(131,'I00070','card',1000.00,'5678','2025-06-06 17:55:13',NULL),(132,'I00070','gift_card',1000.00,'WGC-901234','2025-06-06 17:55:14',NULL),(133,'I00070','gift_card',1000.00,'AGC-789012','2025-06-06 17:55:15',NULL),(134,'I00070','loyalty_points',500.00,'Loyalty Points Redemption','2025-06-06 17:55:15',NULL),(135,'I00070','cash',4400.00,'Cash Payment','2025-06-06 17:55:15',NULL),(136,'I00070','card',1000.00,'1234','2025-06-06 17:55:16',NULL),(137,'I00070','card',1000.00,'5678','2025-06-06 17:55:16',NULL),(138,'I00071','gift_card',1000.00,'WGC-901234','2025-06-06 18:42:39',NULL),(139,'I00071','gift_card',1000.00,'AGC-789012','2025-06-06 18:42:39',NULL),(140,'I00071','loyalty_points',1000.00,'Loyalty Points Redemption','2025-06-06 18:42:39',NULL),(141,'I00071','cash',3900.00,'Cash Payment','2025-06-06 18:42:39',NULL),(142,'I00071','card',1000.00,'1234','2025-06-06 18:42:40',NULL),(143,'I00071','card',1000.00,'5678','2025-06-06 18:42:40',NULL),(144,'I00071','gift_card',1000.00,'WGC-901234','2025-06-06 18:42:41',NULL),(145,'I00071','gift_card',1000.00,'AGC-789012','2025-06-06 18:42:41',NULL),(146,'I00071','loyalty_points',1000.00,'Loyalty Points Redemption','2025-06-06 18:42:41',NULL),(147,'I00071','cash',3900.00,'Cash Payment','2025-06-06 18:42:41',NULL),(148,'I00071','card',1000.00,'1234','2025-06-06 18:42:41',NULL),(149,'I00071','card',1000.00,'5678','2025-06-06 18:42:41',NULL),(150,'I00072','gift_card',1000.00,'WGC-901234','2025-06-06 18:46:25',NULL),(151,'I00072','gift_card',1000.00,'AGC-789012','2025-06-06 18:46:25',NULL),(152,'I00072','loyalty_points',1000.00,'Loyalty Points Redemption','2025-06-06 18:46:25',NULL),(153,'I00072','cash',4000.00,'Cash Payment','2025-06-06 18:46:25',NULL),(154,'I00072','card',1000.00,'1234','2025-06-06 18:46:25',NULL),(155,'I00072','card',1000.00,'5678','2025-06-06 18:46:25',NULL),(156,'I00073','cash',9500.00,'Cash Payment','2025-06-06 18:51:37',NULL),(157,'I00074','cash',8800.00,'Cash Payment','2025-06-06 18:53:45',NULL),(158,'I00074','card',100.00,'1234','2025-06-06 18:53:45',NULL),(159,'I00075','cash',54000.00,'Cash Payment','2025-06-06 18:56:51',NULL),(160,'I00075','card',1000.00,'1234','2025-06-06 18:56:51',NULL),(161,'I00076','loyalty_points',100.00,'Loyalty Points Redemption','2025-06-06 18:58:01',NULL),(162,'I00076','cash',8000.00,'Cash Payment','2025-06-06 18:58:01',NULL),(163,'I00076','card',100.00,'1234','2025-06-06 18:58:01',NULL),(164,'I00076','card',729.00,'5678','2025-06-06 18:58:01',NULL),(165,'I00077','gift_card',5000.00,'BGC-123456','2025-06-06 19:31:00',NULL),(166,'I00077','loyalty_points',1000.00,'Loyalty Points Redemption','2025-06-06 19:31:00',NULL),(167,'I00077','cash',40000.00,'Cash Payment','2025-06-06 19:31:01',NULL),(168,'I00077','card',1000.00,'1234','2025-06-06 19:31:01',NULL),(169,'I00077','card',8000.00,'4567','2025-06-06 19:31:01',NULL);
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
INSERT INTO `product` VALUES ('P00006','Test 01',1,'','pack','','default-image.jpg','active','2025-06-04 20:51:34',NULL),('P001','Samsung Galaxy S21',1,'Latest Samsung smartphone','piece','12 months','/images/galaxy_s21.jpg','active','2025-05-23 18:13:18',NULL),('P002','iPhone 13 Pro',2,'Latest Apple smartphone','piece','12 months','/images/iphone13.jpg','active','2025-05-23 18:13:18',NULL),('P003','Sony 55\" LED TV',3,'4K Ultra HD Smart TV','piece','24 months','/images/sony_tv.jpg','inactive','2025-05-23 18:13:18',NULL),('P004','LG Refrigerator',4,'Double door refrigerator','piece','36 months','/images/lg_fridge.jpg','active','2025-05-23 18:13:18',NULL),('P005','Panasonic Microwave',5,'Convection Microwave Oven','piece','12 months','/images/panasonic_mw.jpg','active','2025-05-23 18:13:18',NULL);
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
INSERT INTO `stock` VALUES (1,'P001','1234567890123',97,80000,95000,'2023-01-01','2025-01-01','Rack A1',5,30,'no_discount',NULL,'2023-01-01 00:00:00','2023-01-01 00:00:00','stock@lexso.com','2025-05-23 18:13:18',NULL),(2,'P002','2345678901234',35,110000,135000,'2023-01-15','2025-01-15','Rack A2',3,30,'no_discount',NULL,'2023-01-01 00:00:00','2023-01-01 00:00:00','stock@lexso.com','2025-05-23 18:13:18',NULL),(3,'P003','3456789012345',91,8000,9500,'2023-02-01','2025-02-01','Rack B1',2,30,'percentage',5.00,'2023-06-01 00:00:00','2023-06-30 23:59:59','stock@lexso.com','2025-05-23 18:13:18',NULL),(4,'P004','4567890123456',18,45000,55000,'2023-02-15','2025-02-15','Rack B2',3,30,'fixed',5000.00,'2023-06-01 00:00:00','2023-06-30 23:59:59','stock@lexso.com','2025-05-23 18:13:18',NULL),(5,'P005','5678901234567',80,7500,8900,'2023-03-01','2025-03-01','Rack C1',5,30,'no_discount',NULL,'2023-01-01 00:00:00','2023-01-01 00:00:00','stock@lexso.com','2025-05-23 18:13:18',NULL);
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
INSERT INTO `user` VALUES ('accountant@lexso.com','hashed_password_5','Account','Manager','ACC001','accountant',5,'567890123V','Active','2025-05-23 23:43:18','/com/lexso/users/signatures/nullsignature.png','/com/lexso/users/profilepics/man.png','94745678901','0745678901','1998-05-05',25,2,NULL,NULL),('admin@lexso.com','Admin','Admin','User','ADM001','admin',1,'123456789V','Active','2025-05-23 23:43:18','/com/lexso/users/signatures/nullsignature.png','/com/lexso/users/profilepics/255_kreedx my image.jpeg','94711234567','0711234567','1990-01-01',33,1,NULL,NULL),('cashier@lexso.com','hashed_password_3','Cashier','User','CSH001','cashier',3,'345678901V','Active','2025-05-23 23:43:18','/com/lexso/users/signatures/nullsignature.png','/com/lexso/users/profilepics/man.png','94723456789','0723456789','1994-03-03',29,2,NULL,NULL),('manager@lexso.com','hashed_password_2','Manager','User','MGR001','manager',2,'234567890V','Active','2025-05-23 23:43:18','/com/lexso/users/signatures/nullsignature.png','/com/lexso/users/profilepics/man.png','94712345678','0712345678','1992-02-02',31,1,NULL,NULL),('stock@lexso.com','hashed_password_4','Stock','Manager','STK001','stockmgr',4,'456789012V','Active','2025-05-23 23:43:18','/com/lexso/users/signatures/nullsignature.png','/com/lexso/users/profilepics/man.png','94734567890','0734567890','1996-04-04',29,1,NULL,NULL);
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

-- Dump completed on 2025-06-07 23:56:10
