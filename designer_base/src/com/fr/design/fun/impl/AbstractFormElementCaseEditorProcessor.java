package com.fr.design.fun.impl;

import com.fr.design.fun.FormElementCaseEditorProcessor;

import java.beans.PropertyDescriptor;

/**
 * Created by zhouping on 2015/9/10.
 */
public abstract class AbstractFormElementCaseEditorProcessor implements FormElementCaseEditorProcessor {

    /**
     * �������Ա�
     * @param temp ���뵱ǰ������class
     * @return �������Ա�
     */
    public PropertyDescriptor[] createPropertyDescriptor(Class<?> temp){
        return new PropertyDescriptor[0];
    }
}
