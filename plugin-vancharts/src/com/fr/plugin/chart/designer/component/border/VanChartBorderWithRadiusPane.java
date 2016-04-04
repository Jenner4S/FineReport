package com.fr.plugin.chart.designer.component.border;

import com.fr.chart.base.AttrBorder;
import com.fr.chart.chartglyph.GeneralInfo;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.general.Inter;
import com.fr.stable.CoreConstants;

import javax.swing.*;
import java.awt.*;

/**
 * ÏßÐÍ + ÑÕÉ« + Ô²½Ç°ë¾¶
 */
public class VanChartBorderWithRadiusPane extends VanChartBorderPane {
    private static final long serialVersionUID = -3937853702118283803L;
    private UISpinner radius;

    protected void initComponents() {
        currentLineCombo = new LineComboBox(CoreConstants.STRIKE_LINE_STYLE_ARRAY_4_CHART);
        currentLineColorPane = new ColorSelectBox(100);
        radius = new UISpinner(0,1000,1,0);
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = { p,f };
        double[] rowSize = {p, p, p, p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_LineStyle")+":"),currentLineCombo},
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Color_Color")+":"),currentLineColorPane},
                new Component[]{new UILabel(Inter.getLocText("plugin-ChartF_Radius")+":"),radius}
        } ;
        JPanel panel = TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"Border"}, components, rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER) ;
        this.add(new JSeparator(), BorderLayout.SOUTH);
    }

    public void populate(GeneralInfo attr) {
        super.populate(attr);
        if(attr == null) {
            return;
        }
        radius.setValue(attr.getRoundRadius());
    }

    public void update(GeneralInfo attr) {
        super.update(attr);
        attr.setRoundRadius((int)radius.getValue());
    }

    public void update(AttrBorder attrBorder) {
        super.update(attrBorder);
        attrBorder.setRoundRadius((int)radius.getValue());
    }

    public void populate(AttrBorder attr) {
        super.populate(attr);
        if(attr == null) {
            return;
        }
        radius.setValue(attr.getRoundRadius());
    }

    public AttrBorder update() {
        AttrBorder attr = super.update();
        attr.setRoundRadius((int)radius.getValue());
        return attr;
    }
}
