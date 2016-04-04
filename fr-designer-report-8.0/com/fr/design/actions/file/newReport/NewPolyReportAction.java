// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.file.newReport;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.*;
import com.fr.general.Inter;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;

public class NewPolyReportAction extends UpdateAction
{

    public NewPolyReportAction()
    {
        setName(Inter.getLocText("M-New_Multi_Report"));
        setMnemonic('F');
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/formExport.png"));
        setAccelerator(KeyStroke.getKeyStroke(77, 2));
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        DesignerContext.getDesignerFrame().addAndActivateJTemplate(new JPolyWorkBook());
    }
}
