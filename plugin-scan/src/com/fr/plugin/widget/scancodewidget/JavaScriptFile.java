package com.fr.plugin.widget.scancodewidget;

import com.fr.stable.fun.impl.AbstractJavaScriptFileHandler;

/**
 * Created by joyxu on 2016/3/8.
 */
public class JavaScriptFile extends AbstractJavaScriptFileHandler {
    @Override
    public String[] pathsForFiles() {
        return new String[]{"/com/fr/plugin/widget/scancodewidget/widget.scancode.js"};
    }

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }
}
