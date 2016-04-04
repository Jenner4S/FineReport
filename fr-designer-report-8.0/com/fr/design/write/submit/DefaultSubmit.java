// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.write.submit;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.fun.impl.AbstractSubmitProvider;
import com.fr.general.Inter;

// Referenced classes of package com.fr.design.write.submit:
//            CustomSubmitJobPane

public class DefaultSubmit extends AbstractSubmitProvider
{

    public DefaultSubmit()
    {
    }

    public BasicBeanPane appearanceForSubmit()
    {
        return new CustomSubmitJobPane();
    }

    public String dataForSubmit()
    {
        return Inter.getLocText("FR-Designer_Submmit_WClass");
    }

    public String keyForSubmit()
    {
        return "submitnormal";
    }
}
