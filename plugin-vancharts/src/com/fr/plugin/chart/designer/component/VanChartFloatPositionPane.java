package com.fr.plugin.chart.designer.component;

import com.fr.chart.chartattr.Chart;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;

/**
 * ����λ�� �����ͼ���õ�
 */
public class VanChartFloatPositionPane extends BasicBeanPane<Chart> {
    private static final long serialVersionUID = -4773313488161065678L;
    private UISpinner floatPosition_x;
    private UISpinner floatPosition_y;

    public VanChartFloatPositionPane(){

        this.setLayout(new BorderLayout());

        this.add(new UILabel(Inter.getLocText("plugin-ChartF_XYFromTheUpLeft")), BorderLayout.CENTER);

        this.add(createCustomFloatPositionPane(), BorderLayout.SOUTH);
    }

    private JPanel createCustomFloatPositionPane(){
        floatPosition_x = new UISpinner(0,100,1,0);
        floatPosition_y = new UISpinner(0,100,1,0);
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p,p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("plugin-ChartF_positionX")+": "),floatPosition_x},
                new Component[]{new UILabel(Inter.getLocText("plugin-ChartF_positionY")+": "),floatPosition_y}
        };

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    public void setFloatPosition_x(double floatPosition_x) {
        this.floatPosition_x.setValue(floatPosition_x);
    }

    public void setFloatPosition_y(double floatPosition_y) {
        this.floatPosition_y.setValue(floatPosition_y);
    }

    public double getFloatPosition_x() {
        return floatPosition_x.getValue();
    }

    public double getFloatPosition_y() {
        return floatPosition_y.getValue();
    }


    /**
     * ���������
     * @return ����
     */
    public String title4PopupWindow() {
        return StringUtils.EMPTY;
    }

    @Override
    public Chart updateBean() {
        return null;
    }

    @Override
    public void populateBean(Chart ob) {

    }
}
