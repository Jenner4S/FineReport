/*
 * Copyright(c) 2001-2011, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe;

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.JComponent;

import com.fr.base.BaseUtils;
import com.fr.base.chart.BaseChartCollection;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.designer.TargetComponent;
import com.fr.design.gui.chart.BaseChartPropertyPane;
import com.fr.design.gui.frpane.UITitlePanel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itabpane.TitleChangeListener;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.general.Inter;

public abstract class MiddleChartPropertyPane extends BaseChartPropertyPane{

	protected TargetComponentContainer container = new TargetComponentContainer();
	protected UILabel nameLabel;

	protected ChartEditPane chartEditPane;

	public MiddleChartPropertyPane() {
		initComponenet();
	}
	
	protected void initComponenet() {
		this.setLayout(new BorderLayout());
		this.setBorder(null);
		
		createNameLabel();
		this.add(createNorthComponent(), BorderLayout.NORTH);
		
		chartEditPane = ChartEditPane.getInstance();
		chartEditPane.setSupportCellData(true);
		this.createMainPane();
	}
	
	protected abstract void createNameLabel();
	
	protected abstract JComponent createNorthComponent();
	
	protected abstract void createMainPane();

	public void setSureProperty() {
		chartEditPane.setContainer(container);
		chartEditPane.addTitleChangeListener(titleListener);
		String tabname = chartEditPane.getSelectedTabName();
		nameLabel.setText(Inter.getLocText("Chart-Property_Table") + (tabname != null ? ('-' + chartEditPane.getSelectedTabName()) : ""));
		resetChartEditPane();
	}
	
	protected void resetChartEditPane() {
		remove(chartEditPane);
		add(chartEditPane, BorderLayout.CENTER);
		validate();
		repaint();
		revalidate();
	}
	
	protected TitleChangeListener titleListener = new TitleChangeListener() {
		
		@Override
		public void fireTitleChange(String addName) {
			nameLabel.setText(Inter.getLocText("Chart-Property_Table") + '-' + addName);
		}
	};

    /**
     * �о�ChartCollection����ͼ�����Խ���.
     * @param collection  �ռ�ͼ��
     * @param ePane  ���
     */
	public void populateChartPropertyPane(ChartCollection collection, TargetComponent<?> ePane) {
		this.container.setEPane(ePane);
		chartEditPane.populate(collection);
	}

    /**
     * �о�ChartCollection����ͼ�����Խ���.
     * @param collection  �ռ�ͼ��
     * @param ePane  ���
     */
	public void populateChartPropertyPane(BaseChartCollection collection, TargetComponent<?> ePane) {
		if (collection instanceof ChartCollection) {
			populateChartPropertyPane((ChartCollection)collection, ePane);
		}
	}

	/**
	 * ����View�ı���.
	 */
	public String getViewTitle() {
		return Inter.getLocText("CellElement-Property_Table");
	}

	/**
	 * ����View��Icon��ַ.
	 */
	public Icon getViewIcon() {
		return BaseUtils.readIcon("/com/fr/design/images/m_report/qb.png");
	}

    /**
     *  Ԥ���嶨λ
     * @return    ��λ
     */
	public Location preferredLocation() {
		return Location.WEST_BELOW;
	}

    /**
     * ��������Panel
     * @return  ����panel
     */
	public UITitlePanel createTitlePanel() {
		return new UITitlePanel(this);
	}

	/**
	 * ˢ��Dockview
	 */
	public void refreshDockingView() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * �����Ƿ�֧�ֵ�Ԫ������.
	 */
	public void setSupportCellData(boolean supportCellData) {
		if(chartEditPane != null) {
			chartEditPane.setSupportCellData(supportCellData);
		}
	}
}

