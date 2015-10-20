package com.fr.design.chart.series.SeriesCondition;

import com.fr.design.chart.ChartPlotFactory;
import com.fr.chart.base.AttrContents;
import com.fr.design.chart.series.SeriesCondition.dlp.DataLabelPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;

import javax.swing.*;
import java.awt.*;

public class DataLabelContentsPane extends BasicPane {
    private static final long serialVersionUID = -7935543122227508109L;
    private DataLabelPane dataLabelPane;

    public DataLabelContentsPane(Class plotClass) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        JPanel pane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
        this.add(pane, BorderLayout.NORTH);

        //  ����, ϵ��, ֵ, �ٷֱ�
        pane.add(createLabelPane(plotClass), BorderLayout.NORTH);
    }

    @Override
    protected String title4PopupWindow() {
        return "Label";
    }

    /**
     * һ��: ���� ϵ�� ֵ
     * ��ͼ: ���� ϵ�� ֵ �ٷֱ� ǣ����.
     */
    private JPanel createLabelPane(Class plotClass) {
        JPanel pane = FRGUIPaneFactory.createBorderLayout_L_Pane();// border ���ʺ�
        pane.setBorder(BorderFactory.createEmptyBorder(2, 0, 10, 10));
        pane.add(dataLabelPane = ChartPlotFactory.createDataLabelPane4Plot(plotClass), BorderLayout.CENTER);
        return pane;
    }

    public void checkGuidBox() {
        dataLabelPane.checkGuidBox();
    }

    public void populate(AttrContents seriesAttrContents) {
        dataLabelPane.populate(seriesAttrContents);
    }

    public void update(AttrContents seriesAttrContents) {
         dataLabelPane.update(seriesAttrContents);
    }
}
