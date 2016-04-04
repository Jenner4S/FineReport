// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.actions.*;
import com.fr.design.actions.report.*;
import com.fr.design.designer.TargetComponent;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.NameSeparator;
import com.fr.design.menu.ShortCut;
import com.fr.design.selection.SelectableElement;
import com.fr.design.selection.Selectedable;
import com.fr.general.Inter;
import com.fr.report.report.TemplateReport;
import javax.swing.JScrollBar;

// Referenced classes of package com.fr.design.mainframe:
//            JWorkBook, ElementCasePane

public abstract class ReportComponent extends TargetComponent
    implements Selectedable
{

    protected ElementCasePane elementCasePane;

    public ElementCasePane getEditingElementCasePane()
    {
        return elementCasePane;
    }

    public ReportComponent(TemplateReport templatereport)
    {
        super(templatereport);
    }

    public TemplateReport getTemplateReport()
    {
        return (TemplateReport)getTarget();
    }

    public abstract JScrollBar getHorizontalScrollBar();

    public abstract JScrollBar getVerticalScrollBar();

    public abstract SelectableElement getDefaultSelectElement();

    public ShortCut[] shortcut4TemplateMenu()
    {
        return (new ShortCut[] {
            new ReportPageSetupAction(this), new ReportHeaderAction(this), new ReportFooterAction(this), new ReportBackgroundAction(this)
        });
    }

    public void cancelFormat()
    {
    }

    public ShortCut[] shortCuts4Authority()
    {
        return (new ShortCut[] {
            new NameSeparator(Inter.getLocText(new String[] {
                "DashBoard-Potence", "Edit"
            })), BaseUtils.isAuthorityEditing() ? new ExitAuthorityEditAction(this) : new AllowAuthorityEditAction(this)
        });
    }

    public ToolBarMenuDockPlus getToolBarMenuDockPlus()
    {
        return new JWorkBook();
    }
}
