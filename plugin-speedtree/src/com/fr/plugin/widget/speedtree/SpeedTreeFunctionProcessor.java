package com.fr.plugin.widget.speedtree;

import com.fr.general.Inter;
import com.fr.stable.fun.FunctionHelper;
import com.fr.stable.fun.impl.AbstractFunctionProcessor;

public class SpeedTreeFunctionProcessor extends AbstractFunctionProcessor {

    private static SpeedTreeFunctionProcessor instance = new SpeedTreeFunctionProcessor();

    public static SpeedTreeFunctionProcessor getInstance() {
        return instance;
    }

    public int getId() {
        return FunctionHelper.generateFunctionID("com.fr.plugin.widget.speedtree");
    }

    @Override
    public String toString() {
        return Inter.getLocText("FR-Widget-Plugin-SpeedTreeEditor_Name");
    }
}