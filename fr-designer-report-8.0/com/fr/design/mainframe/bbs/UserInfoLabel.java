// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.bbs;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.extra.LoginCheckContext;
import com.fr.design.extra.LoginCheckListener;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.*;
import com.fr.general.http.HttpClient;
import com.fr.stable.StringUtils;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

// Referenced classes of package com.fr.design.mainframe.bbs:
//            UserInfoPane, BBSLoginDialog, BBSConstants, BBSDialog

public class UserInfoLabel extends UILabel
{

    private static final long CHECK_MESSAGE_TIME = 30000L;
    private static final long DELAY_TIME = 2000L;
    private static final String MESSAGE_KEY = "messageCount";
    private static final int MIN_MESSAGE_COUNT = 1;
    private String userName;
    private int messageCount;
    private UserInfoPane userInfoPane;
    private BBSLoginDialog bbsLoginDialog;
    private MouseAdapter userInfoAdapter;

    public UserInfoPane getUserInfoPane()
    {
        return userInfoPane;
    }

    public void setUserInfoPane(UserInfoPane userinfopane)
    {
        userInfoPane = userinfopane;
    }

    public BBSLoginDialog getBbsLoginDialog()
    {
        return bbsLoginDialog;
    }

    public void setBbsLoginDialog(BBSLoginDialog bbslogindialog)
    {
        bbsLoginDialog = bbslogindialog;
    }

    public UserInfoLabel(UserInfoPane userinfopane)
    {
        userInfoAdapter = new MouseAdapter() {

            final UserInfoLabel this$0;

            public void mouseEntered(MouseEvent mouseevent)
            {
                setCursor(new Cursor(12));
            }

            public void mouseClicked(MouseEvent mouseevent)
            {
                if(StringUtils.isNotEmpty(userName))
                {
                    try
                    {
                        Desktop.getDesktop().browse(new URI(BBSConstants.DEFAULT_URL));
                    }
                    catch(Exception exception) { }
                    return;
                }
                if(bbsLoginDialog == null)
                    bbsLoginDialog = new BBSLoginDialog(DesignerContext.getDesignerFrame(), UserInfoLabel.this);
                bbsLoginDialog.clearLoginInformation();
                bbsLoginDialog.setModal(true);
                bbsLoginDialog.showWindow();
            }

            
            {
                this$0 = UserInfoLabel.this;
                super();
            }
        }
;
        userInfoPane = userinfopane;
        String s = DesignerEnvManager.getEnvManager().getBBSName();
        addMouseListener(userInfoAdapter);
        setHorizontalAlignment(0);
        setText(s);
        setUserName(s);
        LoginCheckContext.addLoginCheckListener(new LoginCheckListener() {

            final UserInfoLabel this$0;

            public void loginChecked()
            {
                if(bbsLoginDialog == null)
                    bbsLoginDialog = new BBSLoginDialog(DesignerContext.getDesignerFrame(), UserInfoLabel.this);
                bbsLoginDialog.clearLoginInformation();
                bbsLoginDialog.tipForUsernameEmpty();
                bbsLoginDialog.setModal(true);
                bbsLoginDialog.showWindow();
            }

            
            {
                this$0 = UserInfoLabel.this;
                super();
            }
        }
);
    }

    public static void showBBSDialog()
    {
        Thread thread = new Thread(new Runnable() {

            public void run()
            {
                String s;
                if(!FRContext.isChineseEnv())
                    return;
                s = DesignerEnvManager.getEnvManager().getLastShowBBSNewsTime();
                String s1 = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
                if(ComparatorUtils.equals(s, s1))
                    return;
                try
                {
                    Thread.sleep(2000L);
                }
                catch(InterruptedException interruptedexception)
                {
                    FRContext.getLogger().error(interruptedexception.getMessage());
                }
                HttpClient httpclient = new HttpClient(BBSConstants.UPDATE_INFO_URL);
                if(!httpclient.isServerAlive())
                    return;
                String s2 = httpclient.getResponseText();
                if(s2.indexOf(BBSConstants.UPDATE_KEY) == -1)
                    return;
                try
                {
                    BBSDialog bbsdialog = new BBSDialog(DesignerContext.getDesignerFrame());
                    bbsdialog.showWindow(BBSConstants.UPDATE_INFO_URL);
                    DesignerEnvManager.getEnvManager().setLastShowBBSNewsTime(DateUtils.DATEFORMAT2.format(new Date()));
                }
                catch(Throwable throwable) { }
                return;
            }

        }
);
        thread.start();
    }

    private void sleep(long l)
    {
        try
        {
            Thread.sleep(l);
        }
        catch(InterruptedException interruptedexception)
        {
            FRContext.getLogger().error(interruptedexception.getMessage());
        }
    }

    public String getUserName()
    {
        return userName;
    }

    public void resetUserName()
    {
        userName = "";
    }

    public void setUserName(String s)
    {
        if(StringUtils.isEmpty(s))
            return;
        if(StringUtils.isEmpty(userName))
            updateMessageCount();
        DesignerEnvManager.getEnvManager().setBBSName(s);
        userName = s;
    }

    private void updateMessageCount()
    {
        Thread thread = new Thread(new Runnable() {

            final UserInfoLabel this$0;

            public void run()
            {
                sleep(30000L);
                for(; StringUtils.isNotEmpty(DesignerEnvManager.getEnvManager().getBBSName()); sleep(30000L))
                {
                    HashMap hashmap = new HashMap();
                    hashmap.put("username", encode(encode(userName)));
                    HttpClient httpclient = new HttpClient(BBSConstants.GET_MESSAGE_URL, hashmap);
                    httpclient.asGet();
                    if(!httpclient.isServerAlive())
                        continue;
                    try
                    {
                        String s = httpclient.getResponseText();
                        if(StringUtils.isNotEmpty(s))
                            setMessageCount(Integer.parseInt(s));
                    }
                    catch(Exception exception) { }
                }

            }

            
            {
                this$0 = UserInfoLabel.this;
                super();
            }
        }
);
        thread.start();
    }

    private String encode(String s)
    {
        try
        {
            return URLEncoder.encode(s, "UTF-8");
        }
        catch(UnsupportedEncodingException unsupportedencodingexception)
        {
            return s;
        }
    }

    public int getMessageCount()
    {
        return messageCount;
    }

    public void setMessageCount(int i)
    {
        if(messageCount == 1 && i < 1)
        {
            setText(userName);
            return;
        }
        if(messageCount == i || i < 1)
        {
            return;
        } else
        {
            messageCount = i;
            StringBuilder stringbuilder = new StringBuilder();
            stringbuilder.append(" ").append(userName).append("(").append(messageCount).append(")").append(" ");
            setText(stringbuilder.toString());
            return;
        }
    }





}
