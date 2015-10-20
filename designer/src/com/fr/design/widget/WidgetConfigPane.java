package com.fr.design.widget;

import com.fr.design.gui.controlpane.JControlPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.core.WidgetConstants;
import com.fr.form.ui.UserDefinedWidgetConfig;
import com.fr.form.ui.WidgetConfig;
import com.fr.form.ui.WidgetManagerProvider;
import com.fr.general.NameObject;
import com.fr.stable.Nameable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Widget�������
 */
public class WidgetConfigPane extends JControlPane {

    /**
     * �����û��Զ���widget���
     *
     * @return ������
     */
	public NameableCreator[] createNameableCreators() {
		NameableCreator defaultWidget = new NameObjectCreator(WidgetConstants.USER_DEFINED_WIDGETCONFIG,
				"/com/fr/design/images/data/user_widget.png", UserDefinedWidgetConfig.class,
				UserDefinedWidgetConfigPane.class);
		return new NameableCreator[] { defaultWidget };
	}
	
	@Override
	protected String title4PopupWindow() {
		return "config";
	}
	
	public void populate(WidgetManagerProvider widgetManager){
		Iterator<String> nameIt = widgetManager.getWidgetConfigNameIterator();
		List<NameObject> nameObjectList = new ArrayList<NameObject>();
		while (nameIt.hasNext()) {
			String name = nameIt.next();
			nameObjectList.add(new NameObject(name, widgetManager.getWidgetConfig(name)));
		}
		this.populate(nameObjectList.toArray(new NameObject[nameObjectList.size()]));	
	}
	
	public void update(WidgetManagerProvider widgetManager){
		Nameable[] res = this.update();
		NameObject[] res_array = new NameObject[res.length];
		java.util.Arrays.asList(res).toArray(res_array);

		widgetManager.clearAllWidgetConfig();

		for (int i = 0; i < res_array.length; i++) {
			NameObject nameObject = res_array[i];
			widgetManager.putWidgetConfig(nameObject.getName(),(WidgetConfig) nameObject.getObject());
		}
	}
}
