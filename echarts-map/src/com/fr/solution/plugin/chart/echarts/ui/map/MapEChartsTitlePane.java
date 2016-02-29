package com.fr.solution.plugin.chart.echarts.ui.map;

import com.fr.base.BaseUtils;
import com.fr.base.Formula;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.GeneralUtils;
import com.fr.solution.plugin.chart.echarts.base.MapChartTitle;
import com.fr.solution.plugin.chart.echarts.base.NewChart;
import com.fr.stable.StableUtils;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by richie on 16/2/19.
 */
public class MapEChartsTitlePane extends BasicScrollPane<NewChart> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3809178163957176704L;

	private UIButtonGroup<Boolean> showTitle;

    private UIButtonGroup<Integer> alignmentPane;
    
    private TinyFormulaPane tinyFormulaPane;
    
    private JPanel titlePane;
    
    private UITextField subTitle;
    
    //	private UICheckBox isTitleVisible;

    public MapEChartsTitlePane(MapEChartsStylePane parent) {
    }

    @Override
    protected JPanel createContentPane() {
        JPanel panel = new JPanel(new BorderLayout());
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] rowSize3 = {p, p,p};
        double[] labrow1 = {p };
        double[] columnSize2 = {p, f};
        double[] columnSize1 = { f};
//        isTitleVisible = new UICheckBox(Inter.getLocText("Chart-Title_Is_Visible"));
        showTitle = new UIButtonGroup<Boolean>(new String[]{"\u663E\u793A", "\u4E0D\u663E\u793A"}, new Boolean[]{true, false});
//        boolean a = showTitle.
        showTitle.setSelectedIndex(0);
        
        titlePane = createTitlePane();
        JPanel label = TableLayoutHelper.createTableLayoutPane(new Component[][]{
                {new UILabel("\u663E\u793A\u6807\u9898"), showTitle}
        }, labrow1, columnSize2);
        JPanel cen = TableLayoutHelper.createTableLayoutPane(new Component[][]{
                {label},
                /*
                new Component[]{new JSeparator()},*/
                 {titlePane}
        }, rowSize3, columnSize1);
        panel.add(cen, BorderLayout.CENTER);
        showTitle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkTitlePaneUse();
            }
        });
        return panel;
    }
    private void checkTitlePaneUse() {
        titlePane.setVisible(showTitle.getSelectedItem());
    }
    private JPanel createTitlePane(){

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{null,createTitleContentPane(new double[]{p,p},columnSize)},
                new Component[]{new JSeparator(),null},
                new Component[]{null,createTitlePositionPane(new double[]{p,p},columnSize)},
                new Component[]{new JSeparator(),null}
        };

        return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
    }
    private JPanel createTitlePositionPane(double[] row, double[] col){
        Icon[] alignmentIconArray = {BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_left_normal.png"),
                BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_center_normal.png"),
                BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_right_normal.png")};
        Integer[] alignment = new Integer[]{0, 1, 2};

        alignmentPane = new UIButtonGroup<Integer>(alignmentIconArray, alignment);
       
        Component[][] components = new Component[][]{
                new Component[]{alignmentPane,null},
        };
        JPanel panel = TableLayoutHelper.createTableLayoutPane(components,row,col);

        alignmentPane.setSelectedIndex(1);
        return TableLayout4ChartHelper.createTableLayoutPaneWithTitle("\u6807\u9898\u4F4D\u7F6E", panel);
    }
    
    private JPanel createTitleContentPane(double[] row, double[] col){
    	tinyFormulaPane = new TinyFormulaPane();
        subTitle = new UITextField("\u526F\u6807\u9898");
        UILabel label1 = new UILabel("\u4E3B\u6807\u9898");
        UILabel label2 = new UILabel("\u526F\u6807\u9898");
        Component[][] components = new Component[][]{
                new Component[]{label1,tinyFormulaPane},
                new Component[]{label2,subTitle},
        };

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components,row,col);
        return TableLayout4ChartHelper.createTableLayoutPaneWithTitle(/*Inter.getLocText("Plugin-ChartF_Content")*/"\u6807\u9898\u5185\u5BB9", panel);
    }
    
    
    
    

    @Override
    public void populateBean(NewChart ob) {
    	MapChartTitle title = (MapChartTitle) ob.getTitle();
        if (title == null) {
            return;
        }
        subTitle.setText(title.getSubTitle());
        showTitle.setSelectedItem(title.isShow());
        if (title.getTextObject() instanceof Formula) {
            tinyFormulaPane.populateBean(((Formula) title.getTextObject()).getContent());
        } else {
            tinyFormulaPane.populateBean(GeneralUtils.objectToString(title.getTextObject()));
        }

        alignmentPane.setSelectedIndex(title.getPosition());
    }

    @Override
    public void updateBean(NewChart ob) {
        if (ob == null) {
            ob = new NewChart();
        }
        MapChartTitle title = (MapChartTitle) ob.getTitle();
        if (title == null) {
            title = new MapChartTitle();
        }
        title.setSubTitle(subTitle.getText());
        title.setTitleVisble(showTitle.getSelectedItem());
        String titleString = tinyFormulaPane.updateBean();
        title.setTitle(titleString);
        title.setShow(showTitle.getSelectedItem());
        Object titleObj;
        if (StableUtils.maybeFormula(titleString)) {
            titleObj = new Formula(titleString);
        } else {
            titleObj = titleString;
        }
        title.setPosition(alignmentPane.getSelectedItem());	
        title.setTextObject(titleObj);
    }

    @Override
    protected String title4PopupWindow() {
        return "Title";
    }
}
