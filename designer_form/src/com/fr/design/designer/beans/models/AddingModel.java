package com.fr.design.designer.beans.models;

import java.awt.Rectangle;

import com.fr.design.mainframe.FormDesigner;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.adapters.component.CompositeComponentAdapter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.design.utils.ComponentUtils;
import com.fr.general.ComparatorUtils;

/**
 * ���״̬�µ�model
 */
public class AddingModel {

	// ��ǰҪ��ӵ����
	private XCreator creator;
	// ��¼��ǰ����λ����Ϣ
	private int current_x;
	private int current_y;
	private boolean added;

	public AddingModel(FormDesigner designer, XCreator xCreator) {
		String creatorName = getXCreatorName(designer, xCreator);
		this.creator = xCreator;
		instantiateCreator(designer, creatorName);
		// ��ʼ��ʱ�����ظ������ͼ��
		current_x = -this.creator.getWidth();
		current_y = -this.creator.getHeight();
	}

    /**
     * ��˵��
     * @param designer     �����
     * @param creatorName      �����
     */
	public void instantiateCreator(FormDesigner designer, String creatorName) {
		creator.toData().setWidgetName(creatorName);
		ComponentAdapter adapter = new CompositeComponentAdapter(designer, creator);
		adapter.initialize();
		creator.addNotify();
		creator.putClientProperty(AdapterBus.CLIENT_PROPERTIES, adapter);
	}

	public AddingModel(XCreator xCreator, int x, int y) {
		this.creator = xCreator;
		this.creator.backupCurrentSize();
		this.creator.backupParent();
		this.creator.setSize(xCreator.initEditorSize());
		current_x = x - (xCreator.getWidth() / 2);
		current_y = y - (xCreator.getHeight() / 2);
	}

    /**
     * ���ص�ǰ�����ͼ��
     */
	public void reset() {
		current_x = -this.creator.getWidth();
		current_y = -this.creator.getHeight();
	}

	public String getXCreatorName(FormDesigner designer,XCreator x){
		String def= x.createDefaultName();
        if (x.acceptType(XWParameterLayout.class)) {
            return def;
        }
		int i = 0;
		while (designer.getTarget().isNameExist(def + i)) {
			i++;
		}	
		return def+i;
	}
	
	public int getCurrentX() {
		return current_x;
	}

	public int getCurrentY() {
		return current_y;
	}


    /**
     * �ƶ����ͼ�굽����¼�������λ��
     * @param x   ����
     * @param y  ����
     */
	public void moveTo(int x, int y) {
		current_x = x - (this.creator.getWidth() / 2);
		current_y = y - (this.creator.getHeight() / 2);
	}

	public XCreator getXCreator() {
		return this.creator;
	}

    /**
     * ��ǰ����Ƿ��Ѿ���ӵ�ĳ��������
     * @return   �Ƿ���true
     */
	public boolean isCreatorAdded() {
		return added;
	}

    /**
     * ��������
     * @param designer        �����
     * @param container       ����
     * @param x   ����
     * @param y     ����
     * @return  �ɹ�����true
     */
	public boolean add2Container(FormDesigner designer, XLayoutContainer container, int x, int y) {
		Rectangle rect = ComponentUtils.getRelativeBounds(container);
		if(!ComparatorUtils.equals(container.getOuterLayout(), container.getBackupParent())){
			return added = container.getLayoutAdapter().addBean(creator,x,y);
		}
		return added = container.getLayoutAdapter().addBean(creator,
				x + designer.getArea().getHorizontalValue() - rect.x,
				y + designer.getArea().getVerticalValue() - rect.y);
	}
}
