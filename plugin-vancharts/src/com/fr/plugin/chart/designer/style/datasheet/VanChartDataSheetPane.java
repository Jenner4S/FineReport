package com.fr.plugin.chart.designer.style.datasheet;

import com.fr.base.FRContext;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.DataSheet;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.style.FormatPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.style.ChartTextAttrPane;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.attr.axis.AxisType;
import com.fr.plugin.chart.attr.plot.VanChartRectanglePlot;
import com.fr.plugin.chart.base.VanChartConstants;
import com.fr.plugin.chart.designer.component.border.VanChartBorderPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ��ʽ-���ݱ�
 */
public class VanChartDataSheetPane extends BasicScrollPane<Chart> {
    private static final long serialVersionUID = 5547658195141361981L;

    private UICheckBox isShowDataSheet;
    private JPanel dataSheetPane;

    private ChartTextAttrPane textAttrPane;
    private FormatPane formatPane;
    private VanChartBorderPane borderPane;

    private class ContentPane extends JPanel {

        private static final long serialVersionUID = 5601169655874455336L;

        public ContentPane() {
            initComponents();
        }

        private void initComponents() {
            isShowDataSheet = new UICheckBox(Inter.getLocText("Plugin-ChartF_Show_Data_Sheet"));
            dataSheetPane = createDataSheetPane();

            double p = TableLayout.PREFERRED;
            double f = TableLayout.FILL;
            double[] columnSize = {f};
            double[] rowSize = {p, p, p};
            Component[][] components = new Component[][]{
                    new Component[]{isShowDataSheet},
                    new Component[]{new JSeparator()},
                    new Component[]{dataSheetPane}
            };

            JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
            this.setLayout(new BorderLayout());
            this.add(panel,BorderLayout.CENTER);

            isShowDataSheet.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    checkDataSheetPaneUse();
                }
            });
        }
    }
    // ������ݱ�����Ƿ����.
    private void checkDataSheetPaneUse() {
        dataSheetPane.setVisible(isShowDataSheet.isSelected());
    }

    private JPanel createDataSheetPane(){
        textAttrPane = new ChartTextAttrPane();
        formatPane = new FormatPane();
        formatPane.setForDataSheet();
        borderPane = new VanChartBorderPane();

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {LayoutConstants.CHART_ATTR_TOMARGIN, f};
        double[] rowSize = {p,p,p,p,p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Designer-Widget_Style")),null},
                new Component[]{null,textAttrPane},
                new Component[]{new JSeparator(),null},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_DataType")),null},
                new Component[]{null,formatPane},
                new Component[]{new JSeparator(),null},
                new Component[]{borderPane,null},
                new Component[]{new JSeparator(),null},
        };

        return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
    }

    @Override
    /**
     * ����
     * @return ���ر���
     */
    public String title4PopupWindow() {
        return PaneTitleConstants.CHART_STYLE_DATA_TITLE;
    }

    @Override
    protected JPanel createContentPane() {
        return new ContentPane();
    }

    @Override
    public void updateBean(Chart chart) {
        if(chart == null) {
            return;
        }
        Plot plot = chart.getPlot();
        if(plot == null) {
            return;
        }
        DataSheet dataSheet = plot.getDataSheet();
        if (dataSheet == null) {
            dataSheet = new DataSheet();
            dataSheet.setFormat(VanChartAttrHelper.VALUE_FORMAT);
            plot.setDataSheet(dataSheet);
        }
        if(isShowDataSheet.isSelected()){
            dataSheet.setVisible(true);
            dataSheet.setFont(textAttrPane.updateFRFont());
            dataSheet.setFormat(formatPane.update());
            borderPane.update(dataSheet);
        } else {
            dataSheet.setVisible(false);
        }
    }

    @Override
    public void populateBean(Chart chart) {
        if(chart == null || chart.getPlot() == null) {
            return;
        }
        VanChartRectanglePlot rectanglePlot = (VanChartRectanglePlot)chart.getPlot();
        if(rectanglePlot.getXAxisList().size() == 1){
            if(ComparatorUtils.equals(rectanglePlot.getDefaultXAxis().getAxisType(), AxisType.AXIS_CATEGORY)
                    && rectanglePlot.getDefaultXAxis().getPosition() == VanChartConstants.AXIS_BOTTOM){

                //ֻ�е���������������������λ�������棬���ݱ�ſ�����
                isShowDataSheet.setEnabled(true);

                DataSheet dataSheet = chart.getPlot().getDataSheet();
                if (dataSheet != null) {
                    isShowDataSheet.setSelected(dataSheet.isVisible());
                    FRFont font = FRContext.getDefaultValues().getFRFont() == null ? FRFont.getInstance() : FRContext.getDefaultValues().getFRFont();
                    textAttrPane.populate(dataSheet.getFont() == null ? font : dataSheet.getFont());
                    formatPane.populateBean(dataSheet.getFormat());
                    borderPane.populate(dataSheet);
                }
                checkDataSheetPaneUse();
                return;

            }
        }

        isShowDataSheet.setSelected(false);
        isShowDataSheet.setEnabled(false);
        checkDataSheetPaneUse();
    }
}
