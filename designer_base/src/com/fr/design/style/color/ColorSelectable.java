package com.fr.design.style.color;

import java.awt.Color;

public interface ColorSelectable {
	public void setColor(Color color);
	
	public Color getColor();
	
	/**
	 * ѡ����ɫ
	 * @param colorCell ��ɫ��Ԫ��
	 */
	public void colorSetted(ColorCell colorCell);
}
