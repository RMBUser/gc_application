/* ��ʼ��t_gc_db_category��*/
INSERT INTO `t_gc_db_category` (`CATEGORY_TYPE`) VALUES ('�Ϻ��ɻ�������');
INSERT INTO `t_gc_db_category` (`CATEGORY_TYPE`) VALUES ('�Ϻ��к�����');
INSERT INTO `t_gc_db_category` (`CATEGORY_TYPE`) VALUES ('�Ϻ�ʪ����');
INSERT INTO `t_gc_db_category` (`CATEGORY_TYPE`) VALUES ('�Ϻ�������');

/* ��ʼ��t_gc_db_garbage_info��*/
INSERT INTO t_gc_db_garbage_info(GARBAGE_NAME) SELECT g.garbage FROM garbage g;

/* ��ʼ��t_gc_bu_garbage_classification��*/
INSERT INTO t_gc_bu_garbage_classification(GARBAGE_ID,CATEGORY_ID) (SELECT t.GARBAGE_ID,g.type FROM garbage g JOIN t_gc_db_garbage_info t ON BINARY g.garbage = BINARY t.GARBAGE_NAME);

/* ������3����ʪ������4���������*/
UPDATE t_gc_bu_garbage_classification gc SET gc.CATEGORY_ID = 3 WHERE gc.CATEGORY_ID = 4;
UPDATE t_gc_bu_garbage_classification gc SET gc.CATEGORY_ID = 4 WHERE gc.CATEGORY_ID = 8;