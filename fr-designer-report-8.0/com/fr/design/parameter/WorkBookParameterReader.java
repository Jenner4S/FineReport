// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.parameter;

import com.fr.base.FRContext;
import com.fr.base.Parameter;
import com.fr.general.FRLogger;
import com.fr.io.TemplateWorkBookIO;
import com.fr.main.TemplateWorkBook;

// Referenced classes of package com.fr.design.parameter:
//            AbstractParameterReader

public class WorkBookParameterReader extends AbstractParameterReader
{

    public WorkBookParameterReader()
    {
    }

    public Parameter[] readParameterFromPath(String s)
    {
        if(accept(s, new String[] {
    ".cpt"
}))
        {
            try
            {
                TemplateWorkBook templateworkbook = TemplateWorkBookIO.readTemplateWorkBook(FRContext.getCurrentEnv(), s);
                return templateworkbook.getParameters();
            }
            catch(Exception exception)
            {
                FRContext.getLogger().error(exception.getMessage(), exception);
            }
            return new Parameter[0];
        } else
        {
            return null;
        }
    }
}
