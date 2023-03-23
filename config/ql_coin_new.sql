/*
 Navicat Premium Data Transfer

 Source Server         : 120.79.79.176
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : 120.79.79.176:3306
 Source Schema         : ql_coin

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 20/12/2020 10:03:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `table_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '表描述',
  `class_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `package_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生成功能作者',
  `options` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代码生成业务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table
-- ----------------------------
INSERT INTO `gen_table` VALUES (35, 't_deposit_history', '用户充币记录', 'TDepositHistory', 'crud', 'com.ruoyi.qlcoin', 'qlcoin', 'deposit_history', 'deposit_history', 'ruoyi', '{\"treeName\":\"\",\"treeParentCode\":\"\",\"treeCode\":\"\"}', 'admin', '2020-07-24 16:39:30', '', '2020-07-24 17:21:05', '');
INSERT INTO `gen_table` VALUES (36, 't_withdraw_history', '用户提币记录', 'TWithdrawHistory', 'crud', 'com.ruoyi.qlcoin', 'qlcoin', 'withdrawHistory', 'TWithdrawHistory', 'ruoyi', '{\"treeName\":\"\",\"treeParentCode\":\"\",\"treeCode\":\"\"}', 'admin', '2020-07-24 16:39:31', '', '2020-07-24 17:05:57', '');
INSERT INTO `gen_table` VALUES (37, 't_wallet_pool', '用户币地址', 'TWalletPool', 'crud', 'com.ruoyi.qlcoin', 'qlcoin', 'wallepool', 'walletpool', 'ruoyi', '{\"treeName\":\"\",\"treeParentCode\":\"\",\"treeCode\":\"\"}', 'admin', '2020-07-28 09:43:20', '', '2020-07-28 09:46:53', '');
INSERT INTO `gen_table` VALUES (38, 't_sys_config', 'TSysconfig', 'TSysConfig', 'crud', 'com.ruoyi.qlcoin', 'qlcoin', 'sysconfig', 'sysconfig', 'ruoyi', '{\"treeName\":\"\",\"treeParentCode\":\"\",\"treeCode\":\"\"}', 'admin', '2020-07-29 19:18:35', '', '2020-07-29 19:21:42', '');
INSERT INTO `gen_table` VALUES (39, 't_share_img', 'TShareImg', 'TShareImg', 'crud', 'com.ruoyi.qlcoin', 'qlcoin', 'TShareImg', 'TShareImg', 'ruoyi', '{\"treeName\":\"\",\"treeParentCode\":\"\",\"treeCode\":\"\"}', 'admin', '2020-07-31 11:41:47', '', '2020-07-31 11:46:51', '');
INSERT INTO `gen_table` VALUES (40, 't_member', 'member', 'TMember', 'tree', 'com.ruoyi.qlcoin', 'qlcoin', 'member', 'member', 'ruoyi', '{\"treeName\":\"phone\",\"treeParentCode\":\"id\",\"treeCode\":\"wel_member\"}', 'admin', '2020-07-31 21:25:48', '', '2020-07-31 21:33:58', '');

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`  (
  `column_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 497 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代码生成业务表字段' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------
INSERT INTO `gen_table_column` VALUES (414, '35', 'id', '', 'varchar(50)', 'String', 'id', '1', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2020-07-24 16:39:30', NULL, '2020-07-24 17:21:05');
INSERT INTO `gen_table_column` VALUES (415, '35', 'member', '用户手机', 'varchar(50)', 'String', 'member', '0', '0', NULL, NULL, NULL, '1', '1', 'EQ', 'input', '', 2, 'admin', '2020-07-24 16:39:30', NULL, '2020-07-24 17:21:05');
INSERT INTO `gen_table_column` VALUES (416, '35', 'block_number', '区块高度', 'varchar(255)', 'String', 'blockNumber', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 3, 'admin', '2020-07-24 16:39:30', NULL, '2020-07-24 17:21:06');
INSERT INTO `gen_table_column` VALUES (417, '35', 'tx_hash', '交易hash', 'varchar(255)', 'String', 'txHash', '0', '0', NULL, NULL, NULL, '1', '1', 'EQ', 'input', '', 4, 'admin', '2020-07-24 16:39:30', NULL, '2020-07-24 17:21:06');
INSERT INTO `gen_table_column` VALUES (418, '35', 'contract', '合约地址', 'varchar(255)', 'String', 'contract', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 5, 'admin', '2020-07-24 16:39:30', NULL, '2020-07-24 17:21:06');
INSERT INTO `gen_table_column` VALUES (419, '35', 'from_address', 'FROM', 'varchar(255)', 'String', 'fromAddress', '0', '0', NULL, NULL, NULL, '1', '1', 'EQ', 'input', '', 6, 'admin', '2020-07-24 16:39:30', NULL, '2020-07-24 17:21:06');
INSERT INTO `gen_table_column` VALUES (420, '35', 'to_address', 'TO', 'varchar(255)', 'String', 'toAddress', '0', '0', NULL, NULL, NULL, '1', '1', 'EQ', 'input', '', 7, 'admin', '2020-07-24 16:39:30', NULL, '2020-07-24 17:21:06');
INSERT INTO `gen_table_column` VALUES (421, '35', 'coin', '币种', 'varchar(255)', 'String', 'coin', '0', '0', NULL, NULL, NULL, '1', '1', 'EQ', 'input', '', 8, 'admin', '2020-07-24 16:39:30', NULL, '2020-07-24 17:21:06');
INSERT INTO `gen_table_column` VALUES (422, '35', 'amount', '数量', 'varchar(255)', 'String', 'amount', '0', '0', NULL, NULL, NULL, '1', '1', 'EQ', 'input', '', 9, 'admin', '2020-07-24 16:39:31', NULL, '2020-07-24 17:21:06');
INSERT INTO `gen_table_column` VALUES (423, '35', 'create_time', '时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, NULL, NULL, '1', '1', 'EQ', 'datetime', '', 10, 'admin', '2020-07-24 16:39:31', NULL, '2020-07-24 17:21:06');
INSERT INTO `gen_table_column` VALUES (424, '36', 'id', 'null', 'varchar(50)', 'String', 'id', '1', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2020-07-24 16:39:31', NULL, '2020-07-24 17:05:57');
INSERT INTO `gen_table_column` VALUES (425, '36', 'member', '用户ID', 'varchar(50)', 'String', 'member', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2020-07-24 16:39:31', NULL, '2020-07-24 17:05:57');
INSERT INTO `gen_table_column` VALUES (426, '36', 'block_number', '区块高度', 'varchar(255)', 'String', 'blockNumber', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2020-07-24 16:39:31', NULL, '2020-07-24 17:05:57');
INSERT INTO `gen_table_column` VALUES (427, '36', 'tx_hash', '交易hash', 'varchar(255)', 'String', 'txHash', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2020-07-24 16:39:31', NULL, '2020-07-24 17:05:57');
INSERT INTO `gen_table_column` VALUES (428, '36', 'contract', '合约地址', 'varchar(255)', 'String', 'contract', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2020-07-24 16:39:31', NULL, '2020-07-24 17:05:57');
INSERT INTO `gen_table_column` VALUES (429, '36', 'from_address', '转出地址', 'varchar(255)', 'String', 'fromAddress', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2020-07-24 16:39:31', NULL, '2020-07-24 17:05:57');
INSERT INTO `gen_table_column` VALUES (430, '36', 'status', '状态', 'varchar(255)', 'String', 'status', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'radio', 'withdraw_status', 7, 'admin', '2020-07-24 16:39:31', NULL, '2020-07-24 17:05:57');
INSERT INTO `gen_table_column` VALUES (431, '36', 'to_address', '收币地址', 'varchar(255)', 'String', 'toAddress', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 8, 'admin', '2020-07-24 16:39:31', NULL, '2020-07-24 17:05:57');
INSERT INTO `gen_table_column` VALUES (432, '36', 'coin', '币名', 'varchar(255)', 'String', 'coin', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 9, 'admin', '2020-07-24 16:39:31', NULL, '2020-07-24 17:05:57');
INSERT INTO `gen_table_column` VALUES (433, '36', 'amount', '数量', 'varchar(255)', 'String', 'amount', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 10, 'admin', '2020-07-24 16:39:31', NULL, '2020-07-24 17:05:57');
INSERT INTO `gen_table_column` VALUES (434, '36', 'create_time', '时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 11, 'admin', '2020-07-24 16:39:31', NULL, '2020-07-24 17:05:57');
INSERT INTO `gen_table_column` VALUES (435, '37', 'id', '密码', 'varchar(50)', 'String', 'id', '0', '0', NULL, NULL, NULL, '1', NULL, 'EQ', 'input', '', 1, 'admin', '2020-07-28 09:43:21', NULL, '2020-07-28 09:46:53');
INSERT INTO `gen_table_column` VALUES (436, '37', 'address', 'ETH地址', 'varchar(200)', 'String', 'address', '0', '0', NULL, NULL, NULL, '1', '1', 'EQ', 'input', '', 2, 'admin', '2020-07-28 09:43:21', NULL, '2020-07-28 09:46:53');
INSERT INTO `gen_table_column` VALUES (437, '37', 'password', '密码', 'varchar(255)', 'String', 'password', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 3, 'admin', '2020-07-28 09:43:21', NULL, '2020-07-28 09:46:54');
INSERT INTO `gen_table_column` VALUES (438, '37', 'member', '用户', 'varchar(32)', 'String', 'member', '0', '0', NULL, NULL, NULL, '1', '1', 'EQ', 'input', '', 4, 'admin', '2020-07-28 09:43:21', NULL, '2020-07-28 09:46:54');
INSERT INTO `gen_table_column` VALUES (439, '37', 'coin', 'null', 'varchar(50)', 'String', 'coin', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 5, 'admin', '2020-07-28 09:43:21', NULL, '2020-07-28 09:46:54');
INSERT INTO `gen_table_column` VALUES (440, '37', 'usdt_balance', 'null', 'decimal(11,0)', 'Long', 'usdtBalance', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 6, 'admin', '2020-07-28 09:43:21', NULL, '2020-07-28 09:46:54');
INSERT INTO `gen_table_column` VALUES (441, '37', 'eth_balance', 'null', 'decimal(11,0)', 'Long', 'ethBalance', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 7, 'admin', '2020-07-28 09:43:21', NULL, '2020-07-28 09:46:54');
INSERT INTO `gen_table_column` VALUES (442, '37', 'create_time', 'null', 'datetime', 'Date', 'createTime', '0', '0', NULL, NULL, NULL, '1', NULL, 'EQ', 'datetime', '', 8, 'admin', '2020-07-28 09:43:21', NULL, '2020-07-28 09:46:54');
INSERT INTO `gen_table_column` VALUES (443, '38', 'id', 'null', 'varchar(50)', 'String', 'id', '1', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2020-07-29 19:18:35', NULL, '2020-07-29 19:21:42');
INSERT INTO `gen_table_column` VALUES (444, '38', 'param_name', '参数名', 'varchar(255)', 'String', 'paramName', '0', '0', NULL, '1', '1', '1', '1', 'LIKE', 'input', '', 2, 'admin', '2020-07-29 19:18:35', NULL, '2020-07-29 19:21:43');
INSERT INTO `gen_table_column` VALUES (445, '38', 'param_key', '健', 'varchar(255)', 'String', 'paramKey', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2020-07-29 19:18:35', NULL, '2020-07-29 19:21:43');
INSERT INTO `gen_table_column` VALUES (446, '38', 'param_value', '值', 'varchar(255)', 'String', 'paramValue', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2020-07-29 19:18:35', NULL, '2020-07-29 19:21:43');
INSERT INTO `gen_table_column` VALUES (447, '38', 'commit', '备注', 'varchar(255)', 'String', 'commit', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2020-07-29 19:18:35', NULL, '2020-07-29 19:21:43');
INSERT INTO `gen_table_column` VALUES (448, '38', 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'datetime', '', 6, 'admin', '2020-07-29 19:18:35', NULL, '2020-07-29 19:21:43');
INSERT INTO `gen_table_column` VALUES (449, '38', 'update_time', '修改时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, NULL, '1', NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2020-07-29 19:18:36', NULL, '2020-07-29 19:21:43');
INSERT INTO `gen_table_column` VALUES (450, '39', 'id', 'null', 'varchar(50)', 'String', 'id', '1', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2020-07-31 11:41:47', NULL, '2020-07-31 11:46:51');
INSERT INTO `gen_table_column` VALUES (451, '39', 'image', '图片', 'text', 'String', 'image', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'datetime', '', 2, 'admin', '2020-07-31 11:41:47', NULL, '2020-07-31 11:46:51');
INSERT INTO `gen_table_column` VALUES (452, '39', 'type', '类型', 'varchar(255)', 'String', 'type', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'radio', 'SHARE_IMG', 3, 'admin', '2020-07-31 11:41:47', NULL, '2020-07-31 11:46:51');
INSERT INTO `gen_table_column` VALUES (453, '39', 'create_time', 'null', 'datetime', 'Date', 'createTime', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'datetime', '', 4, 'admin', '2020-07-31 11:41:47', NULL, '2020-07-31 11:46:51');
INSERT INTO `gen_table_column` VALUES (454, '39', 'update_time', 'null', 'datetime', 'Date', 'updateTime', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'datetime', '', 5, 'admin', '2020-07-31 11:41:47', NULL, '2020-07-31 11:46:51');
INSERT INTO `gen_table_column` VALUES (455, '40', 'id', 'null', 'varchar(32)', 'String', 'id', '1', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2020-07-31 21:25:48', NULL, '2020-07-31 21:33:58');
INSERT INTO `gen_table_column` VALUES (456, '40', 'phone', '手机号', 'varchar(30)', 'String', 'phone', '0', '0', NULL, NULL, NULL, '1', '1', 'EQ', 'input', '', 2, 'admin', '2020-07-31 21:25:48', NULL, '2020-07-31 21:33:58');
INSERT INTO `gen_table_column` VALUES (457, '40', 'uuid', 'null', 'varchar(32)', 'String', 'uuid', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 3, 'admin', '2020-07-31 21:25:48', NULL, '2020-07-31 21:33:58');
INSERT INTO `gen_table_column` VALUES (458, '40', 'mail', '邮箱', 'varchar(100)', 'String', 'mail', '0', '0', NULL, NULL, NULL, '1', '1', 'EQ', 'input', '', 4, 'admin', '2020-07-31 21:25:48', NULL, '2020-07-31 21:33:58');
INSERT INTO `gen_table_column` VALUES (459, '40', 'password', '登录密码', 'varchar(100)', 'String', 'password', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 5, 'admin', '2020-07-31 21:25:48', NULL, '2020-07-31 21:33:58');
INSERT INTO `gen_table_column` VALUES (460, '40', 'wel_member', '邀请人id', 'varchar(32)', 'String', 'welMember', '0', '0', NULL, NULL, NULL, '1', '1', 'EQ', 'input', '', 6, 'admin', '2020-07-31 21:25:48', NULL, '2020-07-31 21:33:58');
INSERT INTO `gen_table_column` VALUES (461, '40', 'brokerage', '佣金', 'decimal(20,8)', 'Double', 'brokerage', '0', '0', NULL, NULL, NULL, '1', '1', 'EQ', 'input', '', 7, 'admin', '2020-07-31 21:25:48', NULL, '2020-07-31 21:33:58');
INSERT INTO `gen_table_column` VALUES (462, '40', 'wel_code', '邀请码', 'varchar(20)', 'String', 'welCode', '0', '0', NULL, NULL, NULL, '1', '1', 'EQ', 'input', '', 8, 'admin', '2020-07-31 21:25:48', NULL, '2020-07-31 21:33:58');
INSERT INTO `gen_table_column` VALUES (463, '40', 'pay_password', '支付密码', 'varchar(100)', 'String', 'payPassword', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 9, 'admin', '2020-07-31 21:25:48', NULL, '2020-07-31 21:33:58');
INSERT INTO `gen_table_column` VALUES (464, '40', 'type', '账号类型:INTERNAL内部的,EXTERNAL外部的', 'varchar(16)', 'String', 'type', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'select', '', 10, 'admin', '2020-07-31 21:25:48', NULL, '2020-07-31 21:33:58');
INSERT INTO `gen_table_column` VALUES (465, '40', 'uname', 'null', 'varchar(100)', 'String', 'uname', '0', '0', NULL, NULL, NULL, NULL, NULL, 'LIKE', 'input', '', 11, 'admin', '2020-07-31 21:25:48', NULL, '2020-07-31 21:33:58');
INSERT INTO `gen_table_column` VALUES (466, '40', 'sex', 'null', 'varchar(20)', 'String', 'sex', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'select', '', 12, 'admin', '2020-07-31 21:25:49', NULL, '2020-07-31 21:33:58');
INSERT INTO `gen_table_column` VALUES (467, '40', 'birth', 'null', 'varchar(100)', 'String', 'birth', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 13, 'admin', '2020-07-31 21:25:49', NULL, '2020-07-31 21:33:58');
INSERT INTO `gen_table_column` VALUES (468, '40', 'card_no', 'null', 'varchar(100)', 'String', 'cardNo', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 14, 'admin', '2020-07-31 21:25:49', NULL, '2020-07-31 21:33:58');
INSERT INTO `gen_table_column` VALUES (469, '40', 'address', 'null', 'varchar(200)', 'String', 'address', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 15, 'admin', '2020-07-31 21:25:49', NULL, '2020-07-31 21:33:59');
INSERT INTO `gen_table_column` VALUES (470, '40', 'quick_mark', 'null', 'varchar(300)', 'String', 'quickMark', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 16, 'admin', '2020-07-31 21:25:49', NULL, '2020-07-31 21:33:59');
INSERT INTO `gen_table_column` VALUES (471, '40', 'broker', '是(1)否(0)', 'tinyint(4)', 'Integer', 'broker', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 17, 'admin', '2020-07-31 21:25:49', NULL, '2020-07-31 21:33:59');
INSERT INTO `gen_table_column` VALUES (472, '40', 'broker_grade_one', '经纪人等级一', 'varchar(32)', 'String', 'brokerGradeOne', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 18, 'admin', '2020-07-31 21:25:49', NULL, '2020-07-31 21:33:59');
INSERT INTO `gen_table_column` VALUES (473, '40', 'broker_grade_two', '经纪人等级二', 'varchar(32)', 'String', 'brokerGradeTwo', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 19, 'admin', '2020-07-31 21:25:49', NULL, '2020-07-31 21:33:59');
INSERT INTO `gen_table_column` VALUES (474, '40', 'broker_grade_three', '经纪人等级三', 'varchar(32)', 'String', 'brokerGradeThree', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 20, 'admin', '2020-07-31 21:25:49', NULL, '2020-07-31 21:33:59');
INSERT INTO `gen_table_column` VALUES (475, '40', 'node_path', '邀请节点路径', 'text', 'String', 'nodePath', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 21, 'admin', '2020-07-31 21:25:49', NULL, '2020-07-31 21:33:59');
INSERT INTO `gen_table_column` VALUES (476, '40', 'nick_name', 'null', 'varchar(50)', 'String', 'nickName', '0', '0', NULL, NULL, NULL, NULL, NULL, 'LIKE', 'input', '', 22, 'admin', '2020-07-31 21:25:49', NULL, '2020-07-31 21:33:59');
INSERT INTO `gen_table_column` VALUES (477, '40', 'pay_wechat', 'null', 'varchar(300)', 'String', 'payWechat', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 23, 'admin', '2020-07-31 21:25:49', NULL, '2020-07-31 21:33:59');
INSERT INTO `gen_table_column` VALUES (478, '40', 'pay_aliay', 'null', 'varchar(300)', 'String', 'payAliay', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 24, 'admin', '2020-07-31 21:25:49', NULL, '2020-07-31 21:33:59');
INSERT INTO `gen_table_column` VALUES (479, '40', 'wechat_name', 'null', 'varchar(50)', 'String', 'wechatName', '0', '0', NULL, NULL, NULL, NULL, NULL, 'LIKE', 'input', '', 25, 'admin', '2020-07-31 21:25:49', NULL, '2020-07-31 21:33:59');
INSERT INTO `gen_table_column` VALUES (480, '40', 'aliay_name', 'null', 'varchar(50)', 'String', 'aliayName', '0', '0', NULL, NULL, NULL, NULL, NULL, 'LIKE', 'input', '', 26, 'admin', '2020-07-31 21:25:49', NULL, '2020-07-31 21:33:59');
INSERT INTO `gen_table_column` VALUES (481, '40', 'side_link', 'null', 'varchar(300)', 'String', 'sideLink', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 27, 'admin', '2020-07-31 21:25:50', NULL, '2020-07-31 21:33:59');
INSERT INTO `gen_table_column` VALUES (482, '40', 'positive_link', 'null', 'varchar(300)', 'String', 'positiveLink', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 28, 'admin', '2020-07-31 21:25:50', NULL, '2020-07-31 21:33:59');
INSERT INTO `gen_table_column` VALUES (483, '40', 'card_state', 'null', 'varchar(50)', 'String', 'cardState', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 29, 'admin', '2020-07-31 21:25:50', NULL, '2020-07-31 21:33:59');
INSERT INTO `gen_table_column` VALUES (484, '40', 'create_time', 'null', 'datetime', 'Date', 'createTime', '0', '0', NULL, NULL, NULL, '1', '1', 'EQ', 'datetime', '', 30, 'admin', '2020-07-31 21:25:50', NULL, '2020-07-31 21:34:00');
INSERT INTO `gen_table_column` VALUES (485, '40', 'update_time', 'null', 'datetime', 'Date', 'updateTime', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'datetime', '', 31, 'admin', '2020-07-31 21:25:50', NULL, '2020-07-31 21:34:00');
INSERT INTO `gen_table_column` VALUES (486, '40', 'store_name', '商家昵称', 'varchar(100)', 'String', 'storeName', '0', '0', NULL, NULL, NULL, NULL, NULL, 'LIKE', 'input', '', 32, 'admin', '2020-07-31 21:25:50', NULL, '2020-07-31 21:34:00');
INSERT INTO `gen_table_column` VALUES (487, '40', 'bank_user_name', 'null', 'varchar(100)', 'String', 'bankUserName', '0', '0', NULL, NULL, NULL, NULL, NULL, 'LIKE', 'input', '', 33, 'admin', '2020-07-31 21:25:50', NULL, '2020-07-31 21:34:00');
INSERT INTO `gen_table_column` VALUES (488, '40', 'bank_name', 'null', 'varchar(50)', 'String', 'bankName', '0', '0', NULL, NULL, NULL, NULL, NULL, 'LIKE', 'input', '', 34, 'admin', '2020-07-31 21:25:50', NULL, '2020-07-31 21:34:00');
INSERT INTO `gen_table_column` VALUES (489, '40', 'bank_address', 'null', 'varchar(150)', 'String', 'bankAddress', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 35, 'admin', '2020-07-31 21:25:50', NULL, '2020-07-31 21:34:00');
INSERT INTO `gen_table_column` VALUES (490, '40', 'bank_card', 'null', 'varchar(100)', 'String', 'bankCard', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 36, 'admin', '2020-07-31 21:25:50', NULL, '2020-07-31 21:34:00');
INSERT INTO `gen_table_column` VALUES (491, '40', 'fb_status', '法币是否可用', 'varchar(100)', 'String', 'fbStatus', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'radio', '', 37, 'admin', '2020-07-31 21:25:50', NULL, '2020-07-31 21:34:00');
INSERT INTO `gen_table_column` VALUES (492, '40', 'store_state', 'null', 'varchar(50)', 'String', 'storeState', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 38, 'admin', '2020-07-31 21:25:50', NULL, '2020-07-31 21:34:00');
INSERT INTO `gen_table_column` VALUES (493, '40', 'salt', '密码盐', 'varchar(255)', 'String', 'salt', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 39, 'admin', '2020-07-31 21:25:50', NULL, '2020-07-31 21:34:00');
INSERT INTO `gen_table_column` VALUES (494, '40', 'area_code', '区号', 'varchar(10)', 'String', 'areaCode', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 40, 'admin', '2020-07-31 21:25:50', NULL, '2020-07-31 21:34:00');
INSERT INTO `gen_table_column` VALUES (495, '40', 'hand_link', '手持身份证照片', 'varchar(300)', 'String', 'handLink', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'input', '', 41, 'admin', '2020-07-31 21:25:51', NULL, '2020-07-31 21:34:01');
INSERT INTO `gen_table_column` VALUES (496, '40', 'user_status', '封号状态', 'varchar(30)', 'String', 'userStatus', '0', '0', NULL, NULL, NULL, NULL, NULL, 'EQ', 'radio', '', 42, 'admin', '2020-07-31 21:25:51', NULL, '2020-07-31 21:34:01');

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `blob_data` blob NULL,
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `calendar_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `calendar` blob NOT NULL,
  PRIMARY KEY (`sched_name`, `calendar_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `cron_expression` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `time_zone_id` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------
INSERT INTO `qrtz_cron_triggers` VALUES ('RuoyiScheduler', 'TASK_CLASS_NAME1', 'DEFAULT', '0/10 * * * * ?', 'Asia/Shanghai');
INSERT INTO `qrtz_cron_triggers` VALUES ('RuoyiScheduler', 'TASK_CLASS_NAME2', 'DEFAULT', '0/15 * * * * ?', 'Asia/Shanghai');
INSERT INTO `qrtz_cron_triggers` VALUES ('RuoyiScheduler', 'TASK_CLASS_NAME3', 'DEFAULT', '0/20 * * * * ?', 'Asia/Shanghai');

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `entry_id` varchar(95) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `instance_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `fired_time` bigint(13) NOT NULL,
  `sched_time` bigint(13) NOT NULL,
  `priority` int(11) NOT NULL,
  `state` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `job_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `job_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `requests_recovery` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`sched_name`, `entry_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `job_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `job_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `job_class_name` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `is_durable` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `is_update_data` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `requests_recovery` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `job_data` blob NULL,
  PRIMARY KEY (`sched_name`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------
INSERT INTO `qrtz_job_details` VALUES ('RuoyiScheduler', 'TASK_CLASS_NAME1', 'DEFAULT', NULL, 'com.ruoyi.quartz.util.QuartzDisallowConcurrentExecution', '0', '1', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000F5441534B5F50524F504552544945537372001E636F6D2E72756F79692E71756172747A2E646F6D61696E2E5379734A6F6200000000000000010200084C000A636F6E63757272656E747400124C6A6176612F6C616E672F537472696E673B4C000E63726F6E45787072657373696F6E71007E00094C000C696E766F6B6554617267657471007E00094C00086A6F6247726F757071007E00094C00056A6F6249647400104C6A6176612F6C616E672F4C6F6E673B4C00076A6F624E616D6571007E00094C000D6D697366697265506F6C69637971007E00094C000673746174757371007E000978720027636F6D2E72756F79692E636F6D6D6F6E2E636F72652E646F6D61696E2E42617365456E7469747900000000000000010200074C0008637265617465427971007E00094C000A63726561746554696D657400104C6A6176612F7574696C2F446174653B4C0006706172616D7371007E00034C000672656D61726B71007E00094C000B73656172636856616C756571007E00094C0008757064617465427971007E00094C000A75706461746554696D6571007E000C787074000561646D696E7372000E6A6176612E7574696C2E44617465686A81014B59741903000078707708000001622CDE29E078707400007070707400013174000E302F3130202A202A202A202A203F74001172795461736B2E72794E6F506172616D7374000744454641554C547372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B02000078700000000000000001740018E7B3BBE7BB9FE9BB98E8AEA4EFBC88E697A0E58F82EFBC8974000133740001317800);
INSERT INTO `qrtz_job_details` VALUES ('RuoyiScheduler', 'TASK_CLASS_NAME2', 'DEFAULT', NULL, 'com.ruoyi.quartz.util.QuartzDisallowConcurrentExecution', '0', '1', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000F5441534B5F50524F504552544945537372001E636F6D2E72756F79692E71756172747A2E646F6D61696E2E5379734A6F6200000000000000010200084C000A636F6E63757272656E747400124C6A6176612F6C616E672F537472696E673B4C000E63726F6E45787072657373696F6E71007E00094C000C696E766F6B6554617267657471007E00094C00086A6F6247726F757071007E00094C00056A6F6249647400104C6A6176612F6C616E672F4C6F6E673B4C00076A6F624E616D6571007E00094C000D6D697366697265506F6C69637971007E00094C000673746174757371007E000978720027636F6D2E72756F79692E636F6D6D6F6E2E636F72652E646F6D61696E2E42617365456E7469747900000000000000010200074C0008637265617465427971007E00094C000A63726561746554696D657400104C6A6176612F7574696C2F446174653B4C0006706172616D7371007E00034C000672656D61726B71007E00094C000B73656172636856616C756571007E00094C0008757064617465427971007E00094C000A75706461746554696D6571007E000C787074000561646D696E7372000E6A6176612E7574696C2E44617465686A81014B59741903000078707708000001622CDE29E078707400007070707400013174000E302F3135202A202A202A202A203F74001572795461736B2E7279506172616D7328277279272974000744454641554C547372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B02000078700000000000000002740018E7B3BBE7BB9FE9BB98E8AEA4EFBC88E69C89E58F82EFBC8974000133740001317800);
INSERT INTO `qrtz_job_details` VALUES ('RuoyiScheduler', 'TASK_CLASS_NAME3', 'DEFAULT', NULL, 'com.ruoyi.quartz.util.QuartzDisallowConcurrentExecution', '0', '1', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000F5441534B5F50524F504552544945537372001E636F6D2E72756F79692E71756172747A2E646F6D61696E2E5379734A6F6200000000000000010200084C000A636F6E63757272656E747400124C6A6176612F6C616E672F537472696E673B4C000E63726F6E45787072657373696F6E71007E00094C000C696E766F6B6554617267657471007E00094C00086A6F6247726F757071007E00094C00056A6F6249647400104C6A6176612F6C616E672F4C6F6E673B4C00076A6F624E616D6571007E00094C000D6D697366697265506F6C69637971007E00094C000673746174757371007E000978720027636F6D2E72756F79692E636F6D6D6F6E2E636F72652E646F6D61696E2E42617365456E7469747900000000000000010200074C0008637265617465427971007E00094C000A63726561746554696D657400104C6A6176612F7574696C2F446174653B4C0006706172616D7371007E00034C000672656D61726B71007E00094C000B73656172636856616C756571007E00094C0008757064617465427971007E00094C000A75706461746554696D6571007E000C787074000561646D696E7372000E6A6176612E7574696C2E44617465686A81014B59741903000078707708000001622CDE29E078707400007070707400013174000E302F3230202A202A202A202A203F74003872795461736B2E72794D756C7469706C65506172616D7328277279272C20747275652C20323030304C2C203331362E3530442C203130302974000744454641554C547372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B02000078700000000000000003740018E7B3BBE7BB9FE9BB98E8AEA4EFBC88E5A49AE58F82EFBC8974000133740001317800);

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `lock_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`sched_name`, `lock_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------
INSERT INTO `qrtz_locks` VALUES ('RuoyiScheduler', 'STATE_ACCESS');
INSERT INTO `qrtz_locks` VALUES ('RuoyiScheduler', 'TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`sched_name`, `trigger_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `instance_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `last_checkin_time` bigint(13) NOT NULL,
  `checkin_interval` bigint(13) NOT NULL,
  PRIMARY KEY (`sched_name`, `instance_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------
INSERT INTO `qrtz_scheduler_state` VALUES ('RuoyiScheduler', 'VM-0-9-centos1608353643750', 1608429811738, 15000);

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `repeat_count` bigint(7) NOT NULL,
  `repeat_interval` bigint(12) NOT NULL,
  `times_triggered` bigint(10) NOT NULL,
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `str_prop_1` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `str_prop_2` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `str_prop_3` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `int_prop_1` int(11) NULL DEFAULT NULL,
  `int_prop_2` int(11) NULL DEFAULT NULL,
  `long_prop_1` bigint(20) NULL DEFAULT NULL,
  `long_prop_2` bigint(20) NULL DEFAULT NULL,
  `dec_prop_1` decimal(13, 4) NULL DEFAULT NULL,
  `dec_prop_2` decimal(13, 4) NULL DEFAULT NULL,
  `bool_prop_1` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bool_prop_2` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `job_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `job_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `next_fire_time` bigint(13) NULL DEFAULT NULL,
  `prev_fire_time` bigint(13) NULL DEFAULT NULL,
  `priority` int(11) NULL DEFAULT NULL,
  `trigger_state` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_type` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `start_time` bigint(13) NOT NULL,
  `end_time` bigint(13) NULL DEFAULT NULL,
  `calendar_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `misfire_instr` smallint(2) NULL DEFAULT NULL,
  `job_data` blob NULL,
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  INDEX `sched_name`(`sched_name`, `job_name`, `job_group`) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------
INSERT INTO `qrtz_triggers` VALUES ('RuoyiScheduler', 'TASK_CLASS_NAME1', 'DEFAULT', 'TASK_CLASS_NAME1', 'DEFAULT', NULL, 1608353650000, -1, 5, 'PAUSED', 'CRON', 1608353649000, 0, NULL, 2, '');
INSERT INTO `qrtz_triggers` VALUES ('RuoyiScheduler', 'TASK_CLASS_NAME2', 'DEFAULT', 'TASK_CLASS_NAME2', 'DEFAULT', NULL, 1608353670000, -1, 5, 'PAUSED', 'CRON', 1608353657000, 0, NULL, 2, '');
INSERT INTO `qrtz_triggers` VALUES ('RuoyiScheduler', 'TASK_CLASS_NAME3', 'DEFAULT', 'TASK_CLASS_NAME3', 'DEFAULT', NULL, 1608353680000, -1, 5, 'PAUSED', 'CRON', 1608353667000, 0, NULL, 2, '');

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` int(5) NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '参数配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-red', 'Y', 'admin', '2018-03-16 11:33:00', 'admin', '2020-07-24 13:19:18', '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '初始化密码 123456');
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '深黑主题theme-dark，浅色主题theme-light，深蓝主题theme-blue');
INSERT INTO `sys_config` VALUES (4, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '是否开启注册用户功能');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '部门名称',
  `order_num` int(4) NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 110 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, '0', '若依科技', 0, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00');
INSERT INTO `sys_dept` VALUES (101, 100, '0,100', '深圳总公司', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00');
INSERT INTO `sys_dept` VALUES (102, 100, '0,100', '长沙分公司', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00');
INSERT INTO `sys_dept` VALUES (103, 101, '0,100,101', '研发部门', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00');
INSERT INTO `sys_dept` VALUES (104, 101, '0,100,101', '市场部门', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00');
INSERT INTO `sys_dept` VALUES (105, 101, '0,100,101', '测试部门', 3, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00');
INSERT INTO `sys_dept` VALUES (106, 101, '0,100,101', '财务部门', 4, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00');
INSERT INTO `sys_dept` VALUES (107, 101, '0,100,101', '运维部门', 5, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00');
INSERT INTO `sys_dept` VALUES (108, 102, '0,100,102', '市场部门', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00');
INSERT INTO `sys_dept` VALUES (109, 102, '0,100,102', '财务部门', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00');

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int(4) NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 224 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '性别未知');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '显示菜单');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '停用状态');
INSERT INTO `sys_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '正常状态');
INSERT INTO `sys_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '停用状态');
INSERT INTO `sys_dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '默认分组');
INSERT INTO `sys_dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '系统分组');
INSERT INTO `sys_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '系统默认是');
INSERT INTO `sys_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '系统默认否');
INSERT INTO `sys_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '通知');
INSERT INTO `sys_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '公告');
INSERT INTO `sys_dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '正常状态');
INSERT INTO `sys_dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '关闭状态');
INSERT INTO `sys_dict_data` VALUES (18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '其他操作');
INSERT INTO `sys_dict_data` VALUES (19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '新增操作');
INSERT INTO `sys_dict_data` VALUES (20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '修改操作');
INSERT INTO `sys_dict_data` VALUES (21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '删除操作');
INSERT INTO `sys_dict_data` VALUES (22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '授权操作');
INSERT INTO `sys_dict_data` VALUES (23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '导出操作');
INSERT INTO `sys_dict_data` VALUES (24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '导入操作');
INSERT INTO `sys_dict_data` VALUES (25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '强退操作');
INSERT INTO `sys_dict_data` VALUES (26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '生成操作');
INSERT INTO `sys_dict_data` VALUES (27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '清空操作');
INSERT INTO `sys_dict_data` VALUES (28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '正常状态');
INSERT INTO `sys_dict_data` VALUES (29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '停用状态');
INSERT INTO `sys_dict_data` VALUES (100, 1, '订单持续时间', 'orderTime', 'otc_type_config', NULL, 'primary', 'Y', '0', 'admin', '2020-03-30 09:35:09', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (101, 1, '每日取消最大次数', 'backCount', 'otc_type_config', NULL, 'primary', 'Y', '0', 'admin', '2020-03-30 09:35:26', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (102, 1, '最小溢价比例', 'minRatio', 'otc_type_config', NULL, 'primary', 'Y', '0', 'admin', '2020-03-30 09:35:41', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (103, 1, '最大溢价比例', 'maxRatio', 'otc_type_config', NULL, 'primary', 'Y', '0', 'admin', '2020-03-30 09:35:53', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (104, 1, '未付款', 'NONPAYMENT', 'otc_order_price', NULL, 'success', 'Y', '0', 'admin', '2020-03-30 10:05:25', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (105, 1, '已付款', 'PAYMENT', 'otc_order_price', '', 'success', 'Y', '0', 'admin', '2020-03-30 10:05:36', 'admin', '2020-03-30 10:05:48', '');
INSERT INTO `sys_dict_data` VALUES (106, 1, '完成', 'FINISH', 'otc_order_price', NULL, 'success', 'Y', '0', 'admin', '2020-03-30 10:06:07', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (107, 1, '取消', 'CALLOFF', 'otc_order_price', NULL, 'success', 'Y', '0', 'admin', '2020-03-30 10:06:18', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (108, 1, '正常', 'NORMAL', 'otc_shensu_status', NULL, 'info', 'Y', '0', 'admin', '2020-03-30 10:08:20', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (109, 1, '已申诉', 'UNDERWAY', 'otc_shensu_status', '', 'info', 'Y', '0', 'admin', '2020-03-30 10:08:35', 'admin', '2020-03-30 10:08:41', '');
INSERT INTO `sys_dict_data` VALUES (110, 1, '申诉成功', 'SUCCEED', 'otc_shensu_status', NULL, 'info', 'Y', '0', 'admin', '2020-03-30 10:08:54', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (111, 1, '申诉失败', 'DEFEATED', 'otc_shensu_status', NULL, 'info', 'Y', '0', 'admin', '2020-03-30 10:09:05', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (112, 1, '申诉撤销', 'BACK', 'otc_shensu_status', NULL, 'info', 'Y', '0', 'admin', '2020-03-30 10:09:16', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (113, 1, '支付宝', 'ALIAY', 'otc_pay_type', NULL, 'primary', 'Y', '0', 'admin', '2020-03-30 10:12:10', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (114, 1, '微信', 'WECHAT', 'otc_pay_type', NULL, 'primary', 'Y', '0', 'admin', '2020-03-30 10:12:39', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (115, 1, '银行卡', 'BANKCARD', 'otc_pay_type', NULL, 'primary', 'Y', '0', 'admin', '2020-03-30 10:12:53', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (116, 1, '正常', 'NORMAL', 'otc_order_status', NULL, 'primary', 'Y', '0', 'admin', '2020-03-30 10:54:11', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (117, 1, '撤回', 'BACKOUT', 'otc_order_status', NULL, 'primary', 'Y', '0', 'admin', '2020-03-30 10:54:20', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (118, 1, '全部成交', 'FINISH', 'otc_order_status', NULL, 'primary', 'Y', '0', 'admin', '2020-03-30 10:54:33', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (120, 12, '申请中', 'WAIT', 'otc_card_state', '', 'warning', 'N', '0', 'admin', '2020-03-31 09:20:06', 'admin', '2020-04-23 09:42:19', '');
INSERT INTO `sys_dict_data` VALUES (121, 11, '是', 'PASS', 'otc_card_state', '', 'warning', 'N', '0', 'admin', '2020-03-31 09:20:19', 'admin', '2020-04-23 09:42:16', '');
INSERT INTO `sys_dict_data` VALUES (122, 1, '否', 'NO', 'otc_card_state', '', 'warning', 'Y', '0', 'admin', '2020-03-31 09:37:03', 'admin', '2020-04-23 09:42:12', '');
INSERT INTO `sys_dict_data` VALUES (123, 1, '审核通过', 'PASS', 'tibishenhe', NULL, 'primary', 'Y', '0', 'admin', '2020-03-31 18:10:59', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (124, 1, '审核中', 'CREATE', 'tibishenhe', NULL, 'primary', 'Y', '0', 'admin', '2020-03-31 18:11:14', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (125, 1, '未通过', 'REJECT', 'tibishenhe', '', 'primary', 'Y', '0', 'admin', '2020-03-31 18:11:37', 'admin', '2020-05-05 17:58:11', '');
INSERT INTO `sys_dict_data` VALUES (126, 1, '中文', 'CHINESE_SIM', 'language', NULL, 'primary', 'Y', '0', 'admin', '2020-04-01 17:18:21', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (127, 1, '繁体中文', 'CHINESE_TRAD', 'language', NULL, 'primary', 'Y', '0', 'admin', '2020-04-01 17:18:35', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (128, 1, '英文', 'ENGLISH', 'language', NULL, 'primary', 'Y', '0', 'admin', '2020-04-01 17:18:50', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (129, 1, '官方公告', 'OFFICIAL', 'gong_gao_type', NULL, 'success', 'Y', '0', 'admin', '2020-04-01 17:20:42', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (130, 1, '新闻资讯', 'NEWS', 'gong_gao_type', NULL, 'success', 'Y', '0', 'admin', '2020-04-01 17:20:56', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (131, 1, '多开', 'OPEN_UP', 'heyue_jiaoyi_type', NULL, 'primary', 'Y', '0', 'admin', '2020-04-03 14:50:51', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (132, 1, '开空', 'OPEN_DOWN', 'heyue_jiaoyi_type', NULL, 'primary', 'Y', '0', 'admin', '2020-04-03 14:51:09', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (133, 1, '平多', 'CLOSE_UP', 'heyue_jiaoyi_type', NULL, 'primary', 'Y', '0', 'admin', '2020-04-03 14:51:33', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (134, 1, '平空', 'CLOSE_DOWN', 'heyue_jiaoyi_type', NULL, 'primary', 'Y', '0', 'admin', '2020-04-03 14:51:49', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (135, 1, '强制平仓', 'BURCH', 'pingcang_type', NULL, 'success', 'Y', '0', 'admin', '2020-04-03 14:52:27', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (136, 1, '手动平仓', 'MATCH', 'pingcang_type', NULL, 'success', 'Y', '0', 'admin', '2020-04-03 14:52:39', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (137, 1, '已结束', 'CREATE', 'heyue_status', NULL, 'info', 'Y', '0', 'admin', '2020-04-03 14:53:22', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (138, 1, '已完成', 'FINAL', 'heyue_status', NULL, 'info', 'Y', '0', 'admin', '2020-04-03 14:53:36', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (139, 1, '市价', 'ORDERS', 'heyue_order_type', NULL, 'default', 'Y', '0', 'admin', '2020-04-03 14:54:54', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (140, 1, '限价', 'POSITIONS', 'heyue_order_type', NULL, 'default', 'Y', '0', 'admin', '2020-04-03 14:55:11', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (141, 3, '拒绝', 'REFUSE', 'otc_card_state', '', 'warning', 'Y', '0', 'admin', '2020-04-08 12:22:16', 'admin', '2020-04-23 09:42:06', '');
INSERT INTO `sys_dict_data` VALUES (142, 0, '正常', 'NORMAL', 't_user_status', NULL, 'primary', 'Y', '0', 'admin', '2020-04-14 17:40:44', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (143, 11, '封号', 'UNNORMAL', 't_user_status', NULL, 'primary', 'N', '0', 'admin', '2020-04-14 17:41:13', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (144, 1, '不是', '0', 'shifou', '', 'warning', 'Y', '0', 'admin', '2020-04-15 12:09:24', 'admin', '2020-04-23 09:42:43', '');
INSERT INTO `sys_dict_data` VALUES (145, 2, '是', '1', 'shifou', '', 'warning', 'N', '0', 'admin', '2020-04-15 12:09:34', 'admin', '2020-04-23 09:42:40', '');
INSERT INTO `sys_dict_data` VALUES (146, 1, '正常', 'NORMAL', 't_pair_status', NULL, 'success', 'Y', '0', 'admin', '2020-04-15 12:11:46', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (147, 2, '冻结', 'FROZEN', 't_pair_status', NULL, 'success', 'N', '0', 'admin', '2020-04-15 12:12:01', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (148, 1, '主流币', 'MAIN_COIN', 't_pair_type', NULL, 'primary', 'Y', '0', 'admin', '2020-04-15 12:13:47', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (149, 2, '创新币', 'PROJECT_COIN', 't_pair_type', '', 'primary', 'N', '0', 'admin', '2020-04-15 12:14:00', 'admin', '2020-04-15 12:14:06', '');
INSERT INTO `sys_dict_data` VALUES (154, 1, 'USDT/OMNI', 'USDT/OMNI', 'currency', '', 'default', 'Y', '0', 'admin', '2020-04-16 18:31:00', 'admin', '2020-04-22 14:30:09', '');
INSERT INTO `sys_dict_data` VALUES (155, 2, 'FVC', 'FVC', 'currency', NULL, 'default', 'N', '0', 'admin', '2020-04-16 18:31:10', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (156, 3, 'ETH', 'ETH', 'currency', '', 'default', 'Y', '0', 'admin', '2020-04-16 18:32:22', 'admin', '2020-04-16 18:34:01', '');
INSERT INTO `sys_dict_data` VALUES (157, 1, '币币可用', 'balance', 'balance_order', '', 'primary', 'N', '0', 'admin', '2020-04-17 15:45:17', 'admin', '2020-04-17 15:45:24', '');
INSERT INTO `sys_dict_data` VALUES (158, 2, '币币冻结', 'blocked_balance', 'balance_order', NULL, 'primary', 'N', '0', 'admin', '2020-04-17 15:45:41', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (159, 2, '充提可用', 'assets_balance', 'balance_order', NULL, 'primary', 'N', '0', 'admin', '2020-04-17 15:45:58', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (160, 2, '充提冻结', 'assets_blocked_balance', 'balance_order', NULL, 'primary', 'N', '0', 'admin', '2020-04-17 15:46:12', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (161, 2, '合约可用', 'token_balance', 'balance_order', NULL, 'primary', 'N', '0', 'admin', '2020-04-17 15:46:35', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (162, 2, '合约冻结', 'token_blocked_balance', 'balance_order', NULL, 'primary', 'N', '0', 'admin', '2020-04-17 15:46:46', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (163, 2, '法币可用', 'fb_balance', 'balance_order', NULL, 'primary', 'N', '0', 'admin', '2020-04-17 15:47:30', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (164, 2, '法币冻结', 'fb_blocked_balance', 'balance_order', NULL, 'primary', 'N', '0', 'admin', '2020-04-17 15:47:43', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (165, 2, '静态资产', 'raise_balance', 'balance_order', '', 'primary', 'N', '0', 'admin', '2020-04-17 15:47:57', 'admin', '2020-04-17 16:33:22', '');
INSERT INTO `sys_dict_data` VALUES (166, 1, '注册', 'REGISTER', 't_msg_record', NULL, 'primary', 'Y', '0', 'admin', '2020-04-21 12:01:52', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (167, 2, '重置密码', 'RESET', 't_msg_record', NULL, 'primary', 'Y', '0', 'admin', '2020-04-21 12:02:31', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (168, 2, '重置支付密码', 'PAYRESET', 't_msg_record', NULL, 'primary', 'Y', '0', 'admin', '2020-04-21 12:02:44', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (169, 2, '绑定手机或者邮箱', 'SETPHMAIL', 't_msg_record', NULL, 'primary', 'Y', '0', 'admin', '2020-04-21 12:04:15', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (170, 2, '找回密码', 'RETRIEVE', 't_msg_record', NULL, 'primary', 'Y', '0', 'admin', '2020-04-21 12:04:31', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (171, 2, '提币', 'WITHDRAWAL_MONEY', 't_msg_record', NULL, 'primary', 'Y', '0', 'admin', '2020-04-21 12:04:48', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (172, 2, '绑定微信', 'OTCWECHAT', 't_msg_record', NULL, 'primary', 'Y', '0', 'admin', '2020-04-21 12:05:20', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (173, 2, '绑定支付宝', 'OTCALIAY', 't_msg_record', NULL, 'primary', 'Y', '0', 'admin', '2020-04-21 12:05:31', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (174, 2, '绑定银行卡', 'OTCBANK', 't_msg_record', NULL, 'primary', 'Y', '0', 'admin', '2020-04-21 12:05:44', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (175, 2, '提现', 'WITHDRAWAL_COIN', 't_msg_record', NULL, 'primary', 'Y', '0', 'admin', '2020-04-21 12:05:55', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (176, 2, 'USDT/ERC20', 'USDT/ERC20', 'currency', '', 'default', 'Y', '0', 'admin', '2020-04-22 14:29:44', 'admin', '2020-04-22 14:29:57', '');
INSERT INTO `sys_dict_data` VALUES (177, 1, '否', 'NO', 'otc_store_state', '', 'success', 'Y', '0', 'admin', '2020-04-23 09:44:40', 'admin', '2020-04-23 09:45:35', '');
INSERT INTO `sys_dict_data` VALUES (178, 12, '拒绝', 'REFUSE', 'otc_store_state', '', 'success', 'N', '0', 'admin', '2020-04-23 09:44:58', 'admin', '2020-04-23 09:45:31', '');
INSERT INTO `sys_dict_data` VALUES (179, 13, '是', 'PASS', 'otc_store_state', NULL, 'success', 'N', '0', 'admin', '2020-04-23 09:45:26', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (180, 12, '申请中', 'WAIT', 'otc_store_state', NULL, 'success', 'N', '0', 'admin', '2020-04-23 09:45:51', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (181, 1, '发布广告', 'OtcController.orderReleasing', 't_interface_name', '', 'warning', 'Y', '0', 'admin', '2020-04-23 15:58:28', 'admin', '2020-04-23 15:59:14', '');
INSERT INTO `sys_dict_data` VALUES (182, 1, '下单', 'OtcController.placeAnOrder', 't_interface_name', NULL, 'warning', 'Y', '0', 'admin', '2020-04-23 15:59:36', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (183, 1, '确认付款', 'OtcController.payment', 't_interface_name', NULL, 'warning', 'Y', '0', 'admin', '2020-04-23 16:00:15', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (184, 1, '一键购买', 'OtcController.fastPlaceAnOrder', 't_interface_name', NULL, 'warning', 'Y', '0', 'admin', '2020-04-23 16:00:36', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (185, 1, '撤回广告', 'OtcController.orderBack', 't_interface_name', NULL, 'warning', 'Y', '0', 'admin', '2020-04-23 16:01:08', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (186, 1, '确认放币', 'OtcController.deliverGoods', 't_interface_name', NULL, 'warning', 'Y', '0', 'admin', '2020-04-23 16:01:34', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (187, 1, '取消订单', 'OtcController.backOrder', 't_interface_name', NULL, 'warning', 'Y', '0', 'admin', '2020-04-23 16:01:53', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (188, 1, '申诉订单', 'OtcController.appeal', 't_interface_name', NULL, 'warning', 'Y', '0', 'admin', '2020-04-23 16:02:13', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (189, 1, '取消申诉', 'OtcController.appealBack', 't_interface_name', NULL, 'warning', 'Y', '0', 'admin', '2020-04-23 16:02:26', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (190, 1, '修改广告', 'OtcController.updataOrder', 't_interface_name', NULL, 'warning', 'Y', '0', 'admin', '2020-04-23 16:02:47', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (191, 1, '提币', 'BalanceController.extractCoin', 't_interface_name', NULL, 'warning', 'Y', '0', 'admin', '2020-04-23 16:31:49', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (192, 1, '划转资产', 'BalanceController.transferBalances', 't_interface_name', NULL, 'warning', 'Y', '0', 'admin', '2020-04-23 16:32:15', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (193, 1, '快速充币', 'FastBalanceController.recharge', 't_interface_name', NULL, 'warning', 'Y', '0', 'admin', '2020-04-23 16:33:09', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (194, 1, '实名认证', 'MemberController.setAuthen', 't_interface_name', NULL, 'warning', 'Y', '0', 'admin', '2020-04-23 16:35:01', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (195, 1, '申请成为商家', 'MemberController.setStore', 't_interface_name', NULL, 'warning', 'Y', '0', 'admin', '2020-04-23 16:35:39', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (196, 1, '委托交易', 'EntrustController.setEntrust', 't_interface_name', NULL, 'warning', 'Y', '0', 'admin', '2020-04-23 16:43:19', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (197, 1, '撤销委托', 'EntrustController.closeEntrust', 't_interface_name', NULL, 'warning', 'Y', '0', 'admin', '2020-04-23 16:43:41', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (198, 1, '交易/开多/开空', 'ContractOrderController.setContractOrder', 't_interface_name', NULL, 'warning', 'Y', '0', 'admin', '2020-04-23 16:50:12', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (199, 1, '平仓', 'ContractOrderController.setOrderMatch', 't_interface_name', NULL, 'warning', 'Y', '0', 'admin', '2020-04-23 16:50:45', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (200, 1, '一键平仓', 'ContractOrderController.setAllContractMatch', 't_interface_name', NULL, 'warning', 'Y', '0', 'admin', '2020-04-23 16:51:06', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (201, 1, '撤销合约委托单', 'ContractOrderController.closeContractOrder', 't_interface_name', NULL, 'warning', 'Y', '0', 'admin', '2020-04-23 16:51:41', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (202, 1, '开启', 'OPEN', 'open_close', '', 'primary', 'Y', '0', 'admin', '2020-05-18 10:13:00', 'admin', '2020-05-18 10:13:20', '');
INSERT INTO `sys_dict_data` VALUES (203, 2, '关闭', 'CLOSE', 'open_close', NULL, 'danger', 'Y', '0', 'admin', '2020-05-18 10:13:13', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (208, 2, '排队', 'WAIT', 'pe_status', '', '', 'Y', '0', 'admin', '2020-07-24 12:59:09', 'admin', '2020-07-24 15:43:56', '');
INSERT INTO `sys_dict_data` VALUES (209, 1, '正在进行', 'ING', 'pe_status', NULL, NULL, 'Y', '0', 'admin', '2020-07-24 12:59:36', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (210, 3, '结束', 'PASS', 'pe_status', '', '', 'Y', '0', 'admin', '2020-07-24 12:59:53', 'admin', '2020-07-24 15:44:05', '');
INSERT INTO `sys_dict_data` VALUES (211, 1, '平台入金账号', 'MAIN_DEPOSIT_ACCOUNT', 't_user_status', NULL, NULL, 'Y', '0', 'admin', '2020-07-24 15:09:56', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (212, 1, '平台出金账号', 'MAIN_WITHDRAW_ACCOUNT', 't_user_status', NULL, NULL, 'Y', '0', 'admin', '2020-07-24 15:10:50', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (213, 1, '链上转账中', 'WITHDRAW_CHAIN_ING', 'withdraw_status', '', '', 'Y', '0', 'admin', '2020-07-24 16:41:00', 'admin', '2020-07-24 16:41:38', '');
INSERT INTO `sys_dict_data` VALUES (214, 1, '提现完成', 'WITHDRAW_SUCCESS', 'withdraw_status', NULL, NULL, 'Y', '0', 'admin', '2020-07-24 16:41:23', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (215, 1, '项目方', 'PROJECT_BANNER', 'bannerType', '', 'default', 'Y', '0', 'admin', '2020-07-27 22:14:33', 'admin', '2020-07-28 10:01:23', '');
INSERT INTO `sys_dict_data` VALUES (216, 2, '主页', 'DATA_BANNER', 'bannerType', '', 'default', 'Y', '0', 'admin', '2020-07-27 22:14:48', 'admin', '2020-07-28 10:01:33', '');
INSERT INTO `sys_dict_data` VALUES (217, 3, '邀请返佣', 'PROJECT_BANNER', 'bannerType', NULL, NULL, 'Y', '0', 'admin', '2020-07-28 10:01:57', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (218, 1, '币币', 'SPOT', 'tradeType', NULL, 'default', 'Y', '0', 'admin', '2020-07-28 10:17:00', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (219, 2, '合约', 'CONTRACT', 'tradeType', NULL, 'default', 'Y', '0', 'admin', '2020-07-28 10:17:13', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (220, 1, '充币', '1', 'isdw', NULL, 'default', 'Y', '0', 'admin', '2020-07-28 18:21:18', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (221, 2, '提币', '2', 'isdw', '', 'default', 'Y', '0', 'admin', '2020-07-28 18:21:32', 'admin', '2020-07-28 18:22:30', '');
INSERT INTO `sys_dict_data` VALUES (222, 1, '注册', 'SHARE_REG', 'SHARE_IMG', '', '', 'Y', '0', 'admin', '2020-07-31 11:43:49', 'admin', '2020-07-31 11:44:15', '');
INSERT INTO `sys_dict_data` VALUES (223, 2, '应用', 'SHARE_APP', 'SHARE_IMG', NULL, NULL, 'Y', '0', 'admin', '2020-07-31 11:44:05', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 132 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '任务状态列表');
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '任务分组列表');
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '通知类型列表');
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '通知状态列表');
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '操作类型列表');
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '登录状态列表');
INSERT INTO `sys_dict_type` VALUES (100, 'OTC类型配置', 'otc_type_config', '0', 'admin', '2020-03-30 09:33:22', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (101, 'OTC订单状态', 'otc_order_price', '0', 'admin', '2020-03-30 10:03:38', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (102, 'OTC申诉状态', 'otc_shensu_status', '0', 'admin', '2020-03-30 10:07:41', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (103, 'OTC付款方式', 'otc_pay_type', '0', 'admin', '2020-03-30 10:11:28', 'admin', '2020-03-30 10:11:38', '');
INSERT INTO `sys_dict_type` VALUES (104, 'OTC广告状态', 'otc_order_status', '0', 'admin', '2020-03-30 10:53:31', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (105, 'OTC身份证认证', 'otc_card_state', '0', 'admin', '2020-03-31 09:19:04', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (106, '提币审核', 'tibishenhe', '0', 'admin', '2020-03-31 18:10:09', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (107, '国际化语言', 'language', '0', 'admin', '2020-04-01 17:17:59', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (108, '公告类型', 'gong_gao_type', '0', 'admin', '2020-04-01 17:20:26', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (109, '平仓类型', 'pingcang_type', '0', 'admin', '2020-04-03 14:48:44', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (110, '合约交易类型', 'heyue_jiaoyi_type', '0', 'admin', '2020-04-03 14:50:22', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (111, '合约状态', 'heyue_status', '0', 'admin', '2020-04-03 14:53:01', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (112, '订单类型', 'heyue_order_type', '0', 'admin', '2020-04-03 14:53:55', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (113, '账户状态', 't_user_status', '0', 'admin', '2020-04-14 17:39:49', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (114, '0不是1是', 'shifou', '0', 'admin', '2020-04-15 12:09:08', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (115, '币种状态', 't_pair_status', '0', 'admin', '2020-04-15 12:11:35', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (116, '币种类型', 't_pair_type', '0', 'admin', '2020-04-15 12:12:45', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (117, '合约地址', 'token_address', '0', 'admin', '2020-04-16 18:24:42', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (118, '提币币种', 'currency', '0', 'admin', '2020-04-16 18:30:40', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (119, '资产排序', 'balance_order', '0', 'admin', '2020-04-17 15:44:38', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (120, '短信类型', 't_msg_record', '0', 'admin', '2020-04-21 11:52:17', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (121, 'OTC商家认证', 'otc_store_state', '0', 'admin', '2020-04-23 09:44:03', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (122, '接口名称', 't_interface_name', '0', 'admin', '2020-04-23 15:43:13', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (123, '开启关闭', 'open_close', '0', 'admin', '2020-05-18 10:12:42', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (124, '机器人币种', 'robot_pair', '0', 'admin', '2020-05-18 10:25:30', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (125, 'eth地址', 'sys_eth_address', '0', 'admin', '2020-07-24 10:16:11', '', NULL, '1：转币地址，2：收币地址');
INSERT INTO `sys_dict_type` VALUES (126, '私募项目状态', 'pe_status', '0', 'admin', '2020-07-24 12:57:39', 'admin', '2020-07-24 15:28:29', '');
INSERT INTO `sys_dict_type` VALUES (127, '用户提币状态', 'withdraw_status', '0', 'admin', '2020-07-24 16:40:21', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (128, 'banner图类型', 'bannerType', '0', 'admin', '2020-07-27 22:13:55', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (129, '交易类型', 'tradeType', '0', 'admin', '2020-07-28 10:16:40', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (130, '充提开关', 'isdw', '0', 'admin', '2020-07-28 18:20:56', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (131, '分享图片', 'SHARE_IMG', '0', 'admin', '2020-07-31 11:42:50', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_dictionaries
-- ----------------------------
DROP TABLE IF EXISTS `sys_dictionaries`;
CREATE TABLE `sys_dictionaries`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '序列号',
  `ukey` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '键',
  `uvalue` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '值',
  `info` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述信息',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dictionaries
-- ----------------------------
INSERT INTO `sys_dictionaries` VALUES (1, 'transaction_brokerage_number', '5000', '分佣上限值', '2020-01-15 14:20:05', '2020-04-11 18:21:10');
INSERT INTO `sys_dictionaries` VALUES (2, 'transaction_brokerage_ratio', '30', '分佣比例', '2020-01-17 19:36:25', NULL);
INSERT INTO `sys_dictionaries` VALUES (8, 'eth_extract_min_limit', '1', 'ETH提币最小限额', '2020-02-04 13:48:08', '2020-07-29 18:55:31');
INSERT INTO `sys_dictionaries` VALUES (11, 'usdt_extract_handling', '1', 'USDT提币每笔手续费', '2020-03-01 13:13:57', '2020-07-29 18:55:22');
INSERT INTO `sys_dictionaries` VALUES (13, 'eth_extract_handling', '0.005', 'ETH提币每笔手续费', '2020-03-01 13:14:07', '2020-07-29 18:55:40');
INSERT INTO `sys_dictionaries` VALUES (16, 'usdt_extract_min_limit', '200', 'USDT提取最小限额', '2020-03-03 09:46:42', '2020-07-29 18:55:49');

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '定时任务调度表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job` VALUES (1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '0/10 * * * * ?', '3', '1', '1', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_job` VALUES (2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_job` VALUES (3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3', '1', '1', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log`  (
  `job_log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志信息',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '异常信息',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 204 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '定时任务调度日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------
INSERT INTO `sys_job_log` VALUES (1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '系统默认（无参） 总共耗时：15毫秒', '0', '', '2020-04-02 17:16:50');
INSERT INTO `sys_job_log` VALUES (2, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '系统默认（无参） 总共耗时：2毫秒', '0', '', '2020-04-15 11:11:01');
INSERT INTO `sys_job_log` VALUES (3, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:11:08');
INSERT INTO `sys_job_log` VALUES (4, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:11:15');
INSERT INTO `sys_job_log` VALUES (5, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:11:30');
INSERT INTO `sys_job_log` VALUES (6, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:11:45');
INSERT INTO `sys_job_log` VALUES (7, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:12:00');
INSERT INTO `sys_job_log` VALUES (8, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:12:15');
INSERT INTO `sys_job_log` VALUES (9, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:12:30');
INSERT INTO `sys_job_log` VALUES (10, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:12:45');
INSERT INTO `sys_job_log` VALUES (11, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:13:00');
INSERT INTO `sys_job_log` VALUES (12, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:13:15');
INSERT INTO `sys_job_log` VALUES (13, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:13:30');
INSERT INTO `sys_job_log` VALUES (14, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:13:45');
INSERT INTO `sys_job_log` VALUES (15, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:14:00');
INSERT INTO `sys_job_log` VALUES (16, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:14:15');
INSERT INTO `sys_job_log` VALUES (17, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:14:30');
INSERT INTO `sys_job_log` VALUES (18, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:14:45');
INSERT INTO `sys_job_log` VALUES (19, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:15:00');
INSERT INTO `sys_job_log` VALUES (20, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:15:15');
INSERT INTO `sys_job_log` VALUES (21, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:15:30');
INSERT INTO `sys_job_log` VALUES (22, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:15:45');
INSERT INTO `sys_job_log` VALUES (23, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:16:00');
INSERT INTO `sys_job_log` VALUES (24, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:16:15');
INSERT INTO `sys_job_log` VALUES (25, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:16:30');
INSERT INTO `sys_job_log` VALUES (26, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:16:45');
INSERT INTO `sys_job_log` VALUES (27, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:17:00');
INSERT INTO `sys_job_log` VALUES (28, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:17:15');
INSERT INTO `sys_job_log` VALUES (29, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:17:30');
INSERT INTO `sys_job_log` VALUES (30, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:17:45');
INSERT INTO `sys_job_log` VALUES (31, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:18:00');
INSERT INTO `sys_job_log` VALUES (32, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:18:15');
INSERT INTO `sys_job_log` VALUES (33, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:18:30');
INSERT INTO `sys_job_log` VALUES (34, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:18:45');
INSERT INTO `sys_job_log` VALUES (35, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:19:00');
INSERT INTO `sys_job_log` VALUES (36, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:19:15');
INSERT INTO `sys_job_log` VALUES (37, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:19:30');
INSERT INTO `sys_job_log` VALUES (38, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:19:45');
INSERT INTO `sys_job_log` VALUES (39, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:20:00');
INSERT INTO `sys_job_log` VALUES (40, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:20:15');
INSERT INTO `sys_job_log` VALUES (41, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:20:30');
INSERT INTO `sys_job_log` VALUES (42, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:20:45');
INSERT INTO `sys_job_log` VALUES (43, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:21:00');
INSERT INTO `sys_job_log` VALUES (44, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:21:15');
INSERT INTO `sys_job_log` VALUES (45, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:21:30');
INSERT INTO `sys_job_log` VALUES (46, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:21:45');
INSERT INTO `sys_job_log` VALUES (47, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:22:00');
INSERT INTO `sys_job_log` VALUES (48, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:22:15');
INSERT INTO `sys_job_log` VALUES (49, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:22:30');
INSERT INTO `sys_job_log` VALUES (50, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:22:45');
INSERT INTO `sys_job_log` VALUES (51, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:23:00');
INSERT INTO `sys_job_log` VALUES (52, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:23:15');
INSERT INTO `sys_job_log` VALUES (53, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:23:30');
INSERT INTO `sys_job_log` VALUES (54, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:23:45');
INSERT INTO `sys_job_log` VALUES (55, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:24:00');
INSERT INTO `sys_job_log` VALUES (56, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:24:15');
INSERT INTO `sys_job_log` VALUES (57, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:24:30');
INSERT INTO `sys_job_log` VALUES (58, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:24:45');
INSERT INTO `sys_job_log` VALUES (59, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:25:00');
INSERT INTO `sys_job_log` VALUES (60, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:25:15');
INSERT INTO `sys_job_log` VALUES (61, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:25:30');
INSERT INTO `sys_job_log` VALUES (62, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:25:45');
INSERT INTO `sys_job_log` VALUES (63, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:26:00');
INSERT INTO `sys_job_log` VALUES (64, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:26:15');
INSERT INTO `sys_job_log` VALUES (65, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:26:30');
INSERT INTO `sys_job_log` VALUES (66, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:26:45');
INSERT INTO `sys_job_log` VALUES (67, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:27:00');
INSERT INTO `sys_job_log` VALUES (68, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:27:15');
INSERT INTO `sys_job_log` VALUES (69, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:27:30');
INSERT INTO `sys_job_log` VALUES (70, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:27:45');
INSERT INTO `sys_job_log` VALUES (71, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:28:00');
INSERT INTO `sys_job_log` VALUES (72, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:28:15');
INSERT INTO `sys_job_log` VALUES (73, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:28:30');
INSERT INTO `sys_job_log` VALUES (74, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:28:45');
INSERT INTO `sys_job_log` VALUES (75, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:29:00');
INSERT INTO `sys_job_log` VALUES (76, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:29:15');
INSERT INTO `sys_job_log` VALUES (77, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:29:30');
INSERT INTO `sys_job_log` VALUES (78, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:29:45');
INSERT INTO `sys_job_log` VALUES (79, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:30:00');
INSERT INTO `sys_job_log` VALUES (80, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:30:15');
INSERT INTO `sys_job_log` VALUES (81, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:30:30');
INSERT INTO `sys_job_log` VALUES (82, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:30:45');
INSERT INTO `sys_job_log` VALUES (83, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:31:00');
INSERT INTO `sys_job_log` VALUES (84, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:31:15');
INSERT INTO `sys_job_log` VALUES (85, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:31:30');
INSERT INTO `sys_job_log` VALUES (86, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:31:45');
INSERT INTO `sys_job_log` VALUES (87, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:32:00');
INSERT INTO `sys_job_log` VALUES (88, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:32:15');
INSERT INTO `sys_job_log` VALUES (89, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:32:30');
INSERT INTO `sys_job_log` VALUES (90, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:32:45');
INSERT INTO `sys_job_log` VALUES (91, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:33:00');
INSERT INTO `sys_job_log` VALUES (92, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:33:15');
INSERT INTO `sys_job_log` VALUES (93, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:33:30');
INSERT INTO `sys_job_log` VALUES (94, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:33:45');
INSERT INTO `sys_job_log` VALUES (95, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:34:00');
INSERT INTO `sys_job_log` VALUES (96, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:34:15');
INSERT INTO `sys_job_log` VALUES (97, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:34:30');
INSERT INTO `sys_job_log` VALUES (98, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:34:45');
INSERT INTO `sys_job_log` VALUES (99, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:35:00');
INSERT INTO `sys_job_log` VALUES (100, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:35:15');
INSERT INTO `sys_job_log` VALUES (101, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:35:30');
INSERT INTO `sys_job_log` VALUES (102, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:35:45');
INSERT INTO `sys_job_log` VALUES (103, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:36:00');
INSERT INTO `sys_job_log` VALUES (104, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:36:15');
INSERT INTO `sys_job_log` VALUES (105, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:36:30');
INSERT INTO `sys_job_log` VALUES (106, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:36:45');
INSERT INTO `sys_job_log` VALUES (107, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:37:00');
INSERT INTO `sys_job_log` VALUES (108, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:37:15');
INSERT INTO `sys_job_log` VALUES (109, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:37:30');
INSERT INTO `sys_job_log` VALUES (110, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:37:45');
INSERT INTO `sys_job_log` VALUES (111, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:38:00');
INSERT INTO `sys_job_log` VALUES (112, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:38:15');
INSERT INTO `sys_job_log` VALUES (113, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:38:30');
INSERT INTO `sys_job_log` VALUES (114, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:38:45');
INSERT INTO `sys_job_log` VALUES (115, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:39:00');
INSERT INTO `sys_job_log` VALUES (116, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:39:15');
INSERT INTO `sys_job_log` VALUES (117, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:39:30');
INSERT INTO `sys_job_log` VALUES (118, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:39:45');
INSERT INTO `sys_job_log` VALUES (119, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:40:00');
INSERT INTO `sys_job_log` VALUES (120, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:40:15');
INSERT INTO `sys_job_log` VALUES (121, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:40:30');
INSERT INTO `sys_job_log` VALUES (122, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:40:45');
INSERT INTO `sys_job_log` VALUES (123, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:41:00');
INSERT INTO `sys_job_log` VALUES (124, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:41:15');
INSERT INTO `sys_job_log` VALUES (125, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:41:30');
INSERT INTO `sys_job_log` VALUES (126, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:41:45');
INSERT INTO `sys_job_log` VALUES (127, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:42:00');
INSERT INTO `sys_job_log` VALUES (128, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:42:15');
INSERT INTO `sys_job_log` VALUES (129, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:42:30');
INSERT INTO `sys_job_log` VALUES (130, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:42:45');
INSERT INTO `sys_job_log` VALUES (131, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:43:00');
INSERT INTO `sys_job_log` VALUES (132, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:43:15');
INSERT INTO `sys_job_log` VALUES (133, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:43:30');
INSERT INTO `sys_job_log` VALUES (134, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:43:45');
INSERT INTO `sys_job_log` VALUES (135, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:44:00');
INSERT INTO `sys_job_log` VALUES (136, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:44:15');
INSERT INTO `sys_job_log` VALUES (137, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:44:30');
INSERT INTO `sys_job_log` VALUES (138, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:44:45');
INSERT INTO `sys_job_log` VALUES (139, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:45:00');
INSERT INTO `sys_job_log` VALUES (140, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:45:15');
INSERT INTO `sys_job_log` VALUES (141, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:45:30');
INSERT INTO `sys_job_log` VALUES (142, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:45:45');
INSERT INTO `sys_job_log` VALUES (143, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:46:00');
INSERT INTO `sys_job_log` VALUES (144, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:46:15');
INSERT INTO `sys_job_log` VALUES (145, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:46:30');
INSERT INTO `sys_job_log` VALUES (146, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:46:45');
INSERT INTO `sys_job_log` VALUES (147, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:47:00');
INSERT INTO `sys_job_log` VALUES (148, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:47:15');
INSERT INTO `sys_job_log` VALUES (149, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:47:30');
INSERT INTO `sys_job_log` VALUES (150, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:47:45');
INSERT INTO `sys_job_log` VALUES (151, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:48:00');
INSERT INTO `sys_job_log` VALUES (152, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:48:15');
INSERT INTO `sys_job_log` VALUES (153, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:48:30');
INSERT INTO `sys_job_log` VALUES (154, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:48:45');
INSERT INTO `sys_job_log` VALUES (155, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:49:00');
INSERT INTO `sys_job_log` VALUES (156, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:49:15');
INSERT INTO `sys_job_log` VALUES (157, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:49:30');
INSERT INTO `sys_job_log` VALUES (158, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:49:45');
INSERT INTO `sys_job_log` VALUES (159, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:50:00');
INSERT INTO `sys_job_log` VALUES (160, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:50:15');
INSERT INTO `sys_job_log` VALUES (161, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:50:30');
INSERT INTO `sys_job_log` VALUES (162, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:50:45');
INSERT INTO `sys_job_log` VALUES (163, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:51:00');
INSERT INTO `sys_job_log` VALUES (164, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:51:15');
INSERT INTO `sys_job_log` VALUES (165, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:51:30');
INSERT INTO `sys_job_log` VALUES (166, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:51:45');
INSERT INTO `sys_job_log` VALUES (167, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:52:00');
INSERT INTO `sys_job_log` VALUES (168, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:52:15');
INSERT INTO `sys_job_log` VALUES (169, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:52:30');
INSERT INTO `sys_job_log` VALUES (170, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:52:45');
INSERT INTO `sys_job_log` VALUES (171, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:53:00');
INSERT INTO `sys_job_log` VALUES (172, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:53:15');
INSERT INTO `sys_job_log` VALUES (173, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:53:30');
INSERT INTO `sys_job_log` VALUES (174, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:53:45');
INSERT INTO `sys_job_log` VALUES (175, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:54:00');
INSERT INTO `sys_job_log` VALUES (176, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:54:15');
INSERT INTO `sys_job_log` VALUES (177, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:54:30');
INSERT INTO `sys_job_log` VALUES (178, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:54:45');
INSERT INTO `sys_job_log` VALUES (179, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:55:00');
INSERT INTO `sys_job_log` VALUES (180, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:55:15');
INSERT INTO `sys_job_log` VALUES (181, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:55:30');
INSERT INTO `sys_job_log` VALUES (182, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:55:45');
INSERT INTO `sys_job_log` VALUES (183, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:56:00');
INSERT INTO `sys_job_log` VALUES (184, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:56:15');
INSERT INTO `sys_job_log` VALUES (185, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:56:30');
INSERT INTO `sys_job_log` VALUES (186, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:56:45');
INSERT INTO `sys_job_log` VALUES (187, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-04-15 13:57:00');
INSERT INTO `sys_job_log` VALUES (188, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:57:15');
INSERT INTO `sys_job_log` VALUES (189, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:57:30');
INSERT INTO `sys_job_log` VALUES (190, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:57:45');
INSERT INTO `sys_job_log` VALUES (191, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:58:00');
INSERT INTO `sys_job_log` VALUES (192, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:58:15');
INSERT INTO `sys_job_log` VALUES (193, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:58:30');
INSERT INTO `sys_job_log` VALUES (194, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：0毫秒', '0', '', '2020-04-15 13:58:45');
INSERT INTO `sys_job_log` VALUES (195, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '系统默认（无参） 总共耗时：0毫秒', '0', '', '2020-04-15 14:11:11');
INSERT INTO `sys_job_log` VALUES (196, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '系统默认（无参） 总共耗时：2毫秒', '0', '', '2020-04-16 15:13:01');
INSERT INTO `sys_job_log` VALUES (197, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '系统默认（无参） 总共耗时：0毫秒', '0', '', '2020-04-16 18:25:41');
INSERT INTO `sys_job_log` VALUES (198, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '系统默认（多参） 总共耗时：1毫秒', '0', '', '2020-04-16 18:39:41');
INSERT INTO `sys_job_log` VALUES (199, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '系统默认（多参） 总共耗时：3毫秒', '0', '', '2020-04-17 11:38:00');
INSERT INTO `sys_job_log` VALUES (200, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：2毫秒', '0', '', '2020-05-05 16:22:40');
INSERT INTO `sys_job_log` VALUES (201, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '系统默认（多参） 总共耗时：2毫秒', '0', '', '2020-07-29 14:21:01');
INSERT INTO `sys_job_log` VALUES (202, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-07-29 15:39:45');
INSERT INTO `sys_job_log` VALUES (203, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：1毫秒', '0', '', '2020-07-31 14:19:47');

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor`  (
  `info_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `login_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '登录账号',
  `ipaddr` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '操作系统',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '提示消息',
  `login_time` datetime NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 143 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统访问记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------
INSERT INTO `sys_logininfor` VALUES (1, 'admin', '222.90.67.10', 'XX XX', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-08-26 10:18:34');
INSERT INTO `sys_logininfor` VALUES (2, 'admin', '222.90.67.10', 'XX XX', 'Chrome', 'Windows 10', '0', '登录成功', '2020-08-26 10:21:48');
INSERT INTO `sys_logininfor` VALUES (3, 'admin', '222.90.67.10', 'XX XX', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-08-26 10:41:35');
INSERT INTO `sys_logininfor` VALUES (4, 'admin', '222.90.67.10', 'XX XX', 'Chrome', 'Windows 10', '0', '登录成功', '2020-08-26 11:02:43');
INSERT INTO `sys_logininfor` VALUES (5, 'admin', '111.21.63.107', 'XX XX', 'Chrome', 'Windows 10', '0', '登录成功', '2020-08-26 11:46:59');
INSERT INTO `sys_logininfor` VALUES (6, 'admin', '113.139.240.61', 'XX XX', 'Internet Explorer 11', 'Windows 7', '0', '登录成功', '2020-08-26 12:06:56');
INSERT INTO `sys_logininfor` VALUES (7, 'admin', '111.21.63.107', 'XX XX', 'Chrome', 'Windows 10', '0', '登录成功', '2020-08-26 13:50:21');
INSERT INTO `sys_logininfor` VALUES (8, 'admin', '222.90.67.10', 'XX XX', 'Chrome', 'Windows 10', '0', '登录成功', '2020-08-26 13:51:46');
INSERT INTO `sys_logininfor` VALUES (9, 'admin', '111.21.63.107', 'XX XX', 'Chrome', 'Windows 10', '0', '登录成功', '2020-08-26 14:21:31');
INSERT INTO `sys_logininfor` VALUES (10, 'admin', '111.21.63.107', 'XX XX', 'Chrome', 'Windows 10', '0', '登录成功', '2020-08-26 16:10:40');
INSERT INTO `sys_logininfor` VALUES (11, 'admin', '111.21.63.107', 'XX XX', 'Chrome', 'Windows 10', '0', '登录成功', '2020-08-26 17:10:13');
INSERT INTO `sys_logininfor` VALUES (12, 'admin', '111.21.63.107', 'XX XX', 'Chrome', 'Windows 10', '0', '登录成功', '2020-08-26 19:56:21');
INSERT INTO `sys_logininfor` VALUES (13, 'admin', '1.86.245.215', 'XX XX', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-08-28 02:41:07');
INSERT INTO `sys_logininfor` VALUES (14, 'admin', '1.86.245.215', 'XX XX', 'Chrome 8', 'Windows 10', '0', '退出成功', '2020-08-28 02:50:29');
INSERT INTO `sys_logininfor` VALUES (15, 'admin', '1.86.245.215', 'XX XX', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-08-28 02:50:37');
INSERT INTO `sys_logininfor` VALUES (16, 'admin', '1.86.245.215', 'XX XX', 'Chrome 8', 'Windows 10', '1', '密码输入错误1次', '2020-08-28 03:19:23');
INSERT INTO `sys_logininfor` VALUES (17, 'admin', '1.86.245.215', 'XX XX', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-08-28 03:19:31');
INSERT INTO `sys_logininfor` VALUES (18, 'admin', '1.86.245.215', 'XX XX', 'Chrome 8', 'Windows 10', '0', '退出成功', '2020-08-28 03:19:45');
INSERT INTO `sys_logininfor` VALUES (19, 'admin', '1.86.245.215', 'XX XX', 'Chrome 8', 'Windows 10', '1', '密码输入错误1次', '2020-08-28 03:19:56');
INSERT INTO `sys_logininfor` VALUES (20, 'admin', '1.86.245.215', 'XX XX', 'Chrome 8', 'Windows 10', '1', '密码输入错误1次', '2020-08-28 03:53:11');
INSERT INTO `sys_logininfor` VALUES (21, 'admin', '222.90.67.10', 'XX XX', 'Chrome 8', 'Windows 10', '1', '密码输入错误1次', '2020-08-28 09:00:41');
INSERT INTO `sys_logininfor` VALUES (22, 'admin', '222.90.67.10', 'XX XX', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-08-28 09:00:47');
INSERT INTO `sys_logininfor` VALUES (23, 'admin', '222.90.67.10', 'XX XX', 'Chrome 8', 'Windows 10', '1', '密码输入错误1次', '2020-08-28 11:19:56');
INSERT INTO `sys_logininfor` VALUES (24, 'admin', '222.90.67.10', 'XX XX', 'Chrome 8', 'Windows 10', '1', '密码输入错误2次', '2020-08-28 11:21:06');
INSERT INTO `sys_logininfor` VALUES (25, 'admin', '222.90.67.10', 'XX XX', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-08-28 11:21:12');
INSERT INTO `sys_logininfor` VALUES (26, 'admin', '113.140.24.5', 'XX XX', 'Chrome', 'Windows 10', '1', '密码输入错误1次', '2020-08-28 11:31:30');
INSERT INTO `sys_logininfor` VALUES (27, 'admin', '113.140.24.5', 'XX XX', 'Chrome', 'Windows 10', '1', '密码输入错误2次', '2020-08-28 11:31:39');
INSERT INTO `sys_logininfor` VALUES (28, 'admin', '113.140.24.5', 'XX XX', 'Chrome', 'Windows 10', '1', '密码输入错误3次', '2020-08-28 11:31:43');
INSERT INTO `sys_logininfor` VALUES (29, 'admin', '113.140.24.5', 'XX XX', 'Chrome', 'Windows 10', '1', '密码输入错误4次', '2020-08-28 11:31:49');
INSERT INTO `sys_logininfor` VALUES (30, 'admin', '113.140.24.5', 'XX XX', 'Chrome', 'Windows 10', '1', '密码输入错误5次', '2020-08-28 11:31:54');
INSERT INTO `sys_logininfor` VALUES (31, 'admin', '113.140.24.5', 'XX XX', 'Chrome', 'Windows 10', '1', '密码输入错误5次，帐户锁定10分钟', '2020-08-28 11:32:02');
INSERT INTO `sys_logininfor` VALUES (32, 'admin', '113.140.24.5', 'XX XX', 'Chrome', 'Windows 10', '1', '密码输入错误5次，帐户锁定10分钟', '2020-08-28 11:40:25');
INSERT INTO `sys_logininfor` VALUES (33, 'admin', '113.140.24.5', 'XX XX', 'Chrome', 'Windows 10', '1', '密码输入错误5次，帐户锁定10分钟', '2020-08-28 11:42:13');
INSERT INTO `sys_logininfor` VALUES (34, 'admin', '113.140.24.5', 'XX XX', 'Chrome', 'Windows 10', '1', '密码输入错误5次，帐户锁定10分钟', '2020-08-28 11:44:08');
INSERT INTO `sys_logininfor` VALUES (35, 'admin', '113.140.24.5', 'XX XX', 'Chrome', 'Windows 10', '1', '密码输入错误5次，帐户锁定10分钟', '2020-08-28 11:53:03');
INSERT INTO `sys_logininfor` VALUES (36, 'admin', '113.140.24.5', 'XX XX', 'Chrome', 'Windows 10', '1', '密码输入错误5次，帐户锁定10分钟', '2020-08-28 11:53:31');
INSERT INTO `sys_logininfor` VALUES (37, 'admin', '113.140.24.5', 'XX XX', 'Chrome', 'Windows 10', '1', '密码输入错误5次，帐户锁定10分钟', '2020-08-28 11:53:34');
INSERT INTO `sys_logininfor` VALUES (38, 'admin', '111.196.183.191', 'XX XX', 'Chrome', 'Windows 7', '1', '密码输入错误1次', '2020-08-28 12:11:50');
INSERT INTO `sys_logininfor` VALUES (39, 'admin', '111.196.183.191', 'XX XX', 'Chrome', 'Windows 7', '1', '密码输入错误2次', '2020-08-28 12:12:05');
INSERT INTO `sys_logininfor` VALUES (40, 'admin', '111.196.183.191', 'XX XX', 'Chrome', 'Windows 7', '0', '登录成功', '2020-08-28 12:12:25');
INSERT INTO `sys_logininfor` VALUES (41, 'admin', '222.90.67.10', 'XX XX', 'Chrome 8', 'Windows 10', '1', '密码输入错误1次', '2020-08-28 12:21:25');
INSERT INTO `sys_logininfor` VALUES (42, 'admin', '222.90.67.10', 'XX XX', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-08-28 12:21:31');
INSERT INTO `sys_logininfor` VALUES (43, 'admin', '113.140.24.5', 'XX XX', 'Chrome', 'Windows 10', '0', '登录成功', '2020-08-28 12:26:11');
INSERT INTO `sys_logininfor` VALUES (44, 'admin', '222.90.67.10', 'XX XX', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-08-28 19:50:47');
INSERT INTO `sys_logininfor` VALUES (45, 'admin', '222.90.67.10', 'XX XX', 'Chrome 8', 'Windows 10', '1', '密码输入错误1次', '2020-08-28 19:53:46');
INSERT INTO `sys_logininfor` VALUES (46, 'admin', '113.139.246.138', 'XX XX', 'Internet Explorer 11', 'Windows 7', '1', '密码输入错误1次', '2020-08-28 21:27:51');
INSERT INTO `sys_logininfor` VALUES (47, 'admin', '113.139.246.138', 'XX XX', 'Internet Explorer 11', 'Windows 7', '1', '密码输入错误2次', '2020-08-28 21:27:54');
INSERT INTO `sys_logininfor` VALUES (48, 'admin', '113.139.246.138', 'XX XX', 'Internet Explorer 11', 'Windows 7', '1', '密码输入错误3次', '2020-08-28 21:28:01');
INSERT INTO `sys_logininfor` VALUES (49, 'admin', '113.139.246.138', 'XX XX', 'Internet Explorer 11', 'Windows 7', '1', '密码输入错误4次', '2020-08-28 21:29:18');
INSERT INTO `sys_logininfor` VALUES (50, 'admin', '113.139.246.138', 'XX XX', 'Internet Explorer 11', 'Windows 7', '1', '密码输入错误5次', '2020-08-28 21:29:32');
INSERT INTO `sys_logininfor` VALUES (51, 'admin', '113.139.246.138', 'XX XX', 'Internet Explorer 11', 'Windows 7', '1', '密码输入错误5次，帐户锁定10分钟', '2020-08-28 21:29:32');
INSERT INTO `sys_logininfor` VALUES (52, 'admin', '222.90.67.10', 'XX XX', 'Chrome 8', 'Windows 10', '1', '密码输入错误1次', '2020-08-29 08:09:08');
INSERT INTO `sys_logininfor` VALUES (53, 'admin', '222.90.67.10', 'XX XX', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-08-29 08:09:17');
INSERT INTO `sys_logininfor` VALUES (54, 'admin', '117.136.87.47', 'XX XX', 'Chrome Mobile', 'Android 1.x', '1', '密码输入错误1次', '2020-08-30 16:46:02');
INSERT INTO `sys_logininfor` VALUES (55, 'admin', '117.136.87.47', 'XX XX', 'Chrome Mobile', 'Android 1.x', '0', '登录成功', '2020-08-30 16:46:16');
INSERT INTO `sys_logininfor` VALUES (56, 'admin', '39.128.188.244', 'XX XX', 'Chrome Mobile', 'Android Mobile', '1', '密码输入错误1次', '2020-08-30 17:49:19');
INSERT INTO `sys_logininfor` VALUES (57, 'admin', '39.128.188.244', 'XX XX', 'Mobile Safari', 'Android Mobile', '1', '密码输入错误2次', '2020-08-30 17:50:04');
INSERT INTO `sys_logininfor` VALUES (58, 'admin', '39.128.188.244', 'XX XX', 'Mobile Safari', 'Android Mobile', '1', '密码输入错误3次', '2020-08-30 17:50:08');
INSERT INTO `sys_logininfor` VALUES (59, 'admin', '39.128.188.244', 'XX XX', 'Mobile Safari', 'Android Mobile', '0', '登录成功', '2020-08-30 17:50:41');
INSERT INTO `sys_logininfor` VALUES (60, 'admin', '39.128.188.244', 'XX XX', 'Mobile Safari', 'Android Mobile', '0', '登录成功', '2020-08-30 17:53:13');
INSERT INTO `sys_logininfor` VALUES (61, 'admin', '39.128.188.244', 'XX XX', 'Mobile Safari', 'Android Mobile', '0', '登录成功', '2020-08-30 18:06:49');
INSERT INTO `sys_logininfor` VALUES (62, 'admin', '39.128.188.131', 'XX XX', 'Chrome Mobile', 'Android 1.x', '1', '密码输入错误1次', '2020-08-30 20:32:25');
INSERT INTO `sys_logininfor` VALUES (63, 'admin', '39.128.188.131', 'XX XX', 'Chrome Mobile', 'Android 1.x', '0', '登录成功', '2020-08-30 20:33:27');
INSERT INTO `sys_logininfor` VALUES (64, 'admin', '113.139.246.9', 'XX XX', 'Internet Explorer 11', 'Windows 7', '0', '登录成功', '2020-08-30 21:13:38');
INSERT INTO `sys_logininfor` VALUES (65, 'admin', '39.128.188.205', 'XX XX', 'Chrome Mobile', 'Android 1.x', '1', '密码输入错误1次', '2020-08-30 22:10:49');
INSERT INTO `sys_logininfor` VALUES (66, 'admin', '39.128.188.205', 'XX XX', 'Chrome Mobile', 'Android 1.x', '1', '密码输入错误2次', '2020-08-30 22:10:52');
INSERT INTO `sys_logininfor` VALUES (67, 'admin', '39.128.188.205', 'XX XX', 'Chrome Mobile', 'Android 1.x', '0', '登录成功', '2020-08-30 22:11:06');
INSERT INTO `sys_logininfor` VALUES (68, 'admin', '183.225.180.39', 'XX XX', 'Chrome Mobile', 'Android 1.x', '1', '密码输入错误1次', '2020-08-31 00:42:20');
INSERT INTO `sys_logininfor` VALUES (69, 'admin', '183.225.180.39', 'XX XX', 'Chrome Mobile', 'Android 1.x', '1', '密码输入错误2次', '2020-08-31 00:42:28');
INSERT INTO `sys_logininfor` VALUES (70, 'admin', '183.225.180.39', 'XX XX', 'Chrome Mobile', 'Android 1.x', '0', '登录成功', '2020-08-31 00:42:43');
INSERT INTO `sys_logininfor` VALUES (71, 'admin', '183.225.180.39', 'XX XX', 'Chrome Mobile', 'Android 1.x', '0', '登录成功', '2020-08-31 00:51:21');
INSERT INTO `sys_logininfor` VALUES (72, 'admin', '117.136.73.185', 'XX XX', 'Chrome Mobile', 'Android 1.x', '0', '登录成功', '2020-08-31 01:13:56');
INSERT INTO `sys_logininfor` VALUES (73, 'admin', '117.136.73.185', 'XX XX', 'Chrome Mobile', 'Android 1.x', '0', '登录成功', '2020-08-31 01:20:27');
INSERT INTO `sys_logininfor` VALUES (74, 'admin', '117.136.73.185', 'XX XX', 'Chrome Mobile', 'Android 1.x', '0', '登录成功', '2020-08-31 01:26:28');
INSERT INTO `sys_logininfor` VALUES (75, 'admin', '117.136.73.185', 'XX XX', 'Chrome Mobile', 'Android 1.x', '0', '登录成功', '2020-08-31 02:05:22');
INSERT INTO `sys_logininfor` VALUES (76, 'admin', '117.136.73.185', 'XX XX', 'Chrome Mobile', 'Android 1.x', '0', '登录成功', '2020-08-31 03:02:10');
INSERT INTO `sys_logininfor` VALUES (77, 'admin', '117.136.73.113', 'XX XX', 'Chrome Mobile', 'Android 1.x', '0', '登录成功', '2020-08-31 13:52:44');
INSERT INTO `sys_logininfor` VALUES (78, 'admin', '1.86.3.183', 'XX XX', 'Chrome 8', 'Windows 7', '1', '密码输入错误1次', '2020-08-31 14:08:00');
INSERT INTO `sys_logininfor` VALUES (79, 'admin', '1.86.3.183', 'XX XX', 'Chrome 8', 'Windows 7', '0', '登录成功', '2020-08-31 14:08:10');
INSERT INTO `sys_logininfor` VALUES (80, 'admin', '183.225.180.53', 'XX XX', 'Chrome Mobile', 'Android 1.x', '0', '登录成功', '2020-08-31 16:57:41');
INSERT INTO `sys_logininfor` VALUES (81, 'admin', '39.128.188.161', 'XX XX', 'Chrome Mobile', 'Android 1.x', '0', '登录成功', '2020-08-31 19:04:34');
INSERT INTO `sys_logininfor` VALUES (82, 'admin', '1.86.244.34', 'XX XX', 'Chrome 8', 'Windows 10', '1', '密码输入错误1次', '2020-09-01 22:11:44');
INSERT INTO `sys_logininfor` VALUES (83, 'admin', '1.86.244.34', 'XX XX', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-09-01 22:11:51');
INSERT INTO `sys_logininfor` VALUES (84, 'admin', '106.61.40.4', 'XX XX', 'Chrome', 'Windows 7', '1', '密码输入错误1次', '2020-09-01 23:47:29');
INSERT INTO `sys_logininfor` VALUES (85, 'admin', '106.61.40.4', 'XX XX', 'Chrome', 'Windows 7', '1', '密码输入错误2次', '2020-09-01 23:47:34');
INSERT INTO `sys_logininfor` VALUES (86, 'admin', '106.61.40.4', 'XX XX', 'Chrome', 'Windows 7', '1', '密码输入错误3次', '2020-09-01 23:47:46');
INSERT INTO `sys_logininfor` VALUES (87, 'admin', '106.61.40.4', 'XX XX', 'Chrome', 'Windows 7', '0', '登录成功', '2020-09-01 23:48:08');
INSERT INTO `sys_logininfor` VALUES (88, 'admin', '106.61.40.4', 'XX XX', 'Chrome', 'Windows 7', '1', '密码输入错误1次', '2020-09-02 00:34:42');
INSERT INTO `sys_logininfor` VALUES (89, 'admin', '106.61.40.4', 'XX XX', 'Chrome', 'Windows 7', '0', '登录成功', '2020-09-02 00:34:53');
INSERT INTO `sys_logininfor` VALUES (90, 'admin', '106.61.33.6', 'XX XX', 'Chrome', 'Windows 7', '1', '密码输入错误1次', '2020-09-02 09:21:58');
INSERT INTO `sys_logininfor` VALUES (91, 'admin', '106.61.33.6', 'XX XX', 'Chrome', 'Windows 7', '0', '登录成功', '2020-09-02 09:22:04');
INSERT INTO `sys_logininfor` VALUES (92, 'admin', '223.104.239.176', 'XX XX', 'Chrome Mobile', 'Android 1.x', '0', '登录成功', '2020-09-02 10:00:48');
INSERT INTO `sys_logininfor` VALUES (93, 'admin', '103.75.117.65', 'XX XX', 'Chrome 8', 'Windows 7', '1', '密码输入错误1次', '2020-09-02 18:55:35');
INSERT INTO `sys_logininfor` VALUES (94, 'admin', '103.75.117.65', 'XX XX', 'Chrome 8', 'Windows 7', '0', '登录成功', '2020-09-02 18:55:40');
INSERT INTO `sys_logininfor` VALUES (95, 'admin', '103.75.117.65', 'XX XX', 'Chrome 8', 'Windows 7', '1', '密码输入错误1次', '2020-09-03 10:51:17');
INSERT INTO `sys_logininfor` VALUES (96, 'admin', '103.75.117.65', 'XX XX', 'Chrome 8', 'Windows 7', '0', '登录成功', '2020-09-03 10:51:21');
INSERT INTO `sys_logininfor` VALUES (97, 'admin', '103.75.117.65', 'XX XX', 'Chrome 8', 'Windows 7', '0', '登录成功', '2020-09-03 11:10:29');
INSERT INTO `sys_logininfor` VALUES (98, 'admin', '103.75.117.65', 'XX XX', 'Chrome 8', 'Windows 7', '0', '登录成功', '2020-09-03 14:55:36');
INSERT INTO `sys_logininfor` VALUES (99, 'admin', '103.75.117.65', 'XX XX', 'Chrome 8', 'Windows 7', '0', '登录成功', '2020-09-03 16:26:36');
INSERT INTO `sys_logininfor` VALUES (100, 'admin', '103.75.117.65', 'XX XX', 'Chrome 8', 'Windows 7', '0', '登录成功', '2020-09-03 16:49:30');
INSERT INTO `sys_logininfor` VALUES (101, 'admin', '113.52.132.216', 'XX XX', 'Chrome 8', 'Windows 7', '0', '登录成功', '2020-09-04 08:46:31');
INSERT INTO `sys_logininfor` VALUES (102, 'admin', '154.218.6.57', 'XX XX', 'Chrome 8', 'Windows 10', '1', '密码输入错误1次', '2020-09-05 15:50:25');
INSERT INTO `sys_logininfor` VALUES (103, 'admin', '154.218.6.57', 'XX XX', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-09-05 15:50:29');
INSERT INTO `sys_logininfor` VALUES (104, 'admin', '36.45.12.229', 'XX XX', 'Apple WebKit', 'Mac OS X (iPhone)', '1', '密码输入错误1次', '2020-09-05 16:37:15');
INSERT INTO `sys_logininfor` VALUES (105, 'admin', '36.45.12.229', 'XX XX', 'Apple WebKit', 'Mac OS X (iPhone)', '1', '密码输入错误2次', '2020-09-05 16:37:17');
INSERT INTO `sys_logininfor` VALUES (106, 'admin', '223.104.22.1', 'XX XX', 'Chrome Mobile', 'Android 1.x', '1', '密码输入错误1次', '2020-09-07 20:05:47');
INSERT INTO `sys_logininfor` VALUES (107, 'admin', '223.104.22.1', 'XX XX', 'Chrome Mobile', 'Android 1.x', '0', '登录成功', '2020-09-07 20:06:31');
INSERT INTO `sys_logininfor` VALUES (108, 'admin', '223.104.22.1', 'XX XX', 'Chrome Mobile', 'Android 1.x', '0', '登录成功', '2020-09-07 21:18:26');
INSERT INTO `sys_logininfor` VALUES (109, 'admin', '154.218.6.57', 'XX XX', 'Chrome 8', 'Windows 10', '1', '密码输入错误1次', '2020-09-08 12:08:21');
INSERT INTO `sys_logininfor` VALUES (110, 'admin', '154.218.6.57', 'XX XX', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-09-08 12:08:30');
INSERT INTO `sys_logininfor` VALUES (111, 'admin', '154.218.6.57', 'XX XX', 'Chrome 8', 'Windows 10', '1', '密码输入错误1次', '2020-09-08 12:15:30');
INSERT INTO `sys_logininfor` VALUES (112, 'admin', '154.218.6.57', 'XX XX', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-09-08 12:15:37');
INSERT INTO `sys_logininfor` VALUES (113, 'admin', '113.52.132.216', 'XX XX', 'Chrome 8', 'Windows 7', '0', '登录成功', '2020-09-08 12:50:14');
INSERT INTO `sys_logininfor` VALUES (114, 'admin', '154.218.6.57', 'XX XX', 'Chrome 8', 'Windows 10', '1', '密码输入错误1次', '2020-09-08 14:17:52');
INSERT INTO `sys_logininfor` VALUES (115, 'admin', '154.218.6.57', 'XX XX', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-09-08 14:17:56');
INSERT INTO `sys_logininfor` VALUES (116, 'admin', '113.140.24.2', 'XX XX', 'Chrome 8', 'Mac OS X', '1', '密码输入错误1次', '2020-09-08 14:55:50');
INSERT INTO `sys_logininfor` VALUES (117, 'admin', '113.140.24.2', 'XX XX', 'Chrome 8', 'Mac OS X', '0', '登录成功', '2020-09-08 14:56:07');
INSERT INTO `sys_logininfor` VALUES (118, 'admin', '171.106.203.194', 'XX XX', 'Chrome Mobile', 'Android 1.x', '0', '登录成功', '2020-09-10 10:12:20');
INSERT INTO `sys_logininfor` VALUES (119, 'admin', '154.218.6.57', 'XX XX', 'Chrome 8', 'Windows 10', '1', '密码输入错误1次', '2020-09-10 11:35:42');
INSERT INTO `sys_logininfor` VALUES (120, 'admin', '154.218.6.57', 'XX XX', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-09-10 11:35:47');
INSERT INTO `sys_logininfor` VALUES (121, 'admin', '1.86.244.34', 'XX XX', 'Chrome 8', 'Windows 10', '1', '密码输入错误1次', '2020-09-12 21:00:17');
INSERT INTO `sys_logininfor` VALUES (122, 'admin', '1.86.244.34', 'XX XX', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-09-12 21:00:19');
INSERT INTO `sys_logininfor` VALUES (123, 'admin', '113.140.24.2', 'XX XX', 'Chrome 8', 'Windows 10', '1', '密码输入错误1次', '2020-09-13 10:38:55');
INSERT INTO `sys_logininfor` VALUES (124, 'admin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '1', '密码输入错误1次', '2020-12-15 19:11:57');
INSERT INTO `sys_logininfor` VALUES (125, 'admin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '1', '密码输入错误2次', '2020-12-15 19:14:58');
INSERT INTO `sys_logininfor` VALUES (126, 'admin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-12-15 19:25:11');
INSERT INTO `sys_logininfor` VALUES (127, 'admin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-12-15 20:08:47');
INSERT INTO `sys_logininfor` VALUES (128, 'admin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-12-15 21:14:25');
INSERT INTO `sys_logininfor` VALUES (129, 'admin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-12-16 14:31:52');
INSERT INTO `sys_logininfor` VALUES (130, 'admin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-12-16 18:34:45');
INSERT INTO `sys_logininfor` VALUES (131, 'admin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-12-17 13:13:25');
INSERT INTO `sys_logininfor` VALUES (132, 'admin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-12-18 13:09:24');
INSERT INTO `sys_logininfor` VALUES (133, 'admin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-12-18 14:44:58');
INSERT INTO `sys_logininfor` VALUES (134, 'admin', '183.228.48.73', 'XX XX', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-12-19 12:55:15');
INSERT INTO `sys_logininfor` VALUES (135, 'admin', '220.202.152.65', 'XX XX', 'Chrome Mobile', 'Android 1.x', '0', '登录成功', '2020-12-19 12:57:21');
INSERT INTO `sys_logininfor` VALUES (136, 'admin', '220.202.152.65', 'XX XX', 'Chrome Mobile', 'Android 1.x', '0', '登录成功', '2020-12-19 12:58:26');
INSERT INTO `sys_logininfor` VALUES (137, 'admin', '111.22.182.237', 'XX XX', 'Chrome 8', 'Windows 7', '0', '登录成功', '2020-12-19 14:17:52');
INSERT INTO `sys_logininfor` VALUES (138, 'admin', '183.228.48.73', 'XX XX', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-12-19 14:23:11');
INSERT INTO `sys_logininfor` VALUES (139, 'admin', '111.22.182.237', 'XX XX', 'Chrome 8', 'Windows 7', '0', '登录成功', '2020-12-19 16:11:20');
INSERT INTO `sys_logininfor` VALUES (140, 'admin', '111.22.182.237', 'XX XX', 'Chrome 8', 'Windows 7', '0', '登录成功', '2020-12-19 17:18:09');
INSERT INTO `sys_logininfor` VALUES (141, 'admin', '220.202.235.222', 'XX XX', 'Mobile Safari', 'Mac OS X (iPad)', '0', '登录成功', '2020-12-19 19:05:32');
INSERT INTO `sys_logininfor` VALUES (142, 'admin', '111.22.182.237', 'XX XX', 'Chrome 8', 'Windows 7', '0', '登录成功', '2020-12-19 21:15:46');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int(4) NULL DEFAULT 0 COMMENT '显示顺序',
  `url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '#' COMMENT '请求地址',
  `target` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '打开方式（menuItem页签 menuBlank新窗口）',
  `menu_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `perms` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2290 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 100, '#', 'menuItem', 'M', '0', '', 'fa fa-gear', 'admin', '2018-03-16 11:33:00', 'admin', '2020-07-25 12:35:18', '系统管理目录');
INSERT INTO `sys_menu` VALUES (2, '系统监控', 0, 99, '#', 'menuItem', 'M', '0', '', 'fa fa-video-camera', 'admin', '2018-03-16 11:33:00', 'admin', '2020-07-25 12:34:26', '系统监控目录');
INSERT INTO `sys_menu` VALUES (3, '系统工具', 0, 130, '#', 'menuItem', 'M', '0', '', 'fa fa-bars', 'admin', '2018-03-16 11:33:00', 'admin', '2020-07-25 12:35:27', '系统工具目录');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, '/system/user', '', 'C', '0', 'system:user:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, '/system/role', '', 'C', '0', 'system:role:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, '/system/menu', '', 'C', '0', 'system:menu:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (103, '部门管理', 1, 4, '/system/dept', '', 'C', '0', 'system:dept:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '部门管理菜单');
INSERT INTO `sys_menu` VALUES (104, '岗位管理', 1, 5, '/system/post', '', 'C', '0', 'system:post:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '岗位管理菜单');
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 6, '/system/dict', '', 'C', '0', 'system:dict:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '字典管理菜单');
INSERT INTO `sys_menu` VALUES (106, '参数设置', 1, 7, '/system/config', '', 'C', '0', 'system:config:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '参数设置菜单');
INSERT INTO `sys_menu` VALUES (107, '通知公告', 1, 8, '/system/notice', '', 'C', '0', 'system:notice:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '通知公告菜单');
INSERT INTO `sys_menu` VALUES (108, '日志管理', 1, 9, '#', '', 'M', '0', '', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '日志管理菜单');
INSERT INTO `sys_menu` VALUES (109, '在线用户', 2, 1, '/monitor/online', '', 'C', '0', 'monitor:online:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '在线用户菜单');
INSERT INTO `sys_menu` VALUES (110, '定时任务', 2, 2, '/monitor/job', '', 'C', '0', 'monitor:job:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '定时任务菜单');
INSERT INTO `sys_menu` VALUES (111, '数据监控', 2, 3, '/monitor/data', '', 'C', '0', 'monitor:data:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '数据监控菜单');
INSERT INTO `sys_menu` VALUES (112, '服务监控', 2, 3, '/monitor/server', '', 'C', '0', 'monitor:server:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '服务监控菜单');
INSERT INTO `sys_menu` VALUES (113, '表单构建', 3, 1, '/tool/build', '', 'C', '0', 'tool:build:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '表单构建菜单');
INSERT INTO `sys_menu` VALUES (114, '代码生成', 3, 2, '/tool/gen', '', 'C', '0', 'tool:gen:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '代码生成菜单');
INSERT INTO `sys_menu` VALUES (115, '系统接口', 3, 3, '/tool/swagger', '', 'C', '0', 'tool:swagger:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '系统接口菜单');
INSERT INTO `sys_menu` VALUES (500, '操作日志', 108, 1, '/monitor/operlog', '', 'C', '0', 'monitor:operlog:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '操作日志菜单');
INSERT INTO `sys_menu` VALUES (501, '登录日志', 108, 2, '/monitor/logininfor', '', 'C', '0', 'monitor:logininfor:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '登录日志菜单');
INSERT INTO `sys_menu` VALUES (1000, '用户查询', 100, 1, '#', '', 'F', '0', 'system:user:list', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1001, '用户新增', 100, 2, '#', '', 'F', '0', 'system:user:add', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1002, '用户修改', 100, 3, '#', '', 'F', '0', 'system:user:edit', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1003, '用户删除', 100, 4, '#', '', 'F', '0', 'system:user:remove', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1004, '用户导出', 100, 5, '#', '', 'F', '0', 'system:user:export', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1005, '用户导入', 100, 6, '#', '', 'F', '0', 'system:user:import', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1006, '重置密码', 100, 7, '#', '', 'F', '0', 'system:user:resetPwd', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1007, '角色查询', 101, 1, '#', '', 'F', '0', 'system:role:list', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1008, '角色新增', 101, 2, '#', '', 'F', '0', 'system:role:add', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1009, '角色修改', 101, 3, '#', '', 'F', '0', 'system:role:edit', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1010, '角色删除', 101, 4, '#', '', 'F', '0', 'system:role:remove', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1011, '角色导出', 101, 5, '#', '', 'F', '0', 'system:role:export', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1012, '菜单查询', 102, 1, '#', '', 'F', '0', 'system:menu:list', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1013, '菜单新增', 102, 2, '#', '', 'F', '0', 'system:menu:add', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1014, '菜单修改', 102, 3, '#', '', 'F', '0', 'system:menu:edit', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1015, '菜单删除', 102, 4, '#', '', 'F', '0', 'system:menu:remove', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1016, '部门查询', 103, 1, '#', '', 'F', '0', 'system:dept:list', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1017, '部门新增', 103, 2, '#', '', 'F', '0', 'system:dept:add', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1018, '部门修改', 103, 3, '#', '', 'F', '0', 'system:dept:edit', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1019, '部门删除', 103, 4, '#', '', 'F', '0', 'system:dept:remove', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1020, '岗位查询', 104, 1, '#', '', 'F', '0', 'system:post:list', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1021, '岗位新增', 104, 2, '#', '', 'F', '0', 'system:post:add', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1022, '岗位修改', 104, 3, '#', '', 'F', '0', 'system:post:edit', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1023, '岗位删除', 104, 4, '#', '', 'F', '0', 'system:post:remove', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1024, '岗位导出', 104, 5, '#', '', 'F', '0', 'system:post:export', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1025, '字典查询', 105, 1, '#', '', 'F', '0', 'system:dict:list', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1026, '字典新增', 105, 2, '#', '', 'F', '0', 'system:dict:add', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1027, '字典修改', 105, 3, '#', '', 'F', '0', 'system:dict:edit', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1028, '字典删除', 105, 4, '#', '', 'F', '0', 'system:dict:remove', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1029, '字典导出', 105, 5, '#', '', 'F', '0', 'system:dict:export', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1030, '参数查询', 106, 1, '#', '', 'F', '0', 'system:config:list', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1031, '参数新增', 106, 2, '#', '', 'F', '0', 'system:config:add', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1032, '参数修改', 106, 3, '#', '', 'F', '0', 'system:config:edit', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1033, '参数删除', 106, 4, '#', '', 'F', '0', 'system:config:remove', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1034, '参数导出', 106, 5, '#', '', 'F', '0', 'system:config:export', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1035, '公告查询', 107, 1, '#', '', 'F', '0', 'system:notice:list', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1036, '公告新增', 107, 2, '#', '', 'F', '0', 'system:notice:add', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1037, '公告修改', 107, 3, '#', '', 'F', '0', 'system:notice:edit', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1038, '公告删除', 107, 4, '#', '', 'F', '0', 'system:notice:remove', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1039, '操作查询', 500, 1, '#', '', 'F', '0', 'monitor:operlog:list', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1040, '操作删除', 500, 2, '#', '', 'F', '0', 'monitor:operlog:remove', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1041, '详细信息', 500, 3, '#', '', 'F', '0', 'monitor:operlog:detail', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1042, '日志导出', 500, 4, '#', '', 'F', '0', 'monitor:operlog:export', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1043, '登录查询', 501, 1, '#', '', 'F', '0', 'monitor:logininfor:list', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1044, '登录删除', 501, 2, '#', '', 'F', '0', 'monitor:logininfor:remove', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1045, '日志导出', 501, 3, '#', '', 'F', '0', 'monitor:logininfor:export', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1046, '账户解锁', 501, 4, '#', '', 'F', '0', 'monitor:logininfor:unlock', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1047, '在线查询', 109, 1, '#', '', 'F', '0', 'monitor:online:list', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1048, '批量强退', 109, 2, '#', '', 'F', '0', 'monitor:online:batchForceLogout', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1049, '单条强退', 109, 3, '#', '', 'F', '0', 'monitor:online:forceLogout', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1050, '任务查询', 110, 1, '#', '', 'F', '0', 'monitor:job:list', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1051, '任务新增', 110, 2, '#', '', 'F', '0', 'monitor:job:add', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1052, '任务修改', 110, 3, '#', '', 'F', '0', 'monitor:job:edit', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1053, '任务删除', 110, 4, '#', '', 'F', '0', 'monitor:job:remove', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1054, '状态修改', 110, 5, '#', '', 'F', '0', 'monitor:job:changeStatus', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1055, '任务详细', 110, 6, '#', '', 'F', '0', 'monitor:job:detail', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1056, '任务导出', 110, 7, '#', '', 'F', '0', 'monitor:job:export', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1057, '生成查询', 114, 1, '#', '', 'F', '0', 'tool:gen:list', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1058, '生成修改', 114, 2, '#', '', 'F', '0', 'tool:gen:edit', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1059, '生成删除', 114, 3, '#', '', 'F', '0', 'tool:gen:remove', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1060, '预览代码', 114, 4, '#', '', 'F', '0', 'tool:gen:preview', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (1061, '生成代码', 114, 5, '#', '', 'F', '0', 'tool:gen:code', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_menu` VALUES (2012, '交易配置', 2018, 1, '/qlcoin/TOtcTypeConfig', 'menuItem', 'C', '0', 'qlcoin:TOtcTypeConfig:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-24 10:36:45', 'OTC类型配置菜单');
INSERT INTO `sys_menu` VALUES (2013, 'OTC类型配置查询', 2012, 1, '#', '', 'F', '0', 'qlcoin:TOtcTypeConfig:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2014, 'OTC类型配置新增', 2012, 2, '#', '', 'F', '0', 'qlcoin:TOtcTypeConfig:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2015, 'OTC类型配置修改', 2012, 3, '#', '', 'F', '0', 'qlcoin:TOtcTypeConfig:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2016, 'OTC类型配置删除', 2012, 4, '#', '', 'F', '0', 'qlcoin:TOtcTypeConfig:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2017, 'OTC类型配置导出', 2012, 5, '#', '', 'F', '0', 'qlcoin:TOtcTypeConfig:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2018, '法币交易', 0, 96, '#', 'menuItem', 'M', '0', '', 'fa fa-american-sign-language-interpreting', 'admin', '2020-03-30 09:40:15', 'admin', '2020-07-25 12:33:52', '');
INSERT INTO `sys_menu` VALUES (2019, '交易订单', 2018, 1, '/qlcoin/TOtcOrderPrice', 'menuItem', 'C', '0', 'qlcoin:TOtcOrderPrice:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-24 10:37:03', '下单表菜单');
INSERT INTO `sys_menu` VALUES (2020, '下单表查询', 2019, 1, '#', '', 'F', '0', 'qlcoin:TOtcOrderPrice:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2021, '下单表新增', 2019, 2, '#', '', 'F', '0', 'qlcoin:TOtcOrderPrice:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2022, '下单表修改', 2019, 3, '#', '', 'F', '0', 'qlcoin:TOtcOrderPrice:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2023, '下单表删除', 2019, 4, '#', '', 'F', '0', 'qlcoin:TOtcOrderPrice:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2024, '下单表导出', 2019, 5, '#', '', 'F', '0', 'qlcoin:TOtcOrderPrice:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2025, '法币广告', 2018, 1, '/qlcoin/TOtcOrder', 'menuItem', 'C', '0', 'qlcoin:TOtcOrder:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-03-30 10:57:10', '法币广告菜单');
INSERT INTO `sys_menu` VALUES (2026, '法币广告查询', 2025, 1, '#', '', 'F', '0', 'qlcoin:TOtcOrder:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2027, '法币广告新增', 2025, 2, '#', '', 'F', '0', 'qlcoin:TOtcOrder:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2028, '法币广告修改', 2025, 3, '#', '', 'F', '0', 'qlcoin:TOtcOrder:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2029, '法币广告删除', 2025, 4, '#', '', 'F', '0', 'qlcoin:TOtcOrder:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2030, '法币广告导出', 2025, 5, '#', '', 'F', '0', 'qlcoin:TOtcOrder:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2037, '币种配置', 2018, 1, '/qlcoin/TOtcCurrenctConfig', 'menuItem', 'C', '0', 'qlcoin:TOtcCurrenctConfig:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-24 10:37:16', '法币币种配置菜单');
INSERT INTO `sys_menu` VALUES (2038, '法币币种配置查询', 2037, 1, '#', '', 'F', '0', 'qlcoin:TOtcCurrenctConfig:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2039, '法币币种配置新增', 2037, 2, '#', '', 'F', '0', 'qlcoin:TOtcCurrenctConfig:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2040, '法币币种配置修改', 2037, 3, '#', '', 'F', '0', 'qlcoin:TOtcCurrenctConfig:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2041, '法币币种配置删除', 2037, 4, '#', '', 'F', '0', 'qlcoin:TOtcCurrenctConfig:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2042, '法币币种配置导出', 2037, 5, '#', '', 'F', '0', 'qlcoin:TOtcCurrenctConfig:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2043, '申诉类型', 2018, 1, '/qlcoin/TOtcAppealType', 'menuItem', 'C', '0', 'qlcoin:TOtcAppealType:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-24 10:37:33', '申诉类型列表菜单');
INSERT INTO `sys_menu` VALUES (2044, '申诉类型列表查询', 2043, 1, '#', '', 'F', '0', 'qlcoin:TOtcAppealType:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2045, '申诉类型列表新增', 2043, 2, '#', '', 'F', '0', 'qlcoin:TOtcAppealType:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2046, '申诉类型列表修改', 2043, 3, '#', '', 'F', '0', 'qlcoin:TOtcAppealType:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2047, '申诉类型列表删除', 2043, 4, '#', '', 'F', '0', 'qlcoin:TOtcAppealType:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2048, '申诉类型列表导出', 2043, 5, '#', '', 'F', '0', 'qlcoin:TOtcAppealType:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2049, '用户申诉', 2018, 1, '/qlcoin/TOtcOrderAppeal', 'menuItem', 'C', '0', 'qlcoin:TOtcOrderAppeal:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-24 10:34:46', 'Otc申诉表菜单');
INSERT INTO `sys_menu` VALUES (2050, 'Otc申诉表查询', 2049, 1, '#', '', 'F', '0', 'qlcoin:TOtcOrderAppeal:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2051, 'Otc申诉表新增', 2049, 2, '#', '', 'F', '0', 'qlcoin:TOtcOrderAppeal:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2052, 'Otc申诉表修改', 2049, 3, '#', '', 'F', '0', 'qlcoin:TOtcOrderAppeal:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2053, 'Otc申诉表删除', 2049, 4, '#', '', 'F', '0', 'qlcoin:TOtcOrderAppeal:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2054, 'Otc申诉表导出', 2049, 5, '#', '', 'F', '0', 'qlcoin:TOtcOrderAppeal:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2055, '会员管理', 2106, 1, '/qlcoin/OTCMember', 'menuItem', 'C', '0', 'qlcoin:OTCMember:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-04-01 09:06:07', 'OTC认证菜单');
INSERT INTO `sys_menu` VALUES (2056, 'OTC认证查询', 2055, 1, '#', '', 'F', '0', 'qlcoin:OTCMember:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2057, 'OTC认证新增', 2055, 2, '#', '', 'F', '0', 'qlcoin:OTCMember:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2058, 'OTC认证修改', 2055, 3, '#', '', 'F', '0', 'qlcoin:OTCMember:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2059, 'OTC认证删除', 2055, 4, '#', '', 'F', '0', 'qlcoin:OTCMember:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2060, 'OTC认证导出', 2055, 5, '#', '', 'F', '0', 'qlcoin:OTCMember:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2061, '用户资产账户', 2067, 1, '/qlcoin/TBalance', 'menuItem', 'C', '0', 'qlcoin:TBalance:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-24 10:58:48', '用户余额菜单');
INSERT INTO `sys_menu` VALUES (2062, '用户余额查询', 2061, 1, '#', '', 'F', '0', 'qlcoin:TBalance:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2063, '用户余额新增', 2061, 2, '#', '', 'F', '0', 'qlcoin:TBalance:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2064, '用户余额修改', 2061, 3, '#', '', 'F', '0', 'qlcoin:TBalance:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2065, '用户余额删除', 2061, 4, '#', '', 'F', '0', 'qlcoin:TBalance:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2066, '用户余额导出', 2061, 5, '#', '', 'F', '0', 'qlcoin:TBalance:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2067, '资金账户', 0, 80, '#', 'menuItem', 'M', '0', '', 'fa fa-calculator', 'admin', '2020-03-31 11:50:18', 'admin', '2020-07-24 23:44:09', '');
INSERT INTO `sys_menu` VALUES (2068, '历史委托记录', 2080, 7, '/qlcoin/TEntrustHistory', 'menuItem', 'C', '0', 'qlcoin:TEntrustHistory:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-24 10:34:20', '委托历史表菜单');
INSERT INTO `sys_menu` VALUES (2069, '委托历史表查询', 2068, 1, '#', '', 'F', '0', 'qlcoin:TEntrustHistory:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2070, '委托历史表新增', 2068, 2, '#', '', 'F', '0', 'qlcoin:TEntrustHistory:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2071, '委托历史表修改', 2068, 3, '#', '', 'F', '0', 'qlcoin:TEntrustHistory:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2072, '委托历史表删除', 2068, 4, '#', '', 'F', '0', 'qlcoin:TEntrustHistory:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2073, '委托历史表导出', 2068, 5, '#', '', 'F', '0', 'qlcoin:TEntrustHistory:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2074, '币币撮合表', 2080, 5, '/qlcoin/TEntrust', 'menuItem', 'C', '0', 'qlcoin:TEntrust:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-24 10:34:03', '撮合表菜单');
INSERT INTO `sys_menu` VALUES (2075, '撮合表查询', 2074, 1, '#', '', 'F', '0', 'qlcoin:TEntrust:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2076, '撮合表新增', 2074, 2, '#', '', 'F', '0', 'qlcoin:TEntrust:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2077, '撮合表修改', 2074, 3, '#', '', 'F', '0', 'qlcoin:TEntrust:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2078, '撮合表删除', 2074, 4, '#', '', 'F', '0', 'qlcoin:TEntrust:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2079, '撮合表导出', 2074, 5, '#', '', 'F', '0', 'qlcoin:TEntrust:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2080, '币币交易', 0, 90, '#', 'menuItem', 'M', '0', '', 'fa fa-arrows-h', 'admin', '2020-03-31 12:25:58', 'admin', '2020-07-24 10:39:15', '');
INSERT INTO `sys_menu` VALUES (2099, '提币审核', 2105, 1, '/qlcoin/TExtractCoin', 'menuItem', 'C', '0', 'qlcoin:TExtractCoin:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-04-01 09:00:10', '提币审核菜单');
INSERT INTO `sys_menu` VALUES (2100, '提币审核查询', 2099, 1, '#', '', 'F', '0', 'qlcoin:TExtractCoin:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2101, '提币审核新增', 2099, 2, '#', '', 'F', '0', 'qlcoin:TExtractCoin:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2102, '提币审核修改', 2099, 3, '#', '', 'F', '0', 'qlcoin:TExtractCoin:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2103, '提币审核删除', 2099, 4, '#', '', 'F', '0', 'qlcoin:TExtractCoin:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2104, '提币审核导出', 2099, 5, '#', '', 'F', '0', 'qlcoin:TExtractCoin:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2105, '充币提币', 0, 89, '#', 'menuItem', 'M', '0', '', 'fa fa-plane', 'admin', '2020-04-01 08:59:54', 'admin', '2020-07-24 16:29:06', '');
INSERT INTO `sys_menu` VALUES (2106, '会员管理', 0, 1, '#', 'menuItem', 'M', '0', NULL, 'fa fa-address-book', 'admin', '2020-04-01 09:05:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2107, '公告管理', 2221, 1, '/qlcoin/NEWS', 'menuItem', 'C', '0', 'qlcoin:NEWS:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-30 22:58:12', '币币-公告菜单');
INSERT INTO `sys_menu` VALUES (2108, '币币-公告查询', 2107, 1, '#', '', 'F', '0', 'qlcoin:NEWS:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2109, '币币-公告新增', 2107, 2, '#', '', 'F', '0', 'qlcoin:NEWS:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2110, '币币-公告修改', 2107, 3, '#', '', 'F', '0', 'qlcoin:NEWS:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2111, '币币-公告删除', 2107, 4, '#', '', 'F', '0', 'qlcoin:NEWS:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2112, '币币-公告导出', 2107, 5, '#', '', 'F', '0', 'qlcoin:NEWS:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2114, '交易对配置', 2120, 1, '/qlcoin/TContractMul', 'menuItem', 'C', '0', 'qlcoin:TContractMul:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-30 22:57:35', '合约配置菜单');
INSERT INTO `sys_menu` VALUES (2115, '合约配置查询', 2114, 1, '#', '', 'F', '0', 'qlcoin:TContractMul:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2116, '合约配置新增', 2114, 2, '#', '', 'F', '0', 'qlcoin:TContractMul:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2117, '合约配置修改', 2114, 3, '#', '', 'F', '0', 'qlcoin:TContractMul:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2118, '合约配置删除', 2114, 4, '#', '', 'F', '0', 'qlcoin:TContractMul:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2119, '合约配置导出', 2114, 5, '#', '', 'F', '0', 'qlcoin:TContractMul:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2120, '合约交易', 0, 93, '#', 'menuItem', 'M', '0', '', 'fa fa-book', 'admin', '2020-04-03 14:22:55', 'admin', '2020-04-13 12:11:02', '');
INSERT INTO `sys_menu` VALUES (2121, '合约订单表', 2120, 1, '/qlcoin/TContractOrder', 'menuItem', 'C', '0', 'qlcoin:TContractOrder:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-04-03 18:29:15', '合约订单表菜单');
INSERT INTO `sys_menu` VALUES (2122, '合约订单表查询', 2121, 1, '#', '', 'F', '0', 'qlcoin:TContractOrder:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2123, '合约订单表新增', 2121, 2, '#', '', 'F', '0', 'qlcoin:TContractOrder:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2124, '合约订单表修改', 2121, 3, '#', '', 'F', '0', 'qlcoin:TContractOrder:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2125, '合约订单表删除', 2121, 4, '#', '', 'F', '0', 'qlcoin:TContractOrder:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2126, '合约订单表导出', 2121, 5, '#', '', 'F', '0', 'qlcoin:TContractOrder:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2127, 'BANNER', 2221, 1, '/qlcoin/TBanner', 'menuItem', 'C', '0', 'qlcoin:TBanner:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-30 22:59:11', 'banner图菜单');
INSERT INTO `sys_menu` VALUES (2128, 'banner图查询', 2127, 1, '#', '', 'F', '0', 'qlcoin:TBanner:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2129, 'banner图新增', 2127, 2, '#', '', 'F', '0', 'qlcoin:TBanner:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2130, 'banner图修改', 2127, 3, '#', '', 'F', '0', 'qlcoin:TBanner:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2131, 'banner图删除', 2127, 4, '#', '', 'F', '0', 'qlcoin:TBanner:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2132, 'banner图导出', 2127, 5, '#', '', 'F', '0', 'qlcoin:TBanner:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2133, '合约杠杆系数', 2120, 1, '/qlcoin/TLever', 'menuItem', 'C', '0', 'qlcoin:TLever:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-24 10:32:20', '杠杆系数菜单');
INSERT INTO `sys_menu` VALUES (2134, '杠杆系数查询', 2133, 1, '#', '', 'F', '0', 'qlcoin:TLever:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2135, '杠杆系数新增', 2133, 2, '#', '', 'F', '0', 'qlcoin:TLever:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2136, '杠杆系数修改', 2133, 3, '#', '', 'F', '0', 'qlcoin:TLever:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2137, '杠杆系数删除', 2133, 4, '#', '', 'F', '0', 'qlcoin:TLever:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2138, '杠杆系数导出', 2133, 5, '#', '', 'F', '0', 'qlcoin:TLever:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2145, '交易对配置', 2080, 1, '/qlcoin/TPairs', 'menuItem', 'C', '0', 'qlcoin:TPairs:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-29 18:50:07', '交易对管理菜单');
INSERT INTO `sys_menu` VALUES (2146, '交易对管理查询', 2145, 1, '#', '', 'F', '0', 'qlcoin:TPairs:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2147, '交易对管理新增', 2145, 2, '#', '', 'F', '0', 'qlcoin:TPairs:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2148, '交易对管理修改', 2145, 3, '#', '', 'F', '0', 'qlcoin:TPairs:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2149, '交易对管理删除', 2145, 4, '#', '', 'F', '0', 'qlcoin:TPairs:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2150, '交易对管理导出', 2145, 5, '#', '', 'F', '0', 'qlcoin:TPairs:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2152, 'ETH合约配置', 2080, 3, '/qlcoin/TCoinToken', 'menuItem', 'C', '0', 'qlcoin:TCoinToken:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-30 22:57:01', '合约地址菜单');
INSERT INTO `sys_menu` VALUES (2153, '合约地址查询', 2152, 1, '#', '', 'F', '0', 'qlcoin:TCoinToken:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2154, '合约地址新增', 2152, 2, '#', '', 'F', '0', 'qlcoin:TCoinToken:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2155, '合约地址修改', 2152, 3, '#', '', 'F', '0', 'qlcoin:TCoinToken:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2156, '合约地址删除', 2152, 4, '#', '', 'F', '0', 'qlcoin:TCoinToken:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2157, '合约地址导出', 2152, 5, '#', '', 'F', '0', 'qlcoin:TCoinToken:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2159, '平台充提账户', 2067, 1, '/qlcoin/TBalance/sysTBalance', 'menuItem', 'C', '0', 'qlcoin:TBalance:viewSys', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-25 18:38:36', '资金池地址菜单');
INSERT INTO `sys_menu` VALUES (2160, '资金池地址查询', 2159, 1, '#', '', 'F', '0', 'qlcoin:TMemberWallet:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2161, '资金池地址新增', 2159, 2, '#', '', 'F', '0', 'qlcoin:TMemberWallet:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2162, '资金池地址修改', 2159, 3, '#', '', 'F', '0', 'qlcoin:TMemberWallet:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2163, '资金池地址删除', 2159, 4, '#', '', 'F', '0', 'qlcoin:TMemberWallet:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2164, '资金池地址导出', 2159, 5, '#', '', 'F', '0', 'qlcoin:TMemberWallet:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2165, '资金统计', 0, 1, '#', 'menuItem', 'M', '0', '', 'fa fa-battery-4', 'admin', '2020-04-16 18:48:10', 'admin', '2020-07-24 23:44:20', '');
INSERT INTO `sys_menu` VALUES (2166, '平台总资产', 2165, 1, '/qlcoin/Bizhongzongshu', 'menuItem', 'C', '0', 'qlcoin:Bizhongzongshu:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-27 23:49:14', '资产统计菜单');
INSERT INTO `sys_menu` VALUES (2167, '资产统计查询', 2166, 1, '#', '', 'F', '0', 'qlcoin:Bizhongzongshu:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2168, '资产统计新增', 2166, 2, '#', '', 'F', '0', 'qlcoin:Bizhongzongshu:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2169, '资产统计修改', 2166, 3, '#', '', 'F', '0', 'qlcoin:Bizhongzongshu:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2170, '资产统计删除', 2166, 4, '#', '', 'F', '0', 'qlcoin:Bizhongzongshu:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2171, '资产统计导出', 2166, 5, '#', '', 'F', '0', 'qlcoin:Bizhongzongshu:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2172, '商户管理', 2106, 1, '/qlcoin/SroteMember', 'menuItem', 'C', '0', 'qlcoin:SroteMember:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-04-20 11:42:13', 'SroteMember菜单');
INSERT INTO `sys_menu` VALUES (2173, 'SroteMember查询', 2172, 1, '#', '', 'F', '0', 'qlcoin:SroteMember:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2174, 'SroteMember新增', 2172, 2, '#', '', 'F', '0', 'qlcoin:SroteMember:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2175, 'SroteMember修改', 2172, 3, '#', '', 'F', '0', 'qlcoin:SroteMember:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2176, 'SroteMember删除', 2172, 4, '#', '', 'F', '0', 'qlcoin:SroteMember:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2177, 'SroteMember导出', 2172, 5, '#', '', 'F', '0', 'qlcoin:SroteMember:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2178, '短信邮箱发送记录', 2, 1, '/qlcoin/TMessageRecord', 'menuItem', 'C', '0', 'qlcoin:TMessageRecord:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-04-21 14:07:10', '短信邮箱发送记录菜单');
INSERT INTO `sys_menu` VALUES (2179, '短信邮箱发送记录查询', 2178, 1, '#', '', 'F', '0', 'qlcoin:TMessageRecord:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2180, '短信邮箱发送记录新增', 2178, 2, '#', '', 'F', '0', 'qlcoin:TMessageRecord:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2181, '短信邮箱发送记录修改', 2178, 3, '#', '', 'F', '0', 'qlcoin:TMessageRecord:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2182, '短信邮箱发送记录删除', 2178, 4, '#', '', 'F', '0', 'qlcoin:TMessageRecord:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2183, '短信邮箱发送记录导出', 2178, 5, '#', '', 'F', '0', 'qlcoin:TMessageRecord:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2190, '操作接口日志', 2, 1, '/qlcoin/TUserUseLogs', 'menuItem', 'C', '0', 'qlcoin:TUserUseLogs:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-04-23 17:01:06', '操作接口日志菜单');
INSERT INTO `sys_menu` VALUES (2191, '操作接口日志查询', 2190, 1, '#', '', 'F', '0', 'qlcoin:TUserUseLogs:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2192, '操作接口日志新增', 2190, 2, '#', '', 'F', '0', 'qlcoin:TUserUseLogs:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2193, '操作接口日志修改', 2190, 3, '#', '', 'F', '0', 'qlcoin:TUserUseLogs:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2194, '操作接口日志删除', 2190, 4, '#', '', 'F', '0', 'qlcoin:TUserUseLogs:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2195, '操作接口日志导出', 2190, 5, '#', '', 'F', '0', 'qlcoin:TUserUseLogs:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2196, '财务分析', 0, 3, '#', 'menuItem', 'M', '0', '', 'fa fa-area-chart', 'admin', '2020-05-08 09:57:21', 'admin', '2020-07-25 12:33:12', '');
INSERT INTO `sys_menu` VALUES (2197, '充提交易统计', 2196, 1, '/qlcoin/TUserBlanceCollect', 'menuItem', 'C', '0', 'qlcoin:TUserBlanceCollect:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-27 23:45:50', '用户财务菜单');
INSERT INTO `sys_menu` VALUES (2198, '用户财务查询', 2197, 1, '#', '', 'F', '0', 'qlcoin:TUserBlanceCollect:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2199, '用户财务新增', 2197, 2, '#', '', 'F', '0', 'qlcoin:TUserBlanceCollect:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2200, '用户财务修改', 2197, 3, '#', '', 'F', '0', 'qlcoin:TUserBlanceCollect:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2201, '用户财务删除', 2197, 4, '#', '', 'F', '0', 'qlcoin:TUserBlanceCollect:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2202, '用户财务导出', 2197, 5, '#', '', 'F', '0', 'qlcoin:TUserBlanceCollect:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2203, '币币交易统计', 2196, 1, '/qlcoin/TBibiDayRecord', 'menuItem', 'C', '0', 'qlcoin:TBibiDayRecord:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-27 23:48:26', '币币统计菜单');
INSERT INTO `sys_menu` VALUES (2204, '币币统计查询', 2203, 1, '#', '', 'F', '0', 'qlcoin:TBibiDayRecord:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2205, '币币统计新增', 2203, 2, '#', '', 'F', '0', 'qlcoin:TBibiDayRecord:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2206, '币币统计修改', 2203, 3, '#', '', 'F', '0', 'qlcoin:TBibiDayRecord:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2207, '币币统计删除', 2203, 4, '#', '', 'F', '0', 'qlcoin:TBibiDayRecord:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2208, '币币统计导出', 2203, 5, '#', '', 'F', '0', 'qlcoin:TBibiDayRecord:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2209, '法币交易统计', 2196, 1, '/qlcoin/TFabiDayRecord', 'menuItem', 'C', '0', 'qlcoin:TFabiDayRecord:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-27 23:48:42', '法币统计菜单');
INSERT INTO `sys_menu` VALUES (2210, '法币统计查询', 2209, 1, '#', '', 'F', '0', 'qlcoin:TFabiDayRecord:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2211, '法币统计新增', 2209, 2, '#', '', 'F', '0', 'qlcoin:TFabiDayRecord:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2212, '法币统计修改', 2209, 3, '#', '', 'F', '0', 'qlcoin:TFabiDayRecord:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2213, '法币统计删除', 2209, 4, '#', '', 'F', '0', 'qlcoin:TFabiDayRecord:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2214, '法币统计导出', 2209, 5, '#', '', 'F', '0', 'qlcoin:TFabiDayRecord:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2215, '机器人', 2221, 3, '/qlcoin/TRobotConfig', 'menuItem', 'C', '0', 'qlcoin:TRobotConfig:view', 'fa fa-assistive-listening-systems', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-31 14:25:20', '机器人配置菜单');
INSERT INTO `sys_menu` VALUES (2216, '机器人配置查询', 2215, 1, '#', '', 'F', '0', 'qlcoin:TRobotConfig:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2217, '机器人配置新增', 2215, 2, '#', '', 'F', '0', 'qlcoin:TRobotConfig:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2218, '机器人配置修改', 2215, 3, '#', '', 'F', '0', 'qlcoin:TRobotConfig:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2219, '机器人配置删除', 2215, 4, '#', '', 'F', '0', 'qlcoin:TRobotConfig:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2220, '机器人配置导出', 2215, 5, '#', '', 'F', '0', 'qlcoin:TRobotConfig:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2221, '平台配置', 0, 98, '#', 'menuItem', 'M', '0', '', 'fa fa-assistive-listening-systems', 'admin', '2020-05-18 12:07:59', 'admin', '2020-07-25 12:32:47', '');
INSERT INTO `sys_menu` VALUES (2222, '私募项目管理', 2234, 1, '/qlcoin/project', 'menuItem', 'C', '0', 'qlcoin:project:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-24 10:40:14', '私募项目管理菜单');
INSERT INTO `sys_menu` VALUES (2223, '私募项目管理查询', 2222, 1, '#', '', 'F', '0', 'qlcoin:project:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2224, '私募项目管理新增', 2222, 2, '#', '', 'F', '0', 'qlcoin:project:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2225, '私募项目管理修改', 2222, 3, '#', '', 'F', '0', 'qlcoin:project:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2226, '私募项目管理删除', 2222, 4, '#', '', 'F', '0', 'qlcoin:project:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2227, '私募项目管理导出', 2222, 5, '#', '', 'F', '0', 'qlcoin:project:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2228, '私募订单管理', 2234, 1, '/qlcoin/order', 'menuItem', 'C', '0', 'qlcoin:order:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-24 10:40:29', '私募订单管理菜单');
INSERT INTO `sys_menu` VALUES (2229, '私募订单管理查询', 2228, 1, '#', '', 'F', '0', 'qlcoin:order:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2230, '私募订单管理新增', 2228, 2, '#', '', 'F', '0', 'qlcoin:order:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2231, '私募订单管理修改', 2228, 3, '#', '', 'F', '0', 'qlcoin:order:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2232, '私募订单管理删除', 2228, 4, '#', '', 'F', '0', 'qlcoin:order:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2233, '私募订单管理导出', 2228, 5, '#', '', 'F', '0', 'qlcoin:order:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2234, '私募交易', 0, 97, '#', 'menuItem', 'M', '0', '', 'fa fa-address-book', 'admin', '2020-07-24 10:39:58', 'admin', '2020-07-25 12:34:43', '');
INSERT INTO `sys_menu` VALUES (2247, '充提配置', 2105, 1, '/system/SysDictionaries', 'menuItem', 'C', '0', 'system:SysDictionaries:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-29 18:55:00', 'system菜单');
INSERT INTO `sys_menu` VALUES (2248, '参数查询', 2247, 1, '#', 'menuItem', 'F', '0', 'system:SysDictionaries:list', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-24 13:48:00', '');
INSERT INTO `sys_menu` VALUES (2249, '参数新增', 2247, 2, '#', '', 'F', '0', 'system:SysDictionaries:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2250, '参数修改', 2247, 3, '#', '', 'F', '0', 'system:SysDictionaries:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2251, '参数删除', 2247, 4, '#', '', 'F', '0', 'system:SysDictionaries:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2252, '参数导出', 2247, 5, '#', '', 'F', '0', 'system:SysDictionaries:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2259, '提币记录', 2105, 1, '/qlcoin/withdrawHistory', 'menuItem', 'C', '0', 'qlcoin:withdrawHistory:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-24 17:10:16', 'TWithdrawHistory菜单');
INSERT INTO `sys_menu` VALUES (2260, 'TWithdrawHistory查询', 2259, 1, '#', '', 'F', '0', 'qlcoin:withdrawHistory:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2261, 'TWithdrawHistory新增', 2259, 2, '#', '', 'F', '0', 'qlcoin:withdrawHistory:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2262, 'TWithdrawHistory修改', 2259, 3, '#', '', 'F', '0', 'qlcoin:withdrawHistory:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2263, 'TWithdrawHistory删除', 2259, 4, '#', '', 'F', '0', 'qlcoin:withdrawHistory:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2264, 'TWithdrawHistory导出', 2259, 5, '#', '', 'F', '0', 'qlcoin:withdrawHistory:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2265, '充币记录', 2105, 1, '/qlcoin/deposit_history', 'menuItem', 'C', '0', 'qlcoin:deposit_history:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-24 17:27:45', 'deposit_history菜单');
INSERT INTO `sys_menu` VALUES (2266, 'deposit_history查询', 2265, 1, '#', '', 'F', '0', 'qlcoin:deposit_history:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2267, 'deposit_history新增', 2265, 2, '#', '', 'F', '0', 'qlcoin:deposit_history:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2268, 'deposit_history修改', 2265, 3, '#', '', 'F', '0', 'qlcoin:deposit_history:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2269, 'deposit_history删除', 2265, 4, '#', '', 'F', '0', 'qlcoin:deposit_history:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2270, 'deposit_history导出', 2265, 5, '#', '', 'F', '0', 'qlcoin:deposit_history:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2271, '用户余额归集', 2061, 6, '#', 'menuItem', 'F', '0', 'qlcoin:TBalance:notionalpooling', '#', 'admin', '2020-07-25 13:20:38', 'admin', '2020-07-25 13:49:26', '');
INSERT INTO `sys_menu` VALUES (2272, '充币地址', 2105, 1, '/qlcoin/wallepool', 'menuItem', 'C', '0', 'qlcoin:wallepool:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-28 09:57:48', 'walletpool菜单');
INSERT INTO `sys_menu` VALUES (2273, 'walletpool查询', 2272, 1, '#', '', 'F', '0', 'qlcoin:wallepool:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2274, 'walletpool新增', 2272, 2, '#', '', 'F', '0', 'qlcoin:wallepool:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2275, 'walletpool修改', 2272, 3, '#', '', 'F', '0', 'qlcoin:wallepool:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2276, 'walletpool删除', 2272, 4, '#', '', 'F', '0', 'qlcoin:wallepool:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2277, 'walletpool导出', 2272, 5, '#', '', 'F', '0', 'qlcoin:wallepool:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2278, '三方接口', 2221, 1, '/qlcoin/sysconfig', 'menuItem', 'C', '0', 'qlcoin:sysconfig:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-29 19:33:25', 'sysconfig菜单');
INSERT INTO `sys_menu` VALUES (2279, 'sysconfig查询', 2278, 1, '#', '', 'F', '0', 'qlcoin:sysconfig:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2280, 'sysconfig新增', 2278, 2, '#', '', 'F', '0', 'qlcoin:sysconfig:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2281, 'sysconfig修改', 2278, 3, '#', '', 'F', '0', 'qlcoin:sysconfig:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2282, 'sysconfig删除', 2278, 4, '#', '', 'F', '0', 'qlcoin:sysconfig:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2283, 'sysconfig导出', 2278, 5, '#', '', 'F', '0', 'qlcoin:sysconfig:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2284, '分享图片', 2221, 1, '/qlcoin/TShareImg', 'menuItem', 'C', '0', 'qlcoin:TShareImg:view', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-31 13:47:02', 'TShareImg菜单');
INSERT INTO `sys_menu` VALUES (2285, 'TShareImg查询', 2284, 1, '#', '', 'F', '0', 'qlcoin:TShareImg:list', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2286, 'TShareImg新增', 2284, 2, '#', '', 'F', '0', 'qlcoin:TShareImg:add', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2287, 'TShareImg修改', 2284, 3, '#', '', 'F', '0', 'qlcoin:TShareImg:edit', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2288, 'TShareImg删除', 2284, 4, '#', '', 'F', '0', 'qlcoin:TShareImg:remove', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` VALUES (2289, 'TShareImg导出', 2284, 5, '#', '', 'F', '0', 'qlcoin:TShareImg:export', '#', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` int(4) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公告标题',
  `notice_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告内容',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '通知公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (1, '温馨提醒：2018-07-01 若依新版本发布啦', '2', '新版本内容', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '管理员');
INSERT INTO `sys_notice` VALUES (2, '维护通知：2018-07-01 若依系统凌晨维护', '1', '维护内容', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '管理员');

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `oper_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '模块标题',
  `business_type` int(2) NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '请求方式',
  `operator_type` int(1) NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '返回参数',
  `status` int(1) NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`oper_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 113 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '操作日志记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------
INSERT INTO `sys_oper_log` VALUES (1, '操作日志', 9, 'com.ruoyi.web.controller.monitor.SysOperlogController.clean()', 'POST', 1, 'admin', '研发部门', '/monitor/operlog/clean', '113.139.241.52', 'XX XX', '{ }', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-21 21:46:11');
INSERT INTO `sys_oper_log` VALUES (2, '登陆日志', 9, 'com.ruoyi.web.controller.monitor.SysLogininforController.clean()', 'POST', 1, 'admin', '研发部门', '/monitor/logininfor/clean', '113.139.241.52', 'XX XX', '{ }', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-21 21:46:16');
INSERT INTO `sys_oper_log` VALUES (3, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '113.139.241.52', 'XX XX', '{\n  \"id\" : [ \"8e981272b7bd47d2994a23981147d12b\" ],\n  \"pairName\" : [ \"BS/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"0.188\" ],\n  \"willTime\" : [ \"2020-08-21 01:05\" ],\n  \"bindUser\" : [ \"\" ],\n  \"startNum\" : [ \"100.00000000\" ],\n  \"endNum\" : [ \"10000.00000000\" ],\n  \"mstartNum\" : [ \"100.00000000\" ],\n  \"mendNum\" : [ \"10000.00000000\" ],\n  \"startPrice\" : [ \"0.00010000\" ],\n  \"endPrice\" : [ \"0.00030000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-21 21:48:20');
INSERT INTO `sys_oper_log` VALUES (4, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '113.139.241.52', 'XX XX', '{\n  \"id\" : [ \"8e981272b7bd47d2994a23981147d12b\" ],\n  \"pairName\" : [ \"BS/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"0.1855\" ],\n  \"willTime\" : [ \"2020-08-21 01:05:00\" ],\n  \"bindUser\" : [ \"\" ],\n  \"startNum\" : [ \"100.00000000\" ],\n  \"endNum\" : [ \"10000.00000000\" ],\n  \"mstartNum\" : [ \"100.00000000\" ],\n  \"mendNum\" : [ \"10000.00000000\" ],\n  \"startPrice\" : [ \"0.00010000\" ],\n  \"endPrice\" : [ \"0.00030000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-21 21:48:36');
INSERT INTO `sys_oper_log` VALUES (5, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '113.139.241.52', 'XX XX', '{\n  \"id\" : [ \"8e981272b7bd47d2994a23981147d12b\" ],\n  \"pairName\" : [ \"BS/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"0.1952\" ],\n  \"willTime\" : [ \"2020-08-21 01:05:00\" ],\n  \"bindUser\" : [ \"\" ],\n  \"startNum\" : [ \"100.00000000\" ],\n  \"endNum\" : [ \"10000.00000000\" ],\n  \"mstartNum\" : [ \"100.00000000\" ],\n  \"mendNum\" : [ \"10000.00000000\" ],\n  \"startPrice\" : [ \"0.00010000\" ],\n  \"endPrice\" : [ \"0.00030000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-21 21:49:19');
INSERT INTO `sys_oper_log` VALUES (6, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '113.139.241.52', 'XX XX', '{\n  \"id\" : [ \"8e981272b7bd47d2994a23981147d12b\" ],\n  \"pairName\" : [ \"BS/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"0.25\" ],\n  \"willTime\" : [ \"2020-08-26 01:05\" ],\n  \"bindUser\" : [ \"\" ],\n  \"startNum\" : [ \"100.00000000\" ],\n  \"endNum\" : [ \"10000.00000000\" ],\n  \"mstartNum\" : [ \"100.00000000\" ],\n  \"mendNum\" : [ \"10000.00000000\" ],\n  \"startPrice\" : [ \"0.00010000\" ],\n  \"endPrice\" : [ \"0.00030000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-21 21:49:51');
INSERT INTO `sys_oper_log` VALUES (7, 'OTC认证', 2, 'com.ruoyi.qlcoin.controller.OTCMemberController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/OTCMember/edit', '117.136.50.204', 'XX XX', '{\n  \"id\" : [ \"1005d697b9d584e947d4d8006cee6133\" ],\n  \"phone\" : [ \"13858833008\" ],\n  \"uname\" : [ \"倪秀燕\" ],\n  \"cardNo\" : [ \"330322197401252026\" ],\n  \"wechatName\" : [ \"\" ],\n  \"aliayName\" : [ \"\" ],\n  \"cardState\" : [ \"PASS\" ],\n  \"storeName\" : [ \"\" ],\n  \"bankUserName\" : [ \"\" ],\n  \"bankName\" : [ \"\" ],\n  \"bankAddress\" : [ \"\" ],\n  \"bankCard\" : [ \"\" ],\n  \"storeState\" : [ \"NO\" ],\n  \"areaCode\" : [ \"86\" ],\n  \"userStatus\" : [ \"NORMAL\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-22 15:21:33');
INSERT INTO `sys_oper_log` VALUES (8, '用户余额', 2, 'com.ruoyi.qlcoin.controller.TBalanceController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TBalance/edit', '117.136.50.204', 'XX XX', '{\n  \"id\" : [ \"18770494f3ad40f48bc143d205b064be\" ],\n  \"userId\" : [ \"1005d697b9d584e947d4d8006cee6133\" ],\n  \"currency\" : [ \"BS\" ],\n  \"blockedBalance\" : [ \"10000\" ],\n  \"balance\" : [ \"0E-30\" ],\n  \"assetsBlockedBalance\" : [ \"0E-30\" ],\n  \"assetsBalance\" : [ \"0E-30\" ],\n  \"tokenBalance\" : [ \"0E-30\" ],\n  \"tokenBlockedBalance\" : [ \"0E-30\" ],\n  \"fbBalance\" : [ \"0E-30\" ],\n  \"fbBlockedBalance\" : [ \"0E-30\" ],\n  \"raiseBalance\" : [ \"0E-30\" ],\n  \"chainBalance\" : [ \"0E-30\" ],\n  \"remarks\" : [ \"NORMAL\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-22 15:22:13');
INSERT INTO `sys_oper_log` VALUES (9, 'OTC认证', 2, 'com.ruoyi.qlcoin.controller.OTCMemberController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/OTCMember/edit', '117.136.50.204', 'XX XX', '{\n  \"id\" : [ \"a0d47bb216e88bb4a6521ff683c18605\" ],\n  \"phone\" : [ \"13572829862\" ],\n  \"uname\" : [ \"许百剑\" ],\n  \"cardNo\" : [ \"610112196612160016\" ],\n  \"wechatName\" : [ \"\" ],\n  \"aliayName\" : [ \"\" ],\n  \"cardState\" : [ \"PASS\" ],\n  \"storeName\" : [ \"\" ],\n  \"bankUserName\" : [ \"\" ],\n  \"bankName\" : [ \"\" ],\n  \"bankAddress\" : [ \"\" ],\n  \"bankCard\" : [ \"\" ],\n  \"storeState\" : [ \"NO\" ],\n  \"areaCode\" : [ \"86\" ],\n  \"userStatus\" : [ \"NORMAL\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-22 15:22:52');
INSERT INTO `sys_oper_log` VALUES (10, '用户余额', 2, 'com.ruoyi.qlcoin.controller.TBalanceController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TBalance/edit', '117.136.50.204', 'XX XX', '{\n  \"id\" : [ \"3201b5e27e4b159b5609e3e9801e9ecb\" ],\n  \"userId\" : [ \"a0d47bb216e88bb4a6521ff683c18605\" ],\n  \"currency\" : [ \"BS\" ],\n  \"blockedBalance\" : [ \"10000\" ],\n  \"balance\" : [ \"0E-30\" ],\n  \"assetsBlockedBalance\" : [ \"0E-30\" ],\n  \"assetsBalance\" : [ \"0E-30\" ],\n  \"tokenBalance\" : [ \"0E-30\" ],\n  \"tokenBlockedBalance\" : [ \"0E-30\" ],\n  \"fbBalance\" : [ \"0E-30\" ],\n  \"fbBlockedBalance\" : [ \"0E-30\" ],\n  \"raiseBalance\" : [ \"0E-30\" ],\n  \"chainBalance\" : [ \"0E-30\" ],\n  \"remarks\" : [ \"NORMAL\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-22 15:23:25');
INSERT INTO `sys_oper_log` VALUES (11, '用户余额', 2, 'com.ruoyi.qlcoin.controller.TBalanceController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TBalance/edit', '117.136.50.204', 'XX XX', '{\n  \"id\" : [ \"70f2b115330f7e1bfd0c24fd4bf7ad1c\" ],\n  \"userId\" : [ \"a0d47bb216e88bb4a6521ff683c18605\" ],\n  \"currency\" : [ \"USDT\" ],\n  \"blockedBalance\" : [ \"0E-30\" ],\n  \"balance\" : [ \"83256.96\" ],\n  \"assetsBlockedBalance\" : [ \"0E-30\" ],\n  \"assetsBalance\" : [ \"0E-30\" ],\n  \"tokenBalance\" : [ \"0E-30\" ],\n  \"tokenBlockedBalance\" : [ \"0E-30\" ],\n  \"fbBalance\" : [ \"0E-30\" ],\n  \"fbBlockedBalance\" : [ \"0E-30\" ],\n  \"raiseBalance\" : [ \"0E-30\" ],\n  \"chainBalance\" : [ \"0E-30\" ],\n  \"remarks\" : [ \"NORMAL\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-22 15:24:36');
INSERT INTO `sys_oper_log` VALUES (12, '用户余额', 2, 'com.ruoyi.qlcoin.controller.TBalanceController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TBalance/edit', '117.136.50.204', 'XX XX', '{\n  \"id\" : [ \"ef0e7d89b7e8f4bdd9ba47a8f20649fe\" ],\n  \"userId\" : [ \"a0d47bb216e88bb4a6521ff683c18605\" ],\n  \"currency\" : [ \"BTC\" ],\n  \"blockedBalance\" : [ \"0E-30\" ],\n  \"balance\" : [ \"3.69\" ],\n  \"assetsBlockedBalance\" : [ \"0E-30\" ],\n  \"assetsBalance\" : [ \"0E-30\" ],\n  \"tokenBalance\" : [ \"0E-30\" ],\n  \"tokenBlockedBalance\" : [ \"0E-30\" ],\n  \"fbBalance\" : [ \"0E-30\" ],\n  \"fbBlockedBalance\" : [ \"0E-30\" ],\n  \"raiseBalance\" : [ \"0E-30\" ],\n  \"chainBalance\" : [ \"0E-30\" ],\n  \"remarks\" : [ \"NORMAL\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-22 15:25:30');
INSERT INTO `sys_oper_log` VALUES (13, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '223.104.11.24', 'XX XX', '{\n  \"id\" : [ \"8e981272b7bd47d2994a23981147d12b\" ],\n  \"pairName\" : [ \"BS/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"0.205\" ],\n  \"willTime\" : [ \"2020-08-22 00:00:00\" ],\n  \"bindUser\" : [ \"\" ],\n  \"startNum\" : [ \"100.00000000\" ],\n  \"endNum\" : [ \"10000.00000000\" ],\n  \"mstartNum\" : [ \"100.00000000\" ],\n  \"mendNum\" : [ \"10000.00000000\" ],\n  \"startPrice\" : [ \"0.00010000\" ],\n  \"endPrice\" : [ \"0.00030000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-23 10:32:47');
INSERT INTO `sys_oper_log` VALUES (14, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '223.104.11.24', 'XX XX', '{\n  \"id\" : [ \"8e981272b7bd47d2994a23981147d12b\" ],\n  \"pairName\" : [ \"BS/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"0.21\" ],\n  \"willTime\" : [ \"2020-08-22\" ],\n  \"bindUser\" : [ \"\" ],\n  \"startNum\" : [ \"100.00000000\" ],\n  \"endNum\" : [ \"10000.00000000\" ],\n  \"mstartNum\" : [ \"100.00000000\" ],\n  \"mendNum\" : [ \"10000.00000000\" ],\n  \"startPrice\" : [ \"0.00010000\" ],\n  \"endPrice\" : [ \"0.00030000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-23 10:32:50');
INSERT INTO `sys_oper_log` VALUES (15, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '223.104.11.24', 'XX XX', '{\n  \"id\" : [ \"8e981272b7bd47d2994a23981147d12b\" ],\n  \"pairName\" : [ \"BS/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"0.29\" ],\n  \"willTime\" : [ \"2020-08-26\" ],\n  \"bindUser\" : [ \"\" ],\n  \"startNum\" : [ \"100.00000000\" ],\n  \"endNum\" : [ \"10000.00000000\" ],\n  \"mstartNum\" : [ \"100.00000000\" ],\n  \"mendNum\" : [ \"10000.00000000\" ],\n  \"startPrice\" : [ \"0.00010000\" ],\n  \"endPrice\" : [ \"0.00030000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-23 10:33:34');
INSERT INTO `sys_oper_log` VALUES (16, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '111.21.63.107', 'XX XX', '{\n  \"id\" : [ \"0b5a2cacd3f74eb1a0c3e947acc381d2\" ],\n  \"pairName\" : [ \"BTCV/USDT \" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"465.00000000\" ],\n  \"willTime\" : [ \"2020-08-28 11:45\" ],\n  \"bindUser\" : [ \"\" ],\n  \"startNum\" : [ \"0.03000000\" ],\n  \"endNum\" : [ \"5.00000000\" ],\n  \"mstartNum\" : [ \"0.03000000\" ],\n  \"mendNum\" : [ \"5.00000000\" ],\n  \"startPrice\" : [ \"0.30000000\" ],\n  \"endPrice\" : [ \"1.50000000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-24 10:12:53');
INSERT INTO `sys_oper_log` VALUES (17, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '111.21.63.107', 'XX XX', '{\n  \"id\" : [ \"1\" ],\n  \"pairName\" : [ \"SQT/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"5.00000000\" ],\n  \"willTime\" : [ \"2020-08-28 13:45\" ],\n  \"bindUser\" : [ \"18234034894\" ],\n  \"startNum\" : [ \"100.00000000\" ],\n  \"endNum\" : [ \"5000.00000000\" ],\n  \"mstartNum\" : [ \"1000.00000000\" ],\n  \"mendNum\" : [ \"5000.00000000\" ],\n  \"startPrice\" : [ \"0.00100000\" ],\n  \"endPrice\" : [ \"0.00300000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-24 10:13:06');
INSERT INTO `sys_oper_log` VALUES (18, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '111.21.63.107', 'XX XX', '{\n  \"id\" : [ \"96c600738b354f508efc7117bdeed109\" ],\n  \"pairName\" : [ \"FIL/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"12.00000000\" ],\n  \"willTime\" : [ \"2020-08-28 06:30\" ],\n  \"bindUser\" : [ \"\" ],\n  \"startNum\" : [ \"10.00000000\" ],\n  \"endNum\" : [ \"500.00000000\" ],\n  \"mstartNum\" : [ \"10.00000000\" ],\n  \"mendNum\" : [ \"500.00000000\" ],\n  \"startPrice\" : [ \"0.00100000\" ],\n  \"endPrice\" : [ \"0.03000000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-24 10:13:21');
INSERT INTO `sys_oper_log` VALUES (19, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '111.21.63.107', 'XX XX', '{\n  \"id\" : [ \"8e981272b7bd47d2994a23981147d12b\" ],\n  \"pairName\" : [ \"BS/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"0.29000000\" ],\n  \"willTime\" : [ \"2020-08-28 00:30\" ],\n  \"bindUser\" : [ \"\" ],\n  \"startNum\" : [ \"100.00000000\" ],\n  \"endNum\" : [ \"10000.00000000\" ],\n  \"mstartNum\" : [ \"100.00000000\" ],\n  \"mendNum\" : [ \"10000.00000000\" ],\n  \"startPrice\" : [ \"0.00010000\" ],\n  \"endPrice\" : [ \"0.00030000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-24 10:14:03');
INSERT INTO `sys_oper_log` VALUES (20, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '111.21.63.107', 'XX XX', '{\n  \"id\" : [ \"469b03e402a14c42a8dcced5e8d28d54\" ],\n  \"pairName\" : [ \"EOS/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"0E-8\" ],\n  \"willTime\" : [ \"2020-10-24 11:25\" ],\n  \"bindUser\" : [ \"\" ],\n  \"startNum\" : [ \"10.00000000\" ],\n  \"endNum\" : [ \"1000.00000000\" ],\n  \"mstartNum\" : [ \"10.00000000\" ],\n  \"mendNum\" : [ \"1000.00000000\" ],\n  \"startPrice\" : [ \"0.00100000\" ],\n  \"endPrice\" : [ \"0.00300000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-24 10:15:44');
INSERT INTO `sys_oper_log` VALUES (21, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '111.21.63.107', 'XX XX', '{\n  \"id\" : [ \"cac1bf35075245a19e6ffaf46a00366c\" ],\n  \"pairName\" : [ \"BCH/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"\" ],\n  \"willTime\" : [ \"2020-10-22 14:30\" ],\n  \"bindUser\" : [ \"\" ],\n  \"startNum\" : [ \"0.03000000\" ],\n  \"endNum\" : [ \"30.00000000\" ],\n  \"mstartNum\" : [ \"0.03000000\" ],\n  \"mendNum\" : [ \"30.00000000\" ],\n  \"startPrice\" : [ \"0.10010000\" ],\n  \"endPrice\" : [ \"0.30000000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-24 10:31:05');
INSERT INTO `sys_oper_log` VALUES (22, '私募订单管理', 3, 'com.ruoyi.qlcoin.controller.TPeOrderController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/order/remove', '113.139.240.61', 'XX XX', '{\n  \"ids\" : [ \"415526ee-7129-4602-a3ec-1e00699ae0c9\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-25 21:29:45');
INSERT INTO `sys_oper_log` VALUES (23, '登陆日志', 9, 'com.ruoyi.web.controller.monitor.SysLogininforController.clean()', 'POST', 1, 'admin', '研发部门', '/monitor/logininfor/clean', '113.139.240.61', 'XX XX', '{ }', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-25 21:30:23');
INSERT INTO `sys_oper_log` VALUES (24, 'OTC认证', 2, 'com.ruoyi.qlcoin.controller.OTCMemberController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/OTCMember/edit', '117.136.50.202', 'XX XX', '{\n  \"id\" : [ \"888daa2673fe5feeabe53f54bd1715ab\" ],\n  \"phone\" : [ \"13376725602\" ],\n  \"uname\" : [ \"陈上明\" ],\n  \"cardNo\" : [ \"440883199001162210\" ],\n  \"wechatName\" : [ \"\" ],\n  \"aliayName\" : [ \"\" ],\n  \"cardState\" : [ \"PASS\" ],\n  \"storeName\" : [ \"\" ],\n  \"bankUserName\" : [ \"\" ],\n  \"bankName\" : [ \"\" ],\n  \"bankAddress\" : [ \"\" ],\n  \"bankCard\" : [ \"\" ],\n  \"storeState\" : [ \"NO\" ],\n  \"areaCode\" : [ \"86\" ],\n  \"userStatus\" : [ \"NORMAL\" ]\n}', 'null', 1, '\n### Error updating database.  Cause: com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException: Lock wait timeout exceeded; try restarting transaction\n### The error may involve com.ruoyi.qlcoin.mapper.OTCMemberMapper.updateOTCMember-Inline\n### The error occurred while setting parameters\n### SQL: update t_member          SET phone = ?,                                                                                             uname = ?,                                       card_no = ?,                                                                                                                                                                                       card_state = ?,                          update_time = ?,                                                                                           store_state = ?,             area_code = ?,             user_status = ?          where id = ?\n### Cause: com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException: Lock wait timeout exceeded; try restarting transaction\n; Lock wait timeout exceeded; try restarting transaction; nested exception is com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException: Lock wait timeout exceeded; try restarting transaction', '2020-08-27 01:05:54');
INSERT INTO `sys_oper_log` VALUES (25, 'OTC认证', 2, 'com.ruoyi.qlcoin.controller.OTCMemberController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/OTCMember/edit', '117.136.50.202', 'XX XX', '{\n  \"id\" : [ \"888daa2673fe5feeabe53f54bd1715ab\" ],\n  \"phone\" : [ \"13376725602\" ],\n  \"uname\" : [ \"陈上明\" ],\n  \"cardNo\" : [ \"440883199001162210\" ],\n  \"wechatName\" : [ \"\" ],\n  \"aliayName\" : [ \"\" ],\n  \"cardState\" : [ \"PASS\" ],\n  \"storeName\" : [ \"\" ],\n  \"bankUserName\" : [ \"\" ],\n  \"bankName\" : [ \"\" ],\n  \"bankAddress\" : [ \"\" ],\n  \"bankCard\" : [ \"\" ],\n  \"storeState\" : [ \"NO\" ],\n  \"areaCode\" : [ \"86\" ],\n  \"userStatus\" : [ \"NORMAL\" ]\n}', 'null', 1, '\n### Error updating database.  Cause: com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException: Lock wait timeout exceeded; try restarting transaction\n### The error may involve com.ruoyi.qlcoin.mapper.OTCMemberMapper.updateOTCMember-Inline\n### The error occurred while setting parameters\n### SQL: update t_member          SET phone = ?,                                                                                             uname = ?,                                       card_no = ?,                                                                                                                                                                                       card_state = ?,                          update_time = ?,                                                                                           store_state = ?,             area_code = ?,             user_status = ?          where id = ?\n### Cause: com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException: Lock wait timeout exceeded; try restarting transaction\n; Lock wait timeout exceeded; try restarting transaction; nested exception is com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException: Lock wait timeout exceeded; try restarting transaction', '2020-08-27 01:06:14');
INSERT INTO `sys_oper_log` VALUES (26, 'OTC认证', 2, 'com.ruoyi.qlcoin.controller.OTCMemberController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/OTCMember/edit', '117.136.50.202', 'XX XX', '{\n  \"id\" : [ \"888daa2673fe5feeabe53f54bd1715ab\" ],\n  \"phone\" : [ \"13376725602\" ],\n  \"uname\" : [ \"陈上明\" ],\n  \"cardNo\" : [ \"440883199001162210\" ],\n  \"wechatName\" : [ \"\" ],\n  \"aliayName\" : [ \"\" ],\n  \"cardState\" : [ \"WAIT\" ],\n  \"storeName\" : [ \"\" ],\n  \"bankUserName\" : [ \"\" ],\n  \"bankName\" : [ \"\" ],\n  \"bankAddress\" : [ \"\" ],\n  \"bankCard\" : [ \"\" ],\n  \"storeState\" : [ \"NO\" ],\n  \"areaCode\" : [ \"86\" ],\n  \"userStatus\" : [ \"NORMAL\" ]\n}', 'null', 1, '\n### Error updating database.  Cause: com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException: Lock wait timeout exceeded; try restarting transaction\n### The error may involve com.ruoyi.qlcoin.mapper.OTCMemberMapper.updateOTCMember-Inline\n### The error occurred while setting parameters\n### SQL: update t_member          SET phone = ?,                                                                                             uname = ?,                                       card_no = ?,                                                                                                                                                                                       card_state = ?,                          update_time = ?,                                                                                           store_state = ?,             area_code = ?,             user_status = ?          where id = ?\n### Cause: com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException: Lock wait timeout exceeded; try restarting transaction\n; Lock wait timeout exceeded; try restarting transaction; nested exception is com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException: Lock wait timeout exceeded; try restarting transaction', '2020-08-27 01:07:17');
INSERT INTO `sys_oper_log` VALUES (27, '重置密码', 2, 'com.ruoyi.web.controller.system.SysProfileController.resetPwd()', 'POST', 1, 'admin', '研发部门', '/system/user/profile/resetPwd', '1.86.245.215', 'XX XX', '{\n  \"userId\" : [ \"1\" ],\n  \"loginName\" : [ \"admin\" ],\n  \"oldPassword\" : [ \"admin123\" ],\n  \"newPassword\" : [ \"123admin\" ],\n  \"confirm\" : [ \"123admin\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-28 02:50:24');
INSERT INTO `sys_oper_log` VALUES (28, '用户余额', 2, 'com.ruoyi.qlcoin.controller.TBalanceController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TBalance/edit', '222.90.67.10', 'XX XX', '{\n  \"id\" : [ \"afc3f4ce3abc3dd82a120fc122c9bf05\" ],\n  \"userId\" : [ \"3276801b3614610f4a0f06fe9770ef83\" ],\n  \"currency\" : [ \"USDT\" ],\n  \"blockedBalance\" : [ \"0E-30\" ],\n  \"balance\" : [ \"0E-30\" ],\n  \"assetsBlockedBalance\" : [ \"0E-30\" ],\n  \"assetsBalance\" : [ \"0E-30\" ],\n  \"tokenBalance\" : [ \"10000\" ],\n  \"tokenBlockedBalance\" : [ \"0E-30\" ],\n  \"fbBalance\" : [ \"0E-30\" ],\n  \"fbBlockedBalance\" : [ \"0E-30\" ],\n  \"raiseBalance\" : [ \"0E-30\" ],\n  \"chainBalance\" : [ \"0E-30\" ],\n  \"remarks\" : [ \"NORMAL\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-28 09:12:36');
INSERT INTO `sys_oper_log` VALUES (29, '用户余额', 2, 'com.ruoyi.qlcoin.controller.TBalanceController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TBalance/edit', '222.90.67.10', 'XX XX', '{\n  \"id\" : [ \"ca1140d6ca503569789b2c4d51673983\" ],\n  \"userId\" : [ \"a505fd450c92406bfb2a52c530bd8009\" ],\n  \"currency\" : [ \"USDT\" ],\n  \"blockedBalance\" : [ \"0E-30\" ],\n  \"balance\" : [ \"0E-30\" ],\n  \"assetsBlockedBalance\" : [ \"0E-30\" ],\n  \"assetsBalance\" : [ \"0E-30\" ],\n  \"tokenBalance\" : [ \"100000\" ],\n  \"tokenBlockedBalance\" : [ \"0E-30\" ],\n  \"fbBalance\" : [ \"0E-30\" ],\n  \"fbBlockedBalance\" : [ \"0E-30\" ],\n  \"raiseBalance\" : [ \"0E-30\" ],\n  \"chainBalance\" : [ \"0E-30\" ],\n  \"remarks\" : [ \"MAIN_WITHDRAW_ACCOUNT\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-28 12:22:16');
INSERT INTO `sys_oper_log` VALUES (30, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '113.140.24.5', 'XX XX', '{\n  \"id\" : [ \"8e981272b7bd47d2994a23981147d12b\" ],\n  \"pairName\" : [ \"BS/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"0.29000000\" ],\n  \"willTime\" : [ \"2020-10-28 18:30\" ],\n  \"bindUser\" : [ \"\" ],\n  \"startNum\" : [ \"100.00000000\" ],\n  \"endNum\" : [ \"10000.00000000\" ],\n  \"mstartNum\" : [ \"100.00000000\" ],\n  \"mendNum\" : [ \"10000.00000000\" ],\n  \"startPrice\" : [ \"0.00010000\" ],\n  \"endPrice\" : [ \"0.00030000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-28 12:31:06');
INSERT INTO `sys_oper_log` VALUES (31, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '113.140.24.5', 'XX XX', '{\n  \"id\" : [ \"1\" ],\n  \"pairName\" : [ \"SQT/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"5.00000000\" ],\n  \"willTime\" : [ \"2020-10-28 13:45\" ],\n  \"bindUser\" : [ \"18234034894\" ],\n  \"startNum\" : [ \"100.00000000\" ],\n  \"endNum\" : [ \"5000.00000000\" ],\n  \"mstartNum\" : [ \"1000.00000000\" ],\n  \"mendNum\" : [ \"5000.00000000\" ],\n  \"startPrice\" : [ \"0.00100000\" ],\n  \"endPrice\" : [ \"0.00300000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-28 12:31:14');
INSERT INTO `sys_oper_log` VALUES (32, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '113.140.24.5', 'XX XX', '{\n  \"id\" : [ \"96c600738b354f508efc7117bdeed109\" ],\n  \"pairName\" : [ \"FIL/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"12.00000000\" ],\n  \"willTime\" : [ \"2020-10-28 10:30\" ],\n  \"bindUser\" : [ \"\" ],\n  \"startNum\" : [ \"10.00000000\" ],\n  \"endNum\" : [ \"500.00000000\" ],\n  \"mstartNum\" : [ \"10.00000000\" ],\n  \"mendNum\" : [ \"500.00000000\" ],\n  \"startPrice\" : [ \"0.00100000\" ],\n  \"endPrice\" : [ \"0.03000000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-28 12:31:27');
INSERT INTO `sys_oper_log` VALUES (33, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '113.140.24.5', 'XX XX', '{\n  \"id\" : [ \"0b5a2cacd3f74eb1a0c3e947acc381d2\" ],\n  \"pairName\" : [ \"BTCV/USDT \" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"465.00000000\" ],\n  \"willTime\" : [ \"2020-10-28 11:45\" ],\n  \"bindUser\" : [ \"\" ],\n  \"startNum\" : [ \"0.03000000\" ],\n  \"endNum\" : [ \"5.00000000\" ],\n  \"mstartNum\" : [ \"0.03000000\" ],\n  \"mendNum\" : [ \"5.00000000\" ],\n  \"startPrice\" : [ \"0.30000000\" ],\n  \"endPrice\" : [ \"1.50000000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-28 12:31:54');
INSERT INTO `sys_oper_log` VALUES (34, '用户余额', 2, 'com.ruoyi.qlcoin.controller.TBalanceController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TBalance/edit', '222.90.67.10', 'XX XX', '{\n  \"id\" : [ \"ca1140d6ca503569789b2c4d51673983\" ],\n  \"userId\" : [ \"a505fd450c92406bfb2a52c530bd8009\" ],\n  \"currency\" : [ \"USDT\" ],\n  \"blockedBalance\" : [ \"0E-30\" ],\n  \"balance\" : [ \"0E-30\" ],\n  \"assetsBlockedBalance\" : [ \"0E-30\" ],\n  \"assetsBalance\" : [ \"0E-30\" ],\n  \"tokenBalance\" : [ \"10000\" ],\n  \"tokenBlockedBalance\" : [ \"0E-30\" ],\n  \"fbBalance\" : [ \"0E-30\" ],\n  \"fbBlockedBalance\" : [ \"0E-30\" ],\n  \"raiseBalance\" : [ \"0E-30\" ],\n  \"chainBalance\" : [ \"0E-30\" ],\n  \"remarks\" : [ \"MAIN_WITHDRAW_ACCOUNT\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-28 12:44:33');
INSERT INTO `sys_oper_log` VALUES (35, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '113.140.24.5', 'XX XX', '{\n  \"id\" : [ \"8e981272b7bd47d2994a23981147d12b\" ],\n  \"pairName\" : [ \"BS/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"0.24000000\" ],\n  \"willTime\" : [ \"2020-10-28 18:30:00\" ],\n  \"bindUser\" : [ \"\" ],\n  \"startNum\" : [ \"100.00000000\" ],\n  \"endNum\" : [ \"10000.00000000\" ],\n  \"mstartNum\" : [ \"100.00000000\" ],\n  \"mendNum\" : [ \"10000.00000000\" ],\n  \"startPrice\" : [ \"0.00010000\" ],\n  \"endPrice\" : [ \"0.00030000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-28 19:49:12');
INSERT INTO `sys_oper_log` VALUES (36, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '113.140.24.5', 'XX XX', '{\n  \"id\" : [ \"96c600738b354f508efc7117bdeed109\" ],\n  \"pairName\" : [ \"FIL/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"14.00000000\" ],\n  \"willTime\" : [ \"2020-10-28 10:30:00\" ],\n  \"bindUser\" : [ \"\" ],\n  \"startNum\" : [ \"10.00000000\" ],\n  \"endNum\" : [ \"500.00000000\" ],\n  \"mstartNum\" : [ \"10.00000000\" ],\n  \"mendNum\" : [ \"500.00000000\" ],\n  \"startPrice\" : [ \"0.00100000\" ],\n  \"endPrice\" : [ \"0.03000000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-28 19:49:14');
INSERT INTO `sys_oper_log` VALUES (37, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '113.140.24.5', 'XX XX', '{\n  \"id\" : [ \"1\" ],\n  \"pairName\" : [ \"SQT/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"6.00000000\" ],\n  \"willTime\" : [ \"2020-10-28 13:45:00\" ],\n  \"bindUser\" : [ \"18234034894\" ],\n  \"startNum\" : [ \"100.00000000\" ],\n  \"endNum\" : [ \"5000.00000000\" ],\n  \"mstartNum\" : [ \"1000.00000000\" ],\n  \"mendNum\" : [ \"5000.00000000\" ],\n  \"startPrice\" : [ \"0.00100000\" ],\n  \"endPrice\" : [ \"0.00300000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-28 19:49:35');
INSERT INTO `sys_oper_log` VALUES (38, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '113.140.24.5', 'XX XX', '{\n  \"id\" : [ \"0b5a2cacd3f74eb1a0c3e947acc381d2\" ],\n  \"pairName\" : [ \"BTCV/USDT \" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"100.00000000\" ],\n  \"willTime\" : [ \"2020-10-28 11:45:00\" ],\n  \"bindUser\" : [ \"\" ],\n  \"startNum\" : [ \"0.03000000\" ],\n  \"endNum\" : [ \"5.00000000\" ],\n  \"mstartNum\" : [ \"0.03000000\" ],\n  \"mendNum\" : [ \"5.00000000\" ],\n  \"startPrice\" : [ \"0.30000000\" ],\n  \"endPrice\" : [ \"1.50000000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-28 19:50:02');
INSERT INTO `sys_oper_log` VALUES (39, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '113.140.24.5', 'XX XX', '{\n  \"id\" : [ \"8e981272b7bd47d2994a23981147d12b\" ],\n  \"pairName\" : [ \"BS/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"0.24000000\" ],\n  \"willTime\" : [ \"2020-10-28 18:30:00\" ],\n  \"bindUser\" : [ \"\" ],\n  \"startNum\" : [ \"100.00000000\" ],\n  \"endNum\" : [ \"10000.00000000\" ],\n  \"mstartNum\" : [ \"100.00000000\" ],\n  \"mendNum\" : [ \"10000.00000000\" ],\n  \"startPrice\" : [ \"0.00010000\" ],\n  \"endPrice\" : [ \"0.00030000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-28 19:55:35');
INSERT INTO `sys_oper_log` VALUES (40, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '113.140.24.5', 'XX XX', '{\n  \"id\" : [ \"1\" ],\n  \"pairName\" : [ \"SQT/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"4.00000000\" ],\n  \"willTime\" : [ \"2020-08-28 20:00:00\" ],\n  \"bindUser\" : [ \"18234034894\" ],\n  \"startNum\" : [ \"100.00000000\" ],\n  \"endNum\" : [ \"5000.00000000\" ],\n  \"mstartNum\" : [ \"1000.00000000\" ],\n  \"mendNum\" : [ \"5000.00000000\" ],\n  \"startPrice\" : [ \"0.00100000\" ],\n  \"endPrice\" : [ \"0.00300000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-28 19:59:12');
INSERT INTO `sys_oper_log` VALUES (41, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '113.140.24.5', 'XX XX', '{\n  \"id\" : [ \"1\" ],\n  \"pairName\" : [ \"SQT/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"6.00000000\" ],\n  \"willTime\" : [ \"2020-08-28 20:00\" ],\n  \"bindUser\" : [ \"18234034894\" ],\n  \"startNum\" : [ \"100.00000000\" ],\n  \"endNum\" : [ \"5000.00000000\" ],\n  \"mstartNum\" : [ \"1000.00000000\" ],\n  \"mendNum\" : [ \"5000.00000000\" ],\n  \"startPrice\" : [ \"0.00100000\" ],\n  \"endPrice\" : [ \"0.00300000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-28 19:59:16');
INSERT INTO `sys_oper_log` VALUES (42, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '113.140.24.5', 'XX XX', '{\n  \"id\" : [ \"1\" ],\n  \"pairName\" : [ \"SQT/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"3.6200000000\" ],\n  \"willTime\" : [ \"2020-08-28 20:00:00\" ],\n  \"bindUser\" : [ \"18234034894\" ],\n  \"startNum\" : [ \"100.00000000\" ],\n  \"endNum\" : [ \"5000.00000000\" ],\n  \"mstartNum\" : [ \"1000.00000000\" ],\n  \"mendNum\" : [ \"5000.00000000\" ],\n  \"startPrice\" : [ \"0.00100000\" ],\n  \"endPrice\" : [ \"0.00300000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-28 19:59:51');
INSERT INTO `sys_oper_log` VALUES (43, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '113.140.24.5', 'XX XX', '{\n  \"id\" : [ \"1\" ],\n  \"pairName\" : [ \"SQT/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"3.62000000\" ],\n  \"willTime\" : [ \"2020-10-28 20:00\" ],\n  \"bindUser\" : [ \"18234034894\" ],\n  \"startNum\" : [ \"100.00000000\" ],\n  \"endNum\" : [ \"5000.00000000\" ],\n  \"mstartNum\" : [ \"1000.00000000\" ],\n  \"mendNum\" : [ \"5000.00000000\" ],\n  \"startPrice\" : [ \"0.00100000\" ],\n  \"endPrice\" : [ \"0.00300000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-28 20:02:25');
INSERT INTO `sys_oper_log` VALUES (44, '重置密码', 2, 'com.ruoyi.web.controller.system.SysUserController.resetPwd()', 'GET', 1, 'admin', '研发部门', '/system/user/resetPwd/1', '36.44.182.1', 'XX XX', '{ }', '\"system/user/resetPwd\"', 0, NULL, '2020-08-29 01:05:38');
INSERT INTO `sys_oper_log` VALUES (45, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '117.136.87.47', 'XX XX', '{\n  \"id\" : [ \"8e981272b7bd47d2994a23981147d12b\" ],\n  \"pairName\" : [ \"BS/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"0.3\" ],\n  \"willTime\" : [ \"2020-8-28 18:30\" ],\n  \"bindUser\" : [ \"\" ],\n  \"startNum\" : [ \"100.00000000\" ],\n  \"endNum\" : [ \"10000.00000000\" ],\n  \"mstartNum\" : [ \"100.00000000\" ],\n  \"mendNum\" : [ \"10000.00000000\" ],\n  \"startPrice\" : [ \"0.00010000\" ],\n  \"endPrice\" : [ \"0.00030000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-30 16:47:19');
INSERT INTO `sys_oper_log` VALUES (46, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '117.136.87.47', 'XX XX', '{\n  \"id\" : [ \"8e981272b7bd47d2994a23981147d12b\" ],\n  \"pairName\" : [ \"BS/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"0.18\" ],\n  \"willTime\" : [ \"2020-08-28 18:30:00\" ],\n  \"bindUser\" : [ \"\" ],\n  \"startNum\" : [ \"100.00000000\" ],\n  \"endNum\" : [ \"10000.00000000\" ],\n  \"mstartNum\" : [ \"100.00000000\" ],\n  \"mendNum\" : [ \"10000.00000000\" ],\n  \"startPrice\" : [ \"0.00010000\" ],\n  \"endPrice\" : [ \"0.00030000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-08-30 16:48:54');
INSERT INTO `sys_oper_log` VALUES (47, 'OTC认证', 2, 'com.ruoyi.qlcoin.controller.OTCMemberController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/OTCMember/edit', '103.75.117.65', 'XX XX', '{\n  \"id\" : [ \"1fb8aecf26e48e7157ee2a750d6e7b07\" ],\n  \"phone\" : [ \"13279457956\" ],\n  \"uname\" : [ \"测试\" ],\n  \"cardNo\" : [ \"\" ],\n  \"wechatName\" : [ \"\" ],\n  \"aliayName\" : [ \"\" ],\n  \"cardState\" : [ \"NO\" ],\n  \"storeName\" : [ \"\" ],\n  \"bankUserName\" : [ \"\" ],\n  \"bankName\" : [ \"\" ],\n  \"bankAddress\" : [ \"\" ],\n  \"bankCard\" : [ \"\" ],\n  \"storeState\" : [ \"NO\" ],\n  \"areaCode\" : [ \"86\" ],\n  \"userStatus\" : [ \"NORMAL\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-03 14:56:23');
INSERT INTO `sys_oper_log` VALUES (48, '私募项目管理', 2, 'com.ruoyi.qlcoin.controller.TPeProjectController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/project/edit', '103.75.117.65', 'XX XX', '{\n  \"id\" : [ \"1\" ],\n  \"orderIndex\" : [ \"1\" ],\n  \"projectName\" : [ \"BSEX第一期11\" ],\n  \"projectUrl\" : [ \"http://zhdwwrl.cn/bsex/bsex.html\" ],\n  \"projectImg\" : [ \"http://fvex.oss-cn-hangzhou.aliyuncs.com/fvex/1bb3401aba654ec281243f998df2e5a70012.jpg\" ],\n  \"startTime\" : [ \"2020-08-01\" ],\n  \"endTime\" : [ \"2020-08-15\" ],\n  \"nowNum\" : [ \"1000000.0\" ],\n  \"sumNum\" : [ \"1000000.0\" ],\n  \"minNum\" : [ \"1000.0\" ],\n  \"maxNum\" : [ \"2000.000000000000\" ],\n  \"coinPice\" : [ \"0.15\" ],\n  \"coinName\" : [ \"BSEX\" ],\n  \"status\" : [ \"PASS\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-03 15:01:09');
INSERT INTO `sys_oper_log` VALUES (49, '私募项目管理', 2, 'com.ruoyi.qlcoin.controller.TPeProjectController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/project/edit', '103.75.117.65', 'XX XX', '{\n  \"id\" : [ \"1\" ],\n  \"orderIndex\" : [ \"1\" ],\n  \"projectName\" : [ \"BSEX第一期\" ],\n  \"projectUrl\" : [ \"http://zhdwwrl.cn/bsex/bsex.html\" ],\n  \"projectImg\" : [ \"http://fvex.oss-cn-hangzhou.aliyuncs.com/fvex/1bb3401aba654ec281243f998df2e5a70012.jpg\" ],\n  \"startTime\" : [ \"2020-08-01\" ],\n  \"endTime\" : [ \"2020-08-15\" ],\n  \"nowNum\" : [ \"1000000.0\" ],\n  \"sumNum\" : [ \"1000000.0\" ],\n  \"minNum\" : [ \"1000.0\" ],\n  \"maxNum\" : [ \"2000.000000000000\" ],\n  \"coinPice\" : [ \"0.15\" ],\n  \"coinName\" : [ \"BSEX\" ],\n  \"status\" : [ \"PASS\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-03 15:01:26');
INSERT INTO `sys_oper_log` VALUES (50, 'OTC认证', 2, 'com.ruoyi.qlcoin.controller.OTCMemberController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/OTCMember/edit', '103.75.117.65', 'XX XX', '{\n  \"id\" : [ \"1fb8aecf26e48e7157ee2a750d6e7b07\" ],\n  \"phone\" : [ \"13279457956\" ],\n  \"uname\" : [ \"朱连军\" ],\n  \"cardNo\" : [ \"500231198801016011\" ],\n  \"wechatName\" : [ \"\" ],\n  \"aliayName\" : [ \"\" ],\n  \"cardState\" : [ \"PASS\" ],\n  \"storeName\" : [ \"\" ],\n  \"bankUserName\" : [ \"\" ],\n  \"bankName\" : [ \"\" ],\n  \"bankAddress\" : [ \"\" ],\n  \"bankCard\" : [ \"\" ],\n  \"storeState\" : [ \"NO\" ],\n  \"areaCode\" : [ \"86\" ],\n  \"userStatus\" : [ \"NORMAL\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-03 15:09:24');
INSERT INTO `sys_oper_log` VALUES (51, '交易对管理', 3, 'com.ruoyi.qlcoin.controller.TPairsController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TPairs/remove', '154.218.6.57', 'XX XX', '{\n  \"ids\" : [ \"d657eb10014d4a30bf24d33fe4750ccd,d578de6355125c15ff9d60079f720185\" ]\n}', 'null', 1, '\n### Error updating database.  Cause: java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist\n### The error may involve com.ruoyi.qlcoin.mapper.TPairsMapper.deleteTPairsByIds-Inline\n### The error occurred while setting parameters\n### SQL: delete from t_pairs where id in           (               ?          ,              ?          )\n### Cause: java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist\n; uncategorized SQLException; SQL state [HY000]; error code [1449]; The user specified as a definer (\'root\'@\'%\') does not exist; nested exception is java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist', '2020-09-08 12:08:51');
INSERT INTO `sys_oper_log` VALUES (52, '交易对管理', 3, 'com.ruoyi.qlcoin.controller.TPairsController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TPairs/remove', '154.218.6.57', 'XX XX', '{\n  \"ids\" : [ \"d657eb10014d4a30bf24d33fe4750ccd,d578de6355125c15ff9d60079f720185,c23eff15258a4b0ea772c9a0c24b8262\" ]\n}', 'null', 1, '\n### Error updating database.  Cause: java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist\n### The error may involve com.ruoyi.qlcoin.mapper.TPairsMapper.deleteTPairsByIds-Inline\n### The error occurred while setting parameters\n### SQL: delete from t_pairs where id in           (               ?          ,              ?          ,              ?          )\n### Cause: java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist\n; uncategorized SQLException; SQL state [HY000]; error code [1449]; The user specified as a definer (\'root\'@\'%\') does not exist; nested exception is java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist', '2020-09-08 12:09:19');
INSERT INTO `sys_oper_log` VALUES (53, '交易对管理', 3, 'com.ruoyi.qlcoin.controller.TPairsController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TPairs/remove', '154.218.6.57', 'XX XX', '{\n  \"ids\" : [ \"d657eb10014d4a30bf24d33fe4750ccd\" ]\n}', 'null', 1, '\n### Error updating database.  Cause: java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist\n### The error may involve com.ruoyi.qlcoin.mapper.TPairsMapper.deleteTPairsByIds-Inline\n### The error occurred while setting parameters\n### SQL: delete from t_pairs where id in           (               ?          )\n### Cause: java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist\n; uncategorized SQLException; SQL state [HY000]; error code [1449]; The user specified as a definer (\'root\'@\'%\') does not exist; nested exception is java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist', '2020-09-08 12:09:55');
INSERT INTO `sys_oper_log` VALUES (54, '交易对管理', 3, 'com.ruoyi.qlcoin.controller.TPairsController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TPairs/remove', '154.218.6.57', 'XX XX', '{\n  \"ids\" : [ \"d657eb10014d4a30bf24d33fe4750ccd\" ]\n}', 'null', 1, '\n### Error updating database.  Cause: java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist\n### The error may involve com.ruoyi.qlcoin.mapper.TPairsMapper.deleteTPairsByIds-Inline\n### The error occurred while setting parameters\n### SQL: delete from t_pairs where id in           (               ?          )\n### Cause: java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist\n; uncategorized SQLException; SQL state [HY000]; error code [1449]; The user specified as a definer (\'root\'@\'%\') does not exist; nested exception is java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist', '2020-09-08 12:15:48');
INSERT INTO `sys_oper_log` VALUES (55, '交易对管理', 3, 'com.ruoyi.qlcoin.controller.TPairsController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TPairs/remove', '154.218.6.57', 'XX XX', '{\n  \"ids\" : [ \"d657eb10014d4a30bf24d33fe4750ccd\" ]\n}', 'null', 1, '\n### Error updating database.  Cause: java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist\n### The error may involve com.ruoyi.qlcoin.mapper.TPairsMapper.deleteTPairsByIds-Inline\n### The error occurred while setting parameters\n### SQL: delete from t_pairs where id in           (               ?          )\n### Cause: java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist\n; uncategorized SQLException; SQL state [HY000]; error code [1449]; The user specified as a definer (\'root\'@\'%\') does not exist; nested exception is java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist', '2020-09-08 12:17:05');
INSERT INTO `sys_oper_log` VALUES (56, '交易对管理', 3, 'com.ruoyi.qlcoin.controller.TPairsController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TPairs/remove', '154.218.6.57', 'XX XX', '{\n  \"ids\" : [ \"c23eff15258a4b0ea772c9a0c24b8262\" ]\n}', 'null', 1, '\n### Error updating database.  Cause: java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist\n### The error may involve com.ruoyi.qlcoin.mapper.TPairsMapper.deleteTPairsByIds-Inline\n### The error occurred while setting parameters\n### SQL: delete from t_pairs where id in           (               ?          )\n### Cause: java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist\n; uncategorized SQLException; SQL state [HY000]; error code [1449]; The user specified as a definer (\'root\'@\'%\') does not exist; nested exception is java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist', '2020-09-08 12:17:18');
INSERT INTO `sys_oper_log` VALUES (57, '交易对管理', 3, 'com.ruoyi.qlcoin.controller.TPairsController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TPairs/remove', '154.218.6.57', 'XX XX', '{\n  \"ids\" : [ \"c23eff15258a4b0ea772c9a0c24b8262\" ]\n}', 'null', 1, '\n### Error updating database.  Cause: java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist\n### The error may involve com.ruoyi.qlcoin.mapper.TPairsMapper.deleteTPairsByIds-Inline\n### The error occurred while setting parameters\n### SQL: delete from t_pairs where id in           (               ?          )\n### Cause: java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist\n; uncategorized SQLException; SQL state [HY000]; error code [1449]; The user specified as a definer (\'root\'@\'%\') does not exist; nested exception is java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist', '2020-09-08 12:19:09');
INSERT INTO `sys_oper_log` VALUES (58, '交易对管理', 3, 'com.ruoyi.qlcoin.controller.TPairsController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TPairs/remove', '154.218.6.57', 'XX XX', '{\n  \"ids\" : [ \"c23eff15258a4b0ea772c9a0c24b8262\" ]\n}', 'null', 1, '\n### Error updating database.  Cause: java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist\n### The error may involve com.ruoyi.qlcoin.mapper.TPairsMapper.deleteTPairsByIds-Inline\n### The error occurred while setting parameters\n### SQL: delete from t_pairs where id in           (               ?          )\n### Cause: java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist\n; uncategorized SQLException; SQL state [HY000]; error code [1449]; The user specified as a definer (\'root\'@\'%\') does not exist; nested exception is java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist', '2020-09-08 12:19:45');
INSERT INTO `sys_oper_log` VALUES (59, '用户余额', 2, 'com.ruoyi.qlcoin.controller.TBalanceController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TBalance/edit', '113.52.132.216', 'XX XX', '{\n  \"id\" : [ \"c6cf3887b45103546c3b8d6e09362fa6\" ],\n  \"userId\" : [ \"1fb8aecf26e48e7157ee2a750d6e7b07\" ],\n  \"currency\" : [ \"BSEX\" ],\n  \"blockedBalance\" : [ \"0E-30\" ],\n  \"balance\" : [ \"0E-30\" ],\n  \"assetsBlockedBalance\" : [ \"0E-30\" ],\n  \"assetsBalance\" : [ \"5000\" ],\n  \"tokenBalance\" : [ \"0E-30\" ],\n  \"tokenBlockedBalance\" : [ \"0E-30\" ],\n  \"fbBalance\" : [ \"0E-30\" ],\n  \"fbBlockedBalance\" : [ \"0E-30\" ],\n  \"raiseBalance\" : [ \"0E-30\" ],\n  \"chainBalance\" : [ \"0E-30\" ],\n  \"remarks\" : [ \"NORMAL\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-08 12:50:49');
INSERT INTO `sys_oper_log` VALUES (60, '交易对管理', 3, 'com.ruoyi.qlcoin.controller.TPairsController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TPairs/remove', '113.52.132.216', 'XX XX', '{\n  \"ids\" : [ \"2e03d3b5433d448784cb6dabb386af6b\" ]\n}', 'null', 1, '\n### Error updating database.  Cause: java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist\n### The error may involve com.ruoyi.qlcoin.mapper.TPairsMapper.deleteTPairsByIds-Inline\n### The error occurred while setting parameters\n### SQL: delete from t_pairs where id in           (               ?          )\n### Cause: java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist\n; uncategorized SQLException; SQL state [HY000]; error code [1449]; The user specified as a definer (\'root\'@\'%\') does not exist; nested exception is java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist', '2020-09-08 13:21:27');
INSERT INTO `sys_oper_log` VALUES (61, '交易对管理', 3, 'com.ruoyi.qlcoin.controller.TPairsController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TPairs/remove', '113.52.132.216', 'XX XX', '{\n  \"ids\" : [ \"2e03d3b5433d448784cb6dabb386af6b\" ]\n}', 'null', 1, '\n### Error updating database.  Cause: java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist\n### The error may involve com.ruoyi.qlcoin.mapper.TPairsMapper.deleteTPairsByIds-Inline\n### The error occurred while setting parameters\n### SQL: delete from t_pairs where id in           (               ?          )\n### Cause: java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist\n; uncategorized SQLException; SQL state [HY000]; error code [1449]; The user specified as a definer (\'root\'@\'%\') does not exist; nested exception is java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist', '2020-09-08 13:37:29');
INSERT INTO `sys_oper_log` VALUES (62, '交易对管理', 3, 'com.ruoyi.qlcoin.controller.TPairsController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TPairs/remove', '113.52.132.216', 'XX XX', '{\n  \"ids\" : [ \"2e03d3b5433d448784cb6dabb386af6b\" ]\n}', 'null', 1, '\n### Error updating database.  Cause: java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist\n### The error may involve com.ruoyi.qlcoin.mapper.TPairsMapper.deleteTPairsByIds-Inline\n### The error occurred while setting parameters\n### SQL: delete from t_pairs where id in           (               ?          )\n### Cause: java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist\n; uncategorized SQLException; SQL state [HY000]; error code [1449]; The user specified as a definer (\'root\'@\'%\') does not exist; nested exception is java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist', '2020-09-08 13:37:36');
INSERT INTO `sys_oper_log` VALUES (63, '交易对管理', 2, 'com.ruoyi.qlcoin.controller.TPairsController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TPairs/edit', '113.52.132.216', 'XX XX', '{\n  \"id\" : [ \"2e03d3b5433d448784cb6dabb386af6b\" ],\n  \"pairsName\" : [ \"BTCV/USDT\" ],\n  \"mainCur\" : [ \"USDT\" ],\n  \"tokenCur\" : [ \"BTCVd\" ],\n  \"state\" : [ \"NORMAL\" ],\n  \"type\" : [ \"MAIN_COIN\" ],\n  \"sort\" : [ \"12\" ],\n  \"openPrice\" : [ \"0.0\" ],\n  \"price\" : [ \"465.45408798\" ],\n  \"isTop\" : [ \"0\" ],\n  \"tradeMax\" : [ \"50\" ],\n  \"tradeMin\" : [ \"1\" ],\n  \"tradeType\" : [ \"SPOT\" ],\n  \"tradeRate\" : [ \"0.003\" ],\n  \"mainFrom\" : [ \"BTC\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-08 13:40:39');
INSERT INTO `sys_oper_log` VALUES (64, '交易对管理', 3, 'com.ruoyi.qlcoin.controller.TPairsController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TPairs/remove', '113.52.132.216', 'XX XX', '{\n  \"ids\" : [ \"2e03d3b5433d448784cb6dabb386af6b\" ]\n}', 'null', 1, '\n### Error updating database.  Cause: java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist\n### The error may involve com.ruoyi.qlcoin.mapper.TPairsMapper.deleteTPairsByIds-Inline\n### The error occurred while setting parameters\n### SQL: delete from t_pairs where id in           (               ?          )\n### Cause: java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist\n; uncategorized SQLException; SQL state [HY000]; error code [1449]; The user specified as a definer (\'root\'@\'%\') does not exist; nested exception is java.sql.SQLException: The user specified as a definer (\'root\'@\'%\') does not exist', '2020-09-08 13:40:46');
INSERT INTO `sys_oper_log` VALUES (65, '交易对管理', 3, 'com.ruoyi.qlcoin.controller.TPairsController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TPairs/remove', '113.52.132.216', 'XX XX', '{\n  \"ids\" : [ \"2e03d3b5433d448784cb6dabb386af6b\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-08 13:41:57');
INSERT INTO `sys_oper_log` VALUES (66, '交易对管理', 3, 'com.ruoyi.qlcoin.controller.TPairsController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TPairs/remove', '154.218.6.57', 'XX XX', '{\n  \"ids\" : [ \"d657eb10014d4a30bf24d33fe4750ccd\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-08 14:18:30');
INSERT INTO `sys_oper_log` VALUES (67, '交易对管理', 3, 'com.ruoyi.qlcoin.controller.TPairsController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TPairs/remove', '154.218.6.57', 'XX XX', '{\n  \"ids\" : [ \"d578de6355125c15ff9d60079f720185,2afad48f7f194f9d96bbb67660277bc2,c23eff15258a4b0ea772c9a0c24b8262\" ]\n}', '{\n  \"msg\" : \"未查询到合约地址\",\n  \"code\" : 500\n}', 0, NULL, '2020-09-08 14:18:41');
INSERT INTO `sys_oper_log` VALUES (68, '交易对管理', 3, 'com.ruoyi.qlcoin.controller.TPairsController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TPairs/remove', '154.218.6.57', 'XX XX', '{\n  \"ids\" : [ \"d578de6355125c15ff9d60079f720185\" ]\n}', '{\n  \"msg\" : \"操作失败\",\n  \"code\" : 500\n}', 0, NULL, '2020-09-08 14:18:49');
INSERT INTO `sys_oper_log` VALUES (69, '交易对管理', 3, 'com.ruoyi.qlcoin.controller.TPairsController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TPairs/remove', '154.218.6.57', 'XX XX', '{\n  \"ids\" : [ \"c23eff15258a4b0ea772c9a0c24b8262\" ]\n}', '{\n  \"msg\" : \"操作失败\",\n  \"code\" : 500\n}', 0, NULL, '2020-09-08 14:18:56');
INSERT INTO `sys_oper_log` VALUES (70, '交易对管理', 3, 'com.ruoyi.qlcoin.controller.TPairsController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TPairs/remove', '154.218.6.57', 'XX XX', '{\n  \"ids\" : [ \"d1afa5fc53f94f6d9e7fc1a00d20931c\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-08 14:19:08');
INSERT INTO `sys_oper_log` VALUES (71, 'OTC认证', 2, 'com.ruoyi.qlcoin.controller.OTCMemberController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/OTCMember/edit', '113.200.107.43', 'XX XX', '{\n  \"id\" : [ \"448509298ab541aefd3b7628803174a1\" ],\n  \"phone\" : [ \"15509106509\" ],\n  \"uname\" : [ \"\" ],\n  \"cardNo\" : [ \"\" ],\n  \"wechatName\" : [ \"\" ],\n  \"aliayName\" : [ \"\" ],\n  \"cardState\" : [ \"PASS\" ],\n  \"storeName\" : [ \"\" ],\n  \"bankUserName\" : [ \"\" ],\n  \"bankName\" : [ \"\" ],\n  \"bankAddress\" : [ \"\" ],\n  \"bankCard\" : [ \"\" ],\n  \"storeState\" : [ \"PASS\" ],\n  \"areaCode\" : [ \"86\" ],\n  \"userStatus\" : [ \"NORMAL\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-10 10:54:00');
INSERT INTO `sys_oper_log` VALUES (72, '用户余额', 2, 'com.ruoyi.qlcoin.controller.TBalanceController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TBalance/edit', '113.200.107.43', 'XX XX', '{\n  \"id\" : [ \"0f6a12d495d5e8d9aa3250370bbe400a\" ],\n  \"userId\" : [ \"448509298ab541aefd3b7628803174a1\" ],\n  \"currency\" : [ \"USDT\" ],\n  \"blockedBalance\" : [ \"0E-30\" ],\n  \"balance\" : [ \"0E-30\" ],\n  \"assetsBlockedBalance\" : [ \"0E-30\" ],\n  \"assetsBalance\" : [ \"0E-30\" ],\n  \"tokenBalance\" : [ \"0E-30\" ],\n  \"tokenBlockedBalance\" : [ \"0E-30\" ],\n  \"fbBalance\" : [ \"10000\" ],\n  \"fbBlockedBalance\" : [ \"0E-30\" ],\n  \"raiseBalance\" : [ \"0E-30\" ],\n  \"chainBalance\" : [ \"0E-30\" ],\n  \"remarks\" : [ \"NORMAL\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-10 10:54:56');
INSERT INTO `sys_oper_log` VALUES (73, '用户余额', 2, 'com.ruoyi.qlcoin.controller.TBalanceController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TBalance/edit', '113.200.107.43', 'XX XX', '{\n  \"id\" : [ \"0f6a12d495d5e8d9aa3250370bbe400a\" ],\n  \"userId\" : [ \"448509298ab541aefd3b7628803174a1\" ],\n  \"currency\" : [ \"USDT\" ],\n  \"blockedBalance\" : [ \"0E-30\" ],\n  \"balance\" : [ \"10000\" ],\n  \"assetsBlockedBalance\" : [ \"0E-30\" ],\n  \"assetsBalance\" : [ \"10000\" ],\n  \"tokenBalance\" : [ \"10000\" ],\n  \"tokenBlockedBalance\" : [ \"0E-30\" ],\n  \"fbBalance\" : [ \"5000.000000000000000000000000000000\" ],\n  \"fbBlockedBalance\" : [ \"5000.000000000000000000000000000000\" ],\n  \"raiseBalance\" : [ \"0E-30\" ],\n  \"chainBalance\" : [ \"0E-30\" ],\n  \"remarks\" : [ \"NORMAL\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-10 11:17:01');
INSERT INTO `sys_oper_log` VALUES (74, 'OTC认证', 2, 'com.ruoyi.qlcoin.controller.OTCMemberController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/OTCMember/edit', '171.106.203.194', 'XX XX', '{\n  \"id\" : [ \"3b8f80497d7b71938770b1bf00b64a58\" ],\n  \"phone\" : [ \"17377384639\" ],\n  \"uname\" : [ \"李影涛\" ],\n  \"cardNo\" : [ \"231181198512311819\" ],\n  \"wechatName\" : [ \"\" ],\n  \"aliayName\" : [ \"\" ],\n  \"cardState\" : [ \"WAIT\" ],\n  \"storeName\" : [ \"\" ],\n  \"bankUserName\" : [ \"\" ],\n  \"bankName\" : [ \"\" ],\n  \"bankAddress\" : [ \"\" ],\n  \"bankCard\" : [ \"\" ],\n  \"storeState\" : [ \"NO\" ],\n  \"areaCode\" : [ \"86\" ],\n  \"userStatus\" : [ \"NORMAL\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-10 11:35:44');
INSERT INTO `sys_oper_log` VALUES (75, 'OTC认证', 2, 'com.ruoyi.qlcoin.controller.OTCMemberController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/OTCMember/edit', '171.106.203.194', 'XX XX', '{\n  \"id\" : [ \"3b8f80497d7b71938770b1bf00b64a58\" ],\n  \"phone\" : [ \"17377384639\" ],\n  \"uname\" : [ \"李影涛\" ],\n  \"cardNo\" : [ \"231181198512311819\" ],\n  \"wechatName\" : [ \"\" ],\n  \"aliayName\" : [ \"\" ],\n  \"cardState\" : [ \"WAIT\" ],\n  \"storeName\" : [ \"\" ],\n  \"bankUserName\" : [ \"\" ],\n  \"bankName\" : [ \"\" ],\n  \"bankAddress\" : [ \"\" ],\n  \"bankCard\" : [ \"\" ],\n  \"storeState\" : [ \"NO\" ],\n  \"areaCode\" : [ \"86\" ],\n  \"userStatus\" : [ \"NORMAL\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-10 11:35:48');
INSERT INTO `sys_oper_log` VALUES (76, 'OTC认证', 2, 'com.ruoyi.qlcoin.controller.OTCMemberController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/OTCMember/edit', '171.106.203.194', 'XX XX', '{\n  \"id\" : [ \"3b8f80497d7b71938770b1bf00b64a58\" ],\n  \"phone\" : [ \"17377384639\" ],\n  \"uname\" : [ \"李影涛\" ],\n  \"cardNo\" : [ \"231181198512311819\" ],\n  \"wechatName\" : [ \"\" ],\n  \"aliayName\" : [ \"\" ],\n  \"cardState\" : [ \"WAIT\" ],\n  \"storeName\" : [ \"\" ],\n  \"bankUserName\" : [ \"\" ],\n  \"bankName\" : [ \"\" ],\n  \"bankAddress\" : [ \"\" ],\n  \"bankCard\" : [ \"\" ],\n  \"storeState\" : [ \"NO\" ],\n  \"areaCode\" : [ \"86\" ],\n  \"userStatus\" : [ \"NORMAL\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-10 11:36:07');
INSERT INTO `sys_oper_log` VALUES (77, 'OTC认证', 2, 'com.ruoyi.qlcoin.controller.OTCMemberController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/OTCMember/edit', '171.106.203.194', 'XX XX', '{\n  \"id\" : [ \"3b8f80497d7b71938770b1bf00b64a58\" ],\n  \"phone\" : [ \"17377384639\" ],\n  \"uname\" : [ \"李影涛\" ],\n  \"cardNo\" : [ \"231181198512311819\" ],\n  \"wechatName\" : [ \"\" ],\n  \"aliayName\" : [ \"\" ],\n  \"cardState\" : [ \"WAIT\" ],\n  \"storeName\" : [ \"\" ],\n  \"bankUserName\" : [ \"\" ],\n  \"bankName\" : [ \"\" ],\n  \"bankAddress\" : [ \"\" ],\n  \"bankCard\" : [ \"\" ],\n  \"storeState\" : [ \"NO\" ],\n  \"areaCode\" : [ \"86\" ],\n  \"userStatus\" : [ \"NORMAL\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-10 11:36:48');
INSERT INTO `sys_oper_log` VALUES (78, 'OTC认证', 2, 'com.ruoyi.qlcoin.controller.OTCMemberController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/OTCMember/edit', '154.218.6.57', 'XX XX', '{\n  \"id\" : [ \"3b8f80497d7b71938770b1bf00b64a58\" ],\n  \"phone\" : [ \"17377384639\" ],\n  \"uname\" : [ \"李影涛\" ],\n  \"cardNo\" : [ \"231181198512311819\" ],\n  \"wechatName\" : [ \"\" ],\n  \"aliayName\" : [ \"\" ],\n  \"cardState\" : [ \"PASS\" ],\n  \"storeName\" : [ \"\" ],\n  \"bankUserName\" : [ \"\" ],\n  \"bankName\" : [ \"\" ],\n  \"bankAddress\" : [ \"\" ],\n  \"bankCard\" : [ \"\" ],\n  \"storeState\" : [ \"PASS\" ],\n  \"areaCode\" : [ \"86\" ],\n  \"userStatus\" : [ \"NORMAL\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-10 11:37:50');
INSERT INTO `sys_oper_log` VALUES (79, '用户余额', 2, 'com.ruoyi.qlcoin.controller.TBalanceController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TBalance/edit', '111.21.63.98', 'XX XX', '{\n  \"id\" : [ \"2fc3a1b9ced779ffecb0b7123b36af5b\" ],\n  \"userId\" : [ \"448509298ab541aefd3b7628803174a1\" ],\n  \"currency\" : [ \"BTC\" ],\n  \"blockedBalance\" : [ \"0E-30\" ],\n  \"balance\" : [ \"999\" ],\n  \"assetsBlockedBalance\" : [ \"0E-30\" ],\n  \"assetsBalance\" : [ \"0E-30\" ],\n  \"tokenBalance\" : [ \"0E-30\" ],\n  \"tokenBlockedBalance\" : [ \"0E-30\" ],\n  \"fbBalance\" : [ \"0E-30\" ],\n  \"fbBlockedBalance\" : [ \"0E-30\" ],\n  \"raiseBalance\" : [ \"0E-30\" ],\n  \"chainBalance\" : [ \"0E-30\" ],\n  \"remarks\" : [ \"NORMAL\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-10 14:47:57');
INSERT INTO `sys_oper_log` VALUES (80, 'OTC认证', 2, 'com.ruoyi.qlcoin.controller.OTCMemberController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/OTCMember/edit', '1.86.244.34', 'XX XX', '{\n  \"id\" : [ \"448509298ab541aefd3b7628803174a1\" ],\n  \"phone\" : [ \"15509106509\" ],\n  \"uname\" : [ \"王天煜\" ],\n  \"cardNo\" : [ \"610402199605087516\" ],\n  \"wechatName\" : [ \"\" ],\n  \"aliayName\" : [ \"15509106509\" ],\n  \"cardState\" : [ \"PASS\" ],\n  \"storeName\" : [ \"\" ],\n  \"bankUserName\" : [ \"\" ],\n  \"bankName\" : [ \"\" ],\n  \"bankAddress\" : [ \"\" ],\n  \"bankCard\" : [ \"\" ],\n  \"storeState\" : [ \"PASS\" ],\n  \"areaCode\" : [ \"86\" ],\n  \"userStatus\" : [ \"MAIN_WITHDRAW_ACCOUNT\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-12 21:21:53');
INSERT INTO `sys_oper_log` VALUES (81, 'OTC认证', 2, 'com.ruoyi.qlcoin.controller.OTCMemberController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/OTCMember/edit', '1.86.244.34', 'XX XX', '{\n  \"id\" : [ \"3b8f80497d7b71938770b1bf00b64a58\" ],\n  \"phone\" : [ \"17377384639\" ],\n  \"uname\" : [ \"李影涛\" ],\n  \"cardNo\" : [ \"231181198512311819\" ],\n  \"wechatName\" : [ \"\" ],\n  \"aliayName\" : [ \"\" ],\n  \"cardState\" : [ \"PASS\" ],\n  \"storeName\" : [ \"\" ],\n  \"bankUserName\" : [ \"\" ],\n  \"bankName\" : [ \"\" ],\n  \"bankAddress\" : [ \"\" ],\n  \"bankCard\" : [ \"\" ],\n  \"storeState\" : [ \"PASS\" ],\n  \"areaCode\" : [ \"86\" ],\n  \"userStatus\" : [ \"MAIN_DEPOSIT_ACCOUNT\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-12 21:21:59');
INSERT INTO `sys_oper_log` VALUES (82, '合约地址', 3, 'com.ruoyi.qlcoin.controller.TCoinTokenController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TCoinToken/remove', '1.86.244.34', 'XX XX', '{\n  \"ids\" : [ \"060d5997b5a5ae25d4a7c13822c63372\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-12 21:23:18');
INSERT INTO `sys_oper_log` VALUES (83, '合约地址', 3, 'com.ruoyi.qlcoin.controller.TCoinTokenController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TCoinToken/remove', '1.86.244.34', 'XX XX', '{\n  \"ids\" : [ \"060d5997b5a5ae25d4a7c13822c63373\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-12 21:23:19');
INSERT INTO `sys_oper_log` VALUES (84, '币币-公告', 3, 'com.ruoyi.qlcoin.controller.TNoticeController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/NEWS/remove', '1.86.244.34', 'XX XX', '{\n  \"ids\" : [ \"8ab03bd5dae64d3697ab94b169983d94\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-12 21:23:53');
INSERT INTO `sys_oper_log` VALUES (85, '币币-公告', 3, 'com.ruoyi.qlcoin.controller.TNoticeController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/NEWS/remove', '1.86.244.34', 'XX XX', '{\n  \"ids\" : [ \"53afc60d62254aeda851d0b287bfd37a\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-12 21:23:57');
INSERT INTO `sys_oper_log` VALUES (86, '币币-公告', 3, 'com.ruoyi.qlcoin.controller.TNoticeController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/NEWS/remove', '1.86.244.34', 'XX XX', '{\n  \"ids\" : [ \"3b93d97faa02496a8a86d7beacdec7ce\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-12 21:24:01');
INSERT INTO `sys_oper_log` VALUES (87, '币币-公告', 3, 'com.ruoyi.qlcoin.controller.TNoticeController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/NEWS/remove', '1.86.244.34', 'XX XX', '{\n  \"ids\" : [ \"651fb0b433414447a4779e8fea36e578\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-12 21:24:04');
INSERT INTO `sys_oper_log` VALUES (88, '币币-公告', 3, 'com.ruoyi.qlcoin.controller.TNoticeController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/NEWS/remove', '1.86.244.34', 'XX XX', '{\n  \"ids\" : [ \"0716faef15c348c392403b9064da3a4f\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-12 21:24:12');
INSERT INTO `sys_oper_log` VALUES (89, 'asda', 3, 'com.ruoyi.qlcoin.controller.TRobotConfigController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/remove', '1.86.244.34', 'XX XX', '{\n  \"ids\" : [ \"8e981272b7bd47d2994a23981147d12b\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-12 21:24:57');
INSERT INTO `sys_oper_log` VALUES (90, 'asda', 3, 'com.ruoyi.qlcoin.controller.TRobotConfigController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/remove', '1.86.244.34', 'XX XX', '{\n  \"ids\" : [ \"0b5a2cacd3f74eb1a0c3e947acc381d2\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-12 21:24:57');
INSERT INTO `sys_oper_log` VALUES (91, 'asda', 3, 'com.ruoyi.qlcoin.controller.TRobotConfigController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/remove', '1.86.244.34', 'XX XX', '{\n  \"ids\" : [ \"96c600738b354f508efc7117bdeed109\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-12 21:24:58');
INSERT INTO `sys_oper_log` VALUES (92, 'asda', 3, 'com.ruoyi.qlcoin.controller.TRobotConfigController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/remove', '1.86.244.34', 'XX XX', '{\n  \"ids\" : [ \"1\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-12 21:25:08');
INSERT INTO `sys_oper_log` VALUES (93, 'asda', 3, 'com.ruoyi.qlcoin.controller.TRobotConfigController.remove()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/remove', '1.86.244.34', 'XX XX', '{\n  \"ids\" : [ \"f7f380af13324dfdb76d96a27ce84be7\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-09-12 21:25:28');
INSERT INTO `sys_oper_log` VALUES (94, '合约配置', 2, 'com.ruoyi.qlcoin.controller.TContractMulController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TContractMul/edit', '127.0.0.1', '内网IP', '{\r\n  \"id\" : [ \"2a225871779c14aa878b22e34ba12270\" ],\r\n  \"pairsName\" : [ \"LTC/USDT\" ],\r\n  \"contractMul\" : [ \"1000.20000000\" ],\r\n  \"forcePrice\" : [ \"\" ],\r\n  \"makerFee\" : [ \"0.00050000\" ],\r\n  \"takerFee\" : [ \"0.00050000\" ],\r\n  \"forceFee\" : [ \"0.00050000\" ],\r\n  \"tradeFee\" : [ \"\" ],\r\n  \"withdrawFee\" : [ \"2.00000000\" ],\r\n  \"ensure\" : [ \"0.0050\" ]\r\n}', 'null', 1, '\r\n### Error updating database.  Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Out of range value for column \'contract_mul\' at row 1\r\n### The error may involve com.ruoyi.qlcoin.mapper.TContractMulMapper.updateTContractMul-Inline\r\n### The error occurred while setting parameters\r\n### SQL: update t_contract_mul          SET pairs_name = ?,             contract_mul = ?,                          maker_fee = ?,             taker_fee = ?,             force_fee = ?,                          withdraw_fee = ?,             ensure = ?,                          update_time = ?          where id = ?\r\n### Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Out of range value for column \'contract_mul\' at row 1\n; Data truncation: Out of range value for column \'contract_mul\' at row 1; nested exception is com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Out of range value for column \'contract_mul\' at row 1', '2020-12-15 20:08:58');
INSERT INTO `sys_oper_log` VALUES (95, '合约配置', 2, 'com.ruoyi.qlcoin.controller.TContractMulController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TContractMul/edit', '127.0.0.1', '内网IP', '{\r\n  \"id\" : [ \"2a225871779c14aa878b22e34ba12270\" ],\r\n  \"pairsName\" : [ \"LTC/USDT\" ],\r\n  \"contractMul\" : [ \"1000.20000000\" ],\r\n  \"forcePrice\" : [ \"\" ],\r\n  \"makerFee\" : [ \"0.00050000\" ],\r\n  \"takerFee\" : [ \"0.00050000\" ],\r\n  \"forceFee\" : [ \"0.00050000\" ],\r\n  \"tradeFee\" : [ \"\" ],\r\n  \"withdrawFee\" : [ \"2.00000000\" ],\r\n  \"ensure\" : [ \"0.0050\" ]\r\n}', 'null', 1, '\r\n### Error updating database.  Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Out of range value for column \'contract_mul\' at row 1\r\n### The error may involve com.ruoyi.qlcoin.mapper.TContractMulMapper.updateTContractMul-Inline\r\n### The error occurred while setting parameters\r\n### SQL: update t_contract_mul          SET pairs_name = ?,             contract_mul = ?,                          maker_fee = ?,             taker_fee = ?,             force_fee = ?,                          withdraw_fee = ?,             ensure = ?,                          update_time = ?          where id = ?\r\n### Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Out of range value for column \'contract_mul\' at row 1\n; Data truncation: Out of range value for column \'contract_mul\' at row 1; nested exception is com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Out of range value for column \'contract_mul\' at row 1', '2020-12-15 20:11:19');
INSERT INTO `sys_oper_log` VALUES (96, '冲入手续费', 2, 'com.ruoyi.qlcoin.controller.TBalanceController.gasFee()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TBalance/gasfee', '127.0.0.1', '内网IP', '{\r\n  \"id\" : [ \"29b3c356904cc6a32e6620e0a582e444\" ]\r\n}', '{\r\n  \"msg\" : null,\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-12-17 13:16:31');
INSERT INTO `sys_oper_log` VALUES (97, '用户余额', 2, 'com.ruoyi.qlcoin.controller.TBalanceController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TBalance/edit', '127.0.0.1', '内网IP', '{\r\n  \"id\" : [ \"8dfab2f156b767d8bf4b2e6ddfea53c1\" ],\r\n  \"userId\" : [ \"91947f2536fa87358f38768bd92f7329\" ],\r\n  \"currency\" : [ \"USDT\" ],\r\n  \"blockedBalance\" : [ \"0E-30\" ],\r\n  \"balance\" : [ \"10000\" ],\r\n  \"assetsBlockedBalance\" : [ \"0E-30\" ],\r\n  \"assetsBalance\" : [ \"0E-30\" ],\r\n  \"tokenBalance\" : [ \"0E-30\" ],\r\n  \"tokenBlockedBalance\" : [ \"0E-30\" ],\r\n  \"fbBalance\" : [ \"0E-30\" ],\r\n  \"fbBlockedBalance\" : [ \"0E-30\" ],\r\n  \"raiseBalance\" : [ \"0E-30\" ],\r\n  \"chainBalance\" : [ \"0E-30\" ],\r\n  \"remarks\" : [ \"NORMAL\" ]\r\n}', '{\r\n  \"msg\" : \"操作成功\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-12-17 13:16:47');
INSERT INTO `sys_oper_log` VALUES (98, '用户余额', 2, 'com.ruoyi.qlcoin.controller.TBalanceController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TBalance/edit', '127.0.0.1', '内网IP', '{\r\n  \"id\" : [ \"9e6e3c6aedfb8cedaab70555b701686d\" ],\r\n  \"userId\" : [ \"91947f2536fa87358f38768bd92f7329\" ],\r\n  \"currency\" : [ \"ETC\" ],\r\n  \"blockedBalance\" : [ \"0E-30\" ],\r\n  \"balance\" : [ \"10000\" ],\r\n  \"assetsBlockedBalance\" : [ \"0E-30\" ],\r\n  \"assetsBalance\" : [ \"0E-30\" ],\r\n  \"tokenBalance\" : [ \"0E-30\" ],\r\n  \"tokenBlockedBalance\" : [ \"0E-30\" ],\r\n  \"fbBalance\" : [ \"0E-30\" ],\r\n  \"fbBlockedBalance\" : [ \"0E-30\" ],\r\n  \"raiseBalance\" : [ \"0E-30\" ],\r\n  \"chainBalance\" : [ \"0E-30\" ],\r\n  \"remarks\" : [ \"NORMAL\" ]\r\n}', '{\r\n  \"msg\" : \"操作成功\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-12-17 13:22:31');
INSERT INTO `sys_oper_log` VALUES (99, 'sysconfig', 2, 'com.ruoyi.qlcoin.controller.TSysConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/sysconfig/edit', '127.0.0.1', '内网IP', '{\r\n  \"id\" : [ \"3fb354e42ba1422bafa813fd1a2faa4a\" ],\r\n  \"paramName\" : [ \"三方钱包API\" ],\r\n  \"paramKey\" : [ \"ETH_URL\" ],\r\n  \"paramValue\" : [ \"https://sapi.ztpay.org/api/v2\" ],\r\n  \"commit\" : [ \"三方钱包API\" ]\r\n}', '{\r\n  \"msg\" : \"操作成功\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-12-18 13:28:07');
INSERT INTO `sys_oper_log` VALUES (100, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '111.22.182.237', 'XX XX', '{\n  \"id\" : [ \"4\" ],\n  \"pairName\" : [ \"ETH/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"\" ],\n  \"willTime\" : [ \"2020-10-24 00:00:00\" ],\n  \"bindUser\" : [ \"\" ],\n  \"startNum\" : [ \"0.03000000\" ],\n  \"endNum\" : [ \"3.00000000\" ],\n  \"mstartNum\" : [ \"0.03000000\" ],\n  \"mendNum\" : [ \"3.00000000\" ],\n  \"startPrice\" : [ \"0.10010000\" ],\n  \"endPrice\" : [ \"0.30000000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-12-19 14:27:38');
INSERT INTO `sys_oper_log` VALUES (101, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '111.22.182.237', 'XX XX', '{\n  \"id\" : [ \"469b03e402a14c42a8dcced5e8d28d54\" ],\n  \"pairName\" : [ \"EOS/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"0E-8\" ],\n  \"willTime\" : [ \"2020-10-24 11:25:00\" ],\n  \"bindUser\" : [ \"\" ],\n  \"startNum\" : [ \"10.00000000\" ],\n  \"endNum\" : [ \"10.00000000\" ],\n  \"mstartNum\" : [ \"10.00000000\" ],\n  \"mendNum\" : [ \"10.00000000\" ],\n  \"startPrice\" : [ \"0.00100000\" ],\n  \"endPrice\" : [ \"0.00300000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-12-19 14:28:15');
INSERT INTO `sys_oper_log` VALUES (102, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '111.22.182.237', 'XX XX', '{\n  \"id\" : [ \"be6bf9aaf54641b5aff8701db88931f1\" ],\n  \"pairName\" : [ \"ETC/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"0E-8\" ],\n  \"willTime\" : [ \"2020-10-20 15:20:00\" ],\n  \"bindUser\" : [ \"\" ],\n  \"startNum\" : [ \"10.00000000\" ],\n  \"endNum\" : [ \"10.00000000\" ],\n  \"mstartNum\" : [ \"10.00000000\" ],\n  \"mendNum\" : [ \"10.00000000\" ],\n  \"startPrice\" : [ \"0.00100000\" ],\n  \"endPrice\" : [ \"0.00300000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-12-19 14:28:52');
INSERT INTO `sys_oper_log` VALUES (103, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '111.22.182.237', 'XX XX', '{\n  \"id\" : [ \"a6d605c3c10e49e9bb520e48a54a3345\" ],\n  \"pairName\" : [ \"XRP/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"\" ],\n  \"willTime\" : [ \"2020-10-24 15:30:00\" ],\n  \"bindUser\" : [ \"\" ],\n  \"startNum\" : [ \"10.00000000\" ],\n  \"endNum\" : [ \"10.00000000\" ],\n  \"mstartNum\" : [ \"10.00000000\" ],\n  \"mendNum\" : [ \"10.00000000\" ],\n  \"startPrice\" : [ \"0.00010000\" ],\n  \"endPrice\" : [ \"0.00020000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-12-19 14:29:43');
INSERT INTO `sys_oper_log` VALUES (104, 'asda', 2, 'com.ruoyi.qlcoin.controller.TRobotConfigController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TRobotConfig/edit', '111.22.182.237', 'XX XX', '{\n  \"id\" : [ \"eda75ff8fc0e40998a768d019bd70ea3\" ],\n  \"pairName\" : [ \"LTC/USDT\" ],\n  \"openKine\" : [ \"OPEN\" ],\n  \"isOpen\" : [ \"OPEN\" ],\n  \"openTape\" : [ \"OPEN\" ],\n  \"willPrice\" : [ \"\" ],\n  \"willTime\" : [ \"2020-12-17 10:50:00\" ],\n  \"bindUser\" : [ \"\" ],\n  \"startNum\" : [ \"5.00000000\" ],\n  \"endNum\" : [ \"10.00000000\" ],\n  \"mstartNum\" : [ \"5.00000000\" ],\n  \"mendNum\" : [ \"10.00000000\" ],\n  \"startPrice\" : [ \"0.00100000\" ],\n  \"endPrice\" : [ \"0.02000000\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-12-19 14:30:07');
INSERT INTO `sys_oper_log` VALUES (105, 'OTC认证', 2, 'com.ruoyi.qlcoin.controller.OTCMemberController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/OTCMember/edit', '111.22.182.237', 'XX XX', '{\n  \"id\" : [ \"fc6e5e3ae9e8c0f05efd85501edcb7ce\" ],\n  \"phone\" : [ \"\" ],\n  \"uname\" : [ \"\" ],\n  \"cardNo\" : [ \"\" ],\n  \"wechatName\" : [ \"\" ],\n  \"aliayName\" : [ \"\" ],\n  \"cardState\" : [ \"NO\" ],\n  \"storeName\" : [ \"\" ],\n  \"bankUserName\" : [ \"\" ],\n  \"bankName\" : [ \"\" ],\n  \"bankAddress\" : [ \"\" ],\n  \"bankCard\" : [ \"\" ],\n  \"storeState\" : [ \"NO\" ],\n  \"areaCode\" : [ \"\" ],\n  \"userStatus\" : [ \"NORMAL\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-12-19 14:49:48');
INSERT INTO `sys_oper_log` VALUES (106, '冲入手续费', 2, 'com.ruoyi.qlcoin.controller.TBalanceController.gasFee()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TBalance/gasfee', '111.22.182.237', 'XX XX', '{\n  \"id\" : [ \"fbfcf813002699f90c70fd1e2e9bad2e\" ]\n}', '{\n  \"msg\" : \"nested exception is org.apache.ibatis.exceptions.TooManyResultsException: Expected one result (or null) to be returned by selectOne(), but found: 4\",\n  \"code\" : 0\n}', 0, NULL, '2020-12-19 14:53:47');
INSERT INTO `sys_oper_log` VALUES (107, '用户余额', 2, 'com.ruoyi.qlcoin.controller.TBalanceController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TBalance/edit', '111.22.182.237', 'XX XX', '{\n  \"id\" : [ \"b7735a658cc39c008789751b54e937cf\" ],\n  \"userId\" : [ \"fc6e5e3ae9e8c0f05efd85501edcb7ce\" ],\n  \"currency\" : [ \"LTC\" ],\n  \"blockedBalance\" : [ \"0E-30\" ],\n  \"balance\" : [ \"10000\" ],\n  \"assetsBlockedBalance\" : [ \"0E-30\" ],\n  \"assetsBalance\" : [ \"0E-30\" ],\n  \"tokenBalance\" : [ \"0E-30\" ],\n  \"tokenBlockedBalance\" : [ \"0E-30\" ],\n  \"fbBalance\" : [ \"0E-30\" ],\n  \"fbBlockedBalance\" : [ \"0E-30\" ],\n  \"raiseBalance\" : [ \"0E-30\" ],\n  \"chainBalance\" : [ \"0E-30\" ],\n  \"remarks\" : [ \"NORMAL\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-12-19 14:55:12');
INSERT INTO `sys_oper_log` VALUES (108, '交易对管理', 2, 'com.ruoyi.qlcoin.controller.TPairsController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TPairs/edit', '183.228.48.73', 'XX XX', '{\n  \"id\" : [ \"b173b6f0f350067300e147a70ca7c034\" ],\n  \"pairsName\" : [ \"BTC/USDT\" ],\n  \"mainCur\" : [ \"USDT\" ],\n  \"tokenCur\" : [ \"BTC\" ],\n  \"state\" : [ \"NORMAL\" ],\n  \"type\" : [ \"MAIN_COIN\" ],\n  \"sort\" : [ \"12\" ],\n  \"openPrice\" : [ \"\" ],\n  \"price\" : [ \"22933.58269924\" ],\n  \"isTop\" : [ \"1\" ],\n  \"tradeMax\" : [ \"1000000\" ],\n  \"tradeMin\" : [ \"1\" ],\n  \"tradeType\" : [ \"SPOT\", \"CONTRACT\" ],\n  \"isdw\" : [ \"1\" ],\n  \"tradeRate\" : [ \"0.003\" ],\n  \"mainFrom\" : [ \"BTC\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-12-19 15:09:23');
INSERT INTO `sys_oper_log` VALUES (109, '用户余额', 2, 'com.ruoyi.qlcoin.controller.TBalanceController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TBalance/edit', '111.22.182.237', 'XX XX', '{\n  \"id\" : [ \"96addf01ec439b2b20bce904ed35d060\" ],\n  \"userId\" : [ \"fc6e5e3ae9e8c0f05efd85501edcb7ce\" ],\n  \"currency\" : [ \"USDT\" ],\n  \"blockedBalance\" : [ \"0E-30\" ],\n  \"balance\" : [ \"10000\" ],\n  \"assetsBlockedBalance\" : [ \"0E-30\" ],\n  \"assetsBalance\" : [ \"0E-30\" ],\n  \"tokenBalance\" : [ \"0E-30\" ],\n  \"tokenBlockedBalance\" : [ \"0E-30\" ],\n  \"fbBalance\" : [ \"0E-30\" ],\n  \"fbBlockedBalance\" : [ \"0E-30\" ],\n  \"raiseBalance\" : [ \"0E-30\" ],\n  \"chainBalance\" : [ \"0E-30\" ],\n  \"remarks\" : [ \"NORMAL\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-12-19 16:15:54');
INSERT INTO `sys_oper_log` VALUES (110, 'OTC认证', 2, 'com.ruoyi.qlcoin.controller.OTCMemberController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/OTCMember/edit', '111.22.182.237', 'XX XX', '{\n  \"id\" : [ \"fc6e5e3ae9e8c0f05efd85501edcb7ce\" ],\n  \"phone\" : [ \"\" ],\n  \"uname\" : [ \"\" ],\n  \"cardNo\" : [ \"\" ],\n  \"wechatName\" : [ \"\" ],\n  \"aliayName\" : [ \"\" ],\n  \"cardState\" : [ \"PASS\" ],\n  \"storeName\" : [ \"\" ],\n  \"bankUserName\" : [ \"\" ],\n  \"bankName\" : [ \"\" ],\n  \"bankAddress\" : [ \"\" ],\n  \"bankCard\" : [ \"\" ],\n  \"storeState\" : [ \"PASS\" ],\n  \"areaCode\" : [ \"\" ],\n  \"userStatus\" : [ \"NORMAL\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-12-19 16:17:33');
INSERT INTO `sys_oper_log` VALUES (111, '用户余额', 2, 'com.ruoyi.qlcoin.controller.TBalanceController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TBalance/edit', '111.22.182.237', 'XX XX', '{\n  \"id\" : [ \"eb0d5e4fb7d2ee3991fa9b9d2d1768b4\" ],\n  \"userId\" : [ \"fc6e5e3ae9e8c0f05efd85501edcb7ce\" ],\n  \"currency\" : [ \"EOS\" ],\n  \"blockedBalance\" : [ \"0E-30\" ],\n  \"balance\" : [ \"10000\" ],\n  \"assetsBlockedBalance\" : [ \"0E-30\" ],\n  \"assetsBalance\" : [ \"0E-30\" ],\n  \"tokenBalance\" : [ \"0E-30\" ],\n  \"tokenBlockedBalance\" : [ \"0E-30\" ],\n  \"fbBalance\" : [ \"0E-30\" ],\n  \"fbBlockedBalance\" : [ \"0E-30\" ],\n  \"raiseBalance\" : [ \"0E-30\" ],\n  \"chainBalance\" : [ \"0E-30\" ],\n  \"remarks\" : [ \"NORMAL\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-12-19 16:21:50');
INSERT INTO `sys_oper_log` VALUES (112, '用户余额', 2, 'com.ruoyi.qlcoin.controller.TBalanceController.editSave()', 'POST', 1, 'admin', '研发部门', '/qlcoin/TBalance/edit', '111.22.182.237', 'XX XX', '{\n  \"id\" : [ \"96addf01ec439b2b20bce904ed35d060\" ],\n  \"userId\" : [ \"fc6e5e3ae9e8c0f05efd85501edcb7ce\" ],\n  \"currency\" : [ \"USDT\" ],\n  \"blockedBalance\" : [ \"100.000000000000000000000000000000\" ],\n  \"balance\" : [ \"9900.000000000000000000000000000000\" ],\n  \"assetsBlockedBalance\" : [ \"0E-30\" ],\n  \"assetsBalance\" : [ \"0E-30\" ],\n  \"tokenBalance\" : [ \"10000\" ],\n  \"tokenBlockedBalance\" : [ \"0E-30\" ],\n  \"fbBalance\" : [ \"0E-30\" ],\n  \"fbBlockedBalance\" : [ \"0E-30\" ],\n  \"raiseBalance\" : [ \"0E-30\" ],\n  \"chainBalance\" : [ \"0E-30\" ],\n  \"remarks\" : [ \"NORMAL\" ]\n}', '{\n  \"msg\" : \"操作成功\",\n  \"code\" : 0\n}', 0, NULL, '2020-12-19 17:18:32');

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `post_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int(4) NOT NULL COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '岗位信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 'ceo', '董事长', 1, '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_post` VALUES (2, 'se', '项目经理', 2, '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_post` VALUES (3, 'hr', '人力资源', 3, '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
INSERT INTO `sys_post` VALUES (4, 'user', '普通员工', 4, '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(4) NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '管理员', 'admin', 1, '1', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '管理员');
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '2', '0', '2', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '普通角色');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色和部门关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES (2, 100);
INSERT INTO `sys_role_dept` VALUES (2, 101);
INSERT INTO `sys_role_dept` VALUES (2, 105);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (2, 1);
INSERT INTO `sys_role_menu` VALUES (2, 2);
INSERT INTO `sys_role_menu` VALUES (2, 3);
INSERT INTO `sys_role_menu` VALUES (2, 100);
INSERT INTO `sys_role_menu` VALUES (2, 101);
INSERT INTO `sys_role_menu` VALUES (2, 102);
INSERT INTO `sys_role_menu` VALUES (2, 103);
INSERT INTO `sys_role_menu` VALUES (2, 104);
INSERT INTO `sys_role_menu` VALUES (2, 105);
INSERT INTO `sys_role_menu` VALUES (2, 106);
INSERT INTO `sys_role_menu` VALUES (2, 107);
INSERT INTO `sys_role_menu` VALUES (2, 108);
INSERT INTO `sys_role_menu` VALUES (2, 109);
INSERT INTO `sys_role_menu` VALUES (2, 110);
INSERT INTO `sys_role_menu` VALUES (2, 111);
INSERT INTO `sys_role_menu` VALUES (2, 112);
INSERT INTO `sys_role_menu` VALUES (2, 113);
INSERT INTO `sys_role_menu` VALUES (2, 114);
INSERT INTO `sys_role_menu` VALUES (2, 115);
INSERT INTO `sys_role_menu` VALUES (2, 500);
INSERT INTO `sys_role_menu` VALUES (2, 501);
INSERT INTO `sys_role_menu` VALUES (2, 1000);
INSERT INTO `sys_role_menu` VALUES (2, 1001);
INSERT INTO `sys_role_menu` VALUES (2, 1002);
INSERT INTO `sys_role_menu` VALUES (2, 1003);
INSERT INTO `sys_role_menu` VALUES (2, 1004);
INSERT INTO `sys_role_menu` VALUES (2, 1005);
INSERT INTO `sys_role_menu` VALUES (2, 1006);
INSERT INTO `sys_role_menu` VALUES (2, 1007);
INSERT INTO `sys_role_menu` VALUES (2, 1008);
INSERT INTO `sys_role_menu` VALUES (2, 1009);
INSERT INTO `sys_role_menu` VALUES (2, 1010);
INSERT INTO `sys_role_menu` VALUES (2, 1011);
INSERT INTO `sys_role_menu` VALUES (2, 1012);
INSERT INTO `sys_role_menu` VALUES (2, 1013);
INSERT INTO `sys_role_menu` VALUES (2, 1014);
INSERT INTO `sys_role_menu` VALUES (2, 1015);
INSERT INTO `sys_role_menu` VALUES (2, 1016);
INSERT INTO `sys_role_menu` VALUES (2, 1017);
INSERT INTO `sys_role_menu` VALUES (2, 1018);
INSERT INTO `sys_role_menu` VALUES (2, 1019);
INSERT INTO `sys_role_menu` VALUES (2, 1020);
INSERT INTO `sys_role_menu` VALUES (2, 1021);
INSERT INTO `sys_role_menu` VALUES (2, 1022);
INSERT INTO `sys_role_menu` VALUES (2, 1023);
INSERT INTO `sys_role_menu` VALUES (2, 1024);
INSERT INTO `sys_role_menu` VALUES (2, 1025);
INSERT INTO `sys_role_menu` VALUES (2, 1026);
INSERT INTO `sys_role_menu` VALUES (2, 1027);
INSERT INTO `sys_role_menu` VALUES (2, 1028);
INSERT INTO `sys_role_menu` VALUES (2, 1029);
INSERT INTO `sys_role_menu` VALUES (2, 1030);
INSERT INTO `sys_role_menu` VALUES (2, 1031);
INSERT INTO `sys_role_menu` VALUES (2, 1032);
INSERT INTO `sys_role_menu` VALUES (2, 1033);
INSERT INTO `sys_role_menu` VALUES (2, 1034);
INSERT INTO `sys_role_menu` VALUES (2, 1035);
INSERT INTO `sys_role_menu` VALUES (2, 1036);
INSERT INTO `sys_role_menu` VALUES (2, 1037);
INSERT INTO `sys_role_menu` VALUES (2, 1038);
INSERT INTO `sys_role_menu` VALUES (2, 1039);
INSERT INTO `sys_role_menu` VALUES (2, 1040);
INSERT INTO `sys_role_menu` VALUES (2, 1041);
INSERT INTO `sys_role_menu` VALUES (2, 1042);
INSERT INTO `sys_role_menu` VALUES (2, 1043);
INSERT INTO `sys_role_menu` VALUES (2, 1044);
INSERT INTO `sys_role_menu` VALUES (2, 1045);
INSERT INTO `sys_role_menu` VALUES (2, 1046);
INSERT INTO `sys_role_menu` VALUES (2, 1047);
INSERT INTO `sys_role_menu` VALUES (2, 1048);
INSERT INTO `sys_role_menu` VALUES (2, 1049);
INSERT INTO `sys_role_menu` VALUES (2, 1050);
INSERT INTO `sys_role_menu` VALUES (2, 1051);
INSERT INTO `sys_role_menu` VALUES (2, 1052);
INSERT INTO `sys_role_menu` VALUES (2, 1053);
INSERT INTO `sys_role_menu` VALUES (2, 1054);
INSERT INTO `sys_role_menu` VALUES (2, 1055);
INSERT INTO `sys_role_menu` VALUES (2, 1056);
INSERT INTO `sys_role_menu` VALUES (2, 1057);
INSERT INTO `sys_role_menu` VALUES (2, 1058);
INSERT INTO `sys_role_menu` VALUES (2, 1059);
INSERT INTO `sys_role_menu` VALUES (2, 1060);
INSERT INTO `sys_role_menu` VALUES (2, 1061);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门ID',
  `login_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录账号',
  `user_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户昵称',
  `user_type` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '00' COMMENT '用户类型（00系统用户 01注册用户）',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '头像路径',
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '密码',
  `salt` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '盐加密',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '最后登陆IP',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登陆时间',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 103, 'admin', 'BSEX', '00', 'ry@163.com', '15888888888', '1', '/profile/avatar/2020/04/01/11e1f28d640d8a9c4f11d6365b1c3e0c.png', 'e5bc86de8b808d069f5e39027c9b99b2', '079277', '0', '0', '111.22.182.237', '2020-12-19 21:15:46', 'admin', '2018-03-16 11:33:00', 'ry', '2020-12-19 21:15:46', '管理员');

-- ----------------------------
-- Table structure for sys_user_online
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_online`;
CREATE TABLE `sys_user_online`  (
  `sessionId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户会话id',
  `login_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '登录账号',
  `dept_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '部门名称',
  `ipaddr` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '操作系统',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '在线状态on_line在线off_line离线',
  `start_timestamp` datetime NULL DEFAULT NULL COMMENT 'session创建时间',
  `last_access_time` datetime NULL DEFAULT NULL COMMENT 'session最后访问时间',
  `expire_time` int(5) NULL DEFAULT 0 COMMENT '超时时间，单位为分钟',
  PRIMARY KEY (`sessionId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '在线用户记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_online
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户与岗位关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (1, 1);
INSERT INTO `sys_user_post` VALUES (2, 2);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户和角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);

-- ----------------------------
-- Table structure for t_balance
-- ----------------------------
DROP TABLE IF EXISTS `t_balance`;
CREATE TABLE `t_balance`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `currency` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币种',
  `blocked_balance` decimal(60, 30) UNSIGNED ZEROFILL NULL DEFAULT 000000000000000000000000000000.000000000000000000000000000000 COMMENT '冻结余额',
  `balance` decimal(60, 30) UNSIGNED ZEROFILL NULL DEFAULT 000000000000000000000000000000.000000000000000000000000000000 COMMENT '余额',
  `assets_blocked_balance` decimal(60, 30) UNSIGNED ZEROFILL NULL DEFAULT 000000000000000000000000000000.000000000000000000000000000000 COMMENT '资产冻结',
  `assets_balance` decimal(60, 30) UNSIGNED ZEROFILL NULL DEFAULT 000000000000000000000000000000.000000000000000000000000000000,
  `token_balance` decimal(60, 30) UNSIGNED ZEROFILL NULL DEFAULT 000000000000000000000000000000.000000000000000000000000000000 COMMENT '合约可用',
  `token_blocked_balance` decimal(60, 30) UNSIGNED ZEROFILL NULL DEFAULT 000000000000000000000000000000.000000000000000000000000000000 COMMENT '合约冻结',
  `fb_balance` decimal(60, 30) UNSIGNED ZEROFILL NULL DEFAULT 000000000000000000000000000000.000000000000000000000000000000,
  `fb_blocked_balance` decimal(60, 30) UNSIGNED ZEROFILL NULL DEFAULT 000000000000000000000000000000.000000000000000000000000000000,
  `raise_balance` decimal(60, 30) UNSIGNED ZEROFILL NULL DEFAULT 000000000000000000000000000000.000000000000000000000000000000 COMMENT '私募冻结',
  `chain_balance` decimal(60, 30) UNSIGNED ZEROFILL NULL DEFAULT 000000000000000000000000000000.000000000000000000000000000000 COMMENT '恋上资产',
  `remarks` varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id_currency_unique`(`user_id`, `currency`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户余额表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_balance
-- ----------------------------
INSERT INTO `t_balance` VALUES ('00e39a6f410ca7e77497ef1fc8b365df', '404208357cb9d619891c7d3b3dff1dae', 'EOS', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-08 11:24:40', '2020-09-08 11:24:40');
INSERT INTO `t_balance` VALUES ('04ef7b6fce63fd50c28edb8aca6309bd', '3b8f80497d7b71938770b1bf00b64a58', 'ETC', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-07 09:26:17', '2020-09-07 09:26:17');
INSERT INTO `t_balance` VALUES ('0a6a62611d2ac2262f078586be272975', 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'XRP', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-12-19 13:36:13', '2020-12-19 13:36:13');
INSERT INTO `t_balance` VALUES ('0e26b6b103ba4d680663518e5baaa463', '91947f2536fa87358f38768bd92f7329', 'BCH', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-12-16 14:12:58', '2020-12-16 14:12:58');
INSERT INTO `t_balance` VALUES ('0e32ff657797dac86e94a6fa3082cae1', '91947f2536fa87358f38768bd92f7329', 'BTC', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000003.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-12-16 14:12:57', '2020-12-19 12:45:54');
INSERT INTO `t_balance` VALUES ('0f6a12d495d5e8d9aa3250370bbe400a', '448509298ab541aefd3b7628803174a1', 'USDT', 000000000000000000000000001133.966408452497000000000000000000, 000000000000000000000000017765.473732759522225900000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000010000.000000000000000000000000000000, 000000000000000000000000008252.162889325010000000000000000000, 000000000000000000000000000008.543334700000000000000000000000, 000000000000000000000000005000.000000000000000000000000000000, 000000000000000000000000005000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 'NORMAL', '2020-09-08 14:32:30', '2020-09-13 10:38:02');
INSERT INTO `t_balance` VALUES ('27e6f86ee80ecf5e712e7da2b542ec2f', '404208357cb9d619891c7d3b3dff1dae', 'LTC', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-08 11:24:40', '2020-09-08 11:24:40');
INSERT INTO `t_balance` VALUES ('29b3c356904cc6a32e6620e0a582e444', '91947f2536fa87358f38768bd92f7329', 'ETH', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-12-16 14:12:58', '2020-12-16 14:12:58');
INSERT INTO `t_balance` VALUES ('2fc3a1b9ced779ffecb0b7123b36af5b', '448509298ab541aefd3b7628803174a1', 'BTC', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000997.997000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 'NORMAL', '2020-09-08 14:32:30', '2020-09-10 14:49:26');
INSERT INTO `t_balance` VALUES ('31e02ea607dd784257fb1daf7ea167da', 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'BCH', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-12-19 13:36:13', '2020-12-19 13:36:13');
INSERT INTO `t_balance` VALUES ('3304804b35b3800693574030bda5d274', '404208357cb9d619891c7d3b3dff1dae', 'ETC', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-08 11:24:40', '2020-09-08 11:24:40');
INSERT INTO `t_balance` VALUES ('332bc1a20286684d9f3f6bd2bb85486e', '3b8f80497d7b71938770b1bf00b64a58', 'LTC', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-07 09:26:17', '2020-09-07 09:26:17');
INSERT INTO `t_balance` VALUES ('34bee78842acdf060c579b4fe5713db0', '448509298ab541aefd3b7628803174a1', 'LTC', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-08 14:32:30', '2020-09-08 14:32:30');
INSERT INTO `t_balance` VALUES ('35baecc8b50d4e076e2317c4abb7890a', '448509298ab541aefd3b7628803174a1', 'EOS', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-08 14:32:30', '2020-09-08 14:32:30');
INSERT INTO `t_balance` VALUES ('36ab9b0807bcdca07664b856499d3645', '614d6a5e93f01d1e56c6e5db05d7f943', 'ETC', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-10 15:56:00', '2020-09-10 15:56:00');
INSERT INTO `t_balance` VALUES ('38151639d17bf93c8e8bed37b40dd163', 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'ETC', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-12-19 13:36:12', '2020-12-19 13:36:12');
INSERT INTO `t_balance` VALUES ('405947068b896e5ae8d2b8bdd5af194d', '91947f2536fa87358f38768bd92f7329', 'LTC', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-12-16 14:12:59', '2020-12-16 14:12:59');
INSERT INTO `t_balance` VALUES ('457f8ca1ba03a80291f3de9a57ec1c5a', '404208357cb9d619891c7d3b3dff1dae', 'USDT', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-08 11:24:40', '2020-09-08 11:24:40');
INSERT INTO `t_balance` VALUES ('58b67c1ca7cdcc31912d521223e6dedf', '3b8f80497d7b71938770b1bf00b64a58', 'XRP', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-07 09:26:17', '2020-09-07 09:26:17');
INSERT INTO `t_balance` VALUES ('5d43df0fc98015ebca64d1a28febe024', '91947f2536fa87358f38768bd92f7329', 'XRP', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-12-16 14:12:58', '2020-12-16 14:12:58');
INSERT INTO `t_balance` VALUES ('6ad6915ed49f2f2efe879995b24365bd', '614d6a5e93f01d1e56c6e5db05d7f943', 'LTC', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-10 15:56:00', '2020-09-10 15:56:00');
INSERT INTO `t_balance` VALUES ('6f1fef245b93c343550c8af929ac6c57', '3b8f80497d7b71938770b1bf00b64a58', 'BCH', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-07 09:26:17', '2020-09-07 09:26:17');
INSERT INTO `t_balance` VALUES ('74818e409d5b60fdd70197fd4668a6da', '404208357cb9d619891c7d3b3dff1dae', 'BCH', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-08 11:24:40', '2020-09-08 11:24:40');
INSERT INTO `t_balance` VALUES ('799236a7e49b3b34a57ef95c0b2090dd', '448509298ab541aefd3b7628803174a1', 'ETC', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000259.795546554830000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-08 14:32:30', '2020-09-11 13:57:53');
INSERT INTO `t_balance` VALUES ('86618d8eb536783c9ac3c736bad6d86a', '614d6a5e93f01d1e56c6e5db05d7f943', 'XRP', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-10 15:56:00', '2020-09-10 15:56:00');
INSERT INTO `t_balance` VALUES ('8a2f588efb5752a46d92649b078a8909', '3b8f80497d7b71938770b1bf00b64a58', 'BTC', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-07 09:26:17', '2020-09-07 09:26:17');
INSERT INTO `t_balance` VALUES ('8d0b5e9e124d3eb81a26c509bd732bb0', '448509298ab541aefd3b7628803174a1', 'XRP', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-08 14:32:30', '2020-09-08 14:32:30');
INSERT INTO `t_balance` VALUES ('8dfab2f156b767d8bf4b2e6ddfea53c1', '91947f2536fa87358f38768bd92f7329', 'USDT', 000000000000000000000000001078.895999949517914000000000000000, 000000000000000000000000018964.052365987967870158100000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 'NORMAL', '2020-12-16 14:12:59', '2020-12-17 14:29:39');
INSERT INTO `t_balance` VALUES ('96addf01ec439b2b20bce904ed35d060', 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'USDT', 000000000000000000000000000000.000000161550938000000000000000, 000000000000000000000000009900.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000010725.663268000000000000000000000000, 000000000000000000000000004615.798320000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 'NORMAL', '2020-12-19 13:36:15', '2020-12-20 07:25:55');
INSERT INTO `t_balance` VALUES ('9df22d173b7af8527d20ec0d34a59e68', '404208357cb9d619891c7d3b3dff1dae', 'BTC', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-08 11:24:40', '2020-09-08 11:24:40');
INSERT INTO `t_balance` VALUES ('9e6e3c6aedfb8cedaab70555b701686d', '91947f2536fa87358f38768bd92f7329', 'ETC', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000004336.109225366940000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000004000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 'NORMAL', '2020-12-16 14:12:57', '2020-12-19 17:22:29');
INSERT INTO `t_balance` VALUES ('9fc05c984323389e7644e291d369e051', '614d6a5e93f01d1e56c6e5db05d7f943', 'EOS', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-10 15:56:00', '2020-09-10 15:56:00');
INSERT INTO `t_balance` VALUES ('afd71815a34f7605068b9b40d41a3603', '614d6a5e93f01d1e56c6e5db05d7f943', 'USDT', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-10 15:56:00', '2020-09-10 15:56:00');
INSERT INTO `t_balance` VALUES ('b4ea2ea20a23e1e863312b4e4be05566', '614d6a5e93f01d1e56c6e5db05d7f943', 'BTC', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-10 15:56:00', '2020-09-10 15:56:00');
INSERT INTO `t_balance` VALUES ('b69d5f06c0091a8ffc147aa2525b3e46', '3b8f80497d7b71938770b1bf00b64a58', 'BTCV', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-07 09:26:17', '2020-09-07 09:26:17');
INSERT INTO `t_balance` VALUES ('b7735a658cc39c008789751b54e937cf', 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'LTC', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000010001.219101590300000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 'NORMAL', '2020-12-19 13:36:15', '2020-12-19 19:24:57');
INSERT INTO `t_balance` VALUES ('ba7a1cea84f36894dac8e0d071ea8ab8', '3b8f80497d7b71938770b1bf00b64a58', 'USDT', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-07 09:26:17', '2020-09-07 09:26:17');
INSERT INTO `t_balance` VALUES ('c259c14eea9dfbb8443af45a34c03556', '3b8f80497d7b71938770b1bf00b64a58', 'EOS', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-07 09:26:17', '2020-09-07 09:26:17');
INSERT INTO `t_balance` VALUES ('cb97ef5473bbf531dd5891bd7ff84849', '448509298ab541aefd3b7628803174a1', 'BCH', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-08 14:32:30', '2020-09-10 11:20:14');
INSERT INTO `t_balance` VALUES ('d95c044acd22dfe770869e5a531cd878', '614d6a5e93f01d1e56c6e5db05d7f943', 'ETH', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-10 15:56:00', '2020-09-10 15:56:00');
INSERT INTO `t_balance` VALUES ('da287c2e5f843c4540690af312ccb91d', 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'BTC', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-12-19 13:36:12', '2020-12-19 13:36:12');
INSERT INTO `t_balance` VALUES ('e910da0c2b0a1edef97ab155b224393a', '91947f2536fa87358f38768bd92f7329', 'EOS', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-12-16 14:12:58', '2020-12-16 14:12:58');
INSERT INTO `t_balance` VALUES ('eb0d5e4fb7d2ee3991fa9b9d2d1768b4', 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'EOS', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000010000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 'NORMAL', '2020-12-19 13:36:14', '2020-12-19 16:21:50');
INSERT INTO `t_balance` VALUES ('ebf7a9dabd742412858a86ad6de727fc', '404208357cb9d619891c7d3b3dff1dae', 'XRP', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-08 11:24:40', '2020-09-08 11:24:40');
INSERT INTO `t_balance` VALUES ('ee932d619643e58901cf5385ee1f7992', '448509298ab541aefd3b7628803174a1', 'ETH', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-08 14:32:30', '2020-09-08 14:32:30');
INSERT INTO `t_balance` VALUES ('f0a25180f4fa418708b8302bd6274bae', '614d6a5e93f01d1e56c6e5db05d7f943', 'BCH', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-10 15:56:00', '2020-09-10 15:56:00');
INSERT INTO `t_balance` VALUES ('f3aeccfc1c3e08c160314ae667de904d', '404208357cb9d619891c7d3b3dff1dae', 'BTCV', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-08 11:24:40', '2020-09-08 11:24:40');
INSERT INTO `t_balance` VALUES ('fbfcf813002699f90c70fd1e2e9bad2e', 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'ETH', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-12-19 13:36:15', '2020-12-19 13:36:15');
INSERT INTO `t_balance` VALUES ('fe86ba9597c69bfe9d10ddac614d00af', '404208357cb9d619891c7d3b3dff1dae', 'ETH', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-08 11:24:40', '2020-09-08 11:24:40');
INSERT INTO `t_balance` VALUES ('fee6efdad058179381967cc3416fe2aa', '3b8f80497d7b71938770b1bf00b64a58', 'ETH', 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, 000000000000000000000000000000.000000000000000000000000000000, NULL, '2020-09-07 09:26:17', '2020-09-07 09:26:17');

-- ----------------------------
-- Table structure for t_banner
-- ----------------------------
DROP TABLE IF EXISTS `t_banner`;
CREATE TABLE `t_banner`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `banner_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'banner图地址',
  `link_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '跳转URL',
  `banner_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态 DATA_BANNER：主页  PROJECT_BANNER：项目方  	PROJECT_BANNER：邀请返佣',
  `global` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '国际化 CHINESE_SIM 中文 	CHINESE_TRAD 繁体中文 	ENGLISH英文',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '币币-banner图' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_banner
-- ----------------------------
INSERT INTO `t_banner` VALUES ('66deb3115b13418098de4160836dd449', 'https://fvex.oss-cn-hangzhou.aliyuncs.com/fvex/jys0.png', 'https://www.baidu.com', 'DATA_BANNER', 'CHINESE_SIM', '2020-05-05 14:16:46', NULL, '2020-07-28 10:06:56', NULL);
INSERT INTO `t_banner` VALUES ('803e3d25de414610a3ebad5910f110fe', 'http://fvex.oss-cn-hangzhou.aliyuncs.com/fvex/40a901b334804539baea0125fa596f7d未标题-2.jpg', 'https://www.baidu.com', 'DATA_BANNER', 'CHINESE_SIM', '2020-08-11 00:42:48', NULL, NULL, NULL);
INSERT INTO `t_banner` VALUES ('81c141f6d15244468ad8ce8d3fbc0add', 'https://fvex.oss-cn-hangzhou.aliyuncs.com/fvex/jys1.png', 'https://www.baidu.com', 'DATA_BANNER', 'CHINESE_SIM', '2020-04-22 17:18:07', NULL, '2020-06-11 10:44:26', NULL);
INSERT INTO `t_banner` VALUES ('9b79f6cdc05d4206a28f91fcab840e23', 'https://fvex.oss-cn-hangzhou.aliyuncs.com/fvex/jysbanner-bg2.png', NULL, 'DATA_BANNER', 'CHINESE_TRAD', '2020-04-22 17:18:49', NULL, '2020-06-11 10:44:30', NULL);
INSERT INTO `t_banner` VALUES ('9b79f6cdc05d4206a28f91fcab840e24', 'https://fvex.oss-cn-hangzhou.aliyuncs.com/fvex/jysbanner-bg3.png', NULL, 'DATA_BANNER', 'CHINESE_TRAD', '2020-04-22 17:18:49', NULL, '2020-06-11 10:44:30', NULL);
INSERT INTO `t_banner` VALUES ('9b79f6cdc05d4206a28f91fcab840e2f', 'https://fvex.oss-cn-hangzhou.aliyuncs.com/fvex/jysbanner-bg1.png', 'https://fvex.oss-cn-hangzhou.aliyuncs.com/fvex/jysbanner-bg1.png', 'DATA_BANNER', 'CHINESE_TRAD', '2020-07-28 19:08:32', NULL, '2020-06-11 10:44:30', NULL);
INSERT INTO `t_banner` VALUES ('a752798ccfc342aca30bba7a92f4b107', 'http://fvex.oss-cn-hangzhou.aliyuncs.com/fvex/b0c9e5ba90744cad90e1e185152ca7c60011.jpg', 'https://www.baidu.com', 'DATA_BANNER', 'CHINESE_SIM', '2020-08-11 00:50:41', NULL, NULL, NULL);
INSERT INTO `t_banner` VALUES ('e2865655b8bc4609ab59c47118475684', 'https://fvex.oss-cn-hangzhou.aliyuncs.com/fvex/jys2.png', 'https://www.baidu.com', 'DATA_BANNER', 'CHINESE_SIM', '2020-04-22 17:18:49', NULL, '2020-06-11 10:44:30', NULL);

-- ----------------------------
-- Table structure for t_bibi_day_record
-- ----------------------------
DROP TABLE IF EXISTS `t_bibi_day_record`;
CREATE TABLE `t_bibi_day_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `currency` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币种',
  `sum_bibi_buy` decimal(32, 8) NULL DEFAULT NULL COMMENT '币币总购买',
  `sum_bibi_sell` decimal(32, 8) NULL DEFAULT NULL COMMENT '币币总出售',
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '币种交易量' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_bibi_day_record
-- ----------------------------
INSERT INTO `t_bibi_day_record` VALUES (1, 'EOS/USDT', 0.00000000, 0.00000000, '2020-12-20 01:00:14');
INSERT INTO `t_bibi_day_record` VALUES (2, 'BCH/USDT', 0.00000000, 0.00000000, '2020-12-20 01:00:16');
INSERT INTO `t_bibi_day_record` VALUES (3, 'ETC/USDT', 0.00000000, 0.00000000, '2020-12-20 01:00:17');
INSERT INTO `t_bibi_day_record` VALUES (4, 'XRP/USDT', 0.00000000, 0.00000000, '2020-12-20 01:00:20');
INSERT INTO `t_bibi_day_record` VALUES (5, 'BTC/USDT', 0.00000000, 0.00000000, '2020-12-20 01:00:23');
INSERT INTO `t_bibi_day_record` VALUES (6, 'LTC/USDT', 1.22276990, 0.00000000, '2020-12-20 01:00:23');
INSERT INTO `t_bibi_day_record` VALUES (7, 'ETH/USDT', 0.00000000, 0.00000000, '2020-12-20 01:00:24');

-- ----------------------------
-- Table structure for t_brokerage_record
-- ----------------------------
DROP TABLE IF EXISTS `t_brokerage_record`;
CREATE TABLE `t_brokerage_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `consume_user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消费者userId',
  `brokerage_user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '回扣的userId',
  `currency` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消费币种',
  `brokerage_number` decimal(20, 8) NOT NULL DEFAULT 0.00000000 COMMENT '返佣数量',
  `number` decimal(20, 8) NOT NULL DEFAULT 0.00000000 COMMENT '消费数量',
  `status` tinyint(2) NOT NULL DEFAULT 0 COMMENT '待分佣(0)已分佣(1)',
  `brokerage_phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_brokerage_record
-- ----------------------------

-- ----------------------------
-- Table structure for t_coin_token
-- ----------------------------
DROP TABLE IF EXISTS `t_coin_token`;
CREATE TABLE `t_coin_token`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `coin` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `token` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `point` int(22) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '币种信息' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_coin_token
-- ----------------------------
INSERT INTO `t_coin_token` VALUES ('060d5997b5a5ae25d4a7c13822c63371', 'USDT', '0xdac17f958d2ee523a2206206994597c13d831ec7', 6);

-- ----------------------------
-- Table structure for t_commission
-- ----------------------------
DROP TABLE IF EXISTS `t_commission`;
CREATE TABLE `t_commission`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `commission_rate` decimal(10, 4) NULL DEFAULT NULL COMMENT '返佣比率',
  `max_money` decimal(10, 0) NULL DEFAULT NULL COMMENT '最大返佣金额',
  `start_date` date NULL DEFAULT NULL COMMENT '开始时间',
  `end_date` date NULL DEFAULT NULL COMMENT '结束时间',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_commission
-- ----------------------------

-- ----------------------------
-- Table structure for t_contract_mul
-- ----------------------------
DROP TABLE IF EXISTS `t_contract_mul`;
CREATE TABLE `t_contract_mul`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pairs_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易对名称',
  `contract_mul` decimal(18, 8) NULL DEFAULT NULL COMMENT '合约乘数',
  `force_price` decimal(18, 8) NULL DEFAULT NULL COMMENT '强平价格',
  `maker_fee` decimal(18, 8) NULL DEFAULT NULL COMMENT '开仓手续费',
  `taker_fee` decimal(18, 8) NULL DEFAULT NULL COMMENT '平仓手续费',
  `force_fee` decimal(18, 8) NULL DEFAULT NULL COMMENT '强平手续费',
  `trade_fee` decimal(18, 8) NULL DEFAULT NULL COMMENT '交易手续费',
  `withdraw_fee` decimal(18, 8) NULL DEFAULT NULL COMMENT '提币手续费',
  `ensure` decimal(10, 4) NULL DEFAULT NULL COMMENT '维持保证金比率',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_contract_mul
-- ----------------------------
INSERT INTO `t_contract_mul` VALUES ('2a225871779c14aa878b22e34ba12270', 'LTC/USDT', 0.20000000, NULL, 0.00050000, 0.00050000, 0.00050000, NULL, 2.00000000, 0.0050, '2020-01-08 15:11:56', '2020-01-17 14:58:53');
INSERT INTO `t_contract_mul` VALUES ('3ace11f2b05bb8d092896cefc7628268', 'DASH/USDT', 0.10000000, NULL, 0.00050000, 0.00050000, 0.00050000, NULL, 2.00000000, 0.0050, '2020-01-08 15:15:35', '2020-01-17 14:58:04');
INSERT INTO `t_contract_mul` VALUES ('537b5f5cb0ca0ae7ff2a5af8af59cdfd', 'XRP/USDT', 40.00000000, NULL, 0.00050000, 0.00050000, 0.00050000, NULL, 2.00000000, 0.0050, '2020-01-08 15:12:17', '2020-01-17 14:58:37');
INSERT INTO `t_contract_mul` VALUES ('595da8a41b70f04fa1e42b5424761cf7', 'ETH/USDT', 0.05000000, NULL, 0.00050000, 0.00050000, 0.00050000, NULL, 2.00000000, 0.0050, '2020-01-08 15:11:37', '2020-01-17 14:59:16');
INSERT INTO `t_contract_mul` VALUES ('6ae279f97bbd11374f34937d842f536a', 'EOS/USDT', 2.00000000, NULL, 0.00050000, 0.00050000, 0.00050000, NULL, 2.00000000, 0.0050, '2020-01-08 15:11:11', '2020-01-17 14:59:29');
INSERT INTO `t_contract_mul` VALUES ('7893fb26c8314196851ff9cb97bf2f50', 'BTC/USDT', 0.00200000, 0.00000000, 0.00050000, 0.00050000, 0.00050000, 0.00000000, 2.00000000, 0.0050, '2020-01-06 15:08:06', '2020-01-17 14:59:51');
INSERT INTO `t_contract_mul` VALUES ('799103133154937e15e07ec1ae99d521', 'BCH/USDT', 0.05000000, NULL, 0.00050000, 0.00050000, 0.00050000, NULL, 2.00000000, 0.0050, '2020-01-08 15:10:24', '2020-01-17 14:59:40');
INSERT INTO `t_contract_mul` VALUES ('f30d6cfa99b3accd8cfea92769796fa9', 'ETC/USDT', 1.00000000, NULL, 0.00050000, 0.00050000, 0.00050000, NULL, 2.00000000, 0.0050, '2020-01-08 15:12:43', '2020-01-17 14:58:21');

-- ----------------------------
-- Table structure for t_contract_order
-- ----------------------------
DROP TABLE IF EXISTS `t_contract_order`;
CREATE TABLE `t_contract_order`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pairs_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易对名称',
  `main_cur` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `coin_name` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币种名称',
  `lever_num` decimal(10, 0) NULL DEFAULT NULL COMMENT '杠杆倍数',
  `lever_desc` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '杠杆描述',
  `margin` decimal(20, 8) NULL DEFAULT NULL COMMENT '保证金',
  `contract_hands` decimal(30, 0) NULL DEFAULT NULL COMMENT '手数',
  `price` decimal(50, 8) NULL DEFAULT NULL COMMENT '持仓价格',
  `match_price` decimal(50, 8) NULL DEFAULT NULL COMMENT '撮合价格',
  `match_fee` decimal(50, 8) NULL DEFAULT NULL,
  `close_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `price_type` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '价格类型',
  `coin_num` decimal(30, 8) NULL DEFAULT NULL COMMENT '数量',
  `is_contract_hands` decimal(30, 8) NULL DEFAULT NULL COMMENT '可用合约乘数',
  `order_state` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
  `take_fee` decimal(30, 8) NULL DEFAULT NULL,
  `order_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单类型',
  `member` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户',
  `trade_type` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易类型',
  `contract_mul_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合约配置 ID',
  `lever_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '杠杆 ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `member_pairs`(`member`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_contract_order
-- ----------------------------
INSERT INTO `t_contract_order` VALUES ('03e742acf6096b68e9b59cf2d0217ba7', 'LTC/USDT', 'USDT', 'LTC', 20, '20X', 107.24760000, 93, 115.32000000, 121.53000000, 115.50600000, NULL, 'MARKET_PRICE', 18.60000000, 93.00000000, 'CREATE', 1.07247600, 'POSITIONS', 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'OPEN_UP', '2a225871779c14aa878b22e34ba12270', '4d95eb7136e6d92ae59180663b319e95', '2020-12-19 23:49:39', '2020-12-19 23:49:39');
INSERT INTO `t_contract_order` VALUES ('111320e8737fe7b3c659ddf78c517649', 'LTC/USDT', 'USDT', 'LTC', 20, '20X', 107.24760000, 93, 115.32000000, 121.53000000, 115.50600000, NULL, 'MARKET_PRICE', 18.60000000, 93.00000000, 'CREATE', 1.07247600, 'POSITIONS', 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'OPEN_UP', '2a225871779c14aa878b22e34ba12270', '4d95eb7136e6d92ae59180663b319e95', '2020-12-19 23:49:35', '2020-12-19 23:49:35');
INSERT INTO `t_contract_order` VALUES ('40935f351c2e5286a904dda3d235df9b', 'LTC/USDT', 'USDT', 'LTC', 20, '20X', 107.24760000, 93, 115.32000000, 121.53000000, 115.50600000, NULL, 'MARKET_PRICE', 18.60000000, 93.00000000, 'CREATE', 1.07247600, 'POSITIONS', 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'OPEN_UP', '2a225871779c14aa878b22e34ba12270', '4d95eb7136e6d92ae59180663b319e95', '2020-12-19 23:48:51', '2020-12-19 23:48:51');
INSERT INTO `t_contract_order` VALUES ('59abf8d61f0836c3d793778b9cb46d1f', 'LTC/USDT', 'USDT', 'LTC', 20, '20X', 107.24760000, 93, 115.32000000, 121.53000000, 115.50600000, NULL, 'MARKET_PRICE', 18.60000000, 93.00000000, 'CREATE', 1.07247600, 'POSITIONS', 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'OPEN_UP', '2a225871779c14aa878b22e34ba12270', '4d95eb7136e6d92ae59180663b319e95', '2020-12-19 23:49:44', '2020-12-19 23:49:44');
INSERT INTO `t_contract_order` VALUES ('7a1be418a6ed16d9b40c7d78ee2a678f', 'LTC/USDT', 'USDT', 'LTC', 100, '100X', 97.56072000, 423, 115.32000000, 121.53000000, 525.36600000, NULL, 'MARKET_PRICE', 84.60000000, 423.00000000, 'CREATE', 4.87803600, 'POSITIONS', 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'OPEN_UP', '2a225871779c14aa878b22e34ba12270', '8435f32f3d6cfe1f3ff33a8c27d6bde7', '2020-12-19 23:50:16', '2020-12-19 23:50:16');
INSERT INTO `t_contract_order` VALUES ('982901db0e297bc97e3fe3fff3504d16', 'LTC/USDT', 'USDT', 'LTC', 20, '20X', 1363.08240000, 1182, 115.32000000, 121.53000000, 1468.04400000, NULL, 'MARKET_PRICE', 236.40000000, 1182.00000000, 'CREATE', 13.63082400, 'POSITIONS', 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'OPEN_UP', '2a225871779c14aa878b22e34ba12270', '4d95eb7136e6d92ae59180663b319e95', '2020-12-19 23:48:25', '2020-12-19 23:48:25');
INSERT INTO `t_contract_order` VALUES ('a069900fb9fae2e7d46296e713c0bf32', 'LTC/USDT', 'USDT', 'LTC', 20, '20X', 1363.08240000, 1182, 115.32000000, 121.53000000, 1468.04400000, NULL, 'MARKET_PRICE', 236.40000000, 1182.00000000, 'CREATE', 13.63082400, 'POSITIONS', 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'OPEN_UP', '2a225871779c14aa878b22e34ba12270', '4d95eb7136e6d92ae59180663b319e95', '2020-12-19 23:48:36', '2020-12-19 23:48:36');
INSERT INTO `t_contract_order` VALUES ('f355c632f07c5bd4d87006782ecf7fde', 'LTC/USDT', 'USDT', 'LTC', 20, '20X', 1363.08240000, 1182, 115.32000000, 121.53000000, 1468.04400000, NULL, 'MARKET_PRICE', 236.40000000, 1182.00000000, 'CREATE', 13.63082400, 'POSITIONS', 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'OPEN_UP', '2a225871779c14aa878b22e34ba12270', '4d95eb7136e6d92ae59180663b319e95', '2020-12-19 23:48:30', '2020-12-19 23:48:30');

-- ----------------------------
-- Table structure for t_day_balance_sum
-- ----------------------------
DROP TABLE IF EXISTS `t_day_balance_sum`;
CREATE TABLE `t_day_balance_sum`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bkt_sum` decimal(32, 8) NULL DEFAULT 0.00000000,
  `voice_sum` decimal(32, 8) NULL DEFAULT 0.00000000,
  `bkt_day_up` decimal(32, 8) NULL DEFAULT 0.00000000 COMMENT 'BKT每日流入',
  `bkt_to_voice` decimal(32, 8) NULL DEFAULT 0.00000000,
  `voice_to_bkt` decimal(32, 8) NULL DEFAULT 0.00000000,
  `suanli` decimal(32, 8) NULL DEFAULT 0.00000000,
  `zhitui` decimal(32, 8) NULL DEFAULT NULL,
  `bkt_fee` decimal(32, 8) NULL DEFAULT 0.00000000 COMMENT '转账手续费',
  `voice_fee` decimal(32, 8) NULL DEFAULT 0.00000000,
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资产统计' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_day_balance_sum
-- ----------------------------

-- ----------------------------
-- Table structure for t_deposit_history
-- ----------------------------
DROP TABLE IF EXISTS `t_deposit_history`;
CREATE TABLE `t_deposit_history`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `member` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `block_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区块高度',
  `tx_hash` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易hash',
  `contract` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合约地址',
  `from_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'FROM',
  `to_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'TO',
  `coin` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币种',
  `amount` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '充值额',
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_deposit_history
-- ----------------------------
INSERT INTO `t_deposit_history` VALUES ('18f51a22-604a-4164-ab03-e3ca81e6d777', '91947f2536fa87358f38768bd92f7329', NULL, 'ztpay91xrbde6wsucn', '0x7dda533ae01392dd7d8b88bb8ab0b952f9542493', 'from', '0x7dda533ae01392dd7d8b88bb8ab0b952f9542493', 'BTC', '3.0', '2020-12-19 12:45:53');

-- ----------------------------
-- Table structure for t_entrust
-- ----------------------------
DROP TABLE IF EXISTS `t_entrust`;
CREATE TABLE `t_entrust`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pairs` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `main_cur` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `token_cur` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pairs_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `price` decimal(50, 8) NULL DEFAULT NULL,
  `match_price` decimal(50, 8) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '成交价格',
  `count` decimal(50, 8) NULL DEFAULT NULL,
  `percentage_count` decimal(20, 8) NULL DEFAULT NULL,
  `match_count` decimal(50, 8) UNSIGNED ZEROFILL NULL DEFAULT 000000000000000000000000000000000000000000.00000000 COMMENT '成交数量',
  `surplus_count` decimal(50, 8) NULL DEFAULT NULL,
  `member` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `member_type` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `trade_rate` decimal(20, 8) NULL DEFAULT NULL,
  `entrust_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '委托类型',
  `method_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '成交方式',
  `price_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `state` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
  `trade_fee` decimal(20, 8) NULL DEFAULT NULL COMMENT '手续费',
  `match_member` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '撮合方',
  `match_fee` decimal(20, 8) NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_state_search`(`state`) USING BTREE,
  INDEX `fk_member_search`(`member`) USING BTREE,
  INDEX `fk_pairs_name_search`(`pairs_name`) USING BTREE,
  INDEX `fk_pairs_member_state`(`pairs_name`, `member`, `state`) USING BTREE,
  INDEX `fk_id_state`(`id`, `state`) USING BTREE,
  INDEX `fk_member_pairs`(`member`, `pairs_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_entrust
-- ----------------------------
INSERT INTO `t_entrust` VALUES ('0ba42d6cf7824811a4142c9e3e5b6779', '69ecb9a740a84f49a0d8347a171100e9', 'USDT', 'ETC', 'ETC/USDT', 5.11110000, NULL, 221.86347527, 0.05999999, 000000000000000000000000000000000000000000.00000000, 221.86347527, '448509298ab541aefd3b7628803174a1', 'people', 0.00300000, 'BUY', NULL, 'CUSTOM_PRICE', 'CREATE', NULL, NULL, 1133.96640845, '2020-09-11 13:58:27', '2020-09-11 13:58:27');
INSERT INTO `t_entrust` VALUES ('3a8999bf4a4e4818abafc0f42aab2507', 'b173b6f0f350067300e147a70ca7c034', 'USDT', 'BTC', 'BTC/USDT', 3.00000000, NULL, 0.46190000, 0.46190000, 000000000000000000000000000000000000000000.00000000, 0.46190000, '79ae9f39e6ef7f81ade8eed1ff1eefa2', 'people', 0.00300000, 'BUY', NULL, 'CUSTOM_PRICE', 'CREATE', NULL, NULL, 1.38570000, '2020-07-25 10:46:35', '2020-07-25 10:46:35');
INSERT INTO `t_entrust` VALUES ('4b7a7458347941d4a81622bd99915b00', '69ecb9a740a84f49a0d8347a171100e9', 'USDT', 'ETC', 'ETC/USDT', 6.06430000, NULL, 98.95552660, 0.07999999, 000000000000000000000000000000000000000000.00000000, 98.95552660, '91947f2536fa87358f38768bd92f7329', 'people', 0.00300000, 'BUY', NULL, 'CUSTOM_PRICE', 'CREATE', NULL, NULL, 600.09599996, '2020-12-17 13:20:10', '2020-12-17 13:20:10');
INSERT INTO `t_entrust` VALUES ('50413850905646c185cd468e97db903f', 'd578de6355125c15ff9d60079f720186', 'USDT', 'BKT', 'BKT/USDT', 106.14920000, NULL, 1.31635293, 1.00000000, 000000000000000000000000000000000000000000.00000000, 1.31635293, '2d6687063a20035418a46cbbe97f7c00', 'people', 0.00300000, 'BUY', NULL, 'CUSTOM_PRICE', 'CREATE', NULL, NULL, 139.72980938, '2020-07-25 14:59:04', '2020-07-25 14:59:04');
INSERT INTO `t_entrust` VALUES ('a5bbff6b231647799bef754133539da0', 'b173b6f0f350067300e147a70ca7c034', 'USDT', 'BTC', 'BTC/USDT', 3.00000000, NULL, 1.65000000, 1.65000000, 000000000000000000000000000000000000000000.00000000, 1.65000000, '79ae9f39e6ef7f81ade8eed1ff1eefa2', 'people', 0.00300000, 'BUY', NULL, 'CUSTOM_PRICE', 'CREATE', NULL, NULL, 4.95000000, '2020-07-25 10:41:38', '2020-07-25 10:41:38');
INSERT INTO `t_entrust` VALUES ('df136e9fb60745d1bbb3374d93e51b8f', '69ecb9a740a84f49a0d8347a171100e9', 'USDT', 'ETC', 'ETC/USDT', 6.54640000, NULL, 73.13943541, 0.05999999, 000000000000000000000000000000000000000000.00000000, 73.13943541, '91947f2536fa87358f38768bd92f7329', 'people', 0.00300000, 'BUY', NULL, 'CUSTOM_PRICE', 'CREATE', NULL, NULL, 478.79999997, '2020-12-17 13:19:19', '2020-12-17 13:19:19');
INSERT INTO `t_entrust` VALUES ('e7de5fea6c8f47f88115546f1f5cf088', 'd578de6355125c15ff9d60079f720185', 'USDT', 'SQT', 'SQT/USDT', 3.31000000, NULL, 20.24169184, 0.66999999, 000000000000000000000000000000000000000000.00000000, 20.24169184, '79ae9f39e6ef7f81ade8eed1ff1eefa2', 'people', 0.00300000, 'BUY', NULL, 'CUSTOM_PRICE', 'CREATE', NULL, NULL, 66.99999999, '2020-07-23 15:26:20', '2020-07-23 15:26:20');
INSERT INTO `t_entrust` VALUES ('fdf2c813414946a2a3b67394dab92793', 'b173b6f0f350067300e147a70ca7c034', 'USDT', 'BTC', 'BTC/USDT', 3.00000000, NULL, 1.65000000, 1.65000000, 000000000000000000000000000000000000000000.00000000, 1.65000000, '79ae9f39e6ef7f81ade8eed1ff1eefa2', 'people', 0.00300000, 'BUY', NULL, 'CUSTOM_PRICE', 'CREATE', NULL, NULL, 4.95000000, '2020-07-25 10:41:10', '2020-07-25 10:41:10');

-- ----------------------------
-- Table structure for t_entrust_history
-- ----------------------------
DROP TABLE IF EXISTS `t_entrust_history`;
CREATE TABLE `t_entrust_history`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pairs` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `main_cur` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `token_cur` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pairs_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `price` decimal(50, 8) NULL DEFAULT NULL,
  `match_price` decimal(50, 8) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '成交价格',
  `count` decimal(50, 8) NULL DEFAULT NULL,
  `percentage_count` decimal(20, 8) NULL DEFAULT NULL,
  `match_count` decimal(50, 8) UNSIGNED ZEROFILL NULL DEFAULT 000000000000000000000000000000000000000000.00000000 COMMENT '成交数量',
  `surplus_count` decimal(50, 8) NULL DEFAULT NULL,
  `member` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `member_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `trade_rate` decimal(20, 8) NULL DEFAULT NULL,
  `entrust_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '委托类型',
  `method_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '成交方式',
  `price_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `state` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
  `trade_fee` decimal(20, 8) NULL DEFAULT NULL COMMENT '手续费',
  `match_member` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '撮合方',
  `match_fee` decimal(20, 8) NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_state_search`(`state`) USING BTREE,
  INDEX `fk_member`(`member`) USING BTREE,
  INDEX `fk_member_pairs_name`(`member`, `pairs_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_entrust_history
-- ----------------------------
INSERT INTO `t_entrust_history` VALUES ('1e95659d1b0a4547b822b3d74b1ed6fb', 'b739a726a4a04f8db5bf5ff850a9d161', 'USDT', 'LTC', 'LTC/USDT', 81.78153538, 000000000000000000000000000000000000000081.78153538, 1.22276990, 0.01000000, 000000000000000000000000000000000000000001.22276990, 0.00000000, 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'people', 0.00300000, 'BUY', 'ACTIVE', 'MARKET_PRICE', 'FINAL', 0.30000000, 'robot', 99.99999984, '2020-12-19 19:24:55', '2020-12-19 19:24:55');
INSERT INTO `t_entrust_history` VALUES ('2777c6f313ab4d5f9dfa8271bddf595b', '69ecb9a740a84f49a0d8347a171100e9', 'USDT', 'ETC', 'ETC/USDT', 6.06408930, 000000000000000000000000000000000000000006.06408930, 82.45261164, 0.05000000, 000000000000000000000000000000000000000082.45261164, 0.00000000, '91947f2536fa87358f38768bd92f7329', 'people', 0.00300000, 'BUY', 'ACTIVE', 'MARKET_PRICE', 'FINAL', 1.50000000, 'robot', 500.00000000, '2020-12-17 14:29:22', '2020-12-17 14:29:22');
INSERT INTO `t_entrust_history` VALUES ('62da0ef700194a4c9ae9105a3c237d2c', '69ecb9a740a84f49a0d8347a171100e9', 'USDT', 'ETC', 'ETC/USDT', 6.06189734, 000000000000000000000000000000000000000006.06189734, 486.56440807, 0.08000000, 000000000000000000000000000000000000000486.56440807, 0.00000000, '91947f2536fa87358f38768bd92f7329', 'people', 0.00300000, 'SELL', 'ACTIVE', 'MARKET_PRICE', 'FINAL', 8.84851047, 'robot', 2949.50349102, '2020-12-17 14:29:25', '2020-12-17 14:29:25');
INSERT INTO `t_entrust_history` VALUES ('79b210c926f147dfa2a8a23fb7e436c7', '69ecb9a740a84f49a0d8347a171100e9', 'USDT', 'ETC', 'ETC/USDT', 6.06155157, 000000000000000000000000000000000000000006.06155157, 291.28016627, 0.13000000, 000000000000000000000000000000000000000291.28016627, 0.00000000, '91947f2536fa87358f38768bd92f7329', 'people', 0.00300000, 'SELL', 'ACTIVE', 'MARKET_PRICE', 'FINAL', 5.29682925, 'robot', 1765.60974916, '2020-12-17 14:29:38', '2020-12-17 14:29:38');
INSERT INTO `t_entrust_history` VALUES ('86bddc4838bf47b68a439a5a538667ee', '69ecb9a740a84f49a0d8347a171100e9', 'USDT', 'ETC', 'ETC/USDT', 6.06153092, 000000000000000000000000000000000000000006.06153092, 497.59427779, 0.13000000, 000000000000000000000000000000000000000497.59427779, 0.00000000, '91947f2536fa87358f38768bd92f7329', 'people', 0.00300000, 'SELL', 'ACTIVE', 'MARKET_PRICE', 'FINAL', 9.04854930, 'robot', 3016.18310044, '2020-12-17 14:29:19', '2020-12-17 14:29:19');
INSERT INTO `t_entrust_history` VALUES ('96a9c511300d4f68acbdf405a2148af6', '69ecb9a740a84f49a0d8347a171100e9', 'USDT', 'ETC', 'ETC/USDT', 6.06187244, 000000000000000000000000000000000000000006.06187244, 313.43559193, 0.08000000, 000000000000000000000000000000000000000313.43559193, 0.00000000, '91947f2536fa87358f38768bd92f7329', 'people', 0.00300000, 'SELL', 'ACTIVE', 'MARKET_PRICE', 'FINAL', 5.70001973, 'robot', 1900.00657644, '2020-12-17 14:29:28', '2020-12-17 14:29:28');
INSERT INTO `t_entrust_history` VALUES ('bd587586d8fe4c5e8ee330b11809b2da', '69ecb9a740a84f49a0d8347a171100e9', 'USDT', 'ETC', 'ETC/USDT', 6.06187244, 000000000000000000000000000000000000000006.06187244, 407.12555594, 0.13000000, 000000000000000000000000000000000000000407.12555594, 0.00000000, '91947f2536fa87358f38768bd92f7329', 'people', 0.00300000, 'SELL', 'ACTIVE', 'MARKET_PRICE', 'FINAL', 7.40382956, 'robot', 2467.94318717, '2020-12-17 14:29:36', '2020-12-17 14:29:36');
INSERT INTO `t_entrust_history` VALUES ('eaaf8018935c4c219263fcf504848741', '69ecb9a740a84f49a0d8347a171100e9', 'USDT', 'ETC', 'ETC/USDT', 6.06408930, 000000000000000000000000000000000000000006.06408930, 250.65593938, 0.16000000, 000000000000000000000000000000000000000250.65593938, 0.00000000, '91947f2536fa87358f38768bd92f7329', 'people', 0.00300000, 'BUY', 'ACTIVE', 'MARKET_PRICE', 'FINAL', 4.56000000, 'robot', 1519.99999998, '2020-12-17 14:29:31', '2020-12-17 14:29:31');

-- ----------------------------
-- Table structure for t_extract_coin
-- ----------------------------
DROP TABLE IF EXISTS `t_extract_coin`;
CREATE TABLE `t_extract_coin`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `member` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `balance` decimal(50, 8) NULL DEFAULT NULL COMMENT '数量',
  `handling_fee` decimal(20, 8) NOT NULL DEFAULT 0.00000000 COMMENT '手续费',
  `currency` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币种',
  `wallet` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提币地址',
  `state` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核中CREATE;审核通过PASS;拒绝REJECT',
  `hex` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'php:交易hex/交易ID',
  `view_in_explorer` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'php:交易区块页面',
  `from_address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'php:出方地址',
  `to_address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'php:收方地址',
  `token_number` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'php:转账数量',
  `extract_time` datetime NULL DEFAULT NULL COMMENT 'php:时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_extract_coin
-- ----------------------------

-- ----------------------------
-- Table structure for t_fabi_day_record
-- ----------------------------
DROP TABLE IF EXISTS `t_fabi_day_record`;
CREATE TABLE `t_fabi_day_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `currency` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币种',
  `sum_buy` decimal(32, 8) NULL DEFAULT NULL COMMENT '当日购买',
  `sum_sell` decimal(32, 8) NULL DEFAULT NULL COMMENT '当日出售',
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '法币交易统计' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_fabi_day_record
-- ----------------------------
INSERT INTO `t_fabi_day_record` VALUES (1, 'USDT', 0.00000000, 0.00000000, '2020-12-20 01:00:25');

-- ----------------------------
-- Table structure for t_lever
-- ----------------------------
DROP TABLE IF EXISTS `t_lever`;
CREATE TABLE `t_lever`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `lever` bigint(10) NULL DEFAULT NULL COMMENT '倍数',
  `lever_desc` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '倍数描述',
  `pairs_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易对',
  `hands_max` bigint(5) NULL DEFAULT NULL COMMENT '手',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_lever
-- ----------------------------
INSERT INTO `t_lever` VALUES ('02e0895706a335edecf279cdd505f6cd', 200, '200X', 'BCH/USDT', NULL, '2020-01-17 11:09:59', '2020-08-17 14:29:20');
INSERT INTO `t_lever` VALUES ('03401714c9ff61e830143713f861bc9f', 30, '30X', 'ETH/USDT', NULL, '2020-01-17 11:10:52', '2020-01-17 11:10:52');
INSERT INTO `t_lever` VALUES ('073f57b8262a6d0ff3a2be4b51d3a868', 10, '10X', 'XRP/USDT', NULL, '2020-01-17 11:18:40', '2020-01-17 11:18:40');
INSERT INTO `t_lever` VALUES ('08fbc4b553a7beb836c62c15409fb902', 20, '20X', 'BTC/USDT', NULL, '2020-01-17 11:08:19', '2020-01-17 11:08:19');
INSERT INTO `t_lever` VALUES ('14a279a8f2594437d356d12822facb5b', 30, '30X', 'XRP/USDT', NULL, '2020-01-17 11:18:59', '2020-01-17 11:18:59');
INSERT INTO `t_lever` VALUES ('1961b0846abe01f43e48068687e66fe0', 20, '20X', 'ETH/USDT', NULL, '2020-01-17 11:10:43', '2020-01-17 11:10:43');
INSERT INTO `t_lever` VALUES ('2b7bac52b8084f07521d85dfcae0247e', 50, '50X', 'DASH/USDT', NULL, '2020-01-17 11:15:03', '2020-01-17 11:15:03');
INSERT INTO `t_lever` VALUES ('2f750ca605eb0ba727e6bba61ffc15f5', 100, '100X', 'BCH/USDT', NULL, '2020-01-17 11:09:50', '2020-01-17 11:09:50');
INSERT INTO `t_lever` VALUES ('33fef0ab5091dd9ca97f685e35f5918d', 200, '200X', 'BTC/USDT', NULL, '2020-01-17 11:09:06', '2020-01-17 11:09:06');
INSERT INTO `t_lever` VALUES ('39d0e2383ded7c5e00fae78c92d5bdff', 30, '30X', 'LTC/USDT', NULL, '2020-01-17 11:16:07', '2020-01-17 11:16:07');
INSERT INTO `t_lever` VALUES ('3ddcc72c63aabd3d975f02da8812d34f', 50, '50X', 'XRP/USDT', NULL, '2020-01-17 11:19:09', '2020-01-17 11:19:09');
INSERT INTO `t_lever` VALUES ('438e8f8879a7218be3a23d4293241324', 30, '30X', 'DASH/USDT', NULL, '2020-01-17 11:14:52', '2020-01-17 11:14:52');
INSERT INTO `t_lever` VALUES ('4aadb111799860e0dd3add654547b6af', 10, '10X', 'LTC/USDT', NULL, '2020-01-17 11:15:48', '2020-01-17 11:15:48');
INSERT INTO `t_lever` VALUES ('4d95eb7136e6d92ae59180663b319e95', 20, '20X', 'LTC/USDT', NULL, '2020-01-17 11:15:57', '2020-01-17 11:15:57');
INSERT INTO `t_lever` VALUES ('53d3627610cbb4779903a14da3dba735', 30, '30X', 'BCH/USDT', NULL, '2020-01-17 11:09:34', '2020-01-17 11:09:34');
INSERT INTO `t_lever` VALUES ('54b4e1a5bced739e97b1383e0bb57e1c', 50, '50X', 'BTC/USDT', NULL, '2020-01-17 11:08:43', '2020-01-17 11:08:43');
INSERT INTO `t_lever` VALUES ('5d894aa094a5fe42e6096c7d8b64437f', 30, '30X', 'EOS/USDT', NULL, '2020-01-17 11:17:45', '2020-01-17 11:17:45');
INSERT INTO `t_lever` VALUES ('6134c9fe9a4257b2a21e7fc31f015cbe', 20, '20X', 'BCH/USDT', NULL, '2020-01-17 11:09:27', '2020-01-17 11:09:27');
INSERT INTO `t_lever` VALUES ('6268fba1cd5d86375e0a1ed3f0b9d065', 200, '200X', 'EOS/USDT', NULL, '2020-01-17 11:18:23', '2020-01-17 11:18:23');
INSERT INTO `t_lever` VALUES ('6a61623d679f2333f92a59bad3cff3b0', 100, '100X', 'EOS/USDT', NULL, '2020-01-17 11:18:13', '2020-01-17 11:18:13');
INSERT INTO `t_lever` VALUES ('6b76c8da5ab58956a9a4f6870ad0cb0f', 50, '50X', 'BCH/USDT', NULL, '2020-01-17 11:09:42', '2020-01-17 11:09:42');
INSERT INTO `t_lever` VALUES ('6e56dfdb7a7c40da4879f05be77b80a6', 100, '100X', 'XRP/USDT', NULL, '2020-01-17 11:19:18', '2020-01-17 11:19:18');
INSERT INTO `t_lever` VALUES ('72df11d31f13224536e87645859b8497', 200, '200X', 'ETC/USDT', NULL, '2020-01-17 11:13:23', '2020-01-17 11:13:23');
INSERT INTO `t_lever` VALUES ('781d8d0471a63a1ab9d580fec9aece78', 100, '100X', 'ETH/USDT', NULL, '2020-01-17 11:11:11', '2020-01-17 11:11:11');
INSERT INTO `t_lever` VALUES ('83b70a5530652e251f6a99683867a268', 200, '200X', 'LTC/USDT', NULL, '2020-01-17 11:17:02', '2020-01-17 11:17:02');
INSERT INTO `t_lever` VALUES ('8435f32f3d6cfe1f3ff33a8c27d6bde7', 100, '100X', 'LTC/USDT', NULL, '2020-01-17 11:16:53', '2020-01-17 11:16:53');
INSERT INTO `t_lever` VALUES ('86b9e695b11556354a0939f3773a47fd', 10, '10X', 'EOS/USDT', NULL, '2020-01-17 11:17:18', '2020-01-17 11:17:18');
INSERT INTO `t_lever` VALUES ('871c144236c35e15bd4dcf58b7c37246', 50, '50X', 'EOS/USDT', NULL, '2020-01-17 11:18:01', '2020-01-17 11:18:01');
INSERT INTO `t_lever` VALUES ('877ad537cbe5447d747bd08e297c71ae', 50, '50X', 'ETC/USDT', NULL, '2020-01-17 11:12:57', '2020-01-17 11:12:57');
INSERT INTO `t_lever` VALUES ('8d80476ca8987f9070016feb68afc142', 30, '30X', 'BTC/USDT', NULL, '2020-01-17 11:08:31', '2020-01-17 11:08:31');
INSERT INTO `t_lever` VALUES ('90772d2cc3b00221eb5bbe43b3daaa28', 100, '100X', 'DASH/USDT', NULL, '2020-01-17 11:15:13', '2020-01-17 11:15:13');
INSERT INTO `t_lever` VALUES ('96343975b662907704086b9fb5302c9e', 100, '100X', 'BTC/USDT', NULL, '2020-01-17 11:08:55', '2020-01-17 11:08:55');
INSERT INTO `t_lever` VALUES ('9aa830fec4d9743af2bb7aabf07f047d', 200, '200X', 'XRP/USDT', NULL, '2020-01-17 11:19:29', '2020-01-17 11:19:29');
INSERT INTO `t_lever` VALUES ('a2afe773049ba8a961e2895d0d2d08e1', 20, '20X', 'DASH/USDT', NULL, '2020-01-17 11:14:42', '2020-01-17 11:14:42');
INSERT INTO `t_lever` VALUES ('a8dfadd28868eb1758f61def04cec58c', 20, '20X', 'EOS/USDT', NULL, '2020-01-17 11:17:30', '2020-01-17 11:17:30');
INSERT INTO `t_lever` VALUES ('a94279463b266534cf58184473abacf2', 10, '10X', 'ETC/USDT', NULL, '2020-01-17 11:11:42', '2020-01-17 11:11:42');
INSERT INTO `t_lever` VALUES ('b44fbda087ad27360e096470b9896e58', 10, '10X', 'DASH/USDT', NULL, '2020-01-17 11:14:32', '2020-01-17 11:14:32');
INSERT INTO `t_lever` VALUES ('b776aa7c1ef63c768321132ce99393f8', 100, '100X', 'ETC/USDT', NULL, '2020-01-17 11:13:14', '2020-01-17 11:13:14');
INSERT INTO `t_lever` VALUES ('c375309a5cbacd96b07cf239cea9eacf', 10, '10X', 'BTC/USDT', NULL, '2020-01-17 11:08:05', '2020-01-17 11:08:05');
INSERT INTO `t_lever` VALUES ('c3e09019b1bc89682480b65ad3bd95ed', 200, '200X', 'DASH/USDT', NULL, '2020-01-17 11:15:22', '2020-01-17 11:15:22');
INSERT INTO `t_lever` VALUES ('cbfddbccbd5aad2d17dc6b9cbaff5176', 30, '30X', 'ETC/USDT', NULL, '2020-01-17 11:12:44', '2020-01-17 11:12:44');
INSERT INTO `t_lever` VALUES ('dbc510015742df35720395d23fa294fc', 20, '20X', 'ETC/USDT', NULL, '2020-01-17 11:12:34', '2020-01-17 11:12:34');
INSERT INTO `t_lever` VALUES ('e3e6f0c4226ee7f2dffca13493fab338', 50, '50X', 'LTC/USDT', NULL, '2020-01-17 11:16:42', '2020-01-17 11:16:42');
INSERT INTO `t_lever` VALUES ('e73ee3496897c75db80f27229df054e8', 50, '50X', 'ETH/USDT', NULL, '2020-01-17 11:11:02', '2020-01-17 11:11:02');
INSERT INTO `t_lever` VALUES ('eea8f618eb373ef1ccebdfc46307f6cd', 10, '10X', 'BCH/USDT', NULL, '2020-01-17 11:09:18', '2020-01-17 11:09:18');
INSERT INTO `t_lever` VALUES ('ef42e0ded4da4a45f172d3aa6a7531f8', 200, '200X', 'ETH/USDT', NULL, '2020-01-17 11:11:25', '2020-01-17 11:11:25');
INSERT INTO `t_lever` VALUES ('f25d2c1dcd6f74037f61ae681fc34fc4', 10, '10X', 'ETH/USDT', NULL, '2020-01-17 11:10:34', '2020-01-17 11:10:34');
INSERT INTO `t_lever` VALUES ('f5bc27eca45713fa6293002db1e0c42e', 20, '20X', 'XRP/USDT', NULL, '2020-01-17 11:18:50', '2020-01-17 11:18:50');

-- ----------------------------
-- Table structure for t_member
-- ----------------------------
DROP TABLE IF EXISTS `t_member`;
CREATE TABLE `t_member`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `phone` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `uuid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mail` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录密码',
  `wel_member` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邀请人id',
  `brokerage` decimal(20, 8) NOT NULL DEFAULT 0.00000000 COMMENT '佣金',
  `wel_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邀请码',
  `pay_password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '支付密码',
  `type` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号类型:INTERNAL内部的,EXTERNAL外部的',
  `uname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sex` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `birth` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `card_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `quick_mark` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `broker` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是(1)否(0)',
  `broker_grade_one` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '经纪人等级一',
  `broker_grade_two` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '经纪人等级二',
  `broker_grade_three` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '经纪人等级三',
  `node_path` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '邀请节点路径',
  `nick_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pay_wechat` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pay_aliay` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `wechat_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `aliay_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `side_link` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `positive_link` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `card_state` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `store_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '商家昵称',
  `bank_user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bank_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bank_address` varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bank_card` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `fb_status` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'NORMAL' COMMENT '法币是否可用\'NORMAL\'可用，UNNORMAL 不可用',
  `store_state` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'NO',
  `salt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码盐',
  `area_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区号',
  `hand_link` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手持身份证照片',
  `user_status` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '封号状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_member
-- ----------------------------
INSERT INTO `t_member` VALUES ('3b8f80497d7b71938770b1bf00b64a58', '17377384639', '16106667', NULL, 'd98c867b48b2faca6079159916540f82', NULL, 0.00000000, 'VRORZE', '', NULL, '李影涛', '男', '19851231', '231181198512311819', '黑龙江省黑河市北安市', NULL, 0, '', '', '', NULL, '暗香', NULL, NULL, NULL, NULL, 'http://fvex.oss-cn-hangzhou.aliyuncs.com/fvex/728e4f71b6c946bb84424e57c9798efdfile.jpeg', 'http://fvex.oss-cn-hangzhou.aliyuncs.com/fvex/ae787433a98f4dcdae9a190b8ceed6d7file.jpeg', 'PASS', '2020-09-07 09:26:17', '2020-09-12 21:21:31', '', NULL, NULL, NULL, NULL, 'NORMAL', 'PASS', '262c362f8dba4ecba53e9c930ddc13d1', '86', 'http://fvex.oss-cn-hangzhou.aliyuncs.com/fvex/6b8e7f9431bb48168940a50eb412d3b7file.jpeg', 'MAIN_DEPOSIT_ACCOUNT');
INSERT INTO `t_member` VALUES ('404208357cb9d619891c7d3b3dff1dae', '17791260783', '41022375', NULL, '5ee6dad88399cf256fa7800cfb7d47f7', NULL, 0.00000000, 'YWRTO5', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2020-09-08 11:24:40', '2020-09-08 11:24:40', '', NULL, NULL, NULL, NULL, 'NORMAL', 'NO', '0577cfd6b4094f058a54448e115db3f6', '86', NULL, 'NORMAL');
INSERT INTO `t_member` VALUES ('448509298ab541aefd3b7628803174a1', '15509106509', '62135551', NULL, '461d12bc14a865225a297cdce783172e', NULL, 0.00000000, '6F1N8Z', '', NULL, '王天煜', '男', '19960508', '610402199605087516', '陕西省咸阳市秦都区', NULL, 0, '', '', '', NULL, '金牌商家', NULL, 'http://fvex.oss-cn-hangzhou.aliyuncs.com/fvex/12050651743c47b08d3dd141a269bc2ffile.jpeg', NULL, '15509106509', NULL, NULL, 'PASS', '2020-09-08 14:32:30', '2020-09-12 21:21:38', '', NULL, NULL, NULL, NULL, 'NORMAL', 'PASS', '134795e03241457bafe71d12846e1f80', '86', NULL, 'MAIN_WITHDRAW_ACCOUNT');
INSERT INTO `t_member` VALUES ('614d6a5e93f01d1e56c6e5db05d7f943', '15829657732', '72315272', NULL, '2a2c1432f5dc4fa6771001e871a50c3d', NULL, 0.00000000, 'TQ9ER3', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2020-09-10 15:56:00', '2020-09-10 15:56:00', '', NULL, NULL, NULL, NULL, 'NORMAL', 'NO', 'ce7ecd7bd7674c049aac3fb5d3fa9054', '86', NULL, 'NORMAL');
INSERT INTO `t_member` VALUES ('91947f2536fa87358f38768bd92f7329', NULL, '65255372', '481396360@qq.com', '9b72c02f642feb3cc1535ede4a4ef8df', NULL, 0.00000000, '7UOQSR', '9b72c02f642feb3cc1535ede4a4ef8df', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2020-12-16 14:12:41', '2020-12-16 14:12:41', '', NULL, NULL, NULL, NULL, 'NORMAL', 'NO', '9cc7d4e51193414a8426432a309d613c', NULL, NULL, 'NORMAL');
INSERT INTO `t_member` VALUES ('fc6e5e3ae9e8c0f05efd85501edcb7ce', NULL, '35257464', 'hhfylm@qq.com', 'd70e9fa2df944593763eaa156424cd97', NULL, 0.00000000, 'JFYIK3', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'PASS', '2020-12-19 13:36:06', '2020-12-19 16:16:29', '', NULL, NULL, NULL, NULL, 'NORMAL', 'PASS', '4da640c2caec4f8d805c1885052ed63d', NULL, NULL, 'NORMAL');

-- ----------------------------
-- Table structure for t_message_record
-- ----------------------------
DROP TABLE IF EXISTS `t_message_record`;
CREATE TABLE `t_message_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '短信邮箱发送记录' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_message_record
-- ----------------------------
INSERT INTO `t_message_record` VALUES (1, '481396360@qq.com', '【BSEX】您本次操作邮箱验证码为150032有效期5分钟，如非本人操作请忽略！', 'REGISTER', '2020-12-16 14:12:27');
INSERT INTO `t_message_record` VALUES (2, 'hhfylm@qq.com', '【BSEX】您本次操作邮箱验证码为178671有效期5分钟，如非本人操作请忽略！', 'REGISTER', '2020-12-19 13:35:04');
INSERT INTO `t_message_record` VALUES (3, 'hhfylm@qq.com', '【BSEX】您本次操作邮箱验证码为117241有效期5分钟，如非本人操作请忽略！', 'REGISTER', '2020-12-19 13:35:04');
INSERT INTO `t_message_record` VALUES (4, 'hhfylm@qq.com', '【BSEX】您本次操作邮箱验证码为196052有效期5分钟，如非本人操作请忽略！', 'REGISTER', '2020-12-19 13:35:05');
INSERT INTO `t_message_record` VALUES (5, 'hhfylm@qq.com', '【BSEX】您本次操作邮箱验证码为175928有效期5分钟，如非本人操作请忽略！', 'REGISTER', '2020-12-19 13:35:05');
INSERT INTO `t_message_record` VALUES (6, 'hhfylm@qq.com', '【BSEX】您本次操作邮箱验证码为133841有效期5分钟，如非本人操作请忽略！', 'REGISTER', '2020-12-19 13:35:06');
INSERT INTO `t_message_record` VALUES (7, 'hhfylm@qq.com', '【BSEX】您本次操作邮箱验证码为102615有效期5分钟，如非本人操作请忽略！', 'REGISTER', '2020-12-19 13:35:06');
INSERT INTO `t_message_record` VALUES (8, 'hhfylm@qq.com', '【BSEX】您本次操作邮箱验证码为191571有效期5分钟，如非本人操作请忽略！', 'REGISTER', '2020-12-19 13:35:06');
INSERT INTO `t_message_record` VALUES (9, 'hhfylm@qq.com', '【BSEX】您本次操作邮箱验证码为154750有效期5分钟，如非本人操作请忽略！', 'REGISTER', '2020-12-19 13:35:07');
INSERT INTO `t_message_record` VALUES (10, 'hhfylm@qq.com', '【BSEX】您本次操作邮箱验证码为151402有效期5分钟，如非本人操作请忽略！', 'REGISTER', '2020-12-19 13:35:08');
INSERT INTO `t_message_record` VALUES (11, 'hhfylm@qq.com', '【BSEX】您本次操作邮箱验证码为182604有效期5分钟，如非本人操作请忽略！', 'REGISTER', '2020-12-19 13:35:09');
INSERT INTO `t_message_record` VALUES (12, 'hhfylm@qq.com', '【BSEX】您本次操作邮箱验证码为168426有效期5分钟，如非本人操作请忽略！', 'REGISTER', '2020-12-19 13:35:09');
INSERT INTO `t_message_record` VALUES (13, 'hhfylm@qq.com', '【BSEX】您本次操作邮箱验证码为164910有效期5分钟，如非本人操作请忽略！', 'REGISTER', '2020-12-19 13:35:10');
INSERT INTO `t_message_record` VALUES (14, 'hhfylm@qq.com', '【BSEX】您本次操作邮箱验证码为176717有效期5分钟，如非本人操作请忽略！', 'REGISTER', '2020-12-19 13:35:11');
INSERT INTO `t_message_record` VALUES (15, 'hhfylm@qq.com', '【BSEX】您本次操作邮箱验证码为158196有效期5分钟，如非本人操作请忽略！', 'REGISTER', '2020-12-19 13:35:11');
INSERT INTO `t_message_record` VALUES (16, 'hhfylm@qq.com', '【BSEX】您本次操作邮箱验证码为165644有效期5分钟，如非本人操作请忽略！', 'REGISTER', '2020-12-19 13:35:11');
INSERT INTO `t_message_record` VALUES (17, 'hhfylm@qq.com', '【BSEX】您本次操作邮箱验证码为115611有效期5分钟，如非本人操作请忽略！', 'REGISTER', '2020-12-19 13:35:12');
INSERT INTO `t_message_record` VALUES (18, 'hhfylm@qq.com', '【BSEX】您本次操作邮箱验证码为175986有效期5分钟，如非本人操作请忽略！', 'REGISTER', '2020-12-19 13:35:12');
INSERT INTO `t_message_record` VALUES (19, 'hhfylm@qq.com', '【BSEX】您本次操作邮箱验证码为105382有效期5分钟，如非本人操作请忽略！', 'REGISTER', '2020-12-19 13:35:12');
INSERT INTO `t_message_record` VALUES (20, 'hhfylm@qq.com', '【BSEX】您本次操作邮箱验证码为113884有效期5分钟，如非本人操作请忽略！', 'REGISTER', '2020-12-19 13:35:13');
INSERT INTO `t_message_record` VALUES (21, 'hhfylm@qq.com', '【BSEX】您本次操作邮箱验证码为157689有效期5分钟，如非本人操作请忽略！', 'REGISTER', '2020-12-19 13:35:17');

-- ----------------------------
-- Table structure for t_notice
-- ----------------------------
DROP TABLE IF EXISTS `t_notice`;
CREATE TABLE `t_notice`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `title` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告标题',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容',
  `img_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告图地址',
  `link_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '跳转地址',
  `notice_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'OFFICIAL:官方公告 NEWS：新闻资讯',
  `is_favorite` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收藏状态(1:收藏 0：未收藏)',
  `read_count` int(255) NULL DEFAULT 0 COMMENT '浏览量',
  `global` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '国际化 CHINESE_SIM 中文 	CHINESE_TRAD 繁体中文 	ENGLISH英文',
  `bak_text` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用文本',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '币币-公告' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_notice
-- ----------------------------
INSERT INTO `t_notice` VALUES ('2825c4b2df114fc08468a2f41ed0d5a7', 'BSEX关于空投奖励BS的公告', '<p>&nbsp; &nbsp;BSEX联盟社区BS贝思币百万豪礼大放送，SEX全球交易所送10000BS贝思币，贝思币2020年8月16开盘上线“每天1%-5%涨”免费注册，注册好后立马实名认证（注册好登入账号右上角小人点击开，实名认证）拍三张照片，身份证正反面各一张,认证完等待审核通过，通过后赠送10000“BS”奖励。</p><p>&nbsp; &nbsp; &nbsp;&nbsp;【项目亮点】①实名制，注册审核通过就送10000枚BS②零投资，零风险；大干大赚，小干小赚，一个人单干也能赚，人人可为，后期BS价格持续待涨。③组建自己的社群，以后以社群为单位福利多多。④9月1号正式启动，天时地利人和。紧跟我们步伐，时时关注后台公告，未来福利机制会逐渐增加，人人平等。</p><p>&nbsp; &nbsp;</p>', NULL, '', 'OFFICIAL', NULL, 0, 'CHINESE_SIM', NULL, '2020-08-19 01:00:53', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `member` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '会员id',
  `num` decimal(32, 8) NULL DEFAULT NULL COMMENT '数量',
  `one_price` decimal(32, 2) NULL DEFAULT NULL COMMENT '单价',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'NORMAL' COMMENT 'NORMAL 未付款  TRADING 交易中  SUCCEED 交易成功  FALL交易失败',
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_order
-- ----------------------------

-- ----------------------------
-- Table structure for t_order_price
-- ----------------------------
DROP TABLE IF EXISTS `t_order_price`;
CREATE TABLE `t_order_price`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `order_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `buy_member` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sell_member` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `one_price` decimal(32, 8) NULL DEFAULT NULL,
  `num` decimal(32, 8) NULL DEFAULT NULL,
  `pay_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'START' COMMENT 'START 币已冻结 PAYMENT 已付款  SUCCEED 交易成功  FALL交易失败 BACK 已取消',
  `complaint_status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'APPEAL',
  `complaint_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pic` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pic1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `msg` varchar(600) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '下单表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_order_price
-- ----------------------------

-- ----------------------------
-- Table structure for t_otc_appeal_type
-- ----------------------------
DROP TABLE IF EXISTS `t_otc_appeal_type`;
CREATE TABLE `t_otc_appeal_type`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_otc_appeal_type
-- ----------------------------
INSERT INTO `t_otc_appeal_type` VALUES (1, '已付款未放币', '2020-03-30 11:14:24');
INSERT INTO `t_otc_appeal_type` VALUES (2, '未收到付款', '2020-03-20 09:50:13');
INSERT INTO `t_otc_appeal_type` VALUES (3, '未收到货币', '2020-03-20 09:50:48');

-- ----------------------------
-- Table structure for t_otc_currenct_config
-- ----------------------------
DROP TABLE IF EXISTS `t_otc_currenct_config`;
CREATE TABLE `t_otc_currenct_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `currency` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sort` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '法币币种配置表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_otc_currenct_config
-- ----------------------------
INSERT INTO `t_otc_currenct_config` VALUES (1, 'USDT', '1', '2020-08-06 14:32:12', '2020-08-06 14:32:12');

-- ----------------------------
-- Table structure for t_otc_order
-- ----------------------------
DROP TABLE IF EXISTS `t_otc_order`;
CREATE TABLE `t_otc_order`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `member` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户',
  `member_fb_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户法币昵称',
  `currency` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '币种',
  `num` decimal(32, 8) NULL DEFAULT NULL,
  `up_down_number` decimal(32, 8) NOT NULL,
  `min_price` decimal(32, 8) NULL DEFAULT NULL COMMENT '最小成交价格',
  `extremum` decimal(32, 8) NULL DEFAULT NULL,
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '交易说明',
  `direction` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '方向',
  `pay_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收款方式',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单状态',
  `auto_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_croatian_ci NULL DEFAULT NULL,
  `auto_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `freeze` decimal(32, 8) NULL DEFAULT 0.00000000,
  `deal_num` decimal(32, 8) NULL DEFAULT 0.00000000,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_croatian_ci COMMENT = '法币订单表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_otc_order
-- ----------------------------
INSERT INTO `t_otc_order` VALUES ('17143c2e14c890a60ae40488bf50c885', '448509298ab541aefd3b7628803174a1', '', 'USDT', 5000.00000000, 1.00000000, 35108.10000000, 6.94000000, '', 'BUY', 'ALIAY', 'NORMAL', 'UNOPEN', '直接下单，秒到', 0.00000000, 0.00000000, '2020-09-10 11:07:07', '2020-09-10 11:07:07');
INSERT INTO `t_otc_order` VALUES ('20e6d9c3ae2937bd311c218369266b95', '448509298ab541aefd3b7628803174a1', '', 'USDT', 5000.00000000, 1.00000000, 35108.10000000, 6.95000000, '', 'SELL', 'ALIAY', 'NORMAL', 'UNOPEN', '', 0.00000000, 0.00000000, '2020-09-10 11:08:14', '2020-09-10 11:08:14');

-- ----------------------------
-- Table structure for t_otc_order_appeal
-- ----------------------------
DROP TABLE IF EXISTS `t_otc_order_appeal`;
CREATE TABLE `t_otc_order_appeal`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `member_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `price_order_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `pic` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `pic1` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'Otc申诉表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_otc_order_appeal
-- ----------------------------

-- ----------------------------
-- Table structure for t_otc_order_price
-- ----------------------------
DROP TABLE IF EXISTS `t_otc_order_price`;
CREATE TABLE `t_otc_order_price`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `store_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家id',
  `order_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单id',
  `user_direction` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户买卖方向',
  `store_direction` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家买卖方向',
  `num` decimal(32, 8) NULL DEFAULT NULL COMMENT '下单数量',
  `price` decimal(32, 8) NULL DEFAULT NULL COMMENT '价格',
  `total_price` decimal(32, 8) NULL DEFAULT NULL COMMENT '总价',
  `pay_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `currency` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `appeal_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '申诉状态',
  `appeal_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '下单表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_otc_order_price
-- ----------------------------

-- ----------------------------
-- Table structure for t_otc_type_config
-- ----------------------------
DROP TABLE IF EXISTS `t_otc_type_config`;
CREATE TABLE `t_otc_type_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  `min` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单持续总时长' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_otc_type_config
-- ----------------------------
INSERT INTO `t_otc_type_config` VALUES (1, 'orderTime', '15', '2020-05-15 18:55:50', '2020-05-15 18:55:50');
INSERT INTO `t_otc_type_config` VALUES (2, 'backCount', '3', '2020-03-16 22:28:06', '2020-03-16 22:28:07');
INSERT INTO `t_otc_type_config` VALUES (3, 'minRatio', '30', '2020-03-24 11:58:43', '2020-03-24 11:58:46');
INSERT INTO `t_otc_type_config` VALUES (4, 'maxRatio', '30', '2020-03-24 11:58:55', '2020-03-24 11:58:57');

-- ----------------------------
-- Table structure for t_pairs
-- ----------------------------
DROP TABLE IF EXISTS `t_pairs`;
CREATE TABLE `t_pairs`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pairs_name` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易对名称',
  `main_cur` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主币',
  `token_cur` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代币',
  `state` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '冻结状态',
  `type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型',
  `contract` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目方合约地址',
  `point` int(12) NULL DEFAULT NULL,
  `image` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'logo',
  `sort` int(11) NULL DEFAULT NULL,
  `open_price` decimal(20, 8) NULL DEFAULT NULL COMMENT '开盘价',
  `price` decimal(20, 8) NULL DEFAULT NULL,
  `ch_price` decimal(20, 8) NULL DEFAULT NULL,
  `volume` decimal(20, 8) NULL DEFAULT NULL,
  `open` decimal(20, 8) NULL DEFAULT NULL,
  `low_price` decimal(20, 8) NULL DEFAULT NULL,
  `hig_price` decimal(20, 8) NULL DEFAULT NULL,
  `updown` decimal(20, 8) NULL DEFAULT NULL,
  `is_top` tinyint(1) NULL DEFAULT NULL,
  `trade_max` decimal(10, 0) NULL DEFAULT NULL COMMENT '最大交易数量',
  `trade_min` decimal(10, 0) NULL DEFAULT NULL COMMENT '最小交易数量',
  `trade_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币币：SPOT,合约：CONTRACT',
  `trade_rate` decimal(20, 8) NULL DEFAULT NULL,
  `main_from` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ETH BTC',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `is_dw` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否开启充提币类型',
  `withdraw_min` decimal(50, 25) NULL DEFAULT NULL,
  `withdraw_fee` decimal(50, 25) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_pairs
-- ----------------------------
INSERT INTO `t_pairs` VALUES ('0e33238e9a16497298e2ae9b50197db5', 'EOS/USDT', 'USDT', 'EOS', 'NORMAL', 'MAIN_COIN', NULL, NULL, NULL, 9, 0.00000000, 3.08380000, 22.27860472, 2509184.76398994, 3.08700000, 3.06150000, 3.10390000, -0.00103661, 1, NULL, NULL, 'SPOT,CONTRACT', 0.00300000, NULL, '2020-12-20 06:36:05', '2020-07-30 16:51:16', NULL, NULL, NULL);
INSERT INTO `t_pairs` VALUES ('3a67a6977e1143f5b8dcc9af7b17c9a4', 'BCH/USDT', 'USDT', 'BCH', 'NORMAL', 'MAIN_COIN', NULL, NULL, NULL, 8, 0.00000000, 320.85000000, 2317.94874000, 44889.65340905, 320.13000000, 314.13000000, 323.68000000, 0.00224909, 1, NULL, NULL, 'SPOT,CONTRACT', 0.00300000, NULL, '2020-12-20 06:37:10', '2020-07-30 16:50:40', NULL, NULL, NULL);
INSERT INTO `t_pairs` VALUES ('69ecb9a740a84f49a0d8347a171100e9', 'ETC/USDT', 'USDT', 'ETC', 'NORMAL', 'MAIN_COIN', NULL, NULL, NULL, 6, 0.00000000, 6.45150000, 46.60821660, 349848.52641038, 6.46770000, 6.38400000, 6.49200000, -0.00250475, 1, NULL, NULL, 'SPOT,CONTRACT', 0.00300000, NULL, '2020-12-20 06:36:06', '2020-07-30 18:25:49', NULL, NULL, NULL);
INSERT INTO `t_pairs` VALUES ('7a28a28c0a8d46ec91818c7c6dce9ce7', 'XRP/USDT', 'USDT', 'XRP', 'NORMAL', 'MAIN_COIN', NULL, NULL, NULL, 10, 0.00000000, 0.58871000, 4.25307652, 51252590.47710440, 0.59601000, 0.58180000, 0.60353000, -0.01224812, 1, 10000, 1, 'SPOT,CONTRACT', 0.00300000, NULL, '2020-12-20 06:36:06', '2020-07-29 15:25:43', NULL, NULL, NULL);
INSERT INTO `t_pairs` VALUES ('b173b6f0f350067300e147a70ca7c034', 'BTC/USDT', 'USDT', 'BTC', 'NORMAL', 'MAIN_COIN', '', NULL, '', 12, NULL, 23850.06692831, 172302.42351688, 6112.84028262, 24052.68000000, 23570.00000000, 24100.00000000, -0.00842372, 1, 1000000, 1, 'SPOT,CONTRACT', 0.00300000, 'BTC', '2020-12-20 06:36:53', '2019-12-27 13:25:27', '1', NULL, NULL);
INSERT INTO `t_pairs` VALUES ('b739a726a4a04f8db5bf5ff850a9d161', 'LTC/USDT', 'USDT', 'LTC', 'NORMAL', 'MAIN_COIN', NULL, NULL, NULL, 10, 0.00000000, 121.53000000, 877.98133200, 494396.63465407, 118.19000000, 115.85000000, 124.00000000, 0.02825958, 1, 1000, 1, 'SPOT,CONTRACT', 0.00300000, 'usdt', '2020-12-20 06:36:06', '2020-07-29 15:24:52', NULL, NULL, NULL);
INSERT INTO `t_pairs` VALUES ('d578de6355125c15ff9d60079f72018b', 'ETH/USDT', 'USDT', 'ETH', 'NORMAL', 'MAIN_COIN', '', NULL, '', 11, NULL, 661.96000000, 4782.26382400, 111153.42690143, 659.96000000, 655.46000000, 669.71000000, 0.00303049, 1, 1000000, 1, 'SPOT,CONTRACT', 0.00300000, 'ETH', '2020-12-20 06:36:07', '2019-12-27 13:38:15', '1,2', NULL, NULL);

-- ----------------------------
-- Table structure for t_pe_order
-- ----------------------------
DROP TABLE IF EXISTS `t_pe_order`;
CREATE TABLE `t_pe_order`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `pe_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'pe项目id',
  `member` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '购买份额用户id',
  `pe_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '形目名称',
  `pe_price` decimal(20, 12) NULL DEFAULT NULL COMMENT '单价',
  `pe_num` decimal(20, 12) NULL DEFAULT NULL COMMENT '数量',
  `pe_amount` decimal(20, 12) NULL DEFAULT NULL COMMENT '总额',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_pe_order
-- ----------------------------

-- ----------------------------
-- Table structure for t_pe_project
-- ----------------------------
DROP TABLE IF EXISTS `t_pe_project`;
CREATE TABLE `t_pe_project`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `order_index` int(50) NULL DEFAULT NULL COMMENT '订单编号',
  `project_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目名称',
  `project_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目地址',
  `project_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '大图',
  `start_time` datetime NULL DEFAULT NULL COMMENT '私募开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '私募结束时间',
  `now_num` decimal(20, 12) NULL DEFAULT NULL COMMENT '已经募数量',
  `sum_num` decimal(20, 12) NULL DEFAULT NULL COMMENT '私募数',
  `min_num` decimal(20, 12) NULL DEFAULT NULL COMMENT '最小数量',
  `max_num` decimal(20, 12) NULL DEFAULT NULL COMMENT '最大数量',
  `coin_pice` decimal(20, 12) NULL DEFAULT NULL COMMENT '单价',
  `coin_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '币种名称',
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '私募状态 WAIT排队，ING正在进行，PASS结束',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_pe_project
-- ----------------------------
INSERT INTO `t_pe_project` VALUES ('1', 1, 'BSEX第一期', 'http://zhdwwrl.cn/bsex/bsex.html', 'http://fvex.oss-cn-hangzhou.aliyuncs.com/fvex/1bb3401aba654ec281243f998df2e5a70012.jpg', '2020-08-01 00:00:00', '2020-08-15 00:00:00', 1000000.000000000000, 1000000.000000000000, 1000.000000000000, 2000.000000000000, 0.150000000000, 'BSEX', 'PASS', '2020-07-16 15:47:22', '2020-09-03 15:01:26');
INSERT INTO `t_pe_project` VALUES ('2', 2, 'BSEX第二期', 'http://zhdwwrl.cn/bsex/bsex.html', 'http://fvex.oss-cn-hangzhou.aliyuncs.com/fvex/6015b83c69fc4c34af4c96f110688b0b0012.jpg', '2020-08-12 00:00:00', '2020-08-20 00:00:00', 556235.000000000000, 1000000.000000000000, 10000.000000000000, 5000.000000000000, 0.160000000000, 'BSEX', 'ING', '2020-07-16 15:47:22', '2020-08-19 00:23:57');
INSERT INTO `t_pe_project` VALUES ('3', 3, 'BSEX第三期', 'http://zhdwwrl.cn/bsex/bsex.html', 'http://fvex.oss-cn-hangzhou.aliyuncs.com/fvex/fc10d3b432dc46d7bd228eb3cb21051b0012.jpg', '2020-08-20 00:00:00', '2020-08-25 00:00:00', 0.000000000000, 1000000.000000000000, 100.000000000000, 30000.000000000000, 0.170000000000, 'BSEX', 'WAIT', '2020-07-16 15:47:22', '2020-08-19 00:24:35');
INSERT INTO `t_pe_project` VALUES ('4', 4, 'BSEX第四期', 'http://zhdwwrl.cn/bsex/bsex.html', 'http://fvex.oss-cn-hangzhou.aliyuncs.com/fvex/4f386b45a4bf470c823cb1a6055c4bdf0012.jpg', '2020-08-25 00:00:00', '2020-08-30 00:00:00', 0.000000000000, 1000000.000000000000, 100.000000000000, 30000.000000000000, 0.180000000000, 'BSEX', 'WAIT', '2020-07-16 15:47:22', '2020-08-19 00:24:31');
INSERT INTO `t_pe_project` VALUES ('60971f8f55f64afda89d0822a57f0777', 8, 'BSEX第八期', 'http://zhdwwrl.cn/bsex/bsex.html', 'http://fvex.oss-cn-hangzhou.aliyuncs.com/fvex/ed479e87564c4d6ba70fb3edda1329350012.jpg', '2020-09-15 00:00:00', '2020-09-20 00:00:00', 0.000000000000, 1000000.000000000000, 100.000000000000, 30000.000000000000, 0.220000000000, 'BSEX', 'WAIT', '2020-07-31 17:48:31', '2020-08-19 00:24:45');
INSERT INTO `t_pe_project` VALUES ('786fdaae489045feb24ce076d4a33c5a', 7, '雷达金刚----HGK额度抢购', NULL, 'http://fvex.oss-cn-hangzhou.aliyuncs.com/fvex/4540c37ca39b43339928ced219d88d62234.jpg', '2020-08-19 00:00:00', '2020-08-25 00:00:00', 120000.000000000000, 1000000.000000000000, 300.000000000000, 500.000000000000, 0.150000000000, 'HGK', 'ING', '2020-08-18 20:47:04', '2020-08-19 17:04:34');
INSERT INTO `t_pe_project` VALUES ('976b4479e7ba487291317b2eb0aa85b8', 7, 'BSEX第七期', 'http://zhdwwrl.cn/bsex/bsex.html', 'http://fvex.oss-cn-hangzhou.aliyuncs.com/fvex/d4cf3bfdded34a5f9fd5ce2ca5060bf00012.jpg', '2020-09-10 00:00:00', '2020-09-15 00:00:00', 0.000000000000, 1000000.000000000000, 100.000000000000, 30000.000000000000, 0.210000000000, 'BSEX', 'WAIT', '2020-07-31 17:47:19', '2020-08-19 00:24:54');
INSERT INTO `t_pe_project` VALUES ('bb566eb565ce44738ef2a117f3b38a02', 5, 'BSEX第五期', 'http://zhdwwrl.cn/bsex/bsex.html', 'http://fvex.oss-cn-hangzhou.aliyuncs.com/fvex/a7a152cfab0d4fc98fb24751ea7b124d0012.jpg', '2020-08-30 00:00:00', '2020-09-05 00:00:00', 0.000000000000, 1000000.000000000000, 100.000000000000, 30000.000000000000, 0.190000000000, 'BSEX', 'WAIT', '2020-07-31 17:42:27', '2020-08-19 00:25:05');
INSERT INTO `t_pe_project` VALUES ('f7658d5eb0f14a30afbb54326abc9338', 6, 'BSEX第六期', 'http://zhdwwrl.cn/bsex/bsex.html', 'http://fvex.oss-cn-hangzhou.aliyuncs.com/fvex/2c4460bf376f4385ae9e7e8ca02ffaaa0012.jpg', '2020-09-05 00:00:00', '2020-09-10 00:00:00', 0.000000000000, 1000000.000000000000, 100.000000000000, 30000.000000000000, 0.200000000000, 'BSEX', 'WAIT', '2020-07-31 17:44:04', '2020-08-19 00:25:14');

-- ----------------------------
-- Table structure for t_robot_config
-- ----------------------------
DROP TABLE IF EXISTS `t_robot_config`;
CREATE TABLE `t_robot_config`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pair_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易对名称',
  `open_kine` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否开启k线',
  `is_open` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否开启',
  `open_tape` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否开启盘口',
  `will_price` decimal(32, 8) NULL DEFAULT NULL COMMENT '目标价格',
  `will_time` datetime NULL DEFAULT NULL COMMENT '目标时间',
  `bind_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '绑定用户',
  `start_num` decimal(32, 8) NULL DEFAULT NULL COMMENT '下单数量上限',
  `end_num` decimal(32, 8) NULL DEFAULT NULL COMMENT '下单数量波动下限',
  `start_price` decimal(32, 8) NULL DEFAULT NULL COMMENT '下单价格波动上限',
  `end_price` decimal(32, 8) NULL DEFAULT NULL COMMENT '下单价格波动上限',
  `mend_num` decimal(32, 8) UNSIGNED ZEROFILL NULL DEFAULT NULL,
  `mstart_num` decimal(32, 8) UNSIGNED ZEROFILL NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_robot_config
-- ----------------------------
INSERT INTO `t_robot_config` VALUES ('3', 'BTC/USDT', 'OPEN', 'OPEN', 'OPEN', NULL, '2020-10-29 01:15:00', NULL, 0.00060000, 2.99990000, 0.10000000, 7.00000000, 000000000000000000000002.99990000, 000000000000000000000000.00060000, '2020-08-15 11:15:25');
INSERT INTO `t_robot_config` VALUES ('4', 'ETH/USDT', 'OPEN', 'OPEN', 'OPEN', NULL, '2020-10-24 00:00:00', NULL, 0.03000000, 3.00000000, 0.10010000, 0.30000000, 000000000000000000000003.00000000, 000000000000000000000000.03000000, '2020-12-19 14:27:37');
INSERT INTO `t_robot_config` VALUES ('469b03e402a14c42a8dcced5e8d28d54', 'EOS/USDT', 'OPEN', 'OPEN', 'OPEN', 0.00000000, '2020-10-24 11:25:00', NULL, 10.00000000, 10.00000000, 0.00100000, 0.00300000, 000000000000000000000010.00000000, 000000000000000000000010.00000000, '2020-12-19 14:28:14');
INSERT INTO `t_robot_config` VALUES ('a6d605c3c10e49e9bb520e48a54a3345', 'XRP/USDT', 'OPEN', 'OPEN', 'OPEN', NULL, '2020-10-24 15:30:00', NULL, 10.00000000, 10.00000000, 0.00010000, 0.00020000, 000000000000000000000010.00000000, 000000000000000000000010.00000000, '2020-12-19 14:29:41');
INSERT INTO `t_robot_config` VALUES ('be6bf9aaf54641b5aff8701db88931f1', 'ETC/USDT', 'OPEN', 'OPEN', 'OPEN', 0.00000000, '2020-10-20 15:20:00', NULL, 10.00000000, 10.00000000, 0.00100000, 0.00300000, 000000000000000000000010.00000000, 000000000000000000000010.00000000, '2020-12-19 14:28:50');
INSERT INTO `t_robot_config` VALUES ('cac1bf35075245a19e6ffaf46a00366c', 'BCH/USDT', 'OPEN', 'OPEN', 'OPEN', NULL, '2020-10-22 14:30:00', NULL, 0.03000000, 30.00000000, 0.10010000, 0.30000000, 000000000000000000000030.00000000, 000000000000000000000000.03000000, '2020-08-24 10:31:05');
INSERT INTO `t_robot_config` VALUES ('eda75ff8fc0e40998a768d019bd70ea3', 'LTC/USDT', 'OPEN', 'OPEN', 'OPEN', NULL, '2020-12-17 10:50:00', NULL, 5.00000000, 10.00000000, 0.00100000, 0.02000000, 000000000000000000000010.00000000, 000000000000000000000005.00000000, '2020-12-19 14:30:06');

-- ----------------------------
-- Table structure for t_share_img
-- ----------------------------
DROP TABLE IF EXISTS `t_share_img`;
CREATE TABLE `t_share_img`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `image` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '图片地址',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_share_img
-- ----------------------------
INSERT INTO `t_share_img` VALUES ('ce445ab02fdc4f8789f8f068e1e92ad8', 'http://fvex.oss-cn-hangzhou.aliyuncs.com/fvex/f106bee2b9954c5b99e57a7d776f992c未标题-1.png', 'SHARE_REG', '2020-07-31 12:09:10', '2020-07-31 13:39:37');
INSERT INTO `t_share_img` VALUES ('e1b55603399b4c8caf3713c30d1ae991', 'http://fvex.oss-cn-hangzhou.aliyuncs.com/fvex/c9e0fc75bf924d1ba76beeba3a12b689法币交易购买1@3x.png', 'SHARE_REG', '2020-07-31 12:09:24', NULL);

-- ----------------------------
-- Table structure for t_sys_config
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_config`;
CREATE TABLE `t_sys_config`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `param_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数名字',
  `param_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数AK',
  `param_value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数SK',
  `commit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_config
-- ----------------------------
INSERT INTO `t_sys_config` VALUES ('092e19fae6a64be092a8702bd428f2b6', '阿里云OSS文件夹', 'FOLDER', 'fvex/', '指定上传至OSS文件夹', '2020-07-29 19:41:28', '2020-08-03 21:26:21');
INSERT INTO `t_sys_config` VALUES ('1', '阿里云AK', 'ACCESS_KEY_ID', 'LTAI4FhhjDAFtktG6JsGJbP9', 'AK', '2020-07-29 19:09:05', '2020-07-29 23:03:00');
INSERT INTO `t_sys_config` VALUES ('10', '三方钱包APPID', 'APPID', 'ztpay91xrbde6wsucn', '三方钱包APPID', '2020-07-30 04:34:07', '2020-12-18 13:28:07');
INSERT INTO `t_sys_config` VALUES ('161132e9583f401c84b2d244022fcbde', '阿里云法币提醒短信模板', 'OTCTemplateCode', NULL, '法币提醒短信模板', '2020-07-29 21:16:02', '2020-08-03 21:23:06');
INSERT INTO `t_sys_config` VALUES ('1eb66391eca548dcb31ba88dd1ed0814', '阿里云OSS', 'Bucket', 'fvex', '数据Bucket', '2020-07-29 19:40:51', '2020-07-29 23:03:00');
INSERT INTO `t_sys_config` VALUES ('2', '阿里云SK', 'ACCESS_KEY_SECRET', 'vAAQBBGbBdGoGyP7Fw4cffvkwJDfhO', 'AS', '2020-07-29 19:09:05', '2020-07-29 19:42:31');
INSERT INTO `t_sys_config` VALUES ('3', '阿里云区域', 'REGIONID', 'cn-hangzhou', 'OSS,短信参数', '2020-07-29 19:09:05', '2020-08-03 21:25:43');
INSERT INTO `t_sys_config` VALUES ('3fb354e42ba1422bafa813fd1a2faa4a', '三方钱包API', 'ETH_URL', 'https://sapi.ztpay.org/api/v2', '三方钱包API', '2020-07-30 04:34:07', '2020-12-18 13:28:07');
INSERT INTO `t_sys_config` VALUES ('4', '阿里云用户注册短信模板', 'RegisterTemplateCode', 'SMS_183791148', '用户注册短信模板', '2020-07-29 19:09:05', '2020-08-03 21:18:59');
INSERT INTO `t_sys_config` VALUES ('5', '阿里云注册短信签名', 'RegisterSignName', '块说', '注册短信签名', '2020-07-29 19:09:05', '2020-08-03 21:18:42');
INSERT INTO `t_sys_config` VALUES ('6', '阿里云短信签名', 'NoticeSignName', NULL, '法币交易合约交易提醒短信签名', NULL, '2020-08-03 21:22:50');
INSERT INTO `t_sys_config` VALUES ('679f25007b29494d8b254d4c7337a559', '阿里云OSS接入点', 'ENDPOINT', 'http://oss-cn-hangzhou.aliyuncs.com', '阿里云OSS接入点', '2020-07-29 19:39:21', '2020-07-29 23:03:00');
INSERT INTO `t_sys_config` VALUES ('7', '发件邮箱', 'SEND_MAIL', 'support@fvex.top', '注册发送验证码邮件邮箱', '2020-07-29 23:01:19', '2020-08-03 21:25:07');
INSERT INTO `t_sys_config` VALUES ('76c9e7f846ed40709ce2ba72eb9d33d8', '阿里云合约提醒模板', 'BurstTemplateCode', NULL, '合约交易提醒模板', '2020-07-29 21:16:30', '2020-08-03 21:23:24');
INSERT INTO `t_sys_config` VALUES ('8', '邮件主题', 'SUBJECT', '交易所验证码', '注册发送验证码邮件主题', NULL, '2020-08-03 21:24:05');
INSERT INTO `t_sys_config` VALUES ('9', '三方钱包APPSECRET', 'APPSECRET', 'lQzxwfHBraFumshtQfrN7D6E34sxklpz', '三方钱包APPSECRET', '2020-07-30 04:34:07', '2020-12-18 13:28:07');
INSERT INTO `t_sys_config` VALUES ('ea7eda916d9349d991179129521c0f81', '交易所名称', 'EXCHANGE_NAME', 'BSEX', '注册接受邮件交易所名称', '2020-07-29 23:01:19', '2020-08-03 21:24:37');

-- ----------------------------
-- Table structure for t_transfer_info
-- ----------------------------
DROP TABLE IF EXISTS `t_transfer_info`;
CREATE TABLE `t_transfer_info`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `member` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户id',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '类型',
  `currency` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '币种',
  `fee` decimal(20, 8) NOT NULL DEFAULT 0.00000000 COMMENT '⁮钱',
  `balance` decimal(20, 8) NULL DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_transfer_info
-- ----------------------------
INSERT INTO `t_transfer_info` VALUES ('1591d6b3b7b025fb449aa32a2b475bf6', '6a9d42d3d43a420ef55b17d759cf17b6', 'SQKD_MOVE_FB', 'SQT', 1.00000000, 0.00000000, '2020-07-03 18:28:32', NULL);
INSERT INTO `t_transfer_info` VALUES ('1b8e21134dc59a2caa615403d6660a3b', '3276801b3614610f4a0f06fe9770ef83', 'BALANCE_MOVE_TOKEN', 'USDT', 10000.00000000, 10000.00000000, '2020-08-11 12:12:31', NULL);
INSERT INTO `t_transfer_info` VALUES ('271a7c646a95367c7923455f8532434f', '2d6687063a20035418a46cbbe97f7c00', 'BALANCE_MOVE_ASSETS', 'ETH', 0.30000000, 0.00000000, '2020-07-22 14:05:07', NULL);
INSERT INTO `t_transfer_info` VALUES ('2c8f63d035af86c4816a03d940ae54db', '48022ca8e315578770db8f655798b00e', 'ASSETS_MOVE_BALANCE', 'USDT', 69500.90000000, 0.00000000, '2020-08-23 12:20:41', NULL);
INSERT INTO `t_transfer_info` VALUES ('2e4081ece5ebb1884e0e6f86ee61778a', '6466890c4b7f79efd5adc13b393f69f9', 'SQKD_MOVE_FB', 'SQT', 1.00000000, 0.00000000, '2020-07-08 15:03:31', NULL);
INSERT INTO `t_transfer_info` VALUES ('32256c42bbb7b0028440533c4105125e', '1eddbe64c25d5ad7d0e5b0e0616cf4d4', 'TOKEN_MOVE_BALANCE', 'ETH', 100000000.00000000, 0.00000000, '2020-07-25 14:22:56', NULL);
INSERT INTO `t_transfer_info` VALUES ('3359ba7bd73fd009707efc4ea12abd03', '48022ca8e315578770db8f655798b00e', 'BALANCE_MOVE_ASSETS', 'USDT', 7361.25890000, 0.00000000, '2020-07-30 23:29:37', NULL);
INSERT INTO `t_transfer_info` VALUES ('3c5b630c35c8a4073ec374c43b133de0', '3276801b3614610f4a0f06fe9770ef83', 'TOKEN_MOVE_BALANCE', 'USDT', 10000.00000000, 0.00000000, '2020-08-05 01:06:29', NULL);
INSERT INTO `t_transfer_info` VALUES ('4ec5660bf90175ca19dc1f386f75eaa9', '6466890c4b7f79efd5adc13b393f69f9', 'SQKD_MOVE_FB', 'SQT', 2.00000000, 0.00000000, '2020-07-08 15:10:01', NULL);
INSERT INTO `t_transfer_info` VALUES ('4f099f6863952d144f21bff1c770ab03', '3276801b3614610f4a0f06fe9770ef83', 'BALANCE_MOVE_TOKEN', 'USDT', 10000.00000000, 10000.00000000, '2020-08-05 00:57:55', NULL);
INSERT INTO `t_transfer_info` VALUES ('4fca17f0c05f03bfaf515bf58957cd1f', '2d6687063a20035418a46cbbe97f7c00', 'BALANCE_MOVE_ASSETS', 'ETH', 1.05000000, 0.00000000, '2020-07-22 11:10:13', NULL);
INSERT INTO `t_transfer_info` VALUES ('6509910ba7818b9254753b97b377e386', '6466890c4b7f79efd5adc13b393f69f9', 'SQKD_MOVE_FB', 'SQT', 1.79764000, 0.00000000, '2020-07-11 10:44:59', NULL);
INSERT INTO `t_transfer_info` VALUES ('6d9d307a6917df3c2343d6b7d6a9d1bc', '3276801b3614610f4a0f06fe9770ef83', 'TOKEN_MOVE_FB', 'USDT', 10753.80410000, 0.37271996, '2020-09-10 09:10:23', NULL);
INSERT INTO `t_transfer_info` VALUES ('796d66195063dfabe1fb4c3c70d53013', '78bb8152cf0fbb5ad3e314a48f0b8f24', 'ASSETS_MOVE_BALANCE', 'USDT', 300.00000000, 0.00000000, '2020-08-07 11:30:19', NULL);
INSERT INTO `t_transfer_info` VALUES ('7b84839c89b1bd6190d8143281902d96', '91947f2536fa87358f38768bd92f7329', 'BALANCE_MOVE_TOKEN', 'ETC', 4000.00000000, 4000.00000000, '2020-12-19 17:22:30', NULL);
INSERT INTO `t_transfer_info` VALUES ('90bf6b64e1eac771cf632145b4e0d10d', '2d6687063a20035418a46cbbe97f7c00', 'BALANCE_MOVE_ASSETS', 'ETH', 0.60000000, 0.00000000, '2020-07-22 14:16:27', NULL);
INSERT INTO `t_transfer_info` VALUES ('9894890b3c5c83c10eeabcf280dca5d9', '1eddbe64c25d5ad7d0e5b0e0616cf4d4', 'ASSETS_MOVE_BALANCE', 'ETH', 10000.00000000, 100000000.00000000, '2020-07-25 14:22:06', NULL);
INSERT INTO `t_transfer_info` VALUES ('ae40e8b760d7c41e5b99f1ad98e12dca', '48022ca8e315578770db8f655798b00e', 'ASSETS_MOVE_TOKEN', 'USDT', 10000.00000000, 10000.00000000, '2020-08-19 01:28:16', NULL);
INSERT INTO `t_transfer_info` VALUES ('afd52b366c4efa91b39ba769d9ab86fd', '1eddbe64c25d5ad7d0e5b0e0616cf4d4', 'BALANCE_MOVE_TOKEN', 'ETH', 100.00000000, 100.00000000, '2020-07-25 14:23:27', NULL);
INSERT INTO `t_transfer_info` VALUES ('beb002aceb789e5053d3ae14701e9c4e', '1eddbe64c25d5ad7d0e5b0e0616cf4d4', 'FB_MOVE_BALANCE', 'SQT', 10000.00000000, 100000000.00000000, '2020-07-25 11:18:31', NULL);
INSERT INTO `t_transfer_info` VALUES ('c0cb16c6ccdc6d071aaabb8c6f8d523e', '2d6687063a20035418a46cbbe97f7c00', 'ASSETS_MOVE_BALANCE', 'ETH', 0.30000000, 0.00000000, '2020-07-22 14:16:10', NULL);
INSERT INTO `t_transfer_info` VALUES ('c4425e10cd1f74a6702ff51916ba9a60', '48022ca8e315578770db8f655798b00e', 'ASSETS_MOVE_BALANCE', 'USDT', 10000.00000000, 0.00000000, '2020-08-19 01:27:18', NULL);
INSERT INTO `t_transfer_info` VALUES ('c794e8cf0dba1e0e3dbcb3146cf18f33', '48022ca8e315578770db8f655798b00e', 'BALANCE_MOVE_TOKEN', 'USDT', 79481.75760000, 79481.75760000, '2020-08-23 12:21:49', NULL);
INSERT INTO `t_transfer_info` VALUES ('c8bb4d4e67963df2709fcb99d42dad9e', '78bb8152cf0fbb5ad3e314a48f0b8f24', 'BALANCE_MOVE_ASSETS', 'USDT', 300.00000000, 0.00000000, '2020-08-07 11:31:17', NULL);
INSERT INTO `t_transfer_info` VALUES ('e1a6fa3a98dfec23bd203fc870a88427', '6466890c4b7f79efd5adc13b393f69f9', 'SQKD_MOVE_FB', 'SQT', 50.62328000, 0.00000000, '2020-07-03 18:12:40', NULL);
INSERT INTO `t_transfer_info` VALUES ('f1fa792c5d4640c0368cae794060fff4', '1eddbe64c25d5ad7d0e5b0e0616cf4d4', 'FB_MOVE_BALANCE', 'SQT', 10000.00000000, 100000000.00000000, '2020-07-25 11:18:18', NULL);
INSERT INTO `t_transfer_info` VALUES ('f961037096c53032bcd94b5385bd41ad', '2d6687063a20035418a46cbbe97f7c00', 'ASSETS_MOVE_BALANCE', 'ETH', 0.60000000, 0.00000000, '2020-07-22 17:27:23', NULL);
INSERT INTO `t_transfer_info` VALUES ('fc3d9d7b8c7e65e9f8334d2f5042fecc', '1eddbe64c25d5ad7d0e5b0e0616cf4d4', 'FB_MOVE_BALANCE', 'SQT', 10000.00000000, 100000000.00000000, '2020-07-25 11:18:15', NULL);

-- ----------------------------
-- Table structure for t_user_blance_collect
-- ----------------------------
DROP TABLE IF EXISTS `t_user_blance_collect`;
CREATE TABLE `t_user_blance_collect`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `member` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `currency` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币种',
  `sum_chong` decimal(32, 8) NULL DEFAULT NULL COMMENT '总充值',
  `sum_ti` decimal(32, 8) NULL DEFAULT NULL COMMENT '总提币',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户财务分析' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_user_blance_collect
-- ----------------------------
INSERT INTO `t_user_blance_collect` VALUES (1, '3b8f80497d7b71938770b1bf00b64a58', 'EOS', 0.00000000, 0.00000000, '2020-12-20 03:00:07');
INSERT INTO `t_user_blance_collect` VALUES (2, '404208357cb9d619891c7d3b3dff1dae', 'EOS', 0.00000000, 0.00000000, '2020-12-20 03:00:07');
INSERT INTO `t_user_blance_collect` VALUES (3, '448509298ab541aefd3b7628803174a1', 'EOS', 0.00000000, 0.00000000, '2020-12-20 03:00:08');
INSERT INTO `t_user_blance_collect` VALUES (4, '614d6a5e93f01d1e56c6e5db05d7f943', 'EOS', 0.00000000, 0.00000000, '2020-12-20 03:00:09');
INSERT INTO `t_user_blance_collect` VALUES (5, '91947f2536fa87358f38768bd92f7329', 'EOS', 0.00000000, 0.00000000, '2020-12-20 03:00:10');
INSERT INTO `t_user_blance_collect` VALUES (6, '3b8f80497d7b71938770b1bf00b64a58', 'BCH', 0.00000000, 0.00000000, '2020-12-20 03:00:12');
INSERT INTO `t_user_blance_collect` VALUES (7, '404208357cb9d619891c7d3b3dff1dae', 'BCH', 0.00000000, 0.00000000, '2020-12-20 03:00:13');
INSERT INTO `t_user_blance_collect` VALUES (8, '448509298ab541aefd3b7628803174a1', 'BCH', 0.00000000, 0.00000000, '2020-12-20 03:00:14');
INSERT INTO `t_user_blance_collect` VALUES (9, '614d6a5e93f01d1e56c6e5db05d7f943', 'BCH', 0.00000000, 0.00000000, '2020-12-20 03:00:14');
INSERT INTO `t_user_blance_collect` VALUES (10, '91947f2536fa87358f38768bd92f7329', 'BCH', 0.00000000, 0.00000000, '2020-12-20 03:00:15');
INSERT INTO `t_user_blance_collect` VALUES (11, '3b8f80497d7b71938770b1bf00b64a58', 'ETC', 0.00000000, 0.00000000, '2020-12-20 03:00:17');
INSERT INTO `t_user_blance_collect` VALUES (12, '404208357cb9d619891c7d3b3dff1dae', 'ETC', 0.00000000, 0.00000000, '2020-12-20 03:00:18');
INSERT INTO `t_user_blance_collect` VALUES (13, '448509298ab541aefd3b7628803174a1', 'ETC', 0.00000000, 0.00000000, '2020-12-20 03:00:19');
INSERT INTO `t_user_blance_collect` VALUES (14, '614d6a5e93f01d1e56c6e5db05d7f943', 'ETC', 0.00000000, 0.00000000, '2020-12-20 03:00:19');
INSERT INTO `t_user_blance_collect` VALUES (15, '91947f2536fa87358f38768bd92f7329', 'ETC', 0.00000000, 0.00000000, '2020-12-20 03:00:21');
INSERT INTO `t_user_blance_collect` VALUES (16, '3b8f80497d7b71938770b1bf00b64a58', 'XRP', 0.00000000, 0.00000000, '2020-12-20 03:00:23');
INSERT INTO `t_user_blance_collect` VALUES (17, '404208357cb9d619891c7d3b3dff1dae', 'XRP', 0.00000000, 0.00000000, '2020-12-20 03:00:25');
INSERT INTO `t_user_blance_collect` VALUES (18, '448509298ab541aefd3b7628803174a1', 'XRP', 0.00000000, 0.00000000, '2020-12-20 03:00:26');
INSERT INTO `t_user_blance_collect` VALUES (19, '614d6a5e93f01d1e56c6e5db05d7f943', 'XRP', 0.00000000, 0.00000000, '2020-12-20 03:00:27');
INSERT INTO `t_user_blance_collect` VALUES (20, '91947f2536fa87358f38768bd92f7329', 'XRP', 0.00000000, 0.00000000, '2020-12-20 03:00:28');
INSERT INTO `t_user_blance_collect` VALUES (21, '3b8f80497d7b71938770b1bf00b64a58', 'BTC', 0.00000000, 0.00000000, '2020-12-20 03:00:29');
INSERT INTO `t_user_blance_collect` VALUES (22, '404208357cb9d619891c7d3b3dff1dae', 'BTC', 0.00000000, 0.00000000, '2020-12-20 03:00:30');
INSERT INTO `t_user_blance_collect` VALUES (23, '448509298ab541aefd3b7628803174a1', 'BTC', 0.00000000, 0.00000000, '2020-12-20 03:00:31');
INSERT INTO `t_user_blance_collect` VALUES (24, '614d6a5e93f01d1e56c6e5db05d7f943', 'BTC', 0.00000000, 0.00000000, '2020-12-20 03:00:34');
INSERT INTO `t_user_blance_collect` VALUES (25, '91947f2536fa87358f38768bd92f7329', 'BTC', 3.00000000, 0.00000000, '2020-12-20 03:00:35');
INSERT INTO `t_user_blance_collect` VALUES (26, '3b8f80497d7b71938770b1bf00b64a58', 'LTC', 0.00000000, 0.00000000, '2020-12-20 03:00:36');
INSERT INTO `t_user_blance_collect` VALUES (27, '404208357cb9d619891c7d3b3dff1dae', 'LTC', 0.00000000, 0.00000000, '2020-12-20 03:00:37');
INSERT INTO `t_user_blance_collect` VALUES (28, '448509298ab541aefd3b7628803174a1', 'LTC', 0.00000000, 0.00000000, '2020-12-20 03:00:38');
INSERT INTO `t_user_blance_collect` VALUES (29, '614d6a5e93f01d1e56c6e5db05d7f943', 'LTC', 0.00000000, 0.00000000, '2020-12-20 03:00:39');
INSERT INTO `t_user_blance_collect` VALUES (30, '91947f2536fa87358f38768bd92f7329', 'LTC', 0.00000000, 0.00000000, '2020-12-20 03:00:40');
INSERT INTO `t_user_blance_collect` VALUES (31, '3b8f80497d7b71938770b1bf00b64a58', 'ETH', 0.00000000, 0.00000000, '2020-12-20 03:00:43');
INSERT INTO `t_user_blance_collect` VALUES (32, '404208357cb9d619891c7d3b3dff1dae', 'ETH', 0.00000000, 0.00000000, '2020-12-20 03:00:45');
INSERT INTO `t_user_blance_collect` VALUES (33, '448509298ab541aefd3b7628803174a1', 'ETH', 0.00000000, 0.00000000, '2020-12-20 03:00:47');
INSERT INTO `t_user_blance_collect` VALUES (34, '614d6a5e93f01d1e56c6e5db05d7f943', 'ETH', 0.00000000, 0.00000000, '2020-12-20 03:00:48');
INSERT INTO `t_user_blance_collect` VALUES (35, '91947f2536fa87358f38768bd92f7329', 'ETH', 0.00000000, 0.00000000, '2020-12-20 03:00:49');
INSERT INTO `t_user_blance_collect` VALUES (36, '3b8f80497d7b71938770b1bf00b64a58', 'USDT', 0.00000000, 0.00000000, '2020-12-20 03:00:52');
INSERT INTO `t_user_blance_collect` VALUES (37, '404208357cb9d619891c7d3b3dff1dae', 'USDT', 0.00000000, 0.00000000, '2020-12-20 03:00:53');
INSERT INTO `t_user_blance_collect` VALUES (38, '448509298ab541aefd3b7628803174a1', 'USDT', 0.00000000, 0.00000000, '2020-12-20 03:00:54');
INSERT INTO `t_user_blance_collect` VALUES (39, '614d6a5e93f01d1e56c6e5db05d7f943', 'USDT', 0.00000000, 0.00000000, '2020-12-20 03:00:54');
INSERT INTO `t_user_blance_collect` VALUES (40, '91947f2536fa87358f38768bd92f7329', 'USDT', 0.00000000, 0.00000000, '2020-12-20 03:00:55');
INSERT INTO `t_user_blance_collect` VALUES (41, 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'EOS', 0.00000000, 0.00000000, '2020-12-20 03:00:11');
INSERT INTO `t_user_blance_collect` VALUES (42, 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'BCH', 0.00000000, 0.00000000, '2020-12-20 03:00:17');
INSERT INTO `t_user_blance_collect` VALUES (43, 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'ETC', 0.00000000, 0.00000000, '2020-12-20 03:00:22');
INSERT INTO `t_user_blance_collect` VALUES (44, 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'XRP', 0.00000000, 0.00000000, '2020-12-20 03:00:28');
INSERT INTO `t_user_blance_collect` VALUES (45, 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'BTC', 0.00000000, 0.00000000, '2020-12-20 03:00:36');
INSERT INTO `t_user_blance_collect` VALUES (46, 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'LTC', 0.00000000, 0.00000000, '2020-12-20 03:00:41');
INSERT INTO `t_user_blance_collect` VALUES (47, 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'ETH', 0.00000000, 0.00000000, '2020-12-20 03:00:51');
INSERT INTO `t_user_blance_collect` VALUES (48, 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'USDT', 0.00000000, 0.00000000, '2020-12-20 03:01:01');

-- ----------------------------
-- Table structure for t_user_use_logs
-- ----------------------------
DROP TABLE IF EXISTS `t_user_use_logs`;
CREATE TABLE `t_user_use_logs`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `method_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '方法名',
  `method_parm` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '方法参数',
  `method_body` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '操作接口日志表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_user_use_logs
-- ----------------------------
INSERT INTO `t_user_use_logs` VALUES (1, '91947f2536fa87358f38768bd92f7329', 'BalanceController.transferBalances', 'member:91947f2536fa87358f38768bd92f7329,balance:4000,currency:ETC,moveType:BALANCE_MOVE_TOKEN,', '', '2020-12-19 17:22:36');

-- ----------------------------
-- Table structure for t_wallet_pool
-- ----------------------------
DROP TABLE IF EXISTS `t_wallet_pool`;
CREATE TABLE `t_wallet_pool`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `member` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `coin` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `usdt_balance` decimal(11, 0) NULL DEFAULT NULL,
  `eth_balance` decimal(11, 0) NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_wallet_pool
-- ----------------------------
INSERT INTO `t_wallet_pool` VALUES ('09c39177-1903-4433-ab84-a9244133f6b0', '0xf7a6eba883914a7fc0fee0502990f48437c33634', NULL, '404208357cb9d619891c7d3b3dff1dae', NULL, NULL, NULL, '2020-09-08 11:24:41');
INSERT INTO `t_wallet_pool` VALUES ('3ae38447-d0cd-477d-bd79-226405c02c8f', '0x7dda533ae01392dd7d8b88bb8ab0b952f9542493', NULL, '91947f2536fa87358f38768bd92f7329', NULL, NULL, NULL, '2020-12-16 14:56:09');
INSERT INTO `t_wallet_pool` VALUES ('478e4210-cc4e-438d-9e88-698f969a9301', '1EPUFGRCzrMiKs5FrE3uWWeLwQ5Jr1nR1n', NULL, 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'BTC', NULL, NULL, '2020-12-19 13:36:10');
INSERT INTO `t_wallet_pool` VALUES ('5da4a50d-7e88-4b0a-a18e-79d11ac717c6', '0xd94e56a92760b45c9fbed636627a7d2e193b4e82', NULL, '614d6a5e93f01d1e56c6e5db05d7f943', NULL, NULL, NULL, '2020-09-10 15:56:01');
INSERT INTO `t_wallet_pool` VALUES ('762df711-676f-4f94-bf7a-498eba62eaec', '0xbe272d01b27537e1cb07da3e6e41c79b28bfb38b', NULL, '448509298ab541aefd3b7628803174a1', NULL, NULL, NULL, '2020-09-08 14:32:31');
INSERT INTO `t_wallet_pool` VALUES ('91439193-a977-4d49-8658-a4b6cbb3683a', '1CvDXnpuZH61LiMcwefUfpyUMt5uz8Y8Ja', NULL, 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'USDT', NULL, NULL, '2020-12-19 13:36:15');
INSERT INTO `t_wallet_pool` VALUES ('b0d78b29-56dd-40ab-8cd6-c6d2315aad5a', '0x0eaf18fa2f4b0b7f5299994777136300ba2d68ae', NULL, 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'ETH', NULL, NULL, '2020-12-19 13:36:14');
INSERT INTO `t_wallet_pool` VALUES ('d858a0d9-c8cd-424f-937a-78eab4cf22f9', '0x95f43888c76a6d6e1f729296dabf695fa263c112', NULL, '3b8f80497d7b71938770b1bf00b64a58', NULL, NULL, NULL, '2020-09-07 09:26:18');
INSERT INTO `t_wallet_pool` VALUES ('d8b3b1b8-dd51-4826-a4bd-bd020d7f0611', 'MDXn2cM5RPHeApM96jtFpKpfbmWu5ATnom', NULL, 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'LTC', NULL, NULL, '2020-12-19 13:36:16');

-- ----------------------------
-- Table structure for t_warehouse
-- ----------------------------
DROP TABLE IF EXISTS `t_warehouse`;
CREATE TABLE `t_warehouse`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pairs_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `coin_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `main_cur` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `margin` decimal(30, 8) NULL DEFAULT NULL,
  `un_profit_loss` decimal(30, 8) NULL DEFAULT NULL,
  `profit` decimal(30, 8) NULL DEFAULT NULL,
  `profit_up` decimal(30, 8) NULL DEFAULT NULL,
  `token_price` decimal(30, 8) NULL DEFAULT NULL,
  `token_num` decimal(30, 8) NULL DEFAULT NULL,
  `is_token_num` decimal(30, 8) NULL DEFAULT NULL,
  `ave_price` decimal(30, 8) NULL DEFAULT NULL,
  `close_price` decimal(30, 8) NULL DEFAULT NULL,
  `trigger_price` decimal(30, 8) NULL DEFAULT NULL,
  `ord_price` decimal(30, 8) NULL DEFAULT NULL,
  `member` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `trade_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `avg_level` decimal(32, 8) NULL DEFAULT NULL,
  `hands` decimal(32, 0) NULL DEFAULT NULL,
  `orders` decimal(32, 0) NULL DEFAULT NULL,
  `force_price` decimal(32, 8) NULL DEFAULT NULL,
  `state` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `hash_tree_pairs_name`(`pairs_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_warehouse
-- ----------------------------
INSERT INTO `t_warehouse` VALUES ('3202a567ccdc707889821c3961f20a1e', 'ETH/USDT', 'ETH', 'USDT', 8002.10460000, -107379.83000000, 0.00159202, -13.41894856, 120031.56900000, 2.00000000, 16.00000000, 282.66000000, 282.57000000, NULL, NULL, '1eddbe64c25d5ad7d0e5b0e0616cf4d4', 'OPEN_DOWN', 10.00000000, 5662, 2, NULL, 'CREATE', '2020-07-25 18:24:58', '2020-12-20 06:36:10');
INSERT INTO `t_warehouse` VALUES ('43e66483f8b78a637525080780d87d40', 'LTC/USDT', 'LTC', 'USDT', 2726.16480000, 2936.08800000, 0.00000000, 1.07700312, 81784.94400000, 2.00000000, 2.00000000, 115.32000000, 115.32000000, NULL, NULL, 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'OPEN_UP', 20.00000000, 2364, 2, NULL, 'CREATE', '2020-12-19 23:48:34', '2020-12-20 06:36:10');
INSERT INTO `t_warehouse` VALUES ('6a33f122279aff4a4c25a61bddc3ea96', 'LTC/USDT', 'LTC', 'USDT', 2726.16480000, 2936.08800000, 0.00000000, 1.07700312, 81784.94400000, 2.00000000, 2.00000000, 115.32000000, 115.32000000, NULL, NULL, 'fc6e5e3ae9e8c0f05efd85501edcb7ce', 'OPEN_UP', 20.00000000, 2364, 2, NULL, 'CREATE', '2020-12-19 23:48:34', '2020-12-20 06:36:10');
INSERT INTO `t_warehouse` VALUES ('9e1a11d4943be3ab4e4b63572deee508', 'EOS/USDT', 'EOS', 'USDT', 4805.83169259, -48128.17276528, -0.01450295, -10.01453563, 574538.69545536, 5.00000000, 107.00000000, 2.80308414, 2.80511679, NULL, NULL, '448509298ab541aefd3b7628803174a1', 'OPEN_DOWN', 100.00000000, 85724, 5, NULL, 'CREATE', '2020-09-10 14:23:38', '2020-12-20 06:36:07');

-- ----------------------------
-- Table structure for t_wel_user_record
-- ----------------------------
DROP TABLE IF EXISTS `t_wel_user_record`;
CREATE TABLE `t_wel_user_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `member` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `num` decimal(10, 0) NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of t_wel_user_record
-- ----------------------------

-- ----------------------------
-- Table structure for t_withdraw_history
-- ----------------------------
DROP TABLE IF EXISTS `t_withdraw_history`;
CREATE TABLE `t_withdraw_history`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `member` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提币用户ID',
  `block_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易区块高度',
  `tx_hash` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易hash',
  `contract` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合约地址',
  `from_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '转出地址',
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
  `to_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收币地址',
  `coin` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币名',
  `amount` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数量',
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_withdraw_history
-- ----------------------------

-- ----------------------------
-- Triggers structure for table t_brokerage_record
-- ----------------------------
DROP TRIGGER IF EXISTS `tri_update_t_brokerage_record_status`;
delimiter ;;
CREATE TRIGGER `tri_update_t_brokerage_record_status` AFTER UPDATE ON `t_brokerage_record` FOR EACH ROW BEGIN
IF (new.status = '1') THEN
UPDATE t_member SET t_member.brokerage=(t_member.brokerage + new.brokerage_number) WHERE t_member.id=old.brokerage_user_id;
UPDATE t_balance SET t_balance.token_balance=(t_balance.token_balance + new.brokerage_number) WHERE t_balance.user_id=old.brokerage_user_id AND t_balance.currency = 'USDT';
END IF;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table t_pairs
-- ----------------------------
DROP TRIGGER IF EXISTS `tri_insert_t_pairs`;
delimiter ;;
CREATE TRIGGER `tri_insert_t_pairs` AFTER INSERT ON `t_pairs` FOR EACH ROW BEGIN
    INSERT IGNORE  INTO t_balance(id,user_id,currency)
    SELECT MD5(UUID()),user_id,new.token_cur FROM (SELECT DISTINCT user_id FROM t_balance)tmp;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table t_pairs
-- ----------------------------
DROP TRIGGER IF EXISTS `tri_delete_t_pairs`;
delimiter ;;
CREATE TRIGGER `tri_delete_t_pairs` AFTER DELETE ON `t_pairs` FOR EACH ROW BEGIN
 DELETE FROM t_balance WHERE currency = old.token_cur;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
