package com.fr.design.designer.beans;

import java.awt.Graphics;
import java.awt.Rectangle;

public interface Painter {
    //��ǰ�������򣬼����������ı߽�
    void setRenderingBounds(Rectangle rect);

    //��Ⱦ��ڣ���FormDesigner��������ɶ�����Ⱦ
	void paint(Graphics g, int startX, int startY);
}
