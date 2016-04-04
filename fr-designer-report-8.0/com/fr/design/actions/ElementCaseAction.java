// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions;

import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.selection.SelectionEvent;
import com.fr.design.selection.SelectionListener;
import com.fr.grid.selection.CellSelection;

// Referenced classes of package com.fr.design.actions:
//            TemplateComponentAction

public abstract class ElementCaseAction extends TemplateComponentAction
{

    protected ElementCaseAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        elementcasepane.addSelectionChangeListener(new SelectionListener() {

            final ElementCaseAction this$0;

            public void selectionChanged(SelectionEvent selectionevent)
            {
                update();
                if(DesignerContext.getFormatState() != 0)
                {
                    com.fr.grid.selection.Selection selection = ((ElementCasePane)getEditingComponent()).getSelection();
                    if(selection instanceof CellSelection)
                    {
                        CellSelection cellselection = (CellSelection)selection;
                        ((ElementCasePane)getEditingComponent()).setCellNeedTOFormat(cellselection);
                    }
                }
            }

            
            {
                this$0 = ElementCaseAction.this;
                super();
            }
        }
);
    }
}
