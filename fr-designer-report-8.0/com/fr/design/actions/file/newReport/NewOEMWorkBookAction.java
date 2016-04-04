// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.file.newReport;

import com.fr.base.BaseUtils;
import javax.swing.Icon;

// Referenced classes of package com.fr.design.actions.file.newReport:
//            NewWorkBookAction

public class NewOEMWorkBookAction extends NewWorkBookAction
{

    public NewOEMWorkBookAction()
    {
    }

    protected Icon icon()
    {
        return BaseUtils.readIcon("/com/fr/plugin/oem/images/newoemcpt.png");
    }
}
