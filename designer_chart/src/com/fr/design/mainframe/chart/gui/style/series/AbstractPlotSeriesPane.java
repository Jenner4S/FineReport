package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.chartattr.Bar2DPlot;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ChartFillStylePane;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
/**
 * ���Ա�, ͼ����ʽ - ϵ�� ����, ͨ�����ؼ̳�ÿ����ͬ�ķ���, �õ����ݵ����.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version ����ʱ�䣺2013-1-5 ����11:07:51
 */
public abstract class AbstractPlotSeriesPane extends BasicBeanPane<Plot>{
	protected ChartFillStylePane fillStylePane;
	protected Plot plot;
	protected ChartStylePane parentPane;
	protected Chart chart;//��ͼ����,��Ҫ���ݶ���

    public AbstractPlotSeriesPane(ChartStylePane parent, Plot plot) {
        this(parent, plot, false);
    }
	
	public AbstractPlotSeriesPane(ChartStylePane parent, Plot plot, boolean custom) {
        this.plot = plot;
		this.parentPane = parent;
		fillStylePane = getFillStylePane();

		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { f };
		double[] rowSize = { p,p,p};
        Component[][] components = new Component[3][1];

        if(custom) {
        	if(!(plot instanceof Bar2DPlot)) {
        		components[0] = new Component[]{getContentInPlotType()};
        		components[1] = new Component[]{new JSeparator()};
        	}

        	JPanel panel = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);

        	JScrollPane scrollPane = new JScrollPane();
        	scrollPane.setViewportView(panel);
        	scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        	this.setLayout(new BorderLayout());
        	this.add(scrollPane, BorderLayout.CENTER);

        } else {
        	if(fillStylePane != null) {
        		components[0] = new Component[]{fillStylePane};
        		components[1] = new Component[]{new JSeparator()};
        	}

        	JPanel contentPane = getContentInPlotType();
        	if(contentPane != null) {
        		components[2] = new Component[]{contentPane};
        	}

        	JPanel panel = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
        	this.setLayout(new BorderLayout());
        	this.add(panel,BorderLayout.CENTER);
        }
	}
	
	/**
	 * ��ÿ����ͬ����Plot, �õ���ͬ���͵�����. ����: ���εķ��, ���ߵ���������.
	 */
	protected abstract JPanel getContentInPlotType();
	
	/**
	 * ���� ������.
	 */
	protected ChartFillStylePane getFillStylePane() {
		return new ChartFillStylePane();
	}
	
	/**
	 * �������.
	 */
	protected String title4PopupWindow() {
		return Inter.getLocText("FR-Chart-Data_Series");
	}

	/**
	 * ���� donothing
	 */
	public Plot updateBean() {
		return null;
	}
	
	
	/**
	 * ����Plot�����Ե�ϵ�н���
	 */
	public void populateBean(Plot plot) {
		if(plot == null) {
			return;
		}
		if(fillStylePane != null) {
			fillStylePane.populateBean(plot.getPlotFillStyle());
		}
	}

	/**
	 * ���� ϵ�н�������Ե�Plot
	 */
	public void updateBean(Plot plot) {
		if(plot == null) {
			return;
		}
		if(fillStylePane != null) {
			plot.setPlotFillStyle(fillStylePane.updateBean());
		}
	}

	/**
	 * ���õ�ǰ��chart��ֻ�е�ͼ����
	 * @param chart ��ǰ��chart
	 */
	public void setCurrentChart(Chart chart){
		this.chart = chart;
	}
}
