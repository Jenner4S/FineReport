// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.bbs;

import com.fr.base.BaseUtils;
import com.fr.design.DesignerEnvManager;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Referenced classes of package com.fr.design.mainframe.bbs:
//            UserInfoPane, BBSLoginDialog, UserInfoLabel

public class ExitLabel extends UILabel
{

    private static final int MENU_HEIGHT = 20;
    private UserInfoPane infoPane;
    private MouseAdapter mouseListener;

    public ExitLabel(UserInfoPane userinfopane)
    {
        mouseListener = new MouseAdapter() {

            final ExitLabel this$0;

            public void mouseClicked(MouseEvent mouseevent)
            {
                UIPopupMenu uipopupmenu = new UIPopupMenu();
                uipopupmenu.setOnlyText(true);
                uipopupmenu.setPopupSize(infoPane.getWidth(), infoPane.getHeight());
                UIMenuItem uimenuitem = new UIMenuItem(Inter.getLocText("FR-Designer-BBSLogin_Switch-Account"));
                uimenuitem.addMouseListener(new MouseAdapter() {

                    final _cls1 this$1;

                    public void mousePressed(MouseEvent mouseevent1)
                    {
                        clearLoingInformation();
                        updateInfoPane();
                    }

                    
                    {
                        this$1 = _cls1.this;
                        super();
                    }
                }
);
                uipopupmenu.add(uimenuitem);
                GUICoreUtils.showPopupMenu(uipopupmenu, ExitLabel.this, 0, 20);
            }

            private void clearLoingInformation()
            {
                DesignerEnvManager.getEnvManager().setBBSName("");
                DesignerEnvManager.getEnvManager().setBBSPassword("");
            }

            private void updateInfoPane()
            {
                infoPane.markUnSignIn();
                setVisible(false);
                BBSLoginDialog bbslogindialog = infoPane.getUserInfoLabel().getBbsLoginDialog();
                if(bbslogindialog == null)
                {
                    bbslogindialog = new BBSLoginDialog(DesignerContext.getDesignerFrame(), infoPane.getUserInfoLabel());
                    infoPane.getUserInfoLabel().setBbsLoginDialog(bbslogindialog);
                    bbslogindialog.showWindow();
                }
                bbslogindialog.getTipLabel().setVisible(false);
                bbslogindialog.setModal(true);
                bbslogindialog.clearLoginInformation();
                bbslogindialog.setVisible(true);
            }



            
            {
                this$0 = ExitLabel.this;
                super();
            }
        }
;
        infoPane = userinfopane;
        setIcon(BaseUtils.readIcon("/com/fr/design/mainframe/bbs/images/switch.png"));
        addMouseListener(mouseListener);
    }

}
