// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.form.parameter;

import com.fr.base.BaseUtils;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.properties.WidgetPropertyTable;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.core.WidgetOptionFactory;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormDesignerModeForSpecial;
import com.fr.form.main.parameter.FormParameterUI;
import com.fr.form.parameter.FormSubmitButton;
import com.fr.general.Inter;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Map;

// Referenced classes of package com.fr.design.form.parameter:
//            FormParaDesigner, XFormSubmit

public class FormParaTargetMode extends FormDesignerModeForSpecial
{

    public FormParaTargetMode(FormParaDesigner formparadesigner)
    {
        super(formparadesigner);
    }

    public WidgetOption[] getPredefinedWidgetOptions()
    {
        return (new WidgetOption[] {
            WidgetOptionFactory.createByWidgetClass(Inter.getLocText(new String[] {
                "Query", "Form-Button"
            }), BaseUtils.readIcon("/com/fr/web/images/form/resources/preview_16.png"), com/fr/form/parameter/FormSubmitButton)
        });
    }

    public ArrayList createRootDesignerPropertyGroup()
    {
        return WidgetPropertyTable.getCreatorPropertyGroup((FormDesigner)getTarget(), ((FormParaDesigner)getTarget()).getRootComponent());
    }

    public boolean isFormParameterEditor()
    {
        return true;
    }

    public int getMinDesignHeight()
    {
        return ((FormParameterUI)((FormParaDesigner)getTarget()).getTarget()).getDesignSize().height + 20;
    }

    public int getMinDesignWidth()
    {
        return ((FormParameterUI)((FormParaDesigner)getTarget()).getTarget()).getDesignSize().width + 20;
    }

    static 
    {
        XCreatorUtils.objectMap.put(com/fr/form/parameter/FormSubmitButton, com/fr/design/form/parameter/XFormSubmit);
    }
}
