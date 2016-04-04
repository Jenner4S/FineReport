// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator.cardlayout;

import com.fr.base.BaseUtils;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.XButton;
import com.fr.design.mainframe.*;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.CardAddButton;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.container.WCardLayout;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.Icon;

// Referenced classes of package com.fr.design.designer.creator.cardlayout:
//            XWCardTitleLayout, XWCardMainBorderLayout, XCardSwitchButton, XWTabFitLayout, 
//            XWCardLayout, XWCardTagLayout

public class XCardAddButton extends XButton
{

    private XWCardTagLayout tagLayout;
    private XWCardLayout cardLayout;
    private static final int LEFT_GAP = 5;
    private static final int UP_GAP = 10;
    private static final int START_INDEX = 3;
    private static final int INDEX = 0;
    private static Icon ADD_ICON = BaseUtils.readIcon("/com/fr/design/form/images/add.png");
    private Icon addIcon;

    public XWCardTagLayout getTagLayout()
    {
        return tagLayout;
    }

    public void setTagLayout(XWCardTagLayout xwcardtaglayout)
    {
        tagLayout = xwcardtaglayout;
    }

    public XWCardLayout getCardLayout()
    {
        return cardLayout;
    }

    public void setCardLayout(XWCardLayout xwcardlayout)
    {
        cardLayout = xwcardlayout;
    }

    public XCardAddButton(CardAddButton cardaddbutton, Dimension dimension)
    {
        super(cardaddbutton, dimension);
        addIcon = ADD_ICON;
    }

    public XCardAddButton(CardAddButton cardaddbutton, Dimension dimension, XWCardTagLayout xwcardtaglayout, XWCardLayout xwcardlayout)
    {
        super(cardaddbutton, dimension);
        addIcon = ADD_ICON;
        tagLayout = xwcardtaglayout;
        cardLayout = xwcardlayout;
    }

    public void respondClick(EditingMouseListener editingmouselistener, MouseEvent mouseevent)
    {
        FormDesigner formdesigner = editingmouselistener.getDesigner();
        formdesigner.fireTargetModified();
        if(cardLayout == null && tagLayout == null)
            initRalateLayout();
        int i = cardLayout.toData().getWidgetCount();
        setTabUnselectd();
        addTab(i);
        tagLayout.adjustComponentWidth();
        if(editingmouselistener.stopEditing())
        {
            ComponentAdapter componentadapter = AdapterBus.getComponentAdapter(formdesigner, this);
            editingmouselistener.startEditing(this, componentadapter.getDesignerEditor(), componentadapter);
        }
        FormHierarchyTreePane.getInstance().refreshRoot();
        showNewTab(editingmouselistener, i);
        LayoutUtils.layoutRootContainer(formdesigner.getRootComponent());
    }

    private void initRalateLayout()
    {
        XWCardTitleLayout xwcardtitlelayout = (XWCardTitleLayout)getBackupParent();
        tagLayout = xwcardtitlelayout.getTagPart();
        XWCardMainBorderLayout xwcardmainborderlayout = (XWCardMainBorderLayout)xwcardtitlelayout.getBackupParent();
        cardLayout = xwcardmainborderlayout.getCardPart();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D graphics2d = (Graphics2D)g;
        drawAddIcon(graphics2d);
    }

    private void drawAddIcon(Graphics2D graphics2d)
    {
        addIcon.paintIcon(this, graphics2d, 5, 10);
    }

    private void setTabUnselectd()
    {
        for(int i = 0; i < tagLayout.getComponentCount(); i++)
        {
            WCardTagLayout wcardtaglayout = (WCardTagLayout)tagLayout.toData();
            CardSwitchButton cardswitchbutton = wcardtaglayout.getSwitchButton(i);
            cardswitchbutton.setShowButton(false);
        }

    }

    private void addTab(int i)
    {
        Dimension dimension = new Dimension();
        XCardSwitchButton xcardswitchbutton = (XCardSwitchButton)tagLayout.getComponent(0);
        dimension.width = xcardswitchbutton.getWidth();
        String s = cardLayout.toData().getWidgetName();
        CardSwitchButton cardswitchbutton = new CardSwitchButton(i, s);
        cardswitchbutton.setText(getTabTitleName());
        XCardSwitchButton xcardswitchbutton1 = new XCardSwitchButton(cardswitchbutton, dimension, cardLayout, tagLayout);
        cardswitchbutton.setCustomStyle(true);
        cardswitchbutton.setShowButton(true);
        tagLayout.setCurrentCard(cardswitchbutton);
        tagLayout.setTabFitIndex(i);
        tagLayout.add(xcardswitchbutton1);
    }

    private void showNewTab(EditingMouseListener editingmouselistener, int i)
    {
        SelectionModel selectionmodel = editingmouselistener.getSelectionModel();
        XWTabFitLayout xwtabfitlayout = (XWTabFitLayout)cardLayout.getComponent(i);
        selectionmodel.setSelectedCreator(xwtabfitlayout);
    }

    private String getTabTitleName()
    {
        WCardTagLayout wcardtaglayout = (WCardTagLayout)tagLayout.toData();
        int i = wcardtaglayout.getWidgetCount();
        String s = Inter.getLocText("FR-Designer_Title");
        String s1 = (new StringBuilder()).append(s).append(i).toString();
        for(int j = 0; j < i; j++)
        {
            CardSwitchButton cardswitchbutton = wcardtaglayout.getSwitchButton(j);
            String s2 = cardswitchbutton.getText();
            if(ComparatorUtils.equals(s2, s1))
            {
                int k = Integer.parseInt(s1.replaceAll(s, ""));
                s1 = (new StringBuilder()).append(s).append(k + 1).toString();
                j = 0;
            }
        }

        return s1;
    }

}
