// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style;

import com.fr.base.BaseUtils;
import com.fr.base.Utils;
import com.fr.chart.base.TextAttr;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIColorButton;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRFont;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;

public class ChartTextAttrPane extends BasicPane
{

    private static final long serialVersionUID = 0x5d6bbb3a8339c545L;
    protected UIComboBox fontNameComboBox;
    protected UIComboBox fontSizeComboBox;
    protected UIToggleButton bold;
    protected UIToggleButton italic;
    protected UIColorButton fontColor;
    public static Integer Font_Sizes[] = {
        new Integer(6), new Integer(8), new Integer(9), new Integer(10), new Integer(11), new Integer(12), new Integer(14), new Integer(16), new Integer(18), new Integer(20), 
        new Integer(22), new Integer(24), new Integer(26), new Integer(28), new Integer(36), new Integer(48), new Integer(72)
    };

    public ChartTextAttrPane()
    {
        initComponents();
    }

    public String title4PopupWindow()
    {
        return null;
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

    public void update(TextAttr textattr)
    {
        if(textattr == null)
            textattr = new TextAttr();
        FRFont frfont = textattr.getFRFont();
        frfont = updateFRFont();
        textattr.setFRFont(frfont);
    }

    public TextAttr update()
    {
        TextAttr textattr = new TextAttr();
        FRFont frfont = textattr.getFRFont();
        frfont = updateFRFont();
        textattr.setFRFont(frfont);
        return textattr;
    }

    public void populate(FRFont frfont)
    {
        if(frfont == null)
            return;
        fontNameComboBox.setSelectedItem(frfont.getFamily());
        bold.setSelected(frfont.isBold());
        italic.setSelected(frfont.isItalic());
        if(fontSizeComboBox != null)
            fontSizeComboBox.setSelectedItem(Integer.valueOf(frfont.getSize()));
        if(fontColor != null)
            fontColor.setColor(frfont.getForeground());
    }

    public FRFont updateFRFont()
    {
        byte byte0 = 0;
        if(bold.isSelected() && !italic.isSelected())
            byte0 = 1;
        else
        if(!bold.isSelected() && italic.isSelected())
            byte0 = 2;
        else
        if(bold.isSelected() && italic.isSelected())
            byte0 = 3;
        return FRFont.getInstance(Utils.objectToString(fontNameComboBox.getSelectedItem()), byte0, Float.valueOf(Utils.objectToString(fontSizeComboBox.getSelectedItem())).floatValue(), fontColor.getColor());
    }

    public void setEnabled(boolean flag)
    {
        fontNameComboBox.setEnabled(flag);
        fontSizeComboBox.setEnabled(flag);
        fontColor.setEnabled(flag);
        bold.setEnabled(flag);
        italic.setEnabled(flag);
    }

    protected void initComponents()
    {
        fontNameComboBox = new UIComboBox(Utils.getAvailableFontFamilyNames4Report());
        fontSizeComboBox = new UIComboBox(Font_Sizes);
        fontColor = new UIColorButton();
        bold = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/bold.png"));
        italic = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/italic.png"));
        double d = -2D;
        double d1 = -1D;
        Component acomponent[] = {
            fontColor, italic, bold
        };
        JPanel jpanel = new JPanel(new BorderLayout());
        jpanel.add(fontSizeComboBox, "Center");
        jpanel.add(GUICoreUtils.createFlowPane(acomponent, 0, 4), "East");
        double ad[] = {
            d1
        };
        double ad1[] = {
            d, d
        };
        Component acomponent1[][] = {
            {
                fontNameComboBox
            }, {
                jpanel
            }
        };
        JPanel jpanel1 = TableLayoutHelper.createTableLayoutPane(acomponent1, ad1, ad);
        setLayout(new BorderLayout());
        add(jpanel1, "Center");
        populate(FRFont.getInstance());
    }

}
