package com.fr.plugin.widget.radio;

import com.fr.stable.fun.impl.AbstractJavaScriptFileHandler;

/**
 * @author focus
 * @date Jun 17, 2015
 * @since 8.0
 */
public class JavaScriptFile extends AbstractJavaScriptFileHandler {

	/**
	 * js·��
	 * @return String js·��
	 */
    @Override
    public String[] pathsForFiles() {
        return new String[]{
                "/com/fr/plugin/widget/radio/web/estate.js"
        };
    }
}
