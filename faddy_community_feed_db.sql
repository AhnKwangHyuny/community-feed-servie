-- MySQL dump 10.13  Distrib 9.2.0, for macos14.7 (arm64)
--
-- Host: localhost    Database: community-feed
-- ------------------------------------------------------
-- Server version	9.2.0

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
-- Table structure for table `community_commnent`
--

DROP TABLE IF EXISTS `community_commnent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `community_commnent` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `reg_dt` datetime(6) DEFAULT NULL,
  `upd_dt` datetime(6) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `like_count` int DEFAULT NULL,
  `author_id` bigint DEFAULT NULL,
  `post_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `community_commnent`
--

LOCK TABLES `community_commnent` WRITE;
/*!40000 ALTER TABLE `community_commnent` DISABLE KEYS */;
/*!40000 ALTER TABLE `community_commnent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `community_email_verification`
--

DROP TABLE IF EXISTS `community_email_verification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `community_email_verification` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `reg_dt` datetime(6) DEFAULT NULL,
  `upd_dt` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `is_verified` bit(1) NOT NULL,
  `token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `community_email_verification`
--

LOCK TABLES `community_email_verification` WRITE;
/*!40000 ALTER TABLE `community_email_verification` DISABLE KEYS */;
/*!40000 ALTER TABLE `community_email_verification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `community_like`
--

DROP TABLE IF EXISTS `community_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `community_like` (
  `target_id` bigint NOT NULL,
  `target_type` varchar(255) NOT NULL,
  `user_id` bigint NOT NULL,
  `reg_dt` datetime(6) DEFAULT NULL,
  `upd_dt` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`target_id`,`target_type`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `community_like`
--

LOCK TABLES `community_like` WRITE;
/*!40000 ALTER TABLE `community_like` DISABLE KEYS */;
/*!40000 ALTER TABLE `community_like` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `community_post`
--

DROP TABLE IF EXISTS `community_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `community_post` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `reg_dt` datetime(6) DEFAULT NULL,
  `upd_dt` datetime(6) DEFAULT NULL,
  `comment_counter` int NOT NULL DEFAULT '0',
  `content` varchar(255) DEFAULT NULL,
  `like_count` int DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `author_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `community_post`
--

LOCK TABLES `community_post` WRITE;
/*!40000 ALTER TABLE `community_post` DISABLE KEYS */;
/*!40000 ALTER TABLE `community_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `community_user`
--

DROP TABLE IF EXISTS `community_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `community_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `reg_dt` datetime(6) DEFAULT NULL,
  `upd_dt` datetime(6) DEFAULT NULL,
  `follower_count` int DEFAULT NULL,
  `following_count` int DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `profile_image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `community_user`
--

LOCK TABLES `community_user` WRITE;
/*!40000 ALTER TABLE `community_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `community_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `community_user_auth`
--

DROP TABLE IF EXISTS `community_user_auth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `community_user_auth` (
  `email` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `community_user_auth`
--

LOCK TABLES `community_user_auth` WRITE;
/*!40000 ALTER TABLE `community_user_auth` DISABLE KEYS */;
/*!40000 ALTER TABLE `community_user_auth` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `community_user_relation`
--

DROP TABLE IF EXISTS `community_user_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `community_user_relation` (
  `follower_user_id` bigint NOT NULL,
  `following_user_id` bigint NOT NULL,
  `reg_dt` datetime(6) DEFAULT NULL,
  `upd_dt` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`follower_user_id`,`following_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `community_user_relation`
--

LOCK TABLES `community_user_relation` WRITE;
/*!40000 ALTER TABLE `community_user_relation` DISABLE KEYS */;
/*!40000 ALTER TABLE `community_user_relation` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-17 23:13:45
