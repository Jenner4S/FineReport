// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.start;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.UIDialog;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextarea.DescriptionTextArea;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.ActiveKeyGenerator;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.OperatingSystem;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.util.Locale;
import javax.swing.*;

public class CollectUserInformationDialog extends UIDialog
{

    private static final String CN_LOGIN_HTML = "http://www.finereport.com/products/frlogin";
    private static final String EN_LOGIN_HTML = "http://www.finereport.com/en/frlogin";
    private static final String TW_LOGIN_HTML = "http://www.finereport.com/tw/products/frlogin";
    private static final String JP_LOGIN_HTML = "http://www.finereport.com/jp/products/frlogin";
    private static final int ONLINE_VERIFY_TIMEOUT = 30000;
    private UITextField keyTextField;
    private DescriptionTextArea descriptionTextArea;
    ActionListener actionListener;
    private ActionListener verifyActionListener;

    public CollectUserInformationDialog(Frame frame)
    {
        super(frame);
        actionListener = new ActionListener() {

            final CollectUserInformationDialog this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                getKeyAction();
            }

            
            {
                this$0 = CollectUserInformationDialog.this;
                super();
            }
        }
;
        verifyActionListener = new ActionListener() {

            final CollectUserInformationDialog this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                String s = getKey();
                if(ActiveKeyGenerator.verify(s, 30000))
                {
                    String s1 = Inter.getLocText("FR-Designer-Collect_Information_Successfully");
                    JOptionPane.showMessageDialog(CollectUserInformationDialog.this, s1);
                    DesignerEnvManager.getEnvManager().setActivationKey(s);
                    doOK();
                } else
                {
                    String s2 = Inter.getLocText("Collect-The_user_information_code_is_invalid");
                    JOptionPane.showMessageDialog(CollectUserInformationDialog.this, s2);
                }
            }

            
            {
                this$0 = CollectUserInformationDialog.this;
                super();
            }
        }
;
        initComponents();
        setDefaultCloseOperation(0);
    }

    protected void initComponents()
    {
        JPanel jpanel = (JPanel)getContentPane();
        jpanel.setLayout(FRGUIPaneFactory.createM_BorderLayout());
        jpanel.setBorder(BorderFactory.createEmptyBorder(2, 4, 4, 4));
        applyClosingAction();
        JPanel jpanel1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel.add(jpanel1, "Center");
        JPanel jpanel2 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel1.add(jpanel2, "North");
        jpanel2.setBorder(BorderFactory.createTitledBorder(null, Inter.getLocText("Collect-Enter_your_user_information_code(It's_free_to_get_from_product's_official_website)"), 4, 2));
        JPanel jpanel3 = new JPanel(new BorderLayout(4, 4));
        jpanel3.setBorder(BorderFactory.createEmptyBorder(32, 2, 32, 2));
        jpanel2.add(jpanel3);
        UILabel uilabel = new UILabel();
        uilabel.setText((new StringBuilder()).append(Inter.getLocText("FR-Designer-Collect_Information_free")).append(":").toString());
        jpanel3.add(uilabel, "West");
        keyTextField = new UITextField();
        jpanel3.add(keyTextField, "Center");
        keyTextField.setMaximumSize(new Dimension(keyTextField.getPreferredSize().width, 25));
        macSystemHit(jpanel3);
        UIButton uibutton = new UIButton(Inter.getLocText("Collect-Click!_Get_user_information_code"));
        uibutton.setMnemonic('F');
        jpanel3.add(uibutton, "East");
        uibutton.addActionListener(actionListener);
        descriptionTextArea = new DescriptionTextArea();
        descriptionTextArea.setRows(5);
        descriptionTextArea.setBorder(BorderFactory.createTitledBorder(Inter.getLocText("FR-Designer-Collect_Information_Description")));
        descriptionTextArea.setText(Inter.getLocText("Collect-User_Information_DES"));
        UIScrollPane uiscrollpane = new UIScrollPane(descriptionTextArea);
        uiscrollpane.setBorder(null);
        jpanel1.add(uiscrollpane, "Center");
        jpanel.add(createControlButtonPane(), "South");
        setTitle(Inter.getLocText("Collect-Collect_User_Information"));
        setSize(480, 300);
        setModal(true);
        GUICoreUtils.centerWindow(this);
    }

    private void macSystemHit(JPanel jpanel)
    {
        if(OperatingSystem.isMacOS())
        {
            UITextArea uitextarea = new UITextArea();
            uitextarea.setText(Inter.getLocText("FR-Designer-Collect_OSXTips"));
            uitextarea.setEditable(false);
            jpanel.add(uitextarea, "South");
        }
    }

    protected void applyClosingAction()
    {
        addWindowListener(new WindowAdapter() {

            final CollectUserInformationDialog this$0;

            public void windowClosing(WindowEvent windowevent)
            {
                getKeyAction();
            }

            
            {
                this$0 = CollectUserInformationDialog.this;
                super();
            }
        }
);
    }

    private void getKeyAction()
    {
        Locale locale = FRContext.getLocale();
        String s = "http://www.finereport.com/en/frlogin";
        if(ComparatorUtils.equals(locale, Locale.TAIWAN))
            s = "http://www.finereport.com/tw/products/frlogin";
        if(ComparatorUtils.equals(locale, Locale.CHINA))
            s = "http://www.finereport.com/products/frlogin";
        if(ComparatorUtils.equals(locale, Locale.JAPAN))
            s = "http://www.finereport.com/jp/products/frlogin";
        try
        {
            Desktop.getDesktop().browse(new URI(s));
        }
        catch(Exception exception) { }
    }

    public String getKey()
    {
        return keyTextField.getText().trim();
    }

    protected JPanel createControlButtonPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        JPanel jpanel1 = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
        jpanel.add(jpanel1, "East");
        UIButton uibutton = new UIButton(Inter.getLocText("Collect-Use_Designer"));
        uibutton.setMnemonic('F');
        jpanel1.add(uibutton);
        uibutton.addActionListener(verifyActionListener);
        UIButton uibutton1 = new UIButton(Inter.getLocText("Utils-Exit_Designer"));
        uibutton1.setMnemonic('E');
        jpanel1.add(uibutton1);
        uibutton1.addActionListener(new ActionListener() {

            final CollectUserInformationDialog this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                System.exit(0);
            }

            
            {
                this$0 = CollectUserInformationDialog.this;
                super();
            }
        }
);
        getRootPane().setDefaultButton(uibutton);
        return jpanel;
    }

    public void checkValid()
        throws Exception
    {
    }

}
