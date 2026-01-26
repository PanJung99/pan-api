-- 平台模型
CREATE TABLE `md_model` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '标签ID(系统内部标识)',
  `name` VARCHAR(100) NOT NULL COMMENT 'API调用的标识名称（唯一）',
  `display_name` varchar(100) NOT NULL COMMENT '对外展示的模型名称',
  `is_free` tinyint(1) DEFAULT '0' COMMENT '是否免费',
  `category` varchar(32) NOT NULL COMMENT '模型类别：chat-对话模型,image-绘画模型,audio-语音模型,video-视频模型,embedding-多模态向量模型',
  `platform_type` VARCHAR(64) DEFAULT NULL COMMENT '模型厂商：deepseek、chatgpt、glm等等',
  `description` VARCHAR(255) COMMENT '标签描述',
  `is_active` tinyint(1) DEFAULT '1' COMMENT '是否激活：0-禁用 1-启用',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-正常 1-已删除',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_name` (`name`),
  KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='平台模型表';

CREATE TABLE `md_model_binding` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '绑定ID',
  `model_id` BIGINT UNSIGNED NOT NULL COMMENT '平台模型ID',
  `ven_model_id` BIGINT UNSIGNED NOT NULL COMMENT '服务商模型ID',
  `enabled` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用：0-禁用 1-启用',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_model_vendor` (`model_id`, `ven_model_id`),
  KEY `idx_model` (`model_id`),
  KEY `idx_ven_model` (`ven_model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='平台模型与服务商模型绑定表';

-- 模型计费项定义表 与model表一对多的关系，代表模型会复合计费
CREATE TABLE `md_pricing_item` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '计费项ID',

  `model_id` BIGINT UNSIGNED NOT NULL COMMENT '平台模型ID',
  `unit` VARCHAR(32) NOT NULL COMMENT '计费单位：mtokens,nums等 与UnitEnum绑定',

  `price_input` DECIMAL(18,6) COMMENT '输入单价',
  `price_output` DECIMAL(18,6) COMMENT '输出单价',

  `currency` VARCHAR(16) NOT NULL DEFAULT 'CNY',
  `is_active` tinyint(1) DEFAULT '1' COMMENT '是否激活：0-禁用 1-启用',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-正常 1-已删除',

  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_model` (`model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模型计费项定义表';


