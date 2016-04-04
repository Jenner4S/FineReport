// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.javascript;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.javascript.EmailPane;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.*;
import com.fr.general.Inter;
import com.fr.js.EmailJavaScript;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

public class ChartEmailPane extends EmailPane
{

    private UITextField itemNameTextField;

    public ChartEmailPane()
    {
    }

    protected void initCenterPane(UILabel uilabel, JScrollPane jscrollpane, double d, double d1)
    {
        double ad[] = {
            d1, d
        };
        itemNameTextField = new UITextField();
        JTemplate jtemplate = DesignerContext.getDesignerFrame().getSelectedJTemplate();
        boolean flag = jtemplate.isJWorkBook();
        if(flag)
        {
            double ad1[] = {
                d1, d1, d1, d1, d1, d1, d, d1, d1
            };
            showTplContent = new UICheckBox(Inter.getLocText("Email-Can_Preview_Report_Content"));
            centerPane = TableLayoutHelper.createCommonTableLayoutPane(new JComponent[][] {
                new JComponent[] {
                    new UILabel((new StringBuilder()).append(Inter.getLocText("Name")).append(":").toString(), 4), itemNameTextField
                }, new JComponent[] {
                    new UILabel(), tipsPane1
                }, createLinePane(Inter.getLocText("HJS-Mail_to"), maitoEditor = new UITextField()), createLinePane(Inter.getLocText("HJS-CC_to"), ccEditor = new UITextField()), createLinePane(Inter.getLocText("EmailPane-BCC"), bccEditor = new UITextField()), createLinePane(Inter.getLocText("EmailPane-mailSubject"), titleEditor = new UITextField()), new JComponent[] {
                    uilabel, jscrollpane
                }, new JComponent[] {
                    new UILabel(), showTplContent
                }, new JComponent[] {
                    new UILabel(), tipsPane2
                }
            }, ad1, ad, 6D);
        } else
        {
            double ad2[] = {
                d1, d1, d1, d1, d1, d1, d, d1
            };
            centerPane = TableLayoutHelper.createCommonTableLayoutPane(new JComponent[][] {
                new JComponent[] {
                    new UILabel((new StringBuilder()).append(Inter.getLocText("Name")).append(":").toString(), 4), itemNameTextField
                }, new JComponent[] {
                    new UILabel(), tipsPane1
                }, createLinePane(Inter.getLocText("HJS-Mail_to"), maitoEditor = new UITextField()), createLinePane(Inter.getLocText("HJS-CC_to"), ccEditor = new UITextField()), createLinePane(Inter.getLocText("EmailPane-BCC"), bccEditor = new UITextField()), createLinePane(Inter.getLocText("EmailPane-mailSubject"), titleEditor = new UITextField()), new JComponent[] {
                    uilabel, jscrollpane
                }, new JComponent[] {
                    new UILabel(), tipsPane2
                }
            }, ad2, ad, 8D);
        }
    }

    protected void checkEmailConfig(boolean flag)
    {
        super.checkEmailConfig(flag);
        if(itemNameTextField != null)
            itemNameTextField.setEnabled(flag);
    }

    public void populateBean(EmailJavaScript emailjavascript)
    {
        if(itemNameTextField != null)
            itemNameTextField.setName(emailjavascript != null ? emailjavascript.getItemName() : null);
        super.populateBean(emailjavascript);
    }

    public void updateBean(EmailJavaScript emailjavascript)
    {
        if(itemNameTextField != null)
            emailjavascript.setItemName(itemNameTextField.getText());
        super.updateBean(emailjavascript);
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((EmailJavaScript)obj);
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((EmailJavaScript)obj);
    }
}
