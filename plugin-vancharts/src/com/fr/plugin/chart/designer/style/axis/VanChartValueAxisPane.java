package com.fr.plugin.chart.designer.style.axis;

import com.fr.base.Formula;
import com.fr.chart.base.ChartBaseUtils;
import com.fr.design.chart.ChartSwingUtils;
import com.fr.design.chart.axis.MinMaxValuePane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.attr.axis.VanChartValueAxis;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ��ֵ������
 */
public class VanChartValueAxisPane extends VanChartBaseAxisPane {
    private static final long serialVersionUID = 5427425193132271246L;
    protected MinMaxValuePane minMaxValuePane;
    private UICheckBox logBox;
    private UITextField logBaseField;

    public VanChartValueAxisPane(){
        this(false);
    }

    public VanChartValueAxisPane(boolean isXAxis){
        super(isXAxis);
    }

    protected JPanel createContentPane(boolean isXAxis){

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p,p,p,p,p,p,p,p,p,p,p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{new JSeparator(),null},
                new Component[]{createTitlePane(new double[]{p, p, p, p,p}, columnSize, isXAxis),null},
                new Component[]{new JSeparator(),null},
                new Component[]{createLabelPane(new double[]{p, p, p}, columnSize),null},
                new Component[]{new JSeparator(),null},
                new Component[]{createValueDefinition(new double[]{p, p}, columnSize),null},
                new Component[]{new JSeparator(),null},
                new Component[]{createLineStylePane(new double[]{p, p,p,p}, columnSize),null},
                new Component[]{new JSeparator(),null},
                new Component[]{createAxisPositionPane(new double[]{p, p}, columnSize, isXAxis),null},
                new Component[]{new JSeparator(),null},
                new Component[]{createDisplayStrategy(new double[]{p, p}, columnSize),null},
                new Component[]{new JSeparator(),null},
                new Component[]{createValueStylePane(),null},
        };

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    protected JPanel createValueDefinition(double[] row, double[] col){
        initMinMaxValuePane();

        logBox = new UICheckBox(Inter.getLocText("Plugin-ChartF_LogBaseValue") + " ");
        logBaseField = new UITextField();
        logBaseField.setPreferredSize(new Dimension(55, 20));
        logBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logBaseField.setEnabled(logBox.isSelected());
            }
        });
        ChartSwingUtils.addListener(logBox, logBaseField);
        JPanel logPane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        logPane.add(logBox);
        logPane.add(logBaseField);

        Component[][] components = new Component[][]{
                new Component[]{minMaxValuePane, null},
                new Component[]{logPane,null},
        } ;

        logBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkBox();
            }
        });

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, row, col);
        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Plugin-ChartF_ValueDefinition"), panel);
    }

    protected void initMinMaxValuePane() {
        minMaxValuePane = new MinMaxValuePane();
    }

    private void checkBox() {
        logBaseField.setEnabled(logBox.isSelected());
    }

    /**
     * titleӦ����һ�����ԣ���ֻ�ǶԻ���ı���ʱ�õ���������������ʱ��Ҳ���õõ�
     * @return �绯�����
     */
    @Override
    public String title4PopupWindow() {
        return Inter.getLocText("Plugin-ChartF_ValueAxis");
    }

    public void populateBean(VanChartAxis axis){
        VanChartValueAxis valueAxis = (VanChartValueAxis)axis;
        super.populateBean(valueAxis);
        if(minMaxValuePane != null){
            minMaxValuePane.populate(valueAxis);
        }
        if(logBox != null && logBaseField != null){
            logBox.setSelected(valueAxis.isLog());
            if(valueAxis.getLogBase() != null){
                logBaseField.setText(valueAxis.getLogBase().toString());
            }
            checkBox();
        }
    }

    @Override
    public void updateBean(VanChartAxis axis) {
        VanChartValueAxis valueAxis = (VanChartValueAxis)axis;
        super.updateBean(valueAxis);
        if(minMaxValuePane != null){
            minMaxValuePane.update(valueAxis);
        }
        if(logBox != null && logBaseField != null){
            updateLog(valueAxis);
        }
    }

    public VanChartValueAxis updateBean(String axisName, int position){
        VanChartValueAxis axis = new VanChartValueAxis(axisName, position);
        updateBean(axis);
        return axis;
    }

    private void updateLog(VanChartValueAxis valueAxis) {
        if (logBaseField != null && logBox.isSelected()) {
            String increment = logBaseField.getText();
            if (StringUtils.isEmpty(increment)) {
                valueAxis.setLog(false);
                valueAxis.setLogBase(null);
            } else {
                valueAxis.setLog(true);
                Formula formula = new Formula(increment);
                Number number = ChartBaseUtils.formula2Number(formula);
                // ���洦���ֹ ���� ��������ΪС��1��ֵ.
                if (number != null && number.doubleValue() <= 1.0) {
                    valueAxis.setLogBase(new Formula("2"));
                } else {
                    valueAxis.setLogBase(formula);
                }
            }
        } else {
            valueAxis.setLog(false);
        }
    }
}
