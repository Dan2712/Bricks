package logic;

import java.io.File;

import UI.ConstantsUI;

/**
 * Logic
 * 
 * @author DraLastat
 *
 */
public class ConstantsLogic {

	/**
	 * �������ļ�·��
	 */
	// ��-�ֶ������ļ� ·��
	public final static String TABLE_FIELD_DIR = ConstantsUI.CURRENT_DIR + File.separator + "TableField";
	// ��-�ֶ������ļ� ·��
	public final static String TABLE_FIELD_INIT_DIR = ConstantsUI.CURRENT_DIR + File.separator + "TableField"
			+ File.separator + "init";
	// Trigger�����ļ�
	public final static String TRIGGER_FILE = ConstantsUI.CURRENT_DIR + File.separator + "Trigger" + File.separator
			+ "Trigger.txt";
	// ������ļ� ·��
	public final static String SNAPS_DIR = ConstantsUI.CURRENT_DIR + File.separator + "snaps";
	// ������ļ����� ·��
	public final static String SNAPS_BAK_DIR = ConstantsUI.CURRENT_DIR + File.separator + "snaps_bak";
	// sql��־�ļ�·��
	public final static String LOG_SQL_DIR = ConstantsUI.CURRENT_DIR + File.separator + "log_SQL";
	// sql��־�ļ�
	public final static String LOG_SQL = ConstantsUI.CURRENT_DIR + File.separator + "log_SQL" + File.separator
			+ "log_sql.csv";

	// mysql���ⱸ��bat�ļ�·��
	public final static String BAT_DIR_MYSQL = ConstantsUI.CURRENT_DIR + File.separator + "DB_Backup" + File.separator
			+ "mysql_backup.bat";

	// mysql����sql�ļ�·��
	public final static String MYSQL_TABLE_BACKUP_SQL_FILE = ConstantsUI.CURRENT_DIR + File.separator + "DB_Backup"
			+ File.separator + "mysql_table_backup.sql";

	// mysql����sql�ļ�·��
	public final static String PATH_MYSQL_BAK = ConstantsUI.CURRENT_DIR + File.separator + "DB_Backup" + File.separator
			+ "Target";

	/**
	 * ������ʽ
	 */
	// trigger�����ļ�����������������
	public final static String REGEX_TRIGGER_TABLE = "<([^<>]+)>";
	// trigger�����ļ�������������������
	public final static String REGEX_TRIGGER_PRIM_KEY = "\\(([^()]+)\\)";
	// trigger�����ļ����������ֶε�����
	public final static String REGEX_TRIGGER_FIELDS = "\\{([^{}]+)\\}";
	// trigger�����ļ�����������������������������
	public final static String REGEX_TRIGGER_OTHER = "\\[([^\\[\\]]+)\\]";

}
