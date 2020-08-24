/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.1.120
 Source Server Type    : MySQL
 Source Server Version : 100207
 Source Host           : 192.168.1.120:3306
 Source Schema         : bootplus

 Target Server Type    : MySQL
 Target Server Version : 100207
 File Encoding         : 65001

 Date: 14/06/2018 17:34:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '类型：目录、菜单、按钮',
  `icon` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '系统管理', '', '', 'CATALOG', 'fa fa-cog', 0);
INSERT INTO `sys_menu` VALUES (2, 1, '管理员列表', 'admin/sys/admin.html', '', 'MENU', 'fa fa-user', 1);
INSERT INTO `sys_menu` VALUES (3, 1, '角色管理', 'admin/sys/role.html', NULL, 'MENU', 'fa fa-user-secret', 2);
INSERT INTO `sys_menu` VALUES (4, 1, '菜单管理', 'admin/sys/menu.html', NULL, 'MENU', 'fa fa-th-list', 3);
INSERT INTO `sys_menu` VALUES (5, 2, '查看', NULL, 'sys:user:list,sys:user:info', 'BUTTON', NULL, 0);
INSERT INTO `sys_menu` VALUES (6, 2, '新增', '', 'sys:user:save,sys:role:select', 'BUTTON', '', 0);
INSERT INTO `sys_menu` VALUES (7, 2, '修改', '', 'sys:user:update,sys:role:select', 'BUTTON', '', 0);
INSERT INTO `sys_menu` VALUES (8, 2, '删除', NULL, 'sys:user:delete', 'BUTTON', NULL, 0);
INSERT INTO `sys_menu` VALUES (9, 3, '查看', NULL, 'sys:role:list,sys:role:info', 'BUTTON', NULL, 0);
INSERT INTO `sys_menu` VALUES (10, 3, '新增', NULL, 'sys:role:save,sys:menu:perms', 'BUTTON', NULL, 0);
INSERT INTO `sys_menu` VALUES (11, 3, '修改', NULL, 'sys:role:update,sys:menu:perms', 'BUTTON', NULL, 0);
INSERT INTO `sys_menu` VALUES (12, 3, '删除', NULL, 'sys:role:delete', 'BUTTON', NULL, 0);
INSERT INTO `sys_menu` VALUES (13, 4, '查看', NULL, 'sys:menu:list,sys:menu:info', 'BUTTON', NULL, 0);
INSERT INTO `sys_menu` VALUES (14, 4, '新增', NULL, 'sys:menu:save,sys:menu:select', 'BUTTON', NULL, 0);
INSERT INTO `sys_menu` VALUES (15, 4, '修改', NULL, 'sys:menu:update,sys:menu:select', 'BUTTON', NULL, 0);
INSERT INTO `sys_menu` VALUES (16, 4, '删除', NULL, 'sys:menu:delete', 'BUTTON', NULL, 0);
INSERT INTO `sys_menu` VALUES (17, 1, '监控管理', 'druid/index.html', 'druid:druid:manager', 'MENU', 'fa fa-cogs', 3);
INSERT INTO `sys_menu` VALUES (18, 1, '接口文档', 'apidoc/index.html', '', 'MENU', 'fa fa-chrome', 0);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `create_time` int(11) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (13, 'admin', 'admina', 1498801511);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 251 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色与菜单对应关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (241, 13, 1);
INSERT INTO `sys_role_menu` VALUES (242, 13, 2);
INSERT INTO `sys_role_menu` VALUES (243, 13, 5);
INSERT INTO `sys_role_menu` VALUES (244, 13, 6);
INSERT INTO `sys_role_menu` VALUES (245, 13, 3);
INSERT INTO `sys_role_menu` VALUES (246, 13, 9);
INSERT INTO `sys_role_menu` VALUES (247, 13, 4);
INSERT INTO `sys_role_menu` VALUES (248, 13, 13);
INSERT INTO `sys_role_menu` VALUES (249, 13, 17);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '密码',
  `sex` tinyint(1) DEFAULT 0 COMMENT '性别 0=保密/1=男/2=女',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手机号',
  `last_login_time` int(11) DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '最后登录IP',
  `avatar_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '头像缩略图地址',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态 0：禁用、1：正常',
  `create_time` int(11) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 1, '2434387555@qq.com', '13647910412', 1528967776, '0:0:0:0:0:0:0:1', 'upload/adminAvatar/201707/1499675749475head.jpg', 1, 1498801511);
INSERT INTO `sys_user` VALUES (7, 'lastone', 'd50b4c0cab140a3310e256d86fd3cd33c02f145635df4694e71df062c1679a8f', 2, '2434387555@qq.com', '17180190232', 1499069279, '192.168.1.88', 'upload/adminAvatar/201707/1499675749475head.jpg', 1, 1499069190);
INSERT INTO `sys_user` VALUES (8, 'joey', 'bfef4adc39f01b033fe749bb5f28f10b581fef319d34445d21a7bc63fe732fa3', 2, '2434387555@qq.com', '15870631411', 1499670576, '192.168.1.88', 'upload/adminAvatar/201707/1499675749475head.jpg', 0, 1499410874);

-- ----------------------------
-- Table structure for sys_user_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_login_log`;
CREATE TABLE `sys_user_login_log`  (
  `log_id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '登录日志ID',
  `login_time` int(11) DEFAULT NULL COMMENT '登录时间',
  `login_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '登录IP',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `operating_system` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '操作系统',
  `browser` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '浏览器',
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 409 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户登录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_login_log
-- ----------------------------
INSERT INTO `sys_user_login_log` VALUES (402, 1528708116, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_81', 'CHROME');
INSERT INTO `sys_user_login_log` VALUES (403, 1528960179, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_81', 'CHROME');
INSERT INTO `sys_user_login_log` VALUES (404, 1528961880, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_81', 'CHROME');
INSERT INTO `sys_user_login_log` VALUES (405, 1528964863, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_81', 'CHROME');
INSERT INTO `sys_user_login_log` VALUES (406, 1528965837, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_81', 'CHROME');
INSERT INTO `sys_user_login_log` VALUES (407, 1528967698, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_81', 'CHROME');
INSERT INTO `sys_user_login_log` VALUES (408, 1528967776, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_81', 'CHROME');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 74 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户与角色对应关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (62, 1, 13);
INSERT INTO `sys_user_role` VALUES (72, 8, 13);
INSERT INTO `sys_user_role` VALUES (73, 7, 13);

SET FOREIGN_KEY_CHECKS = 1;

UPDATE sys_menu set type = 'CATALOG' WHERE type = '0';
UPDATE sys_menu set type = 'MENU' WHERE type = '1';
UPDATE sys_menu set type = 'BUTTON' WHERE type = '2';

-- ----------------------------
-- Table structure for sys_task
-- ----------------------------
DROP TABLE IF EXISTS `sys_task`;
CREATE TABLE `sys_task` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `cron_expression` varchar(50) DEFAULT NULL COMMENT '执行时间',
  `job_name` varchar(20) DEFAULT NULL COMMENT '任务名称',
  `biz_module` varchar(20) DEFAULT NULL COMMENT '业务模块',
  `biz_id` varchar(30) DEFAULT NULL COMMENT '业务id',
  `biz_tag` varchar(30) DEFAULT NULL COMMENT '业务标识',
  `callback_data` varchar(500) DEFAULT NULL COMMENT '回调内容',
  `callback_type` varchar(10) DEFAULT NULL COMMENT '回调类型(PRINT/CLASS/HTTP/HESSIAN)',
  `callback_url` varchar(200) DEFAULT NULL COMMENT '回调地址',
  `enabled` tinyint(1) NOT NULL COMMENT '是否有效',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='平台_定时任务';
