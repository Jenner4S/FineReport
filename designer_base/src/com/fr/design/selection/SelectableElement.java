package com.fr.design.selection;

import com.fr.design.designer.TargetComponent;

/**
 * 
 * @author zhou
 * @since 2012-7-26����10:20:22
 */
public interface SelectableElement {

	/**
	 * ��ȡѡ��Ԫ�صĿ��ٱ༭����
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public QuickEditor getQuickEditor(TargetComponent tc);

}
