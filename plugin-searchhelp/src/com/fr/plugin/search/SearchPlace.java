package com.fr.plugin.search;

import com.fr.design.fun.impl.AbstractTitleProcessor;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.utils.gui.GUICoreUtils;

import java.awt.*;

public class SearchPlace extends AbstractTitleProcessor {

    @Override
    public void hold(Container container, Component loggerComponent, Component loginComponent) {
        UILabel fixLabel = new UILabel();
        fixLabel.setPreferredSize(new Dimension(200, 0));
        container.add(loggerComponent, BorderLayout.WEST);
        container.add(loginComponent, BorderLayout.EAST);
        SearchPane searchTextField = new SearchPane();
        searchTextField.setPreferredSize(new Dimension(250, 24));
        container.add(searchTextField, BorderLayout.CENTER);
    }
}