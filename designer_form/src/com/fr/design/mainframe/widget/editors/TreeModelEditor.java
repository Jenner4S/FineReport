package com.fr.design.mainframe.widget.editors;

import com.fr.design.mainframe.widget.accessibles.AccessiblePropertyEditor;
import com.fr.design.mainframe.widget.accessibles.AccessibleTreeModelEditor;

/**
 * �༭��ͼ���ؼ����������ؼ�
 * @since 6.5.3
 */
public class TreeModelEditor extends AccessiblePropertyEditor {

    public TreeModelEditor() {
        super(new AccessibleTreeModelEditor());
    }
}
