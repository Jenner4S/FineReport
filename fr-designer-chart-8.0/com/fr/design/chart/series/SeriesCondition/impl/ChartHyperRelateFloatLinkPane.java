// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.base.Utils;
import com.fr.chart.web.ChartHyperRelateFloatLink;
import com.fr.design.DesignModelAdapter;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.stable.ParameterProvider;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class ChartHyperRelateFloatLinkPane extends BasicBeanPane
{
    public static class CHART_METER extends ChartHyperRelateFloatLinkPane
    {

        protected int getChartParaType()
        {
            return 10;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((ChartHyperRelateFloatLink)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ChartHyperRelateFloatLink)obj);
        }

        public CHART_METER()
        {
        }
    }

    public static class CHART_GANTT extends ChartHyperRelateFloatLinkPane
    {

        protected int getChartParaType()
        {
            return 9;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((ChartHyperRelateFloatLink)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ChartHyperRelateFloatLink)obj);
        }

        public CHART_GANTT()
        {
        }
    }

    public static class CHART_STOCK extends ChartHyperRelateFloatLinkPane
    {

        protected int getChartParaType()
        {
            return 6;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((ChartHyperRelateFloatLink)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ChartHyperRelateFloatLink)obj);
        }

        public CHART_STOCK()
        {
        }
    }

    public static class CHART_BUBBLE extends ChartHyperRelateFloatLinkPane
    {

        protected int getChartParaType()
        {
            return 5;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((ChartHyperRelateFloatLink)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ChartHyperRelateFloatLink)obj);
        }

        public CHART_BUBBLE()
        {
        }
    }

    public static class CHART_XY extends ChartHyperRelateFloatLinkPane
    {

        protected int getChartParaType()
        {
            return 4;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((ChartHyperRelateFloatLink)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ChartHyperRelateFloatLink)obj);
        }

        public CHART_XY()
        {
        }
    }

    public static final class CHART_PIE extends ChartHyperRelateFloatLinkPane
    {

        protected int getChartParaType()
        {
            return 3;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((ChartHyperRelateFloatLink)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ChartHyperRelateFloatLink)obj);
        }

        public CHART_PIE()
        {
        }
    }

    public static final class CHART_GIS extends ChartHyperRelateFloatLinkPane
    {

        protected int getChartParaType()
        {
            return 8;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((ChartHyperRelateFloatLink)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ChartHyperRelateFloatLink)obj);
        }

        public CHART_GIS()
        {
        }
    }

    public static final class CHART_MAP extends ChartHyperRelateFloatLinkPane
    {

        protected int getChartParaType()
        {
            return 2;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((ChartHyperRelateFloatLink)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ChartHyperRelateFloatLink)obj);
        }

        public CHART_MAP()
        {
        }
    }

    public static class CHART_NO_RENAME extends ChartHyperRelateFloatLinkPane
    {

        protected boolean needRenamePane()
        {
            return false;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((ChartHyperRelateFloatLink)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ChartHyperRelateFloatLink)obj);
        }

        public CHART_NO_RENAME()
        {
        }
    }


    private static final long serialVersionUID = 0xd21628cfc65f1b17L;
    private UITextField itemNameTextField;
    private UIComboBox floatNameBox;
    private ReportletParameterViewPane parameterViewPane;

    public ChartHyperRelateFloatLinkPane()
    {
        initComponent();
    }

    private void initComponent()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_L_Pane();
        if(needRenamePane())
        {
            itemNameTextField = new UITextField();
            add(GUICoreUtils.createNamedPane(itemNameTextField, (new StringBuilder()).append(Inter.getLocText("Name")).append(":").toString()), "North");
        }
        add(jpanel, "Center");
        floatNameBox = new UIComboBox(getFloatNames());
        floatNameBox.setPreferredSize(new Dimension(90, 20));
        JPanel jpanel1 = FRGUIPaneFactory.createBoxFlowInnerContainer_S_Pane();
        jpanel1.add(new UILabel((new StringBuilder()).append(Inter.getLocText("M_Insert-Float")).append(":").toString()));
        jpanel1.add(floatNameBox);
        javax.swing.border.Border border = null;
        java.awt.Font font = null;
        TitledBorder titledborder = new TitledBorder(border, Inter.getLocText(new String[] {
            "Related", "M_Insert-Float"
        }), 4, 2, font, new Color(1, 159, 222));
        jpanel.setBorder(titledborder);
        jpanel.add(jpanel1, "North");
        parameterViewPane = new ReportletParameterViewPane(getChartParaType());
        parameterViewPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("Parameters")));
        parameterViewPane.setPreferredSize(new Dimension(500, 200));
        add(parameterViewPane, "South");
    }

    protected int getChartParaType()
    {
        return 1;
    }

    protected boolean needRenamePane()
    {
        return true;
    }

    private String[] getFloatNames()
    {
        DesignModelAdapter designmodeladapter = DesignModelAdapter.getCurrentModelAdapter();
        if(designmodeladapter != null)
            return designmodeladapter.getFloatNames();
        else
            return new String[0];
    }

    public void populateBean(ChartHyperRelateFloatLink charthyperrelatefloatlink)
    {
        if(charthyperrelatefloatlink == null)
            return;
        if(itemNameTextField != null)
            itemNameTextField.setText(charthyperrelatefloatlink.getItemName());
        floatNameBox.setSelectedItem(charthyperrelatefloatlink.getRelateCCName());
        java.util.List list = parameterViewPane.update();
        list.clear();
        ParameterProvider aparameterprovider[] = charthyperrelatefloatlink.getParameters();
        parameterViewPane.populate(aparameterprovider);
    }

    public ChartHyperRelateFloatLink updateBean()
    {
        ChartHyperRelateFloatLink charthyperrelatefloatlink = new ChartHyperRelateFloatLink();
        updateBean(charthyperrelatefloatlink);
        if(itemNameTextField != null)
            charthyperrelatefloatlink.setItemName(itemNameTextField.getText());
        return charthyperrelatefloatlink;
    }

    public void updateBean(ChartHyperRelateFloatLink charthyperrelatefloatlink)
    {
        if(floatNameBox.getSelectedItem() != null)
            charthyperrelatefloatlink.setRelateCCName(Utils.objectToString(floatNameBox.getSelectedItem()));
        java.util.List list = parameterViewPane.update();
        if(list != null && !list.isEmpty())
        {
            ParameterProvider aparameterprovider[] = new ParameterProvider[list.size()];
            list.toArray(aparameterprovider);
            charthyperrelatefloatlink.setParameters(aparameterprovider);
        } else
        {
            charthyperrelatefloatlink.setParameters(null);
        }
        if(itemNameTextField != null)
            charthyperrelatefloatlink.setItemName(itemNameTextField.getText());
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText(new String[] {
            "Related", "M_Insert-Float"
        });
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((ChartHyperRelateFloatLink)obj);
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((ChartHyperRelateFloatLink)obj);
    }
}
