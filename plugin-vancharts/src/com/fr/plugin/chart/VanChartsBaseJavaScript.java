package com.fr.plugin.chart;

import com.fr.stable.fun.impl.AbstractJavaScriptFileHandler;

/**
 * Created by eason on 15/8/26.
 */
public class VanChartsBaseJavaScript extends AbstractJavaScriptFileHandler{
    /**
     * �ļ���·��
     * @return ·��
     */
    public String[] pathsForFiles(){
        return new String[]{
                "/com/fr/web/core/js/es5-sham.js",
                "/com/fr/web/core/js/raphael.js"
        };
    }

}
