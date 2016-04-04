// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.AttrTrendLine;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.chart.series.SeriesCondition:
//            TrendLineControlPane

public class TrendLinePane extends ConditionAttrSingleConditionPane
{

    private UILabel nameLabel;
    private UIButton editTrendLineButton;
    private AttrTrendLine attrTrendLine;

    public TrendLinePane(ConditionAttributesPane conditionattributespane)
    {
        this(conditionattributespane, true);
    }

    public TrendLinePane(ConditionAttributesPane conditionattributespane, boolean flag)
    {
        super(conditionattributespane, flag);
        attrTrendLine = new AttrTrendLine();
        nameLabel = new UILabel(Inter.getLocText("Chart_TrendLine"));
        editTrendLineButton = new UIButton(Inter.getLocText(new String[] {
            "Edit", "Chart_TrendLine"
        }));
        editTrendLineButton.addActionListener(new ActionListener() {

            final TrendLinePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                final TrendLineControlPane controlPane = new TrendLineControlPane();
                controlPane.populate(attrTrendLine);
                BasicDialog basicdialog = controlPane.showWindow(SwingUtilities.getWindowAncestor(TrendLinePane.this), new DialogActionAdapter() {

                    final TrendLineControlPane val$controlPane;
                    final _cls1 this$1;

                    public void doOk()
                    {
                        controlPane.update(attrTrendLine);
                    }

                    
                    {
                        this$1 = _cls1.this;
                        controlPane = trendlinecontrolpane;
                        super();
                    }
                }
);
                basicdialog.setVisible(true);
            }

            
            {
                this$0 = TrendLinePane.this;
                super();
            }
        }
);
        if(flag)
            add(nameLabel);
        add(editTrendLineButton);
    }

    public String nameForPopupMenuItem()
    {
        return Inter.getLocText("Chart_TrendLine");
    }

    public void populate(DataSeriesCondition dataseriescondition)
    {
        if(dataseriescondition instanceof AttrTrendLine)
            attrTrendLine = (AttrTrendLine)dataseriescondition;
    }

    public DataSeriesCondition update()
    {
        return attrTrendLine;
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
