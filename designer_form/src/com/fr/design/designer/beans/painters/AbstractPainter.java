package com.fr.design.designer.beans.painters;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;

import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.general.Inter;

public abstract class AbstractPainter implements HoverPainter {

    protected Point hotspot;
    protected Rectangle hotspot_bounds;
    protected XLayoutContainer container;
    protected XCreator creator;

    /**
     * ���캯��
     * @param container ����
     */
    public AbstractPainter(XLayoutContainer container) {
        this.container = container;
    }

    @Override
    public void setHotspot(Point p) {
        hotspot = p;
    }
    
    /**
     *  ����ʼ����
     *  @param g ��ͼ��
     *  @param startX ��ʼxλ��
     *  @param startY ��ʼyλ��
     */
	public void paint(Graphics g, int startX, int startY) {
		if(hotspot_bounds != null){
			drawHotspot(g, hotspot_bounds.x, hotspot_bounds.y, hotspot_bounds.width, hotspot_bounds.height, Color.lightGray, true, false);
		}
	}
    
	/**
	 * ���ñ߽�
	 * @param rect λ��
	 */
    @Override
    public void setRenderingBounds(Rectangle rect) {
        hotspot_bounds = rect;
    }

    @Override
    public void setCreator(XCreator component) {
        this.creator = component;
    }

    protected void drawHotspot(Graphics g, int x, int y, int width, int height, boolean accept) {
        Color bColor = accept ? XCreatorConstants.LAYOUT_HOTSPOT_COLOR : XCreatorConstants.LAYOUT_FORBIDDEN_COLOR;
        drawHotspot(g, x, y, width, height, bColor, accept, false);
    }
    
    /**
     * ����Ӧ�����Ǳ���Ⱦ��ʾ��Ҫ���������������ǻ��߿�
     */
    protected void drawRegionBackground(Graphics g, int x, int y, int width, int height, Color bColor, boolean accept) {
    	drawHotspot(g, x, y, width, height, bColor, accept, true);
    }

    protected void drawHotspot(Graphics g, int x, int y, int width, int height, Color bColor, boolean accept, boolean drawBackground) {
        Graphics2D g2d = (Graphics2D) g;
        Color color = g2d.getColor();
        Stroke backup = g2d.getStroke();
        // ������������ʽ
        g2d.setStroke(XCreatorConstants.STROKE);
        g2d.setColor(bColor);
        if (!accept) {
            g2d.drawString(Inter.getLocText("Cannot-Add_To_This_Area") + "!", x + width / 3, y + height / 2);
        } else if (drawBackground) {
        	 g2d.fillRect(x, y, width, height);
        } else {
        	g2d.drawRect(x, y, width, height);
        }
        g2d.setStroke(backup);
        g2d.setColor(color);
    }
    
    
}
