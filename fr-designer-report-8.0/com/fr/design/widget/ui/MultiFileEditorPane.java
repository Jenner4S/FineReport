// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.DictionaryComboBox;
import com.fr.design.gui.icombobox.DictionaryConstants;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.FieldEditor;
import com.fr.form.ui.MultiFileEditor;
import com.fr.general.Inter;
import java.awt.Dimension;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.widget.ui:
//            FieldEditorDefinePane

public class MultiFileEditorPane extends FieldEditorDefinePane
{

    private DictionaryComboBox acceptType;
    private UICheckBox singleFileCheckBox;
    private UINumberField fileSizeField;

    public MultiFileEditorPane()
    {
        initComponents();
    }

    protected String title4PopupWindow()
    {
        return "file";
    }

    protected JPanel setFirstContentPane()
    {
        acceptType = new DictionaryComboBox(DictionaryConstants.acceptTypes, DictionaryConstants.fileTypeDisplays);
        acceptType.setPreferredSize(new Dimension(400, 18));
        singleFileCheckBox = new UICheckBox(Inter.getLocText("SINGLE_FILE_UPLOAD"));
        fileSizeField = new UINumberField();
        fileSizeField.setPreferredSize(new Dimension(80, 18));
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        JPanel jpanel1 = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
        jpanel1.add(singleFileCheckBox);
        jpanel.add(jpanel1, "North");
        JPanel jpanel2 = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
        jpanel2.setLayout(FRGUIPaneFactory.createLabelFlowLayout());
        jpanel2.add(new UILabel((new StringBuilder()).append("   ").append(Inter.getLocText("File-Allow_Upload_Files")).append(":").toString()));
        jpanel2.add(acceptType);
        jpanel.add(jpanel2, "Center");
        JPanel jpanel3 = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
        jpanel3.add(new UILabel((new StringBuilder()).append(" ").append(Inter.getLocText("File-File_Size_Limit")).append(":").toString()));
        jpanel3.add(fileSizeField);
        jpanel3.add(new UILabel(" KB"));
        jpanel2.add(jpanel3);
        return jpanel;
    }

    protected void populateSubFieldEditorBean(MultiFileEditor multifileeditor)
    {
        acceptType.setSelectedItem(multifileeditor.getAccept());
        singleFileCheckBox.setSelected(multifileeditor.isSingleFile());
        fileSizeField.setValue(multifileeditor.getMaxSize());
    }

    protected MultiFileEditor updateSubFieldEditorBean()
    {
        MultiFileEditor multifileeditor = new MultiFileEditor();
        multifileeditor.setAccept((String)acceptType.getSelectedItem());
        multifileeditor.setSingleFile(singleFileCheckBox.isSelected());
        multifileeditor.setMaxSize(fileSizeField.getValue());
        return multifileeditor;
    }

    protected volatile FieldEditor updateSubFieldEditorBean()
    {
        return updateSubFieldEditorBean();
    }

    protected volatile void populateSubFieldEditorBean(FieldEditor fieldeditor)
    {
        populateSubFieldEditorBean((MultiFileEditor)fieldeditor);
    }
}
