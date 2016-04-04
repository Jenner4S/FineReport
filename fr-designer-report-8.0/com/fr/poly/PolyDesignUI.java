// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly;

import com.fr.base.*;
import com.fr.design.utils.ComponentUtils;
import com.fr.general.Background;
import com.fr.general.Inter;
import com.fr.page.PaperSettingProvider;
import com.fr.page.ReportSettingsProvider;
import com.fr.poly.creator.BlockCreator;
import com.fr.poly.creator.BlockEditor;
import com.fr.poly.model.AddedData;
import com.fr.poly.model.AddingData;
import com.fr.report.report.Report;
import com.fr.report.report.TemplateReport;
import com.fr.stable.unit.UNIT;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.ComponentUI;

// Referenced classes of package com.fr.poly:
//            PolyArea, PolyDesigner

public class PolyDesignUI extends ComponentUI
{

    private static final Color PAGINATE_LINE_COLOR;
    private static final double SCROLL_POINT = 50D;
    private static final int SCROLL_DISTANCE = 15;
    private PolyDesigner designer;
    private int resolution;
    private int ten;
    private int hundred;

    public PolyDesignUI()
    {
        resolution = ScreenResolution.getScreenResolution();
        ten = 10;
        hundred = 100;
    }

    public void installUI(JComponent jcomponent)
    {
        designer = ((PolyArea)jcomponent).getPolyDesigner();
    }

    private void paintBackground(Graphics g, JComponent jcomponent)
    {
        Graphics2D graphics2d = (Graphics2D)g;
        Dimension dimension = jcomponent.getSize();
        TemplateReport templatereport = designer.getTemplateReport();
        graphics2d.setPaint(Color.white);
        GraphHelper.fillRect(g, 0.0D, 0.0D, dimension.width, dimension.height);
        ReportSettingsProvider reportsettingsprovider = templatereport.getReportSettings();
        if(reportsettingsprovider != null)
        {
            Background background = reportsettingsprovider.getBackground();
            if(background != null)
            {
                java.awt.geom.Rectangle2D.Double double1 = new java.awt.geom.Rectangle2D.Double(0.0D, 0.0D, dimension.width, dimension.height);
                background.paint(graphics2d, double1);
            }
        }
    }

    public void paint(Graphics g, JComponent jcomponent)
    {
        paintBackground(g, jcomponent);
        Graphics2D graphics2d = (Graphics2D)g;
        paintAddedData(graphics2d);
        AddingData addingdata = designer.getAddingData();
        if(addingdata != null && addingdata.getCreator() != null)
            paintAddingData(graphics2d, addingdata);
        paintPaginateLine(graphics2d);
    }

    private void paintAddedData(Graphics g)
    {
        AddedData addeddata = designer.getAddedData();
        int i = 0;
        for(int j = addeddata.getAddedCount(); i < j; i++)
        {
            BlockCreator blockcreator = addeddata.getAddedAt(i);
            if(blockcreator == designer.getSelection())
            {
                paintPositionLine(g, blockcreator.getX(), blockcreator.getY(), designer.getHorizontalValue(), designer.getVerticalValue());
                if(blockcreator.getEditor().isDragging())
                {
                    blockcreator.getEditor().paintAbsorptionline(g);
                    changeForbiddenWindowVisibility(blockcreator);
                    scrollWhenCreatorAtCorner(blockcreator);
                } else
                {
                    blockcreator.getEditor().hideForbiddenWindow();
                }
            } else
            {
                paintCreator(g, blockcreator, blockcreator.getX() - designer.getHorizontalValue(), blockcreator.getY() - designer.getVerticalValue(), blockcreator.getWidth(), blockcreator.getHeight());
            }
        }

    }

    private void changeForbiddenWindowVisibility(BlockCreator blockcreator)
    {
        Rectangle rectangle = blockcreator.getBounds();
        boolean flag = designer.intersectsAllBlock(blockcreator);
        if(!flag)
        {
            blockcreator.getEditor().hideForbiddenWindow();
            return;
        } else
        {
            int i = (int)((designer.getAreaLocationX() + rectangle.getCenterX()) - (double)designer.getHorizontalValue());
            int j = (int)((designer.getAreaLocationY() + rectangle.getCenterY()) - (double)designer.getVerticalValue());
            blockcreator.getEditor().showForbiddenWindow(i, j);
            return;
        }
    }

    private Rectangle getCreatorPixRectangle(BlockCreator blockcreator, Point point)
    {
        int i = blockcreator.getWidth();
        int j = blockcreator.getHeight();
        int k = (point.x - i / 2) + designer.getHorizontalValue();
        int l = (point.y - j / 2) + designer.getVerticalValue();
        return new Rectangle(k, l, i, j);
    }

    private void scrollWhenCreatorAtCorner(final BlockCreator creator)
    {
        Thread thread = new Thread(new Runnable() {

            final BlockCreator val$creator;
            final PolyDesignUI this$0;

            public void run()
            {
                try
                {
                    Thread.sleep(100L);
                    int i = creator.getX() + creator.getWidth();
                    int j = creator.getY() + creator.getHeight();
                    JScrollBar jscrollbar = designer.getHorizontalScrollBar();
                    JScrollBar jscrollbar1 = designer.getVerticalScrollBar();
                    int k = designer.getWidth() + jscrollbar.getValue();
                    int l = designer.getHeight() + jscrollbar1.getValue();
                    if((double)i > (double)k - 50D)
                        jscrollbar.setValue(jscrollbar.getValue() + 15);
                    if((double)j > (double)l - 50D)
                        jscrollbar1.setValue(jscrollbar1.getValue() + 15);
                }
                catch(InterruptedException interruptedexception) { }
            }

            
            {
                this$0 = PolyDesignUI.this;
                creator = blockcreator;
                super();
            }
        }
);
        thread.start();
    }

    private void paintPositionLine(Graphics g, int i, int j, int k, int l)
    {
        int i1 = i - k;
        int j1 = j - l;
        g.setColor(Color.gray);
        GraphHelper.drawLine(g, 0.0D, j1, i1, j1, 7);
        GraphHelper.drawLine(g, i1, 0.0D, i1, j1, 7);
        GraphHelper.drawString(g, convertUnit(j), 0.0D, j1);
        GraphHelper.drawString(g, convertUnit(i), i1, 10D);
    }

    private String convertUnit(int i)
    {
        short word0 = designer.getRulerLengthUnit();
        int j = ScreenResolution.getScreenResolution();
        if(word0 == 0)
        {
            Double double1 = java.lang.Double.valueOf(((double)(i + 2) * 1.0D * 36576D) / 1440D / (double)j);
            return (new StringBuilder()).append(double1.intValue()).append(Inter.getLocText("Unit_MM")).toString();
        }
        if(word0 == 1)
        {
            Double double2 = java.lang.Double.valueOf(((double)(i + 2) * 1.0D * 36576D) / 1440D / (double)j);
            return (new StringBuilder()).append((new DecimalFormat("0.0")).format(((double)double2.intValue() * 1.0D) / (double)ten)).append(Inter.getLocText("Unit_CM")).toString();
        }
        if(word0 == 2)
        {
            Double double3 = java.lang.Double.valueOf(i != 0 ? (((double)(i + 2) * 1.0D) / (double)j) * (double)hundred : 0.0D);
            return (new StringBuilder()).append((new DecimalFormat("0.00")).format(((double)double3.intValue() * 1.0D) / (double)hundred)).append(Inter.getLocText("Unit_INCH")).toString();
        }
        if(word0 == 3)
        {
            int k = i != 0 ? ((i + 2) * 72) / j : 0;
            return (new StringBuilder()).append(k).append(Inter.getLocText("Unit_PT")).toString();
        } else
        {
            return (new StringBuilder()).append("").append(i).toString();
        }
    }

    private void paintAddingData(Graphics g, AddingData addingdata)
    {
        BlockCreator blockcreator = addingdata.getCreator();
        int i = addingdata.getCurrentX();
        int j = addingdata.getCurrentY();
        int k = blockcreator.getWidth();
        int l = blockcreator.getHeight();
        paintCreator(g, blockcreator, i, j, k, l);
    }

    private void paintCreator(Graphics g, JComponent jcomponent, int i, int j, int k, int l)
    {
        ArrayList arraylist = new ArrayList();
        ComponentUtils.disableBuffer(jcomponent, arraylist);
        Graphics g1 = g.create(i, j, k, l);
        jcomponent.paint(g1);
        g1.dispose();
        ComponentUtils.resetBuffer(arraylist);
    }

    private void paintPaginateLine(Graphics2D graphics2d)
    {
        Graphics2D graphics2d1 = (Graphics2D)graphics2d.create();
        GraphHelper.setStroke(graphics2d1, GraphHelper.getStroke(9));
        graphics2d1.setPaint(PAGINATE_LINE_COLOR);
        Report report = (Report)designer.getTarget();
        ReportSettingsProvider reportsettingsprovider = report.getReportSettings();
        PaperSettingProvider papersettingprovider = reportsettingsprovider.getPaperSetting();
        PaperSize papersize = papersettingprovider.getPaperSize();
        Margin margin = papersettingprovider.getMargin();
        double d = papersize.getWidth().toPixD(resolution);
        double d1 = papersize.getHeight().toPixD(resolution);
        if(papersettingprovider.getOrientation() == 1)
        {
            d = papersize.getHeight().toPixD(resolution);
            d1 = papersize.getWidth().toPixD(resolution);
        }
        d = d - margin.getLeft().toPixD(resolution) - (double)margin.getRight().toPixI(resolution);
        d1 = d1 - (double)margin.getTop().toPixI(resolution) - (double)margin.getBottom().toPixI(resolution);
        int i = designer.getHorizontalValue();
        int j = designer.getVerticalValue();
        int k = designer.getWidth();
        int l = designer.getHeight();
        for(double d2 = d; d2 < (double)(k + i); d2 += d)
            GraphHelper.drawLine(graphics2d1, d2 - (double)i, 0.0D, d2 - (double)i, l, 9);

        for(double d3 = d1; d3 < (double)(l + j); d3 += d1)
            GraphHelper.drawLine(graphics2d1, 0.0D, d3 - (double)j, k, d3 - (double)j, 9);

        graphics2d1.dispose();
    }

    static 
    {
        PAGINATE_LINE_COLOR = Color.GRAY;
    }

}
