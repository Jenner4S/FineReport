// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.grid;

import com.fr.base.BaseUtils;
import com.fr.base.DynamicUnitList;
import com.fr.base.FRContext;
import com.fr.base.Formula;
import com.fr.base.GraphHelper;
import com.fr.base.Margin;
import com.fr.base.PaperSize;
import com.fr.base.Utils;
import com.fr.base.background.ColorBackground;
import com.fr.base.background.ImageBackground;
import com.fr.design.constants.UIConstants;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import com.fr.design.roleAuthority.RoleTree;
import com.fr.form.ui.Widget;
import com.fr.general.Background;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.page.PaperSettingProvider;
import com.fr.page.ReportSettingsProvider;
import com.fr.privilege.finegrain.FloatPrivilegeControl;
import com.fr.report.ReportHelper;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.FloatElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellExpandAttr;
import com.fr.report.cell.cellattr.CellGUIAttr;
import com.fr.report.cell.cellattr.CellPageAttr;
import com.fr.report.core.PaintUtils;
import com.fr.report.core.ReportUtils;
import com.fr.report.elementcase.ElementCase;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.report.Report;
import com.fr.report.stable.ReportSettings;
import com.fr.report.worksheet.FormElementCase;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.ColumnRow;
import com.fr.stable.script.CalculatorUtils;
import com.fr.stable.unit.FU;
import com.fr.stable.unit.UNIT;
import com.fr.third.antlr.ANTLRException;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

// Referenced classes of package com.fr.grid:
//            CellElementPainter, DrawFlowRect, Grid

public class GridUI extends ComponentUI
{
    private class DrawHorizontalLineHelper extends DrawLineHelper
    {

        private double realHeight;
        final GridUI this$0;

        protected java.awt.geom.Line2D.Double getPaginateLine2D(int i)
        {
            return new java.awt.geom.Line2D.Double(i, 0.0D, i, gridSize.height);
        }

        protected void setLine2D(int i)
        {
            tmpLine2D.setLine(i, 0.0D, i, realHeight);
        }

        protected void drawLastLine(Graphics2D graphics2d, int i)
        {
            GraphHelper.drawLine(graphics2d, i, 0.0D, i, realHeight);
        }

        DrawHorizontalLineHelper(int i, int j, boolean flag, boolean flag1, DynamicUnitList dynamicunitlist, double d, java.util.List list, double d1, int k)
        {
            this$0 = GridUI.this;
            super(i, j, flag, flag1, dynamicunitlist, d, list, k);
            realHeight = d1;
        }
    }

    private class DrawVerticalLineHelper extends DrawLineHelper
    {

        private double realWidth;
        final GridUI this$0;

        protected java.awt.geom.Line2D.Double getPaginateLine2D(int i)
        {
            return new java.awt.geom.Line2D.Double(0.0D, i, gridSize.width, i);
        }

        protected void setLine2D(int i)
        {
            tmpLine2D.setLine(0.0D, i, realWidth, i);
        }

        protected void drawLastLine(Graphics2D graphics2d, int i)
        {
            GraphHelper.drawLine(graphics2d, 0.0D, i, realWidth, i);
        }

        DrawVerticalLineHelper(int i, int j, boolean flag, boolean flag1, DynamicUnitList dynamicunitlist, double d, java.util.List list, double d1, int k)
        {
            this$0 = GridUI.this;
            super(i, j, flag, flag1, dynamicunitlist, d, list, k);
            realWidth = d1;
        }
    }

    private static abstract class DrawLineHelper
    {

        private int startIndex;
        private int endIndex;
        private boolean showGridLine;
        private boolean showPaginateLine;
        private DynamicUnitList sizeList;
        private double paperPaintSize;
        private java.util.List paginateLineList;
        Line2D tmpLine2D;
        private int resolution;

        protected void iterateStart2End(Graphics2D graphics2d)
        {
            float f = 0.0F;
            float f3 = 0.0F;
            float f4 = 0.0F;
            for(int i = 0; i <= endIndex; i++)
            {
                if(i == 0)
                {
                    i = startIndex;
                    for(int j = 0; j < startIndex; j++)
                    {
                        float f1 = sizeList.get(j).toPixF(resolution);
                        f3 += f1;
                        if((double)f3 >= paperPaintSize)
                            f3 = f1;
                    }

                }
                float f2 = sizeList.get(i).toPixF(resolution);
                f3 += f2;
                if(showGridLine)
                {
                    setLine2D((int)f4);
                    graphics2d.draw(tmpLine2D);
                }
                if(showPaginateLine && (double)f3 >= paperPaintSize)
                {
                    paginateLineList.add(getPaginateLine2D((int)f4));
                    f3 = f2;
                }
                f4 += f2;
            }

            if(showGridLine)
                drawLastLine(graphics2d, (int)f4);
        }

        protected abstract void setLine2D(int i);

        protected abstract java.awt.geom.Line2D.Double getPaginateLine2D(int i);

        protected abstract void drawLastLine(Graphics2D graphics2d, int i);

        DrawLineHelper(int i, int j, boolean flag, boolean flag1, DynamicUnitList dynamicunitlist, double d, 
                java.util.List list, int k)
        {
            tmpLine2D = new java.awt.geom.Line2D.Double(0.0D, 0.0D, 0.0D, 0.0D);
            startIndex = i;
            endIndex = j;
            showGridLine = flag;
            showPaginateLine = flag1;
            sizeList = dynamicunitlist;
            paperPaintSize = d;
            paginateLineList = list;
            resolution = k;
        }
    }


    public static int INVALID_INTEGER = 0x80000000;
    protected Dimension gridSize;
    protected int verticalValue;
    protected int horizontalValue;
    protected double paperPaintWidth;
    protected double paperPaintHeight;
    protected DynamicUnitList rowHeightList;
    protected DynamicUnitList columnWidthList;
    protected int verticalEndValue;
    protected int horizontalEndValue;
    protected DrawFlowRect drawFlowRect;
    protected java.util.List paintCellElementList;
    protected java.util.List paintCellElementRectangleList;
    protected java.util.List paginateLineList;
    protected static Background WHITE_Backgorund;
    protected CellElementPainter painter;
    protected java.awt.geom.Rectangle2D.Double left_col_row_rect;
    protected java.awt.geom.Rectangle2D.Double top_col_row_rect;
    protected java.awt.geom.Rectangle2D.Double back_or_selection_rect;
    protected java.awt.geom.Rectangle2D.Double drag_cell_rect;
    protected java.awt.geom.Rectangle2D.Double cell_back_rect;
    protected java.awt.geom.Rectangle2D.Double tmpRectangle;
    protected int resolution;
    private boolean isAuthority;

    public GridUI(int i)
    {
        paintCellElementList = new ArrayList();
        paintCellElementRectangleList = new ArrayList();
        paginateLineList = new ArrayList();
        painter = new CellElementPainter();
        left_col_row_rect = new java.awt.geom.Rectangle2D.Double(0.0D, 0.0D, 0.0D, 0.0D);
        top_col_row_rect = new java.awt.geom.Rectangle2D.Double(0.0D, 0.0D, 0.0D, 0.0D);
        back_or_selection_rect = new java.awt.geom.Rectangle2D.Double(0.0D, 0.0D, 0.0D, 0.0D);
        drag_cell_rect = new java.awt.geom.Rectangle2D.Double(0.0D, 0.0D, 0.0D, 0.0D);
        cell_back_rect = new java.awt.geom.Rectangle2D.Double(0.0D, 0.0D, 0.0D, 0.0D);
        tmpRectangle = new java.awt.geom.Rectangle2D.Double(INVALID_INTEGER, INVALID_INTEGER, INVALID_INTEGER, INVALID_INTEGER);
        isAuthority = false;
        resolution = i;
    }

    protected ReportSettingsProvider getReportSettings(ElementCase elementcase)
    {
        if(elementcase instanceof Report)
            return ReportUtils.getReportSettings((Report)elementcase);
        if(elementcase instanceof FormElementCase)
            return ((FormElementCase)elementcase).getReportSettings();
        else
            return new ReportSettings();
    }

    protected void paintBackground(Graphics g, Grid grid, ElementCase elementcase, int i)
    {
        Graphics2D graphics2d = (Graphics2D)g;
        back_or_selection_rect.setRect(0.0D, 0.0D, gridSize.getWidth(), gridSize.getHeight());
        clearBackground(graphics2d, grid);
        paperPaintWidth = 0.0D;
        paperPaintHeight = 0.0D;
        ReportSettingsProvider reportsettingsprovider = getReportSettings(elementcase);
        PaperSettingProvider papersettingprovider = reportsettingsprovider.getPaperSetting();
        if(grid.isShowPaginateLine())
        {
            PaperSize papersize = papersettingprovider.getPaperSize();
            Margin margin = papersettingprovider.getMargin();
            double d = papersize.getWidth().toPixD(i);
            double d1 = papersize.getHeight().toPixD(i);
            if(papersettingprovider.getOrientation() == 1)
            {
                d = papersize.getHeight().toPixD(i);
                d1 = papersize.getWidth().toPixD(i);
            }
            paperPaintWidth = d - margin.getLeft().toPixD(i) - margin.getRight().toPixD(i);
            paperPaintHeight = d1 - margin.getTop().toPixD(i) - margin.getBottom().toPixD(i) - reportsettingsprovider.getHeaderHeight().toPixD(i) - reportsettingsprovider.getFooterHeight().toPixD(i);
        }
        Background background = reportsettingsprovider.getBackground();
        if(background != null)
        {
            if(grid.isEnabled() && !(background instanceof ImageBackground))
                background.paint(graphics2d, back_or_selection_rect);
            paintScrollBackground(graphics2d, grid, background, papersettingprovider, reportsettingsprovider);
        }
    }

    private void clearBackground(Graphics2D graphics2d, Grid grid)
    {
        if(grid.isEnabled())
            graphics2d.setPaint(Color.WHITE);
        else
            graphics2d.setPaint(UIManager.getColor("control"));
        GraphHelper.fill(graphics2d, back_or_selection_rect);
    }

    private void paintScrollBackground(Graphics2D graphics2d, Grid grid, Background background, PaperSettingProvider papersettingprovider, ReportSettingsProvider reportsettingsprovider)
    {
        boolean flag = grid.isEditable() || isAuthority;
        if(flag && (background instanceof ImageBackground))
        {
            if(!grid.isShowPaginateLine())
                calculatePaper(papersettingprovider, reportsettingsprovider);
            ImageBackground imagebackground = (ImageBackground)background;
            int i = columnWidthList.getRangeValue(0, horizontalValue).toPixI(resolution);
            int j = rowHeightList.getRangeValue(0, verticalValue).toPixI(resolution);
            for(int k = 0; (double)k * paperPaintWidth < gridSize.getWidth(); k++)
            {
                for(int l = 0; (double)l * paperPaintHeight < gridSize.getHeight(); l++)
                {
                    back_or_selection_rect.setRect((double)k * paperPaintWidth, (double)l * paperPaintHeight, paperPaintWidth, paperPaintHeight);
                    imagebackground.paint4Scroll(graphics2d, back_or_selection_rect, i, j);
                }

            }

            back_or_selection_rect.setRect(0.0D, 0.0D, gridSize.getWidth(), gridSize.getHeight());
        }
    }

    private void calculatePaper(PaperSettingProvider papersettingprovider, ReportSettingsProvider reportsettingsprovider)
    {
        PaperSize papersize = papersettingprovider.getPaperSize();
        Margin margin = papersettingprovider.getMargin();
        double d = papersize.getWidth().toPixD(resolution);
        double d1 = papersize.getHeight().toPixD(resolution);
        if(papersettingprovider.getOrientation() == 1 && papersize.getWidth().toPixD(resolution) < papersize.getHeight().toPixD(resolution))
        {
            d = Math.max(papersize.getWidth().toPixD(resolution), papersize.getHeight().toPixD(resolution));
            d1 = Math.min(papersize.getWidth().toPixD(resolution), papersize.getHeight().toPixD(resolution));
        } else
        if(papersettingprovider.getOrientation() == 0 && papersize.getWidth().toPixD(resolution) > papersize.getHeight().toPixD(resolution))
        {
            d = Math.min(papersize.getWidth().toPixD(resolution), papersize.getHeight().toPixD(resolution));
            d1 = Math.max(papersize.getWidth().toPixD(resolution), papersize.getHeight().toPixD(resolution));
        }
        paperPaintWidth = d - margin.getLeft().toPixD(resolution) - margin.getRight().toPixD(resolution);
        paperPaintHeight = d1 - margin.getTop().toPixD(resolution) - margin.getBottom().toPixD(resolution) - reportsettingsprovider.getHeaderHeight().toPixD(resolution) - reportsettingsprovider.getFooterHeight().toPixD(resolution);
    }

    private void paintGridLine(Graphics g, Grid grid, double d, double d1, int i)
    {
        Graphics2D graphics2d = (Graphics2D)g;
        graphics2d.setPaint(grid.getGridLineColor());
        GraphHelper.setStroke(graphics2d, GraphHelper.getStroke(1));
        paginateLineList.clear();
        (new DrawVerticalLineHelper(grid.getVerticalBeginValue(), verticalEndValue, grid.isShowGridLine(), grid.isShowPaginateLine(), rowHeightList, paperPaintHeight, paginateLineList, d, i)).iterateStart2End(graphics2d);
        (new DrawHorizontalLineHelper(grid.getHorizontalBeginValue(), horizontalEndValue, grid.isShowGridLine(), grid.isShowPaginateLine(), columnWidthList, paperPaintWidth, paginateLineList, d1, i)).iterateStart2End(graphics2d);
    }

    public void finalize()
    {
        try
        {
            super.finalize();
            if(drawFlowRect != null)
                drawFlowRect.exit();
        }
        catch(Throwable throwable)
        {
            FRContext.getLogger().error(throwable.getMessage(), throwable);
        }
    }

    private void paintCellElements(Graphics g, Grid grid, TemplateElementCase templateelementcase, int i)
    {
        Graphics2D graphics2d = (Graphics2D)g;
        CellElement cellelement = null;
        ElementCasePane elementcasepane = grid.getElementCasePane();
        Selection selection = elementcasepane.getSelection();
        if(selection instanceof CellSelection)
            cellelement = templateelementcase.getCellElement(((CellSelection)selection).getColumn(), ((CellSelection)selection).getRow());
        int j = grid.getHorizontalBeginValue();
        int k = grid.getVerticalBeginValue();
        Shape shape = null;
        TemplateCellElement templatecellelement = null;
        Iterator iterator = templateelementcase.intersect(j, k, horizontalEndValue - j, verticalEndValue - k);
        double d = columnWidthList.getRangeValue(0, horizontalValue).toPixD(i);
        double d1 = rowHeightList.getRangeValue(0, verticalValue).toPixD(i);
        left_col_row_rect.setRect(0.0D, 0.0D, 0.0D, 0.0D);
        top_col_row_rect.setRect(0.0D, 0.0D, 0.0D, 0.0D);
        paintDetailedCellElements(graphics2d, iterator, templatecellelement, elementcasepane, cellelement, d, d1, shape, templateelementcase);
        paintBorder(graphics2d, templatecellelement, templateelementcase);
        paintFatherLeft(graphics2d);
    }

    private void paintDetailedCellElements(Graphics2D graphics2d, Iterator iterator, TemplateCellElement templatecellelement, ElementCasePane elementcasepane, CellElement cellelement, double d, 
            double d1, Shape shape, TemplateElementCase templateelementcase)
    {
        do
        {
            if(!iterator.hasNext())
                break;
            templatecellelement = (TemplateCellElement)iterator.next();
            if(templatecellelement != null)
            {
                calculateForcedPagingOfCellElement(elementcasepane, templatecellelement, d, d1);
                storeFatherLocation(cellelement, templatecellelement);
                caculateScrollVisibleBounds(tmpRectangle, templatecellelement.getColumn(), templatecellelement.getRow(), templatecellelement.getColumnSpan(), templatecellelement.getRowSpan());
                shape = graphics2d.getClip();
                graphics2d.clip(tmpRectangle);
                graphics2d.translate(tmpRectangle.getX() + 1.0D, tmpRectangle.getY() + 1.0D);
                cell_back_rect.setRect(0.0D, 0.0D, tmpRectangle.getWidth() - 1.0D, tmpRectangle.getHeight() - 1.0D);
                if(templatecellelement.getColumnSpan() > 1 || templatecellelement.getRowSpan() > 1)
                    WHITE_Backgorund.paint(graphics2d, cell_back_rect);
                paintCellElementList.add(templatecellelement);
                paintCellElementRectangleList.add(tmpRectangle.clone());
                int i = (int)tmpRectangle.getWidth();
                int j = (int)tmpRectangle.getHeight();
                painter.paintBackground(graphics2d, templateelementcase, templatecellelement, i, j);
                painter.paintContent(graphics2d, templateelementcase, templatecellelement, i, j, resolution);
                graphics2d.translate(-tmpRectangle.getX() - 1.0D, -tmpRectangle.getY() - 1.0D);
                paintAuthorityCell(graphics2d, templatecellelement);
                graphics2d.setClip(shape);
            }
        } while(true);
    }

    private void paintAuthorityCell(Graphics2D graphics2d, TemplateCellElement templatecellelement)
    {
        String s = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        boolean flag = false;
        if(templatecellelement.getWidget() != null)
            flag = templatecellelement.getWidget().isDirtyWidget(s);
        boolean flag1 = templatecellelement.isDoneAuthority(s) || templatecellelement.isDoneNewValueAuthority(s);
        boolean flag2 = flag || flag1;
        if(isAuthority && flag2)
        {
            Composite composite = graphics2d.getComposite();
            graphics2d.setComposite(AlphaComposite.getInstance(3, 0.7F));
            java.awt.Paint paint1 = graphics2d.getPaint();
            graphics2d.setPaint(UIConstants.AUTHORITY_COLOR);
            GraphHelper.fill(graphics2d, tmpRectangle);
            graphics2d.setComposite(composite);
            graphics2d.setPaint(paint1);
        }
    }

    private void storeFatherLocation(CellElement cellelement, TemplateCellElement templatecellelement)
    {
        if(cellelement == templatecellelement)
        {
            CellExpandAttr cellexpandattr = templatecellelement.getCellExpandAttr();
            if(cellexpandattr != null)
            {
                ColumnRow columnrow = cellexpandattr.getLeftParentColumnRow();
                if(ColumnRow.validate(columnrow))
                    caculateScrollVisibleBounds(left_col_row_rect, columnrow.getColumn(), columnrow.getRow(), 1, 1);
                ColumnRow columnrow1 = cellexpandattr.getUpParentColumnRow();
                if(ColumnRow.validate(columnrow1))
                    caculateScrollVisibleBounds(top_col_row_rect, columnrow1.getColumn(), columnrow1.getRow(), 1, 1);
            }
        }
    }

    private void paintBorder(Graphics2D graphics2d, TemplateCellElement templatecellelement, TemplateElementCase templateelementcase)
    {
        for(int i = 0; i < paintCellElementList.size(); i++)
        {
            templatecellelement = (TemplateCellElement)paintCellElementList.get(i);
            java.awt.geom.Rectangle2D.Double double1 = (java.awt.geom.Rectangle2D.Double)paintCellElementRectangleList.get(i);
            CellGUIAttr cellguiattr = templatecellelement.getCellGUIAttr();
            if(cellguiattr == null)
                cellguiattr = CellGUIAttr.DEFAULT_CELLGUIATTR;
            graphics2d.translate(double1.getX(), double1.getY());
            painter.paintBorder(graphics2d, templateelementcase, templatecellelement, double1.getWidth(), double1.getHeight());
            graphics2d.translate(-double1.getX(), -double1.getY());
        }

    }

    private void paintFatherLeft(Graphics2D graphics2d)
    {
        if(validate(left_col_row_rect) && left_col_row_rect.getHeight() > 5D)
        {
            graphics2d.setPaint(Color.BLUE);
            double d = left_col_row_rect.getX() + 4D;
            double d2 = left_col_row_rect.getY() + left_col_row_rect.getHeight() / 2D;
            GraphHelper.drawLine(graphics2d, d, d2 - 5D, d, d2 + 5D);
            GeneralPath generalpath = new GeneralPath(0, 3);
            generalpath.moveTo((float)d, (float)d2 + 5F);
            generalpath.lineTo((float)d + 3F, ((float)d2 + 5F) - 4F);
            generalpath.lineTo((float)d - 2.0F, ((float)d2 + 5F) - 4F);
            GraphHelper.fill(graphics2d, generalpath);
        }
        if(validate(top_col_row_rect) && top_col_row_rect.getWidth() > 5D)
        {
            graphics2d.setPaint(Color.BLUE);
            double d1 = top_col_row_rect.getX() + top_col_row_rect.getWidth() / 2D;
            double d3 = top_col_row_rect.getY() + 4D;
            GraphHelper.drawLine(graphics2d, d1 - 5D, d3, d1 + 5D, d3);
            GeneralPath generalpath1 = new GeneralPath(0, 3);
            generalpath1.moveTo((float)d1 + 5F, (float)d3);
            generalpath1.lineTo(((float)d1 + 5F) - 4F, (float)d3 + 3F);
            generalpath1.lineTo(((float)d1 + 5F) - 4F, (float)d3 - 3F);
            GraphHelper.fill(graphics2d, generalpath1);
        }
    }

    private void paintPaginateLines(Graphics g, Grid grid)
    {
        JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if(!jtemplate.isJWorkBook())
            return;
        Graphics2D graphics2d = (Graphics2D)g;
        if(paginateLineList.size() > 0)
        {
            java.awt.geom.Line2D.Double double1 = new java.awt.geom.Line2D.Double(0.0D, 0.0D, 0.0D, 0.0D);
            for(int i = 0; i < paginateLineList.size(); i++)
            {
                Line2D line2d = (Line2D)paginateLineList.get(i);
                for(int k = i + 1; k < paginateLineList.size(); k++)
                {
                    Line2D line2d1 = (Line2D)paginateLineList.get(k);
                    if(line2d1.getX1() == line2d.getX1() && line2d1.getX2() == line2d.getX2() && line2d1.getY1() == line2d.getY1() && line2d1.getY2() == line2d.getY2())
                        paginateLineList.remove(k);
                }

            }

            graphics2d.setPaint(grid.getPaginationLineColor());
            GraphHelper.setStroke(graphics2d, GraphHelper.getStroke(9));
            int j = 0;
            for(int l = paginateLineList.size(); j < l; j++)
                graphics2d.draw((Shape)paginateLineList.get(j));

            graphics2d.setPaintMode();
        }
    }

    private void paintGridSelection(Graphics g, Grid grid, ElementCase elementcase)
    {
        Graphics2D graphics2d = (Graphics2D)g;
        Selection selection = grid.getElementCasePane().getSelection();
        if(selection instanceof CellSelection)
        {
            CellSelection cellselection = (CellSelection)selection;
            Rectangle rectangle = cellselection.getEditRectangle();
            int i = cellselection.getCellRectangleCount();
            Area area = new Area();
            Area area1 = new Area();
            ElementCasePane elementcasepane = grid.getElementCasePane();
            if(DesignerContext.getFormatState() == 0)
            {
                caculateScrollVisibleBounds(tmpRectangle, new Rectangle(cellselection.getColumn(), cellselection.getRow(), cellselection.getColumnSpan(), cellselection.getRowSpan()));
                if(validate(tmpRectangle))
                    area1.add(new Area(tmpRectangle));
                if(i == 1)
                    paintOne(area, cellselection, grid, graphics2d);
                else
                    paintMore(area, cellselection, grid, graphics2d, i, area1);
            } else
            {
                Rectangle rectangle1 = null;
                rectangle1 = paintReferenceCell(elementcasepane, grid, graphics2d, area1, area, rectangle1);
                paintFormatArea(area, cellselection, grid, graphics2d, rectangle1);
            }
            paintGridSelectionForFormula(graphics2d, elementcase, cellselection);
        }
    }

    private Rectangle paintReferenceCell(ElementCasePane elementcasepane, Grid grid, Graphics2D graphics2d, Area area, Area area1, Rectangle rectangle)
    {
        CellSelection cellselection = elementcasepane.getFormatReferencedCell();
        if(cellselection == null)
            return null;
        if(DesignerContext.getReferencedIndex() != HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().getEditingReportIndex())
            return null;
        rectangle = new Rectangle(cellselection.getColumn(), cellselection.getRow(), cellselection.getColumnSpan(), cellselection.getRowSpan());
        caculateScrollVisibleBounds(tmpRectangle, rectangle);
        if(validate(tmpRectangle))
            area.add(new Area(tmpRectangle));
        paintOne(area1, cellselection, grid, graphics2d);
        return rectangle;
    }

    private void paintFormatArea(Area area, CellSelection cellselection, Grid grid, Graphics2D graphics2d, Rectangle rectangle)
    {
        Rectangle rectangle1 = new Rectangle(cellselection.getColumn(), cellselection.getRow(), cellselection.getColumnSpan(), cellselection.getRowSpan());
        if(ComparatorUtils.equals(DesignerContext.getReferencedElementCasePane(), grid.getElementCasePane()) && ComparatorUtils.equals(rectangle1, rectangle))
            return;
        caculateScrollVisibleBounds(tmpRectangle, rectangle1);
        if(validate(tmpRectangle))
        {
            area = new Area(tmpRectangle);
            double d = tmpRectangle.getX();
            double d1 = tmpRectangle.getY();
            double d2 = tmpRectangle.getWidth();
            double d3 = tmpRectangle.getHeight();
            if(cellselection.getRowSpan() > 1 || cellselection.getColumnSpan() > 1)
            {
                Composite composite = graphics2d.getComposite();
                graphics2d.setComposite(AlphaComposite.getInstance(3, 0.5F));
                graphics2d.setPaint(grid.getSelectedBackground());
                GraphHelper.fill(graphics2d, area);
                graphics2d.setComposite(composite);
            }
            paintNormal(graphics2d, d, d1, d2, d3, grid);
        }
    }

    private void paintOne(Area area, CellSelection cellselection, Grid grid, Graphics2D graphics2d)
    {
        if(validate(tmpRectangle))
        {
            area = new Area(tmpRectangle);
            double d = tmpRectangle.getX();
            double d1 = tmpRectangle.getY();
            double d2 = tmpRectangle.getWidth();
            double d3 = tmpRectangle.getHeight();
            if(cellselection.getRowSpan() > 1 || cellselection.getColumnSpan() > 1)
            {
                Composite composite = graphics2d.getComposite();
                graphics2d.setComposite(AlphaComposite.getInstance(3, 0.5F));
                graphics2d.setPaint(grid.getSelectedBackground());
                GraphHelper.fill(graphics2d, area);
                graphics2d.setComposite(composite);
            }
            paintGridSelectionDependsOnTableSelectionPane(graphics2d, d, d1, d2, d3, grid);
        }
    }

    private void paintMore(Area area, CellSelection cellselection, Grid grid, Graphics2D graphics2d, int i, Area area1)
    {
        area = new Area();
        for(int j = 0; j < i; j++)
        {
            Rectangle rectangle = cellselection.getCellRectangle(j);
            caculateScrollVisibleBounds(tmpRectangle, rectangle);
            if(validate(tmpRectangle))
            {
                area.add(new Area(tmpRectangle));
                paintGridSelectionDependsOnTableSelectionPane(graphics2d, tmpRectangle.x, tmpRectangle.y, tmpRectangle.width, tmpRectangle.height, grid);
            }
        }

        area.add(area1);
        area.exclusiveOr(area1);
        Composite composite = graphics2d.getComposite();
        graphics2d.setComposite(AlphaComposite.getInstance(3, 0.5F));
        graphics2d.setPaint(grid.getSelectedBackground());
        GraphHelper.fill(graphics2d, area);
        graphics2d.setComposite(composite);
        graphics2d.setPaint(Color.blue);
        if(area1 != null)
            GraphHelper.draw(graphics2d, area1);
    }

    private void paintGridSelectionForFormula(Graphics2D graphics2d, ElementCase elementcase, CellSelection cellselection)
    {
        if(elementcase.getCellValue(cellselection.getColumn(), cellselection.getRow()) instanceof Formula)
        {
            Formula formula = (Formula)elementcase.getCellValue(cellselection.getColumn(), cellselection.getRow());
            String s = formula.getContent();
            ColumnRow acolumnrow[] = new ColumnRow[0];
            try
            {
                acolumnrow = CalculatorUtils.relatedColumnRowArray(s.substring(1));
            }
            catch(ANTLRException antlrexception) { }
            Area area = null;
            for(int i = 0; i < acolumnrow.length; i++)
            {
                ColumnRow columnrow = acolumnrow[i];
                int j = 1;
                int k = 1;
                CellElement cellelement = elementcase.getCellElement(columnrow.getColumn(), columnrow.getRow());
                if(cellelement != null)
                {
                    j = cellelement.getColumnSpan();
                    k = cellelement.getRowSpan();
                    if(j > 1 || k > 1)
                        columnrow = ColumnRow.valueOf(cellelement.getColumn(), cellelement.getRow());
                }
                caculateScrollVisibleBounds(tmpRectangle, columnrow.getColumn(), columnrow.getRow(), j, k);
                if(validate(tmpRectangle))
                    paintFormulaCellArea(graphics2d, area, i);
            }

        }
    }

    private void paintFormulaCellArea(Graphics2D graphics2d, Area area, int i)
    {
        area = new Area(new java.awt.geom.Rectangle2D.Double(tmpRectangle.getX(), tmpRectangle.getY(), tmpRectangle.getWidth(), tmpRectangle.getHeight()));
        area.exclusiveOr(new Area(new java.awt.geom.Rectangle2D.Double(tmpRectangle.getX() + 1.0D, tmpRectangle.getY() + 1.0D, tmpRectangle.getWidth() - 2D, tmpRectangle.getHeight() - 2D)));
        area.add(new Area(new java.awt.geom.Rectangle2D.Double(tmpRectangle.getX(), tmpRectangle.getY(), 3D, 3D)));
        area.add(new Area(new java.awt.geom.Rectangle2D.Double((tmpRectangle.getX() + tmpRectangle.getWidth()) - 3D, tmpRectangle.getY(), 3D, 3D)));
        area.add(new Area(new java.awt.geom.Rectangle2D.Double(tmpRectangle.getX(), (tmpRectangle.getY() + tmpRectangle.getHeight()) - 3D, 3D, 3D)));
        area.add(new Area(new java.awt.geom.Rectangle2D.Double((tmpRectangle.getX() + tmpRectangle.getWidth()) - 3D, (tmpRectangle.getY() + tmpRectangle.getHeight()) - 3D, 3D, 3D)));
        graphics2d.setPaint(new Color(((i + 2) * 50) % 256, ((i + 1) * 50) % 256, (i * 50) % 256));
        if(area != null)
            GraphHelper.fill(graphics2d, area);
    }

    private void paintGridSelectionDependsOnTableSelectionPane(Graphics2D graphics2d, double d, double d1, double d2, 
            double d3, Grid grid)
    {
        if(grid.IsNotShowingTableSelectPane())
        {
            paintNormal(graphics2d, d, d1, d2, d3, grid);
        } else
        {
            Stroke stroke = graphics2d.getStroke();
            graphics2d.setStroke(new BasicStroke(1.0F));
            if(drawFlowRect == null)
                drawFlowRect = new DrawFlowRect();
            drawFlowRect.setGrid(grid);
            drawFlowRect.drawFlowRect(graphics2d, (int)d, (int)d1, (int)d2 + (int)d, (int)d3 + (int)d1);
            graphics2d.setStroke(stroke);
        }
    }

    private void paintNormal(Graphics2D graphics2d, double d, double d1, double d2, 
            double d3, Grid grid)
    {
        back_or_selection_rect.setRect(d - 1.0D, d1 - 1.0D, d2 + 3D, d3 + 3D);
        Area area = new Area(back_or_selection_rect);
        back_or_selection_rect.setRect(d + 2D, d1 + 2D, d2 - 3D, d3 - 3D);
        area.exclusiveOr(new Area(back_or_selection_rect));
        back_or_selection_rect.setRect((d + d2) - 1.0D, (d1 + d3) - 3D, 3D, 1.0D);
        area.exclusiveOr(new Area(back_or_selection_rect));
        back_or_selection_rect.setRect((d + d2) - 3D, (d1 + d3) - 1.0D, 1.0D, 3D);
        area.exclusiveOr(new Area(back_or_selection_rect));
        back_or_selection_rect.setRect((d + d2) - 2D, (d1 + d3) - 2D, 5D, 5D);
        area.add(new Area(back_or_selection_rect));
        graphics2d.setPaint(grid.getSelectedBorderLineColor());
        GraphHelper.fill(graphics2d, area);
        graphics2d.setPaintMode();
    }

    private void paintFloatElements(Graphics g, Grid grid, ElementCase elementcase, int i)
    {
        Graphics2D graphics2d = (Graphics2D)g;
        Selection selection = grid.getElementCasePane().getSelection();
        Iterator iterator = elementcase.floatIterator();
        do
        {
            if(!iterator.hasNext())
                break;
            FloatElement floatelement = (FloatElement)iterator.next();
            int j = rowHeightList.getValueIndex(FU.getInstance(floatelement.getTopDistance().toFU() + floatelement.getHeight().toFU()));
            if(isSuitablePosition(j))
            {
                float f = columnWidthList.getRangeValue(horizontalValue, 0).toPixF(i) + floatelement.getLeftDistance().toPixF(i);
                float f1 = rowHeightList.getRangeValue(verticalValue, 0).toPixF(i) + floatelement.getTopDistance().toPixF(i);
                graphics2d.translate(f, f1);
                PaintUtils.paintFloatElement(graphics2d, floatelement, floatelement.getWidth().toPixI(i), floatelement.getHeight().toPixI(i), i);
                graphics2d.translate(-f, -f1);
            }
        } while(true);
        paintFloatElementsBorder(graphics2d, grid, selection, iterator, elementcase);
        paintAuthorityFloatElement(graphics2d, elementcase, ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName(), grid);
    }

    private boolean isSuitablePosition(int i)
    {
        return 0 >= verticalValue || 0 <= verticalEndValue || i >= verticalValue || i <= verticalValue;
    }

    private void paintFloatElementsBorder(Graphics2D graphics2d, Grid grid, Selection selection, Iterator iterator, ElementCase elementcase)
    {
        if(selection instanceof FloatSelection)
        {
            iterator = elementcase.floatIterator();
            do
            {
                if(!iterator.hasNext())
                    break;
                FloatElement floatelement = (FloatElement)iterator.next();
                if(ComparatorUtils.equals(floatelement.getName(), ((FloatSelection)selection).getSelectedFloatName()))
                {
                    Rectangle2D arectangle2d[] = calculateFloatElementPoints(floatelement);
                    GraphHelper.setStroke(graphics2d, GraphHelper.getStroke(1));
                    int i = 0;
                    while(i < arectangle2d.length) 
                    {
                        graphics2d.setPaint(Utils.getXORColor(grid.getSelectedBorderLineColor()));
                        GraphHelper.fill(graphics2d, arectangle2d[i]);
                        graphics2d.setPaint(grid.getSelectedBorderLineColor());
                        GraphHelper.draw(graphics2d, arectangle2d[i]);
                        i++;
                    }
                }
            } while(true);
        }
    }

    private Rectangle2D[] calculateFloatElementPoints(FloatElement floatelement)
    {
        float f = columnWidthList.getRangeValue(horizontalValue, 0).toPixF(resolution) + floatelement.getLeftDistance().toPixF(resolution);
        float f1 = rowHeightList.getRangeValue(verticalValue, 0).toPixF(resolution) + floatelement.getTopDistance().toPixF(resolution);
        int i = (int)(f + floatelement.getWidth().toPixF(resolution));
        int j = (int)(f1 + floatelement.getHeight().toPixF(resolution));
        return (new Rectangle2D[] {
            new java.awt.geom.Rectangle2D.Double(f - 3F, f1 - 3F, 6D, 6D), new java.awt.geom.Rectangle2D.Double((f + (float)i) / 2.0F - 3F, f1 - 3F, 6D, 6D), new java.awt.geom.Rectangle2D.Double(i - 3, f1 - 3F, 6D, 6D), new java.awt.geom.Rectangle2D.Double(i - 3, (f1 + (float)j) / 2.0F - 3F, 6D, 6D), new java.awt.geom.Rectangle2D.Double(i - 3, j - 3, 6D, 6D), new java.awt.geom.Rectangle2D.Double((f + (float)i) / 2.0F - 3F, j - 3, 6D, 6D), new java.awt.geom.Rectangle2D.Double(f - 3F, j - 3, 6D, 6D), new java.awt.geom.Rectangle2D.Double(f - 3F, (f1 + (float)j) / 2.0F - 3F, 6D, 6D)
        });
    }

    private void paintAuthorityFloatElement(Graphics2D graphics2d, ElementCase elementcase, String s, Grid grid)
    {
        Iterator iterator = elementcase.floatIterator();
        do
        {
            if(!iterator.hasNext())
                break;
            FloatElement floatelement = (FloatElement)iterator.next();
            if(floatelement.getFloatPrivilegeControl().checkInvisible(s))
            {
                float f = columnWidthList.getRangeValue(horizontalValue, 0).toPixF(resolution) + floatelement.getLeftDistance().toPixF(resolution);
                float f1 = rowHeightList.getRangeValue(verticalValue, 0).toPixF(resolution) + floatelement.getTopDistance().toPixF(resolution);
                int i = (int)(f + floatelement.getWidth().toPixF(resolution));
                int j = (int)(f1 + floatelement.getHeight().toPixF(resolution));
                double d = f;
                double d1 = f1;
                double d2 = (double)i - d;
                double d3 = (double)j - d1;
                if(isAuthority)
                    paintAuthorityFloatElementBorder(graphics2d, d, d1, d2, d3, grid);
            }
        } while(true);
    }

    private void paintAuthorityFloatElementBorder(Graphics2D graphics2d, double d, double d1, double d2, 
            double d3, Grid grid)
    {
        back_or_selection_rect.setRect(d - 1.0D, d1 - 1.0D, d2 + 3D, d3 + 3D);
        Area area = new Area(back_or_selection_rect);
        graphics2d.setXORMode(Utils.getXORColor(grid.getSelectedBorderLineColor()));
        graphics2d.setPaint(UIConstants.AUTHORITY_COLOR);
        graphics2d.setComposite(AlphaComposite.getInstance(3, 0.7F));
        GraphHelper.fill(graphics2d, area);
        graphics2d.setPaintMode();
    }

    private void paintDragCellBorder(Graphics g, Grid grid)
    {
        Graphics2D graphics2d = (Graphics2D)g;
        if((grid.getDragType() == 1 || grid.getDragType() == 2) && grid.getDragRectangle() != null)
        {
            caculateScrollVisibleBounds(drag_cell_rect, grid.getDragRectangle());
            graphics2d.setPaint(Color.GRAY);
            GraphHelper.draw(graphics2d, drag_cell_rect, 5);
        }
    }

    public void paint(Graphics g, JComponent jcomponent)
    {
        if(!(jcomponent instanceof Grid))
            throw new IllegalArgumentException("The component c to paint must be a Grid!");
        isAuthority = BaseUtils.isAuthorityEditing();
        Graphics2D graphics2d = (Graphics2D)g;
        Grid grid = (Grid)jcomponent;
        ElementCasePane elementcasepane = grid.getElementCasePane();
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        dealWithSizeBeforePaint(grid, templateelementcase);
        double d = gridSize.getWidth();
        double d1 = gridSize.getHeight();
        paintBackground(graphics2d, grid, templateelementcase, resolution);
        paintGridLine(graphics2d, grid, d, d1, resolution);
        paintCellElementList.clear();
        paintCellElementRectangleList.clear();
        paintCellElements(graphics2d, grid, templateelementcase, resolution);
        paintPaginateLines(graphics2d, grid);
        if(!isAuthority || !(templateelementcase instanceof WorkSheet) || ((WorkSheet)templateelementcase).isPaintSelection())
            paintGridSelection(graphics2d, grid, templateelementcase);
        paintFloatElements(graphics2d, grid, templateelementcase, resolution);
        paintDragCellBorder(graphics2d, grid);
        grid.ajustEditorComponentBounds();
    }

    private void dealWithSizeBeforePaint(Grid grid, TemplateElementCase templateelementcase)
    {
        rowHeightList = ReportHelper.getRowHeightList(templateelementcase);
        columnWidthList = ReportHelper.getColumnWidthList(templateelementcase);
        verticalValue = grid.getVerticalValue();
        horizontalValue = grid.getHorizontalValue();
        int i = grid.getVerticalExtent();
        int j = grid.getHorizontalExtent();
        grid.setVerticalBeinValue(verticalValue);
        grid.setHorizontalBeginValue(horizontalValue);
        gridSize = grid.getSize();
        verticalEndValue = verticalValue + i + 1;
        horizontalEndValue = horizontalValue + j + 1;
    }

    public void caculateScrollVisibleBounds(java.awt.geom.Rectangle2D.Double double1, CellElement cellelement)
    {
        caculateScrollVisibleBounds(double1, cellelement.getColumn(), cellelement.getRow(), cellelement.getColumnSpan(), cellelement.getRowSpan());
    }

    public void caculateScrollVisibleBounds(java.awt.geom.Rectangle2D.Double double1, Rectangle rectangle)
    {
        caculateScrollVisibleBounds(double1, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public void caculateScrollVisibleBounds(java.awt.geom.Rectangle2D.Double double1, int i, int j, int k, int l)
    {
        if(outOfScreen(i, j, k, l))
        {
            double1.x = INVALID_INTEGER;
        } else
        {
            double1.x = columnWidthList.getRangeValue(horizontalValue, i).toPixD(resolution);
            double1.y = rowHeightList.getRangeValue(verticalValue, j).toPixD(resolution);
            double1.width = columnWidthList.getRangeValue(i, i + k).toPixD(resolution);
            double1.height = rowHeightList.getRangeValue(j, j + l).toPixD(resolution);
        }
    }

    private boolean outOfScreen(int i, int j, int k, int l)
    {
        return i > horizontalEndValue || j > verticalEndValue || i + k <= horizontalValue || j + l <= verticalValue;
    }

    public static boolean validate(Rectangle2D rectangle2d)
    {
        return rectangle2d != null && rectangle2d.getX() != (double)INVALID_INTEGER;
    }

    public void calculateForcedPagingOfCellElement(ElementCasePane elementcasepane, CellElement cellelement, double d, double d1)
    {
        if(cellelement == null)
            return;
        CellPageAttr cellpageattr = cellelement.getCellPageAttr();
        if(cellpageattr == null)
            return;
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        DynamicUnitList dynamicunitlist = ReportHelper.getColumnWidthList(templateelementcase);
        DynamicUnitList dynamicunitlist1 = ReportHelper.getRowHeightList(templateelementcase);
        int i = elementcasepane.getSize().width;
        int j = elementcasepane.getSize().height;
        if(cellpageattr.isPageAfterColumn())
        {
            double d2 = dynamicunitlist.getRangeValueFromZero(cellelement.getColumn() + cellelement.getColumnSpan()).toPixD(resolution) - d;
            paginateLineList.add(new java.awt.geom.Line2D.Double(d2, 0.0D, d2, j));
        }
        if(cellpageattr.isPageBeforeColumn())
        {
            double d3 = dynamicunitlist.getRangeValueFromZero(cellelement.getColumn()).toPixD(resolution) - d;
            paginateLineList.add(new java.awt.geom.Line2D.Double(d3, 0.0D, d3, j));
        }
        if(cellpageattr.isPageAfterRow())
        {
            double d4 = dynamicunitlist1.getRangeValueFromZero(cellelement.getRow() + cellelement.getRowSpan()).toPixD(resolution) - d1;
            paginateLineList.add(new java.awt.geom.Line2D.Double(0.0D, d4, i, d4));
        }
        if(cellpageattr.isPageBeforeRow())
        {
            double d5 = dynamicunitlist1.getRangeValueFromZero(cellelement.getRow()).toPixD(resolution) - d1;
            paginateLineList.add(new java.awt.geom.Line2D.Double(0.0D, d5, i, d5));
        }
    }

    static 
    {
        WHITE_Backgorund = ColorBackground.getInstance(Color.WHITE);
    }
}
