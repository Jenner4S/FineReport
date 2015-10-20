package com.fr.design.mainframe.chart.gui.style;

import com.fr.chart.chartglyph.GeneralInfo;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.style.GradientPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.backgroundpane.BackgroundSettingPane;
import com.fr.design.mainframe.backgroundpane.ColorBackgroundPane;
import com.fr.design.mainframe.backgroundpane.ImageBackgroundPane;
import com.fr.design.mainframe.backgroundpane.NullBackgroundPane;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Background;
import com.fr.general.Inter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

/**
 * ͼ��  ���Ա�.�������� ����.(���� ��, ��ɫ, ͼƬ, ����)
* @author kunsnat E-mail:kunsnat@gmail.com
* @version ����ʱ�䣺2013-1-21 ����03:55:32
 */
public class ChartBackgroundPane extends BasicPane{
	private static final long serialVersionUID = 6955952013135176051L;
	private static final double ALPHA_V = 100.0;
	protected static final int CHART_GRADIENT_WIDTH = 150;
	protected List<BackgroundSettingPane> paneList;
	
	private UIComboBox typeComboBox;
	private UINumberDragPane transparent;
	
	public ChartBackgroundPane() {
		typeComboBox = new UIComboBox();
		final CardLayout cardlayout = new CardLayout();
		paneList = new ArrayList<BackgroundSettingPane>();
		
		initList();
		
		final JPanel centerPane = new JPanel(cardlayout) {

			@Override
			public Dimension getPreferredSize() {// AUGUST:ʹ�õ�ǰ���ĵĸ߶�
				int index = typeComboBox.getSelectedIndex();
				return new Dimension(super.getPreferredSize().width, paneList.get(index).getPreferredSize().height);
			}
		};
		for (int i = 0; i < paneList.size(); i++) {
			BackgroundSettingPane pane = paneList.get(i);
			typeComboBox.addItem(pane.title4PopupWindow());
			centerPane.add(pane, pane.title4PopupWindow());
		}
		
		typeComboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				cardlayout.show(centerPane, (String)typeComboBox.getSelectedItem());
				fireStateChanged();
			}
		});
		
		transparent = new UINumberDragPane(0, 100);


		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		
		double[] columnSize = {p, f};
		double[] rowSize = { p,p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{typeComboBox, null} ,
                new Component[]{centerPane, null},
                new Component[]{new UILabel(Inter.getLocText("Chart_Alpha_Int")), null},
                new Component[]{null, transparent}
        };
        JPanel panel = TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"Background"}, components,rowSize,columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);
        this.add(new JSeparator(), BorderLayout.SOUTH);
	}
	
	protected void initList() {
		paneList.add(new NullBackgroundPane());
		paneList.add(new ColorBackgroundPane());
		paneList.add(new ImageBackgroundPane());
		paneList.add(new GradientPane(CHART_GRADIENT_WIDTH));
	}
	
	/**
     */
	private void fireStateChanged() {
		Object[] listeners = listenerList.getListenerList();
		ChangeEvent e = null;

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				if (e == null) {
					e = new ChangeEvent(this);
				}
				((ChangeListener)listeners[i + 1]).stateChanged(e);
			}
		}
	}

    /**
     * ���ر���
     * @return ����
     */
	public String title4PopupWindow() {
		return "";
	}
	
	public void populate(GeneralInfo attr) {
		if(attr == null) {
			return;
		}
		Background background = attr.getBackground();
		double alpha = attr.getAlpha() * ALPHA_V;
		transparent.populateBean(alpha);
		for (int i = 0; i < paneList.size(); i++) {
			BackgroundSettingPane pane = paneList.get(i);
			if (pane.accept(background)) {
				pane.populateBean(background);
				typeComboBox.setSelectedIndex(i);
				return;
			}
		}
	}
	
	public void update(GeneralInfo attr) {
		if (attr == null) {
			attr = new GeneralInfo();
		}
		attr.setBackground(paneList.get(typeComboBox.getSelectedIndex()).updateBean());
		attr.setAlpha((float) (transparent.updateBean() / ALPHA_V));
	}
}
