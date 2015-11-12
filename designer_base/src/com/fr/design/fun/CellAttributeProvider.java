package com.fr.design.fun;

import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;

/**
 * Created by zhouping on 2015/11/11.
 */
public interface CellAttributeProvider {
    String MARK_STRING = "CellAttributeProvider";

    /**
     * 构造单元格属性面板
     * @return 面板类
     */
    AbstractAttrNoScrollPane createCellAttributePane();
}
