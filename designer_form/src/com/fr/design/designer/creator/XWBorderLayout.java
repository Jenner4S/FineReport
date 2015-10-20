/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ContainerEvent;
import java.beans.IntrospectionException;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRBorderLayoutAdapter;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.form.layout.FRBorderLayout;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;

/**
 * @author richer
 * @since 6.5.3
 */
public class XWBorderLayout extends XLayoutContainer {
    private int Num = 5;
    public XWBorderLayout(){
        super(new WBorderLayout(),new Dimension(WBorderLayout.DEFAULT_WIDTH, WBorderLayout.DEFAULT_HEIGHT));
    }
	
    public XWBorderLayout(WBorderLayout widget, Dimension initSize) {
        super(widget, initSize);
    }

    @Override
    protected String getIconName() {
        return "layout_border.png";
    }

    /**
     * Ĭ������
     * @return      ����
     */
	public String createDefaultName() {
    	return "border";
    }

    /**
     * ת������Ӧ WBorderLayout
     * @return   ��Ӧ WBorderLayout
     */
    public WBorderLayout toData() {
        return (WBorderLayout) data;
    }

    @Override
	protected void initLayoutManager() {
        this.setLayout(new FRBorderLayout(toData().getHgap(), toData().getVgap()));
    }
    /**
     *  ��ʼ��С
     * @return   ��ʼ��С
     */
    public Dimension initEditorSize() {
        return new Dimension(WBorderLayout.DEFAULT_WIDTH, WBorderLayout.DEFAULT_HEIGHT);
    }

    /**
     *  �õ�������
     * @return ������
     * @throws java.beans.IntrospectionException    �״�
     */
    public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
        return  new CRPropertyDescriptor[] {
                new CRPropertyDescriptor("widgetName", this.data.getClass()).setI18NName(Inter
                        .getLocText("Form-Widget_Name"))
        };
    }

    /**
     * ��WLayoutת��ΪXLayoutContainer
     */
    public void convert() {
        isRefreshing = true;
        WBorderLayout wb = this.toData();
        this.removeAll();
        String[] arrs = {WBorderLayout.NORTH, WBorderLayout.SOUTH, WBorderLayout.EAST, WBorderLayout.WEST, WBorderLayout.CENTER};
        for (int i = 0; i < arrs.length; i++) {
            Widget wgt = wb.getLayoutWidget(arrs[i]);
            if (wgt != null) {
                XWidgetCreator comp = (XWidgetCreator) XCreatorUtils.createXCreator(wgt, calculatePreferredSize(wgt));
                this.add(comp, arrs[i]);
                comp.setBackupParent(this);
            }
        }
        isRefreshing = false;
    }
    

    /**
     * ��ƽ�������������ʱ��Ҫ֪ͨWLayout��������paint
     * @param e    �¼�
     */
    public void componentAdded(ContainerEvent e) {
        if (isRefreshing) {
            return;
        }
        XWidgetCreator creator = (XWidgetCreator) e.getChild();
        BorderLayout b = (BorderLayout) getLayout();
        Object constraints = b.getConstraints(creator);
        WBorderLayout wb = this.toData();
        Widget w = creator.toData();
        add(wb, w, constraints);
        doResizePreferredSize(creator, calculatePreferredSize(w));
    }

    @Override
    protected Dimension calculatePreferredSize(Widget wgt) {
        WBorderLayout wlayout = this.toData();
        Object constraints = wlayout.getConstraints(wgt);
        Dimension d = new Dimension();
        if (ComparatorUtils.equals(WBorderLayout.NORTH,constraints)) {
            d.height = wlayout.getNorthSize();
        } else if (ComparatorUtils.equals(WBorderLayout.SOUTH,constraints)) {
            d.height = wlayout.getSouthSize();
        } else if (ComparatorUtils.equals(WBorderLayout.EAST,constraints)) {
            d.width = wlayout.getEastSize();
        } else if (ComparatorUtils.equals(WBorderLayout.WEST,constraints)) {
            d.width = wlayout.getWestSize();
        }
        return d;
    }

    private void doResizePreferredSize(XWidgetCreator comp, Dimension d) {
        comp.setPreferredSize(d);
    }

    /**
     * ����ؼ�
     * @param layout      ����
     * @param wgt   �ؼ�
     * @param constraints    ��λ
     */
    public static void add(WBorderLayout layout, Widget wgt, Object constraints) {
        if (ComparatorUtils.equals(WBorderLayout.NORTH,constraints)) {
            layout.addNorth(wgt);
        } else if (ComparatorUtils.equals(WBorderLayout.SOUTH,constraints)) {
            layout.addSouth(wgt);
        } else if (ComparatorUtils.equals(WBorderLayout.EAST,constraints)) {
            layout.addEast(wgt);
        } else if (ComparatorUtils.equals(WBorderLayout.WEST,constraints)) {
            layout.addWest(wgt);
        } else if (ComparatorUtils.equals(WBorderLayout.CENTER,constraints)) {
            layout.addCenter(wgt);
        }
    }

    /**
     * ���¼����С
     */
    public void recalculateChildrenSize() {
        Dimension d = getSize();
        WBorderLayout layout = toData();
        layout.setNorthSize(d.height / Num);
        layout.setSouthSize(d.height / Num);
        layout.setWestSize(d.width / Num);
        layout.setEastSize(d.width / Num);
    }


    /**
     * ����ӵ�ʱ����Ҫ�ѿ�����ķ���ȷ����������д��add����
     * @param comp        ���
     * @param constraints         ��λ
     */
    public void add(Component comp, Object constraints) {
        super.add(comp, constraints);
        if (comp == null) {
            return;
        }
        XCreator creator = (XCreator) comp;
        // ��ӵ�����ʱ������ײ�
        if (ComparatorUtils.equals(BorderLayout.NORTH, constraints)) {
            creator.setDirections(new int[]{Direction.BOTTOM});
            // ��ӵ��ϲ�ʱ�����춥��
        } else if (ComparatorUtils.equals(BorderLayout.SOUTH, constraints)) {
            creator.setDirections(new int[]{Direction.TOP});
            // ��ӵ�������ʱ������������
        } else if (ComparatorUtils.equals(BorderLayout.EAST, constraints)) {
            creator.setDirections(new int[]{Direction.LEFT});
            // ��ӵ�������ʱ������ұ�����
        } else if (ComparatorUtils.equals(BorderLayout.WEST, constraints)) {
            creator.setDirections(new int[]{Direction.RIGHT});
        }
    }
    
	@Override
	public LayoutAdapter getLayoutAdapter() {
		return new FRBorderLayoutAdapter(this);
	}
}
