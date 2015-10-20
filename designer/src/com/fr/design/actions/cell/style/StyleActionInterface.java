package com.fr.design.actions.cell.style;

import com.fr.base.Style;

/**
 * peter:�������ֻ���ڱ༭Style��ʱ��ʹ��.
 */
public interface StyleActionInterface {
    public Style executeStyle(Style style2Mod, Style selectedStyle);

    /**
     * Update Style.
     *
     * @param style style
     */
    public void updateStyle(Style style);
}
