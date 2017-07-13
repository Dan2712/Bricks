package com.dji.bricks.UI;

import com.dji.bricks.tools.PropertyUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * 
 *
 * @author DraLastat
 */
public class ConstantsUI {

    /**
     * 
     */
    public final static String APP_NAME = "BRICKS";
    public final static String APP_VERSION = "v_1.00_170630";

    /**
     * �����ڴ�С
     */
    public final static int MAIN_WINDOW_X = 240;
    public final static int MAIN_WINDOW_Y = 100;
    public final static int MAIN_WINDOW_WIDTH = 890;
    public final static int MAIN_WINDOW_HEIGHT = 660;

    /**
     * ϵͳ��ǰ·��
     */
    public final static String CURRENT_DIR = System.getProperty("user.dir");

    /**
     * ������ͼ��
     */
    public final static Image IMAGE_ICON = Toolkit.getDefaultToolkit()
            .getImage(CURRENT_DIR + File.separator + "icon" + File.separator + "Bricks.png");

    /**
     * �����ڱ���ɫ
     */
    public final static Color MAIN_BACK_COLOR = Color.WHITE;

    /**
     * ����������ɫ
     */
    public final static Color TOOL_BAR_BACK_COLOR = new Color(70, 130, 180);
    /**
     * ��������ɫ
     */
    public final static Color TABLE_BACK_COLOR = new Color(62, 62, 62);
    /**
     * �����������ɫ
     */
    public final static Color TABLE_LINE_COLOR = new Color(229, 229, 229);
    /**
     * ��ӡ�򱳾�ɫ
     */
    public final static Color TABLE_LOG_COLOR = new Color(211, 211, 211);

    /**
     * ����
     */
    // ��������
    public final static Font FONT_TITLE = new Font(PropertyUtil.getProperty("ds.ui.font.family"), 0, 27);
    // ������������
    public final static Font SEC_TITLE = new Font(PropertyUtil.getProperty("ds.ui.font.family"), 0, 16);
    // ��ͨ����
    public final static Font FONT_NORMAL = new Font(PropertyUtil.getProperty("ds.ui.font.family"), 0, 13);
    // ��ͨ����
    public final static Font FONT_ELE_NORMAL = new Font(PropertyUtil.getProperty("ds.ui.font.family"), 0, 16);
    // radio����
    public final static Font FONT_RADIO = new Font(PropertyUtil.getProperty("ds.ui.font.family"), 0, 15);

    /**
     * ��ͼ��
     */
    public final static ImageIcon ICON_DATA_SYNC = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "Bricks.png");
    /**
     * ������ͼ��
     */
    // ״̬ Ĭ��
    public final static ImageIcon ICON_STATUS = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "status.png");
    // ״̬ ����
    public final static ImageIcon ICON_STATUS_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "statusEnable.png");
    // ���ݿ� Ĭ��
    public final static ImageIcon ICON_DATABASE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "database.png");
    // ���ݿ� ����
    public final static ImageIcon ICON_DATABASE_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "databaseEnable.png");
    // ִ�мƻ� Ĭ��
    public final static ImageIcon ICON_SCHEDULE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "schedule.png");
    // ִ�мƻ� ����
    public final static ImageIcon ICON_SCHEDULE_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "scheduleEnable.png");
    // ���� Ĭ��
    public final static ImageIcon ICON_SETTING = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "setting.png");
    // ���� ����
    public final static ImageIcon ICON_SETTING_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "settingEnable.png");
    // ���� Ĭ��
    public final static ImageIcon ICON_BACKUP = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "backup.png");
    // ���� ����
    public final static ImageIcon ICON_BACKUP_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "backupEnable.png");

    /**
     * ��ť ͼ��
     */
    // ���ƻ�ִ�� Ĭ��
    public final static ImageIcon ICON_START_SCHEDULE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "startSchedule.png");
    // ���ƻ�ִ�� ����
    public final static ImageIcon ICON_START_SCHEDULE_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "startScheduleEnable.png");
    // ���ƻ�ִ�� ʧЧ
    public final static ImageIcon ICON_START_SCHEDULE_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "startScheduleDisable.png");
    // ���ƻ�ִ�� Ĭ��
    public final static ImageIcon ICON_FINDCASE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "findcase.png");
    // ���ƻ�ִ�� ����
    public final static ImageIcon ICON_FINDCASE_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "findcaseenable.png");
    // ���ƻ�ִ�� ʧЧ
    public final static ImageIcon ICON_FINDCASE_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "findcaseenable.png");
    // ���ש�� Ĭ��
    public final static ImageIcon ICON_ELE_ADD = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "add.png");
    // ���ש�� ����
    public final static ImageIcon ICON_ELE_ADD_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "addenable.png");
    // ���ש�� ʧЧ
    public final static ImageIcon ICON_ELE_ADD_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "addenable.png");
    
    // ���ש�� Ĭ��
    public final static ImageIcon ICON_ELE_CHK = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "unknow.png");
    // ���ש�� ����
    public final static ImageIcon ICON_ELE_CHK_TRUE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "true.png");
    // ���ש�� ʧЧ
    public final static ImageIcon ICON_ELE_CHK_FALSE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "false.png");
    
    // ��ʼ Ĭ��
    public final static ImageIcon ICON_START = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "start.png");
    // ��ʼ ����
    public final static ImageIcon ICON_START_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "startEnable.png");
    // ��ʼ ʧЧ
    public final static ImageIcon ICON_START_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "startDisable.png");
    
    // ֹͣ Ĭ��
    public final static ImageIcon ICON_STOP = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "stop.png");
    // ֹͣ ����
    public final static ImageIcon ICON_STOP_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "stopEnable.png");
    // ֹͣ ʧЧ
    public final static ImageIcon ICON_STOP_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "stopDisable.png");
    // ����ͬ�� Ĭ��
    public final static ImageIcon ICON_SYNC_NOW = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "syncNow.png");
    // ����ͬ�� ����
    public final static ImageIcon ICON_SYNC_NOW_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "syncNowEnable.png");
    // ����ͬ�� ʧЧ
    public final static ImageIcon ICON_SYNC_NOW_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "syncNowDisable.png");
    // �������� Ĭ��
    public final static ImageIcon ICON_TEST_LINK = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "testLinkButton.png");
    // �������� ����
    public final static ImageIcon ICON_TEST_LINK_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "testLinkButtonEnable.png");
    // �������� ʧЧ
    public final static ImageIcon ICON_TEST_LINK_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "testLinkButtonDisable.png");
    // ���� Ĭ��
    public final static ImageIcon ICON_SAVE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "saveButton.png");
    // ���� ����
    public final static ImageIcon ICON_SAVE_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "saveButtonEnable.png");
    // ���� ʧЧ
    public final static ImageIcon ICON_SAVE_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "saveButtonDisable.png");
    // �½����� Ĭ��
    public final static ImageIcon ICON_NEW_BAK = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "newBak.png");
    // �½����� ����
    public final static ImageIcon ICON_NEW_BAK_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "newBakEnable.png");
    // �½����� ʧЧ
    public final static ImageIcon ICON_NEW_BAK_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "newBakDisable.png");
    // ɾ������ Ĭ��
    public final static ImageIcon ICON_DEL_BAK = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "deleteBak.png");
    // ɾ������ ����
    public final static ImageIcon ICON_DEL_BAK_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "deleteBakEnable.png");
    // ɾ������ ʧЧ
    public final static ImageIcon ICON_DEL_BAK_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "deleteBakDisable.png");
    // ��ԭ���� Ĭ��
    public final static ImageIcon ICON_RECOVER_BAK = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "recoverBak.png");
    // ��ԭ���� ����
    public final static ImageIcon ICON_RECOVER_BAK_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "recoverBakEnable.png");
    // ��ԭ���� ʧЧ
    public final static ImageIcon ICON_RECOVER_BAK_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "recoverBakDisable.png");

    // ������б��� Ĭ��
    public final static ImageIcon ICON_CLEAR_ALL_BAKS = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "clearAllBaks.png");
    // ������б��� ����
    public final static ImageIcon ICON_CLEAR_ALL_BAKS_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "clearAllBaksEnable.png");
    // ������б��� ʧЧ
    public final static ImageIcon ICON_CLEAR_ALL_BAKS_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "clearAllBaksDisable.png");

    // ���ֶζ�Ӧ��ϵ Ĭ��
    public final static ImageIcon ICON_TABLE_FIELD = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "tableFiled.png");
    // ���ֶζ�Ӧ��ϵ ����
    public final static ImageIcon ICON_TABLE_FIELD_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "tableFiledEnable.png");
    // ���ֶζ�Ӧ��ϵ ʧЧ
    public final static ImageIcon ICON_TABLE_FIELD_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "tableFiledDisable.png");
    // ���Log Ĭ��
    public final static ImageIcon ICON_CLEAR_LOG = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "clearLog.png");
    // ���Log ����
    public final static ImageIcon ICON_CLEAR_LOG_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "clearLogEnable.png");
    // ���Log ʧЧ
    public final static ImageIcon ICON_CLEAR_LOG_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "clearLogDisable.png");

    /**
     * ��ʽ�������
     */
    // �����ˮƽ���
    public final static int MAIN_H_GAP = 25;
    // �����Label ��С
    public final static Dimension LABLE_SIZE = new Dimension(1300, 30);
    // Item Label ��С
    public final static Dimension LABLE_SIZE_ITEM = new Dimension(78, 30);
    // Item Label ��С
    public final static Dimension LABLE_SIZE_ELE_ITEM = new Dimension(100, 30);
    // Item Null Label ��С
    public final static Dimension LABLE_SIZE_NULL_ITEM = new Dimension(320, 30);
    // Item Null Label ��С
    public final static Dimension LABLE_NULL_ITEM = new Dimension(80, 30);
    // Casecre Null Label ��С
    public final static Dimension LABLE_SIZE_CASECRE_NULL_ITEM = new Dimension(500, 30);
    // Casecre Null Label ��С
    public final static Dimension LABLE_SIZE_CASECRE_NULL_SEC_ITEM = new Dimension(130, 30);
    // CASErun Null Label ��С
    public final static Dimension LABLE_SIZE_CASE_NULL_ITEM = new Dimension(350, 30);
    // Item text field ��С
    public final static Dimension TEXT_FIELD_SIZE_ITEM = new Dimension(190, 24);
    // Item Combox field ��С
    public final static Dimension TEXT_COMBOX_SIZE_ITEM = new Dimension(130, 24);
    // radio ��С
    public final static Dimension RADIO_SIZE = new Dimension(1300, 60);
    // �߼�ѡ�����Item ��С
    public final static Dimension PANEL_ITEM_SIZE = new Dimension(1300, 40);

}
