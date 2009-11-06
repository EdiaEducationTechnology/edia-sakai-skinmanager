ALTER TABLE `edia_skinmanager_achive` CHANGE COLUMN `name` `skin_name` VARCHAR(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
 CHANGE COLUMN `lastModified` `skin_lastModified` DATETIME DEFAULT NULL,
 CHANGE COLUMN `version` `skin_version` INTEGER NOT NULL,
 CHANGE COLUMN `comment` `skin_comment` VARCHAR(55) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
 CHANGE COLUMN `file` `skin_file` LONGBLOB DEFAULT NULL,
 CHANGE COLUMN `active` `skin_active` BIT(1) DEFAULT NULL;