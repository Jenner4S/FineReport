package com.fr.design.dialog;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.general.Inter;
import com.fr.stable.core.PropertyChangeAdapter;
import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;

/*
 * _denny: 这里的BasicPane通常会有populate & update方法
 * 记得populate 一定要在对应的dialog setVisible之前populate
 */
public abstract class BasicPane extends JPanel {

	/**
	 *显示窗口
	 * @param window 窗口
	 * @return 对话框
	 */
	public BasicDialog showWindow(Window window) {
		return this.showWindow(window, null);
	}


	/**
	 * 显示窗口
	 * @param window 窗口
	 * @param l 对话框监听器
	 * @return 对话框
	 */
	public BasicDialog showWindow(Window window, DialogActionListener l) {
		BasicDialog dg;
		if (window instanceof Frame) {
			dg = new DIALOG((Frame) window);
		} else {
			dg = new DIALOG((Dialog) window);
		}

		if (l != null) {
			dg.addDialogActionListener(l);
		}

		dg.setBasicDialogSize(BasicDialog.DEFAULT);
		GUICoreUtils.centerWindow(dg);
		dg.setResizable(false);
		return dg;
	}

	/**
	 * 显示窗口
	 * @param window 窗口
	 * @param isNeedButtonsPane 是否需要确定删除按钮
	 * @return 对话框
	 */
	public BasicDialog showWindow(Window window, boolean isNeedButtonsPane) {
		BasicDialog dg;
		if (window instanceof Frame) {
			dg = new DIALOG((Frame) window, isNeedButtonsPane);
		} else {
			dg = new DIALOG((Dialog) window, isNeedButtonsPane);
		}
		dg.setBasicDialogSize(BasicDialog.DEFAULT);
		GUICoreUtils.centerWindow(dg);
		dg.setResizable(false);
		return dg;
	}



	/**
	 * 图表类型选择时 弹出的按钮大小, 不适合用最大最小, 因为图表大小 默认是规定好的, 那么界面大小也是必须配合.
	 * 并且包括 条件显示中 多个条件的大小
	 * @param window 窗口
	 * @param l 监听器
	 * @return 对话框
	 */
	public BasicDialog showWindow4ChartType(Window window, DialogActionListener l) {
		BasicDialog dg;
		if (window instanceof Frame) {
			dg = new DIALOG((Frame) window);
		} else {
			dg = new DIALOG((Dialog) window);
		}

		if (l != null) {
			dg.addDialogActionListener(l);
		}

		dg.setBasicDialogSize(BasicDialog.CHART);
		GUICoreUtils.centerWindow(dg);
		dg.setResizable(false);
		return dg;
	}

	/**
	 * 显示窗口
	 * @param window 窗口
	 * @param l 对话框监听器
	 * @return 对话框
	 */
	public BasicDialog showSmallWindow(Window window, DialogActionListener l) {
		BasicDialog dg;
		if (window instanceof Frame) {
			dg = new DIALOG((Frame) window);
		} else {
			dg = new DIALOG((Dialog) window);
		}

		if (l != null) {
			dg.addDialogActionListener(l);
		}
		dg.setBasicDialogSize(BasicDialog.SMALL);
		GUICoreUtils.centerWindow(dg);
		dg.setResizable(false);
		return dg;
	}

	/**
	 * 显示窗口
	 * @param window 窗口
	 * @param l 对话框监听器
	 * @return 对话框
	 */
	public BasicDialog showMediumWindow(Window window, DialogActionListener l) {
		BasicDialog dg;
		if (window instanceof Frame) {
			dg = new DIALOG((Frame) window);
		} else {
			dg = new DIALOG((Dialog) window);
		}

		if (l != null) {
			dg.addDialogActionListener(l);
		}
		dg.setBasicDialogSize(BasicDialog.MEDIUM);
		GUICoreUtils.centerWindow(dg);
		dg.setResizable(false);
		return dg;
	}

	/**
	 * 显示窗口
	 * @param window 窗口
	 * @param l 对话框监听器
	 * @return 对话框
	 */
	public BasicDialog showLargeWindow(Window window, DialogActionListener l) {
		BasicDialog dg;
		if (window instanceof Frame) {
			dg = new DIALOG((Frame) window);
		} else {
			dg = new DIALOG((Dialog) window);
		}

		if (l != null) {
			dg.addDialogActionListener(l);
		}
		dg.setBasicDialogSize(BasicDialog.LARGE);
		GUICoreUtils.centerWindow(dg);
		dg.setResizable(false);
		return dg;
	}

	/**
	 * 显示窗口
	 * @param window 窗口
	 * @param l 对话框监听器
	 * @return 对话框
	 */
	public UIDialog showUnsizedWindow(Window window, DialogActionListener l) {
		UIDialog dg;
		if (window instanceof Frame) {
			dg = new UnsizedDialog((Frame) window);
		} else {
			dg = new UnsizedDialog((Dialog) window);
		}

		if (l != null) {
			dg.addDialogActionListener(l);
		}
		GUICoreUtils.centerWindow(dg);
		dg.setResizable(false);
		return dg;
	}

	/**
	 * 显示窗口
	 * @param window 窗口
	 * @param l 对话框监听器
	 * @return 对话框
	 */
	public BasicDialog showWindow4ChartMapArray(Window window,DialogActionListener l){
		BasicDialog dg;
		if (window instanceof Frame) {
			dg = new DIALOG((Frame) window);
		} else {
			dg = new DIALOG((Dialog) window);
		}

		if (l != null) {
			dg.addDialogActionListener(l);
		}
		dg.setBasicDialogSize(BasicDialog.MAP_SIZE);
		GUICoreUtils.centerWindow(dg);
		dg.setResizable(false);
		return dg;
	}


	/**
	 * 显示窗口
	 * @param window 窗口
	 * @return 对话框
	 */
	public BasicDialog showWindow4UpdateOnline(Window window){
		BasicDialog dg;
		if (window instanceof Frame) {
			dg = new DIALOG((Frame) window,false);
		} else {
			dg = new DIALOG((Dialog) window,false);
		}
		dg.setBasicDialogSize(BasicDialog.UPDATE_ONLINE_SIZE);
		GUICoreUtils.centerWindow(dg);
		dg.setResizable(false);
		return dg;
	}

	protected abstract String title4PopupWindow();

	public String getTitle(){
		return title4PopupWindow();
	}

	/**
	 * 作为名字面板
	 * @return 面板
	 */
	public NamePane asNamePane() {
		return new NamePane(this);
	}

	/**
     * 检查是否符合规范
     *
     * @throws Exception 异常
    */
	public void checkValid() throws Exception {
	}

	public static final class NamePane extends BasicPane {
		private UITextField nameTextField;
		private BasicPane centerPane;
		private UILabel showfield;
		private PropertyChangeAdapter changeListener;

		private NamePane(BasicPane bPane) {
			this.setLayout(new BorderLayout(4, 4));

			nameTextField = new UITextField(30);
			JPanel northPane = new JPanel(new BorderLayout(4, 4));
			northPane.add(new UILabel(Inter.getLocText("FR-Designer-Hyperlink_Name") + ":"), BorderLayout.WEST);
			northPane.add(nameTextField, BorderLayout.CENTER);
			northPane.add(showfield = new UILabel(" "), BorderLayout.EAST);
			showfield.setForeground(new Color(204, 0, 1));
			showfield.setPreferredSize(new Dimension(220, showfield.getPreferredSize().height));
			this.add(northPane, BorderLayout.NORTH);
			this.centerPane = bPane;
			this.add(bPane, BorderLayout.CENTER);
			this.nameTextField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {

				@Override
				public void insertUpdate(DocumentEvent e) {
					doTextChanged();
				}

				@Override
				public void removeUpdate(DocumentEvent e) {
					doTextChanged();
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					doTextChanged();
				}

			});
		}

		private void doTextChanged() {
			if (changeListener != null) {
				changeListener.propertyChange();
			}
		}

		public String getObjectName() {
			return this.nameTextField.getText().trim();
		}

		public void setObjectName(String name) {
			this.nameTextField.setText(name);
		}

		public void setShowText(String name) {
			this.showfield.setText(name);
		}

		@Override
		protected String title4PopupWindow() {
			return centerPane.title4PopupWindow();
		}

		/**
	     * 检查是否符合规范
	     *
	     * @throws Exception 异常
	    */
		public void checkValid() throws Exception {
			super.checkValid();

			this.centerPane.checkValid();
		}

		/**
		 * 添加属性改变的监听器
		 * @param listener 监听器
		 */
		public void addPropertyChangeListener(PropertyChangeAdapter listener) {
			this.changeListener = listener;
		}
	}

	private class DIALOG extends BasicDialog {
		public DIALOG(Frame parent) {
			super(parent, BasicPane.this);
			this.setTitle(BasicPane.this.title4PopupWindow());
		}

		public DIALOG(Dialog parent) {
			super(parent, BasicPane.this);
			this.setTitle(BasicPane.this.title4PopupWindow());
		}


		public DIALOG(Frame parent, boolean isNeedButtonPane) {
			super(parent, BasicPane.this, isNeedButtonPane);
			this.setTitle(BasicPane.this.title4PopupWindow());
		}


		public DIALOG(Dialog parent, boolean isNeedButtonPane) {
			super(parent, BasicPane.this, isNeedButtonPane);
			this.setTitle(BasicPane.this.title4PopupWindow());
		}



		/**
		 * init Components
		 */


		/**
		 * Check valid.
		 */
		public void checkValid() throws Exception {
			BasicPane.this.checkValid();
		}

	}

	private class UnsizedDialog extends UIDialog {

		public UnsizedDialog(Frame parent) {
			super(parent, BasicPane.this);
			this.setTitle(BasicPane.this.title4PopupWindow());
		}

		public UnsizedDialog(Dialog parent) {
			super(parent, BasicPane.this);
			this.setTitle(BasicPane.this.title4PopupWindow());
		}


		public void checkValid() throws Exception {
			BasicPane.this.checkValid();
		}

	}


}