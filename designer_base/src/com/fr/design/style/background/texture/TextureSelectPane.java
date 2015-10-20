package com.fr.design.style.background.texture;

import com.fr.general.Inter;
import com.fr.design.style.background.BackgroundPane.BackgroundSettingPane;
import com.fr.design.style.background.BackgroundPane.TextureBackgroundPane;
import com.fr.design.style.background.BackgroundSelectPane;



/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2011-10-31 ����02:44:45
 * ��˵��: ���������ѡ��"����"����, �ο�: @Code ColorSelectPane
 */
public class TextureSelectPane extends BackgroundSelectPane {
	private static final long serialVersionUID = -3119065431234298949L;
	
	public TextureSelectPane(double preWidth) {
		initBackgroundShowPane(getShowPane(preWidth));
	}
	
	public BackgroundSettingPane getShowPane(double preWidth) {
		// ������ʵ���. ����4��. ���8��. 
		int column = Math.max((int)preWidth / 40, 4);
		return new TextureBackgroundPane(column);
	}
	
	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("Background-Texture");
	}
}
