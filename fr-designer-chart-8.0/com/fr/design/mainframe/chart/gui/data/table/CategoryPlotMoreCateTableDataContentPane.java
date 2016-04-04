// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.base.BaseUtils;
import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.Bar2DPlot;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.NormalTableDataDefinition;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.data.table:
//            CategoryPlotTableDataContentPane, SeriesTypeUseComboxPane

public class CategoryPlotMoreCateTableDataContentPane extends CategoryPlotTableDataContentPane
    implements UIObserver
{

    private static final long serialVersionUID = 0xd21fdc988a743765L;
    private static final int COMBOX_GAP = 8;
    private static final int COMBOX_WIDTH = 100;
    private static final int COMBOX_HEIGHT = 20;
    private JPanel boxPane;
    private ArrayList boxList;
    private UIButton addButton;
    private UIObserverListener uiobListener;

    public CategoryPlotMoreCateTableDataContentPane()
    {
        boxList = new ArrayList();
        uiobListener = null;
    }

    public CategoryPlotMoreCateTableDataContentPane(ChartDataPane chartdatapane)
    {
        boxList = new ArrayList();
        uiobListener = null;
        categoryCombox = new UIComboBox();
        categoryCombox.setPreferredSize(new Dimension(100, 20));
        JPanel jpanel = new JPanel(new BorderLayout(4, 0));
        jpanel.setBorder(BorderFactory.createMatteBorder(0, 0, 6, 1, getBackground()));
        BoldFontTextLabel boldfonttextlabel = new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("FR-Chart-Category_Name")).append(":").toString(), 4);
        boldfonttextlabel.setPreferredSize(new Dimension(75, 20));
        addButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/add.png"));
        addButton.setPreferredSize(new Dimension(20, 20));
        jpanel.add(GUICoreUtils.createBorderLayoutPane(new Component[] {
            categoryCombox, addButton, null, boldfonttextlabel, null
        }));
        boxPane = new JPanel();
        boxPane.setLayout(new BoxLayout(boxPane, 1));
        jpanel.add(boxPane, "South");
        setLayout(new BorderLayout());
        add(jpanel, "North");
        seriesTypeComboxPane = new SeriesTypeUseComboxPane(chartdatapane, new Bar2DPlot());
        add(seriesTypeComboxPane, "South");
        addButton.addActionListener(new ActionListener() {

            final CategoryPlotMoreCateTableDataContentPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(boxList.size() < 2)
                {
                    addNewCombox();
                    relayoutPane();
                }
                checkSeriseUse(categoryCombox.getSelectedItem() != null);
            }

            
            {
                this$0 = CategoryPlotMoreCateTableDataContentPane.this;
                super();
            }
        }
);
        categoryCombox.addItemListener(new ItemListener() {

            final CategoryPlotMoreCateTableDataContentPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                checkSeriseUse(categoryCombox.getSelectedItem() != null);
                makeToolTipUse(categoryCombox);
                checkAddButton();
            }

            
            {
                this$0 = CategoryPlotMoreCateTableDataContentPane.this;
                super();
            }
        }
);
    }

    protected void checkSeriseUse(boolean flag)
    {
        super.checkSeriseUse(flag);
        addButton.setEnabled(flag);
    }

    private UIComboBox addNewCombox()
    {
        final JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(2, 0, 2));
        final UIComboBox combox = new UIComboBox();
        combox.setPreferredSize(new Dimension(100, 20));
        int i = categoryCombox.getItemCount();
        for(int j = 0; j < i; j++)
            combox.addItem(categoryCombox.getItemAt(j));

        combox.registerChangeListener(uiobListener);
        combox.addItemListener(new ItemListener() {

            final UIComboBox val$combox;
            final CategoryPlotMoreCateTableDataContentPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                makeToolTipUse(combox);
            }

            
            {
                this$0 = CategoryPlotMoreCateTableDataContentPane.this;
                combox = uicombobox;
                super();
            }
        }
);
        combox.setSelectedItem(categoryCombox.getItemAt(0));
        makeToolTipUse(combox);
        buttonPane.add(combox);
        UIButton uibutton = new UIButton(BaseUtils.readIcon("com/fr/design/images/toolbarbtn/close.png"));
        buttonPane.add(uibutton);
        boxPane.add(buttonPane);
        boxList.add(combox);
        checkAddButton();
        uibutton.addActionListener(new ActionListener() {

            final JPanel val$buttonPane;
            final UIComboBox val$combox;
            final CategoryPlotMoreCateTableDataContentPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                boxPane.remove(buttonPane);
                boxList.remove(combox);
                checkAddButton();
                relayoutPane();
            }

            
            {
                this$0 = CategoryPlotMoreCateTableDataContentPane.this;
                buttonPane = jpanel;
                combox = uicombobox;
                super();
            }
        }
);
        uibutton.registerChangeListener(uiobListener);
        return combox;
    }

    private void checkAddButton()
    {
        int i = boxList.size();
        addButton.setEnabled(i < 2 && categoryCombox.getSelectedItem() != null);
    }

    private void relayoutPane()
    {
        revalidate();
    }

    public void checkBoxUse(boolean flag)
    {
        super.checkBoxUse(flag);
        checkAddButton();
    }

    protected void refreshBoxListWithSelectTableData(java.util.List list)
    {
        super.refreshBoxListWithSelectTableData(list);
        int i = 0;
        for(int j = boxList.size(); i < j; i++)
            refreshBoxItems((UIComboBox)boxList.get(i), list);

    }

    public void registerChangeListener(UIObserverListener uiobserverlistener)
    {
        uiobListener = uiobserverlistener;
    }

    public boolean shouldResponseChangeListener()
    {
        return true;
    }

    public void populateBean(ChartCollection chartcollection)
    {
        super.populateBean(chartcollection);
        boxList.clear();
        TopDefinitionProvider topdefinitionprovider = chartcollection.getSelectedChart().getFilterDefinition();
        if(topdefinitionprovider instanceof NormalTableDataDefinition)
        {
            NormalTableDataDefinition normaltabledatadefinition = (NormalTableDataDefinition)topdefinitionprovider;
            int i = normaltabledatadefinition.getMoreCateSize();
            for(int j = 0; j < i; j++)
            {
                UIComboBox uicombobox = addNewCombox();
                uicombobox.setSelectedItem(normaltabledatadefinition.getMoreCateWithIndex(j));
            }

        }
        checkAddButton();
        checkSeriseUse(categoryCombox.getSelectedItem() != null);
    }

    public void updateBean(ChartCollection chartcollection)
    {
        super.updateBean(chartcollection);
        TopDefinitionProvider topdefinitionprovider = chartcollection.getSelectedChart().getFilterDefinition();
        if(topdefinitionprovider instanceof NormalTableDataDefinition)
        {
            NormalTableDataDefinition normaltabledatadefinition = (NormalTableDataDefinition)topdefinitionprovider;
            normaltabledatadefinition.clearMoreCate();
            int i = 0;
            for(int j = boxList.size(); i < j; i++)
            {
                UIComboBox uicombobox = (UIComboBox)boxList.get(i);
                if(uicombobox.getSelectedItem() != null)
                    normaltabledatadefinition.addMoreCate(uicombobox.getSelectedItem().toString());
            }

        }
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
