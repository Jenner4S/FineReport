// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition;

import com.fr.base.FRContext;
import com.fr.chart.base.AttrAlpha;
import com.fr.chart.base.AttrBackground;
import com.fr.chart.base.AttrContents;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.data.condition.AbstractCondition;
import com.fr.data.condition.ListCondition;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.condition.LiteConditionPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import java.awt.Dimension;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.chart.series.SeriesCondition:
//            LabelAlphaPane, LabelContentsPane, LabelBackgroundPane, ChartConditionPaneFactory

public class DataSeriesConditionPane extends ConditionAttributesPane
{

    public DataSeriesConditionPane()
    {
        initAvailableActionList();
        initComponents();
    }

    private void initAvailableActionList()
    {
        addBasicAction();
        addAxisPositionAction();
        addStyleAction();
        addBorderAction();
        addTrendLineAction();
        addAction2UseAbleActionList();
    }

    protected void initComponents()
    {
        super.initComponents();
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_L_Pane();
        add(jpanel);
        jpanel.setBorder(BorderFactory.createEmptyBorder());
        jpanel.add(liteConditionPane = ChartConditionPaneFactory.createChartConditionPane(getClass()), "Center");
        liteConditionPane.setPreferredSize(new Dimension(300, 300));
    }

    protected void addBasicAction()
    {
        classPaneMap.put(com/fr/chart/base/AttrAlpha, new LabelAlphaPane(this));
        classPaneMap.put(com/fr/chart/base/AttrContents, new LabelContentsPane(this, class4Correspond()));
    }

    protected void addStyleAction()
    {
        classPaneMap.put(com/fr/chart/base/AttrBackground, new LabelBackgroundPane(this));
    }

    protected void addAxisPositionAction()
    {
    }

    protected void addBorderAction()
    {
    }

    protected void addTrendLineAction()
    {
    }

    public Class class4Correspond()
    {
        return com/fr/chart/chartattr/Plot;
    }

    protected void addAction2UseAbleActionList()
    {
        useAbleActionList.clear();
        for(Iterator iterator = classPaneMap.values().iterator(); iterator.hasNext(); useAbleActionList.add(((ConditionAttrSingleConditionPane)iterator.next()).getHighLightConditionAction()));
    }

    public ConditionAttrSingleConditionPane createConditionAttrSingleConditionPane(Class class1)
    {
        try
        {
            return (ConditionAttrSingleConditionPane)class1.newInstance();
        }
        catch(InstantiationException instantiationexception)
        {
            FRContext.getLogger().error(instantiationexception.getMessage(), instantiationexception);
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            FRContext.getLogger().error(illegalaccessexception.getMessage(), illegalaccessexception);
        }
        return null;
    }

    public void populateBean(ConditionAttr conditionattr)
    {
        selectedItemPane.removeAll();
        initAvailableActionList();
        if(conditionattr.getCondition() == null)
            liteConditionPane.populateBean(new ListCondition());
        else
            liteConditionPane.populateBean(conditionattr.getCondition());
        for(int i = 0; i < conditionattr.getDataSeriesConditionCount(); i++)
        {
            DataSeriesCondition dataseriescondition = conditionattr.getDataSeriesCondition(i);
            ConditionAttrSingleConditionPane conditionattrsingleconditionpane = (ConditionAttrSingleConditionPane)classPaneMap.get(dataseriescondition.getClass());
            if(conditionattrsingleconditionpane != null && useAbleActionList.contains(conditionattrsingleconditionpane.getHighLightConditionAction()))
            {
                conditionattrsingleconditionpane.populate(dataseriescondition);
                selectedItemPane.add(conditionattrsingleconditionpane);
                useAbleActionList.remove(conditionattrsingleconditionpane.getHighLightConditionAction());
            }
        }

        updateMenuDef();
        validate();
        repaint(10L);
    }

    public ConditionAttr updateBean()
    {
        ConditionAttr conditionattr = new ConditionAttr();
        updateBeanInvoked(conditionattr);
        return conditionattr;
    }

    public void updateBean(ConditionAttr conditionattr)
    {
        conditionattr.removeAll();
        updateBeanInvoked(conditionattr);
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText(new String[] {
            "Condition", "Display"
        });
    }

    public void updateBeanInvoked(ConditionAttr conditionattr)
    {
        conditionattr.removeAll();
        Iterator iterator = classPaneMap.values().iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            ConditionAttrSingleConditionPane conditionattrsingleconditionpane = (ConditionAttrSingleConditionPane)iterator.next();
            if(conditionattrsingleconditionpane.getParent() == selectedItemPane)
                conditionattr.addDataSeriesCondition((DataSeriesCondition)conditionattrsingleconditionpane.update());
        } while(true);
        AbstractCondition abstractcondition = (AbstractCondition)liteConditionPane.updateBean();
        conditionattr.setCondition(abstractcondition);
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((ConditionAttr)obj);
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((ConditionAttr)obj);
    }
}
