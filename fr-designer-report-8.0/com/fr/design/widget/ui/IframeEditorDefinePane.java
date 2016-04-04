// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.IframeEditor;
import com.fr.general.Inter;
import com.fr.stable.ParameterProvider;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.widget.ui:
//            AbstractDataModify

public class IframeEditorDefinePane extends AbstractDataModify
{

    private UITextField srcTextField;
    private ReportletParameterViewPane parameterViewPane;
    private UICheckBox horizontalCheck;
    private UICheckBox verticalCheck;

    public IframeEditorDefinePane()
    {
        initComponents();
    }

    private void initComponents()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
        jpanel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
        JPanel jpanel1 = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
        jpanel1.add(horizontalCheck = new UICheckBox(Inter.getLocText("Preference-Horizontal_Scroll_Bar_Visible")));
        jpanel1.add(verticalCheck = new UICheckBox(Inter.getLocText("Preference-Vertical_Scroll_Bar_Visible")));
        jpanel.add(jpanel1);
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, -2D
        };
        double ad1[] = {
            d, d1
        };
        Component acomponent[][] = {
            {
                new UILabel((new StringBuilder()).append(Inter.getLocText("Form-Url")).append(":").toString()), srcTextField = new UITextField()
            }, {
                new UILabel((new StringBuilder()).append(Inter.getLocText("Parameter")).append(":").toString()), parameterViewPane = new ReportletParameterViewPane()
            }
        };
        parameterViewPane.setPreferredSize(new Dimension(400, 256));
        JPanel jpanel2 = TableLayoutHelper.createTableLayoutPane(acomponent, ad, ad1);
        jpanel.add(jpanel2);
        add(jpanel, "Center");
    }

    protected String title4PopupWindow()
    {
        return "iframe";
    }

    public void populateBean(IframeEditor iframeeditor)
    {
        srcTextField.setText(iframeeditor.getSrc());
        parameterViewPane.populate(iframeeditor.getParameters());
        horizontalCheck.setSelected(iframeeditor.isOverflowx());
        verticalCheck.setSelected(iframeeditor.isOverflowy());
    }

    public IframeEditor updateBean()
    {
        IframeEditor iframeeditor = new IframeEditor();
        iframeeditor.setSrc(srcTextField.getText());
        java.util.List list = parameterViewPane.update();
        iframeeditor.setParameters((ParameterProvider[])list.toArray(new ParameterProvider[list.size()]));
        iframeeditor.setOverflowx(horizontalCheck.isSelected());
        iframeeditor.setOverflowy(verticalCheck.isSelected());
        return iframeeditor;
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((IframeEditor)obj);
    }
}
