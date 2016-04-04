package com.fr.plugin.chart.designer.component;

import com.fr.chart.base.AttrColor;
import com.fr.chart.base.AttrLineStyle;
import com.fr.chart.base.LineStyleInfo;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.VanChartAttrTrendLine;
import com.fr.plugin.chart.base.VanChartConstants;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Mitisky on 15/10/19.
 */
public class VanChartTrendLinePane extends BasicPane{

    private UITextField trendLineName;
    private ColorSelectBox trendLineColor;
    private LineComboBox trendLineType;

    public VanChartTrendLinePane() {
        this.setLayout(new BorderLayout());
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p,p,p};
        double[] col = {p,f};
        trendLineName = new UITextField();
        trendLineColor = new ColorSelectBox(100);
        trendLineType = new LineComboBox(VanChartConstants.ALERT_LINE_STYLE);

        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Name")), trendLineName},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_LineStyle")), trendLineType},
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Color_Color")), trendLineColor},
        };

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, row, col);
        this.add(panel, BorderLayout.CENTER);
    }
    protected String title4PopupWindow(){
        return Inter.getLocText("Chart-Trend_Line");
    }

    public void populate(VanChartAttrTrendLine trendLine) {
        if(trendLine != null){
            trendLineName.setText(trendLine.getTrendLineName());
            LineStyleInfo lineStyleInfo = trendLine.getLineStyleInfo();
            trendLineColor.setSelectObject(lineStyleInfo.getAttrLineColor().getSeriesColor());
            trendLineType.setSelectedLineStyle(lineStyleInfo.getAttrLineStyle().getLineStyle());
        }
    }

    public VanChartAttrTrendLine update() {
        VanChartAttrTrendLine  trendLine = new VanChartAttrTrendLine();
        trendLine.setTrendLineName(trendLineName.getText());

        LineStyleInfo lineStyleInfo = trendLine.getLineStyleInfo();
        lineStyleInfo.setAttrLineStyle(new AttrLineStyle(trendLineType.getSelectedLineStyle()));
        lineStyleInfo.setAttrLineColor(new AttrColor(trendLineColor.getSelectObject()));
        return trendLine;
    }

}
