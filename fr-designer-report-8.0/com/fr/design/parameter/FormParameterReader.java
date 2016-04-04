// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.parameter;

import com.fr.base.FRContext;
import com.fr.base.Parameter;
import com.fr.form.main.Form;
import com.fr.form.main.FormIO;
import com.fr.general.FRLogger;

// Referenced classes of package com.fr.design.parameter:
//            AbstractParameterReader

public class FormParameterReader extends AbstractParameterReader
{

    public FormParameterReader()
    {
    }

    public Parameter[] readParameterFromPath(String s)
    {
        if(accept(s, new String[] {
    ".frm", ".form"
}))
        {
            try
            {
                Form form = FormIO.readForm(FRContext.getCurrentEnv(), s);
                return form.getParameters();
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
