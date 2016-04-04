// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.quickeditor.floatquick;

import com.fr.base.*;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.quickeditor.FloatQuickEditor;
import com.fr.report.ReportHelper;
import com.fr.report.cell.FloatElement;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public class FloatStringQuickEditor extends FloatQuickEditor
{

    private UITextField stringTextField;
    private boolean reserveInResult;
    private boolean reserveOnWriteOrAnaly;
    DocumentListener documentListener;

    public FloatStringQuickEditor()
    {
        reserveInResult = false;
        reserveOnWriteOrAnaly = true;
        documentListener = new DocumentListener() {

            final FloatStringQuickEditor this$0;

            public void insertUpdate(DocumentEvent documentevent)
            {
                changeReportPaneCell(stringTextField.getText().trim());
            }

            public void removeUpdate(DocumentEvent documentevent)
            {
                changeReportPaneCell(stringTextField.getText().trim());
            }

            public void changedUpdate(DocumentEvent documentevent)
            {
                changeReportPaneCell(stringTextField.getText().trim());
            }

            
            {
                this$0 = FloatStringQuickEditor.this;
                super();
            }
        }
;
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d1
        };
        double ad1[] = {
            d
        };
        stringTextField = new UITextField();
        Component acomponent[][] = {
            {
                stringTextField
            }
        };
        javax.swing.JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(jpanel, "Center");
    }

    protected void refreshDetails()
    {
        String s = null;
        Object obj = floatElement.getValue();
        if(obj == null)
            s = "";
        else
        if(obj instanceof Formula)
        {
            Formula formula = (Formula)obj;
            s = formula.getContent();
            reserveInResult = formula.isReserveInResult();
            reserveOnWriteOrAnaly = formula.isReserveOnWriteOrAnaly();
        } else
        {
            s = obj.toString();
        }
        showText(s);
    }

    public void showText(String s)
    {
        stringTextField.getDocument().removeDocumentListener(documentListener);
        stringTextField.setText(s);
        stringTextField.getDocument().addDocumentListener(documentListener);
    }

    protected void changeReportPaneCell(String s)
    {
        if(s != null && s.length() > 0 && s.charAt(0) == '=')
        {
            Formula formula = new Formula(s);
            formula.setReserveInResult(reserveInResult);
            formula.setReserveOnWriteOrAnaly(reserveOnWriteOrAnaly);
            floatElement.setValue(formula);
        } else
        {
            Style style = floatElement.getStyle();
            if(floatElement != null && style != null && style.getFormat() != null && style.getFormat() == TextFormat.getInstance())
                floatElement.setValue(s);
            else
                floatElement.setValue(ReportHelper.convertGeneralStringAccordingToExcel(s));
        }
        fireTargetModified();
        stringTextField.requestFocus();
    }

}
