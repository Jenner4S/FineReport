// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report;

import com.fr.base.Formula;
import com.fr.data.Verifier;
import com.fr.design.gui.itableeditorpane.UIArrayTableModel;
import com.fr.design.gui.itableeditorpane.UITableEditorPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.stable.bridge.StableFactory;
import com.fr.write.ReportWriteAttrProvider;
import com.fr.write.ValueVerifierProvider;
import java.util.List;
import javax.swing.JPanel;

public class ValueVerifierEditPane extends JPanel
{

    private UITableEditorPane tableEditorPane;
    private final String columnNames[] = {
        Inter.getLocText(new String[] {
            "Verify-Verify_Formula", "Verify-ToolTips"
        }, new String[] {
            "(", ")"
        }), Inter.getLocText("Verify-Error_Information")
    };

    public ValueVerifierEditPane()
    {
        setLayout(FRGUIPaneFactory.createM_BorderLayout());
        tableEditorPane = new UITableEditorPane(new UIArrayTableModel(columnNames, new int[] {
            1, 2, 3, 4
        }));
        add(tableEditorPane, "Center");
    }

    public void populate(ReportWriteAttrProvider reportwriteattrprovider)
    {
        if(reportwriteattrprovider == null)
            return;
        int i = reportwriteattrprovider.getVerifierCount();
        Object aobj[][] = new Object[reportwriteattrprovider.getValueVerifierCount()][];
        int j = 0;
        for(int k = 0; k < i; k++)
        {
            if(!(reportwriteattrprovider.getVerifier(k) instanceof ValueVerifierProvider))
                continue;
            Formula formula = ((ValueVerifierProvider)reportwriteattrprovider.getVerifier(k)).getFormula();
            if(formula != null)
            {
                String s = formula.getContent().substring(1);
                String s1 = reportwriteattrprovider.getVerifier(k).getMessage();
                aobj[j++] = (new Object[] {
                    s, s1
                });
            }
        }

        tableEditorPane.populate(((Object []) (aobj)));
    }

    public void update(ReportWriteAttrProvider reportwriteattrprovider, String s)
    {
        List list = tableEditorPane.update();
        reportwriteattrprovider.clearVerifiers(true);
        for(int i = 0; i < list.size(); i++)
        {
            Object aobj[] = (Object[])list.get(i);
            if(aobj != null && aobj[0] != null)
            {
                ValueVerifierProvider valueverifierprovider = (ValueVerifierProvider)StableFactory.getMarkedInstanceObjectFromClass("ValueVerifier", com/fr/write/ValueVerifierProvider);
                valueverifierprovider.setFormula(new Formula(readValueVerifyObject(aobj[0])));
                valueverifierprovider.setMessage(readValueVerifyObject(aobj[1]));
                reportwriteattrprovider.addVerifier(s, (Verifier)valueverifierprovider);
            }
        }

    }

    private String readValueVerifyObject(Object obj)
    {
        if(obj == null)
            return null;
        else
            return obj.toString();
    }
}
