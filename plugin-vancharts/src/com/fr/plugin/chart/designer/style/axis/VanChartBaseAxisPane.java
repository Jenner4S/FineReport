package com.fr.plugin.chart.designer.style.axis;

import com.fr.base.BaseUtils;
import com.fr.base.Formula;
import com.fr.base.Utils;
import com.fr.chart.base.TextAttr;
import com.fr.chart.chartattr.Title;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.style.FormatPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.style.ChartTextAttrPane;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.attr.axis.AxisTickLineType;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.base.VanChartConstants;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.stable.Constants;
import com.fr.stable.CoreConstants;
import com.fr.stable.StableUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ������Ļ�����������࣬ʱ�䣬ֵ�ȹ����Ĳ��֡�
 */
public class VanChartBaseAxisPane extends FurtherBasicBeanPane<VanChartAxis> {

    private static final long serialVersionUID = -5717246802333308973L;
    private static final double ROTATION_MAX = 90.0;
    protected TinyFormulaPane titleContent;
    protected UIButtonGroup<Integer> titleAlignPane;
    protected UIToggleButton titleUseHtml;
    protected ChartTextAttrPane titleTextAttrPane;
    protected UINumberDragPane titleTextRotation;

    protected UIButtonGroup showLabel;
    protected ChartTextAttrPane labelTextAttrPane;
    protected UINumberDragPane labelTextRotation;
    protected UIButtonGroup<Integer> labelGapStyle;
    protected UITextField labelGapValue;

    protected LineComboBox axisLineStyle;
    protected ColorSelectBox axisLineColor;
    protected UIButtonGroup<AxisTickLineType> mainTick;
    protected UIButtonGroup<AxisTickLineType> secondTick;

    protected UIButtonGroup<Integer> position;
    protected UIToggleButton reversed;

    protected UIButtonGroup<Integer> axisLimitSize;
    protected UISpinner maxProportion;

    protected UIButtonGroup valueFormatStyle;
    protected FormatPane valueFormat;
    protected JPanel centerPane;
    protected UITextArea customFormatter;
    protected UIToggleButton valueUseHtml;

    private VanChartStylePane parent;

    public VanChartBaseAxisPane(){
        this(true);
    }

    public VanChartBaseAxisPane(boolean isXAxis){
        this.setLayout(new BorderLayout());
        this.add(createContentPane(isXAxis), BorderLayout.CENTER);
    }

    public void setParent(VanChartStylePane parent){
        this.parent = parent;
    }

    protected void reLayoutPane(boolean isXAxis){
        this.removeAll();
        this.add(createContentPane(isXAxis), BorderLayout.CENTER);
    }

    protected JPanel createContentPane(boolean isXAxis){

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p,p,p,p,p,p,p,p,p,p,p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{new JSeparator(),null},
                new Component[]{createTitlePane(new double[]{p, p, p, p, p}, columnSize, isXAxis),null},
                new Component[]{new JSeparator(),null},
                new Component[]{createLabelPane(new double[]{p, p, p}, columnSize),null},
                new Component[]{new JSeparator(),null},
                new Component[]{createLineStylePane(new double[]{p, p,p,p}, columnSize),null},
                new Component[]{new JSeparator(),null},
                new Component[]{createAxisPositionPane(new double[]{p, p}, columnSize, isXAxis),null},
                new Component[]{new JSeparator(),null},
                new Component[]{createDisplayStrategy(new double[]{p, p}, columnSize),null},
                new Component[]{new JSeparator(),null},
                new Component[]{createValueStylePane(),null},
        };

        return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
    }

    protected JPanel createTitlePane(double[] row, double[] col, boolean isXAxis){
        titleAlignPane = isXAxis ? getXAxisTitleAlignPane() : getYAxisTitleAlignPane();
        titleAlignPane.setSelectedItem(Constants.CENTER);
        titleContent = new TinyFormulaPane();
        titleUseHtml = new UIToggleButton(Inter.getLocText("Plugin-ChartF_Html"));
        titleTextAttrPane = new ChartTextAttrPane();
        titleTextRotation = new UINumberDragPane(-ROTATION_MAX,ROTATION_MAX);
        if(isXAxis){
            titleTextRotation.populateBean(0.0);
        } else {
            titleTextRotation.populateBean(-ROTATION_MAX);
        }
        Component[][] components = new Component[][]{
                new Component[]{titleContent,null},
                new Component[]{titleAlignPane,null},
                new Component[]{titleUseHtml,null},
                new Component[]{titleTextAttrPane,null},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_TextRotation")),titleTextRotation},
        };

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, row, col);
        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(PaneTitleConstants.CHART_STYLE_TITLE_TITLE, panel);
    }

    private UIButtonGroup<Integer> getXAxisTitleAlignPane(){
        Icon[] alignmentIconArray = {BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_left_normal.png"),
                BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_center_normal.png"),
                BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_right_normal.png")};
        Integer[] alignment = new Integer[]{Constants.LEFT, Constants.CENTER, Constants.RIGHT};

        return new UIButtonGroup<Integer>(alignmentIconArray, alignment);
    }
    private UIButtonGroup<Integer> getYAxisTitleAlignPane(){
        Icon[] alignmentIconArray = {BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/v_top_normal.png"),
                BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/v_center_normal.png"),
                BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/v_down_normal.png")};
        Integer[] alignment = new Integer[]{Constants.TOP, Constants.CENTER, Constants.BOTTOM};

        return new UIButtonGroup<Integer>(alignmentIconArray, alignment);
    }

    protected JPanel createLabelPane(double[] row, double[] col){
        showLabel = new UIButtonGroup(new String[]{Inter.getLocText("Chart-Use_Show"), Inter.getLocText("Plugin-ChartF_Hidden")});
        labelTextAttrPane = new ChartTextAttrPane();

        labelTextRotation = new UINumberDragPane(-ROTATION_MAX,ROTATION_MAX);
        labelGapStyle = new UIButtonGroup<Integer>(new String[]{Inter.getLocText("Plugin-ChartF_Automatic"),Inter.getLocText("Plugin-ChartF_Fixed")});
        labelGapValue = new UITextField();
        Component[][] gapComponents = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_TextRotation")), labelTextRotation},
                new Component[]{new UILabel(Inter.getLocText("ChartF-Label_Interval")), labelGapStyle},
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Axis_labelInterval")),labelGapValue},
        };
        JPanel gapPanel = TableLayoutHelper.createTableLayoutPane(gapComponents, row, col);

        Component[][] components = new Component[][]{
                new Component[]{showLabel,null},
                new Component[]{labelTextAttrPane, null},
                new Component[]{gapPanel,null},
        };
        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, row, col);
        showLabel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkLabelPane();
            }
        });
        labelGapStyle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkLabelGapValuePane();
            }
        });
        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(PaneTitleConstants.CHART_STYLE_LABEL_TITLE, panel);
    }

    protected JPanel createLineStylePane(double[] row, double[] col){
        axisLineStyle = new LineComboBox(CoreConstants.LINE_STYLE_ARRAY_4_AXIS);
        axisLineColor = new ColorSelectBox(100);
        String[] strings = new String[]{Inter.getLocText("Plugin-ChartF_Open"),Inter.getLocText("Plugin-ChartF_Close")};
        AxisTickLineType[] values = new AxisTickLineType[]{AxisTickLineType.TICK_LINE_OUTSIDE, AxisTickLineType.TICK_LINE_NONE};
        mainTick = new UIButtonGroup<AxisTickLineType>(strings, values);
        secondTick = new UIButtonGroup<AxisTickLineType>(strings, values);

        JPanel panel = TableLayoutHelper.createTableLayoutPane(getLineStylePaneComponents(), row, col);
        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Plugin-ChartF_AxisLineStyle"), panel);
    }

    protected Component[][] getLineStylePaneComponents() {
        return new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_type")),axisLineStyle} ,
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Color_Color")),axisLineColor},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_MainGraduationLine")),mainTick},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_SecondGraduationLine")),secondTick},
        };
    }

    protected JPanel createAxisPositionPane(double[] row, double[] col, boolean isXAxis){
        position = new UIButtonGroup<Integer>(getAxisPositionNameArray(isXAxis), getAxisPositionValueArray(isXAxis));
        reversed = new UIToggleButton(Inter.getLocText("Plugin-ChartF_OpenAxisReversed"));
        Component[][] components = new Component[][]{
                new Component[]{position, null},
                new Component[]{reversed,null},
        } ;

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, row, col);
        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Chart-Layout_Position"), panel);
    }

    private String[] getAxisPositionNameArray(boolean isXAxis){
        if(isXAxis){
            return new String[]{Inter.getLocText("Plugin-ChartF_AxisTop"), Inter.getLocText("Plugin-ChartF_AxisBottom"), Inter.getLocText("Plugin-ChartF_AxisVerticalZero")};
        } else {
            return new String[]{Inter.getLocText("Chart-Layout_Left"), Inter.getLocText("Chart-Layout_Right"), Inter.getLocText("Plugin-ChartF_AxisVerticalZero")};
        }
    }

    private Integer[] getAxisPositionValueArray(boolean isXAxis){
        if(isXAxis){
            return new Integer[]{VanChartConstants.AXIS_TOP, VanChartConstants.AXIS_BOTTOM, VanChartConstants.AXIS_VERTICAL_ZERO};
        } else {
            return new Integer[]{VanChartConstants.AXIS_LEFT, VanChartConstants.AXIS_RIGHT, VanChartConstants.AXIS_VERTICAL_ZERO};
        }
    }

    protected JPanel createDisplayStrategy(double[] row, double[] col){
        maxProportion = new UISpinner(0,100,1,30);
        axisLimitSize = new UIButtonGroup<Integer>(new String[]{Inter.getLocText("Plugin-ChartF_LimitAreaSize"),Inter.getLocText("Plugin-ChartF_NotLimitAreaSize")});
        Component[][] components = new Component[][]{
                new Component[]{axisLimitSize,null},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_AxisProportion")+":"),maxProportion},
        };
        JPanel panel = TableLayoutHelper.createTableLayoutPane(components,row,col);

        axisLimitSize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkMaxProPortionUse();
            }
        });

        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Plugin-ChartF_DisplayStrategy"), panel);
    }

    protected JPanel createValueStylePane(){
        valueFormatStyle = new UIButtonGroup<Integer>(new String[]{Inter.getLocText("Plugin-ChartF_Common"),
                Inter.getLocText("Plugin-ChartF_Custom")});

        valueFormat = new FormatPane();
        checkFormatType();

        customFormatter = new UITextArea();
        valueUseHtml = new UIToggleButton(Inter.getLocText("Plugin-ChartF_Html"));
        JPanel customPanel = new JPanel(new BorderLayout(0, 4));
        customPanel.add(customFormatter, BorderLayout.CENTER);
        customPanel.add(valueUseHtml, BorderLayout.SOUTH);

        centerPane = new JPanel(new CardLayout());
        centerPane.add(valueFormat,Inter.getLocText("Plugin-ChartF_Common"));
        centerPane.add(customPanel, Inter.getLocText("Plugin-ChartF_Custom"));

        JPanel contentPane = new JPanel(new BorderLayout(0, 4));
        contentPane.add(valueFormatStyle, BorderLayout.NORTH);
        contentPane.add(centerPane, BorderLayout.CENTER);

        valueFormatStyle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkCardPane();
            }
        });

        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Chart-Use_Format"), contentPane);
    }

    protected void checkFormatType() {

    }

    protected void checkAllUse() {
        checkCardPane();
        checkLabelPane();
        checkMaxProPortionUse();
    }

    protected void checkCardPane() {
        if(centerPane != null && valueFormatStyle != null){
            CardLayout cardLayout = (CardLayout) centerPane.getLayout();
            if (valueFormatStyle.getSelectedIndex() == 1) {
                cardLayout.show(centerPane,Inter.getLocText("Plugin-ChartF_Custom"));
            } else {
                cardLayout.show(centerPane, Inter.getLocText("Plugin-ChartF_Common"));
            }
        }
    }

    protected void checkLabelPane() {
        if(showLabel != null){
            boolean enabled = showLabel.getSelectedIndex() == 0;
            if(labelTextAttrPane != null){
                labelTextAttrPane.setEnabled(enabled);
            }
            if(labelTextRotation != null){
                labelTextRotation.setEnabled(enabled);
            }
            if(labelGapValue != null){
                labelGapValue.setEnabled(enabled);
            }
            if(labelGapStyle != null){
                labelGapStyle.setEnabled(enabled);
            }
            if(enabled){
                checkLabelGapValuePane();
            }
        }
    }

    private void checkLabelGapValuePane() {
        if(labelGapValue != null && labelGapStyle != null){
            labelGapValue.setEnabled(labelGapStyle.getSelectedIndex() == 1);
        }
    }

    //��������ʾռ���Ƿ����
    private void checkMaxProPortionUse() {
        if(maxProportion != null && axisLimitSize != null){
            maxProportion.setEnabled(axisLimitSize.getSelectedIndex() == 0 && axisLimitSize.isEnabled());
        }
    }

    /**
     * �Ƿ���ָ������
     * @param ob ����
     * @return �Ƿ���ָ������
     */
    public boolean accept(Object ob){
        return false;
    }

    /**
     * titleӦ����һ�����ԣ���ֻ�ǶԻ���ı���ʱ�õ���������������ʱ��Ҳ���õõ�
     * @return �绯�����
     */
    @Override
    public String title4PopupWindow(){
        return Inter.getLocText("Plugin-ChartF_CategoryAxis");
    }

    /**
     * ����
     */
    public void reset(){

    }
    @Override
    public void populateBean(VanChartAxis axis) {
        if(parent != null){
            reLayoutPane(VanChartAttrHelper.isXAxis(axis.getAxisName()));
            parent.initAllListeners();
        }

        populateTitle(axis);

        populateLabel(axis);

        populateLineStyle(axis);

        populatePosition(axis);

        populateDisplayStrategy(axis);

        populateFormat(axis);

        checkAllUse();
    }

    //����
    private void populateTitle(VanChartAxis axis){
        Title axisTitle = axis.getTitle();
        if(axisTitle != null){
            if (axisTitle.getTextObject() instanceof Formula && titleContent != null) {
                titleContent.populateBean(((Formula) axisTitle.getTextObject()).getContent());
            } else if(titleContent != null){
                titleContent.populateBean(Utils.objectToString(axisTitle.getTextObject()));
            }

            if(titleAlignPane != null){
                titleAlignPane.setSelectedItem(axisTitle.getPosition());
            }
            if(titleTextAttrPane != null){
                titleTextAttrPane.populate(axisTitle.getTextAttr());
            }
            if(titleUseHtml != null){
                titleUseHtml.setSelected(axis.isTitleUseHtml());
            }
            if(titleTextRotation != null){
                titleTextRotation.populateBean((double)axisTitle.getTextAttr().getRotation());
            }
        }
    }

    //��ǩ
    private void populateLabel(VanChartAxis axis){
        if(showLabel != null){
            showLabel.setSelectedIndex(axis.isShowAxisLabel() ? 0 : 1);
        }
        TextAttr labelTextAttr = axis.getTextAttr();
        if(labelTextAttrPane != null){
            labelTextAttrPane.populate(labelTextAttr);
        }
        if(labelTextRotation != null){
            labelTextRotation.populateBean((double)labelTextAttr.getRotation());
        }
        if(labelGapStyle != null){
            labelGapStyle.setSelectedIndex(axis.isAutoLabelGap() ? 0 : 1);
        }
        if(labelGapValue != null){
            labelGapValue.setText(axis.getLabelNumber().getContent());
        }
    }

    //������ʽ
    private void populateLineStyle(VanChartAxis axis){
        if(axisLineStyle != null){
            axisLineStyle.setSelectedLineStyle(axis.getAxisStyle());
        }
        if(axisLineColor != null){
            axisLineColor.setSelectObject(axis.getAxisColor());
        }
        if(mainTick != null){
            mainTick.setSelectedItem(axis.getMainTickLine());
        }
        if(secondTick != null){
            secondTick.setSelectedItem(axis.getSecTickLine());
        }
    }

    //λ��
    private void populatePosition(VanChartAxis axis){
        if(position != null){
            position.setSelectedItem(axis.getPosition());
            if(position.getSelectedItem() == null){
                position.setSelectedIndex(1);
            }
        }
        if(reversed != null){
            reversed.setSelected(axis.hasAxisReversed());
        }
    }

    //��ʾ����
    private void populateDisplayStrategy(VanChartAxis axis) {
        if(axisLimitSize != null){
            axisLimitSize.setSelectedIndex(axis.isLimitSize() ? 0 : 1);
        }
        if(maxProportion != null){
            maxProportion.setValue(axis.getMaxHeight());
        }

    }

    //��ʽ
    protected void populateFormat(VanChartAxis axis) {
        if(valueFormatStyle != null){
            valueFormatStyle.setSelectedIndex(axis.isCommonValueFormat() ? 0 : 1);
        }
        if(valueFormat != null){
            valueFormat.populateBean(axis.getFormat());
        }
        if(customFormatter != null){
            customFormatter.setText(axis.getCustomValueFormatText());
        }
        if(valueUseHtml != null){
            valueUseHtml.setSelected(axis.isCustomValueFormatUseHtml());
        }
    }

    public void updateBean(VanChartAxis axis) {
        updateTitle(axis);

        updateLabel(axis);

        updateLineStyle(axis);

        updatePosition(axis);

        updateDisplayStrategy(axis);

        updateFormat(axis);
    }
    //����
    private void updateTitle(VanChartAxis axis){
        Title axisTitle = axis.getTitle();
        if(axisTitle == null){
            axisTitle = new Title();
            axis.setTitle(axisTitle);
        }

        if(titleContent != null){
            String titleString = titleContent.updateBean();
            Object titleObj;
            if (StableUtils.maybeFormula(titleString)) {
                titleObj = new Formula(titleString);
            } else {
                titleObj = titleString;
            }
            axisTitle.setTextObject(titleObj);
        }
        if(titleAlignPane != null){
            axisTitle.setPosition(titleAlignPane.getSelectedItem());
        }

        TextAttr textAttr = axisTitle.getTextAttr();
        if(titleTextAttrPane != null){
            titleTextAttrPane.update(textAttr);
        }
        if(titleUseHtml != null){
            axis.setTitleUseHtml(titleUseHtml.isSelected());
        }
        if(titleTextRotation != null){
            textAttr.setRotation(titleTextRotation.updateBean().intValue());
        }
    }

    //��ǩ
    private void updateLabel(VanChartAxis axis){
        if(showLabel != null){
            axis.setShowAxisLabel(showLabel.getSelectedIndex() == 0);
        }
        TextAttr labelTextAttr = axis.getTextAttr();
        if(labelTextAttrPane != null){
            labelTextAttrPane.update(labelTextAttr);
        }
        if(labelTextRotation != null){
            labelTextAttr.setRotation(labelTextRotation.updateBean().intValue());
        }
        if(labelGapStyle != null){
            axis.setAutoLabelGap(labelGapStyle.getSelectedIndex() == 0);
        }
        if(labelGapValue != null){
            if(!axis.isAutoLabelGap()){
                axis.setLabelIntervalNumber(new Formula(labelGapValue.getText()));
            } else {
                axis.setLabelIntervalNumber(new Formula("0"));
            }
        }
    }

    //������ʽ
    private void updateLineStyle(VanChartAxis axis){
        if(axisLineStyle != null){
            axis.setAxisStyle(axisLineStyle.getSelectedLineStyle());
        }
        if(axisLineColor != null){
            axis.setAxisColor(axisLineColor.getSelectObject());
        }
        if(mainTick != null){
            axis.setMainTickLine(mainTick.getSelectedItem());
        }
        if(secondTick != null){
            axis.setSecTickLine(secondTick.getSelectedItem());
        }
    }

    //λ��
    private void updatePosition(VanChartAxis axis){
        if(position != null){
            axis.setPosition(position.getSelectedItem());
        }
        if(reversed != null){
            axis.setAxisReversed(reversed.isSelected());
        }
    }

    //��ʾ����
    private void updateDisplayStrategy(VanChartAxis axis){
        if(axisLimitSize != null){
            axis.setLimitSize(axisLimitSize.getSelectedIndex() == 0);
        }
        if(maxProportion != null){
            axis.setMaxHeight(maxProportion.getValue());
        }
    }

    protected void updateFormat(VanChartAxis axis) {
        if(valueFormatStyle != null){
            axis.setCommonValueFormat(valueFormatStyle.getSelectedIndex() == 0);
        }
        if(valueFormat != null){
            axis.setFormat(valueFormat.update());
        }
        if(customFormatter != null){
            axis.setCustomValueFormatText(customFormatter.getText());
        }
        if(valueUseHtml != null){
            axis.setCustomValueFormatUseHtml(valueUseHtml.isSelected());
        }
    }

    /**
     * X�����᲻ͬ�����л�,newһ���µ�
     * @param axisName ����������
     * @return �µ�axis
     */
    public VanChartAxis updateBean(String axisName, int position){
        VanChartAxis axis = new VanChartAxis(axisName, position);
        this.updateBean(axis);
        return axis;
    }

    public VanChartAxis updateBean(){
        return null;
    }
}
