// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class ErrorTemplatePane extends BasicBeanPane
{

    private UITextField templateField;

    public ErrorTemplatePane()
    {
        templateField = null;
        setLayout(FRGUIPaneFactory.createBorderLayout());
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        templateField = new UITextField(36);
        double d = -2D;
        double ad[] = {
            d, d, d, d, d
        };
        double ad1[] = {
            d, d
        };
        JPanel jpanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        jpanel.add(templateField);
        Component acomponent[][] = {
            {
                new UILabel((new StringBuilder()).append(Inter.getLocText("Template_Path")).append(":").toString()), jpanel
            }, {
                new UILabel((new StringBuilder()).append(Inter.getLocText("Template_Parameters")).append(":").toString()), null
            }, {
                new UILabel("message:"), new UILabel(Inter.getLocText("Verify-Message"))
            }, {
                new UILabel("charset:"), new UILabel(Inter.getLocText("Server_Charset"))
            }, {
                new UILabel("exception:"), new UILabel(Inter.getLocText("Exception_StackTrace"))
            }
        };
        JPanel jpanel1 = TableLayoutHelper.createTableLayoutPane(acomponent, ad, ad1);
        add(jpanel1, "North");
    }

    protected String title4PopupWindow()
    {
        return "ErrorTemplate";
    }

    public void populateBean(String s)
    {
        templateField.setText(s);
    }

    public String updateBean()
    {
        return templateField.getText();
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((String)obj);
    }
}
