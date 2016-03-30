package com.fr.plugin.widget.clock;

import com.fr.design.fun.impl.AbstractFormWidgetOptionProvider;
import com.fr.form.ui.Widget;

/**
 * @author richie
 * @date 2015-03-23
 * @since 8.0
 */
public class ClockImpl extends AbstractFormWidgetOptionProvider {

    @Override
    public Class<? extends Widget> classForWidget() {
        return Clock.class;
    }

    @Override
    public Class<?> appearanceForWidget() {
        return XClock.class;
    }

    @Override
    public String iconPathForWidget() {
        return "/com/fr/plugin/widget/clock/images/clock.png";
    }

    @Override
    public String nameForWidget() {
        return " ±÷”";
    }

    @Override
    public boolean isContainer() {
        return false;
    }
}
