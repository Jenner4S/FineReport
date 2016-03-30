package com.fr.plugin.widget.clock;

import com.fr.stable.fun.JavaScriptFileHandler;

/**
 * @author richie
 * @date 2015-03-23
 * @since 8.0
 * ���Ҫʹ��canvasʵ�ֵ�ʱ�ӣ���ʹ��widget.clock.js����ļ�
 * ���Ҫʹ��css3ʵ�ֵ�ʱ�ӣ���ʹ��widget.otherclock.js����ļ�
 */
public class JavaScriptFile implements JavaScriptFileHandler {
    @Override
    public String[] pathsForFiles() {
        return new String[]{
                //"/com/fr/plugin/widget/clock/widget.clock.js",
                "/com/fr/plugin/widget/clock/widget.otherclock.js"
        };
    }

	@Override
	public String encode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int currentAPILevel() {
		// TODO Auto-generated method stub
		return 0;
	}
}
