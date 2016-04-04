// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.gui.core;

import com.fr.base.BaseUtils;
import com.fr.form.ui.ElementCaseEditor;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.*;
import com.fr.general.Inter;
import javax.swing.Icon;

// Referenced classes of package com.fr.design.gui.core:
//            WidgetOption, WidgetOptionFactory

public class FormWidgetOption extends WidgetOption
{

    public static final WidgetOption ABSOLUTELAYOUTCONTAINER = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Designer_AbsoluteLayout"), BaseUtils.readIcon("/com/fr/web/images/form/resources/layout_absolute.png"), com/fr/form/ui/container/WAbsoluteLayout);
    public static final WidgetOption BORDERLAYOUTCONTAINER = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Designer_BorderLayout"), BaseUtils.readIcon("/com/fr/web/images/form/resources/layout_border.png"), com/fr/form/ui/container/WBorderLayout);
    public static final WidgetOption CARDLAYOUTCONTAINER = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Designer_CardLayout"), BaseUtils.readIcon("/com/fr/web/images/form/resources/card_layout_16.png"), com/fr/form/ui/container/WCardLayout);
    public static final WidgetOption HORIZONTALBOXLAYOUTCONTAINER = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Designer_Layout-HBox"), BaseUtils.readIcon("/com/fr/web/images/form/resources/boxlayout_h_16.png"), com/fr/form/ui/container/WHorizontalBoxLayout);
    public static final WidgetOption VERTICALBOXLAYOUTCONTAINER = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Designer_VerticalBoxLayout"), BaseUtils.readIcon("/com/fr/web/images/form/resources/boxlayout_v_16.png"), com/fr/form/ui/container/WVerticalBoxLayout);
    public static final WidgetOption FITLAYOUTCONTAINER = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Designer-Layout_Adaptive_Layout"), BaseUtils.readIcon("/com/fr/web/images/form/resources/boxlayout_v_16.png"), com/fr/form/ui/container/WFitLayout);
    public static final WidgetOption PARAMETERCONTAINER = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Designer_Para-Body"), BaseUtils.readIcon("/com/fr/web/images/form/resources/layout_parameter.png"), com/fr/form/ui/container/WParameterLayout);
    public static final WidgetOption ELEMENTCASE = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Designer_Form-Report"), BaseUtils.readIcon("/com/fr/web/images/form/resources/report_16.png"), com/fr/form/ui/ElementCaseEditor);

    public FormWidgetOption()
    {
    }

    public String optionName()
    {
        return null;
    }

    public Icon optionIcon()
    {
        return null;
    }

    public Class widgetClass()
    {
        return null;
    }

    public Widget createWidget()
    {
        return null;
    }

    public static WidgetOption[] getFormContainerInstance()
    {
        return (new WidgetOption[] {
            ABSOLUTELAYOUTCONTAINER, BORDERLAYOUTCONTAINER, HORIZONTALBOXLAYOUTCONTAINER, VERTICALBOXLAYOUTCONTAINER, CARDLAYOUTCONTAINER, FITLAYOUTCONTAINER
        });
    }

    public static WidgetOption[] getFormLayoutInstance()
    {
        return (new WidgetOption[] {
            CARDLAYOUTCONTAINER
        });
    }

}
