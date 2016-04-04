// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.file;

import com.fr.file.filter.ChooseFileFilter;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.ProductConstants;

// Referenced classes of package com.fr.file:
//            FILEChooserPane

public class FILEChooserPane4Chart extends FILEChooserPane
{

    private static final FILEChooserPane4Chart INSTANCE = new FILEChooserPane4Chart(true, true);

    public static FILEChooserPane4Chart getInstance(boolean flag, boolean flag1)
    {
        INSTANCE.showEnv = flag;
        INSTANCE.showLoc = flag1;
        INSTANCE.showWebReport = false;
        INSTANCE.setModelOfPlaceList();
        INSTANCE.removeAllFilter();
        return INSTANCE;
    }

    public FILEChooserPane4Chart(boolean flag, boolean flag1)
    {
        super(flag, flag1);
    }

    protected void fileType()
    {
        String s = ProductConstants.APP_NAME;
        if(ComparatorUtils.equals(suffix, ".crt"))
        {
            addChooseFILEFilter(new ChooseFileFilter("crt", (new StringBuilder()).append(s).append(Inter.getLocText(new String[] {
                "Utils-The-Chart", "FR-App-All_File"
            })).toString()));
            return;
        } else
        {
            return;
        }
    }

    protected String getEnvProjectName()
    {
        return Inter.getLocText("FR-Chart-Env_Directory");
    }

}
