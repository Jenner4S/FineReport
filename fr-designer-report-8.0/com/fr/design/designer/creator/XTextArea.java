// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.base.*;
import com.fr.design.mainframe.widget.editors.RegexEditor;
import com.fr.design.mainframe.widget.editors.WidgetValueEditor;
import com.fr.design.mainframe.widget.renderer.RegexCellRencerer;
import com.fr.form.ui.TextArea;
import com.fr.form.ui.WidgetValue;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.*;
import java.beans.IntrospectionException;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.designer.creator:
//            XFieldEditor, CRPropertyDescriptor

public class XTextArea extends XFieldEditor
{

    public XTextArea(TextArea textarea, Dimension dimension)
    {
        super(textarea, dimension);
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.addAll(super.supportedDescriptor(), new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("widgetValue", data.getClass())).setI18NName(Inter.getLocText(new String[] {
                "Widget", "Value"
            })).setEditorClass(com/fr/design/mainframe/widget/editors/WidgetValueEditor), (new CRPropertyDescriptor("regex", data.getClass())).setI18NName(Inter.getLocText("Input_Rule")).setEditorClass(com/fr/design/mainframe/widget/editors/RegexEditor$RegexEditor4TextArea).putKeyValue("renderer", com/fr/design/mainframe/widget/renderer/RegexCellRencerer).putKeyValue("category", "Advanced"), (new CRPropertyDescriptor("waterMark", data.getClass())).setI18NName(Inter.getLocText("WaterMark")).putKeyValue("category", "Advanced")
        });
    }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        TextArea textarea = (TextArea)data;
        if(textarea.getWidgetValue() != null)
        {
            Graphics2D graphics2d = (Graphics2D)g.create();
            BaseUtils.drawStringStyleInRotation(graphics2d, getWidth(), getHeight(), textarea.getWidgetValue().toString(), Style.getInstance(FRFont.getInstance()).deriveHorizontalAlignment(2).deriveVerticalAlignment(1).deriveTextStyle(0), ScreenResolution.getScreenResolution());
        }
    }

    protected JComponent initEditor()
    {
        setBorder(FIELDBORDER);
        return this;
    }

    public Dimension initEditorSize()
    {
        return BIG_PREFERRED_SIZE;
    }

    protected String getIconName()
    {
        return "text_area_16.png";
    }
}
