package com.fr.design.fun;

import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.js.JavaScript;

/**
 * Created by zack on 2015/8/14.
 */
public interface JavaScriptActionProvider {

    public static final String XML_TAG = "JavaScriptActionProvider";

    FurtherBasicBeanPane<? extends JavaScript> getJavaScriptActionPane();
}
