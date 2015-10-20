package com.fr.design.widget;

import com.fr.design.beans.BasicBeanPane;
import com.fr.form.ui.Widget;

/**
 * �����пؼ�����ƽ���
 */
public class Appearance {

    public static final String P_MARK = "Mark100";

    private Class<? extends BasicBeanPane<? extends Widget>> defineClass;
    private String displayName;

    /**
     * ���캯������������ڿؼ�����
     * @param defineClass �������ƽ������
     * @param displayName ��ʾֵ
     */
    public Appearance(Class<? extends BasicBeanPane<? extends Widget>> defineClass, String displayName) {
        this.defineClass = defineClass;
        this.displayName = displayName;
    }

    public Class<? extends BasicBeanPane<? extends Widget>> getDefineClass() {
        return defineClass;
    }

    public String getDisplayName() {
        return displayName;
    }
}
