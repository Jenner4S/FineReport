package com.fr.design.mainframe.toolbar;

import javax.swing.*;

import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;

public interface ToolBarMenuDockPlus {
    /**
     * ģ��Ĺ���
     *
     * @return ����
     */
	public ToolBarDef[] toolbars4Target();

    /**
     * �ļ��˵����Ӳ˵�
     *
     * @return �Ӳ˵�
     */
	public ShortCut[] shortcut4FileMenu();

    /**
     * Ŀ��Ĳ˵�
     *
     * @return �˵�
     */
	public MenuDef[] menus4Target();

    /**
     * ���Ĺ�����
     *
     * @return ��������
     */
	public JPanel[] toolbarPanes4Form();

    /**
     * ���Ĺ��߰�ť
     *
     * @return ���߰�ť
     */
	public JComponent[] toolBarButton4Form();

    /**
     * Ȩ��ϸ����״̬�µĹ������
     *
     * @return �������
     */
	public JComponent toolBar4Authority();

    public int getMenuState();

    public int getToolBarHeight();

    /**
     * �����˵����Ӳ˵� ��Ŀǰ����ͼ�������
     *
     * @return �Ӳ˵�
     */
	public ShortCut[] shortcut4ExportMenu();
	
}
