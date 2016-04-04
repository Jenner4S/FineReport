// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data.report;

import com.fr.base.BaseUtils;
import com.fr.base.Utils;
import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.*;
import com.fr.chart.chartdata.NormalReportDataDefinition;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ChartDataFilterPane;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.data.report:
//            CategoryPlotReportDataContentPane

public class CategoryPlotMoreCateReportDataContentPane extends CategoryPlotReportDataContentPane
    implements UIObserver
{

    private static final long serialVersionUID = 0xf06cbdf3756acacaL;
    private JPanel boxPane;
    private UIButton addButton;
    private ArrayList formualList;
    private UIObserverListener uiobListener;

    public CategoryPlotMoreCateReportDataContentPane()
    {
        formualList = new ArrayList();
        uiobListener = null;
    }

    public CategoryPlotMoreCateReportDataContentPane(ChartDataPane chartdatapane)
    {
        formualList = new ArrayList();
        uiobListener = null;
        initEveryPane();
        categoryName = initCategoryBox((new StringBuilder()).append(Inter.getLocText("FR-Chart-Category_Name")).append(":").toString());
        JPanel jpanel = new JPanel();
        jpanel.setLayout(new BorderLayout(2, 2));
        jpanel.add(categoryName);
        addButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/add.png"));
        addButton.setPreferredSize(new Dimension(20, 20));
        jpanel.add(addButton, "East");
        boxPane = new JPanel();
        boxPane.setLayout(new BoxLayout(boxPane, 1));
        boxPane.setBackground(Color.red);
        jpanel.add(boxPane, "South");
        add(jpanel, "0,0,2,0");
        add(new BoldFontTextLabel(Inter.getLocText("FR-Chart-Data_Filter")), "0,4,2,4");
        add(filterPane = new ChartDataFilterPane(new Bar2DPlot(), chartdatapane), "0,6,2,4");
        addButton.addActionListener(new ActionListener() {

            final CategoryPlotMoreCateReportDataContentPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                addNewCatePane();
            }

            
            {
                this$0 = CategoryPlotMoreCateReportDataContentPane.this;
                super();
            }
        }
);
    }

    public void checkBoxUse()
    {
        super.checkBoxUse();
        String s = categoryName.getUITextField().getText();
        addButton.setEnabled(StringUtils.isNotEmpty(s));
    }

    private TinyFormulaPane addNewCatePane()
    {
        final TinyFormulaPane pane = initCategoryBox("");
        pane.setPreferredSize(new Dimension(122, 16));
        pane.registerChangeListener(uiobListener);
        formualList.add(pane);
        final JPanel newButtonPane = new JPanel();
        newButtonPane.setLayout(new FlowLayout(2, 0, 0));
        newButtonPane.add(pane);
        UIButton uibutton = new UIButton(BaseUtils.readIcon("com/fr/design/images/toolbarbtn/close.png"));
        newButtonPane.add(uibutton);
        boxPane.add(newButtonPane);
        uibutton.addActionListener(new ActionListener() {

            final JPanel val$newButtonPane;
            final TinyFormulaPane val$pane;
            final CategoryPlotMoreCateReportDataContentPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                boxPane.remove(newButtonPane);
                formualList.remove(pane);
                checkAddButton();
                relayoutPane();
            }

            
            {
                this$0 = CategoryPlotMoreCateReportDataContentPane.this;
                newButtonPane = jpanel;
                pane = tinyformulapane;
                super();
            }
        }
);
        uibutton.registerChangeListener(uiobListener);
        checkAddButton();
        relayoutPane();
        return pane;
    }

    private void checkAddButton()
    {
        int i = formualList.size();
        addButton.setEnabled(i < 2);
    }

    private void relayoutPane()
    {
        revalidate();
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
        formualList.clear();
        TopDefinitionProvider topdefinitionprovider = chartcollection.getSelectedChart().getFilterDefinition();
        if(topdefinitionprovider instanceof NormalReportDataDefinition)
        {
            NormalReportDataDefinition normalreportdatadefinition = (NormalReportDataDefinition)topdefinitionprovider;
            int i = normalreportdatadefinition.getMoreCateSize();
            if(normalreportdatadefinition.getCategoryName() != null && i > 0)
            {
                for(int j = 0; j < i; j++)
                {
                    TinyFormulaPane tinyformulapane = addNewCatePane();
                    tinyformulapane.populateBean(Utils.objectToString(normalreportdatadefinition.getMoreCateWithIndex(j)));
                }

            }
        }
        checkAddButton();
    }

    public void updateBean(ChartCollection chartcollection)
    {
        super.updateBean(chartcollection);
        TopDefinitionProvider topdefinitionprovider = chartcollection.getSelectedChart().getFilterDefinition();
        if(topdefinitionprovider instanceof NormalReportDataDefinition)
        {
            NormalReportDataDefinition normalreportdatadefinition = (NormalReportDataDefinition)topdefinitionprovider;
            normalreportdatadefinition.clearMoreCate();
            int i = 0;
            for(int j = formualList.size(); i < j; i++)
            {
                TinyFormulaPane tinyformulapane = (TinyFormulaPane)formualList.get(i);
                normalreportdatadefinition.addMoreCate(canBeFormula(tinyformulapane.updateBean()));
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
