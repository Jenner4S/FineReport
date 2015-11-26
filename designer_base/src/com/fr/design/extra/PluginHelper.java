package com.fr.design.extra;

import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.general.*;
import com.fr.general.http.HttpClient;
import com.fr.plugin.Plugin;
import com.fr.plugin.PluginLoader;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StableUtils;
import com.fr.stable.xml.XMLTools;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * @author richie
 * @date 2015-03-10
 * @since 8.0
 */
public class PluginHelper {
    private static final String TEMP_PATH = System.getProperty("user.dir") + "/tmp";
    private static final String DOWNLOAD_PATH = System.getProperty("user.dir") + "/download";
    private static final String TEMP_FILE = "temp.zip";

    /**
     * ���ز��
     *
     * @param id ���id
     * @param p ���ذٷֱȴ���
     *
     */
    public static void downloadPluginFile(String id, Process<Double> p) throws Exception {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        HttpClient httpClient = new HttpClient(PluginConstants.PLUGIN_DOWNLOAD_URL, map);
        if (httpClient.getResponseCode() == HttpURLConnection.HTTP_OK) {
            int totalSize = httpClient.getContentLength();
            InputStream reader = httpClient.getResponseStream();
            String temp = StableUtils.pathJoin(DOWNLOAD_PATH, TEMP_FILE);
            StableUtils.makesureFileExist(new File(temp));
            FileOutputStream writer = new FileOutputStream(temp);
            byte[] buffer = new byte[PluginConstants.BYTES_NUM];
            int bytesRead = 0;
            int totalBytesRead = 0;

            while ((bytesRead = reader.read(buffer)) > 0) {
                writer.write(buffer, 0, bytesRead);
                buffer = new byte[PluginConstants.BYTES_NUM];
                totalBytesRead += bytesRead;
                p.process(totalBytesRead / (double) totalSize);
            }
            reader.close();
            writer.flush();
            writer.close();
        }
    }

    public static File getDownloadTempFile() {
        return new File(StableUtils.pathJoin(DOWNLOAD_PATH, TEMP_FILE));
    }

    /**
     * ��ѹ���ļ��ж�ȡ�����Ϣ
     *
     * @param chosenFile ѡ���ѹ���ļ�
     * @return ���ز������
     * @throws Exception ��ȡ�����Ϣʧ�����׳��쳣
     */
    public static Plugin readPlugin(File chosenFile) throws Exception {
        // ��Ҫ��ɾ����ʱĿ¼��֤��ѹ�������ļ�����Ͱ�װʧ�ܵ��ļ���ϵ�һ��
        StableUtils.deleteFile(new File(TEMP_PATH));

        IOUtils.unzip(chosenFile, TEMP_PATH);
        File pluginFileDir = getTempPluginFileDirectory();
        if (pluginFileDir == null) {
            return null;
        }
        Plugin plugin = null;
        if (pluginFileDir.isDirectory()) {
            File[] pluginFiles = pluginFileDir.listFiles();
            if (ArrayUtils.isNotEmpty(pluginFiles)) {
                for (File f : pluginFiles) {
                    if (f.getName().equals("plugin.xml")) {
                        FileInputStream in = new FileInputStream(f);
                        plugin = new Plugin();
                        XMLTools.readInputStreamXML(plugin, in);
                        if (!plugin.isValidate()) {
                            return null;
                        }
                        in.close();
                        break;
                    }
                }
            }
        }
        return plugin;
    }

    /**
     * ��ѡ�е�ѹ���ļ��а�װ���
     *
     * @param chosenFile ѡ���ѹ���ļ�
     * @param after ��������¼�
     * @throws Exception ��װʧ�����׳��쳣
     */
    public static void installPluginFromDisk(File chosenFile, After after) throws Exception {
        Plugin plugin = readPlugin(chosenFile);
        installPluginFromUnzippedTempDir(FRContext.getCurrentEnv(), plugin, after);
    }

    /**
     * ��ѹ���ļ��и���Restart*.class��restart.exe��binĿ¼��
     * @param file ����ļ�
     * @param plugin ���
     * @throws Exception
     */
    public static void copyFilesToBinFolder(File file, Plugin plugin) throws Exception {
        File[] pluginFiles = file.listFiles();
        for(int i = 0; i < pluginFiles.length; ++i) {
            File restartFile = pluginFiles[i];
            if(restartFile.getAbsolutePath().endsWith(".class")) {
                String installHome = StableUtils.getInstallHome();
                IOUtils.copy(restartFile, new File(StableUtils.pathJoin(new String[]{installHome, "bin"})));
            }
        }
    }

    /**
     * �Ӳ��ѹ������ѹ������ʱ�ļ��а�װ���
     *
     * @param env    �������л���
     * @param plugin ���
     * @param after ��������¼�
     * @throws Exception
     */
    public static void installPluginFromUnzippedTempDir(Env env, final Plugin plugin, final After after) throws Exception {
        if (plugin == null) {
            throw new Exception(Inter.getLocText("FR-Designer-Plugin_Illegal_Plugin_Zip_Cannot_Be_Install"));
        }
        if (PluginLoader.getLoader().isInstalled(plugin)) {
            throw new Exception(Inter.getLocText("FR-Designer-Plugin_Has_Been_Installed"));
        }
        if (plugin.isJarExpired()) {
            String jarExpiredInfo = Inter.getLocText(new String[]{"FR-Designer-Plugin_Jar_Expired", ",", "FR-Designer-Plugin_Install_Failed", ",", "FR-Designer-Plugin_Please_Update_Jar", plugin.getRequiredJarTime()});
            FRLogger.getLogger().error(jarExpiredInfo);
            throw new Exception(jarExpiredInfo);
        }
        if (plugin.isValidate()) {
            File file = getTempPluginFileDirectory();
            env.copyFilesToPluginAndLibFolder(file, plugin);
            copyFilesToBinFolder(file, plugin);
            env.movePluginEmbFile(file, plugin);
        }
        // ɾ���Ž�ѹ�ļ�����ʱ�ļ���
        StableUtils.deleteFile(new File(TEMP_PATH));
        new SwingWorker<String, Void>() {

            @Override
            protected String doInBackground() throws Exception {
                return sendInstalledPluginInfo(plugin);
            }

            @Override
            protected void done() {
                try {
                    String text = get();
                    FRLogger.getLogger().info("plugin install:" + text);
                } catch (InterruptedException e) {
                    FRLogger.getLogger().error(e.getMessage(), e);
                } catch (ExecutionException e) {
                    FRLogger.getLogger().error(e.getMessage(), e);
                }
                if (after != null) {
                    after.done();
                }
            }
        }.execute();
    }

    /**
     * ��ȡ�����ѹ����ʱ�ļ���
     *
     * @return ��ʱ�ļ�
     */
    public static File getTempPluginFileDirectory() {
        File file = new File(TEMP_PATH);
        if (file.isDirectory() && !file.getName().startsWith(".")) {
            File[] files = file.listFiles();
            if (ArrayUtils.isNotEmpty(files)) {
                for (File f : files) {
                    if (foundConfigFile(f)) {
                        return f;
                    }
                }
            }
        }
        return null;
    }

    private static boolean foundConfigFile(File dir) {
        if (!dir.isDirectory()) {
            return false;
        }
        File[] files = dir.listFiles();
        if (ArrayUtils.isNotEmpty(files)) {
            for (File f : files) {
                if ("plugin.xml".equals(f.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * �����л�����ж�ز��
     *
     * @param env    �������л���
     * @param plugin ���
     * @throws Exception ж�س����ʱ���׳����쳣
     *
     * @return  ����û��ɾ�������ļ��ļ���
     */
    public static String[] uninstallPlugin(Env env, Plugin plugin) throws Exception {
        if (plugin == null || env == null) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        PluginLoader.getLoader().deletePlugin(plugin);
        return env.deleteFileFromPluginAndLibFolder(plugin);
    }

    /**
     * �Ƚϲ���İ汾������򵥵ıȼ��ַ���������Ҫ��������Ϊ���
     *
     * @param plugin    ��ǰ�Ĳ��
     * @param oldPlugin �ϵĲ��
     * @return ��ǰ������ϵĲ���汾���򷵻�true�����򷵻�false
     */
    public static boolean isNewThan(Plugin plugin, Plugin oldPlugin) {
        return ComparatorUtils.compare(plugin.getVersion(), oldPlugin.getVersion()) > 0;
    }

    private static String sendInstalledPluginInfo(final Plugin plugin) {
        if (StableUtils.isDebug()) {
            return "debug status";
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("key", DesignerEnvManager.getEnvManager().getActivationKey());
        map.put("detail", plugin.toJSONObject().toString());
        map.put("build", GeneralUtils.readBuildNO());
        HttpClient httpClient = new HttpClient(PluginConstants.PLUGIN_INSTALL_INFO, map);
        httpClient.setTimeout(TIME_OUT);
        httpClient.asGet();
        return httpClient.getResponseText();
    }

    private static final int TIME_OUT = 5000;
}
