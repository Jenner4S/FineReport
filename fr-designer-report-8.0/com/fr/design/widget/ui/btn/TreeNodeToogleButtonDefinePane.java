// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui.btn;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.widget.btn.ButtonDetailPane;
import com.fr.form.ui.Button;
import com.fr.general.Inter;
import com.fr.report.web.button.form.TreeNodeToggleButton;
import java.awt.Component;

public class TreeNodeToogleButtonDefinePane extends ButtonDetailPane
{

    public TreeNodeToogleButtonDefinePane()
    {
        initComponents();
    }

    protected void initComponents()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        double d = -2D;
        double ad[] = {
            d
        };
        double ad1[] = {
            d, 300D
        };
        Component acomponent[][] = {
            {
                new UILabel((new StringBuilder()).append(Inter.getLocText(new String[] {
                    "Form-Button", "Type"
                })).append(":").toString()), createButtonTypeComboBox()
            }
        };
        javax.swing.JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad, ad1);
        add(jpanel, "Center");
    }

    public TreeNodeToggleButton update()
    {
        return createButton();
    }

    public TreeNodeToggleButton createButton()
    {
        return new TreeNodeToggleButton();
    }

    public Class classType()
    {
        return com/fr/report/web/button/form/TreeNodeToggleButton;
    }

    public volatile Button update()
    {
        return update();
    }

    public volatile Button createButton()
    {
        return createButton();
    }
}
