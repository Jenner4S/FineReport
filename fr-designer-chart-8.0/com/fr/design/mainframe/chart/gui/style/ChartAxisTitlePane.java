// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style;

import com.fr.base.*;
import com.fr.chart.base.TextAttr;
import com.fr.chart.chartattr.Axis;
import com.fr.chart.chartattr.Title;
import com.fr.design.dialog.BasicPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.stable.StableUtils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style:
//            ChartTextAttrPane

public class ChartAxisTitlePane extends BasicPane
{

    private static final long serialVersionUID = 0xb68d76d4e763fd28L;
    private UICheckBox isAxisTitleVisable;
    private TinyFormulaPane axisTitleContentPane;
    private ChartTextAttrPane axisTitleAttrPane;
    private UIButtonGroup titleAlignmentPane;
    private JPanel titlePane;

    public ChartAxisTitlePane()
    {
        initComponents();
    }

    private void initComponents()
    {
        isAxisTitleVisable = new UICheckBox(Inter.getLocText("Axis_Title"));
        axisTitleContentPane = new TinyFormulaPane();
        axisTitleAttrPane = new ChartTextAttrPane();
        axisTitleAttrPane.populate(FRFont.getInstance("Microsoft YaHei", 0, 9F));
        Icon aicon[] = {
            BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_left_normal.png"), BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_center_normal.png"), BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_right_normal.png")
        };
        Integer ainteger[] = {
            Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(4)
        };
        titleAlignmentPane = new UIButtonGroup(aicon, ainteger);
        titleAlignmentPane.setSelectedItem(Integer.valueOf(0));
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            46D, d1
        };
        double ad1[] = {
            d, d, d, d
        };
        Component acomponent[][] = {
            {
                null, axisTitleContentPane
            }, {
                null, axisTitleAttrPane
            }, {
                null, new UILabel(Inter.getLocText("Alignment-Style"))
            }, {
                null, titleAlignmentPane
            }
        };
        titlePane = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        isAxisTitleVisable.addActionListener(new ActionListener() {

            final ChartAxisTitlePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                checkTitleUse();
            }

            
            {
                this$0 = ChartAxisTitlePane.this;
                super();
            }
        }
);
        double ad2[] = {
            d, d
        };
        double ad3[] = {
            d1
        };
        setLayout(new BorderLayout());
        JPanel jpanel = TableLayoutHelper.createTableLayoutPane(new Component[][] {
            new Component[] {
                isAxisTitleVisable
            }, new Component[] {
                titlePane
            }
        }, ad2, ad3);
        add(jpanel, "Center");
    }

    private void checkTitleUse()
    {
        isAxisTitleVisable.setEnabled(true);
        titlePane.setVisible(isAxisTitleVisable.isSelected());
    }

    protected String title4PopupWindow()
    {
        return "";
    }

    public Dimension getPreferredSize()
    {
        if(isAxisTitleVisable.isSelected())
            return super.getPreferredSize();
        else
            return isAxisTitleVisable.getPreferredSize();
    }

    public void update(Axis axis)
    {
        if(isAxisTitleVisable.isSelected())
        {
            if(axis.getTitle() == null)
            {
                Title title = new Title(Inter.getLocText(new String[] {
                    "ChartF-Axis", "ChartF-Title"
                }));
                axis.setTitle(title);
                axisTitleContentPane.populateBean(Utils.objectToString(title.getTextObject()));
                axisTitleAttrPane.populate(FRFont.getInstance("Microsoft YaHei", 0, 9F));
            }
            Title title1 = axis.getTitle();
            title1.setTitleVisible(true);
            if(axis.getPosition() == 2 || axis.getPosition() == 4)
                title1.getTextAttr().setAlignText(1);
            if(StableUtils.canBeFormula(axisTitleContentPane.updateBean()))
                title1.setTextObject(new Formula(axisTitleContentPane.updateBean()));
            else
                title1.setTextObject(axisTitleContentPane.updateBean());
            axisTitleAttrPane.update(title1.getTextAttr());
            title1.setPosition(((Integer)titleAlignmentPane.getSelectedItem()).intValue());
        } else
        if(axis.getTitle() != null)
            axis.getTitle().setTitleVisible(false);
        makeTitleAlignText(axis);
    }

    private void makeTitleAlignText(Axis axis)
    {
        if(axis.getPosition() == 2 || axis.getPosition() == 4)
        {
            Title title = axis.getTitle();
            if(title != null)
                title.getTextAttr().setAlignText(1);
        }
    }

    public void populate(Axis axis)
    {
        isAxisTitleVisable.setSelected(axis.getTitle() != null && axis.getTitle().isTitleVisible());
        if(isAxisTitleVisable.isSelected())
        {
            axisTitleContentPane.populateBean(Utils.objectToString(axis.getTitle().getTextObject()));
            axisTitleAttrPane.populate(axis.getTitle().getTextAttr());
            titleAlignmentPane.setSelectedItem(Integer.valueOf(axis.getTitle().getPosition()));
        }
        checkTitleUse();
    }

}
