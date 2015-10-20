package com.fr.design.designer.beans.adapters.layout;

import java.awt.LayoutManager;

import com.fr.general.ComparatorUtils;
import com.fr.design.beans.GroupModel;
import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.painters.NullPainter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.design.utils.ComponentUtils;
import com.fr.design.utils.gui.LayoutUtils;

public abstract class AbstractLayoutAdapter implements LayoutAdapter {

    protected XLayoutContainer container;
    protected LayoutManager layout;

    public AbstractLayoutAdapter(XLayoutContainer container) {
        this.container = container;
        this.layout = container.getLayout();
    }
    
    /**
     * �Ƿ�ʹ�ÿؼ����ݴ�С
     * @param xCreator �ؼ�
     * @return ����������ͬ����֧�ֱ��ݵĻ�����true
     */
    public boolean whetherUseBackupSize(XCreator xCreator) {
    	Class clazz = container.getClass();
    	Class bkClazz = null;
    	if(xCreator.getBackupParent() != null) {
    		bkClazz = xCreator.getBackupParent().getClass();
    	}
    	return ComparatorUtils.equals(bkClazz, clazz)
    		&& supportBackupSize();
    }
    
    /**
     * �Ƿ�֧���ñ��ݴ�С
     * @return ��
     */
    public boolean supportBackupSize() {
    	return false;
    }

    /**
     * �еĿؼ�����ק������С����Ҫ���������������¼����µ�ǰ�ĳߴ��Ƿ���ʣ���������ʣ�����Ҫ����fixһ��
     * @param creator ���
     */
    public void fix(XCreator creator) {
    }

    /**
     * ��ʾparent�������child�����CardLayout����ʾĳ������ʾ������������
     * @param child ���
     */
    @Override
    public void showComponent(XCreator child) {
    	child.setVisible(true);
    }
    
    /**
     * �����ComponentAdapter��������ʱ��������ֲ��ֹ�������Ϊ�գ���̶����øò��ֹ�������
     * addComp�������������ľ�����ӡ��ڸ÷����ڣ����ֹ����������ṩ����Ĺ��ܡ�
     * @param creator ����ӵ������
     * @param x ��ӵ�λ��x����λ���������container��
     * @param y ��ӵ�λ��y����λ���������container��
     * @return �Ƿ���ӳɹ����ɹ�����true������false
     */
    @Override
	public boolean addBean(XCreator creator, int x, int y) {
		if (!accept(creator, x, y)) {
			return false;
		}
		addComp(creator, x, y);
		((XWidgetCreator) creator).recalculateChildrenSize();
		return true;
	}
    
    /**
     * ɾ�����
     * @param creator ���
     * @param initWidth ���֮ǰ���
     * @param initHeight ���֮ǰ�߶�
     */
    public void removeBean(XCreator creator, int creatorWidth, int creatorHeight) {
    	delete(creator, creatorWidth, creatorHeight);
    }
    
    protected void delete(XCreator creator, int creatorWidth, int creatorHeight) {
    }
    
    protected abstract void addComp(XCreator creator, int x, int y);

    /**
     * ������һ�����
     * @param dragged ���
     */
    @Override
    public void addNextComponent(XCreator dragged) {
        container.add(dragged);
        LayoutUtils.layoutRootContainer(container);
    }

    /**
     * Ŀ��ؼ�λ�ò������
     * @param target Ŀ��
     * @param added �������
     */
    @Override
    public void addBefore(XCreator target, XCreator added) {
        int index = ComponentUtils.indexOfComponent(container, target);

        if (index == -1) {
            container.add(added, 0);
        } else {
            container.add(added, index);
        }

        LayoutUtils.layoutRootContainer(container);
    }

    /**
     * ����Ŀ���������
     * @param target Ŀ��
     * @param added �������
     */
    @Override
    public void addAfter(XCreator target, XCreator added) {
        int index = ComponentUtils.indexOfComponent(container, target);

        if (index == -1) {
            container.add(added);
        } else {
            index++;

            if (index >= container.getComponentCount()) {
                container.add(added);
            } else {
                container.add(added, index);
            }
        }

        LayoutUtils.layoutRootContainer(container);
    }
    
    @Override
    public HoverPainter getPainter() {
        return new NullPainter(container);
    }
    
    /**
     * �Ƿ��ܽ��ո�������
     * @return ���򷵻�true
     */
    @Override
    public boolean canAcceptMoreComponent() {
        return true;
    }

    @Override
    public ConstraintsGroupModel getLayoutConstraints(XCreator creator) {
        return null;
    }

    @Override
    public GroupModel getLayoutProperties() {
        return null;
    }
}
