package com.fr.design.mainframe.chart.gui.style.area;

import java.util.ArrayList;
import java.util.List;

import com.fr.chart.chartattr.*;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.mainframe.BaseJForm;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ThirdTabPane;
import com.fr.design.mainframe.chart.gui.style.legend.AutoSelectedPane;
import com.fr.general.ComparatorUtils;

/**
 * ���Ա�, ͼ����ʽ-�������.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version ����ʱ�䣺2013-1-9 ����12:25:00
 */
public class ChartAreaPane extends ThirdTabPane<Chart> implements AutoSelectedPane {
	private static final long serialVersionUID = -3532884544720748530L;
	private static final int PRE_WIDTH = 220;
	private ChartWholeAreaPane areaPane;
	private ChartPlotAreaPane plotPane;
	
	public ChartAreaPane(Plot plot, ChartStylePane parent) {
		super(plot, parent);
	}

    /**
     * ���� ʹ�ñ���
     * @return     ����
     */
	public String title4PopupWindow() {
		return PaneTitleConstants.CHART_STYLE_AREA_TITLE;
	}

	@Override
	protected List<NamePane> initPaneList(Plot plot, AbstractAttrNoScrollPane parent) {
		List<NamePane> paneList = new ArrayList<NamePane>();
		areaPane = new ChartWholeAreaPane();
		plotPane = new ChartPlotAreaPane();
		if(parent instanceof ChartStylePane) {
			plotPane.setParentPane((ChartStylePane)parent);
		}
        JTemplate jTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if (jTemplate.isJWorkBook() || jTemplate.getEditingReportIndex() == BaseJForm.ELEMENTCASE_TAB) {
            //���е�ͼ�������ͼ����Ų���ؼ����Ա����ʽ����
            paneList.add(new NamePane(areaPane.title4PopupWindow(), areaPane));
        }else if(jTemplate.isChartBook()){
            paneList.add(new NamePane(areaPane.title4PopupWindow(), areaPane));
        }

		if(plot.isSupportPlotBackground()) {
			paneList.add(new NamePane(plotPane.title4PopupWindow(), plotPane));
		}
		return paneList;
	}
	
	@Override
	protected int getContentPaneWidth() {
		return PRE_WIDTH;
	}

	/**
	 * ���½���
	 */
	public void populateBean(Chart chart) {
		areaPane.populateBean(chart);
		plotPane.populateBean(chart);
	}

	/**
	 * �����������.
	 */
	@Override
	public void updateBean(Chart chart) {
		areaPane.updateBean(chart);
		plotPane.updateBean(chart);
	}

	/**
	 * ����ѡ�еĽ���id
	 */
	public void setSelectedIndex(String id) {
		for (int i = 0; i < paneList.size(); i++) {
            if (ComparatorUtils.equals(id, nameArray[i])) {
            	tabPane.setSelectedIndex(i);
                break;
            }
        }
	}
}

