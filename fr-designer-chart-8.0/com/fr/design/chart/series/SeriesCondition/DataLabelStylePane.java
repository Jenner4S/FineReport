// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition;

import com.fr.base.Utils;
import com.fr.chart.base.AttrContents;
import com.fr.chart.base.TextAttr;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.style.FRFontPane;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import java.awt.Color;
import java.awt.Dimension;

public class DataLabelStylePane extends BasicPane
{

    private static final long serialVersionUID = 0x5dd9779232e4ab01L;
    private UIComboBox nameBox;
    private UIComboBox fontStyleBox;
    private UIComboBox sizeBox;
    private ColorSelectBox colorBox;

    public DataLabelStylePane()
    {
        initPane(true);
    }

    public DataLabelStylePane(boolean flag)
    {
        initPane(flag);
    }

    private void initPane(boolean flag)
    {
        setLayout(FRGUIPaneFactory.createBoxFlowLayout());
        add(nameBox = new UIComboBox(Utils.getAvailableFontFamilyNames4Report()));
        nameBox.setPreferredSize(new Dimension(80, 20));
        String as[] = {
            Inter.getLocText("FRFont-plain"), Inter.getLocText("FRFont-bold"), Inter.getLocText("FRFont-italic"), Inter.getLocText("FRFont-bolditalic")
        };
        add(fontStyleBox = new UIComboBox(as));
        fontStyleBox.setPreferredSize(new Dimension(80, 20));
        add(sizeBox = new UIComboBox(FRFontPane.Font_Sizes));
        sizeBox.setPreferredSize(new Dimension(80, 20));
        if(flag)
            add(colorBox = new ColorSelectBox(60));
        FRFont frfont = FRFont.getInstance();
        nameBox.setSelectedItem(frfont.getFontName());
        fontStyleBox.setSelectedIndex(frfont.getStyle());
        sizeBox.setSelectedItem(Integer.valueOf(frfont.getSize()));
        if(colorBox != null)
            colorBox.setSelectObject(frfont.getForeground());
    }

    protected String title4PopupWindow()
    {
        return "Label";
    }

    public void populate(AttrContents attrcontents)
    {
        if(attrcontents == null)
        {
            return;
        } else
        {
            populate(attrcontents.getTextAttr());
            return;
        }
    }

    public void populate(TextAttr textattr)
    {
        if(textattr == null)
        {
            return;
        } else
        {
            FRFont frfont = textattr.getFRFont();
            populate(frfont);
            return;
        }
    }

    public void populate(FRFont frfont)
    {
        if(frfont == null)
            return;
        nameBox.setSelectedItem(frfont.getFamily());
        fontStyleBox.setSelectedIndex(frfont.getStyle());
        sizeBox.setSelectedItem(Integer.valueOf(frfont.getSize()));
        if(colorBox != null)
            colorBox.setSelectObject(frfont.getForeground());
    }

    public void update(AttrContents attrcontents)
    {
        if(attrcontents.getTextAttr() == null)
            attrcontents.setTextAttr(new TextAttr());
        update(attrcontents.getTextAttr());
    }

    public void update(TextAttr textattr)
    {
        textattr.setFRFont(getInstanceFont());
    }

    public FRFont getInstanceFont()
    {
        float f = Utils.objectToNumber(sizeBox.getSelectedItem(), false).floatValue();
        String s = Utils.objectToString(nameBox.getSelectedItem());
        Color color = colorBox == null ? Color.black : colorBox.getSelectObject();
        return FRFont.getInstance(s, fontStyleBox.getSelectedIndex(), f, color);
    }
}
