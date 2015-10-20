package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.base.Utils;
import com.fr.chart.web.ChartHyperRelateFloatLink;
import com.fr.design.DesignModelAdapter;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.stable.ParameterProvider;
import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2011-12-28 ����03:02:43
 * ��˵��: ͼ����  ��������Ԫ��
 */
public class ChartHyperRelateFloatLinkPane extends BasicBeanPane<ChartHyperRelateFloatLink> {
	private static final long serialVersionUID = -3308412003405587689L;

    private UITextField itemNameTextField;

	private UIComboBox floatNameBox;
	private ReportletParameterViewPane parameterViewPane;
	
	public ChartHyperRelateFloatLinkPane() {
		this.initComponent();
	}
	
	private void initComponent() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		
		JPanel centerPane = FRGUIPaneFactory.createBorderLayout_L_Pane();

        if(needRenamePane()){
            itemNameTextField = new UITextField();
            this.add(GUICoreUtils.createNamedPane(itemNameTextField, Inter.getLocText("Name") + ":"), BorderLayout.NORTH);
        }

		this.add(centerPane, BorderLayout.CENTER);
		floatNameBox = new UIComboBox(getFloatNames());
		floatNameBox.setPreferredSize(new Dimension(90, 20));
		
		JPanel pane = FRGUIPaneFactory.createBoxFlowInnerContainer_S_Pane();
		pane.add(new UILabel(Inter.getLocText("M_Insert-Float") + ":"));
		pane.add(floatNameBox);
		
		Border boder = null;
		Font font = null;	
		TitledBorder border = new TitledBorder(boder, Inter.getLocText(new String[]{"Related", "M_Insert-Float"}), 4, 2, font, new Color(1, 159, 222));
		// Բ�ǲ���
		centerPane.setBorder(border);
		
		centerPane.add(pane, BorderLayout.NORTH);
		
		parameterViewPane = new ReportletParameterViewPane(getChartParaType());
		parameterViewPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("Parameters")));
		parameterViewPane.setPreferredSize(new Dimension(500, 200));
		this.add(parameterViewPane, BorderLayout.SOUTH);
	}
	
	protected int getChartParaType() {
		return ParameterTableModel.CHART_NORMAL_USE;
	}

    /**
     * �Ƿ���Ҫ�����������Ŀռ�
     * @return Ĭ����Ҫ����
     */
    protected boolean needRenamePane(){
        return true;
    }

	private String[] getFloatNames() {
		DesignModelAdapter adapter = DesignModelAdapter.getCurrentModelAdapter();
		if(adapter != null ) {
			return adapter.getFloatNames();
		}
		return new String[0];
	}
	
	@Override
	public void populateBean(ChartHyperRelateFloatLink ob) {
		if(ob == null) {
			return ;
		}

        if(itemNameTextField != null){
            itemNameTextField.setText(ob.getItemName());
        }

		floatNameBox.setSelectedItem(ob.getRelateCCName());
		
		List parameterList = this.parameterViewPane.update();
		parameterList.clear();

		ParameterProvider[] parameters = ob.getParameters();
		parameterViewPane.populate(parameters);
	}

	@Override
	public ChartHyperRelateFloatLink updateBean() {
		ChartHyperRelateFloatLink chartLink = new ChartHyperRelateFloatLink();
		updateBean(chartLink);
        if(itemNameTextField != null){
            chartLink.setItemName(this.itemNameTextField.getText());
        }
		return chartLink;
	}
	
	public void updateBean(ChartHyperRelateFloatLink chartLink) {
		
		if(floatNameBox.getSelectedItem() != null) {
			chartLink.setRelateCCName(Utils.objectToString(floatNameBox.getSelectedItem()));
		}
		
		List parameterList = this.parameterViewPane.update();
		if (parameterList != null && !parameterList.isEmpty()) {
			ParameterProvider[] parameters = new ParameterProvider[parameterList.size()];
			parameterList.toArray(parameters);

			chartLink.setParameters(parameters);
		} else {
			chartLink.setParameters(null);
		}
        if(itemNameTextField != null){
            chartLink.setItemName(this.itemNameTextField.getText());
        }
	}

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText(new String[]{"Related", "M_Insert-Float"});
	}

    public static class CHART_NO_RENAME extends ChartHyperRelateFloatLinkPane{
        protected boolean needRenamePane(){
            return false;
        }
    }

	public static final class CHART_MAP extends ChartHyperRelateFloatLinkPane {
		protected int getChartParaType() {
			return ParameterTableModel.CHART_MAP_USE;
		}
	}
	
	public static final class CHART_GIS extends ChartHyperRelateFloatLinkPane {
		protected int getChartParaType() {
			return ParameterTableModel.CHART_GIS_USE;
		}
	}
	
	public static final class CHART_PIE extends ChartHyperRelateFloatLinkPane {
		protected int getChartParaType() {
			return ParameterTableModel.CHART_PIE_USE;
		}
	}

    public static class CHART_XY extends ChartHyperRelateFloatLinkPane {
        protected int getChartParaType() {
            return ParameterTableModel.CHART__XY_USE;
        }
    }

    public static class CHART_BUBBLE extends ChartHyperRelateFloatLinkPane {
        protected int getChartParaType() {
            return ParameterTableModel.CHART_BUBBLE_USE;
        }
    }

    public static class CHART_STOCK extends  ChartHyperRelateFloatLinkPane {
        protected int getChartParaType() {
            return ParameterTableModel.CHART_STOCK_USE;
        }
    }

    public static class CHART_GANTT extends  ChartHyperRelateFloatLinkPane {
        protected int getChartParaType() {
            return ParameterTableModel.CHART_GANTT_USE;
        }
    }

    public static class CHART_METER extends  ChartHyperRelateFloatLinkPane {
        protected int getChartParaType() {
            return ParameterTableModel.CHART_METER_USE;
        }
    }
}
