// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.clipboard;

import com.fr.base.FRContext;
import com.fr.general.FRLogger;
import com.fr.grid.selection.CellSelection;
import com.fr.report.cell.*;
import com.fr.report.elementcase.TemplateElementCase;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

public class CellElementsClip
    implements Cloneable, Serializable
{

    private int columnSpan;
    private int rowSpan;
    private TemplateCellElement clips[];

    public CellElementsClip(int i, int j, TemplateCellElement atemplatecellelement[])
    {
        columnSpan = 0;
        rowSpan = 0;
        columnSpan = i;
        rowSpan = j;
        clips = atemplatecellelement;
    }

    public String compateExcelPaste()
    {
        Arrays.sort(clips, CellElementComparator.getRowFirstComparator());
        StringBuffer stringbuffer = new StringBuffer();
        int i = -1;
        for(int j = 0; j < clips.length; j++)
        {
            TemplateCellElement templatecellelement = clips[j];
            if(i == -1)
                i = templatecellelement.getRow();
            if(i < templatecellelement.getRow())
            {
                for(int k = i; k < templatecellelement.getRow(); k++)
                    stringbuffer.append('\n');

                i = templatecellelement.getRow();
            }
            if(stringbuffer.length() > 0 && stringbuffer.charAt(stringbuffer.length() - 1) != '\n')
                stringbuffer.append('\t');
            stringbuffer.append(templatecellelement.getValue());
        }

        return stringbuffer.toString();
    }

    public CellSelection pasteAt(TemplateElementCase templateelementcase, int i, int j)
    {
        TemplateCellElement templatecellelement;
        for(Iterator iterator = templateelementcase.intersect(i, j, columnSpan, rowSpan); iterator.hasNext(); templateelementcase.removeCellElement(templatecellelement))
            templatecellelement = (TemplateCellElement)iterator.next();

        for(int k = 0; k < clips.length; k++)
        {
            TemplateCellElement templatecellelement1;
            try
            {
                templatecellelement1 = (TemplateCellElement)clips[k].clone();
            }
            catch(CloneNotSupportedException clonenotsupportedexception)
            {
                FRContext.getLogger().error(clonenotsupportedexception.getMessage(), clonenotsupportedexception);
                return null;
            }
            templateelementcase.addCellElement((TemplateCellElement)templatecellelement1.deriveCellElement(i + templatecellelement1.getColumn(), j + templatecellelement1.getRow()), false);
        }

        return new CellSelection(i, j, columnSpan, rowSpan);
    }

    public void pasteAtRegion(TemplateElementCase templateelementcase, int i, int j, int k, int l, int i1, int j1)
    {
        for(int k1 = 0; k1 < clips.length; k1++)
        {
            TemplateCellElement templatecellelement = clips[k1];
            templatecellelement = (TemplateCellElement)templatecellelement.deriveCellElement(i + templatecellelement.getColumn(), j + templatecellelement.getRow());
            if(templatecellelement.getColumn() < k + i1 && templatecellelement.getRow() < l + j1 && templatecellelement.getColumn() >= k && templatecellelement.getRow() >= l)
                templateelementcase.addCellElement(templatecellelement);
        }

    }

    public Object clone()
        throws CloneNotSupportedException
    {
        CellElementsClip cellelementsclip = (CellElementsClip)super.clone();
        if(clips != null)
        {
            cellelementsclip.clips = new TemplateCellElement[clips.length];
            for(int i = 0; i < clips.length; i++)
                cellelementsclip.clips[i] = (TemplateCellElement)clips[i].clone();

        }
        return cellelementsclip;
    }
}
