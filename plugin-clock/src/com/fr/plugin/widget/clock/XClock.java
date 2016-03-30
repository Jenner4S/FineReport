package com.fr.plugin.widget.clock;

import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.general.IOUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author richie
 * @date 2015-03-23
 * @since 8.0
 */
public class XClock extends XWidgetCreator {

    public XClock(Clock widget, Dimension initSize) {
        super(widget, initSize);
    }

    @Override
    protected JComponent initEditor() {
        if (editor == null) {
            editor = new JLabel(IOUtils.readIcon(getIconPath()));
        }
        return editor;
    }

    public String getIconPath() {
        return "/com/fr/plugin/widget/clock/images/clock.png";
    }
}
