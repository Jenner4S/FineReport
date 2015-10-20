package com.fr.design.gui.imenu;

/**
 * Created with IntelliJ IDEA.
 * User: richie
 * Date: 13-12-5
 * Time: ����11:11
 */

import javax.swing.*;
import java.awt.*;


public class UIScrollMenu extends UIMenu {
    // Covers the one in the JMenu because the method that creates it in JMenu is private
    /**
     * The popup menu portion of the menu.
     */
    private JPopupMenu popupMenu;


    /**
     * Constructs a new <code>JMenu</code> with no text.
     */
    public UIScrollMenu() {
        this("");
    }

    /**
     * Constructs a new <code>JMenu</code> with the supplied string as its text.
     *
     * @param s the text for the menu label
     */
    public UIScrollMenu(String s) {
        super(s);
    }

    /**
     * Constructs a menu whose properties are taken from the <code>Action</code> supplied.
     *
     * @param a an <code>Action</code>
     */
    public UIScrollMenu(Action a) {
        this();
        setAction(a);
    }


    /**
     * Lazily creates the popup menu. This method will create the popup using the <code>JScrollPopupMenu</code> class.
     */
    protected void ensurePopupMenuCreated() {
        if (popupMenu == null) {
            this.popupMenu = new UIScrollPopUpMenu();
            popupMenu.setInvoker(this);
            popupListener = createWinListener(popupMenu);
        }
    }

    //////////////////////////////
//// All of these methods are necessary because ensurePopupMenuCreated() is private in JMenu
//////////////////////////////

    /**
     *������
     */
    public void updateUI() {
      setUI(new UIMenuUI());
    }


    /**
     * �ж�popupmeu�Ƿ�����
     * @return  ������� ����true
     */
    public boolean isPopupMenuVisible() {
        ensurePopupMenuCreated();
        return popupMenu.isVisible();
    }


    /**
     * ����popupmenuλ��
     * @param x
     * @param y
     */
    public void setMenuLocation(int x, int y) {
        super.setMenuLocation(x, y);
        if (popupMenu != null) {
            popupMenu.setLocation(x, y);
        }
    }

    /**
     * ��popupmenu��� JMenuItem
     * @param menuItem �˵���
     * @return    �˵���
     */
    public JMenuItem add(JMenuItem menuItem) {
        ensurePopupMenuCreated();
        return popupMenu.add(menuItem);
    }

    /**
     * ������
     * @param c   ���
     * @return    ���
     */
    public Component add(Component c) {
        ensurePopupMenuCreated();
        popupMenu.add(c);
        return c;
    }

    /**
     * ��ָ��λ��������
     * @param c       ���
     * @param index     λ��
     * @return   ���
     */
    public Component add(Component c, int index) {
        ensurePopupMenuCreated();
        popupMenu.add(c, index);
        return c;
    }


    /**
     * ��ӷָ���
     */
    public void addSeparator() {
        ensurePopupMenuCreated();
        popupMenu.addSeparator();
    }

    /**
     * ���menuitem��ָ��λ��
     * @param s      �ַ�
     * @param pos     λ��
     */
    public void insert(String s, int pos) {
        if (pos < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }

        ensurePopupMenuCreated();
        popupMenu.insert(new JMenuItem(s), pos);
    }

    /**
     * ���ô����ô��ָ��λ��
     * @param mi     �˵���
     * @param pos   λ��
     * @return       �˵���
     */
    public JMenuItem insert(JMenuItem mi, int pos) {
        if (pos < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }
        ensurePopupMenuCreated();
        popupMenu.insert(mi, pos);
        return mi;
    }

    /**
     * ��ӵ�ָ��λ��
     * @param a      �¼�
     * @param pos   λ��
     * @return       �˵���
     */
    public JMenuItem insert(Action a, int pos) {
        if (pos < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }

        ensurePopupMenuCreated();
        JMenuItem mi = new JMenuItem(a);
        mi.setHorizontalTextPosition(JButton.TRAILING);
        mi.setVerticalTextPosition(JButton.CENTER);
        popupMenu.insert(mi, pos);
        return mi;
    }

    /**
     *  ��ӷָ�����ָ��λ��
     * @param index  ָ��λ��
     */
    public void insertSeparator(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }

        ensurePopupMenuCreated();
        popupMenu.insert(new JPopupMenu.Separator(), index);
    }


    /**
     * �Ƴ�
     * @param item   �˵���
     */
    public void remove(JMenuItem item) {
        if (popupMenu != null) {
            popupMenu.remove(item);
        }
    }

    /**
     * �Ƴ�ָ��λ�ò˵���
     * @param pos  ָ��λ��
     */
    public void remove(int pos) {
        if (pos < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }
        if (pos > getItemCount()) {
            throw new IllegalArgumentException("index greater than the number of items.");
        }
        if (popupMenu != null) {
            popupMenu.remove(pos);
        }
    }

    /**
     * �Ƴ����
     * @param c  ���
     */
    public void remove(Component c) {
        if (popupMenu != null) {
            popupMenu.remove(c);
        }
    }

    /**
     * �Ƴ�����
     */
    public void removeAll() {
        if (popupMenu != null) {
            popupMenu.removeAll();
        }
    }

    /**
     *  �������
     * @return   �������
     */
    public int getMenuComponentCount() {
        return (popupMenu == null) ? 0 : popupMenu.getComponentCount();
    }

    /**
     * ָ��λ�����
     * @param n   ָ��λ��
     * @return    ���
     */
    public Component getMenuComponent(int n) {
        return (popupMenu == null) ? null : popupMenu.getComponent(n);
    }

    /**
     * �������
     * @return   �������
     */
    public Component[] getMenuComponents() {
        return (popupMenu == null) ? new Component[0] : popupMenu.getComponents();
    }

    /**
     * ȡ�õ����˵�
     * @return  �˵�
     */
    public JPopupMenu getPopupMenu() {
        ensurePopupMenuCreated();
        return popupMenu;
    }

    /**
     * �õ���Ԫ��
     * @return  ��Ԫ��
     */
    public MenuElement[] getSubElements() {
        return popupMenu == null ? new MenuElement[0] : new MenuElement[]{popupMenu};
    }


    /**
     *   �������������λ
     * @param o ��λ
     */
    public void applyComponentOrientation(ComponentOrientation o) {
        super.applyComponentOrientation(o);

        if (popupMenu != null) {
            int ncomponents = getMenuComponentCount();
            for (int i = 0; i < ncomponents; ++i) {
                getMenuComponent(i).applyComponentOrientation(o);
            }
            popupMenu.setComponentOrientation(o);
        }
    }

    /**
     * �������������λ
     * @param o ��λ
     */
    public void setComponentOrientation(ComponentOrientation o) {
        super.setComponentOrientation(o);
        if (popupMenu != null) {
            popupMenu.setComponentOrientation(o);
        }
    }
}
