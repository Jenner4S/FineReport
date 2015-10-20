package com.fr.design.fun;

import com.fr.data.impl.Connection;
import com.fr.design.beans.BasicBeanPane;

/**
 * @author : richie
 * @since : 8.0
 */
public interface ConnectionProvider {

    public static final String XML_TAG = "ConnectionProvider";

    /**
     * �������ӵ����˵�������
     * @return ����
     */
    public String nameForConnection();

    /**
     * �������ӵ����˵���ͼ��
     * @return ͼ��·��
     */
    public String iconPathForConnection();

    /**
     * �������ӵ�����
     * @return ��������
     */
    public Class<? extends com.fr.data.impl.Connection> classForConnection();

    /**
     * �������ӵ���ƽ���
     * @return ��ƽ���
     */
    public Class<? extends BasicBeanPane<? extends Connection>> appearanceForConnection();
}
