package com.fr.design;

import com.fr.design.mainframe.DesignerContext;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.GeneralUtils;
import com.fr.stable.ArrayUtils;
import com.fr.stable.OperatingSystem;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author richie
 * @date 2015-03-26
 * @since 8.0
 */
public class RestartHelper {

    public static final String RECORD_FILE = StableUtils.pathJoin(StableUtils.getInstallHome(), "delete.properties");
    public static final String MOVE_FILE = StableUtils.pathJoin(StableUtils.getInstallHome(), "move.properties");


    /**
     * ��Ҫɾ�����ļ�����¼��delete.properties��
     *
     * @param files Ҫɾ�����ļ�
     */
    public static void saveFilesWhichToDelete(String[] files) {
        Properties properties = new Properties();
        File file = new File(RECORD_FILE);
        if (file.exists()) {
            try {
                FileInputStream file2DeleteInputStream = new FileInputStream(file);
                properties.load(file2DeleteInputStream);
                file2DeleteInputStream.close();
            } catch (IOException e) {
                FRLogger.getLogger().error(e.getMessage(), e);
            }
        }
        if (ArrayUtils.getLength(files) != 0) {
            int size = properties.values().size();
            for (int i = 0, len = files.length; i < len; i++) {
                properties.setProperty((i + size) + "", files[i]);
            }
        }
        try {
            FileOutputStream file2DeleteOutputStream = new FileOutputStream(file);
            properties.store(file2DeleteOutputStream, "save");
            file2DeleteOutputStream.close();
        } catch (IOException e) {
            FRLogger.getLogger().error(e.getMessage(), e);
        }
    }

    /**
     * ��Ҫ�ƶ����ļ�����¼��move.properties��
     *
     * @param map �ƶ����ļ�
     */
    public static void saveFilesWhichToMove(Map<String, String> map) {
        Properties properties = new Properties();
        File file = new File(MOVE_FILE);
        if (file.exists()) {
            try {
                FileInputStream file2MoveInputStream = new FileInputStream(file);
                properties.load(file2MoveInputStream);
                file2MoveInputStream.close();
            } catch (IOException e) {
                FRLogger.getLogger().error(e.getMessage(), e);
            }
        }
        if (!map.isEmpty()) {
            for (String key : map.keySet()) {
                properties.setProperty(key, map.get(key));
            }
        }
        try {
            FileOutputStream file2MoveOutputStream = new FileOutputStream(file);
            properties.store(file2MoveOutputStream, "save");
            file2MoveOutputStream.close();
        } catch (IOException e) {
            FRLogger.getLogger().error(e.getMessage(), e);
        }
    }

    /**
     * ��Ҫ��������������ɾ���ļ�
     */
    public static void deleteRecordFilesWhenStart() {
        File ff = new File(RECORD_FILE);
        // ����ļ������ھ�ֱ�ӷ�����
        if (!ff.exists()) {
            return;
        }
        restart();
    }

    private static boolean deleteWhenDebug() {
        File ff = new File(RECORD_FILE);
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(ff));
        } catch (IOException ignore) {
            return true;
        }

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String filePath = GeneralUtils.objectToString(entry.getValue());
            File file = new File(filePath);
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        deleteFile(f);
                    }
                }
            }
            deleteFile(file);
        }
        return ff.delete();
    }

    private static boolean deleteFile(File f) {
        return StableUtils.deleteFile(f);
    }

    /**
     * ���������
     */
    public static void restart() {
        restart(ArrayUtils.EMPTY_STRING_ARRAY);
    }

    /**
     * �����������ɾ��ĳЩ�ض����ļ�
     *
     * @param filesToBeDelete Ҫɾ�����ļ�����
     */
    public static void restart(String[] filesToBeDelete) {
        String installHome = StableUtils.getInstallHome();
        if (StringUtils.isEmpty(installHome) || ComparatorUtils.equals(".", installHome)) {
            deleteWhenDebug();
            return;
        }

        try {
            if (OperatingSystem.isMacOS()) {
                restartInMacOS(installHome, filesToBeDelete);
            } else {
                restartInWindows(installHome, filesToBeDelete);
            }
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        } finally {
            DesignerContext.getDesignerFrame().exit();
        }
    }

    private static void restartInMacOS(String installHome, String[] filesToBeDelete) throws Exception {
        ProcessBuilder builder = new ProcessBuilder();
        List<String> commands = new ArrayList<String>();
        commands.add("open");
        commands.add(installHome + File.separator + "bin" + File.separator + "restart.app");
        if (ArrayUtils.isNotEmpty(filesToBeDelete)) {
            commands.add("--args");
            commands.add(StableUtils.join(filesToBeDelete, "+"));
        }
        builder.command(commands);
        builder.start();
    }

    private static void restartInWindows(String installHome, String[] filesToBeDelete) throws Exception {
        ProcessBuilder builder = new ProcessBuilder();
        List<String> commands = new ArrayList<String>();
        commands.add(installHome + File.separator + "bin" + File.separator + "restart.exe");
        if (ArrayUtils.isNotEmpty(filesToBeDelete)) {
            commands.add(StableUtils.join(filesToBeDelete, "+"));
        }
        builder.command(commands);
        builder.start();
    }
}
