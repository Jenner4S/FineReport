// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.ui.NoneWidget;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.widget.ui:
//            ParameterTreeComboBox

public class BasicWidgetPropertySettingPane extends BasicPane
{

    private ParameterTreeComboBox widgetNameComboBox;
    private UICheckBox enableCheckBox;
    private UICheckBox visibleCheckBox;
    private Widget widget;

    public BasicWidgetPropertySettingPane()
    {
        setLayout(FRGUIPaneFactory.createLabelFlowLayout());
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel.setBorder(BorderFactory.createEmptyBorder(0, -2, 0, 0));
        add(jpanel);
        JPanel jpanel1 = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
        enableCheckBox = new UICheckBox(Inter.getLocText("Enabled"), true);
        jpanel1.add(enableCheckBox);
        visibleCheckBox = new UICheckBox(Inter.getLocText("Widget-Visible"), true);
        jpanel1.add(visibleCheckBox);
        jpanel.add(jpanel1, "North");
        JPanel jpanel2 = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
        widgetNameComboBox = new ParameterTreeComboBox();
        widgetNameComboBox.refreshTree();
        jpanel2.add(new UILabel((new StringBuilder()).append(Inter.getLocText("Form-Widget_Name")).append(":").toString()));
        jpanel2.add(widgetNameComboBox);
        jpanel.add(jpanel2, "Center");
    }

    protected String title4PopupWindow()
    {
        return "property";
    }

    public void populate(Widget widget1)
    {
        if(widget1 instanceof NoneWidget)
        {
            widget = null;
            GUICoreUtils.setEnabled(this, false);
            return;
        } else
        {
            GUICoreUtils.setEnabled(this, true);
            widget = widget1;
            widgetNameComboBox.setSelectedItem(widget1.getWidgetName());
            enableCheckBox.setSelected(widget.isEnabled());
            visibleCheckBox.setSelected(widget.isVisible());
            return;
        }
    }

    public void update(Widget widget1)
    {
        if(widget == null)
        {
            return;
        } else
        {
            widget1.setWidgetName(widgetNameComboBox.getSelectedParameterName());
            widget1.setEnabled(enableCheckBox.isSelected());
            widget1.setVisible(visibleCheckBox.isSelected());
            return;
        }
    }
}
