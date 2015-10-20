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
 * _denny: �����BasicPaneͨ������populate & update����
 * �ǵ�populate һ��Ҫ�ڶ�Ӧ��dialog setVisible֮ǰpopulate
 */
public abstract class BasicPane extends JPanel {

	/**
	 *��ʾ����
	 * @param window ����
	 * @return �Ի���
	 */
	public BasicDialog showWindow(Window window) {
		return this.showWindow(window, null);
	}


	/**
	 * ��ʾ����
	 * @param window ����
	 * @param l �Ի��������
	 * @return �Ի���
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
	 * ��ʾ����
	 * @param window ����
	 * @param isNeedButtonsPane �Ƿ���Ҫȷ��ɾ����ť
	 * @return �Ի���
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
	 * ͼ������ѡ��ʱ �����İ�ť��С, ���ʺ��������С, ��Ϊͼ���С Ĭ���ǹ涨�õ�, ��ô�����СҲ�Ǳ������.
	 * ���Ұ��� ������ʾ�� ��������Ĵ�С
	 * @param window ����
	 * @param l ������
	 * @return �Ի���
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
	 * ��ʾ����
	 * @param window ����
	 * @param l �Ի��������
	 * @return �Ի���
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
	 * ��ʾ����
	 * @param window ����
	 * @param l �Ի��������
	 * @return �Ի���
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
	 * ��ʾ����
	 * @param window ����
	 * @param l �Ի��������
	 * @return �Ի���
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
	 * ��ʾ����
	 * @param window ����
	 * @param l �Ի��������
	 * @return �Ի���
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
	 * ��ʾ����
	 * @param window ����
	 * @param l �Ի��������
	 * @return �Ի���
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
	 * ��ʾ����
	 * @param window ����
	 * @return �Ի���
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
	 * ��Ϊ�������
	 * @return ���
	 */
	public NamePane asNamePane() {
		return new NamePane(this);
	}

	/**
     * ����Ƿ���Ϲ淶
     *
     * @throws Exception �쳣
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
	     * ����Ƿ���Ϲ淶
	     *
	     * @throws Exception �쳣
	    */
		public void checkValid() throws Exception {
			super.checkValid();

			this.centerPane.checkValid();
		}

		/**
		 * ������Ըı�ļ�����
		 * @param listener ������
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
