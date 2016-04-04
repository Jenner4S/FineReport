// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.base.FRContext;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.EditingStringListPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.mainframe.DesignerContext;
import com.fr.file.FILE;
import com.fr.file.FILEChooserPane;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import com.fr.web.attr.ReportWebAttr;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class WebJsPane extends BasicPane
{

    private UITextField localText;
    private UITextField urlText;
    private UIRadioButton localFileRadioButton;
    private UIRadioButton urlFileRadioButton;
    private EditingStringListPane editingPane;
    UIButton chooseFile;
    UIButton testConnection;
    UILabel infor1;
    UILabel infor2;
    private ActionListener chooseFileListener;
    private ActionListener testConnectionListener;
    private ActionListener radioActionListener;
    private KeyListener urlTextListener;

    public WebJsPane()
    {
        chooseFileListener = new ActionListener() {

            final WebJsPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                FILEChooserPane filechooserpane = FILEChooserPane.getInstance(false, false, true, new ChooseFileFilter("js", (new StringBuilder()).append("javascript").append(Inter.getLocText("File")).toString()));
                if(filechooserpane.showOpenDialog(DesignerContext.getDesignerFrame()) == 0)
                {
                    FILE file = filechooserpane.getSelectedFILE();
                    if(file == null)
                        return;
                    String s = file.getName();
                    String s1 = s.substring(s.lastIndexOf(".") + 1);
                    if(!"js".equalsIgnoreCase(s1))
                        return;
                    localText.setText(file.getPath().substring(1));
                    editingPane.setAddEnabled(true);
                }
                filechooserpane.removeFILEFilter(new ChooseFileFilter("js"));
            }

            
            {
                this$0 = WebJsPane.this;
                super();
            }
        }
;
        testConnectionListener = new ActionListener() {

            final WebJsPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                String s = urlText.getText();
                if(!s.matches("^[a-zA-z]+://.+js"))
                {
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(WebJsPane.this), Inter.getLocText("Add_JS_warning"));
                    return;
                }
                InputStream inputstream = null;
                try
                {
                    URL url = new URL(urlText.getText());
                    URLConnection urlconnection = url.openConnection();
                    inputstream = urlconnection.getInputStream();
                }
                catch(Throwable throwable)
                {
                    FRContext.getLogger().error(throwable.getMessage(), throwable);
                }
                if(inputstream == null)
                {
                    JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("Datasource-Connection_failed"));
                } else
                {
                    JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("Datasource-Connection_successfully"));
                    try
                    {
                        inputstream.close();
                    }
                    catch(IOException ioexception)
                    {
                        inputstream = null;
                    }
                }
            }

            
            {
                this$0 = WebJsPane.this;
                super();
            }
        }
;
        radioActionListener = new ActionListener() {

            final WebJsPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(localFileRadioButton.isSelected())
                {
                    localRadioSelectAction();
                    urlFileRadioButton.setForeground(new Color(143, 142, 139));
                    localFileRadioButton.setForeground(Color.black);
                    infor1.setText(Inter.getLocText("JS_WARNING1"));
                    infor2.setText(" ");
                } else
                if(urlFileRadioButton.isSelected())
                {
                    urlRadioSelectAction();
                    localFileRadioButton.setForeground(new Color(143, 142, 139));
                    urlFileRadioButton.setForeground(Color.black);
                    infor2.setText(Inter.getLocText("JS_WARNING2"));
                    infor1.setText(" ");
                }
                if(StringUtils.isEmpty(urlText.getText()) && StringUtils.isEmpty(localText.getText()))
                    editingPane.setAddEnabled(false);
            }

            
            {
                this$0 = WebJsPane.this;
                super();
            }
        }
;
        urlTextListener = new KeyAdapter() {

            final WebJsPane this$0;

            public void keyReleased(KeyEvent keyevent)
            {
                String s = urlText.getText();
                if(s != null && s.matches("^[a-zA-z]+://.+js"))
                    editingPane.setAddEnabled(true);
            }

            
            {
                this$0 = WebJsPane.this;
                super();
            }
        }
;
        setLayout(new BorderLayout(0, 20));
        setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 0));
        localFileRadioButton = new UIRadioButton((new StringBuilder()).append(Inter.getLocText("Disk_File")).append(":").toString(), true);
        urlFileRadioButton = new UIRadioButton((new StringBuilder()).append(Inter.getLocText("Url_location")).append(":").toString(), false);
        ButtonGroup buttongroup = new ButtonGroup();
        buttongroup.add(localFileRadioButton);
        buttongroup.add(urlFileRadioButton);
        localFileRadioButton.addActionListener(radioActionListener);
        urlFileRadioButton.addActionListener(radioActionListener);
        urlFileRadioButton.setForeground(new Color(143, 142, 139));
        localFileRadioButton.setForeground(Color.black);
        localText = new UITextField();
        localText.setEditable(false);
        urlText = new UITextField();
        localText.setPreferredSize(new Dimension(450, 20));
        urlText.setPreferredSize(new Dimension(450, 20));
        urlText.addKeyListener(urlTextListener);
        urlText.setEnabled(false);
        chooseFile = new UIButton(Inter.getLocText("Selection"));
        chooseFile.addActionListener(chooseFileListener);
        testConnection = new UIButton(Inter.getLocText("Test_URL"));
        testConnection.setEnabled(false);
        testConnection.addActionListener(testConnectionListener);
        if(testConnection.getPreferredSize().width > chooseFile.getPreferredSize().width)
            chooseFile.setPreferredSize(testConnection.getPreferredSize());
        else
            testConnection.setPreferredSize(chooseFile.getPreferredSize());
        createNorthPane();
        createEditingPane();
    }

    private void createNorthPane()
    {
        JPanel jpanel = new JPanel(new BorderLayout(0, 5));
        JPanel jpanel1 = new JPanel(new BorderLayout(0, 5));
        JPanel jpanel2 = new JPanel(new FlowLayout(0, 7, 0));
        jpanel2.add(localFileRadioButton);
        jpanel2.add(localText);
        jpanel2.add(chooseFile);
        jpanel1.add(jpanel2, "North");
        infor1 = new UILabel(Inter.getLocText("JS_WARNING1"));
        infor1.setForeground(new Color(207, 42, 39));
        jpanel1.add(infor1, "Center");
        JPanel jpanel3 = new JPanel(new BorderLayout(0, 5));
        JPanel jpanel4 = new JPanel(new FlowLayout(0, 7, 0));
        jpanel4.add(urlFileRadioButton);
        jpanel4.add(urlText);
        jpanel4.add(testConnection);
        jpanel3.add(jpanel4, "North");
        infor2 = new UILabel(Inter.getLocText("JS_WARNING2"));
        infor2.setForeground(new Color(207, 42, 39));
        jpanel3.add(infor2, "Center");
        jpanel.add(jpanel1, "North");
        jpanel.add(jpanel3, "Center");
        add(jpanel, "North");
    }

    private void createEditingPane()
    {
        editingPane = new EditingStringListPane() {

            final WebJsPane this$0;

            protected String getAddOrEditString()
            {
                if(localFileRadioButton.isSelected())
                    return localText.getText();
                String s = urlText.getText();
                if(s.matches("^[a-zA-z]+://.+js"))
                {
                    return s;
                } else
                {
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(WebJsPane.this), Inter.getLocText("Add_JS_warning"));
                    return "";
                }
            }

            protected void selectedChanged(String s)
            {
                if(s == null)
                {
                    localFileRadioButton.doClick();
                    localText.setText("");
                    return;
                }
                if(s.matches("^[a-zA-z]+://.+js"))
                {
                    urlFileRadioButton.doClick();
                    urlText.setText(s);
                } else
                {
                    localFileRadioButton.doClick();
                    localText.setText(s);
                }
                checkEnableState();
            }

            
            {
                this$0 = WebJsPane.this;
                super();
            }
        }
;
        add(editingPane, "Center");
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("ReportServerP-Import_JavaScript");
    }

    private void localRadioSelectAction()
    {
        localFileRadioButton.setSelected(true);
        chooseFile.setEnabled(true);
        localText.setEnabled(true);
        urlText.setText("");
        urlText.setEnabled(false);
        testConnection.setEnabled(false);
    }

    private void urlRadioSelectAction()
    {
        urlFileRadioButton.setSelected(true);
        testConnection.setEnabled(true);
        urlText.setEnabled(true);
        localText.setText("");
        localText.setEnabled(false);
        chooseFile.setEnabled(false);
    }

    public void populate(ReportWebAttr reportwebattr)
    {
        if(reportwebattr == null)
        {
            editingPane.populateBean(new ArrayList());
            return;
        }
        ArrayList arraylist = new ArrayList();
        for(int i = 0; i < reportwebattr.getJSImportCount(); i++)
            if(StringUtils.isNotBlank(reportwebattr.getJSImport(i)))
                arraylist.add(reportwebattr.getJSImport(i));

        editingPane.populateBean(arraylist);
    }

    public void update(ReportWebAttr reportwebattr)
    {
        java.util.List list = editingPane.updateBean();
        reportwebattr.clearJSImportList();
        for(int i = 0; i < list.size(); i++)
        {
            String s = (String)list.get(i);
            reportwebattr.addJSImport(s);
        }

    }







}
