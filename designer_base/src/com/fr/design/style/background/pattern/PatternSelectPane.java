package com.fr.design.style.background.pattern;

import com.fr.general.Inter;
import com.fr.design.style.background.BackgroundPane.BackgroundSettingPane;
import com.fr.design.style.background.BackgroundPane.PatternBackgroundPaneNoFore;
import com.fr.design.style.background.BackgroundSelectPane;



/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2011-11-1 ����03:12:49
 * ��˵��: ͼ���ı���ѡ�� ��������
 */
public class PatternSelectPane extends BackgroundSelectPane {
	private static final long serialVersionUID = 6504634749254957415L;
	
	public PatternSelectPane(double preWidth) {
		initBackgroundShowPane(getShowPane(preWidth));
	}

	@Override
	public BackgroundSettingPane getShowPane(double preWidth) {
		// ����6��. ��Ϊ��Ŀ̫����. �����ĺܳ�
		int column = Math.max((int)preWidth / 25, 6);
		return new PatternBackgroundPaneNoFore(column); 
	}

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("Background-Pattern");
	}

}
