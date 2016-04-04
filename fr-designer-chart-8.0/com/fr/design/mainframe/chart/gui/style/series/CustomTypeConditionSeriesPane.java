// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.base.*;
import com.fr.chart.chartglyph.*;
import com.fr.data.condition.AbstractCondition;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.series.SeriesCondition.ChartConditionPane;
import com.fr.design.condition.LiteConditionPane;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.xcombox.MarkerComboBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.stable.CoreConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class CustomTypeConditionSeriesPane extends BasicBeanPane
{
    private class CustomTypeLineSeriesPane extends BasicBeanPane
    {

        private UIButtonGroup positionGroup;
        protected UICheckBox isCurve;
        protected UIButtonGroup isNullValueBreak;
        protected LineComboBox lineStyle;
        protected MarkerComboBox markerPane;
        final CustomTypeConditionSeriesPane this$0;

        public void populateBean(CustomAttr customattr)
        {
            AttrLineSeries attrlineseries = new AttrLineSeries();
            if(customattr.getExisted(com/fr/chart/base/AttrLineSeries) != null)
                attrlineseries = (AttrLineSeries)customattr.getExisted(com/fr/chart/base/AttrLineSeries);
            isCurve.setSelected(attrlineseries.isCurve());
            isNullValueBreak.setSelectedIndex(attrlineseries.isNullValueBreak() ? 0 : 1);
            lineStyle.setSelectedLineStyle(attrlineseries.getLineStyle());
            markerPane.setSelectedMarker(MarkerFactory.createMarker(attrlineseries.getMarkerType()));
            positionGroup.setSelectedItem(attrlineseries.getAxisPosition().getAxisPosition());
            customattr.removeAll();
            customattr.addDataSeriesCondition(attrlineseries);
        }

        public void updateBean(CustomAttr customattr)
        {
            customattr.removeAll();
            AttrLineSeries attrlineseries = new AttrLineSeries();
            customattr.addDataSeriesCondition(attrlineseries);
            attrlineseries.setCurve(isCurve.isSelected());
            attrlineseries.setNullValueBreak(isNullValueBreak.getSelectedIndex() == 0);
            attrlineseries.setLineStyle(lineStyle.getSelectedLineStyle());
            attrlineseries.setMarkerType(markerPane.getSelectedMarkder().getMarkerType());
            attrlineseries.setAxisPosition(ChartAxisPosition.parse((String)positionGroup.getSelectedItem()));
        }

        public CustomAttr updateBean()
        {
            return new CustomAttr();
        }

        protected String title4PopupWindow()
        {
            return Inter.getLocText("FR-Chart-Series_Config");
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((CustomAttr)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((CustomAttr)obj);
        }

        public CustomTypeLineSeriesPane()
        {
            this$0 = CustomTypeConditionSeriesPane.this;
            super();
            UILabel uilabel = new UILabel(Inter.getLocText("FR-Chart-Axis_Choose"));
            String as[] = {
                Inter.getLocText("ChartF-MainAxis"), Inter.getLocText("ChartF-SecondAxis")
            };
            String as1[] = {
                ChartAxisPosition.AXIS_LEFT.getAxisPosition(), ChartAxisPosition.AXIS_RIGHT.getAxisPosition()
            };
            positionGroup = new UIButtonGroup(as, as1);
            positionGroup.setAllToolTips(as);
            positionGroup.setSelectedItem(ChartAxisPosition.AXIS_LEFT.getAxisPosition());
            JPanel jpanel = new JPanel(FRGUIPaneFactory.createLabelFlowLayout());
            jpanel.add(uilabel);
            jpanel.add(positionGroup);
            isCurve = new UICheckBox(Inter.getLocText("Chart_Curve"));
            lineStyle = new LineComboBox(CoreConstants.STRIKE_LINE_STYLE_ARRAY_4_CHART);
            markerPane = new MarkerComboBox(MarkerFactory.getMarkerArray());
            isCurve.setPreferredSize(new Dimension(150, 20));
            lineStyle.setPreferredSize(new Dimension(150, 20));
            markerPane.setPreferredSize(new Dimension(150, 20));
            String as2[] = {
                Inter.getLocText("Chart_Null_Value_Break"), Inter.getLocText("Chart_Null_Value_Continue")
            };
            Boolean aboolean[] = {
                Boolean.valueOf(true), Boolean.valueOf(false)
            };
            isNullValueBreak = new UIButtonGroup(as2, aboolean);
            double d = -2D;
            double ad[] = {
                d, d
            };
            double ad1[] = {
                d, d, d, d, d
            };
            Component acomponent[][] = {
                {
                    jpanel, null
                }, {
                    new BoldFontTextLabel(Inter.getLocText("Chart_Line_Style")), isCurve
                }, {
                    new BoldFontTextLabel(Inter.getLocText("Line-Style")), lineStyle
                }, {
                    new BoldFontTextLabel(Inter.getLocText("FR-Chart-Marker_Type")), markerPane
                }, {
                    new BoldFontTextLabel(Inter.getLocText("Null_Value_Show")), isNullValueBreak
                }
            };
            setLayout(new BorderLayout());
            add(TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad), "North");
        }
    }

    private class CustomTypeAreaSeriesPane extends BasicBeanPane
    {

        private UICheckBox isCurve;
        protected MarkerComboBox markerPane;
        private UIButtonGroup positionGroup;
        final CustomTypeConditionSeriesPane this$0;

        public void populateBean(CustomAttr customattr)
        {
            AttrAreaSeries attrareaseries = new AttrAreaSeries();
            if(customattr.getExisted(com/fr/chart/base/AttrAreaSeries) != null)
                attrareaseries = (AttrAreaSeries)customattr.getExisted(com/fr/chart/base/AttrAreaSeries);
            isCurve.setSelected(attrareaseries.isCurve());
            markerPane.setSelectedMarker(MarkerFactory.createMarker(attrareaseries.getMarkerType()));
            positionGroup.setSelectedItem(attrareaseries.getAxisPosition().getAxisPosition());
            customattr.removeAll();
            customattr.addDataSeriesCondition(attrareaseries);
        }

        public void updateBean(CustomAttr customattr)
        {
            customattr.removeAll();
            AttrAreaSeries attrareaseries = new AttrAreaSeries();
            customattr.addDataSeriesCondition(attrareaseries);
            attrareaseries.setCurve(isCurve.isSelected());
            attrareaseries.setMarkerType(markerPane.getSelectedMarkder().getMarkerType());
            attrareaseries.setAxisPosition(ChartAxisPosition.parse((String)positionGroup.getSelectedItem()));
        }

        public CustomAttr updateBean()
        {
            return new CustomAttr();
        }

        protected String title4PopupWindow()
        {
            return Inter.getLocText("FR-Chart-Series_Config");
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((CustomAttr)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((CustomAttr)obj);
        }

        public CustomTypeAreaSeriesPane()
        {
            this$0 = CustomTypeConditionSeriesPane.this;
            super();
            UILabel uilabel = new UILabel(Inter.getLocText("FR-Chart-Axis_Choose"));
            String as[] = {
                Inter.getLocText("ChartF-MainAxis"), Inter.getLocText("ChartF-SecondAxis")
            };
            String as1[] = {
                ChartAxisPosition.AXIS_LEFT.getAxisPosition(), ChartAxisPosition.AXIS_RIGHT.getAxisPosition()
            };
            positionGroup = new UIButtonGroup(as, as1);
            positionGroup.setAllToolTips(as);
            positionGroup.setSelectedItem(ChartAxisPosition.AXIS_LEFT.getAxisPosition());
            JPanel jpanel = new JPanel(FRGUIPaneFactory.createLabelFlowLayout());
            jpanel.add(uilabel);
            jpanel.add(positionGroup);
            isCurve = new UICheckBox(Inter.getLocText("FR-Chart-Curve_Line"));
            markerPane = new MarkerComboBox(MarkerFactory.getMarkerArray());
            markerPane.setPreferredSize(new Dimension(150, 20));
            double d = -2D;
            double ad[] = {
                d, d
            };
            double ad1[] = {
                d, d, d
            };
            Component acomponent[][] = {
                {
                    jpanel, null
                }, {
                    new UILabel(Inter.getLocText("FR-Chart-Line_Style")), isCurve
                }, {
                    new BoldFontTextLabel(Inter.getLocText("FR-Chart-Marker_Type")), markerPane
                }
            };
            setLayout(new BorderLayout());
            add(TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad), "North");
        }
    }

    private class CustomTypeBar3DSeriesPane extends BasicBeanPane
    {

        private UIButtonGroup positionGroup;
        final CustomTypeConditionSeriesPane this$0;

        public void populateBean(CustomAttr customattr)
        {
            AttrAxisPosition attraxisposition = new AttrAxisPosition(ChartAxisPosition.AXIS_RIGHT);
            if(customattr.getExisted(com/fr/chart/base/AttrAxisPosition) != null)
                attraxisposition = (AttrAxisPosition)customattr.getExisted(com/fr/chart/base/AttrAxisPosition);
            positionGroup.setSelectedItem(attraxisposition.getAxisPosition().getAxisPosition());
            customattr.removeAll();
            customattr.addDataSeriesCondition(attraxisposition);
        }

        public void updateBean(CustomAttr customattr)
        {
            customattr.removeAll();
            AttrAxisPosition attraxisposition = new AttrAxisPosition();
            attraxisposition.setAxisPosition(ChartAxisPosition.parse((String)positionGroup.getSelectedItem()));
            customattr.addDataSeriesCondition(attraxisposition);
        }

        public CustomAttr updateBean()
        {
            return new CustomAttr();
        }

        protected String title4PopupWindow()
        {
            return Inter.getLocText("FR-Chart-Series_Config");
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((CustomAttr)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((CustomAttr)obj);
        }

        public CustomTypeBar3DSeriesPane()
        {
            this$0 = CustomTypeConditionSeriesPane.this;
            super();
            UILabel uilabel = new UILabel(Inter.getLocText("FR-Chart-Axis_Choose"));
            String as[] = {
                Inter.getLocText("ChartF-MainAxis"), Inter.getLocText("ChartF-SecondAxis")
            };
            String as1[] = {
                ChartAxisPosition.AXIS_LEFT.getAxisPosition(), ChartAxisPosition.AXIS_RIGHT.getAxisPosition()
            };
            positionGroup = new UIButtonGroup(as, as1);
            positionGroup.setAllToolTips(as);
            positionGroup.setSelectedItem(ChartAxisPosition.AXIS_RIGHT.getAxisPosition());
            setLayout(FRGUIPaneFactory.createLabelFlowLayout());
            add(uilabel);
            add(positionGroup);
        }
    }

    private class CustomTypeBarSeriesPane extends BasicBeanPane
    {

        private static final double HUNDRED = 100D;
        private static final double MAX_TIME = 5D;
        private UIButtonGroup positionGroup;
        private UINumberDragPane seriesGap;
        private UINumberDragPane categoryGap;
        final CustomTypeConditionSeriesPane this$0;

        public void populateBean(CustomAttr customattr)
        {
            AttrBarSeries attrbarseries = new AttrBarSeries();
            if(customattr.getExisted(com/fr/chart/base/AttrBarSeries) != null)
                attrbarseries = (AttrBarSeries)customattr.getExisted(com/fr/chart/base/AttrBarSeries);
            seriesGap.populateBean(Double.valueOf(attrbarseries.getSeriesOverlapPercent() * 100D));
            categoryGap.populateBean(Double.valueOf(attrbarseries.getCategoryIntervalPercent() * 100D));
            positionGroup.setSelectedItem(attrbarseries.getAxisPosition().getAxisPosition());
            customattr.removeAll();
            customattr.addDataSeriesCondition(attrbarseries);
        }

        public void updateBean(CustomAttr customattr)
        {
            customattr.removeAll();
            AttrBarSeries attrbarseries = new AttrBarSeries();
            attrbarseries.setAxisPosition(ChartAxisPosition.parse((String)positionGroup.getSelectedItem()));
            attrbarseries.setSeriesOverlapPercent(seriesGap.updateBean().doubleValue() / 100D);
            attrbarseries.setCategoryIntervalPercent(categoryGap.updateBean().doubleValue() / 100D);
            customattr.addDataSeriesCondition(attrbarseries);
        }

        public CustomAttr updateBean()
        {
            return new CustomAttr();
        }

        protected String title4PopupWindow()
        {
            return Inter.getLocText("FR-Chart-Series_Config");
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((CustomAttr)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((CustomAttr)obj);
        }

        public CustomTypeBarSeriesPane()
        {
            this$0 = CustomTypeConditionSeriesPane.this;
            super();
            UILabel uilabel = new UILabel(Inter.getLocText("FR-Chart-Axis_Choose"));
            String as[] = {
                Inter.getLocText("ChartF-MainAxis"), Inter.getLocText("ChartF-SecondAxis")
            };
            String as1[] = {
                ChartAxisPosition.AXIS_LEFT.getAxisPosition(), ChartAxisPosition.AXIS_RIGHT.getAxisPosition()
            };
            positionGroup = new UIButtonGroup(as, as1);
            positionGroup.setAllToolTips(as);
            positionGroup.setSelectedItem(ChartAxisPosition.AXIS_LEFT.getAxisPosition());
            JPanel jpanel = new JPanel(FRGUIPaneFactory.createLabelFlowLayout());
            jpanel.add(uilabel);
            jpanel.add(positionGroup);
            seriesGap = new UINumberDragPane(-100D, 100D);
            categoryGap = new UINumberDragPane(0.0D, 500D);
            seriesGap.setPreferredSize(new Dimension(150, 20));
            categoryGap.setPreferredSize(new Dimension(150, 20));
            double d = -2D;
            double ad[] = {
                d, d
            };
            double ad1[] = {
                d, d
            };
            Component acomponent[][] = {
                {
                    new UILabel(Inter.getLocText("FR-Chart-Series_Gap")), seriesGap
                }, {
                    new UILabel(Inter.getLocText("FR-Chart-Category_Gap")), categoryGap
                }
            };
            setLayout(new BoxLayout(this, 1));
            add(jpanel);
            add(TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad));
        }
    }


    private static final int STYLE_WIDTH = 490;
    private static final int STYLE_HEIGHT = 50;
    private CustomAttr editing;
    protected UIRadioButton barRadioButton;
    protected UIRadioButton barStackButton;
    protected UIRadioButton bar3DRadioButton;
    protected UIRadioButton bar3DStackButton;
    protected UIRadioButton lineRadioButton;
    protected UIRadioButton areaStackButton;
    private CardLayout cardLayout;
    private JPanel cardPane;
    protected LiteConditionPane liteConditionPane;
    BasicBeanPane barPane;
    BasicBeanPane barStackPane;
    BasicBeanPane bar3DPane;
    BasicBeanPane bar3DStackPane;
    BasicBeanPane linePane;
    BasicBeanPane areaPane;

    public CustomTypeConditionSeriesPane()
    {
        editing = new CustomAttr();
        barPane = null;
        barStackPane = null;
        bar3DPane = null;
        bar3DStackPane = null;
        linePane = null;
        areaPane = null;
        setLayout(new BorderLayout());
        liteConditionPane = new ChartConditionPane();
        JPanel jpanel = new JPanel();
        jpanel.setLayout(new BoxLayout(jpanel, 1));
        jpanel.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FR-Chart-Series_Config"), null));
        jpanel.add(liteConditionPane);
        setLayout(new BorderLayout());
        add(getCustomAttrPane(), "North");
        add(jpanel, "Center");
        initListener();
    }

    private void initListener()
    {
        initBarListener();
        lineRadioButton.addActionListener(new ActionListener() {

            final CustomTypeConditionSeriesPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(lineRadioButton.isSelected())
                {
                    cardLayout.show(cardPane, "Line");
                    linePane.populateBean(editing);
                }
            }

            
            {
                this$0 = CustomTypeConditionSeriesPane.this;
                super();
            }
        }
);
        areaStackButton.addActionListener(new ActionListener() {

            final CustomTypeConditionSeriesPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(areaStackButton.isSelected())
                {
                    cardLayout.show(cardPane, "Area");
                    areaPane.populateBean(editing);
                }
            }

            
            {
                this$0 = CustomTypeConditionSeriesPane.this;
                super();
            }
        }
);
    }

    private void initBarListener()
    {
        barRadioButton.addActionListener(new ActionListener() {

            final CustomTypeConditionSeriesPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(barRadioButton.isSelected())
                {
                    cardLayout.show(cardPane, "Bar");
                    barPane.populateBean(editing);
                }
            }

            
            {
                this$0 = CustomTypeConditionSeriesPane.this;
                super();
            }
        }
);
        barStackButton.addActionListener(new ActionListener() {

            final CustomTypeConditionSeriesPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(barStackButton.isSelected())
                {
                    cardLayout.show(cardPane, "BarStack");
                    barStackPane.populateBean(editing);
                }
            }

            
            {
                this$0 = CustomTypeConditionSeriesPane.this;
                super();
            }
        }
);
        bar3DRadioButton.addActionListener(new ActionListener() {

            final CustomTypeConditionSeriesPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(bar3DRadioButton.isSelected())
                {
                    cardLayout.show(cardPane, "Bar3D");
                    bar3DPane.populateBean(editing);
                }
            }

            
            {
                this$0 = CustomTypeConditionSeriesPane.this;
                super();
            }
        }
);
        bar3DStackButton.addActionListener(new ActionListener() {

            final CustomTypeConditionSeriesPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(bar3DStackButton.isSelected())
                {
                    cardLayout.show(cardPane, "Bar3DStack");
                    bar3DStackPane.populateBean(editing);
                }
            }

            
            {
                this$0 = CustomTypeConditionSeriesPane.this;
                super();
            }
        }
);
    }

    private JPanel getCustomAttrPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createBoxFlowInnerContainer_S_Pane();
        jpanel.add(barRadioButton = new UIRadioButton(Inter.getLocText("ChartF-Column")));
        jpanel.add(barStackButton = new UIRadioButton(Inter.getLocText("I-BarStyle_NormalStack")));
        jpanel.add(bar3DRadioButton = new UIRadioButton(Inter.getLocText("FR-Chart-Bar3D_Chart")));
        jpanel.add(bar3DStackButton = new UIRadioButton(Inter.getLocText("FR-Chart-Bar3DStack_Chart")));
        jpanel.add(lineRadioButton = new UIRadioButton(Inter.getLocText("ChartF-Line")));
        jpanel.add(areaStackButton = new UIRadioButton(Inter.getLocText("I-AreaStyle_Stack")));
        ButtonGroup buttongroup = new ButtonGroup();
        buttongroup.add(barRadioButton);
        buttongroup.add(barStackButton);
        buttongroup.add(bar3DRadioButton);
        buttongroup.add(bar3DStackButton);
        buttongroup.add(lineRadioButton);
        buttongroup.add(areaStackButton);
        barRadioButton.setSelected(true);
        jpanel.setPreferredSize(new Dimension(490, 50));
        JPanel jpanel1 = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
        cardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        jpanel1.add(cardPane);
        cardLayout = new CardLayout();
        cardPane.setLayout(cardLayout);
        cardPane.add(barPane = new CustomTypeBarSeriesPane(), "Bar");
        cardPane.add(barStackPane = new CustomTypeBarSeriesPane(), "BarStack");
        cardPane.add(bar3DPane = new CustomTypeBar3DSeriesPane(), "Bar3D");
        cardPane.add(bar3DStackPane = new CustomTypeBar3DSeriesPane(), "Bar3DStack");
        cardPane.add(linePane = new CustomTypeLineSeriesPane(), "Line");
        cardPane.add(areaPane = new CustomTypeAreaSeriesPane(), "Area");
        cardLayout.show(cardPane, "Bar");
        JPanel jpanel2 = new JPanel();
        jpanel2.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FR-Chart-Choose_Style"), null));
        jpanel2.setLayout(new BoxLayout(jpanel2, 1));
        jpanel2.add(jpanel);
        jpanel2.add(jpanel1);
        return jpanel2;
    }

    public void populateBean(CustomAttr customattr)
    {
        static class _cls7
        {

            static final int $SwitchMap$com$fr$chart$base$ChartCustomRendererType[];

            static 
            {
                $SwitchMap$com$fr$chart$base$ChartCustomRendererType = new int[ChartCustomRendererType.values().length];
                try
                {
                    $SwitchMap$com$fr$chart$base$ChartCustomRendererType[ChartCustomRendererType.BAR_RENDERER.ordinal()] = 1;
                }
                catch(NoSuchFieldError nosuchfielderror) { }
                try
                {
                    $SwitchMap$com$fr$chart$base$ChartCustomRendererType[ChartCustomRendererType.BAR_STACK.ordinal()] = 2;
                }
                catch(NoSuchFieldError nosuchfielderror1) { }
                try
                {
                    $SwitchMap$com$fr$chart$base$ChartCustomRendererType[ChartCustomRendererType.BAR3D.ordinal()] = 3;
                }
                catch(NoSuchFieldError nosuchfielderror2) { }
                try
                {
                    $SwitchMap$com$fr$chart$base$ChartCustomRendererType[ChartCustomRendererType.BAR3D_STACK.ordinal()] = 4;
                }
                catch(NoSuchFieldError nosuchfielderror3) { }
                try
                {
                    $SwitchMap$com$fr$chart$base$ChartCustomRendererType[ChartCustomRendererType.LINE_RENDERER.ordinal()] = 5;
                }
                catch(NoSuchFieldError nosuchfielderror4) { }
                try
                {
                    $SwitchMap$com$fr$chart$base$ChartCustomRendererType[ChartCustomRendererType.AREA_STACK.ordinal()] = 6;
                }
                catch(NoSuchFieldError nosuchfielderror5) { }
            }
        }

        switch(_cls7..SwitchMap.com.fr.chart.base.ChartCustomRendererType[customattr.getRenderer().ordinal()])
        {
        case 1: // '\001'
            barRadioButton.setSelected(true);
            cardLayout.show(cardPane, "Bar");
            barPane.populateBean(customattr);
            break;

        case 2: // '\002'
            barStackButton.setSelected(true);
            cardLayout.show(cardPane, "BarStack");
            barStackPane.populateBean(customattr);
            break;

        case 3: // '\003'
            bar3DRadioButton.setSelected(true);
            cardLayout.show(cardPane, "Bar3D");
            bar3DPane.populateBean(customattr);
            break;

        case 4: // '\004'
            bar3DStackButton.setSelected(true);
            cardLayout.show(cardPane, "Bar3DStack");
            bar3DStackPane.populateBean(customattr);
            break;

        case 5: // '\005'
            lineRadioButton.setSelected(true);
            cardLayout.show(cardPane, "Line");
            linePane.populateBean(customattr);
            break;

        case 6: // '\006'
            areaStackButton.setSelected(true);
            cardLayout.show(cardPane, "Area");
            areaPane.populateBean(customattr);
            break;

        default:
            barRadioButton.setSelected(true);
            cardLayout.show(cardPane, "Bar");
            barPane.populateBean(customattr);
            break;
        }
        liteConditionPane.populateBean(customattr.getCondition());
    }

    public CustomAttr updateBean()
    {
        return new CustomAttr();
    }

    public void updateBean(CustomAttr customattr)
    {
        customattr.removeAll();
        if(barRadioButton.isSelected())
        {
            customattr.setRenderer(ChartCustomRendererType.BAR_RENDERER);
            barPane.updateBean(customattr);
        } else
        if(lineRadioButton.isSelected())
        {
            customattr.setRenderer(ChartCustomRendererType.LINE_RENDERER);
            linePane.updateBean(customattr);
        } else
        if(barStackButton.isSelected())
        {
            customattr.setRenderer(ChartCustomRendererType.BAR_STACK);
            barStackPane.updateBean(customattr);
        } else
        if(areaStackButton.isSelected())
        {
            customattr.setRenderer(ChartCustomRendererType.AREA_STACK);
            areaPane.updateBean(customattr);
        } else
        if(bar3DRadioButton.isSelected())
        {
            customattr.setRenderer(ChartCustomRendererType.BAR3D);
            bar3DPane.updateBean(customattr);
        } else
        if(bar3DStackButton.isSelected())
        {
            customattr.setRenderer(ChartCustomRendererType.BAR3D_STACK);
            bar3DStackPane.updateBean(customattr);
        }
        customattr.setCondition((AbstractCondition)liteConditionPane.updateBean());
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("FR-Chart-Series_Config");
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((CustomAttr)obj);
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((CustomAttr)obj);
    }



}
