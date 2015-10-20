package com.fr.design.mainframe.widget.editors;

import java.beans.PropertyEditor;

import com.fr.design.Exception.ValidationException;

/**
 * @author richer
 * @since 6.5.3
 * ���Ա༭��
 */
public interface ExtendedPropertyEditor extends PropertyEditor {
	void validateValue() throws ValidationException;

	void setDefaultValue(Object v);
	
	//ͷ������,������������,������Ա��˷�̫��ʱ����,Ϊʵ����๦��,��������������;
	boolean refreshInTime();
}
