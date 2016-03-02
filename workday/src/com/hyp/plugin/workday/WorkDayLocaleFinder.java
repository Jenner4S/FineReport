package com.hyp.plugin.workday;

import com.fr.stable.fun.impl.AbstractLocaleFinder;

/**
 * Created by richie on 16/1/29.
 */
public class WorkDayLocaleFinder extends AbstractLocaleFinder {
    @Override
    public String find() {
        return "com/hyp/plugin/workday/local/workday";
    }
}
