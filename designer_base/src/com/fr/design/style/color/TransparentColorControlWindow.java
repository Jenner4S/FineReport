package com.fr.design.style.color;

import com.fr.design.gui.ipoppane.PopupHider;

/**
 * ֧��͸����colorControlWindow
 * 
 * @author focus
 * @time 2014��11��13������7:44:11
 * @version
 */
public abstract class TransparentColorControlWindow extends  ColorControlWindow{
	protected abstract void colorChanged();

	/**
	 * constructor
	 * 
	 * @param popupHider
	 */
	public TransparentColorControlWindow(PopupHider popupHider) {
		super(true, popupHider);
	}

}
