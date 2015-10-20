package com.fr.design.mainframe.chart.gui.other;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.fr.design.chart.ChartControlPane;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.general.Inter;

public class ChartSwitchPane extends AbstractAttrNoScrollPane{

	private UIButton changeButton;
	
	private ChartCollection editingChartCollection;
	
	private ChartEditPane currentChartEditPane;
	
	public ChartSwitchPane() {
		
	}
	
	@Override
	protected JPanel createContentPane() {
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		
		changeButton = new UIButton(Inter.getLocText("Switch"));
		
		pane.add(changeButton, BorderLayout.NORTH);
		
		changeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final ChartControlPane chartTypeManager = new ChartControlPane();
				chartTypeManager.populate(editingChartCollection);
				
				BasicDialog dlg = chartTypeManager.showWindow4ChartType(SwingUtilities.getWindowAncestor(new JPanel()), new DialogActionAdapter() {
					public void doOk() {
						chartTypeManager.update(editingChartCollection);//kunsnat: ȷ��ˢ��"chartSelectIndex"
						
						if(currentChartEditPane != null) {
							currentChartEditPane.populate(editingChartCollection);// ѡ����Plot֮�� ˢ�¶�Ӧ����, ���糬�����ӵ�, Ȼ�����update.
							currentChartEditPane.GoToPane(PaneTitleConstants.CHART_TYPE_TITLE);
							currentChartEditPane.GoToPane(PaneTitleConstants.CHART_OTHER_TITLE, PaneTitleConstants.CHART_OTHER_TITLE_CHANGE);
							currentChartEditPane.fire();
						}
					}
				});
				
				dlg.setVisible(true);
			}
		});
		
		return pane;
	}
	
	/**
	 * ע�� �л��¼��ĸı� �ͳ�����ͬ.
	 * @param listener
	 */
	public void registerChartEditPane(ChartEditPane currentChartEditPane) {
		this.currentChartEditPane = currentChartEditPane;
	}
	
	public void populateBean(ChartCollection c) {
		this.editingChartCollection = c;
	}
	
	public void updateBean(ChartCollection c) {
		
	}
	
	/**
	 * �������
	 * @param ���ر���
	 */
	public String title4PopupWindow() {
		return Inter.getLocText("Chart-Switch");
	}

	@Override
	public String getIconPath() {
		return null;
	}
}
