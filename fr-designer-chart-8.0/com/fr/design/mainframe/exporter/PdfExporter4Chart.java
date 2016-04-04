// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.exporter;

import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.design.mainframe.*;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.stable.OperatingSystem;
import com.fr.stable.StableUtils;
import com.fr.third.com.lowagie.text.*;
import com.fr.third.com.lowagie.text.pdf.*;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.*;
import java.util.HashMap;
import java.util.Locale;

// Referenced classes of package com.fr.design.mainframe.exporter:
//            ImageExporter4Chart

public class PdfExporter4Chart extends ImageExporter4Chart
{
    public static class MyFontMapper
        implements FontMapper
    {
        public static class BaseFontParameters
        {

            public String fontName;
            public String encoding;
            public boolean embedded;
            public boolean cached;
            public byte ttfAfm[];
            public byte pfb[];

            public String toString()
            {
                return (new StringBuilder()).append("{fontName:").append(fontName).append(",encoding:").append(encoding).append(",embedded:").append(embedded).append(",cached:").append(cached).toString();
            }

            public BaseFontParameters(String s)
            {
                fontName = s;
                encoding = "Identity-H";
                embedded = true;
                cached = true;
            }
        }


        public static final String CHINESE_SIMPLIFIED_FONT = "STSong-Light";
        public static final String CHINESE_SIMPLIFIED_ENCODING_H = "UniGB-UCS2-H";
        public static final String CHINESE_SIMPLIFIED_ENCODING_V = "UniGB-UCS2-V";
        public static final String CHINESE_TRADITIONAL_FONT_M_HEI = "MHei-Medium";
        public static final String CHINESE_TRADITIONAL_FONT_M_SUNG = "MSung-Light";
        public static final String CHINESE_TRADITIONAL_ENCODING_H = "UniCNS-UCS2-H";
        public static final String CHINESE_TRADITIONAL_ENCODING_V = "UniCNS-UCS2-V";
        public static final String JAPANESE_FONT_GO = "HeiseiKakuGo-W5";
        public static final String JAPANESE_FONT_MIN = "HeiseiMin-W3";
        public static final String JAPANESE_ENCODING_H = "UniJIS-UCS2-H";
        public static final String JAPANESE_ENCODING_V = "UniJIS-UCS2-V";
        public static final String JAPANESE_ENCODING_HW_H = "UniJIS-UCS2-HW-H";
        public static final String JAPANESE_ENCODING_HW_V = "UniJIS-UCS2-HW-V";
        public static final String KOREAN_FONT_GO_THIC = "HYGoThic-Medium";
        public static final String KOREAN_FONT_S_MYEONG_JO = "HYSMyeongJo-Medium";
        public static final String KOREAN_ENCODING_H = "UniKS-UCS2-H";
        public static final String KOREAN_ENCODING_V = "UniKS-UCS2-V";
        public static BaseFont defaultFont;
        private HashMap mapper;

        public BaseFont awtToPdf(Font font)
        {
            try
            {
                BaseFontParameters basefontparameters = getBaseFontParameters(font.getFontName());
                if(basefontparameters == null)
                    basefontparameters = getBaseFontParameters(font.getName());
                if(basefontparameters != null)
                    return BaseFont.createFont(basefontparameters.fontName, basefontparameters.encoding, basefontparameters.embedded, basefontparameters.cached, basefontparameters.ttfAfm, basefontparameters.pfb);
            }
            catch(Exception exception)
            {
                FRContext.getLogger().error(exception.getMessage(), exception);
                throw new ExceptionConverter(exception);
            }
            if(defaultFont != null)
                return defaultFont;
            String s = "Courier";
            if(font.isBold() && font.isItalic())
                s = "Courier-BoldOblique";
            else
            if(font.isBold())
                s = "Courier-Bold";
            else
            if(font.isItalic())
                s = "Courier-Oblique";
            return BaseFont.createFont(s, "Cp1252", false);
        }

        public Font pdfToAwt(BaseFont basefont, int i)
        {
            String as[][] = basefont.getFullFontName();
            if(as.length == 1)
                return new Font(as[0][3], 0, i);
            String s = null;
            String s1 = null;
            for(int j = 0; j < as.length; j++)
            {
                String as1[] = as[j];
                if(ComparatorUtils.equals(as1[0], "1") && ComparatorUtils.equals(as1[1], "0"))
                {
                    s = as1[3];
                    continue;
                }
                if(!ComparatorUtils.equals(as1[2], "1033"))
                    continue;
                s1 = as1[3];
                break;
            }

            String s2 = s1;
            if(s2 == null)
                s2 = s;
            if(s2 == null)
                s2 = as[0][3];
            return new Font(s2, 0, i);
        }

        private BaseFontParameters getBaseFontParameters(String s)
        {
            return (BaseFontParameters)mapper.get(s);
        }

        protected void insertNames(String as[][], String s)
        {
            String s1 = null;
            int i = 0;
            do
            {
                if(i >= as.length)
                    break;
                String as1[] = as[i];
                if(ComparatorUtils.equals(as1[2], "1033"))
                {
                    s1 = as1[3];
                    break;
                }
                i++;
            } while(true);
            if(s1 == null)
                s1 = as[0][3];
            BaseFontParameters basefontparameters = new BaseFontParameters(s);
            mapper.put(s1, basefontparameters);
            for(int j = 0; j < as.length; j++)
                mapper.put(as[j][3], basefontparameters);

        }

        public int insertDirectory(String s)
        {
            File file = new File(s);
            if(!file.exists() || !file.isDirectory())
                return 0;
            File afile[] = file.listFiles();
            int i = 0;
            for(int j = 0; j < afile.length; j++)
            {
                File file1 = afile[j];
                String s1 = file1.getPath().toLowerCase();
                try
                {
                    if(matchPostfix(s1))
                    {
                        String as[][] = BaseFont.getFullFontName(file1.getPath(), "Cp1252", null);
                        insertNames(as, file1.getPath());
                        i++;
                        continue;
                    }
                    if(!s1.endsWith(".ttc"))
                        continue;
                    String as1[] = BaseFont.enumerateTTCNames(file1.getPath());
                    for(int k = 0; k < as1.length; k++)
                    {
                        String s2 = String.valueOf((new StringBuffer(file1.getPath())).append(',').append(k));
                        String as2[][] = BaseFont.getFullFontName(s2, "Cp1252", null);
                        insertNames(as2, s2);
                    }

                    i++;
                }
                catch(Exception exception)
                {
                    FRContext.getLogger().error(exception.getMessage(), exception);
                }
            }

            return i;
        }

        private boolean matchPostfix(String s)
        {
            return s.endsWith(".ttf") || s.endsWith(".otf") || s.endsWith(".afm");
        }

        public MyFontMapper()
        {
            mapper = new HashMap();
        }
    }


    protected static MyFontMapper fontMapper = null;

    public PdfExporter4Chart()
    {
    }

    public void export(OutputStream outputstream, JChart jchart)
        throws Exception
    {
        ChartDesigner chartdesigner = jchart.getChartDesigner();
        int i = chartdesigner.getArea().getCustomWidth();
        int j = chartdesigner.getArea().getCustomHeight();
        Document document = null;
        PdfWriter pdfwriter = null;
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        if(document == null)
        {
            document = new Document(new Rectangle(i, j));
            pdfwriter = PdfWriter.getInstance(document, bytearrayoutputstream);
            document.open();
        }
        PdfContentByte pdfcontentbyte = pdfwriter.getDirectContent();
        Graphics2D graphics2d = pdfcontentbyte.createGraphics(i, j, prepareFontMapper());
        paintGlyph(graphics2d, i, j, chartdesigner);
        graphics2d.dispose();
        if(document != null)
            document.close();
        try
        {
            outputstream.write(bytearrayoutputstream.toByteArray());
            outputstream.flush();
            outputstream.close();
        }
        catch(IOException ioexception)
        {
            FRLogger.getLogger().error(ioexception.getMessage());
        }
    }

    private static void insertDirectory4Linux()
    {
        InsertDirectory(fontMapper, new File("/usr/X11R6/lib/X11/fonts"));
        InsertDirectory(fontMapper, new File("/usr/share/fonts"));
        String s = StableUtils.pathJoin(new String[] {
            FRContext.getCurrentEnv().getPath(), "fonts"
        });
        InsertDirectory(fontMapper, new File(s));
        InsertDirectory(fontMapper, new File("/usr/X/lib/X11/fonts/TrueType"));
        InsertDirectory(fontMapper, new File("/usr/openwin/lib/X11/fonts/TrueType"));
    }

    private static void insertDirectory4Windows()
    {
        String s = System.getProperty("java.library.path");
        String as[] = StableUtils.splitString(s, ";");
        for(int i = 0; i < as.length; i++)
        {
            File file = new File(as[i]);
            InsertDirectory(fontMapper, new File(file, "Fonts"));
        }

        InsertDirectory(fontMapper, new File("C:\\WINNT\\Fonts"));
        InsertDirectory(fontMapper, new File("C:\\WINDOWS\\Fonts"));
    }

    protected static void InsertDirectory(MyFontMapper myfontmapper, File file)
    {
        if(file == null || !file.exists() || !file.isDirectory())
            return;
        myfontmapper.insertDirectory(file.getAbsolutePath());
        File afile[] = file.listFiles();
        for(int i = 0; i < afile.length; i++)
            InsertDirectory(myfontmapper, afile[i]);

    }

    protected static MyFontMapper prepareFontMapper()
    {
        if(fontMapper != null)
            return fontMapper;
        fontMapper = new MyFontMapper();
        try
        {
            if(OperatingSystem.isWindows())
                insertDirectory4Windows();
            else
                insertDirectory4Linux();
            String s = System.getProperty("java.home");
            if(s != null)
            {
                File file = new File(StableUtils.pathJoin(new String[] {
                    s, "lib", "fonts"
                }));
                if(file.exists() && file.isDirectory())
                    InsertDirectory(fontMapper, file);
            }
            if(FRContext.getLocale() == Locale.CHINA || FRContext.getLocale() == Locale.CHINESE)
                MyFontMapper.defaultFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
            else
            if(FRContext.getLocale() == Locale.TAIWAN || FRContext.getLocale() == Locale.TRADITIONAL_CHINESE)
                MyFontMapper.defaultFont = BaseFont.createFont("MSung-Light", "UniCNS-UCS2-H", false);
            else
            if(FRContext.getLocale() == Locale.JAPAN || FRContext.getLocale() == Locale.JAPANESE)
                MyFontMapper.defaultFont = BaseFont.createFont("HeiseiKakuGo-W5", "UniJIS-UCS2-H", false);
            else
            if(FRContext.getLocale() == Locale.KOREA || FRContext.getLocale() == Locale.KOREAN)
                MyFontMapper.defaultFont = BaseFont.createFont("HYGoThic-Medium", "UniKS-UCS2-H", false);
            else
                MyFontMapper.defaultFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
        }
        catch(Exception exception)
        {
            FRContext.getLogger().error(exception.getMessage(), exception);
        }
        return fontMapper;
    }

}
