package com.fr.design.fun;


import java.awt.event.AWTEventListener;

/**
 * Created by zack on 2015/8/17.
 */
public interface GlobalListenerProvider {

    public static final String XML_TAG = "GlobalListenerProvider";

    public AWTEventListener listener();
}