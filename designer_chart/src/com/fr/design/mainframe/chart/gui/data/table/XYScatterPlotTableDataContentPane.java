package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.XYScatterPlot;
import com.fr.chart.chartdata.ScatterTableDefinition;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ChartDataFilterPane;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * ���Ա�  ���ݽ��� ɢ��ͼ  ���ݼ����ݽ���.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version ����ʱ�䣺2012-12-27 ����04:12:59
 */
public class XYScatterPlotTableDataContentPane extends AbstractTableDataContentPane{
	
	private UIComboBox seriesName;
	private UIComboBox xCombox;
	private UIComboBox yCombox;
	
	private ChartDataFilterPane dataScreeningPane;
	
	public XYScatterPlotTableDataContentPane(ChartDataPane parent) {
		seriesName = new UIComboBox();
		xCombox = new UIComboBox();
		yCombox = new UIComboBox();
		dataScreeningPane = new ChartDataFilterPane(new XYScatterPlot(), parent);

        seriesName.addItem(Inter.getLocText("Chart-Use_None"));
		
		seriesName.setPreferredSize(new Dimension(100, 20));
		xCombox.setPreferredSize(new Dimension(100, 20));
		yCombox.setPreferredSize(new Dimension(100, 20));

		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { p,f};
		double[] rowSize = { p,p,p,p,p,p};

        Component[][] components = new Component[][]{
                new Component[]{new BoldFontTextLabel(" " + Inter.getLocText("Chart-Series_Name") + ":"),seriesName},
                new Component[]{new BoldFontTextLabel("  " + Inter.getLocText("Chart-Scatter_Name") + "x" + ":"), xCombox},
                new Component[]{new BoldFontTextLabel("  " + Inter.getLocText("Chart-Scatter_Name") + "y" + ":"), yCombox},
                new Component[]{new JSeparator(),null},
                new Component[]{new BoldFontTextLabel(Inter.getLocText("Chart-Data_Filter"))},
                new Component[]{dataScreeningPane,null}
        };

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);
        
        seriesName.addItemListener(tooltipListener);
        xCombox.addItemListener(tooltipListener);
        yCombox.addItemListener(tooltipListener);
	}
	
	/**
	 * ���� box�Ƿ����.
     * @param hasUse  �Ƿ�ʹ��.
	 */
	public void checkBoxUse(boolean hasUse) {
		seriesName.setEnabled(hasUse);
		xCombox.setEnabled(hasUse);
		yCombox.setEnabled(hasUse);
	}
	
	protected void refreshBoxListWithSelectTableData(List columnNameList) {
		refreshBoxItems(seriesName, columnNameList);
        seriesName.addItem(Inter.getLocText("Chart-Use_None"));
		refreshBoxItems(xCombox, columnNameList);
		refreshBoxItems(yCombox, columnNameList);
	}

    /**
     * ������е�box����
     */
    public void clearAllBoxList(){
        clearBoxItems(seriesName);
        seriesName.addItem(Inter.getLocText("Chart-Use_None"));
        clearBoxItems(xCombox);
        clearBoxItems(yCombox);
    }

	/**
	 * ����ɢ��ͼ �����ݼ����ݽ���.
	 */
	public void updateBean(ChartCollection collection) {
		
		ScatterTableDefinition definition = new ScatterTableDefinition();
		collection.getSelectedChart().setFilterDefinition(definition);
		
		Object resultName = seriesName.getSelectedItem();
		Object resultX = xCombox.getSelectedItem();
		Object resultY = yCombox.getSelectedItem();

        if(resultName == null || ArrayUtils.contains(ChartConstants.NONE_KEYS, resultName)) {
            definition.setSeriesName(StringUtils.EMPTY);
        } else {
			definition.setSeriesName(resultName.toString());
        }

		if(resultX != null) {
			definition.setScatterX(resultX.toString());
		}
		
		if(resultY != null) {
			definition.setScatterY(resultY.toString());
		}
		
		dataScreeningPane.updateBean(collection);
	}

	/**
	 * ����ɢ��ͼ�����ݼ�����.
	 */
	public void populateBean(ChartCollection collection) {
		TopDefinitionProvider top = collection.getSelectedChart().getFilterDefinition();
		if(top instanceof ScatterTableDefinition) {
			ScatterTableDefinition definition = (ScatterTableDefinition)top;

            if(definition.getSeriesName() == null || ComparatorUtils.equals(StringUtils.EMPTY, definition.getSeriesName())) {
                seriesName.setSelectedItem(Inter.getLocText("Chart-Use_None"));
            } else {
			    combineCustomEditValue(seriesName, definition.getSeriesName());
            }

			combineCustomEditValue(xCombox, definition.getScatterX());
			combineCustomEditValue(yCombox, definition.getScatterY());
		}
		dataScreeningPane.populateBean(collection);
	}

	/**
	 * ���²���
	 */
	public void redoLayoutPane(){
		dataScreeningPane.relayoutPane(this.isNeedSummaryCaculateMethod());
	}
}
