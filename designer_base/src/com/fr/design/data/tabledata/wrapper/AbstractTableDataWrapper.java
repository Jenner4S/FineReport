package com.fr.design.data.tabledata.wrapper;

import com.fr.base.TableData;
import com.fr.data.TableDataSource;
import com.fr.data.impl.EmbeddedTableData;
import com.fr.design.DesignModelAdapter;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.datapane.preview.PreviewTablePane;
import com.fr.design.data.tabledata.tabledatapane.AbstractTableDataPane;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.utils.DesignUtils;
import com.fr.general.ComparatorUtils;
import com.fr.stable.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractTableDataWrapper implements TableDataWrapper {
	protected TableData tabledata;
	private String name;
	private List<String> columnNameList;
	public AbstractTableDataWrapper(TableData tabledata) {
		this(tabledata,null);
	}

	public AbstractTableDataWrapper(TableData tabledata,String name) {
		this.tabledata = tabledata;
		this.name = name;
	}

	public TableData getTableData() {
		return tabledata;
	}

    /**
	 * ��ȡ���ݼ�����������list
	 * 
	 * @return ���ݼ�����������list
	 * 
	 *
	 * @date 2014-11-24-����3:51:41
	 * 
	 */
	public List<String> calculateColumnNameList() {
		if(columnNameList != null){
			return columnNameList;
		}
		
		DesignModelAdapter adapter = DesignModelAdapter.getCurrentModelAdapter();
		TableDataSource tds = adapter == null ? null : adapter.getBook();
		String[] colNames = tabledata.getColumnNames(tds);
		if(ArrayUtils.isNotEmpty(colNames)){
            columnNameList = new ArrayList<String>();
			columnNameList.addAll(Arrays.asList(colNames));
			return columnNameList;
		}
		
		EmbeddedTableData embeddedTableData = null;
		try {
			embeddedTableData = DesignTableDataManager.previewTableDataNotNeedInputParameters(tabledata, TableData.RESULT_NOT_NEED, false);
		} catch (Exception e) {
            if (e.getMessage()!=null) {
                DesignUtils.errorMessage(e.getMessage());
            }
		}
		columnNameList = DesignTableDataManager.getColumnNamesByTableData(embeddedTableData);
		return columnNameList;
	}

    /**
	 * �����ӽڵ�
	 * 
	 * @return �����ӽڵ�
	 * 
	 *
	 * @date 2014-11-24-����3:51:17
	 * 
	 */
	public ExpandMutableTreeNode[] load() {
		List<String> namelist = calculateColumnNameList();
		ExpandMutableTreeNode[] res = new ExpandMutableTreeNode[namelist.size()];
		for (int i = 0; i < res.length; i++) {
			res[i] = new ExpandMutableTreeNode(namelist.get(i));
		}

		return res;
	}

	/**
	 * Ԥ�����ݼ�
	 * 
	 *
	 * @date 2014-11-24-����3:50:20
	 * 
	 */
	public void previewData() {
		PreviewTablePane.previewTableData(tabledata);
	}

    /**
	 * Ԥ�����ݼ�,������ʾֵ��ʵ��ֵ�ı�ǽ��
	 * 
	 * @param keyIndex ʵ��ֵ
	 * @param valueIndex ��ʾֵ
	 * 
	 *
	 * @date 2014-11-24-����3:50:20
	 * 
	 */
	public void previewData(final int keyIndex,final int valueIndex){
		PreviewTablePane.previewTableData(tabledata, keyIndex, valueIndex);
	}
	@Override
	public String getTableDataName(){
		return name; 
	}

	/**
	 * ��ȡ���ݼ������
	 * 
	 * @return ���ݼ����
	 * 
	 *
	 * @date 2014-11-24-����3:50:00
	 * 
	 */
	public AbstractTableDataPane<?> creatTableDataPane() {
		return TableDataFactory.creatTableDataPane(tabledata, name);
	}

    public boolean equals (Object obj) {
        return obj instanceof AbstractTableDataWrapper
                && ComparatorUtils.equals(this.name, ((AbstractTableDataWrapper) obj).getTableDataName())
                && ComparatorUtils.equals(this.tabledata, ((AbstractTableDataWrapper) obj).getTableData());
    }

}
