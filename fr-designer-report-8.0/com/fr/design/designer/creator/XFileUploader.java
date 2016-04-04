// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.FileEditor;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.Dimension;
import java.beans.IntrospectionException;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.designer.creator:
//            XFieldEditor, CRPropertyDescriptor

public class XFileUploader extends XFieldEditor
{

    public XFileUploader(FileEditor fileeditor, Dimension dimension)
    {
        super(fileeditor, dimension);
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.addAll(super.supportedDescriptor(), new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("allowTypes", data.getClass())).setI18NName(Inter.getLocText("File-Allow_Upload_Files")).putKeyValue("category", "Advanced")
        });
    }

    protected JComponent initEditor()
    {
        if(editor == null)
        {
            editor = FRGUIPaneFactory.createBorderLayout_S_Pane();
            UITextField uitextfield = new UITextField(10);
            editor.add(uitextfield, "Center");
            UIButton uibutton = new UIButton("...");
            uibutton.setPreferredSize(new Dimension(24, 24));
            editor.add(uibutton, "East");
        }
        return editor;
    }

    protected String getIconName()
    {
        return "file_up.png";
    }
}
