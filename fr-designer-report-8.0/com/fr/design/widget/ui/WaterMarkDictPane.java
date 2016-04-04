// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.WaterMark;
import com.fr.general.Inter;
import java.awt.event.KeyListener;
import javax.swing.JPanel;

public class WaterMarkDictPane extends JPanel
{

    private UITextField waterMarkTextField;

    public WaterMarkDictPane()
    {
        setLayout(FRGUIPaneFactory.createLabelFlowLayout());
        add(new UILabel((new StringBuilder()).append(Inter.getLocText("WaterMark")).append(":").toString()));
        waterMarkTextField = new UITextField(16);
        add(waterMarkTextField);
    }

    public void populate(WaterMark watermark)
    {
        waterMarkTextField.setText(watermark.getWaterMark());
    }

    public void addInputKeyListener(KeyListener keylistener)
    {
        waterMarkTextField.addKeyListener(keylistener);
    }

    public void removeInputKeyListener(KeyListener keylistener)
    {
        waterMarkTextField.removeKeyListener(keylistener);
    }

    public void update(WaterMark watermark)
    {
        watermark.setWaterMark(waterMarkTextField.getText());
    }

    public void setWaterMark(String s)
    {
        waterMarkTextField.setText(s);
    }

    public String getWaterMark()
    {
        return waterMarkTextField.getText();
    }
}
