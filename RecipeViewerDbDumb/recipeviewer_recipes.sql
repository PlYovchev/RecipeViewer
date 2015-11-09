CREATE DATABASE  IF NOT EXISTS `recipeviewer` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `recipeviewer`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: localhost    Database: recipeviewer
-- ------------------------------------------------------
-- Server version	5.6.20-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `recipes`
--

DROP TABLE IF EXISTS `recipes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recipes` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Title` longtext,
  `DateRecipeAdded` datetime DEFAULT NULL,
  `Rating` int(11) NOT NULL,
  `ImageUrl` longtext,
  `AuthorId` varchar(128) DEFAULT NULL,
  `Description` longtext,
  PRIMARY KEY (`Id`),
  KEY `IX_AuthorId` (`AuthorId`) USING HASH,
  CONSTRAINT `FK_Recipes_AspNetUsers_AuthorId` FOREIGN KEY (`AuthorId`) REFERENCES `aspnetusers` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recipes`
--

LOCK TABLES `recipes` WRITE;
/*!40000 ALTER TABLE `recipes` DISABLE KEYS */;
INSERT INTO `recipes` VALUES (3,'Pancakes','2012-12-12 00:00:00',5,'/images/pancakes.jpg','eb0667f5-0eb4-4b6d-abc7-50e40932b6b4',NULL),(4,'Boiling Eggs','2011-05-11 00:00:00',3,'/images/boiling_eggs.jpg',NULL,NULL),(5,'Chicken Soup','2010-07-22 00:00:00',4,'/images/chicken_soup.jpg',NULL,NULL),(6,'Pizza','2014-11-08 00:00:00',5,'/images/pizza.jpg',NULL,'От тестото се разточват блатове с големина според вкуса. Всеки блат се намазва добре с доматеното пюре, настъргва се кашкавал, добавя се топеното сирене, синьото сирене и моцарелата. Поръсва се с босилек и риган на вкус, слагат се 6-7 обезкостени и нарязани на ситно маслини. Пече се в предварително загрята фурна за около 10 минути.');
/*!40000 ALTER TABLE `recipes` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-11-10  0:31:22
