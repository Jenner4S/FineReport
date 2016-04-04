// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.base.*;
import com.fr.design.mainframe.widget.editors.*;
import com.fr.form.ui.NumberEditor;
import com.fr.form.ui.WidgetValue;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.*;
import java.beans.IntrospectionException;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.designer.creator:
//            XWrapperedFieldEditor, CRPropertyDescriptor

public class XNumberEditor extends XWrapperedFieldEditor
{

    public XNumberEditor(NumberEditor numbereditor, Dimension dimension)
    {
        super(numbereditor, dimension);
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        CRPropertyDescriptor acrpropertydescriptor[] = super.supportedDescriptor();
        CRPropertyDescriptor crpropertydescriptor = (new CRPropertyDescriptor("allowDecimals", data.getClass())).setEditorClass(com/fr/design/mainframe/widget/editors/InChangeBooleanEditor).setI18NName(Inter.getLocText("FR-Designer_Allow_Decimals"));
        CRPropertyDescriptor crpropertydescriptor1 = (new CRPropertyDescriptor("maxDecimalLength", data.getClass())).setI18NName(Inter.getLocText(new String[] {
            "Double", "Numbers"
        }));
        acrpropertydescriptor = (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.addAll(acrpropertydescriptor, ((NumberEditor)data).isAllowDecimals() ? ((Object []) (new CRPropertyDescriptor[] {
            crpropertydescriptor, crpropertydescriptor1
        })) : ((Object []) (new CRPropertyDescriptor[] {
            crpropertydescriptor
        })));
        return (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.addAll(acrpropertydescriptor, new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("allowNegative", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_Allow_Negative")).setEditorClass(com/fr/design/mainframe/widget/editors/InChangeBooleanEditor), (new CRPropertyDescriptor("minValue", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_Min_Value")).setEditorClass(com/fr/design/mainframe/widget/editors/SpinnerMinNumberEditor), (new CRPropertyDescriptor("maxValue", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_Max_Value")).setEditorClass(com/fr/design/mainframe/widget/editors/SpinnerMaxNumberEditor), (new CRPropertyDescriptor("widgetValue", data.getClass())).setI18NName(Inter.getLocText(new String[] {
                "Widget", "Value"
            })).setEditorClass(com/fr/design/mainframe/widget/editors/WidgetValueEditor), (new CRPropertyDescriptor("waterMark", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_WaterMark")).putKeyValue("category", "Advanced")
        });
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        NumberEditor numbereditor = (NumberEditor)data;
        if(numbereditor.getWidgetValue() != null)
        {
            Graphics2D graphics2d = (Graphics2D)g.create();
            BaseUtils.drawStringStyleInRotation(graphics2d, getWidth(), getHeight(), numbereditor.getWidgetValue().toString(), Style.getInstance(FRFont.getInstance()).deriveHorizontalAlignment(2).deriveTextStyle(1), ScreenResolution.getScreenResolution());
        }
    }

    protected JComponent initEditor()
    {
        setBorder(FIELDBORDER);
        return this;
    }

    protected String getIconName()
    {
        return "number_field_16.png";
    }
}
