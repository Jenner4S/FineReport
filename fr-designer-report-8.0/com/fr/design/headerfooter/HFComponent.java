// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.headerfooter;

import com.fr.base.BaseUtils;
import com.fr.base.headerfooter.*;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Referenced classes of package com.fr.design.headerfooter:
//            HFAttributesEditDialog, MoveActionListener

public class HFComponent extends UILabel
    implements MoveActionListener
{

    private HFElement hfElement;
    private ActionListener moveLeftActionListener;
    private ActionListener moveRightActionListener;
    private ActionListener deleteActionListener;
    private ChangeListener contentChangeListener;
    private JPopupMenu popupMenu;
    private MouseListener editMouseListener;

    public HFComponent(HFElement hfelement)
    {
        moveLeftActionListener = null;
        moveRightActionListener = null;
        deleteActionListener = null;
        contentChangeListener = null;
        editMouseListener = new MouseAdapter() {

            final HFComponent this$0;

            public void mouseReleased(MouseEvent mouseevent)
            {
                if(mouseevent.isPopupTrigger())
                {
                    GUICoreUtils.showPopupMenu(popupMenu, HFComponent.this, mouseevent.getX(), mouseevent.getY());
                    return;
                } else
                {
                    popupHFAttributesEditDialog();
                    return;
                }
            }

            
            {
                this$0 = HFComponent.this;
                super();
            }
        }
;
        setBorder(BorderFactory.createLineBorder(Color.gray));
        setHorizontalAlignment(0);
        setCursor(Cursor.getPredefinedCursor(12));
        addMouseListener(editMouseListener);
        popupMenu = new JPopupMenu();
        UIMenuItem uimenuitem = new UIMenuItem((new StringBuilder()).append(Inter.getLocText("Edit")).append("...").toString());
        uimenuitem.setMnemonic('E');
        popupMenu.add(uimenuitem);
        uimenuitem.addMouseListener(editMouseListener);
        popupMenu.add(new JSeparator());
        menuItemAction("HF-Move_Left", 'L');
        menuItemAction("HF-Move_Right", 'R');
        popupMenu.add(new JSeparator());
        menuItemAction("Delete", 'D');
        setHFElement(hfelement);
    }

    public void menuItemAction(String s, final char o)
    {
        UIMenuItem uimenuitem = new UIMenuItem(Inter.getLocText(s));
        uimenuitem.setMnemonic(o);
        popupMenu.add(uimenuitem);
        uimenuitem.addActionListener(new ActionListener() {

            final char val$o;
            final HFComponent this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                switch(o)
                {
                case 68: // 'D'
                    doDelete();
                    break;

                case 82: // 'R'
                    doMoveRight();
                    break;

                case 76: // 'L'
                    doMoveLeft();
                    break;
                }
            }

            
            {
                this$0 = HFComponent.this;
                o = c;
                super();
            }
        }
);
    }

    public HFElement getHFElement()
    {
        return hfElement;
    }

    public void setHFElement(HFElement hfelement)
    {
        hfElement = hfelement;
        setIcon(getHFElementIcon(hfelement));
        setToolTipText(getHFELementText(hfelement));
    }

    public ActionListener getMoveLeftActionListener()
    {
        return moveLeftActionListener;
    }

    public void setMoveLeftActionListener(ActionListener actionlistener)
    {
        moveLeftActionListener = actionlistener;
    }

    public ActionListener getMoveRightActionListener()
    {
        return moveRightActionListener;
    }

    public void setMoveRightActionListener(ActionListener actionlistener)
    {
        moveRightActionListener = actionlistener;
    }

    public ActionListener getDeleteActionListener()
    {
        return deleteActionListener;
    }

    public void setDeleteActionListener(ActionListener actionlistener)
    {
        deleteActionListener = actionlistener;
    }

    public ChangeListener getContentChangeListener()
    {
        return contentChangeListener;
    }

    public void setContentChangeListener(ChangeListener changelistener)
    {
        contentChangeListener = changelistener;
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(24, 24);
    }

    private void popupHFAttributesEditDialog()
    {
        final HFAttributesEditDialog hfAttributesEditDialog = new HFAttributesEditDialog();
        hfAttributesEditDialog.populate(hfElement);
        hfAttributesEditDialog.addMoveActionListener(this);
        hfAttributesEditDialog.showWindow(SwingUtilities.getWindowAncestor(this), new DialogActionAdapter() {

            final HFAttributesEditDialog val$hfAttributesEditDialog;
            final HFComponent this$0;

            public void doOk()
            {
                hfAttributesEditDialog.update();
                contentChanged();
            }

            
            {
                this$0 = HFComponent.this;
                hfAttributesEditDialog = hfattributeseditdialog;
                super();
            }
        }
).setVisible(true);
    }

    public void doMoveLeft()
    {
        if(moveLeftActionListener != null)
        {
            ActionEvent actionevent = new ActionEvent(this, 100, "Move Left");
            moveLeftActionListener.actionPerformed(actionevent);
        }
    }

    public void doMoveRight()
    {
        if(moveRightActionListener != null)
        {
            ActionEvent actionevent = new ActionEvent(this, 100, "Move Right");
            moveRightActionListener.actionPerformed(actionevent);
        }
    }

    public void doDelete()
    {
        if(deleteActionListener != null)
        {
            ActionEvent actionevent = new ActionEvent(this, 100, "Delete");
            deleteActionListener.actionPerformed(actionevent);
        }
    }

    private void contentChanged()
    {
        if(contentChangeListener != null)
        {
            ChangeEvent changeevent = new ChangeEvent(this);
            contentChangeListener.stateChanged(changeevent);
        }
    }

    public static String getHFELementText(HFElement hfelement)
    {
        if(hfelement.getClass().equals(com/fr/base/headerfooter/TextHFElement))
            return Inter.getLocText("Text");
        if(hfelement.getClass().equals(com/fr/base/headerfooter/FormulaHFElement))
            return Inter.getLocText("Formula");
        if(hfelement.getClass().equals(com/fr/base/headerfooter/PageNumberHFElement))
            return Inter.getLocText("HF-Page_Number");
        if(hfelement.getClass().equals(com/fr/base/headerfooter/NumberOfPageHFElement))
            return Inter.getLocText("HF-Number_of_Page");
        if(hfelement.getClass().equals(com/fr/base/headerfooter/DateHFElement))
            return Inter.getLocText("Date");
        if(hfelement.getClass().equals(com/fr/base/headerfooter/TimeHFElement))
            return Inter.getLocText("Time");
        if(hfelement.getClass().equals(com/fr/base/headerfooter/ImageHFElement))
            return Inter.getLocText("Image");
        if(hfelement.getClass().equals(com/fr/base/headerfooter/NewLineHFElement))
            return Inter.getLocText("HF-New_Line");
        else
            return Inter.getLocText("HF-Undefined");
    }

    public static Icon getHFElementIcon(HFElement hfelement)
    {
        if(hfelement.getClass().equals(com/fr/base/headerfooter/TextHFElement))
            return BaseUtils.readIcon("/com/fr/base/images/dialog/headerfooter/text.png");
        if(hfelement.getClass().equals(com/fr/base/headerfooter/FormulaHFElement))
            return BaseUtils.readIcon("/com/fr/base/images/dialog/headerfooter/formula.png");
        if(hfelement.getClass().equals(com/fr/base/headerfooter/PageNumberHFElement))
            return BaseUtils.readIcon("/com/fr/base/images/dialog/headerfooter/page.png");
        if(hfelement.getClass().equals(com/fr/base/headerfooter/NumberOfPageHFElement))
            return BaseUtils.readIcon("/com/fr/base/images/dialog/headerfooter/pages.png");
        if(hfelement.getClass().equals(com/fr/base/headerfooter/DateHFElement))
            return BaseUtils.readIcon("/com/fr/base/images/dialog/headerfooter/date.png");
        if(hfelement.getClass().equals(com/fr/base/headerfooter/TimeHFElement))
            return BaseUtils.readIcon("/com/fr/base/images/dialog/headerfooter/time.png");
        if(hfelement.getClass().equals(com/fr/base/headerfooter/ImageHFElement))
            return BaseUtils.readIcon("/com/fr/base/images/dialog/headerfooter/image.png");
        if(hfelement.getClass().equals(com/fr/base/headerfooter/NewLineHFElement))
            return BaseUtils.readIcon("/com/fr/base/images/dialog/headerfooter/newLine.png");
        else
            return BaseUtils.readIcon("/com/fr/base/images/dialog/headerfooter/undefined.png");
    }



}
