// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.form.javascript;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.javascript.EmailPane;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

public class FormEmailPane extends EmailPane
{

    public FormEmailPane()
    {
    }

    protected void initCenterPane(UILabel uilabel, JScrollPane jscrollpane, double d, double d1)
    {
        double ad[] = {
            d1, d1, d1, d1, d1, d, d1
        };
        double ad1[] = {
            d1, d
        };
        centerPane = TableLayoutHelper.createCommonTableLayoutPane(new JComponent[][] {
            new JComponent[] {
                new UILabel(), tipsPane1
            }, createLinePane(Inter.getLocText("HJS-Mail_to"), maitoEditor = new UITextField()), createLinePane(Inter.getLocText("HJS-CC_to"), ccEditor = new UITextField()), createLinePane(Inter.getLocText("EmailPane-BCC"), bccEditor = new UITextField()), createLinePane(Inter.getLocText("EmailPane-mailSubject"), titleEditor = new UITextField()), new JComponent[] {
                uilabel, jscrollpane
            }, new JComponent[] {
                new UILabel(), tipsPane2
            }
        }, ad, ad1, 7D);
    }
}
