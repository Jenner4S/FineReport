/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import com.fr.base.BaseUtils;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.events.DesignerEditor;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.*;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WTitleLayout;
import com.fr.stable.StableUtils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author richer
 * @since 6.5.3 com.fr.base.listener.OB��������
 * 
 */
public abstract class XCreator extends JPanel implements XComponent, XCreatorTools {

	protected static final Border DEFALUTBORDER = BorderFactory.createLineBorder(new Color(210, 210, 210), 1);
	public static final Dimension SMALL_PREFERRED_SIZE = new Dimension(80, 21);
	protected static final Dimension MIDDLE_PREFERRED_SIZE = new Dimension(80, 50);
	protected static final Dimension BIG_PREFERRED_SIZE = new Dimension(80, 80);
	// barry: ��ק�ؼ�ʱ���ؼ�Ҫ�ָ�ԭʼ��С�����Ȱѿؼ���ǰ��С���ݵ����
	protected Dimension backupSize;
	protected XLayoutContainer backupParent;

	protected Widget data;
	protected JComponent editor;
	// XCreator���뵽ĳЩXLayoutContainer��ʱ���ܵ�����Ȼ��߸߶�
	private int[] directions;
	private Rectangle backupBound;

	public XCreator(Widget ob, Dimension initSize) {
		this.data = ob;

		this.initEditor();

		if (editor != null && editor != this) {
			this.setLayout(FRGUIPaneFactory.createBorderLayout());
			add(editor, BorderLayout.CENTER);
		}

		if (initSize.width == 0) {
			initSize.width = this.initEditorSize().width;
		}
		if (initSize.height == 0) {
			initSize.height = this.initEditorSize().height;
		}
		this.setPreferredSize(initSize);
		this.setSize(initSize);
		this.setMaximumSize(initSize);
		this.initXCreatorProperties();
	}

	public int[] getDirections() {
		return directions;
	}

	public void setDirections(int[] directions) {
		this.directions = directions;
	}

	/**
	 * Ӧ�ñ��ݵĴ�С
	 */
	public void useBackupSize() {
		if (this.backupSize != null) {
			setSize(this.backupSize);
		}
	}
	
	/**
	 * ���ݵ�ǰ��С
	 */
	public void backupCurrentSize() {
		this.backupSize = getSize();
	}

	public XLayoutContainer getBackupParent() {
		return backupParent;
	}

	public void setBackupParent(XLayoutContainer backupContainer) {
		this.backupParent = backupContainer;
	}

	/**
	 * ���ݵ�ǰparent����
	 */
	public void backupParent() {
		setBackupParent(XCreatorUtils.getParentXLayoutContainer(this));
	}
	
	/**
	 * ��ȡ��ǰXCreator��һ����װ������
	 * 
	 * @param widgetName ��ǰ�����
	 * 
	 * @return ��װ�ĸ�����
	 * 
	 *
	 * @date 2014-11-25-����4:47:23
	 * 
	 */
	protected XLayoutContainer getCreatorWrapper(String widgetName){
		return new XWTitleLayout();
	}
	
	/**
	 * ����ǰ������ӵ���������
	 * 
	 * @param parentPanel ���������
	 * 
	 *
	 * @date 2014-11-25-����4:57:55
	 * 
	 */
	protected void addToWrapper(XLayoutContainer parentPanel, int width, int minHeight){			
		parentPanel.add(this, WTitleLayout.BODY);
	}
	
	/**
	 * ���ø�����������
	 * 
	 * @param parentPanel ��ǰ������
	 * @param widgetName ��ǰ�ؼ���
	 * 
	 *
	 * @date 2014-11-27-����9:47:00
	 * 
	 */
	protected void setWrapperName(XLayoutContainer parentPanel, String widgetName){
		parentPanel.toData().setWidgetName(widgetName);
	}
	
	/**
	 * ��ʼ����ǰ����ĸ�����
	 * �����Ϊ����: Scale������, Title������, Border�Զ��������
	 * 
	 * @param minHeight ��С�߶�
	 * 
	 * @return ������
	 * 
	 *
	 * @date 2014-11-25-����5:15:23
	 * 
	 */
	public XLayoutContainer initCreatorWrapper(int minHeight){
		XLayoutContainer parentPanel;
		String widgetName = this.toData().getWidgetName();
		parentPanel = this.getCreatorWrapper(widgetName);
		
		int width = this.getWidth();
		int height = this.getHeight();
		
		parentPanel.setLocation(this.getX(), this.getY());
		parentPanel.setSize(width, height);
		setWrapperName(parentPanel, widgetName);
		this.setLocation(0, 0);
		this.addToWrapper(parentPanel, width, minHeight);
		LayoutUtils.layoutRootContainer(parentPanel);
		
		return parentPanel;
	}

	/**
	 * ��ʼ��creator������ֵ
	 */
	public void rebuid() {
		initXCreatorProperties();
	}

	/**
	 * �����������ֵ
	 * @return �����������ֵ
	 * @throws IntrospectionException �쳣
	 */
	public abstract CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException;

	/**
	 * ����creator��Ӧ�Ŀؼ�widget
	 * @return �ؼ�widget
	 */
	public Widget toData() {
		return this.data;
	}

	protected abstract JComponent initEditor();

	/**
	 * ����Widget������ֵ��ʼ��XCreator������ֵ
	 */
	protected abstract void initXCreatorProperties();

	/**
	 * ����XCreator��Ĭ�ϴ�С80x21
	 * @return Ĭ�ϵ���С��С
	 */
	public Dimension initEditorSize() {
		return SMALL_PREFERRED_SIZE;
	}

	protected String getIconName() {
        return "";
    }

	public String getIconPath() {
		return "/com/fr/web/images/form/resources/" + getIconName();
	}

	/**
	 * �������Ĭ����
	 * @return �������(Сд)
	 */
	public String createDefaultName() {
		String name = this.getClass().getSimpleName();
		return Character.toLowerCase(name.charAt(1)) + name.substring(2);
	}

	@Override
	public void setBounds(Rectangle bounds) {
		Dimension size = this.getMinimumSize();
		if (bounds.getWidth() < size.width) {
			bounds.width = size.width;
			//����϶�������á�
			bounds.x = this.getX();
		}
		if (bounds.getHeight() < size.height) {
			bounds.height = size.height;
			bounds.y = this.getY();
		}
		super.setBounds(bounds);
	}

	public DesignerEditor<? extends JComponent> getDesignerEditor() {
		return null;
	}

	/**
	 * ����Ȩ�ޱ༭���߽���
	 * @param jform ������
	 *@param formEditor ��ƽ������
	 *@return ���߽���
	 */
	public JComponent createToolPane(BaseJForm jform, FormDesigner formEditor) {
		if (!BaseUtils.isAuthorityEditing()) {
			if (isDedicateContainer()) {
				// ͼ���ͱ�������ڿؼ���������ʾ������Ӧ�����Ա�Ҫ��ʾ���˴�������
				XCreator child = ((XLayoutContainer) this).getXCreator(0);
				return child.createToolPane(jform, formEditor);
			}
			return WidgetPropertyPane.getInstance(formEditor);
		} else {
			//�ж��ǲ��ǲ��֣����ֲ�֧��Ȩ�ޱ༭
			if (formEditor.isSupportAuthority()) {
				AuthorityPropertyPane authorityPropertyPane = new AuthorityPropertyPane(formEditor);
				authorityPropertyPane.populate();
				return authorityPropertyPane;
			}

			return new NoSupportAuthorityEdit();

		}

	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(0, 0);
	}
	
	/**
	 * �Ƿ�֧���л����������༭
	 * @return ���򷵻�true
	 */
	public boolean isReport(){
		return false;
	}
    
    /**
     * ������Ƿ��������������
     * @return ���򷵻�true
     */
    public boolean canEnterIntoParaPane(){
        return true;
    }

    /**
     * ������Ƿ�������������
     * @return ���򷵻�true
     */
    public boolean canEnterIntoAdaptPane(){
        return true;
    }

    /**
     * ������Ƿ������ק(���в�����������Ӧ���ֲ�������ק)
     * @return ���򷵻�true
     */
    public boolean isSupportDrag(){
        return true;
    }

    public List<String> getAllXCreatorNameList(XCreator xCreator,  List<String> namelist){
        namelist.add(xCreator.toData().getWidgetName());
        return namelist;
    }

    /**
     * �Ƿ��в�ѯ��ť
     * @param xCreator  �ؼ�������
     * @return  ���޲�ѯ��ť
     */
    public boolean SearchQueryCreators(XCreator xCreator) {
        return false;
    }

	/**
	 * @return the backupBound
	 */
	public Rectangle getBackupBound() {
		return backupBound;
	}

	/**
	 * @param rec the backupBound to set
	 */
	public void setBackupBound(Rectangle rec) {
		this.backupBound = rec;
	}
	
	/**
	 * �ؼ�������ʾ�����
	 * @param path �ؼ���list
	 */
	public void notShowInComponentTree(ArrayList<Component> path) {
		return;
	}
	
	/**
	 * �������������
	 * @param name ����
	 */
	public void resetCreatorName(String name) {
		toData().setWidgetName(name);
	}
	
	/**
	 * ���ر༭���������scaleΪ���ڲ����
	 * @return ���
	 */
	public XCreator getEditingChildCreator() {
		return this;
	}
	
	/**
	 * ���ض�Ӧ���Ա�������scale��title�����������
	 * @return ���
	 */
	public XCreator getPropertyDescriptorCreator() {
		return this;
	}
	
	/**
	 * �����������Bound; û�в�����
	 * @param minHeight ��С�߶�
	 */
	public void updateChildBound(int minHeight) {
		return;
	}
	
	/**
	 * �Ƿ���Ϊ�ؼ�����Ҷ�ӽڵ�
	 * @return ���򷵻�true
	 */
	public boolean isComponentTreeLeaf() {
		return true;
	}
	
	/**
	 *  �Ƿ�Ϊsclae��titleר������
	 * @return ���򷵻�true
	 */
	public boolean isDedicateContainer() {
		return false;
	}
	
	/**
     * �Ƿ������������
     * @param acceptTypes ���յ�����
     * @return ����ָ���������򷵻�true,���򷵻�false
     */
    public boolean acceptType(Class<?>... acceptTypes) {
        for (Class<?> type : acceptTypes) {
            if (StableUtils.classInstanceOf(this.getClass(), type)) {
                return true;
            }
        }
        return false;
    }

	/**
	 * �Ƿ����Ҫ����(����Ӧ�ﲿ�������Ҫ, �����֡��ı���������������ѡ�����롢��������������ѡ��������)
	 * 
	 * @return ���򷵻�true
	 */
	public boolean shouldScaleCreator() {
		return false;
	}
	
	/**
	 * �Ƿ�֧�ֱ�����ʽ
	 * @return Ĭ��false
	 */
	public boolean hasTitleStyle() {
		return false;
	}
	
	/**
	 * ��Ӧ����¼�
	 * 
	 * @param editingMouseListener �������λ�ô�����
	 * @param e ������¼�
	 */
	public void respondClick(EditingMouseListener editingMouseListener,MouseEvent e){
		FormDesigner designer = editingMouseListener.getDesigner();
		SelectionModel selectionModel = editingMouseListener.getSelectionModel();

		if (e.getClickCount() <= 1) {
			selectionModel.selectACreatorAtMouseEvent(e);
		}

		if (editingMouseListener.stopEditing()) {
			if (this != designer.getRootComponent()) {
				ComponentAdapter adapter = AdapterBus.getComponentAdapter(designer, this);
				editingMouseListener.startEditing(this, adapter.getDesignerEditor(), adapter);
			}
		}
	}
	
	/**
	 * ɾ��������
	 * 
	 * @param creator ��ǰ���
	 * @param designer �������
	 *
	 */
	public void deleteRelatedComponent(XCreator creator,FormDesigner designer){
		return;
	}
	
	/**
	 * ѡ��������
	 * 
	 * @param creator ��ǰ���
	 * 
	 */
	public void seleteRelatedComponent(XCreator creator){
		return;
	}
	
	/**
	 * �������
	 * @return
	 * String
	 */
	public XCreator getXCreator(){
		return this;
	}
	
	/**
	 * ���ٷֱȵ������
	 * @param percent �ٷֱ�
	 * void
	 */
	public void adjustCompSize(double percent){
		return;
	}
	
	/**
	 * ����һЩ��Ҫ�������
	 * @return ����һЩ��Ҫ�������
	 * ArrayList<?>
	 */
	public ArrayList<?> getTargetChildrenList(){
		return new ArrayList();
	}
	
	public XLayoutContainer getOuterLayout(){
		return this.getBackupParent();
	}
	
	/**
	 * ���µ�����������
	 * @param width ���
	 */
	public void recalculateChildWidth(int width){
		return;
	}
	/**
	 * ���µ���������߶�
	 * @param height �߶�
	 */
	public void recalculateChildHeight(int height){
		return;
	}
}
