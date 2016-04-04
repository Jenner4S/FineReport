// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.gui.ibutton.UIPasswordField;
import com.fr.design.mainframe.widget.editors.RegexEditor;
import com.fr.design.mainframe.widget.editors.WidgetValueEditor;
import com.fr.design.mainframe.widget.renderer.RegexCellRencerer;
import com.fr.form.ui.Password;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.Dimension;
import java.beans.IntrospectionException;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.designer.creator:
//            XWrapperedFieldEditor, CRPropertyDescriptor

public class XPassword extends XWrapperedFieldEditor
{

    public XPassword(Password password, Dimension dimension)
    {
        super(password, dimension);
    }

    protected JComponent initEditor()
    {
        if(editor == null)
            editor = new UIPasswordField();
        return editor;
    }

    protected String getIconName()
    {
        return "password_field_16.png";
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.addAll(super.supportedDescriptor(), new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("widgetValue", data.getClass())).setI18NName(Inter.getLocText(new String[] {
                "Widget", "Value"
            })).setEditorClass(com/fr/design/mainframe/widget/editors/WidgetValueEditor), (new CRPropertyDescriptor("regex", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_Input_Rule")).setEditorClass(com/fr/design/mainframe/widget/editors/RegexEditor$RegexEditor4TextArea).putKeyValue("renderer", com/fr/design/mainframe/widget/renderer/RegexCellRencerer).putKeyValue("category", "Advanced"), (new CRPropertyDescriptor("waterMark", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_WaterMark")).putKeyValue("category", "Advanced")
        });
    }
}
