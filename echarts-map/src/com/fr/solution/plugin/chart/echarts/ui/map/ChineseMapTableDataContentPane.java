package com.fr.solution.plugin.chart.echarts.ui.map;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;

import javax.swing.*;

import java.awt.*;
import java.util.List;

/**
 * Created by richie on 16/1/29.
 */
public class ChineseMapTableDataContentPane extends AbstractTableDataContentPane implements UIObserver {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1080400922583336704L;
	private UICorrelationPane correlationPane;

    public ChineseMapTableDataContentPane(ChartDataPane parent) {
        setLayout(new BorderLayout());
        UILabel label = new UILabel("测试布局");
        setBorder(BorderFactory.createLineBorder(Color.RED));
        add(label, BorderLayout.NORTH);
        correlationPane = new UICorrelationPane("区域名", "区域值");
        add(correlationPane, BorderLayout.CENTER);

    }

    @Override
    public void updateBean(ChartCollection ob) {

    }

    @Override
    public void clearAllBoxList() {

    }

    @SuppressWarnings("rawtypes")
	@Override
    protected void refreshBoxListWithSelectTableData(List columnNameList) {

    }

    @Override
    public void registerChangeListener(UIObserverListener listener) {

    }

    @Override
    public boolean shouldResponseChangeListener() {
        return true;
    }
}
