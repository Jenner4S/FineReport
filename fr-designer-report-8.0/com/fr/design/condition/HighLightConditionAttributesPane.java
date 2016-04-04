// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.condition;

import com.fr.data.condition.ListCondition;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.fun.HighlightProvider;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.data.Condition;
import com.fr.report.cell.cellattr.highlight.BackgroundHighlightAction;
import com.fr.report.cell.cellattr.highlight.BorderHighlightAction;
import com.fr.report.cell.cellattr.highlight.ColWidthHighlightAction;
import com.fr.report.cell.cellattr.highlight.DefaultHighlight;
import com.fr.report.cell.cellattr.highlight.FRFontHighlightAction;
import com.fr.report.cell.cellattr.highlight.ForegroundHighlightAction;
import com.fr.report.cell.cellattr.highlight.HighlightAction;
import com.fr.report.cell.cellattr.highlight.HyperlinkHighlightAction;
import com.fr.report.cell.cellattr.highlight.PaddingHighlightAction;
import com.fr.report.cell.cellattr.highlight.PageHighlightAction;
import com.fr.report.cell.cellattr.highlight.PresentHighlightAction;
import com.fr.report.cell.cellattr.highlight.RowHeightHighlightAction;
import com.fr.report.cell.cellattr.highlight.ValueHighlightAction;
import com.fr.report.cell.cellattr.highlight.WidgetHighlightAction;
import java.awt.Dimension;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.condition:
//            ConditionAttributesPane, ObjectLiteConditionPane, ForeGroundPane, BackPane, 
//            FontPane, PresentHighlightPane, PaddingPane, RowHeightPane, 
//            ColumnWidthPane, PagePane, HyperlinkPane, BorderHighlightPane, 
//            WidgetHighlightPane, NewRealValuePane, ConditionAttrSingleConditionPane, LiteConditionPane

public class HighLightConditionAttributesPane extends ConditionAttributesPane
{

    public HighLightConditionAttributesPane()
    {
        initComponents();
    }

    public void initComponents()
    {
        super.initComponents();
        initActionList();
        initLiteConditionPane();
    }

    protected void initLiteConditionPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        add(jpanel);
        liteConditionPane = new ObjectLiteConditionPane();
        jpanel.add(liteConditionPane, "Center");
        liteConditionPane.setPreferredSize(new Dimension(300, 300));
    }

    protected void initActionList()
    {
        classPaneMap.put(com/fr/report/cell/cellattr/highlight/ForegroundHighlightAction, new ForeGroundPane(this));
        classPaneMap.put(com/fr/report/cell/cellattr/highlight/BackgroundHighlightAction, new BackPane(this));
        classPaneMap.put(com/fr/report/cell/cellattr/highlight/FRFontHighlightAction, new FontPane(this));
        classPaneMap.put(com/fr/report/cell/cellattr/highlight/PresentHighlightAction, new PresentHighlightPane(this));
        classPaneMap.put(com/fr/report/cell/cellattr/highlight/PaddingHighlightAction, new PaddingPane(this));
        classPaneMap.put(com/fr/report/cell/cellattr/highlight/RowHeightHighlightAction, new RowHeightPane(this));
        classPaneMap.put(com/fr/report/cell/cellattr/highlight/ColWidthHighlightAction, new ColumnWidthPane(this));
        classPaneMap.put(com/fr/report/cell/cellattr/highlight/PageHighlightAction, new PagePane(this));
        classPaneMap.put(com/fr/report/cell/cellattr/highlight/HyperlinkHighlightAction, new HyperlinkPane(this));
        classPaneMap.put(com/fr/report/cell/cellattr/highlight/BorderHighlightAction, new BorderHighlightPane(this));
        classPaneMap.put(com/fr/report/cell/cellattr/highlight/WidgetHighlightAction, new WidgetHighlightPane(this));
        classPaneMap.put(com/fr/report/cell/cellattr/highlight/ValueHighlightAction, new NewRealValuePane(this));
        HighlightProvider ahighlightprovider[] = ExtraDesignClassManager.getInstance().getHighlightProviders();
        HighlightProvider ahighlightprovider1[] = ahighlightprovider;
        int i = ahighlightprovider1.length;
        for(int j = 0; j < i; j++)
        {
            HighlightProvider highlightprovider = ahighlightprovider1[j];
            classPaneMap.put(highlightprovider.classForHighlightAction(), highlightprovider.appearanceForCondition(this));
        }

        useAbleActionList.clear();
        for(Iterator iterator = classPaneMap.values().iterator(); iterator.hasNext(); useAbleActionList.add(((ConditionAttrSingleConditionPane)iterator.next()).getHighLightConditionAction()));
    }

    protected String title4PopupWindow()
    {
        return "Condition";
    }

    public void populateBean(DefaultHighlight defaulthighlight)
    {
        selectedItemPane.removeAll();
        initActionList();
        int i = 0;
        for(int j = defaulthighlight.actionCount(); i < j; i++)
        {
            HighlightAction highlightaction = defaulthighlight.getHighlightAction(i);
            ConditionAttrSingleConditionPane conditionattrsingleconditionpane = (ConditionAttrSingleConditionPane)classPaneMap.get(highlightaction.getClass());
            conditionattrsingleconditionpane.populate(highlightaction);
            selectedItemPane.add(conditionattrsingleconditionpane);
            useAbleActionList.remove(conditionattrsingleconditionpane.getHighLightConditionAction());
        }

        liteConditionPane.populateBean(((Condition) (defaulthighlight.getCondition() != null ? defaulthighlight.getCondition() : ((Condition) (new ListCondition())))));
        checkConditionPane();
        updateMenuDef();
        validate();
        repaint(10L);
    }

    public DefaultHighlight updateBean()
    {
        DefaultHighlight defaulthighlight = new DefaultHighlight();
        Iterator iterator = classPaneMap.values().iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            ConditionAttrSingleConditionPane conditionattrsingleconditionpane = (ConditionAttrSingleConditionPane)iterator.next();
            if(conditionattrsingleconditionpane.getParent() == selectedItemPane)
                defaulthighlight.addHighlightAction((HighlightAction)conditionattrsingleconditionpane.update());
        } while(true);
        defaulthighlight.setCondition(liteConditionPane.updateBean());
        return defaulthighlight;
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((DefaultHighlight)obj);
    }
}
