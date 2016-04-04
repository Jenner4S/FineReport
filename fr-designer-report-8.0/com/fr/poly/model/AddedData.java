// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly.model;

import com.fr.general.ComparatorUtils;
import com.fr.poly.PolyDesigner;
import com.fr.poly.creator.BlockCreator;
import com.fr.report.poly.TemplateBlock;
import com.fr.stable.StringUtils;
import java.util.ArrayList;
import java.util.List;

public class AddedData
{

    private List addedList;
    private PolyDesigner designer;
    private int num;

    public AddedData(PolyDesigner polydesigner)
    {
        addedList = new ArrayList();
        num = 1;
        designer = polydesigner;
    }

    public void addBlockCreator(BlockCreator blockcreator)
    {
        blockcreator.setDesigner(designer);
        TemplateBlock templateblock = blockcreator.getValue();
        if(StringUtils.isEmpty(templateblock.getBlockName()))
            templateblock.setBlockName(createUnRepeatedName());
        addedList.add(blockcreator);
    }

    private String createUnRepeatedName()
    {
        String s = (new StringBuilder()).append("block").append(num).toString();
        int i = 0;
        for(int j = getAddedCount(); i < j; i++)
        {
            TemplateBlock templateblock = getAddedAt(i).getValue();
            if(ComparatorUtils.equals(s, templateblock.getBlockName()))
            {
                num++;
                return createUnRepeatedName();
            }
        }

        return s;
    }

    public void removeBlockCreator(BlockCreator blockcreator)
    {
        addedList.remove(blockcreator);
    }

    public int getAddedCount()
    {
        return addedList.size();
    }

    public BlockCreator getAddedAt(int i)
    {
        if(i < 0 || i > addedList.size() - 1)
            return null;
        else
            return (BlockCreator)addedList.get(i);
    }
}
