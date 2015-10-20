package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.base.Utils;
import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.ChartSummaryColumn;
import com.fr.chart.chartdata.MoreNameCDDefinition;
import com.fr.data.util.function.DataFunction;
import com.fr.data.util.function.NoneFunction;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.itable.UITable;
import com.fr.design.gui.itable.UITableEditor;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.data.CalculateComboBox;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

/**
 * ���Ա� ���ݼ�����: ϵ���� ʹ���ֶ���.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2012-12-26 ����04:39:46
 */
public class SeriesNameUseFieldNamePane extends FurtherBasicBeanPane<ChartCollection> {
    private static final String[] HEADS = {Inter.getLocText("FR-Chart-Field_Name"), Inter.getLocText("FR-Chart-Series_Name"), Inter.getLocText("FR-Chart-Data_Summary")};
    private static final String[] HEADS_NO_SUMMARY = {Inter.getLocText("FR-Chart-Field_Name"), Inter.getLocText("FR-Chart-Series_Name")};
    private UICorrelationPane seriesDataPane;
    private List<String> field = new ArrayList<String>();
    private JPanel centerPane;
    private boolean isNeedSummary = true;
    private UIObserverListener observerListener;


    public SeriesNameUseFieldNamePane() {
        isNeedSummary = true;
        initCenterPane(HEADS);
    }

    private void initCenterPane(final String[] heads) {
        seriesDataPane = new UICorrelationPane(heads) {

            @Override
            protected ActionListener getAddButtonListener() {
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String[] blank =heads.length == 3?new String[]{StringUtils.EMPTY, StringUtils.EMPTY, Inter.getLocText("FR-Chart-Data_None")}:
                                new String[]{StringUtils.EMPTY, StringUtils.EMPTY};
                        tablePane.addLine(blank);
                        fireTargetChanged();
                    }
                };
            }

            public UITableEditor createUITableEditor() {
                return new InnerTableEditor();
            }

            public void stopPaneEditing(ChangeEvent e) {
                fireTargetChanged();
            }

            public void registerChangeListener(UIObserverListener listener) {
                super.registerChangeListener(listener);
                observerListener = listener;
            }

        };
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f};
        double[] rowSize = {p, p};
        Component[][] components = new Component[][]{
                new Component[]{seriesDataPane},
                new Component[]{new BoldFontTextLabel(Inter.getLocText("FR-Chart-Data_Filter"))},
        };
        centerPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.add(centerPane, BorderLayout.CENTER);
    }

    /**
     * �ж��Ƕ�����
     * @param ob ��������
     * @return ���򷵻�true
     */
    public boolean accept(Object ob) {
        ChartCollection collection = (ChartCollection) ob;
        return collection.getSelectedChart().getFilterDefinition() instanceof MoreNameCDDefinition;
    }

    /**
     * ����
     * @return ����
     */
    public String title4PopupWindow() {
        return Inter.getLocText(new String[]{"FR-Chart-Data_Use", "FR-Chart-Field_Name"});
    }

    /**
     * ����.
     */
    public void reset() {

    }


    /**
     * ���ݼ����ʱ
     * @param list  ���ݼ�
     */
    public void refreshBoxListWithSelectTableData(List list) {
        field.clear();

        for (int i = 0, length = list.size(); i < length; i++) {
            Object ob = list.get(i);
            if (ob != null) {
                field.add(ob.toString());
            }
        }
    }

    /**
     * ������е�box����
     */
    public void clearAllBoxList(){
        field.clear();
    }

    private void fireStop() {
        seriesDataPane.stopCellEditing();
        seriesDataPane.fireTargetChanged();
    }

    /**
     * ���²������
     * @param isNeedSummary �Ƕ���Ҫ���ܣ�ͼ���������ʱ����Ҫ��
     */
    public void relayoutPane(boolean isNeedSummary){
        if(this.isNeedSummary != isNeedSummary){
            this.remove(centerPane);
            initCenterPane(isNeedSummary?HEADS:HEADS_NO_SUMMARY);
            initListener(this);
            this.validate();
            this.isNeedSummary = isNeedSummary;
        }
    }

    private void initListener(Container parentComponent) {
   		for (int i = 0; i < parentComponent.getComponentCount(); i++) {
   			Component tmpComp = parentComponent.getComponent(i);

   			if (tmpComp instanceof Container) {
   				initListener((Container) tmpComp);
   			}
   			if (tmpComp instanceof UIObserver) {
   				((UIObserver) tmpComp).registerChangeListener(observerListener);
   			}
   		}
   	}

    public void populateBean(ChartCollection collection,boolean isNeedSummary){
        relayoutPane(isNeedSummary);
        TopDefinitionProvider topDefinition = collection.getSelectedChart().getFilterDefinition();
        if (topDefinition instanceof MoreNameCDDefinition) {
            MoreNameCDDefinition moreDefinition = (MoreNameCDDefinition) topDefinition;
            ChartSummaryColumn[] chartSummaryColumnArray = moreDefinition.getChartSummaryColumn();
            if (chartSummaryColumnArray == null || chartSummaryColumnArray.length == 0) {
                return;
            }
            List<Object[]> list = new ArrayList<Object[]>();
            for (int i = 0; i < chartSummaryColumnArray.length; i++) {
                ChartSummaryColumn column = chartSummaryColumnArray[i];
                String[] nameArray = {column.getName(), column.getCustomName(), getFunctionString(column.getFunction())};
                list.add(nameArray);
            }
            seriesDataPane.populateBean(list);
        }
    }

    /**
     * ����ChartCollection ���½���
     */
    public void populateBean(ChartCollection collection) {
        this.populateBean(collection,true);
    }

    private String getFunctionString(DataFunction function) {
        for (int i = 0; i < CalculateComboBox.CLASS_ARRAY.length; i++) {
            Class tmp = function.getClass();
            if (ComparatorUtils.equals(tmp, CalculateComboBox.CLASS_ARRAY[i])) {
                return CalculateComboBox.CALCULATE_ARRAY[i];
            }
        }
        return CalculateComboBox.CALCULATE_ARRAY[0];
    }

    /**
     * ����������Ե�ChartCollection
     */
    public void updateBean(ChartCollection collection) {
        TopDefinitionProvider normalDefinition = collection.getSelectedChart().getFilterDefinition();
        MoreNameCDDefinition moreDefinition = null;
        if (normalDefinition instanceof MoreNameCDDefinition) {
            moreDefinition = (MoreNameCDDefinition) normalDefinition;
        } else {
            moreDefinition = new MoreNameCDDefinition();
        }

        List<Object[]> data = seriesDataPane.updateBean();
        ChartSummaryColumn[] dataArray = new ChartSummaryColumn[data.size()];
        for (int i = 0; i < dataArray.length; i++) {
            Object[] line = data.get(i);
            String first = Utils.objectToString(line[0]);
            String second = Utils.objectToString(line[1]);
            if(isNeedSummary){
                String third = Utils.objectToString(line[2]);
                dataArray[i] = new ChartSummaryColumn(first, second, getFcuntionByName(third));
            } else{
                dataArray[i] = new ChartSummaryColumn(first,second,new NoneFunction());
            }
        }
        moreDefinition.setChartSummaryColumn(dataArray);
        collection.getSelectedChart().setFilterDefinition(moreDefinition);
    }

    private DataFunction getFcuntionByName(String name) {
        int index = 0;
        for (int i = 0; i < CalculateComboBox.CALCULATE_ARRAY.length; i++) {
            if (ComparatorUtils.equals(name, CalculateComboBox.CALCULATE_ARRAY[i])) {
                index = i;
            }
        }
        try {
            return (DataFunction) CalculateComboBox.CLASS_ARRAY[index].newInstance();
        } catch (InstantiationException e) {
            FRLogger.getLogger().error("Function Error");
        } catch (IllegalAccessException e) {
            FRLogger.getLogger().error("Function Error");
        }
        return new NoneFunction();
    }

    /**
     * ����Ϊ�µ�ChartCollection
     */
    public ChartCollection updateBean() {
        return null;
    }

    private void checkRow(int row) {
        UITable table = seriesDataPane.getTable();
        Object object = table.getValueAt(row, 0);
        if (object != null) {
            table.setValueAt(object, row, 1);
        }
    }

    private class InnerTableEditor extends UITableEditor {
        private JComponent editorComponent;

        @Override
        public Object getCellEditorValue() {
            if (editorComponent instanceof UIComboBox) {
                return ((UIComboBox) editorComponent).getSelectedItem();
            } else if (editorComponent instanceof UITextField) {
                return ((UITextField) editorComponent).getText();
            } else if (editorComponent instanceof CalculateComboBox) {
                return ((CalculateComboBox) editorComponent).getSelectedItem();
            }
            return super.getCellEditorValue();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, final int row, int column) {

            switch (column) {
                case 0:
                    return createComboxEdit(row, value);
                case 1:
                    return createTextEdit(value);
                default:
                    CalculateComboBox calculateComboBox = new CalculateComboBox();
                    if (value != null) {
                        calculateComboBox.setSelectedItem(value);
                    }
                    calculateComboBox.addItemListener(new ItemListener() {
                        public void itemStateChanged(ItemEvent e) {
                            fireStop();
                        }
                    });
                    editorComponent = calculateComboBox;
                    return calculateComboBox;
            }
        }

        private UIComboBox createComboxEdit(final int row, Object value) {
            UIComboBox uiComboBox = new UIComboBox(field.toArray());

            uiComboBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    fireStop();
                    checkRow(row);
                    seriesDataPane.fireTargetChanged();
                }
            });
            editorComponent = uiComboBox;

            if (value != null && StringUtils.isNotEmpty(value.toString())) {
                uiComboBox.getModel().setSelectedItem(value);
            } else {
                uiComboBox.getModel().setSelectedItem(value);
            }

            return uiComboBox;
        }

        private UITextField createTextEdit(Object value) {
            UITextField uiTextField = new UITextField();
            editorComponent = uiTextField;
            if (value != null) {
                uiTextField.setText(value.toString());
            }

            uiTextField.registerChangeListener(new UIObserverListener() {
                @Override
                public void doChange() {
                    seriesDataPane.fireTargetChanged();// kunsnat: ����ֹͣ�༭, ��Ϊ������ж�.
                }
            });

            return uiTextField;
        }
    }


}
