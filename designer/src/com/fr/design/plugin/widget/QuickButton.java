package com.fr.design.plugin.widget;

import com.fr.form.ui.Button;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.script.Calculator;
import com.fr.stable.core.NodeVisitor;
import com.fr.stable.web.Repository;

/**
 * @author richie
 * @date 2015-01-29
 * @since 8.0
 */
public class QuickButton extends Button {

    public String getXType() {
        return "quickbutton";
    }

    /**
     * 以JSONObject方式返回button的配置信息
     *
     * @param repo ： see 请求
     *             @param nodeVisitor 节点访问
     * @param c ： 指定解析器
     *          @return 配置
     */
    public JSONObject createJSONConfig(Repository repo, Calculator c, NodeVisitor nodeVisitor) throws JSONException {
        JSONObject jo = super.createJSONConfig(repo, c, nodeVisitor);
        jo.put("style", "blue");
        return jo;
    }
}
