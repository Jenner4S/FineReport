package com.fr.design.mainframe.chart.gui.data;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 一般数据界面
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-1-18 下午03:45:25
 */
public class NormalChartDataPane extends DataContentsPane {

	private UIComboBoxPane<ChartCollection> dataPane;
	private TableDataPane tableDataPane;
	private ReportDataPane reportDataPane;
	private AttributeChangeListener listener;
	
	private ChartDataPane parent;
	
	public NormalChartDataPane(AttributeChangeListener listener, ChartDataPane parent) {
		this.listener = listener;
		this.parent = parent;
		initAll();
	}
	
	public NormalChartDataPane(AttributeChangeListener listener, ChartDataPane parent, boolean supportCellData) {
		this.listener = listener;
		this.parent = parent;
		initAll();
		
		dataPane.justSupportOneSelect(true);
	}
	
	@Override
	protected JPanel createContentPane() {
		return new BasicScrollPane<ChartCollection>() {

			@Override
			protected JPanel createContentPane() {
				JPanel contentPane = new JPanel(new BorderLayout());
				dataPane = new UIComboBoxPane<ChartCollection>() {
					protected void initLayout() {
						this.setLayout(new BorderLayout(LayoutConstants.HGAP_LARGE,6));
						JPanel northPane = new JPanel(new BorderLayout(LayoutConstants.HGAP_LARGE,0));
						northPane.add(jcb, BorderLayout.CENTER);
                        UILabel label1 = new UILabel(Inter.getLocText("Chart-Data_Resource") + ":", SwingConstants.RIGHT);
                        label1.setPreferredSize(new Dimension(75,20));
                        northPane.add(GUICoreUtils.createBorderLayoutPane(new Component[]{jcb, null, null, label1, null}));
						this.add(northPane, BorderLayout.NORTH);
						this.add(cardPane, BorderLayout.CENTER);

					}
					@Override
					protected String title4PopupWindow() {
						return null;
					}

					@Override
					protected List<FurtherBasicBeanPane<? extends ChartCollection>> initPaneList() {
						tableDataPane = new TableDataPane(parent);
						reportDataPane = new ReportDataPane(parent);
						List<FurtherBasicBeanPane<? extends ChartCollection>> paneList = new ArrayList<FurtherBasicBeanPane<? extends ChartCollection>>();
						paneList.add(tableDataPane);
						paneList.add(reportDataPane);
						return paneList;
					}
				};
				contentPane.add(dataPane, BorderLayout.CENTER);
				dataPane.setBorder(BorderFactory.createEmptyBorder(10 ,0, 0, 0));
				return contentPane;
			}

			@Override
			protected String title4PopupWindow() {
				return "";
			}

			@Override
			public void populateBean(ChartCollection ob) {
				
			}
		};

	}

	/**
	 * 更新界面 数据内容
	 */
	public void populate(ChartCollection collection) {
		reportDataPane.refreshContentPane(collection);
		tableDataPane.refreshContentPane(collection);
		
		dataPane.populateBean(collection);
		this.initAllListeners();
		this.addAttributeChangeListener(listener);
		
		reportDataPane.checkBoxUse();
		tableDataPane.checkBoxUse();
	}

	/**
	 * 保存 数据界面内容
	 */
	public void update(ChartCollection collection) {
		if(dataPane.getSelectedIndex() == 0) {
			tableDataPane.updateBean(collection);
		} else {
			reportDataPane.updateBean(collection);
		}
	}

	/**
	 * 是否支持单元格数据
	 */
	public void setSupportCellData(boolean supportCellData) {
		dataPane.justSupportOneSelect(supportCellData);
	}
}