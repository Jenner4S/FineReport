package com.fr.design.fun;

import com.fr.base.TableData;
import com.fr.design.data.tabledata.tabledatapane.AbstractTableDataPane;

/**
 * @author : richie
 * @since : 7.1.1
 * �Զ��屨�����ݼ�����ӿڣ��������ڵ�һ�������ݼ�
 */
public interface TableDataDefineProvider {

    public static final String XML_TAG = "TableDataDefineProvider";

    /**
     * �Զ�������ݼ����ý�������Ӧ�����ݼ�����
     * @return ���ݼ�������
     */
    public Class<? extends TableData> classForTableData();

    /**
     * �Զ������ݼ����ý�������Ӧ�ĳ�ʼ�����ݼ����ͣ���һ�����ݼ��ж��ʵ�ֵ�ʱ����Ч
     * @return ���ݼ�����
     */
    public Class<? extends TableData> classForInitTableData();

    /**
     * �Զ�������ݼ����ý�������Ӧ�Ľ�������
     * @return ���ݼ���������
     */
    public Class<? extends AbstractTableDataPane> appearanceForTableData();

    /**
     * �Զ������ݼ����ý����ڲ˵��ϵ���ʵ����
     * @return ����
     */
    public String nameForTableData();

    /**
     * �Զ������ݼ����½���ʱ������ǰ׺
     * @return ����ǰ׺
     */
    public String prefixForTableData();

    /**
     * �Զ������ݼ��ڲ˵�����ʵ��ͼ��
     * @return ͼ��
     */
    public String iconPathForTableData();
}
