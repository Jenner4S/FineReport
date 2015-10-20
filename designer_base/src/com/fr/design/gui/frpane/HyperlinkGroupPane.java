package com.fr.design.gui.frpane;

import com.fr.design.actions.HyperlinkPluginAction;
import com.fr.design.actions.UpdateAction;
import com.fr.general.NameObject;
import com.fr.design.gui.controlpane.JControlPane;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.module.DesignModuleFactory;
import com.fr.general.Inter;
import com.fr.js.JavaScript;
import com.fr.js.NameJavaScript;
import com.fr.js.NameJavaScriptGroup;
import com.fr.plugin.PluginManager;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Nameable;

import java.util.ArrayList;

/**
 * �������� ����.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2013-6-25 ����11:17:57
 */
public class HyperlinkGroupPane extends JControlPane {

    /**
     * ������Ӱ�ť��NameableCreator
     *
     * @return ����Nameable��ť����.
     */
    public NameableCreator[] createNameableCreators() {
        NameableCreator[] creators = DesignModuleFactory.getHyperlinkGroupType().getHyperlinkCreators();
        PluginManager.getInstance().setExtensionPoint(HyperlinkPluginAction.XML_TAG);
        ArrayList<UpdateAction> templateArrayLisy = PluginManager.getInstance().getResultList();
        if (templateArrayLisy.isEmpty()) {
            return creators;
        }
        NameableCreator[] pluginCreators = new NameableCreator[templateArrayLisy.size()];
        for (int i = 0; i < templateArrayLisy.size(); i++) {
            pluginCreators[i] = ((HyperlinkPluginAction) templateArrayLisy.get(i)).getHyperlinkCreator();
        }
        return (NameableCreator[]) ArrayUtils.addAll(creators, pluginCreators);
    }

    /**
     * �����б�ı���.
     *
     * @return ���ر����ַ���.
     */
    public String title4PopupWindow() {
        return Inter.getLocText("Hyperlink");
    }

    public void populate(NameJavaScriptGroup nameHyperlink_array) {
        java.util.List<NameObject> list = new ArrayList<NameObject>();
        if (nameHyperlink_array != null) {
            for (int i = 0; i < nameHyperlink_array.size(); i++) {
                list.add(new NameObject(nameHyperlink_array.getNameHyperlink(i).getName(), nameHyperlink_array.getNameHyperlink(i).getJavaScript()));
            }
        }

        this.populate(list.toArray(new NameObject[list.size()]));
    }

    /**
     * updateJs��Group
     *
     * @return ����NameJavaScriptGroup
     */
    public NameJavaScriptGroup updateJSGroup() {
        Nameable[] res = this.update();
        NameJavaScript[] res_array = new NameJavaScript[res.length];
        for (int i = 0; i < res.length; i++) {
            NameObject no = (NameObject) res[i];
            res_array[i] = new NameJavaScript(no.getName(), (JavaScript) no.getObject());
        }

        return new NameJavaScriptGroup(res_array);
    }
}
