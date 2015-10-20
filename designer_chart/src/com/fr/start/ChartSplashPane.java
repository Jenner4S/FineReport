package com.fr.start;

import java.awt.Image;

import com.fr.base.BaseUtils;

public class ChartSplashPane extends SplashPane{

	/**
	 * ´´½¨Æô¶¯»­ÃæµÄ±³¾°Í¼Æ¬
	 * 
	 * @return ±³¾°Í¼Æ¬
	 * 
	 */
	public Image createSplashBackground() {
		return BaseUtils.readImage("/com/fr/design/images/splash4Chart.png");
	}
}