// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.axis;

import com.fr.chart.chartattr.Axis;
import com.fr.chart.chartattr.Plot;
import com.fr.design.dialog.BasicPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import java.awt.CardLayout;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

// Referenced classes of package com.fr.design.chart.axis:
//            AxisStyleObject

public abstract class ChartStyleAxisPane extends BasicPane
    implements ListSelectionListener
{

    protected static final String CATE_AXIS = Inter.getLocText("ChartF-Category_Axis");
    protected static final String VALUE_AXIS = Inter.getLocText("Chart_F_Radar_Axis");
    protected static final String SECOND_AXIS = Inter.getLocText(new String[] {
        "Second", "Chart_F_Radar_Axis"
    });
    private JList mainList;
    private CardLayout cardLayout;
    private JPanel cardDisplayPane;
    private java.util.List axisStylePaneList;

    public ChartStyleAxisPane(Plot plot)
    {
        cardDisplayPane = null;
        axisStylePaneList = new ArrayList();
        initComponents(plot);
    }

    private void initComponents(Plot plot)
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        DefaultListModel defaultlistmodel = new DefaultListModel();
        mainList = new JList(defaultlistmodel);
        AxisStyleObject aaxisstyleobject[] = createAxisStyleObjects(plot);
        cardLayout = new CardLayout();
        cardDisplayPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        cardDisplayPane.setLayout(cardLayout);
        for(int i = 0; i < aaxisstyleobject.length; i++)
        {
            AxisStyleObject axisstyleobject = aaxisstyleobject[i];
            defaultlistmodel.addElement(axisstyleobject.getName());
            com.fr.design.mainframe.chart.gui.style.axis.ChartAxisUsePane chartaxisusepane = axisstyleobject.getAxisStylePane();
            axisStylePaneList.add(chartaxisusepane);
            cardDisplayPane.add(chartaxisusepane, axisstyleobject.getName());
        }

        mainList.setSelectedIndex(0);
        mainList.addListSelectionListener(this);
        add(new JSplitPane(1, true, mainList, cardDisplayPane));
    }

    protected String title4PopupWindow()
    {
        return "Axis";
    }

    public void valueChanged(ListSelectionEvent listselectionevent)
    {
        cardLayout.show(cardDisplayPane, (String)mainList.getSelectedValue());
    }

    public abstract AxisStyleObject[] createAxisStyleObjects(Plot plot);

    public void populate(Plot plot)
    {
        int i = 0;
        for(int j = axisStylePaneList.size(); i < j; i++);
    }

    public void update(Plot plot)
    {
        int i = 0;
        for(int j = axisStylePaneList.size(); i < j; i++);
    }

    private Axis getAxisFromPlotByListIndex(Plot plot, int i)
    {
        return plot.getAxis(i);
    }

}
