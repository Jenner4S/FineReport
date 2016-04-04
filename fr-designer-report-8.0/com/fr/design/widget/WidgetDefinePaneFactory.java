// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget;

import com.fr.base.FRContext;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.widget.ui.ButtonDefinePane;
import com.fr.design.widget.ui.CheckBoxDefinePane;
import com.fr.design.widget.ui.CheckBoxGroupDefinePane;
import com.fr.design.widget.ui.ComboBoxDefinePane;
import com.fr.design.widget.ui.ComboCheckBoxDefinePane;
import com.fr.design.widget.ui.DateEditorDefinePane;
import com.fr.design.widget.ui.IframeEditorDefinePane;
import com.fr.design.widget.ui.ListEditorDefinePane;
import com.fr.design.widget.ui.MultiFileEditorPane;
import com.fr.design.widget.ui.NoneWidgetDefinePane;
import com.fr.design.widget.ui.NumberEditorDefinePane;
import com.fr.design.widget.ui.PasswordDefinePane;
import com.fr.design.widget.ui.RadioDefinePane;
import com.fr.design.widget.ui.RadioGroupDefinePane;
import com.fr.design.widget.ui.TextAreaDefinePane;
import com.fr.design.widget.ui.TextFieldEditorDefinePane;
import com.fr.design.widget.ui.TreeComboBoxEditorDefinePane;
import com.fr.design.widget.ui.TreeEditorDefinePane;
import com.fr.design.widget.ui.UserEditorDefinePane;
import com.fr.form.ui.*;
import com.fr.general.FRLogger;
import com.fr.report.web.button.form.TreeNodeToggleButton;
import com.fr.report.web.button.write.AppendRowButton;
import com.fr.report.web.button.write.DeleteRowButton;
import com.fr.stable.bridge.StableFactory;
import java.util.HashMap;
import java.util.Map;

// Referenced classes of package com.fr.design.widget:
//            Appearance, DataModify, Operator

public class WidgetDefinePaneFactory
{
    public static class RN
    {

        private DataModify definePane;
        private String cardName;

        public DataModify getDefinePane()
        {
            return definePane;
        }

        public String getCardName()
        {
            return cardName;
        }

        public RN(DataModify datamodify, String s)
        {
            definePane = datamodify;
            cardName = s;
        }
    }


    private static Map defineMap;

    private WidgetDefinePaneFactory()
    {
    }

    public static RN createWidgetDefinePane(Widget widget, Operator operator)
    {
        Appearance appearance = (Appearance)defineMap.get(widget.getClass());
        DataModify datamodify = null;
        try
        {
            datamodify = (DataModify)appearance.getDefineClass().newInstance();
            datamodify.populateBean(widget);
            operator.did(datamodify.dataUI(), appearance.getDisplayName());
        }
        catch(Exception exception)
        {
            FRContext.getLogger().error(exception.getMessage(), exception);
        }
        return new RN(datamodify, appearance.getDisplayName());
    }

    static 
    {
        defineMap = new HashMap();
        defineMap.put(com/fr/form/ui/NumberEditor, new Appearance(com/fr/design/widget/ui/NumberEditorDefinePane, "2"));
        defineMap.put(com/fr/form/ui/DateEditor, new Appearance(com/fr/design/widget/ui/DateEditorDefinePane, "3"));
        defineMap.put(com/fr/form/ui/ComboCheckBox, new Appearance(com/fr/design/widget/ui/ComboCheckBoxDefinePane, "5"));
        defineMap.put(com/fr/form/ui/Radio, new Appearance(com/fr/design/widget/ui/RadioDefinePane, "9"));
        defineMap.put(com/fr/form/ui/CheckBox, new Appearance(com/fr/design/widget/ui/CheckBoxDefinePane, "11"));
        defineMap.put(com/fr/form/ui/TreeComboBoxEditor, new Appearance(com/fr/design/widget/ui/TreeComboBoxEditorDefinePane, "7"));
        defineMap.put(com/fr/form/ui/TreeEditor, new Appearance(com/fr/design/widget/ui/TreeEditorDefinePane, "6"));
        defineMap.put(com/fr/form/ui/MultiFileEditor, new Appearance(com/fr/design/widget/ui/MultiFileEditorPane, "19"));
        defineMap.put(com/fr/form/ui/TextArea, new Appearance(com/fr/design/widget/ui/TextAreaDefinePane, "15"));
        defineMap.put(com/fr/form/ui/Password, new Appearance(com/fr/design/widget/ui/PasswordDefinePane, "16"));
        defineMap.put(com/fr/form/ui/IframeEditor, new Appearance(com/fr/design/widget/ui/IframeEditorDefinePane, "18"));
        defineMap.put(com/fr/form/ui/TextEditor, new Appearance(com/fr/design/widget/ui/TextFieldEditorDefinePane, "0"));
        defineMap.put(com/fr/form/ui/NameWidget, new Appearance(com/fr/design/widget/ui/UserEditorDefinePane, "UserDefine"));
        defineMap.put(com/fr/form/ui/ComboCheckBox, new Appearance(com/fr/design/widget/ui/ComboCheckBoxDefinePane, "5"));
        defineMap.put(com/fr/form/ui/ListEditor, new Appearance(com/fr/design/widget/ui/ListEditorDefinePane, "8"));
        defineMap.put(com/fr/form/ui/ComboBox, new Appearance(com/fr/design/widget/ui/ComboBoxDefinePane, "1"));
        defineMap.put(com/fr/form/ui/RadioGroup, new Appearance(com/fr/design/widget/ui/RadioGroupDefinePane, "10"));
        defineMap.put(com/fr/form/ui/CheckBoxGroup, new Appearance(com/fr/design/widget/ui/CheckBoxGroupDefinePane, "12"));
        defineMap.put(com/fr/form/ui/NoneWidget, new Appearance(com/fr/design/widget/ui/NoneWidgetDefinePane, "-1"));
        defineMap.put(com/fr/form/ui/Button, new Appearance(com/fr/design/widget/ui/ButtonDefinePane, "14"));
        defineMap.put(com/fr/form/ui/FreeButton, new Appearance(com/fr/design/widget/ui/ButtonDefinePane, "14"));
        if(StableFactory.getMarkedClass("SubmitButton", com/fr/form/ui/Widget) != null)
            defineMap.put(StableFactory.getMarkedClass("SubmitButton", com/fr/form/ui/Widget), new Appearance(com/fr/design/widget/ui/ButtonDefinePane, "14"));
        defineMap.put(com/fr/report/web/button/write/AppendRowButton, new Appearance(com/fr/design/widget/ui/ButtonDefinePane, "14"));
        defineMap.put(com/fr/report/web/button/write/DeleteRowButton, new Appearance(com/fr/design/widget/ui/ButtonDefinePane, "14"));
        defineMap.put(com/fr/report/web/button/form/TreeNodeToggleButton, new Appearance(com/fr/design/widget/ui/ButtonDefinePane, "14"));
        defineMap.putAll(ExtraDesignClassManager.getInstance().getCellWidgetOptionsMap());
    }
}
