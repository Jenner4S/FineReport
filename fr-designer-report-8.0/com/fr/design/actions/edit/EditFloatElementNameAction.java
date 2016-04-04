// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.edit;

import com.fr.design.actions.FloatSelectionAction;
import com.fr.design.dialog.*;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.Inter;
import com.fr.grid.selection.FloatSelection;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.ElementCase;
import javax.swing.SwingUtilities;

public class EditFloatElementNameAction extends FloatSelectionAction
{
    class NamePane extends BasicPane
    {

        private UITextField jtext;
        final EditFloatElementNameAction this$0;

        protected String title4PopupWindow()
        {
            return Inter.getLocText(new String[] {
                "Set", "Float_Element_Name"
            });
        }

        public void populate(String s)
        {
            jtext.setText(s);
        }

        public String update()
        {
            return jtext.getText();
        }

        public NamePane()
        {
            this$0 = EditFloatElementNameAction.this;
            super();
            jtext = new UITextField(15);
            add(jtext);
        }
    }


    public EditFloatElementNameAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setName(Inter.getLocText(new String[] {
            "Set", "Float_Element_Name"
        }));
    }

    protected boolean executeActionReturnUndoRecordNeededWithFloatSelection(FloatSelection floatselection)
    {
        final ElementCasePane reportPane = (ElementCasePane)getEditingComponent();
        final com.fr.report.elementcase.TemplateElementCase report = reportPane.getEditingElementCase();
        final FloatElement selectedFloatElement = report.getFloatElement(floatselection.getSelectedFloatName());
        final NamePane pane = new NamePane();
        pane.populate(selectedFloatElement.getName());
        BasicDialog basicdialog = pane.showSmallWindow(SwingUtilities.getWindowAncestor(reportPane), new DialogActionAdapter() {

            final NamePane val$pane;
            final ElementCase val$report;
            final FloatElement val$selectedFloatElement;
            final ElementCasePane val$reportPane;
            final EditFloatElementNameAction this$0;

            public void doOk()
            {
                String s = pane.update();
                if(report.getFloatElement(s) == null)
                    selectedFloatElement.setName(s);
                reportPane.setSelection(new FloatSelection(s));
            }

            
            {
                this$0 = EditFloatElementNameAction.this;
                pane = namepane;
                report = elementcase;
                selectedFloatElement = floatelement;
                reportPane = elementcasepane;
                super();
            }
        }
);
        basicdialog.setVisible(true);
        return true;
    }
}
