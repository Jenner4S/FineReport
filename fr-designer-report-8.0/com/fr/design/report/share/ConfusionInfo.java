// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report.share;

import com.fr.data.impl.EmbeddedTableData;

public class ConfusionInfo
{

    private String tabledataName;
    private String confusionKeys[];
    private String columnNames[];
    private Class colType[];

    public ConfusionInfo(EmbeddedTableData embeddedtabledata, String s)
    {
        tabledataName = s;
        int i = embeddedtabledata.getColumnCount();
        confusionKeys = new String[i];
        columnNames = new String[i];
        colType = new Class[i];
        for(int j = 0; j < i; j++)
        {
            columnNames[j] = embeddedtabledata.getColumnName(j);
            confusionKeys[j] = "";
            colType[j] = embeddedtabledata.getColumnClass(j);
        }

    }

    public ConfusionInfo(String s, String as[], String as1[], Class aclass[])
    {
        tabledataName = s;
        confusionKeys = as;
        columnNames = as1;
        colType = aclass;
    }

    public String getTabledataName()
    {
        return tabledataName;
    }

    public void setTabledataName(String s)
    {
        tabledataName = s;
    }

    public String[] getConfusionKeys()
    {
        return confusionKeys;
    }

    public void setConfusionKeys(String as[])
    {
        confusionKeys = as;
    }

    public String[] getColumnNames()
    {
        return columnNames;
    }

    public void setColumnNames(String as[])
    {
        columnNames = as;
    }

    public Class[] getColType()
    {
        return colType;
    }

    public void setColType(Class aclass[])
    {
        colType = aclass;
    }

    public boolean isNumberColumn(int i)
    {
        return colType[i] == java/lang/Integer || colType[i] == java/lang/Double || colType[i] == java/lang/Float;
    }
}
