package com.fr.design.chart.fun;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;

/**
 * Created by eason on 14/12/29.
 * @since 8.0
 * �Զ���ͼ����������ƽ���ӿ�
 */
public interface IndependentChartUIProvider {

    public static final String XML_TAG = "IndependentChartUIProvider";
    /**
     *ͼ������Ͷ���������ͣ��������Ա�ĵ�һ������
     * @return ͼ������Ͷ����������
     */
    public AbstractChartTypePane getPlotTypePane();

    /**
     * ���ݼ�����Դ�Ľ���
     * @return ���ݼ�����Դ�Ľ���
     */
    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent);


    /**
     * ��Ԫ������Դ�Ľ���
     * @return ��Ԫ������Դ�Ľ���
     */
    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent);

    /**
     * �������Խ���
     * @return �������Խ���
     */
    public ConditionAttributesPane getPlotConditionPane(Plot plot);

    /**
     * ϵ�н���
     * @return ϵ�н���
     */
    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot);


    /**
     * ͼ������Խ�������
     * @return ���Խ���
     */
    public AbstractChartAttrPane[] getAttrPaneArray(AttributeChangeListener listener);

    /**
     * �Ƿ�ʹ��Ĭ�ϵĽ��棬Ϊ�˱�����������л�
     * @return �Ƿ�ʹ��Ĭ�ϵĽ���
     */
    public boolean isUseDefaultPane();

    /**
     *ͼ��·��
     * @return ͼ��·��
     */
    public String getIconPath();
}
