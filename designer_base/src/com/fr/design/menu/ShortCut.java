package com.fr.design.menu;

import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

/**
 * ������MenuDef����ToolBarDef����Ӷ���ʱ�õĽӿ�
 * august:ShortCutû�б�Ҫ���л���XMLabled
 * ԭ����ô��Menudef���ṩ�־û�������̫�˷���Դ
 */
public abstract class ShortCut {

    private MenuKeySet menuKeySet = null;

    /**
     * ���Ĳ˵���ӵ�menul����ȥ
     * @param menu ��ӵ���menu
     */
    public abstract void intoJPopupMenu(JPopupMenu menu);

    /**
     * ���ò˵���ӵ�����������ȥ
     * @param toolBar ������
     */
    public abstract void intoJToolBar(JToolBar toolBar);


    public abstract void setEnabled(boolean b);

    /**
     * �Ƿ����
     * @return ����
     */
    public abstract boolean isEnabled();


    /**
     *��Ϊ�������˵�
     * @param scs �˵�
     * @return ������
     */
    public static final ToolBarDef asToolBarDef(ShortCut[] scs) {
        ToolBarDef def = new ToolBarDef();
        def.addShortCut(scs);

        return def;
    }


    public MenuKeySet getMenuKeySet() {
        return menuKeySet;
    }

    public void setMenuKeySet(MenuKeySet menuKeySet) {
        this.menuKeySet = menuKeySet;
    }
}
