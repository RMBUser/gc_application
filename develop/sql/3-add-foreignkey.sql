ALTER TABLE `t_gc_bu_garbage_classification` ADD CONSTRAINT `foreign_garbage` FOREIGN KEY (`GARBAGE_ID`) REFERENCES `t_gc_db_garbage_info` (`GARBAGE_ID`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `t_gc_bu_garbage_classification` ADD CONSTRAINT `foreign_category` FOREIGN KEY (`CATEGORY_ID`) REFERENCES `t_gc_db_category` (`CATEGORY_ID`) ON DELETE CASCADE ON UPDATE CASCADE;

