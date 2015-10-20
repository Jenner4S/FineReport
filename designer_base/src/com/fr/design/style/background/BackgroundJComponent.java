package com.fr.design.style.background;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import com.fr.general.Background;

/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2011-11-1 ����10:08:53
 * ��˵��: ���ڻ����ֱ�����JComponent, һ���JComponentֻ����Color
 */
public class BackgroundJComponent extends JComponent {
	private static final long serialVersionUID = 6592722025414985376L;
	
	protected Background background ;
	
	public BackgroundJComponent() {
		
	}
	
	public BackgroundJComponent(Background background) {
		this.background = background;
	}
	
    public void paint(Graphics g) {
    	super.paint(g);
    	
    	if(background != null && this.getSize().getWidth() > 0 && this.getSize().getHeight() > 0) {
    		background.paint(g, new Rectangle2D.Double(1, 1, this.getSize().getWidth() - 2, 
    				this.getSize().getHeight() - 2));
    	} 
    }
    
    public void setEmptyBackground() {
    	this.background = null;
    }
    
    public void setSelfBackground(Background background) {
    	this.background = background;
    }
}
