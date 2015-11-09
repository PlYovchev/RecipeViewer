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
-- Table structure for table `aspnetusers`
--

DROP TABLE IF EXISTS `aspnetusers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aspnetusers` (
  `Id` varchar(128) NOT NULL,
  `Email` varchar(256) DEFAULT NULL,
  `EmailConfirmed` tinyint(1) NOT NULL,
  `PasswordHash` varchar(128) NOT NULL,
  `SecurityStamp` longtext,
  `PhoneNumber` longtext,
  `PhoneNumberConfirmed` tinyint(1) NOT NULL,
  `TwoFactorEnabled` tinyint(1) NOT NULL,
  `LockoutEndDateUtc` datetime DEFAULT NULL,
  `LockoutEnabled` tinyint(1) NOT NULL,
  `AccessFailedCount` int(11) NOT NULL,
  `UserName` varchar(128) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `UserNameIndex` (`UserName`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aspnetusers`
--

LOCK TABLES `aspnetusers` WRITE;
/*!40000 ALTER TABLE `aspnetusers` DISABLE KEYS */;
INSERT INTO `aspnetusers` VALUES ('1143da7f-49e4-4bc0-8b16-0d6ab5eefc9e','plm@plm.pl',0,'AHd8dGqymHBxv8wjlHBVaR9Zg3jvVz6zdhuBHzQFKZHAdZvco8Wkx6XEn462265sLw==','e963be54-3276-492f-ac5a-cade6efad3a9',NULL,0,0,NULL,0,0,'plm@plm.pl'),('4a03fc50-5a2f-4e6b-89dc-2ebf47934b5e','plref@abv.bg',0,'AKDk5+MmF8Y8sdN7wNrbp81Uc2M0AO5TSwR7HJJ7vgjm8z6xqarGTChiGbusG/Cphg==','e0dd2656-8958-4fa6-aaed-0da37932e112',NULL,0,0,NULL,0,0,'plref@abv.bg'),('69a2af5e-cbe2-4c0a-9353-15aec20eba14','test@test.te',0,'AH0nT8fQsbdYKL7oJ9zGtEFZiuDW0+KWwZhS85fTKFdGaE6Zi+uDu+0uBbyo5zZ3Gw==','e33127d3-a5ee-4520-b746-33e1ab4cb40b',NULL,0,0,NULL,0,0,'test@test.te'),('87291df2-415b-4d5c-8b6d-13652d9210ea','pll@pll.pl',0,'APfKCk19wouYKdg48t2ifIf87ll+WvvoOyJk6cXsmb3zy4fPqsUYBciXnGMokh29Mw==','7bef8518-0df5-442e-8ffb-e9c2a42d5687',NULL,0,0,NULL,0,0,'pll@pll.pl'),('a1262292-6d3d-430f-858c-acc193a5c340','pl@gmail.com',0,'AG4tLw6kmkRS3sSmsj9o2Ovzj9EiINupZlTABXxRK7gFSi8M0WuhwxbfJsnQIEJLhw==','92d02723-3307-4675-8287-8f490cfd434a',NULL,0,0,NULL,0,0,'pl@gmail.com'),('bad909b0-24f5-48fe-9dad-5307992c5f80','pllll@pll.pl',0,'AD7nuY0T9T+rsnejnI54k+dTWPpun2iKZwpVe2j3sYmsiHSeQkg+IZLNZKYJ3iucJg==','63521d6b-d1b5-4244-ac17-b1426d3f49f5',NULL,0,0,NULL,0,0,'pllll@pll.pl'),('eb0667f5-0eb4-4b6d-abc7-50e40932b6b4','pl@pl.pl',0,'AFqZrME0NKM/n1Rh0prMdmLaIVBWVMUTqH/axo2QyTZ7AX8lxn4RYLV0PnQ8WFxOJg==','35de4098-2482-4a17-99f1-e828461c8b7c',NULL,0,0,NULL,0,0,'pl@pl.pl');
/*!40000 ALTER TABLE `aspnetusers` ENABLE KEYS */;
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
