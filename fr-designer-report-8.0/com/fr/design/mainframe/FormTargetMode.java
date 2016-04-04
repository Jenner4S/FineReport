// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.designer.properties.WidgetPropertyTable;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.core.WidgetOptionFactory;
import com.fr.form.main.Form;
import com.fr.form.parameter.FormSubmitButton;
import com.fr.form.ui.container.WLayout;
import com.fr.general.Inter;
import java.awt.Dimension;
import java.util.ArrayList;

// Referenced classes of package com.fr.design.mainframe:
//            FormDesignerModeForSpecial, FormDesigner

public class FormTargetMode extends FormDesignerModeForSpecial
{

    public FormTargetMode(FormDesigner formdesigner)
    {
        super(formdesigner);
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
        return WidgetPropertyTable.getCreatorPropertyGroup((FormDesigner)getTarget(), ((FormDesigner)getTarget()).getRootComponent());
    }

    public boolean isFormParameterEditor()
    {
        return false;
    }

    public int getMinDesignHeight()
    {
        return ((Form)((FormDesigner)getTarget()).getTarget()).getContainer().getMinDesignSize().height;
    }

    public int getMinDesignWidth()
    {
        return ((Form)((FormDesigner)getTarget()).getTarget()).getContainer().getMinDesignSize().width;
    }
}
