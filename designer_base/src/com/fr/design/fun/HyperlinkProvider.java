package com.fr.design.fun;

import com.fr.design.gui.controlpane.NameableCreator;

/**
 * Created by zack on 2016/1/20.
 */
public interface HyperlinkProvider {
    public static final String XML_TAG = "HyperlinkProvider";

    /**
     * ����һ��������������
     * @return  NameableCreator
     */
    public NameableCreator createHyperlinkCreator();
}
