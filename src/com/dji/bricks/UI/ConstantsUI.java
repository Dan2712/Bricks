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
    public final static String APP_VERSION = "v_1.00_170717";

    /**
     * 
     */
    public final static int MAIN_WINDOW_X = 240;
    public final static int MAIN_WINDOW_Y = 100;
    public final static int MAIN_WINDOW_WIDTH = 890;
    public final static int MAIN_WINDOW_HEIGHT = 660;

    /**
     * 
     */
    public final static String CURRENT_DIR = System.getProperty("user.dir");

    /**
     * 
     */
    public final static Image IMAGE_ICON = Toolkit.getDefaultToolkit()
            .getImage(CURRENT_DIR + File.separator + "icon" + File.separator + "Bricks.png");

    /**
     *
     */
    public final static Color MAIN_BACK_COLOR = Color.WHITE;

    /**
     * 
     */
    public final static Color TOOL_BAR_BACK_COLOR = new Color(70, 130, 180);
    /**
     * 
     */
    public final static Color TABLE_BACK_COLOR = new Color(62, 62, 62);
    /**
     * 
     */
    public final static Color TABLE_LINE_COLOR = new Color(229, 229, 229);
    /**
     * 
     */
    public final static Color TABLE_LOG_COLOR = new Color(211, 211, 211);

    /**
     * 
     */
    public final static Font FONT_TITLE = new Font(PropertyUtil.getProperty("ds.ui.font.family"), 0, 27);
    public final static Font SEC_TITLE = new Font(PropertyUtil.getProperty("ds.ui.font.family"), 0, 16);
    public final static Font FONT_NORMAL = new Font(PropertyUtil.getProperty("ds.ui.font.family"), 0, 13);
    public final static Font FONT_ELE_NORMAL = new Font(PropertyUtil.getProperty("ds.ui.font.family"), 0, 16);
    public final static Font FONT_RADIO = new Font(PropertyUtil.getProperty("ds.ui.font.family"), 0, 15);

    /**
     * 
     */
    public final static ImageIcon ICON_DATA_SYNC = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "Bricks.png");
    /**
     * 
     */
    public final static ImageIcon ICON_STATUS = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "status.png");
    public final static ImageIcon ICON_STATUS_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "statusEnable.png");
    public final static ImageIcon ICON_DATABASE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "database.png");
    public final static ImageIcon ICON_DATABASE_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "databaseEnable.png");
    public final static ImageIcon ICON_SCHEDULE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "schedule.png");
    public final static ImageIcon ICON_SCHEDULE_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "scheduleEnable.png");
    public final static ImageIcon ICON_SETTING = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "setting.png");
    public final static ImageIcon ICON_SETTING_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "settingEnable.png");
    public final static ImageIcon ICON_BACKUP = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "backup.png");
    public final static ImageIcon ICON_BACKUP_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "backupEnable.png");

    /**
     * 
     */
    public final static ImageIcon ICON_START_SCHEDULE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "startSchedule.png");
    public final static ImageIcon ICON_START_SCHEDULE_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "startScheduleEnable.png");
    public final static ImageIcon ICON_START_SCHEDULE_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "startScheduleDisable.png");
    public final static ImageIcon ICON_FINDCASE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "findcase.png");
    public final static ImageIcon ICON_FINDCASE_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "findcaseenable.png");
    public final static ImageIcon ICON_FINDCASE_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "findcaseenable.png");
    public final static ImageIcon ICON_ELE_ADD = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "add.png");
    public final static ImageIcon ICON_ELE_ADD_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "addenable.png");
    public final static ImageIcon ICON_ELE_ADD_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "addenable.png");
    public final static ImageIcon ICON_ELE_CHK = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "unknow.png");
    public final static ImageIcon ICON_ELE_CHK_TRUE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "true.png");
    public final static ImageIcon ICON_ELE_CHK_FALSE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "false.png");
    public final static ImageIcon ICON_START = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "start.png");
    public final static ImageIcon ICON_START_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "startEnable.png");
    public final static ImageIcon ICON_START_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "startDisable.png");
    public final static ImageIcon ICON_STOP = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "stop.png");
    public final static ImageIcon ICON_STOP_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "stopEnable.png");
    public final static ImageIcon ICON_STOP_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "stopDisable.png");
    public final static ImageIcon ICON_SYNC_NOW = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "syncNow.png");
    public final static ImageIcon ICON_SYNC_NOW_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "syncNowEnable.png");
    public final static ImageIcon ICON_SYNC_NOW_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "syncNowDisable.png");
    public final static ImageIcon ICON_TEST_LINK = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "testLinkButton.png");
    public final static ImageIcon ICON_TEST_LINK_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "testLinkButtonEnable.png");
    public final static ImageIcon ICON_TEST_LINK_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "testLinkButtonDisable.png");
    public final static ImageIcon ICON_SAVE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "saveButton.png");
    public final static ImageIcon ICON_SAVE_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "saveButtonEnable.png");
    public final static ImageIcon ICON_SAVE_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "saveButtonDisable.png");
    public final static ImageIcon ICON_NEW_BAK = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "newBak.png");
    public final static ImageIcon ICON_NEW_BAK_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "newBakEnable.png");
    public final static ImageIcon ICON_NEW_BAK_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "newBakDisable.png");
    public final static ImageIcon ICON_DEL_BAK = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "deleteBak.png");
    public final static ImageIcon ICON_DEL_BAK_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "deleteBakEnable.png");
    public final static ImageIcon ICON_DEL_BAK_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "deleteBakDisable.png");
    public final static ImageIcon ICON_RECOVER_BAK = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "recoverBak.png");
    public final static ImageIcon ICON_RECOVER_BAK_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "recoverBakEnable.png");
    public final static ImageIcon ICON_RECOVER_BAK_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "recoverBakDisable.png");
    public final static ImageIcon ICON_CLEAR_ALL_BAKS = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "clearAllBaks.png");
    public final static ImageIcon ICON_CLEAR_ALL_BAKS_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "clearAllBaksEnable.png");
    public final static ImageIcon ICON_CLEAR_ALL_BAKS_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "clearAllBaksDisable.png");
    public final static ImageIcon ICON_TABLE_FIELD = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "tableFiled.png");
    public final static ImageIcon ICON_TABLE_FIELD_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "tableFiledEnable.png");
    public final static ImageIcon ICON_TABLE_FIELD_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "tableFiledDisable.png");
    public final static ImageIcon ICON_CLEAR_LOG = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "clearLog.png");
    public final static ImageIcon ICON_CLEAR_LOG_ENABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "clearLogEnable.png");
    public final static ImageIcon ICON_CLEAR_LOG_DISABLE = new ImageIcon(
            CURRENT_DIR + File.separator + "icon" + File.separator + "clearLogDisable.png");

    /**
     * 
     */
    public final static int MAIN_H_GAP = 25;
    public final static Dimension LABLE_SIZE = new Dimension(1300, 30);
    public final static Dimension LABLE_SIZE_ITEM = new Dimension(78, 30);
    public final static Dimension LABLE_SIZE_ELE_ITEM = new Dimension(100, 30);
    public final static Dimension LABLE_SIZE_NULL_ITEM = new Dimension(320, 30);
    public final static Dimension LABLE_NULL_ITEM = new Dimension(80, 30);
    public final static Dimension LABLE_SIZE_CASECRE_NULL_ITEM = new Dimension(500, 30);
    public final static Dimension LABLE_SIZE_CASECRE_NULL_SEC_ITEM = new Dimension(130, 30);
    public final static Dimension LABLE_SIZE_CASE_NULL_ITEM = new Dimension(350, 30);
    public final static Dimension TEXT_FIELD_SIZE_ITEM = new Dimension(190, 24);
    public final static Dimension TEXT_COMBOX_SIZE_ITEM = new Dimension(130, 24);
    public final static Dimension RADIO_SIZE = new Dimension(1300, 60);
    public final static Dimension PANEL_ITEM_SIZE = new Dimension(1300, 40);

}
