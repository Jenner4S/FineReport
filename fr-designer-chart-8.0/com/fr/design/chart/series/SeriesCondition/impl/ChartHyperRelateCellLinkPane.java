// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.base.Utils;
import com.fr.chart.web.ChartHyperRelateCellLink;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.columnrow.ColumnRowVerticalPane;
import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.stable.ColumnRow;
import com.fr.stable.ParameterProvider;
import java.awt.Dimension;
import javax.swing.JPanel;

public class ChartHyperRelateCellLinkPane extends BasicBeanPane
{
    public static class CHART_METER extends ChartHyperRelateCellLinkPane
    {

        protected int getChartParaType()
        {
            return 10;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((ChartHyperRelateCellLink)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ChartHyperRelateCellLink)obj);
        }

        public CHART_METER()
        {
        }
    }

    public static class CHART_GANTT extends ChartHyperRelateCellLinkPane
    {

        protected int getChartParaType()
        {
            return 9;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((ChartHyperRelateCellLink)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ChartHyperRelateCellLink)obj);
        }

        public CHART_GANTT()
        {
        }
    }

    public static class CHART_STOCK extends ChartHyperRelateCellLinkPane
    {

        protected int getChartParaType()
        {
            return 6;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((ChartHyperRelateCellLink)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ChartHyperRelateCellLink)obj);
        }

        public CHART_STOCK()
        {
        }
    }

    public static class CHART_BUBBLE extends ChartHyperRelateCellLinkPane
    {

        protected int getChartParaType()
        {
            return 5;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((ChartHyperRelateCellLink)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ChartHyperRelateCellLink)obj);
        }

        public CHART_BUBBLE()
        {
        }
    }

    public static class CHART_XY extends ChartHyperRelateCellLinkPane
    {

        protected int getChartParaType()
        {
            return 4;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((ChartHyperRelateCellLink)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ChartHyperRelateCellLink)obj);
        }

        public CHART_XY()
        {
        }
    }

    public static class CHART_PIE extends ChartHyperRelateCellLinkPane
    {

        protected int getChartParaType()
        {
            return 3;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((ChartHyperRelateCellLink)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ChartHyperRelateCellLink)obj);
        }

        public CHART_PIE()
        {
        }
    }

    public static class CHART_GIS extends ChartHyperRelateCellLinkPane
    {

        protected int getChartParaType()
        {
            return 8;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((ChartHyperRelateCellLink)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ChartHyperRelateCellLink)obj);
        }

        public CHART_GIS()
        {
        }
    }

    public static class CHART_MAP extends ChartHyperRelateCellLinkPane
    {

        protected int getChartParaType()
        {
            return 2;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((ChartHyperRelateCellLink)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ChartHyperRelateCellLink)obj);
        }

        public CHART_MAP()
        {
        }
    }

    public static class CHART_NO_RENAME extends ChartHyperRelateCellLinkPane
    {

        protected boolean needRenamePane()
        {
            return false;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((ChartHyperRelateCellLink)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ChartHyperRelateCellLink)obj);
        }

        public CHART_NO_RENAME()
        {
        }
    }


    private static final long serialVersionUID = 0x6d496f86baeefffeL;
    private UITextField itemNameTextField;
    private ColumnRowVerticalPane colRowPane;
    private ReportletParameterViewPane parameterViewPane;

    public ChartHyperRelateCellLinkPane()
    {
        initComponent();
    }

    private void initComponent()
    {
        setLayout(FRGUIPaneFactory.createM_BorderLayout());
        if(needRenamePane())
        {
            itemNameTextField = new UITextField();
            add(GUICoreUtils.createNamedPane(itemNameTextField, (new StringBuilder()).append(Inter.getLocText("Name")).append(":").toString()), "North");
        }
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_L_Pane();
        add(jpanel, "Center");
        jpanel.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText(new String[] {
            "Related", "Cell"
        })));
        colRowPane = new ColumnRowVerticalPane();
        jpanel.add(colRowPane, "North");
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

    public void populateBean(ChartHyperRelateCellLink charthyperrelatecelllink)
    {
        if(charthyperrelatecelllink == null)
            return;
        if(itemNameTextField != null)
            itemNameTextField.setText(charthyperrelatecelllink.getItemName());
        if(charthyperrelatecelllink.getRelateCCName() != null)
        {
            ColumnRow columnrow = ColumnRow.valueOf(charthyperrelatecelllink.getRelateCCName());
            colRowPane.populate(columnrow);
        } else
        {
            colRowPane.populate(ColumnRow.valueOf("A1"));
        }
        java.util.List list = parameterViewPane.update();
        list.clear();
        ParameterProvider aparameterprovider[] = charthyperrelatecelllink.getParameters();
        parameterViewPane.populate(aparameterprovider);
    }

    public ChartHyperRelateCellLink updateBean()
    {
        ChartHyperRelateCellLink charthyperrelatecelllink = new ChartHyperRelateCellLink();
        updateBean(charthyperrelatecelllink);
        if(itemNameTextField != null)
            charthyperrelatecelllink.setItemName(itemNameTextField.getText());
        return charthyperrelatecelllink;
    }

    public void updateBean(ChartHyperRelateCellLink charthyperrelatecelllink)
    {
        charthyperrelatecelllink.setRelateCCName(Utils.objectToString(colRowPane.update()));
        java.util.List list = parameterViewPane.update();
        if(list != null && !list.isEmpty())
        {
            ParameterProvider aparameterprovider[] = new ParameterProvider[list.size()];
            list.toArray(aparameterprovider);
            charthyperrelatecelllink.setParameters(aparameterprovider);
        } else
        {
            charthyperrelatecelllink.setParameters(null);
        }
        if(itemNameTextField != null)
            charthyperrelatecelllink.setItemName(itemNameTextField.getText());
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText(new String[] {
            "Related", "Cell"
        });
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((ChartHyperRelateCellLink)obj);
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((ChartHyperRelateCellLink)obj);
    }
}
