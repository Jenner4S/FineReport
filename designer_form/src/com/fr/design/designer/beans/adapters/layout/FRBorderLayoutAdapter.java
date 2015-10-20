package com.fr.design.designer.beans.adapters.layout;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import com.fr.general.ComparatorUtils;
import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.beans.painters.FRBorderLayoutPainter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWBorderLayout;
import com.fr.design.designer.properties.FRBorderLayoutConstraints;
import com.fr.design.form.layout.FRBorderLayout;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.design.utils.gui.LayoutUtils;

public class FRBorderLayoutAdapter extends AbstractLayoutAdapter {

    private HoverPainter painter;

    public FRBorderLayoutAdapter(XLayoutContainer container) {
        super(container);
        painter = new FRBorderLayoutPainter(container);
    }

    @Override
	public HoverPainter getPainter() {
		return painter;
	}

    /**
     * �еĿؼ�����ק������С����Ҫ���������������¼����µ�ǰ�ĳߴ��Ƿ���ʣ���������ʣ�����Ҫ����fixһ��
     * @param creator ���
     */
    public void fix(XCreator creator) {
        FRBorderLayout layout = (FRBorderLayout)container.getFRLayout();
        Object constraints = layout.getConstraints(creator);
        if (ComparatorUtils.equals(constraints, BorderLayout.NORTH)) {
            ((XWBorderLayout)container).toData().setNorthSize(creator.getHeight());
        } else if (ComparatorUtils.equals(constraints, BorderLayout.SOUTH)) {
            ((XWBorderLayout)container).toData().setSouthSize(creator.getHeight());
        } else if (ComparatorUtils.equals(constraints, BorderLayout.EAST)) {
            ((XWBorderLayout)container).toData().setEastSize(creator.getWidth());
        } else if (ComparatorUtils.equals(constraints, BorderLayout.WEST)) {
            ((XWBorderLayout)container).toData().setWestSize(creator.getWidth());
        } else {
        	return;
        }
        container.recalculateChildrenPreferredSize();
    }

    /**
     * �������
     * @param child    ���
     * @param x    ������
     * @param y    ������
     */
    public void addComp(XCreator child, int x, int y) {
        String placement = getPlacement(child, x, y);     
        container.add(child, placement);
        LayoutUtils.layoutRootContainer(container);
    }

    /**
     * ��������״̬ʱ��������ƶ���ĳ�������Ϸ�ʱ������������в��ֹ������������øò���
     * ������������accept��������ǰλ���Ƿ���Է��ã����ṩ����ı�ʶ�������ɫ�����ʶ����
     * ����BorderLayout�У����ĳ����λ�Ѿ���������������ʱӦ�÷���false��ʶ�����򲻿���
     * ���á�
     *@param creator ���
     *@param x ��ӵ�λ��x����λ���������container��
     *@param y ��ӵ�λ��y����λ���������container��
     *@return �Ƿ���Է���
     */
    public boolean accept(XCreator creator, int x, int y) {
        String placement = getPlacement(creator, x, y);
        FRBorderLayout blayout = (FRBorderLayout) container.getLayout();
        Component comp = blayout.getLayoutComponent(placement);
        return comp == null;
    }

    public Dimension getPreferredSize(XCreator creator) {
        int hw = container.getWidth();
        int hh = container.getHeight();

        Dimension prefSize = creator.getSize();

        if (prefSize.width > (hw / 3)) {
            prefSize.width = hw / 3;
        }

        if (prefSize.height > (hh / 3)) {
            prefSize.height = hh / 3;
        }

        return prefSize;
    }

    private String getPlacement(XCreator creator, int x, int y) {
        int width = container.getWidth();
        int height = container.getHeight();
        WBorderLayout wLayout = ((XWBorderLayout)container).toData();
        int northSize = wLayout.getNorthSize();
        int southSize = wLayout.getSouthSize();
        int eastSize = wLayout.getEastSize();
        int westSize = wLayout.getWestSize();    
        if (y < northSize) {
            return BorderLayout.NORTH;
        } else if ((y >= northSize) && (y < (height - southSize))) {
            if (x < westSize) {
                return BorderLayout.WEST;
            } else if ((x >= westSize) && (x < (width - eastSize))) {
                return BorderLayout.CENTER;
            } else {
                return BorderLayout.EAST;
            }
        } else {
            return BorderLayout.SOUTH;
        }
    }

    /**
     * ������һ�����
     * @param dragged ���
     */
    public void addNextComponent(XCreator dragged) {
        FRBorderLayout layout = (FRBorderLayout) container.getLayout();
        Component north = layout.getLayoutComponent(BorderLayout.NORTH);
        Component south = layout.getLayoutComponent(BorderLayout.SOUTH);
        Component west = layout.getLayoutComponent(BorderLayout.WEST);
        Component east = layout.getLayoutComponent(BorderLayout.EAST);
        Component center = layout.getLayoutComponent(BorderLayout.CENTER);

        if (north == null) {
            container.add(dragged, BorderLayout.NORTH);
        } else if (south == null) {
            container.add(dragged, BorderLayout.SOUTH);
        } else if (west == null) {
            container.add(dragged, BorderLayout.WEST);
        } else if (east == null) {
            container.add(dragged, BorderLayout.EAST);
        } else if (center == null) {
            container.add(dragged, BorderLayout.CENTER);
        }

        LayoutUtils.layoutRootContainer(container);
    }

    /**
     * Ŀ��ؼ�λ�ò������
     * @param target Ŀ��
     * @param added �������
     */
    public void addBefore(XCreator target, XCreator added) {
        addNextComponent(added);
    }

    /**
     * ����Ŀ���������
     * @param target Ŀ��
     * @param added �������
     */
    public void addAfter(XCreator target, XCreator added) {
        addNextComponent(added);
    }

    /**
     * �Ƿ��ܽ��ո�������
     * @return ���򷵻�true
     */
    public boolean canAcceptMoreComponent() {
        FRBorderLayout layout = (FRBorderLayout) container.getLayout();
        Component north = layout.getLayoutComponent(BorderLayout.NORTH);
        Component south = layout.getLayoutComponent(BorderLayout.SOUTH);
        Component west = layout.getLayoutComponent(BorderLayout.WEST);
        Component east = layout.getLayoutComponent(BorderLayout.EAST);
        Component center = layout.getLayoutComponent(BorderLayout.CENTER);

        return (north == null) || (south == null) || (west == null) || (east == null) || (center == null);
    }

    @Override
	public ConstraintsGroupModel getLayoutConstraints(XCreator creator) {
		return new FRBorderLayoutConstraints(container, creator);
	}

}
