package com.fr.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

import com.fr.base.io.XMLEncryptUtils;
import com.fr.design.gui.itree.filetree.FileComparator;
import com.fr.design.gui.itree.filetree.FileTreeIcon;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogManager;
import com.fr.stable.StableUtils;
import com.fr.stable.project.ProjectConstants;

public class FileFILE implements FILE {

    private File file;

    public FileFILE(FileFILE parent, String name) {
        this(new File(parent.file, name));
    }

    public FileFILE(File file) {
        this.file = file;
    }

    /**
     * ��׺
     *
     * @return ��׺
     */
    public String prefix() {
        return FILEFactory.FILE_PREFIX;
    }

    /**
     * �Ƿ���Ŀ¼
     *
     * @returns ���򷵻�true
     */
    public boolean isDirectory() {
        return file == null ? false : file.isDirectory();
    }

    @Override
    public String getName() {
        if (file == null) {
            return "";
        }
        FileSystemView view = FileSystemView.getFileSystemView();
        return view.getSystemDisplayName(file);
    }

    public String getTotalName() {
        if (file == null) {
            return "";
        }

        return file.getName();
    }

    @Override
    public Icon getIcon() {
        if (file == null) {
            return FileTreeIcon.BLANK_IMAGE_ICON;
        }
        FileSystemView view = FileSystemView.getFileSystemView();
        try {
            return view.getSystemIcon(file);
        } catch (Exception e) {
            /*
             * alex:����ʾMac����ĳ��Ŀ¼��ʱ��,��Ȼ����
             * java.io.FileNotFoundException: File F:\.HFS+ Private Directory Data
             */
            return FileTreeIcon.BLANK_IMAGE_ICON;
        }
    }

    @Override
    public String getPath() {
        if (file == null) {
            return "";
        }

        return file.getAbsolutePath();
    }

    @Override
    public void setPath(String path) {
        file = new File(path);
    }

    @Override
    public FILE getParent() {
        if (file == null) {
            return null;
        }

        return new FileFILE(file.getParentFile());
    }

    /**
     * �г���ǰĿ¼�����е��ļ����ļ���
     *
     * @return �ļ�
     */
    public FILE[] listFiles() {
        if (file == null) {
            return new FILE[0];
        }

        if (!file.isDirectory()) {
            return new FILE[]{this};
        }

        File[] file_array = file.listFiles();
        if (file_array == null) {
            return new FILE[0];
        }
        java.util.Arrays.sort(file_array, new FileComparator());

        java.util.List<FILE> res_list = new ArrayList<FILE>(file_array.length);

        for (int i = 0; i < file_array.length; i++) {
            // ��Ϊ��һЩϵͳ�ļ�,���������ڴ��,����listFiles��ʱ�����,��ȴnot exists
            if (file_array[i].exists()) {
                res_list.add(new FileFILE(file_array[i]));
            }
        }

        return res_list.toArray(new FILE[res_list.size()]);
    }

    /**
     * �½�һ��Ŀ¼
     *
     * @param name ����
     * @return �½�Ŀ¼
     */
    public boolean createFolder(String name) {
        if (file == null || !file.isDirectory()) {
            return false;
        }

        File new_file = new File(StableUtils.pathJoin(new String[]{
                file.getAbsolutePath(), name
        }));

        if (new_file.exists()) {
            return false;
        }

        return new_file.mkdir();
    }

    /**
     * �Ƿ����
     *
     * @return �Ƿ����
     */
    public boolean exists() {
        return file == null ? false : file.exists();
    }

    /**
     * �Ƿ����
     *
     * @return �Ƿ����
     */
    public boolean mkfile() throws IOException {
        return StableUtils.makesureFileExist(file);
    }

    /**
     * ��Ϊ������
     *
     * @return ������
     * @throws Exception �쳣
     */
    public InputStream asInputStream() throws Exception {
        InputStream in = new java.io.FileInputStream(file);
        return file.getName().endsWith(".cpt") || file.getName().endsWith(".frm")
                ? XMLEncryptUtils.decodeInputStream(in) : in;
    }

    /**
     * ��Ϊ�����
     *
     * @return �����
     * @throws Exception �쳣
     */
    public OutputStream asOutputStream() throws Exception {
        if (file == null || !file.exists()) {
            return null;
        }
        FRLogManager.declareResourceWriteStart(file.getAbsolutePath());
        java.io.OutputStream out = null;
        try {
            out = new FileOutputStream(file);
        } catch (Exception e) {
            throw FRLogManager.createLogPackedException(e);
        }
        return out;
    }

    /**
     * �ر��ļ�
     *
     * @throws Exception �쳣
     */
    public void closeTemplate() throws Exception {
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FileFILE)) {
            return false;
        }

        return ComparatorUtils.equals(this.file, ((FileFILE) obj).file);
    }

    /**
         * ����hash��
         *
         * @return ����hash��
         */
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.file != null ? this.file.hashCode() : 0);
        return hash;
    }

    /**
     * string����
     *
     * @return �ַ���
     */
    public String toString() {
        return this.prefix() + (this.file == null ? "" : this.file.getAbsolutePath());
    }

    @Override
    public String getEnvFullName() {
        String[] nodes = file.getAbsolutePath().split(ProjectConstants.REPORTLETS_NAME);
        return nodes[nodes.length - 1].substring(1);
    }

    /**
         * �Ƿ����ڴ��ļ�
         * @return ���򷵻�true
         */
    public boolean isMemFile() {
        return false;
    }

    /**
         * �Ƿ��ǻ����ļ�
         * @return ���򷵻�true
         */
    public boolean isEnvFile() {
        return false;
    }
}
