/**
 * 
 */
package com.fr.start;

import com.fr.base.BaseUtils;
import com.fr.base.GraphHelper;
import com.fr.design.mainframe.bbs.BBSConstants;
import com.fr.general.GeneralContext;
import com.fr.general.Inter;
import com.fr.general.ModuleContext;
import com.fr.stable.*;
import com.fr.stable.module.ModuleAdapter;
import com.fr.stable.module.ModuleListener;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.TimerTask;

/**
 * @author neil
 *
 * @date: 2015-3-13-����9:47:58
 */
public class ReportSplashPane extends SplashPane{
	
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
	
    private String showText = "";
    
    private String moduleID = "";
    private int loadingIndex = 0;
    private String[] loading = new String[]{"..", "....", "......"};
    private java.util.Timer timer = new java.util.Timer();

    public ReportSplashPane() {
        this.setBackground(null);
        
        timer.schedule(new TimerTask() {
            public void run() {
                loadingIndex++;
                ReportSplashPane.this.setShowText(moduleID.isEmpty() ? StringUtils.EMPTY : moduleID + loading[loadingIndex % 3]);
                ReportSplashPane.this.repaint();
            }
        }, 0, 300);
        
        ModuleContext.registerModuleListener(moduleListener);
    }
    
    private ModuleListener moduleListener = new ModuleAdapter() {
        @Override
        public void onStartBefore(String moduleName, String moduleI18nName) {
            moduleID = moduleI18nName;
            loadingIndex++;
            ReportSplashPane.this.setShowText(moduleID.isEmpty() ? StringUtils.EMPTY : moduleID + loading[loadingIndex % 3]);
            ReportSplashPane.this.repaint();
        }
    };

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        Image image = getSplashImage();
        ImageIcon imageIcon = new ImageIcon(image);
        GraphHelper.paintImage(g2d, imageIcon.getIconWidth(), imageIcon.getIconHeight(), image, Constants.IMAGE_DEFAULT, Constants.NULL, Constants.CENTER, -1, -1);
    }

    public void setShowText(String text) {
        this.showText = text;
    }

    public BufferedImage getSplashImage() {
        // p:��ʼ��splashImage,���л����ַ�.
        Image image = createSplashBackground();
        BufferedImage splashBuffedImage = CoreGraphHelper.toBufferedImage(image);

        Graphics2D splashG2d = splashBuffedImage.createGraphics();
        splashG2d.setPaint(new Color(230, 230, 230));
        splashG2d.setFont(new Font("Dialog", Font.PLAIN, 11));

        //������Ҫ��ʾ���ı�
        paintShowText(splashG2d);

        return splashBuffedImage;
    }
    
    private void paintShowText(Graphics2D splashG2d){
        FontRenderContext fontRenderContext = splashG2d.getFontRenderContext();
        LineMetrics fm = splashG2d.getFont().getLineMetrics("",
                fontRenderContext);
        double leading = fm.getLeading();
        double ascent = fm.getAscent();
        double height = fm.getHeight();

        splashG2d.setPaint(MODULE_COLOR);
        splashG2d.setFont(new Font("Dialog", Font.PLAIN, 12));

        //����ģ����Ϣ
        double y = MODULE_INFO_Y + height + leading + ascent;
        GraphHelper.drawString(splashG2d, showText, MODULE_INFO_X, y);
        
        //ÿ�������лһλ��̳�û�
        splashG2d.setPaint(THANK_COLOR);
        String content = Inter.getLocText("FR-Designer_Thanks-To") + GUEST;
        GraphHelper.drawString(splashG2d, content, THANK_INFO_X, y);
    }
    
    private static String getRandomUser(){
    	int num = new Random().nextInt(BBSConstants.ALL_GUEST.length);
    	return StringUtils.BLANK + BBSConstants.ALL_GUEST[num];
    }
    
    /**
	 * ���ڹرպ�ȡ����ʱ��ȡģ����Ϣ��timer
	 * 
	 */
	public void releaseTimer() {
        timer.cancel();
	}
	
	/**
	 * ������������ı���ͼƬ
	 * 
	 * @return ����ͼƬ
	 * 
	 */
	public Image createSplashBackground() {
		String fileName = getImageName();
		return BaseUtils.readImage(StableUtils.pathJoin(OEM_PATH, fileName));
    }
	
	//��ȡͼƬ�ļ���
	private String getImageName(){
		boolean isChina = GeneralContext.isChineseEnv();
        //jdk1.8��͸����bug, ������setWindowTransparent��, JFrameֱ����С����, ����mac�µļ���ͼƬ
        return isChina ? SPLASH_MAC_CN : SPLASH_MAC_EN;
	}


	
}
