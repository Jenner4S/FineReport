// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.write.submit;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.write.DMLConfigJob;

// Referenced classes of package com.fr.design.write.submit:
//            SmartInsertDBManipulationPane

public class SmartInsertDMLJobPane extends BasicBeanPane
{

    private SmartInsertDBManipulationPane dbPane;

    public SmartInsertDMLJobPane(ElementCasePane elementcasepane)
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        dbPane = new SmartInsertDBManipulationPane(elementcasepane);
        add(dbPane, "Center");
    }

    protected String title4PopupWindow()
    {
        return "DB";
    }

    public void populateBean(DMLConfigJob dmlconfigjob)
    {
        dbPane.populateBean(dmlconfigjob.getDBManipulation());
    }

    public DMLConfigJob updateBean()
    {
        DMLConfigJob dmlconfigjob = new DMLConfigJob();
        dmlconfigjob.setDBManipulation(dbPane.updateBean());
        return dmlconfigjob;
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((DMLConfigJob)obj);
    }
}
