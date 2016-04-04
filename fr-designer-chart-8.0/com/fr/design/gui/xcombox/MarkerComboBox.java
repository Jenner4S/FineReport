// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.gui.xcombox;

import com.fr.base.*;
import com.fr.base.background.ColorBackground;
import com.fr.chart.chartglyph.Marker;
import com.fr.chart.chartglyph.NullMarker;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.general.*;
import java.awt.*;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;

public class MarkerComboBox extends UIComboBox
{
    class MarkerCellRenderer extends UIComboBoxRenderer
    {

        private Marker marker;
        private boolean isSelected;
        final MarkerComboBox this$0;

        public Component getListCellRendererComponent(JList jlist, Object obj, int i, boolean flag, boolean flag1)
        {
            marker = (Marker)obj;
            isSelected = flag;
            return this;
        }

        public void paint(Graphics g)
        {
            Graphics2D graphics2d = (Graphics2D)g;
            Dimension dimension = getSize();
            graphics2d.setColor(Color.black);
            graphics2d.setFont(FRContext.getDefaultValues().getFRFont());
            if(marker != null)
                if(marker instanceof NullMarker)
                {
                    graphics2d.setColor(Color.black);
                    FRFont frfont = FRContext.getDefaultValues().getFRFont();
                    int i = ScreenResolution.getScreenResolution();
                    java.awt.Font font = frfont.applyResolutionNP(i);
                    graphics2d.setFont(font);
                    FontMetrics fontmetrics = GraphHelper.getFontMetrics(font);
                    GraphHelper.drawString(graphics2d, Inter.getLocText("None"), 12D, (dimension.height - fontmetrics.getHeight()) / 2 + fontmetrics.getAscent());
                } else
                {
                    if(marker.getBackground() == null)
                        marker.setBackground(ColorBackground.getInstance(Color.black));
                    marker.paint(graphics2d, dimension.width / 2, dimension.height / 2);
                }
            if(isSelected)
            {
                graphics2d.setColor(Color.blue);
                GraphHelper.drawRect(graphics2d, 0.0D, 0.0D, dimension.width - 1, dimension.height - 1);
            }
        }

        public Dimension getPreferredSize()
        {
            return new Dimension(36, 16);
        }

        public Dimension getMinimumSize()
        {
            return getPreferredSize();
        }

        MarkerCellRenderer()
        {
            this$0 = MarkerComboBox.this;
            super();
            marker = null;
            isSelected = false;
        }
    }


    public MarkerComboBox(Marker amarker[])
    {
        setModel(new DefaultComboBoxModel(amarker));
        setRenderer(new MarkerCellRenderer());
    }

    public Marker getSelectedMarkder()
    {
        return (Marker)getSelectedItem();
    }

    public void setSelectedMarker(Marker marker)
    {
        setSelectedItem(marker);
    }
}
