// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.gui;

import com.fr.base.FRContext;
import com.fr.base.ScreenResolution;
import com.fr.base.chart.BaseChart;
import com.fr.base.chart.BaseChartCollection;
import com.fr.base.chart.BaseChartGlyph;
import com.fr.chart.chartattr.Axis;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.AxisGlyph;
import com.fr.design.chart.gui.active.ActiveGlyph;
import com.fr.design.chart.gui.active.ChartActiveGlyph;
import com.fr.design.gui.chart.MiddleChartComponent;
import com.fr.general.FRLogger;
import com.fr.stable.ArrayUtils;
import com.fr.stable.core.PropertyChangeListener;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

// Referenced classes of package com.fr.design.chart.gui:
//            ActiveGlyphFactory

public class ChartComponent extends MiddleChartComponent
    implements MouseListener, MouseMotionListener
{

    private static final long serialVersionUID = 0xa53cdfa5c397841L;
    private final java.util.List listeners;
    private ChartCollection chartCollection4Design;
    private Chart editingChart;
    private BaseChartGlyph chartGlyph;
    private int chartWidth;
    private int chartHeight;
    private Point point;
    private ActiveGlyph activeGlyph;
    private boolean supportEdit;
    private final int resizeCursors[] = {
        6, 8, 7, 11, 10, 5, 9, 4
    };

    public ChartComponent()
    {
        listeners = new ArrayList();
        chartWidth = -1;
        chartHeight = -1;
        supportEdit = true;
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public ChartComponent(ChartCollection chartcollection)
    {
        this();
        populate(chartcollection);
    }

    public ChartComponent(BaseChartCollection basechartcollection)
    {
        this();
        populate(basechartcollection);
    }

    public ChartComponent(ChartCollection chartcollection, PropertyChangeListener propertychangelistener)
    {
        this();
        populate(chartcollection);
        addStopEditingListener(propertychangelistener);
    }

    public void addStopEditingListener(PropertyChangeListener propertychangelistener)
    {
        if(!listeners.contains(propertychangelistener))
            listeners.add(propertychangelistener);
    }

    private void fireStopEditing()
    {
        int i = listeners.size();
        for(int j = i; j > 0; j--)
            ((PropertyChangeListener)listeners.get(j - 1)).propertyChange();

    }

    public void reset()
    {
        fireStopEditing();
        editingChart = null;
        chartGlyph = null;
        activeGlyph = null;
        point = null;
        chartHeight = chartWidth = -1;
        editingChart = chartCollection4Design.getSelectedChart();
        refreshChartGlyph();
        activeGlyph = ActiveGlyphFactory.createActiveGlyph(this, chartGlyph);
        repaint();
    }

    public void populate(BaseChartCollection basechartcollection)
    {
        try
        {
            chartCollection4Design = (ChartCollection)basechartcollection;
        }
        catch(Exception exception)
        {
            FRContext.getLogger().error("ChartCollection clone is Error");
        }
        reset();
    }

    public BaseChartCollection update()
    {
        return chartCollection4Design;
    }

    public void setSupportEdit(boolean flag)
    {
        supportEdit = flag;
    }

    public boolean isSupportEdit()
    {
        return supportEdit;
    }

    public ChartCollection getChartCollection()
    {
        return chartCollection4Design;
    }

    public int getChartSize()
    {
        return chartCollection4Design != null ? chartCollection4Design.getChartCount() : 0;
    }

    public BaseChart getEditingChart()
    {
        return editingChart;
    }

    public BaseChartGlyph getChartGlyph()
    {
        return chartGlyph;
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D graphics2d = (Graphics2D)g;
        Paint paint = graphics2d.getPaint();
        graphics2d.setPaint(Color.white);
        graphics2d.fillRect(0, 0, getBounds().width, getBounds().height);
        graphics2d.setPaint(paint);
        graphics2d.translate(2, 2);
        if(needRefreshChartGlyph())
            refreshChartGlyph();
        Object obj = graphics2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawChartGlyph(graphics2d);
        ActiveGlyph activeglyph = getActiveGlyph();
        if(activeglyph != null)
            activeglyph.paint4ActiveGlyph(graphics2d, chartGlyph);
        graphics2d.translate(-2, -2);
        if(obj == null)
            obj = RenderingHints.VALUE_ANTIALIAS_OFF;
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, obj);
    }

    private void refreshChartGlyph()
    {
        Dimension dimension = getBounds().getSize();
        editingChart = chartCollection4Design.getSelectedChart();
        if(editingChart != null)
        {
            chartGlyph = editingChart.createGlyph(editingChart.defaultChartData());
            activeGlyph = ActiveGlyphFactory.createActiveGlyph(this, chartGlyph);
        }
        chartWidth = dimension.width - 4;
        chartHeight = dimension.height - 4;
    }

    private ActiveGlyph getActiveGlyph()
    {
        if(point == null)
            activeGlyph = new ChartActiveGlyph(this, chartGlyph);
        else
            activeGlyph = (new ChartActiveGlyph(this, chartGlyph)).findActionGlyphFromChildren(point.x, point.y);
        return activeGlyph;
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
        int i = mouseevent.getClickCount();
        if(i >= 1 && activeGlyph != null && isSupportEdit())
            activeGlyph.goRightPane();
    }

    public void mousePressed(MouseEvent mouseevent)
    {
        point = new Point(mouseevent.getX(), mouseevent.getY());
        if(!ArrayUtils.contains(resizeCursors, getCursor().getType()))
            activeGlyph = (new ChartActiveGlyph(this, chartGlyph)).findActionGlyphFromChildren(point.x, point.y);
        if(activeGlyph == null)
        {
            return;
        } else
        {
            repaint();
            return;
        }
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public void mouseDragged(MouseEvent mouseevent)
    {
    }

    public void mouseMoved(MouseEvent mouseevent)
    {
        ActiveGlyph activeglyph = getActiveGlyph();
        if(activeglyph != null)
            activeglyph.onMouseMove(mouseevent);
    }

    public AxisGlyph getActiveAxisGlyph()
    {
        return (AxisGlyph)activeGlyph.getGlyph();
    }

    public Axis getActiveAxis()
    {
        AxisGlyph axisglyph = getActiveAxisGlyph();
        if(editingChart.getPlot() != null)
            return editingChart.getPlot().getAxis(axisglyph.getAxisType());
        else
            return null;
    }

    private boolean needRefreshChartGlyph()
    {
        return chartGlyph == null || chartWidth != getBounds().width || chartHeight != getBounds().height;
    }

    private void drawChartGlyph(Graphics2D graphics2d)
    {
        if(chartGlyph != null)
        {
            if(chartGlyph.isRoundBorder())
                chartGlyph.setBounds(new java.awt.geom.RoundRectangle2D.Double(0.0D, 0.0D, chartWidth, chartHeight, 10D, 10D));
            else
                chartGlyph.setBounds(new java.awt.geom.Rectangle2D.Double(0.0D, 0.0D, chartWidth, chartHeight));
            java.awt.Image image = chartGlyph.toImage(chartWidth, chartHeight, ScreenResolution.getScreenResolution());
            graphics2d.drawImage(image, 0, 0, chartWidth, chartHeight, null);
        }
    }
}
