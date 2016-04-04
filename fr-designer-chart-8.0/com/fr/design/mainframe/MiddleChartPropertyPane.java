// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.base.chart.BaseChartCollection;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.designer.TargetComponent;
import com.fr.design.gui.chart.BaseChartPropertyPane;
import com.fr.design.gui.frpane.UITitlePanel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itabpane.TitleChangeListener;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import javax.swing.Icon;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.mainframe:
//            TargetComponentContainer, DockingView

public abstract class MiddleChartPropertyPane extends BaseChartPropertyPane
{

    protected TargetComponentContainer container;
    protected UILabel nameLabel;
    protected ChartEditPane chartEditPane;
    protected TitleChangeListener titleListener;

    public MiddleChartPropertyPane()
    {
        container = new TargetComponentContainer();
        titleListener = new TitleChangeListener() {

            final MiddleChartPropertyPane this$0;

            public void fireTitleChange(String s)
            {
                nameLabel.setText((new StringBuilder()).append(Inter.getLocText("Chart-Property_Table")).append('-').append(s).toString());
            }

            
            {
                this$0 = MiddleChartPropertyPane.this;
                super();
            }
        }
;
        initComponenet();
    }

    protected void initComponenet()
    {
        setLayout(new BorderLayout());
        setBorder(null);
        createNameLabel();
        add(createNorthComponent(), "North");
        chartEditPane = ChartEditPane.getInstance();
        chartEditPane.setSupportCellData(true);
        createMainPane();
    }

    protected abstract void createNameLabel();

    protected abstract JComponent createNorthComponent();

    protected abstract void createMainPane();

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

    public void populateChartPropertyPane(ChartCollection chartcollection, TargetComponent targetcomponent)
    {
        container.setEPane(targetcomponent);
        chartEditPane.populate(chartcollection);
    }

    public void populateChartPropertyPane(BaseChartCollection basechartcollection, TargetComponent targetcomponent)
    {
        if(basechartcollection instanceof ChartCollection)
            populateChartPropertyPane((ChartCollection)basechartcollection, targetcomponent);
    }

    public String getViewTitle()
    {
        return Inter.getLocText("CellElement-Property_Table");
    }

    public Icon getViewIcon()
    {
        return BaseUtils.readIcon("/com/fr/design/images/m_report/qb.png");
    }

    public DockingView.Location preferredLocation()
    {
        return DockingView.Location.WEST_BELOW;
    }

    public UITitlePanel createTitlePanel()
    {
        return new UITitlePanel(this);
    }

    public void refreshDockingView()
    {
    }

    public void setSupportCellData(boolean flag)
    {
        if(chartEditPane != null)
            chartEditPane.setSupportCellData(flag);
    }
}
