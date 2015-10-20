/**
 * 
 */
package com.fr.start;

import java.awt.Image;

import com.fr.base.BaseUtils;

/**
 * @author neil
 *
 * @date: 2015-3-13-ÉÏÎç9:47:58
 */
public class BISplashPane extends SplashPane{

	/**
	 * ´´½¨Æô¶¯»­ÃæµÄ±³¾°Í¼Æ¬
	 * 
	 * @return ±³¾°Í¼Æ¬
	 * 
	 */
	public Image createSplashBackground() {
        return BaseUtils.readImage("/com/fr/base/images/oem/splash-EN.jpg");
    }
	
}
