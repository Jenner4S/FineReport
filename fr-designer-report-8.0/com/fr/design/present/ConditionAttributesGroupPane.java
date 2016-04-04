// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.present;

import com.fr.base.FRContext;
import com.fr.design.condition.HighLightConditionAttributesPane;
import com.fr.design.gui.controlpane.*;
import com.fr.general.*;
import com.fr.report.cell.cellattr.highlight.*;
import java.util.ArrayList;
import java.util.List;

public class ConditionAttributesGroupPane extends JControlPane
{

    public ConditionAttributesGroupPane()
    {
    }

    public NameableCreator[] createNameableCreators()
    {
        return (new NameableCreator[] {
            new NameObjectCreator(Inter.getLocText("Condition_Attributes"), com/fr/report/cell/cellattr/highlight/DefaultHighlight, com/fr/design/condition/HighLightConditionAttributesPane)
        });
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("Condition_Attributes");
    }

    public void populate(HighlightGroup highlightgroup)
    {
        if(highlightgroup == null || highlightgroup.size() <= 0)
            return;
        ArrayList arraylist = new ArrayList();
        for(int i = 0; i < highlightgroup.size(); i++)
            arraylist.add(new NameObject(highlightgroup.getHighlight(i).getName(), highlightgroup.getHighlight(i)));

        populate((com.fr.stable.Nameable[])arraylist.toArray(new NameObject[arraylist.size()]));
    }

    public HighlightGroup updateHighlightGroup()
    {
        com.fr.stable.Nameable anameable[] = update();
        Highlight ahighlight[] = new Highlight[anameable.length];
        for(int i = 0; i < anameable.length; i++)
        {
            Highlight highlight = (Highlight)((NameObject)anameable[i]).getObject();
            highlight.setName(((NameObject)anameable[i]).getName());
            try
            {
                highlight = (Highlight)highlight.clone();
            }
            catch(CloneNotSupportedException clonenotsupportedexception)
            {
                FRContext.getLogger().error(clonenotsupportedexception.getMessage(), clonenotsupportedexception);
            }
            ahighlight[i] = highlight;
        }

        return new HighlightGroup(ahighlight);
    }
}
