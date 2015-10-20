/**
 * 
 */
package com.fr.design.designer.creator;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ContainerEvent;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRScaleLayoutAdapter;
import com.fr.design.form.layout.FRScaleLayout;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget;
import com.fr.form.ui.container.WScaleLayout;

/**
 * ����Ӧ������������ʱ�����ֿؼ��������ı���������Ҫ���ֿؼ�Ĭ�ϸ߶�21�� �ô�������ʵ��
 * 
 * @author jim
 * @date 2014-8-5
 */
public class XWScaleLayout extends DedicateLayoutContainer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8870102816567181548L;
    public static final int INDEX=0;

	/**
	 * ���췽��
	 */
	public XWScaleLayout() {
		this(new WScaleLayout("scalePanel"), new Dimension());
	}

	/**
	 * �������캯��
	 * 
	 * @param widget �ؼ�widget
	 * @param initSize �ߴ��С
	 */
	public XWScaleLayout(WScaleLayout widget, Dimension initSize) {
		super(widget, initSize);
	}
	
	/**
	 * ��ʼ��������Ӧ�Ĳ��� ������ֻװһ����Ҫ����ԭ���߶ȵĿؼ���������Ϊabsolute
	 */
	@Override
	protected void initLayoutManager() {
		this.setLayout(new FRScaleLayout());
	}

	/**
	 * ��������Ⱦ��
	 */
	@Override
	public LayoutAdapter getLayoutAdapter() {
		return new FRScaleLayoutAdapter(this);
	}

	/**
	 * ����������Ӧ��wlayout
	 * 
	 * @return ͬ��
	 */
	@Override
	public WScaleLayout toData() {
		return (WScaleLayout) data;
	}

	/**
	 * ����Ĭ�����name
	 * 
	 * @return ������
	 */
	@Override
	public String createDefaultName() {
		return "scalePanel";
	}

	/**
	 * ��WLayoutת��ΪXLayoutContainer
	 */
	@Override
	public void convert() {
		isRefreshing = true;
		WScaleLayout layout = this.toData();
		this.removeAll();
		for (int i = 0, num = layout.getWidgetCount(); i < num; i++) {
			BoundsWidget bw = (BoundsWidget) layout.getWidget(i);
			if (bw != null) {
				Rectangle bounds = bw.getBounds();
				XWidgetCreator comp = (XWidgetCreator) XCreatorUtils
						.createXCreator(bw.getWidget());
				this.add(comp, bw.getWidget().getWidgetName());
				comp.setBounds(bounds);
			}
		}
		isRefreshing = false;
	}

	/**
	 * �������
	 * 
	 * @param e
	 *            �����¼�
	 */
	@Override
	public void componentAdded(ContainerEvent e) {
		if (isRefreshing) {
			return;
		}
		WScaleLayout layout = this.toData();
		layout.removeAll();
		for (int i = 0, num = this.getComponentCount(); i < num; i++) {
			XWidgetCreator creator = (XWidgetCreator) this.getComponent(i);
			layout.addWidget(new BoundsWidget(creator.toData(), creator
					.getBounds()));
		}
	}

	/**
	 * ���ɾ��
	 * 
	 * @param e
	 *            �����¼�
	 */
	@Override
	public void componentRemoved(ContainerEvent e) {
		if (isRefreshing) {
			return;
		}
		XWidgetCreator xwc = ((XWidgetCreator) e.getChild());
		Widget wgt = xwc.toData();
		BoundsWidget bw = new BoundsWidget(wgt, xwc.getBounds());
		WScaleLayout wlayout = this.toData();
		wlayout.removeWidget(bw);
	}

	/**
	 * ���ش������Ķ�Ӧ���
	 * @return ���
	 */
	public XCreator getEditingChildCreator() {
		return getXCreator(INDEX);
	}
	
	/**
	 * ���������backupBound,scale��title����ҲҪͬ�������������
	 * @param minHeight ��С�߶�
	 */
	public void updateChildBound(int minHeight) {
		XCreator child = getXCreator(INDEX);
		child.setSize(getWidth(), minHeight);
	}
	
}
