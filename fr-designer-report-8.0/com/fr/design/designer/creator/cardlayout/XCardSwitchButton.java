// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator.cardlayout;

import com.fr.base.BaseUtils;
import com.fr.base.background.ColorBackground;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.XButton;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.*;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.*;
import com.fr.form.ui.container.WCardLayout;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;
import com.fr.general.Background;
import com.fr.general.FRFont;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import javax.swing.Icon;

// Referenced classes of package com.fr.design.designer.creator.cardlayout:
//            XWTabFitLayout, XWCardTagLayout, XWCardTitleLayout, XWCardMainBorderLayout, 
//            XWCardLayout

public class XCardSwitchButton extends XButton
{

    private XWCardLayout cardLayout;
    private XWCardTagLayout tagLayout;
    private static final int LEFT_GAP = 16;
    public static final Color NORMAL_GRAL = new Color(236, 236, 236);
    public static final Color CHOOSED_GRAL = new Color(222, 222, 222);
    private static final int MIN_SIZE = 1;
    private static final int RIGHT_OFFSET = 15;
    private static final int TOP_OFFSET = 25;
    private static final int FONT_SIZE_ADJUST = 2;
    private static Icon MOUSE_COLSE = BaseUtils.readIcon("/com/fr/design/images/buttonicon/close_icon.png");
    private Icon closeIcon;

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

    public XCardSwitchButton(CardSwitchButton cardswitchbutton, Dimension dimension)
    {
        super(cardswitchbutton, dimension);
        closeIcon = MOUSE_COLSE;
    }

    public XCardSwitchButton(CardSwitchButton cardswitchbutton, Dimension dimension, XWCardLayout xwcardlayout, XWCardTagLayout xwcardtaglayout)
    {
        super(cardswitchbutton, dimension);
        closeIcon = MOUSE_COLSE;
        cardLayout = xwcardlayout;
        tagLayout = xwcardtaglayout;
    }

    public void respondClick(EditingMouseListener editingmouselistener, MouseEvent mouseevent)
    {
        FormDesigner formdesigner = editingmouselistener.getDesigner();
        SelectionModel selectionmodel = editingmouselistener.getSelectionModel();
        if(cardLayout == null)
            initRalateLayout(this);
        XCardSwitchButton xcardswitchbutton = this;
        CardSwitchButton cardswitchbutton = (CardSwitchButton)xcardswitchbutton.toData();
        int i = cardswitchbutton.getIndex();
        if(isSeletectedClose(mouseevent, formdesigner))
            if(tagLayout.getComponentCount() <= 1)
            {
                deleteTabLayout(selectionmodel, formdesigner);
                return;
            } else
            {
                deleteCard(xcardswitchbutton, i);
                tagLayout.adjustComponentWidth();
                formdesigner.fireTargetModified();
                LayoutUtils.layoutRootContainer(formdesigner.getRootComponent());
                FormHierarchyTreePane.getInstance().refreshRoot();
                return;
            }
        changeButtonState(i);
        XWTabFitLayout xwtabfitlayout = (XWTabFitLayout)cardLayout.getComponent(i);
        selectionmodel.setSelectedCreator(xwtabfitlayout);
        if(editingmouselistener.stopEditing())
        {
            ComponentAdapter componentadapter = AdapterBus.getComponentAdapter(formdesigner, this);
            editingmouselistener.startEditing(this, componentadapter.getDesignerEditor(), componentadapter);
        }
    }

    private void deleteCard(XCardSwitchButton xcardswitchbutton, int i)
    {
        tagLayout.remove(xcardswitchbutton);
        XWTabFitLayout xwtabfitlayout = (XWTabFitLayout)cardLayout.getComponent(i);
        xwtabfitlayout.removeAll();
        cardLayout.remove(i);
        for(int j = 0; j < tagLayout.getComponentCount(); j++)
        {
            XCardSwitchButton xcardswitchbutton1 = (XCardSwitchButton)tagLayout.getComponent(j);
            CardSwitchButton cardswitchbutton = (CardSwitchButton)xcardswitchbutton1.toData();
            XWTabFitLayout xwtabfitlayout1 = (XWTabFitLayout)cardLayout.getComponent(j);
            WTabFitLayout wtabfitlayout = (WTabFitLayout)xwtabfitlayout1.toData();
            int k = cardswitchbutton.getIndex();
            int l = wtabfitlayout.getIndex();
            if(k > i)
                cardswitchbutton.setIndex(--k);
            if(l > i)
                wtabfitlayout.setIndex(--l);
        }

    }

    private void initRalateLayout(XCardSwitchButton xcardswitchbutton)
    {
        tagLayout = (XWCardTagLayout)getBackupParent();
        XWCardTitleLayout xwcardtitlelayout = (XWCardTitleLayout)tagLayout.getBackupParent();
        XWCardMainBorderLayout xwcardmainborderlayout = (XWCardMainBorderLayout)xwcardtitlelayout.getBackupParent();
        cardLayout = xwcardmainborderlayout.getCardPart();
    }

    private boolean isSeletectedClose(MouseEvent mouseevent, FormDesigner formdesigner)
    {
        int i = formdesigner.getArea().getHorScrollBar().getValue();
        int j = mouseevent.getX() + i;
        int k = mouseevent.getY();
        XLayoutContainer xlayoutcontainer = cardLayout.getBackupParent();
        Point point = xlayoutcontainer.getLocation();
        double d = point.getX();
        double d1 = point.getY();
        JForm jform = (JForm)HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if(jform.getFormDesign().getParaComponent() != null)
            k -= jform.getFormDesign().getParaHeight();
        j = (int)((double)j - d);
        k = (int)((double)k - d1);
        XCardSwitchButton xcardswitchbutton = this;
        Point point1 = xcardswitchbutton.getLocation();
        int l = xcardswitchbutton.getWidth();
        int i1 = xcardswitchbutton.getHeight();
        double d2 = point1.getX() + (double)(l - 15);
        double d3 = point1.getY() + (double)(i1 - 25);
        return d2 < (double)j && (double)j < d2 + 15D && (double)k < d3;
    }

    private void changeButtonState(int i)
    {
        for(int j = 0; j < tagLayout.getComponentCount(); j++)
        {
            XCardSwitchButton xcardswitchbutton = (XCardSwitchButton)tagLayout.getComponent(j);
            CardSwitchButton cardswitchbutton = (CardSwitchButton)xcardswitchbutton.toData();
            cardswitchbutton.setShowButton(cardswitchbutton.getIndex() == i);
        }

    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D graphics2d = (Graphics2D)g;
        drawBackgorund();
        drawTitle();
        Dimension dimension = getContentLabel().getSize();
        getContentBackground().paint(g, new java.awt.geom.Rectangle2D.Double(0.0D, 0.0D, dimension.getWidth(), dimension.getHeight()));
        drawCloseIcon(graphics2d);
    }

    private void drawCloseIcon(Graphics2D graphics2d)
    {
        closeIcon.paintIcon(this, graphics2d, getWidth() - 16, 0);
    }

    private void drawBackgorund()
    {
        CardSwitchButton cardswitchbutton = (CardSwitchButton)toData();
        if(cardswitchbutton.isShowButton())
        {
            rebuid();
            ColorBackground colorbackground = ColorBackground.getInstance(CHOOSED_GRAL);
            setContentBackground(colorbackground);
        } else
        {
            rebuid();
            ColorBackground colorbackground1 = ColorBackground.getInstance(NORMAL_GRAL);
            setContentBackground(colorbackground1);
        }
    }

    private void drawTitle()
    {
        CardSwitchButton cardswitchbutton = (CardSwitchButton)toData();
        setButtonText(cardswitchbutton.getText());
        if(cardLayout == null)
            initRalateLayout(this);
        LayoutBorderStyle layoutborderstyle = cardLayout.toData().getBorderStyle();
        WidgetTitle widgettitle = layoutborderstyle.getTitle();
        FRFont frfont = widgettitle.getFrFont();
        FRFont frfont1 = FRFont.getInstance(frfont.getName(), frfont.getStyle(), frfont.getSize() + 2);
        UILabel uilabel = getContentLabel();
        uilabel.setFont(frfont1);
        uilabel.setForeground(frfont.getForeground());
        Object obj = widgettitle.getBackground();
        if(obj != null)
            if(cardswitchbutton.isShowButton())
            {
                obj = ColorBackground.getInstance(CHOOSED_GRAL);
                setContentBackground(((Background) (obj)));
            } else
            {
                setContentBackground(((Background) (obj)));
            }
    }

    private void deleteTabLayout(SelectionModel selectionmodel, FormDesigner formdesigner)
    {
        XLayoutContainer xlayoutcontainer = cardLayout.getBackupParent();
        if(xlayoutcontainer != null)
        {
            selectionmodel.setSelectedCreator(xlayoutcontainer);
            selectionmodel.deleteSelection();
        }
        LayoutUtils.layoutRootContainer(formdesigner.getRootComponent());
        FormHierarchyTreePane.getInstance().refreshRoot();
        selectionmodel.setSelectedCreator(formdesigner.getRootComponent());
    }

}
