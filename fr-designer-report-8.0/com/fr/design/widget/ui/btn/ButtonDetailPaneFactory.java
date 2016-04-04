// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui.btn;

import com.fr.base.FRContext;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.widget.btn.ButtonDetailPane;
import com.fr.form.ui.*;
import com.fr.general.FRLogger;
import com.fr.report.web.button.form.TreeNodeToggleButton;
import com.fr.report.web.button.write.AppendRowButton;
import com.fr.report.web.button.write.DeleteRowButton;
import com.fr.stable.bridge.StableFactory;
import java.util.HashMap;
import java.util.Map;

// Referenced classes of package com.fr.design.widget.ui.btn:
//            DefaultButtonDetailPane, FreeButtonDetailPane, TreeNodeToogleButtonDefinePane, AppendRowButtonDefinePane, 
//            DeleteRowButtonDefinePane

public class ButtonDetailPaneFactory
{

    private static Map detailMap;

    public ButtonDetailPaneFactory()
    {
    }

    public static ButtonDetailPane createButtonDetailPane(Button button)
    {
        Class class1 = (Class)detailMap.get(button.getClass().getName());
        ButtonDetailPane buttondetailpane = null;
        if(class1 != null)
            try
            {
                buttondetailpane = (ButtonDetailPane)class1.newInstance();
                buttondetailpane.populate(button);
            }
            catch(Exception exception)
            {
                FRContext.getLogger().error(exception.getMessage(), exception);
            }
        return buttondetailpane;
    }

    public static ButtonDetailPane createButtonDetailPane(Class class1, Button button)
    {
        if(class1 == null)
            return createButtonDetailPane(button);
        Class class2 = (Class)detailMap.get(class1.getName());
        ButtonDetailPane buttondetailpane = null;
        if(class2 != null)
            try
            {
                buttondetailpane = (ButtonDetailPane)class2.newInstance();
                buttondetailpane.populate(button != null ? button : buttondetailpane.createButton());
            }
            catch(Exception exception)
            {
                FRContext.getLogger().error(exception.getMessage(), exception);
            }
        return buttondetailpane;
    }

    static 
    {
        detailMap = new HashMap();
        detailMap.put(com/fr/form/ui/Button.getName(), com/fr/design/widget/ui/btn/DefaultButtonDetailPane);
        detailMap.put(com/fr/form/ui/FreeButton.getName(), com/fr/design/widget/ui/btn/FreeButtonDetailPane);
        if(StableFactory.getMarkedClass("SubmitButton", com/fr/form/ui/Widget) != null)
            detailMap.put(StableFactory.getMarkedClass("SubmitButton", com/fr/form/ui/Widget).getName(), DesignModuleFactory.getButtonDetailPaneClass());
        detailMap.put(com/fr/report/web/button/form/TreeNodeToggleButton.getName(), com/fr/design/widget/ui/btn/TreeNodeToogleButtonDefinePane);
        detailMap.put(com/fr/report/web/button/write/AppendRowButton.getName(), com/fr/design/widget/ui/btn/AppendRowButtonDefinePane);
        detailMap.put(com/fr/report/web/button/write/DeleteRowButton.getName(), com/fr/design/widget/ui/btn/DeleteRowButtonDefinePane);
    }
}
