package com.fr.plugin.widget.radio;

import com.fr.stable.fun.impl.AbstractCssFileHandler;

/**
 * @author focus
 * @date 2015-06-16
 * @since 8.0
 */
public class CssFile extends AbstractCssFileHandler {

	/**
	 * css·��
	 * @return String[] css·��
	 */
    @Override
    public String[] pathsForFiles() {
        return new String[]{
                "/com/fr/plugin/widget/radio/web/estate.css",
        };
    }

}
