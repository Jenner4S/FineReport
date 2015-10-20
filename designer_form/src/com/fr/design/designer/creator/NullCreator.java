package com.fr.design.designer.creator;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComponent;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.SwingConstants;

import com.fr.form.ui.Widget;

//��������ķ�����������ģ��򲻿��������ǲ�ͬ�汾֮��
public class NullCreator extends XWidgetCreator {

	public NullCreator(Widget widget, Dimension initSize) {
		super(widget, initSize);
	}

	@Override
	protected String getIconName() {
		return "none_widget.png";
	}

	@Override
	protected JComponent initEditor() {
		UILabel l = new UILabel("UNEXPECTED WIDGET");
		l.setForeground(Color.red);
		l.setVerticalAlignment(SwingConstants.CENTER);
		l.setHorizontalAlignment(SwingConstants.CENTER);
		setBorder(DEFALUTBORDER);
		return editor = l;
	}
}
