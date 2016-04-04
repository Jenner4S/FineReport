// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report.share;

import com.fr.data.impl.EmbeddedTableData;
import com.fr.general.GeneralUtils;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;
import java.util.HashMap;

// Referenced classes of package com.fr.design.report.share:
//            ConfusionInfo

public class ConfuseTabledataAction
{

    public ConfuseTabledataAction()
    {
    }

    public void confuse(ConfusionInfo confusioninfo, EmbeddedTableData embeddedtabledata)
    {
        int i = embeddedtabledata.getRowCount();
        String as[] = confusioninfo.getConfusionKeys();
        int j = 0;
label0:
        for(int k = ArrayUtils.getLength(as); j < k; j++)
        {
            if(StringUtils.isEmpty(as[j]))
                continue;
            HashMap hashmap = new HashMap();
            int l = 0;
            do
            {
                if(l >= i)
                    continue label0;
                Object obj = embeddedtabledata.getValueAt(l, j);
                Object obj1;
                if(hashmap.containsKey(obj))
                {
                    obj1 = hashmap.get(obj);
                } else
                {
                    obj1 = confusionValue(confusioninfo, j, as[j], hashmap, obj);
                    hashmap.put(obj, obj1);
                }
                embeddedtabledata.setValueAt(obj1, l, j);
                l++;
            } while(true);
        }

    }

    private Object confusionValue(ConfusionInfo confusioninfo, int i, String s, HashMap hashmap, Object obj)
    {
        if(confusioninfo.isNumberColumn(i))
        {
            Number number = GeneralUtils.objectToNumber(s, false);
            Number number1 = GeneralUtils.objectToNumber(obj, false);
            return Double.valueOf(number1.doubleValue() * number.doubleValue());
        }
        String s1 = GeneralUtils.objectToString(obj);
        if(StringUtils.isEmpty(s1))
            return s1;
        else
            return (new StringBuilder()).append(s).append(hashmap.size()).toString();
    }
}
