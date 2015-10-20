/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.data.datapane.connect;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.gui.controlpane.NameObjectCreator;

import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-2-25
 * Time: ����5:05
 */
public abstract class DataConnectionAction extends UpdateAction {
    public static final String XML_TAG = "connectionType";

    public DataConnectionAction() {
        this.setName(this.getDisplayName());
        this.setSmallIcon(BaseUtils.readIcon(this.getIconPath()));
    }

    /**
     * ����
     *
     * @return
     */
    public abstract String getDisplayName();

    /**
     * ͼ��
     *
     * @return
     */
    public abstract String getIconPath();

    /**
     * �õ��������ӵ�������
     *
     * @return
     */
    public abstract Class getConnectionClass();

    /**
     * �õ���Ӧ������
     *
     * @return
     */
    public abstract Class getUpdateConnectionPaneClass();


    public NameObjectCreator getConnectionCreator() {
        return new NameObjectCreator(getDisplayName(), getIconPath(), getConnectionClass(), getUpdateConnectionPaneClass());
    }

    /**
     * ����
     * @param e ����
     */
    public void actionPerformed(ActionEvent e) {
    }
}
