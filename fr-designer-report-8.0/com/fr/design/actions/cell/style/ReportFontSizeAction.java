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
import java.util.Vector;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.actions.cell.style:
//            AbstractStyleAction

public class ReportFontSizeAction extends AbstractStyleAction
{

    private static final int MAX_FONT_SIZE = 100;

    public ReportFontSizeAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setName(Inter.getLocText("FRFont-Size"));
    }

    public Style executeStyle(Style style, Style style1)
    {
        Object obj = getValue(com/fr/design/gui/icombobox/UIComboBox.getName());
        if(obj != null && (obj instanceof UIComboBox))
        {
            Object obj1 = ((UIComboBox)obj).getSelectedItem();
            float f = Utils.round5(Float.parseFloat(obj1.toString()));
            if(style.getFRFont().getSize() == style1.getFRFont().getSize())
                style = StyleUtils.setReportFontSize(style, style1.getFRFont().getSize());
            if(f == (float)style.getFRFont().getSize())
                return style;
            style = StyleUtils.setReportFontSize(style, f);
        }
        return style;
    }

    public void setFontSize(float f)
    {
        Object obj = getValue(com/fr/design/gui/icombobox/UIComboBox.getName());
        if(obj != null && (obj instanceof UIComboBox))
        {
            UIComboBox uicombobox = (UIComboBox)obj;
            if(ComparatorUtils.equals(uicombobox.getSelectedItem(), Float.valueOf(f)))
                return;
            uicombobox.removeActionListener(this);
            uicombobox.setSelectedItem((new StringBuilder()).append(f).append("").toString());
            uicombobox.addActionListener(this);
        }
    }

    public JComponent createToolBarComponent()
    {
        Object obj = getValue(com/fr/design/gui/icombobox/UIComboBox.getName());
        if(obj == null || !(obj instanceof UIComboBox))
        {
            Vector vector = new Vector();
            for(int i = 1; i < 100; i++)
                vector.add(Integer.valueOf(i));

            UIComboBox uicombobox = new UIComboBox(vector);
            putValue(com/fr/design/gui/icombobox/UIComboBox.getName(), uicombobox);
            uicombobox.setMinimumSize(new Dimension(50, 20));
            uicombobox.setPreferredSize(new Dimension(50, 20));
            uicombobox.setEnabled(isEnabled());
            uicombobox.addActionListener(this);
            uicombobox.setEditable(true);
            return uicombobox;
        } else
        {
            return (JComponent)obj;
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
            setFontSize(Utils.round5(frfont.getSize2D()));
            return;
        }
    }
}
