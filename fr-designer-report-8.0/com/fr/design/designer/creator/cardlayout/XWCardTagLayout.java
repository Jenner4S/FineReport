// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator.cardlayout;

import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.*;
import com.fr.design.mainframe.EditingMouseListener;
import com.fr.design.mainframe.FormDesigner;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.container.WCardLayout;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;
import java.awt.Dimension;
import java.awt.event.ContainerEvent;
import java.awt.event.MouseEvent;
import javax.swing.border.Border;

// Referenced classes of package com.fr.design.designer.creator.cardlayout:
//            XWTabFitLayout, XWCardTitleLayout, XWCardMainBorderLayout, XWCardLayout

public class XWCardTagLayout extends XWHorizontalBoxLayout
{

    private static final int MIN_SIZE = 1;
    private String tagName;
    private int tabFitIndex;
    private CardSwitchButton currentCard;
    private XWCardLayout cardLayout;

    public CardSwitchButton getCurrentCard()
    {
        return currentCard;
    }

    public void setCurrentCard(CardSwitchButton cardswitchbutton)
    {
        currentCard = cardswitchbutton;
    }

    public int getTabFitIndex()
    {
        return tabFitIndex;
    }

    public void setTabFitIndex(int i)
    {
        tabFitIndex = i;
    }

    public String getTagName()
    {
        return tagName;
    }

    public void setTagName(String s)
    {
        tagName = s;
    }

    public XWCardTagLayout(WCardTagLayout wcardtaglayout, Dimension dimension)
    {
        super(wcardtaglayout, dimension);
        tagName = "Tab";
        tabFitIndex = 0;
    }

    public XWCardTagLayout(WCardTagLayout wcardtaglayout, Dimension dimension, XWCardLayout xwcardlayout)
    {
        super(wcardtaglayout, dimension);
        tagName = "Tab";
        tabFitIndex = 0;
        cardLayout = xwcardlayout;
    }

    public void componentAdded(ContainerEvent containerevent)
    {
        super.componentAdded(containerevent);
        if(cardLayout == null)
            initCardLayout();
        int i = cardLayout.toData().getWidgetCount();
        String s = (new StringBuilder()).append(tagName).append(getTabNameIndex()).toString();
        WTabFitLayout wtabfitlayout = new WTabFitLayout(s, tabFitIndex, currentCard);
        wtabfitlayout.setTabNameIndex(getTabNameIndex());
        XWTabFitLayout xwtabfitlayout = new XWTabFitLayout(wtabfitlayout, new Dimension());
        xwtabfitlayout.setBackupParent(cardLayout);
        cardLayout.add(xwtabfitlayout, s);
        cardLayout.toData().setShowIndex(i);
        cardLayout.showCard();
    }

    private void initCardLayout()
    {
        XWCardTitleLayout xwcardtitlelayout = (XWCardTitleLayout)getBackupParent();
        XWCardMainBorderLayout xwcardmainborderlayout = (XWCardMainBorderLayout)xwcardtitlelayout.getBackupParent();
        cardLayout = xwcardmainborderlayout.getCardPart();
    }

    public void convert()
    {
        isRefreshing = true;
        WCardTagLayout wcardtaglayout = (WCardTagLayout)toData();
        removeAll();
        for(int i = 0; i < wcardtaglayout.getWidgetCount(); i++)
        {
            com.fr.form.ui.Widget widget = wcardtaglayout.getWidget(i);
            if(widget != null)
            {
                XWidgetCreator xwidgetcreator = (XWidgetCreator)XCreatorUtils.createXCreator(widget, calculatePreferredSize(widget));
                add(xwidgetcreator, i);
                xwidgetcreator.setBackupParent(this);
            }
        }

        isRefreshing = false;
    }

    public void stopAddingState(FormDesigner formdesigner)
    {
        formdesigner.stopAddingState();
    }

    private int getTabNameIndex()
    {
        int i = 0;
        WCardLayout wcardlayout = cardLayout.toData();
        int j = wcardlayout.getWidgetCount();
        if(j < 1)
            return i;
        for(int k = 0; k < j; k++)
        {
            WTabFitLayout wtabfitlayout = (WTabFitLayout)wcardlayout.getWidget(k);
            int l = wtabfitlayout.getTabNameIndex();
            i = Math.max(l, i);
        }

        return ++i;
    }

    public void adjustComponentWidth()
    {
        int i = getComponentCount();
        int j = getWidth();
        int k = i * 80 + i;
        int l = 80;
        if(j - k < 80)
            l = (j - 80 - i) / i;
        WCardTagLayout wcardtaglayout = (WCardTagLayout)toData();
        for(int i1 = 0; i1 < i; i1++)
        {
            CardSwitchButton cardswitchbutton = wcardtaglayout.getSwitchButton(i1);
            wcardtaglayout.setWidthAtWidget(cardswitchbutton, l);
        }

    }

    public void setBorder(Border border)
    {
    }

    public void respondClick(EditingMouseListener editingmouselistener, MouseEvent mouseevent)
    {
        FormDesigner formdesigner = editingmouselistener.getDesigner();
        SelectionModel selectionmodel = editingmouselistener.getSelectionModel();
        XWCardTitleLayout xwcardtitlelayout = (XWCardTitleLayout)getBackupParent();
        if(xwcardtitlelayout != null)
        {
            XWCardMainBorderLayout xwcardmainborderlayout = (XWCardMainBorderLayout)xwcardtitlelayout.getBackupParent();
            if(xwcardmainborderlayout != null)
            {
                XWCardLayout xwcardlayout = xwcardmainborderlayout.getCardPart();
                selectionmodel.setSelectedCreator(xwcardlayout);
            }
        }
        if(editingmouselistener.stopEditing() && this != formdesigner.getRootComponent())
        {
            ComponentAdapter componentadapter = AdapterBus.getComponentAdapter(formdesigner, this);
            editingmouselistener.startEditing(this, componentadapter.getDesignerEditor(), componentadapter);
        }
    }
}
