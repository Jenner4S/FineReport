/*
 * Copyright(c) 2001-2011, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.form.parameter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.fr.base.BaseUtils;
import com.fr.base.Parameter;
import com.fr.base.parameter.ParameterUI;
import com.fr.design.DesignModelAdapter;
import com.fr.design.designer.beans.actions.CopyAction;
import com.fr.design.designer.beans.actions.CutAction;
import com.fr.design.designer.beans.actions.FormDeleteAction;
import com.fr.design.designer.beans.actions.PasteAction;
import com.fr.design.designer.beans.adapters.layout.FRAbsoluteLayoutAdapter;
import com.fr.design.designer.beans.events.DesignerEditListener;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWAbsoluteLayout;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.design.designer.properties.FormWidgetAuthorityEditPane;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.mainframe.AuthorityEditPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.EastRegionContainerPane;
import com.fr.design.mainframe.FormArea;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormDesignerModeForSpecial;
import com.fr.design.mainframe.FormParaPane;
import com.fr.design.mainframe.FormWidgetDetailPane;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.mainframe.WidgetToolBarPane;
import com.fr.design.parameter.ParaDefinitePane;
import com.fr.design.parameter.ParameterDesignerProvider;
import com.fr.design.parameter.ParameterPropertyPane;
import com.fr.form.main.Form;
import com.fr.form.main.parameter.FormParameterUI;
import com.fr.form.parameter.FormSubmitButton;
import com.fr.form.ui.EditorHolder;
import com.fr.form.ui.WidgetValue;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.form.ui.container.WLayout;
import com.fr.form.ui.container.WParameterLayout;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;

/**
 * Created by IntelliJ IDEA.
 * User   : Richer
 * Version: 6.5.5
 * Date   : 11-7-5
 * Time   : ����7:46
 * �����͵Ĳ��������
 */
// TODO ALEX_SEP FormDesigner��FormParaDesignEditorӦ�ù���Form�ı༭,����FormParaDesignEditor��Ӧ��ֱ�Ӿ���FormDesigner
public class FormParaDesigner extends FormDesigner implements ParameterDesignerProvider {
	private static final int NUM_IN_A_LINE = 4;
	private static final int H_COMPONENT_GAP = 165;
	private static final int V_COMPONENT_GAP = 25;
	private static final int FIRST_V_LOCATION = 35;
	private static final int FIRST_H_LOCATION = 90;
	private static final int SECOND_H_LOCATION = 170;
	private static final int ADD_HEIGHT = 20;
	private static final int H_GAP = 105;

	private static Image paraImage = BaseUtils.readImage("/com/fr/design/images/form/parameter.png");

	public FormParaDesigner() {
		this(new FormParameterUI());
	}

	public FormParaDesigner(FormParameterUI ui) {
		super(gen(ui));
	}

	private static Form gen(Form form) {
		WLayout container = form.getContainer();
		if (container == null) {
			container = new WParameterLayout();
		}
		container.setWidgetName("para");
		form.setContainer(container);
		return form;
	}

	protected FormDesignerModeForSpecial<?> createFormDesignerTargetMode() {
		return new FormParaTargetMode(this);
	}

	/**
	 * ��ʼ�༭��������ʱ����еĳ�ʼ��
	 */
	public void initBeforeUpEdit() {
		WidgetToolBarPane.getInstance(this);
		EastRegionContainerPane.getInstance().replaceDownPane(
                FormWidgetDetailPane.getInstance(this));
		if (!BaseUtils.isAuthorityEditing()) {
			EastRegionContainerPane.getInstance().replaceUpPane(
					WidgetPropertyPane.getInstance(this));
		} else {
			showAuthorityEditPane();
		}

	}

    /**
     * ����Ȩ�ޱ༭���
     * @return ���
     */
	public AuthorityEditPane createAuthorityEditPane() {
		return new FormWidgetAuthorityEditPane(this);
	}

    /**
     * �������Ա����
     * @return   �������Ա����
     */
	public JPanel getEastUpPane() {
		return WidgetPropertyPane.getInstance(this);
	}

    /**
     *   �������Ա�
     * @return    �������Ա�
     */

	public JPanel getEastDownPane() {
		return FormWidgetDetailPane.getInstance(this);
	}

    /**
     * Ȩ�ޱ༭���
     * @return     Ȩ�ޱ༭���
     */
	public AuthorityEditPane getAuthorityEditPane() {
		FormWidgetAuthorityEditPane formWidgetAuthorityEditPane = new FormWidgetAuthorityEditPane(this);
		formWidgetAuthorityEditPane.populateDetials();
		return formWidgetAuthorityEditPane;
	}

	/**
	 * ��������FormParaDesigner��ParameterDefinitePane����¼�
	 *
	 * @param paraDefinitePane ���
	 */
	public void addListener(final ParaDefinitePane paraDefinitePane) {
		this.getEditListenerTable().addListener(new DesignerEditListener() {

			@Override
			public void fireCreatorModified(DesignerEvent evt) {
				if (evt.getCreatorEventID() == DesignerEvent.CREATOR_ADDED
						|| evt.getCreatorEventID() == DesignerEvent.CREATOR_CUTED
						|| evt.getCreatorEventID() == DesignerEvent.CREATOR_PASTED
						|| evt.getCreatorEventID() == DesignerEvent.CREATOR_DELETED
						|| evt.getCreatorEventID() == DesignerEvent.CREATOR_EDITED
						|| evt.getCreatorEventID() == DesignerEvent.CREATOR_RENAMED) {
					paraDefinitePane.setParameterArray(
							paraDefinitePane.getNoRepeatParas(DesignModelAdapter.getCurrentModelAdapter().getParameters()));
					paraDefinitePane.refreshParameter();
				}
			}
		});
	}

	/**
	 * ����һ��FormArea
	 *
	 * @return ����
	 */
	public Component createWrapper() {
		FormArea area = new FormArea(this, false);
		area.setBorder(BorderFactory.createEmptyBorder(0, 13, 0, 0));
		return area;
	}

	/**
	 * ˢ�¿ؼ�
	 */
	public void refreshAllNameWidgets() {
		XCreatorUtils.refreshAllNameWidgets(this.getRootComponent());
	}

	/**
	 * ˢ��tableData
	 *
	 * @param oldName ������
	 * @param newName  ������
	 */
	public void refresh4TableData(String oldName, String newName) {
		this.getTarget().renameTableData(oldName, newName);
		this.getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_SELECTED);
	}

	/**
	 * ˢ�²���
	 *
	 * @param p  �������
	 */
	public void refreshParameter(ParaDefinitePane p) {
		XLayoutContainer rootContainer = this.getRootComponent();
		java.util.List<String> namelist = getAllXCreatorNameList(rootContainer);
		// parameterArray�Ǳ�������в���, nameList���Ѿ��ڲ��������ӹ��ؼ��Ĳ�����
		// �����еĲ����б�Ƚ� ����Ѿ����� �ͳ�ȥ
		Parameter[] ps = p.getParameterArray();
		if (ps != null) {
			for (Parameter parameter : ps) {
				for (String name : namelist) {
					if (name.equalsIgnoreCase(parameter.getName())) {
						p.setParameterArray((Parameter[]) ArrayUtils.removeElement(p.getParameterArray(), parameter));
					}
				}
			}
		}
		ParameterPropertyPane.getInstance().getParameterToolbarPane().populateBean(
				p.getParameterArray() == null ? new Parameter[0] : p.getParameterArray());
		ParameterPropertyPane.getInstance().repaintContainer();
	}

	/**
	 * �ж������������Ƿ�û�пؼ�
	 *
	 * @return   ��������Ƿ�û�пؼ�
	 */
	public boolean isBlank() {
		XLayoutContainer rootContainer = this.getRootComponent();
		List<String> xx = getAllXCreatorNameList(rootContainer);
		return xx.isEmpty();
	}

    protected void setToolbarButtons(boolean flag) {
        DesignerContext.getDesignerFrame().checkCombineUp(!flag, NAME_ARRAY_LIST);
    }

	/**
	 * ������������еĿؼ��Ƿ��к�ģ�����ͬ����
	 *
	 * @param allParameters ����
	 * @return �Ƿ���ͬ��
	 */
	public boolean isWithoutParaXCreator(Parameter[] allParameters) {
		XLayoutContainer rootContainer = this.getRootComponent();
		List<String> xx = getAllXCreatorNameList(rootContainer);
		for (Parameter parameter : allParameters) {
			for (String name : xx) {
				if (name.equalsIgnoreCase(parameter.getName())) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * �������ؼ�������
	 *
	 * @return ����
	 */
	public List<String> getAllXCreatorNameList() {
		XLayoutContainer rootContainer = this.getRootComponent();
		List<String> namelist = new ArrayList<String>();
		for (int i = 0; i < rootContainer.getXCreatorCount(); i++) {
			if (rootContainer.getXCreator(i) instanceof XLayoutContainer) {
				namelist.addAll(getAllXCreatorNameList((XLayoutContainer) rootContainer.getXCreator(i)));
			} else {
				namelist.add(rootContainer.getXCreator(i).toData().getWidgetName());
			}
		}
		return namelist;
	}

	private List<String> getAllXCreatorNameList(XLayoutContainer rootContainer) {
		List<String> namelist = new ArrayList<String>();
		for (int i = 0; i < rootContainer.getXCreatorCount(); i++) {
			if (rootContainer.getXCreator(i) instanceof XLayoutContainer) {
				namelist.addAll(getAllXCreatorNameList((XLayoutContainer) rootContainer.getXCreator(i)));
			} else {
				namelist.add(rootContainer.getXCreator(i).toData().getWidgetName());
			}
		}
		return namelist;
	}

	/**
	 * �Ƿ��в�ѯ��ť
	 *
	 * @return   ���޲�ѯ��ť
	 */
	public boolean isWithQueryButton() {
		XLayoutContainer rootContainer = this.getRootComponent();
		return SearchQueryCreators(rootContainer);
	}

    /**
     * ���ظ���ճ��ɾ���ȶ���
     * @return ͬ��
     */
    public Action[] getActions() {
        if (designer_actions == null) {
            designer_actions = new Action[]{new CutAction(this), new CopyAction(this), new PasteAction(this),
                    new FormDeleteAction(this)};
        }
        return designer_actions;
    }

	private boolean SearchQueryCreators(XLayoutContainer rootContainer) {
		boolean b = false;
		for (int i = 0; i < rootContainer.getXCreatorCount(); i++) {
			if (rootContainer.getXCreator(i) instanceof XLayoutContainer) {
				b = SearchQueryCreators((XLayoutContainer) rootContainer.getXCreator(i));
			} else if (rootContainer.getXCreator(i) instanceof XFormSubmit) {
				b = true;
			}
		}
		return b;
	}

	/**
	 * ����getTarget Ϊ�˷���ParameterUI�ӿڶ�����ͻ��д��
	 *
	 * @return
	 */
	public ParameterUI getParaTarget() {
		return (FormParameterUI) super.getTarget();
	}

	/**
	 * ParameterDefinitePaneͨ��ParaDesigner������ParameterPropertyPane
	 *
	 * @param p ���
	 */
	public void populateParameterPropertyPane(ParaDefinitePane p) {
		ParameterPropertyPane.getInstance().populateBean(p);
	}

    /**
     *  ��ʼ��
     */
	public void initWidgetToolbarPane() {
		WidgetToolBarPane.getInstance(this);
	}

	/**
	 * populate
	 *
	 * @param ui
	 */
	public void populate(ParameterUI ui) {
		if (ui == null) {
			return;
		}
		if (this.getTarget() == ui) {
			repaint();
			return;
		}
		this.setTarget((FormParameterUI) ui.convert());
		this.refreshRoot();
	}

    /**
     * ����ֱ���жϵײ��Ƿ��Ǿ��Բ���
     * @return ���򷵻�true
     */
    public boolean hasWAbsoluteLayout() {
        return this.getTarget().getContainer() instanceof WAbsoluteLayout;
    }

    /**
     * ˢ�µײ�����
     */
    public void refreshRoot() {
        XLayoutContainer layoutContainer = (XLayoutContainer) XCreatorUtils.createXCreator(this.getTarget()
                .getContainer());
        if (layoutContainer == null) {
            layoutContainer = new XWParameterLayout();
        }
        layoutContainer.setSize(LARGE_PREFERRED_SIZE);
        setRootComponent(layoutContainer);
    }

    /**
     * �Ƿ��Ǳ���Ĳ������
     * @return     ��
     */
    public boolean isFormParaDesigner(){
        return true;
    }

    public XLayoutContainer getParaComponent() {
        return getRootComponent();
    }

	private void paintLinkParameters(Graphics clipg) {
		Parameter[] paras = DesignModelAdapter.getCurrentModelAdapter().getParameters();
		if (paras == null || paras.length == 0) {
			return;
		}
		Graphics g = clipg.create();
		g.setColor(Color.RED);
		if (!(this.getRootComponent() instanceof XWAbsoluteLayout)) {
			return;
		}
		XWAbsoluteLayout layout = (XWAbsoluteLayout) this.getRootComponent();
		for (int i = 0; i < layout.getXCreatorCount(); i++) {
			XCreator creator = layout.getXCreator(i);
			if (!creator.isVisible()) {
				continue;
			}
			for (Parameter p : paras) {
				if (p.getName().equalsIgnoreCase(creator.toData().getWidgetName())) {
					g.drawImage(paraImage, creator.getX() - 4, creator.getY() + 2, null);
					break;
				}
			}
		}
	}

	/**
	 * �õ����ʵĴ�С
	 *
	 * @return
	 */
	public Dimension getPreferredSize() {
		return getDesignSize();
	}

	public Dimension getDesignSize() {
		return ((FormParameterUI) getTarget()).getDesignSize();
	}

	/**
	 * ���ø߶�
	 *
	 * @param height
	 */
	public void setDesignHeight(int height) {
		Dimension dim = getPreferredSize();
		dim.height = height;
		((FormParameterUI) getTarget()).setDesignSize(dim);
	}

	/**
	 * paintContent
	 *
	 * @param clipg
	 */
	public void paintContent(Graphics clipg) {
		Dimension dim;
		dim = ((FormParameterUI) getTarget()).getDesignSize();
		getRootComponent().setSize(dim);
		getRootComponent().paint(clipg);
		paintLinkParameters(clipg);
		paintOp(clipg, getOutlineBounds());
	}

	private void paintOp(Graphics offg, Rectangle bounds) {
		Color oldColor = offg.getColor();
		Insets insets = getOutlineInsets();
		offg.setColor(XCreatorConstants.OP_COLOR);
		offg.fillRect(bounds.x, bounds.y + bounds.height, bounds.width + insets.right, insets.bottom);
		offg.fillRect(bounds.x + bounds.width, bounds.y, insets.right, bounds.height);
		offg.setColor(oldColor);
	}

	protected void setRootComponent(XLayoutContainer component) {
		component.setDirections(new int[]{Direction.BOTTOM, Direction.RIGHT});
		super.setRootComponent(component);
	}

	/**
	 * ˢ�³ߴ�
	 */
	public void populateRootSize() {
		((FormParameterUI) getTarget()).setDesignSize(getRootComponent().getSize());
		if (getParaComponent().acceptType(XWParameterLayout.class)) {
	        WParameterLayout layout = (WParameterLayout)getParaComponent().toData();
	        layout.setDesignWidth(getRootComponent().getWidth());
		}
	}

	/**
	 * �����������Ŀ��
	 *
	 * @param width ָ���Ŀ��
	 */
	public void updateWidth(int width) {
		FormParameterUI parameterUI = ((FormParameterUI) getTarget());
		parameterUI.setDesignSize(new Dimension(width, parameterUI.getDesignSize().height));
	}

	/**
	 * �����������ĸ߶�
	 *
	 * @param height ָ���ĸ߶�
	 */
	public void updateHeight(int height) {
		FormParameterUI parameterUI = ((FormParameterUI) getTarget());
		parameterUI.setDesignSize(new Dimension(parameterUI.getDesignSize().width, height));
	}

	/**
	 * �ڲ����ܶ�ʱ��ȫ����ӵ�ʱ�򣬿�������һ���Ű棬��ȥ���ͻ��ڲ������ѵ�һ��
	 *
	 * @param creator ���   z
	 * @param x  ����
	 * @param y ����     c
	 * @param layout ����
	 * @return �Ƿ���չ
	 */
	public boolean prepareForAdd(XCreator creator, int x, int y, XWAbsoluteLayout layout) {
		// �������棬�Զ���չ
		if (!isRoot(layout)) {
			return false;
		}

		Dimension size = layout.getSize();
		Boolean needResize = false;

		if (creator.getWidth() / 2 + x > layout.getWidth()) {
			size.width = creator.getWidth() / 2 + x + ADD_HEIGHT;
			needResize = true;
		}
		if (creator.getHeight() / 2 + y > layout.getHeight()) {
			size.height = creator.getHeight() / 2 + y + ADD_HEIGHT;
			needResize = true;
		}
		if (needResize) {
			layout.setSize(size);
			populateRootSize();
		}
		return true;
	}

    /**
     * �������
     * @param parameter ����        c
     * @param currentIndex λ��   w
     * @return �Ƿ����   s
     */
	public boolean addingParameter2Editor(Parameter parameter, int currentIndex) {
		com.fr.form.ui.Label label = new com.fr.form.ui.Label();
		String name = parameter.getName();
		label.setWidgetName("Label" + name);
		label.setWidgetValue(new WidgetValue(name + ":"));
		XCreator xCreator = XCreatorUtils.createXCreator(label);
		if (!(this.autoAddComponent(xCreator, H_COMPONENT_GAP * (currentIndex % NUM_IN_A_LINE)
				+ FIRST_H_LOCATION, FIRST_V_LOCATION + V_COMPONENT_GAP * (currentIndex / NUM_IN_A_LINE)))) {
			return false;
		}
		EditorHolder editor = new EditorHolder(parameter);
		xCreator = XCreatorUtils.createXCreator(editor);
		if (!(this.autoAddComponent(xCreator, H_COMPONENT_GAP * (currentIndex % NUM_IN_A_LINE)
				+ SECOND_H_LOCATION, FIRST_V_LOCATION + V_COMPONENT_GAP * (currentIndex / NUM_IN_A_LINE)))) {
			return false;
		}
		return true;
	}


    /**
     * �������
     * @param parameter ����        c
     * @param currentIndex λ��   w
     * @return �Ƿ����   s
     */
	public boolean addingParameter2EditorWithQueryButton(Parameter parameter, int currentIndex) {
		com.fr.form.ui.Label label = new com.fr.form.ui.Label();
		String name = parameter.getName();
		label.setWidgetName("Label" + name);
		label.setWidgetValue(new WidgetValue(name + ":"));
		XCreator xCreator = XCreatorUtils.createXCreator(label);
		if (!(this.autoAddComponent(xCreator, FIRST_H_LOCATION, FIRST_V_LOCATION + V_COMPONENT_GAP
				* (currentIndex / NUM_IN_A_LINE)))) {
			return false;
		}
		EditorHolder editor = new EditorHolder(parameter);
		editor.setWidgetName(name);
		xCreator = XCreatorUtils.createXCreator(editor);
		if (!(this.autoAddComponent(xCreator, SECOND_H_LOCATION, FIRST_V_LOCATION + V_COMPONENT_GAP
				* (currentIndex / NUM_IN_A_LINE)))) {
			return false;
		}
		FormSubmitButton formSubmitButton = new FormSubmitButton();
		formSubmitButton.setWidgetName("Search");
		formSubmitButton.setText(Inter.getLocText("FR-Designer_Query"));
		xCreator = XCreatorUtils.createXCreator(formSubmitButton);
		if (!(this.autoAddComponent(xCreator, 270, FIRST_V_LOCATION + V_COMPONENT_GAP
				* (currentIndex / NUM_IN_A_LINE)))) {
			return false;
		}
		return true;
	}

    /**
     * �������
     * @param parameterArray  ����        c
     * @param currentIndex λ��   w
     * @return �Ƿ����   s
     */
	public void addingAllParameter2Editor(Parameter[] parameterArray, int currentIndex) {
		for (int i = 0; i < parameterArray.length; i++) {
			com.fr.form.ui.Label label = new com.fr.form.ui.Label();
			label.setWidgetName("Label" + parameterArray[i].getName());
			label.setWidgetValue(new WidgetValue(parameterArray[i].getName() + ":"));
			XCreator xCreator = XCreatorUtils.createXCreator(label);

			if (!(this.autoAddComponent(xCreator, H_COMPONENT_GAP * (currentIndex % NUM_IN_A_LINE)
					+ FIRST_H_LOCATION, FIRST_V_LOCATION + V_COMPONENT_GAP * (currentIndex / NUM_IN_A_LINE)))) {
				break;
			}
			// ÿ����ʾ5��
			EditorHolder editor = new EditorHolder(parameterArray[i]);
			editor.setWidgetName(parameterArray[i].getName());
			xCreator = XCreatorUtils.createXCreator(editor);
			if (!(this.autoAddComponent(xCreator, H_COMPONENT_GAP * (currentIndex % NUM_IN_A_LINE)
					+ SECOND_H_LOCATION, FIRST_V_LOCATION + V_COMPONENT_GAP * (currentIndex / NUM_IN_A_LINE)))) {
				break;
			}
			currentIndex++;
		}
		if (!isWithQueryButton()) {
			FormSubmitButton formSubmitButton = new FormSubmitButton();
			formSubmitButton.setWidgetName("Search");
			formSubmitButton.setText(Inter.getLocText("FR-Designer_Query"));
			XCreator xCreator = XCreatorUtils.createXCreator(formSubmitButton);
			if (!(this.autoAddComponent(xCreator, H_COMPONENT_GAP * 3 + H_GAP, FIRST_V_LOCATION
					+ V_COMPONENT_GAP * (currentIndex / NUM_IN_A_LINE)))) {
				return;
			}
		}
	}

	/**
	 * �Զ����
	 *
	 * @param xCreator  ���       z
	 * @param x λ��    w
	 * @param y  λ��
	 * @return �Ƿ����   s
	 */
	public boolean autoAddComponent(XCreator xCreator, int x, int y) {
		XWAbsoluteLayout layout = (XWAbsoluteLayout) this.getRootComponent();
		FRAbsoluteLayoutAdapter adapter = (FRAbsoluteLayoutAdapter) layout.getLayoutAdapter();
		if (prepareForAdd(xCreator, x, y, layout)) {
			adapter.addBean(xCreator, x, y);
		}
		this.getSelectionModel().setSelectedCreator(xCreator);
		repaint();
		return true;
	}

    /**
     * ������
     * @return ���������      g
     */
	public JPanel[] toolbarPanes4Form() {
		return new JPanel[]{FormParaPane.getInstance(this)};
	}

    /**
     * ���ƵȰ�ť
     * @return ��ť�� a
     */
	public JComponent[] toolBarButton4Form() {
		return new JComponent[]{new CutAction(this).createToolBarComponent(), new CopyAction(this).createToolBarComponent(), new PasteAction(this).createToolBarComponent(),
				new FormDeleteAction(this).createToolBarComponent()};
	}
}
