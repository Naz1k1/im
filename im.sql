/*
 Navicat Premium Dump SQL

 Source Server         : localMySql
 Source Server Type    : MySQL
 Source Server Version : 80405 (8.4.5)
 Source Host           : localhost:3306
 Source Schema         : im

 Target Server Type    : MySQL
 Target Server Version : 80405 (8.4.5)
 File Encoding         : 65001

 Date: 15/06/2025 21:13:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for friend_notification
-- ----------------------------
DROP TABLE IF EXISTS `friend_notification`;
CREATE TABLE `friend_notification` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sender_id` bigint NOT NULL COMMENT '发送方ID',
  `receiver_id` bigint NOT NULL COMMENT '接收方ID',
  `type` tinyint NOT NULL COMMENT '1-好友请求 2-请求通过 3-请求拒绝',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '附加消息',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_receiver_status` (`receiver_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='好友通知表';

-- ----------------------------
-- Records of friend_notification
-- ----------------------------
BEGIN;
INSERT INTO `friend_notification` (`id`, `sender_id`, `receiver_id`, `type`, `content`, `created_at`, `updated_at`) VALUES (2, 1, 2, 2, '已接受您的好友请求', '2025-06-14 00:15:29', '2025-06-14 00:15:29');
INSERT INTO `friend_notification` (`id`, `sender_id`, `receiver_id`, `type`, `content`, `created_at`, `updated_at`) VALUES (4, 1, 3, 2, NULL, '2025-06-14 02:48:42', '2025-06-14 02:48:42');
COMMIT;

-- ----------------------------
-- Table structure for friend_relationship
-- ----------------------------
DROP TABLE IF EXISTS `friend_relationship`;
CREATE TABLE `friend_relationship` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '关系ID',
  `user_id` bigint NOT NULL COMMENT '用户ID（关联users.id）',
  `friend_id` bigint NOT NULL COMMENT '好友ID（关联users.id）',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-请求中 1-已好友 2-已拒绝 3-已拉黑',
  `remark` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '好友备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_friend` (`user_id`,`friend_id`) USING BTREE,
  KEY `idx_user_status` (`user_id`,`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='好友关系表';

-- ----------------------------
-- Records of friend_relationship
-- ----------------------------
BEGIN;
INSERT INTO `friend_relationship` (`id`, `user_id`, `friend_id`, `status`, `remark`, `created_at`, `updated_at`) VALUES (1, 1, 2, 1, NULL, '2025-06-14 00:15:29', '2025-06-14 00:15:29');
INSERT INTO `friend_relationship` (`id`, `user_id`, `friend_id`, `status`, `remark`, `created_at`, `updated_at`) VALUES (2, 2, 1, 1, NULL, '2025-06-14 00:15:29', '2025-06-14 00:15:29');
INSERT INTO `friend_relationship` (`id`, `user_id`, `friend_id`, `status`, `remark`, `created_at`, `updated_at`) VALUES (3, 1, 3, 1, NULL, '2025-06-14 02:48:42', '2025-06-14 02:48:42');
INSERT INTO `friend_relationship` (`id`, `user_id`, `friend_id`, `status`, `remark`, `created_at`, `updated_at`) VALUES (4, 3, 1, 1, NULL, '2025-06-14 02:48:42', '2025-06-14 02:48:42');
COMMIT;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '帐号',
  `email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '邮箱',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `last_seen` datetime DEFAULT NULL COMMENT '上次登录时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `salt` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码盐',
  `nickname` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '昵称',
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '头像',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`),
  UNIQUE KEY `idx_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
INSERT INTO `users` (`id`, `username`, `email`, `password`, `last_seen`, `created_at`, `updated_at`, `salt`, `nickname`, `avatar`) VALUES (1, 'test0', 'yakosang2001@163.com', 'ca5bb119c2f1c38ceb1dda529c742cb2d47e07a5407d02f32f62f080701196a2', NULL, '2025-06-11 15:06:34', '2025-06-11 15:06:34', 'ISCXhIRscJIxL7zQ', '测试0', '/static/images/default-avatar.png');
INSERT INTO `users` (`id`, `username`, `email`, `password`, `last_seen`, `created_at`, `updated_at`, `salt`, `nickname`, `avatar`) VALUES (2, 'yako', 'yakosang2001@gmail.com', '220c298d0211c17bfb13aee08773f34e774327595b8e7bb5901d76347d22a888', NULL, '2025-06-13 17:46:28', '2025-06-13 17:46:28', 'og198GeRvVFZgyjI', 'yakosang', '/static/images/default-avatar.png');
INSERT INTO `users` (`id`, `username`, `email`, `password`, `last_seen`, `created_at`, `updated_at`, `salt`, `nickname`, `avatar`) VALUES (3, 'test1', '14@qq.com', '9a346e89c31bea96bbf874db8a98d4d5de69536a4a4dc89c99022e48e2323914', NULL, '2025-06-14 02:46:30', '2025-06-14 02:46:30', 'O0vscBa5Id0Tt9H8', '测试2', '/static/images/default-avatar.png');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
