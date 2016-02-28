package com.fr.plugin.chart.designer.component;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.base.AttrAreaSeriesFillColorBackground;
import com.fr.plugin.chart.designer.component.background.VanChartMarkerBackgroundPane;

import java.awt.*;

/**
 * ���ͼ���ɫ���ý���
 */
public class VanChartAreaSeriesFillColorPane extends BasicPane {
    private static final long serialVersionUID = 9166866984438854779L;
    private VanChartMarkerBackgroundPane fillColorPane;
    private UINumberDragPane transparent;

    public VanChartAreaSeriesFillColorPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p,p};
        double[] col = {f};
        fillColorPane = new VanChartMarkerBackgroundPane();
        transparent = new UINumberDragPane(0, 100);

        Component[][] components = new Component[][]{
                new Component[]{fillColorPane},
                new Component[]{transparent},
        };

        this.add(TableLayoutHelper.createTableLayoutPane(components, row, col));
    }

    protected String title4PopupWindow(){
        return Inter.getLocText("Plugin-ChartF_FillColor");
    }

    public void populate(AttrAreaSeriesFillColorBackground fillColorBackground) {
        if(fillColorBackground == null){
            fillColorBackground = new AttrAreaSeriesFillColorBackground();
        }
        fillColorPane.populate(fillColorBackground.getColorBackground());
        transparent.populateBean(fillColorBackground.getAlpha() * VanChartAttrHelper.PERCENT);
    }

    public AttrAreaSeriesFillColorBackground update() {
        AttrAreaSeriesFillColorBackground fillColorBackground = new AttrAreaSeriesFillColorBackground();
        fillColorBackground.setColorBackground(fillColorPane.update());
        fillColorBackground.setAlpha(transparent.updateBean()/VanChartAttrHelper.PERCENT);
        return fillColorBackground;
    }
}
