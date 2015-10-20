package com.fr.design.style.background;

import javax.swing.JPanel;

import com.fr.design.style.background.gradient.GradientBackgroundSelectPane;
import com.fr.design.style.color.ColorUIComboBoxPane;

/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2012-4-24 ����03:42:10
 * ��˵��: û��ͼƬѡ��ı��� box
 */
public class BackgroundUIComboBoxNoImagePane extends BackgroundUIComboBoxPane {
	private static final long serialVersionUID = 7083594227200640469L;

	public BackgroundUIComboBoxNoImagePane() {
		initPane();
	}
	
	protected String[] getSelectType() {
		return new String[]{none, color, gradient};
	}
	
	protected void initSelectPane() {
		if(layoutPane != null) {
			layoutPane.add(new JPanel(), "none");
			layoutPane.add(colorPane = new ColorUIComboBoxPane(), "color");
			layoutPane.add(gradientPane = new GradientBackgroundSelectPane(), "gradient");
			if(cardLayout != null) {
				cardLayout.show(layoutPane, "none");
			}
		}
	}
}
