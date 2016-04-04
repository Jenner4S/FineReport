// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition;

import com.fr.base.background.ColorBackground;
import com.fr.chart.base.AttrBackground;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.style.background.BackgroundPane;
import com.fr.design.style.background.BackgroundPreviewLabel;
import com.fr.general.Inter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

public class LabelBackgroundPane extends ConditionAttrSingleConditionPane
{

    private UILabel backgroundLabel;
    private BackgroundPreviewLabel backgroundPreviewPane;
    private AttrBackground attrBackground;

    public LabelBackgroundPane(final ConditionAttributesPane conditionAttributesPane)
    {
        super(conditionAttributesPane, true);
        attrBackground = new AttrBackground();
        backgroundLabel = new UILabel(Inter.getLocText("Background"));
        backgroundPreviewPane = new BackgroundPreviewLabel();
        backgroundPreviewPane.setPreferredSize(new Dimension(80, 20));
        UIButton uibutton = new UIButton(Inter.getLocText("Edit"));
        MouseAdapter mouseadapter = new MouseAdapter() {

            final ConditionAttributesPane val$conditionAttributesPane;
            final LabelBackgroundPane this$0;

            public void mousePressed(MouseEvent mouseevent)
            {
                final BackgroundPane backgroundPane = new BackgroundPane();
                BasicDialog basicdialog = backgroundPane.showWindow(SwingUtilities.getWindowAncestor(conditionAttributesPane));
                backgroundPane.populate(backgroundPreviewPane.getBackgroundObject());
                basicdialog.addDialogActionListener(new DialogActionAdapter() {

                    final BackgroundPane val$backgroundPane;
                    final _cls1 this$1;

                    public void doOk()
                    {
                        backgroundPreviewPane.setBackgroundObject(backgroundPane.update());
                        backgroundPreviewPane.repaint();
                    }

                    
                    {
                        this$1 = _cls1.this;
                        backgroundPane = backgroundpane;
                        super();
                    }
                }
);
                basicdialog.setVisible(true);
            }

            
            {
                this$0 = LabelBackgroundPane.this;
                conditionAttributesPane = conditionattributespane;
                super();
            }
        }
;
        uibutton.addMouseListener(mouseadapter);
        backgroundPreviewPane.addMouseListener(mouseadapter);
        add(backgroundLabel);
        add(backgroundPreviewPane);
        add(uibutton);
        backgroundPreviewPane.setBackgroundObject(ColorBackground.getInstance(Color.white));
    }

    public String nameForPopupMenuItem()
    {
        return Inter.getLocText("ChartF-Background_Color");
    }

    protected String title4PopupWindow()
    {
        return nameForPopupMenuItem();
    }

    public void populate(DataSeriesCondition dataseriescondition)
    {
        if(dataseriescondition instanceof AttrBackground)
        {
            attrBackground = (AttrBackground)dataseriescondition;
            backgroundPreviewPane.setBackgroundObject(attrBackground.getSeriesBackground());
        }
    }

    public DataSeriesCondition update()
    {
        attrBackground.setSeriesBackground(backgroundPreviewPane.getBackgroundObject());
        return attrBackground;
    }

    public volatile Object update()
    {
        return update();
    }

    public volatile void populate(Object obj)
    {
        populate((DataSeriesCondition)obj);
    }

}
