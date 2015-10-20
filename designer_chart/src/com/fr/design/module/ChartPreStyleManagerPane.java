package com.fr.design.module;

import com.fr.base.ChartPreStyleManagerProvider;
import com.fr.base.ChartPreStyleServerManager;
import com.fr.base.Utils;
import com.fr.chart.base.ChartPreStyle;
import com.fr.design.gui.controlpane.JControlPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.stable.Nameable;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * ͼ��Ԥ������� ����, �ڹ�����-������������.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2013-8-21 ����02:33:48
 */
public class ChartPreStyleManagerPane extends JControlPane {

	@Override
    /**
     * ���������ֵ�creator
     * @return �����ֵ�creator����
     */
	public NameableCreator[] createNameableCreators() {
		return new NameableCreator[]{
				new NameObjectCreator(Inter.getLocText("FR-Designer_PreStyle"),
						ChartPreStyle.class, ChartPreStylePane.class)
		};
	}

	@Override
	protected String title4PopupWindow() {
        return Inter.getLocText("FR-Designer_Chart-PreStyle");
	}
	
	public void populateBean() {
		ChartPreStyleManagerProvider manager = ChartPreStyleServerManager.getProviderInstance();
		
		ArrayList list = new ArrayList();
		
		Iterator keys = manager.names();
		while(keys.hasNext()) {
			Object key = keys.next();
			ChartPreStyle value = (ChartPreStyle)manager.getPreStyle(key);
			
			list.add(new NameObject(Utils.objectToString(key), value));
		}
		
		Nameable[] values = (Nameable[])list.toArray(new Nameable[list.size()]);
		populate(values);
		
		if(manager.containsName(manager.getCurrentStyle())) {
			this.setSelectedName(manager.getCurrentStyle());
		}
	}
	
	public void updateBean() {
		ChartPreStyleManagerProvider manager = ChartPreStyleServerManager.getProviderInstance();
		manager.clearPreStyles();
		
		Nameable[] values = this.update();
		
		manager.setCurrentStyle(getSelectedName());
		
		for(int i = 0; i < values.length; i++) {
			Nameable value = values[i];
			manager.putPreStyle(value.getName(), ((NameObject)value).getObject());
		}
		
		manager.writerPreChartStyle();
		
		// ֪ͨ��������ˢ��. 
		DesignerFrame frame = DesignerContext.getDesignerFrame();
		if(frame != null) {
			frame.repaint();
		}
	}

}
