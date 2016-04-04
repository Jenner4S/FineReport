// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.design.actions.report.*;
import com.fr.design.designer.EditingState;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.menu.*;
import com.fr.design.selection.SelectableElement;
import com.fr.design.selection.SelectionListener;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.CellElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.ArrayUtils;
import javax.swing.*;

// Referenced classes of package com.fr.design.mainframe:
//            ReportComponent, ElementCasePaneDelegate, ElementCasePaneAuthorityEditPane, AuthorityEditPane

public class WorkSheetDesigner extends ReportComponent
{

    public WorkSheetDesigner(WorkSheet worksheet)
    {
        super(worksheet);
        setLayout(FRGUIPaneFactory.createBorderLayout());
        elementCasePane = new ElementCasePaneDelegate(worksheet);
        add(elementCasePane, "Center");
        ((ElementCasePaneDelegate)elementCasePane).addTargetModifiedListener(new TargetModifiedListener() {

            final WorkSheetDesigner this$0;

            public void targetModified(TargetModifiedEvent targetmodifiedevent)
            {
                fireTargetModified();
            }

            
            {
                this$0 = WorkSheetDesigner.this;
                super();
            }
        }
);
    }

    public void setTarget(WorkSheet worksheet)
    {
        super.setTarget(worksheet);
        ((ElementCasePaneDelegate)elementCasePane).setTarget(worksheet);
    }

    public int getMenuState()
    {
        return 0;
    }

    public AuthorityEditPane createAuthorityEditPane()
    {
        ElementCasePaneAuthorityEditPane elementcasepaneauthorityeditpane = new ElementCasePaneAuthorityEditPane(elementCasePane);
        elementcasepaneauthorityeditpane.populateDetials();
        return elementcasepaneauthorityeditpane;
    }

    public EditingState createEditingState()
    {
        return ((ElementCasePaneDelegate)elementCasePane).createEditingState();
    }

    public void copy()
    {
        ((ElementCasePaneDelegate)elementCasePane).copy();
    }

    public boolean paste()
    {
        return ((ElementCasePaneDelegate)elementCasePane).paste();
    }

    public boolean cut()
    {
        return ((ElementCasePaneDelegate)elementCasePane).cut();
    }

    public void stopEditing()
    {
        ((ElementCasePaneDelegate)elementCasePane).stopEditing();
    }

    public ToolBarDef[] toolbars4Target()
    {
        return ((ElementCasePaneDelegate)elementCasePane).toolbars4Target();
    }

    public JComponent[] toolBarButton4Form()
    {
        return ((ElementCasePaneDelegate)elementCasePane).toolBarButton4Form();
    }

    public ShortCut[] shortcut4TemplateMenu()
    {
        return (ShortCut[])(ShortCut[])ArrayUtils.addAll(super.shortcut4TemplateMenu(), new ShortCut[] {
            new DottedSeparator(), new ReportWriteAttrAction(this), new ReportColumnsAction(this), new ReportPageAttrAction(this), new ReportEngineAttrAction(this)
        });
    }

    public MenuDef[] menus4Target()
    {
        return ((ElementCasePaneDelegate)elementCasePane).menus4Target();
    }

    public void requestFocus()
    {
        super.requestFocus();
        ((ElementCasePaneDelegate)elementCasePane).requestFocus();
    }

    public JScrollBar getHorizontalScrollBar()
    {
        return ((ElementCasePaneDelegate)elementCasePane).getHorizontalScrollBar();
    }

    public JScrollBar getVerticalScrollBar()
    {
        return ((ElementCasePaneDelegate)elementCasePane).getVerticalScrollBar();
    }

    public JPanel getEastUpPane()
    {
        return ((ElementCasePaneDelegate)elementCasePane).getEastUpPane();
    }

    public JPanel getEastDownPane()
    {
        return ((ElementCasePaneDelegate)elementCasePane).getEastDownPane();
    }

    public Selection getSelection()
    {
        return ((ElementCasePaneDelegate)elementCasePane).getSelection();
    }

    public void setSelection(Selection selection)
    {
        if(selection == null)
            selection = new CellSelection();
        ((ElementCasePaneDelegate)elementCasePane).setSelection(selection);
    }

    public void removeSelection()
    {
        TemplateElementCase templateelementcase = ((ElementCasePaneDelegate)elementCasePane).getEditingElementCase();
        if(templateelementcase instanceof WorkSheet)
            ((WorkSheet)templateelementcase).setPaintSelection(false);
        ((ElementCasePaneDelegate)elementCasePane).repaint();
    }

    public Selection getDefaultSelectElement()
    {
        TemplateElementCase templateelementcase = ((ElementCasePaneDelegate)elementCasePane).getEditingElementCase();
        CellElement cellelement = templateelementcase.getCellElement(0, 0);
        return cellelement != null ? new CellSelection(0, 0, cellelement.getColumnSpan(), cellelement.getRowSpan()) : new CellSelection();
    }

    public void addSelectionChangeListener(SelectionListener selectionlistener)
    {
        ((ElementCasePaneDelegate)elementCasePane).addSelectionChangeListener(selectionlistener);
    }

    public void removeSelectionChangeListener(SelectionListener selectionlistener)
    {
        ((ElementCasePaneDelegate)elementCasePane).removeSelectionChangeListener(selectionlistener);
    }

    public volatile SelectableElement getDefaultSelectElement()
    {
        return getDefaultSelectElement();
    }

    public volatile void setSelection(SelectableElement selectableelement)
    {
        setSelection((Selection)selectableelement);
    }

    public volatile SelectableElement getSelection()
    {
        return getSelection();
    }

    public volatile void setTarget(Object obj)
    {
        setTarget((WorkSheet)obj);
    }
}
