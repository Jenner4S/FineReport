package com.fr.plugin.chart.designer.component.background;

import com.fr.design.mainframe.backgroundpane.ColorBackgroundPane;
import com.fr.design.mainframe.backgroundpane.NullBackgroundPane;

/**
 * ͼ��  ���Ա�.�������� ����.(���� ��, ��ɫ, ����)+������Ӱ
 */

public class VanChartBackgroundWithOutImagePane extends VanChartBackgroundPane {
    private static final long serialVersionUID = 1322979785605013853L;

    public VanChartBackgroundWithOutImagePane() {
        super();
    }

    protected void initList() {
        paneList.add(new NullBackgroundPane());
        paneList.add(new ColorBackgroundPane());
        paneList.add(new VanChartGradientPane());
    }
}
