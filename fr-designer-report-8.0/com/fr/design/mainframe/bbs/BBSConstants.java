// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.bbs;

import com.fr.stable.StringUtils;
import java.util.Properties;

public class BBSConstants
{

    public static final String GET_MESSAGE_URL = loadAttribute("GET_MESSAGE_URL", "http://feedback.finedevelop.com:3000/bbs/message/count");
    public static final String DEFAULT_URL = loadAttribute("DEFAULT_URL", "http://bbs.finereport.com/home.php?mod=space&do=pm");
    public static final String SHARE_URL = loadAttribute("SHARE_URL", "http://bbs.finereport.com/");
    public static final String COLLECT_URL = loadAttribute("COLLECT_URL", "http://bbs.finereport.com/");
    public static final String VERIFY_URL = loadAttribute("VERIFY_URL", "http://bbs.finereport.com/");
    public static final String UPDATE_INFO_URL = loadAttribute("UPDATE_INFO_URL", "http://bbs.finereport.com/");
    public static final String BBS_MOBILE_MOD = loadAttribute("BBS_MOBILE_MOD", "http://bbs.finereport.com/forum.php?mobile=1");
    public static final String UPDATE_KEY = loadAttribute("UPDATE_KEY", "newIsPopup");
    private static final String GUEST_KEY = "USER";
    private static final String LINK_KEY = "LINK";
    private static final int GUEST_NUM = 5;
    public static final String ALL_GUEST[] = loadAllGuestsInfo("USER");
    public static final String ALL_LINK[] = loadAllGuestsInfo("LINK");
    private static Properties PROP = null;

    public BBSConstants()
    {
    }

    private static String[] loadAllGuestsInfo(String s)
    {
        String as[] = new String[5];
        for(int i = 0; i < 5; i++)
            as[i] = loadAttribute((new StringBuilder()).append(s).append(i).toString(), "");

        return as;
    }

    private static String loadAttribute(String s, String s1)
    {
        if(PROP == null)
        {
            PROP = new Properties();
            try
            {
                PROP.load(com/fr/design/mainframe/bbs/BBSConstants.getResourceAsStream("/com/fr/design/mainframe/bbs/bbs.properties"));
            }
            catch(Exception exception) { }
        }
        String s2 = PROP.getProperty(s);
        if(StringUtils.isEmpty(s2))
            s2 = s1;
        return s2;
    }

}
