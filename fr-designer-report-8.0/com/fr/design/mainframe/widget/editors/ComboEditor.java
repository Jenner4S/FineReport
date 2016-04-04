// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.editors;

import com.fr.design.Exception.ValidationException;
import com.fr.design.gui.icombobox.UIComboBox;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.*;

// Referenced classes of package com.fr.design.mainframe.widget.editors:
//            AbstractPropertyEditor

public class ComboEditor extends AbstractPropertyEditor
{

    protected UIComboBox comboBox;

    public ComboEditor()
    {
        comboBox = new UIComboBox();
        initComboBoxLookAndFeel();
        ComboBoxModel comboboxmodel = model();
        if(comboboxmodel != null)
            comboBox.setModel(comboboxmodel);
        ListCellRenderer listcellrenderer = renderer();
        if(listcellrenderer != null)
            comboBox.setRenderer(listcellrenderer);
    }

    public ComboEditor(Object aobj[])
    {
        this(new UIComboBox(aobj));
    }

    public ComboEditor(Vector vector)
    {
        this(new UIComboBox(vector));
    }

    public ComboEditor(ComboBoxModel comboboxmodel)
    {
        this(new UIComboBox(comboboxmodel));
    }

    public ComboEditor(UIComboBox uicombobox)
    {
        comboBox = uicombobox;
        initComboBoxLookAndFeel();
    }

    private void initComboBoxLookAndFeel()
    {
        comboBox.setEditable(false);
        comboBox.addActionListener(new ActionListener() {

            final ComboEditor this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                firePropertyChanged();
            }

            
            {
                this$0 = ComboEditor.this;
                super();
            }
        }
);
        ((JComponent)comboBox.getEditor().getEditorComponent()).setBorder(null);
        comboBox.setBorder(null);
    }

    public ComboBoxModel model()
    {
        return null;
    }

    public ListCellRenderer renderer()
    {
        return null;
    }

    public void setValue(Object obj)
    {
        comboBox.setSelectedItem(obj);
    }

    public Object getValue()
    {
        return comboBox.getSelectedItem();
    }

    public Component getCustomEditor()
    {
        return comboBox;
    }

    public void validateValue()
        throws ValidationException
    {
    }
}
