package com.fr.plugin.widget.radio;

import com.fr.form.ui.ToggleButton;

/**
 * �ز���ҵ��ѡ��ť�鰴ť
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
