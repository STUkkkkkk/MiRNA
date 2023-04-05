/*
 Navicat Premium Data Transfer

 Source Server         : Mysql8
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3307
 Source Schema         : Mirna

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 05/04/2023 10:57:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gene_mirna
-- ----------------------------
DROP TABLE IF EXISTS `gene_mirna`;
CREATE TABLE `gene_mirna`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `gene` varchar(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '只因的名字',
  `mirna_name` varchar(110) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'mirna名字',
  `publication` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '出版物',
  `methods` varchar(110) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '方式',
  `tissue` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组织',
  `cell_line` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '细胞系',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `mirna_name`(`mirna_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 596609 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mirna_struct
-- ----------------------------
DROP TABLE IF EXISTS `mirna_struct`;
CREATE TABLE `mirna_struct`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `mirna_name` varchar(33) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'mirna名字',
  `first` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '第一层',
  `second` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '第二层',
  `third` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '第三层',
  `fourth` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '第四层',
  `fifth` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '第五层',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `mirna_name`(`mirna_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 68412 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for prediction
-- ----------------------------
DROP TABLE IF EXISTS `prediction`;
CREATE TABLE `prediction`  (
  `mirna_id` int(0) NULL DEFAULT NULL,
  `disease_id` int(0) NULL DEFAULT NULL,
  `proved` tinyint(0) NULL DEFAULT NULL,
  `forecast_relevance` double NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for record
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `search_name` varchar(110) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `times` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `search_name`(`search_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for relationship
-- ----------------------------
DROP TABLE IF EXISTS `relationship`;
CREATE TABLE `relationship`  (
  `mirna_name` varchar(155) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pmid` int(0) NULL DEFAULT NULL,
  `disease_name` varchar(155) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for relationship_disease
-- ----------------------------
DROP TABLE IF EXISTS `relationship_disease`;
CREATE TABLE `relationship_disease`  (
  `pmid` int(0) NOT NULL,
  `disease_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for relationship_mirna
-- ----------------------------
DROP TABLE IF EXISTS `relationship_mirna`;
CREATE TABLE `relationship_mirna`  (
  `mirna_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pmid` int(0) NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for search_mirna
-- ----------------------------
DROP TABLE IF EXISTS `search_mirna`;
CREATE TABLE `search_mirna`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `mirna_name` varchar(110) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `mirna_name`(`mirna_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1067 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_article
-- ----------------------------
DROP TABLE IF EXISTS `tb_article`;
CREATE TABLE `tb_article`  (
  `pmid` int(0) NOT NULL,
  `title` varchar(2255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `type` varchar(2255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `authors` varchar(2255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `doi` varchar(2255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `keywords` varchar(2255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `library` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `abs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `date` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`pmid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_disease
-- ----------------------------
DROP TABLE IF EXISTS `tb_disease`;
CREATE TABLE `tb_disease`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `disease_name` varchar(155) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1203 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_message
-- ----------------------------
DROP TABLE IF EXISTS `tb_message`;
CREATE TABLE `tb_message`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(110) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `email` varchar(110) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户的邮箱',
  `message` blob NULL COMMENT '留言信息',
  `create_time` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '联系我们的信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_mirna
-- ----------------------------
DROP TABLE IF EXISTS `tb_mirna`;
CREATE TABLE `tb_mirna`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `mirna_name` varchar(155) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1208 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_prediction
-- ----------------------------
DROP TABLE IF EXISTS `tb_prediction`;
CREATE TABLE `tb_prediction`  (
  `mirna` varchar(110) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `disease` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `proved` tinyint(0) NULL DEFAULT NULL,
  `forecast_relevance` double NULL DEFAULT NULL,
  INDEX `index_name`(`mirna`, `disease`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_relationship
-- ----------------------------
DROP TABLE IF EXISTS `tb_relationship`;
CREATE TABLE `tb_relationship`  (
  `mirna_id` int(0) NULL DEFAULT NULL,
  `disease_id` int(0) NULL DEFAULT NULL,
  `pmid` int(0) NULL DEFAULT NULL,
  INDEX `mirna_id`(`mirna_id`) USING BTREE,
  INDEX `disease_id`(`disease_id`) USING BTREE,
  INDEX `pmid`(`pmid`) USING BTREE,
  CONSTRAINT `tb_relationship_ibfk_1` FOREIGN KEY (`mirna_id`) REFERENCES `tb_mirna` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `tb_relationship_ibfk_2` FOREIGN KEY (`disease_id`) REFERENCES `tb_disease` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `tb_relationship_ibfk_3` FOREIGN KEY (`pmid`) REFERENCES `tb_article` (`pmid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- View structure for kkk
-- ----------------------------
DROP VIEW IF EXISTS `kkk`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `kkk` AS select distinct `gene_mirna`.`mirna_name` AS `mirna_name` from `gene_mirna`;

-- ----------------------------
-- View structure for ok
-- ----------------------------
DROP VIEW IF EXISTS `ok`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `ok` AS select distinct `gene_mirna`.`mirna_name` AS `mirna_name` from `gene_mirna`;

-- ----------------------------
-- View structure for rela
-- ----------------------------
DROP VIEW IF EXISTS `rela`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `rela` AS select distinct `relationship`.`mirna_name` AS `mirna_name`,`relationship`.`pmid` AS `pmid`,`relationship`.`disease_name` AS `disease_name` from `relationship`;

-- ----------------------------
-- View structure for stu
-- ----------------------------
DROP VIEW IF EXISTS `stu`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `stu` AS select `tb_prediction`.`mirna` AS `mirna`,`tb_prediction`.`disease` AS `disease`,`tb_prediction`.`proved` AS `proved`,`tb_prediction`.`forecast_relevance` AS `forecast_relevance` from `tb_prediction` where (`tb_prediction`.`disease` like '%,%');

SET FOREIGN_KEY_CHECKS = 1;
