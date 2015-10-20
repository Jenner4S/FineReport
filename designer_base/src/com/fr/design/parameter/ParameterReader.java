package com.fr.design.parameter;

import com.fr.base.Parameter;

/**
 * @author richie
 * @date 14/11/10
 * @since 8.0
 * ������ȡ�ӿڣ����Ը��ݲ�ͬ��ʵ�ֶ�ȡ��ͬ���͵�ģ�������ͼ����������Ⱦ����ʵ�֣�
 */
public interface ParameterReader {

    /**
     * ��ȡģ�����ָ��·���µ�ģ�����
     * @param tplPath ģ��·��
     * @return ��������
     */
    public Parameter[] readParameterFromPath(String tplPath);

    /**
     * ���ܵ�����
     * @param tplPath ģ��·��
     * @param acceptTypes ����
     * @return ������ܸ������ͣ���ִ�ж�ȡ����
     */
    public boolean accept(String tplPath, String... acceptTypes);

}
