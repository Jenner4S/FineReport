/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.fun;

import com.fr.design.data.datapane.TableDataNameObjectCreator;

import java.util.Map;

/**
 * @author : richie
 * @since : 8.0
 * @deprecated
 * @see com.fr.design.fun.TableDataDefineProvider
 * @see com.fr.design.fun.ServerTableDataDefineProvider
 */
public interface TableDataCreatorProvider {
    public static final String XML_TAG = "TableDataCreatorProvider";

    /**
     * ��ȡ�������ݼ���ƽ�����������
     * @param creators ���õ����ݼ���ƽ�����������
     * @return �����ı������ݼ���ƽ�����������
     */
    public TableDataNameObjectCreator[] produceReportTableDataCreator(TableDataNameObjectCreator[] creators);

    /**
     * ��ȡ���������ݼ���ƽ�����������
     * @param creators ���õ����ݼ���ƽ�����������
     * @return �����ķ��������ݼ���ƽ�����������
     */
    public TableDataNameObjectCreator[] produceServerTableDataCreator(TableDataNameObjectCreator[] creators);

    /**
     * ��Ҫ������ʹ�õ����ݼ��Լ�����ƽ�����һ����¼
     * @return �����¼��Ӧ
     */
    public Map<String, TableDataNameObjectCreator> registerMap();
}
