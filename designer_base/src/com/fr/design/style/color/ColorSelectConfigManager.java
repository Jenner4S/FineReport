package com.fr.design.style.color;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * ���ʹ����ɫ
 * @author focus
 *
 */
public class ColorSelectConfigManager{
	
	// ���ʹ�õ���ɫ����
	private int colorNums = 20;
	// ���ʹ����ɫ
	private List<Color> colors;
	
	private static ColorSelectConfigManager colorSelectConfigManager = null;
	
	public  Color[] getColors() {
		if(colors == null){
			colors = new ArrayList<Color>();
		}
		return colors.toArray(new Color[colors.size()]);
	}

	public int getColorNum() {
		return colorNums;
	}
	public void setColorNum(int colorNums) {
		this.colorNums = colorNums;
	}

	public synchronized static ColorSelectConfigManager getInstance() {
		if (colorSelectConfigManager == null) {
			colorSelectConfigManager = new ColorSelectConfigManager();
		}
		return colorSelectConfigManager;
	}

	/**
	 * �����ɫ�����ʹ�ö�����
	 * 
	 * @param color ��ɫ
	 * 
	 */
	public void addToColorQueue(Color color){
		// �����ظ������ʹ����ɫ
		// ��Ϊ�и�����ȳ������⣬���ʹ�õ���ɫ��Ҫ�ŵ���ǰ������û��set
		if(colors.contains(color)){
			colors.remove(color);
		}
		colors.add(color);
	} 
}
