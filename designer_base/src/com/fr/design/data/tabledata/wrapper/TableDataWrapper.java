package com.fr.design.data.tabledata.wrapper;

import com.fr.base.TableData;
import com.fr.design.gui.itree.refreshabletree.loader.ChildrenNodesLoader;

import javax.swing.*;
import java.util.List;

/**
 * ��TableData��װ�� ��������ģ�����ݼ������������ݼ����洢�������ݼ�
 * �����˲���TableData��һЩҪ�õķ�����tabledata�����֡�ͼ�ꡢ�������������ϡ�Ԥ�����ݼ���
 * ע�����ڴ洢����StoreProcedure�����ϲ���TableData
 * !!Notice:��֧�ֶ����ݼ����޸ġ��������ȸı�TableData�Ĳ�����һ���ȶ������ݼ�
 * ��ȷ����SQL��ѯ��䣩����Ӧ��Ӧ��TableDataWrappe�� ������ݼ��仯�ˣ���ôTableDataWrappeҲ���������ɡ�
 * ���Ա�֤>>>>>>>>calculateColumnNameList()ֻ����һ�Σ���������ȷ��<<<<<<
 * 
 * @author zhou
 * @since 2012-3-28����9:51:49
 */
public interface TableDataWrapper extends ChildrenNodesLoader {

	/**
	 * ���ݼ�����
	 * 
	 * @return
	 */
	public String getTableDataName();

    /**
     * TableData
     *
     * @return
     */
    public TableData getTableData();

    /**
	 * ���ݼ�ͼ��
	 * 
	 * @return
	 */
	public Icon getIcon();

	/**
	 * ���ݼ�ִ�н�����ص������ֶ�
	 * 
	 * TODO:Ҫ��Ҫ����Exception�أ����˸о����б�Ҫ
	 * @return
	 */
	public List<String> calculateColumnNameList();

	/**
	 * Ԥ�����ݼ�
	 */
	public void previewData();

	/**
	 * Ԥ�����ݼ���������ʾֵ��ʵ��ֵ�ı�ǽ��
	 */
	public void previewData(final int keyIndex, final int valueIndex);

	/**
	 * �Ƿ��쳣 TODO:���Ӧ�ú�calculateColumnNameList���������һ��
	 * 
	 * @return
	 */
	public boolean isUnusual();

}
