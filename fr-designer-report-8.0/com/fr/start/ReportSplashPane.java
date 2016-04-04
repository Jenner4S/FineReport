// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.start;

import com.fr.base.BaseUtils;
import com.fr.base.GraphHelper;
import com.fr.design.mainframe.bbs.BBSConstants;
import com.fr.general.*;
import com.fr.stable.CoreGraphHelper;
import com.fr.stable.StableUtils;
import com.fr.stable.module.ModuleAdapter;
import com.fr.stable.module.ModuleListener;
import java.awt.*;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.ImageIcon;

// Referenced classes of package com.fr.start:
//            SplashPane

public class ReportSplashPane extends SplashPane
{

    private static final String OEM_PATH = "/com/fr/base/images/oem";
    private static final String SPLASH_CN = "splash_chinese.png";
    private static final String SPLASH_EN = "splash_english.png";
    private static final String SPLASH_MAC_CN = "splash_chinese_mac.png";
    private static final String SPLASH_MAC_EN = "splash_english_mac.png";
    private static final Color MODULE_COLOR = new Color(230, 230, 230);
    private static final int MODULE_INFO_X = 25;
    private static final int MODULE_INFO_Y = 270;
    private static final Color THANK_COLOR = new Color(72, 216, 249);
    private static final int THANK_INFO_X = 460;
    private static final String GUEST = getRandomUser();
    private String showText;
    private String moduleID;
    private int loadingIndex;
    private String loading[] = {
        "..", "....", "......"
    };
    private Timer timer;
    private ModuleListener moduleListener;

    public ReportSplashPane()
    {
        showText = "";
        moduleID = "";
        loadingIndex = 0;
        timer = new Timer();
        moduleListener = new ModuleAdapter() {

            final ReportSplashPane this$0;

            public void onStartBefore(String s, String s1)
            {
                moduleID = s1;
                loadingIndex++;
                setShowText(moduleID.isEmpty() ? "" : (new StringBuilder()).append(moduleID).append(loading[loadingIndex % 3]).toString());
                repaint();
            }

            
            {
                this$0 = ReportSplashPane.this;
                super();
            }
        }
;
        setBackground(null);
        timer.schedule(new TimerTask() {

            final ReportSplashPane this$0;

            public void run()
            {
                loadingIndex++;
                setShowText(moduleID.isEmpty() ? "" : (new StringBuilder()).append(moduleID).append(loading[loadingIndex % 3]).toString());
                repaint();
            }

            
            {
                this$0 = ReportSplashPane.this;
                super();
            }
        }
, 0L, 300L);
        ModuleContext.registerModuleListener(moduleListener);
    }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D graphics2d = (Graphics2D)g;
        BufferedImage bufferedimage = getSplashImage();
        ImageIcon imageicon = new ImageIcon(bufferedimage);
        GraphHelper.paintImage(graphics2d, imageicon.getIconWidth(), imageicon.getIconHeight(), bufferedimage, 3, -1, 0, -1, -1);
    }

    public void setShowText(String s)
    {
        showText = s;
    }

    public BufferedImage getSplashImage()
    {
        Image image = createSplashBackground();
        BufferedImage bufferedimage = CoreGraphHelper.toBufferedImage(image);
        Graphics2D graphics2d = bufferedimage.createGraphics();
        graphics2d.setPaint(new Color(230, 230, 230));
        graphics2d.setFont(new Font("Dialog", 0, 11));
        paintShowText(graphics2d);
        return bufferedimage;
    }

    private void paintShowText(Graphics2D graphics2d)
    {
        java.awt.font.FontRenderContext fontrendercontext = graphics2d.getFontRenderContext();
        LineMetrics linemetrics = graphics2d.getFont().getLineMetrics("", fontrendercontext);
        double d = linemetrics.getLeading();
        double d1 = linemetrics.getAscent();
        double d2 = linemetrics.getHeight();
        graphics2d.setPaint(MODULE_COLOR);
        graphics2d.setFont(new Font("Dialog", 0, 12));
        double d3 = 270D + d2 + d + d1;
        GraphHelper.drawString(graphics2d, showText, 25D, d3);
        graphics2d.setPaint(THANK_COLOR);
        String s = (new StringBuilder()).append(Inter.getLocText("FR-Designer_Thanks-To")).append(GUEST).toString();
        GraphHelper.drawString(graphics2d, s, 460D, d3);
    }

    private static String getRandomUser()
    {
        int i = (new Random()).nextInt(BBSConstants.ALL_GUEST.length);
        return (new StringBuilder()).append(" ").append(BBSConstants.ALL_GUEST[i]).toString();
    }

    public void releaseTimer()
    {
        timer.cancel();
    }

    public Image createSplashBackground()
    {
        String s = getImageName();
        return BaseUtils.readImage(StableUtils.pathJoin(new String[] {
            "/com/fr/base/images/oem", s
        }));
    }

    private String getImageName()
    {
        boolean flag = GeneralContext.isChineseEnv();
        return flag ? "splash_chinese_mac.png" : "splash_english_mac.png";
    }






}
