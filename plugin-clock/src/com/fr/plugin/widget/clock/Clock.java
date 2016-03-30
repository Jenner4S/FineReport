package com.fr.plugin.widget.clock;

import com.fr.form.ui.Widget;

/**
 * @author richie
 * @date 2015-03-23
 * @since 8.0
 */
public class Clock extends Widget {
    @Override
    public String getXType() {
        return "clock";
    }

    @Override
    public boolean isEditor() {
        return false;
    }

    @Override
    public String[] supportedEvents() {
        return new String[0];
    }
}
