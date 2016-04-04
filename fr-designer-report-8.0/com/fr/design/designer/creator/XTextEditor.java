// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.base.*;
import com.fr.design.mainframe.widget.editors.RegexEditor;
import com.fr.design.mainframe.widget.editors.WidgetValueEditor;
import com.fr.design.mainframe.widget.renderer.RegexCellRencerer;
import com.fr.form.ui.TextEditor;
import com.fr.form.ui.WidgetValue;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.*;
import java.beans.IntrospectionException;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.designer.creator:
//            XWrapperedFieldEditor, CRPropertyDescriptor

public class XTextEditor extends XWrapperedFieldEditor
{

    public XTextEditor(TextEditor texteditor, Dimension dimension)
    {
        super(texteditor, dimension);
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.addAll(super.supportedDescriptor(), new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("widgetValue", data.getClass())).setI18NName(Inter.getLocText(new String[] {
                "FR-Designer_Widget", "Value"
            })).setEditorClass(com/fr/design/mainframe/widget/editors/WidgetValueEditor), (new CRPropertyDescriptor("regex", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_Input_Rule")).setEditorClass(com/fr/design/mainframe/widget/editors/RegexEditor).putKeyValue("renderer", com/fr/design/mainframe/widget/renderer/RegexCellRencerer).putKeyValue("category", "Advanced"), (new CRPropertyDescriptor("waterMark", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_WaterMark")).putKeyValue("category", "Advanced")
        });
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        TextEditor texteditor = (TextEditor)data;
        if(texteditor.getWidgetValue() != null)
        {
            Graphics2D graphics2d = (Graphics2D)g.create();
            BaseUtils.drawStringStyleInRotation(graphics2d, getWidth(), getHeight(), texteditor.getWidgetValue().toString(), Style.getInstance(FRFont.getInstance()).deriveHorizontalAlignment(2).deriveTextStyle(1), ScreenResolution.getScreenResolution());
        }
    }

    protected JComponent initEditor()
    {
        setBorder(FIELDBORDER);
        return this;
    }

    protected String getIconName()
    {
        return "text_field_16.png";
    }
}
