package com.fr.design.mainframe.chart.gui.style;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import com.fr.chart.chartattr.Axis;
import com.fr.chart.chartattr.CategoryAxis;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;

/**
 * ������ ֵ���ͽ���(�ı������� ���� ����������).
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2013-1-4 ����10:15:35
 */
public class ChartAxisValueTypePane extends BasicPane {
	
	private UIComboBoxPane boxPane;
	
	private DateAxisValuePane dateAxisPane;
	private TextAxisValueTypePane textAxisPane;
	
	public ChartAxisValueTypePane() {
		this.setLayout(new BorderLayout());
		
		boxPane = new UIComboBoxPane<Axis>() {

			protected List<FurtherBasicBeanPane<? extends Axis>> initPaneList() {
				
				List list = new ArrayList<FurtherBasicBeanPane>();
				
				list.add(dateAxisPane = new DateAxisValuePane());
				list.add(textAxisPane = new TextAxisValueTypePane());
				return list;
			}

			protected String title4PopupWindow() {
				return "";
			}
		};
		
		this.add(boxPane, BorderLayout.NORTH);
	}
	
	/**
	 * �������.
	 */
	protected String title4PopupWindow() {
		return Inter.getLocText("AxisValue");
	}
	
	/**
	 * �ж�����, ���½�������
	 */
	public void populateBean(CategoryAxis axis) {
		if(axis != null && axis.isDate()) {
			boxPane.setSelectedIndex(0);
			dateAxisPane.populateBean(axis);
		} else {
			boxPane.setSelectedIndex(1);
		}
	}
	
	/**
	 * �����������.
	 */
	public void updateBean(CategoryAxis axis) {
		if(boxPane.getSelectedIndex() == 0) {
			dateAxisPane.updateBean(axis);
			axis.setDate(true);
		} else {
			axis.setDate(false);
			textAxisPane.updateBean(axis);
		}
	}
	
	//����ͼ��ʱ�������᲻�����ı��������������
	public void removeTextAxisPane(){
		if(this.boxPane.getUIComboBox().getItemCount() > 1){ //��ֹ��ε���
			this.boxPane.getUIComboBox().removeItemAt(1);
			this.boxPane.getCards().remove(1);
		}
	}
	
	/**
	 * �ı�������.
	* @author kunsnat E-mail:kunsnat@gmail.com
	* @version ����ʱ�䣺2013-1-4 ����10:55:05
	 */
	private class TextAxisValueTypePane extends FurtherBasicBeanPane<CategoryAxis> {
		
		/**
		 *  �жϽ������.
		 */
		public boolean accept(Object ob) {
			return ob instanceof CategoryAxis;
		}

		/**
		 * ����
		 */
		public void reset() {
			
		}

		/**
		 * �������
		 */
		public String title4PopupWindow() {
			return Inter.getLocText("Chart_Text_Axis");
		}

		/**
		 * ���½��� donothing
		 */
		public void populateBean(CategoryAxis ob) {
		}

		/**
		 * �����������. donothing
		 */
		public CategoryAxis updateBean() {
			return null;
		}
		
		/**
		 * ������� donothing
		 */
		public void updateBean(CategoryAxis axis) {
			
		}
	}
}
