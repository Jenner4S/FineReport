package com.fr.design.menu;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.imenu.UIMenu;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.gui.imenu.UIScrollMenu;
import com.fr.design.gui.iscrollbar.UIScrollBar;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Define Menu.
 */
public class MenuDef extends ShortCut {

	protected String name;
	protected char mnemonic;
	protected String iconPath;
	protected String tooltip;
	//item List.
	private List<ShortCut> shortcutList = new ArrayList<ShortCut>();
	// peter:������JMenu, UIButton�Լ�enabled��������Ϊ��MenuDef�����Ŀؼ����õ�
	protected boolean enabled = true;
	protected UIMenu createdJMenu;
	protected UIButton createdButton;
	protected JPopupMenu popupMenu;
    private boolean hasScrollSubMenu;

	private String anchor;

	public MenuDef() {
	}

	public MenuDef(String name) {
		this.setName(name);
	}

	public MenuDef(String name, char mnemonic) {
		this.setName(name);
		this.setMnemonic(mnemonic);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public char getMnemonic() {
		return mnemonic;
	}

	public void setMnemonic(char mnemonic) {
		this.mnemonic = mnemonic;
	}

    public void setHasScrollSubMenu(boolean scrollSubMenu) {
        this.hasScrollSubMenu = scrollSubMenu;
    }

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public int getShortCutCount() {
		return this.shortcutList.size();
	}

	public ShortCut getShortCut(int index) {
		return this.shortcutList.get(index);
	}

	public String getAnchor() {
		return anchor == null ? StringUtils.EMPTY : anchor;
	}

	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}

	/**
	 * ����˵���
	 * @param index �����λ��
	 * @param shortCut �˵���Ϣ
	 */
	public void insertShortCut(int index, ShortCut shortCut) {
        int size = this.shortcutList.size();
        index = Math.min(index, size);
		this.shortcutList.add(index, shortCut);
	}

    /**
     * �ÿɱ�����������������
     * @param shortcut ����   �洢�˵�����Ϣ
     */
	public void addShortCut(ShortCut... shortcut) {
		for (ShortCut i : shortcut) {
			this.shortcutList.add(i);
		}
	}

    /**
     * ����
     */
    public void clearShortCuts() {
		this.shortcutList.clear();
	}

    /**
     * ����UIButton
     * @return  �˵���ť
     */
	public UIButton createUIButton() {
		if (createdButton == null) {
			if (iconPath != null) {
				createdButton = new UIButton(BaseUtils.readIcon(iconPath));
				createdButton.set4ToolbarButton();
			} else {
				createdButton = new UIButton(name);
			}
			// ������������Զ�������
			createdButton.setName(name);
			createdButton.setToolTipText(tooltip);
			createdButton.addMouseListener(mouseListener);
		}

		return createdButton;
	}

	public void setTooltip(String text) {
		this.tooltip = text;
	}

    /**
     * ����JMenu
     * @return     �˵�
     */
	public UIMenu createJMenu() {
		if (createdJMenu == null) {
            if (hasScrollSubMenu) {
                createdJMenu = new UIScrollMenu(this.getName());
            } else {
                createdJMenu = new UIMenu(this.getName());
            }
			createdJMenu.setMnemonic(this.getMnemonic());
			if (this.iconPath != null) {
				createdJMenu.setIcon(BaseUtils.readIcon(this.iconPath));
			}
			createdJMenu.addMenuListener(menuDefListener);
		}

		return createdJMenu;
	}

    /**
     * ���� JPopupMenu
     * @return       �����˵�
     */
	public JPopupMenu createJPopupMenu() {
		UIMenu menu = createJMenu();
		updateMenu();
		return menu.getPopupMenu();
	}

    /**
     * �����Ƿ����
     * @param b ������
     */
	@Override
	public void setEnabled(boolean b) {
		this.enabled = b;

		if (createdButton != null) {
			createdButton.setEnabled(enabled);
		}

		if (createdJMenu != null) {
			createdJMenu.setEnabled(enabled);
		}
	}

    /**
     * ��ť״̬
     * @return ״̬
     */
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * ���²˵�
	 */
	public void updateMenu() {
		//peter:���������������JMenu�ĺ��ӿؼ�,���ǲ�update,action.
		this.updatePopupMenu(this.createJMenu().getPopupMenu());

		//peter:��Ҫ����JMenu��enabled����.
		if (createdJMenu != null) {
			createdJMenu.setEnabled(createdJMenu.getPopupMenu().getComponentCount() > 0 && enabled);
			createdJMenu.repaint(10);
		}
	}

    /**
     * ���²˵�
     * @param popupMenu �˵�
     */
	protected void updatePopupMenu(JPopupMenu popupMenu) {
        removeComponent(popupMenu);
		this.popupMenu = popupMenu;
		// һ��ʼ�ǲ��ܲ���ָ�����
		boolean nec_seperator = false;
		boolean isFirstItem = true;
		int actionCount = this.getShortCutCount();
		for (int i = 0; i < actionCount; i++) {
			ShortCut shortcut = this.getShortCut(i);

			// ���shortcut��SeparatorDef,�Ȳ���,�ȱ��һ��nec_seperatorΪtrue,����һ��shortcut��Ҫ�ӵ�PopupMenuʱ�ټ�
			if (shortcut instanceof SeparatorDef) {
				nec_seperator = true;
				continue;
			}
			if (nec_seperator) {
				if (!isFirstItem) {
					SeparatorDef.DEFAULT.intoJPopupMenu(popupMenu);
				}
				nec_seperator = false;
			}

			shortcut.intoJPopupMenu(popupMenu);
			isFirstItem = false;
		}

		if (createdJMenu != null && createdJMenu.getPopupMenu() != null){
			setEnabled(createdJMenu.getPopupMenu().getComponentCount() > 0 && enabled);
        }
	}

    /**
     * ɾ��������� ���˹�����
     * @param popupMenu �˵�
     */
    public void removeComponent(JPopupMenu popupMenu){
        UIScrollBar uiScrollBar = new UIScrollBar();
        if(hasScrollSubMenu){
            for(Component comp : popupMenu.getComponents()){
                if(comp instanceof UIScrollBar){
                    uiScrollBar =(UIScrollBar) comp;
                }
            }
        }
        popupMenu.removeAll();
        if(hasScrollSubMenu){
            popupMenu.add(uiScrollBar);
        }
    }

    /**
     * ��Ӳ˵���
     * @param menu �˵�
     */
	@Override
	public void intoJPopupMenu(JPopupMenu menu) {
		updateMenu();

		menu.add(this.createJMenu());
	}

    /**
     * ���
     * @param toolBar �˵���
     */
	@Override
	public void intoJToolBar(JToolBar toolBar) {
		toolBar.add(this.createUIButton());
	}

	private MenuListener menuDefListener = new MenuListener() {

		@Override
		public void menuCanceled(MenuEvent evt) {
		}

		@Override
		public void menuDeselected(MenuEvent evt) {
		}

		@Override
		public void menuSelected(MenuEvent evt) {
			Object source = evt.getSource();
			if (!(source instanceof JMenu)) {
				return;
			}

			MenuDef.this.updateMenu();
		}
	};
	private MouseListener mouseListener = new MouseAdapter() {

		@Override
		public void mouseReleased(MouseEvent evt) {
			Object source = evt.getSource();
			UIButton button = (UIButton) source;
			if (!button.isEnabled()) {
				return;
			}

			UIPopupMenu popupMenu = new UIPopupMenu();
			popupMenu.setInvoker(button);
			MenuDef.this.updatePopupMenu(popupMenu);

			GUICoreUtils.showPopupMenu(popupMenu, button, 0, button.getSize().height);
		}
	};


	//ben: for ui test
	public JPopupMenu getPopupMenu() {
		return popupMenu;
	}

	public void setPopupMenu(JPopupMenu popupMenu) {
		this.popupMenu = popupMenu;
	}


}
