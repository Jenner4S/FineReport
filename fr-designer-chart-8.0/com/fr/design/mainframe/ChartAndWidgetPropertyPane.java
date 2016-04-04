// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.constants.UIConstants;
import com.fr.design.designer.TargetComponent;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itabpane.TitleChangeListener;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.module.DesignModuleFactory;
import com.fr.general.Inter;
import java.awt.Component;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Referenced classes of package com.fr.design.mainframe:
//            MiddleChartPropertyPane, BaseWidgetPropertyPane, BaseFormDesigner

public class ChartAndWidgetPropertyPane extends MiddleChartPropertyPane
{

    private static ChartAndWidgetPropertyPane singleton;
    private BaseWidgetPropertyPane widgetpane;
    private UIToggleButton hisButton;

    public static synchronized ChartAndWidgetPropertyPane getInstance(BaseFormDesigner baseformdesigner)
    {
        if(singleton == null)
            singleton = new ChartAndWidgetPropertyPane(baseformdesigner);
        singleton.setWidgetPropertyPane(DesignModuleFactory.getWidgetPropertyPane(baseformdesigner));
        singleton.setSureProperty();
        return singleton;
    }

    public static ChartAndWidgetPropertyPane getInstance()
    {
        if(singleton == null)
            singleton = new ChartAndWidgetPropertyPane();
        return singleton;
    }

    public ChartAndWidgetPropertyPane()
    {
        widgetpane = null;
    }

    public ChartAndWidgetPropertyPane(BaseFormDesigner baseformdesigner)
    {
        widgetpane = null;
        widgetpane = DesignModuleFactory.getWidgetPropertyPane(baseformdesigner);
    }

    public void setWidgetPropertyPane(BaseWidgetPropertyPane basewidgetpropertypane)
    {
        widgetpane = basewidgetpropertypane;
    }

    protected void createMainPane()
    {
        add(chartEditPane, "Center");
    }

    protected void createNameLabel()
    {
        nameLabel = new UILabel();
        nameLabel.setHorizontalAlignment(2);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(-2, 6, 2, 0));
    }

    protected JComponent createNorthComponent()
    {
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        JPanel jpanel1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel1.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        hisButton = new UIToggleButton(Inter.getLocText(new String[] {
            "Widget", "Attribute"
        }), UIConstants.HISTORY_ICON);
        hisButton.setNormalPainted(false);
        hisButton.setBorderPaintedOnlyWhenPressed(true);
        jpanel1.add(hisButton, "Center");
        hisButton.addChangeListener(new ChangeListener() {

            final ChartAndWidgetPropertyPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                cardChange();
            }

            
            {
                this$0 = ChartAndWidgetPropertyPane.this;
                super();
            }
        }
);
        jpanel.add(nameLabel, "Center");
        jpanel.add(jpanel1, "East");
        titleListener = new TitleChangeListener() {

            final ChartAndWidgetPropertyPane this$0;

            public void fireTitleChange(String s)
            {
                if(hisButton.isSelected())
                    nameLabel.setText(Inter.getLocText(new String[] {
                        "Widget", "Form-Widget_Property_Table"
                    }));
                else
                    nameLabel.setText((new StringBuilder()).append(Inter.getLocText("Chart-Property_Table")).append('-').append(s).toString());
            }

            
            {
                this$0 = ChartAndWidgetPropertyPane.this;
                super();
            }
        }
;
        return jpanel;
    }

    public void populateChartPropertyPane(ChartCollection chartcollection, TargetComponent targetcomponent)
    {
        super.populateChartPropertyPane(chartcollection, targetcomponent);
        resetChartEditPane();
    }

    protected void resetChartEditPane()
    {
        cardChange();
    }

    private void cardChange()
    {
        remove(chartEditPane);
        remove((Component)widgetpane);
        if(hisButton.isSelected())
        {
            nameLabel.setText(Inter.getLocText(new String[] {
                "Widget", "Form-Widget_Property_Table"
            }));
            add((Component)widgetpane, "Center");
        } else
        {
            String s = chartEditPane.getSelectedTabName();
            nameLabel.setText((new StringBuilder()).append(Inter.getLocText(new String[] {
                "Utils-The-Chart", "Form-Widget_Property_Table"
            })).append(s == null ? "" : (new StringBuilder()).append('-').append(chartEditPane.getSelectedTabName()).toString()).toString());
            add(chartEditPane, "Center");
        }
        validate();
        repaint();
        revalidate();
    }


}
