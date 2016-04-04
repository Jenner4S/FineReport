// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.form;

import com.fr.base.BaseUtils;
import com.fr.base.GraphHelper;
import com.fr.design.constants.UIConstants;
import com.fr.design.mainframe.BaseJForm;
import com.fr.form.FormElementCaseContainerProvider;
import com.fr.general.Inter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import javax.swing.Icon;
import javax.swing.JComponent;

public class FormTabPane extends JComponent
    implements MouseListener, MouseMotionListener
{

    private static final Icon WORK_SHEET_ICON = BaseUtils.readIcon("com/fr/base/images/oem/worksheet.png");
    private static final Icon POLY_SHEET_ICON = BaseUtils.readIcon("com/fr/design/images/sheet/polysheet.png");
    private static final int GAP = 5;
    private BaseJForm form;
    private FormElementCaseContainerProvider elementCase;
    private double specialLocation1;
    private double specialLocation2;
    private int formTabWidth;
    private int ecTabWidth;
    private static final int ADD_WIDTH_BY_SHEETNAME = 20;
    private int tabHeight;
    private static final int FORM_INDEX = 0;
    private static final int EC_INDEX = 1;
    private int mouseOveredIndex;
    private int selectedIndex;

    public FormTabPane(FormElementCaseContainerProvider formelementcasecontainerprovider, BaseJForm basejform)
    {
        specialLocation1 = 2.5D;
        specialLocation2 = 4.3301270000000001D;
        formTabWidth = 100;
        ecTabWidth = formTabWidth;
        tabHeight = 17;
        mouseOveredIndex = -1;
        selectedIndex = -1;
        elementCase = formelementcasecontainerprovider;
        form = basejform;
        setLayout(new BorderLayout(0, 0));
        addMouseListener(this);
        addMouseMotionListener(this);
        setBorder(null);
        setForeground(new Color(99, 99, 99));
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D graphics2d = (Graphics2D)g;
        calculateECWidth();
        paintFormTab(graphics2d, 0, Inter.getLocText("Form"), POLY_SHEET_ICON);
        paintECTab(graphics2d, formTabWidth, elementCase.getElementCaseContainerName(), WORK_SHEET_ICON);
    }

    private void paintECTab(Graphics2D graphics2d, int i, String s, Icon icon)
    {
        double ad[] = {
            (double)i, (double)i, (double)(i + ecTabWidth), (double)(i + ecTabWidth), (double)i
        };
        double ad1[] = {
            -1D, (double)tabHeight, (double)tabHeight, -1D, -1D
        };
        if(1 == mouseOveredIndex)
            graphics2d.setPaint(new GradientPaint(1.0F, 1.0F, Color.WHITE, 1.0F, tabHeight - 1, UIConstants.NORMAL_BACKGROUND));
        else
            graphics2d.setPaint(new GradientPaint(1.0F, 1.0F, UIConstants.NORMAL_BACKGROUND, 1.0F, tabHeight - 1, UIConstants.NORMAL_BACKGROUND));
        GeneralPath generalpath = new GeneralPath(0, ad.length);
        generalpath.moveTo((float)ad[0], (float)ad1[0]);
        generalpath.curveTo((double)(float)ad[0] - specialLocation1, ad1[0] - specialLocation2, (double)(float)ad[0] - specialLocation2, ad1[0] - specialLocation1, ad[0], ad1[0]);
        for(int j = 1; j <= 2; j++)
            generalpath.lineTo((float)ad[j], (float)ad1[j]);

        generalpath.lineTo((float)ad[3], (float)ad1[3]);
        generalpath.curveTo((double)(float)ad[3] + specialLocation1, (double)(float)ad1[3] - specialLocation2, (double)(float)ad[3] + specialLocation2, (double)(float)ad1[3] - specialLocation1, (float)ad[3], (float)ad1[3]);
        generalpath.lineTo((float)ad[0], (float)ad1[0]);
        generalpath.closePath();
        graphics2d.fill(generalpath);
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setPaint(UIConstants.LINE_COLOR);
        graphics2d.draw(new java.awt.geom.Line2D.Double(ad[0], ad1[0], ad[1], ad1[1]));
        graphics2d.draw(new java.awt.geom.Line2D.Double(ad[2], ad1[2], ad[3], ad1[3]));
        graphics2d.draw(new java.awt.geom.Line2D.Double(ad[0], 0.0D, ad[2], 0.0D));
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        icon.paintIcon(this, graphics2d, i + 5, 2);
        graphics2d.setPaint(getForeground());
        graphics2d.drawString(s, i + icon.getIconWidth() + 10, tabHeight - 5);
    }

    private void paintFormTab(Graphics2D graphics2d, int i, String s, Icon icon)
    {
        double ad[] = {
            (double)i, (double)i, (double)(i + formTabWidth), (double)(i + formTabWidth), (double)i
        };
        double ad1[] = {
            -1D, (double)(tabHeight - 1), (double)(tabHeight - 1), -1D, -1D
        };
        if(0 == mouseOveredIndex)
            graphics2d.setPaint(new GradientPaint(1.0F, 1.0F, new Color(255, 150, 0), 1.0F, tabHeight - 1, UIConstants.NORMAL_BACKGROUND));
        else
            graphics2d.setPaint(new GradientPaint(1.0F, 1.0F, Color.ORANGE, 1.0F, tabHeight - 1, UIConstants.NORMAL_BACKGROUND));
        GeneralPath generalpath = new GeneralPath(0, ad.length);
        generalpath.moveTo((float)ad[0], (float)ad1[0]);
        generalpath.curveTo((double)(float)ad[0] - specialLocation1, ad1[0] - specialLocation2, (double)(float)ad[0] - specialLocation2, ad1[0] - specialLocation1, ad[0], ad1[0]);
        for(int j = 1; j <= 2; j++)
            generalpath.lineTo((float)ad[j], (float)ad1[j]);

        generalpath.lineTo((float)ad[3], (float)ad1[3]);
        generalpath.curveTo((double)(float)ad[3] + specialLocation1, (double)(float)ad1[3] - specialLocation2, (double)(float)ad[3] + specialLocation2, (double)(float)ad1[3] - specialLocation1, (float)ad[3], (float)ad1[3]);
        generalpath.lineTo((float)ad[0], (float)ad1[0]);
        generalpath.closePath();
        graphics2d.fill(generalpath);
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setPaint(UIConstants.LINE_COLOR);
        graphics2d.draw(new java.awt.geom.Line2D.Double(ad[0], ad1[0], ad[1], ad1[1]));
        graphics2d.draw(new java.awt.geom.Line2D.Double(ad[2], ad1[2], ad[3], ad1[3]));
        graphics2d.draw(new java.awt.geom.Line2D.Double(ad[0], 0.0D, ad[2], 0.0D));
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        icon.paintIcon(this, graphics2d, i + 5, 2);
        graphics2d.setPaint(getForeground());
        graphics2d.drawString(s, i + icon.getIconWidth() + 10, tabHeight - 5);
    }

    private void calculateECWidth()
    {
        FontMetrics fontmetrics = GraphHelper.getFontMetrics(getFont());
        int i = fontmetrics.charWidth('M');
        String s = elementCase.getElementCaseContainerName();
        ecTabWidth = Math.max(ecTabWidth, fontmetrics.stringWidth(s) + i * 2 + 20);
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
        selectedIndex = getTabIndex(mouseevent.getX());
        if(selectedIndex == 0)
            form.tabChanged(0);
        repaint();
    }

    public void mousePressed(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
        mouseOveredIndex = -1;
        repaint();
    }

    public void mouseDragged(MouseEvent mouseevent)
    {
    }

    public void mouseMoved(MouseEvent mouseevent)
    {
        mouseOveredIndex = getTabIndex(mouseevent.getX());
        repaint();
    }

    private int getTabIndex(int i)
    {
        return i <= 0 || i > formTabWidth ? 1 : 0;
    }

}
