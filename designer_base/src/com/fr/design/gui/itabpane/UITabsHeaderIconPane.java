package com.fr.design.gui.itabpane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;

import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.BaseUtils;
import com.fr.design.gui.core.UITabComponent;
import com.fr.design.gui.ibutton.UITabButton;
import com.fr.stable.StringUtils;

/**
 * ������Ū���ӳټ��صģ������ڵ�Ԫ�����Ա��Ǳ�û�����壬������.�������Ǵ���ģ�û����ģ����κν�������(����˵populate() update())
 * 
 * @author zhou
 * @since 2012-5-11����3:30:18
 */
public class UITabsHeaderIconPane extends JPanel implements UITabComponent {
	private static final long serialVersionUID = 1L;

	private UILabel nameLabel;

	private UITabPaneCreator[] creators;
	private ArrayList<UITabButton> labels;

	private JPanel centerPane;

	private int selectedIndex = -1;

	public UITabsHeaderIconPane(final String name, UITabPaneCreator[] tabcreators) {
		this.creators = tabcreators;
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, UIConstants.LINE_COLOR));

		JPanel northTabsPane = new JPanel(new BorderLayout()) {
			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(super.getPreferredSize().width, 42);
			}
		};
		this.add(northTabsPane, BorderLayout.NORTH);

		northTabsPane.setBorder(null);
		nameLabel = new UILabel() {
			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(super.getPreferredSize().width, 18);
			}
		};
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		northTabsPane.add(nameLabel, BorderLayout.NORTH);
		nameLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.LINE_COLOR));
		JPanel tabsPane = new JPanel();
		tabsPane.setLayout(new GridLayout(1, creators.length));
		northTabsPane.add(tabsPane, BorderLayout.CENTER);
		labels = new ArrayList<UITabButton>(creators.length);
		for (int i = 0; i < creators.length; i++) {
			String iconpath = creators[i].getIconPath();// august:���ͼ��·��Ϊ�գ���˵��Ҫ���ı���
			final UITabButton label = StringUtils.isEmpty(iconpath) ? new UITabButton(creators[i].getTabName()) : new IconTabLabel(BaseUtils.readIcon(creators[i]
					.getIconPath()));
			tabsPane.add(label);
			labels.add(label);
			label.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					int newselectedIndex = labels.indexOf(label);
					if (selectedIndex != newselectedIndex) {
						setSelectedIndex(newselectedIndex);
					}
				}
			});
		}

		centerPane = new JPanel(new BorderLayout());
		centerPane.setBorder(null);
		this.add(centerPane, BorderLayout.CENTER);

		this.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				nameLabel.setText(name + '-' + creators[selectedIndex].getTabName());
				show(creators[selectedIndex].getPane());
			}
		});

		setSelectedIndex(0);

		initKeyListener();
	}

	private void initKeyListener() {
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		InputMap inputMapAncestor = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = this.getActionMap();
		inputMapAncestor.clear();
		actionMap.clear();

		inputMapAncestor.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.CTRL_MASK), "switch");
		actionMap.put("switch", new AbstractAction() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent evt) {
				setSelectedIndex(selectedIndex + 1 == creators.length ? 0 : selectedIndex + 1);
			}
		});
	}

	// august: ����Ч������
	private void show(final JPanel panel) {
		int count = centerPane.getComponentCount();// ��ȡcenterPanel�пؼ���
		List<Component> list = new ArrayList<Component>();//
		for (Component comp : centerPane.getComponents()) {
			list.add(comp);
		}
		if (count > 0) {// ���centerPanel�пؼ�������0��ִ��Ч��
			for (int i = 0; i < count; i++) {
				Component comp = centerPane.getComponent(i);// ��ø�λ�õĿؼ�

				if (comp instanceof JPanel) {
					final JPanel currentPanel = (JPanel)comp;// ��õ�ǰpanel

					// augsut:�����ö��̣߳���Ϊswing�ǵ��̵߳ģ����þ�ʵ�ֲ��˶�̬Ч��
					new Thread() {
						public void run() {
							int height = centerPane.getHeight();
							int width = centerPane.getWidth();
							int y = -height;
							for (int i = 0; i <= height; i += 30) {
								// �������λ��
								currentPanel.setBounds(0, i, width, height);
								panel.setBounds(0, y, width, height);
								y += 30;
								try {
									Thread.sleep(3);
								} catch (InterruptedException e) {
								}
							}
							if (currentPanel != panel) {
								centerPane.remove(currentPanel);// �Ƴ���ǰ���
							}
							panel.setBounds(0, 0, width, height);

						}
					}.start();
					break;
				}
			}
		} else {
			panel.setBounds(0, 0, centerPane.getWidth(), centerPane.getHeight());// ���û�����ʼλ��
		}

		if (!list.contains(panel)) {
			centerPane.add(panel);// ���Ҫ�л������
		}

		centerPane.validate();// �ع��������
		centerPane.repaint();// �ػ��������
	}

	/**
	 * Adds a <code>ChangeListener</code> to the listener list.
	 */
	public void addChangeListener(ChangeListener l) {
		this.listenerList.add(ChangeListener.class, l);
	}

	/**
	 * removes a <code>ChangeListener</code> from the listener list.
	 */
	public void removeChangeListener(ChangeListener l) {
		this.listenerList.remove(ChangeListener.class, l);
	}

	// august: Process the listeners last to first
	private void fireSelectedChanged() {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				((ChangeListener)listeners[i + 1]).stateChanged(new ChangeEvent(this));
			}
		}
	}

	@Override
	public int getSelectedIndex() {
		return selectedIndex;
	}

	@Override
	public synchronized void setSelectedIndex(int newselectedIndex) {
		selectedIndex = newselectedIndex;
		fireSelectedChanged();
		for (int i = 0; i < labels.size(); i++) {
			labels.get(i).setSelectedDoNotFireListener(i == selectedIndex);
		}
	}

	private class IconTabLabel extends UITabButton {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public IconTabLabel(Icon readIcon) {
			super(readIcon);
			this.setLayout(new BorderLayout());
			this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, UIConstants.LINE_COLOR));
		}

		@Override
		protected void paintRolloverAndSelected(Graphics2D g2d, int w, int h) {
			if (!isSelected()) {
				GradientPaint gp = new GradientPaint(1, 1, TOP, 1, h - 1, DOWN);
				g2d.setPaint(gp);
				g2d.fillRect(0, h / 2, w, h / 2);
			} else {
				Color blue = UIConstants.LIGHT_BLUE;
				g2d.setColor(blue);
				g2d.fillRect(0, 0, w, h / 2);
				GradientPaint gp = new GradientPaint(1, 1, blue, 1, h - 1, blue);
				g2d.setPaint(gp);
				g2d.fillRect(0, h / 2, w, h / 2);
			}
		}

	}

}