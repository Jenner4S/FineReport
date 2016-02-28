package com.fr.plugin.chart.designer.style.axis.radar;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.style.axis.VanChartBaseAxisPane;

import javax.swing.*;
import java.awt.*;

/**
 * �״�ͼ��x�ᣬ��������.
 */
public class VanChartRadarXAxisPane extends VanChartBaseAxisPane {

    private static final long serialVersionUID = -668494063365759428L;

    protected JPanel createContentPane(boolean isXAxis){

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p,p,p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{new JSeparator(),null},
                new Component[]{createLabelPane(new double[]{p, p, p}, columnSize),null},
                new Component[]{new JSeparator(),null},
                new Component[]{createLineStylePane(new double[]{p, p, p, p}, columnSize),null},
                new Component[]{new JSeparator(),null},
                new Component[]{createValueStylePane(),null},
        };

        return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
    }

    protected Component[][] getLineStylePaneComponents() {
        return new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_type")),axisLineStyle} ,
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Color_Color")),axisLineColor},
        };
    }
}
