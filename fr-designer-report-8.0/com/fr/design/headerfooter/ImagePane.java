// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.headerfooter;

import com.fr.base.BaseUtils;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.style.background.image.ImageFileChooser;
import com.fr.design.style.background.image.ImagePreviewPane;
import com.fr.general.Inter;
import com.fr.stable.CoreGraphHelper;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;

public class ImagePane extends BasicPane
{

    private ImagePreviewPane imagePreviewPane;
    private ImageFileChooser imageFileChooser;
    ActionListener selectPictureActionListener = new ActionListener() {

        final ImagePane this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            int i = imageFileChooser.showOpenDialog(ImagePane.this);
            if(i != 1)
            {
                File file = imageFileChooser.getSelectedFile();
                if(file != null && file.isFile())
                {
                    java.awt.image.BufferedImage bufferedimage = BaseUtils.readImage(file.getPath());
                    CoreGraphHelper.waitForImage(bufferedimage);
                    imagePreviewPane.setImage(bufferedimage);
                } else
                {
                    imagePreviewPane.setImage(null);
                }
                imagePreviewPane.repaint();
            }
        }

            
            {
                this$0 = ImagePane.this;
                super();
            }
    }
;

    public ImagePane()
    {
        this(true);
    }

    public ImagePane(boolean flag)
    {
        imageFileChooser = null;
        setLayout(FRGUIPaneFactory.createM_BorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        add(jpanel);
        if(flag)
            jpanel.setBorder(BorderFactory.createTitledBorder(Inter.getLocText("Preview")));
        imagePreviewPane = new ImagePreviewPane();
        jpanel.add(new JScrollPane(imagePreviewPane));
        JPanel jpanel1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        add(jpanel1, "East");
        if(flag)
            jpanel1.setBorder(BorderFactory.createEmptyBorder(8, 2, 0, 2));
        else
            jpanel1.setBorder(BorderFactory.createEmptyBorder(4, 2, 0, 2));
        UIButton uibutton = new UIButton(Inter.getLocText("Image-Select_Picture"));
        jpanel1.add(uibutton, "North");
        uibutton.addActionListener(selectPictureActionListener);
        imageFileChooser = new ImageFileChooser();
        imageFileChooser.setMultiSelectionEnabled(false);
    }

    protected String title4PopupWindow()
    {
        return "image";
    }

    public void populate(Image image)
    {
        if(image == null)
        {
            return;
        } else
        {
            imagePreviewPane.setImage(image);
            return;
        }
    }

    public Image update()
    {
        return imagePreviewPane.getImage();
    }


}
