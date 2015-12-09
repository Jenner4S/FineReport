package com.fr.design.fun;

import com.fr.form.ui.Widget;

/**
 * @author : focus
 * @since : 8.0
 * �Զ���web�������˵�
 */
public interface ToolbarItemProvider {

    String XML_TAG = "ToolbarItemProvider";

    /**
     * �Զ���web�������˵�ʵ���࣬������Լ̳���com.fr.form.ui.ToolBarMenuButton ���� com.fr.form.ui.ToolBarButton;
     *
     * @return �˵���
     */
    Class<? extends Widget> classForWidget();

    /**
     * �Զ���web�������˵�������������ϵ�ͼ��·��
     *
     * @return ͼ�����ڵ�·��
     */
    String iconPathForWidget();

    /**
     * �Զ���web�������˵������������ʾ������
     *
     * @return �˵���
     */
    String nameForWidget();

}
