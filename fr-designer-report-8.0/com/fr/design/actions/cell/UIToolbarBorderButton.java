// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.cell;

import com.fr.base.BaseUtils;
import com.fr.base.CellBorderStyle;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UICombinationButton;
import com.fr.design.gui.ipoppane.PopupHider;
import com.fr.design.icon.BorderIcon;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.style.BorderPane;
import com.fr.design.style.BorderUtils;
import com.fr.design.style.color.TransparentPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.event.*;

public class UIToolbarBorderButton extends UICombinationButton
    implements PopupHider
{
    class NormalBorderPane extends TransparentPane
    {

        PopupHider popupHider;
        final UIToolbarBorderButton this$0;

        public void initCenterPaneChildren(JPanel jpanel)
        {
            JPanel jpanel1 = new JPanel();
            jpanel.add(jpanel1);
            jpanel1.setLayout(new GridLayout(3, 4, 2, 2));
            for(int i = 0; i < UIToolbarBorderButton.borderStyleArray.length; i++)
            {
                final UIButton borderStyleCell = new UIButton(new BorderIcon(UIToolbarBorderButton.borderStyleArray[i]));
                borderStyleCell.set4ToolbarButton();
                borderStyleCell.addMouseListener(new MouseAdapter() {

                    final NormalBorderPane this$1;

                    public void mousePressed(MouseEvent mouseevent)
                    {
                        UIButton uibutton = (UIButton)mouseevent.getSource();
                        if(uibutton.getIcon() instanceof BorderIcon)
                        {
                            BorderIcon bordericon = (BorderIcon)uibutton.getIcon();
                            setCellBorderStyle(bordericon.cellBorderStyle);
                            hidePopupMenu();
                        } else
                        {
                            setCellBorderStyle(UIToolbarBorderButton.borderStyleArray[0]);
                        }
                    }

                
                {
                    this$1 = NormalBorderPane.this;
                    super();
                }
                }
);
                borderStyleCell.addMouseListener(new MouseAdapter() {

                    final UIButton val$borderStyleCell;
                    final NormalBorderPane this$1;

                    public void mouseEntered(MouseEvent mouseevent)
                    {
                        borderStyleCell.setBorder(BorderFactory.createEtchedBorder());
                    }

                    public void mouseExited(MouseEvent mouseevent)
                    {
                        borderStyleCell.setBorder(null);
                    }

                
                {
                    this$1 = NormalBorderPane.this;
                    borderStyleCell = uibutton;
                    super();
                }
                }
);
                borderStyleCell.setToolTipText(UIToolbarBorderButton.BorderStyleTooltips[i]);
                jpanel1.add(borderStyleCell);
            }

            jpanel.add(Box.createVerticalStrut(5));
            jpanel.add(new JSeparator());
            jpanel.add(Box.createVerticalStrut(5));
        }

        public void doTransparent()
        {
            setCellBorderStyle(null);
            popupHider.hidePopupMenu();
        }

        public void customButtonPressed()
        {
            popupHider.hidePopupMenu();
            final BorderPane borderPane = new BorderPane();
            BasicDialog basicdialog = borderPane.showWindow(SwingUtilities.getWindowAncestor(reportPane));
            Object aobj[] = BorderUtils.createCellBorderObject(reportPane);
            if(aobj != null && aobj.length == 4)
                borderPane.populate((CellBorderStyle)aobj[0], ((Boolean)aobj[1]).booleanValue(), ((Integer)aobj[2]).intValue(), (Color)aobj[3]);
            basicdialog.addDialogActionListener(new DialogActionAdapter() {

                final BorderPane val$borderPane;
                final NormalBorderPane this$1;

                public void doOk()
                {
                    CellBorderStyle cellborderstyle = borderPane.update();
                    setCellBorderStyle(cellborderstyle);
                }

                
                {
                    this$1 = NormalBorderPane.this;
                    borderPane = borderpane;
                    super();
                }
            }
);
            basicdialog.setVisible(true);
        }

        protected String title4PopupWindow()
        {
            return Inter.getLocText("Border");
        }

        public NormalBorderPane(boolean flag, PopupHider popuphider)
        {
            this$0 = UIToolbarBorderButton.this;
            super(flag);
            popupHider = popuphider;
        }
    }

    class BorderStyleControlWindow extends JPopupMenu
    {

        final UIToolbarBorderButton this$0;

        public void initComponents(boolean flag)
        {
            setLightWeightPopupEnabled(JPopupMenu.getDefaultLightWeightPopupEnabled());
            setLayout(FRGUIPaneFactory.createBorderLayout());
            setBorderPainted(true);
            setBorder(UIManager.getBorder("PopupMenu.border"));
            setOpaque(false);
            setDoubleBuffered(true);
            setFocusable(false);
            add(new NormalBorderPane(flag, UIToolbarBorderButton.this), "Center");
            pack();
        }

        public BorderStyleControlWindow(boolean flag)
        {
            this$0 = UIToolbarBorderButton.this;
            super();
            initComponents(flag);
        }
    }


    private EventListenerList styleChangeListenerList;
    private boolean isCanBeNull;
    private ElementCasePane reportPane;
    private JPopupMenu popupWin;
    private CellBorderStyle cellBorderStyle;
    private static final CellBorderStyle borderStyleArray[];
    private static final String BorderStyleTooltips[] = {
        Inter.getLocText("NO_Border_Line"), Inter.getLocText("Bottom_Border_Line"), Inter.getLocText("Left_Border_Line"), Inter.getLocText("Right_Border_Line"), Inter.getLocText("Double_Bottom_BorderLine"), Inter.getLocText("Thick_Bottom_Border_Line"), Inter.getLocText("Top_Bottom_Border_Line"), Inter.getLocText("Top_And_Double_Bottom_Border_Line"), Inter.getLocText("Top_And_Thick_Bottom_Border_Line"), Inter.getLocText("All_Border_Line"), 
        Inter.getLocText("Out_Border_Line"), Inter.getLocText("Out_Thick_Border_Line")
    };

    public UIToolbarBorderButton(Icon icon, ElementCasePane elementcasepane)
    {
        super(new UIButton(icon), new UIButton(BaseUtils.readIcon("/com/fr/design/images/gui/popup.gif")));
        styleChangeListenerList = new EventListenerList();
        isCanBeNull = false;
        cellBorderStyle = new CellBorderStyle();
        reportPane = elementcasepane;
    }

    public CellBorderStyle getCellBorderStyle()
    {
        return cellBorderStyle;
    }

    public void setCellBorderStyle(CellBorderStyle cellborderstyle)
    {
        cellBorderStyle = cellborderstyle;
        leftButton.setIcon(new BorderIcon(cellborderstyle));
        fireStyleStateChanged();
    }

    public void setEnabled(boolean flag)
    {
        super.setEnabled(flag);
        if(!flag)
            cellBorderStyle = null;
        leftButton.setEnabled(flag);
        rightButton.setEnabled(flag);
    }

    public void setToolTipText(String s)
    {
        super.setToolTipText(s);
        leftButton.setToolTipText(s);
        rightButton.setToolTipText(s);
    }

    private void showPopupMenu()
    {
        if(popupWin != null && popupWin.isVisible())
        {
            hidePopupMenu();
            return;
        }
        if(!isEnabled())
        {
            return;
        } else
        {
            popupWin = getActionPopupMenu();
            GUICoreUtils.showPopupMenu(popupWin, this, 0, getSize().height);
            return;
        }
    }

    protected JPopupMenu getActionPopupMenu()
    {
        if(popupWin == null)
            popupWin = new BorderStyleControlWindow(isCanBeNull());
        return popupWin;
    }

    protected void leftButtonClickEvent()
    {
        cellBorderStyle = ((BorderIcon)getLeftButton().getIcon()).cellBorderStyle;
        fireStyleStateChanged();
    }

    protected void rightButtonClickEvent()
    {
        showPopupMenu();
    }

    public void addStyleChangeListener(ChangeListener changelistener)
    {
        styleChangeListenerList.add(javax/swing/event/ChangeListener, changelistener);
    }

    public void removeColorChangeListener(ChangeListener changelistener)
    {
        styleChangeListenerList.remove(javax/swing/event/ChangeListener, changelistener);
    }

    public void fireStyleStateChanged()
    {
        Object aobj[] = styleChangeListenerList.getListenerList();
        ChangeEvent changeevent = null;
        for(int i = aobj.length - 2; i >= 0; i -= 2)
        {
            if(aobj[i] != javax/swing/event/ChangeListener)
                continue;
            if(changeevent == null)
                changeevent = new ChangeEvent(this);
            ((ChangeListener)aobj[i + 1]).stateChanged(changeevent);
        }

    }

    public boolean isCanBeNull()
    {
        return isCanBeNull;
    }

    public void setCanBeNull(boolean flag)
    {
        isCanBeNull = flag;
    }

    public void hidePopupMenu()
    {
        if(popupWin != null)
            popupWin.setVisible(false);
        popupWin = null;
    }

    static 
    {
        borderStyleArray = (new CellBorderStyle[] {
            new CellBorderStyle(), new CellBorderStyle(Color.black, 0, Color.black, 0, Color.black, 1, Color.black, 0, Color.black, 0, Color.black, 0), new CellBorderStyle(Color.black, 0, Color.black, 1, Color.black, 0, Color.black, 0, Color.black, 0, Color.black, 0), new CellBorderStyle(Color.black, 0, Color.black, 0, Color.black, 0, Color.black, 1, Color.black, 0, Color.black, 0), new CellBorderStyle(Color.black, 0, Color.black, 0, Color.black, 6, Color.black, 0, Color.black, 0, Color.black, 0), new CellBorderStyle(Color.black, 0, Color.black, 0, Color.black, 5, Color.black, 0, Color.black, 0, Color.black, 0), new CellBorderStyle(Color.black, 1, Color.black, 0, Color.black, 1, Color.black, 0, Color.black, 0, Color.black, 0), new CellBorderStyle(Color.black, 1, Color.black, 0, Color.black, 6, Color.black, 0, Color.black, 0, Color.black, 0), new CellBorderStyle(Color.black, 1, Color.black, 0, Color.black, 5, Color.black, 0, Color.black, 0, Color.black, 0), new CellBorderStyle(Color.black, 1, Color.black, 1, Color.black, 1, Color.black, 1, Color.black, 1, Color.black, 1), 
            new CellBorderStyle(Color.black, 1, Color.black, 1, Color.black, 1, Color.black, 1, Color.black, 0, Color.black, 0), new CellBorderStyle(Color.black, 5, Color.black, 5, Color.black, 5, Color.black, 5, Color.black, 0, Color.black, 0)
        });
    }



}
