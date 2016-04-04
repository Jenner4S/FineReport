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
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

// Referenced classes of package com.fr.grid:
//            GridRow, Grid

public class GridRowUI extends ComponentUI
{

    private Color detailsBackground;

    public GridRowUI()
    {
        detailsBackground = Color.lightGray;
    }

    public void paint(Graphics g, JComponent jcomponent)
    {
        if(!(jcomponent instanceof GridRow))
            throw new IllegalArgumentException("The component c to paint must be a GridColumn!");
        Graphics2D graphics2d = (Graphics2D)g;
        GridRow gridrow = (GridRow)jcomponent;
        ElementCasePane elementcasepane = gridrow.getElementCasePane();
        Dimension dimension = gridrow.getSize();
        com.fr.report.elementcase.TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        DynamicUnitList dynamicunitlist = ReportHelper.getRowHeightList(templateelementcase);
        int i = elementcasepane.getGrid().getVerticalValue();
        int j = i;
        elementcasepane.getGrid().setVerticalBeinValue(j);
        int k = elementcasepane.getGrid().getVerticalExtent();
        int l = i + k + 1;
        double d = dimension.getHeight();
        int i1 = ScreenResolution.getScreenResolution();
        int j1 = templateelementcase.getRowCount();
        double d1 = 0.0D;
        if(j1 > j)
            d1 = dynamicunitlist.getRangeValue(j, j1).toPixD(i1);
        d1 = Math.min(d, d1);
        if(gridrow.getBackground() != null)
        {
            graphics2d.setPaint(detailsBackground);
            GraphHelper.fill(graphics2d, new java.awt.geom.Rectangle2D.Double(0.0D, 0.0D, dimension.getWidth(), d1));
            graphics2d.setPaint(gridrow.getBackground());
            GraphHelper.fill(graphics2d, new java.awt.geom.Rectangle2D.Double(0.0D, d1, dimension.getHeight(), dimension.getHeight() - d1));
        }
        graphics2d.setPaint(gridrow.getSeparatorLineColor());
        GraphHelper.drawLine(graphics2d, 0.0D, 0.0D, dimension.getWidth(), 0.0D);
        double d2 = 0.0D;
        drawRow(j, l, dynamicunitlist, i1, d2, gridrow, graphics2d);
        graphics2d.setColor(gridrow.getSeparatorLineColor());
        GraphHelper.drawLine(graphics2d, 0.0D, 0.0D, 0.0D, d2);
    }

    private void drawRow(int i, int j, DynamicUnitList dynamicunitlist, int k, double d, GridRow gridrow, 
            Graphics2D graphics2d)
    {
        double d1 = 0.0D;
        double d2 = 0.0D;
        ElementCasePane elementcasepane = gridrow.getElementCasePane();
        Dimension dimension = gridrow.getSize();
        com.fr.report.elementcase.TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        for(int l = 0; l <= j; l++)
        {
            if(l == 0)
                l = i;
            d1 += d2;
            d2 = dynamicunitlist.get(l).toPixD(k);
            d = d2 != 0.0D ? d1 + d2 : d1 + 1.0D;
            Selection selection = elementcasepane.getSelection();
            int ai[] = selection.getSelectedRows();
            boolean flag;
            if(IntList.asList(ai).contain(l))
            {
                graphics2d.setPaint(gridrow.getSelectedBackground());
                GraphHelper.fill(graphics2d, new java.awt.geom.Rectangle2D.Double(0.0D, d1 + 1.0D, dimension.width, d2 - 1.0D));
                flag = true;
            } else
            {
                flag = false;
            }
            drawAuthority(templateelementcase, graphics2d, d1, d2, dimension, l);
            graphics2d.setColor(gridrow.getSeparatorLineColor());
            GraphHelper.drawLine(graphics2d, 0.0D, d, dimension.getWidth(), d);
            Integer integer = gridrow.getDisplay(l);
            String s = integer.toString();
            if(templateelementcase.getReportPageAttr() != null)
            {
                if(l >= templateelementcase.getReportPageAttr().getRepeatHeaderRowFrom() && l <= templateelementcase.getReportPageAttr().getRepeatHeaderRowTo())
                    s = (new StringBuilder()).append(s).append("(H)").toString();
                if(l >= templateelementcase.getReportPageAttr().getRepeatFooterRowFrom() && l <= templateelementcase.getReportPageAttr().getRepeatFooterRowTo())
                    s = (new StringBuilder()).append(s).append("(F)").toString();
            }
            drawNormalContent(l, graphics2d, gridrow, s, d2, flag, templateelementcase, dimension, d1);
        }

    }

    private void drawAuthority(ElementCase elementcase, Graphics2D graphics2d, double d, double d1, Dimension dimension, 
            int i)
    {
        boolean flag = BaseUtils.isAuthorityEditing();
        if(flag)
        {
            ColumnRowPrivilegeControl columnrowprivilegecontrol = elementcase.getRowPrivilegeControl(i);
            String s = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
            if(columnrowprivilegecontrol.checkInvisible(s))
            {
                graphics2d.setColor(UIConstants.AUTHORITY_COLOR);
                graphics2d.setComposite(AlphaComposite.getInstance(3, 0.7F));
                GraphHelper.fill(graphics2d, new java.awt.geom.Rectangle2D.Double(0.0D, d + 1.0D, dimension.width, d1 - 1.0D));
            }
        }
    }

    private void drawNormalContent(int i, Graphics2D graphics2d, GridRow gridrow, String s, double d, boolean flag, 
            ElementCase elementcase, Dimension dimension, double d1)
    {
        FontRenderContext fontrendercontext = graphics2d.getFontRenderContext();
        float f = GraphHelper.getFontMetrics(gridrow.getFont()).getAscent();
        double d2 = gridrow.getFont().getStringBounds(s, fontrendercontext).getWidth();
        double d3 = gridrow.getFont().getStringBounds(s, fontrendercontext).getHeight();
        if(d3 <= d + 2D)
        {
            if(flag)
                graphics2d.setColor(gridrow.getSelectedForeground());
            else
            if(gridrow.isEnabled())
                graphics2d.setColor(gridrow.getForeground());
            else
                graphics2d.setPaint(UIManager.getColor("controlShadow"));
            GraphHelper.drawString(graphics2d, s, ((double)dimension.width - d2) / 2D, (d1 + (d - d3) / 2D + 2D + (double)f) - 2D);
        }
    }
}
