// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.grid;

import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.Selection;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import javax.swing.*;

// Referenced classes of package com.fr.grid:
//            Grid

public class GridKeyAction extends AbstractAction
{
    private static abstract class VisibleHelper
    {

        public abstract void ensureVisible(ElementCasePane elementcasepane, Rectangle rectangle);

        private VisibleHelper()
        {
        }

    }

    private static abstract class Helper
    {

        public abstract int getCoordinate(Rectangle rectangle);

        public abstract int getSize(Rectangle rectangle);

        public abstract int getOppoCoordinate(Rectangle rectangle);

        public abstract int getOppoSize(Rectangle rectangle);

        public abstract Rectangle asRect(int i, int j, int k, int l);

        private Helper()
        {
        }

    }


    private Grid grid;
    private String actionKey;
    private boolean isShift;
    private static final Helper UNION_LEFT = new Helper() {

        public int getCoordinate(Rectangle rectangle)
        {
            return rectangle.x;
        }

        public int getSize(Rectangle rectangle)
        {
            return rectangle.width;
        }

        public int getOppoCoordinate(Rectangle rectangle)
        {
            return rectangle.y;
        }

        public int getOppoSize(Rectangle rectangle)
        {
            return rectangle.height;
        }

        public Rectangle asRect(int i, int j, int k, int l)
        {
            return new Rectangle(i, j, k, l);
        }

    }
;
    private static final VisibleHelper LEFT = new VisibleHelper() {

        public void ensureVisible(ElementCasePane elementcasepane, Rectangle rectangle)
        {
            elementcasepane.ensureColumnRowVisible((rectangle.x + rectangle.width) - 1, rectangle.y);
        }

    }
;
    private static final Helper UNION_UP = new Helper() {

        public int getCoordinate(Rectangle rectangle)
        {
            return GridKeyAction.UNION_LEFT.getOppoCoordinate(rectangle);
        }

        public int getSize(Rectangle rectangle)
        {
            return GridKeyAction.UNION_LEFT.getOppoSize(rectangle);
        }

        public int getOppoCoordinate(Rectangle rectangle)
        {
            return GridKeyAction.UNION_LEFT.getCoordinate(rectangle);
        }

        public int getOppoSize(Rectangle rectangle)
        {
            return GridKeyAction.UNION_LEFT.getSize(rectangle);
        }

        public Rectangle asRect(int i, int j, int k, int l)
        {
            return new Rectangle(j, i, l, k);
        }

    }
;
    private static final VisibleHelper UP = new VisibleHelper() {

        public void ensureVisible(ElementCasePane elementcasepane, Rectangle rectangle)
        {
            elementcasepane.ensureColumnRowVisible(rectangle.x, (rectangle.y + rectangle.height) - 1);
        }

    }
;

    public GridKeyAction(Grid grid1, String s, boolean flag)
    {
        grid = grid1;
        actionKey = s;
        isShift = flag;
    }

    protected static void initGridInputActionMap(Grid grid1)
    {
        InputMap inputmap = grid1.getInputMap();
        ActionMap actionmap = grid1.getActionMap();
        inputmap.put(KeyStroke.getKeyStroke(37, 0), "left");
        actionmap.put("left", new GridKeyAction(grid1, "left", false));
        inputmap.put(KeyStroke.getKeyStroke(37, 1), "left_shift");
        actionmap.put("left_shift", new GridKeyAction(grid1, "left", true));
        inputmap.put(KeyStroke.getKeyStroke(39, 0), "right");
        actionmap.put("right", new GridKeyAction(grid1, "right", false));
        inputmap.put(KeyStroke.getKeyStroke(39, 1), "right_shift");
        actionmap.put("right_shift", new GridKeyAction(grid1, "right", true));
        inputmap.put(KeyStroke.getKeyStroke(38, 0), "up");
        actionmap.put("up", new GridKeyAction(grid1, "up", false));
        inputmap.put(KeyStroke.getKeyStroke(38, 1), "up_shift");
        actionmap.put("up_shift", new GridKeyAction(grid1, "up", true));
        inputmap.put(KeyStroke.getKeyStroke(40, 0), "down");
        actionmap.put("down", new GridKeyAction(grid1, "down", false));
        inputmap.put(KeyStroke.getKeyStroke(40, 1), "down_shift");
        actionmap.put("down_shift", new GridKeyAction(grid1, "down", true));
        inputmap.put(KeyStroke.getKeyStroke(10, 0), "enter");
        actionmap.put("enter", new GridKeyAction(grid1, "enter", false));
        inputmap.put(KeyStroke.getKeyStroke(10, 1), "enter_shift");
        actionmap.put("enter_shift", new GridKeyAction(grid1, "enter", true));
        inputmap.put(KeyStroke.getKeyStroke(9, 0), "tab");
        actionmap.put("tab", new GridKeyAction(grid1, "tab", false));
        inputmap.put(KeyStroke.getKeyStroke(9, 1), "tab_shift");
        actionmap.put("tab_shift", new GridKeyAction(grid1, "tab", true));
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        if("left".equals(actionKey))
            selectionMove(2);
        else
        if("right".equals(actionKey))
            selectionMove(4);
        else
        if("up".equals(actionKey))
            selectionMove(1);
        else
        if("down".equals(actionKey))
            selectionMove(3);
        else
        if("enter".equals(actionKey))
            enterKeyPressed();
        else
        if("tab".equals(actionKey))
            tabKeyPressed();
    }

    private void enterKeyPressed()
    {
        if(grid.isCellEditing())
            grid.stopEditing();
        selectionMove(3);
    }

    private void tabKeyPressed()
    {
        if(grid.isCellEditing())
            grid.stopEditing();
        selectionMove(4);
    }

    private void selectionMove(int i)
    {
        ElementCasePane elementcasepane = grid.getElementCasePane();
        if(i == 3)
            elementcasepane.getSelection().moveDown(elementcasepane);
        else
        if(i == 4)
            elementcasepane.getSelection().moveRight(elementcasepane);
        else
        if(i == 1)
            elementcasepane.getSelection().moveUp(elementcasepane);
        else
        if(i == 2)
            elementcasepane.getSelection().moveLeft(elementcasepane);
        elementcasepane.repaint();
    }


}
