// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.form;

import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.*;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.form.FormElementCaseContainerProvider;
import com.fr.form.FormElementCaseProvider;
import com.fr.report.worksheet.FormElementCase;
import java.util.*;
import javax.swing.*;

// Referenced classes of package com.fr.design.mainframe.form:
//            FormTabPane, FormECCompositeProvider, FormElementCaseDesigner

public class FormReportComponentComposite extends JComponent
    implements TargetModifiedListener, FormECCompositeProvider
{

    private FormElementCaseDesigner elementCaseDesigner;
    private BaseJForm jForm;
    private FormTabPane sheetNameTab;
    private JPanel hbarContainer;
    private List targetModifiedList;

    public FormReportComponentComposite(BaseJForm basejform, FormElementCaseDesigner formelementcasedesigner, FormElementCaseContainerProvider formelementcasecontainerprovider)
    {
        targetModifiedList = new ArrayList();
        jForm = basejform;
        setLayout(FRGUIPaneFactory.createBorderLayout());
        elementCaseDesigner = formelementcasedesigner;
        add(elementCaseDesigner, "Center");
        sheetNameTab = new FormTabPane(formelementcasecontainerprovider, basejform);
        add(createSouthControlPane(), "South");
        elementCaseDesigner.addTargetModifiedListener(this);
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

    public void setEditingElementCase(FormElementCase formelementcase)
    {
        elementCaseDesigner.setTarget(formelementcase);
        fireTargetModified();
    }

    private JComponent createSouthControlPane()
    {
        hbarContainer = FRGUIPaneFactory.createBorderLayout_S_Pane();
        hbarContainer.add(elementCaseDesigner.getHorizontalScrollBar());
        JSplitPane jsplitpane = new JSplitPane(1, sheetNameTab, hbarContainer);
        jsplitpane.setBorder(null);
        jsplitpane.setDividerSize(3);
        jsplitpane.setResizeWeight(0.59999999999999998D);
        return jsplitpane;
    }

    public void stopEditing()
    {
        elementCaseDesigner.stopEditing();
    }

    public void setComposite()
    {
        DesignerContext.getDesignerFrame().resetToolkitByPlus((ToolBarMenuDockPlus)jForm);
        validate();
        repaint(40L);
    }

    public void setSelectedWidget(FormElementCaseProvider formelementcaseprovider)
    {
        if(formelementcaseprovider != null)
            elementCaseDesigner.setTarget(formelementcaseprovider);
    }

    public void fireTargetModified()
    {
        jForm.fireTargetModified();
    }
}
