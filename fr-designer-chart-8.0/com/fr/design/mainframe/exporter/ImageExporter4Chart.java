// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.exporter;

import com.fr.base.chart.BaseChartGlyph;
import com.fr.base.chart.chartdata.*;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartglyph.ChartGlyph;
import com.fr.data.TableDataSource;
import com.fr.design.mainframe.*;
import com.fr.form.ui.ChartBook;
import com.fr.script.Calculator;
import com.fr.stable.CoreGraphHelper;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import javax.imageio.ImageIO;

// Referenced classes of package com.fr.design.mainframe.exporter:
//            Exporter4Chart

public class ImageExporter4Chart
    implements Exporter4Chart
{

    private int resolution;
    protected Calculator calculator;

    public ImageExporter4Chart()
    {
        resolution = 96;
    }

    public void export(OutputStream outputstream, JChart jchart)
        throws Exception
    {
        ChartDesigner chartdesigner = jchart.getChartDesigner();
        int i = chartdesigner.getArea().getCustomWidth();
        int j = chartdesigner.getArea().getCustomHeight();
        BufferedImage bufferedimage = CoreGraphHelper.createBufferedImage(i, j, 1);
        Graphics2D graphics2d = bufferedimage.createGraphics();
        paintGlyph(graphics2d, i, j, chartdesigner);
        ImageIO.write(bufferedimage, "png", outputstream);
    }

    protected void paintGlyph(Graphics2D graphics2d, int i, int j, ChartDesigner chartdesigner)
    {
        if(i == 0 || j == 0)
            return;
        calculator = Calculator.createCalculator();
        calculator.setAttribute(com/fr/data/TableDataSource, null);
        graphics2d.setColor(Color.white);
        graphics2d.fillRect(0, 0, i, j);
        ChartCollection chartcollection = (ChartCollection)((ChartBook)chartdesigner.getTarget()).getChartCollection();
        Chart chart = chartcollection.getSelectedChart();
        TopDefinitionProvider topdefinitionprovider = chart.getFilterDefinition();
        ChartData chartdata = null;
        if(topdefinitionprovider instanceof BaseTableDefinition)
            chartdata = ((BaseTableDefinition)topdefinitionprovider).calcu4ChartData(calculator, chart.getDataProcessor());
        if(chartdata == null)
            chartdata = chart.defaultChartData();
        BaseChartGlyph basechartglyph = null;
        if(chart != null && chart.getPlot() != null)
            basechartglyph = chart.createGlyph(chartdata);
        if(basechartglyph instanceof ChartGlyph)
        {
            java.awt.Image image = ((ChartGlyph)basechartglyph).toImage(i, j, resolution);
            graphics2d.drawImage(image, 0, 0, null);
        }
    }
}
