package com.fr.design.gui.itable;

import javax.swing.JComponent;
import com.fr.design.gui.ilable.UILabel;

/**
 * ���� �༭�ؼ� ���� ��toString
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2013-1-6 ����03:37:18
 */
public class UIComponentTable extends UITable {
	
	public UIComponentTable(int columnSize) {
		this.setModel(new UITableComponentModel(columnSize));
		initComponents();
	}
	
	/**
	 * @param value  �����е�ֵ(�ַ���)
	 * @param row
	 * @param column
	 * @return �б���Ĭ����ʾ�Ķ���������кܶ����ݣ�����װ��һ��JPanel����Ƕ����
	 */
	protected JComponent getRenderCompoment(Object value, int row, int column) {
		UILabel text = new UILabel();
		if (value != null) {
			text.setText(value.toString());
		}
		if(value instanceof JComponent) {
			return (JComponent)value;
		}
		return text;
	}

}
