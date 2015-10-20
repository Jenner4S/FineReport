/**
 * 
 */
package com.fr.start;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.fr.base.BaseUtils;
import com.fr.base.GraphHelper;
import com.fr.general.GeneralContext;
import com.fr.stable.Constants;
import com.fr.stable.CoreGraphHelper;

/**
 * @author neil
 *
 * @date: 2015-3-13-����10:20:43
 */
public class SplashPane extends JPanel{

	/**
	 * ��ȡ�Ѿ�������ϵ���������
	 * 
	 * @return ������ϵ���������
	 * 
	 */
	public BufferedImage getSplashImage() {
		Image image = createSplashBackground();
		return CoreGraphHelper.toBufferedImage(image);
	}
	
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        Image image = getSplashImage();
        ImageIcon imageIcon = new ImageIcon(image);
        GraphHelper.paintImage(g2d, imageIcon.getIconWidth(), imageIcon.getIconHeight(), image, Constants.IMAGE_DEFAULT, Constants.NULL, Constants.CENTER, -1, -1);
    }

    /**
	 * ����������������, ��̬�ı���ı�, �� ��ǰ������ģ����Ϣ
	 * 
	 * @param text ָ�����ı�
	 * 
	 */
	public void setShowText(String text) {
		
	}

	/**
	 * ������������ı���ͼƬ
	 * 
	 * @return ����ͼƬ
	 * 
	 */
	public Image createSplashBackground() {
        if (GeneralContext.isChineseEnv()) {
            return BaseUtils.readImage("/com/fr/base/images/oem/splash_chinese.png");
        }
        
        return BaseUtils.readImage("/com/fr/base/images/oem/splash_english.png");
    }
	
    /**
	 * ���ڹرպ�ȡ����ʱ��ȡģ����Ϣ��timer
	 * 
	 */
	public void releaseTimer() {
		
	}
	
}
