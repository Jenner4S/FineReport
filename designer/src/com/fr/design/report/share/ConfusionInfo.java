/**
 * 
 */
package com.fr.design.report.share;

import com.fr.data.impl.EmbeddedTableData;
import com.fr.stable.StringUtils;

/**
 * ���ݼ�������ص���Ϣ
 * 
 * @author neil
 * 
 * @date: 2015-3-9-����10:56:40
 */
public class ConfusionInfo {

	//���ݼ���
	private String tabledataName;
	//������ÿһ�е�key
	private String[] confusionKeys;
	//����
	private String[] columnNames;
	//������
	private Class[] colType;
	
	public ConfusionInfo(EmbeddedTableData tabledata, String tabledataName){
		this.tabledataName = tabledataName;
		
		int columnCount = tabledata.getColumnCount();
		this.confusionKeys = new String[columnCount];
		this.columnNames = new String[columnCount];
		this.colType = new Class[columnCount];
		for (int i = 0; i < columnCount; i++) {
			columnNames[i] = tabledata.getColumnName(i);
			confusionKeys[i] = StringUtils.EMPTY;
			colType[i] = tabledata.getColumnClass(i);
		}
		
	}
	
	public ConfusionInfo(String tabledataName, String[] confusionKeys, String[] columnNames, Class[] colType) {
		this.tabledataName = tabledataName;
		this.confusionKeys = confusionKeys;
		this.columnNames = columnNames;
		this.colType = colType;
	}
	
	public String getTabledataName() {
		return tabledataName;
	}

	public void setTabledataName(String tabledataName) {
		this.tabledataName = tabledataName;
	}

	public String[] getConfusionKeys() {
		return confusionKeys;
	}

	public void setConfusionKeys(String[] confusionKeys) {
		this.confusionKeys = confusionKeys;
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	public Class[] getColType() {
		return colType;
	}

	public void setColType(Class[] colType) {
		this.colType = colType;
	}
	
	/**
	 * ָ�������Ƿ����������͵�
	 * 
	 * @param col ָ������
	 * 
	 * @return ָ�������Ƿ����������͵�
	 * 
	 */
	public boolean isNumberColumn(int col){
		return colType[col] == Integer.class ||
				colType[col] == Double.class ||
				colType[col] == Float.class;
	}
	
}
