// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.bbs.BBSConstants;
import com.fr.general.http.HttpClient;
import com.fr.stable.StringUtils;
import com.fr.stable.core.UUID;
import java.io.PrintStream;
import java.util.*;

public class ActiveKeyGenerator
{

    public static final int AUTH_ERROR = -1;
    public static final int AUTH_SUCCESS = 0;
    public static final int AUTH_FAILED = 1;
    private static final int CONNECT_LEN = 4;
    private static final int KEY_LEN = 5;
    private static final int MIN_NUM_COUNT = 10;
    private static final int MAGIC_NUM = 7;
    private static final String SPLIT_CHAR = "-";
    private static final int MAX_TRY_COUNT = 100;

    public ActiveKeyGenerator()
    {
    }

    public static String generateActiveKey()
    {
        for(int i = 0; i < 100; i++)
        {
            String s = UUID.randomUUID().toString();
            char ac[] = s.toCharArray();
            int j = ac.length;
            int ai[] = new int[j];
            if(invalidEachCharCount(j, ac, ai) || isCharAllNum(j, ai, ac))
                continue;
            String s1 = new String(ac);
            if(localVerify(s1))
                return s1;
        }

        return "";
    }

    public static boolean verify(String s, int i)
    {
        return localVerify(s) && onLineVerify(s, i);
    }

    public static boolean onLineVerify(String s)
    {
        return onLineVerify(s, -1);
    }

    private static HttpClient prepareVerifyConnect(DesignerEnvManager designerenvmanager, int i, String s)
    {
        HashMap hashmap = new HashMap();
        hashmap.put("uuid", designerenvmanager.getUUID());
        hashmap.put("key", s);
        hashmap.put("username", designerenvmanager.getBBSName());
        HttpClient httpclient = new HttpClient(BBSConstants.VERIFY_URL, hashmap);
        if(i != -1)
            httpclient.setTimeout(i);
        return httpclient;
    }

    public static boolean onLineVerify(String s, int i)
    {
        DesignerEnvManager designerenvmanager = DesignerEnvManager.getEnvManager();
        HttpClient httpclient = prepareVerifyConnect(designerenvmanager, i, s);
        return true;
    }

    public static boolean localVerify(String s)
    {
        if(StringUtils.isEmpty(s) || invalidSplitLength(s))
            return false;
        char ac[] = s.toCharArray();
        int i = ac.length;
        int ai[] = new int[i];
        if(invalidEachCharCount(i, ac, ai))
            return false;
        int j = i / 7;
        if(j != 5)
            return false;
        else
            return validRemain(j, ai);
    }

    private static boolean isCharAllNum(int i, int ai[], char ac[])
    {
        int j = i / 7;
        for(int k = 0; k < j; k++)
        {
            int l = 0;
            for(int i1 = 0; i1 < 7; i1++)
                l += ai[i1 + k * 7];

            if(l == 0)
                return true;
            updateRemain(l, ai, k, ac);
        }

        return false;
    }

    private static boolean invalidSplitLength(String s)
    {
        return s.split("-").length != 4;
    }

    private static int getCharIntValue(char c)
    {
        if(!BaseUtils.isNum(c))
            return 0;
        else
            return Character.getNumericValue(c);
    }

    private static boolean validRemain(int i, int ai[])
    {
        for(int j = 0; j < i; j++)
        {
            int k = 0;
            for(int l = 0; l < 7; l++)
                k += ai[l + j * 7];

            if(k == 0)
                return false;
            if(k % 7 != 0)
                return false;
        }

        return true;
    }

    private static boolean invalidEachCharCount(int i, char ac[], int ai[])
    {
        HashMap hashmap = new HashMap();
        for(int j = 0; j < i; j++)
        {
            int k = hashmap.containsKey(Character.valueOf(ac[j])) ? ((Integer)hashmap.get(Character.valueOf(ac[j]))).intValue() + 1 : 1;
            hashmap.put(Character.valueOf(ac[j]), Integer.valueOf(k));
            ai[j] = getCharIntValue(ac[j]);
        }

        if(hashmap.size() <= 10)
            return true;
        for(Iterator iterator = hashmap.entrySet().iterator(); iterator.hasNext();)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            if(((Integer)entry.getValue()).intValue() > 10)
                return true;
        }

        return false;
    }

    private static void updateRemain(int i, int ai[], int j, char ac[])
    {
        int k = i % 7;
        int l = 6 + j * 7;
        int i1;
        for(i1 = ai[l] - k; i1 <= 0; i1 += 7);
        ac[l] = (char)(i1 + 48);
    }

    public static void main(String args[])
    {
        String s = "671b3b43-cfb4-40e7-9ca8-fe71ba33b8e3";
        String s1 = generateActiveKey();
        boolean flag = localVerify(s1);
        System.out.println(flag);
    }
}
