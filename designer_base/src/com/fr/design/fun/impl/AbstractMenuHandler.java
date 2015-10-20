package com.fr.design.fun.impl;

import com.fr.design.fun.MenuHandler;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.ShortCut;
import com.fr.general.ComparatorUtils;

/**
 * @author richie
 * @date 2015-05-13
 * @since 8.0
 */
public abstract class AbstractMenuHandler implements MenuHandler {

    public boolean equals(Object obj) {
        return obj instanceof AbstractMenuHandler
                && ComparatorUtils.equals(category(), ((AbstractMenuHandler) obj).category())
                && shortCutEquals(this, (AbstractMenuHandler)obj);
    }

    private boolean shortCutEquals(AbstractMenuHandler target, AbstractMenuHandler self){
        ShortCut targetShortCut = target.shortcut();
        ShortCut selfShortCut = self.shortcut();

        if (targetShortCut == null && selfShortCut == null){
            return true;
        }

        if (targetShortCut != null && selfShortCut != null){
            return ComparatorUtils.equals(targetShortCut.getClass(), selfShortCut.getClass());
        }

        return false;
    }

    /**
     * ��ȡ��ǰ�˵���Ӧ��Action
     * ����Ҫѡ�ж���, (�ļ�, ������, ����)
     *
     * @return �˵�Action
     *
     */
    public ShortCut shortcut(){
        return null;
    }

    /**
     * ��ȡ��ǰ�˵���Ӧ��Action
     *
     * @param plus ��ǰѡ�еĶ���(ģ��)
     *
     * @return �˵�Action
     *
     */
    public ShortCut shortcut(ToolBarMenuDockPlus plus){
        return null;
    }
}
