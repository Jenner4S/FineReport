package com.fr.file;

import javax.swing.Icon;

public interface FILE {

    /**
     * ��׺
     * @return ��׺
     */
	public String prefix();

    /**
     * �Ƿ���Ŀ¼
     * @return ���򷵻�true
     */
	public boolean isDirectory();
	
	// Name
	public String getName();
	
	// Icon
	public Icon getIcon();
	
	// ��ǰĿ¼��Path
	public String getPath();
	
	public void setPath(String path);
	
	// ȡ��ǰĿ¼���ϼ�Ŀ¼
	public FILE getParent();

    /**
     * �г���ǰĿ¼�����е��ļ����ļ���
     * @return �ļ�
     */
	public FILE[] listFiles();

    /**
     * �½�һ��Ŀ¼
     * @param name ����
     * @return �½�Ŀ¼
     */
	public boolean createFolder(String name);

    /**
     * �½��ļ�
     * @return �Ƿ��½��ɹ�
     * @throws Exception �쳣
     */
	public boolean mkfile() throws Exception;

    /**
     * �Ƿ����
     * @return �Ƿ����
     */
	public boolean exists();

    /**
     * �ر��ļ�
     * @throws Exception �쳣
     */
	public void closeTemplate() throws Exception;

    /**
     * ��Ϊ������
     * @return ������
     * @throws Exception �쳣
     */
	public java.io.InputStream asInputStream() throws Exception;

    /**
     * ��Ϊ�����
     * @return �����
     * @throws Exception �쳣
     */
	public java.io.OutputStream asOutputStream() throws Exception;
	
	public String getEnvFullName();


    /**
     * �Ƿ����ڴ��ļ�
     * @return ���򷵻�true
     */
	public boolean isMemFile();

    /**
     * �Ƿ��ǻ����ļ�
     * @return ���򷵻�true
     */
    public boolean isEnvFile();
}
