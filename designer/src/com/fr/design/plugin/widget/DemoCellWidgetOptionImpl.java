package com.fr.design.plugin.widget;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.fun.CellWidgetOptionProvider;
import com.fr.form.ui.Widget;

/**
 * @author richie
 * @date 2015-01-29
 * @since 8.0
 */
public class DemoCellWidgetOptionImpl implements CellWidgetOptionProvider {
    @Override
    public Class<? extends Widget> classForWidget() {
        return QuickButton.class;
    }

    @Override
    public Class<? extends BasicBeanPane<? extends Widget>> appearanceForWidget() {
        return QuickButtonDefinePane.class;
    }

    @Override
    public String iconPathForWidget() {
        return "/com/fr/design/images/data/user_widget.png";
    }

    @Override
    public String nameForWidget() {
        return "Ãÿ ‚∞¥≈•";
    }
}
