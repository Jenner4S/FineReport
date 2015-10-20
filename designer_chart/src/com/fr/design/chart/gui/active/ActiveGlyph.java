package com.fr.design.chart.gui.active;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import com.fr.base.ScreenResolution;
import com.fr.base.chart.BaseChartGlyph;
import com.fr.base.chart.Glyph;
import com.fr.design.chart.gui.ActiveGlyphFactory;
import com.fr.design.chart.gui.ChartComponent;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-22
 * Time   : ����3:51
 * ѡ�е�Glyph
 */
public abstract class ActiveGlyph {
    protected Glyph parentGlyph;
    protected ChartComponent chartComponent;

    public ActiveGlyph(ChartComponent chartComponent, Glyph parentGlyph) {
        this.chartComponent = chartComponent;
        this.parentGlyph = parentGlyph;
    }

    public abstract Glyph getGlyph();
    
    public void drawAllGlyph(Graphics2D g2d, int resolution){
		Point2D offset4Paint = offset4Paint();
        g2d.translate(offset4Paint.getX(), offset4Paint.getY());
		this.getGlyph().draw(g2d, resolution);
		g2d.translate(-offset4Paint.getX(), -offset4Paint.getY());
	};
    
    /**
     * ���Ա���, ͨ����� չ������Ӧ�Ľ���.
     */
    public abstract void goRightPane();

    /**
     * ����ƫ�Ƶ�
     * @return ƫ�Ƶ�
     */
    public Point2D offset4Paint() {
        return new Point2D.Double(
                this.parentGlyph.getShape().getBounds().getX(),
                this.parentGlyph.getShape().getBounds().getY()
        );
    }

    public void paint4ActiveGlyph(Graphics2D g2d, BaseChartGlyph chartGlyph) {
    	if(this.parentGlyph == null) {
    		return;
    	}
    	
        Paint oldPaint = g2d.getPaint();
        Composite oldComposite = g2d.getComposite();
    	g2d.setPaint(Color.white);
    	g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

        g2d.fill(chartGlyph.getShape());
        drawAllGlyph(g2d, ScreenResolution.getScreenResolution());

        g2d.setPaint(oldPaint);
        g2d.setComposite(oldComposite);
    }
    
    protected void drawSelectedBounds4Active(Graphics2D g2d) {
    	if (this.getGlyph() != null) {
    		Shape shape = this.getGlyph().getShape();
    		if (shape != null) {
    			g2d.draw(shape);
    		}
    	}
    }


    /**
     *��ǰ��ActiveGlyph�Ƿ��������mouseX, mouseY
     * @param mouseX ����X
     * @param mouseY ����Y
     * @return �����򷵻�true
     */
    public boolean contains(int mouseX, int mouseY) {
        if (getGlyph() == null || getGlyph().getShape() == null){
        	return false;
        }

        Point2D offset = this.offset4Paint();

        /*
        * alex:��ΪLine2D.contains(x, y)��Ȼ����false
        * ������intersectһ������,��������С��4 * 4��,�����һЩ,������һЩ
        */
        return getGlyph().getShape().intersects(mouseX - offset.getX() - 2, mouseY - offset.getY() - 2, 4, 4);
    }

    /**
     * �ڵ�ǰѡ�е�ActiveGlyph��,��������Children������mouseX, mouseYƥ���ActiveGlyph
     * @param mouseX ����X
     * @param mouseY ����Y
     * @return ��ǰativeGlyph
     */
    public ActiveGlyph findActionGlyphFromChildren(int mouseX, int mouseY) {
        Glyph currentGlyph = getGlyph();
        // ����Ӧ��.
        if (currentGlyph == null) {
            return null;
        }
        java.util.Iterator selectableChildren = currentGlyph.selectableChildren();

        ActiveGlyph resAG = null;
        while (selectableChildren.hasNext() && resAG == null) {
            ActiveGlyph childActiveGlyph = ActiveGlyphFactory.createActiveGlyph(chartComponent, selectableChildren.next(), currentGlyph);

            // ���childActiveGlyph��Ϊnull,��һ�����ӱ���û�з�������
            if (childActiveGlyph != null) {
                resAG = childActiveGlyph.findActionGlyphFromChildren(mouseX, mouseY);
            }

            // ���childActiveGlyph���ӱ�û�з���������,�Ϳ�һ�����childGlyph�Ƿ��������
            if (resAG == null && childActiveGlyph != null && childActiveGlyph.contains(mouseX, mouseY)) {
                resAG = childActiveGlyph;
            }
        }

        // �����ǰActiveGlyph�������ӱ���û����mouseX, mouseY��ƥ���,��һ�����Լ��Ƿ�ƥ��
        if (resAG == null) {
            if (this.contains(mouseX, mouseY)) {
                resAG = this;
            }
        }

        return resAG;
    }

    /**
     * ��ק
     * @param e �¼�
     */
    public void onMouseDragged(MouseEvent e) {

    }

    /**
     * �ƶ��¼�
     * @param e �¼�
     */
    public void onMouseMove(MouseEvent e) {

    }
}
