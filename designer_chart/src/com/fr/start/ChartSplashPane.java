package com.fr.start;

import java.awt.Image;

import com.fr.base.BaseUtils;

public class ChartSplashPane extends SplashPane{

	/**
	 * ������������ı���ͼƬ
	 * 
	 * @return ����ͼƬ
	 * 
	 */
	public Image createSplashBackground() {
		return BaseUtils.readImage("/com/fr/design/images/splash4Chart.png");
	}
}