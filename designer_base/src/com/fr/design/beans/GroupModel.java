package com.fr.design.beans;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * richer:���Ժ��¼�����
 * @since 6.5.3
 */
public interface GroupModel {
    /**
     * �����������ķ���,��ͨ���Է�Ϊ�������Ժ�����,�¼����Ը����¼����Ʋ�ͬ���з���
     */
    String getGroupName();

    /**
     * �ܹ�����������
     */
    int getRowCount();
    
    /**
     * ��ȡ���Ա��е�row�е���Ⱦ��
     * @param row
     */
    TableCellRenderer getRenderer(int row);

    /**
     * ��ȡ���Ա��е�row�еı༭��
     * @param row
     */
    TableCellEditor getEditor(int row);

    /**
     * ��ȡ���Ա��е�row�е�column�е�ֵ
     * @param row
     * @param column
     * @return ����е�ֵ
     */
    Object getValue(int row, int column);

    /**
     * �������Ա��е�row�е�column�е�ֵΪvalue
     * @param value
     * @param row
     * @param column
     * @return ���óɹ��򷵻�true�����򷵻�false
     */
    boolean setValue(Object value, int row, int column);

    /**
     * �����Ƿ�ɱ༭
     * @param row
     * @return ��row�пɱ༭����true�����򷵻�false
     */
    boolean isEditable(int row);
}
