-- MySQL dump 10.13  Distrib 8.0.40, for macos14 (arm64)
--
-- Host: 127.0.0.1    Database: sakssystem
-- ------------------------------------------------------
-- Server version	9.3.0

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
-- Table structure for table `bruker`
--

DROP TABLE IF EXISTS `bruker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bruker` (
  `id` int NOT NULL AUTO_INCREMENT,
  `brukernavn` varchar(50) NOT NULL,
  `rolle` enum('TESTER','UTVIKLER','LEDER') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bruker`
--

LOCK TABLES `bruker` WRITE;
/*!40000 ALTER TABLE `bruker` DISABLE KEYS */;
INSERT INTO `bruker` VALUES (1,'Vibeke','TESTER'),(2,'Ranem','UTVIKLER'),(3,'Sara','LEDER');
/*!40000 ALTER TABLE `bruker` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kategori`
--

DROP TABLE IF EXISTS `kategori`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kategori` (
  `id` int NOT NULL AUTO_INCREMENT,
  `navn` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kategori`
--

LOCK TABLES `kategori` WRITE;
/*!40000 ALTER TABLE `kategori` DISABLE KEYS */;
INSERT INTO `kategori` VALUES (1,'UI-feil'),(2,'Backend-feil'),(3,'Funksjonsforespørsel');
/*!40000 ALTER TABLE `kategori` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prioritet`
--

DROP TABLE IF EXISTS `prioritet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prioritet` (
  `id` int NOT NULL AUTO_INCREMENT,
  `navn` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prioritet`
--

LOCK TABLES `prioritet` WRITE;
/*!40000 ALTER TABLE `prioritet` DISABLE KEYS */;
INSERT INTO `prioritet` VALUES (1,'Lav'),(2,'Middels'),(3,'Høy');
/*!40000 ALTER TABLE `prioritet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sak`
--

DROP TABLE IF EXISTS `sak`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sak` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tittel` varchar(100) DEFAULT NULL,
  `beskrivelse` text,
  `prioritet_id` int DEFAULT NULL,
  `kategori_id` int DEFAULT NULL,
  `status_id` int DEFAULT NULL,
  `reporter_id` int DEFAULT NULL,
  `mottaker_id` int DEFAULT NULL,
  `opprettetTid` datetime DEFAULT NULL,
  `oppdatertTid` datetime DEFAULT NULL,
  `utviklerkommentar` text,
  `testerTilbakemelding` text,
  PRIMARY KEY (`id`),
  KEY `prioritet_id` (`prioritet_id`),
  KEY `kategori_id` (`kategori_id`),
  KEY `status_id` (`status_id`),
  KEY `reporter_id` (`reporter_id`),
  KEY `mottaker_id` (`mottaker_id`),
  CONSTRAINT `sak_ibfk_1` FOREIGN KEY (`prioritet_id`) REFERENCES `prioritet` (`id`),
  CONSTRAINT `sak_ibfk_2` FOREIGN KEY (`kategori_id`) REFERENCES `kategori` (`id`),
  CONSTRAINT `sak_ibfk_3` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`),
  CONSTRAINT `sak_ibfk_4` FOREIGN KEY (`reporter_id`) REFERENCES `bruker` (`id`),
  CONSTRAINT `sak_ibfk_5` FOREIGN KEY (`mottaker_id`) REFERENCES `bruker` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sak`
--

LOCK TABLES `sak` WRITE;
/*!40000 ALTER TABLE `sak` DISABLE KEYS */;
/*!40000 ALTER TABLE `sak` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `kode` varchar(20) DEFAULT NULL,
  `etikett` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` VALUES (1,'SUBMITTED','Innsendt'),(2,'ASSIGNED','Tildelt'),(3,'IN_PROGRESS','Pågår'),(4,'FIXED','Rettet'),(5,'RESOLVED','Løst'),(6,'TEST_FAILED','Test mislyktes'),(7,'CLOSED','Lukket');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-20 14:55:35
