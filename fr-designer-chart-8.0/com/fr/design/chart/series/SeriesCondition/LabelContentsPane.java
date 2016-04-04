// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.AttrContents;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.chart.series.SeriesCondition:
//            DataLabelContentsPane

public class LabelContentsPane extends ConditionAttrSingleConditionPane
{

    private UILabel nameLabel;
    private DataLabelContentsPane dataLabelContentsPane;
    private AttrContents attrContents;

    public LabelContentsPane(ConditionAttributesPane conditionattributespane, Class class1)
    {
        this(conditionattributespane, true, class1);
    }

    public LabelContentsPane(ConditionAttributesPane conditionattributespane, boolean flag, Class class1)
    {
        super(conditionattributespane, flag);
        attrContents = new AttrContents();
        nameLabel = new UILabel(Inter.getLocText(new String[] {
            "Label", "HJS-Message"
        }));
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        if(flag)
        {
            removeAll();
            setLayout(FRGUIPaneFactory.createBorderLayout());
            JPanel jpanel1 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            jpanel1.setPreferredSize(new Dimension(300, 30));
            add(jpanel1, "North");
            jpanel1.add(cancel);
            jpanel1.add(nameLabel);
            jpanel.setBorder(BorderFactory.createEmptyBorder(6, 25, 0, 0));
        }
        dataLabelContentsPane = new DataLabelContentsPane(class1);
        jpanel.add(dataLabelContentsPane);
        add(jpanel);
    }

    public String nameForPopupMenuItem()
    {
        return Inter.getLocText(new String[] {
            "Label", "HJS-Message"
        });
    }

    protected String title4PopupWindow()
    {
        return nameForPopupMenuItem();
    }

    public void checkGuidBox()
    {
        if(dataLabelContentsPane != null)
            dataLabelContentsPane.checkGuidBox();
    }

    public void populate(DataSeriesCondition dataseriescondition)
    {
        if(dataseriescondition instanceof AttrContents)
        {
            attrContents = (AttrContents)dataseriescondition;
            dataLabelContentsPane.populate(attrContents);
        }
    }

    public DataSeriesCondition update()
    {
        dataLabelContentsPane.update(attrContents);
        return attrContents;
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
