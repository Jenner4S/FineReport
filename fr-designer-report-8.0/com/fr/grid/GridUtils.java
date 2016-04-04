// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.grid;

import com.fr.base.DynamicUnitList;
import com.fr.base.ScreenResolution;
import com.fr.design.cell.clipboard.CellElementsClip;
import com.fr.design.cell.clipboard.ElementsTransferable;
import com.fr.design.cell.clipboard.FloatElementsClip;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.ReportHelper;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.FloatElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellGUIAttr;
import com.fr.report.core.PaintUtils;
import com.fr.report.elementcase.ElementCase;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.ColumnRow;
import com.fr.stable.unit.FU;
import com.fr.stable.unit.PT;
import com.fr.stable.unit.UNIT;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

// Referenced classes of package com.fr.grid:
//            Grid

public class GridUtils
{

    public static final int DRAG_NONE = 0;
    public static final int DRAG_CELLSELECTION = 1;
    public static final int DRAG_CELLSELECTION_BOTTOMRIGHT_CORNER = 2;
    public static final int DRAG_FLOAT = 3;
    public static final int DRAG_CELL_SIZE = 1;
    public static final int DRAG_SELECT_UNITS = 2;

    private GridUtils()
    {
    }

    public static Object[] getAboveFloatElementCursor(ElementCasePane elementcasepane, double d, double d1)
    {
        Object aobj[] = null;
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        Selection selection = elementcasepane.getSelection();
        for(Iterator iterator = templateelementcase.floatIterator(); iterator.hasNext();)
        {
            FloatElement floatelement = (FloatElement)iterator.next();
            double ad[] = caculateFloatElementLocations(floatelement, ReportHelper.getColumnWidthList(templateelementcase), ReportHelper.getRowHeightList(templateelementcase), elementcasepane.getGrid().getVerticalValue(), elementcasepane.getGrid().getHorizontalValue());
            int i = ScreenResolution.getScreenResolution();
            java.awt.geom.Rectangle2D.Double double1 = new java.awt.geom.Rectangle2D.Double(ad[0], ad[1], floatelement.getWidth().toPixD(i), floatelement.getHeight().toPixD(i));
            if(!(selection instanceof FloatSelection) || !ComparatorUtils.equals(floatelement.getName(), ((FloatSelection)selection).getSelectedFloatName()))
            {
                if(double1.contains(d, d1))
                    aobj = (new Object[] {
                        floatelement, new Cursor(13)
                    });
            } else
            {
                Cursor cursor = null;
                Rectangle2D arectangle2d[] = getCornerRect(ad);
                int ai[] = {
                    6, 8, 7, 11, 5, 9, 4, 10
                };
                int j = 0;
                do
                {
                    if(j >= arectangle2d.length)
                        break;
                    if(arectangle2d[j].contains(d, d1))
                    {
                        cursor = new Cursor(ai[j]);
                        break;
                    }
                    j++;
                } while(true);
                if(double1.contains(d, d1) && cursor == null)
                    aobj = (new Object[] {
                        floatelement, new Cursor(13)
                    });
                if(cursor != null)
                    return (new Object[] {
                        floatelement, cursor
                    });
            }
        }

        return aobj;
    }

    private static Rectangle2D[] getCornerRect(double ad[])
    {
        double d = ad[0];
        double d1 = ad[1];
        double d2 = ad[2];
        double d3 = ad[3];
        Rectangle2D arectangle2d[] = {
            new java.awt.geom.Rectangle2D.Double(d - 3D, d1 - 3D, 6D, 6D), new java.awt.geom.Rectangle2D.Double((d + d2) / 2D - 3D, d1 - 3D, 6D, 6D), new java.awt.geom.Rectangle2D.Double(d2 - 3D, d1 - 3D, 6D, 6D), new java.awt.geom.Rectangle2D.Double(d2 - 3D, (d1 + d3) / 2D - 3D, 6D, 6D), new java.awt.geom.Rectangle2D.Double(d2 - 3D, d3 - 3D, 6D, 6D), new java.awt.geom.Rectangle2D.Double((d + d2) / 2D - 3D, d3 - 3D, 6D, 6D), new java.awt.geom.Rectangle2D.Double(d - 3D, d3 - 3D, 6D, 6D), new java.awt.geom.Rectangle2D.Double(d - 3D, (d1 + d3) / 2D - 3D, 6D, 6D)
        };
        return arectangle2d;
    }

    public static double[] caculateFloatElementLocations(FloatElement floatelement, DynamicUnitList dynamicunitlist, DynamicUnitList dynamicunitlist1, int i, int j)
    {
        int k = ScreenResolution.getScreenResolution();
        double d = dynamicunitlist.getRangeValue(j, 0).toPixD(k) + floatelement.getLeftDistance().toPixD(k);
        double d1 = dynamicunitlist1.getRangeValue(i, 0).toPixD(k) + floatelement.getTopDistance().toPixD(k);
        double d2 = d + floatelement.getWidth().toPixD(k);
        double d3 = d1 + floatelement.getHeight().toPixD(k);
        return (new double[] {
            d, d1, d2, d3
        });
    }

    public static ColumnRow getEventColumnRow(ElementCasePane elementcasepane, double d, double d1)
    {
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        DynamicUnitList dynamicunitlist = ReportHelper.getRowHeightList(templateelementcase);
        DynamicUnitList dynamicunitlist1 = ReportHelper.getColumnWidthList(templateelementcase);
        int i = elementcasepane.getGrid().getVerticalValue();
        int j = elementcasepane.getGrid().getHorizontalValue();
        int k = elementcasepane.getGrid().getVerticalBeginValue();
        int l = elementcasepane.getGrid().getHorizontalBeginValue();
        return ColumnRow.valueOf(cc_selected_column_or_row(d, l, j, dynamicunitlist1), cc_selected_column_or_row(d1, k, i, dynamicunitlist));
    }

    private static int cc_selected_column_or_row(double d, int i, int j, DynamicUnitList dynamicunitlist)
    {
        double d1 = 0.0D;
        int k = 0;
        int l = ScreenResolution.getScreenResolution();
        if(d < 0.0D)
        {
            for(k = j; d1 >= d; k--)
                d1 -= dynamicunitlist.get(k).toPixD(l);

        } else
        {
            boolean flag = false;
            int i1 = i;
            do
            {
                if(i1 >= 0)
                    break;
                d1 += dynamicunitlist.get(i1).toPixD(l);
                if(d1 > d)
                {
                    k = i1;
                    flag = true;
                    break;
                }
                i1++;
            } while(true);
            if(!flag)
            {
                k = j;
                do
                {
                    d1 += dynamicunitlist.get(k).toPixD(l);
                    if(d1 > d)
                        break;
                    k++;
                } while(true);
            }
        }
        return k;
    }

    public static ColumnRow getAdjustEventColumnRow(ElementCasePane elementcasepane, double d, double d1)
    {
        ColumnRow columnrow = getEventColumnRow(elementcasepane, d, d1);
        int i = Math.max(columnrow.getColumn(), 0);
        int j = Math.max(columnrow.getRow(), 0);
        return ColumnRow.valueOf(i, j);
    }

    public static boolean canMove(ElementCasePane elementcasepane, int i, int j)
    {
        if(elementcasepane.mustInVisibleRange())
        {
            Grid grid = elementcasepane.getGrid();
            int k = (grid.getVerticalValue() + grid.getVerticalExtent()) - 1;
            int l = (grid.getHorizontalValue() + grid.getHorizontalExtent()) - 1;
            if(i > l)
                return false;
            if(j > k)
                return false;
        }
        return true;
    }

    public static void doSelectCell(ElementCasePane elementcasepane, int i, int j)
    {
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        CellElement cellelement = templateelementcase.getCellElement(i, j);
        if(cellelement == null)
            elementcasepane.setSelection(new CellSelection(i, j, 1, 1));
        else
            elementcasepane.setSelection(new CellSelection(cellelement.getColumn(), cellelement.getRow(), cellelement.getColumnSpan(), cellelement.getRowSpan()));
    }

    public static ElementsTransferable caculateElementsTransferable(ElementCasePane elementcasepane)
    {
        ElementsTransferable elementstransferable = new ElementsTransferable();
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        Selection selection = elementcasepane.getSelection();
        if(selection instanceof FloatSelection)
        {
            FloatSelection floatselection = (FloatSelection)selection;
            FloatElementsClip floatelementsclip = new FloatElementsClip(templateelementcase.getFloatElement(floatselection.getSelectedFloatName()));
            elementstransferable.addObject(floatelementsclip);
        } else
        {
            CellSelection cellselection = (CellSelection)selection;
            ArrayList arraylist = new ArrayList();
            Rectangle rectangle = new Rectangle(cellselection.getColumn(), cellselection.getRow(), cellselection.getColumnSpan(), cellselection.getRowSpan());
            Iterator iterator = templateelementcase.intersect(cellselection.getColumn(), cellselection.getRow(), cellselection.getColumnSpan(), cellselection.getRowSpan());
            do
            {
                if(!iterator.hasNext())
                    break;
                TemplateCellElement templatecellelement = (TemplateCellElement)iterator.next();
                Rectangle rectangle1 = new Rectangle(templatecellelement.getColumn(), templatecellelement.getRow(), templatecellelement.getColumnSpan(), templatecellelement.getRowSpan());
                if(GUICoreUtils.isTheSameRect(rectangle, rectangle1) || rectangle.contains(rectangle1))
                    arraylist.add((TemplateCellElement)templatecellelement.deriveCellElement(templatecellelement.getColumn() - cellselection.getColumn(), templatecellelement.getRow() - cellselection.getRow()));
            } while(true);
            elementstransferable.addObject(new CellElementsClip(cellselection.getColumnSpan(), cellselection.getRowSpan(), (TemplateCellElement[])arraylist.toArray(new TemplateCellElement[arraylist.size()])));
        }
        return elementstransferable;
    }

    public static ColumnRow getAdjustLastColumnRowOfReportPane(ElementCasePane elementcasepane)
    {
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        return ColumnRow.valueOf(Math.max(1, templateelementcase.getColumnCount()), Math.max(1, templateelementcase.getRowCount()));
    }

    public static int getExtentValue(int i, DynamicUnitList dynamicunitlist, double d, int j)
    {
        double d1 = 0.0D;
        int k = 0x7fffffff;
        int l = i;
        do
        {
            if(l > k)
                break;
            d1 += dynamicunitlist.get(l).toPixD(j);
            if(d1 > d)
            {
                i = l;
                for(int i1 = l; dynamicunitlist.get(i1).equal_zero(); i1++)
                    i = i1;

                break;
            }
            l++;
        } while(true);
        return i;
    }

    public static void shrinkToFit(int i, TemplateElementCase templateelementcase, TemplateCellElement templatecellelement)
    {
        DynamicUnitList dynamicunitlist = ReportHelper.getColumnWidthList(templateelementcase);
        DynamicUnitList dynamicunitlist1 = ReportHelper.getRowHeightList(templateelementcase);
        CellGUIAttr cellguiattr = templatecellelement.getCellGUIAttr();
        if(cellguiattr == null)
            cellguiattr = new CellGUIAttr();
        if(cellguiattr.getAdjustMode() == 1 || cellguiattr.getAdjustMode() == 3 && i == 1)
            fitHetght(templatecellelement, dynamicunitlist, dynamicunitlist1);
        else
        if(cellguiattr.getAdjustMode() == 2 || cellguiattr.getAdjustMode() == 3 && i == 2)
            fitWidth(templatecellelement, dynamicunitlist, dynamicunitlist1);
    }

    private static void fitHetght(TemplateCellElement templatecellelement, DynamicUnitList dynamicunitlist, DynamicUnitList dynamicunitlist1)
    {
        int i = templatecellelement.getColumn();
        UNIT unit = PaintUtils.analyzeCellElementPreferredHeight(templatecellelement, dynamicunitlist.getRangeValue(i, i + templatecellelement.getColumnSpan()));
        if(templatecellelement.getRowSpan() == 1)
        {
            dynamicunitlist1.set(templatecellelement.getRow(), UNIT.max(unit, dynamicunitlist1.get(templatecellelement.getRow())));
        } else
        {
            int j = (templatecellelement.getRow() + templatecellelement.getRowSpan()) - 1;
            long l = unit.toFU() - dynamicunitlist1.getRangeValue(templatecellelement.getRow(), j + 1).toFU();
            if(l > 0L)
            {
                for(int k = templatecellelement.getRow(); k <= j; k++)
                    dynamicunitlist1.set(k, FU.getInstance(dynamicunitlist1.get(k).toFU() + l / (long)templatecellelement.getRowSpan()));

            }
        }
    }

    private static void fitWidth(TemplateCellElement templatecellelement, DynamicUnitList dynamicunitlist, DynamicUnitList dynamicunitlist1)
    {
        UNIT unit = PaintUtils.getPreferredWidth(templatecellelement, PT.valueOfFU(dynamicunitlist1.getRangeValue(templatecellelement.getRow(), templatecellelement.getRow() + templatecellelement.getRowSpan()).toFU()));
        if(templatecellelement.getColumnSpan() == 1)
        {
            dynamicunitlist.set(templatecellelement.getColumn(), UNIT.max(unit, dynamicunitlist.get(templatecellelement.getColumn())));
        } else
        {
            int i = (templatecellelement.getColumn() + templatecellelement.getColumnSpan()) - 1;
            long l = unit.toFU() - dynamicunitlist.getRangeValue(templatecellelement.getColumn(), i + 1).toFU();
            if(l > 0L)
            {
                for(int j = templatecellelement.getColumn(); j <= i; j++)
                    dynamicunitlist.set(j, FU.getInstance(dynamicunitlist.get(j).toFU() + l / (long)templatecellelement.getColumnSpan() + 1L));

            }
        }
    }
}
