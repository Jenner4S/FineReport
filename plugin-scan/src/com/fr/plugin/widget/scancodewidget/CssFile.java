package com.fr.plugin.widget.scancodewidget;

import com.fr.stable.fun.impl.AbstractCssFileHandler;

/**
 * Created by joyxu on 2016/3/14.
 */
public class CssFile extends AbstractCssFileHandler {
    @Override
    public String[] pathsForFiles() {
        return new String[]{
                "/com/fr/plugin/widget/scancodewidget/css/scan.css"
        };
    }

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }
}
