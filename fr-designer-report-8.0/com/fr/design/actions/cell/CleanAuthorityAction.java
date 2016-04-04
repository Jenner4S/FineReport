// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.cell;

import com.fr.design.actions.ElementCaseAction;
import com.fr.design.mainframe.*;
import com.fr.design.roleAuthority.*;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;
import com.fr.grid.selection.*;
import com.fr.report.cell.FloatElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

public class CleanAuthorityAction extends ElementCaseAction
{

    public CleanAuthorityAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setName(Inter.getLocText(new String[] {
            "Clear", "DashBoard-Potence"
        }));
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        Selection selection = elementcasepane.getSelection();
        String s = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        if(selection instanceof FloatSelection)
        {
            String s1 = ((FloatSelection)selection).getSelectedFloatName();
            TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
            FloatElement floatelement = templateelementcase.getFloatElement(s1);
            if(floatelement.isDoneAuthority(s))
                floatelement.cleanAuthority(s);
            doAfterAuthority(elementcasepane);
            return;
        }
        CellSelection cellselection = (CellSelection)selection;
        boolean flag = cellselection.getSelectedType() == 1 || cellselection.getSelectedType() == 2;
        if(flag && cellselection.getCellRectangleCount() == 1)
            cleanColumnRow(cellselection, elementcasepane, s);
        else
            cleanCell(cellselection, elementcasepane, s);
        doAfterAuthority(elementcasepane);
    }

    private void doAfterAuthority(ElementCasePane elementcasepane)
    {
        elementcasepane.repaint();
        elementcasepane.fireTargetModified();
        RolesAlreadyEditedPane.getInstance().refreshDockingView();
        RolesAlreadyEditedPane.getInstance().repaint();
        if(EastRegionContainerPane.getInstance().getUpPane() instanceof AuthorityPropertyPane)
        {
            AuthorityPropertyPane authoritypropertypane = (AuthorityPropertyPane)EastRegionContainerPane.getInstance().getUpPane();
            authoritypropertypane.populate();
            EastRegionContainerPane.getInstance().replaceUpPane(authoritypropertypane);
        }
    }

    private void cleanCell(CellSelection cellselection, ElementCasePane elementcasepane, String s)
    {
        if(s == null)
            return;
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        int i = cellselection.getCellRectangleCount();
        for(int j = 0; j < i; j++)
        {
            Rectangle rectangle = cellselection.getCellRectangle(j);
            for(int k = 0; k < rectangle.height; k++)
            {
                for(int l = 0; l < rectangle.width; l++)
                {
                    int i1 = l + rectangle.x;
                    int j1 = k + rectangle.y;
                    TemplateCellElement templatecellelement = templateelementcase.getTemplateCellElement(i1, j1);
                    if(templatecellelement == null)
                        continue;
                    if(templatecellelement.isDoneAuthority(s) || templatecellelement.isDoneNewValueAuthority(s))
                        templatecellelement.cleanAuthority(s);
                    if(templatecellelement.getWidget() == null)
                        continue;
                    boolean flag = templatecellelement.getWidget().isDoneVisibleAuthority(s) || templatecellelement.getWidget().isDoneUsableAuthority(s);
                    if(flag)
                        templatecellelement.getWidget().cleanAuthority(s);
                }

            }

        }

    }

    private void cleanColumnRow(CellSelection cellselection, ElementCasePane elementcasepane, String s)
    {
        if(s == null)
            return;
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        if(cellselection.getSelectedType() == 1)
        {
            for(int i = cellselection.getColumn(); i < cellselection.getColumn() + cellselection.getColumnSpan(); i++)
                templateelementcase.removeColumnPrivilegeControl(i, s);

        } else
        {
            for(int j = cellselection.getRow(); j < cellselection.getRow() + cellselection.getRowSpan(); j++)
                templateelementcase.removeRowPrivilegeControl(j, s);

        }
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        return false;
    }
}
