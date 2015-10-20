/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.actions;

import com.fr.design.gui.controlpane.NameObjectCreator;

import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-2-26
 * Time: ����10:47
 */
public abstract class HyperlinkPluginAction extends UpdateAction {
    public static final String XML_TAG = "hyperlinkType";

    public HyperlinkPluginAction() {
        this.setName(this.getDisplayName());
    }


    /**
     * ����
     *
     * @return
     */
    public abstract String getDisplayName();

    /**
     * �õ��������ӵ�������
     *
     * @return
     */
    public abstract Class getHyperlinkClass();

    /**
     * �õ���Ӧ������
     *
     * @return
     */
    public abstract Class getUpdateHyperlinkPaneClass();

    public NameObjectCreator getHyperlinkCreator() {
        return new NameObjectCreator(getDisplayName(), getHyperlinkClass(), getUpdateHyperlinkPaneClass());
    }

    /**
     * ����
     * @param e ����
     */
    public void actionPerformed(ActionEvent e) {
    }
}
