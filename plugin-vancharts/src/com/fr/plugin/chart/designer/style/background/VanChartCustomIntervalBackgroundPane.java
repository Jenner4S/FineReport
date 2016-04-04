package com.fr.plugin.chart.designer.style.background;

import com.fr.base.Formula;
import com.fr.base.Utils;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.attr.axis.VanChartCustomIntervalBackground;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;

import javax.swing.*;
import java.awt.*;

/**
 * �Զ�������������
 */
public class VanChartCustomIntervalBackgroundPane extends BasicBeanPane<VanChartCustomIntervalBackground>{
    private static final long serialVersionUID = 2700739847414325705L;

    private UIButtonGroup backgroundAxis;
    private TinyFormulaPane bottomValue;
    private TinyFormulaPane topValue;
    private ColorSelectBox color;
    private UINumberDragPane transparent;

    public VanChartCustomIntervalBackgroundPane(){
    }

    private void doLayoutPane(){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //
        JPanel top = FRGUIPaneFactory.createBorderLayout_L_Pane();
        this.add(top);
        top.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("Plugin-ChartF_CustomIntervalBackground") + ":", null));
        top.add(createContentPane());
    }

    private JPanel createContentPane()
    {
        bottomValue = new TinyFormulaPane();
        topValue = new TinyFormulaPane();
        bottomValue.setPreferredSize(new Dimension(124,20));
        topValue.setPreferredSize(new Dimension(124,20));
        color = new ColorSelectBox(100);
        transparent = new UINumberDragPane(0,100);
        double p = TableLayout.PREFERRED;
        double[] columnSize = {p,p};
        double[] rowSize = {p,p,p};

        JPanel axisPane = TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Plugin-ChartF_Axis"), backgroundAxis);

        Component[][] rangeComponents = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_BottomValue")),bottomValue},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_TopValue")),topValue},
        };
        JPanel temp = TableLayoutHelper.createTableLayoutPane(rangeComponents, rowSize, columnSize);
        JPanel rangePane = TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Plugin-ChartF_Range"), temp);

        Component[][] styleComponents = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Color_Color")),color},
                new Component[]{new UILabel(Inter.getLocText("Plugin-Chart_Alpha")),transparent},
        };
        temp = TableLayoutHelper.createTableLayoutPane(styleComponents, rowSize, columnSize);
        JPanel stylePane = TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Chart-Style_Name"), temp);

        Component[][] components = getPaneComponents(axisPane, rangePane, stylePane);

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    protected Component[][] getPaneComponents(JPanel axisPane, JPanel rangePane, JPanel stylePane) {
        return new Component[][]{
                new Component[]{axisPane,null},
                new Component[]{rangePane,null},
                new Component[]{stylePane,null},
        };
    }

    protected String title4PopupWindow(){
        return Inter.getLocText("Plugin-ChartF_CustomIntervalBackground");
    }

    public void populateBean(VanChartCustomIntervalBackground customIntervalBackground){
        backgroundAxis = new UIButtonGroup(customIntervalBackground.getAxisNamesArray(), customIntervalBackground.getAxisNamesArray());
        backgroundAxis.setSelectedItem(customIntervalBackground.getAxisName());

        doLayoutPane();

        bottomValue.populateBean(Utils.objectToString(customIntervalBackground.getFromFormula()));
        topValue.populateBean(Utils.objectToString(customIntervalBackground.getToFormula()));
        color.setSelectObject(customIntervalBackground.getBackgroundColor());
        transparent.populateBean(customIntervalBackground.getAlpha() * VanChartAttrHelper.PERCENT);


    }

    public void updateBean(VanChartCustomIntervalBackground customIntervalBackground){
        customIntervalBackground.setAxisName(backgroundAxis.getSelectedItem().toString());

        customIntervalBackground.setFromFormula(new Formula(bottomValue.updateBean()));
        customIntervalBackground.setToFormula(new Formula(topValue.updateBean()));
        customIntervalBackground.setBackgroundColor(color.getSelectObject());
        customIntervalBackground.setAlpha(transparent.updateBean() / VanChartAttrHelper.PERCENT);
    }

    public VanChartCustomIntervalBackground updateBean(){
        return null;
    }
}
