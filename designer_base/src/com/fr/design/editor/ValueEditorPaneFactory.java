package com.fr.design.editor;

import com.fr.base.Formula;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.editor.editor.*;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ValueEditorPaneFactory {

    /**
     * �������༭����ValueEditorPane
     *
     * @param editors �Զ���ı༭��
     * @return ����pane
     */
    public static ValueEditorPane createValueEditorPane(Editor<?>[] editors) {
        return createValueEditorPane(editors, StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * �����༭�� ���� ������ValueEditorPane
     *
     * @param editors         �༭��
     * @param popupName       ����������
     * @param textEditorValue ֵ
     * @return ����pane
     */
    public static ValueEditorPane createValueEditorPane(Editor<?>[] editors, String popupName, String textEditorValue) {
        return new ValueEditorPane(editors, popupName, textEditorValue);
    }

    /**
     * �����༭�� ���� ������ValueEditorPane
     *
     * @param editors             �༭��
     * @param popupName           ����������
     * @param textEditorValue     ֵ
     * @param editor_center_width �༭������Ŀ��
     * @return ����pane
     */
    public static ValueEditorPane createValueEditorPane(Editor<?>[] editors, String popupName, String textEditorValue, int editor_center_width) {
        return new ValueEditorPane(editors, popupName, textEditorValue, editor_center_width);
    }

    /**
     * ����������ֵ�༭�����
     *
     * @return ����ֵ�༭�����
     */
    public static ValueEditorPane createBasicValueEditorPane() {
        return createValueEditorPane(basicEditors(), StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * ������ʽ�༭�����
     *
     * @return ���ع�ʽ�༭�����
     */
    public static ValueEditorPane createFormulaValueEditorPane() {
        return createValueEditorPane(new Editor[]{new FormulaEditor(Inter.getLocText("Parameter-Formula"))},
                StringUtils.EMPTY, StringUtils.EMPTY);
    }
    /**
     * ����������ֵ�༭�����
     *
     * @param editor_center_width ָ��ֵ�༭����������
     * @return ����ֵ�༭�����
     */
    public static ValueEditorPane createBasicValueEditorPane(int editor_center_width) {
        return createValueEditorPane(basicEditors(), StringUtils.EMPTY, StringUtils.EMPTY, editor_center_width);
    }

    /**
     * Process�õ�editorPane
     *
     * @return ֵ�༭�����
     */
    public static ValueEditorPane createFormEditorPane() {
        return createValueEditorPane(formEditors(), StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * StoreProced�õ�EditorPane
     *
     * @return ֵ�༭�����
     */
    public static ValueEditorPane createStoreProcedValueEditorPane() {
        return createValueEditorPane(StoreProcedureEditors(), StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * ��չ��ValueEditorPane
     *
     * @return ֵ�༭�����
     */
    public static ValueEditorPane createExtendedValueEditorPane() {
        return createValueEditorPane(extendedEditors(), StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * URLʹ�õ�ValueEditorPane
     *
     * @param popupName       ����������
     * @param textEditorValue �༭��ֵ
     * @return ֵ�༭������
     */
    public static ValueEditorPane createURLValueEditorPane(String popupName, String textEditorValue) {
        return createValueEditorPane(URLEditors(popupName, textEditorValue), StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * �������ڵ�ValueEditorPane
     *
     * @param popupName       ����
     * @param textEditorValue ֵ
     * @return ֵ�༭�����
     */
    public static ValueEditorPane createDateValueEditorPane(String popupName, String textEditorValue) {
        return createValueEditorPane(dateEditors(popupName, textEditorValue), StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * �������б༭����ValueEditorPane
     *
     * @return ֵ�༭�����
     */
    public static ValueEditorPane createAllValueEditorPane() {
        return createValueEditorPane(allEditors(), StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * ����������ʽ����pane
     * @return �༭�����
     */
    public static ValueEditorPane  createBasicEditorWithoutFormulaPane(){
        return createValueEditorPane(basicEditorsWithoutFormula(), StringUtils.EMPTY, StringUtils.EMPTY);
    }
    /**
     * ����NoCRNoColumn
     *
     * @return ֵ�༭��
     */
    public static ValueEditorPane createNoCRNoColumnValueEditorPane() {
        return createValueEditorPane(noCRnoColumnEditors(), StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * ������ֵ�༭��
     * @return ֵ�༭��
     */
    public static ValueEditorPane createNumberValueEditorPane(){
        return createValueEditorPane(numberEditors(), StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * �������ڱ༭��
     * @return ֵ�༭��
     */
    public static ValueEditorPane createDateValueEditorPane(){
        return createValueEditorPane(dateEditors(), StringUtils.EMPTY, StringUtils.EMPTY);
    }
    /**
     * ���ݲ���paraUseType �����༭������.
     *
     * @param paraUseType ��������
     * @return ֵ�༭��
     */
    public static ValueEditorPane createVallueEditorPaneWithUseType(int paraUseType) {
        if (paraUseType == ParameterTableModel.NO_CHART_USE) {
            return createBasicValueEditorPane();
        } else if (paraUseType == ParameterTableModel.FORM_NORMAL_USE) {
            return createFormEditorPane();
        } else {
            return createChartHotValueEditorPane(paraUseType);
        }
    }

    /**
     * ͼ���õĲ����༭����ValueEditorPane
     *
     * @param paraUseType ��������
     * @return ֵ�༭��
     */
    public static ValueEditorPane createChartHotValueEditorPane(int paraUseType) {
        return createValueEditorPane(chartHotEditors(paraUseType), StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * ������һЩValueEditorPane���õ���Editors
     *
     * @return ֵ�༭��
     */
    public static Editor<?>[] basicEditors() {
        FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
        return new Editor[]{
                new TextEditor(),
                new IntegerEditor(),
                new DoubleEditor(),
                new DateEditor(true, Inter.getLocText("Date")),
                new BooleanEditor(),
                formulaEditor
        };
    }

    /**
     * ����һЩ�༭��.
     *
     * @return ֵ�༭��
     */
    public static Editor<?>[] formEditors() {
        FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
        return new Editor[]{
                new TextEditor(),
                new IntegerEditor(),
                new DoubleEditor(),
                new DateEditor(true, Inter.getLocText("Date")),
                new BooleanEditor(),
                formulaEditor,
                new WidgetNameEditor(Inter.getLocText("Widget"))
        };
    }

    /**
     * ��չ��Ԫ���һЩ�༭��
     *
     * @return ֵ�༭��
     */
    public static Editor<?>[] extendedEditors() {
        FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
        return new Editor[]{
                new TextEditor(),
                new IntegerEditor(),
                new DoubleEditor(),
                new DateEditor(true, Inter.getLocText("Date")),
                new BooleanEditor(),
                formulaEditor,
                new ParameterEditor(),
                new ColumnRowEditor(Inter.getLocText("Cell"))
        };
    }

	/**
	 * ����Ԫ����ı༭��
	 * @return ֵ�༭��
	 */
	public static Editor<?>[] extendedCellGroupEditors() {
		FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
		return new Editor[]{
				new TextEditor(),
				new IntegerEditor(),
				new DoubleEditor(),
				new DateEditor(true, Inter.getLocText("Date")),
				new BooleanEditor(),
				formulaEditor,
				new ParameterEditor(),
				new ColumnRowEditor(Inter.getLocText("Cell")),
				new ColumnRowGroupEditor(Inter.getLocText("Cell_Group"))
		};
	}

	/**
	 * ֻ�е�Ԫ��͵�Ԫ����ı༭��
	 * @return �༭��b
	 */
	public static Editor<?>[] cellGroupEditor() {
		return new Editor[] {
				new ColumnRowEditor(Inter.getLocText("Cell")),
				new ColumnRowGroupEditor(Inter.getLocText("Cell_Group"))
		};
	}

    /**
     * URL��һЩ�༭��.
     *
     * @param popupName       ����
     * @param textEditorValue ֵ
     * @return ֵ�༭��
     */
    public static Editor<?>[] URLEditors(String popupName, String textEditorValue) {
        return new Editor[]{
                new NoneEditor(textEditorValue, StringUtils.isEmpty(popupName) ? Inter.getLocText("None") : popupName),
                new TextEditor()
        };
    }

    /**
     * �������͵�һЩ�༭��
     *
     * @param popupName       ����
     * @param textEditorValue ֵ
     * @return ֵ�༭��
     */
    public static Editor<?>[] dateEditors(String popupName, String textEditorValue) {
        return new Editor[]{
                new NoneEditor(textEditorValue, StringUtils.isEmpty(popupName) ? Inter.getLocText("None") : popupName),
                new DateEditor(true, Inter.getLocText("Date")),
                new FormulaEditor(Inter.getLocText("Parameter-Formula"))
        };
    }

    /**
     * �������͵ı༭��
     *
     * @return ֵ�༭��
     */
    public static Editor<?>[] allEditors() {
        FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
//        formulaEditor.setEnabled(true);
        return new Editor[]{
                new TextEditor(),
                new IntegerEditor(),
                new DoubleEditor(),
                new DateEditor(true, Inter.getLocText("Date")),
                new BooleanEditor(),
                formulaEditor,
                new ParameterEditor(),
                new ColumnRowEditor(Inter.getLocText("Cell")),
                new ColumnSelectedEditor(),
                //23328 allEditors��ɾ���ؼ�ѡ��
//                new WidgetNameEditor(Inter.getLocText("Widget"))
        };
    }

    /**
     * ������ʽ�༭��
     * @return �༭��������ʽ
     */
    public static Editor<?>[] basicEditorsWithoutFormula(){
        return new Editor[]{
                new TextEditor(),
                new IntegerEditor(),
                new DoubleEditor(),
                new DateEditor(true, Inter.getLocText("Date")),
                new BooleanEditor(),
        };
    }

    /**
     * noCRnoColumn�༭��
     *
     * @return �༭��
     */
    public static Editor<?>[] noCRnoColumnEditors() {
        FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
        return new Editor[]{
                new TextEditor(),
                new IntegerEditor(),
                new DoubleEditor(),
                new DateEditor(true, Inter.getLocText("Date")),
                new BooleanEditor(),
                formulaEditor,
                new ParameterEditor(),
        };
    }

    /**
     * ��ֵ�༭��
      * @return �༭��
     */
    public static Editor<?>[] numberEditors() {
        FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
        return new Editor[]{
                new IntegerEditor(),
                new DoubleEditor(),
                formulaEditor,
                new ParameterEditor(),
        };
    }

    /**
     * ���ڱ༭��
     * @return �༭��
     */
    public static Editor<?>[] dateEditors() {
        FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
        return new Editor[]{
                new DateEditor(true, Inter.getLocText("Date")),
                formulaEditor,
                new ParameterEditor(),
        };
    }

    /**
     * �洢��һЩ�༭��
     *
     * @return �洢���̵ı༭��
     */
    public static Editor<?>[] StoreProcedureEditors() {
        FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
        formulaEditor.setEnabled(true);
        return new Editor[]{
                new CursorEditor(),
                new TextEditor(),
                new IntegerEditor(),
                new DoubleEditor(),
                new DateEditor(true, Inter.getLocText("Date")),
                new BooleanEditor(),
                formulaEditor
        };
    }

    /**
     * ͼ���ȵ��һЩ�༭��
     *
     * @param paraUseType ��������
     * @return ֵ�༭��
     */
    public static Editor[] chartHotEditors(int paraUseType) {
        List<Editor> list = createEditors4Chart(paraUseType);

        list.add(new TextEditor());
        list.add(new IntegerEditor());
        list.add(new DoubleEditor());
        list.add(new DateEditor(true, Inter.getLocText("Date")));
        list.add(new BooleanEditor());

        FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
        formulaEditor.setEnabled(true);
        list.add(formulaEditor);

        return list.toArray(new Editor[list.size()]);
    }

    /**
     * Ϊͼ�����༭��.
     *
     * @param paraUseType ��������
     * @return ֵ�༭��
     */
    private static List<Editor> createEditors4Chart(int paraUseType) {
        if(paraUseType == ParameterTableModel.CHART_PIE_USE) {
            return getPieEditor();
        } else if(paraUseType == ParameterTableModel.CHART_MAP_USE) {
            return getMapEditor();
        } else if(paraUseType == ParameterTableModel.CHART_GIS_USE) {
            return getGisEditor();
        } else if(paraUseType == ParameterTableModel.CHART__XY_USE) {
            return getXYEditor();
        } else if(paraUseType == ParameterTableModel.CHART_BUBBLE_USE) {
            return getBubbbleEdtor();
        } else if(paraUseType == ParameterTableModel.CHART_NO_USE) {
            return getChartNoUseEditor();
        } else if(paraUseType == ParameterTableModel.CHART_METER_USE) {
            return getMeterEditor();
        } else if(paraUseType == ParameterTableModel.CHART_STOCK_USE) {
            return getStockEditor();
        } else if(paraUseType == ParameterTableModel.CHART_GANTT_USE) {
            return getGanttEditor();
        } else if(paraUseType == ParameterTableModel.FORM_ELEMENTCASE_USE) {
            return getFormElementCaseEditor();
        }   else if(paraUseType == ParameterTableModel.FORM_CHART_USE) {
           return getFormChartEditor();
        }
        else {
            return getChartEditor();
        }
    }

    private static List<Editor> getMeterEditor() {
        ConstantsEditor cate = new ConstantsEditor(Inter.getLocText("CategoryName"), new Formula("CATEGORY"));
        cate.setEnabled(false);
        ConstantsEditor value = new ConstantsEditor(Inter.getLocText("Chart-Series_Value"), new Formula("VALUE"));
        value.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(cate);
        lists.add(value);

        return lists;
    }

    private static List<Editor> getPieEditor() {
        ConstantsEditor series = new ConstantsEditor(Inter.getLocText("ChartF-Series_Name"), new Formula("SERIES"));
        series.setEnabled(false);
        ConstantsEditor value = new ConstantsEditor(Inter.getLocText("Chart-Series_Value"), new Formula("VALUE"));
        value.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(series);
        lists.add(value);
        return lists;
    }

    private static List<Editor> getGisEditor() {
        ConstantsEditor areaValue = new ConstantsEditor(Inter.getLocText("Area_Value"), new Formula("AREA_VALUE"));
        areaValue.setEnabled(false);
        ConstantsEditor chartAddress = new ConstantsEditor(Inter.getLocText("Chart-Address"), new Formula("ADDRESS"));
        chartAddress.setEnabled(false);
        ConstantsEditor addressName = new ConstantsEditor(Inter.getLocText("Chart-Address-Name"), new Formula("ADDRESS_NAME"));
        addressName.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(chartAddress);
        lists.add(addressName);
        lists.add(areaValue);

        return lists;
    }

    private static List<Editor> getGanttEditor() {
        ConstantsEditor projectid = new ConstantsEditor(Inter.getLocText("Chart_ProjectID"), new Formula("PROJECTID"));
        projectid.setEnabled(false);
        ConstantsEditor step = new ConstantsEditor(Inter.getLocText("Chart_Step_Name"), new Formula("STEP"));
        step.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(projectid);
        lists.add(step);

        return lists;
    }

    private static List<Editor> getXYEditor() {
        ConstantsEditor series = new ConstantsEditor(Inter.getLocText("ChartF-Series_Name"), new Formula("SERIES"));
        series.setEnabled(false);
        ConstantsEditor value = new ConstantsEditor(Inter.getLocText("Chart-Series_Value"), new Formula("VALUE"));
        value.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(series);
        lists.add(value);

        return lists;
    }

    private static List<Editor> getStockEditor() {
        List<Editor> lists = new ArrayList<Editor>();

        return lists;
    }

    private static List<Editor> getBubbbleEdtor() {
        ConstantsEditor series = new ConstantsEditor(Inter.getLocText("ChartF-Series_Name"), new Formula("SERIES"));
        series.setEnabled(false);
        ConstantsEditor value = new ConstantsEditor(Inter.getLocText("Chart-Series_Value"), new Formula("VALUE"));
        value.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(series);
        lists.add(value);

        return lists;
    }

    private static List<Editor> getChartNoUseEditor() {
        List<Editor> lists = new ArrayList<Editor>();

        return lists;
    }

    private static List<Editor> getMapEditor() {
        ConstantsEditor areaValue = new ConstantsEditor(Inter.getLocText("Area_Value"), new Formula("AREA_VALUE"));
        areaValue.setEnabled(false);
        ConstantsEditor areaName = new ConstantsEditor(Inter.getLocText("Area_Name"), new Formula("AREA_NAME"));
        areaName.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(areaName);
        lists.add(areaValue);

        return lists;
    }

    private static List<Editor> getChartEditor() {
        ConstantsEditor cate = new ConstantsEditor(Inter.getLocText("CategoryName"), new Formula("CATEGORY"));
        cate.setEnabled(false);
        ConstantsEditor series = new ConstantsEditor(Inter.getLocText("ChartF-Series_Name"), new Formula("SERIES"));
        series.setEnabled(false);
        ConstantsEditor value = new ConstantsEditor(Inter.getLocText("Chart-Series_Value"), new Formula("VALUE"));
        value.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(cate);
        lists.add(series);
        lists.add(value);

        return lists;
    }

    private static List<Editor> getFormElementCaseEditor() {

        List<Editor> lists = new ArrayList<Editor>();

        return lists;
    }

    private static List<Editor> getFormChartEditor() {
        ConstantsEditor cate = new ConstantsEditor(Inter.getLocText("CategoryName"), new Formula("CATEGORY"));
        cate.setEnabled(false);
        ConstantsEditor series = new ConstantsEditor(Inter.getLocText("ChartF-Series_Name"), new Formula("SERIES"));
        series.setEnabled(false);
        ConstantsEditor value = new ConstantsEditor(Inter.getLocText("Chart-Series_Value"), new Formula("VALUE"));
        value.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(cate);
        lists.add(series);
        lists.add(value);

        return lists;
    }

    /**
     * ����һ��ʵ��ֵ����ʾֵ�����
     *
     * @param keyColumnPane ʵ��ֵ
     * @param valueDictPane ��ʾֵ
     * @return ����һ��ʵ��ֵ����ʾֵ�����
     */
    public static JPanel createKeyAndValuePane(ValueEditorPane keyColumnPane, ValueEditorPane valueDictPane) {
        JPanel pane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();

        JPanel paneLeft = FRGUIPaneFactory.createBorderLayout_S_Pane();
        pane.add(paneLeft);
        paneLeft.add(new UILabel(" " + Inter.getLocText("Actual_Value") + ":"), BorderLayout.NORTH);
        paneLeft.add(keyColumnPane, BorderLayout.CENTER);

        JPanel paneRight = FRGUIPaneFactory.createBorderLayout_S_Pane();
        pane.add(paneRight);
        paneRight.add(new UILabel(" " + Inter.getLocText("Display_Value") + ":"), BorderLayout.NORTH);

        paneRight.add(valueDictPane, BorderLayout.CENTER);

        return pane;
    }
}
