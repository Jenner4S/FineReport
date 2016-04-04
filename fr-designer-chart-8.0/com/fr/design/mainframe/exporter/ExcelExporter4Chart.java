// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.exporter;

import com.fr.base.ExcelUtils;
import com.fr.design.mainframe.*;
import com.fr.general.FRLogger;
import com.fr.general.IOUtils;
import com.fr.stable.CoreGraphHelper;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

// Referenced classes of package com.fr.design.mainframe.exporter:
//            ImageExporter4Chart

public class ExcelExporter4Chart extends ImageExporter4Chart
{

    private static final int PICTURE_TYPE_PNG = 6;
    private static final int DEFAULT_COLUMN_SPAN = 12;
    private static final int DEFAULT_ROW_SPAN = 26;
    private Workbook workbook;
    private ClientAnchor anchor;

    public ExcelExporter4Chart()
    {
    }

    public void export(OutputStream outputstream, JChart jchart)
        throws Exception
    {
        try
        {
            ChartDesigner chartdesigner = jchart.getChartDesigner();
            int i = chartdesigner.getArea().getCustomWidth();
            int j = chartdesigner.getArea().getCustomHeight();
            BufferedImage bufferedimage = CoreGraphHelper.createBufferedImage(i, j, 1);
            Graphics2D graphics2d = bufferedimage.createGraphics();
            paintGlyph(graphics2d, i, j, chartdesigner);
            graphics2d.dispose();
            if(checkExcelExportVersion())
                workbook = new XSSFWorkbook();
            else
                workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet();
            if(checkExcelExportVersion())
                anchor = new XSSFClientAnchor(0, 0, 0, 0, 1, 1, 12, 26);
            else
                anchor = new HSSFClientAnchor(0, 0, 0, 0, (short)1, 1, (short)12, 26);
            Drawing drawing = sheet.createDrawingPatriarch();
            drawing.createPicture(anchor, loadPicture(bufferedimage));
            workbook.write(outputstream);
            outputstream.flush();
        }
        catch(Exception exception)
        {
            FRLogger.getLogger().error(exception.getMessage());
        }
    }

    private boolean checkExcelExportVersion()
    {
        return ExcelUtils.checkPOIJarExist();
    }

    private int loadPicture(BufferedImage bufferedimage)
        throws IOException
    {
        ByteArrayOutputStream bytearrayoutputstream = null;
        int i;
        bytearrayoutputstream = new ByteArrayOutputStream();
        IOUtils.writeImage(bufferedimage, "png", bytearrayoutputstream);
        i = workbook.addPicture(bytearrayoutputstream.toByteArray(), 6);
        if(bytearrayoutputstream != null)
        {
            bytearrayoutputstream.flush();
            bytearrayoutputstream.close();
        }
        bufferedimage.flush();
        return i;
        Exception exception;
        exception;
        if(bytearrayoutputstream != null)
        {
            bytearrayoutputstream.flush();
            bytearrayoutputstream.close();
        }
        bufferedimage.flush();
        throw exception;
    }
}
