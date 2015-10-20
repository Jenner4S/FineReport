package com.fr.design.gui.iscrollbar;

import javax.swing.*;
import java.awt.*;

/**
 * UIScrollBar��û�����ϵİ�ť��,��Ϊ8����
 *
 * @author zhou
 * @since 2012-5-9����4:32:59
 */
public class UIScrollBar extends JScrollBar {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
    private int temp = 10;

    public UIScrollBar(){
    }

	public UIScrollBar(int orientation) {
		super(orientation);
		setUI(new UIScrollBarUI());
	}

	@Override
	public Dimension getPreferredSize() {
		return getOrientation() == UIScrollBar.VERTICAL ?
				new Dimension(10, super.getPreferredSize().height)
				: new Dimension(super.getPreferredSize().width, 10);
	}

	@Override
	/**
	 * ȡ�ÿ��
	 */
	public int getWidth() {
		return getOrientation() == UIScrollBar.VERTICAL ? temp : super.getWidth();
	}

	@Override
	/**
	 * ȡ�ø߶�
	 */
	public int getHeight() {
		return getOrientation() == UIScrollBar.HORIZONTAL ? temp : super.getHeight();
	}
}
