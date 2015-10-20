package com.fr.file.filter;

import com.fr.file.FILE;

public interface FILEFilter {

    /**
     *�ļ��Ƿ�֧��
     * @param f �ļ�
     * @return ֧���򷵻�true
     */
    public boolean accept(FILE f);

    /**
     * ȡ����
     * @return ����
     */
    public String getDescription();

    /**
     * �Ƿ����Ҳ������չ
     * @param extension  ��չ
     * @return �����򷵻�true
     */
    public boolean containsExtension(String extension);
    
}
