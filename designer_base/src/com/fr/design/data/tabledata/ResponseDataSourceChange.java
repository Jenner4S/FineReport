package com.fr.design.data.tabledata;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: С�һ�
 * Date: 13-8-5
 * Time: ����11:24
 * To change this template use File | Settings | File Templates.
 */
public interface ResponseDataSourceChange {

    /**
     * ��Ӧ���ݼ��ı�
     */
    public void fireDSChanged();

    /**
     * ��Ӧ���ݼ��ı�
     */
    public void fireDSChanged(Map<String, String> map);


}
