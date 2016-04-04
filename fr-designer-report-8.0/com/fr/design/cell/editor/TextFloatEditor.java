// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.editor;

import com.fr.base.Style;
import com.fr.base.Utils;
import com.fr.design.gui.itextfield.EditTextField;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.grid.Grid;
import com.fr.quickeditor.floatquick.FloatStringQuickEditor;
import com.fr.report.cell.FloatElement;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.BorderFactory;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

// Referenced classes of package com.fr.design.cell.editor:
//            AbstractFloatEditor

public class TextFloatEditor extends AbstractFloatEditor
{

    private Grid grid;
    private FloatElement floatElement;
    protected EditTextField textField;
    protected String oldValue;
    private KeyListener textKeyListener;

    public Grid getGrid()
    {
        return grid;
    }

    public FloatElement getFloatElement()
    {
        return floatElement;
    }

    public TextFloatEditor()
    {
        oldValue = "";
        textKeyListener = new KeyListener() {

            final TextFloatEditor this$0;

            public void keyTyped(KeyEvent keyevent)
            {
                ajustTextFieldSize();
            }

            public void keyPressed(KeyEvent keyevent)
            {
                int i = keyevent.getKeyCode();
                if(i == 27)
                {
                    textField.setText(oldValue);
                    keyevent.consume();
                } else
                if(i == 10 || i == 9)
                {
                    grid.requestFocus();
                    grid.dispatchEvent(keyevent);
                }
            }

            public void keyReleased(KeyEvent keyevent)
            {
            }

            
            {
                this$0 = TextFloatEditor.this;
                super();
            }
        }
;
        textField = new EditTextField();
        textField.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
        textField.addKeyListener(textKeyListener);
        textField.addFocusListener(new FocusAdapter() {

            final TextFloatEditor this$0;

            public void focusLost(FocusEvent focusevent)
            {
                stopFloatEditing();
            }

            
            {
                this$0 = TextFloatEditor.this;
                super();
            }
        }
);
        textField.getDocument().addDocumentListener(new DocumentListener() {

            final TextFloatEditor this$0;

            public void removeUpdate(DocumentEvent documentevent)
            {
                textchanged();
            }

            public void insertUpdate(DocumentEvent documentevent)
            {
                textchanged();
            }

            public void changedUpdate(DocumentEvent documentevent)
            {
                textchanged();
            }

            
            {
                this$0 = TextFloatEditor.this;
                super();
            }
        }
);
        textField.setFocusTraversalKeysEnabled(false);
    }

    private void textchanged()
    {
        FloatStringQuickEditor floatstringquickeditor = (FloatStringQuickEditor)grid.getElementCasePane().getCurrentEditor();
        floatstringquickeditor.showText(textField.getText());
    }

    public int getMaxLength()
    {
        return textField.getMaxLength();
    }

    public void setMaxLength(int i)
    {
        textField.setMaxLength(i);
    }

    public Object getFloatEditorValue()
        throws Exception
    {
        return textField.getText();
    }

    public Component getFloatEditorComponent(Grid grid1, FloatElement floatelement, int i)
    {
        grid = grid1;
        floatElement = floatelement;
        Object obj = floatelement.getValue();
        if(obj == null)
            obj = "";
        if(!(obj instanceof String) && !(obj instanceof Number))
            obj = "";
        oldValue = Utils.objectToString(obj);
        textField.setText(oldValue);
        Style style = floatelement.getStyle();
        ajustTextStyle(grid1, style, obj, i);
        return textField;
    }

    protected void ajustTextStyle(Grid grid1, Style style, Object obj, int i)
    {
        GUICoreUtils.adjustStyle(style, textField, i, obj);
    }

    private void ajustTextFieldSize()
    {
        Dimension dimension = textField.getSize();
        Dimension dimension1 = textField.getPreferredSize();
        textField.setSize((int)Math.max(dimension.getWidth(), dimension1.getWidth()), (int)dimension.getHeight());
    }



}
