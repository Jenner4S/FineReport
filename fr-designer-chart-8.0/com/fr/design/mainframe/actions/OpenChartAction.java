// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.actions;

import com.fr.design.actions.file.OpenTemplateAction;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.file.*;
import java.awt.event.ActionEvent;

public class OpenChartAction extends OpenTemplateAction
{

    public OpenChartAction()
    {
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        FILEChooserPane4Chart filechooserpane4chart = FILEChooserPane4Chart.getInstance(true, true);
        if(filechooserpane4chart.showOpenDialog(DesignerContext.getDesignerFrame(), ".crt") == 0)
        {
            FILE file = filechooserpane4chart.getSelectedFILE();
            if(file == null)
                return;
            DesignerContext.getDesignerFrame().openTemplate(file);
        }
    }
}
