package com.fr.design.gui.itable;


/**
 * Ԥ��� ֻ��UITableDataModel ���һ����: setValue ����toString ���ֱ༭component����.
 * 
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2013-1-6 ����03:32:22
 */
public class UITableComponentModel extends UITableDataModel {

	public UITableComponentModel(int columnSize) {
		super(columnSize);
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if(rowIndex >= values.size() || columnIndex >= columnSize || aValue == null) {
			return;
		}
		values.get(rowIndex)[columnIndex] = aValue;
	}
}
