package com.fr.design.chart.report;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.Formula;
import com.fr.base.Utils;
import com.fr.chart.chartdata.BaseSeriesDefinition;
import com.fr.chart.chartdata.GisMapReportDefinition;
import com.fr.chart.chartdata.SeriesDefinition;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.constants.UIConstants;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itable.UITableEditor;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;

/**
 * ���Ա�gis��ͼ��Ԫ������Դ���ý���
 *
 * @author eason
 */
public class GisMapReportDataContentPane extends FurtherBasicBeanPane<GisMapReportDefinition> implements UIObserver {
    private UIButtonGroup<String> addressType;
    private UIButtonGroup<String> lnglatOrder;
    private TinyFormulaPane addressPane;
    private TinyFormulaPane addressNamePane;
    private UICorrelationPane seriesPane;
    private JPanel orderPane;
    private ArrayList<ChangeListener> changeListeners = new ArrayList<ChangeListener>();

    public GisMapReportDataContentPane() {
        initCom();
    }

    private void initCom() {
        this.setLayout(new BorderLayout(0, 0));
        addressType = new UIButtonGroup<String>(new String[]{Inter.getLocText("Chart-Address"), Inter.getLocText("Chart-LngLat")});
        lnglatOrder = new UIButtonGroup<String>(new String[]{Inter.getLocText("Chart-LngFirst"), Inter.getLocText("Chart-LatFirst")});
        addressPane = new TinyFormulaPane();
        addressNamePane = new TinyFormulaPane();
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = new double[]{p, f};
        double[] rowSize = new double[]{p, p, p};

        orderPane = new JPanel(new BorderLayout(LayoutConstants.VGAP_MEDIUM, 0)) {
            @Override
            public Dimension getPreferredSize() {
                if (this.isVisible()) {
                    return super.getPreferredSize();
                } else {
                    return new Dimension(0, 0);
                }
            }
        };
        orderPane.add(new UILabel(Inter.getLocText("Chart-LatLngOrder")), BorderLayout.WEST);
        orderPane.add(lnglatOrder, BorderLayout.CENTER);
        orderPane.setVisible(false);
        lnglatOrder.setSelectedIndex(0);
        addressType.setSelectedIndex(0);
        Component[][] components = new Component[][]{
                new Component[]{addressType, addressPane},
                new Component[]{orderPane, null},
                new Component[]{new UILabel(" " +Inter.getLocText("Chart-Address-Name")+":", SwingConstants.RIGHT), addressNamePane},
        };
        JPanel northPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);

        this.add(northPane, BorderLayout.NORTH);

        String[] columnNames = new String[]{Inter.getLocText(new String[]{"Filed", "Title"}), Inter.getLocText("Area_Value")};
        seriesPane = new UICorrelationPane(columnNames) {
            public UITableEditor createUITableEditor() {
                return new InnerTableEditor();
            }
        };

        this.add(seriesPane, BorderLayout.CENTER);
    }

    /**
     * �������.
     * @param ob ����
     * @return true��ʾ����
     */
    public boolean accept(Object ob) {
        return true;
    }

    /**
     * ����
     */
    public void reset() {

    }

    /**
     * ���浯������.
     * @return  ����
     */
    public String title4PopupWindow() {
        return Inter.getLocText("Cell");
    }

    @Override
    public void populateBean(GisMapReportDefinition ob) {
        if (ob.getCategoryName() != null) {
            if (ob.isAddress()) {
                addressType.setSelectedIndex(0);
                orderPane.setVisible(false);
            } else {
                addressType.setSelectedIndex(1);
                orderPane.setVisible(true);
            }

            if (ob.isLngFirst()) {
                lnglatOrder.setSelectedIndex(0);
            } else {
                lnglatOrder.setSelectedIndex(1);
            }

            addressPane.populateBean(Utils.objectToString(ob.getCategoryName()));
            if (ob.getAddressName() != null) {
                addressNamePane.populateBean(Utils.objectToString(ob.getAddressName()));
            }
            int size = ob.getTitleValueSize();
            List paneList = new ArrayList();
            for (int i = 0; i < size; i++) {
                BaseSeriesDefinition first = ob.getTitleValueWithIndex(i);
                if (first != null && first.getSeriesName() != null && first.getValue() != null) {
                    paneList.add(new Object[]{first.getSeriesName(), first.getValue()});
                }
            }
            if (!paneList.isEmpty()) {
                seriesPane.populateBean(paneList);
            }
        }
    }

    @Override
    public GisMapReportDefinition updateBean() {
        GisMapReportDefinition reportDefinition = new GisMapReportDefinition();
        if (this.addressType.getSelectedIndex() == 0) {
            reportDefinition.setAddressType(true);
            orderPane.setVisible(false);
        } else {
            reportDefinition.setAddressType(false);
            orderPane.setVisible(true);
        }

        if (this.lnglatOrder.getSelectedIndex() == 0) {
            reportDefinition.setLnglatOrder(true);
        } else {
            reportDefinition.setLnglatOrder(false);
        }

        String address = addressPane.updateBean();
        if (StringUtils.isBlank(address)) {
            return null;
        }
        if (StableUtils.canBeFormula(address)) {
            reportDefinition.setCategoryName(new Formula(address));
        } else {
            reportDefinition.setCategoryName(address);
        }

        String addressName = addressNamePane.updateBean();
        if (addressName != null && !StringUtils.isBlank(addressName)) {
            if (StableUtils.canBeFormula(addressName)) {
                reportDefinition.setAddressName(addressName);
            } else {
                reportDefinition.setAddressName(addressName);
            }
        }
        List values = seriesPane.updateBean();
        if (values != null && !values.isEmpty()) {
            for (int i = 0, size = values.size(); i < size; i++) {
                Object[] objects = (Object[]) values.get(i);
                Object name = objects[0];
                Object value = objects[1];

                if (StableUtils.canBeFormula(value)) {
                    value = new Formula(Utils.objectToString(value));
                }
                SeriesDefinition definition = new SeriesDefinition(name, value);
                reportDefinition.addTitleValue(definition);
            }
        }
        return reportDefinition;
    }

    /**
     * ������Ǽ�һ���۲��߼����¼�
     *
     * @param listener �۲��߼����¼�
     */
    public void registerChangeListener(final UIObserverListener listener) {
        changeListeners.add(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                listener.doChange();
            }
        });
    }

    /**
     * ����Ƿ���Ҫ��Ӧ��ӵĹ۲����¼�
     *
     * @return �����Ҫ��Ӧ�۲����¼��򷵻�true�����򷵻�false
     */
    public boolean shouldResponseChangeListener() {
        return true;
    }

    private class InnerTableEditor extends UITableEditor {
        private JComponent editorComponent;

        /**
         * ���ص�ǰ�༭����ֵ
         */
        public Object getCellEditorValue() {
            if (editorComponent instanceof TinyFormulaPane) {
                return ((TinyFormulaPane) editorComponent).getUITextField().getText();
            } else if (editorComponent instanceof UITextField) {
                return ((UITextField) editorComponent).getText();
            }

            return super.getCellEditorValue();
        }

        /**
         * ���ص�ǰ�༭��..
         */
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (column == table.getModel().getColumnCount()) {
                return null;
            }
            return getEditorComponent(column, value);
        }

        private JComponent getEditorComponent(int column, Object value) {
            if (column == 0) {
                UITextField field = new UITextField();
                addListener4UITextFiled(field);

                if (value != null) {
                    field.setText(Utils.objectToString(value));
                }
                editorComponent = field;
            } else {
                TinyFormulaPane tinyPane = new TinyFormulaPane() {
                    @Override
                    public void okEvent() {
                        seriesPane.stopCellEditing();
                        seriesPane.fireTargetChanged();
                    }
                };
                tinyPane.setBackground(UIConstants.FLESH_BLUE);

                addListener4UITextFiled(tinyPane.getUITextField());

                if (value != null) {
                    tinyPane.getUITextField().setText(Utils.objectToString(value));
                }

                editorComponent = tinyPane;
            }
            return editorComponent;
        }

        private void addListener4UITextFiled(UITextField textField) {

            textField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
//					seriesPane.stopCellEditing();	//kunsnat: ��stop����Ϊ����ֱ�ӵ����ʽ�༭��ť, ������Ҫ������β��ܵ���.
                    seriesPane.fireTargetChanged();
                }
            });
        }
    }
}
