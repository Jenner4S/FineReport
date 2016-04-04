// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator.cardlayout;

import com.fr.base.background.ColorBackground;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRCardLayoutAdapter;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.*;
import com.fr.design.form.layout.FRCardLayout;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.widget.editors.CardTagWLayoutBorderStyleEditor;
import com.fr.design.mainframe.widget.renderer.LayoutBorderStyleRenderer;
import com.fr.form.ui.*;
import com.fr.form.ui.container.WCardLayout;
import com.fr.form.ui.container.WLayout;
import com.fr.form.ui.container.cardlayout.*;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.core.PropertyChangeAdapter;
import java.awt.*;
import java.awt.event.ContainerEvent;
import java.beans.IntrospectionException;
import javax.swing.border.Border;

// Referenced classes of package com.fr.design.designer.creator.cardlayout:
//            XWCardMainBorderLayout, XWCardTitleLayout, XCardAddButton, XWCardTagLayout, 
//            XCardSwitchButton

public class XWCardLayout extends XLayoutContainer
{

    private CardLayout cardLayout;
    private boolean initFlag;
    private static final int NORTH = 0;
    private static final Color TITLE_COLOR = new Color(51, 132, 240);

    public XWCardLayout(WCardLayout wcardlayout, Dimension dimension)
    {
        super(wcardlayout, dimension);
        initFlag = true;
    }

    protected String getIconName()
    {
        return "card_layout_16.png";
    }

    public String createDefaultName()
    {
        return "tabpane";
    }

    public WCardLayout toData()
    {
        return (WCardLayout)data;
    }

    protected void initLayoutManager()
    {
        cardLayout = new FRCardLayout(toData().getHgap(), toData().getVgap());
        setLayout(cardLayout);
    }

    protected void addWidgetToSwingComponent(WLayout wlayout)
    {
        for(int i = 0; i < wlayout.getWidgetCount(); i++)
        {
            Widget widget = wlayout.getWidget(i);
            XWidgetCreator xwidgetcreator = (XWidgetCreator)XCreatorUtils.createXCreator(wlayout.getWidget(i));
            add(xwidgetcreator, widget.getWidgetName(), i);
            xwidgetcreator.setBackupParent(this);
        }

    }

    public void showCard()
    {
        WCardLayout wcardlayout = toData();
        if(wcardlayout.getWidgetCount() > 0)
            cardLayout.show(this, wcardlayout.getShowIndex2Name());
    }

    protected XLayoutContainer getCreatorWrapper(String s)
    {
        initStyle();
        Dimension dimension = new Dimension();
        WCardMainBorderLayout wcardmainborderlayout = new WCardMainBorderLayout();
        XWCardMainBorderLayout xwcardmainborderlayout = new XWCardMainBorderLayout(wcardmainborderlayout, dimension);
        setBackupParent(xwcardmainborderlayout);
        XWCardTitleLayout xwcardtitlelayout = initTitlePart(s, xwcardmainborderlayout);
        xwcardmainborderlayout.addTitlePart(xwcardtitlelayout);
        return xwcardmainborderlayout;
    }

    private XWCardTitleLayout initTitlePart(String s, XWCardMainBorderLayout xwcardmainborderlayout)
    {
        Dimension dimension = new Dimension();
        WCardTitleLayout wcardtitlelayout = new WCardTitleLayout(toData().getWidgetName());
        XWCardTitleLayout xwcardtitlelayout = new XWCardTitleLayout(wcardtitlelayout, dimension);
        xwcardtitlelayout.setBackupParent(xwcardmainborderlayout);
        XWCardTagLayout xwcardtaglayout = initTagPart(s, xwcardtitlelayout);
        XCardAddButton xcardaddbutton = initAddButton(s, xwcardtitlelayout, xwcardtaglayout);
        xwcardtitlelayout.addNewButton(xcardaddbutton);
        xwcardtitlelayout.addTagPart(xwcardtaglayout);
        return xwcardtitlelayout;
    }

    private XCardAddButton initAddButton(String s, XWCardTitleLayout xwcardtitlelayout, XWCardTagLayout xwcardtaglayout)
    {
        Dimension dimension = new Dimension();
        CardAddButton cardaddbutton = new CardAddButton(s);
        XCardAddButton xcardaddbutton = new XCardAddButton(cardaddbutton, dimension, xwcardtaglayout, this);
        xcardaddbutton.setBackupParent(xwcardtitlelayout);
        return xcardaddbutton;
    }

    private XWCardTagLayout initTagPart(String s, XWCardTitleLayout xwcardtitlelayout)
    {
        Dimension dimension = new Dimension();
        WCardTagLayout wcardtaglayout = new WCardTagLayout();
        XWCardTagLayout xwcardtaglayout = new XWCardTagLayout(wcardtaglayout, dimension, this);
        xwcardtaglayout.setBackupParent(xwcardtitlelayout);
        XCardSwitchButton xcardswitchbutton = initFirstButton(s, xwcardtaglayout);
        xwcardtaglayout.add(xcardswitchbutton);
        return xwcardtaglayout;
    }

    private XCardSwitchButton initFirstButton(String s, XWCardTagLayout xwcardtaglayout)
    {
        CardSwitchButton cardswitchbutton = new CardSwitchButton(s);
        cardswitchbutton.setText((new StringBuilder()).append(Inter.getLocText("FR-Designer_Title")).append(0).toString());
        cardswitchbutton.setInitialBackground(ColorBackground.getInstance(Color.WHITE));
        xwcardtaglayout.setCurrentCard(cardswitchbutton);
        XCardSwitchButton xcardswitchbutton = new XCardSwitchButton(cardswitchbutton, new Dimension(80, -1), this, xwcardtaglayout);
        cardswitchbutton.setCustomStyle(true);
        xcardswitchbutton.setBackupParent(xwcardtaglayout);
        return xcardswitchbutton;
    }

    protected void setWrapperName(XLayoutContainer xlayoutcontainer, String s)
    {
        xlayoutcontainer.toData().setWidgetName((new StringBuilder()).append("border_card_").append(s).toString());
    }

    protected void addToWrapper(XLayoutContainer xlayoutcontainer, int i, int j)
    {
        xlayoutcontainer.add(this, "Center");
    }

    public void componentAdded(ContainerEvent containerevent)
    {
        if(isRefreshing)
        {
            return;
        } else
        {
            XWidgetCreator xwidgetcreator = (XWidgetCreator)containerevent.getChild();
            xwidgetcreator.setDirections(null);
            WCardLayout wcardlayout = toData();
            Widget widget = xwidgetcreator.toData();
            wcardlayout.addWidget(widget);
            return;
        }
    }

    public LayoutAdapter getLayoutAdapter()
    {
        return new FRCardLayoutAdapter(this);
    }

    public boolean hasTitleStyle()
    {
        return true;
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return (new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("widgetName", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_Form-Widget_Name")).setPropertyChangeListener(new PropertyChangeAdapter() {

                final XWCardLayout this$0;

                public void propertyChange()
                {
                    WCardLayout wcardlayout = toData();
                    changeRalateSwitchCardname(wcardlayout.getWidgetName());
                }

            
            {
                this$0 = XWCardLayout.this;
                super();
            }
            }
), (new CRPropertyDescriptor("borderStyle", data.getClass())).setEditorClass(com/fr/design/mainframe/widget/editors/CardTagWLayoutBorderStyleEditor).setRendererClass(com/fr/design/mainframe/widget/renderer/LayoutBorderStyleRenderer).setI18NName(Inter.getLocText("FR-Engine_Style")).putKeyValue("category", "Advanced").setPropertyChangeListener(new PropertyChangeAdapter() {

                final XWCardLayout this$0;

                public void propertyChange()
                {
                    initStyle();
                }

            
            {
                this$0 = XWCardLayout.this;
                super();
            }
            }
)
        });
    }

    protected void initStyle()
    {
        LayoutBorderStyle layoutborderstyle = toData().getBorderStyle();
        initBorderTitleStyle(layoutborderstyle);
        initBorderStyle();
        clearOrShowTitleLayout(ComparatorUtils.equals(Integer.valueOf(layoutborderstyle.getType()), Integer.valueOf(1)));
    }

    private void initBorderTitleStyle(LayoutBorderStyle layoutborderstyle)
    {
        if(!initFlag)
        {
            return;
        } else
        {
            layoutborderstyle.setType(1);
            layoutborderstyle.setBorder(1);
            WidgetTitle widgettitle = layoutborderstyle.getTitle();
            widgettitle.setBackground(ColorBackground.getInstance(TITLE_COLOR));
            initFlag = false;
            return;
        }
    }

    protected void clearOrShowTitleLayout(boolean flag)
    {
        XWCardMainBorderLayout xwcardmainborderlayout = (XWCardMainBorderLayout)getBackupParent();
        if(xwcardmainborderlayout != null)
        {
            XWCardTitleLayout xwcardtitlelayout = (XWCardTitleLayout)xwcardmainborderlayout.getComponent(0);
            if(xwcardtitlelayout != null)
            {
                WCardTitleLayout wcardtitlelayout = (WCardTitleLayout)xwcardtitlelayout.toData();
                xwcardtitlelayout.setVisible(flag);
                wcardtitlelayout.setVisible(flag);
            }
        }
    }

    private void changeRalateSwitchCardname(String s)
    {
        XWCardMainBorderLayout xwcardmainborderlayout = (XWCardMainBorderLayout)getBackupParent();
        WCardMainBorderLayout wcardmainborderlayout = xwcardmainborderlayout.toData();
        WCardTitleLayout wcardtitlelayout = wcardmainborderlayout.getTitlePart();
        WCardTagLayout wcardtaglayout = wcardtitlelayout.getTagPart();
        int i = 0;
        for(int j = wcardtaglayout.getWidgetCount(); i < j; i++)
        {
            CardSwitchButton cardswitchbutton = wcardtaglayout.getSwitchButton(i);
            cardswitchbutton.setCardLayoutName(s);
        }

    }

    public void deleteRelatedComponent(XCreator xcreator, FormDesigner formdesigner)
    {
        XWCardMainBorderLayout xwcardmainborderlayout = (XWCardMainBorderLayout)xcreator.getBackupParent();
        SelectionModel selectionmodel = formdesigner.getSelectionModel();
        selectionmodel.setSelectedCreator(xwcardmainborderlayout);
        selectionmodel.deleteSelection();
    }

    public void setBorder(Border border)
    {
        super.setBorder(border);
        XWCardMainBorderLayout xwcardmainborderlayout = (XWCardMainBorderLayout)getBackupParent();
        if(xwcardmainborderlayout != null)
            xwcardmainborderlayout.setBorder(border);
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
