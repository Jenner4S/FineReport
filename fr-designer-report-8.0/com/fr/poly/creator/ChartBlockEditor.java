// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly.creator;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.chart.*;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.chart.MiddleChartComponent;
import com.fr.design.mainframe.*;
import com.fr.design.mainframe.cell.QuickEditorRegion;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.poly.PolyDesigner;
import com.fr.poly.hanlder.ColumnOperationMouseHandler;
import com.fr.poly.hanlder.RowOperationMouseHandler;
import com.fr.report.poly.PolyChartBlock;
import com.fr.report.poly.TemplateBlock;
import com.fr.stable.core.PropertyChangeAdapter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.Border;

// Referenced classes of package com.fr.poly.creator:
//            BlockEditor, BlockCreator, ChartBlockCreator

public class ChartBlockEditor extends BlockEditor
{
    private class ChartButton extends JToggleButton
    {

        private BaseChart chart;
        final ChartBlockEditor this$0;

        public Dimension getPreferredSize()
        {
            return new Dimension(22, 22);
        }


        public ChartButton(BaseChart basechart, String s, String s1, int i)
        {
            this$0 = ChartBlockEditor.this;
            super();
            chart = basechart;
            setToolTipText(s);
            String s2 = (new StringBuilder()).append("com/fr/design/images/poly/").append(s1).append('/').append(s1).append('-').append(i).append(".png").toString();
            Icon icon = null;
            try
            {
                icon = BaseUtils.readIcon(s2);
            }
            catch(Exception exception)
            {
                icon = BaseUtils.readIcon("com/fr/design/images/poly/normal.png");
            }
            setIcon(icon);
            setBorder(null);
            setMargin(null);
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setRequestFocusEnabled(false);
            addMouseListener(new MouseAdapter() {

                final ChartBlockEditor val$this$0;
                final ChartButton this$1;

                public void mouseClicked(MouseEvent mouseevent)
                {
                    if(BaseUtils.isAuthorityEditing())
                        return;
                    BaseChart basechart1 = null;
                    try
                    {
                        basechart1 = (BaseChart)chart.clone();
                    }
                    catch(CloneNotSupportedException clonenotsupportedexception)
                    {
                        FRContext.getLogger().error(clonenotsupportedexception.getMessage(), clonenotsupportedexception);
                        return;
                    }
                    BaseChartCollection basechartcollection = ((PolyChartBlock)creator.getValue()).getChartCollection();
                    basechartcollection.switchPlot(basechart1.getBasePlot());
                    initEffective(basechartcollection);
                    creator.setValue(creator.getValue());
                    removeAll();
                    initComponets();
                    initNorthBarComponent();
                    addColumnRowListeners();
                    addBoundsListener();
                    initDataChangeListener();
                    doLayout();
                    repaint();
                    QuickEditorRegion.getInstance().populate(creator.getQuickEditor(designer));
                    LayoutUtils.layoutRootContainer(designer);
                    designer.fireTargetModified();
                    designer.repaint();
                }

                public void mouseEntered(MouseEvent mouseevent)
                {
                    setBorder(ChartBlockEditor.buttonBorder);
                }

                public void mouseExited(MouseEvent mouseevent)
                {
                    setBorder(null);
                }


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
        }
    }


    private static final int BOUND_OFF = 21;
    private static Border buttonBorder = new UIRoundedBorder(new Color(149, 149, 149), 1, 5);
    private static String chartsNames[][];
    private static BaseChartNameID typeName[];
    private ChartButton chartButtons[];

    public ChartBlockEditor(PolyDesigner polydesigner, ChartBlockCreator chartblockcreator)
    {
        super(polydesigner, chartblockcreator);
        chartButtons = null;
    }

    private void initNorthBarComponent()
    {
        JPanel jpanel = new JPanel(new FlowLayout(0, 2, 0));
        add("top", jpanel);
        BaseChart basechart = ((MiddleChartComponent)editComponent).getEditingChart();
        String s = basechart.getChartName();
        int i = 0;
label0:
        for(int j = 0; j < typeName.length; j++)
        {
            String as[] = chartsNames[j];
            int k = 0;
            do
            {
                if(k >= as.length)
                    continue label0;
                if(ComparatorUtils.equals(s, as[k]))
                {
                    i = j;
                    continue label0;
                }
                k++;
            } while(true);
        }

        String s1 = typeName[i].getPlotID();
        BaseChart abasechart[] = BaseChartGetter.getStaticChartTypes(s1);
        chartButtons = new ChartButton[abasechart.length];
        int l = 0;
        for(int i1 = abasechart.length; l < i1; l++)
        {
            chartButtons[l] = new ChartButton(abasechart[l], abasechart[l].getChartName(), typeName[i].getName(), l);
            jpanel.add(chartButtons[l]);
        }

    }

    public void checkChartButtonsEnable()
    {
    }

    public void refreshChartComponent()
    {
        ((MiddleChartComponent)editComponent).reset();
    }

    protected MiddleChartComponent createEffective()
    {
        if(editComponent == null)
            initEffective(((PolyChartBlock)creator.getValue()).getChartCollection());
        return (MiddleChartComponent)editComponent;
    }

    private void initEffective(BaseChartCollection basechartcollection)
    {
        editComponent = DesignModuleFactory.getChartComponent(basechartcollection);
        ((MiddleChartComponent)editComponent).addStopEditingListener(new PropertyChangeAdapter() {

            final ChartBlockEditor this$0;

            public void propertyChange()
            {
                QuickEditorRegion.getInstance().populate(creator.getQuickEditor(designer));
                designer.fireTargetModified();
            }

            
            {
                this$0 = ChartBlockEditor.this;
                super();
            }
        }
);
        ((MiddleChartComponent)editComponent).setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
    }

    public void setBounds(Rectangle rectangle)
    {
        setBounds(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public PolyChartBlock getValue()
    {
        PolyChartBlock polychartblock = (PolyChartBlock)creator.getValue();
        if(editComponent != null)
        {
            BaseChartCollection basechartcollection = ((MiddleChartComponent)editComponent).update();
            polychartblock.setChartCollection(basechartcollection);
        }
        return polychartblock;
    }

    protected Dimension getAddHeigthPreferredSize()
    {
        return new Dimension(30, 15);
    }

    protected Dimension getAddWidthPreferredSize()
    {
        return new Dimension(15, 30);
    }

    protected RowOperationMouseHandler createRowOperationMouseHandler()
    {
        return new com.fr.poly.hanlder.RowOperationMouseHandler.ChartBlockRowOperationMouseHandler(designer, this);
    }

    protected ColumnOperationMouseHandler createColumnOperationMouseHandler()
    {
        return new com.fr.poly.hanlder.ColumnOperationMouseHandler.ChartBlockColumnOperationMouseHandler(designer, this);
    }

    protected void initDataChangeListener()
    {
    }

    public MiddleChartComponent getEditChartComponent()
    {
        return createEffective();
    }

    public void refreshChartCompon()
    {
        ((MiddleChartComponent)editComponent).reset();
    }

    public void resetSelectionAndChooseState()
    {
        designer.setChooseType(com.fr.poly.PolyDesigner.SelectionType.BLOCK);
        if(BaseUtils.isAuthorityEditing())
        {
            JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
            if(jtemplate.isJWorkBook())
                jtemplate.removeParameterPaneSelection();
            EastRegionContainerPane.getInstance().replaceUpPane(new NoSupportAuthorityEdit());
        }
        QuickEditorRegion.getInstance().populate(creator.getQuickEditor(designer));
    }

    public volatile TemplateBlock getValue()
    {
        return getValue();
    }

    protected volatile JComponent createEffective()
    {
        return createEffective();
    }

    static 
    {
        typeName = BaseChartGetter.getStaticAllChartBaseNames();
        chartsNames = new String[typeName.length][];
        for(int i = 0; i < typeName.length; i++)
        {
            BaseChart abasechart[] = BaseChartGetter.getStaticChartTypes(typeName[i].getPlotID());
            chartsNames[i] = new String[abasechart.length];
            for(int j = 0; j < abasechart.length; j++)
                chartsNames[i][j] = abasechart[j].getChartName();

        }

    }



}
