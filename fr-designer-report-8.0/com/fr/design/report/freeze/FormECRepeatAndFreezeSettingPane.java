// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report.freeze;

import com.fr.general.Inter;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.report.freeze:
//            RepeatAndFreezeSettingPane

public class FormECRepeatAndFreezeSettingPane extends RepeatAndFreezeSettingPane
{

    public FormECRepeatAndFreezeSettingPane()
    {
    }

    protected String getPageFrozenTitle()
    {
        return (new StringBuilder()).append(Inter.getLocText("FR-Engine_Frozen")).append(":").toString();
    }

    protected void initWriteListener()
    {
    }

    protected void addWriteFrozen(JPanel jpanel)
    {
    }
}
