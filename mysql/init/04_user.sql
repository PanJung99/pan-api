-- 用户表（核心账户信息）
CREATE TABLE `us_user` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(64) NOT NULL COMMENT '用户名',
  `login_type` TINYINT NOT NULL DEFAULT 1 COMMENT '登录类型：1-密码登录 2-微信登录 3-手机验证码',
  `password` VARCHAR(128) DEFAULT NULL COMMENT '加密密码（PBKDF2WithHmacSHA256算法）',
  `salt` VARCHAR(64) DEFAULT NULL COMMENT '密码盐值（16字节Base64编码）',
  
  `wechat_openid` VARCHAR(64) DEFAULT NULL COMMENT '微信OpenID（微信登录时使用）',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号（国际区号+号码）',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱地址',
  
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-正常 1-已删除',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_wechat` (`wechat_openid`),
  UNIQUE KEY `uk_phone` (`phone`),
  UNIQUE KEY `uk_email` (`email`),
  KEY `idx_login_type` (`login_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户主表';

-- 用户余额表（与用户1：1关系）
CREATE TABLE `us_balance` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT UNSIGNED NOT NULL,
    `current_balance` DECIMAL(25,10) NOT NULL DEFAULT 0.00,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户余额表';

-- API Key表（用户与Key 1:N关系）
CREATE TABLE `us_api_key` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Key ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '关联用户ID',
  `key_name` VARCHAR(128) NOT NULL COMMENT 'Key名称（用户自定义）',
  `api_key` VARCHAR(64) NOT NULL COMMENT 'API Key（唯一索引）',
  `quota` DECIMAL(15,4) NOT NULL DEFAULT 0.00 COMMENT '总限额',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-正常 1-已删除',
  `last_used` DATETIME DEFAULT NULL COMMENT '最后使用时间',
  `expire_time` DATETIME DEFAULT NULL COMMENT '过期时间（NULL为永久有效）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_api_key` (`api_key`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='API密钥表';

-- 账单表（余额变动记录）
CREATE TABLE `us_bill` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '账单ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '关联用户ID',
  `api_key_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '关联API Key ID（API扣费时非空）',
  `bill_type` TINYINT NOT NULL COMMENT '账单类型：1-充值 2-API扣费 99-人工调整',
  `amount` DECIMAL(25,10) NOT NULL COMMENT '变动金额（正数为收入，负数为支出）',
  `before_balance` DECIMAL(25,10) COMMENT '变动前余额',
  `after_balance` DECIMAL(25,10) COMMENT '变动后余额',
  `related_id` VARCHAR(64) DEFAULT NULL COMMENT '关联业务ID（支付订单/请求ID）',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '账单描述',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `model_tag_id` bigint(20) DEFAULT NULL COMMENT '模型ID',
  `model_tag_name` varchar(50) DEFAULT NULL COMMENT '模型名称',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_bill_type` (`bill_type`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_api_key` (`api_key_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单记录表';

-- 租户管理员表
CREATE TABLE `us_admin` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '用户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租户管理员关系表';