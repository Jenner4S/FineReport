package com.fr.design.gui.ibutton;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import com.fr.base.Utils;
import com.fr.design.constants.UIConstants;
import com.fr.design.event.GlobalNameListener;
import com.fr.design.event.GlobalNameObserver;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ipoppane.PopupHider;
import com.fr.general.ComparatorUtils;
import com.fr.design.style.color.ColorControlWindow;
import com.fr.design.utils.gui.GUICoreUtils;

public class UIColorButton extends UIButton implements PopupHider, UIObserver, GlobalNameObserver {
	private static final int SIZE = 16;
	private static final int SIZE_2 = 2;
	private static final int SIZE_4 = 4;
	private static final int SIZE_6 = 6;
	private Color color = Color.BLACK;
	private ColorControlWindow popupWin;
	private EventListenerList colorChangeListenerList = new EventListenerList();
	private boolean isEventBanned = false;
	private String colorButtonName = "";
	private UIObserverListener uiObserverListener;
	private GlobalNameListener globalNameListener = null;

	public UIColorButton() {
		this(UIConstants.FONT_ICON);
	}

	public UIColorButton(Icon icon) {
		super(icon);
		setUI(getButtonUI());
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				showPopupMenu();
			}
		});
		iniListener();
	}

	private void iniListener() {
		if (shouldResponseChangeListener()) {
			this.addColorChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					if (uiObserverListener == null) {
						return;
					}
					if (globalNameListener != null && shouldResponseNameListener()) {
						globalNameListener.setGlobalName(colorButtonName);
					}
					uiObserverListener.doChange();
				}
			});
		}
	}

	private UIButtonUI getButtonUI() {
		return new UIButtonUI() {
			@Override
			protected void paintIcon(Graphics g, JComponent c) {
				super.paintIcon(g, c);
				AbstractButton b = (AbstractButton) c;
				ButtonModel model = b.getModel();
				if (model.isEnabled()) {
					g.setColor(UIColorButton.this.getColor());
				} else {
					g.setColor(new Color(Utils.filterRGB(UIColorButton.this.getColor().getRGB(), 50)));
				}
				g.fillRect((b.getWidth() - SIZE) / SIZE_2, b.getHeight() - SIZE_6, SIZE, SIZE_4);
			}
		};
	}

	public void setEventBanned(boolean isEventBanned) {
		this.isEventBanned = isEventBanned;
	}

	public void setGlobalName(String name) {
		colorButtonName = name;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		if (ComparatorUtils.equals(this.color, color)) {
			return;
		}

		this.color = color;
		hidePopupMenu();
		fireColorStateChanged();
	}

	private void showPopupMenu() {
		if (isEventBanned) {
			return;
		}

		if (popupWin != null && popupWin.isVisible()) {
			hidePopupMenu();
			return;
		}

		if (!this.isEnabled()) {
			return;
		}

		popupWin = this.getColorControlWindow();

		GUICoreUtils.showPopupMenu(popupWin, this, 0, this.getSize().height);
	}

	/**
	 * ����popupmenu
	 */
	public void hidePopupMenu() {
		if (popupWin != null) {
			popupWin.setVisible(false);
			repaint();
		}

		popupWin = null;
	}

	private ColorControlWindow getColorControlWindow() {
		//find parant.
		if (this.popupWin == null) {
			this.popupWin = new ColorControlWindow(UIColorButton.this) {
				@Override
				protected void colorChanged() {
					UIColorButton.this.setColor(this.getColor());
				}

			};
		}

		return popupWin;
	}

	/**
	 * ��Ӽ���
	 * 
	 * @param changeListener �����б�
	 */
	public void addColorChangeListener(ChangeListener changeListener) {
		colorChangeListenerList.add(ChangeListener.class, changeListener);
	}

	/**
	 * �Ƴ�����
	 *  Removes an old ColorChangeListener.
	 * @param changeListener �����б�
	 */
	public void removeColorChangeListener(ChangeListener changeListener) {
		colorChangeListenerList.remove(ChangeListener.class, changeListener);
	}

	/**
	 * ��ɫ״̬�ı�
	 */
	public void fireColorStateChanged() {
		Object[] listeners = colorChangeListenerList.getListenerList();
		ChangeEvent e = null;

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				if (e == null) {
					e = new ChangeEvent(this);
				}
				((ChangeListener) listeners[i + 1]).stateChanged(e);
			}
		}
	}


	/**
	 * ע��״̬�ı����
	 *
	 * @param listener �۲��߼����¼�
	 */
	public void registerChangeListener(UIObserverListener listener) {
		uiObserverListener = listener;
	}

	/**
	 * �Ƿ���Ҫ��Ӧ����
	 *
	 * @return �Ƿ���Ӧ
	 */
	public boolean shouldResponseChangeListener() {
		return true;
	}

	/**
	 * ע�����
	 *
	 * @param listener �۲��߼����¼�
	 */
	public void registerNameListener(GlobalNameListener listener) {
		globalNameListener = listener;
	}

	/**
	 * �Ƿ���Ҫ��Ӧ
	 *
	 * @return �Ƿ���Ӧ
	 */
	public boolean shouldResponseNameListener() {
		return true;
	}

	/**
	 * ������
	 * 
	 * @param args ����
	 */
	public static void main(String... args) {
		LayoutManager layoutManager = null;
		JFrame jf = new JFrame("test");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel content = (JPanel) jf.getContentPane();
		content.setLayout(layoutManager);

		UIColorButton bb = new UIColorButton(UIConstants.FONT_ICON);
		bb.setBounds(20, 20, bb.getPreferredSize().width, bb.getPreferredSize().height);
		content.add(bb);
		GUICoreUtils.centerWindow(jf);
		jf.setSize(400, 400);
		jf.setVisible(true);
	}
}
