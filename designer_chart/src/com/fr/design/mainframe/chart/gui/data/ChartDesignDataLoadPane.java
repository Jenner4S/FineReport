package com.fr.design.mainframe.chart.gui.data;

import com.fr.base.TableData;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.data.tabledata.wrapper.TemplateTableDataWrapper;
import com.fr.design.dialog.BasicPane;
import com.fr.design.mainframe.AbstractChartDataPane4Chart;

/**
 * ͼ����������� ����
 * Created by kunsnat on 14-10-21.
 * kunsnat@gmail.com
 */
public abstract class ChartDesignDataLoadPane extends BasicPane {

    private AbstractChartDataPane4Chart parentPane;

    public ChartDesignDataLoadPane(AbstractChartDataPane4Chart parentPane){
        this.parentPane = parentPane;
    }

    /**
     * �������ݼ�
     *
     * @param tableData ���ݼ�
     */
    public abstract void populateChartTableData(TableData tableData);

    /**
     * ���ݽ��� ��ȡ���ݼ����.
     *
     * @return �������ݼ�
     */
    public abstract TableData getTableData();


    protected abstract String getNamePrefix();

    //��Ӧ�����¼�
    protected void fireChange() {
        parentPane.fireTableDataChange();
    }


    public TableDataWrapper getTableDataWrapper(){
        return new TemplateTableDataWrapper(getTableData());
    }
}
