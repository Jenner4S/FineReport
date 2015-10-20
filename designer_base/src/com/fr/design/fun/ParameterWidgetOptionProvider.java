package com.fr.design.fun;

import com.fr.form.ui.Widget;

/**
 * @author : richie
 * @since : 8.0
 * �Զ����������ؼ�
 */
public interface ParameterWidgetOptionProvider {

    public static final String XML_TAG = "ParameterWidgetOptionProvider";

    /**
     * �Զ�������ؼ���ʵ���࣬������Ҫ�̳���com.fr.form.ui.Widget
     * @return �ؼ���
     */
    public Class<? extends Widget> classForWidget();

    /**
     * �Զ�������ؼ�����ƽ����࣬������Ҫ�̳���com.fr.form.designer.creator.XWidgetCreator
     * @return �ؼ���ƽ�����
     */
    public Class<?> appearanceForWidget();

    /**
     * �Զ�������ؼ�������������ϵ�ͼ��·��
     * @return ͼ�����ڵ�·��
     */
    public String iconPathForWidget();

    /**
     * �Զ�������ؼ�������
     * @return �ؼ�����
     */
    public String nameForWidget();
}
