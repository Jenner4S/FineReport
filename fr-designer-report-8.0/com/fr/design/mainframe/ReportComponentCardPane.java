// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import com.fr.design.roleAuthority.RoleTree;
import com.fr.poly.PolyDesigner;
import com.fr.privilege.finegrain.WorkSheetPrivilegeControl;
import com.fr.report.poly.PolyWorkSheet;
import com.fr.report.report.TemplateReport;
import com.fr.report.worksheet.WorkSheet;
import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.mainframe:
//            WorkSheetDesigner, ReportComponent, JTemplate

public class ReportComponentCardPane extends JComponent
    implements TargetModifiedListener
{

    protected ReportComponent editingComponet;
    private CardLayout cl;
    private WorkSheetDesigner sheetDezi;
    private PolyDesigner polyDezi;
    private java.util.List targetModifiedList;

    public ReportComponentCardPane()
    {
        targetModifiedList = new ArrayList();
        setLayout(cl = new CardLayout());
    }

    public void requestGrifFocus()
    {
        if(sheetDezi != null)
            sheetDezi.requestFocus();
    }

    protected void stopEditing()
    {
        if(editingComponet != null)
            editingComponet.stopEditing();
    }

    protected boolean cut()
    {
        if(editingComponet != null)
            return editingComponet.cut();
        else
            return false;
    }

    protected void copy()
    {
        if(editingComponet != null)
            editingComponet.copy();
    }

    protected boolean paste()
    {
        if(editingComponet != null)
            return editingComponet.paste();
        else
            return false;
    }

    public void addTargetModifiedListener(TargetModifiedListener targetmodifiedlistener)
    {
        targetModifiedList.add(targetmodifiedlistener);
    }

    public void targetModified(TargetModifiedEvent targetmodifiedevent)
    {
        TargetModifiedListener targetmodifiedlistener;
        for(Iterator iterator = targetModifiedList.iterator(); iterator.hasNext(); targetmodifiedlistener.targetModified(targetmodifiedevent))
            targetmodifiedlistener = (TargetModifiedListener)iterator.next();

    }

    protected void showJWorkSheet(WorkSheet worksheet)
    {
        if(sheetDezi == null)
        {
            sheetDezi = new WorkSheetDesigner(worksheet);
            add(sheetDezi, "WS");
            sheetDezi.addTargetModifiedListener(this);
        } else
        {
            sheetDezi.setTarget(worksheet);
        }
        cl.show(this, "WS");
        editingComponet = sheetDezi;
    }

    protected void showPoly(PolyWorkSheet polyworksheet)
    {
        if(polyDezi == null)
        {
            polyDezi = new PolyDesigner(polyworksheet);
            add(polyDezi, "PL");
            polyDezi.addTargetModifiedListener(this);
        } else
        {
            polyDezi.setTarget(polyworksheet);
        }
        cl.show(this, "PL");
        editingComponet = polyDezi;
    }

    protected void populate(TemplateReport templatereport)
    {
        if(templatereport instanceof WorkSheet)
            showJWorkSheet((WorkSheet)templatereport);
        else
        if(templatereport instanceof PolyWorkSheet)
            showPoly((PolyWorkSheet)templatereport);
        if(BaseUtils.isAuthorityEditing())
        {
            JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
            String s = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
            jtemplate.setSheetCovered(templatereport.getWorkSheetPrivilegeControl().checkInvisible(s));
        }
    }

    protected TemplateReport update()
    {
        if(polyDezi != null && polyDezi.isVisible())
            return (TemplateReport)polyDezi.getTarget();
        if(sheetDezi != null && sheetDezi.isVisible())
            return (TemplateReport)sheetDezi.getTarget();
        else
            return null;
    }
}
