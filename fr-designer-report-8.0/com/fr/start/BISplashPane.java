// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.start;

import com.fr.base.BaseUtils;
import java.awt.Image;

// Referenced classes of package com.fr.start:
//            SplashPane

public class BISplashPane extends SplashPane
{

    public BISplashPane()
    {
    }

    public Image createSplashBackground()
    {
        return BaseUtils.readImage("/com/fr/base/images/oem/splash-EN.jpg");
    }
}
