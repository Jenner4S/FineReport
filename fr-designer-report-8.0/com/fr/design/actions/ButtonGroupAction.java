// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions;

import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.mainframe.ElementCasePane;
import javax.swing.Icon;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.actions:
//            ElementCaseAction

public abstract class ButtonGroupAction extends ElementCaseAction
{

    protected Icon iconArray[];
    protected Integer valueArray[];
    private UIButtonGroup group;

    protected ButtonGroupAction(ElementCasePane elementcasepane, Icon aicon[], Integer ainteger[])
    {
        super(elementcasepane);
        iconArray = aicon;
        valueArray = ainteger;
    }

    protected Integer getSelectedValue()
    {
        if(getSelectedIndex() < 0 || getSelectedIndex() >= valueArray.length)
            return valueArray[0];
        else
            return valueArray[getSelectedIndex()];
    }

    private int getSelectedIndex()
    {
        return createToolBarComponent().getSelectedIndex();
    }

    protected void setSelectedIndex(int i)
    {
        int j = -1;
        int k = 0;
        do
        {
            if(k >= valueArray.length)
                break;
            if(i == valueArray[k].intValue())
            {
                j = k;
                break;
            }
            k++;
        } while(true);
        if(i == -1 && createToolBarComponent().hasClick())
        {
            j = getSelectedIndex();
            createToolBarComponent().setClickState(false);
        }
        createToolBarComponent().removeActionListener(this);
        createToolBarComponent().setSelectedIndex(j);
        createToolBarComponent().addActionListener(this);
    }

    public UIButtonGroup createToolBarComponent()
    {
        if(group == null)
        {
            group = new UIButtonGroup(iconArray, valueArray);
            group.addActionListener(this);
        }
        return group;
    }

    public volatile JComponent createToolBarComponent()
    {
        return createToolBarComponent();
    }
}
