// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.grid;

import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.elementcase.ElementCase;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JScrollBar;

// Referenced classes of package com.fr.grid:
//            Grid, KeyEventWork

public class GridKeyListener
    implements KeyListener
{

    private Grid grid;
    private long keyPressedLastTime;
    private boolean isKeyPressedContentChanged;

    public GridKeyListener(Grid grid1)
    {
        keyPressedLastTime = 0L;
        isKeyPressedContentChanged = false;
        grid = grid1;
    }

    public void keyPressed(KeyEvent keyevent)
    {
        if(!grid.isEnabled() || keyevent.isConsumed())
            return;
        KeyEvent keyevent1 = KeyEventWork.processKeyEvent(keyevent);
        if(keyevent1 == null)
            return;
        long l = System.currentTimeMillis();
        int i = keyevent.getKeyCode();
        boolean flag = false;
        ElementCasePane elementcasepane = grid.getElementCasePane();
        com.fr.report.elementcase.TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        if(elementcasepane.getSelection() instanceof FloatSelection)
        {
            if(l - keyPressedLastTime <= 2L)
                return;
            keyPressedLastTime = l;
            dealWithFloatSelection(elementcasepane, i);
        } else
        {
            if(l - keyPressedLastTime <= 32L)
                return;
            keyPressedLastTime = l;
            dealWithCellSelection(keyevent, i);
        }
        switch(i)
        {
        case 33: // '!'
            elementcasepane.getVerticalScrollBar().setValue(Math.max(0, grid.getVerticalValue() - grid.getVerticalExtent()));
            flag = true;
            break;

        case 34: // '"'
            elementcasepane.getVerticalScrollBar().setValue(grid.getVerticalValue() + grid.getVerticalExtent());
            flag = true;
            break;

        case 65: // 'A'
            if(i == 65 && keyevent.isControlDown())
            {
                elementcasepane.setSelection(new CellSelection(0, 0, templateelementcase.getColumnCount(), templateelementcase.getRowCount()));
                flag = true;
            }
            flag = true;
            break;
        }
        if(flag)
            elementcasepane.repaint();
    }

    private void dealWithFloatSelection(ElementCasePane elementcasepane, int i)
    {
        boolean flag = false;
        FloatSelection floatselection = (FloatSelection)elementcasepane.getSelection();
        switch(i)
        {
        case 37: // '%'
            floatselection.moveLeft(elementcasepane);
            flag = true;
            break;

        case 39: // '\''
            floatselection.moveRight(elementcasepane);
            flag = true;
            break;

        case 38: // '&'
            floatselection.moveUp(elementcasepane);
            flag = true;
            break;

        case 40: // '('
            floatselection.moveDown(elementcasepane);
            flag = true;
            break;
        }
        if(flag)
        {
            grid.getElementCasePane().repaint();
            isKeyPressedContentChanged = true;
        }
    }

    private void dealWithCellSelection(KeyEvent keyevent, int i)
    {
        switch(i)
        {
        case 27: // '\033'
            if(grid.isCellEditing())
                grid.cancelEditing();
            break;

        case 113: // 'q'
            if(!grid.isCellEditing())
                grid.startEditing();
            break;
        }
        if(IS_NUM_PAD_KEY(i))
            keyTyped(keyevent);
    }

    public void keyReleased(KeyEvent keyevent)
    {
        if(!grid.isEnabled() || keyevent.isConsumed())
            return;
        KeyEvent keyevent1 = KeyEventWork.processKeyEvent(keyevent);
        if(keyevent1 == null)
            return;
        if(isKeyPressedContentChanged)
        {
            grid.getElementCasePane().fireTargetModified();
            isKeyPressedContentChanged = false;
        }
    }

    public void keyTyped(KeyEvent keyevent)
    {
        if(!grid.isEnabled() || keyevent.isConsumed())
            return;
        KeyEvent keyevent1 = KeyEventWork.processKeyEvent(keyevent);
        if(keyevent1 == null || keyevent.isControlDown())
            return;
        char c = keyevent.getKeyChar();
        if(c == '\t')
            return;
        int i = keyevent.getKeyCode();
        if(Character.isDefined(c))
        {
            Selection selection = grid.getElementCasePane().getSelection();
            if(selection instanceof CellSelection)
            {
                if(!grid.getElementCasePane().isSelectedOneCell())
                {
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
                if(!grid.isCellEditing())
                    grid.startEditing(true);
                if(grid.getCellEditor() != null && grid.editorComponent != null)
                    if(IS_NUM_PAD_KEY(i))
                    {
                        KeyEvent keyevent2 = new KeyEvent(grid, 401, 0L, 0, i - 48, c);
                        grid.editorComponent.dispatchEvent(keyevent2);
                        keyevent2.consume();
                    } else
                    if(!keyevent.isConsumed())
                        grid.editorComponent.dispatchEvent(keyevent);
            }
        }
    }

    private static boolean IS_NUM_PAD_KEY(int i)
    {
        return i == 96 || i == 97 || i == 98 || i == 99 || i == 100 || i == 101 || i == 102 || i == 103 || i == 104 || i == 105 || i == 106 || i == 107 || i == 109 || i == 110 || i == 111;
    }
}
