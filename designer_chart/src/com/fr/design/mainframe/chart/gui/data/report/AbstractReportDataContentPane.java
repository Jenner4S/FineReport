package com.fr.design.mainframe.chart.gui.data.report;

import com.fr.base.Formula;
import com.fr.base.Utils;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.constants.UIConstants;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.event.UIObserverListener;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.itable.UITable;
import com.fr.design.gui.itable.UITableEditor;
import com.fr.design.layout.TableLayout;
import com.fr.stable.StableUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

/**
 * ����� ���Ա� ��Ԫ�����ݽ���.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version ����ʱ�䣺2012-12-27 ����10:31:53
 */
public abstract class AbstractReportDataContentPane extends BasicBeanPane<ChartCollection>{
	private static final double ROW = 6;
	
	protected UICorrelationPane seriesPane;
	
	protected abstract String[] columnNames();
	
	protected void initEveryPane() {
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { p, TableLayout.LEADING, f };
		double[] rowSize = {p, ROW, p, ROW, p, ROW, p};
		
		this.setLayout(new TableLayout(columnSize, rowSize));

        initSeriesPane();

		this.add(seriesPane, "0,2,2,2");
	}

    //kunsnat: ��������, ����ͼ, �ɼ�ͼ ��һ�� Ĭ��Ӧ�ò��ɱ༭.
    protected void initSeriesPane() {
        seriesPane = new UICorrelationPane(columnNames()) {
            public UITableEditor createUITableEditor() {
                return new InnerTableEditor();
            }

            protected UITable initUITable() {
                return new UITable(columnCount) {

                    public UITableEditor createTableEditor() {
                        return createUITableEditor();
                    }

                    public void tableCellEditingStopped(ChangeEvent e) {
                        stopPaneEditing(e);
                    }
                };
            }
        };
    }
	
	/**
	 * ������Box ���� �Ƿ����.
	 */
	public void checkBoxUse() {
		
	}
	
	/**
	 * ���Ƿ��� doNothing
	 */
	@Override
	public ChartCollection updateBean() {
		return null;
	}
	
	/**
	 * ����ֱ�Ӹ��� �����б�֮��Ľ���
	 */
	protected void populateList(List list) {
		seriesPane.populateBean(list);
	}
	
	/**
	 * ���ڸ��µõ����µĽ����б�ֵ
	 */
	protected List updateList() {
		return seriesPane.updateBean();
	}

	/**
	 * �������: 
	 */
	protected String title4PopupWindow() {
		return "";
	}
	
	protected HashMap<Object, Object> createNameValue(List<Object[]> list) {
		HashMap<Object, Object> values = new HashMap<Object, Object>();
		
		for(int i = 0; i < list.size(); i++) {
			Object[] tmp = list.get(i);
			values.put(tmp[0], tmp[1]);
		}
		
		return values;
	}
	
	protected Object canBeFormula(Object object) {
		if(object == null) {
			return null;
		}
		
		return StableUtils.canBeFormula(object) ? new Formula(object.toString()) : object.toString();
	}

    protected class InnerTableEditor extends UITableEditor {
		private TinyFormulaPane editorComponent;

		/**
		 * ���ص�ǰ�༭����ֵ
		 */
		public Object getCellEditorValue() {
			return editorComponent.getUITextField().getText();
		}

		/**
		 * ���ص�ǰ�༭��..
		 */
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			if(column == table.getModel().getColumnCount()) {
				return null;
			}
			
			seriesPane.stopCellEditing();
			
			TinyFormulaPane editor = getEditorComponent();
			editor.getUITextField().setText(Utils.objectToString(value));
			return editor;
		}

		private TinyFormulaPane getEditorComponent() {
			editorComponent = null;
			if (editorComponent == null) {
				editorComponent = new TinyFormulaPane() {
					@Override
					public void okEvent() {
						seriesPane.stopCellEditing();
						seriesPane.fireTargetChanged();
					}
				};
				editorComponent.setBackground(UIConstants.FLESH_BLUE);
				
				editorComponent.getUITextField().registerChangeListener(new UIObserverListener() {
					@Override
					public void doChange() {
						seriesPane.fireTargetChanged();
					}
				});
			}
			return editorComponent;
		}
	}

}
