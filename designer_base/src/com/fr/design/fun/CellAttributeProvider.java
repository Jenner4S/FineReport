package com.fr.design.fun;

import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;

/**
 * Created by zhouping on 2015/11/11.
 */
public interface CellAttributeProvider {
    String MARK_STRING = "CellAttributeProvider";

    /**
     * ���쵥Ԫ���������
     * @return �����
     */
    AbstractAttrNoScrollPane createCellAttributePane();
}
