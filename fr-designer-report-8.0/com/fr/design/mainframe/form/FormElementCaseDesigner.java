// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.form;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.actions.*;
import com.fr.design.designer.EditingState;
import com.fr.design.designer.TargetComponent;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.*;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.*;
import com.fr.design.selection.*;
import com.fr.form.FormElementCaseProvider;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.CellElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.worksheet.FormElementCase;
import com.fr.report.worksheet.WorkSheet;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

// Referenced classes of package com.fr.design.mainframe.form:
//            FormElementCasePaneDelegate, FormECDesignerProvider

public class FormElementCaseDesigner extends TargetComponent
    implements Selectedable, FormECDesignerProvider
{

    protected FormElementCasePaneDelegate elementCasePane;

    public FormElementCasePaneDelegate getEditingElementCasePane()
    {
        return elementCasePane;
    }

    public FormElementCaseDesigner(FormElementCaseProvider formelementcaseprovider)
    {
        super(formelementcaseprovider);
        setLayout(FRGUIPaneFactory.createBorderLayout());
        elementCasePane = new FormElementCasePaneDelegate((FormElementCase)formelementcaseprovider);
        elementCasePane.setSelection(getDefaultSelectElement());
        add(elementCasePane, "Center");
        elementCasePane.addTargetModifiedListener(new TargetModifiedListener() {

            final FormElementCaseDesigner this$0;

            public void targetModified(TargetModifiedEvent targetmodifiedevent)
            {
                fireTargetModified();
            }

            
            {
                this$0 = FormElementCaseDesigner.this;
                super();
            }
        }
);
    }

    public void setTarget(FormElementCaseProvider formelementcaseprovider)
    {
        super.setTarget(formelementcaseprovider);
        elementCasePane.setTarget((FormElementCase)formelementcaseprovider);
    }

    public int getMenuState()
    {
        return 0;
    }

    public ShortCut[] shortCuts4Authority()
    {
        return (new ShortCut[] {
            new NameSeparator(Inter.getLocText(new String[] {
                "DashBoard-Potence", "Edit"
            })), BaseUtils.isAuthorityEditing() ? new ExitAuthorityEditAction(this) : new AllowAuthorityEditAction(this)
        });
    }

    public AuthorityEditPane createAuthorityEditPane()
    {
        ElementCasePaneAuthorityEditPane elementcasepaneauthorityeditpane = new ElementCasePaneAuthorityEditPane(elementCasePane);
        elementcasepaneauthorityeditpane.populateDetials();
        return elementcasepaneauthorityeditpane;
    }

    public BufferedImage getElementCaseImage(Dimension dimension)
    {
        BufferedImage bufferedimage = null;
        try
        {
            bufferedimage = new BufferedImage(dimension.width, dimension.height, 1);
            Graphics g = bufferedimage.getGraphics();
            Color color = g.getColor();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, dimension.width, dimension.height);
            g.setColor(color);
            elementCasePane.paintComponents(g);
        }
        catch(Exception exception)
        {
            FRContext.getLogger().error(exception.getMessage());
        }
        return bufferedimage;
    }

    public EditingState createEditingState()
    {
        return elementCasePane.createEditingState();
    }

    public void copy()
    {
        elementCasePane.copy();
    }

    public boolean paste()
    {
        return elementCasePane.paste();
    }

    public boolean cut()
    {
        return elementCasePane.cut();
    }

    public void stopEditing()
    {
        elementCasePane.stopEditing();
    }

    public ToolBarDef[] toolbars4Target()
    {
        return elementCasePane.toolbars4Target();
    }

    public JComponent[] toolBarButton4Form()
    {
        return elementCasePane.toolBarButton4Form();
    }

    public MenuDef[] menus4Target()
    {
        return elementCasePane.menus4Target();
    }

    public void requestFocus()
    {
        super.requestFocus();
        elementCasePane.requestFocus();
    }

    public JScrollBar getHorizontalScrollBar()
    {
        return elementCasePane.getHorizontalScrollBar();
    }

    public JScrollBar getVerticalScrollBar()
    {
        return elementCasePane.getVerticalScrollBar();
    }

    public JPanel getEastUpPane()
    {
        return elementCasePane.getEastUpPane();
    }

    public JPanel getEastDownPane()
    {
        return elementCasePane.getEastDownPane();
    }

    public SelectableElement getSelection()
    {
        return elementCasePane.getSelection();
    }

    public void setSelection(SelectableElement selectableelement)
    {
        if(selectableelement == null)
            selectableelement = new CellSelection();
        elementCasePane.setSelection((Selection)selectableelement);
    }

    public void removeSelection()
    {
        TemplateElementCase templateelementcase = elementCasePane.getEditingElementCase();
        if(templateelementcase instanceof WorkSheet)
            ((WorkSheet)templateelementcase).setPaintSelection(false);
        elementCasePane.repaint();
    }

    public Selection getDefaultSelectElement()
    {
        TemplateElementCase templateelementcase = elementCasePane.getEditingElementCase();
        CellElement cellelement = templateelementcase.getCellElement(0, 0);
        return cellelement != null ? new CellSelection(0, 0, cellelement.getColumnSpan(), cellelement.getRowSpan()) : new CellSelection();
    }

    public void addSelectionChangeListener(SelectionListener selectionlistener)
    {
        elementCasePane.addSelectionChangeListener(selectionlistener);
    }

    public void removeSelectionChangeListener(SelectionListener selectionlistener)
    {
        elementCasePane.removeSelectionChangeListener(selectionlistener);
    }

    public ToolBarMenuDockPlus getToolBarMenuDockPlus()
    {
        return new JWorkBook();
    }

    public void cancelFormat()
    {
    }

    public FormElementCase getElementCase()
    {
        return (FormElementCase)getTarget();
    }

    public ShortCut[] shortcut4TemplateMenu()
    {
        return new ShortCut[0];
    }

    public FormElementCaseProvider getEditingElementCase()
    {
        return (FormElementCaseProvider)getEditingElementCasePane().getTarget();
    }

    public volatile void setTarget(Object obj)
    {
        setTarget((FormElementCaseProvider)obj);
    }

    public volatile JComponent getEastUpPane()
    {
        return getEastUpPane();
    }

    public volatile JComponent getEastDownPane()
    {
        return getEastDownPane();
    }
}
