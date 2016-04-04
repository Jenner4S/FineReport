// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart;

import com.fr.base.FRContext;
import com.fr.chart.base.ChartInternationalNameContentBean;
import com.fr.chart.chartattr.*;
import com.fr.chart.charttypes.ChartTypeManager;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.*;
import com.fr.stable.StableUtils;
import java.awt.Color;
import java.awt.Component;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

// Referenced classes of package com.fr.design.chart:
//            ChartCommonWizardPane

public class ChartTypePane extends ChartCommonWizardPane
{

    private static final long serialVersionUID = 0xefaf6bc4be62849eL;
    private ChartInternationalNameContentBean typeName[];
    private Chart charts4Icon[][];
    private JList mainTypeList;
    private JList iconViewList;
    private DefaultListModel iconListModel;
    ListCellRenderer iconCellRenderer;
    protected ListSelectionListener listSelectionListener;

    public ChartTypePane()
    {
        typeName = ChartTypeManager.getInstance().getAllChartBaseNames();
        charts4Icon = (Chart[][])null;
        charts4Icon = new Chart[typeName.length][];
        for(int i = 0; i < typeName.length; i++)
        {
            Chart achart[] = ChartTypeManager.getInstance().getChartTypes(typeName[i].getPlotID());
            charts4Icon[i] = new Chart[1];
            for(int k = 0; k < 1; k++)
                try
                {
                    charts4Icon[i][k] = (Chart)achart[k].clone();
                    charts4Icon[i][k].setTitle(null);
                    if(charts4Icon[i][k].getPlot() != null)
                        charts4Icon[i][k].getPlot().setLegend(null);
                }
                catch(CloneNotSupportedException clonenotsupportedexception)
                {
                    FRContext.getLogger().error(clonenotsupportedexception.getMessage(), clonenotsupportedexception);
                }

        }

        mainTypeList = null;
        iconViewList = null;
        iconListModel = null;
        iconCellRenderer = new DefaultListCellRenderer() {

            private static final long serialVersionUID = 0xd478d555ce0998eL;
            final ChartTypePane this$0;

            public Component getListCellRendererComponent(JList jlist, Object obj, int l, boolean flag, boolean flag1)
            {
                super.getListCellRendererComponent(jlist, obj, l, flag, flag1);
                setText("");
                if(obj instanceof ChartIcon)
                {
                    setIcon((ChartIcon)obj);
                    setHorizontalAlignment(0);
                    if(flag)
                        setBackground(new Color(57, 107, 181));
                    ChartIcon charticon = (ChartIcon)obj;
                    setBorder(GUICoreUtils.createTitledBorder(getChartName(charticon), null));
                }
                return this;
            }

            
            {
                this$0 = ChartTypePane.this;
                super();
            }
        }
;
        listSelectionListener = new ListSelectionListener() {

            final ChartTypePane this$0;

            public void valueChanged(ListSelectionEvent listselectionevent)
            {
                int l = mainTypeList.getSelectedIndex();
                Chart achart1[] = charts4Icon[l];
                iconListModel.clear();
                for(int i1 = 0; i1 < achart1.length; i1++)
                    iconListModel.addElement(new ChartIcon(achart1[i1]));

                iconViewList.setSelectedIndex(0);
            }

            
            {
                this$0 = ChartTypePane.this;
                super();
            }
        }
;
        setLayout(FRGUIPaneFactory.createBorderLayout());
        DefaultListModel defaultlistmodel = new DefaultListModel();
        mainTypeList = new JList(defaultlistmodel);
        for(int j = 0; j < typeName.length; j++)
            defaultlistmodel.insertElementAt(Inter.getLocText(typeName[j].getName()), j);

        mainTypeList.addListSelectionListener(listSelectionListener);
        JScrollPane jscrollpane = new JScrollPane(mainTypeList);
        jscrollpane.setVerticalScrollBarPolicy(20);
        iconViewList = new JList();
        iconListModel = new DefaultListModel();
        iconViewList.setModel(iconListModel);
        iconViewList.setVisibleRowCount(0);
        iconViewList.setLayoutOrientation(2);
        iconViewList.setCellRenderer(iconCellRenderer);
        JScrollPane jscrollpane1 = new JScrollPane(iconViewList);
        mainTypeList.setSelectedIndex(0);
        JSplitPane jsplitpane = new JSplitPane(1, true, jscrollpane, jscrollpane1);
        jsplitpane.setDividerLocation(120);
        jsplitpane.setBorder(BorderFactory.createTitledBorder(Inter.getLocText("M-Popup_ChartType")));
        add(jsplitpane);
        iconViewList.setSelectedIndex(0);
    }

    public String getChartName(ChartIcon charticon)
    {
        Chart chart = (Chart)charticon.getChart();
        return chart.getChartName();
    }

    public void populate(Chart chart)
    {
        if(chart == null)
            return;
        Plot plot = chart.getPlot();
        int i = 0;
        int j = 0;
label0:
        for(int k = 0; k < typeName.length; k++)
        {
            Chart achart[] = ChartTypeManager.getInstance().getChartTypes(typeName[k].getPlotID());
            int l = 0;
            do
            {
                if(l >= achart.length)
                    continue label0;
                if(achart[l].getPlot().match4GUI(plot))
                {
                    i = k;
                    j = l;
                    continue label0;
                }
                l++;
            } while(true);
        }

        mainTypeList.setSelectedIndex(i);
        iconViewList.setSelectedIndex(j);
    }

    public void update(Chart chart)
    {
        String s = typeName[mainTypeList.getSelectedIndex()].getPlotID();
        Chart chart1 = ChartTypeManager.getInstance().getChartTypes(s)[iconViewList.getSelectedIndex()];
        if(chart1.getPlot() != null)
        {
            if((chart1.getPlot() instanceof MapPlot) && (!VT4FR.isLicAvailable(StableUtils.getBytes()) || !VT4FR.CHART_MAP.support()))
            {
                JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Chart-Map_Not_Supported"));
                throw new RegistEditionException(VT4FR.CHART_MAP);
            }
            if(chart1.getPlot() != null)
                try
                {
                    chart.changePlotInNewType((Plot)chart1.getPlot().clone());
                }
                catch(CloneNotSupportedException clonenotsupportedexception)
                {
                    FRContext.getLogger().error(clonenotsupportedexception.getMessage(), clonenotsupportedexception);
                }
        }
    }

    public void update(ChartCollection chartcollection)
    {
        if(chartcollection == null)
            return;
        Chart chart = chartcollection.getSelectedChart();
        if(chart == null)
        {
            String s = typeName[mainTypeList.getSelectedIndex()].getPlotID();
            Chart chart1 = ChartTypeManager.getInstance().getChartTypes(s)[iconViewList.getSelectedIndex()];
            try
            {
                chart = (Chart)chart1.clone();
                chartcollection.addChart(chart);
            }
            catch(CloneNotSupportedException clonenotsupportedexception)
            {
                FRContext.getLogger().error(clonenotsupportedexception.getMessage(), clonenotsupportedexception);
            }
        }
        update(chart);
    }




}
