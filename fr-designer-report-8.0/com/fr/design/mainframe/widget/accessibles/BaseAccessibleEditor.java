// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.accessibles;

import com.fr.base.BaseUtils;
import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.widget.editors.ITextComponent;
import com.fr.design.mainframe.widget.editors.TextField;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Referenced classes of package com.fr.design.mainframe.widget.accessibles:
//            AccessibleEditor

public class BaseAccessibleEditor extends BasicPane
    implements AccessibleEditor
{

    private ArrayList listeners;
    private boolean showButton;
    protected Encoder encoder;
    private Decoder decoder;
    private UIButton btPopup;
    protected ITextComponent txtValue;

    public BaseAccessibleEditor(Encoder encoder1, Decoder decoder1, boolean flag)
    {
        listeners = new ArrayList();
        showButton = flag;
        encoder = encoder1;
        decoder = decoder1;
        initComponents();
        txtValue.setEditable(decoder1 != null);
        ((JComponent)txtValue).setBorder(null);
    }

    public void requestFocus()
    {
        super.requestFocus();
        if(decoder != null)
            ((JComponent)txtValue).requestFocus();
        else
        if(showButton)
            btPopup.requestFocus();
    }

    protected ITextComponent createTextField()
    {
        return new TextField();
    }

    private void initComponents()
    {
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 1));
        txtValue = createTextField();
        setLayout(FRGUIPaneFactory.createBorderLayout());
        txtValue.addActionListener(new ActionListener() {

            final BaseAccessibleEditor this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                txtValueActionPerformed(actionevent);
            }

            
            {
                this$0 = BaseAccessibleEditor.this;
                super();
            }
        }
);
        add((JComponent)txtValue, "Center");
        setOpaque(false);
        if(showButton)
        {
            btPopup = new UIButton();
            initPopupButton();
            btPopup.addActionListener(new ActionListener() {

                final BaseAccessibleEditor this$0;

                public void actionPerformed(ActionEvent actionevent)
                {
                    showEditorPane();
                }

            
            {
                this$0 = BaseAccessibleEditor.this;
                super();
            }
            }
);
            add(btPopup, "East");
        }
    }

    protected String title4PopupWindow()
    {
        return "Base";
    }

    protected void showEditorPane()
    {
    }

    protected void initPopupButton()
    {
        if(!isComboButton())
        {
            btPopup.setText("...");
            btPopup.setPreferredSize(new Dimension(20, 20));
        } else
        {
            btPopup.setRolloverEnabled(true);
            btPopup.setFocusPainted(false);
            btPopup.setPreferredSize(new Dimension(15, 19));
            btPopup.setBorderPainted(false);
            btPopup.setContentAreaFilled(false);
            btPopup.setMargin(new Insets(0, 0, 0, 0));
            btPopup.setIcon(BaseUtils.readIcon("/com/fr/design/images/form/designer/drop_up.png"));
            btPopup.setPressedIcon(BaseUtils.readIcon("/com/fr/design/images/form/designer/drop_down.png"));
            btPopup.setRolloverIcon(BaseUtils.readIcon("/com/fr/design/images/form/designer/drop_over.png"));
        }
    }

    protected boolean isComboButton()
    {
        return false;
    }

    private void txtValueActionPerformed(ActionEvent actionevent)
    {
        try
        {
            validateValue();
            fireStateChanged();
        }
        catch(ValidationException validationexception)
        {
            showMessage(validationexception.getMessage(), this);
            txtValue.selectAll();
            ((JComponent)txtValue).requestFocus();
        }
    }

    public Component getEditor()
    {
        return this;
    }

    public Object getValue()
    {
        return decoder.decode(txtValue.getText());
    }

    public void setValue(Object obj)
    {
        if(encoder != null)
            txtValue.setText(encoder.encode(obj));
        txtValue.setValue(obj);
    }

    public void addChangeListener(ChangeListener changelistener)
    {
        if(!listeners.contains(changelistener))
            listeners.add(changelistener);
    }

    public void removeChangeListener(ChangeListener changelistener)
    {
        if(listeners.contains(changelistener))
            listeners.remove(changelistener);
    }

    protected void fireStateChanged()
    {
        ChangeEvent changeevent = new ChangeEvent(this);
        ChangeListener changelistener;
        for(Iterator iterator = listeners.iterator(); iterator.hasNext(); changelistener.stateChanged(changeevent))
            changelistener = (ChangeListener)iterator.next();

    }

    public Encoder getEncoder()
    {
        return encoder;
    }

    public void setEncoder(Encoder encoder1)
    {
        encoder = encoder1;
    }

    public void validateValue()
        throws ValidationException
    {
        if(decoder != null)
            decoder.validate(txtValue.getText());
    }

    public static void showMessage(String s, Component component)
    {
        JOptionPane.showMessageDialog(component, s, "Validation Error", 0);
    }

}
