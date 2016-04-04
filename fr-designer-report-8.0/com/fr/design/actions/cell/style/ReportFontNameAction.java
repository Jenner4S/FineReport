// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.cell.style;

import com.fr.base.Style;
import com.fr.base.Utils;
import com.fr.base.core.StyleUtils;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.*;
import java.awt.Dimension;
import javax.swing.ComboBoxModel;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.actions.cell.style:
//            AbstractStyleAction

public class ReportFontNameAction extends AbstractStyleAction
{

    public ReportFontNameAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setName(Inter.getLocText("FRFont-Family"));
    }

    public Style executeStyle(Style style, Style style1)
    {
        Object obj = getValue(com/fr/design/gui/icombobox/UIComboBox.getName());
        if(obj != null && (obj instanceof UIComboBox))
        {
            String s = (String)((UIComboBox)obj).getSelectedItem();
            if(ComparatorUtils.equals(style.getFRFont().getName(), style1.getFRFont().getName()))
                style = StyleUtils.setReportFontName(style, style1.getFRFont().getName());
            if(ComparatorUtils.equals(s, style.getFRFont().getName()))
                return style;
            style = StyleUtils.setReportFontName(style, s);
        }
        return style;
    }

    public void setFontName(String s)
    {
        Object obj = getValue(com/fr/design/gui/icombobox/UIComboBox.getName());
        if(obj != null && (obj instanceof UIComboBox))
        {
            UIComboBox uicombobox = (UIComboBox)obj;
            ComboBoxModel comboboxmodel = uicombobox.getModel();
            int i = 0;
            do
            {
                if(i >= comboboxmodel.getSize())
                    break;
                Object obj1 = comboboxmodel.getElementAt(i);
                if(ComparatorUtils.equals(obj1, s))
                {
                    uicombobox.removeActionListener(this);
                    uicombobox.setSelectedIndex(i);
                    uicombobox.addActionListener(this);
                    break;
                }
                i++;
            } while(true);
        }
    }

    public JComponent createToolBarComponent()
    {
        Object obj = getValue(com/fr/design/gui/icombobox/UIComboBox.getName());
        if(obj == null || !(obj instanceof UIComboBox))
        {
            UIComboBox uicombobox = new UIComboBox(Utils.getAvailableFontFamilyNames4Report());
            putValue(com/fr/design/gui/icombobox/UIComboBox.getName(), uicombobox);
            uicombobox.setPreferredSize(new Dimension(Math.min(140, uicombobox.getPreferredSize().width), uicombobox.getPreferredSize().height));
            uicombobox.setEnabled(isEnabled());
            uicombobox.addActionListener(this);
            return uicombobox;
        } else
        {
            return (UIComboBox)obj;
        }
    }

    public void updateStyle(Style style)
    {
        if(style == null)
            return;
        FRFont frfont = style.getFRFont();
        if(frfont == null)
        {
            return;
        } else
        {
            setFontName(frfont.getFamily());
            return;
        }
    }
}
