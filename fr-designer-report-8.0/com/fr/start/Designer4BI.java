// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.start;

import com.fr.base.FRContext;
import com.fr.design.actions.file.newReport.NewWorkBookAction;
import com.fr.design.actions.report.ReportFooterAction;
import com.fr.design.actions.report.ReportHeaderAction;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.general.ComparatorUtils;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.fr.start:
//            Designer, BISplashPane, SplashPane

public class Designer4BI extends Designer
{

    public static void main(String args[])
    {
        new Designer4BI(args);
    }

    public Designer4BI(String as[])
    {
        super(as);
    }

    protected void initLanguage()
    {
        FRContext.setLanguage(2);
    }

    protected SplashPane createSplashPane()
    {
        return new BISplashPane();
    }

    public ShortCut[] createNewFileShortCuts()
    {
        ArrayList arraylist = new ArrayList();
        arraylist.add(new NewWorkBookAction());
        return (ShortCut[])arraylist.toArray(new ShortCut[arraylist.size()]);
    }

    public MenuDef[] createTemplateShortCuts(ToolBarMenuDockPlus toolbarmenudockplus)
    {
        MenuDef amenudef[] = toolbarmenudockplus.menus4Target();
        MenuDef amenudef1[] = amenudef;
        int i = amenudef1.length;
        for(int j = 0; j < i; j++)
        {
            MenuDef menudef = amenudef1[j];
            ArrayList arraylist = new ArrayList();
            int k = 0;
            for(int l = menudef.getShortCutCount(); k < l; k++)
            {
                ShortCut shortcut = menudef.getShortCut(k);
                if(!ComparatorUtils.equals(shortcut.getClass(), com/fr/design/actions/report/ReportHeaderAction) && !ComparatorUtils.equals(shortcut.getClass(), com/fr/design/actions/report/ReportFooterAction))
                    arraylist.add(shortcut);
            }

            menudef.clearShortCuts();
            k = 0;
            for(int i1 = arraylist.size(); k < i1; k++)
                menudef.addShortCut(new ShortCut[] {
                    (ShortCut)arraylist.get(k)
                });

        }

        return amenudef;
    }
}
