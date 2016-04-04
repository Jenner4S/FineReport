// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.FieldEditor;
import com.fr.general.Inter;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

// Referenced classes of package com.fr.design.widget.ui:
//            AbstractDataModify

public abstract class FieldEditorDefinePane extends AbstractDataModify
{

    private UICheckBox allowBlankCheckBox;
    private UITextField errorMsgTextField;

    public FieldEditorDefinePane()
    {
        initComponents();
    }

    protected void initComponents()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
        add(jpanel, "North");
        JPanel jpanel1 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        jpanel1.setBorder(BorderFactory.createEmptyBorder(0, -2, 0, 0));
        allowBlankCheckBox = new UICheckBox(Inter.getLocText("Allow_Blank"));
        jpanel1.add(allowBlankCheckBox);
        allowBlankCheckBox.addItemListener(new ItemListener() {

            final FieldEditorDefinePane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                errorMsgTextField.setEnabled(!allowBlankCheckBox.isSelected());
            }

            
            {
                this$0 = FieldEditorDefinePane.this;
                super();
            }
        }
);
        JPanel jpanel2 = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        jpanel1.add(jpanel2);
        jpanel.add(jpanel1);
        jpanel2.add(new UILabel((new StringBuilder()).append(Inter.getLocText(new String[] {
            "Error", "Tooltips"
        })).append(":").toString()));
        errorMsgTextField = new UITextField(16);
        jpanel2.add(errorMsgTextField);
        errorMsgTextField.getDocument().addDocumentListener(new DocumentListener() {

            final FieldEditorDefinePane this$0;

            public void changedUpdate(DocumentEvent documentevent)
            {
                errorMsgTextField.setToolTipText(errorMsgTextField.getText());
            }

            public void insertUpdate(DocumentEvent documentevent)
            {
                errorMsgTextField.setToolTipText(errorMsgTextField.getText());
            }

            public void removeUpdate(DocumentEvent documentevent)
            {
                errorMsgTextField.setToolTipText(errorMsgTextField.getText());
            }

            
            {
                this$0 = FieldEditorDefinePane.this;
                super();
            }
        }
);
        JPanel jpanel3 = setFirstContentPane();
        if(jpanel3 != null)
            add(jpanel3, "Center");
    }

    public void populateBean(FieldEditor fieldeditor)
    {
        allowBlankCheckBox.setSelected(fieldeditor.isAllowBlank());
        errorMsgTextField.setEnabled(!allowBlankCheckBox.isSelected());
        errorMsgTextField.setText(fieldeditor.getErrorMessage());
        populateSubFieldEditorBean(fieldeditor);
    }

    protected abstract void populateSubFieldEditorBean(FieldEditor fieldeditor);

    public FieldEditor updateBean()
    {
        FieldEditor fieldeditor = updateSubFieldEditorBean();
        fieldeditor.setAllowBlank(allowBlankCheckBox.isSelected());
        fieldeditor.setErrorMessage(errorMsgTextField.getText());
        return fieldeditor;
    }

    protected abstract FieldEditor updateSubFieldEditorBean();

    protected abstract JPanel setFirstContentPane();

    public void checkValid()
        throws Exception
    {
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((FieldEditor)obj);
    }


}
