/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions;

import com.fr.base.NameStyle;
import com.fr.base.ScreenResolution;
import com.fr.base.Style;
import com.fr.design.constants.UIConstants;
import com.fr.design.actions.core.ActionUtils;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.imenu.UICheckBoxMenuItem;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.menu.ShortCut;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * An UpdateAction functions allows actions to have an efficient transient state,
 * it's update method is called every time the action is about to be displayed
 * in UI - allowing the action to poll whatever information it wishes to update its state
 * before being displayed
 * As a regular Swing Action object - it stores the name, icon, and enabled state for a single action.
 * <p/>
 * For example, a 'cut' action my be enabled or disabled based on the state of the selection
 * in a text component. Just before the edit menu is displayed, the cut action is given the
 * opportunity to update itself, and will appear in the menu with the correct state.
 * august:不需要考虑UpdateAction的持久化，难道需要持久化吗？ 又不像以前那样保存docking的大小布局
 * 如果是ToggleButton，就额外继承ToggleButtonUpdateAction接口
 */
public abstract class UpdateAction extends ShortCut implements Action {

	/**
	 * Specifies whether action is enabled; the default is true.
	 */
	private boolean enabled = true;

	/**
	 * Contains the array of key bindings.
	 * august:关键词key，是Action里面的final常量，如：Action.NAME、Action.SMALL_ICON等等
	 */
	private Map<String, Object> componentMap;

	/**
	 * Constructor
	 */
	public UpdateAction() {
		//设置默认的small icon,必须有一个默认的图片,这样菜单整体美观.
		this.putValue(Action.SMALL_ICON, UIConstants.BLACK_ICON);
	}

	/**
	 * Returns true if the action is enabled.
	 *
	 * @return true if the action is enabled, false otherwise
	 * @see Action#isEnabled
	 */
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Enables or disables the action.
	 *
	 * @param newValue true to enable the action, false to
	 *                 disable it
	 * @see Action#setEnabled
	 */
	@Override
	public void setEnabled(boolean newValue) {
		boolean oldValue = this.enabled;

		if (oldValue != newValue) {
			this.enabled = newValue;

			//循环遍历所有的Enable属性.
			Iterator<Object> valueIt = this.componentMap.values().iterator();
			while (valueIt.hasNext()) {
				Object valueObject = valueIt.next();
				if (valueObject instanceof JComponent) {
					((JComponent) valueObject).setEnabled(this.enabled);
				}
			}
		}
	}


	/**
	 * Returns the name for the action, used for a menu or button.
	 *
	 * @return the name for the action.
	 */
	public String getName() {
		return (String) this.getValue(Action.NAME);
	}

	/**
	 * Sets the name for the action, used for a menu or button.
	 *
	 * @param name the name for the action.
	 */
	public void setName(String name) {
		this.putValue(Action.NAME, name);
	}

	/**
	 * Returns the smallIcon property setting.
	 *
	 * @return The small icon for the action.
	 */
	public Icon getSmallIcon() {
		return (Icon) this.getValue(Action.SMALL_ICON);
	}

	/**
	 * Sets the smallIcon property. The smallIcon may be displayed as a menu item's icon or
	 * possibly as the icon for a button. This icon should be 16x16 pixels in size.
	 *
	 * @param smallIcon The small icon for the action.
	 */
	public void setSmallIcon(Icon smallIcon) {
		this.putValue(Action.SMALL_ICON, smallIcon);
	}

	/**
	 * Returns the mnemonic property setting.
	 *
	 * @return The mnemonic character for the action.
	 */
	public char getMnemonic() {
		Integer n = (Integer) this.getValue(Action.MNEMONIC_KEY);
		return n == null ? '\0' : (char) n.intValue();
	}

	/**
	 * Sets the mnemonic property. The mnemonic character is the 'hotkey' for the menu item
	 * or button displaying the action. This *must* be a character in the 'name' property,
	 * or it will have no effect.
	 *
	 * @param mnemonic The mnemonic character for the action
	 */
	public void setMnemonic(char mnemonic) {
		this.putValue(Action.MNEMONIC_KEY, new Integer(mnemonic));
	}

	/**
	 * Returns the key used for storing a <code>KeyStroke</code> to be used as the
	 * accelerator for the action.
	 *
	 * @return the key as the accelerator for the action.
	 */
	public KeyStroke getAccelerator() {
		return (KeyStroke) this.getValue(Action.ACCELERATOR_KEY);
	}

	/**
	 * Sets the key used for storing a <code>KeyStroke</code> to be used as the
	 * accelerator for the action.
	 *
	 * @param accelerator the key as the accelerator for the action.
	 */
	public void setAccelerator(KeyStroke accelerator) {
		this.putValue(Action.ACCELERATOR_KEY, accelerator);
	}

	/**
	 * update enable
	 */
	public void update() {
	}

	/**
	 * Gets the <code>Object</code> associated with the specified key.
	 *
	 * @param key a string containing the specified <code>key</code>
	 * @return the binding <code>Object</code> stored with this key; if there
	 *         are no keys, it will return <code>null</code>
	 * @see Action#getValue
	 */
	@Override
	public Object getValue(String key) {
		if (componentMap == null) {
			return null;
		}
		return componentMap.get(key);
	}

	/**
	 * Sets the <code>Value</code> associated with the specified key.
	 *
	 * @param key      the <code>String</code> that identifies the stored object
	 * @param newValue the <code>Object</code> to store using this key
	 * @see Action#putValue
	 */
	@Override
	public void putValue(String key, Object newValue) {
		if (componentMap == null) {
			componentMap = new HashMap<String, Object>();
		}


		if (newValue == null) {
			componentMap.remove(key);
		} else {
			componentMap.put(key, newValue);
		}

	}

	@Override
	public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {

	}

	@Override
	public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
	}

	/**
	 * Gets menu item.
	 *
	 * @return the created menu item.
	 */
	public UIMenuItem createMenuItem() {
		Object object = this.getValue(UIMenuItem.class.getName());
		if (object == null && !(object instanceof UIMenuItem)) {
			UIMenuItem menuItem = new UIMenuItem(this);
			// 设置名字用作单元测
			menuItem.setName(getName());
			object = menuItem;

			this.putValue(UIMenuItem.class.getName(), object);
		}

		return (UIMenuItem) object;
	}

	/**
	 * Gets component on toolbar.
	 *
	 * @return the created components on toolbar.
	 */
	public JComponent createToolBarComponent() {
		Object object = this.getValue(UIButton.class.getName());
		if (!(object instanceof AbstractButton)) {
			UIButton button = null;
			button = new UIButton();
			// 添加一个名字作为自动化测试用
			button.setName(getName());
			button.set4ToolbarButton();

			//设置属性.
			Integer mnemonicInteger = (Integer) this.getValue(Action.MNEMONIC_KEY);
			if (mnemonicInteger != null) {
				button.setMnemonic((char) mnemonicInteger.intValue());
			}

			button.setIcon((Icon) this.getValue(Action.SMALL_ICON));
			button.addActionListener(this);

			button.registerKeyboardAction(this, this.getAccelerator(), JComponent.WHEN_IN_FOCUSED_WINDOW);

			this.putValue(UIButton.class.getName(), button);
			button.setText(StringUtils.EMPTY);
			button.setEnabled(this.isEnabled());

			//peter:产生tooltip
			button.setToolTipText(ActionUtils.createButtonToolTipText(this));
			object = button;
		}

		return (JComponent) object;
	}

	/**
	 * Equals
	 */
	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof UpdateAction)) {
			return false;
		}

		return ((UpdateAction) object).getName().equals(this.getName());
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 59 * hash + (this.enabled ? 1 : 0);
		return hash;
	}

	/*
	 * Add this ShortCut into JPopupMenu
	 */
	@Override
	public void intoJPopupMenu(JPopupMenu menu) {
		update();

		menu.add(this.createMenuItem());
	}

	/*
	 * Add this ShortCut into JToolBar
	 */
	@Override
	public void intoJToolBar(JToolBar toolBar) {
		update();

		toolBar.add(this.createToolBarComponent());
	}

	/**
	 * 全局style的菜单
	 */
	public static class UseMenuItem extends UIMenuItem {

		private NameStyle nameStyle;

		public UseMenuItem(Action action) {
			super(action);
			setPreferredSize(new Dimension(80, 20));
		}

		public UseMenuItem(String text, Icon icon) {
			super(text, icon);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			if (nameStyle == null) {
				return;
			}

			Style.paintBackground((Graphics2D) g, nameStyle, getWidth() - 1, getHeight() - 1);

			Style.paintContent((Graphics2D) g, nameStyle.getName(), nameStyle, getWidth() - 1, getHeight() - 1, ScreenResolution.getScreenResolution());

			Style.paintBorder((Graphics2D) g, nameStyle, getWidth() - 1, getHeight() - 1);
		}

		public NameStyle getNameStyle() {
			return this.nameStyle;
		}

		public void setNameStyle(NameStyle nameStyle) {
			this.nameStyle = nameStyle;
		}

		@Override
		public Dimension getMinimumSize() {
			return getPreferredSize();
		}
	}

	/**
	 * Gets menu item.
	 *
	 * @return the created menu item.
	 */
	public UseMenuItem createUseMenuItem() {
		Object object = this.getValue(UseMenuItem.class.getName());
		if (object == null && !(object instanceof UseMenuItem)) {
			object = new UseMenuItem(this);
			this.putValue(UseMenuItem.class.getName(), object);
		}
		return (UseMenuItem) object;
	}

	public static UICheckBoxMenuItem createCheckBoxMenuItem(UpdateAction action) {
		UICheckBoxMenuItem menuItem = new UICheckBoxMenuItem(action.getName());

		// 设置属性.
		Integer mnemonicInteger = (Integer) action
				.getValue(Action.MNEMONIC_KEY);
		if (mnemonicInteger != null) {
			menuItem.setMnemonic((char) mnemonicInteger.intValue());
		}
		menuItem.setIcon((Icon) action.getValue(Action.SMALL_ICON));
//        if(menuItem.isSelected()){
//            menuItem.setIcon(FRSwingConstants.YES_ICON);
//        }
		menuItem.addActionListener(action);
		menuItem.setToolTipText((String) action.getValue(Action.LONG_DESCRIPTION));
		menuItem.setAccelerator((KeyStroke) action.getValue(Action.ACCELERATOR_KEY));

		return menuItem;
	}
}