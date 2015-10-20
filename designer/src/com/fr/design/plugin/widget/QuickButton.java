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
     * ��JSONObject��ʽ����button��������Ϣ
     *
     * @param repo �� see ����
     *             @param nodeVisitor �ڵ����
     * @param c �� ָ��������
     *          @return ����
     */
    public JSONObject createJSONConfig(Repository repo, Calculator c, NodeVisitor nodeVisitor) throws JSONException {
        JSONObject jo = super.createJSONConfig(repo, c, nodeVisitor);
        jo.put("style", "blue");
        return jo;
    }
}
