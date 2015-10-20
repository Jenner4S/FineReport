package com.fr.file;

import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.Icon;

public class MemFILE implements FILE {

    private String name;

    public MemFILE(String name) {
        this.name = name;
    }

    /**
     * �½�һ��Ŀ¼
     *
     * @param name ����
     * @return �½�Ŀ¼
     */
    public boolean createFolder(String name) {
        return false;
    }

    /**
     * �Ƿ����
     *
     * @return �Ƿ����
     */
    public boolean exists() {
        return false;
    }

    /**
     * �½��ļ�
     *
     * @return �Ƿ��½��ɹ�
     * @throws Exception �쳣
     */
    public boolean mkfile() throws Exception {
        return false;
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPath() {
        return name;
    }

    public String getEnvFullName() {
        return name;
    }

    /**
     * �Ƿ����ڴ��ļ�
     *
     * @return ���򷵻�true
     */
    public boolean isMemFile() {
        return true;
    }

    /**
     * �Ƿ��ǻ����ļ�
     *
     * @return ���򷵻�true
     */
    public boolean isEnvFile() {
        return false;
    }

    @Override
    public FILE getParent() {
        return null;
    }

    /**
     * �Ƿ���Ŀ¼
     *
     * @return ���򷵻�true
     */
    public boolean isDirectory() {
        return false;
    }

    /**
     * �г���ǰĿ¼�����е��ļ����ļ���
     *
     * @return �ļ�
     */
    public FILE[] listFiles() {
        return new FILE[0];
    }

    /**
     * ��׺
     *
     * @return ��׺
     */
    public String prefix() {
        return FILEFactory.MEM_PREFIX;
    }

    /**
     * string����
     *
     * @return �ַ���
     */
    public String toString() {
        return prefix() + this.name;
    }

    @Override
    public void setPath(String path) {
        this.name = path;
    }

    /**
     * �ر��ļ�
     *
     * @throws Exception �쳣
     */
    public void closeTemplate() throws Exception {
    }

    /**
     * ��Ϊ������
     *
     * @return ������
     * @throws Exception �쳣
     */
    public InputStream asInputStream() throws Exception {
        return null;
    }

    /**
     * ��Ϊ�����
     *
     * @return �����
     * @throws Exception �쳣
     */
    public OutputStream asOutputStream() throws Exception {
        return null;
    }
}
