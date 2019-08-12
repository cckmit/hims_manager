/*
Navicat MySQL Data Transfer

Source Server         : monitor
Source Server Version : 80013
Source Host           : 172.16.48.66:3306
Source Database       : monitoring

Target Server Type    : MYSQL
Target Server Version : 80013
File Encoding         : 65001

Date: 2019-07-12 16:32:26
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for log_type
-- ----------------------------
DROP TABLE IF EXISTS `log_type`;
CREATE TABLE `log_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` varchar(50) NOT NULL COMMENT '日志类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of log_type
-- ----------------------------
INSERT INTO `log_type` VALUES ('1', 'lemon-client');
INSERT INTO `log_type` VALUES ('2', 'lemon-ics-async-invoker');
INSERT INTO `log_type` VALUES ('3', 'lemon-txnplte');
INSERT INTO `log_type` VALUES ('4', 'lemon-access');
INSERT INTO `log_type` VALUES ('5', 'lemon-ics-invoker');
INSERT INTO `log_type` VALUES ('6', 'lemon-stream-producer');
INSERT INTO `log_type` VALUES ('7', 'lemon-stream-consumer');
INSERT INTO `log_type` VALUES ('8', 'lemonerror');
INSERT INTO `log_type` VALUES ('9', 'ics-consvr');
INSERT INTO `log_type` VALUES ('10', 'ics-txnplte');
INSERT INTO `log_type` VALUES ('11', 'ics-lsnsvr');
INSERT INTO `log_type` VALUES ('12', 'ics-consumer');
INSERT INTO `log_type` VALUES ('13', 'ics-web');
INSERT INTO `log_type` VALUES ('14', 'ics-txnexec');
INSERT INTO `log_type` VALUES ('15', 'ics-producer');
INSERT INTO `log_type` VALUES ('16', 'ics-rstsvr');
INSERT INTO `log_type` VALUES ('17', 'icserror');
INSERT INTO `log_type` VALUES ('18', 'nginx-access');
INSERT INTO `log_type` VALUES ('19', 'nginx-error');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) NOT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `perms` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(11) NOT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COMMENT='菜单管理';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '0', '系统管理', 'admin:setting', '0');
INSERT INTO `sys_menu` VALUES ('2', '1', '用户管理', 'admin:user', '1');
INSERT INTO `sys_menu` VALUES ('3', '1', '角色管理', 'admin:role', '1');
INSERT INTO `sys_menu` VALUES ('4', '2', '查看', 'sys:user:list,sys:user:info', '2');
INSERT INTO `sys_menu` VALUES ('5', '2', '新增', 'sys:user:save,sys:role:select', '2');
INSERT INTO `sys_menu` VALUES ('6', '2', '修改', 'sys:user:update,sys:role:select', '2');
INSERT INTO `sys_menu` VALUES ('7', '2', '删除', 'sys:user:delete', '2');
INSERT INTO `sys_menu` VALUES ('8', '3', '查看', 'sys:role:list,sys:role:info', '2');
INSERT INTO `sys_menu` VALUES ('9', '3', '新增', 'sys:role:save,sys:menu:list', '2');
INSERT INTO `sys_menu` VALUES ('10', '3', '修改', 'sys:role:update,sys:menu:list', '2');
INSERT INTO `sys_menu` VALUES ('11', '3', '删除', 'sys:role:delete', '2');
INSERT INTO `sys_menu` VALUES ('12', '0', '日志检索', 'admin:search', '0');
INSERT INTO `sys_menu` VALUES ('13', '12', '普通检索', 'admin:ordianrySearch', '1');
INSERT INTO `sys_menu` VALUES ('14', '12', '高级检索', 'admin:advanceSearch', '1');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `create_user_no` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13007 DEFAULT CHARSET=utf8 COMMENT='角色';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('10506', '超级管理员', '超级管理员', '1', '2019-06-23 22:20:20');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=300 DEFAULT CHARSET=utf8 COMMENT='角色与菜单对应关系';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('285', '10506', '1');
INSERT INTO `sys_role_menu` VALUES ('286', '10506', '2');
INSERT INTO `sys_role_menu` VALUES ('287', '10506', '4');
INSERT INTO `sys_role_menu` VALUES ('288', '10506', '5');
INSERT INTO `sys_role_menu` VALUES ('289', '10506', '6');
INSERT INTO `sys_role_menu` VALUES ('290', '10506', '7');
INSERT INTO `sys_role_menu` VALUES ('291', '10506', '3');
INSERT INTO `sys_role_menu` VALUES ('292', '10506', '8');
INSERT INTO `sys_role_menu` VALUES ('293', '10506', '9');
INSERT INTO `sys_role_menu` VALUES ('294', '10506', '10');
INSERT INTO `sys_role_menu` VALUES ('295', '10506', '11');
INSERT INTO `sys_role_menu` VALUES ('296', '10506', '12');
INSERT INTO `sys_role_menu` VALUES ('297', '10506', '13');
INSERT INTO `sys_role_menu` VALUES ('298', '10506', '14');
INSERT INTO `sys_role_menu` VALUES ('299', '10506', '6666666');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_no` varchar(20) NOT NULL COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `salt` varchar(20) DEFAULT NULL COMMENT '盐',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `create_user_no` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_no`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '9ec9750e709431dad22365cabc5c625482e574c74adaebba7dd02f1129e4ce1d', 'YzcmCZNvbXocrsz9dm8e', 'root@renren.io', '13612345678', '1', '1', '2016-11-11 11:11:11');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_no` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 COMMENT='用户与角色对应关系';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', '1', '10506');
