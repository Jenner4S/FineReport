package com.fr.design.parameter;

import java.awt.BorderLayout;
import java.awt.Component;

import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;

import com.fr.base.Parameter;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.general.Inter;

public class ParameterPane extends BasicBeanPane<Parameter> {
	/*
	 * richer:�����������
	 * ������������6.5�У�������������ϴ�Ķ���ȡ�����ڲ�������ĵط����ñ༭��������
	 * ������������Ĭ�ϲ�������Ҳ�Զ����ɣ������ܸ��û���
	 * �������������������Ĵ�С����ʹ��(600, 350),����ͳһ
	 */

	// ��������
	private UITextField nameTextField;

	// ����Ĭ��ֵ
	private ValueEditorPane valueEditor;

	public ParameterPane() {
		this.initComponents();
	}

	protected void initComponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		nameTextField = new UITextField(10);
		nameTextField.setEditable(false);
		
		JPanel textFieldPanel=FRGUIPaneFactory.createBorderLayout_S_Pane();
		textFieldPanel.add(nameTextField,BorderLayout.CENTER);
		
		valueEditor = ValueEditorPaneFactory.createBasicValueEditorPane();

		// richer:Ҫ������ʾ�Ŀؼ�
		Component[][] components = {{null},
				{ null, new UILabel(Inter.getLocText("Name") + ":"),textFieldPanel },
				{ null, new UILabel(Inter.getLocText("Utils-Default_Value") + ":"),valueEditor }
				};
		double p =TableLayout.PREFERRED;
		double f =TableLayout.FILL;
		double[] rowSize = {p, p, p, p};
		double[] columnSize = {p, p, f, p, p};

		JPanel centerPane = TableLayoutHelper.createGapTableLayoutPane(
				components, rowSize, columnSize, 20, 10);
		this.add(centerPane, BorderLayout.CENTER);
	}
	
	public void setNameEditable(boolean bool) {
		this.nameTextField.setEditable(true);
	}
	
	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("Parameter");
	}

	@Override
	public void populateBean(Parameter parameter) {
		if (parameter == null) {
			return;
		}
		
		this.nameTextField.setText(parameter.getName());
		this.valueEditor.populate(parameter.getValue());
	}

	@Override
	public Parameter updateBean() {
		Parameter parameter = new Parameter();
		parameter.setName(this.nameTextField.getText());
    	parameter.setValue(valueEditor.update());
		return parameter;
	}
	
	public void checkValid() throws Exception{
		this.valueEditor.checkValid();
	}
}
