// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.base.Parameter;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.web.ChartHyperPoplink;
import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JPanel;

public class ChartHyperPopAttrPane extends AbstractChartAttrPane
{

    private UITextField titleField;
    private UINumberField widthField;
    private UINumberField heightField;
    private ReportletParameterViewPane parameterViewPane;
    public static final int DEFAULT_H_VALUE = 270;
    public static final int DEFAULT_V_VALUE = 500;
    private int paraType;

    public ChartHyperPopAttrPane(int i)
    {
        paraType = i;
        initAll();
    }

    protected JPanel createContentPane()
    {
        JPanel jpanel = new JPanel();
        jpanel.setLayout(FRGUIPaneFactory.createBorderLayout());
        titleField = new UITextField(15);
        titleField.setPreferredSize(new Dimension(200, 20));
        widthField = new UINumberField(4);
        widthField.setColumns(10);
        widthField.setPreferredSize(new Dimension(200, 20));
        heightField = new UINumberField(4);
        heightField.setColumns(10);
        heightField.setPreferredSize(new Dimension(200, 20));
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d, d, d, d
        };
        Component acomponent[][] = {
            {
                new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("FR-Designer-Widget-Style_Title")).append(":").toString(), 4), titleField
            }, {
                new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("FR-Designer_Width")).append(":").toString(), 4), widthField
            }, {
                new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("FR-Designer_Height")).append(":").toString(), 4), heightField
            }
        };
        widthField.setText(String.valueOf(500));
        heightField.setText(String.valueOf(270));
        JPanel jpanel1 = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        jpanel.add(jpanel1, "North");
        parameterViewPane = new ReportletParameterViewPane(paraType);
        parameterViewPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FR-Designer_Parameter")));
        parameterViewPane.setPreferredSize(new Dimension(200, 200));
        jpanel.add(parameterViewPane, "Center");
        return jpanel;
    }

    public void populateBean(ChartHyperPoplink charthyperpoplink)
    {
        titleField.setText(charthyperpoplink.getChartDigTitle());
        widthField.setText(String.valueOf(charthyperpoplink.getWidth()));
        heightField.setText(String.valueOf(charthyperpoplink.getHeight()));
        java.util.List list = parameterViewPane.update();
        list.clear();
        com.fr.stable.ParameterProvider aparameterprovider[] = charthyperpoplink.getParameters();
        parameterViewPane.populate(aparameterprovider);
    }

    public void updateBean(ChartHyperPoplink charthyperpoplink)
    {
        String s = titleField.getText();
        if(StringUtils.isBlank(s))
            s = "Chart";
        charthyperpoplink.setChartDigTitle(s);
        charthyperpoplink.setWidth((int)widthField.getValue());
        charthyperpoplink.setHeight((int)heightField.getValue());
        java.util.List list = parameterViewPane.update();
        if(!list.isEmpty())
        {
            Parameter aparameter[] = new Parameter[list.size()];
            list.toArray(aparameter);
            charthyperpoplink.setParameters(aparameter);
        } else
        {
            charthyperpoplink.setParameters(null);
        }
    }

    public void populate(ChartCollection chartcollection)
    {
    }

    public void update(ChartCollection chartcollection)
    {
    }

    public String getIconPath()
    {
        return "com/fr/design/images/chart/link.png";
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("FR-Hyperlink_ChartHyperlink");
    }
}
