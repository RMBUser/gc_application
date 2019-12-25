CREATE TABLE `t_gc_db_lookup_value` (
`LKV_ID`  int NOT NULL AUTO_INCREMENT COMMENT 'ֵ��ID' ,
`LOOKUP_TYPE_CODE`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '���ͱ���' ,
`LOOKUP_TYPE`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '����' ,
`LOOKUP_KEY`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '���Ͱ�����ֵ' ,
`LOOKUP_VALUE`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '���Ͱ���ӳ��ֵ' ,
`VALUE_DESC`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'ֵ����' ,
`ORDER_NO`  int NULL DEFAULT NULL COMMENT '˳��' ,
`CREATED_TIME`  datetime NOT NULL DEFAULT now() COMMENT '����ʱ��' ,
`IS_ENABLE`  tinyint NOT NULL DEFAULT 1 COMMENT '�Ƿ���ã�1������ 0��������' ,
PRIMARY KEY (`LKV_ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
;

INSERT INTO t_gc_db_lookup_value(LOOKUP_TYPE_CODE,LOOKUP_TYPE,LOOKUP_KEY,LOOKUP_VALUE,VALUE_DESC,ORDER_NO) VALUES ('GC001','��������ӿ�ӳ��','�ɻ���','1','choviwu�ӿڵĿɻ���ӳ�����ǵ�CATEGORY_ID=1',0);
INSERT INTO t_gc_db_lookup_value(LOOKUP_TYPE_CODE,LOOKUP_TYPE,LOOKUP_KEY,LOOKUP_VALUE,VALUE_DESC,ORDER_NO) VALUES ('GC001','��������ӿ�ӳ��','�к�����','2','choviwu�ӿڵ��к�����ӳ�����ǵ�CATEGORY_ID=2',1);
INSERT INTO t_gc_db_lookup_value(LOOKUP_TYPE_CODE,LOOKUP_TYPE,LOOKUP_KEY,LOOKUP_VALUE,VALUE_DESC,ORDER_NO) VALUES ('GC001','��������ӿ�ӳ��','ʪ����','3','choviwu�ӿڵ�ʪ����ӳ�����ǵ�CATEGORY_ID=3',2);
INSERT INTO t_gc_db_lookup_value(LOOKUP_TYPE_CODE,LOOKUP_TYPE,LOOKUP_KEY,LOOKUP_VALUE,VALUE_DESC,ORDER_NO) VALUES ('GC001','��������ӿ�ӳ��','������','4','choviwu�ӿڵĸ�����ӳ�����ǵ�CATEGORY_ID=4',3);
