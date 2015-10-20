/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.utils.gui;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.base.background.ColorBackground;
import com.fr.data.util.function.*;
import com.fr.design.actions.UpdateAction;
import com.fr.design.actions.core.ActionUtils;
import com.fr.design.border.UITitledBorder;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.EditTextField;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.style.color.ColorCell;
import com.fr.design.style.color.ColorFactory;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.design.style.color.ColorSelectable;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.stable.Constants;
import com.fr.stable.OperatingSystem;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class GUICoreUtils{

	private static final int WINDOW_GAP = 20;
	private static final int HEIGHT_GAP = 28;
	private static final int WIN_LOCATION_Y=23;
	private static final int CASE_FOUR = 4;
	
	

	private GUICoreUtils() {
	}

	/**
	 * August:һ���cursor�� pngͼƬ����32*32�ģ�����ķ�������������16*16��ͼƬ����Ӧ��cursorͼ��
	 *
	 * @param cursor ���
	 * @param hotSpot �ȵ�
	 * @param name ����
	 * @param ob �۲���
	 * @return ���
	 */
	public static Cursor createCustomCursor(Image cursor, Point hotSpot, String name, ImageObserver ob) {

		Dimension bestCursorSize = Toolkit.getDefaultToolkit().getBestCursorSize(cursor.getWidth(ob), cursor.getHeight(ob));

		BufferedImage bufferedImage = new BufferedImage(bestCursorSize.width, bestCursorSize.height, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < bestCursorSize.width; x++) {
			for (int y = 0; y < bestCursorSize.height; y++) {
				bufferedImage.setRGB(x, y, 0);
			}
		}
		bufferedImage.getGraphics().drawImage(cursor, 0, 0, ob);

		return Toolkit.getDefaultToolkit().createCustomCursor(bufferedImage, hotSpot, name);

	}

	/**
	 * ��ʼ���������
	 * @param centerPane �������
	 * @param colorSelectable ��ɫѡ��
	 */
	public static void initCenterPaneChildren(JPanel centerPane, ColorSelectable colorSelectable) {
		JPanel menuColorPane1 = new JPanel();
		centerPane.add(menuColorPane1);

		menuColorPane1.setLayout(new GridLayout(5, 8, 5, 5));
		for (int i = 0; i < ColorFactory.MenuColors.length; i++) {
			menuColorPane1.add(new ColorCell(ColorFactory.MenuColors[i], colorSelectable));
		}

		centerPane.add(Box.createVerticalStrut(5));
		centerPane.add(new JSeparator());
	}

	/**
	 * ������ʽ
	 * @param style ��ʽ
	 * @param textField �ı���
	 * @param resolution ������
	 * @param value ���뷽ʽ
	 */
	public static void adjustStyle(Style style, EditTextField textField, int resolution, Object value) {
		if (style == null) {
			// peter:��ȡĬ�ϵ�Style.
			style = Style.DEFAULT_STYLE;
		}

		// alignment.
		int horizontalAlignment = BaseUtils.getAlignment4Horizontal(style, value);
		if (horizontalAlignment == Constants.LEFT) {
			textField.setHorizontalAlignment(SwingConstants.LEFT);
		} else if (horizontalAlignment == Constants.CENTER) {
			textField.setHorizontalAlignment(SwingConstants.CENTER);
		} else if (horizontalAlignment == Constants.RIGHT) {
			textField.setHorizontalAlignment(SwingConstants.RIGHT);
		} else {
			textField.setHorizontalAlignment(SwingConstants.LEFT);
		}

		FRFont frFont = style.getFRFont();
		textField.setFont(new Font(frFont.getFontName(), frFont.getStyle(), frFont.getShowSize(resolution)));
		textField.setForeground(style.getFRFont().getForeground());

		if (style.getBackground() instanceof ColorBackground) {
			textField.setBackground(((ColorBackground) style.getBackground()).getColor());
		} else {
			textField.setBackground(Color.white);
		}
	}

	/**
	 * ����һ���߽粼��
	 *
	 * @param args �����ڲ���Ԫ�أ�λ�õ�
	 * @return ���б߽粼�ֵ����
	 */
	public static JPanel createBorderLayoutPane(Object... args) {
		if (args.length % 2 != 0) {
			throw new IllegalArgumentException("Illegal Arguments");
		}
		BorderLayout layout = new BorderLayout();
		Object maybeHgap = args[args.length - 2];
		Object maybeVgap = args[args.length - 1];
		boolean hasGap = false;
		if (maybeHgap instanceof Integer && maybeVgap instanceof Integer) {
			layout.setHgap((Integer) maybeHgap);
			layout.setVgap((Integer) maybeVgap);
			hasGap = true;
		}
		JPanel pane = new JPanel(layout);
		pane.setOpaque(false);
		for (int i = 0; i < (hasGap ? args.length - 2 : args.length) / 2; i++) {
			pane.add((Component) args[2 * i], args[2 * i + 1]);
		}
		return pane;
	}

	/**
	 * set color title border
	 * ���ô���ɫ�ı߿�
	 * @param s ����
	 * @param c ��ɫ
	 * @return ͬ��
	 */
	public static TitledBorder createTitledBorder(String s, Color c) {
		UITitledBorder tb = UITitledBorder.createBorderWithTitle(s);
		if (c == null) {
			c = new Color(102, 153, 255);
		}
		tb.setTitleColor(c);
		return tb;
	}

	/**
	 * ���ô���ɫ�ı߿�
	 * set color title border
	 * @param s ����
	 * @return ͬ��
	 */
	public static TitledBorder createTitledBorder(String s) {
		return createTitledBorder(s, new Color(102, 153, 255));
	}

	/**
	 * �������������
	 * @param updateAction ���¶���
	 * @return UIToggleButton ��ť
	 * 
	 */
	public static UIToggleButton createToolBarComponent(UpdateAction updateAction) {
		UIToggleButton button = new UIToggleButton();
		button.set4ToolbarButton();
		Object object = updateAction.getValue(UIToggleButton.class.getName());
		if (!(object instanceof AbstractButton)) {

			Integer mnemonicInteger = (Integer) updateAction.getValue(Action.MNEMONIC_KEY);
			if (mnemonicInteger != null) {
				button.setMnemonic((char) mnemonicInteger.intValue());
			}

			button.setIcon((Icon) updateAction.getValue(Action.SMALL_ICON));
			button.addActionListener(updateAction);

			button.registerKeyboardAction(updateAction, updateAction.getAccelerator(), JComponent.WHEN_IN_FOCUSED_WINDOW);

			updateAction.putValue(UIToggleButton.class.getName(), button);
			button.setText(StringUtils.EMPTY);
			button.setEnabled(updateAction.isEnabled());

			button.setToolTipText(ActionUtils.createButtonToolTipText(updateAction));
			object = button;
		}

		return (UIToggleButton) object;
	}

	/**
	 * ����һ������
	 *
	 * @param win the current window august:����Ҫ���������־ģ���Ӱ��
	 */
	public static void centerWindow(Window win) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		Dimension winSize = win.getSize();

		if (winSize.height > screenSize.height) {
			winSize.height = screenSize.height;
		}
		if (winSize.width > screenSize.width) {
			winSize.width = screenSize.width;
		}
		win.setLocation((screenSize.width - winSize.width) / 2, (screenSize.height - winSize.height) / 2 - WINDOW_GAP);
	}

	/**
	 * Gets window/frame to screen center.
	 * @param owerWin ������
	 * @param win ����
	 */
	public static void setWindowCenter(Window owerWin, Window win) {
		Point owerPoint = owerWin.getLocation();
		Dimension owerSize = owerWin.getSize();
		Dimension winSize = win.getSize();

		win.setLocation((owerSize.width - winSize.width) / 2 + owerPoint.x, (owerSize.height - winSize.height) / 2 + owerPoint.y);
	}

	/**
	 * Gets window/frame to screen center.
	 * @param ����
	 */
	public static void setWindowFullScreen(Window win) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		if (OperatingSystem.isWindows()) {
			win.setLocation(0, 0);
			win.setSize(screenSize.width, screenSize.height - HEIGHT_GAP);
		} else {
			win.setLocation(5, WIN_LOCATION_Y);
			win.setSize(screenSize.width, screenSize.height - HEIGHT_GAP *2);
		}
	}

	/**
	 * Shows down component.
	 * ��ʾ�����رղ˵�
	 * @param popup �����˵�
	 * @param �����
	 */
	public static void showPopupCloseMenu(JPopupMenu popup, Component parentComponent) {
		if (popup == null) {// check null.
			return;
		}

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Window frame = SwingUtilities.getWindowAncestor(parentComponent);

		int leftX = 0;

		int rightX = parentComponent.getLocation().x + frame.getLocation().x + popup.getPreferredSize().width;
		if (rightX > screenSize.width) {
			leftX = screenSize.width - rightX;
		}

		popup.show(parentComponent, leftX, parentComponent.getSize().height);
	}

	/**
	 * ��ʾ�����˵�
	 * @param popup �����˵�
	 * @param parentComponent �����
	 * @param x x����
	 * @param y y����
	 */
	public static void showPopMenuWithParentWidth(JPopupMenu popup, Component parentComponent, int x, int y) {
		if (popup == null) {// check null.
			return;
		}
		Dimension size = popup.getPreferredSize();
		size.width = Math.max(size.width, parentComponent.getWidth());
		popup.setPreferredSize(size);
		showPopupCloseMenu(popup, parentComponent);
	}

	/**
	 * ��ʾ�����˵�
	 * @param popup �����˵�
	 * @param parentComponent �����
	 * @param x x����
	 * @param y y����
	 */
	public static void showPopupMenu(JPopupMenu popup, Component parentComponent, int x, int y) {
		if (popup == null) {// check null.
			return;
		}

		Point point = new Point(x, y);
		SwingUtilities.convertPointToScreen(point, parentComponent);

		Dimension size = popup.getPreferredSize();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		screen.setSize(screen.getSize().width, screen.height - HEIGHT_GAP);

		// peter:����X�ĸ߶�.
		if (point.x + size.width > screen.width && size.width < screen.width) {
			x += (screen.width - point.x - size.width);
		}

		// peter:����y�߶�.
		if (point.y + size.height > screen.height && size.height < screen.height) {
			y -= size.height;
		}

		popup.show(parentComponent, x, y);
	}

	/**
	 * Set enabled.<br>
	 * With the enabled of all children component.
	 * @param parentComponent �����
	 * @param enabled �Ƿ����
	 */
	public static void setEnabled(JComponent parentComponent, boolean enabled) {
		// check the border of comp.
		Border border = parentComponent.getBorder();
		if (border != null && border instanceof TitledBorder) {
			TitledBorder titledBorder = (TitledBorder) border;

			if (enabled) {
				titledBorder.setTitleColor(UIManager.getColor("Label.foreground"));
			} else {
				titledBorder.setTitleColor(UIManager.getColor("Label.disabledForeground"));
			}
		}

		for (int i = 0; i < parentComponent.getComponentCount(); i++) {
			Component tmpComp = parentComponent.getComponent(i);

			if (tmpComp instanceof JComponent) {
				GUICoreUtils.setEnabled((JComponent) tmpComp, enabled);
			} else {
				tmpComp.setEnabled(enabled);
			}
		}

		parentComponent.setEnabled(enabled);
	}

	/**
	 * ���Ӽ���
	 * @param parentComponent �����
	 * @param changeListener ����
	 * @author kunsnat E-mail kunsnat@gmail.com
	 */
	public static void addChangeListener(JComponent parentComponent, ChangeListener changeListener) {
		for (int i = 0; i < parentComponent.getComponentCount(); i++) {
			Component tmpComp = parentComponent.getComponent(i);

			// addColorChangeListener ColorSelectBox
			if (tmpComp instanceof AbstractButton) {
				((AbstractButton) tmpComp).addChangeListener(changeListener);
			} else if (tmpComp instanceof ColorSelectBox) {
				((ColorSelectBox) tmpComp).addSelectChangeListener(changeListener);
			} else if (tmpComp instanceof JSlider) {
				((JSlider) tmpComp).addChangeListener(changeListener);
			} else if (tmpComp instanceof JComponent) {
				GUICoreUtils.addChangeListener((JComponent) tmpComp, changeListener);
			}
		}
	}

	/**
	 * ���Ӽ���
	 * @param parentComponent �����
	 * @param actionListener ����
	 */
	public static void addActionListener(JComponent parentComponent, ActionListener actionListener) {
		for (int i = 0; i < parentComponent.getComponentCount(); i++) {
			Component tmpComp = parentComponent.getComponent(i);

			if (tmpComp instanceof UIComboBox) {
				((UIComboBox) tmpComp).addActionListener(actionListener);
			} else if (tmpComp instanceof JComponent) {
				GUICoreUtils.addActionListener((JComponent) tmpComp, actionListener);
			}
		}
	}

	/**
	 * ����һ�����������,�ұ���comp��һ��JPanel
	 * @param comp ���
	 * @param name ����
	 * @return ���
	 */
	public static JPanel createNamedPane(JComponent comp, String name) {
		JPanel mainPane = new JPanel();
		mainPane.setLayout(FRGUIPaneFactory.createM_BorderLayout());

		mainPane.add(new UILabel(name), BorderLayout.WEST);
		mainPane.add(comp, BorderLayout.CENTER);

		return mainPane;
	}

	 /**
	  * ����һ���ϱ�������,�±���comp��һ��JPanel
	  * @param comp ���
	  * @param name ����
	  * @return ���
	  */
	public static JPanel createVerticalNamedPane(JComponent comp, String name) {
		JPanel mainPane = new JPanel();
		mainPane.setLayout(FRGUIPaneFactory.createM_BorderLayout());

		mainPane.add(new UILabel(name), BorderLayout.NORTH);
		mainPane.add(comp, BorderLayout.CENTER);

		return mainPane;
	}

	/**
	 * ����һ��Flow Pane, flowAligment��FlowLayout.LEFT, CENTER, RIGHT.
	 * @param comp ���
	 * @param flowAlignment ���뷽ʽ
	 * @return ���
	 */
	public static JPanel createFlowPane(Component comp, int flowAlignment) {// by
		return GUICoreUtils.createFlowPane(new Component[]{comp}, flowAlignment);
	}

	/**
	 * ����һ��Flow Pane, flowAligment��FlowLayout.LEFT, CENTER, RIGHT.
	 * @param comps ���
	 * @param flowAlignment ���뷽ʽ
	 * @return ���
	 */
	public static JPanel createFlowPane(Component[] comps, int flowAlignment) {// by
		return GUICoreUtils.createFlowPane(comps, flowAlignment, 0);
	}

	/**
	 * ����һ��Flow Pane, flowAligment��FlowLayout.LEFT, CENTER, RIGHT.
	 * @param comps ���
	 * @param flowAlignement ���뷽ʽ
	 * @param hSpace ˮƽ���
	 * @return ���
	 */
	public static JPanel createFlowPane(Component[] comps, int flowAlignment, int hSpace) {// by
		// peter
		return GUICoreUtils.createFlowPane(comps, flowAlignment, hSpace, 0);
	}

	/**
	 * ����һ��Flow Pane, flowAligment��FlowLayout.LEFT, CENTER, RIGHT,
	 * @param comps ���
	 * @param flowAlignment ���뷽ʽ
	 * @param hSpace ��ֱ���
	 * @param vSpace ˮƽ���
	 * @return  ���
	 */
	public static JPanel createFlowPane(Component[] comps, int flowAlignment, int hSpace, int vSpace) {
		JPanel leftPane = new /**/JPanel();
		leftPane.setLayout(new /**/FlowLayout(flowAlignment, hSpace, vSpace));
		for (int i = 0; i < comps.length; i++) {
			leftPane.add(comps[i]);
		}

		return leftPane;
	}

	/**
	 * ����һ������ʽ����Ϊ���ֵ����
	 * @param comps ����е�����Լ����ֵĲ�������3λ��������ѡ���ֱ��ʾ���뷽ʽ��ˮƽ��϶����ֱ��϶
	 * @return  ���
	 */
	public static JPanel createFlowPane(Object... comps) {
		int len = comps.length;
		int last = len;
		FlowLayout layout = new FlowLayout();
		JPanel panel = new JPanel(layout);
		if (len > 3 && comps[len - 3] instanceof Integer && comps[len - 2] instanceof Integer && comps[len - 1] instanceof Integer) {
			layout.setAlignment((Integer) comps[len - 3]);
			layout.setHgap((Integer) comps[len - 2]);
			layout.setVgap((Integer) comps[len - 1]);
			last = len - 3;
		} else if (len > 2 && comps[len - 1] instanceof Integer && comps[len - 2] instanceof Component) {
			layout.setAlignment((Integer) comps[len - 1]);
			last = len - 1;
		}
		for (int i = 0; i < last; i++) {
			if (comps[i] instanceof Component) {
				panel.add((Component)comps[i]);
			}
		}
		return panel;
	}

	/**
	 * ����һ��BorderPane, boderPosition=BoderLayout.CENTER, NORTH, SOUNTH, RIGHT.
	 * @param comp ���
	 * @param boderPosition λ��
	 * @return ���
	 */
	public static JPanel createBorderPane(JComponent comp, String boderPosition) {// by
		// peter
		JPanel newPane = new /**/JPanel();
		newPane.setLayout(FRGUIPaneFactory.createBorderLayout());
		newPane.add(comp, boderPosition);

		return newPane;
	}

    /**
     * ����һ���߽粼�ֵ����
     * @param components  ����е��������һ�����λ�����м䣬�ڶ������λ���ٶ��ߣ�
     *        ���������λ�����ϱߣ����ĸ����λ�������ߣ���������λ���ڱ���
     * @return  ���б߽粼�ֵ�����
     */
    public static JPanel createBorderLayoutPane(Component[] components) {
		JPanel pane = new JPanel(new BorderLayout());
		for (int i = 0, len = components.length; i < len; i++) {
			switch (i) {
				case 0:
					pane.add(components[0], BorderLayout.CENTER);
					break;
				case 1:
					if (components[1] != null) {
						pane.add(components[1], BorderLayout.EAST);
					}
					break;
				case 2:
					if (components[2] != null) {
						pane.add(components[2], BorderLayout.SOUTH);
					}
					break;
				case 3:
					if (components[3] != null) {
						pane.add(components[3], BorderLayout.WEST);
					}
					break;
				case CASE_FOUR:
					if (components[CASE_FOUR] != null) {
						pane.add(components[CASE_FOUR], BorderLayout.NORTH);
					}
					break;
				default:
					pane.add(components[0], BorderLayout.CENTER);
			}
		}
		return pane;
	}

	/**
	 * it's a very good method, user can get treePath from treeNode.
	 * @param �ڵ�
	 * @return ·��
	 */
	public static TreePath getTreePath(TreeNode treeNode) {
		List<TreeNode> objectList = new ArrayList<TreeNode>();

		// peter:��Ҫ�ж�treenode��Ϊ��.
		if (treeNode != null) {
			objectList.add(treeNode);
			while ((treeNode = treeNode.getParent()) != null) {
				objectList.add(0, treeNode);
			}
		}

		Object[] objects = new Object[objectList.size()];
		objectList.toArray(objects);

		// peter:Ϊ�˲��׳�Exception,ֱ�ӷ���null.
		if (objects.length <= 0) {
			return null;
		}

		return new TreePath(objects);
	}

	/**
	 * peter:�����������Ǹ�ѡ�е�TreePath
	 * @param tree ��
	 * @param treePaths ·��
	 * @return ·��
	 */
	public static TreePath getTopTreePath(JTree tree, TreePath[] treePaths) {
		if (tree == null || treePaths == null || treePaths.length == 0) {
			return null;
		}

		TreePath topTreePath = null;

		// peter:��ʼ�Ƚ���.
		int row = Integer.MAX_VALUE;
		for (int i = 0; i < treePaths.length; i++) {
			int tmpRow = tree.getRowForPath(treePaths[i]);
			if (tmpRow < row) {
				row = tmpRow;
				topTreePath = treePaths[i];
			}
		}

		return topTreePath;
	}

	/**
	 * ���UI��TitledBorder,Ĭ�ϵ�LineBorder����ɫ.
	 * @return ��ɫ
	 */
	public static Color getTitleLineBorderColor() {
		Border b = UIManager.getBorder("TitledBorder.border");
		if (b instanceof LineBorder) {
			return ((LineBorder) b).getLineColor();
		}

		return Color.GRAY;
	}

	/**
	 * peter: ɾ��ѡ�е����нڵ�
	 * @param ancestorWindow ������
	 * @param nodeList �ڵ��б�
	 * @return ����ֵ
	 */
	public static boolean removeJListSelectedNodes(Window ancestorWindow, JList nodeList) {
		int selectedIndex = nodeList.getSelectedIndex();
		if (selectedIndex == -1) {
			return false;
		}

		int returnVal = JOptionPane.showConfirmDialog(ancestorWindow, Inter.getLocText("Utils-Are_you_sure_to_remove_the_selected_item") + "?", Inter.getLocText("Remove"),
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (returnVal == JOptionPane.OK_OPTION) {
			int minSelectedIndex = nodeList.getMinSelectionIndex();
			int[] selectedIndices = nodeList.getSelectedIndices();
			// peter:������Ȼ��Ӻ���ǰɾ�����������ᷢ������.
			Arrays.sort(selectedIndices);
			for (int i = selectedIndices.length - 1; i >= 0; i--) {
				((DefaultListModel) nodeList.getModel()).remove(selectedIndices[i]);
			}

			if (nodeList.getModel().getSize() > 0) {
				if (minSelectedIndex < nodeList.getModel().getSize()) {
					// nodeList.setSelectedIndex(minSelectedIndex);
					nodeList.setSelectedValue(nodeList.getModel().getElementAt(minSelectedIndex), true);
				} else {
					nodeList.setSelectedValue(nodeList.getModel().getElementAt(nodeList.getModel().getSize() - 1), true);
					// nodeList.setSelectedIndex(nodeList.getModel().getSize() -
					// 1);
				}
			}

			return true;
		}

		return false;
	}

	/**
	 * �õ�Spinner�ı༭��
	 * @param spinner spinner
	 * @return �ı���
	 */
	public static JFormattedTextField getSpinnerTextField(JSpinner spinner) {
		JComponent editor = spinner.getEditor();
		if (editor instanceof JSpinner.DefaultEditor) {
			return ((JSpinner.DefaultEditor) editor).getTextField();
		} else {
			System.err.println("Unexpected editor type: " + spinner.getEditor().getClass() + " isn't a descendant of DefaultEditor");
			return null;
		}
	}

	/**
	 * Ϊ��Spinner�ı༭�����ÿ��
	 * @param spinner spinner
	 * @columns ����
	 */
	public static void setColumnForSpinner(JSpinner spinner, int columns) {
		JFormattedTextField cftf = getSpinnerTextField(spinner);
		if (cftf != null) {
			cftf.setColumns(columns); // specify more width than we need
			cftf.setHorizontalAlignment(UITextField.LEFT);
		}
	}

	/**
	 * ************************************************************************
	 * peter:�ػ�.
	 *
	 * @param component ���
	 */
	public static void repaint(final Component component) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				component.invalidate();
				component.validate();
				component.repaint();
			}
		});
	}

	
	/**
	 * harry�������Զ��尴ť(ָ����Ƥ�����Ƶİ�ť)
	 * @param icon ͼ��
	 * @param roverIcon ����ͼ��
	 * @param pressedIcon ���ͼ��
	 * @return ��ť
	 */
	public static UIButton createTransparentButton(Icon icon, Icon roverIcon, Icon pressedIcon) {
		UIButton button = new UIButton();
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setBorder(null);
		button.setMargin(null);
		button.setOpaque(false);
		button.setIcon(icon);
		button.setRolloverEnabled(true);
		button.setRolloverIcon(roverIcon);
		button.setPressedIcon(pressedIcon);
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		button.setRequestFocusEnabled(false);

		return button;
	}

	public static DataFunction[] FunctionArray = null;

	/**
	 * ��ȡ����
	 * @return DataFunction[] ����
	 * 
	 */
	public static DataFunction[] getFunctionArray() {
		if (FunctionArray == null) {
			FunctionArray = new DataFunction[]{new SumFunction(), new AverageFunction(), new MaxFunction(), new MinFunction(), new CountFunction(), new NoneFunction(),};
		}

		return FunctionArray;
	}

	/**
	 * ��UIComboBox�ڲ�����ItemListener�������ѡ��ĳ��
	 *
	 * @param jcb ��ѡ��
	 * @param item ѡ��
	 */
	public static void setSelectedItemQuietly(UIComboBox jcb, Object item) {
		ItemListener[] listeners = jcb.getItemListeners();
		for (ItemListener aListener : listeners) {
			jcb.removeItemListener(aListener);
		}

		jcb.setSelectedItem(item);

		for (ItemListener aListener : listeners) {
			jcb.addItemListener(aListener);
		}
	}

	/**
	 * ��UIComboBox�ڲ�����ItemListener�������ѡ��ĳ��
	 *
	 * @param jcb ��ѡ��
	 * @param index ѡ�����
	 */
	public static void setSelectedItemQuietly(UIComboBox jcb, int index) {
		ItemListener[] listeners = jcb.getItemListeners();
		for (ItemListener aListener : listeners) {
			jcb.removeItemListener(aListener);
		}

		jcb.setSelectedIndex(index);

		for (ItemListener aListener : listeners) {
			jcb.addItemListener(aListener);
		}
	}

	/**
	 * �Ƿ���ͬһ����
	 * @param oneRect ���ο�
	 * @param otherRect �������ο�
	 * @return ͬ��
	 */
	public static boolean isTheSameRect(Rectangle oneRect, Rectangle otherRect) {
		return oneRect.getX() == otherRect.getX()
				&& oneRect.getY() == otherRect.getY()
				&& oneRect.getWidth() == otherRect.getWidth()
				&& oneRect.getHeight() == otherRect.getHeight();
	}
}
