package com.fr.design.style.background.texture;

import com.fr.general.Inter;
import com.fr.design.style.background.BackgroundPane.BackgroundSettingPane;
import com.fr.design.style.background.BackgroundPane.TextureBackgroundPane;
import com.fr.design.style.background.BackgroundSelectPane;



/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2011-10-31 下午02:44:45
 * 类说明: 背景纹理的选择"弹出"界面, 参考: @Code ColorSelectPane
 */
public class TextureSelectPane extends BackgroundSelectPane {
	private static final long serialVersionUID = -3119065431234298949L;
	
	public TextureSelectPane(double preWidth) {
		initBackgroundShowPane(getShowPane(preWidth));
	}
	
	public BackgroundSettingPane getShowPane(double preWidth) {
		// 计算合适的列. 至少4个. 最多8个. 
		int column = Math.max((int)preWidth / 40, 4);
		return new TextureBackgroundPane(column);
	}
	
	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("Background-Texture");
	}
}