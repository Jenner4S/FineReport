// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.chart.BaseChartCollection;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.designer.TargetComponent;
import com.fr.design.gui.chart.BaseChartPropertyPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itabpane.TitleChangeListener;
import com.fr.design.mainframe.chart.ChartDesignEditPane;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Icon;

// Referenced classes of package com.fr.design.mainframe:
//            TargetComponentContainer, DockingView, BaseWidgetPropertyPane

public class ChartDesignerPropertyPane extends BaseChartPropertyPane
{

    private static ChartDesignerPropertyPane instance;
    private TargetComponentContainer container;
    private ChartEditPane chartEditPane;
    private UILabel nameLabel;
    private TitleChangeListener titleListener;

    public static synchronized ChartDesignerPropertyPane getInstance()
    {
        if(instance == null)
            instance = new ChartDesignerPropertyPane();
        instance.setSureProperty();
        return instance;
    }

    public ChartDesignerPropertyPane()
    {
        container = new TargetComponentContainer();
        titleListener = new TitleChangeListener() {

            final ChartDesignerPropertyPane this$0;

            public void fireTitleChange(String s)
            {
                nameLabel.setText((new StringBuilder()).append(Inter.getLocText("Chart-Property_Table")).append('-').append(s).toString());
            }

            
            {
                this$0 = ChartDesignerPropertyPane.this;
                super();
            }
        }
;
        setLayout(new BorderLayout());
        setBorder(null);
        createNameLabel();
        add(nameLabel, "North");
        chartEditPane = ChartDesignEditPane.getInstance();
        add(chartEditPane, "Center");
    }

    private void createNameLabel()
    {
        nameLabel = new UILabel() {

            final ChartDesignerPropertyPane this$0;

            public Dimension getPreferredSize()
            {
                return new Dimension(super.getPreferredSize().width, 18);
            }

            
            {
                this$0 = ChartDesignerPropertyPane.this;
                super();
            }
        }
;
        nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));
        nameLabel.setHorizontalAlignment(0);
    }

    public void setSureProperty()
    {
        chartEditPane.setContainer(container);
        chartEditPane.addTitleChangeListener(titleListener);
        String s = chartEditPane.getSelectedTabName();
        nameLabel.setText((new StringBuilder()).append(Inter.getLocText("Chart-Property_Table")).append(s == null ? "" : (new StringBuilder()).append('-').append(chartEditPane.getSelectedTabName()).toString()).toString());
        resetChartEditPane();
    }

    protected void resetChartEditPane()
    {
        remove(chartEditPane);
        add(chartEditPane, "Center");
        validate();
        repaint();
        revalidate();
    }

    public void setSupportCellData(boolean flag)
    {
    }

    public void populateChartPropertyPane(BaseChartCollection basechartcollection, TargetComponent targetcomponent)
    {
        if(basechartcollection instanceof ChartCollection)
        {
            container.setEPane(targetcomponent);
            chartEditPane.populate((ChartCollection)basechartcollection);
        }
    }

    public void setWidgetPropertyPane(BaseWidgetPropertyPane basewidgetpropertypane)
    {
    }

    public void refreshDockingView()
    {
    }

    public String getViewTitle()
    {
        return null;
    }

    public Icon getViewIcon()
    {
        return null;
    }

    public DockingView.Location preferredLocation()
    {
        return null;
    }

}
