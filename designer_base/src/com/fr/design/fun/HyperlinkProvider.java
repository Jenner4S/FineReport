package com.fr.design.fun;

import com.fr.design.gui.controlpane.NameableCreator;

/**
 * Created by zack on 2016/1/20.
 */
public interface HyperlinkProvider {
    public static final String XML_TAG = "HyperlinkProvider";

    /**
     * 创建一个超级连接类型
     * @return  NameableCreator
     */
    public NameableCreator createHyperlinkCreator();
}
