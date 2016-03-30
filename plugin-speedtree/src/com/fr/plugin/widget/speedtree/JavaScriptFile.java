package com.fr.plugin.widget.speedtree;

import com.fr.stable.fun.impl.AbstractJavaScriptFileHandler;

/**
 * @author zack
 * @date 2015-10-12
 * @since 8.0
 */
public class JavaScriptFile extends AbstractJavaScriptFileHandler {
    /**
     * 获得js路径
     * @return js路径
     */
    @Override
    public String[] pathsForFiles() {
        return new String[]{
                "/com/fr/plugin/widget/speedtree/web/speedtree.js"
        };
    }
}