// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.design.editor.editor.LongEditor;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import com.fr.web.attr.ReportWebAttr;
import javax.swing.*;

public class CommonPane extends JPanel
{

    private UITextField titleTextField;
    private LongEditor cacheValidateTimeEditor;

    public CommonPane()
    {
        initComponents();
    }

    protected void initComponents()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        setBorder(BorderFactory.createEmptyBorder(6, 2, 4, 2));
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_L_Pane();
        add(jpanel, "North");
        titleTextField = new UITextField(24);
        cacheValidateTimeEditor = new LongEditor();
        JComponent ajcomponent[][] = {
            {
                new UILabel((new StringBuilder()).append(Inter.getLocText("Title")).append(":").toString()), titleTextField, null
            }, {
                new UILabel((new StringBuilder()).append(Inter.getLocText("CacheValidateTime")).append(":").toString()), cacheValidateTimeEditor, new UILabel("milliseconds")
            }
        };
        add(TableLayoutHelper.createCommonTableLayoutPane(ajcomponent, new double[] {
            -2D, -2D
        }, new double[] {
            -2D, -2D, -2D
        }, 4D), "Center");
    }

    public void populate(ReportWebAttr reportwebattr)
    {
        if(reportwebattr.getTitle() != null && reportwebattr.getTitle().length() > 0)
            titleTextField.setText(reportwebattr.getTitle());
        cacheValidateTimeEditor.setValue(Long.valueOf(reportwebattr.getCacheValidateTime()));
    }

    public void update(ReportWebAttr reportwebattr)
    {
        if(!StringUtils.isEmpty(titleTextField.getText()))
            reportwebattr.setTitle(titleTextField.getText());
        else
            reportwebattr.setTitle(null);
        reportwebattr.setCacheValidateTime(cacheValidateTimeEditor.getValue().longValue());
    }
}
