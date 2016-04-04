package com.fr.plugin.chart.designer.component;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.LineStyle;
import com.fr.plugin.chart.base.VanChartAttrLine;
import com.fr.stable.CoreConstants;

import javax.swing.*;
import java.awt.*;

/**
 * line�������
 */
public class VanChartLineTypePane extends BasicPane {

    private static final long serialVersionUID = -6581862503009962973L;
    protected LineComboBox lineWidth;//����
    private UIButtonGroup<LineStyle> lineStyle;//��̬
    protected UIButtonGroup nullValueBreak;//��ֵ�Ͽ�

    public VanChartLineTypePane() {
        lineWidth = new LineComboBox(CoreConstants.STRIKE_LINE_STYLE_ARRAY_4_CHART);
        String[] textArray = new String[]{Inter.getLocText("Plugin-ChartF_NormalLine"),
                Inter.getLocText("Plugin-ChartF_StepLine"), Inter.getLocText("Plugin-ChartF_CurveLine")};
        lineStyle = new UIButtonGroup<LineStyle>(textArray, LineStyle.values());
        nullValueBreak = new UIButtonGroup(new String[]{Inter.getLocText("Plugin-ChartF_Open"), Inter.getLocText("Plugin-ChartF_Close")});

        this.setLayout(new BorderLayout());
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        this.add(createContentPane(p, f), BorderLayout.CENTER);
    }

    protected JPanel createContentPane(double p, double f) {
        double[] row = {p,p,p};
        double[] col = {p,f};

        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_LineStyle")), lineWidth},
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Style_Present")), lineStyle},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Null_Value_Break")), nullValueBreak},
        };

        return TableLayoutHelper.createTableLayoutPane(components, row, col);
    }

    protected String title4PopupWindow(){
        return Inter.getLocText("Plugin-ChartF_Line");
    }

    public void populate(VanChartAttrLine line) {
        if(line == null){
            line = new VanChartAttrLine();
        }
        lineWidth.setSelectedLineStyle(line.getLineWidth());
        lineStyle.setSelectedItem(line.getLineStyle());
        nullValueBreak.setSelectedIndex(line.isNullValueBreak() ? 0 : 1);
    }

    public VanChartAttrLine update() {
        VanChartAttrLine line = new VanChartAttrLine();
        line.setLineWidth(lineWidth.getSelectedLineStyle());
        line.setLineStyle(lineStyle.getSelectedItem());
        line.setNullValueBreak(nullValueBreak.getSelectedIndex() == 0);
        return line;
    }
}
