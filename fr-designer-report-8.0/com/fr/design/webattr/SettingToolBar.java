// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.base.BaseUtils;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

// Referenced classes of package com.fr.design.webattr:
//            ToolBarPane, EditToolBar

public class SettingToolBar extends JPanel
{
    private class DelAction extends AbstractAction
    {

        final SettingToolBar this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            toolBarPane.removeAll();
            toolBarPane.removeButtonList();
            toolBarPane.repaint();
        }

        public DelAction()
        {
            this$0 = SettingToolBar.this;
            super();
            putValue("SmallIcon", delIcon);
        }
    }

    private class SetAction extends AbstractAction
    {

        final SettingToolBar this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            final EditToolBar tb = new EditToolBar();
            tb.populate(toolBarPane.getFToolBar());
            BasicDialog basicdialog = tb.showWindow(DesignerContext.getDesignerFrame());
            basicdialog.addDialogActionListener(new DialogActionAdapter() {

                final EditToolBar val$tb;
                final SetAction this$1;

                public void doOk()
                {
                    toolBarPane.setFToolBar(tb.update());
                }

                
                {
                    this$1 = SetAction.this;
                    tb = edittoolbar;
                    super();
                }
            }
);
            basicdialog.setVisible(true);
        }

        public SetAction()
        {
            this$0 = SettingToolBar.this;
            super();
            putValue("SmallIcon", setIcon);
        }
    }


    private Icon setIcon;
    private Icon delIcon;
    private UIButton setButton;
    private UIButton delButton;
    private ToolBarPane toolBarPane;

    public SettingToolBar(String s, ToolBarPane toolbarpane)
    {
        setIcon = BaseUtils.readIcon("com/fr/design/images/toolbarbtn/toolbarbtnsetting.png");
        delIcon = BaseUtils.readIcon("com/fr/design/images/toolbarbtn/toolbarbtnclear.png");
        setBackground(Color.lightGray);
        add(new UILabel(s));
        toolBarPane = toolbarpane;
        setButton = GUICoreUtils.createTransparentButton(setIcon, setIcon, setIcon);
        setButton.setToolTipText(Inter.getLocText("Edit_Button_ToolBar"));
        setButton.setAction(new SetAction());
        delButton = GUICoreUtils.createTransparentButton(delIcon, delIcon, delIcon);
        delButton.setToolTipText(Inter.getLocText("Remove_Button_ToolBar"));
        delButton.setAction(new DelAction());
        add(setButton);
        add(delButton);
    }

    public void setEnabled(boolean flag)
    {
        setButton.setEnabled(flag);
        delButton.setEnabled(flag);
    }

    public void addActionListener(ActionListener actionlistener)
    {
        setButton.addActionListener(actionlistener);
        delButton.addActionListener(actionlistener);
    }



}
