package com.fr.design.mainframe.exporter;

import com.fr.base.FRContext;
import com.fr.design.mainframe.ChartDesigner;
import com.fr.design.mainframe.JChart;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.stable.OperatingSystem;
import com.fr.stable.StableUtils;
import com.fr.third.com.lowagie.text.Document;
import com.fr.third.com.lowagie.text.ExceptionConverter;
import com.fr.third.com.lowagie.text.Rectangle;
import com.fr.third.com.lowagie.text.pdf.*;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-22
 * Time: ����10:20
 */
public class PdfExporter4Chart extends ImageExporter4Chart {
    protected static MyFontMapper fontMapper = null;


    /**
     * �����crt������Ŀ���ļ�
     *
     * @param out   �����
     * @param chart chart�ļ�
     * @throws Exception ����ʧ�����׳����쳣
     */
    public void export(OutputStream out, JChart chart) throws Exception {
        ChartDesigner designer = chart.getChartDesigner();
        int imageWidth = designer.getArea().getCustomWidth();
        int imageHeight = designer.getArea().getCustomHeight();
        Document document = null;
        PdfWriter writer = null;
        ByteArrayOutputStream bo = new ByteArrayOutputStream();

        if (document == null) {
            document = new Document(new Rectangle(imageWidth, imageHeight));
            writer = PdfWriter.getInstance(document, bo);
            document.open();
        }
        //��chart����PDF��ȥ
        PdfContentByte cb = writer.getDirectContent();
        Graphics2D g2d = cb.createGraphics(imageWidth, imageHeight, prepareFontMapper());
        paintGlyph(g2d, imageWidth, imageHeight, designer);
        g2d.dispose();
        if (document != null) {
            document.close();
        }

        try {
            out.write(bo.toByteArray());
            out.flush();
            out.close();
        } catch (IOException e) {
            FRLogger.getLogger().error(e.getMessage());
        }


    }

    private static void insertDirectory4Linux() {
       /*
   	 * alex:��222��̨Redhat�Ļ����ϲ���,���ְѴ�windows������simsun.ttcֻ�з���usr/share/fonts/default/Type1����ſ���������
   	 * ����Ƿ���usr/share/fontsĿ¼����${env}/resources/fontsĿ¼����,��Ȼ���Զ���,����awtToPdf֮�󷵻س�ȥȴ�����޷�������
   	 * ��������Ȼ����һ��
   	 *
   	 * ����ʵ�ںܹ���,���ܱ������ϵͳ������ļ���������а�
   	 *
   	 * PS:��������Graphics drawString��ʱ������������
   	 */
        //Linux
        InsertDirectory(fontMapper, new File("/usr/X11R6/lib/X11/fonts"));
        InsertDirectory(fontMapper, new File("/usr/share/fonts"));
        String path = StableUtils.pathJoin(new String[]{FRContext.getCurrentEnv().getPath(), "fonts"});
        //��ȥweb-inf/fonts������һ��, ʡȥ�ͻ��Ĵ���jdk��װ·�����鷳
        InsertDirectory(fontMapper, new File(path));

        //Solaris
        InsertDirectory(fontMapper, new File("/usr/X/lib/X11/fonts/TrueType"));
        InsertDirectory(fontMapper, new File("/usr/openwin/lib/X11/fonts/TrueType"));
    }

    private static void insertDirectory4Windows() {
        String libraryPath = System.getProperty("java.library.path");
        String[] libraryPathArray = StableUtils.splitString(libraryPath, ";");
        for (int i = 0; i < libraryPathArray.length; i++) {
            File libraryFile = new File(libraryPathArray[i]);
            InsertDirectory(fontMapper, new File(libraryFile, "Fonts"));
        }

        InsertDirectory(fontMapper, new File("C:\\WINNT\\Fonts"));
        InsertDirectory(fontMapper, new File("C:\\WINDOWS\\Fonts"));
    }

    //peter:ѭ�����е�Ŀ¼���������е�FontMapper.
    protected static void InsertDirectory(MyFontMapper fontMapper, File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return;
        }

        fontMapper.insertDirectory(dir.getAbsolutePath());

        File[] listFiles = dir.listFiles();
        for (int i = 0; i < listFiles.length; i++) {
            InsertDirectory(fontMapper, listFiles[i]);
        }
    }


    /**
     * Prepares FontMapper.
     */
    protected static MyFontMapper prepareFontMapper() {
        if (fontMapper != null) {
            return fontMapper;
        }

        fontMapper = new MyFontMapper();
        try {
            //Ȼ�����ϵͳFont����.
            if (OperatingSystem.isWindows()) {
                insertDirectory4Windows();

            } else {
                insertDirectory4Linux();

            }

            String javaHomeProp = System.getProperty("java.home");
            if (javaHomeProp != null) {
                File javaFontFile = new File(StableUtils.pathJoin(new String[]{
                        javaHomeProp, "lib", "fonts"
                }));
                if (javaFontFile.exists() && javaFontFile.isDirectory()) {
                    InsertDirectory(fontMapper, javaFontFile);
                }
            }

            if (FRContext.getLocale() == Locale.CHINA || FRContext.getLocale() == Locale.CHINESE) {
                MyFontMapper.defaultFont = BaseFont.createFont(MyFontMapper.CHINESE_SIMPLIFIED_FONT, MyFontMapper.CHINESE_SIMPLIFIED_ENCODING_H, BaseFont.NOT_EMBEDDED);
            } else if (FRContext.getLocale() == Locale.TAIWAN || FRContext.getLocale() == Locale.TRADITIONAL_CHINESE) {
                MyFontMapper.defaultFont = BaseFont.createFont(MyFontMapper.CHINESE_TRADITIONAL_FONT_M_SUNG, MyFontMapper.CHINESE_TRADITIONAL_ENCODING_H, BaseFont.NOT_EMBEDDED);
            } else if (FRContext.getLocale() == Locale.JAPAN || FRContext.getLocale() == Locale.JAPANESE) {
                MyFontMapper.defaultFont = BaseFont.createFont(MyFontMapper.JAPANESE_FONT_GO, MyFontMapper.JAPANESE_ENCODING_H, BaseFont.NOT_EMBEDDED);
            } else if (FRContext.getLocale() == Locale.KOREA || FRContext.getLocale() == Locale.KOREAN) {
                MyFontMapper.defaultFont = BaseFont.createFont(MyFontMapper.KOREAN_FONT_GO_THIC, MyFontMapper.KOREAN_ENCODING_H, BaseFont.NOT_EMBEDDED);
            } else {
                //Ĭ��Ҳ����һ����45422 , ������Ĭ������, linuxӢ�Ļ�����������
                MyFontMapper.defaultFont = BaseFont.createFont(MyFontMapper.CHINESE_SIMPLIFIED_FONT, MyFontMapper.CHINESE_SIMPLIFIED_ENCODING_H, BaseFont.NOT_EMBEDDED);
            }
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }

        return fontMapper;
    }

    public static class MyFontMapper implements FontMapper {

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

        public static class BaseFontParameters {

            public String fontName;
            public String encoding;
            public boolean embedded;
            public boolean cached;
            public byte ttfAfm[];
            public byte pfb[];

            public BaseFontParameters(String fontName) {
                this.fontName = fontName;
                encoding = BaseFont.IDENTITY_H;
                embedded = true;
                cached = true;
            }

            /**
             * toString����
             *
             * @return ����˵��
             */
            public String toString() {
                return "{fontName:" + fontName + ",encoding:" + encoding + ",embedded:" + embedded + ",cached:" + cached;
            }
        }

        public MyFontMapper() {
            mapper = new HashMap();
        }

        /**
         * ת������
         *
         * @param font awt����
         * @return pdf����
         */
        public BaseFont awtToPdf(Font font) {
            try {
                BaseFontParameters p = getBaseFontParameters(font.getFontName());

   				/*
   											 * alex:��������
   											 * ������,Arial������getFontName���ص���Arial Bold,����������ķ����еõ���Ӧ��p
   											 * ����getFontName�����õ�
   											 * ������linux�ϲ���,������getFontName���ص�ȴ��Dialog�����߼�����,ֻ��getName�ŷ���SimSun
   											 * ���Ի���ҪgetBaseFontParametersһ��
   											 */
                if (p == null) {
                    p = getBaseFontParameters(font.getName());
                }

                if (p != null) {
                    return BaseFont.createFont(p.fontName, p.encoding, p.embedded, p.cached, p.ttfAfm, p.pfb);
                } else {
                    //				FRContext.getLogger().info(Inter.getLocText("Utils-Font_Not_Found") + ":" + font.getFontName());

                    // alex:δ�ҵ����ʵ�����,�����Ĭ������,��֮,û�еĻ�,����Bold & Italic��������
                    if (defaultFont != null) {
                        return defaultFont;
                    } else {
                        String fontKey = BaseFont.COURIER;
                        if (font.isBold() && font.isItalic()) {
                            fontKey = BaseFont.COURIER_BOLDOBLIQUE;
                        } else if (font.isBold()) {
                            fontKey = BaseFont.COURIER_BOLD;
                        } else if (font.isItalic()) {
                            fontKey = BaseFont.COURIER_OBLIQUE;
                        }

                        return BaseFont.createFont(fontKey, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
                    }
                }
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
                throw new ExceptionConverter(e);
            }
        }

        /**
         * ת������
         *
         * @param font pdf����
         * @param size ��С
         * @return ת����awt����
         */
        public Font pdfToAwt(BaseFont font, int size) {
            String[][] names = font.getFullFontName();
            if (names.length == 1) {
                return new Font(names[0][3], 0, size);
            }
            String name10 = null;
            String name3x = null;
            for (int k = 0; k < names.length; ++k) {
                String[] name = names[k];
                if (ComparatorUtils.equals(name[0], "1") && ComparatorUtils.equals(name[1], "0")) {
                    name10 = name[3];
                } else if (ComparatorUtils.equals(name[2], "1033")) {
                    name3x = name[3];
                    break;
                }
            }
            String finalName = name3x;
            if (finalName == null) {
                finalName = name10;
            }
            if (finalName == null) {
                finalName = names[0][3];
            }
            return new Font(finalName, 0, size);
        }

        private BaseFontParameters getBaseFontParameters(String name) {
            return (BaseFontParameters) mapper.get(name);
        }

        protected void insertNames(String names[][], String path) {
            String main = null;
            int k = 0;
            do {
                if (k >= names.length) {
                    break;
                }
                String[] name = names[k];
                if (ComparatorUtils.equals(name[2], "1033")) {
                    main = name[3];
                    break;
                }
                k++;
            } while (true);
            if (main == null) {
                main = names[0][3];
            }
            BaseFontParameters p = new BaseFontParameters(path);

            // alex:��һ�������ֿ��ܶ�Ӧͬ����BaseFontParameters,����simsun�����嶼��Ӧͬ����p
            mapper.put(main, p);
            for (k = 0; k < names.length; k++) {
                mapper.put(names[k][3], p);
            }
        }

        /**
         * ����Ŀ¼�������
         *
         * @param dir Ŀ¼
         * @return ��������
         */
        public int insertDirectory(String dir) {
            File file = new File(dir);
            if (!file.exists() || !file.isDirectory()) {
                return 0;
            }

            File[] files = file.listFiles();
            int count = 0;
            for (int k = 0; k < files.length; k++) {
                file = files[k];
                String name = file.getPath().toLowerCase();
                try {
                    if (matchPostfix(name)) {
                        String[][] names = BaseFont.getFullFontName(file.getPath(), BaseFont.CP1252, null);
                        insertNames(names, file.getPath());
                        count++;
                    } else if (name.endsWith(".ttc")) {
                        String[] ttcs = BaseFont.enumerateTTCNames(file.getPath());
                        for (int j = 0; j < ttcs.length; j++) {
                            String nt = String.valueOf(new StringBuffer(file.getPath()).append(',').append(j));
                            String[][] names = BaseFont.getFullFontName(nt, BaseFont.CP1252, null);
                            insertNames(names, nt);
                        }

                        count++;
                    }
                } catch (Exception exception) {
                    FRContext.getLogger().error(exception.getMessage(), exception);
                }
            }

            return count;
        }

        private boolean matchPostfix(String name) {
            return name.endsWith(".ttf") || name.endsWith(".otf") || name.endsWith(".afm");
        }
    }
}
