// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.design.designer.beans.events.CreatorEventListenerTable;
import com.fr.design.designer.creator.*;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.frpane.UINumberSlidePane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.scrollruler.*;
import com.fr.design.utils.ComponentUtils;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.form.ui.container.WFitLayout;
import com.fr.general.FRScreen;
import com.fr.general.Inter;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JComponent;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Referenced classes of package com.fr.design.mainframe:
//            FormScrollBar, FormDesigner, FormDesignerModeForSpecial, JTemplate

public class FormArea extends JComponent
    implements ScrollRulerComponent
{
    private class FormRulerLayout extends RulerLayout
    {

        private int DESIGNERWIDTH;
        private int DESIGNERHEIGHT;
        private int TOPGAP;
        final FormArea this$0;

        public void layoutContainer(Container container)
        {
            synchronized(container.getTreeLock())
            {
                Insets insets = container.getInsets();
                int i = insets.top;
                int j = insets.left;
                int k = container.getHeight() - insets.bottom;
                int l = container.getWidth() - insets.right;
                Dimension dimension = resizePane.getPreferredSize();
                Dimension dimension1 = null;
                Dimension dimension2 = null;
                resizePane.setBounds(j, k - dimension.height, l, dimension.height);
                if(horScrollBar != null)
                {
                    dimension1 = horScrollBar.getPreferredSize();
                    dimension2 = verScrollBar.getPreferredSize();
                    horScrollBar.setBounds(j, k - dimension1.height - dimension.height, l - BARSIZE, dimension1.height);
                    verScrollBar.setBounds(l - dimension2.width, i, dimension2.width, k - BARSIZE - dimension.height);
                }
                FormDesigner formdesigner = (FormDesigner)designer;
                XLayoutContainer xlayoutcontainer = formdesigner.getRootComponent();
                if(xlayoutcontainer.acceptType(new Class[] {
        com/fr/design/designer/creator/XWFitLayout
    }))
                {
                    DESIGNERWIDTH = xlayoutcontainer.getWidth();
                    DESIGNERHEIGHT = formdesigner.hasWAbsoluteLayout() ? xlayoutcontainer.getHeight() + formdesigner.getParaHeight() : xlayoutcontainer.getHeight();
                }
                Rectangle rectangle = new Rectangle(j + (l - DESIGNERWIDTH) / 2, TOPGAP, l, k);
                if(isValid)
                {
                    int i1 = k - dimension1.height - dimension.height - TOPGAP * 2;
                    int j1 = l - dimension2.width;
                    DESIGNERWIDTH = DESIGNERWIDTH <= j1 ? DESIGNERWIDTH : j1;
                    DESIGNERHEIGHT = DESIGNERHEIGHT <= i1 ? DESIGNERHEIGHT : i1;
                    int k1 = j + (verScrollBar.getX() - DESIGNERWIDTH) / 2;
                    rectangle = new Rectangle(k1, TOPGAP, DESIGNERWIDTH, DESIGNERHEIGHT);
                }
                designer.setBounds(rectangle);
            }
        }

        public FormRulerLayout()
        {
            this$0 = FormArea.this;
            super();
            DESIGNERWIDTH = 960;
            DESIGNERHEIGHT = 540;
            TOPGAP = 8;
        }
    }


    private static final double SLIDER_FLOAT = 120D;
    private static final double SLIDER_MIN = 60D;
    public static final double DEFAULT_SLIDER = 100D;
    private static final int ROTATIONS = 50;
    private FormDesigner designer;
    private int horizontalValue;
    private int verticalValue;
    private int verticalMax;
    private int horicalMax;
    private FormScrollBar verScrollBar;
    private FormScrollBar horScrollBar;
    private UINumberField widthPane;
    private UINumberField heightPane;
    private UINumberSlidePane slidePane;
    private boolean isValid;
    private double START_VALUE;
    private double screenValue;

    public FormScrollBar getHorScrollBar()
    {
        return horScrollBar;
    }

    public void setHorScrollBar(FormScrollBar formscrollbar)
    {
        horScrollBar = formscrollbar;
    }

    public FormArea(FormDesigner formdesigner)
    {
        this(formdesigner, true);
    }

    public FormArea(FormDesigner formdesigner, boolean flag)
    {
        horizontalValue = 0;
        verticalValue = 0;
        verticalMax = 0;
        horicalMax = 0;
        isValid = true;
        START_VALUE = 100D;
        designer = formdesigner;
        designer.setParent(this);
        isValid = flag;
        verScrollBar = new FormScrollBar(1, this);
        horScrollBar = new FormScrollBar(0, this);
        if(flag)
        {
            setLayout(new FormRulerLayout());
            formdesigner.setBorder(new LineBorder(new Color(198, 198, 198)));
            add("Center", formdesigner);
            addFormSize();
            add("Vertical", verScrollBar);
            add("Horizontal", horScrollBar);
            enableEvents(0x20000L);
        } else
        {
            setLayout(new RulerLayout());
            add("Center", formdesigner);
            addFormRuler();
        }
        setFocusTraversalKeysEnabled(false);
    }

    private void addFormSize()
    {
        double d = -1D;
        double d1 = -2D;
        double ad[] = {
            d
        };
        double ad1[] = {
            d1, d, d1, d1, d1, d1, d1, d, d1
        };
        UILabel uilabel = new UILabel("form");
        uilabel.setPreferredSize(new Dimension(200, 0));
        widthPane = new UINumberField();
        widthPane.setPreferredSize(new Dimension(60, 0));
        heightPane = new UINumberField();
        heightPane.setPreferredSize(new Dimension(60, 0));
        slidePane = new UINumberSlidePane(60D, 120D);
        slidePane.setPreferredSize(new Dimension(200, 0));
        javax.swing.JPanel jpanel = TableLayoutHelper.createCommonTableLayoutPane(new JComponent[][] {
            new JComponent[] {
                uilabel, new UILabel(), widthPane, new UILabel(Inter.getLocText("FR-Designer_Indent-Pixel")), new UILabel("x"), heightPane, new UILabel(Inter.getLocText("FR-Designer_Indent-Pixel")), new UILabel(), slidePane
            }
        }, ad, ad1, 8D);
        add("Bottom", jpanel);
        setWidgetsConfig();
        slidePane.setEnabled(false);
        slidePane.setVisible(false);
        initCalculateSize();
    }

    private void setWidgetsConfig()
    {
        UINumberField _tmp = widthPane;
        widthPane.setHorizontalAlignment(0);
        UINumberField _tmp1 = heightPane;
        heightPane.setHorizontalAlignment(0);
        widthPane.setMaxDecimalLength(0);
        heightPane.setMaxDecimalLength(0);
        widthPane.setValue(designer.getRootComponent().getWidth());
        heightPane.setValue(designer.getRootComponent().getHeight());
        addWidthPaneListener();
        addHeightPaneListener();
    }

    private void initTransparent()
    {
        initCalculateSize();
        slidePane.addChangeListener(new ChangeListener() {

            final FormArea this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                double d = ((UINumberSlidePane)changeevent.getSource()).getValue();
                reCalculateRoot(d, true);
                JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                if(jtemplate != null)
                    jtemplate.fireTargetModified();
            }

            
            {
                this$0 = FormArea.this;
                super();
            }
        }
);
    }

    public double getScreenValue()
    {
        return screenValue;
    }

    public void setScreenValue(double d)
    {
        screenValue = d;
    }

    private void initCalculateSize()
    {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        screenValue = FRScreen.getByDimension(dimension).getValue().doubleValue();
        XLayoutContainer xlayoutcontainer = designer.getRootComponent();
        if(xlayoutcontainer.acceptType(new Class[] {
    com/fr/design/designer/creator/XWFitLayout
}))
        {
            XWFitLayout xwfitlayout = (XWFitLayout)xlayoutcontainer;
            if(screenValue != 100D)
            {
                reCalculateRoot(screenValue, true);
            } else
            {
                int i = xwfitlayout.getAcualInterval();
                xwfitlayout.addCompInterval(i);
            }
        }
        LayoutUtils.layoutContainer(xlayoutcontainer);
    }

    private void addWidthPaneListener()
    {
        widthPane.addActionListener(new ActionListener() {

            final FormArea this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                int i = (int)((UINumberField)actionevent.getSource()).getValue();
                changeWidthPaneValue(i);
            }

            
            {
                this$0 = FormArea.this;
                super();
            }
        }
);
        widthPane.addFocusListener(new FocusAdapter() {

            final FormArea this$0;

            public void focusLost(FocusEvent focusevent)
            {
                int i = (int)((UINumberField)focusevent.getSource()).getValue();
                changeWidthPaneValue(i);
            }

            
            {
                this$0 = FormArea.this;
                super();
            }
        }
);
    }

    private void changeWidthPaneValue(int i)
    {
        XWFitLayout xwfitlayout = (XWFitLayout)designer.getRootComponent();
        if(i != xwfitlayout.toData().getContainerWidth())
        {
            reCalculateWidth(i);
            designer.getEditListenerTable().fireCreatorModified(5);
        }
    }

    private void addHeightPaneListener()
    {
        heightPane.addActionListener(new ActionListener() {

            final FormArea this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                int i = (int)((UINumberField)actionevent.getSource()).getValue();
                changeHeightPaneValue(i);
            }

            
            {
                this$0 = FormArea.this;
                super();
            }
        }
);
        heightPane.addFocusListener(new FocusAdapter() {

            final FormArea this$0;

            public void focusLost(FocusEvent focusevent)
            {
                int i = (int)((UINumberField)focusevent.getSource()).getValue();
                changeHeightPaneValue(i);
            }

            
            {
                this$0 = FormArea.this;
                super();
            }
        }
);
    }

    private void changeHeightPaneValue(int i)
    {
        XWFitLayout xwfitlayout = (XWFitLayout)designer.getRootComponent();
        if(i != xwfitlayout.toData().getContainerHeight())
        {
            reCalculateHeight(i);
            designer.getEditListenerTable().fireCreatorModified(5);
        }
    }

    private void reCalculateWidth(int i)
    {
        XLayoutContainer xlayoutcontainer = designer.getRootComponent();
        if(xlayoutcontainer.acceptType(new Class[] {
    com/fr/design/designer/creator/XWFitLayout
}))
        {
            XWFitLayout xwfitlayout = (XWFitLayout)xlayoutcontainer;
            Dimension dimension = new Dimension(xwfitlayout.toData().getContainerWidth(), xwfitlayout.toData().getContainerHeight());
            Rectangle rectangle = new Rectangle(dimension);
            xwfitlayout.setBackupBound(rectangle);
            int j = i - rectangle.width;
            if(j == 0)
                return;
            double d = (double)j / (double)rectangle.width;
            if(d < 0.0D && !xwfitlayout.canReduce(d))
            {
                widthPane.setValue(rectangle.width);
                return;
            }
            xwfitlayout.setSize(i, rectangle.height);
            xwfitlayout.adjustCreatorsWidth(d);
            if(xwfitlayout.getNeedAddWidth() > 0)
            {
                widthPane.setValue(xwfitlayout.getWidth());
                xwfitlayout.setNeedAddWidth(0);
            }
            doReCalculateRoot(i, rectangle.height, xwfitlayout);
        }
    }

    private void reCalculateHeight(int i)
    {
        XLayoutContainer xlayoutcontainer = designer.getRootComponent();
        if(xlayoutcontainer.acceptType(new Class[] {
    com/fr/design/designer/creator/XWFitLayout
}))
        {
            XWFitLayout xwfitlayout = (XWFitLayout)xlayoutcontainer;
            Dimension dimension = new Dimension(xwfitlayout.toData().getContainerWidth(), xwfitlayout.toData().getContainerHeight());
            Rectangle rectangle = new Rectangle(dimension);
            xwfitlayout.setBackupBound(rectangle);
            int j = i - rectangle.height;
            if(j == 0)
                return;
            double d = (double)j / (double)rectangle.height;
            if(d < 0.0D && !xwfitlayout.canReduce(d))
            {
                heightPane.setValue(rectangle.height);
                return;
            }
            xwfitlayout.setSize(rectangle.width, i);
            xwfitlayout.adjustCreatorsHeight(d);
            if(xwfitlayout.getNeedAddHeight() > 0)
            {
                heightPane.setValue(xwfitlayout.getHeight());
                xwfitlayout.setNeedAddHeight(0);
            }
            doReCalculateRoot(rectangle.width, i, xwfitlayout);
        }
    }

    private void doReCalculateRoot(int i, int j, XWFitLayout xwfitlayout)
    {
        START_VALUE = 100D;
        if(screenValue == 100D)
        {
            xwfitlayout.getParent().setSize(i, j + designer.getParaHeight());
            validate();
        } else
        {
            xwfitlayout.setBackupGap(screenValue / 100D);
            reCalculateRoot(screenValue, false);
        }
    }

    private void reCalculateRoot(double d, boolean flag)
    {
        if(d == START_VALUE)
            return;
        double d1 = (d - START_VALUE) / START_VALUE;
        XLayoutContainer xlayoutcontainer = designer.getRootComponent();
        if(xlayoutcontainer.acceptType(new Class[] {
    com/fr/design/designer/creator/XWFitLayout
}))
        {
            XWFitLayout xwfitlayout = (XWFitLayout)xlayoutcontainer;
            xwfitlayout.setContainerPercent(d / 100D);
            traverAndAdjust(xwfitlayout, d1);
            xwfitlayout.adjustCreatorsWhileSlide(d1);
            Dimension dimension = new Dimension(xwfitlayout.getWidth(), xwfitlayout.getHeight());
            if(xwfitlayout.getParent() != null)
            {
                int i = designer.getParaHeight();
                if(flag && i > 0)
                {
                    i += (int)((double)i * d1);
                    designer.setParaHeight(i);
                    XWBorderLayout xwborderlayout = (XWBorderLayout)xwfitlayout.getParent();
                    xwborderlayout.toData().setNorthSize(i);
                    xwborderlayout.removeAll();
                    xwborderlayout.add(designer.getParaComponent(), "North");
                    xwborderlayout.add(designer.getRootComponent(), "Center");
                }
                xwfitlayout.getParent().setSize(dimension.width, dimension.height + i);
                validate();
            }
            START_VALUE = d;
        }
    }

    private void traverAndAdjust(XCreator xcreator, double d)
    {
        for(int i = 0; i < xcreator.getComponentCount(); i++)
        {
            Component component = xcreator.getComponent(i);
            if(component instanceof XCreator)
            {
                XCreator xcreator1 = (XCreator)component;
                xcreator1.adjustCompSize(d);
                traverAndAdjust(xcreator1, d);
            }
        }

    }

    public void addFormRuler()
    {
        VerticalRuler verticalruler = new VerticalRuler(this);
        HorizontalRuler horizontalruler = new HorizontalRuler(this);
        add("VRuler", verticalruler);
        add("HRuler", horizontalruler);
    }

    protected void processMouseWheelEvent(MouseWheelEvent mousewheelevent)
    {
        int i = mousewheelevent.getID();
        switch(i)
        {
        case 507: 
            int j = mousewheelevent.getWheelRotation();
            int k = verScrollBar.getValue() + j * 50;
            k = Math.min(k, verticalMax);
            k = Math.max(0, k);
            doLayout();
            verScrollBar.setValue(k);
            break;
        }
    }

    public FormDesigner getFormEditor()
    {
        return designer;
    }

    private boolean shouldSetScrollValue(XCreator xcreator)
    {
        return !isValid || designer.isRoot(xcreator) || getDesignerWidth() >= designer.getRootComponent().getWidth();
    }

    public void scrollPathToVisible(XCreator xcreator)
    {
        xcreator.seleteRelatedComponent(xcreator);
        if(!ComponentUtils.isComponentVisible(xcreator) && !designer.isRoot(xcreator) && xcreator.toData().isVisible())
            designer.makeVisible(xcreator);
        if(shouldSetScrollValue(xcreator))
            return;
        Rectangle rectangle = ComponentUtils.getRelativeBounds(xcreator);
        int i = getDesignerWidth();
        if(rectangle.width <= i && rectangle.x < getHorizontalValue())
            horScrollBar.setValue(rectangle.x);
        else
        if(rectangle.x + rectangle.width > i)
            horScrollBar.setValue((rectangle.x + rectangle.width) - i);
        int j = getDesignerHeight();
        if(rectangle.height < j && rectangle.y < getVerticalValue())
            verScrollBar.setValue(rectangle.y);
        else
        if(rectangle.y + rectangle.height > j)
            verScrollBar.setValue((rectangle.y + rectangle.height) - j);
    }

    public void doLayout()
    {
        layout();
        if(isValid)
        {
            XLayoutContainer xlayoutcontainer = designer.getRootComponent();
            setScrollBarProperties(xlayoutcontainer.getWidth() - designer.getWidth(), horScrollBar);
            setScrollBarProperties((designer.getParaHeight() + xlayoutcontainer.getHeight()) - designer.getHeight(), verScrollBar);
        }
    }

    private void setScrollBarProperties(int i, FormScrollBar formscrollbar)
    {
        if(i == 0 && isScrollNotVisible(formscrollbar))
            return;
        if(i <= 0)
        {
            setScrollBarMax(0, formscrollbar);
            formscrollbar.setMaximum(0);
            formscrollbar.setValue(0);
            formscrollbar.setEnabled(false);
        } else
        {
            int j = verticalValue;
            setScrollBarMax(i, formscrollbar);
            formscrollbar.setEnabled(true);
            formscrollbar.setMaximum(i);
            formscrollbar.setValue(i);
            formscrollbar.setValue(j);
        }
    }

    private boolean isScrollNotVisible(FormScrollBar formscrollbar)
    {
        if(formscrollbar.getOrientation() == 1)
            return verticalMax == 0;
        else
            return horicalMax == 0;
    }

    private void setScrollBarMax(int i, FormScrollBar formscrollbar)
    {
        if(formscrollbar.getOrientation() == 1)
            verticalMax = i;
        else
            horicalMax = i;
    }

    public int getMinHeight()
    {
        return designer.getDesignerMode().getMinDesignHeight();
    }

    public int getMinWidth()
    {
        return designer.getDesignerMode().getMinDesignWidth();
    }

    public short getRulerLengthUnit()
    {
        return -1;
    }

    public int getHorizontalValue()
    {
        return horizontalValue;
    }

    public void setHorizontalValue(int i)
    {
        horizontalValue = i;
    }

    public int getVerticalValue()
    {
        return verticalValue;
    }

    public void setVerticalValue(int i)
    {
        verticalValue = i;
    }

    public int getDesignerHeight()
    {
        return designer.getHeight();
    }

    public int getDesignerWidth()
    {
        return designer.getWidth();
    }

    public double getWidthPaneValue()
    {
        return widthPane.getValue();
    }

    public void setWidthPaneValue(int i)
    {
        widthPane.setValue(i);
    }

    public void setHeightPaneValue(int i)
    {
        heightPane.setValue(i);
    }

    public double getHeightPaneValue()
    {
        return heightPane.getValue();
    }

    public double getSlideValue()
    {
        return screenValue;
    }

    public Dimension getAreaSize()
    {
        return new Dimension(horScrollBar.getMaximum(), verScrollBar.getMaximum());
    }

    public void setAreaSize(Dimension dimension, int i, int j, double d, double d1, 
            double d2)
    {
        verticalMax = (int)dimension.getHeight();
        horicalMax = (int)dimension.getHeight();
        if(d != widthPane.getValue())
        {
            widthPane.setValue(d);
            reCalculateWidth((int)d);
        }
        if(d1 != heightPane.getValue())
        {
            heightPane.setValue(d1);
            reCalculateHeight((int)d1);
        }
        if(designer.getRootComponent().acceptType(new Class[] {
    com/fr/design/designer/creator/XWFitLayout
}) && d2 == 100D)
        {
            XWFitLayout xwfitlayout = (XWFitLayout)designer.getRootComponent();
            xwfitlayout.moveContainerMargin();
            xwfitlayout.addCompInterval(xwfitlayout.getAcualInterval());
        } else
        if(designer.getRootComponent().acceptType(new Class[] {
    com/fr/design/designer/creator/XWFitLayout
}))
        {
            START_VALUE = 100D;
            reCalculateRoot(d2, true);
        }
    }

    public Point calculateScroll(int i, int j, int k, int l, int i1, int j1)
    {
        int k1 = j1 != 1 ? horicalMax : verticalMax;
        if(i == k1 + i1 && k > k1)
            return new Point(l, i);
        else
            return new Point(k, j);
    }




}
