package com.fr.design.designer.beans.events;

import java.util.EventListener;

/**
 * ���������������ı༭�������ӿ�
 * @since 6.5.4
 * @author richer
 */
public interface DesignerEditListener extends EventListener {

	void fireCreatorModified(DesignerEvent evt);
	
}
