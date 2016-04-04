// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.gui.xpane;

import com.fr.base.*;
import com.fr.base.background.ColorBackground;
import com.fr.design.designer.creator.cardlayout.XCardSwitchButton;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.form.ui.LayoutBorderStyle;
import com.fr.form.ui.WidgetTitle;
import com.fr.general.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

public class LayoutBorderPreviewPane extends JPanel
{
    private class titlePreviewPane extends UITextArea
    {

        private FRFont frFont;
        final LayoutBorderPreviewPane this$0;

        public void paintComponent(Graphics g)
        {
            Graphics2D graphics2d = (Graphics2D)g;
            Dimension dimension = getSize();
            graphics2d.setColor(getBackground());
            GraphHelper.fillRect(graphics2d, 0.0D, 0.0D, dimension.width, dimension.height);
            if(frFont == null)
                return;
            FontMetrics fontmetrics = getFontMetrics(frFont);
            int i = ScreenResolution.getScreenResolution();
            if(isEnabled())
                graphics2d.setColor(frFont.getForeground());
            else
                graphics2d.setColor(new Color(237, 237, 237));
            graphics2d.setFont(frFont.applyResolutionNP(i));
            WidgetTitle widgettitle = borderStyle.getTitle();
            String s = widgettitle.getTextObject().toString();
            boolean flag = false;
            boolean flag1 = false;
            if(!isTabLayout)
            {
                int j = (dimension.width - fontmetrics.stringWidth(s)) / 2;
                int l = (dimension.height - fontmetrics.getHeight()) / 2 + fontmetrics.getAscent();
                if(widgettitle.getPosition() == 2)
                    j = smallGAP;
                else
                if(widgettitle.getPosition() == 4)
                    j = dimension.width - fontmetrics.stringWidth(s) - smallGAP - fontmetrics.getMaxAdvance();
                Background background = widgettitle.getBackground();
                if(background != null)
                {
                    java.awt.geom.Rectangle2D.Double double1 = new java.awt.geom.Rectangle2D.Double(0.0D, 0.0D, getWidth(), getHeight());
                    background.paint(g, double1);
                }
                GraphHelper.drawString(graphics2d, s, j, l);
            } else
            {
                int k = (dimension.width / 2 - fontmetrics.stringWidth(s)) / 2;
                int i1 = (dimension.height - fontmetrics.getHeight()) / 2 + fontmetrics.getAscent();
                drawTabBack(graphics2d, g, widgettitle, fontmetrics, k, i1);
            }
            g.setColor(borderStyle.getColor());
            int j1 = GraphHelper.getLineStyleSize(borderStyle.getBorder());
            java.awt.geom.Rectangle2D.Double double2 = new java.awt.geom.Rectangle2D.Double(0.0D, getHeight() - 1, getWidth(), getHeight());
            double d = (double2.getX() + (double)(j1 != 1 ? 2 : 1)) - (double)j1 - 1.0D;
            double d1 = (double2.getY() + (double)(j1 != 1 ? 2 : 1)) - (double)j1;
            java.awt.geom.RoundRectangle2D.Double double3 = new java.awt.geom.RoundRectangle2D.Double(d, d1, double2.getWidth() + (double)j1, double2.getHeight() + (double)j1, 0.0D, 0.0D);
            GraphHelper.draw(g, double3, borderStyle.getBorder());
        }

        private void drawTabBack(Graphics2D graphics2d, Graphics g, WidgetTitle widgettitle, FontMetrics fontmetrics, int i, int j)
        {
            Dimension dimension = getSize();
            String s = (new StringBuilder()).append(Inter.getLocText("FR-Designer_Title")).append("0").toString();
            ColorBackground colorbackground = ColorBackground.getInstance(XCardSwitchButton.CHOOSED_GRAL);
            java.awt.geom.Rectangle2D.Double double1 = new java.awt.geom.Rectangle2D.Double(getWidth() / 2, 0.0D, getWidth() / 2, getHeight());
            colorbackground.paint(g, double1);
            String s1 = (new StringBuilder()).append(Inter.getLocText("FR-Designer_Title")).append("1").toString();
            GraphHelper.drawString(graphics2d, s1, (dimension.width / 2 - fontmetrics.stringWidth(s)) / 2 + dimension.width / 2, j);
            Background background = widgettitle.getBackground();
            if(background != null)
            {
                java.awt.geom.Rectangle2D.Double double2 = new java.awt.geom.Rectangle2D.Double(0.0D, 0.0D, getWidth() / 2, getHeight());
                background.paint(g, double2);
            }
            GraphHelper.drawString(graphics2d, s, i, j);
        }

        public void setFontObject(FRFont frfont)
        {
            frFont = frfont;
            repaint();
        }

        public titlePreviewPane()
        {
            this$0 = LayoutBorderPreviewPane.this;
            super();
            frFont = null;
            frFont = FRContext.getDefaultValues().getFRFont();
        }
    }


    private titlePreviewPane jp;
    private LayoutBorderStyle borderStyle;
    private int smallGAP;
    private int GAP;
    private boolean isTabLayout;
    private static final String TAB_ZERO = "0";
    private static final String TAB_ONE = "1";

    public boolean isTagLayout()
    {
        return isTabLayout;
    }

    public void setTagLayout(boolean flag)
    {
        isTabLayout = flag;
    }

    public LayoutBorderPreviewPane(LayoutBorderStyle layoutborderstyle)
    {
        smallGAP = 5;
        GAP = 10;
        borderStyle = layoutborderstyle;
        repaint();
        jp = new titlePreviewPane();
        add(jp);
    }

    public LayoutBorderPreviewPane(LayoutBorderStyle layoutborderstyle, boolean flag)
    {
        this(layoutborderstyle);
        isTabLayout = true;
    }

    public void repaint(LayoutBorderStyle layoutborderstyle)
    {
        borderStyle = layoutborderstyle;
        super.repaint();
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        Background background = borderStyle.getBackground();
        if(background != null)
        {
            java.awt.geom.Rectangle2D.Double double1 = new java.awt.geom.Rectangle2D.Double(smallGAP, smallGAP, getWidth() - GAP, getHeight() - GAP);
            background.paint(g, double1);
        } else
        {
            g.setColor(Color.white);
            g.fillRect(smallGAP, smallGAP, getWidth() - GAP, getHeight() - GAP);
        }
        updateBorders(g);
    }

    private void updateBorders(Graphics g)
    {
        if(borderStyle != null)
        {
            int i = borderStyle.getTitle().getFrFont().getSize() + GAP;
            jp.setPreferredSize(new Dimension(getWidth() - GAP, i));
            jp.setBounds(smallGAP, smallGAP, getWidth() - GAP, i);
            borderStyle.paint(g, new java.awt.geom.Rectangle2D.Double(smallGAP, smallGAP, getWidth() - GAP, getHeight() - GAP));
            jp.setFontObject(borderStyle.getTitle().getFrFont());
            jp.setVisible(borderStyle.getType() == 1);
        }
    }



}
