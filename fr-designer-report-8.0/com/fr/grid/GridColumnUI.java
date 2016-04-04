// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.grid;

import com.fr.base.*;
import com.fr.cache.list.IntList;
import com.fr.design.constants.UIConstants;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import com.fr.design.roleAuthority.RoleTree;
import com.fr.grid.selection.Selection;
import com.fr.page.ReportPageAttrProvider;
import com.fr.privilege.finegrain.ColumnRowPrivilegeControl;
import com.fr.report.ReportHelper;
import com.fr.report.elementcase.ElementCase;
import com.fr.stable.unit.FU;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

// Referenced classes of package com.fr.grid:
//            GridColumn, Grid

public class GridColumnUI extends ComponentUI
{

    protected Color withoutDetailsBackground;
    protected int resolution;

    public GridColumnUI()
    {
        withoutDetailsBackground = Color.lightGray;
        resolution = ScreenResolution.getScreenResolution();
    }

    public void paint(Graphics g, JComponent jcomponent)
    {
        if(!(jcomponent instanceof GridColumn))
            throw new IllegalArgumentException("The component c to paint must be a GridColumn!");
        Graphics2D graphics2d = (Graphics2D)g;
        GridColumn gridcolumn = (GridColumn)jcomponent;
        ElementCasePane elementcasepane = gridcolumn.getElementCasePane();
        Grid grid = elementcasepane.getGrid();
        Dimension dimension = gridcolumn.getSize();
        com.fr.report.elementcase.TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        DynamicUnitList dynamicunitlist = ReportHelper.getColumnWidthList(templateelementcase);
        int i = grid.getHorizontalValue();
        int j = grid.getHorizontalBeginValue();
        j = i;
        grid.setHorizontalBeginValue(j);
        int k = grid.getHorizontalExtent();
        int l = i + k + 1;
        double d = dimension.getWidth();
        int i1 = templateelementcase.getColumnCount();
        double d1 = 0.0D;
        if(i1 >= j)
            d1 = dynamicunitlist.getRangeValue(j, i1).toPixD(resolution);
        d1 = Math.min(d, d1);
        if(gridcolumn.getBackground() != null)
        {
            graphics2d.setPaint(withoutDetailsBackground);
            GraphHelper.fill(graphics2d, new java.awt.geom.Rectangle2D.Double(0.0D, 0.0D, d1, dimension.getHeight()));
            graphics2d.setPaint(gridcolumn.getBackground());
            GraphHelper.fill(graphics2d, new java.awt.geom.Rectangle2D.Double(d1, 0.0D, dimension.getWidth() - d1, dimension.getHeight()));
        }
        graphics2d.setPaint(gridcolumn.getSeparatorLineColor());
        GraphHelper.drawLine(graphics2d, 0.0D, 0.0D, 0.0D, dimension.getHeight());
        double d2 = 0.0D;
        drawColumn(j, l, dynamicunitlist, d2, elementcasepane, graphics2d, gridcolumn);
        graphics2d.setColor(gridcolumn.getSeparatorLineColor());
        GraphHelper.drawLine(graphics2d, 0.0D, 0.0D, d2, 0.0D);
    }

    private void drawColumn(int i, int j, DynamicUnitList dynamicunitlist, double d, ElementCasePane elementcasepane, Graphics2D graphics2d, 
            GridColumn gridcolumn)
    {
        double d1 = 0.0D;
        double d2 = 0.0D;
        com.fr.report.elementcase.TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        Dimension dimension = gridcolumn.getSize();
        FontRenderContext fontrendercontext = graphics2d.getFontRenderContext();
        LineMetrics linemetrics = gridcolumn.getFont().getLineMetrics("", fontrendercontext);
        float f = linemetrics.getAscent();
        for(int k = 0; k <= j; k++)
        {
            if(k == 0)
                k = i;
            d1 += d2;
            d2 = dynamicunitlist.get(k).toPixD(resolution);
            d = d2 > 0.0D ? d1 + d2 : d1 + 1.0D;
            Selection selection = elementcasepane.getSelection();
            int ai[] = selection.getSelectedColumns();
            boolean flag;
            if(IntList.asList(ai).contain(k))
            {
                graphics2d.setColor(gridcolumn.getSelectedBackground());
                GraphHelper.fill(graphics2d, new java.awt.geom.Rectangle2D.Double(d1 + 1.0D, 0.0D, d2 - 1.0D, dimension.height));
                flag = true;
            } else
            {
                flag = false;
            }
            drawAuthority(templateelementcase, graphics2d, d1, d2, dimension, k);
            graphics2d.setColor(gridcolumn.getSeparatorLineColor());
            GraphHelper.drawLine(graphics2d, d, 0.0D, d, dimension.height);
            paintContent(k, graphics2d, d1, dimension, d2, flag, gridcolumn, templateelementcase, f, fontrendercontext);
        }

    }

    private void drawAuthority(ElementCase elementcase, Graphics2D graphics2d, double d, double d1, Dimension dimension, 
            int i)
    {
        boolean flag = BaseUtils.isAuthorityEditing();
        if(flag)
        {
            ColumnRowPrivilegeControl columnrowprivilegecontrol = elementcase.getColumnPrivilegeControl(i);
            String s = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
            if(columnrowprivilegecontrol.checkInvisible(s))
            {
                graphics2d.setComposite(AlphaComposite.getInstance(3, 0.7F));
                graphics2d.setColor(UIConstants.AUTHORITY_COLOR);
                GraphHelper.fill(graphics2d, new java.awt.geom.Rectangle2D.Double(d + 1.0D, 0.0D, d1 - 1.0D, dimension.height));
            }
        }
    }

    private void paintContent(int i, Graphics2D graphics2d, double d, Dimension dimension, double d1, 
            boolean flag, GridColumn gridcolumn, ElementCase elementcase, float f, FontRenderContext fontrendercontext)
    {
        String s = gridcolumn.getDisplay(i);
        if(s == null)
            return;
        if(elementcase.getReportPageAttr() != null)
        {
            if(i >= elementcase.getReportPageAttr().getRepeatHeaderColumnFrom() && i <= elementcase.getReportPageAttr().getRepeatHeaderColumnTo())
                s = (new StringBuilder()).append(s).append("(HR)").toString();
            if(i >= elementcase.getReportPageAttr().getRepeatFooterColumnFrom() && i <= elementcase.getReportPageAttr().getRepeatFooterColumnTo())
                s = (new StringBuilder()).append(s).append("(FR)").toString();
        }
        double d2 = gridcolumn.getFont().getStringBounds(s, fontrendercontext).getWidth();
        if(d2 > d1)
            paintMoreContent(i, graphics2d, d, dimension, d1, flag, gridcolumn, elementcase, s, d2, f);
        else
            paintNormalContent(i, graphics2d, d, d1, flag, gridcolumn, elementcase, s, d2, f);
    }

    private void paintMoreContent(int i, Graphics2D graphics2d, double d, Dimension dimension, double d1, 
            boolean flag, GridColumn gridcolumn, ElementCase elementcase, String s, double d2, float f)
    {
        Graphics2D graphics2d1 = (Graphics2D)graphics2d.create();
        graphics2d1.setClip(new java.awt.geom.Rectangle2D.Double(0.0D, d, dimension.width, d1));
        if(flag)
            graphics2d1.setPaint(gridcolumn.getSelectedForeground());
        else
        if(gridcolumn.isEnabled())
            graphics2d1.setPaint(gridcolumn.getForeground());
        else
            graphics2d.setPaint(UIManager.getColor("controlShadow"));
        GraphHelper.drawString(graphics2d1, s, d + (d1 - d2) / 2D, f + 2.0F + 1.0F);
        graphics2d1.dispose();
    }

    private void paintNormalContent(int i, Graphics2D graphics2d, double d, double d1, boolean flag, 
            GridColumn gridcolumn, ElementCase elementcase, String s, double d2, float f)
    {
        if(flag)
            graphics2d.setPaint(gridcolumn.getSelectedForeground());
        else
        if(gridcolumn.isEnabled())
            graphics2d.setPaint(gridcolumn.getForeground());
        else
            graphics2d.setPaint(UIManager.getColor("controlShadow"));
        GraphHelper.drawString(graphics2d, s, d + (d1 - d2) / 2D, f + 2.0F + 1.0F);
    }
}
