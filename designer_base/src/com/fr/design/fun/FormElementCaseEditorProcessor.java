package com.fr.design.fun;

import java.beans.PropertyDescriptor;

/**
 * Created by zhouping on 2015/9/10.
 */
public interface FormElementCaseEditorProcessor {

    String MARK_STRING = "PropertyEditor";

    /**
     * �������Ա�
     * @param temp ���뵱ǰ������class
     * @return �������Ա�
     */
    PropertyDescriptor[] createPropertyDescriptor(Class<?> temp);

}
