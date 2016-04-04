// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.present;

import com.fr.general.Inter;
import com.fr.stable.ColumnRow;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ColumnRowTableModel extends AbstractTableModel
{

    private String columnNames[] = {
        Inter.getLocText("Column"), Inter.getLocText("Row")
    };
    private List columnRowList;

    public ColumnRowTableModel()
    {
        columnRowList = new ArrayList();
    }

    public String getColumnName(int i)
    {
        return columnNames[i];
    }

    public int getColumnCount()
    {
        return columnNames.length;
    }

    public int getRowCount()
    {
        return columnRowList.size();
    }

    public Object getValueAt(int i, int j)
    {
        ColumnRow columnrow = (ColumnRow)columnRowList.get(i);
        switch(j)
        {
        case 0: // '\0'
            return new Integer(columnrow.getColumn());

        case 1: // '\001'
            return new Integer(columnrow.getRow());
        }
        return null;
    }

    public void setValueAt(Object obj, int i, int j)
    {
        ColumnRow columnrow = (ColumnRow)columnRowList.get(i);
        if(j == 0 && (obj instanceof Integer))
        {
            int k = ((Integer)obj).intValue();
            int i1 = columnrow.getRow();
            columnrow = ColumnRow.valueOf(k, i1);
        } else
        if(j == 1 && (obj instanceof Integer))
        {
            int l = columnrow.getColumn();
            int j1 = ((Integer)obj).intValue();
            columnrow = ColumnRow.valueOf(l, j1);
        }
        columnRowList.remove(i);
        columnRowList.add(i, columnrow);
    }

    public Class getColumnClass(int i)
    {
        if(i == 0 || i == 1)
            return java/lang/Integer;
        else
            return java/lang/String;
    }

    public boolean isCellEditable(int i, int j)
    {
        return false;
    }

    public void addColumnRow(ColumnRow columnrow)
    {
        columnRowList.add(columnrow);
    }

    public void removeColumnRow(int i)
    {
        columnRowList.remove(i);
    }

    public ColumnRow getColumnRow(int i)
    {
        return (ColumnRow)columnRowList.get(i);
    }

    public void setColumnRow(ColumnRow columnrow, int i)
    {
        if(columnRowList.get(i) != null)
            columnRowList.remove(i);
        columnRowList.add(i, columnrow);
    }

    public void removeAllColumnRow()
    {
        columnRowList.clear();
    }
}
