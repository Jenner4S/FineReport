package com.fr.design.gui.frpane;

import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.module.DesignModuleFactory;

/**
 * �����ĳ�����������, ������ͼ����س���.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2013-6-26 ����04:46:02
 */
public class BaseHyperlinkGroup implements HyperlinkGroupType {

	/**
	 * ����֧�ֵĳ�����������
	 *
	 * @return
	 */
	public NameableCreator[] getHyperlinkCreators() {
		return DesignModuleFactory.getCreators4Hyperlink();
	}

}
