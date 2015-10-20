package com.fr.design.file;


public interface FileOperations {
    /**
     *��ѡ�еı����ļ�
     */
	public void openSelectedReport();

    /**
     *���ļ���
     */
	public void openContainerFolder();

    /**
     *ˢ��
     */
	public void refresh();

    /**
     *ɾ���ļ�
     */
	public void deleteFile();

    /**
     *�����ļ���
     */
	public void lockFile();

    /**
     *�ļ�����
     */
	public void unLockFile();

	public String getSelectedTemplatePath();

    /**
     *�ļ����Ƿ����
     * @param newName ԭ��
     * @param oldName �µ��ļ���
     * @param suffix ��׺��
     * @return �Ƿ����
     */
	public boolean isNameAlreadyExist(String newName, String oldName, String suffix);
}
