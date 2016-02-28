package com.fr.plugin.chart;

import com.fr.stable.fun.impl.AbstractJavaScriptFileHandler;

/**
 * Created by eason on 15/8/26.
 */
public class VanChartsBaseJavaScript extends AbstractJavaScriptFileHandler{
    /**
     * 文件的路径
     * @return 路径
     */
    public String[] pathsForFiles(){
        return new String[]{
                "/com/fr/web/core/js/es5-sham.js",
                "/com/fr/web/core/js/raphael.js"
        };
    }

}
