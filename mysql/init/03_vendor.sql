-- 服务商表 (存储服务商基础信息)
CREATE TABLE `ven_vendor` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '服务商ID',
  `name` VARCHAR(100) NOT NULL COMMENT '服务商名称',
  `ven_type` varchar(30) NOT NULL COMMENT '服务商类型 与VenTypeEnum绑定',
  `api_base_url` VARCHAR(255) NOT NULL COMMENT 'API基础地址',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-正常 1-已删除',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务商基本信息表';

-- 服务商Token表 (一对多关系)
CREATE TABLE `ven_vendor_token` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Token ID',
  `vendor_id` BIGINT UNSIGNED NOT NULL COMMENT '服务商ID',
  `api_key` VARCHAR(255) NOT NULL COMMENT 'API密钥',
  `token_name` VARCHAR(100) COMMENT 'Token名称/描述',
  `is_active` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否激活：0-禁用 1-启用',
  `expires_at` DATETIME COMMENT '过期时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_vendor_id` (`vendor_id`),
  KEY `idx_api_key` (`api_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务商Token表';




-- 模型表 (存储服务商提供的具体模型)
CREATE TABLE `ven_model` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '模型ID(UUID)',
  `vendor_id` BIGINT NOT NULL COMMENT '服务商ID',
  `name` VARCHAR(100) NOT NULL COMMENT '模型名称(服务商内部名称)',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-正常 1-已删除',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_vendor` (`vendor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务商模型表';






