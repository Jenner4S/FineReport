package com.bit.plugin.combox;

import com.fr.stable.fun.impl.AbstractLocaleFinder;

/**
 * Created by zack on 2015/9/16.
 */
public class LocaleFinder extends AbstractLocaleFinder {
    /**
     * 国际化文件路径
     * @return 路径
     */
    public String find() {
        return "com/bit/plugin/combox/local/mycombox";
    }
}