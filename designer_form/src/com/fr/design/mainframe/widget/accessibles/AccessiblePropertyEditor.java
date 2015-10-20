package com.fr.design.mainframe.widget.accessibles;

import java.awt.Component;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.design.Exception.ValidationException;
import com.fr.design.mainframe.widget.editors.AbstractPropertyEditor;

/**
 * @author richer
 * @since 6.5.3
 * ����ͨ����ť��������ʽ��ȥ�༭���ı༭��
 */
public class AccessiblePropertyEditor extends AbstractPropertyEditor {

    protected AccessibleEditor editor;

    public AccessiblePropertyEditor(AccessibleEditor editor) {
        this.editor = editor;
        editor.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                firePropertyChanged();
            }
        });
    }

    @Override
    public void setValue(Object value) {
        editor.setValue(value);
    }

    @Override
    public Object getValue() {
        return editor.getValue();
    }

    @Override
    public Component getCustomEditor() {
        return editor.getEditor();
    }

    @Override
    public void validateValue() throws ValidationException {
        editor.validateValue();
    }
}
