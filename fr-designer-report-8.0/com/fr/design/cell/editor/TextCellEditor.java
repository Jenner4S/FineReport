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
import com.fr.quickeditor.cellquick.CellStringQuickEditor;
import com.fr.report.cell.TemplateCellElement;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.BorderFactory;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

// Referenced classes of package com.fr.design.cell.editor:
//            AbstractCellEditor

public class TextCellEditor extends AbstractCellEditor
{

    private Grid grid;
    protected EditTextField textField;
    protected String oldValue;
    DocumentListener documentlistener;
    private KeyListener textKeyListener;

    public Grid getGrid()
    {
        return grid;
    }

    public TextCellEditor()
    {
        oldValue = "";
        documentlistener = new DocumentListener() {

            final TextCellEditor this$0;

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
                this$0 = TextCellEditor.this;
                super();
            }
        }
;
        textKeyListener = new KeyListener() {

            final TextCellEditor this$0;

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
                if(i == 10 || i == 9 || i == 38 || i == 40)
                {
                    grid.requestFocus();
                    grid.dispatchEvent(keyevent);
                }
            }

            public void keyReleased(KeyEvent keyevent)
            {
            }

            
            {
                this$0 = TextCellEditor.this;
                super();
            }
        }
;
        textField = new EditTextField();
        textField.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
        textField.addKeyListener(textKeyListener);
        textField.addFocusListener(new FocusAdapter() {

            final TextCellEditor this$0;

            public void focusLost(FocusEvent focusevent)
            {
                stopCellEditing();
            }

            
            {
                this$0 = TextCellEditor.this;
                super();
            }
        }
);
        textField.getDocument().addDocumentListener(documentlistener);
        textField.setFocusTraversalKeysEnabled(false);
    }

    public int getMaxLength()
    {
        return textField.getMaxLength();
    }

    private void textchanged()
    {
        CellStringQuickEditor cellstringquickeditor = (CellStringQuickEditor)grid.getElementCasePane().getCurrentEditor();
        cellstringquickeditor.showText(textField.getText());
    }

    public void setMaxLength(int i)
    {
        textField.setMaxLength(i);
    }

    public Object getCellEditorValue()
        throws Exception
    {
        return textField.getText();
    }

    public Component getCellEditorComponent(Grid grid1, TemplateCellElement templatecellelement, int i)
    {
        grid = grid1;
        Object obj = null;
        if(templatecellelement != null)
            obj = templatecellelement.getValue();
        if(obj == null)
            obj = "";
        if(!(obj instanceof String) && !(obj instanceof Number))
            obj = "";
        oldValue = Utils.objectToString(obj);
        textField.setText(oldValue);
        Style style = null;
        if(templatecellelement != null)
            style = templatecellelement.getStyle();
        ajustTextStyle(grid1, style, obj, i);
        return textField;
    }

    public String getOldValue()
    {
        return oldValue;
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
