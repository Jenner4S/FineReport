// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.bbs;

import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.UIDialog;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.ActionLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ipasswordfield.UIPassWordField;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.*;
import com.fr.general.http.HttpClient;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.bbs:
//            UserInfoLabel, UserInfoPane, ExitLabel

public class BBSLoginDialog extends UIDialog
{
    class BoxCenterAligmentPane extends JPanel
    {

        private UILabel textLabel;
        final BBSLoginDialog this$0;

        public void setFont(Font font)
        {
            super.setFont(font);
            if(textLabel != null)
                textLabel.setFont(font);
        }

        public BoxCenterAligmentPane(String s)
        {
            this(new UILabel(s));
        }

        public BoxCenterAligmentPane(UILabel uilabel)
        {
            this$0 = BBSLoginDialog.this;
            super();
            setLayout(FRGUIPaneFactory.createBorderLayout());
            JPanel jpanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            add(jpanel, "Center");
            textLabel = uilabel;
            jpanel.add(textLabel);
        }
    }


    private static final int DIALOG_WIDTH = 400;
    private static final int DIALOG_HEIGHT = 200;
    private static final String URL = "http://bbs.finereport.com/member.php?mod=logging&action=login&loginsubmit=yes&infloat=yes&lssubmit=yes&inajax=1";
    private static final String PASSWORD_RESET_URL = "http://bbs.finereport.com/portal.php?mod=topic&topicid=2";
    private static final String REGISTER_URL = "http://bbs.finereport.com/member.php?mod=register";
    private static final String LOGIN_SUCCESS_FLAG = "http://bbs.finereport.com";
    private static final Font DEFAULT_FONT = FRFont.getInstance("SimSun", 0, 14F);
    private static final String TEST_CONNECTION_URL = "http://bbs.finereport.com/member.php?mod=logging&action=login&loginsubmit=yes&infloat=yes&lssubmit=yes&inajax=1&username=test&password=123456";
    private static final int TIME_OUT = 10000;
    private static final int BUTTON_WIDTH = 90;
    private static final int V_GAP = 20;
    private static final int BUTTON_H_GAP = 155;
    private static final int NORTH_PANE_HEIGHT = 24;
    private static final int FIELD_HEIGHT = 26;
    private static final int FIELD_WIDTH = 204;
    private static final int FLOWLAYOUT_H_GAP = 15;
    private UILabel userLabel;
    private UILabel passLabel;
    private UITextField nameField;
    private UIPassWordField passField;
    private UIButton loginButton;
    private UILabel tipLabel;
    private BoxCenterAligmentPane passwordReset;
    private BoxCenterAligmentPane registerLabel;
    private KeyListener keyListener;
    private UserInfoLabel userInfoLabel;

    public UILabel getTipLabel()
    {
        return tipLabel;
    }

    public void setTipLabel(UILabel uilabel)
    {
        tipLabel = uilabel;
    }

    public BBSLoginDialog(Frame frame, UserInfoLabel userinfolabel)
    {
        super(frame);
        keyListener = new KeyAdapter() {

            final BBSLoginDialog this$0;

            public void keyPressed(KeyEvent keyevent)
            {
                int i = keyevent.getKeyCode();
                if(27 == i)
                {
                    setVisible(false);
                    return;
                }
                if(10 == i)
                    login();
            }

            
            {
                this$0 = BBSLoginDialog.this;
                super();
            }
        }
;
        JPanel jpanel = (JPanel)getContentPane();
        initComponents(jpanel);
        userInfoLabel = userinfolabel;
        setSize(new Dimension(400, 200));
    }

    private void initComponents(JPanel jpanel)
    {
        setTitle(Inter.getLocText("FR-Designer-BBSLogin_Login-Title"));
        tipLabel = new UILabel(Inter.getLocText("FR-Designer-BBSLogin_Login-Failure-Tip"));
        userLabel = new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Designer-BBSLogin_Account")).append(":").toString());
        passLabel = new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Designer-BBSLogin_Password")).append(":").toString());
        nameField = new UITextField();
        passField = new UIPassWordField();
        loginButton = new UIButton(Inter.getLocText("FR-Designer-BBSLogin_Login"));
        passwordReset = getURLActionLabel("http://bbs.finereport.com/portal.php?mod=topic&topicid=2");
        registerLabel = getURLActionLabel("http://bbs.finereport.com/member.php?mod=register");
        loginButton.addActionListener(new ActionListener() {

            final BBSLoginDialog this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                login();
            }

            
            {
                this$0 = BBSLoginDialog.this;
                super();
            }
        }
);
        nameField.addKeyListener(keyListener);
        passField.addKeyListener(keyListener);
        loginButton.addKeyListener(keyListener);
        userLabel.setFont(DEFAULT_FONT);
        passLabel.setFont(DEFAULT_FONT);
        tipLabel.setVisible(false);
        jpanel.setLayout(new BorderLayout());
        JPanel jpanel1 = new JPanel();
        jpanel1.setLayout(new BorderLayout(0, 20));
        initNorthPane(jpanel1);
        initCenterPane(jpanel1);
        initSouthPane(jpanel1);
        jpanel.add(jpanel1, "North");
        setResizable(false);
    }

    private void login()
    {
        if(nameField.getText().isEmpty())
        {
            tipForUsernameEmpty();
            nameField.requestFocus();
            return;
        }
        if(String.valueOf(passField.getPassword()).isEmpty())
        {
            tipForPasswordEmpty();
            passField.requestFocus();
            return;
        }
        if(!testConnection())
        {
            connectionFailue();
            return;
        }
        if(login(nameField.getText(), String.valueOf(passField.getPassword())))
            loginSuccess();
        else
            loginFailure();
    }

    private boolean testConnection()
    {
        HttpClient httpclient = new HttpClient("http://bbs.finereport.com/member.php?mod=logging&action=login&loginsubmit=yes&infloat=yes&lssubmit=yes&inajax=1&username=test&password=123456");
        return httpclient.isServerAlive();
    }

    private void initNorthPane(JPanel jpanel)
    {
        JPanel jpanel1 = new JPanel();
        jpanel1.setPreferredSize(new Dimension(400, 24));
        jpanel1.add(tipLabel);
        jpanel.add(jpanel1, "North");
    }

    private void initCenterPane(JPanel jpanel)
    {
        JPanel jpanel1 = new JPanel();
        jpanel1.setLayout(new GridLayout(2, 1, 0, 20));
        JPanel jpanel2 = new JPanel();
        jpanel2.setLayout(new FlowLayout(2, 15, 0));
        jpanel2.add(userLabel);
        nameField.setPreferredSize(new Dimension(204, 26));
        jpanel2.add(nameField);
        jpanel2.add(passwordReset);
        jpanel1.add(jpanel2);
        JPanel jpanel3 = new JPanel();
        jpanel3.setLayout(new FlowLayout(2, 15, 0));
        jpanel3.add(passLabel);
        jpanel3.add(passField);
        passField.setPreferredSize(new Dimension(204, 26));
        jpanel3.add(registerLabel);
        jpanel1.add(jpanel3);
        jpanel.add(jpanel1, "Center");
    }

    private void initSouthPane(JPanel jpanel)
    {
        JPanel jpanel1 = new JPanel();
        jpanel1.setLayout(new FlowLayout(2, 155, 0));
        loginButton.setPreferredSize(new Dimension(90, 26));
        loginButton.setFont(DEFAULT_FONT);
        jpanel1.add(loginButton);
        jpanel.add(jpanel1, "South");
    }

    private void loginSuccess()
    {
        DesignerEnvManager.getEnvManager().setBBSPassword(String.valueOf(passField.getPassword()));
        userInfoLabel.setUserName(nameField.getText());
        userInfoLabel.getUserInfoPane().markSignIn(nameField.getText());
        userInfoLabel.getUserInfoPane().getSwitchAccountLabel().setVisible(true);
        setVisible(false);
    }

    private void loginFailure()
    {
        setLoginFailureTxt(Inter.getLocText("FR-Designer-BBSLogin_Login-Failure-Tip"));
    }

    public void tipForUsernameEmpty()
    {
        setLoginFailureTxt(Inter.getLocText("FR-Designer-BBSLogin_Username-Empty-Tip"));
    }

    private void tipForPasswordEmpty()
    {
        setLoginFailureTxt(Inter.getLocText("FR-Designer-BBSLogin_Password-Empty-Tip"));
    }

    private void setLoginFailureTxt(String s)
    {
        tipLabel.setText(s);
        tipLabel.setForeground(Color.RED);
        tipLabel.repaint();
        tipLabel.setVisible(true);
    }

    private void connectionFailue()
    {
        setLoginFailureTxt(Inter.getLocText("FR-Designer-BBSLogin_Connection-Failure"));
    }

    public void clearLoginInformation()
    {
        tipLabel.setText("");
        nameField.setText("");
        passField.setText("");
    }

    public void showWindow()
    {
        GUICoreUtils.centerWindow(this);
        setVisible(true);
    }

    public void checkValid()
        throws Exception
    {
    }

    public static boolean login(String s, String s1)
    {
        String s2 = (new StringBuilder()).append("http://bbs.finereport.com/member.php?mod=logging&action=login&loginsubmit=yes&infloat=yes&lssubmit=yes&inajax=1&username=").append(s).append("&password=").append(s1).toString();
        HttpClient httpclient = new HttpClient(s2);
        httpclient.setTimeout(10000);
        if(httpclient.getResponseCodeNoException() == 200)
            try
            {
                String s3 = httpclient.getResponseText("GBK");
                if(s3.contains("http://bbs.finereport.com"))
                    return true;
            }
            catch(Exception exception)
            {
                FRLogger.getLogger().error(exception.getMessage());
            }
        return false;
    }

    private BoxCenterAligmentPane getURLActionLabel(final String url)
    {
        ActionLabel actionlabel = new ActionLabel(url);
        if(ComparatorUtils.equals(url, "http://bbs.finereport.com/portal.php?mod=topic&topicid=2"))
            actionlabel.setText(Inter.getLocText("FR-Designer-BBSLogin_Forgot-Password"));
        else
            actionlabel.setText(Inter.getLocText("FR-Designer-BBSLogin_Register-Account"));
        actionlabel.addActionListener(new ActionListener() {

            final String val$url;
            final BBSLoginDialog this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                try
                {
                    Desktop.getDesktop().browse(new URI(url));
                }
                catch(Exception exception) { }
            }

            
            {
                this$0 = BBSLoginDialog.this;
                url = s;
                super();
            }
        }
);
        return new BoxCenterAligmentPane(actionlabel);
    }


}
