// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator.cardlayout;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRTabFitLayoutAdapter;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.*;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormHierarchyTreePane;
import com.fr.design.mainframe.widget.editors.PaddingMarginEditor;
import com.fr.design.mainframe.widget.renderer.PaddingMarginCellRenderer;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.form.ui.container.WFitLayout;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;
import com.fr.general.Inter;
import java.awt.*;
import java.beans.IntrospectionException;
import javax.swing.border.Border;

// Referenced classes of package com.fr.design.designer.creator.cardlayout:
//            XWCardLayout, XWCardMainBorderLayout, XWCardTitleLayout, XWCardTagLayout

public class XWTabFitLayout extends XWFitLayout
{

    private static final int MIN_SIZE = 1;
    private Dimension referDim;

    public Dimension getReferDim()
    {
        return referDim;
    }

    public void setReferDim(Dimension dimension)
    {
        referDim = dimension;
    }

    public XWTabFitLayout()
    {
        this(new WTabFitLayout(), new Dimension());
    }

    public XWTabFitLayout(WTabFitLayout wtabfitlayout, Dimension dimension)
    {
        super(wtabfitlayout, dimension);
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return (new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("widgetName", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_Form-Widget_Name")), (new CRPropertyDescriptor("margin", data.getClass())).setEditorClass(com/fr/design/mainframe/widget/editors/PaddingMarginEditor).setRendererClass(com/fr/design/mainframe/widget/renderer/PaddingMarginCellRenderer).setI18NName(Inter.getLocText("FR-Designer_Layout-Padding")).putKeyValue("category", "Advanced")
        });
    }

    public LayoutAdapter getLayoutAdapter()
    {
        return new FRTabFitLayoutAdapter(this);
    }

    public void deleteRelatedComponent(XCreator xcreator, FormDesigner formdesigner)
    {
        XWTabFitLayout xwtabfitlayout = (XWTabFitLayout)xcreator;
        WTabFitLayout wtabfitlayout = (WTabFitLayout)xwtabfitlayout.toData();
        int i = wtabfitlayout.getIndex();
        XWCardLayout xwcardlayout = (XWCardLayout)xwtabfitlayout.getBackupParent();
        XWCardMainBorderLayout xwcardmainborderlayout = (XWCardMainBorderLayout)xwcardlayout.getBackupParent();
        XWCardTitleLayout xwcardtitlelayout = xwcardmainborderlayout.getTitlePart();
        XWCardTagLayout xwcardtaglayout = xwcardtitlelayout.getTagPart();
        WCardTagLayout wcardtaglayout = (WCardTagLayout)xwcardtaglayout.toData();
        if(wcardtaglayout.getWidgetCount() <= 1)
        {
            deleteTabLayout(xwcardmainborderlayout, formdesigner);
            return;
        }
        int j = 0;
        do
        {
            if(j >= xwcardtaglayout.getComponentCount())
                break;
            CardSwitchButton cardswitchbutton = wcardtaglayout.getSwitchButton(j);
            if(cardswitchbutton.getIndex() == i)
            {
                xwcardtaglayout.remove(j);
                break;
            }
            j++;
        } while(true);
        refreshIndex(wcardtaglayout, xwcardlayout, i);
        LayoutUtils.layoutRootContainer(formdesigner.getRootComponent());
    }

    private void deleteTabLayout(XLayoutContainer xlayoutcontainer, FormDesigner formdesigner)
    {
        SelectionModel selectionmodel = formdesigner.getSelectionModel();
        if(xlayoutcontainer != null)
        {
            selectionmodel.setSelectedCreator(xlayoutcontainer);
            selectionmodel.deleteSelection();
        }
        LayoutUtils.layoutRootContainer(formdesigner.getRootComponent());
        FormHierarchyTreePane.getInstance().refreshRoot();
        selectionmodel.setSelectedCreator(formdesigner.getRootComponent());
    }

    private void refreshIndex(WCardTagLayout wcardtaglayout, XWCardLayout xwcardlayout, int i)
    {
        for(int j = 0; j < wcardtaglayout.getWidgetCount(); j++)
        {
            CardSwitchButton cardswitchbutton = wcardtaglayout.getSwitchButton(j);
            XWTabFitLayout xwtabfitlayout = (XWTabFitLayout)xwcardlayout.getComponent(j);
            WTabFitLayout wtabfitlayout = (WTabFitLayout)xwtabfitlayout.toData();
            int k = wtabfitlayout.getIndex();
            int l = cardswitchbutton.getIndex();
            if(l > i)
                cardswitchbutton.setIndex(--l);
            if(k > i)
                wtabfitlayout.setIndex(--k);
        }

    }

    public void seleteRelatedComponent(XCreator xcreator)
    {
        XWTabFitLayout xwtabfitlayout = (XWTabFitLayout)xcreator;
        WTabFitLayout wtabfitlayout = (WTabFitLayout)xwtabfitlayout.toData();
        int i = wtabfitlayout.getIndex();
        XWCardLayout xwcardlayout = (XWCardLayout)xwtabfitlayout.getBackupParent();
        XWCardMainBorderLayout xwcardmainborderlayout = (XWCardMainBorderLayout)xwcardlayout.getBackupParent();
        XWCardTitleLayout xwcardtitlelayout = xwcardmainborderlayout.getTitlePart();
        XWCardTagLayout xwcardtaglayout = xwcardtitlelayout.getTagPart();
        WCardTagLayout wcardtaglayout = (WCardTagLayout)xwcardtaglayout.toData();
        for(int j = 0; j < xwcardtaglayout.getComponentCount(); j++)
        {
            CardSwitchButton cardswitchbutton = wcardtaglayout.getSwitchButton(j);
            cardswitchbutton.setShowButton(cardswitchbutton.getIndex() == i);
        }

    }

    public XLayoutContainer findNearestFit()
    {
        XLayoutContainer xlayoutcontainer = getBackupParent();
        return xlayoutcontainer != null ? xlayoutcontainer.findNearestFit() : null;
    }

    public void adjustCompSize(double d)
    {
        adjustCreatorsWhileSlide(d);
    }

    public void setBorder(Border border)
    {
    }

    public void adjustCreatorsWidth(double d)
    {
        if(getComponentCount() == 0)
        {
            toData().setContainerWidth(getWidth());
            return;
        }
        updateWidgetBackupBounds();
        int i = toData().getCompInterval();
        if(i > 0 && hasCalGap)
        {
            moveCompInterval(getAcualInterval());
            updateCompsWidget();
        }
        layoutWidthResize(d);
        if(d < 0.0D && getNeedAddWidth() > 0)
        {
            setSize(getWidth() + getNeedAddWidth(), getHeight());
            modifyEdgemostCreator(true);
        }
        addCompInterval(getAcualInterval());
        setReferDim(null);
        updateCompsWidget();
        toData().setContainerWidth(getWidth());
        updateWidgetBackupBounds();
        LayoutUtils.layoutContainer(this);
    }

    public void adjustCreatorsHeight(double d)
    {
        if(getComponentCount() == 0)
        {
            toData().setContainerHeight(getHeight());
            return;
        }
        updateWidgetBackupBounds();
        int i = toData().getCompInterval();
        if(i > 0 && hasCalGap)
        {
            moveCompInterval(getAcualInterval());
            updateCompsWidget();
        }
        layoutHeightResize(d);
        if(d < 0.0D && getNeedAddHeight() > 0)
        {
            setSize(getWidth(), getHeight() + getNeedAddHeight());
            modifyEdgemostCreator(false);
        }
        addCompInterval(getAcualInterval());
        updateCompsWidget();
        toData().setContainerHeight(getHeight());
        updateWidgetBackupBounds();
        LayoutUtils.layoutContainer(this);
    }

    public XLayoutContainer getOuterLayout()
    {
        XWCardLayout xwcardlayout = (XWCardLayout)getBackupParent();
        return xwcardlayout.getBackupParent();
    }

    private void updateCompsWidget()
    {
        for(int i = 0; i < getComponentCount(); i++)
        {
            XCreator xcreator = getXCreator(i);
            com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget boundswidget = toData().getBoundsWidget(xcreator.toData());
            boundswidget.setBounds(getComponent(i).getBounds());
            boundswidget.setBackupBounds(getComponent(i).getBounds());
        }

    }

    public void moveCompInterval(int i)
    {
        if(i == 0)
            return;
        int j = i / 2;
        double d = getReferWidth();
        double d1 = getReferHeight();
        int k = 0;
        for(int l = getComponentCount(); k < l; k++)
        {
            Component component = getComponent(k);
            Rectangle rectangle = component.getBounds();
            Rectangle rectangle1 = new Rectangle(rectangle);
            if(rectangle.x > 0)
            {
                rectangle1.x -= j;
                rectangle1.width += j;
            }
            if((double)(rectangle.width + rectangle.x) < d)
                rectangle1.width += j;
            if(rectangle.y > 0)
            {
                rectangle1.y -= j;
                rectangle1.height += j;
            }
            if((double)(rectangle.height + rectangle.y) < d1)
                rectangle1.height += j;
            component.setBounds(rectangle1);
        }

        hasCalGap = false;
    }

    private double getReferWidth()
    {
        if(referDim != null)
            return referDim.getWidth();
        else
            return (double)getWidth();
    }

    private double getReferHeight()
    {
        if(referDim != null)
            return referDim.getHeight();
        else
            return (double)getHeight();
    }

    public void addCompInterval(int i)
    {
        if(i == 0)
            return;
        int j = i / 2;
        double d = getReferWidth();
        double d1 = getReferHeight();
        int k = 0;
        for(int l = getComponentCount(); k < l; k++)
        {
            Component component = getComponent(k);
            Rectangle rectangle = component.getBounds();
            Rectangle rectangle1 = new Rectangle(rectangle);
            if(rectangle.x > 0)
            {
                rectangle1.x += j;
                rectangle1.width -= j;
            }
            if((double)(rectangle.width + rectangle.x) < d)
                rectangle1.width -= j;
            if(rectangle.y > 0)
            {
                rectangle1.y += j;
                rectangle1.height -= j;
            }
            if((double)(rectangle.height + rectangle.y) < d1)
                rectangle1.height -= j;
            component.setBounds(rectangle1);
        }

        hasCalGap = true;
    }
}
