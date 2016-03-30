package com.fr.plugin.widget.radio;

import com.fr.form.ui.ToggleButton;

/**
 * 地产行业单选按钮组按钮
 * @author focus
 * @date Jun 17, 2015
 * @since 8.0
 */
public class EstateRadio extends ToggleButton {

    public String getXType() {
        return "estateradio";
    }

    public boolean equals(Object obj) {
        return super.equals(obj) && obj instanceof EstateRadio;
    }
}
