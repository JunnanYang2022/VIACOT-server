/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : 124.223.174.137:3306
 Source Schema         : lineage

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 02/04/2022 21:17:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cell_annotation
-- ----------------------------
DROP TABLE IF EXISTS `cell_annotation`;
CREATE TABLE `cell_annotation`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `node_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '节点id',
  `cell_discription` varchar(1000) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '细胞注释',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1474 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_genes_expression
-- ----------------------------
DROP TABLE IF EXISTS `tb_genes_expression`;
CREATE TABLE `tb_genes_expression`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `node_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `expression_or_blot` mediumint(0) DEFAULT NULL,
  `gene_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `tree_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tree_id_genename`(`tree_id`, `gene_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45601938 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_lineage_tree
-- ----------------------------
DROP TABLE IF EXISTS `tb_lineage_tree`;
CREATE TABLE `tb_lineage_tree`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `node_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '节点id',
  `node_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '节点名称',
  `ancestor_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '父节点id',
  `child_id` varchar(2000) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '子节点id，多个子节点id用逗号分隔',
  `species` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '细胞来源和数据序号',
  `generation` bigint(0) DEFAULT NULL COMMENT '代 最顶级节点只能有一条数据，代数为1',
  `tree_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '树id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx`(`tree_id`, `generation`) USING BTREE,
  INDEX `unque_idx`(`tree_id`, `node_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33861 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_tree
-- ----------------------------
DROP TABLE IF EXISTS `tb_tree`;
CREATE TABLE `tb_tree`  (
  `tree_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '树id',
  `tree_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '树名称',
  `species` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '物种名',
  `latin_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '物种拉丁名称',
  `paper` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '文献',
  `eoa` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'Experimental Or Algorithmic Method',
  PRIMARY KEY (`tree_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
