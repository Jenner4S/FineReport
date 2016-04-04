// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.utils;

import com.fr.base.Style;
import com.fr.design.actions.cell.style.StyleActionInterface;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.*;
import com.fr.report.cell.*;
import com.fr.report.elementcase.ElementCase;
import com.fr.report.elementcase.TemplateElementCase;
import java.awt.Rectangle;

public class ReportActionUtils
{
    public static interface IterAction
    {

        public abstract void dealWith(CellElement cellelement);
    }


    private ReportActionUtils()
    {
    }

    public static boolean executeAction(StyleActionInterface styleactioninterface, ElementCasePane elementcasepane)
    {
        Selection selection = elementcasepane.getSelection();
        if(selection instanceof FloatSelection)
        {
            TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
            FloatElement floatelement = templateelementcase.getFloatElement(((FloatSelection)selection).getSelectedFloatName());
            Style style = floatelement.getStyle();
            floatelement.setStyle(styleactioninterface.executeStyle(style, style));
        } else
        {
            TemplateElementCase templateelementcase1 = elementcasepane.getEditingElementCase();
            Object obj = templateelementcase1.getTemplateCellElement(((CellSelection)selection).getColumn(), ((CellSelection)selection).getRow());
            if(obj == null)
            {
                obj = new DefaultTemplateCellElement(((CellSelection)selection).getColumn(), ((CellSelection)selection).getRow());
                templateelementcase1.addCellElement(((TemplateCellElement) (obj)));
            }
            Style style1 = ((TemplateCellElement) (obj)).getStyle();
            actionIterateWithCellSelection((CellSelection)selection, templateelementcase1, new IterAction(styleactioninterface, style1) {

                final StyleActionInterface val$styleActionInterface;
                final Style val$selectedStyle;

                public void dealWith(CellElement cellelement)
                {
                    Style style2 = cellelement.getStyle();
                    cellelement.setStyle(styleActionInterface.executeStyle(style2, selectedStyle));
                }

            
            {
                styleActionInterface = styleactioninterface;
                selectedStyle = style;
                super();
            }
            }
);
        }
        return true;
    }

    public static void actionIterateWithCellSelection(CellSelection cellselection, TemplateElementCase templateelementcase, IterAction iteraction)
    {
        int i = cellselection.getCellRectangleCount();
        for(int j = 0; j < i; j++)
        {
            Rectangle rectangle = cellselection.getCellRectangle(j);
            for(int k = rectangle.height - 1; k >= 0; k--)
            {
                for(int l = rectangle.width - 1; l >= 0; l--)
                {
                    int i1 = l + rectangle.x;
                    int j1 = k + rectangle.y;
                    Object obj = templateelementcase.getTemplateCellElement(i1, j1);
                    if(obj == null)
                    {
                        obj = new DefaultTemplateCellElement(i1, j1);
                        templateelementcase.addCellElement(((TemplateCellElement) (obj)));
                    } else
                    if(((TemplateCellElement) (obj)).getColumn() != i1 || ((TemplateCellElement) (obj)).getRow() != j1)
                        continue;
                    iteraction.dealWith(((CellElement) (obj)));
                }

            }

        }

    }

    public static Style getCurrentStyle(ElementCasePane elementcasepane)
    {
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        Selection selection = elementcasepane.getSelection();
        if(selection instanceof FloatSelection)
        {
            FloatElement floatelement = templateelementcase.getFloatElement(((FloatSelection)selection).getSelectedFloatName());
            return floatelement.getStyle();
        }
        CellElement cellelement = templateelementcase.getCellElement(((CellSelection)selection).getColumn(), ((CellSelection)selection).getRow());
        if(cellelement == null)
            return Style.DEFAULT_STYLE;
        else
            return cellelement.getStyle();
    }
}
