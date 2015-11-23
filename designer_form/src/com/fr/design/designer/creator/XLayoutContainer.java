/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import com.fr.base.FRContext;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.form.layout.FRLayoutManager;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.widget.editors.PaddingMarginEditor;
import com.fr.design.mainframe.widget.editors.WLayoutBorderStyleEditor;
import com.fr.design.mainframe.widget.renderer.LayoutBorderStyleRenderer;
import com.fr.design.mainframe.widget.renderer.PaddingMarginCellRenderer;
import com.fr.design.parameter.ParameterBridge;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WLayout;
import com.fr.general.Background;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.core.PropertyChangeAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.beans.IntrospectionException;
import java.util.List;

/**
 * @author richer
 * @since 6.5.3
 */
public abstract class XLayoutContainer extends XBorderStyleWidgetCreator implements ContainerListener, ParameterBridge {
	
	// �����ڲ����Ĭ����С���36����С�߶�21
	public static int MIN_WIDTH = 36;
	public static int MIN_HEIGHT = 21; 
	
	protected static final Dimension LARGEPREFERREDSIZE = new Dimension(200, 200);
    protected boolean isRefreshing;
    protected int default_Length = 5; // ȡָ�������ڵ������Ĭ��Ϊ5��֤ȡ�Ĳ����ڵ����ʱx��y������ڷǱ߿���

    public XLayoutContainer(WLayout widget, Dimension initSize) {
        super(widget, initSize);
        this.addContainerListener(this);
    }

    /**
     *  �õ�������
     * @return ������
     * @throws IntrospectionException
     */
	public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
		return  new CRPropertyDescriptor[] {
                new CRPropertyDescriptor("widgetName", this.data.getClass()).setI18NName(Inter
                        .getLocText("FR-Designer_Form-Widget_Name")),
                new CRPropertyDescriptor("borderStyle", this.data.getClass()).setEditorClass(
                        WLayoutBorderStyleEditor.class).setRendererClass(LayoutBorderStyleRenderer.class).setI18NName(
                        Inter.getLocText("FR-Engine_Style")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced")
                        .setPropertyChangeListener(new PropertyChangeAdapter() {

                            @Override
                            public void propertyChange() {
                            	initStyle();
                            }
                        }),
                new CRPropertyDescriptor("margin", this.data.getClass()).setEditorClass(PaddingMarginEditor.class)
                        .setRendererClass(PaddingMarginCellRenderer.class).setI18NName(Inter.getLocText("FR-Designer_Layout-Padding"))
                        .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
                    };
	}

    /**
     *   ���ض�Ӧ��wlayout
     * @return wlayout�ؼ�
     */
    public WLayout toData() {
        return (WLayout) data;
    }

	@Override
	protected void initXCreatorProperties() {
		super.initXCreatorProperties();
		initBorderStyle();
		this.initLayoutManager();
		this.convert();
	}

    @Override
	protected JComponent initEditor() {
		return this;
	}

	/**
	 * ��ǰ���zorderλ���滻�µĿؼ�
	 * @param widget �ؼ�
	 * @param  oldcreator �����
	 * @return ���
	 */
	public XCreator replace(Widget widget, XCreator oldcreator) {
		int i = this.getComponentZOrder(oldcreator);
		if (i != -1) {
			this.toData().replace(widget, oldcreator.toData());
			this.convert();
			XCreator creator = (XCreator) this.getComponent(i);
			creator.setSize(oldcreator.getSize());
			return creator;
		}
		return null;
	}

    /**
     *  ��ʼ��ʱĬ�ϵ������С
     * @return   Ĭ��Dimension
     */
    public Dimension initEditorSize() {
        return LARGEPREFERREDSIZE;
    }

    protected abstract void initLayoutManager();

    /**
     * ��WLayoutת��ΪXLayoutContainer
     */
    public void convert() {
        isRefreshing = true;
        WLayout layout = this.toData();
        this.removeAll();
        addWidgetToSwingComponent(layout);
        isRefreshing = false;
    }

    protected void addWidgetToSwingComponent(WLayout layout) {
        for (int i = 0; i < layout.getWidgetCount(); i++) {
            Widget wgt = layout.getWidget(i);
            if (wgt != null) {
                XWidgetCreator comp = (XWidgetCreator) XCreatorUtils.createXCreator(wgt, calculatePreferredSize(wgt));
                this.add(comp, i);
            }
        }
    }

    /**
     * ��ƽ�������������ʱ��Ҫ֪ͨWLayout��������paint
     * @param e     ��˵��
     */
    @Override
    public void componentAdded(ContainerEvent e) {
        if (isRefreshing) {
            return;
        }
        XWidgetCreator creator = (XWidgetCreator) e.getChild();
        WLayout wlayout = this.toData();
        Widget wgt = creator.toData();
        wlayout.addWidget(wgt);
        this.recalculateChildrenPreferredSize();
    }

    /**
     * ��ƽ�������������ʱ��Ҫ֪ͨWLayout��������paint
     * @param e     ��˵��
     */
    @Override
    public void componentRemoved(ContainerEvent e) {
        if (isRefreshing) {
            return;
        }
        WLayout wlayout = this.toData();
        Widget wgt = ((XWidgetCreator) e.getChild()).toData();
        wlayout.removeWidget(wgt);
        this.recalculateChildrenPreferredSize();
    }

    /**
     * ����widget������ֵ����ȡ
     * @param wgt
     * @return
     */
    protected Dimension calculatePreferredSize(Widget wgt) {
        return new Dimension();
    }

    /**
     * ���µ���������Ĵ�С
     */
    public void recalculateChildrenPreferredSize() {
        for (int i = 0; i < this.getComponentCount(); i++) {
            XCreator creator = this.getXCreator(i);
            Widget wgt = creator.toData();
            Dimension dim = calculatePreferredSize(wgt);
            creator.setPreferredSize(dim);
            creator.setMaximumSize(dim);
        }
    }

    public int getXCreatorCount() {
        return getComponentCount();
    }
    
    public XCreator getXCreator(int i) {
        return (XCreator) getComponent(i);
    }

    /**
     * ������Ƿ��������������
     * @return ���򷵻�true
     */
    public boolean canEnterIntoParaPane(){
        return false;
    }
    
    /**
	 * �Ƿ���Ϊ�ؼ�����Ҷ�ӽڵ�
	 * @return ���򷵻�true
	 */
	public boolean isComponentTreeLeaf() {
		return false;
	}

    public List<String> getAllXCreatorNameList(XCreator xCreator, List<String> namelist){
        for (int i = 0; i < ((XLayoutContainer)xCreator).getXCreatorCount(); i++) {
            XCreator creatorSon = ((XLayoutContainer)xCreator).getXCreator(i);
            creatorSon.getAllXCreatorNameList(creatorSon, namelist);
        }
        return namelist;
    }

    /**
     * �Ƿ��в�ѯ��ť
     * @param xCreator  �ؼ�������
     * @return  ���޲�ѯ��ť
     */
    public boolean SearchQueryCreators(XCreator xCreator) {
        for (int i = 0; i < ((XLayoutContainer)xCreator).getXCreatorCount(); i++) {
            XCreator creatorSon = ((XLayoutContainer)xCreator).getXCreator(i);
            if(creatorSon.SearchQueryCreators(creatorSon)){
                return true;
            }
        }
        return false;
    }
	
    public FRLayoutManager getFRLayout() {
        LayoutManager layout = getLayout();
        if (layout instanceof FRLayoutManager) {
            return (FRLayoutManager) layout;
        }
        FRContext.getLogger().error("FRLayoutManager isn't exsit!");
        return null;
    }

	public abstract LayoutAdapter getLayoutAdapter();

	public int getIndexOfChild(Object child) {
		int count = getComponentCount();
		for (int i = 0; i < count; i++) {
			Component comp = getComponent(i);
			if (comp == child) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * ��ҪΪ����Ӧ��
	 * ����ָ��point���Ϸ����
	 * @param x  xλ��
	 * @param y  yλ��
	 * @return ָ��λ�õ����
	 */
	public Component getTopComp(int x, int y) {
		return this.getComponentAt(x, y-default_Length);
	}
	
	/**
	 * ��ҪΪ����Ӧ��
	 * ����ָ��point�������
	 * @param x  xλ��
	 * @param y  yλ��
	 * @return ָ��λ�õ����
	 */
	public Component getLeftComp(int x, int y) {
		return this.getComponentAt(x-default_Length, y);
	}
	
	/**
	 * ����ָ��point���ҷ����
	 * @param x  xλ��
	 * @param y  yλ��
	 * @param w  ���
	 * @return ָ��λ�õ����
	 */
	public Component getRightComp(int x, int y, int w) {
		return this.getComponentAt(x+w+default_Length, y);
	}
	
	/**
	 * ����ָ��point���·����
	 * @param x  xλ��
	 * @param y  yλ��
	 * @param h �߶�
	 * @return ָ��λ�õ����
	 */
	public Component getBottomComp(int x, int y, int h) {
		return this.getComponentAt(x, y+h+default_Length);
	}
	
	/**
	 * ����ָ��point���Ϸ������Ҳ�����
	 * @param x  xλ��
	 * @param y  yλ��
	 * @param w ���
	 * @return ָ��λ�õ����
	 */
	public Component getRightTopComp(int x, int y, int w) {
		return this.getComponentAt(x+w-default_Length, y-default_Length);
	}
	
	/**
	 * ����ָ��point���������²�����
	 * @param x  xλ��
	 * @param y  yλ��
	 * @param h �߶�
	 * @return ָ��λ�õ����
	 */
	public Component getBottomLeftComp(int x, int y, int h) {
		return this.getComponentAt(x-default_Length, y+h-default_Length);
	}
	
	/**
	 * ����ָ��point���ҷ������²�����
	 * @param x  xλ��
	 * @param y  yλ��
	 * @param h �߶�
	 * @param w ���
	 * @return ָ��λ�õ����
	 */
	public Component getBottomRightComp(int x, int y, int h, int w) {
		return this.getComponentAt(x+w+default_Length, y+h-default_Length);
	}
	
	/**
	 * ����ָ��point���·������Ҳ�����
	 * @param x  xλ��
	 * @param y  yλ��
	 * @param h �߶�
	 * @param w ���
	 * @return ָ��λ�õ����
	 */
	public Component getRightBottomComp(int x, int y, int h, int w) {
		return this.getComponentAt(x+w-default_Length, y+h+default_Length);
	}

    /**
     * �Ƿ��ӳ�չʾ�������ݣ�Ҳ����˵�Ƿ�Ҫ�ȵ���˲�ѯ֮���ִ�б���
     * @return �����true�����ʾ���֮��ſ�ʼ���㣬false���ʾ����ݲ���Ĭ��ֱֵ�Ӽ��㱨��չ��
     */
    public boolean isDelayDisplayContent() {
        return false;
    }

    /**
     * �Ƿ���ʾ��������
     * @return ��ʾ���������򷵻�true�����򷵻�false
     */
    public boolean isDisplay() {
        return false;
    }

    public Background getDataBackground(){
        return toData().getBackground();
    }

    /**
     * ��ȡ��������Ŀ��
     * @return ���
     */
    public int getDesignWidth() {
        return 0;
    }

    /**
     * ��ȡ�������Ķ��뷽ʽ
     * @return ���������ֶ��뷽ʽ
     */
    public int getPosition() {
        return 0;
    }
	
    /**
     * �л��������״̬
     * 
     * @param designer �������
     */
    public void stopAddingState(FormDesigner designer){
    	return;
    }
    
    /**
	 * Ѱ�������Ϊ����Ӧ���ֵĸ�����
	 * 
	 * @return ��������
	 * 
	 *
	 * @date 2014-12-30-����3:15:28
	 * 
	 */
    public XLayoutContainer findNearestFit(){
    	//һ��һ��������, �ҵ������fit��һ���return
    	XLayoutContainer parent = this.getBackupParent();
    	return parent == null ? null : parent.findNearestFit();
    } 
    
    /**
     * ��ȡ���������ڲ����������ֵ
     * 
     * @return ����������
     */
    public int[] getHors(){
    	return ArrayUtils.EMPTY_INT_ARRAY;
    }
    
    /**
     * ��ȡ���������ڲ����������ֵ
     * 
     * @return ����������
     */
    public int[] getVeris(){
    	return ArrayUtils.EMPTY_INT_ARRAY;
    }

    public void setDelayDisplayContent(boolean delayPlaying){

    }

    public void setPosition(int align){

    }

    public void setDisplay(boolean showWindow){

    }

    public void setBackground(Background background){

    }
}
