package com.fr.design.mainframe.widget.editors;

import com.fr.design.mainframe.widget.accessibles.AccessibleDictionaryEditor;
import com.fr.design.mainframe.widget.accessibles.AccessiblePropertyEditor;

/**
 * ���о��������ֵ����ԵĿؼ���ʹ�ô˱༭��
 * @version 6.5.3
 */
public class DictionaryEditor extends AccessiblePropertyEditor {

    public DictionaryEditor() {
        super(new AccessibleDictionaryEditor());
    }
}
