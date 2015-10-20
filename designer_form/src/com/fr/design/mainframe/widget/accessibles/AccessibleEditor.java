package com.fr.design.mainframe.widget.accessibles;

import java.awt.Component;

import javax.swing.event.ChangeListener;

import com.fr.design.Exception.ValidationException;

/**
 * ���Ա༭��
 * @since 6.5.2
 */
public interface AccessibleEditor {

    /**
     * �ж������ֵ�Ƿ����Ҫ��
     * @throws ValidationException
     */
    public void validateValue() throws ValidationException;

    /**
     * ��ȡ�༭�������ֵ
     */
    public Object getValue();

    /**
     * ���༭������ֵ
     * @param v
     */
    public void setValue(Object v);

    /**
     * �Զ���༭��
     * @return
     */
    public Component getEditor();

    public void addChangeListener(ChangeListener l);

    public void removeChangeListener(ChangeListener l);
}
