package com.fr.file;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.io.XMLEncryptUtils;
import com.fr.design.gui.itree.filetree.FileNodeComparator;
import com.fr.design.gui.itree.filetree.FileTreeIcon;
import com.fr.file.filetree.FileNode;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.CoreConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.project.ProjectConstants;

import javax.swing.*;

import java.io.InputStream;
import java.io.OutputStream;

public class FileNodeFILE implements FILE {

    private FileNode node;
    // carl����¼��FILE��Ӧ�����л���,ÿ�δ����������µ�ǰ�����л���
    private String envPath;

    public FileNodeFILE(FileNodeFILE parent, String name, boolean isDir) {
        FileNode fn = parent.node;
        String parentDir;
        if (fn.isDirectory()) {
            parentDir = fn.getEnvPath();
        } else {
            parentDir = fn.getParent();
        }

        this.node = new FileNode(StableUtils.pathJoin(new String[]{
                parentDir, name
        }), isDir);
        this.envPath = FRContext.getCurrentEnv().getPath();
    }

    public FileNodeFILE(FileNode node) {
        this.node = node;
        this.envPath = FRContext.getCurrentEnv().getPath();
    }

    public FileNodeFILE(String envPath) {
        this.node = null;
        this.envPath = envPath;
    }

    public FileNodeFILE(FileNode node, String envPath) {
        this.node = node;
        this.envPath = envPath;
    }

    /**
     * prefix ��׺
     *
     * @return ���غ�׺
     */
    public String prefix() {
        if (ComparatorUtils.equals(getEnvPath(), FRContext.getCurrentEnv().getWebReportPath())) {
            return FILEFactory.WEBREPORT_PREFIX;
        }
        return FILEFactory.ENV_PREFIX;
    }

    /**
     * @return
     */
    public String getEnvPath() {
        return this.envPath;
    }

    /**
     * �Ƿ���Ŀ¼
     *
     * @return ���򷵻�true
     */
    public boolean isDirectory() {
        return ComparatorUtils.equals(node, null) ? true : node.isDirectory();
    }

    /**
     * @return
     */
    public String getName() {
        if (node == null) {
            return null;
        }

        if (ComparatorUtils.equals(node.getEnvPath(), ProjectConstants.REPORTLETS_NAME)) {
            return Inter.getLocText("Utils-Report_Runtime_Env");
        } else {
            return node.getName();
        }
    }

    /**
     * @return
     */
    public Icon getIcon() {
        if (node == null) {
            return null;
        }

        if (ComparatorUtils.equals(node.getEnvPath(), ProjectConstants.REPORTLETS_NAME)) {
            return BaseUtils.readIcon("/com/fr/base/images/oem/logo.png");
        } else {
            return FileTreeIcon.getIcon(node);
        }
    }

    /**
     * @return
     */
    public String getPath() {
        if (node == null) {
            return "";
        }

        return node.getEnvPath();
    }

    /**
     * @param path
     */
    public void setPath(String path) {
        node.setEnvPath(path);
    }

    /**
     * @return
     */
    public FILE getParent() {
        if (node == null) {
            return null;
        }

        return new FileNodeFILE(new FileNode(node.getParent(), true));
    }

    /**
     * �ļ�
     *
     * @return �ļ���
     */
    public FILE[] listFiles() {
        if (ComparatorUtils.equals(node, null)) {
            node = new FileNode(CoreConstants.SEPARATOR, true);
            //return new FILE[0];
        }
        if (!node.isDirectory()) {
            return new FILE[]{this};
        }

        try {
            FileNode[] node_array;
            node_array = listFile(node.getEnvPath());
            java.util.Arrays.sort(node_array, new FileNodeComparator());

            FILE[] res_array = new FILE[node_array.length];
            for (int i = 0; i < node_array.length; i++) {
                res_array[i] = new FileNodeFILE(node_array[i], envPath);
            }

            return res_array;
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
            return new FILE[0];
        }
    }

    /**
     * ��Ŀ¼�ļ���ַ
     *
     * @param rootFilePath ���ļ�·��
     * @return �����ļ��ڵ�
     */
    private FileNode[] listFile(String rootFilePath) {
        if (ComparatorUtils.equals(envPath, FRContext.getCurrentEnv().getWebReportPath())) {
            try {
                return FRContext.getCurrentEnv().listReportPathFile(rootFilePath);
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        } else {
            try {
                return FRContext.getCurrentEnv().listFile(rootFilePath);
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
        return new FileNode[0];
    }

    /**
     * �����ļ���
     *
     * @param name �ļ�������
     * @return �����ɹ�����true
     */
    public boolean createFolder(String name) {
        if (ComparatorUtils.equals(node, null) || !node.isDirectory()) {
            return false;
        }

        try {
            return FRContext.getCurrentEnv().createFolder(StableUtils.pathJoin(new String[]{
                    node.getEnvPath(), name
            }));
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * �Ƿ���ס
     *
     * @return �ļ���������true
     */
    public boolean isLocked() {
        if (node == null) {
            return false;
        }

        try {
            return FRContext.getCurrentEnv().fileLocked(node.getEnvPath());
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * �Ƿ����
     *
     * @return �ļ����ڷ��� true
     */
    public boolean exists() {
        if (node == null) {
            return false;
        }
        // �����л�����һ�£�����false
        if (!isCurrentEnv()) {
            return false;
        }

        try {
            return FRContext.getCurrentEnv().fileExists(node.getEnvPath());
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * �Ƿ��ǵ�ǰ����
     *
     * @return �Ǳ���ǰ��������true
     */
    public boolean isCurrentEnv() {
        return ComparatorUtils.equals(FRContext.getCurrentEnv().getPath(), envPath);
    }

    /**
     * �����ļ�
     *
     * @return �ɹ�����true
     */
    public boolean mkfile() {
        if (node == null) {
            return false;
        }

        try {
            return FRContext.getCurrentEnv().createFile(node.getEnvPath());
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * ��Ϊ������
     *
     * @return ������
     * @throws Exception
     */
    public InputStream asInputStream() throws Exception {
        if (node == null) {
            return null;
        }

        String envPath = node.getEnvPath();
        // envPath������reportlets��ͷ
        if (!envPath.startsWith(ProjectConstants.REPORTLETS_NAME)) {
            return null;
        }

        InputStream in = FRContext.getCurrentEnv().readBean(
                envPath.substring(ProjectConstants.REPORTLETS_NAME.length() + 1),
                ProjectConstants.REPORTLETS_NAME
        );
        
        return envPath.endsWith(".cpt") || envPath.endsWith(".frm")
                ? XMLEncryptUtils.decodeInputStream(in) : in;
    }
    
    /**
     * ��Ϊ�����
     *
     * @return ���������
     * @throws Exception
     */
    public OutputStream asOutputStream() throws Exception {
        if (ComparatorUtils.equals(node, null)) {
            return null;
        }

        String envPath = node.getEnvPath();
        // envPath������reportlets��ͷ
        if (!envPath.startsWith(ProjectConstants.REPORTLETS_NAME)) {
            return null;
        }
        return FRContext.getCurrentEnv().writeBean(
                envPath.substring(ProjectConstants.REPORTLETS_NAME.length() + 1),
                ProjectConstants.REPORTLETS_NAME
        );
    }

    /**
     * �ر�ģ��
     *
     * @throws Exception
     */
    public void closeTemplate() throws Exception {
        if (node == null) {
            return;
        }

        String envPath = node.getEnvPath();
        // envPath������reportlets��ͷ
        if (!envPath.startsWith(ProjectConstants.REPORTLETS_NAME)) {
            return;
        }

        FRContext.getCurrentEnv().unlockTemplate(
                envPath.substring(ProjectConstants.REPORTLETS_NAME.length() + 1));
    }

    /**
     * �õ�������ȫ��
     *
     * @return
     */
    public String getEnvFullName() {
        return this.node.getEnvPath().substring(ProjectConstants.REPORTLETS_NAME.length() + 1);
    }

    /**
     * �Ƿ����ڴ��ļ�
     *
     * @return ���򷵻�true
     */
    public boolean isMemFile() {
        return false;
    }

    /**
     * �Ƿ��ǻ����ļ�
     *
     * @return ���򷵻�true
     */
    public boolean isEnvFile() {
        return true;
    }

    /**
     * �Ƿ���ͬ
     *
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof FileNodeFILE)) {
            return false;
        }

        return ComparatorUtils.equals(this.envPath, ((FileNodeFILE) obj).envPath) && ComparatorUtils.equals(this.node, ((FileNodeFILE) obj).node);
    }

    /**
     * ����hash��
     *
     * @return ����hash��
     */
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + (this.node != null ? this.node.hashCode() : 0);
        hash = 61 * hash + (this.envPath != null ? this.envPath.hashCode() : 0);
        return hash;
    }

    /**
     * ��Ϊ�ַ�������
     *
     * @return String  �ַ���
     */
    public String toString() {
        return prefix() + (this.node != null ? this.node.getEnvPath() : "");
    }
}
