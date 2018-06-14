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
  `type` int(11) DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '系统管理', '', '', 0, 'fa fa-cog', 0);
INSERT INTO `sys_menu` VALUES (2, 1, '管理员列表', 'admin/sys/admin.html', '', 1, 'fa fa-user', 1);
INSERT INTO `sys_menu` VALUES (3, 1, '角色管理', 'admin/sys/role.html', NULL, 1, 'fa fa-user-secret', 2);
INSERT INTO `sys_menu` VALUES (4, 1, '菜单管理', 'admin/sys/menu.html', NULL, 1, 'fa fa-th-list', 3);
INSERT INTO `sys_menu` VALUES (5, 2, '查看', NULL, 'sys:user:list,sys:user:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (6, 2, '新增', '', 'sys:user:save,sys:role:select', 2, '', 0);
INSERT INTO `sys_menu` VALUES (7, 2, '修改', '', 'sys:user:update,sys:role:select', 2, '', 0);
INSERT INTO `sys_menu` VALUES (8, 2, '删除', NULL, 'sys:user:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (9, 3, '查看', NULL, 'sys:role:list,sys:role:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (10, 3, '新增', NULL, 'sys:role:save,sys:menu:perms', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (11, 3, '修改', NULL, 'sys:role:update,sys:menu:perms', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (12, 3, '删除', NULL, 'sys:role:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (13, 4, '查看', NULL, 'sys:menu:list,sys:menu:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (14, 4, '新增', NULL, 'sys:menu:save,sys:menu:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (15, 4, '修改', NULL, 'sys:menu:update,sys:menu:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (16, 4, '删除', NULL, 'sys:menu:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (17, 1, '监控管理', 'druid/index.html', 'druid:druid:manager', 1, 'fa fa-cogs', 3);

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
  `status` tinyint(4) DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `create_time` int(11) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 1, '2434387555@qq.com', '13647910412', 1528967776, '0:0:0:0:0:0:0:1', 'upload/adminAvatar/201707/1499675749475head.jpg', 1, 1498801511);
INSERT INTO `sys_user` VALUES (7, 'lastone', 'd50b4c0cab140a3310e256d86fd3cd33c02f145635df4694e71df062c1679a8f', 2, 'asd@qq.com', '13456465465', 1499069279, '192.168.1.88', 'upload/adminAvatar/201707/1499675749475head.jpg', 1, 1499069190);
INSERT INTO `sys_user` VALUES (8, 'joey', 'bfef4adc39f01b033fe749bb5f28f10b581fef319d34445d21a7bc63fe732fa3', 2, '2434387555@qq.com', '13647910412', 1499670576, '192.168.1.88', 'upload/adminAvatar/201707/1499675749475head.jpg', 0, 1499410874);

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
INSERT INTO `sys_user_login_log` VALUES (144, 1498643487, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (145, 1498643522, '192.168.1.13', 1, 'WINDOWS_7', 'CHROME');
INSERT INTO `sys_user_login_log` VALUES (146, 1498644136, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (147, 1498696770, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (148, 1498696935, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (149, 1498697948, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (150, 1498698072, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (151, 1498698196, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (152, 1498699082, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (153, 1498699432, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (154, 1498699706, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (155, 1498699646, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (156, 1498699799, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (157, 1498700536, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (158, 1498703621, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (159, 1498703553, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (160, 1498703797, '192.168.1.26', 1, 'WINDOWS_7', 'CHROME39');
INSERT INTO `sys_user_login_log` VALUES (161, 1498703941, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME');
INSERT INTO `sys_user_login_log` VALUES (162, 1498704012, '192.168.1.26', 1, 'WINDOWS_7', 'CHROME39');
INSERT INTO `sys_user_login_log` VALUES (163, 1498704110, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (164, 1498704322, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (165, 1498704455, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (166, 1498704533, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (167, 1498704682, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (168, 1498706068, '192.168.1.26', 1, 'WINDOWS_7', 'CHROME39');
INSERT INTO `sys_user_login_log` VALUES (169, 1498707433, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (170, 1498707589, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (171, 1498708225, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME');
INSERT INTO `sys_user_login_log` VALUES (172, 1498714561, '192.168.1.13', 1, 'WINDOWS_7', 'CHROME');
INSERT INTO `sys_user_login_log` VALUES (173, 1498714892, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (174, 1498714912, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (175, 1498717536, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (176, 1498719628, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (177, 1498719718, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (178, 1498720250, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (179, 1498721509, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (180, 1498721472, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (181, 1498721707, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (182, 1498722682, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (183, 1498722697, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (184, 1498722774, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (185, 1498722797, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (186, 1498723998, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (187, 1498724109, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (188, 1498724427, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (189, 1498724612, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (190, 1498726269, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (191, 1498726979, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (192, 1498727250, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (193, 1498729214, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (194, 1498730522, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (195, 1498730704, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (196, 1498784021, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (197, 1498784093, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (198, 1498784793, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (199, 1498785172, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (200, 1498786010, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (201, 1498786323, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (202, 1498786548, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (203, 1498786548, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (204, 1498786716, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (205, 1498786626, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (206, 1498786988, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (207, 1498787137, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (208, 1498787414, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (209, 1498787655, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (210, 1498787901, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (211, 1498788939, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (212, 1498789243, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (213, 1498792867, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (214, 1498793558, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (215, 1498793866, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (216, 1498794052, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (217, 1498794300, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (218, 1498801042, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (219, 1498800942, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (220, 1498802396, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (221, 1498802454, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (222, 1498803515, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (223, 1498804811, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (224, 1498805228, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (225, 1498805241, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (226, 1498805469, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (227, 1498805899, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (228, 1498806813, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (229, 1498806800, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (230, 1498806995, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (231, 1498807290, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (232, 1498807203, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (233, 1498807725, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (234, 1498808125, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (235, 1498809962, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (236, 1498811173, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (237, 1498811214, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (238, 1498811582, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (239, 1498811989, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (240, 1498812194, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (241, 1498812256, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (242, 1498812373, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (243, 1498812703, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (244, 1498812751, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (245, 1498814101, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (246, 1498814281, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (247, 1498814294, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (248, 1498815015, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (249, 1499042315, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (250, 1499043527, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (251, 1499049141, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (252, 1499051653, '192.168.1.88', 1, 'WINDOWS_7', 'IE9');
INSERT INTO `sys_user_login_log` VALUES (253, 1499051758, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME51');
INSERT INTO `sys_user_login_log` VALUES (254, 1499052573, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (255, 1499052849, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (256, 1499053115, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (257, 1499060576, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (258, 1499062246, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (259, 1499066402, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (260, 1499066583, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (261, 1499066779, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (262, 1499067338, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (263, 1499069038, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (264, 1499069279, '192.168.1.88', 7, 'WINDOWS_7', 'IE9');
INSERT INTO `sys_user_login_log` VALUES (265, 1499069518, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (266, 1499069582, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (267, 1499070803, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (268, 1499070825, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (269, 1499071525, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (270, 1499131270, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (271, 1499134326, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (272, 1499134791, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (273, 1499153429, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (274, 1499155324, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (275, 1499155573, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (276, 1499157566, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (277, 1499157784, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (278, 1499224438, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (279, 1499224558, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (280, 1499237804, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (281, 1499238665, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (282, 1499240768, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (283, 1499240844, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (284, 1499240927, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (285, 1499241009, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (286, 1499241689, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (287, 1499241852, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (288, 1499241940, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (289, 1499242070, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (290, 1499242204, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (291, 1499242576, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (292, 1499242686, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (293, 1499242786, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (294, 1499243019, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (295, 1499243826, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (296, 1499244330, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (297, 1499244413, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (298, 1499244893, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (299, 1499245106, '0:0:0:0:0:0:0:1', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (300, 1499304924, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (301, 1499305120, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (302, 1499305230, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (303, 1499305433, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (304, 1499305555, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (305, 1499305754, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (306, 1499305988, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (307, 1499306086, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (308, 1499306206, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (309, 1499306272, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (310, 1499306446, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (311, 1499323795, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (312, 1499324004, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (313, 1499325387, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (314, 1499325470, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (315, 1499325657, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (316, 1499325738, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (317, 1499325800, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (318, 1499326940, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (319, 1499326972, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (320, 1499327095, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (321, 1499327157, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (322, 1499327897, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (323, 1499328094, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (324, 1499328214, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (325, 1499333844, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (326, 1499389330, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (327, 1499392633, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (328, 1499394310, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (329, 1499394750, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (330, 1499397025, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (331, 1499397556, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (332, 1499399214, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (333, 1499407108, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME51');
INSERT INTO `sys_user_login_log` VALUES (334, 1499407181, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (335, 1499410304, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (336, 1499410387, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (337, 1499410642, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (338, 1499410906, '192.168.1.88', 8, 'WINDOWS_7', 'CHROME51');
INSERT INTO `sys_user_login_log` VALUES (339, 1499410928, '192.168.1.88', 8, 'WINDOWS_7', 'CHROME51');
INSERT INTO `sys_user_login_log` VALUES (340, 1499419066, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (341, 1499649774, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (342, 1499649874, '192.168.1.11', 1, 'WINDOWS_7', 'CHROME');
INSERT INTO `sys_user_login_log` VALUES (343, 1499650352, '192.168.1.11', 1, 'WINDOWS_7', 'CHROME');
INSERT INTO `sys_user_login_log` VALUES (344, 1499651147, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (345, 1499651546, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (346, 1499651696, '192.168.1.12', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (347, 1499652951, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (348, 1499653330, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (349, 1499656282, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (350, 1499657144, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (351, 1499665646, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (352, 1499665846, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (353, 1499666672, '192.168.1.12', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (354, 1499667521, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (355, 1499668376, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (356, 1499668738, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (357, 1499668939, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (358, 1499669915, '192.168.1.12', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (359, 1499670576, '192.168.1.88', 8, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (360, 1499675439, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (361, 1499675579, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (362, 1499675677, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (363, 1499675734, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (364, 1499676954, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (365, 1499678029, '192.168.1.12', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (366, 1499733831, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (367, 1499736644, '192.168.1.12', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (368, 1499740053, '192.168.1.12', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (369, 1499739950, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (370, 1499751595, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (371, 1499754490, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (372, 1499754532, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME51');
INSERT INTO `sys_user_login_log` VALUES (373, 1499754626, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME');
INSERT INTO `sys_user_login_log` VALUES (374, 1499754671, '192.168.1.88', 1, 'WINDOWS_7', 'IE9');
INSERT INTO `sys_user_login_log` VALUES (375, 1499754889, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (376, 1499755582, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (377, 1499823697, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (378, 1499824314, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (379, 1499830448, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (380, 1499830781, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (381, 1499851241, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (382, 1499911630, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (383, 1499911942, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (384, 1499912210, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (385, 1499912417, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (386, 1499912551, '192.168.1.88', 1, 'WINDOWS_7', 'IE9');
INSERT INTO `sys_user_login_log` VALUES (387, 1499913005, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (388, 1499914224, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (389, 1499914472, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (390, 1499915663, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (391, 1499918084, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (392, 1499925822, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (393, 1499936087, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (394, 1499938281, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (395, 1499938450, '192.168.1.88', 1, 'WINDOWS_7', 'IE9');
INSERT INTO `sys_user_login_log` VALUES (396, 1500012618, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (397, 1500018058, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (398, 1500018358, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (399, 1500018527, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (400, 1500018635, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
INSERT INTO `sys_user_login_log` VALUES (401, 1500018856, '192.168.1.88', 1, 'WINDOWS_7', 'CHROME49');
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
