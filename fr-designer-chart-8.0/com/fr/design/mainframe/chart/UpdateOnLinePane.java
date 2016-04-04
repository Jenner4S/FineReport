// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart;

import com.fr.design.ChartEnvManager;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.actions.ChartDownLoadWorker;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.ProductConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

public class UpdateOnLinePane extends BasicPane
{

    private static final int GAP = 40;
    private static final int H_GAP = 16;
    private static final int SIDE_GAP = 30;
    private static final int RIGHT_BORDER_GAP = 34;
    private static final Color LABEL_COLOR = new Color(114, 114, 114);
    private static final int MESSAGE_FONT_SIZE = 20;
    private static final int PUSH_FONT_SIZE = 12;
    private static final int PROGRESS_WIDTH = 500;
    private static final int PROGRESS_HEIGHT = 14;
    private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("##.##");
    private static final int PRECENT = 100;
    private static final Color FOREGROUNG = new Color(23, 190, 86);
    private static final Color BACKGROUND = new Color(210, 210, 210);
    String serverVersion;
    UIButton okButton;
    UIButton updateButton;
    UIButton cancleButton;
    UICheckBox pushAuto;
    private JPanel messagePane;
    private JPanel optionsPane;
    private BasicDialog parentDialog;
    private ChartDownLoadWorker downLoadWorker;
    private boolean isUpdateCancle;
    private ActionListener updateListener;
    private ActionListener okListener;
    private ActionListener cancleListener;

    public void setParentDialog(BasicDialog basicdialog)
    {
        parentDialog = basicdialog;
    }

    public UpdateOnLinePane(String s)
    {
        serverVersion = ProductConstants.RELEASE_VERSION;
        okButton = new UIButton(Inter.getLocText("FR-Chart-Dialog_OK"));
        updateButton = new UIButton(Inter.getLocText("FR-Chart-App_Update"));
        cancleButton = new UIButton(Inter.getLocText("FR-Chart-Dialog_Cancle"));
        pushAuto = new UICheckBox(Inter.getLocText("FR-Chart-UpdateMessage_PushAuto"));
        downLoadWorker = null;
        isUpdateCancle = false;
        updateListener = new ActionListener() {

            final UpdateOnLinePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                final JProgressBar progressBar = init4UpdatingPane();
                downLoadWorker = new ChartDownLoadWorker() {

                    final JProgressBar val$progressBar;
                    final _cls1 this$1;

                    protected void process(java.util.List list)
                    {
                        progressBar.setValue((int)(((Double)list.get(list.size() - 1)).doubleValue() * 100D));
                    }

                    public void done()
                    {
                        try
                        {
                            get();
                        }
                        catch(Exception exception)
                        {
                            init4UpdateFaild();
                            return;
                        }
                        if(!isUpdateCancle)
                        {
                            replaceFiles();
                            dialogExit();
                            super.done();
                        }
                    }

                    
                    {
                        this$1 = _cls1.this;
                        progressBar = jprogressbar;
                        super();
                    }
                }
;
                downLoadWorker.execute();
            }

            
            {
                this$0 = UpdateOnLinePane.this;
                super();
            }
        }
;
        okListener = new ActionListener() {

            final UpdateOnLinePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                dialogExit();
            }

            
            {
                this$0 = UpdateOnLinePane.this;
                super();
            }
        }
;
        cancleListener = new ActionListener() {

            final UpdateOnLinePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(downLoadWorker != null)
                {
                    isUpdateCancle = true;
                    downLoadWorker.cancel(true);
                }
                dialogExit();
            }

            
            {
                this$0 = UpdateOnLinePane.this;
                super();
            }
        }
;
        serverVersion = s;
        isUpdateCancle = false;
        pushAuto.setSelected(ChartEnvManager.getEnvManager().isPushUpdateAuto());
        pushAuto.addItemListener(new ItemListener() {

            final UpdateOnLinePane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                ChartEnvManager.getEnvManager().setPushUpdateAuto(pushAuto.isSelected());
            }

            
            {
                this$0 = UpdateOnLinePane.this;
                super();
            }
        }
);
        init4PanesLayout();
        initListeners();
        judge();
    }

    private void initListeners()
    {
        updateButton.addActionListener(updateListener);
        okButton.addActionListener(okListener);
        cancleButton.addActionListener(cancleListener);
    }

    private void init4PanesLayout()
    {
        setLayout(new BorderLayout());
        messagePane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        optionsPane = new JPanel(new FlowLayout(2, 16, 0));
        optionsPane.setBorder(new EmptyBorder(0, 0, 40, 34));
        add(messagePane, "Center");
        add(optionsPane, "South");
        pushAuto.setFont(new Font(Inter.getLocText("FR-Designer-All_MSBold"), 0, 12));
        pushAuto.setForeground(LABEL_COLOR);
        revalidate();
    }

    private void init4UpdateFaild()
    {
        messagePane.removeAll();
        UILabel uilabel = new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Chart-Version_UpdateFail")).append("!").toString());
        uilabel.setHorizontalAlignment(0);
        uilabel.setFont(new Font(Inter.getLocText("FR-Designer-All_MSBold"), 0, 20));
        uilabel.setForeground(LABEL_COLOR);
        messagePane.add(uilabel, "Center");
        optionsPane.removeAll();
        optionsPane.add(okButton);
        revalidate();
    }

    private JProgressBar init4UpdatingPane()
    {
        messagePane.removeAll();
        JPanel jpanel = new JPanel(new GridLayout(2, 1));
        UILabel uilabel = new UILabel(Inter.getLocText("FR-Chart-App_UpdateProgress"));
        uilabel.setHorizontalAlignment(0);
        uilabel.setFont(new Font(Inter.getLocText("FR-Designer-All_MSBold"), 0, 20));
        uilabel.setForeground(LABEL_COLOR);
        uilabel.setBorder(new EmptyBorder(12, 0, 0, 0));
        jpanel.add(uilabel);
        JProgressBar jprogressbar = new JProgressBar();
        jprogressbar.setMaximum(100);
        jprogressbar.setMinimum(0);
        jprogressbar.setValue(0);
        jprogressbar.setBorder(new EmptyBorder(20, 30, 60, 30));
        jpanel.add(jprogressbar);
        messagePane.add(jpanel, "Center");
        optionsPane.removeAll();
        optionsPane.add(cancleButton);
        revalidate();
        return jprogressbar;
    }

    private void init4VersionSamePane()
    {
        messagePane.removeAll();
        UILabel uilabel = new UILabel(Inter.getLocText("FR-Chart-Versions_Lasted"));
        uilabel.setHorizontalAlignment(0);
        uilabel.setFont(new Font(Inter.getLocText("FR-Designer-All_MSBold"), 0, 20));
        uilabel.setForeground(LABEL_COLOR);
        messagePane.add(uilabel, "Center");
        optionsPane.removeAll();
        optionsPane.add(pushAuto);
        optionsPane.add(okButton);
        revalidate();
    }

    private void init4VersionDifferentPane()
    {
        messagePane.removeAll();
        createPaneShowVersions();
        optionsPane.removeAll();
        optionsPane.add(pushAuto);
        optionsPane.add(updateButton);
        optionsPane.add(cancleButton);
        revalidate();
    }

    private void createPaneShowVersions()
    {
        JPanel jpanel = new JPanel(new GridLayout(2, 1));
        UILabel uilabel = new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Chart-Version_Local")).append(":").append(ProductConstants.RELEASE_VERSION).toString());
        uilabel.setFont(new Font(Inter.getLocText("FR-Designer-All_MSBold"), 0, 20));
        uilabel.setForeground(LABEL_COLOR);
        uilabel.setBorder(new EmptyBorder(12, 0, 0, 0));
        UILabel uilabel1 = new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Chart-Version_Lasted")).append(":").append(serverVersion).toString());
        uilabel1.setFont(new Font(Inter.getLocText("FR-Designer-All_MSBold"), 0, 20));
        uilabel1.setForeground(LABEL_COLOR);
        uilabel1.setBorder(new EmptyBorder(-32, 0, 0, 0));
        uilabel.setHorizontalAlignment(0);
        uilabel1.setHorizontalAlignment(0);
        jpanel.add(uilabel);
        jpanel.add(uilabel1);
        messagePane.add(jpanel, "Center");
    }

    private void judge()
    {
        if(ComparatorUtils.equals(ProductConstants.RELEASE_VERSION, serverVersion))
            init4VersionSamePane();
        else
            init4VersionDifferentPane();
    }

    private void dialogExit()
    {
        parentDialog.setVisible(false);
        parentDialog.dispose();
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("FR-Chart-Help_UpdateOnline");
    }








}
