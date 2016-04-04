// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style;

import com.fr.base.BaseUtils;
import com.fr.base.Utils;
import com.fr.design.gui.ibutton.UIColorButton;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRFont;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style:
//            ChartTextAttrPane

public class ChartTextAttrNoColorPane extends ChartTextAttrPane
{

    public ChartTextAttrNoColorPane()
    {
    }

    protected void initComponents()
    {
        fontColor = new UIColorButton();
        fontNameComboBox = new UIComboBox(Utils.getAvailableFontFamilyNames4Report());
        fontSizeComboBox = new UIComboBox(Font_Sizes);
        bold = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/bold.png"));
        italic = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/italic.png"));
        double d = -2D;
        double d1 = -1D;
        Component acomponent[] = {
            italic, bold
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
        return FRFont.getInstance(Utils.objectToString(fontNameComboBox.getSelectedItem()), byte0, Float.valueOf(Utils.objectToString(fontSizeComboBox.getSelectedItem())).floatValue());
    }
}
