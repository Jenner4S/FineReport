// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.other;

import com.fr.chart.chartattr.*;
import com.fr.chart.chartglyph.*;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.gui.frpane.UICorrelationComboBoxPane;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.general.Inter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class ChartConditionAttrPane extends BasicScrollPane
{

    private static final long serialVersionUID = 0x4f76bb5d63d5c253L;
    private UICorrelationComboBoxPane conditionPane;

    public ChartConditionAttrPane()
    {
    }

    protected JPanel createContentPane()
    {
        if(conditionPane == null)
            conditionPane = new UICorrelationComboBoxPane();
        return conditionPane;
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("Chart-Condition_Display");
    }

    public void populateBean(Chart chart)
    {
        Plot plot = chart.getPlot();
        Class class1 = ChartTypeInterfaceManager.getInstance().getPlotConditionPane(chart.getPlot()).getClass();
        ArrayList arraylist = new ArrayList();
        if(plot instanceof CustomPlot)
            arraylist.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Condition_Attributes"), new CustomAttr(), class1));
        else
            arraylist.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Condition_Attributes"), new ConditionAttr(), class1));
        conditionPane.refreshMenuAndAddMenuAction(arraylist);
        ConditionCollection conditioncollection = chart.getPlot().getConditionCollection();
        ArrayList arraylist1 = new ArrayList();
        for(int i = 0; i < conditioncollection.getConditionAttrSize(); i++)
            arraylist1.add(new UIMenuNameableCreator(conditioncollection.getConditionAttr(i).getName(), conditioncollection.getConditionAttr(i), class1));

        conditionPane.populateBean(arraylist1);
        conditionPane.doLayout();
    }

    public void updateBean(Chart chart)
    {
        List list = conditionPane.updateBean();
        ConditionCollection conditioncollection = chart.getPlot().getConditionCollection();
        conditioncollection.clearConditionAttr();
        for(int i = 0; i < list.size(); i++)
        {
            UIMenuNameableCreator uimenunameablecreator = (UIMenuNameableCreator)list.get(i);
            ConditionAttr conditionattr = (ConditionAttr)uimenunameablecreator.getObj();
            conditionattr.setName(uimenunameablecreator.getName());
            conditioncollection.addConditionAttr(conditionattr);
        }

    }

    public volatile void updateBean(Object obj)
    {
        updateBean((Chart)obj);
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((Chart)obj);
    }
}
