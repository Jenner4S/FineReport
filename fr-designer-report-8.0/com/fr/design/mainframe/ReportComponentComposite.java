// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.FRContext;
import com.fr.design.designer.EditingState;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.gui.icontainer.UIModeControlContainer;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.grid.Grid;
import com.fr.main.impl.WorkBook;
import com.fr.report.report.TemplateReport;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

// Referenced classes of package com.fr.design.mainframe:
//            ReportComponentCardPane, WorkSheetDesigner, JWorkBook, SheetNameTabPane, 
//            ReportComponent, ElementCasePane, DesignerContext, DesignerFrame

public class ReportComponentComposite extends JComponent
{

    private JWorkBook parent;
    private UIModeControlContainer parentContainer;
    protected ReportComponentCardPane centerCardPane;
    private JPanel CellElementRegion;
    private List templateStateList;
    private SheetNameTabPane sheetNameTab;
    private JPanel hbarContainer;

    public ReportComponentComposite(JWorkBook jworkbook)
    {
        parentContainer = null;
        templateStateList = new ArrayList();
        parent = jworkbook;
        setLayout(FRGUIPaneFactory.createBorderLayout());
        add(centerCardPane = new ReportComponentCardPane(), "Center");
        sheetNameTab = jworkbook.createSheetNameTabPane(this);
        sheetNameTab.setSelectedIndex(0);
        CellElementRegion = FRGUIPaneFactory.createBorderLayout_S_Pane();
        add(CellElementRegion, "North");
        add(createSouthControlPane(), "South");
    }

    protected void doBeforeChange(int i)
    {
        if(i >= 0)
            templateStateList.set(i, centerCardPane.editingComponet.createEditingState());
    }

    protected void doAfterChange(int i)
    {
        WorkBook workbook = getEditingWorkBook();
        if(workbook == null)
        {
            FRContext.getLogger().error((new StringBuilder()).append(Inter.getLocText("Read_failure")).append("!").toString());
            return;
        }
        centerCardPane.populate(workbook.getTemplateReport(i));
        if(parentContainer != null)
            parentContainer.setDownPane(this);
        if(templateStateList.size() > i)
        {
            EditingState editingstate = (EditingState)templateStateList.get(i);
            if(editingstate != null)
                editingstate.revert();
        } else
        {
            for(; templateStateList.size() <= i; templateStateList.add(null));
            centerCardPane.editingComponet.setSelection(centerCardPane.editingComponet.getDefaultSelectElement());
        }
        if(centerCardPane.editingComponet.elementCasePane == null)
            return;
        Grid grid = centerCardPane.editingComponet.elementCasePane.getGrid();
        if(!grid.hasFocus() && grid.isRequestFocusEnabled())
            grid.requestFocus();
    }

    public void removeSelection()
    {
        if(centerCardPane.editingComponet instanceof WorkSheetDesigner)
            ((WorkSheetDesigner)centerCardPane.editingComponet).removeSelection();
        else
            centerCardPane.populate(centerCardPane.editingComponet.getTemplateReport());
    }

    public TemplateReport getEditingTemplateReport()
    {
        return centerCardPane.editingComponet.getTemplateReport();
    }

    public int getEditingIndex()
    {
        return sheetNameTab.getSelectedIndex();
    }

    public void setParentContainer(UIModeControlContainer uimodecontrolcontainer)
    {
        parentContainer = uimodecontrolcontainer;
    }

    public void setComponents()
    {
        CellElementRegion.removeAll();
        hbarContainer.removeAll();
        hbarContainer.add(centerCardPane.editingComponet.getHorizontalScrollBar());
        centerCardPane.editingComponet.getHorizontalScrollBar().setValue(centerCardPane.editingComponet.getHorizontalScrollBar().getValue());
        centerCardPane.editingComponet.getVerticalScrollBar().setValue(centerCardPane.editingComponet.getVerticalScrollBar().getValue());
        doLayout();
    }

    public int getSelectedIndex()
    {
        return sheetNameTab.getSelectedIndex();
    }

    protected ReportComponent getEditingReportComponent()
    {
        return centerCardPane.editingComponet;
    }

    protected WorkBook getEditingWorkBook()
    {
        return (WorkBook)parent.getTarget();
    }

    public void addTargetModifiedListener(TargetModifiedListener targetmodifiedlistener)
    {
        centerCardPane.addTargetModifiedListener(targetmodifiedlistener);
    }

    private JComponent createSouthControlPane()
    {
        hbarContainer = FRGUIPaneFactory.createBorderLayout_S_Pane();
        hbarContainer.add(centerCardPane.editingComponet.getHorizontalScrollBar());
        JSplitPane jsplitpane = new JSplitPane(1, sheetNameTab, hbarContainer);
        jsplitpane.setBorder(null);
        jsplitpane.setDividerSize(3);
        jsplitpane.setResizeWeight(0.59999999999999998D);
        return jsplitpane;
    }

    public void setSelectedIndex(int i)
    {
        sheetNameTab.setSelectedIndex(i);
        centerCardPane.populate(getEditingWorkBook().getTemplateReport(i));
    }

    public void stopEditing()
    {
        centerCardPane.stopEditing();
    }

    public void setComposite()
    {
        DesignerContext.getDesignerFrame().resetToolkitByPlus(parent);
        parent.setComposite();
        validate();
        repaint(40L);
    }

    public void fireTargetModified()
    {
        parent.fireTargetModified();
    }
}
