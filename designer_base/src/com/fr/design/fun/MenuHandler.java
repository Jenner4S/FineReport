package com.fr.design.fun;

import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.ShortCut;

/**
 * @author richie
 * @date 2015-04-01
 * @since 8.0
 * ������˵�������ӿ�
 */
public interface MenuHandler {

    String MARK_STRING = "MenuHandler";

    int LAST = -1;

    String HELP = "help";
    String SERVER = "server";
    String FILE = "file";
    String TEMPLATE = "template";
    String INSERT = "insert";
    String CELL = "cell";

    /**
     * ����˵���λ��
     *
     * @param total �����λ��
     *
     * @return ����λ�ã������ŵ�����򷵻�-1
     */
    int insertPosition(int total);

    /**
     * �Ƿ��ڲ���Ĳ˵�ǰ����һ���ָ��
     * @return �Ƿ����ָ���
     */
    boolean insertSeparatorBefore();

    /**
     * �Ƿ��ڲ���Ĳ˵������һ���ָ��
     * @return �Ƿ����ָ���
     */
    boolean insertSeparatorAfter();

    /**
     * �����ķ���˵�
     * @return ����˵���
     */
    String category();

    /**
     * ����Ĳ˵�������
     * @return �˵�������
     */
    ShortCut shortcut();

    /**
     * ����Ĳ˵�������
     * @param plus ��ǰģ��
     *
     * @return �˵�������
     */
    ShortCut shortcut(ToolBarMenuDockPlus plus);

    /**
     * �����˵�����ȵ����
     * @param obj �Ƚ϶���
     * @return ����򷵻�true�����򷵻�false
     */
    boolean equals(Object obj);
}
