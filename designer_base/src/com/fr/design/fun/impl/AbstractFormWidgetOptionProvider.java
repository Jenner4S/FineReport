package com.fr.design.fun.impl;

import com.fr.design.fun.FormWidgetOptionProvider;

/**
 * @author richie
 * @date 2015-05-13
 * @since 8.0
 */
public abstract class AbstractFormWidgetOptionProvider implements FormWidgetOptionProvider {

    @Override
    public boolean isContainer() {
        return false;
    }
}