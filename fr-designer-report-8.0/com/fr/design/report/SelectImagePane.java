// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.style.background.image.ImageFileChooser;
import com.fr.design.style.background.image.ImagePreviewPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.report.cell.Elem;
import com.fr.report.cell.cellattr.CellImage;
import com.fr.stable.CoreGraphHelper;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;

public class SelectImagePane extends BasicPane
{

    private ImagePreviewPane previewPane;
    private ImageFileChooser imageFileChooser;
    private UIRadioButton defaultRadioButton;
    private UIRadioButton tiledRadioButton;
    private UIRadioButton extendRadioButton;
    private UIRadioButton adjustRadioButton;
    private Style imageStyle;
    private Image previewImage;
    private File imageFile;
    ActionListener selectPictureActionListener;
    ActionListener layoutActionListener;

    public SelectImagePane()
    {
        previewPane = null;
        imageFileChooser = null;
        defaultRadioButton = null;
        tiledRadioButton = null;
        extendRadioButton = null;
        adjustRadioButton = null;
        imageStyle = null;
        previewImage = null;
        selectPictureActionListener = new ActionListener() {

            final SelectImagePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                int i = imageFileChooser.showOpenDialog(SelectImagePane.this);
                if(i != 1)
                {
                    File file = imageFileChooser.getSelectedFile();
                    if(file != null && file.isFile())
                    {
                        java.awt.image.BufferedImage bufferedimage = BaseUtils.readImage(file.getPath());
                        CoreGraphHelper.waitForImage(bufferedimage);
                        imageFile = file;
                        setImageStyle();
                        previewPane.setImage(bufferedimage);
                        previewPane.setImageStyle(imageStyle);
                        previewImage = bufferedimage;
                    } else
                    {
                        previewPane.setImage(null);
                    }
                    previewPane.repaint();
                }
            }

            
            {
                this$0 = SelectImagePane.this;
                super();
            }
        }
;
        layoutActionListener = new ActionListener() {

            final SelectImagePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                setImageStyle();
                changeImageStyle();
            }

            
            {
                this$0 = SelectImagePane.this;
                super();
            }
        }
;
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        add(jpanel, "Center");
        JPanel jpanel1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel.add(jpanel1, "Center");
        jpanel1.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("Preview"), null));
        previewPane = new ImagePreviewPane();
        jpanel1.add(new JScrollPane(previewPane));
        JPanel jpanel2 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel.add(jpanel2, "East");
        jpanel2.setBorder(BorderFactory.createEmptyBorder(8, 2, 4, 0));
        UIButton uibutton = new UIButton(Inter.getLocText("Image-Select_Picture"));
        jpanel2.add(uibutton, "North");
        uibutton.setMnemonic('S');
        uibutton.addActionListener(selectPictureActionListener);
        JPanel jpanel3 = FRGUIPaneFactory.createMediumHGapHighTopFlowInnerContainer_M_Pane();
        jpanel2.add(jpanel3, "Center");
        defaultRadioButton = new UIRadioButton(Inter.getLocText("Default"));
        tiledRadioButton = new UIRadioButton(Inter.getLocText("Image-Titled"));
        extendRadioButton = new UIRadioButton(Inter.getLocText("Image-Extend"));
        adjustRadioButton = new UIRadioButton(Inter.getLocText("Image-Adjust"));
        defaultRadioButton.addActionListener(layoutActionListener);
        tiledRadioButton.addActionListener(layoutActionListener);
        extendRadioButton.addActionListener(layoutActionListener);
        adjustRadioButton.addActionListener(layoutActionListener);
        JPanel jpanel4 = new JPanel(new GridLayout(4, 1, 15, 15));
        jpanel4.add(defaultRadioButton);
        jpanel4.add(tiledRadioButton);
        jpanel4.add(extendRadioButton);
        jpanel4.add(adjustRadioButton);
        jpanel3.add(jpanel4);
        ButtonGroup buttongroup = new ButtonGroup();
        buttongroup.add(defaultRadioButton);
        buttongroup.add(tiledRadioButton);
        buttongroup.add(extendRadioButton);
        buttongroup.add(adjustRadioButton);
        defaultRadioButton.setSelected(true);
        imageFileChooser = new ImageFileChooser();
        imageFileChooser.setMultiSelectionEnabled(false);
    }

    private void changeImageStyle()
    {
        previewPane.setImageStyle(imageStyle);
        previewPane.repaint();
    }

    private void setImageStyle()
    {
        if(tiledRadioButton.isSelected())
            imageStyle = Style.DEFAULT_STYLE.deriveImageLayout(0);
        else
        if(adjustRadioButton.isSelected())
            imageStyle = Style.DEFAULT_STYLE.deriveImageLayout(4);
        else
        if(extendRadioButton.isSelected())
            imageStyle = Style.DEFAULT_STYLE.deriveImageLayout(2);
        else
            imageStyle = Style.DEFAULT_STYLE.deriveImageLayout(1);
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("Image");
    }

    public void populate(Elem elem)
    {
        Style style = null;
        if(elem != null)
        {
            Object obj = elem.getValue();
            if(obj != null && (obj instanceof Image))
            {
                Image image = (Image)obj;
                previewPane.setImage(image);
                previewImage = image;
            }
            style = elem.getStyle();
        }
        if(style.getImageLayout() == 0)
            tiledRadioButton.setSelected(true);
        else
        if(style.getImageLayout() == 2)
            extendRadioButton.setSelected(true);
        else
        if(style.getImageLayout() == 4)
        {
            adjustRadioButton.setSelected(true);
        } else
        {
            style.deriveImageLayout(1);
            defaultRadioButton.setSelected(true);
        }
        imageStyle = style;
        changeImageStyle();
    }

    public CellImage update()
    {
        CellImage cellimage = new CellImage();
        cellimage.setImage(previewPane.getImage());
        cellimage.setStyle(imageStyle);
        return cellimage;
    }

    public File getSelectedImage()
    {
        return imageFile;
    }







}
