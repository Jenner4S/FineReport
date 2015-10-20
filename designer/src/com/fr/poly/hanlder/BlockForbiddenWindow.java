/**
 * 
 */
package com.fr.poly.hanlder;

import javax.swing.JWindow;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.icon.IconPathConstants;
import com.fr.general.Inter;

/**
 * ��ֹ���ص�����ʾ����
 * 
 * @author neil
 *
 * @date: 2015-2-27-����10:49:42
 */
public class BlockForbiddenWindow extends JWindow{
	
	private static final int WIDTH = 150;
	private static final int HEIGHT = 20;
	
    private UIButton promptButton = new UIButton(Inter.getLocText("FR-Designer_Block-intersect"), BaseUtils.readIcon(IconPathConstants.FORBID_ICON_PATH));

	/**
	 * ���캯��
	 */
	public BlockForbiddenWindow() {
		this.add(promptButton);
		
		this.setSize(WIDTH, HEIGHT);
	}
	
	/**
	 * ��ָ��λ����ʾ����, Ĭ�Ͻ�window�����ĵ�ŵ�ָ��λ����
	 * 
	 * @param x x����
	 * @param y y����
	 * 
	 */
	public void showWindow(int x, int y){
		this.setLocation(x - WIDTH / 2, y - HEIGHT / 2);
		this.setVisible(true);
	}
	
	/**
	 * ���ص�ǰ����
	 * 
	 */
	public void hideWindow(){
		this.setVisible(false);
	}
}
