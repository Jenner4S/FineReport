/**
 * 
 */
package com.fr.start;

import java.awt.Image;

import com.fr.base.BaseUtils;

/**
 * @author neil
 *
 * @date: 2015-3-13-����9:47:58
 */
public class BISplashPane extends SplashPane{

	/**
	 * ������������ı���ͼƬ
	 * 
	 * @return ����ͼƬ
	 * 
	 */
	public Image createSplashBackground() {
        return BaseUtils.readImage("/com/fr/base/images/oem/splash-EN.jpg");
    }
	
}
