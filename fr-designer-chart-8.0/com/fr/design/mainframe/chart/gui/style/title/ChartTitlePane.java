// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.title;

import com.fr.base.*;
import com.fr.chart.base.TextAttr;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Title;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.style.*;
import com.fr.general.Inter;
import com.fr.stable.StableUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ChartTitlePane extends BasicScrollPane
{
    private class ContentPane extends JPanel
    {

        private static final long serialVersionUID = 0xa66919fceeb528f9L;
        final ChartTitlePane this$0;

        private void initComponents()
        {
            isTitleVisable = new UICheckBox(Inter.getLocText("Chart_Title_Is_Visible"));
            titlePane = createTitlePane();
            double d = -2D;
            double d1 = -1D;
            double ad[] = {
                d1
            };
            double ad1[] = {
                d, d
            };
            Component acomponent[][] = {
                {
                    isTitleVisable
                }, {
                    titlePane
                }
            };
            JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
            setLayout(new BorderLayout());
            add(jpanel, "Center");
            isTitleVisable.addActionListener(new ActionListener() {

                final ContentPane this$1;

                public void actionPerformed(ActionEvent actionevent)
                {
                    checkTitlePaneUse();
                }

                
                {
                    this$1 = ContentPane.this;
                    super();
                }
            }
);
        }

        public ContentPane()
        {
            this$0 = ChartTitlePane.this;
            super();
            initComponents();
        }
    }


    private static final long serialVersionUID = 0x4fc821066d9919f2L;
    private UICheckBox isTitleVisable;
    private TinyFormulaPane titleContent;
    private ChartTextAttrPane textAttrPane;
    private UIButtonGroup alignmentPane;
    private JPanel chartDefaultAttrPane;
    private ChartBorderPane borderPane;
    private ChartBackgroundNoImagePane backgroundPane;
    private JPanel titlePane;

    public ChartTitlePane()
    {
    }

    private JPanel createTitlePane()
    {
        borderPane = new ChartBorderPane();
        backgroundPane = new ChartBackgroundNoImagePane();
        chartDefaultAttrPane = createDefaultAttrPane();
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d, d, d
        };
        Component acomponent[][] = {
            {
                chartDefaultAttrPane, null
            }, {
                new JSeparator(), null
            }, {
                borderPane, null
            }, {
                backgroundPane, null
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    private JPanel createDefaultAttrPane()
    {
        titleContent = new TinyFormulaPane();
        textAttrPane = new ChartTextAttrPane();
        Icon aicon[] = {
            BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_left_normal.png"), BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_center_normal.png"), BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_right_normal.png")
        };
        Integer ainteger[] = {
            Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(4)
        };
        alignmentPane = new UIButtonGroup(aicon, ainteger);
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            46D, d, d1
        };
        double ad1[] = {
            d, d, d
        };
        Component acomponent[][] = {
            {
                null, titleContent, null
            }, {
                null, textAttrPane, null
            }, {
                null, new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("Alignment-Style")).append(":").toString()), alignmentPane
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    private void checkTitlePaneUse()
    {
        isTitleVisable.setVisible(true);
        isTitleVisable.setEnabled(true);
        titlePane.setVisible(isTitleVisable.isSelected());
        repaint();
    }

    public String title4PopupWindow()
    {
        return PaneTitleConstants.CHART_STYLE_TITLE_TITLE;
    }

    protected JPanel createContentPane()
    {
        return new ContentPane();
    }

    public void populateBean(Chart chart)
    {
        Title title = chart.getTitle();
        if(title == null)
            return;
        isTitleVisable.setSelected(title.isTitleVisible());
        if(title.getTextObject() instanceof Formula)
            titleContent.populateBean(((Formula)title.getTextObject()).getContent());
        else
            titleContent.populateBean(Utils.objectToString(title.getTextObject()));
        alignmentPane.setSelectedItem(Integer.valueOf(title.getPosition()));
        TextAttr textattr = title.getTextAttr();
        if(textattr == null)
            textattr = new TextAttr();
        textAttrPane.populate(textattr);
        borderPane.populate(title);
        backgroundPane.populate(title);
        checkTitlePaneUse();
    }

    public void updateBean(Chart chart)
    {
        if(chart == null)
            chart = new Chart();
        Title title = chart.getTitle();
        if(title == null)
            title = new Title("");
        title.setTitleVisible(isTitleVisable.isSelected());
        String s = titleContent.updateBean();
        Object obj;
        if(StableUtils.maybeFormula(s))
            obj = new Formula(s);
        else
            obj = s;
        title.setTextObject(obj);
        TextAttr textattr = title.getTextAttr();
        if(textattr == null)
            textattr = new TextAttr();
        title.setPosition(((Integer)alignmentPane.getSelectedItem()).intValue());
        textAttrPane.update(textattr);
        borderPane.update(title);
        backgroundPane.update(title);
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((Chart)obj);
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((Chart)obj);
    }






}
