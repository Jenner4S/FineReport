// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator.cardlayout;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XWBorderLayout;
import com.fr.form.ui.AbstractBorderStyleWidget;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.*;
import com.fr.form.ui.container.cardlayout.WCardMainBorderLayout;
import java.awt.*;
import java.util.ArrayList;

// Referenced classes of package com.fr.design.designer.creator.cardlayout:
//            XWCardLayout, XWCardTitleLayout, XWTabFitLayout

public class XWCardMainBorderLayout extends XWBorderLayout
{

    private static final int CENTER = 1;
    private static final int NORTH = 0;
    public static final Color DEFAULT_BORDER_COLOR = new Color(210, 210, 210);
    private static final int LAYOUT_INDEX = 0;
    private static final int TITLE_STYLE = 2;
    private static final int NORMAL_STYLE = 1;

    public XWCardMainBorderLayout(WCardMainBorderLayout wcardmainborderlayout, Dimension dimension)
    {
        super(wcardmainborderlayout, dimension);
    }

    public WCardMainBorderLayout toData()
    {
        return (WCardMainBorderLayout)super.toData();
    }

    public void addTitlePart(XWCardTitleLayout xwcardtitlelayout)
    {
        add(xwcardtitlelayout, "North");
    }

    public void addCardPart(XWCardLayout xwcardlayout)
    {
        add(xwcardlayout, "Center");
    }

    public XWCardLayout getCardPart()
    {
        return getComponentCount() != 2 ? (XWCardLayout)getComponent(0) : (XWCardLayout)getComponent(1);
    }

    public XWCardTitleLayout getTitlePart()
    {
        return (XWCardTitleLayout)getComponent(0);
    }

    public XCreator getXCreator()
    {
        switch(getComponentCount())
        {
        case 2: // '\002'
            return (XCreator)getComponent(1);

        case 1: // '\001'
            return (XCreator)getComponent(0);
        }
        return this;
    }

    public void notShowInComponentTree(ArrayList arraylist)
    {
        arraylist.remove(0);
    }

    public ArrayList getTargetChildrenList()
    {
        ArrayList arraylist = new ArrayList();
        XWCardLayout xwcardlayout = getCardPart();
        int i = 0;
        for(int j = xwcardlayout.getComponentCount(); i < j; i++)
        {
            XWTabFitLayout xwtabfitlayout = (XWTabFitLayout)xwcardlayout.getComponent(i);
            arraylist.add(xwtabfitlayout);
        }

        return arraylist;
    }

    public void recalculateChildWidth(int i)
    {
        ArrayList arraylist = getTargetChildrenList();
        int j = arraylist.size();
        if(j > 0)
        {
            for(int k = 0; k < j; k++)
            {
                XWTabFitLayout xwtabfitlayout = (XWTabFitLayout)arraylist.get(k);
                xwtabfitlayout.setBackupBound(xwtabfitlayout.getBounds());
                int l = xwtabfitlayout.getWidth();
                int i1 = i - l;
                double d = (double)i1 / (double)l;
                if(d < 0.0D && !xwtabfitlayout.canReduce(d))
                    return;
                xwtabfitlayout.setSize(xwtabfitlayout.getWidth() + i1, xwtabfitlayout.getHeight());
                for(int j1 = 0; j1 < xwtabfitlayout.getComponentCount(); j1++)
                {
                    XCreator xcreator = xwtabfitlayout.getXCreator(j1);
                    com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget boundswidget = xwtabfitlayout.toData().getBoundsWidget(xcreator.toData());
                    boundswidget.setBounds(xwtabfitlayout.getComponent(j1).getBounds());
                }

                xwtabfitlayout.adjustCreatorsWidth(d);
            }

        }
    }

    public void recalculateChildHeight(int i)
    {
        ArrayList arraylist = getTargetChildrenList();
        int j = arraylist.size();
        if(j > 0)
        {
            for(int k = 0; k < j; k++)
            {
                XWTabFitLayout xwtabfitlayout = (XWTabFitLayout)arraylist.get(k);
                xwtabfitlayout.setBackupBound(xwtabfitlayout.getBounds());
                int l = xwtabfitlayout.getHeight();
                int i1 = i - l - 36;
                if(i1 < 0)
                    xwtabfitlayout.setReferDim(new Dimension(xwtabfitlayout.getWidth(), xwtabfitlayout.getHeight()));
                double d = (double)i1 / (double)l;
                if(d < 0.0D && !xwtabfitlayout.canReduce(d))
                    return;
                xwtabfitlayout.setSize(xwtabfitlayout.getWidth(), xwtabfitlayout.getHeight() + i1);
                for(int j1 = 0; j1 < xwtabfitlayout.getComponentCount(); j1++)
                {
                    XCreator xcreator = xwtabfitlayout.getXCreator(j1);
                    com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget boundswidget = xwtabfitlayout.toData().getBoundsWidget(xcreator.toData());
                    boundswidget.setBounds(xwtabfitlayout.getComponent(j1).getBounds());
                }

                xwtabfitlayout.adjustCreatorsHeight(d);
            }

        }
    }

    public volatile WBorderLayout toData()
    {
        return toData();
    }

    public volatile WLayout toData()
    {
        return toData();
    }

    public volatile AbstractBorderStyleWidget toData()
    {
        return toData();
    }

    public volatile Widget toData()
    {
        return toData();
    }

}
