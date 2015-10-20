package com.fr.design.designer.beans;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.RootPaneContainer;

import com.fr.design.mainframe.FormDesigner;
import com.fr.design.designer.beans.adapters.component.CompositeComponentAdapter;
import com.fr.design.designer.beans.painters.NullLayoutPainter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.utils.ComponentUtils;

/**
 * ���������࣬Ϊ�������������������ֺͲ�����������
 */
public class AdapterBus {

	public static final String CLIENT_PROPERTIES = "component.adapter";

	public static JComponent getJComponent(Component component) {
		JComponent jcomponent;
		if (component instanceof JComponent) {
			jcomponent = (JComponent) component;
		} else if (component instanceof RootPaneContainer) {
			jcomponent = (JComponent) ((RootPaneContainer) component).getContentPane();
		} else {
			return null;
		}
		return jcomponent;
	}

	/**
	 * ��ȡ���������componentClass��Ӧ������������������ʼӳ�����û�и���������
	 * ����������丸���Ӧ����������ֱ�����ҵ�Component��Ϊֹ���������û�в��ҵ���
	 * ��ʹ��ȱʡ�������������DefaultComponentAdapter
	 * 
	 * @return �����������Ӧ���������������
	 */
	public static ComponentAdapter getComponentAdapter(FormDesigner designer, JComponent creator) {
		JComponent jcomponent = getJComponent(creator);
		ComponentAdapter adapter = (ComponentAdapter) jcomponent.getClientProperty("component.adapter");
		if (adapter == null) {
			adapter = new CompositeComponentAdapter(designer, creator);
			jcomponent.putClientProperty(CLIENT_PROPERTIES, adapter);
		}
		return adapter;
	}

	public static XCreator getFirstInvisibleParent(XCreator comp) {
		XCreator parent = comp;
	
		while ((parent != null) && parent.isVisible()) {
			parent = XCreatorUtils.getParentXLayoutContainer(parent);
		}
	
		return parent;
	}

	public static LayoutAdapter searchLayoutAdapter(FormDesigner designer, XCreator comp) {
		if (ComponentUtils.isRootComponent(comp)) {
			return null;
		}
		return XCreatorUtils.getParentXLayoutContainer(comp).getLayoutAdapter();
	}

	public static HoverPainter getContainerPainter(FormDesigner designer, XLayoutContainer container) {
		// ���������������
		LayoutAdapter containerAdapter = container.getLayoutAdapter();
		HoverPainter painter = containerAdapter.getPainter();
		if (painter != null) {
			return painter;
		}
		return new NullLayoutPainter(container);
	}
}
