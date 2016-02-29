package com.fr.plugin.designer;

import com.fr.stable.fun.impl.AbstractLocaleFinder;

public class ActualLocaleFinder extends AbstractLocaleFinder {

    @Override
    /**
     * 返回路径
     * @return 同上
     */
    public String find() {
        return "com/fr/plugin/designer/resource/locale/search";
    }
}