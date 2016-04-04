// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report;

import com.fr.base.ConfigManager;
import com.fr.base.ConfigManagerProvider;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.style.StylePane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import java.awt.event.*;
import javax.swing.*;

public class ReportStylePane extends StylePane
{

    private static final int Y_OFFSET = 8;

    public ReportStylePane()
    {
        getPreviewArea().addMouseListener(new MouseAdapter() {

            final ReportStylePane this$0;

            public void mousePressed(MouseEvent mouseevent)
            {
                if(!SwingUtilities.isRightMouseButton(mouseevent))
                {
                    return;
                } else
                {
                    JPopupMenu jpopupmenu = new JPopupMenu();
                    UIMenuItem uimenuitem = new UIMenuItem(Inter.getLocText("FR-Designer_Save_As_Global_Style"));
                    jpopupmenu.add(uimenuitem);
                    uimenuitem.addActionListener(new ActionListener() {

                        final _cls1 this$1;

                        public void actionPerformed(ActionEvent actionevent)
                        {
                            String s = JOptionPane.showInputDialog(getParent(), Inter.getLocText("FR-Designer_Input_The_Name_Of_Gloabel_Style"));
                            if(ComparatorUtils.equals(s, ""))
                                return;
                            if(ConfigManager.getProviderInstance().getStyle(s) == null)
                                ConfigManager.getProviderInstance().putStyle(s, updateBean());
                            else
                                JOptionPane.showMessageDialog(getParent(), (new StringBuilder()).append(Inter.getLocText("FR-Designer_This_Name_Has_Exsit")).append("!").toString(), Inter.getLocText("FR-Designer_Warning"), 2);
                        }

                    
                    {
                        this$1 = _cls1.this;
                        super();
                    }
                    }
);
                    GUICoreUtils.showPopupMenu(jpopupmenu, ReportStylePane.this, mouseevent.getX() - 1, mouseevent.getY() + 8);
                    return;
                }
            }

            
            {
                this$0 = ReportStylePane.this;
                super();
            }
        }
);
    }
}
