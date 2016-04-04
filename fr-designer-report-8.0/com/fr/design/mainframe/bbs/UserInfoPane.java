// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.bbs;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.BasicPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.*;
import com.fr.stable.StringUtils;
import java.awt.*;
import java.text.*;
import java.util.Calendar;
import java.util.Date;

// Referenced classes of package com.fr.design.mainframe.bbs:
//            UserInfoLabel, ExitLabel, BBSLoginDialog

public class UserInfoPane extends BasicPane
{

    private static final Color UN_LOGIN_BACKGROUND = new Color(210, 210, 210);
    private static final Color LOGIN_BACKGROUND = new Color(184, 220, 242);
    private static final int WIDTH = 104;
    private static final int HEIGHT = 24;
    private static final int LOGIN_DIFF_DAY = 7;
    private static final int WAIT_TIME = 10000;
    private UserInfoLabel userInfoLabel;
    private ExitLabel switchAccountLabel;

    public UserInfoLabel getUserInfoLabel()
    {
        return userInfoLabel;
    }

    public void setUserInfoLabel(UserInfoLabel userinfolabel)
    {
        userInfoLabel = userinfolabel;
    }

    public ExitLabel getSwitchAccountLabel()
    {
        return switchAccountLabel;
    }

    public void setSwitchAccountLabel(ExitLabel exitlabel)
    {
        switchAccountLabel = exitlabel;
    }

    public UserInfoPane()
    {
        setPreferredSize(new Dimension(104, 24));
        setLayout(new BorderLayout());
        userInfoLabel = new UserInfoLabel(this);
        switchAccountLabel = new ExitLabel(this);
        markUnSignIn();
        autoLogin();
        autoPushLoginDialog();
        add(userInfoLabel, "Center");
        add(switchAccountLabel, "East");
    }

    private void autoLogin()
    {
        Thread thread = new Thread(new Runnable() {

            final UserInfoPane this$0;

            public void run()
            {
                String s = DesignerEnvManager.getEnvManager().getBBSName();
                String s1 = DesignerEnvManager.getEnvManager().getBBSPassword();
                if(!BBSLoginDialog.login(s, s1))
                    markUnSignIn();
                else
                    markSignIn(s);
            }

            
            {
                this$0 = UserInfoPane.this;
                super();
            }
        }
);
        thread.start();
    }

    private int getDiffFromLastLogin()
    {
        String s = DesignerEnvManager.getEnvManager().getLastShowBBSTime();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
        Object obj = null;
        try
        {
            if(s != null)
            {
                Date date;
                synchronized(this)
                {
                    date = simpledateformat.parse(s);
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int i = calendar.get(6);
                calendar.setTime(new Date());
                int j = calendar.get(6);
                return j - i;
            }
        }
        catch(ParseException parseexception)
        {
            FRLogger.getLogger().error(parseexception.getMessage());
        }
        return 1;
    }

    private void autoPushLoginDialog()
    {
        Thread thread = new Thread(new Runnable() {

            final UserInfoPane this$0;

            public void run()
            {
                Thread.sleep(10000L);
                if(!FRContext.isChineseEnv())
                    return;
                try
                {
                    if(getDiffFromLastLogin() < 7)
                        return;
                }
                catch(InterruptedException interruptedexception)
                {
                    FRContext.getLogger().error(interruptedexception.getMessage());
                }
                String s = DesignerEnvManager.getEnvManager().getBBSName();
                if(StringUtils.isNotEmpty(s))
                    return;
                BBSLoginDialog bbslogindialog = userInfoLabel.getBbsLoginDialog();
                if(bbslogindialog == null)
                {
                    bbslogindialog = new BBSLoginDialog(DesignerContext.getDesignerFrame(), userInfoLabel);
                    userInfoLabel.setBbsLoginDialog(bbslogindialog);
                }
                bbslogindialog.showWindow();
                DesignerEnvManager.getEnvManager().setLastShowBBSTime(DateUtils.DATEFORMAT2.format(new Date()));
                return;
            }

            
            {
                this$0 = UserInfoPane.this;
                super();
            }
        }
);
        thread.start();
    }

    public void markUnSignIn()
    {
        userInfoLabel.setText(Inter.getLocText("FR-Base_UnSignIn"));
        switchAccountLabel.setVisible(false);
        userInfoLabel.setOpaque(true);
        userInfoLabel.setBackground(UN_LOGIN_BACKGROUND);
        userInfoLabel.resetUserName();
    }

    public void markSignIn(String s)
    {
        userInfoLabel.setText(s);
        userInfoLabel.setUserName(s);
        switchAccountLabel.setVisible(true);
        userInfoLabel.setOpaque(true);
        userInfoLabel.setBackground(LOGIN_BACKGROUND);
        switchAccountLabel.setOpaque(true);
        switchAccountLabel.setBackground(LOGIN_BACKGROUND);
    }

    protected String title4PopupWindow()
    {
        return "";
    }



}
