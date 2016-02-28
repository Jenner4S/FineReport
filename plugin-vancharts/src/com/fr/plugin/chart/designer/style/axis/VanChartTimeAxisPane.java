package com.fr.plugin.chart.designer.style.axis;

import com.fr.base.Formula;
import com.fr.base.Utils;
import com.fr.design.chart.ChartSwingUtils;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.editor.DateEditor;
import com.fr.design.editor.editor.Editor;
import com.fr.design.editor.editor.FormulaEditor;
import com.fr.design.gui.date.UIDatePicker;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.DateUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.axis.TimeType;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.attr.axis.VanChartTimeAxis;
import com.fr.plugin.chart.base.VanChartConstants;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * ʱ��������
 */
public class VanChartTimeAxisPane extends VanChartBaseAxisPane {

    private static final long serialVersionUID = 1371126030195384450L;
    private static final String[] TYPES = new String[]{
            TimeType.TIME_YEAR.getLocText(), TimeType.TIME_MONTH.getLocText(), TimeType.TIME_DAY.getLocText(),
            TimeType.TIME_HOUR.getLocText(), TimeType.TIME_MINUTE.getLocText(), TimeType.TIME_SECOND.getLocText()
    };
    private TimeMinMaxValuePane timeMinMaxValuePane;

    public VanChartTimeAxisPane(boolean isXAxis){
        super(isXAxis);
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
                new Component[]{createValueDefinition(),null},
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

    private JPanel createValueDefinition(){
        timeMinMaxValuePane = new TimeMinMaxValuePane();
        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Plugin-ChartF_ValueDefinition"), timeMinMaxValuePane);
    }

    protected void checkFormatType() {
        valueFormat.setComboBoxModel(true);
    }

    @Override
    public void updateBean(VanChartAxis axis) {
        VanChartTimeAxis timeAxis = (VanChartTimeAxis)axis;
        super.updateBean(timeAxis);
        timeMinMaxValuePane.update(timeAxis);
    }

    public VanChartTimeAxis updateBean(String axisName, int position) {
        VanChartTimeAxis axis = new VanChartTimeAxis(axisName, VanChartConstants.AXIS_BOTTOM);
        updateBean(axis);
        return axis;
    }

    @Override
    public void populateBean(VanChartAxis axis) {
        VanChartTimeAxis timeAxis = (VanChartTimeAxis)axis;
        super.populateBean(timeAxis);
        timeMinMaxValuePane.populate(timeAxis);
    }

    /**
     * titleӦ����һ�����ԣ���ֻ�ǶԻ���ı���ʱ�õ���������������ʱ��Ҳ���õõ�
     * @return �绯�����
     */
    @Override
    public String title4PopupWindow() {
        return Inter.getLocText("Plugin-ChartF_TimeAxis");
    }

    private class TimeMinMaxValuePane extends JPanel{

        private static final long serialVersionUID = 5910309251773119715L;
        private UICheckBox maxCheckBox;
        private ValueEditorPane maxValueField;
        private UICheckBox minCheckBox;
        private ValueEditorPane minValueField;

        private UICheckBox mainTickBox;
        private UITextField mainUnitField;
        private UIComboBox mainType;

        private UICheckBox secondTickBox;
        private UITextField secondUnitField;
        private UIComboBox secondType;

        public TimeMinMaxValuePane(){
            setLayout(FRGUIPaneFactory.createBorderLayout());

            initMin();
            initMax();
            initMain();
            initSecond();

            double p = TableLayout.PREFERRED;
            double f = TableLayout.FILL;
            double[] rowSize = {p, p, p};
            double[] columnSize = {p, f};
            Component[][] maxMin = {
                    {minCheckBox, minValueField},
                    {maxCheckBox, maxValueField},
            };
            JPanel maxMinPane = TableLayoutHelper.createTableLayoutPane(maxMin, rowSize, columnSize);


            JPanel mainTickPane = new JPanel();
            mainTickPane.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            mainTickPane.add(mainTickBox);
            mainTickPane.add(mainUnitField);
            mainTickPane.add(mainType);

            JPanel secTickPane = new JPanel();
            secTickPane.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            secTickPane.add(secondTickBox);
            secTickPane.add(secondUnitField);
            secTickPane.add(secondType);

            Component[][] components = {
                    {maxMinPane, null},
                    {mainTickPane, null},
                    {secTickPane, null},
            };
            this.add(TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize));
        }

        private void initMin() {
            // ��Сֵ.
            minCheckBox = new UICheckBox(Inter.getLocText("FR-Chart-Data_Min"));
            Date tmp = null;
            DateEditor dateEditor = new DateEditor(tmp, true, Inter.getLocText("FR-Designer_Date"), UIDatePicker.STYLE_CN_DATETIME1);
            Editor formulaEditor = new FormulaEditor(Inter.getLocText("Plugin-ChartF_Formula"));
            Editor[] editor = new Editor[]{dateEditor, formulaEditor};
            minValueField = new ValueEditorPane(editor);
            minValueField.setEnabled(false);
            minCheckBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    checkBoxUse();
                }
            });
        }

        private void initMax() {
            // ���ֵ
            maxCheckBox = new UICheckBox(Inter.getLocText("FR-Chart-Data_Max"));
            Date tmp = null;
            DateEditor dateEditor = new DateEditor(tmp, true, Inter.getLocText("FR-Designer_Date"), UIDatePicker.STYLE_CN_DATETIME1);
            Editor formulaEditor = new FormulaEditor(Inter.getLocText("Plugin-ChartF_Formula"));
            Editor[] editor = new Editor[]{dateEditor, formulaEditor};
            maxValueField = new ValueEditorPane(editor);
            maxValueField.setEnabled(false);
            maxCheckBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    checkBoxUse();
                }
            });
        }

        private void initMain() {
            // ��Ҫ�̶ȵ�λ
            mainTickBox = new UICheckBox(Inter.getLocText("Plugin-ChartF_MainType"));
            mainUnitField = new UITextField();
            mainUnitField.setPreferredSize(new Dimension(20, 20));
            mainUnitField.setEditable(false);
            mainType = new UIComboBox(TYPES);
            mainType.setEnabled(false);

            mainTickBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    checkBoxUse();
                }
            });

            ChartSwingUtils.addListener(mainTickBox, mainUnitField);
        }

        private void initSecond() {
            // ��Ҫ�̶ȵ�λ
            secondTickBox = new UICheckBox(Inter.getLocText("Plugin-ChartF_SecType"));
            secondUnitField = new UITextField();
            secondUnitField.setPreferredSize(new Dimension(20, 20));
            secondUnitField.setEditable(false);
            secondType = new UIComboBox(TYPES);
            secondType.setEnabled(false);

            secondTickBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    checkBoxUse();
                }
            });

            ChartSwingUtils.addListener(secondTickBox, secondUnitField);
        }


        private void checkBoxUse() {
            minValueField.setEnabled(minCheckBox.isSelected());
            maxValueField.setEnabled(maxCheckBox.isSelected());
            if(minValueField.getCurrentEditor() instanceof FormulaEditor){
                FormulaEditor tmpEditor = (FormulaEditor)minValueField.getCurrentEditor();
                tmpEditor.enableEditor(minCheckBox.isSelected());
            }

            if(maxValueField.getCurrentEditor() instanceof FormulaEditor){
                FormulaEditor tmpEditor = (FormulaEditor)maxValueField.getCurrentEditor();
                tmpEditor.enableEditor(maxCheckBox.isSelected());
            }

            mainType.setEnabled(mainTickBox.isSelected());
            mainUnitField.setEnabled(mainTickBox.isSelected());
            secondType.setEnabled(secondTickBox.isSelected());
            secondUnitField.setEnabled(secondTickBox.isSelected());
        }

        public void update(VanChartTimeAxis timeAxis){
            if (minCheckBox.isSelected()) {//��Сֵ
                if(minValueField.getCurrentEditor() instanceof FormulaEditor){
                    Formula min = (Formula)minValueField.update();
                    timeAxis.setMinValue(min);
                    timeAxis.setCustomMinValue(!StringUtils.isEmpty(min.getPureContent()));
                }else{
                    Date datetmp = (Date)minValueField.update();
                    DateEditor dateEditor = (DateEditor)minValueField.getCurrentEditor();
                    String dateString = dateEditor.getUIDatePickerFormat().format(datetmp);
                    timeAxis.setCustomMinValue(!StringUtils.isEmpty(dateString));
                    timeAxis.setMinValue(new Formula(dateString));
                }
            } else {
                timeAxis.setCustomMinValue(false);
            }
            if (maxCheckBox.isSelected()) {//���ֵ
                if(maxValueField.getCurrentEditor() instanceof FormulaEditor){
                    Formula max = (Formula)maxValueField.update();
                    timeAxis.setMaxValue(max);
                    timeAxis.setCustomMaxValue(!StringUtils.isEmpty(max.getPureContent()));
                }else{
                    Date datetmp = (Date)maxValueField.update();
                    DateEditor dateEditor = (DateEditor)maxValueField.getCurrentEditor();
                    String dateString = dateEditor.getUIDatePickerFormat().format(datetmp);
                    timeAxis.setCustomMaxValue(!StringUtils.isEmpty(dateString));
                    timeAxis.setMaxValue(new Formula(dateString));
                }
            } else {
                timeAxis.setCustomMaxValue(false);
            }
            if (mainTickBox.isSelected() && StringUtils.isNotEmpty(mainUnitField.getText())) {//��Ҫ�̶ȵ�λ
                timeAxis.setCustomMainUnit(true);
                timeAxis.setMainUnit(new Formula(mainUnitField.getText()));
                String item = mainType.getSelectedItem().toString();
                timeAxis.setMainType(TimeType.parseString(item));
            } else {
                timeAxis.setCustomMainUnit(false);
            }
            if (secondTickBox.isSelected() && StringUtils.isNotEmpty(secondUnitField.getText())) { //��Ҫ�̶ȵ�λ
                timeAxis.setCustomSecUnit(true);
                timeAxis.setSecUnit(new Formula(secondUnitField.getText()));
                String item = secondType.getSelectedItem().toString();
                timeAxis.setSecondType(TimeType.parseString(item));
            } else {
                timeAxis.setCustomSecUnit(false);
            }
            checkBoxUse();
        }

        public void populate(VanChartTimeAxis timeAxis){
            // ��Сֵ
            if (timeAxis.isCustomMinValue() && timeAxis.getMinValue() != null) {
                minCheckBox.setSelected(true);
                String dateStr = timeAxis.getMinValue().getPureContent();
                if(!isDateForm(dateStr)){
                    minValueField.populate(timeAxis.getMinValue());
                }else{
                    Date tmpDate = getDateFromFormula(timeAxis.getMinValue());
                    minValueField.populate(tmpDate);
                }

            }

            // ���ֵ
            if (timeAxis.isCustomMaxValue() && timeAxis.getMaxValue() != null) {
                maxCheckBox.setSelected(true);
                String dateStr = timeAxis.getMaxValue().getPureContent();
                if(!isDateForm(dateStr)){
                    maxValueField.populate(timeAxis.getMaxValue());
                }else{
                    Date tmpDate = getDateFromFormula(timeAxis.getMaxValue());
                    maxValueField.populate(tmpDate);
                }
            }

            //��Ҫ�̶ȵ�λ
            if (timeAxis.isCustomMainUnit() && timeAxis.getMainUnit() != null) {
                mainTickBox.setSelected(true);
                mainUnitField.setText(Utils.objectToString(timeAxis.getMainUnit()));
                mainType.setSelectedItem(timeAxis.getMainType().getLocText());
            }

            //��Ҫ�̶ȵ�λ
            if (timeAxis.isCustomSecUnit() && timeAxis.getSecUnit() != null) {
                secondTickBox.setSelected(true);
                secondUnitField.setText(Utils.objectToString(timeAxis.getSecUnit()));
                secondType.setSelectedItem(timeAxis.getSecondType().getLocText());
            }

            checkBoxUse();
        }

        private boolean isDateForm(String form){
            form = Pattern.compile("\"").matcher(form).replaceAll(StringUtils.EMPTY);
            //ȫ�������ֵĻ�ֱ�ӷ��أ�string2Date���ȫ��������Ҳ��ת��������
            if(form.matches("^[+-]?[0-9]*[0-9]$")){
                return false;
            }
            return (DateUtils.string2Date(form, true) != null);
        }

        //����formula������������ת��Ϊָ����ʽ������
        private  Date getDateFromFormula(Formula dateFormula){
            String dateStr = dateFormula.getPureContent();
            dateStr = Pattern.compile("\"").matcher(dateStr).replaceAll(StringUtils.EMPTY);
            Date toDate = DateUtils.string2Date(dateStr, true);
            try {
                String tmp = DateUtils.getDate2LStr(toDate);
                toDate = DateUtils.DATETIMEFORMAT2.parse(tmp);
            } catch (ParseException e) {
                FRLogger.getLogger().error("cannot get date");
            }
            return toDate;
        }

    }
}
