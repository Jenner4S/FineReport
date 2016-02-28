package com.fr.plugin.chart.designer.style.background;

import com.fr.base.Formula;
import com.fr.base.Utils;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.style.FRFontPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.attr.axis.VanChartAlertValue;
import com.fr.plugin.chart.base.VanChartConstants;
import com.fr.stable.Constants;
import com.fr.stable.StableUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created by Mitisky on 15/10/13.
 */
public class VanChartAlertValuePane extends BasicBeanPane<VanChartAlertValue> {
    private static final long serialVersionUID = -1208941770684286439L;
    private UIButtonGroup alertAxis;
    protected TinyFormulaPane alertValue;
    protected LineComboBox alertLineStyle;
    protected ColorSelectBox alertLineColor;

    private UIButtonGroup alertTextPosition;
    private TinyFormulaPane alertText;
    private UIComboBox fontSize;
    private UIComboBox fontName;
    private ColorSelectBox fontColor;

    public VanChartAlertValuePane(){
        initComponents();
    }

    private void initComponents(){
        alertValue = new TinyFormulaPane();
        alertLineStyle = new LineComboBox(VanChartConstants.ALERT_LINE_STYLE);
        alertLineColor = new ColorSelectBox(100);
        alertTextPosition = new UIButtonGroup(new String[]{Inter.getLocText("Plugin-ChartF_AxisTop"),Inter.getLocText("Plugin-ChartF_AxisBottom")});
        alertText = new TinyFormulaPane();
        fontSize = new UIComboBox(FRFontPane.FONT_SIZES);
        fontName = new UIComboBox(Utils.getAvailableFontFamilyNames4Report());
        fontColor = new ColorSelectBox(100);
    }


    private void doLayoutPane(){
        this.removeAll();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //����������
        JPanel top = FRGUIPaneFactory.createBorderLayout_L_Pane();
        this.add(top);
        top.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("Plugin-ChartF_AlertSet") + ":", null));
        top.add(createTopPane());
        //��ʾ����
        JPanel bottom = FRGUIPaneFactory.createBorderLayout_L_Pane();
        this.add(bottom);
        bottom.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("Plugin-ChartF_AlertText") + ":", null));
        bottom.add(createBottomPane());
    }

    protected JPanel createTopPane()
    {
        double p = TableLayout.PREFERRED;
        double[] columnSize = {p,p};
        double[] rowSize = {p,p,p,p};
        Component[][] components = getTopPaneComponents();

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    protected Component[][] getTopPaneComponents() {
        return new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Chart-Layout_Position")),alertAxis},
                new Component[]{new UILabel(Inter.getLocText("Chart-Use_Value")),alertValue},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_LineStyle")),alertLineStyle},
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Color_Color")),alertLineColor},
        };
    }

    private JPanel createBottomPane(){
        alertTextPosition.setSelectedIndex(0);
        double p = TableLayout.PREFERRED;
        double[] columnSize = {p,p};
        double[] rowSize = {p,p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Chart-Layout_Position")),alertTextPosition},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Content")),alertText},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Font")),fontName},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer-FRFont_Size")),fontSize},
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Color_Color")),fontColor},
        };

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    private void checkPositionPane() {
        boolean selectXAxis = VanChartAttrHelper.isXAxis(alertAxis.getSelectedItem().toString());
        if(selectXAxis){
            alertTextPosition = new UIButtonGroup(new String[]{Inter.getLocText("Plugin-ChartF_AxisTop"),Inter.getLocText("Plugin-ChartF_AxisBottom")});
        } else {
            alertTextPosition = new UIButtonGroup(new String[]{Inter.getLocText("Plugin-ChartF_AlertLeft"),Inter.getLocText("Plugin-ChartF_AlertRight")});
        }
        doLayoutPane();
    }

    protected String title4PopupWindow(){
        return Inter.getLocText("Plugin-ChartF_AlertLine");
    }

    public void populateBean(VanChartAlertValue chartAlertValue){
        alertAxis = new UIButtonGroup(chartAlertValue.getAxisNamesArray(), chartAlertValue.getAxisNamesArray());
        alertAxis.setSelectedItem(chartAlertValue.getAxisName());
        alertAxis.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                checkPositionPane();
            }
        });

        checkPositionPane();

        alertValue.populateBean(Utils.objectToString(chartAlertValue.getAlertValueFormula()));
        alertLineStyle.setSelectedLineStyle(chartAlertValue.getLineStyle().getLineStyle());
        alertLineColor.setSelectObject(chartAlertValue.getLineColor().getSeriesColor());

        if(VanChartAttrHelper.isXAxis(chartAlertValue.getAxisName())){
            alertTextPosition.setSelectedIndex(chartAlertValue.getAlertPosition() == Constants.TOP ? 0 : 1);
        } else {
            alertTextPosition.setSelectedIndex(chartAlertValue.getAlertPosition() == Constants.LEFT ? 0 : 1);
        }

        if (chartAlertValue.getAlertContentFormula() instanceof Formula) {
            alertText.populateBean(((Formula) chartAlertValue.getAlertContentFormula()).getContent());
        } else {
            alertText.populateBean(Utils.objectToString(chartAlertValue.getAlertContentFormula()));
        }
        fontName.setSelectedItem(chartAlertValue.getAlertFont().getName());
        fontSize.setSelectedItem(chartAlertValue.getAlertFont().getSize());
        fontColor.setSelectObject(chartAlertValue.getAlertFont().getForeground());
    }

    @Override
    public void updateBean(VanChartAlertValue chartAlertValue) {
        chartAlertValue.setAxisName(alertAxis.getSelectedItem().toString());

        chartAlertValue.setAlertValueFormula(new Formula(alertValue.updateBean()));
        chartAlertValue.getLineColor().setSeriesColor(alertLineColor.getSelectObject());
        chartAlertValue.getLineStyle().setLineStyle(alertLineStyle.getSelectedLineStyle());

        String contentString = alertText.updateBean();
        Object contentObj;
        if (StableUtils.maybeFormula(contentString)) {
            contentObj = new Formula(contentString);
        } else {
            contentObj = contentString;
        }

        chartAlertValue.setAlertContentFormula(contentObj);
        String name = Utils.objectToString(fontName.getSelectedItem());
        int size = Utils.objectToNumber(fontSize.getSelectedItem(), true).intValue();
        Color color = fontColor.getSelectObject();
        chartAlertValue.setAlertFont(FRFont.getInstance(name, Font.PLAIN, size, color));
        if(VanChartAttrHelper.isXAxis(Utils.objectToString(alertAxis.getSelectedItem()))){
            chartAlertValue.setAlertPosition(alertTextPosition.getSelectedIndex() == 0 ? Constants.TOP : Constants.BOTTOM);
        } else {
            chartAlertValue.setAlertPosition(alertTextPosition.getSelectedIndex() == 0 ? Constants.LEFT : Constants.RIGHT);
        }
    }

    public VanChartAlertValue updateBean(){
        return null;
    }
}
