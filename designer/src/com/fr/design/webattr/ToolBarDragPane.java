package com.fr.design.webattr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;

import javax.swing.Icon;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.gui.ilable.UILabel;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.gui.core.WidgetOption;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;
import com.fr.report.web.Location;
import com.fr.report.web.ToolBarManager;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;
import com.fr.design.utils.gui.GUICoreUtils;

/**
 * �µ���קToolBar button��ʵ���Զ��幤���� ����web�����Ƕ���.Ӧ�ò���ToolBarDragPane����Ϊʵ��û���ṩdrag����
 * 
 * @editor zhou 2012-3-22����3:57:22
 */

public class ToolBarDragPane extends WidgetToolBarPane {
	private static final int COLUMN = 4;
	private int row = 5;
	private DefaultTableModel toolbarButtonTableModel;
	private JTable layoutTable;
	private UICheckBox isUseToolBarCheckBox = new UICheckBox(Inter.getLocText("Use_ToolBar") + ":"); // �Ƿ�ʹ�ù�����
	private boolean isEnabled;

	public ToolBarDragPane() {
		WidgetOption[] options = ExtraDesignClassManager.getInstance().getWebWidgetOptions();
		if(options != null){
			double optionNums = options.length;
			row += Math.ceil(optionNums / COLUMN);
		}
		toolbarButtonTableModel = new TableModel(row ,COLUMN);
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		JPanel north = FRGUIPaneFactory.createBorderLayout_S_Pane();
		UIButton defaultButton = new UIButton(Inter.getLocText("Restore-Default"));
		// �ָ�Ĭ�ϰ�ť
		defaultButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				northToolBar.removeButtonList();
				northToolBar.removeAll();
				southToolBar.removeButtonList();
				southToolBar.removeAll();
				if (defaultToolBar == null) {
					return;
				}
				ToolBarManager toolBarManager = defaultToolBar;
				toolBarManager.setToolBarLocation(Location.createTopEmbedLocation());
				ToolBarManager[] tbm = new ToolBarManager[] { toolBarManager };
				populateBean(tbm);
				ToolBarDragPane.this.repaint();
			}
		});

		north.add(isUseToolBarCheckBox, BorderLayout.WEST);
		JPanel aa = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
		aa.add(defaultButton);
		north.add(aa, BorderLayout.CENTER);
		this.add(north, BorderLayout.NORTH);

		northToolBar = new ToolBarPane();
		northToolBar.setPreferredSize(new Dimension(ImageObserver.WIDTH, 26));
		northToolBar.setBackground(Color.lightGray);

		UIButton topButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/arrow/arrow_up.png"));
		topButton.setBorder(null);
		// topButton.setMargin(null);
		topButton.setOpaque(false);
		topButton.setContentAreaFilled(false);
		topButton.setFocusPainted(false);
		topButton.setRequestFocusEnabled(false);
		topButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (isSelectedtable()) {
					WidgetOption no = (WidgetOption)layoutTable.getValueAt(layoutTable.getSelectedRow(), layoutTable.getSelectedColumn());
					Widget widget = no.createWidget();
					ToolBarButton tb = new ToolBarButton(no.optionIcon(), widget);
					tb.setNameOption(no);
					northToolBar.add(tb);
					northToolBar.validate();
					northToolBar.repaint();
				} else {
					JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("ChooseOneButton"));
				}
			}
		});
		UIButton downButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/arrow/arrow_down.png"));
		downButton.setBorder(null);
		downButton.setMargin(null);
		downButton.setOpaque(false);
		downButton.setContentAreaFilled(false);
		downButton.setFocusPainted(false);
		downButton.setRequestFocusEnabled(false);
		downButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (isSelectedtable()) {
					WidgetOption no = (WidgetOption)layoutTable.getValueAt(layoutTable.getSelectedRow(), layoutTable.getSelectedColumn());
					Widget widget = no.createWidget();
					ToolBarButton tb = new ToolBarButton(no.optionIcon(), widget);
					tb.setNameOption(no);
					southToolBar.add(tb);
					southToolBar.validate();
					southToolBar.repaint();
				} else {
					JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("ChooseOneButton"));
				}
			}
		});
		layoutTable = new JTable(toolbarButtonTableModel);
		layoutTable.setDefaultRenderer(Object.class, tableRenderer);
		layoutTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		layoutTable.setColumnSelectionAllowed(false);
		layoutTable.setRowSelectionAllowed(false);
		layoutTable.setBackground(Color.white);
		layoutTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 1 && !(SwingUtilities.isRightMouseButton(e)) && isEnabled) {
					WidgetOption no = (WidgetOption)layoutTable.getValueAt(layoutTable.getSelectedRow(), layoutTable.getSelectedColumn());
					Widget widget = no.createWidget();
					ToolBarButton tb = new ToolBarButton(no.optionIcon(), widget);
					tb.setNameOption(no);
					northToolBar.add(tb);
					northToolBar.validate();
					northToolBar.repaint();
				}
			}
		});
		JPanel center = FRGUIPaneFactory.createBorderLayout_S_Pane();
		center.setBackground(Color.white);
		center.add(topButton, BorderLayout.NORTH);
		JPanel small = FRGUIPaneFactory.createBorderLayout_S_Pane();
		small.setBackground(Color.white);
		small.add(new UILabel(StringUtils.BLANK), BorderLayout.NORTH);
		small.add(layoutTable, BorderLayout.CENTER);
		center.add(small, BorderLayout.CENTER);
		center.add(downButton, BorderLayout.SOUTH);
		southToolBar = new ToolBarPane();
		southToolBar.setPreferredSize(new Dimension(ImageObserver.WIDTH, 26));
		southToolBar.setBackground(Color.lightGray);
		JPanel movePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		JPanel northContentPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		SettingToolBar top = new SettingToolBar(Inter.getLocText("ToolBar_Top"), northToolBar);
		northContentPane.add(top, BorderLayout.EAST);
		northContentPane.add(northToolBar, BorderLayout.CENTER);
		northContentPane.setBackground(Color.lightGray);

		JPanel southContentPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		SettingToolBar bottom = new SettingToolBar(Inter.getLocText("ToolBar_Bottom"), southToolBar);
		southContentPane.add(bottom, BorderLayout.EAST);
		southContentPane.add(southToolBar, BorderLayout.CENTER);
		southContentPane.setBackground(Color.lightGray);

		movePane.add(northContentPane, BorderLayout.NORTH);
		movePane.add(center, BorderLayout.CENTER);
		movePane.add(southContentPane, BorderLayout.SOUTH);

		this.add(new JScrollPane(movePane), BorderLayout.CENTER);
		isUseToolBarCheckBox.setSelected(true);
	}

	private boolean isSelectedtable() {
		for (int i = 0; i < layoutTable.getColumnCount(); i++) {
			if (layoutTable.isColumnSelected(i)) {
				return true;
			}
		}
		return false;
	}

	public void setAllEnabled(boolean b) {
		GUICoreUtils.setEnabled(this, b);
		isEnabled = b;
	}

	/**
	 * �Ƿ�ѡ��
	 * @return ͬ��
	 */
	public boolean isUseToolbar() {
		return this.isUseToolBarCheckBox.isSelected();
	}

	private class TableModel extends DefaultTableModel {
		public TableModel(int i, int j) {
			super(i, j);
		}

		// ��ֹjtable��˫���༭����
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	}

	public void setDefaultToolBar(ToolBarManager defaultToolBar, WidgetOption[] buttonArray) {
		super.setDefaultToolBar(defaultToolBar);
		if (buttonArray != null) {
			for (int i = 0; i < buttonArray.length; i++) {
				toolbarButtonTableModel.setValueAt(buttonArray[i], i % row, i / row);
			}
		}

	}

	DefaultTableCellRenderer tableRenderer = new DefaultTableCellRenderer() {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if (value instanceof WidgetOption) {
				WidgetOption nameOption = (WidgetOption)value;
				this.setText(nameOption.optionName());

				Icon icon = nameOption.optionIcon();
				if (icon != null) {
					this.setIcon(icon);
				}
			}
			if (value == null) {
				this.setText(StringUtils.EMPTY);
				this.setIcon(null);
			}
			return this;
		}
	};

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText(new String[]{"ReportServerP-Toolbar", "Set"});
	}

	public void setCheckBoxSelected(boolean b) {
		this.isUseToolBarCheckBox.setSelected(b);
	}

	@Override
	public void populateBean(ToolBarManager[] toolBarManager) {
		if (ArrayUtils.isEmpty(toolBarManager)) {
			defaultToolBar.setToolBarLocation(Location.createTopEmbedLocation());
			toolBarManager = new ToolBarManager[] { defaultToolBar };
		}
		super.populateBean(toolBarManager);
	}
	
	@Override
	public ToolBarManager[] updateBean() {
		if(!isUseToolbar()){
			return new ToolBarManager[0];
		}
		return super.updateBean();
	}

}
