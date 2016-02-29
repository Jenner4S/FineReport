package com.fr.solution.plugin.chart.echarts.ui.map;

import com.fr.base.Utils;
import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartdata.MoreNameCDDefinition;
import com.fr.chart.chartdata.OneValueCDDefinition;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.constants.UIConstants;
import com.fr.design.event.UIObserverListener;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itable.UITable;
import com.fr.design.gui.itable.UITableEditor;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ChartDataFilterPane;
import com.fr.design.mainframe.chart.gui.data.table.SeriesNameUseFieldNamePane;
import com.fr.design.mainframe.chart.gui.data.table.SeriesNameUseFieldValuePane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.solution.plugin.chart.echarts.base.MapChartNameRef;
import com.fr.solution.plugin.chart.echarts.base.NewChart;

import javax.swing.*;
import javax.swing.event.ChangeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 属性表: 柱形, 饼图 数据集界面, "系列名使用"界面.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-12-26 下午03:17:08
 */
public class SeriesTypeUseComboxPane extends UIComboBoxPane<ChartCollection> {

    private SeriesNameUseFieldValuePane nameFieldValuePane;
    private SeriesNameUseFieldNamePane nameFieldNamePane;

    private UICorrelationPane seriesPane;

    private ChartDataPane parent;
    private Plot initplot;
    private boolean isNeedSummary = true;

    public SeriesTypeUseComboxPane(ChartDataPane parent, Plot initplot) {
        this.initplot = initplot;
        this.parent = parent;
        cards = initPaneList();
        this.isNeedSummary = true;
        initComponents();
    }

    protected void initLayout() {
        this.setLayout(new BorderLayout(4, LayoutConstants.VGAP_MEDIUM));
        JPanel northPane = new JPanel(new BorderLayout(4, 0));
        UILabel label1 = new UILabel(Inter.getLocText("ChartF-Series_Name_From") + ":", SwingConstants.RIGHT);
        label1.setPreferredSize(new Dimension(75, 20));
        northPane.add(GUICoreUtils.createBorderLayoutPane(new Component[]{jcb, null, null, label1, null}));
        northPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 1));
        this.add(northPane, BorderLayout.NORTH);
        this.add(cardPane, BorderLayout.CENTER);
        this.add(seriesPane = new UICorrelationPane(new String[]{"字段名","区域名"}) {
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
        },BorderLayout.SOUTH);
    }

    /**
     * 检查box 是否使用, hasUse, 表示上层已经使用, 否则, 则此界面都可使用
     * @param hasUse 是否使用
     */
    public void checkUseBox(boolean hasUse) {
        jcb.setEnabled(hasUse);
        nameFieldValuePane.checkUse(hasUse);
//        dataScreeningPane.checkBoxUse();
    }

    /**
     * 切换 变更数据集时, 刷新Box选中项目
     * @param list 列表
     */
    public void refreshBoxListWithSelectTableData(List list) {
        nameFieldValuePane.refreshBoxListWithSelectTableData(list);
        nameFieldNamePane.refreshBoxListWithSelectTableData(list);
    }

    /**
     * 清空所有的box设置
     */
    public void clearAllBoxList(){
        nameFieldValuePane.clearAllBoxList();
        nameFieldNamePane.clearAllBoxList();
    }

    /**
     * 界面标题
     * @return 界面标题
     */
    protected String title4PopupWindow() {
        return Inter.getLocText("ChartF-Series_Name_From");
    }

    @Override
    protected List<FurtherBasicBeanPane<? extends ChartCollection>> initPaneList() {
        nameFieldValuePane = new SeriesNameUseFieldValuePane();
        nameFieldNamePane = new SeriesNameUseFieldNamePane();
        List<FurtherBasicBeanPane<? extends ChartCollection>> paneList = new ArrayList<FurtherBasicBeanPane<? extends ChartCollection>>();
        paneList.add(nameFieldValuePane);
        paneList.add(nameFieldNamePane);
        return paneList;
    }

    public void populateBean(ChartCollection ob, boolean isNeedSummary) {
        this.isNeedSummary = isNeedSummary;
        TopDefinitionProvider definition = ob.getSelectedChart().getFilterDefinition();
        if (definition instanceof OneValueCDDefinition) {
            this.setSelectedIndex(0);
            nameFieldValuePane.populateBean(ob, isNeedSummary);
        } else if (definition instanceof MoreNameCDDefinition) {
            this.setSelectedIndex(1);
            nameFieldNamePane.populateBean(ob, isNeedSummary);
        }
        MapChartNameRef name_ref =  ((NewChart) ob.getSelectedChart()).getName_ref();
    	List<Object[]> listobj = new ArrayList<Object[]>();
    	System.out.println("GGGGGGGG:::"+name_ref.getListName());
    	if(null!=name_ref){
    		JSONObject name_lis = name_ref.getListName();
    		 Iterator iterator = name_lis.keys();
    		for(;iterator.hasNext();){
    			String key = (String) iterator.next();
//    			if(key!=null)
    			Object[] objs=null;
    			try {
    				objs=new String[]{key,name_lis.getString(key)};
    				} 
    			catch (JSONException e){// TODO Auto-generated catch block
				e.printStackTrace();
				}
    			listobj.add(objs);
    		}
    		seriesPane.populateBean(listobj);
    		seriesPane.doLayout();
    	}
        
        
//        dataScreeningPane.populateBean(ob, isNeedSummary);
    }

    /**
     * 重新布局整个面板
     * @param isNeedSummary 是否需要汇总
     */
    public void relayoutPane(boolean isNeedSummary) {
        this.isNeedSummary = isNeedSummary;
        if (jcb.getSelectedIndex() == 0) {
            nameFieldValuePane.relayoutPane(this.isNeedSummary);
        } else {
            nameFieldNamePane.relayoutPane(this.isNeedSummary);
        }
//        dataScreeningPane.relayoutPane(this.isNeedSummary);
    }


    @Override
    protected void comboBoxItemStateChanged() {
        if (jcb.getSelectedIndex() == 0) {
            nameFieldValuePane.relayoutPane(this.isNeedSummary);
        } else {
            nameFieldNamePane.relayoutPane(this.isNeedSummary);
        }
    }

    public void populateBean(ChartCollection ob) {
        this.populateBean(ob, true);
    }

    /**
     * 保存界面属性到Ob-ChartCollection
     */
    public void updateBean(ChartCollection ob) {
        if (this.getSelectedIndex() == 0) {
            nameFieldValuePane.updateBean(ob);
        } else {
            nameFieldNamePane.updateBean(ob);
        }
        
        MapChartNameRef name_ref =  ((NewChart) ob.getSelectedChart()).getName_ref();
		List<Object[]> listobj = seriesPane.updateBean();
		Iterator<Object[]> iterator = listobj.iterator();
		JSONObject json = new JSONObject();
		for(;iterator.hasNext();){
			Object[] objs  = iterator.next();
			if(objs[0]!=null&&objs[1]!=null)
			try {
				
				json.put((String )objs[0], (String )objs[1]);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		name_ref.setListName(json);
		System.out.println("updatabean：：：："+json);

//        dataScreeningPane.updateBean(ob);
    }
    
    

    protected class InnerTableEditor extends UITableEditor {
		private TinyFormulaPane editorComponent;

		/**
		 * 返回当前编辑器的值
		 */
		public Object getCellEditorValue() {
			return editorComponent.getUITextField().getText();
		}

		/**
		 * 返回当前编辑器..
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
