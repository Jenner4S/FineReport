// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.cell.settingpane.style;

import com.fr.base.Style;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeListener;

// Referenced classes of package com.fr.design.mainframe.cell.settingpane.style:
//            CustomStylePane, PredefinedStylePane

public class StylePane extends UIComboBoxPane
{

    private CustomStylePane customStylePane;
    private PredefinedStylePane predefinedStylePane;

    public StylePane()
    {
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("Style");
    }

    protected void comboBoxItemStateChanged()
    {
        if(jcb.getSelectedIndex() == 0 && predefinedStylePane.updateBean() != null)
            customStylePane.populateBean(predefinedStylePane.updateBean());
        else
            predefinedStylePane.populateBean(null);
    }

    public void addPredefinedChangeListener(ChangeListener changelistener)
    {
        predefinedStylePane.addChangeListener(changelistener);
    }

    public void addCustomTabChangeListener(ChangeListener changelistener)
    {
        customStylePane.addTabChangeListener(changelistener);
    }

    public void updateBorder()
    {
        if(getSelectedIndex() == 0 && customStylePane.isBorderPaneSelected())
            customStylePane.updateBorder();
    }

    public void dealWithBorder(ElementCasePane elementcasepane)
    {
        if(getSelectedIndex() == 0)
            customStylePane.dealWithBorder(elementcasepane);
    }

    public void setSelctedByName(String s)
    {
        jcb.setSelectedIndex(ComparatorUtils.equals(Inter.getLocText("Custom"), s) ? 0 : 1);
    }

    public Style updateStyle(Style style)
    {
        return customStylePane.updateStyle(style);
    }

    protected List initPaneList()
    {
        ArrayList arraylist = new ArrayList();
        arraylist.add(customStylePane = new CustomStylePane());
        arraylist.add(predefinedStylePane = new PredefinedStylePane());
        return arraylist;
    }
}
