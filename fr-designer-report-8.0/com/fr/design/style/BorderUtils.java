// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.style;

import com.fr.base.CellBorderStyle;
import com.fr.base.Style;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.ComparatorUtils;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.FloatElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.ElementCase;
import com.fr.report.elementcase.TemplateElementCase;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Hashtable;

public abstract class BorderUtils
{
    public static class BorderStyleColor
    {

        private int style;
        private Color color;

        public Color getColor()
        {
            return color;
        }

        public void setColor(Color color1)
        {
            color = color1;
        }

        public int getStyle()
        {
            return style;
        }

        public void setStyle(int i)
        {
            style = i;
        }

        public boolean equals(Object obj)
        {
            if(obj instanceof BorderStyleColor)
            {
                BorderStyleColor borderstylecolor = (BorderStyleColor)obj;
                if(getStyle() == borderstylecolor.getStyle() && ComparatorUtils.equals(getColor(), borderstylecolor.getColor()))
                    return true;
            }
            return false;
        }

        public BorderStyleColor(int i, Color color1)
        {
            setStyle(i);
            setColor(color1);
        }
    }

    public static class ColumnRow
    {

        private int column;
        private int row;
        private boolean isVertical;

        public int getColumn()
        {
            return column;
        }

        public void setColumn(int i)
        {
            column = i;
        }

        public int getRow()
        {
            return row;
        }

        public void setRow(int i)
        {
            row = i;
        }

        public int hashCode()
        {
            return toString().hashCode();
        }

        public boolean equals(Object obj)
        {
            if(obj instanceof ColumnRow)
            {
                ColumnRow columnrow = (ColumnRow)obj;
                if(getColumn() == columnrow.getColumn() && getRow() == columnrow.getRow())
                    return true;
            }
            return false;
        }

        public String toString()
        {
            if(isVertical)
                return (new StringBuilder()).append("ColumnRow[").append(getColumn()).append(", ").append(getRow()).append(", vertical]").toString();
            else
                return (new StringBuilder()).append("ColumnRow[").append(getColumn()).append(", ").append(getRow()).append(", horizontal]").toString();
        }

        public ColumnRow()
        {
            this(-1, -1, true);
        }

        public ColumnRow(int i, int j, boolean flag)
        {
            column = -1;
            row = -1;
            isVertical = true;
            column = i;
            row = j;
            isVertical = flag;
        }
    }


    private static boolean insideModel;
    private static int NUMBER = 4;

    public BorderUtils()
    {
    }

    public static Object[] createCellBorderObject(ElementCasePane elementcasepane)
    {
        CellBorderStyle cellborderstyle = new CellBorderStyle();
        insideModel = false;
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        Selection selection = elementcasepane.getSelection();
        Object aobj[];
        if(selection instanceof FloatSelection)
        {
            aobj = new Object[NUMBER];
            floatSet(cellborderstyle, templateelementcase, selection);
            borderStyle(cellborderstyle, aobj, 0);
        } else
        {
            CellSelection cellselection = ((CellSelection)selection).clone();
            int i = cellselection.getCellRectangleCount();
            if(i == 1)
            {
                aobj = new Object[NUMBER];
                int j = cellselection.getColumn();
                int l = cellselection.getRow();
                int j1 = cellselection.getColumnSpan();
                int k1 = cellselection.getRowSpan();
                calStyle(l, j, k1, j1, templateelementcase, cellborderstyle, true);
                borderStyle(cellborderstyle, aobj, 0);
            } else
            {
                int k = NUMBER * i;
                aobj = new Object[k];
                for(int i1 = 0; i1 < i; i1++)
                {
                    CellBorderStyle cellborderstyle1 = new CellBorderStyle();
                    insideModel = false;
                    Rectangle rectangle = cellselection.getCellRectangle(i1);
                    int l1 = rectangle.x;
                    int i2 = rectangle.y;
                    int j2 = rectangle.width;
                    int k2 = rectangle.height;
                    calStyle(i2, l1, k2, j2, templateelementcase, cellborderstyle1, true);
                    borderStyle(cellborderstyle1, aobj, i1 * NUMBER);
                }

            }
        }
        return aobj;
    }

    private static void calStyle(int i, int j, int k, int l, ElementCase elementcase, CellBorderStyle cellborderstyle, boolean flag)
    {
        Hashtable hashtable = new Hashtable();
        Hashtable hashtable1 = new Hashtable();
        Hashtable hashtable2 = new Hashtable();
        Hashtable hashtable3 = new Hashtable();
        checkUp(i, j, l, elementcase, hashtable1);
        checkLeft(i, j, k, elementcase, hashtable3);
        checkDown(i, j, k, l, elementcase, hashtable);
        checkRight(i, j, k, l, elementcase, hashtable2);
        checkAll(i, j, k, l, elementcase, hashtable, hashtable1, hashtable2, hashtable3);
        ArrayList arraylist = new ArrayList();
        inspectUp(i, j, l, hashtable, cellborderstyle, arraylist, flag);
        inspectLeft(i, j, k, hashtable2, cellborderstyle, arraylist, flag);
        inspectDown(i, j, k, l, hashtable1, cellborderstyle, arraylist, flag);
        inspectRight(i, j, k, l, hashtable3, cellborderstyle, arraylist, flag);
        inspectInsideModel(i, j, k, l, hashtable, hashtable1, hashtable2, hashtable3, cellborderstyle, arraylist, flag);
    }

    private static void inspectInsideModel(int i, int j, int k, int l, Hashtable hashtable, Hashtable hashtable1, Hashtable hashtable2, Hashtable hashtable3, 
            CellBorderStyle cellborderstyle, java.util.List list, boolean flag)
    {
        if(l > 1 || k > 1)
        {
            insideModel = true;
            list.clear();
            for(int i1 = j + 1; i1 < j + l; i1++)
            {
                for(int k1 = i; k1 < i + k; k1++)
                {
                    BorderStyleColor borderstylecolor2 = (BorderStyleColor)hashtable2.get(new ColumnRow(i1, k1, true));
                    list.add(borderstylecolor2);
                    borderstylecolor2 = (BorderStyleColor)hashtable3.get(new ColumnRow(i1, k1, true));
                    list.add(borderstylecolor2);
                }

            }

            if(!list.isEmpty())
                if(isAllEquals(list))
                {
                    BorderStyleColor borderstylecolor = (BorderStyleColor)list.get(0);
                    if(borderstylecolor != null)
                    {
                        cellborderstyle.setVerticalColor(borderstylecolor.getColor());
                        cellborderstyle.setVerticalStyle(borderstylecolor.getStyle());
                    }
                } else
                if(flag)
                {
                    cellborderstyle.setVerticalColor(Color.BLACK);
                    cellborderstyle.setVerticalStyle(0);
                } else
                {
                    cellborderstyle.setVerticalColor(Color.GRAY);
                    cellborderstyle.setVerticalStyle(15);
                }
            list.clear();
            for(int j1 = j; j1 < j + l; j1++)
            {
                for(int l1 = i + 1; l1 < i + k; l1++)
                {
                    BorderStyleColor borderstylecolor3 = (BorderStyleColor)hashtable.get(new ColumnRow(j1, l1, false));
                    list.add(borderstylecolor3);
                    borderstylecolor3 = (BorderStyleColor)hashtable1.get(new ColumnRow(j1, l1, false));
                    list.add(borderstylecolor3);
                }

            }

            if(!list.isEmpty())
                if(isAllEquals(list))
                {
                    BorderStyleColor borderstylecolor1 = (BorderStyleColor)list.get(0);
                    if(borderstylecolor1 != null)
                    {
                        cellborderstyle.setHorizontalColor(borderstylecolor1.getColor());
                        cellborderstyle.setHorizontalStyle(borderstylecolor1.getStyle());
                    }
                } else
                if(flag)
                {
                    cellborderstyle.setHorizontalColor(Color.BLACK);
                    cellborderstyle.setHorizontalStyle(0);
                } else
                {
                    cellborderstyle.setHorizontalColor(Color.GRAY);
                    cellborderstyle.setHorizontalStyle(15);
                }
        }
    }

    private static void inspectUp(int i, int j, int k, Hashtable hashtable, CellBorderStyle cellborderstyle, java.util.List list, boolean flag)
    {
        list.clear();
        for(int l = j; l < j + k; l++)
        {
            BorderStyleColor borderstylecolor1 = (BorderStyleColor)hashtable.get(new ColumnRow(l, i, false));
            list.add(borderstylecolor1);
        }

        if(!list.isEmpty())
            if(isAllEquals(list))
            {
                BorderStyleColor borderstylecolor = (BorderStyleColor)list.get(0);
                if(borderstylecolor != null)
                {
                    cellborderstyle.setTopColor(borderstylecolor.getColor());
                    cellborderstyle.setTopStyle(borderstylecolor.getStyle());
                }
            } else
            if(flag)
            {
                cellborderstyle.setTopColor(Color.BLACK);
                cellborderstyle.setTopStyle(0);
            } else
            {
                cellborderstyle.setTopColor(Color.GRAY);
                cellborderstyle.setTopStyle(15);
            }
    }

    private static void inspectLeft(int i, int j, int k, Hashtable hashtable, CellBorderStyle cellborderstyle, java.util.List list, boolean flag)
    {
        list.clear();
        for(int l = i; l < i + k; l++)
        {
            BorderStyleColor borderstylecolor1 = (BorderStyleColor)hashtable.get(new ColumnRow(j, l, true));
            list.add(borderstylecolor1);
        }

        if(!list.isEmpty())
            if(isAllEquals(list))
            {
                BorderStyleColor borderstylecolor = (BorderStyleColor)list.get(0);
                if(borderstylecolor != null)
                {
                    cellborderstyle.setLeftColor(borderstylecolor.getColor());
                    cellborderstyle.setLeftStyle(borderstylecolor.getStyle());
                }
            } else
            if(flag)
            {
                cellborderstyle.setLeftColor(Color.BLACK);
                cellborderstyle.setLeftStyle(0);
            } else
            {
                cellborderstyle.setLeftColor(Color.GRAY);
                cellborderstyle.setLeftStyle(15);
            }
    }

    private static void inspectDown(int i, int j, int k, int l, Hashtable hashtable, CellBorderStyle cellborderstyle, java.util.List list, boolean flag)
    {
        list.clear();
        for(int i1 = j; i1 < j + l; i1++)
        {
            BorderStyleColor borderstylecolor1 = (BorderStyleColor)hashtable.get(new ColumnRow(i1, i + k, false));
            list.add(borderstylecolor1);
        }

        if(!list.isEmpty())
            if(isAllEquals(list))
            {
                BorderStyleColor borderstylecolor = (BorderStyleColor)list.get(0);
                if(borderstylecolor != null)
                {
                    cellborderstyle.setBottomColor(borderstylecolor.getColor());
                    cellborderstyle.setBottomStyle(borderstylecolor.getStyle());
                }
            } else
            if(flag)
            {
                cellborderstyle.setBottomColor(Color.BLACK);
                cellborderstyle.setBottomStyle(0);
            } else
            {
                cellborderstyle.setBottomColor(Color.GRAY);
                cellborderstyle.setBottomStyle(15);
            }
    }

    private static void inspectRight(int i, int j, int k, int l, Hashtable hashtable, CellBorderStyle cellborderstyle, java.util.List list, boolean flag)
    {
        list.clear();
        for(int i1 = i; i1 < i + k; i1++)
        {
            BorderStyleColor borderstylecolor1 = (BorderStyleColor)hashtable.get(new ColumnRow(j + l, i1, true));
            list.add(borderstylecolor1);
        }

        if(!list.isEmpty())
            if(isAllEquals(list))
            {
                BorderStyleColor borderstylecolor = (BorderStyleColor)list.get(0);
                if(borderstylecolor != null)
                {
                    cellborderstyle.setRightColor(borderstylecolor.getColor());
                    cellborderstyle.setRightStyle(borderstylecolor.getStyle());
                }
            } else
            if(flag)
            {
                cellborderstyle.setRightColor(Color.BLACK);
                cellborderstyle.setRightStyle(0);
            } else
            {
                cellborderstyle.setRightColor(Color.GRAY);
                cellborderstyle.setRightStyle(15);
            }
    }

    private static void floatSet(CellBorderStyle cellborderstyle, ElementCase elementcase, Selection selection)
    {
        FloatElement floatelement = elementcase.getFloatElement(((FloatSelection)selection).getSelectedFloatName());
        Style style = floatelement.getStyle();
        cellborderstyle.setTopColor(style.getBorderTopColor());
        cellborderstyle.setTopStyle(style.getBorderTop());
        cellborderstyle.setLeftColor(style.getBorderLeftColor());
        cellborderstyle.setLeftStyle(style.getBorderLeft());
        cellborderstyle.setBottomColor(style.getBorderBottomColor());
        cellborderstyle.setBottomStyle(style.getBorderBottom());
        cellborderstyle.setRightColor(style.getBorderRightColor());
        cellborderstyle.setRightStyle(style.getBorderRight());
    }

    private static void borderStyle(CellBorderStyle cellborderstyle, Object aobj[], int i)
    {
        int j = 0;
        Color color = Color.BLACK;
        if(cellborderstyle.getLeftStyle() != 0 && cellborderstyle.getLeftStyle() != 15)
        {
            j = cellborderstyle.getLeftStyle();
            color = cellborderstyle.getLeftColor();
        } else
        if(cellborderstyle.getTopStyle() != 0 && cellborderstyle.getTopStyle() != 15)
        {
            j = cellborderstyle.getTopStyle();
            color = cellborderstyle.getTopColor();
        } else
        if(cellborderstyle.getBottomStyle() != 0 && cellborderstyle.getBottomStyle() != 15)
        {
            j = cellborderstyle.getBottomStyle();
            color = cellborderstyle.getBottomColor();
        } else
        if(cellborderstyle.getRightStyle() != 0 && cellborderstyle.getRightStyle() != 15)
        {
            j = cellborderstyle.getRightStyle();
            color = cellborderstyle.getRightColor();
        } else
        if(cellborderstyle.getVerticalStyle() != 0 && cellborderstyle.getVerticalStyle() != 15)
        {
            j = cellborderstyle.getVerticalStyle();
            color = cellborderstyle.getVerticalColor();
        } else
        if(cellborderstyle.getHorizontalStyle() != 0 && cellborderstyle.getHorizontalStyle() != 15)
        {
            j = cellborderstyle.getHorizontalStyle();
            color = cellborderstyle.getHorizontalColor();
        }
        aobj[i] = cellborderstyle;
        aobj[i + 1] = insideModel ? ((Object) (Boolean.TRUE)) : ((Object) (Boolean.FALSE));
        aobj[i + 2] = new Integer(j);
        aobj[i + 3] = color;
    }

    private static void checkUp(int i, int j, int k, ElementCase elementcase, Hashtable hashtable)
    {
        if(i - 1 >= 0)
        {
            for(int l = j; l < j + k; l++)
            {
                CellElement cellelement = elementcase.getCellElement(l, i - 1);
                if(cellelement == null)
                    continue;
                Style style = cellelement.getStyle();
                if(style.getBorderBottom() == 0)
                    continue;
                for(int i1 = cellelement.getColumn(); i1 < cellelement.getColumn() + cellelement.getColumnSpan(); i1++)
                    hashtable.put(new ColumnRow(i1, i, false), new BorderStyleColor(style.getBorderBottom(), style.getBorderBottomColor()));

            }

        }
    }

    private static void checkLeft(int i, int j, int k, ElementCase elementcase, Hashtable hashtable)
    {
        if(j - 1 >= 0)
        {
            for(int l = i; l < i + k; l++)
            {
                CellElement cellelement = elementcase.getCellElement(j - 1, l);
                if(cellelement == null)
                    continue;
                Style style = cellelement.getStyle();
                if(style.getBorderRight() == 0)
                    continue;
                for(int i1 = cellelement.getRow(); i1 < cellelement.getRow() + cellelement.getRowSpan(); i1++)
                    hashtable.put(new ColumnRow(j, i1, true), new BorderStyleColor(style.getBorderRight(), style.getBorderRightColor()));

            }

        }
    }

    private static void checkDown(int i, int j, int k, int l, ElementCase elementcase, Hashtable hashtable)
    {
        for(int i1 = j; i1 < j + l; i1++)
        {
            CellElement cellelement = elementcase.getCellElement(i1, i + k);
            if(cellelement == null)
                continue;
            Style style = cellelement.getStyle();
            if(style.getBorderTop() == 0)
                continue;
            for(int j1 = cellelement.getColumn(); j1 < cellelement.getColumn() + cellelement.getColumnSpan(); j1++)
                hashtable.put(new ColumnRow(j1, i + k, false), new BorderStyleColor(style.getBorderTop(), style.getBorderTopColor()));

        }

    }

    private static void checkRight(int i, int j, int k, int l, ElementCase elementcase, Hashtable hashtable)
    {
        for(int i1 = i; i1 < i + k; i1++)
        {
            CellElement cellelement = elementcase.getCellElement(j + l, i1);
            if(cellelement == null)
                continue;
            Style style = cellelement.getStyle();
            if(style.getBorderLeft() == 0)
                continue;
            for(int j1 = cellelement.getRow(); j1 < cellelement.getRow() + cellelement.getRowSpan(); j1++)
                hashtable.put(new ColumnRow(j + l, j1, true), new BorderStyleColor(style.getBorderLeft(), style.getBorderLeftColor()));

        }

    }

    private static void checkAll(int i, int j, int k, int l, ElementCase elementcase, Hashtable hashtable, Hashtable hashtable1, Hashtable hashtable2, 
            Hashtable hashtable3)
    {
        for(int i1 = j; i1 < j + l; i1++)
        {
            for(int j1 = i; j1 < i + k; j1++)
            {
                CellElement cellelement = elementcase.getCellElement(i1, j1);
                if(cellelement == null)
                    continue;
                Style style = cellelement.getStyle();
                if(style.getBorderTop() != 0)
                {
                    for(int k1 = cellelement.getColumn(); k1 < cellelement.getColumn() + cellelement.getColumnSpan(); k1++)
                        hashtable.put(new ColumnRow(k1, cellelement.getRow(), false), new BorderStyleColor(style.getBorderTop(), style.getBorderTopColor()));

                }
                if(style.getBorderLeft() != 0)
                {
                    for(int l1 = cellelement.getRow(); l1 < cellelement.getRow() + cellelement.getRowSpan(); l1++)
                        hashtable2.put(new ColumnRow(cellelement.getColumn(), l1, true), new BorderStyleColor(style.getBorderLeft(), style.getBorderLeftColor()));

                }
                if(style.getBorderBottom() != 0)
                {
                    for(int i2 = cellelement.getColumn(); i2 < cellelement.getColumn() + cellelement.getColumnSpan(); i2++)
                        hashtable1.put(new ColumnRow(i2, cellelement.getRow() + cellelement.getRowSpan(), false), new BorderStyleColor(style.getBorderBottom(), style.getBorderBottomColor()));

                }
                if(style.getBorderRight() == 0)
                    continue;
                for(int j2 = cellelement.getRow(); j2 < cellelement.getRow() + cellelement.getRowSpan(); j2++)
                    hashtable3.put(new ColumnRow(cellelement.getColumn() + cellelement.getColumnSpan(), j2, true), new BorderStyleColor(style.getBorderRight(), style.getBorderRightColor()));

            }

        }

    }

    public static boolean updateCellBorderStyle(ElementCasePane elementcasepane, CellBorderStyle cellborderstyle)
    {
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        Selection selection = elementcasepane.getSelection();
        int i = ((CellSelection)selection).getCellRectangleCount();
        for(int j = 0; j < i; j++)
        {
            Rectangle rectangle = ((CellSelection)selection).getCellRectangle(j);
            int k = rectangle.x;
            int l = rectangle.y;
            int i1 = rectangle.width;
            int j1 = rectangle.height;
            for(int k1 = k; k1 < k + i1; k1++)
            {
                for(int l1 = l; l1 < l + j1; l1++)
                {
                    Object obj = templateelementcase.getTemplateCellElement(k1, l1);
                    if(obj == null)
                    {
                        obj = new DefaultTemplateCellElement(k1, l1);
                        templateelementcase.addCellElement(((TemplateCellElement) (obj)));
                    }
                    Style style = ((TemplateCellElement) (obj)).getStyle();
                    style = updateToStyle(cellborderstyle, style);
                    ((TemplateCellElement) (obj)).setStyle(style);
                }

            }

        }

        return true;
    }

    private static Style updateToStyle(CellBorderStyle cellborderstyle, Style style)
    {
        style = style.deriveBorder(cellborderstyle.getTopStyle(), cellborderstyle.getTopColor(), cellborderstyle.getBottomStyle(), cellborderstyle.getBottomColor(), cellborderstyle.getLeftStyle(), cellborderstyle.getLeftColor(), cellborderstyle.getRightStyle(), cellborderstyle.getRightColor());
        style.deriveBorder(cellborderstyle.getHorizontalStyle(), cellborderstyle.getHorizontalColor(), cellborderstyle.getHorizontalStyle(), cellborderstyle.getHorizontalColor(), cellborderstyle.getVerticalStyle(), cellborderstyle.getVerticalColor(), cellborderstyle.getVerticalStyle(), cellborderstyle.getVerticalColor());
        return style;
    }

    public static boolean update(ElementCasePane elementcasepane, CellBorderStyle cellborderstyle)
    {
        boolean flag = false;
        Object aobj[] = createCellBorderObject(elementcasepane);
        if(aobj == null || aobj.length < NUMBER)
            return false;
        CellBorderStyle cellborderstyle1 = (CellBorderStyle)aobj[0];
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        Selection selection = elementcasepane.getSelection();
        if(selection instanceof FloatSelection)
        {
            FloatElement floatelement = templateelementcase.getFloatElement(((FloatSelection)selection).getSelectedFloatName());
            if(!ComparatorUtils.equals(cellborderstyle1, cellborderstyle))
            {
                flag = true;
                Style style = floatelement.getStyle();
                floatelement.setStyle(style.deriveBorder(cellborderstyle.getTopStyle(), cellborderstyle.getTopColor(), cellborderstyle.getBottomStyle(), cellborderstyle.getBottomColor(), cellborderstyle.getLeftStyle(), cellborderstyle.getLeftColor(), cellborderstyle.getRightStyle(), cellborderstyle.getRightColor()));
            }
            return flag;
        }
        int i = ((CellSelection)selection).getCellRectangleCount();
        for(int j = 0; j < i; j++)
        {
            CellBorderStyle cellborderstyle2 = (CellBorderStyle)aobj[j * NUMBER];
            Rectangle rectangle = ((CellSelection)selection).getCellRectangle(j);
            int k = rectangle.x;
            int l = rectangle.y;
            int i1 = rectangle.width;
            int j1 = rectangle.height;
            if(!ComparatorUtils.equals(cellborderstyle2, cellborderstyle))
            {
                flag = true;
                setStyle(l, k, j1, i1, templateelementcase, cellborderstyle2, cellborderstyle);
            }
        }

        elementcasepane.repaint();
        return flag;
    }

    private static void setStyle(int i, int j, int k, int l, TemplateElementCase templateelementcase, CellBorderStyle cellborderstyle, CellBorderStyle cellborderstyle1)
    {
        for(int i1 = j; i1 < j + l; i1++)
        {
            for(int j1 = i; j1 < i + k; j1++)
            {
                Object obj = templateelementcase.getTemplateCellElement(i1, j1);
                if(obj == null)
                {
                    obj = new DefaultTemplateCellElement(i1, j1);
                    templateelementcase.addCellElement(((TemplateCellElement) (obj)));
                }
                Style style = ((TemplateCellElement) (obj)).getStyle();
                style = inspectStyle(i, j, k, l, cellborderstyle, cellborderstyle1, ((TemplateCellElement) (obj)), style);
                ((TemplateCellElement) (obj)).setStyle(style);
            }

        }

    }

    private static Style inspectStyle(int i, int j, int k, int l, CellBorderStyle cellborderstyle, CellBorderStyle cellborderstyle1, TemplateCellElement templatecellelement, Style style)
    {
        if(templatecellelement.getColumn() == j)
        {
            if(cellborderstyle.getLeftStyle() != cellborderstyle1.getLeftStyle() || !ComparatorUtils.equals(cellborderstyle.getLeftColor(), cellborderstyle1.getLeftColor()))
                style = style.deriveBorderLeft(cellborderstyle1.getLeftStyle(), cellborderstyle1.getLeftColor());
        } else
        if(cellborderstyle.getVerticalStyle() != cellborderstyle1.getVerticalStyle() || !ComparatorUtils.equals(cellborderstyle.getVerticalColor(), cellborderstyle1.getVerticalColor()))
            style = style.deriveBorderLeft(cellborderstyle1.getVerticalStyle(), cellborderstyle1.getVerticalColor());
        if(templatecellelement.getColumn() + templatecellelement.getColumnSpan() == j + l)
        {
            if(cellborderstyle.getRightStyle() != cellborderstyle1.getRightStyle() || !ComparatorUtils.equals(cellborderstyle.getRightColor(), cellborderstyle1.getRightColor()))
                style = style.deriveBorderRight(cellborderstyle1.getRightStyle(), cellborderstyle1.getRightColor());
        } else
        if(cellborderstyle.getVerticalStyle() != cellborderstyle1.getVerticalStyle() || !ComparatorUtils.equals(cellborderstyle.getVerticalColor(), cellborderstyle1.getVerticalColor()))
            style = style.deriveBorderRight(cellborderstyle1.getVerticalStyle(), cellborderstyle1.getVerticalColor());
        if(templatecellelement.getRow() == i)
        {
            if(cellborderstyle.getTopStyle() != cellborderstyle1.getTopStyle() || !ComparatorUtils.equals(cellborderstyle.getTopColor(), cellborderstyle1.getTopColor()))
                style = style.deriveBorderTop(cellborderstyle1.getTopStyle(), cellborderstyle1.getTopColor());
        } else
        if(cellborderstyle.getHorizontalStyle() != cellborderstyle1.getHorizontalStyle() || !ComparatorUtils.equals(cellborderstyle.getHorizontalColor(), cellborderstyle1.getHorizontalColor()))
            style = style.deriveBorderTop(cellborderstyle1.getHorizontalStyle(), cellborderstyle1.getHorizontalColor());
        if(templatecellelement.getRow() + templatecellelement.getRowSpan() == i + k)
        {
            if(cellborderstyle.getBottomStyle() != cellborderstyle1.getBottomStyle() || !ComparatorUtils.equals(cellborderstyle.getBottomColor(), cellborderstyle1.getBottomColor()))
                style = style.deriveBorderBottom(cellborderstyle1.getBottomStyle(), cellborderstyle1.getBottomColor());
        } else
        if(cellborderstyle.getHorizontalStyle() != cellborderstyle1.getHorizontalStyle() || !ComparatorUtils.equals(cellborderstyle.getHorizontalColor(), cellborderstyle1.getHorizontalColor()))
            style = style.deriveBorderBottom(cellborderstyle1.getHorizontalStyle(), cellborderstyle1.getHorizontalColor());
        return style;
    }

    private static boolean isAllEquals(java.util.List list)
    {
        for(int i = 0; i + 1 < list.size(); i++)
            if(!ComparatorUtils.equals(list.get(i), list.get(i + 1)))
                return false;

        return true;
    }

}
