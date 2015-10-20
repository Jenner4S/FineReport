package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.base.chart.BaseChartCollection;
import com.fr.chart.chartattr.Bar2DPlot;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.ChartFactory;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.chart.web.ChartHyperPoplink;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.ChartHyperEditPane;
import com.fr.general.Inter;
import com.fr.design.utils.gui.GUICoreUtils;

import java.awt.*;

/**
 * ��˵��: ͼ���� -- ���� ������. 
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2011-12-28 ����10:41:39
 */
public class ChartHyperPoplinkPane extends BasicBeanPane<ChartHyperPoplink> {
	private static final long serialVersionUID = 2469115951510144738L;
	private UITextField itemNameTextField;
	private ChartHyperEditPane hyperEditPane;
	private ChartComponent chartComponent;
	
	public ChartHyperPoplinkPane() {
		this.setLayout(FRGUIPaneFactory.createM_BorderLayout());

        if(this.needRenamePane()){
            itemNameTextField = new UITextField();
            this.add(GUICoreUtils.createNamedPane(itemNameTextField, Inter.getLocText("FR-Chart-Use_Name") + ":"), BorderLayout.NORTH);
        }

		hyperEditPane = new ChartHyperEditPane(getChartParaType());
		this.add(hyperEditPane, BorderLayout.CENTER);
		
		ChartCollection cc = new ChartCollection();
		cc.addChart(new Chart(new Bar2DPlot()));
		
		chartComponent = new ChartComponent();
		chartComponent.setPreferredSize(new Dimension(220, 170));// �ڵ�Ԫ�񵯳�ʱ ��Ҫ������֤���Ա�Ĵ�С.
		chartComponent.setSupportEdit(false);
		chartComponent.populate(cc);
		
		this.add(chartComponent, BorderLayout.EAST);
		
		hyperEditPane.populate(cc);
		
		hyperEditPane.useChartComponent(chartComponent);
	}
	
	protected int getChartParaType() {
		return ParameterTableModel.CHART_NORMAL_USE;
	}

    /**
     * �Ƿ���Ҫ�����������Ŀռ�
     * @return Ĭ����Ҫ����
     */
    protected boolean needRenamePane(){
        return true;
    }
	
	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("FR-Chart-Pop_Chart");
	}

	@Override
	public void populateBean(ChartHyperPoplink chartHyperlink) {
        if(itemNameTextField != null){
            this.itemNameTextField.setText(chartHyperlink.getItemName());
        }

		BaseChartCollection cc = chartHyperlink.getChartCollection();
		if (cc == null || cc.getChartCount() < 1) {
			cc = new ChartCollection();
			cc.addChart(new Chart(ChartFactory.createBar2DPlot()));
			chartHyperlink.setChartCollection(cc);
		}
		
		hyperEditPane.populateHyperLink(chartHyperlink);
		chartComponent.populate(cc);
	}

	/**
	 * ��������HyperlinkGoup�л�ʱ updateBean.
	 * @return ���صĵ�������.
	 */
	public ChartHyperPoplink updateBean() {
		ChartHyperPoplink chartLink = new ChartHyperPoplink();
		updateBean(chartLink);
        if(itemNameTextField != null){
            chartLink.setItemName(this.itemNameTextField.getText());
        }
		return chartLink;
	}
	
	/**
	 * ���Ա� ��Ӧupdate
	 */
	public void updateBean(ChartHyperPoplink chartHyperlink) {
		hyperEditPane.updateHyperLink(chartHyperlink);
		chartHyperlink.setChartCollection(chartComponent.update());
		
		ChartEditPane.getInstance().fire();// ��Ӧ����ͼ�����¼���.
        if(itemNameTextField != null){
            chartHyperlink.setItemName(this.itemNameTextField.getText());
        }
	}

    public static class CHART_NO_RENAME extends ChartHyperPoplinkPane{
        protected boolean needRenamePane(){
            return false;
        }
    }

	public static class CHART_MAP extends ChartHyperPoplinkPane {
		
		protected int getChartParaType() {
			return ParameterTableModel.CHART_MAP_USE;
		}
	}
	
	public static class CHART_GIS extends ChartHyperPoplinkPane {
		
		protected int getChartParaType() {
			return ParameterTableModel.CHART_GIS_USE;
		}
	}
	
	public static class CHART_PIE extends ChartHyperPoplinkPane {
    	@Override
    	protected int getChartParaType() {
    		return ParameterTableModel.CHART_PIE_USE;
    	}
    }

    public static class CHART_XY extends ChartHyperPoplinkPane {
        protected int getChartParaType() {
            return ParameterTableModel.CHART__XY_USE;
        }
    }

    public static class CHART_BUBBLE extends ChartHyperPoplinkPane {
        protected int getChartParaType() {
            return ParameterTableModel.CHART_BUBBLE_USE;
        }
    }

    public static class CHART_STOCK extends  ChartHyperPoplinkPane {
        protected int getChartParaType() {
            return ParameterTableModel.CHART_STOCK_USE;
        }
    }

    public static class CHART_GANTT extends  ChartHyperPoplinkPane {
        protected int getChartParaType() {
            return ParameterTableModel.CHART_GANTT_USE;
        }
    }

    public static class CHART_METER extends  ChartHyperPoplinkPane {
        protected int getChartParaType() {
            return ParameterTableModel.CHART_METER_USE;
        }
    }
}
