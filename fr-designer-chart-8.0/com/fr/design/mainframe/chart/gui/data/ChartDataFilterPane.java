// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data;

import com.fr.base.Utils;
import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartdata.TopDefinition;
import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.style.AbstractChartTabPane;
import com.fr.design.mainframe.chart.gui.style.ThirdTabPane;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Referenced classes of package com.fr.design.mainframe.chart.gui.data:
//            PresentComboBox

public class ChartDataFilterPane extends ThirdTabPane
{
    private class CategoryFilterPaneWithOutPresentPane extends CategoryFilterPane
    {

        final ChartDataFilterPane this$0;

        protected JPanel createContentPane()
        {
            setLayout(new java.awt.BorderLayout());
            JPanel jpanel = new JPanel();
            add(jpanel, "North");
            jpanel.setLayout(new BoxLayout(jpanel, 1));
            jpanel.setPreferredSize(new java.awt.Dimension(200, 110));
            initOtherPane(jpanel);
            return jpanel;
        }

        public void populateBean(TopDefinition topdefinition)
        {
            if(topdefinition == null)
            {
                return;
            } else
            {
                populateBean4NoPresent(topdefinition);
                return;
            }
        }

        public void updateBean(TopDefinitionProvider topdefinitionprovider)
        {
            updateBean4NoPresent(topdefinitionprovider);
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((TopDefinitionProvider)obj);
        }

        public CategoryFilterPaneWithOutPresentPane(AbstractAttrNoScrollPane abstractattrnoscrollpane)
        {
            this$0 = ChartDataFilterPane.this;
            super(abstractattrnoscrollpane);
        }
    }

    private class SeriesFilterWithOutPresentPane extends SeriesFilterPane
    {

        final ChartDataFilterPane this$0;

        protected JPanel createContentPane()
        {
            setLayout(new java.awt.BorderLayout());
            JPanel jpanel = new JPanel();
            add(jpanel, "North");
            jpanel.setLayout(new BoxLayout(jpanel, 1));
            jpanel.setPreferredSize(new java.awt.Dimension(200, 110));
            initOtherPane(jpanel);
            return jpanel;
        }

        public void populateBean(TopDefinition topdefinition)
        {
            if(topdefinition == null)
            {
                return;
            } else
            {
                populate4NoPresent(topdefinition);
                return;
            }
        }

        public void updateBean(TopDefinition topdefinition)
        {
            update4NoPresent(topdefinition);
        }

        public SeriesFilterWithOutPresentPane(AbstractAttrNoScrollPane abstractattrnoscrollpane)
        {
            this$0 = ChartDataFilterPane.this;
            super(abstractattrnoscrollpane);
        }
    }

    private class SeriesFilterPane extends AbstractChartTabPane
    {

        private UICheckBox onlyPreData;
        private UITextField preDataNum;
        private UICheckBox notShowNull;
        private UICheckBox combineOther;
        private PresentComboBox present;
        private AbstractAttrNoScrollPane parent;
        final ChartDataFilterPane this$0;

        public java.awt.Dimension getPreferredSize()
        {
            java.awt.Dimension dimension = super.getPreferredSize();
            dimension.height = 130;
            return dimension;
        }

        protected JPanel createContentPane()
        {
            setLayout(new java.awt.BorderLayout());
            JPanel jpanel = new JPanel();
            add(jpanel, "North");
            jpanel.setLayout(new BoxLayout(jpanel, 1));
            jpanel.setPreferredSize(new java.awt.Dimension(200, 110));
            initOtherPane(jpanel);
            initPresentPane(jpanel);
            return jpanel;
        }

        protected void initOtherPane(JPanel jpanel)
        {
            JPanel jpanel1 = new JPanel();
            jpanel1.setLayout(new java.awt.FlowLayout(0));
            jpanel1.setPreferredSize(new java.awt.Dimension(200, 20));
            jpanel.add(jpanel1);
            onlyPreData = new UICheckBox(Inter.getLocText("FR-Chart-Data_OnlyUseBefore"));
            preDataNum = new UITextField();
            preDataNum.setPreferredSize(new java.awt.Dimension(50, 20));
            jpanel1.add(onlyPreData);
            jpanel1.add(preDataNum);
            jpanel1.add(new UILabel(Inter.getLocText("FR-Chart-Data_Records")));
            JPanel jpanel2 = new JPanel();
            jpanel2.setLayout(new java.awt.FlowLayout(2, 0, 0));
            jpanel.add(jpanel2);
            combineOther = new UICheckBox(Inter.getLocText("FR-Chart-Data_CombineOther"));
            combineOther.setSelected(true);
            jpanel2.add(combineOther);
            JPanel jpanel3 = new JPanel();
            jpanel.add(jpanel3);
            jpanel3.setLayout(new java.awt.FlowLayout(0, 5, 0));
            notShowNull = new UICheckBox(Inter.getLocText("FR-Chart-Data_NotShowSeries"));
            jpanel3.add(notShowNull);
            onlyPreData.addChangeListener(new ChangeListener() {

                final SeriesFilterPane this$1;

                public void stateChanged(ChangeEvent changeevent)
                {
                    checkBoxUse();
                }

                
                {
                    this$1 = SeriesFilterPane.this;
                    super();
                }
            }
);
        }

        private void initPresentPane(JPanel jpanel)
        {
            JPanel jpanel1 = new JPanel();
            jpanel1.setLayout(new java.awt.FlowLayout(0, 8, 0));
            jpanel.add(jpanel1);
            present = new PresentComboBox() {

                final SeriesFilterPane this$1;

                protected void fireChange()
                {
                    fire();
                }

                
                {
                    this$1 = SeriesFilterPane.this;
                    super();
                }
            }
;
            present.setPreferredSize(new java.awt.Dimension(70, 20));
            jpanel1.add(new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("FR-Chart-Style_Present")).append(":").toString()));
            jpanel1.add(present);
        }

        private void fire()
        {
            if(parent != null)
                parent.attributeChanged();
        }

        public String title4PopupWindow()
        {
            return Inter.getLocText("FR-Chart-Data_Series");
        }

        public void checkBoxUse()
        {
            preDataNum.setEnabled(onlyPreData.isSelected());
            combineOther.setEnabled(onlyPreData.isSelected());
        }

        public void populateBean(TopDefinitionProvider topdefinitionprovider)
        {
            if(topdefinitionprovider == null)
                return;
            populate4NoPresent(topdefinitionprovider);
            if(present != null)
                present.populate(topdefinitionprovider.getSeriesPresent());
        }

        protected void populate4NoPresent(TopDefinitionProvider topdefinitionprovider)
        {
            if(topdefinitionprovider.getTopSeries() == -1)
            {
                onlyPreData.setSelected(false);
            } else
            {
                onlyPreData.setSelected(true);
                preDataNum.setText(String.valueOf(topdefinitionprovider.getTopSeries()));
                combineOther.setSelected(!topdefinitionprovider.isDiscardOtherSeries());
            }
            notShowNull.setSelected(topdefinitionprovider.isDiscardNullSeries());
        }

        public void updateBean(TopDefinitionProvider topdefinitionprovider)
        {
            update4NoPresent(topdefinitionprovider);
            if(present != null)
                topdefinitionprovider.setSeriesPresent(present.update());
        }

        protected void update4NoPresent(TopDefinitionProvider topdefinitionprovider)
        {
            if(onlyPreData.isSelected())
            {
                if(StringUtils.isNotEmpty(preDataNum.getText()))
                {
                    Number number = Utils.objectToNumber(preDataNum.getText(), true);
                    if(number != null)
                        topdefinitionprovider.setTopSeries(number.intValue());
                }
                topdefinitionprovider.setDiscardOtherSeries(!combineOther.isSelected());
            } else
            {
                topdefinitionprovider.setTopSeries(Integer.valueOf(-1).intValue());
            }
            topdefinitionprovider.setDiscardNullSeries(notShowNull.isSelected());
        }

        public TopDefinition updateBean()
        {
            return null;
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((TopDefinitionProvider)obj);
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((TopDefinitionProvider)obj);
        }


        public SeriesFilterPane(AbstractAttrNoScrollPane abstractattrnoscrollpane)
        {
            this$0 = ChartDataFilterPane.this;
            super(true);
            parent = abstractattrnoscrollpane;
        }
    }

    private class CategoryFilterPane extends AbstractChartTabPane
    {

        private UICheckBox onlyPreData;
        private UITextField preDataNum;
        private UICheckBox combineOther;
        private UICheckBox notShowNull;
        private PresentComboBox present;
        private AbstractAttrNoScrollPane parent;
        final ChartDataFilterPane this$0;

        public java.awt.Dimension getPreferredSize()
        {
            java.awt.Dimension dimension = super.getPreferredSize();
            dimension.height = 130;
            return dimension;
        }

        protected JPanel createContentPane()
        {
            setLayout(new java.awt.BorderLayout());
            JPanel jpanel = new JPanel();
            add(jpanel, "North");
            jpanel.setLayout(new BoxLayout(jpanel, 1));
            jpanel.setPreferredSize(new java.awt.Dimension(200, 110));
            initOtherPane(jpanel);
            initPresentPane(jpanel);
            return jpanel;
        }

        protected void initOtherPane(JPanel jpanel)
        {
            JPanel jpanel1 = new JPanel();
            jpanel1.setLayout(new java.awt.FlowLayout(0));
            jpanel1.setPreferredSize(new java.awt.Dimension(200, 20));
            jpanel.add(jpanel1);
            onlyPreData = new UICheckBox(Inter.getLocText("FR-Chart-Data_OnlyUseBefore"));
            preDataNum = new UITextField();
            preDataNum.setPreferredSize(new java.awt.Dimension(50, 20));
            jpanel1.add(onlyPreData);
            jpanel1.add(preDataNum);
            jpanel1.add(new BoldFontTextLabel(Inter.getLocText("FR-Chart-Data_Records")));
            JPanel jpanel2 = new JPanel();
            jpanel2.setLayout(new java.awt.FlowLayout(2, 0, 0));
            jpanel.add(jpanel2);
            combineOther = new UICheckBox(Inter.getLocText("FR-Chart-Data_CombineOther"));
            combineOther.setSelected(true);
            jpanel2.add(combineOther);
            JPanel jpanel3 = new JPanel();
            jpanel.add(jpanel3);
            jpanel3.setLayout(new java.awt.FlowLayout(0, 5, 0));
            notShowNull = new UICheckBox(Inter.getLocText("FR-Chart-Data_NotShowCate"));
            jpanel3.add(notShowNull);
            onlyPreData.addChangeListener(new ChangeListener() {

                final CategoryFilterPane this$1;

                public void stateChanged(ChangeEvent changeevent)
                {
                    checkBoxUse();
                }

                
                {
                    this$1 = CategoryFilterPane.this;
                    super();
                }
            }
);
        }

        private void initPresentPane(JPanel jpanel)
        {
            JPanel jpanel1 = new JPanel();
            jpanel1.setLayout(new java.awt.FlowLayout(0, 8, 0));
            jpanel.add(jpanel1);
            present = new PresentComboBox() {

                final CategoryFilterPane this$1;

                protected void fireChange()
                {
                    fire();
                }

                
                {
                    this$1 = CategoryFilterPane.this;
                    super();
                }
            }
;
            present.setPreferredSize(new java.awt.Dimension(70, 20));
            jpanel1.add(new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("FR-Chart-Style_Present")).append(":").toString()));
            jpanel1.add(present);
        }

        private void fire()
        {
            if(parent != null)
                parent.attributeChanged();
        }

        public String title4PopupWindow()
        {
            return Inter.getLocText("FR-Chart-Style_Category");
        }

        public void checkBoxUse()
        {
            preDataNum.setEnabled(onlyPreData.isSelected());
            combineOther.setEnabled(onlyPreData.isSelected());
        }

        public void populateBean(TopDefinitionProvider topdefinitionprovider)
        {
            if(topdefinitionprovider == null)
                return;
            populateBean4NoPresent(topdefinitionprovider);
            if(present != null)
                present.populate(topdefinitionprovider.getCategoryPresent());
        }

        public void updateBean(TopDefinitionProvider topdefinitionprovider)
        {
            updateBean4NoPresent(topdefinitionprovider);
            topdefinitionprovider.setCategoryPresent(present.update());
        }

        public TopDefinition updateBean()
        {
            return null;
        }

        protected void updateBean4NoPresent(TopDefinitionProvider topdefinitionprovider)
        {
            if(onlyPreData.isSelected())
            {
                if(StringUtils.isNotEmpty(preDataNum.getText()))
                {
                    Number number = Utils.objectToNumber(preDataNum.getText(), true);
                    if(number != null)
                        topdefinitionprovider.setTopCate(Integer.valueOf(preDataNum.getText()).intValue());
                }
                topdefinitionprovider.setDiscardOtherCate(!combineOther.isSelected());
            } else
            {
                topdefinitionprovider.setTopCate(Integer.valueOf(-1).intValue());
            }
            topdefinitionprovider.setDiscardNullCate(notShowNull.isSelected());
        }

        protected void populateBean4NoPresent(TopDefinitionProvider topdefinitionprovider)
        {
            if(topdefinitionprovider.getTopCate() == -1)
            {
                onlyPreData.setSelected(false);
            } else
            {
                onlyPreData.setSelected(true);
                preDataNum.setText(String.valueOf(topdefinitionprovider.getTopCate()));
                combineOther.setSelected(!topdefinitionprovider.isDiscardOtherCate());
            }
            notShowNull.setSelected(topdefinitionprovider.isDiscardNullCate());
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((TopDefinitionProvider)obj);
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((TopDefinitionProvider)obj);
        }


        public CategoryFilterPane(AbstractAttrNoScrollPane abstractattrnoscrollpane)
        {
            this$0 = ChartDataFilterPane.this;
            super(true);
            parent = abstractattrnoscrollpane;
        }
    }


    private static final long serialVersionUID = 0x32a943514a7fa9f2L;
    private static final int PAN_WIDTH = 210;
    private static final int FIL_HEIGHT = 130;
    private CategoryFilterPane categoryPane;
    private SeriesFilterPane seriesPane;
    private boolean isNeedPresent;
    private AbstractAttrNoScrollPane parentPane;
    private Plot plot4Pane;

    public ChartDataFilterPane(Plot plot, ChartDataPane chartdatapane)
    {
        super(plot, chartdatapane);
        isNeedPresent = true;
        parentPane = null;
        plot4Pane = null;
        isNeedPresent = true;
    }

    protected List initPaneList(Plot plot, AbstractAttrNoScrollPane abstractattrnoscrollpane)
    {
        plot4Pane = plot;
        parentPane = abstractattrnoscrollpane;
        ArrayList arraylist = new ArrayList();
        if(plot == null || plot.isSupportCategoryFilter())
        {
            categoryPane = new CategoryFilterPane(abstractattrnoscrollpane);
            arraylist.add(new com.fr.design.mainframe.chart.gui.style.ThirdTabPane.NamePane(categoryPane.title4PopupWindow(), categoryPane));
        }
        if(plot == null || plot.isSupportSeriesFilter())
        {
            seriesPane = new SeriesFilterPane(abstractattrnoscrollpane);
            arraylist.add(new com.fr.design.mainframe.chart.gui.style.ThirdTabPane.NamePane(seriesPane.title4PopupWindow(), seriesPane));
        }
        return arraylist;
    }

    protected int getContentPaneWidth()
    {
        return 210;
    }

    public void checkBoxUse()
    {
        if(categoryPane != null)
            categoryPane.checkBoxUse();
        if(seriesPane != null)
            seriesPane.checkBoxUse();
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("FR-Chart-Data_Filter");
    }

    public void relayoutPane(boolean flag)
    {
        if(isNeedPresent == flag)
        {
            return;
        } else
        {
            isNeedPresent = flag;
            removeAll();
            paneList = initPaneList4NoPresent(plot4Pane, parentPane);
            initAllPane();
            validate();
            return;
        }
    }

    private List initPaneList4NoPresent(Plot plot, AbstractAttrNoScrollPane abstractattrnoscrollpane)
    {
        ArrayList arraylist = new ArrayList();
        if(plot == null || plot.isSupportCategoryFilter())
        {
            categoryPane = new CategoryFilterPaneWithOutPresentPane(abstractattrnoscrollpane);
            arraylist.add(new com.fr.design.mainframe.chart.gui.style.ThirdTabPane.NamePane(categoryPane.title4PopupWindow(), categoryPane));
        }
        if(plot == null || plot.isSupportSeriesFilter())
        {
            seriesPane = new SeriesFilterWithOutPresentPane(abstractattrnoscrollpane);
            arraylist.add(new com.fr.design.mainframe.chart.gui.style.ThirdTabPane.NamePane(seriesPane.title4PopupWindow(), seriesPane));
        }
        return arraylist;
    }

    public void populateBean(ChartCollection chartcollection, boolean flag)
    {
        relayoutPane(flag);
        if(categoryPane != null)
            categoryPane.populateBean(chartcollection.getSelectedChart().getFilterDefinition());
        if(seriesPane != null)
            seriesPane.populateBean(chartcollection.getSelectedChart().getFilterDefinition());
        checkBoxUse();
    }

    public void populateBean(ChartCollection chartcollection)
    {
        populateBean(chartcollection, true);
    }

    public void updateBean(ChartCollection chartcollection)
    {
        if(categoryPane != null)
            categoryPane.updateBean(chartcollection.getSelectedChart().getFilterDefinition());
        if(seriesPane != null)
            seriesPane.updateBean(chartcollection.getSelectedChart().getFilterDefinition());
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((ChartCollection)obj);
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((ChartCollection)obj);
    }
}
