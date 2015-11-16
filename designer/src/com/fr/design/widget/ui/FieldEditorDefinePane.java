package com.fr.design.widget.ui;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;

import com.fr.design.gui.frpane.TreeSettingPane;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.form.ui.FieldEditor;
import com.fr.general.Inter;
import com.fr.design.widget.DataModify;

public abstract class FieldEditorDefinePane<T extends FieldEditor> extends AbstractDataModify<T> {
	private UICheckBox allowBlankCheckBox;
	// richer:������Ϣ�������пؼ����е����ԣ����Էŵ�������
	private UITextField errorMsgTextField;

	public FieldEditorDefinePane() {
		this.initComponents();
	}

	protected void initComponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		JPanel northPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
		this.add(northPane, BorderLayout.NORTH);
		JPanel firstPanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		firstPanel.setBorder(BorderFactory.createEmptyBorder(0, -2, 0, 0));
		//JPanel firstPanel = FRGUIPaneFactory.createBorderLayout_M_Pane();
		allowBlankCheckBox = new UICheckBox(Inter.getLocText("Allow_Blank"));
		// �Ƿ�����Ϊ��
		firstPanel.add(allowBlankCheckBox);
		allowBlankCheckBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				errorMsgTextField.setEnabled(!allowBlankCheckBox.isSelected());
			}
		});

		// ������Ϣ
		JPanel errorMsgPane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
		firstPanel.add(errorMsgPane);
		northPane.add(firstPanel);
		errorMsgPane.add(new UILabel(Inter.getLocText(new String[]{"Error", "Tooltips"}) + ":"));
		errorMsgTextField = new UITextField(16);
		errorMsgPane.add(errorMsgTextField);

		// richer:��ҪΪ�˷���鿴�Ƚϳ��Ĵ�����Ϣ
		errorMsgTextField.getDocument().addDocumentListener(new DocumentListener() {

			public void changedUpdate(DocumentEvent e) {
				errorMsgTextField.setToolTipText(errorMsgTextField.getText());
			}

			public void insertUpdate(DocumentEvent e) {
				errorMsgTextField.setToolTipText(errorMsgTextField.getText());
			}

			public void removeUpdate(DocumentEvent e) {
				errorMsgTextField.setToolTipText(errorMsgTextField.getText());
			}
		});
		JPanel contentPane = this.setFirstContentPane();
		if (contentPane != null) {
			//contentPane.add(firstPanel);
			this.add(contentPane, BorderLayout.CENTER);
		} else {
			//this.add(firstPanel, BorderLayout.CENTER);
		}
	}

	@Override
	public void populateBean(T ob) {
		this.allowBlankCheckBox.setSelected(ob.isAllowBlank());
		errorMsgTextField.setEnabled(!allowBlankCheckBox.isSelected());
		this.errorMsgTextField.setText(ob.getErrorMessage());

		populateSubFieldEditorBean(ob);
	}

	protected abstract void populateSubFieldEditorBean(T ob);

	@Override
	public T updateBean() {
		T e = updateSubFieldEditorBean();

		e.setAllowBlank(this.allowBlankCheckBox.isSelected());
		e.setErrorMessage(this.errorMsgTextField.getText());

		return e;
	}

	protected abstract T updateSubFieldEditorBean();

	protected abstract JPanel setFirstContentPane();

	@Override
	public void checkValid() throws Exception {

	}
}
