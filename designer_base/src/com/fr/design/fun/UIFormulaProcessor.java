package com.fr.design.fun;

import com.fr.design.formula.UIFormula;

/**
 * @author richie
 * @date 2015-04-17
 * @since 8.0
 * 公式编辑器界面处理接口
 */
public interface UIFormulaProcessor {
    String MARK_STRING = "UIFormulaProcessor";

    /**
     * 普通的公式编辑器界面类
     * @return 公式编辑器界面类
     */
    UIFormula appearanceFormula();

    /**
     * 当需要显示“保留公式”项时的公式编辑器界面类
     * @return 公式编辑器界面类
     */
    UIFormula appearanceWhenReserveFormula();
}
