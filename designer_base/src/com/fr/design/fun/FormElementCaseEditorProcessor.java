package com.fr.design.fun;

import java.beans.PropertyDescriptor;

/**
 * Created by zhouping on 2015/9/10.
 */
public interface FormElementCaseEditorProcessor {

    String MARK_STRING = "PropertyEditor";

    /**
     * 生成属性表
     * @param temp 传入当前操作的class
     * @return 返回属性表
     */
    PropertyDescriptor[] createPropertyDescriptor(Class<?> temp);

}
