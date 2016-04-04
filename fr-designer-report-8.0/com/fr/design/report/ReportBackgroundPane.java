// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.style.background.BackgroundPane;
import com.fr.general.Inter;
import com.fr.page.ReportSettingsProvider;

public class ReportBackgroundPane extends BasicPane
{

    private UICheckBox isPrintBackgroundCheckBox;
    private BackgroundPane backgroundPane;

    public ReportBackgroundPane()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        backgroundPane = new BackgroundPane();
        add(backgroundPane, "Center");
        isPrintBackgroundCheckBox = new UICheckBox(Inter.getLocText("ReportGUI-Print_Background"));
        add(isPrintBackgroundCheckBox, "South");
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText(new String[] {
            "paper", "Background"
        });
    }

    public void populate(ReportSettingsProvider reportsettingsprovider)
    {
        backgroundPane.populate(reportsettingsprovider.getBackground());
        isPrintBackgroundCheckBox.setSelected(reportsettingsprovider.isPrintBackground());
    }

    public void update(ReportSettingsProvider reportsettingsprovider)
    {
        reportsettingsprovider.setBackground(backgroundPane.update());
        reportsettingsprovider.setPrintBackground(isPrintBackgroundCheckBox.isSelected());
    }
}
