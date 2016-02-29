package com.fr.plugin.designer;

import com.fr.design.fun.impl.AbstractGlobalListenerProvider;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;

/**
 * Created by vito on 2015/9/23
 */
public class BosskeyOfSearch extends AbstractGlobalListenerProvider {
    /**
     *监听器
     * @return 事件监听器
     */
    @Override
    public AWTEventListener listener() {
        return new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                if (event instanceof KeyEvent) {
                    KeyEvent e = (KeyEvent) event;
                    if (e.getID() == KeyEvent.KEY_RELEASED) {
                        ActionTreeSearch.click(e);
                    }
                }
            }
        };
    }
}