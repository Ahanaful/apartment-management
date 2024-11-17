-- MySQL dump 10.13  Distrib 9.1.0, for macos14 (arm64)
--
-- Host: localhost    Database: Apartment_management
-- ------------------------------------------------------
-- Server version	9.1.0

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
-- Current Database: `Apartment_management`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `Apartment_management` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `Apartment_management`;

--
-- Table structure for table `lockout`
--

DROP TABLE IF EXISTS `lockout`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lockout` (
  `LockoutID` int NOT NULL AUTO_INCREMENT,
  `ResidentID` int NOT NULL,
  `LockoutCount` int DEFAULT '0',
  PRIMARY KEY (`LockoutID`),
  KEY `ResidentID` (`ResidentID`),
  CONSTRAINT `lockout_ibfk_1` FOREIGN KEY (`ResidentID`) REFERENCES `residents` (`ResidentID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lockout`
--

LOCK TABLES `lockout` WRITE;
/*!40000 ALTER TABLE `lockout` DISABLE KEYS */;
INSERT INTO `lockout` VALUES (1,1,0),(2,2,0),(3,3,0),(4,4,0),(5,5,0),(6,6,0),(7,7,0),(8,8,0),(9,9,0),(10,10,0),(11,11,0),(12,12,0),(13,13,0),(14,14,0);
/*!40000 ALTER TABLE `lockout` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `packages`
--

DROP TABLE IF EXISTS `packages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `packages` (
  `PackageID` int NOT NULL AUTO_INCREMENT,
  `ResidentID` int NOT NULL,
  `TrackingNumber` varchar(100) NOT NULL,
  `Delivered` enum('YES','NO') DEFAULT 'NO',
  PRIMARY KEY (`PackageID`),
  UNIQUE KEY `TrackingNumber` (`TrackingNumber`),
  KEY `ResidentID` (`ResidentID`),
  CONSTRAINT `packages_ibfk_1` FOREIGN KEY (`ResidentID`) REFERENCES `residents` (`ResidentID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `packages`
--

LOCK TABLES `packages` WRITE;
/*!40000 ALTER TABLE `packages` DISABLE KEYS */;
INSERT INTO `packages` VALUES (1,12,'PKG-00001','NO');
/*!40000 ALTER TABLE `packages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `residents`
--

DROP TABLE IF EXISTS `residents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `residents` (
  `ResidentID` int NOT NULL AUTO_INCREMENT,
  `FirstName` text,
  `LastName` text,
  `RoomNumber` int DEFAULT NULL,
  `TempID` int DEFAULT NULL,
  PRIMARY KEY (`ResidentID`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `residents`
--

LOCK TABLES `residents` WRITE;
/*!40000 ALTER TABLE `residents` DISABLE KEYS */;
INSERT INTO `residents` VALUES (1,'Alice','Johnson',101,NULL),(2,'Bob','Smith',102,NULL),(3,'Charlie','Davis',103,NULL),(4,'Diana','Moore',104,NULL),(5,'Ehsan','Ahmed ',142,NULL),(6,'Shehran Salam','Salam',143,NULL),(7,'Ahanaf','Alam',150,NULL),(8,'Edward','Wilson',201,NULL),(9,'Fiona','Brown',202,NULL),(10,'George','White',203,NULL),(11,'Hannah','Taylor',204,NULL),(12,'Suzana','Ahmed',250,NULL),(13,'Ivy','Anderson',301,NULL),(14,'Jack','King',302,NULL);
/*!40000 ALTER TABLE `residents` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `temp_cards`
--

DROP TABLE IF EXISTS `temp_cards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `temp_cards` (
  `TempCardID` int NOT NULL AUTO_INCREMENT,
  `ResidentID` int NOT NULL,
  `HasTempCard` enum('YES','NO') DEFAULT 'NO',
  PRIMARY KEY (`TempCardID`),
  KEY `ResidentID` (`ResidentID`),
  CONSTRAINT `temp_cards_ibfk_1` FOREIGN KEY (`ResidentID`) REFERENCES `residents` (`ResidentID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `temp_cards`
--

LOCK TABLES `temp_cards` WRITE;
/*!40000 ALTER TABLE `temp_cards` DISABLE KEYS */;
INSERT INTO `temp_cards` VALUES (1,1,'NO'),(2,2,'NO'),(3,3,'NO'),(4,4,'NO'),(5,5,'NO'),(6,6,'NO'),(7,7,'NO'),(8,8,'NO'),(9,9,'NO'),(10,10,'NO'),(11,11,'NO'),(12,12,'NO'),(13,13,'NO'),(14,14,'NO');
/*!40000 ALTER TABLE `temp_cards` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-16 21:28:22
