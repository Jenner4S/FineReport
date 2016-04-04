// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui.btn;

import com.fr.base.background.ImageBackground;
import com.fr.design.dialog.*;
import com.fr.design.gui.frpane.ImgChoosePane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.FreeButton;
import com.fr.general.Background;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class ButtonSytleDefinedPane extends BasicPane
{
    class BackgroundPane extends JPanel
    {

        private UIButton editButton;
        private ImgChoosePane choosePane;
        private Background background;
        private UILabel ImagePreviewPane;
        final ButtonSytleDefinedPane this$0;

        public void populate(Background background1)
        {
            background = background1;
            if((background1 instanceof ImageBackground) && ((ImageBackground)background1).getImage() != null)
                ImagePreviewPane.setIcon(new ImageIcon(((ImageBackground)background1).getImage()));
            else
                ImagePreviewPane.setIcon(null);
        }

        public ImageBackground update()
        {
            return (ImageBackground)background;
        }




        BackgroundPane(String s, String s1)
        {
            this$0 = ButtonSytleDefinedPane.this;
            super();
            setLayout(FRGUIPaneFactory.createLabelFlowLayout());
            setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 150));
            UILabel uilabel = new UILabel(s);
            uilabel.setToolTipText(s1);
            uilabel.setPreferredSize(new Dimension(100, 20));
            add(uilabel);
            ImagePreviewPane = new UILabel();
            ImagePreviewPane.setPreferredSize(new Dimension(100, 20));
            add(ImagePreviewPane);
            editButton = new UIButton(Inter.getLocText("Edit"));
            editButton.addActionListener(new ActionListener() {

                final ButtonSytleDefinedPane val$this$0;
                final BackgroundPane this$1;

                public void actionPerformed(ActionEvent actionevent)
                {
                    if(choosePane == null)
                        choosePane = new ImgChoosePane();
                    BasicDialog basicdialog = choosePane.showWindow(SwingUtilities.getWindowAncestor(_fld0));
                    basicdialog.addDialogActionListener(new DialogActionAdapter() {

                        final _cls1 this$2;

                        public void doOk()
                        {
                            populate(choosePane.update());
                        }

                        
                        {
                            this$2 = _cls1.this;
                            super();
                        }
                    }
);
                    choosePane.populate((ImageBackground)background);
                    basicdialog.setVisible(true);
                }


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            add(editButton);
        }
    }


    private BackgroundPane initBackgroundPane;
    private BackgroundPane overBackgroundPane;
    private BackgroundPane clickBackgroundPane;
    private Background initBackground;
    private Background overBackground;
    private Background clickBackground;

    public ButtonSytleDefinedPane()
    {
        initComponents();
    }

    protected void initComponents()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = new JPanel();
        jpanel.setLayout(new BorderLayout());
        initBackgroundPane = new BackgroundPane((new StringBuilder()).append(Inter.getLocText("Background-Initial")).append(":").toString(), Inter.getLocText("The_initial_background_of_the_button"));
        overBackgroundPane = new BackgroundPane((new StringBuilder()).append(Inter.getLocText("Background-Over")).append(":").toString(), Inter.getLocText("Mouse_move-background"));
        clickBackgroundPane = new BackgroundPane((new StringBuilder()).append(Inter.getLocText("Background-Click")).append(":").toString(), Inter.getLocText("Mouse_move-background"));
        JPanel jpanel1 = FRGUIPaneFactory.createYBoxEmptyBorderPane();
        jpanel1.setBorder(new TitledBorder(Inter.getLocText(new String[] {
            "Custom", "Form-Button", "Style"
        })));
        jpanel1.add(initBackgroundPane);
        jpanel1.add(overBackgroundPane);
        jpanel1.add(clickBackgroundPane);
        jpanel.add(jpanel1, "West");
        add(jpanel, "Center");
    }

    public void populate(FreeButton freebutton)
    {
        if(freebutton == null)
        {
            return;
        } else
        {
            initBackgroundPane.populate(freebutton.getInitialBackground());
            overBackgroundPane.populate(freebutton.getOverBackground());
            clickBackgroundPane.populate(freebutton.getClickBackground());
            return;
        }
    }

    public FreeButton update(FreeButton freebutton)
    {
        freebutton.setCustomStyle(true);
        freebutton.setInitialBackground(initBackgroundPane.update());
        freebutton.setOverBackground(overBackgroundPane.update());
        freebutton.setClickBackground(clickBackgroundPane.update());
        return freebutton;
    }

    protected String title4PopupWindow()
    {
        return null;
    }
}
