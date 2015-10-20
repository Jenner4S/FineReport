package com.fr.design.gui.ibutton;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.BaseUtils;
import com.fr.design.constants.UIConstants;
import com.fr.design.event.GlobalNameListener;
import com.fr.design.event.GlobalNameObserver;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Constants;
import com.fr.design.utils.gui.GUICoreUtils;

public class UIButtonGroup<T> extends JPanel implements GlobalNameObserver {
	private boolean isTwoLine = false;
	private static final long serialVersionUID = 1L;
	protected List<UIToggleButton> labelButtonList;
	protected int selectedIndex = -1;
	private List<T> objectList;// ��һ��render������
	private GlobalNameListener globalNameListener = null;
	private String buttonGroupName = "";
	private boolean isToolBarComponent = false;
    private boolean isClick;

	public UIButtonGroup(String[] textArray) {
		this(textArray, null);
	}

	public UIButtonGroup(Icon[] iconArray) {
		this(iconArray, null);
	}

	public UIButtonGroup(Icon[] iconArray, T[] objects) {
		if (!ArrayUtils.isEmpty(objects) && iconArray.length == objects.length) {
			this.objectList = Arrays.asList(objects);
		}
		labelButtonList = new ArrayList<UIToggleButton>(iconArray.length);
		this.setLayout(getGridLayout(iconArray.length));
		this.setBorder(getGroupBorder());
		for (int i = 0; i < iconArray.length; i++) {
			final int index = i;
			Icon icon = iconArray[i];
			final UIToggleButton labelButton = new UIToggleButton(icon) {
				@Override
				protected MouseListener getMouseListener() {
					return new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent e) {
                            isClick = true;
							if (!isEnabled()) {
								return;
							}
							if (globalNameListener != null) {
								globalNameListener.setGlobalName(buttonGroupName);
							}
							setSelectedWithFireChanged(index);
						}
					};
				}

				public boolean shouldResponseNameListener() {
					return false;
				}
			};
			initButton(labelButton);
		}
	}

     public boolean hasClick(){
         return isClick;
     }

      public void setClickState(boolean changeFlag){
          isClick = changeFlag;
    }

	public void setForToolBarButtonGroup(boolean isToolBarComponent) {
		this.isToolBarComponent = isToolBarComponent;
		if (isToolBarComponent) {
			for (int i = 0; i < labelButtonList.size(); i++) {
				labelButtonList.get(i).set4ToolbarButton();
			}
		}
		repaint();

	}


	/**
	 * setEnabled
	 *
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for (int i = 0; i < labelButtonList.size(); i++) {
			labelButtonList.get(i).setEnabled(enabled);
		}
	}

	public UIButtonGroup(String[] textArray, T[] objects) {
		if (!ArrayUtils.isEmpty(objects) && textArray.length == objects.length) {
			this.objectList = Arrays.asList(objects);
		}
		labelButtonList = new ArrayList<UIToggleButton>(textArray.length);
		this.setLayout(getGridLayout(textArray.length));
		this.setBorder(getGroupBorder());
		for (int i = 0; i < textArray.length; i++) {
			final int index = i;
			final UIToggleButton labelButton = new UIToggleButton(textArray[i]) {
				@Override
				protected MouseListener getMouseListener() {
					return new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent e) {
							if (!isEnabled()) {
								return;
							}
							if(globalNameListener != null) {
								globalNameListener.setGlobalName(buttonGroupName);
							}
							setSelectedWithFireChanged(index);
						}
					};
				}

				@Override
				public Insets getInsets() {
					return new Insets(0, 2, 0, 2);
				}

				public boolean shouldResponseNameListener() {
					return false;
				}

			};
			initButton(labelButton);
		}
	}

	public void setGlobalName(String name) {
		buttonGroupName = name;
	}

	protected void initButton(UIToggleButton labelButton) {
		labelButton.setRoundBorder(false);
		labelButton.setBorderPainted(false);
		labelButtonList.add(labelButton);
		this.add(labelButton);
	}

	protected Border getGroupBorder() {
		return BorderFactory.createEmptyBorder(1, 1, 1, 1);
	}

	protected GridLayout getGridLayout(int number) {
		return new GridLayout(0, number, 1, 0);
	}

	public void setTwoLine() {
		this.isTwoLine = true;
	}

	/**
	 * paintComponent
	 *
	 * @param g
	 */
	public void paintComponents(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Shape oldClip = g2d.getClip();
		g2d.clip(new RoundRectangle2D.Double(1, 1, getWidth(), getHeight(), UIConstants.ARC, UIConstants.ARC));
		super.paintComponents(g);
		g2d.setClip(oldClip);
	}

	/**
	 * ����Border����
	 *
	 * @param g
	 */
	@Override
	protected void paintBorder(Graphics g) {
		if(isToolBarComponent){
			return;
		}
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(UIConstants.LINE_COLOR);
		if (!isTwoLine) {
			int width = 0;
			for (int i = 0; i < labelButtonList.size() - 1; i++) {
				width += labelButtonList.get(i).getWidth() + 1;
				int height = labelButtonList.get(i).getHeight();
				g.drawLine(width, 0, width, height);
			}

			width += labelButtonList.get(labelButtonList.size() - 1).getWidth() + 1;

			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.drawRoundRect(0, 0, width, getHeight() - 1, UIConstants.ARC, UIConstants.ARC);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		} else {
			int width = 0;
			int upCount = (labelButtonList.size() - 1) / 2 + 1;
			for (int i = 0; i < upCount - 1; i++) {
				width += labelButtonList.get(i).getWidth() + 1;
				int height = labelButtonList.get(i).getHeight() * 2 + 1;
				g.drawLine(width, 0, width, height);
			}

			width += labelButtonList.get(upCount).getWidth() + 1;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.drawRoundRect(0, 0, width, getHeight() - 1, UIConstants.ARC, UIConstants.ARC);
			g2d.drawLine(0, getHeight() / 2, width, getHeight() / 2);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		}

	}

	/**
	 * setSelectedItem
	 *
	 * @param ob
	 */
	public void setSelectedItem(T ob) {
		if (objectList == null) {
			setSelectedIndex(-1);
			return;
		}
		setSelectedIndex(objectList.indexOf(ob));
	}

	/**
	 * getSelectedItem
	 *
	 * @return
	 */
	public T getSelectedItem() {
		if (selectedIndex == -1) {
			return null;
		}
		return objectList.get(selectedIndex);
	}

	/**
	 * getSelectedIndex
	 *
	 * @return
	 */
	public int getSelectedIndex() {
		return selectedIndex;
	}

	protected void setSelectedWithFireChanged(int newSelectedIndex) {
		selectedIndex = newSelectedIndex;
		for (int i = 0; i < labelButtonList.size(); i++) {
			if (i == selectedIndex) {
				labelButtonList.get(i).setSelectedWithFireListener(true);
			} else {
				labelButtonList.get(i).setSelected(false);
			}
		}
	}

	/**
	 * setSelectedIndex
	 *
	 * @param newSelectedIndex
	 */
	public void setSelectedIndex(int newSelectedIndex) {
		selectedIndex = newSelectedIndex;
		for (int i = 0; i < labelButtonList.size(); i++) {
			labelButtonList.get(i).setSelected(i == selectedIndex);
		}
	}

	private void fireStateChanged() {
		Object[] listeners = listenerList.getListenerList();
		ChangeEvent e = null;

		for (int i = 0; i < listeners.length; i++) {
			if (listeners[i] == ChangeListener.class) {
				if (e == null) {
					e = new ChangeEvent(this);
				}
				((ChangeListener) listeners[i + 1]).stateChanged(e);
			}
		}
	}

	/**
	 * getButton
	 *
	 * @param index
	 * @return
	 */
	public UIToggleButton getButton(int index) {
		return labelButtonList.get(index);
	}

	/**
	 * �����е�Button���Tips
	 *
	 * @param tips
	 */
	public void setAllToolTips(String[] tips) {
		for (int i = 0; i < labelButtonList.size(); i++) {
			labelButtonList.get(i).setToolTipText(tips[i]);
		}
	}


	/**
	 * ע��ȫ���������ּ�����
	 *
	 * @param listener �۲��߼����¼�
	 */
	public void registerNameListener(GlobalNameListener listener) {
		globalNameListener = listener;
	}

	/**
	 * �Ƿ���Ӧ���ּ����¼�
	 *
	 * @return
	 */
	public boolean shouldResponseNameListener() {
		return true;
	}


	/**
	 * @param l
	 */
	public void addChangeListener(ChangeListener l) {
		for (int i = 0; i < labelButtonList.size(); i++) {
			labelButtonList.get(i).addChangeListener(l);
			listenerList.add(ChangeListener.class, l);
		}
	}

	/**
	 * @param l
	 */
	public void removeChangeListener(ChangeListener l) {
		this.listenerList.remove(ChangeListener.class, l);
	}


	/**
	 * @param l
	 */
	public void addActionListener(ActionListener l) {
		for (int i = 0; i < labelButtonList.size(); i++) {
			labelButtonList.get(i).addActionListener(l);
		}
	}


	/**
	 * @param l
	 */
	public void removeActionListener(ActionListener l) {
		for (int i = 0; i < labelButtonList.size(); i++) {
			labelButtonList.get(i).removeActionListener(l);
		}
	}

	/**
	 * populate
	 */
	public void populateBean() {
		fireStateChanged();
	}

	/**
	 * main
	 *
	 * @param args
	 */
	public static void main(String... args) {
		JFrame jf = new JFrame("test");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel content = (JPanel) jf.getContentPane();
		content.setLayout(new BorderLayout());
		Icon[] a1 = {BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_left_normal.png"), BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_center_normal.png"),
				BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_right_normal.png")};
		Integer[] a2 = new Integer[]{Constants.LEFT, Constants.CENTER, Constants.RIGHT};
		UIButtonGroup<Integer> bb = new UIButtonGroup<Integer>(a1, a2);
		bb.setBounds(20, 20, bb.getPreferredSize().width, bb.getPreferredSize().height);
		bb.setSelectedIndex(0);
		bb.setEnabled(false);
		content.add(bb);
		GUICoreUtils.centerWindow(jf);
		jf.setSize(400, 400);
		jf.setVisible(true);
	}


}
