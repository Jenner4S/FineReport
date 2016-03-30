package com.fr.plugin.widget.scancodewidget;

import com.fr.design.fun.FormWidgetOptionProvider;
import com.fr.design.fun.impl.AbstractParameterWidgetOptionProvider;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;
import com.fr.stable.fun.FunctionHelper;
import com.fr.stable.fun.FunctionProcessor;
import com.fr.stable.fun.impl.AbstractFunctionProcessor;

/**
 * Created by joyxu on 2016/3/7.
 */
public class ScanImpl extends AbstractParameterWidgetOptionProvider implements FormWidgetOptionProvider{

    public static final FunctionProcessor ONEFUNCTION = new AbstractFunctionProcessor(){
        //插件的id，传入pluginID，如com.fr.plugin.MultiLevelReport
        @Override
        public int getId(){

            String pluginID = "com.fr.plugin.widget.scancodewidget.Scan";
            return FunctionHelper.generateFunctionID(pluginID);
        }
        //插件的名字
        @Override
        public String toString(){
            return Inter.getLocText("FR-Designer-Scan_Scan_Code");
        }
    };



    @Override
    public boolean isContainer() {

        return false;
    }


    @Override
    public Class<? extends Widget> classForWidget() {
        return Scan.class;
    }

    @Override
    public Class<?> appearanceForWidget() {
        return XScan.class;
    }

    @Override
    public String iconPathForWidget() {
        return "/com/fr/plugin/widget/scancodewidget/images/scancodeIcon.png";
    }

    @Override
    public String nameForWidget() {
        return  Inter.getLocText("FR-Designer-Scan_Scan_Code");
    }

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }
}
