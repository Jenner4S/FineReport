package com.fr.design.actions.tabledata;

import com.fr.base.BaseUtils;
import com.fr.design.data.datapane.TableDataNameObjectCreator;
import com.fr.design.actions.UpdateAction;

import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 13-12-19
 * Time: ����5:21
 */
public abstract class TableDataAction extends UpdateAction {
    public static final String XML_TAG_TEMPLATE = "template_tabledata_Type";
    public static final String XML_TAG_SERVER = "server_tabledata_Type";

    public TableDataAction() {
        this.setName(this.getDisplayName());
        this.setSmallIcon(BaseUtils.readIcon(this.getIconPath()));
    }


    /**
     * ����
     *
     * @return
     */
    public abstract String getDisplayName();

    /**
     * ͼ��
     *
     * @return
     */
    public abstract String getIconPath();

    /**
     * ��׺
     *
     * @return
     */
    public abstract String getPrefix();


    /**
     * �õ�TableData��������
     *
     * @return
     */
    public abstract Class getTableDataClass();

    /**
     * �õ���Ӧ������
     *
     * @return
     */
    public abstract Class getUpdateTableDataPaneClass();

    /**
     * ���ڳ�ʼ�����ࡣŵ�ǲ���Ҫ��ʼ��������Ҫʵ��
     *
     * @return
     */
    public Class getClass4Init() {
        return null;
    }

    /**
     * �ò˵������Ƿ���Ҫ����ָ��
     *
     * @return ���򷵻�true
     */
    public boolean isNeedInsertSeparator() {
        return false;
    }

    public TableDataNameObjectCreator getTableDataCreator() {
        if (this.getClass4Init() == null) {
            return new TableDataNameObjectCreator(this);
        } else {
            return new TableDataNameObjectCreator(this, getClass4Init());
        }
    }


    /**
     * ����
     * @param e ����
     */
    public void actionPerformed(ActionEvent e) {
    }


}
