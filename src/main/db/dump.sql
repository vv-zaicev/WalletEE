CREATE DATABASE  IF NOT EXISTS `wallets` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `wallets`;
-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: localhost    Database: wallets
-- ------------------------------------------------------
-- Server version	8.0.35

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `transactioncategory`
--

DROP TABLE IF EXISTS `TransactionCategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TransactionCategory` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `TransactionTypeId` int DEFAULT NULL,
  `TransactionCategoryName` tinytext,
  PRIMARY KEY (`Id`),
  KEY `TransactionTypeId` (`TransactionTypeId`),
  CONSTRAINT `TransactionCategory_ibfk_1` FOREIGN KEY (`TransactionTypeId`) REFERENCES `TransactionTypes` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactioncategory`
--

LOCK TABLES `TransactionCategory` WRITE;
/*!40000 ALTER TABLE `TransactionCategory` DISABLE KEYS */;
INSERT INTO `TransactionCategory` VALUES (1,1,'Зарплата'),(2,1,'Дивиденды'),(3,1,'Кэшбэк'),(4,2,'Продукты'),(5,2,'Транспорт'),(6,2,'Услуги'),(7,2,'Покупки');
/*!40000 ALTER TABLE `TransactionCategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `Transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Transactions` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `Description` tinytext,
  `Sum` decimal(20,2) DEFAULT NULL,
  `Date` date DEFAULT NULL,
  `TransactionTypeId` int DEFAULT NULL,
  `WalletId` int DEFAULT NULL,
  `TransactionCategoryId` int DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `TransactionTypeId` (`TransactionTypeId`),
  KEY `WalletId` (`WalletId`),
  KEY `TransactionCategoryId` (`TransactionCategoryId`),
  CONSTRAINT `Transactions_ibfk_1` FOREIGN KEY (`TransactionTypeId`) REFERENCES `TransactionTypes` (`Id`),
  CONSTRAINT `Transactions_ibfk_2` FOREIGN KEY (`WalletId`) REFERENCES `Wallets` (`Id`),
  CONSTRAINT `Transactions_ibfk_3` FOREIGN KEY (`TransactionCategoryId`) REFERENCES `TransactionCategory` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactions`
--

LOCK TABLES `Transactions` WRITE;
/*!40000 ALTER TABLE `Transactions` DISABLE KEYS */;
/*!40000 ALTER TABLE `Transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transactiontypes`
--

DROP TABLE IF EXISTS `TransactionTypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TransactionTypes` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `TransactionTypeName` tinytext,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactiontypes`
--

LOCK TABLES `TransactionTypes` WRITE;
/*!40000 ALTER TABLE `TransactionTypes` DISABLE KEYS */;
INSERT INTO `TransactionTypes` VALUES (1,'Income'),(2,'Expenses');
/*!40000 ALTER TABLE `TransactionTypes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wallets`
--

DROP TABLE IF EXISTS `Wallets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Wallets` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `WalletName` tinytext,
  `Balance` decimal(20,2) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wallets`
--

LOCK TABLES `Wallets` WRITE;
/*!40000 ALTER TABLE `Wallets` DISABLE KEYS */;
INSERT INTO `Wallets` VALUES (1,'Основной кошелек',500.00),(2,'Запосной счет',380.00);
/*!40000 ALTER TABLE `Wallets` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-02-01 21:17:17
