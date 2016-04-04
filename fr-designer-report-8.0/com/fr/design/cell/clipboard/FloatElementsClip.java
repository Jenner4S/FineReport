// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.clipboard;

import com.fr.base.FRContext;
import com.fr.design.cell.FloatElementsProvider;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.grid.selection.FloatSelection;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.ElementCase;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.unit.FU;
import com.fr.stable.unit.OLDPIX;
import java.io.Serializable;
import java.util.Iterator;

public class FloatElementsClip
    implements Cloneable, Serializable, FloatElementsProvider
{

    private FloatElement floatEl;

    public FloatElementsClip(FloatElement floatelement)
    {
        floatEl = floatelement;
    }

    public FloatSelection pasteAt(TemplateElementCase templateelementcase)
    {
        if(floatEl == null)
            return null;
        FloatElement floatelement;
        try
        {
            floatelement = (FloatElement)floatEl.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception)
        {
            FRContext.getLogger().error(clonenotsupportedexception.getMessage(), clonenotsupportedexception);
            return null;
        }
        for(; templateelementcase.getFloatElement(floatelement.getName()) != null; floatelement.setName((new StringBuilder()).append(floatelement.getName()).append("-Copy").toString()));
        for(; isContainSameBoundFloatElement(templateelementcase, floatelement); floatelement.setLeftDistance(FU.getInstance(floatelement.getLeftDistance().toFU() + (new OLDPIX(50F)).toFU())))
            floatelement.setTopDistance(FU.getInstance(floatelement.getTopDistance().toFU() + (new OLDPIX(50F)).toFU()));

        templateelementcase.addFloatElement(floatelement);
        return new FloatSelection(floatelement.getName());
    }

    private static boolean isContainSameBoundFloatElement(ElementCase elementcase, FloatElement floatelement)
    {
        if(floatelement == null)
            return false;
        for(Iterator iterator = elementcase.floatIterator(); iterator.hasNext();)
        {
            FloatElement floatelement1 = (FloatElement)iterator.next();
            if(hasSameDistance(floatelement1, floatelement))
                return true;
        }

        return false;
    }

    private static boolean hasSameDistance(FloatElement floatelement, FloatElement floatelement1)
    {
        return ComparatorUtils.equals(floatelement.getTopDistance(), floatelement1.getTopDistance()) && ComparatorUtils.equals(floatelement.getLeftDistance(), floatelement1.getLeftDistance()) && ComparatorUtils.equals(floatelement.getWidth(), floatelement1.getWidth()) && ComparatorUtils.equals(floatelement.getHeight(), floatelement1.getHeight());
    }

    public Object clone()
        throws CloneNotSupportedException
    {
        FloatElementsClip floatelementsclip = (FloatElementsClip)super.clone();
        if(floatEl != null)
            floatelementsclip.floatEl = (FloatElement)floatEl.clone();
        return floatelementsclip;
    }
}
