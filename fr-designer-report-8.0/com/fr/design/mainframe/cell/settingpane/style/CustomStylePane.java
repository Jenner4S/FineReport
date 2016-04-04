// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.cell.settingpane.style;

import com.fr.base.CellBorderStyle;
import com.fr.base.NameStyle;
import com.fr.base.Style;
import com.fr.design.actions.utils.ReportActionUtils;
import com.fr.design.dialog.MultiTabPane;
import com.fr.design.gui.ibutton.FiveButtonLayout;
import com.fr.design.gui.ibutton.UITabGroup;
import com.fr.design.gui.style.AbstractBasicStylePane;
import com.fr.design.gui.style.AlignmentPane;
import com.fr.design.gui.style.BackgroundPane;
import com.fr.design.gui.style.BorderPane;
import com.fr.design.gui.style.FRFontPane;
import com.fr.design.gui.style.FormatPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.style.BorderUtils;
import com.fr.general.Inter;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.event.ChangeListener;

public class CustomStylePane extends MultiTabPane
{

    private static final int LENGTH_FOUR = 4;
    private static final int THREE_INDEX = 3;
    private String NameArray[];
    private ElementCasePane reportPane;
    private BackgroundPane backgroundPane;

    public CustomStylePane()
    {
        backgroundPane = null;
        tabPane.setOneLineTab(false);
        tabPane.setLayout(new FiveButtonLayout());
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText(new String[] {
            "Custom", "Style"
        });
    }

    public void addTabChangeListener(ChangeListener changelistener)
    {
        if(tabPane != null)
            tabPane.addChangeListener(changelistener);
        if(backgroundPane != null)
            backgroundPane.addChangeListener(changelistener);
    }

    public void reset()
    {
        populateBean(((Style) (null)));
    }

    public void populateBean(Style style)
    {
        for(int i = 0; i < paneList.size(); i++)
            ((AbstractBasicStylePane)paneList.get(i)).populateBean(style);

    }

    public Style updateBean()
    {
        return updateStyle(ReportActionUtils.getCurrentStyle(reportPane));
    }

    public Style updateStyle(Style style)
    {
        return ((AbstractBasicStylePane)paneList.get(tabPane.getSelectedIndex())).update(style);
    }

    public boolean isBorderPaneSelected()
    {
        return tabPane.getSelectedIndex() == 3;
    }

    public void dealWithBorder(ElementCasePane elementcasepane)
    {
        reportPane = elementcasepane;
        Object aobj[] = BorderUtils.createCellBorderObject(reportPane);
        if(aobj != null && aobj.length % 4 == 0)
            if(aobj.length == 4)
                ((BorderPane)paneList.get(3)).populateBean((CellBorderStyle)aobj[0], ((Boolean)aobj[1]).booleanValue(), ((Integer)aobj[2]).intValue(), (Color)aobj[3]);
            else
                ((BorderPane)paneList.get(3)).populateBean(new CellBorderStyle(), Boolean.TRUE.booleanValue(), 0, (Color)aobj[3]);
    }

    public void updateBorder()
    {
        BorderUtils.update(reportPane, ((BorderPane)paneList.get(3)).update());
    }

    public boolean accept(Object obj)
    {
        return (obj instanceof Style) && !(obj instanceof NameStyle);
    }

    protected java.util.List initPaneList()
    {
        paneList = new ArrayList();
        paneList.add(new FormatPane());
        paneList.add(new AlignmentPane());
        paneList.add(new FRFontPane());
        paneList.add(new BorderPane());
        paneList.add(backgroundPane = new BackgroundPane());
        return paneList;
    }

    public void updateBean(Style style)
    {
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((Style)obj);
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((Style)obj);
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }
}
