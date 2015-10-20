package com.fr.design.style.background.texture;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Background;
import com.fr.design.style.background.BackgroundPane4BoxChange;

/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2011-11-3 ����10:36:21
 * ��˵��: ����ѡ���UIComboBox�л���֧pane, ԭ��ͼ@5471
 */
public class TextureUIComboBoxPane extends BackgroundPane4BoxChange {
	private static final long serialVersionUID = 6730560115446086974L;
	
	private TextureSelectBox textureBox;
	
	public TextureUIComboBoxPane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		
		JPanel pane = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
		this.add(pane, BorderLayout.NORTH);
		
		pane.add(textureBox = new TextureSelectBox(80));
	}
	
	public void populate(Background background) {
		
	}
	
	public Background update() {
		return null;
	}
	
	@Override
	protected String title4PopupWindow() {
		return null;
	}

}
