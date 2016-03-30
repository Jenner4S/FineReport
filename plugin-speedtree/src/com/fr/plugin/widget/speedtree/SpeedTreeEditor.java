package com.fr.plugin.widget.speedtree;

import com.fr.form.ui.TreeComboBoxEditor;
import com.fr.json.JSONArray;
import com.fr.plugin.ExtraClassManager;
import com.fr.script.Calculator;
import com.fr.stable.fun.FunctionProcessor;
import com.fr.web.core.SessionIDInfor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by zack on 2015/12/15.
 */
public class SpeedTreeEditor extends TreeComboBoxEditor {
    public SpeedTreeEditor() {
        super();
    }

    public String getXType() {
        return "speedtree";
    }

    @Override
    public JSONArray createJSONData(SessionIDInfor sessionIDInfor, Calculator c, HttpServletRequest req) throws Exception {
        FunctionProcessor processor = ExtraClassManager.getInstance().getFunctionProcessor();
        if (processor != null){
            processor.recordFunction(SpeedTreeFunctionProcessor.getInstance());
        }
        return super.createJSONData(sessionIDInfor, c, req);
    }

    //速度优先的下拉树直接根据层级来判断是否有子节点。ztree也是这样的。如果子节点没有数据只是样子有点难看而已，后台数据应该还是一样的
    @Override
    protected boolean hasChildrenForLayerBuild(Calculator c, int treeLayer, Map layerMap) {
        return this.treeNodeAttr.length != treeLayer + 1;
    }
}