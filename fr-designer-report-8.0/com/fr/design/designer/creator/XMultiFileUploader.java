// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.MultiFileEditor;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.Dimension;
import java.beans.IntrospectionException;
import javax.swing.JComponent;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.designer.creator:
//            XFieldEditor, CRPropertyDescriptor

public class XMultiFileUploader extends XFieldEditor
{

    public XMultiFileUploader(MultiFileEditor multifileeditor, Dimension dimension)
    {
        super(multifileeditor, dimension);
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.addAll(super.supportedDescriptor(), new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("singleFile", data.getClass())).setI18NName(Inter.getLocText("SINGLE_FILE_UPLOAD")).putKeyValue("category", "Advanced"), (new CRPropertyDescriptor("accept", data.getClass())).setI18NName(Inter.getLocText("File-Allow_Upload_Files")).putKeyValue("category", "Advanced"), (new CRPropertyDescriptor("maxSize", data.getClass())).setI18NName(Inter.getLocText("File-File_Size_Limit")).putKeyValue("category", "Advanced")
        });
    }

    protected JComponent initEditor()
    {
        if(editor == null)
        {
            editor = FRGUIPaneFactory.createBorderLayout_S_Pane();
            JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
            jpanel.add(new UITextField(10), "Center");
            UIButton uibutton = new UIButton("...");
            uibutton.setPreferredSize(new Dimension(24, 24));
            jpanel.add(uibutton, "East");
            editor.add(jpanel, "North");
            JPanel jpanel1 = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
            editor.add(jpanel1, "Center");
            JPanel jpanel2 = FRGUIPaneFactory.createBorderLayout_S_Pane();
            jpanel1.add(jpanel2);
            UILabel uilabel = new UILabel(BaseUtils.readIcon("com/fr/web/images/form/resources/files_up_delete_16.png"));
            jpanel2.add(uilabel, "West");
            jpanel2.add(new UILabel("file1.png"), "Center");
            JPanel jpanel3 = FRGUIPaneFactory.createBorderLayout_S_Pane();
            jpanel1.add(jpanel3);
            UILabel uilabel1 = new UILabel(BaseUtils.readIcon("com/fr/web/images/form/resources/files_up_delete_16.png"));
            jpanel3.add(uilabel1, "West");
            jpanel3.add(new UILabel("file2.xml"), "Center");
        }
        return editor;
    }

    public Dimension initEditorSize()
    {
        return MIDDLE_PREFERRED_SIZE;
    }

    public boolean canEnterIntoParaPane()
    {
        return false;
    }

    protected String getIconName()
    {
        return "files_up.png";
    }
}
