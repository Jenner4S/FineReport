package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.base.Parameter;
import com.fr.design.DesignState;
import com.fr.design.designer.TargetComponent;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.Painter;
import com.fr.design.designer.beans.actions.FormDeleteAction;
import com.fr.design.designer.beans.adapters.layout.FRParameterLayoutAdapter;
import com.fr.design.designer.beans.events.CreatorEventListenerTable;
import com.fr.design.designer.beans.events.DesignerEditListener;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.designer.beans.location.Location;
import com.fr.design.designer.beans.location.RootResizeDirection;
import com.fr.design.designer.beans.models.AddingModel;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.beans.models.StateModel;
import com.fr.design.designer.creator.*;
import com.fr.design.designer.properties.FormWidgetAuthorityEditPane;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.parameter.ParaDefinitePane;
import com.fr.design.parameter.ParameterPropertyPane;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;
import com.fr.design.utils.ComponentUtils;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.FormElementCaseContainerProvider;
import com.fr.form.FormElementCaseProvider;
import com.fr.form.main.Form;
import com.fr.form.parameter.FormSubmitButton;
import com.fr.form.ui.EditorHolder;
import com.fr.form.ui.PaddingMargin;
import com.fr.form.ui.Widget;
import com.fr.form.ui.WidgetValue;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.form.ui.container.WFitLayout;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.bridge.StableFactory;
import edu.emory.mathcs.backport.java.util.Arrays;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * ��ƽ��������������ǽ�����ƹ��ߵĺ��ģ���Ҫ������Ǳ���ƽ������ʾ��������Ʋ���״̬�� ��ʾ���༭״̬����ʾ�ȵȡ�
 */
public class FormDesigner extends TargetComponent<Form> implements TreeSelectionListener, InvocationHandler, BaseFormDesigner ,ParaDefinitePane{
    protected static final ArrayList<String> NAME_ARRAY_LIST = new ArrayList<String>(
            Arrays.asList(new String[]{Inter.getLocText("M_Edit-Cut"), Inter.getLocText("M_Edit-Copy"), Inter.getLocText("M_Edit-Delete")})
    );
    private static final int BORDER_WIDTH = 6;
    //�ײ�������Ĭ�ϴ�С
    protected static final Dimension LARGE_PREFERRED_SIZE = new Dimension(WBorderLayout.DEFAULT_WIDTH, WBorderLayout.DEFAULT_HEIGHT);
    private int paraHeight = 0;
    /**
     * ��ǰ������Ƶ�������ĸ��ڵ㡣Ŀǰֻ֧��JPanel��Ϊ���ڵ㡣���Ժ����׵��޸�ʹ��֧������
     * ����������Ƶ������name���Զ���Ϊ�գ���ֵΪ������ı������ơ�
     */
    private XLayoutContainer rootComponent;
    private XLayoutContainer paraComponent;
    private boolean drawLineMode;
    private FormArea formArea;
    private ConnectorHelper ConnectorHelper;
    private boolean isReportBlockEditing = false;

    /**
     * ����ı������Ƿ����л���Ա������¼���״̬��ֻ��Ϊ���ʱ��ʱ״̬ʹ�á�
     */
    // �༭״̬ʱ��괦����
    private transient EditingMouseListener editingMouseListener;
    // �༭״̬�µ�model���洢�༭״̬�µ���ʱ״̬��������ק��������ȵ�ȵ�
    private transient StateModel stateModel;
    // ���״̬�µ�model���洢���״̬�µ���ʱ״̬������Ҫ��ӵ��������ǰ���λ�õȵ�
    private transient AddingModel addingModel;
    // ��ǰ���������Ⱦ��painter����ҪĿ��������Ⱦ��������λ����ʾ����ͨ�����ⲿ�����ã���
    // �������Ⱦʱ��������Ⱦ��Щλ����ʾ��
    private transient Painter painter;
    // �洢��ѡ������ͼ��а��model
    private transient SelectionModel selectionModel;

    // �༭״̬���¼���
    private CreatorEventListenerTable edit;
    protected Action[] designer_actions;
    private FormDesignerModeForSpecial<?> desigerMode;
    private Action switchAction;
    private FormElementCaseContainerProvider elementCaseContainer;
    private Parameter[] parameterArray;
    //������Ӳ���λ��
    private int currentIndex;
    private static final int NUM_IN_A_LINE = 4;
    private static final int H_COMPONENT_GAP = 165;
    private static final int V_COMPONENT_GAP = 25;
    private static final int FIRST_V_LOCATION = 35;
    private static final int FIRST_H_LOCATION = 90;
    private static final int SECOND_H_LOCATION = 170;
    private static final int ADD_HEIGHT = 20;
    private static final int H_GAP = 105;

    public FormDesigner(Form form) {
    	this(form, null);
    }
    
    public FormDesigner(Form form, Action switchAction) {
        super(form);
        setDoubleBuffered(true);
        // Ϊ�˴�������¼�����ҪFormDesigner�ܹ���ȡ����
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        this.setOpaque(true);
        this.setBackground(Color.WHITE);

        // ��ʼ��
        edit = new CreatorEventListenerTable();
        selectionModel = new SelectionModel(this);
        stateModel = new StateModel(this);
        desigerMode = createFormDesignerTargetMode();
        updateUI();// ��ʼ��������ƹ��ߵ�UIʵ��
        refreshRoot();// ��ʼ��ȱʡ��������
        initializeListener();// ��ʼ���¼�������

        new FormDesignerDropTarget(this);// ���Drag and Drop.
        
        this.switchAction = switchAction;
        populateParameterPropertyPane();
    }

    /**
     * ˢ�¿ؼ������
     */
    public void populateParameterPropertyPane() {
        //����
        setParameterArray(getNoRepeatParas(getTarget().getParameters()));
        refreshParameter();
        //����
        ParameterPropertyPane.getInstance().populateBean(this);
    }

    public Parameter[] getNoRepeatParas(Parameter[] paras){
        List<Parameter> paraList = new ArrayList<Parameter>();
        java.util.Set set = new java.util.HashSet();
        for (Parameter p : paras) {
            if (!set.contains(p.getName().toLowerCase())) {
                paraList.add(p);
                set.add(p.getName().toLowerCase());
            }
        }
        return paraList.toArray(new Parameter[paraList.size()]);
    }

    public void setParameterArray(Parameter[] ps){
        parameterArray = ps;
    }


    public Parameter[] getParameterArray(){
        return parameterArray;
    }

    /**
     * ˢ�²���
     */
    public void refreshParameter(){
        XLayoutContainer rootContainer = this.getParaComponent();
        if (rootContainer != null){
        java.util.List<String> namelist = new ArrayList<String>();
        rootContainer.getAllXCreatorNameList(rootContainer,namelist);
        // parameterArray�Ǳ�������в���, nameList���Ѿ��ڲ��������ӹ��ؼ��Ĳ�����
        // �����еĲ����б�Ƚ� ����Ѿ����� �ͳ�ȥ
        Parameter[] ps = getParameterArray();
        if (ps != null) {
            removeSame(ps, namelist);
        }
        }
        ParameterPropertyPane.getInstance().getParameterToolbarPane().populateBean(
                getParameterArray() == null ? new Parameter[0] : getParameterArray());
        ParameterPropertyPane.getInstance().repaintContainer();
    }

   private void removeSame(Parameter[] parameters, List<String> namelist){
       for (Parameter parameter : parameters) {
           for (String name : namelist) {
               if (name.equalsIgnoreCase(parameter.getName())) {
                   setParameterArray((Parameter[]) ArrayUtils.removeElement(getParameterArray(), parameter));
               }
           }
       }
   }


    /**
     * �Ƿ��в�ѯ��ť
     * @return   ���޲�ѯ��ť
     */
    public boolean isWithQueryButton(){
        XLayoutContainer rootContainer = this.getParaComponent();
        return rootContainer != null && rootContainer.SearchQueryCreators(rootContainer);
    }

    /**
     * ����������������
     * @param parameter ����
     * @return �Ƿ����
     */
    public void addingParameter2Editor(Parameter parameter){
        if(getParaComponent() == null){
            addParaPaneTooltips();
            return;
        }
        com.fr.form.ui.Label label = new com.fr.form.ui.Label();
        String name = parameter.getName();
        label.setWidgetName("Label" + name);
        label.setWidgetValue(new WidgetValue(name + ":"));
        XCreator xLabel = XCreatorUtils.createXCreator(label);

        EditorHolder editor = new EditorHolder(parameter);
        XCreator xCreator = XCreatorUtils.createXCreator(editor);

        if (!(this.autoAddComponent(xLabel, H_COMPONENT_GAP * (currentIndex % NUM_IN_A_LINE)
                + FIRST_H_LOCATION, FIRST_V_LOCATION + V_COMPONENT_GAP * (currentIndex / NUM_IN_A_LINE)))) {
            return ;
        }
        if (!(this.autoAddComponent(xCreator, H_COMPONENT_GAP * (currentIndex % NUM_IN_A_LINE)
                + SECOND_H_LOCATION, FIRST_V_LOCATION + V_COMPONENT_GAP * (currentIndex / NUM_IN_A_LINE)))) {
            return ;
        }
        currentIndex++;
        parameterArray = (Parameter[]) ArrayUtils.removeElement(parameterArray, parameter);
        refreshParameter();
        EastRegionContainerPane.getInstance().refreshDownPane();
    }

    /**
     * ���������������壬�в�ѯ��ť
     * @param parameter ����
     * @return �Ƿ����
     */
    public void addingParameter2EditorWithQueryButton(Parameter parameter){
        if(getParaComponent() == null){
            addParaPaneTooltips();
            return;
        }
        com.fr.form.ui.Label label = new com.fr.form.ui.Label();
        String name = parameter.getName();
        label.setWidgetName("Label" + name);
        label.setWidgetValue(new WidgetValue(name + ":"));
        XCreator xLabel = XCreatorUtils.createXCreator(label);

        EditorHolder editor = new EditorHolder(parameter);
        editor.setWidgetName(name);
        XCreator xCreator = XCreatorUtils.createXCreator(editor);

        if (!(this.autoAddComponent(xLabel, FIRST_H_LOCATION, FIRST_V_LOCATION + V_COMPONENT_GAP
                * (currentIndex / NUM_IN_A_LINE)))) {
            return ;
        }

        if (!(this.autoAddComponent(xCreator, SECOND_H_LOCATION, FIRST_V_LOCATION + V_COMPONENT_GAP
                * (currentIndex / NUM_IN_A_LINE)))) {
            return ;
        }
        FormSubmitButton formSubmitButton = new FormSubmitButton();
        formSubmitButton.setWidgetName("Search");
        formSubmitButton.setText(Inter.getLocText("FR-Designer_Query"));
        xCreator = XCreatorUtils.createXCreator(formSubmitButton);
        if (!(this.autoAddComponent(xCreator, 270, FIRST_V_LOCATION + V_COMPONENT_GAP
                * (currentIndex / NUM_IN_A_LINE)))) {
            return ;
        }
        currentIndex = currentIndex + NUM_IN_A_LINE - currentIndex % NUM_IN_A_LINE;
        parameterArray = (Parameter[]) ArrayUtils.removeElement(parameterArray, parameter);
        refreshParameter();
        EastRegionContainerPane.getInstance().refreshDownPane();
    }

    /**
     * һ��������в���
     */
    public void addingAllParameter2Editor(){
        if(getParaComponent() == null){
            addParaPaneTooltips();
            return;
        }
        if (parameterArray == null) {
            return;
        }

        for (int i = 0; i < parameterArray.length; i++) {
            com.fr.form.ui.Label label = new com.fr.form.ui.Label();
            label.setWidgetName("Label" + parameterArray[i].getName());
            label.setWidgetValue(new WidgetValue(parameterArray[i].getName() + ":"));
            XCreator xLabel = XCreatorUtils.createXCreator(label);

            // ÿ����ʾ5��
            EditorHolder editor = new EditorHolder(parameterArray[i]);
            editor.setWidgetName(parameterArray[i].getName());
            XCreator xCreator = XCreatorUtils.createXCreator(editor);

            if (!(this.autoAddComponent(xLabel, H_COMPONENT_GAP * (currentIndex % NUM_IN_A_LINE)
                    + FIRST_H_LOCATION, FIRST_V_LOCATION + V_COMPONENT_GAP * (currentIndex / NUM_IN_A_LINE)))) {
                break;
            }

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

        parameterArray = null;
        refreshParameter();
        //��֪��Ϊʲô����������ؼ���ֻ��һ��label�����ˢ��һ�¿ؼ�������
        EastRegionContainerPane.getInstance().refreshDownPane();
    }

    private void addParaPaneTooltips(){
        JOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(),Inter.getLocText("FR-Designer-Form-Please_Drag_ParaPane"),
                Inter.getLocText("FR-Designer_Tooltips"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * �Զ�������
     * @param xCreator  ���
     * @param x ������
     * @param y  ������
     * @return �Ƿ���ӳɹ�
     */
    public boolean autoAddComponent(XCreator xCreator, int x, int y) {
        XWParameterLayout layout = (XWParameterLayout) this.getParaComponent();
        FRParameterLayoutAdapter adapter = (FRParameterLayoutAdapter) layout.getLayoutAdapter();
        if (prepareForAdd(xCreator, x, y, layout)) {
            adapter.addBean(xCreator, x, y);
        }
        this.getSelectionModel().setSelectedCreator(xCreator);
        repaint();
        return true;
    }

    /**
     * �ڲ����ܶ�ʱ��ȫ����ӵ�ʱ�򣬿�������һ���Ű棬��ȥ���ͻ��ڲ������ѵ�һ��
     * @param creator ���
     * @param x  ����
     * @param y ����
     * @param layout ����
     * @return �Ƿ���չ
     */
    public boolean prepareForAdd(XCreator creator, int x, int y, XWParameterLayout layout) {
        // �������棬�Զ���չ
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
            setParaHeight(size.height);
        }
        return true;
    }

    /**
     * ����������
     */
    public void addParaComponent(){
    	if (paraComponent != null) {
    		return ;
    	}
    	paraHeight = WBorderLayout.DEFAULT_SIZE;
        paraComponent = new XWParameterLayout();
        paraComponent.toData().setWidgetName("para");
        paraComponent.setSize(paraComponent.initEditorSize());
        XWBorderLayout formLayoutContainer = (XWBorderLayout) rootComponent.getParent();
        formLayoutContainer.toData().setNorthSize(paraHeight);
        formLayoutContainer.add(paraComponent,WBorderLayout.NORTH);
        //����northSize������para������border��С����ʱ��para��root�Ĵ�С������Ӧ����
        formLayoutContainer.setSize(formLayoutContainer.getWidth(), formLayoutContainer.getHeight() + paraHeight);
        selectionModel.reset();
        selectionModel.setSelectedCreator(paraComponent);
        invalidateLayout();
        populateParameterPropertyPane();
    }

    /**
     *  ���ظ��ڵ㸸����
     * @return  ������
     */
    public Component getTopContainer(){
    	 if(rootComponent != null){
    		 // ����root���ڵĸ���������designer
    	     return LayoutUtils.getTopContainer(rootComponent);
    	 }
        return  XCreatorUtils.createXCreator(this.getTarget().getContainer());
    }

    /**
     * ���ز�������߶� 
     * @return  para�߶�
     */
    public int getParaHeight(){
        return paraHeight;
    }

    /**
     * ����para�ĸ߶�
     * @param height  �߶�
     */
    public void setParaHeight(int height){
    	XWBorderLayout container = (XWBorderLayout) getTopContainer();
    	container.toData().setNorthSize(height);
    	container.setSize(container.getWidth(), container.getHeight() + height - getParaHeight());
        paraHeight = height;
    }

    /**
     * ɾ����������
     */
    public void removeParaComponent(){
        XWBorderLayout formLayoutContainer = (XWBorderLayout) getTopContainer();
        formLayoutContainer.toData().removeWidget(paraComponent.toData());
        paraHeight = 0;
        paraComponent = null;
        formLayoutContainer.setSize(rootComponent.getWidth(), rootComponent.getHeight());
        EastRegionContainerPane.getInstance().replaceDownPane(this.getEastDownPane());
        //ɾ�����ػ���
        invalidateLayout();
    }

    /**
     * �л�
     * @param elementCaseContainer       ����
     */
    public void switchTab(FormElementCaseContainerProvider elementCaseContainer){
    	if(this.switchAction == null){
    		return;
    	}
    	this.elementCaseContainer = elementCaseContainer;
    	this.switchAction.actionPerformed(null);
    }
    
    public void setElementCaseContainer(FormElementCaseContainerProvider elementCaseContainer){
    	this.elementCaseContainer = elementCaseContainer;
    }
    
    public FormElementCaseProvider getElementCase(){
    	return this.elementCaseContainer.getElementCase();
    }

    public String getElementCaseContainerName(){
    	return this.elementCaseContainer.getElementCaseContainerName();
    }
    
    public void setElementCase(FormElementCaseProvider elementCase){
    	this.elementCaseContainer.setElementCase(elementCase);
    }
    
    public void setElementCaseBackground(BufferedImage image){
    	this.elementCaseContainer.setBackground(image);
    }
    
    public Dimension getElementCaseContainerSize(){
    	return this.elementCaseContainer.getSize();
    }
    
    public FormElementCaseContainerProvider getElementCaseContainer(){
    	return this.elementCaseContainer;
    }

    protected FormDesignerModeForSpecial<?> createFormDesignerTargetMode() {
        return new FormTargetMode(this);
    }

    public FormDesignerModeForSpecial<?> getDesignerMode() {
        return this.desigerMode;
    }

    public CreatorEventListenerTable getEditListenerTable() {
        return edit;
    }

    /**
     * ���Ӽ����¼�
     * @param listener ��������༭�¼�
     */
    public void addDesignerEditListener(DesignerEditListener listener) {
        getEditListenerTable().addListener(listener);
    }

    /**
     * ȡ����ʽ
     */
    public void cancelFormat() {
        return;
    }

    /**
     * �����жϲ�������Ƿ�Ϊ���Բ���
     * @return ���򷵻�true
     */
    public boolean hasWAbsoluteLayout() {
        if (paraComponent != null && paraComponent.acceptType(XWParameterLayout.class)){
            return true;
        }
        return false;
    }

    /**
     *  �����Ƿ�Ϊ�����༭
     * @param isEditing �Ƿ�Ϊ�����༭
     */
    public void setReportBlockEditing(boolean isEditing) {
        this.isReportBlockEditing = isEditing;
    }

    /**
     * �Ƿ�Ϊ�����༭
     * @return �Ƿ�Ϊ�����༭
     */
    public boolean isReportBlockEditing() {
        return this.isReportBlockEditing;
    }

    /**
     * �Ƿ��������ؼ�
     * @param creator ���
     * @param newName �µ������
     * @return  ������б仯���Ҳ�������һ������true
     */
    public boolean renameCreator(XCreator creator, String newName) {
        if (ComparatorUtils.equals(creator.toData().getWidgetName(), newName)) {
            return false;
        }
        if (this.getTarget().isNameExist(newName)) {
            JOptionPane.showMessageDialog(this, "\"" + newName + "\"" + Inter.getLocText("Utils-has_been_existed")
                    + "!", Inter.getLocText("FR-Designer_Alert"), JOptionPane.WARNING_MESSAGE);
            return false;
        }
        creator.resetCreatorName(newName);
        getEditListenerTable().fireCreatorModified(creator, DesignerEvent.CREATOR_EDITED);
        return true;
    }

    /**
     * �����������Ŀ��
     *
     * @param width ָ���Ŀ��
     */
    public void updateWidth(int width) {
       //TODO
    }

    /**
     * ���½��沼�֣��ػ�
     * @param proxy ��̬������
     * @param method �ӿڷ���
     * @param args ����
     * @return �������κζ���
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    	if(rootComponent != null){
			LayoutUtils.layoutRootContainer(rootComponent);
		}
		if(paraComponent != null){
		  	LayoutUtils.layoutRootContainer(paraComponent);
		 }
        repaint();
        return null;
    }

    /**
     * ��ʼ���¼�����������ʼ״̬Ϊ�༭״̬�������³�ʼ������ӱ༭����¼�������
     */
    private void initializeListener() {
        addKeyListener(new FormEditorKeyListener(this));
        // ���
        editingMouseListener = new EditingMouseListener(this);
        addMouseMotionListener(editingMouseListener);
        addMouseListener(editingMouseListener);
        addInvocationHandler(this);

        getEditListenerTable().addListener(new DesignerEditListener() {

            @Override
            public void fireCreatorModified(DesignerEvent evt) {
            	// ֻ��ѡ�����ʱ������ģ����£�������Ҫ����
                if (evt.getCreatorEventID() != DesignerEvent.CREATOR_SELECTED) {
                    FormDesigner.this.fireTargetModified();
                    //bug59192
                    //setParameterArray(getNoRepeatParas(getTarget().getParameters()));
                    //refreshParameter();
                }
            }

        });
    }

    /**
     * ��������¼�
     * @param h ��̬����
     */
    public void addInvocationHandler(InvocationHandler h) {
        ClassLoader loader = getClass().getClassLoader();
        Class[] interfaces = new Class[]{DesignerEditListener.class};
        Object proxyListener = Proxy.newProxyInstance(loader, interfaces, h);
        addDesignerEditListener((DesignerEditListener) proxyListener);
        addDesignerEditListener(new FormWidgetAuthorityEditAdapter());
    }

    /**
     * ֹͣ���ģʽ�����ر༭ģʽ
     */
    public void stopAddingState() {
        // �ָ�Ϊ�գ�UI�����addingModel�Ƿ�վ����Ƿ�ֹͣ��ȾҪ��ӵ����
        addingModel = null;
        painter = null;
        // DropTarget��Ȼ��addingMouseListener,�ĳ����,����Դ��ק��
        new FormDesignerDropTarget(this);
        // ����ֹͣ���ģʽ���¼�
        repaint();
    }

    /**
     *  ������UI��ΪDesignerUI��������Ⱦ
     */
    @Override
    public void updateUI() {
        setUI(new FormDesignerUI());
    }

    /**
     * ����ק����ѡ��ʽ����ͷ�ʱ���ô˺�����������ѡ������
     *
     * @param e ��ǰ����¼�����������ʼ�㹹��ѡ��򣬼��㱻Ȧ������
     */
    public void selectComponents(MouseEvent e) {
        // ����stateModel��selectComponent���±�ѡ��������stateModel��������ק��ʼ��
        stateModel.selectCreators(e);
        // ���stateModelΪ����ק״̬
        stateModel.reset();
        repaint();
    }

    /**
     * ��root����ݹ����x,y���ڵ������ע�������ڱ���Ƶ�����������name���Ա��벻Ϊ��
     */
    private XCreator xCreatorAt(int x, int y, XCreator root, XCreator[] except) {
    	if (root == null || !root.isVisible()) {
            return null;
        }
        x -= root.getX();
        y -= root.getY();

        if (root instanceof XLayoutContainer) {
            XLayoutContainer rootContainer = (XLayoutContainer) root;
            int count = rootContainer.getXCreatorCount();
            for (int i = 0; i < count; i++) {
                XCreator child = rootContainer.getXCreator(i);
                if (ArrayUtils.contains(except, child)) {
                    continue;
                }
                // ֻ��name��Ϊ�յ��������������Χ������ݹ����ݵ���
                XCreator dest = xCreatorAt(x, y, child, except);

                if (dest != null && !ArrayUtils.contains(except, child)) {
                    return dest;
                }
            }
        }

        Rectangle rect = ComponentUtils.computeVisibleRect(root);
        if (isIntersectArea(x, y, rect)) {
            // �ж��Ƿ��ڽ�������
            return root;
        }

        return null;
    }

    private boolean isIntersectArea(int x, int y, Rectangle rect) {
        return x >= rect.getX() && (x <= (rect.getX() + rect.getWidth())) && (y >= rect.getY())
                && (y <= (rect.getY() + rect.getHeight()));
    }

    /**
     * ���±߿���״̬
     * @param e ����¼�
     */
    public void updateDrawLineMode(MouseEvent e) {
        Point p = ConnectorHelper.getNearWidgetPoint(e);
        if (p == null) {
            XComponent comp = getComponentAt(e);
            if (comp == rootComponent) {
                p = new Point(e.getX() + formArea.getHorizontalValue(), e.getY() + formArea.getVerticalValue());
            }
        }
        stateModel.startDrawLine(p);
    }

    /**
     * ˢ�µײ�����
     */
    public void refreshRoot() {
    	// �����ָ���������refreshRoot����ʱ���target.getContainer���widget���֮ǰ��һ�������Բ���root�ж���ȡ
    	XLayoutContainer formLayoutContainer = (XLayoutContainer) XCreatorUtils.createXCreator(this.getTarget().getContainer());
        // ����Ĭ�϶���1���ײ��border��Ϊ0����Ȼû����
        this.getTarget().getContainer().setMargin(new PaddingMargin(0,0,0,0));
        formLayoutContainer.setBorder(null);
        if (formLayoutContainer.acceptType(XWBorderLayout.class)) {
        	WBorderLayout borderLayout = (WBorderLayout) formLayoutContainer.toData();

        	Widget northWidget = borderLayout.getLayoutWidget(WBorderLayout.NORTH);
        	Widget centerWidget = borderLayout.getLayoutWidget(WBorderLayout.CENTER);
            //�����У������������ټ�
            formLayoutContainer.removeAll();

            refreshNorth(northWidget, formLayoutContainer);
        	refreshCenter(centerWidget, formLayoutContainer);

        }  else {
            formLayoutContainer.setSize(LARGE_PREFERRED_SIZE);
            setRootComponent(formLayoutContainer);
        }
    }
    
    private void refreshNorth(Widget northWidget, XLayoutContainer formLayoutContainer) {
    		// ���û�в�������, ��ô�ʹ����¸߶��Լ���������İ�ťҪ����
    		if (northWidget == null) {
    			paraComponent = null;
    			paraHeight = 0;
    			FormParaWidgetPane.getInstance(this);
    			return;
    		}
    
    		XLayoutContainer northContainer = (XLayoutContainer) XCreatorUtils.createXCreator(northWidget);
    		paraHeight = ((XWBorderLayout)formLayoutContainer).toData().getNorthSize();
    		paraComponent = northContainer;
    		northContainer.setSize(0,paraHeight);
    		formLayoutContainer.add(northContainer, WBorderLayout.NORTH);
    	}
    
    	private void refreshCenter(Widget centerWidget, XLayoutContainer formLayoutContainer) {
    		// ������center��, ˵�����½���ģ��
    		if (centerWidget == null) {
    			XLayoutContainer layoutContainer = (XLayoutContainer) XCreatorUtils.createXCreator(new WFitLayout("body"));
    			layoutContainer.setSize(LARGE_PREFERRED_SIZE);
    			setRootComponent(layoutContainer);
    			formLayoutContainer.add(rootComponent, WBorderLayout.CENTER);
    			return;
    		}
    
    		XLayoutContainer centerContainer = (XLayoutContainer) XCreatorUtils.createXCreator(centerWidget);
    		Dimension d = new Dimension();
    		d.width = ((WFitLayout) centerWidget).getContainerWidth();
    		d.height = ((WFitLayout) centerWidget).getContainerHeight();
    		centerContainer.setSize(d);
    		formLayoutContainer.setSize(d.width, d.height + paraHeight);
    		setRootComponent(centerContainer);
    		// �ٴδ�ʱ��layout��root�����ڱ߾�Ļ��������
    		LayoutUtils.layoutContainer(centerContainer);
    		formLayoutContainer.add(rootComponent, WBorderLayout.CENTER);
    	}
    

    protected Insets getOutlineInsets() {
        return new Insets(10, 10, 10, 10);
    }

    public Painter getPainter() {
        return painter;
    }

    public void setPainter(Painter p) {
        painter = p;
    }

    public XLayoutContainer getRootComponent() {
        return rootComponent;
    }

    public XLayoutContainer getParaComponent() {
        return paraComponent;
    }

    /**
     * �Ƿ��Ǳ���Ĳ������
     * @return       ��(����)
     */
    public boolean isFormParaDesigner(){
        return false;
    }

    /**
     *  �Ƿ�Ϊ�ײ�����
     * @param comp ���
     * @return ���򷵻�true
     */
    public boolean isRoot(XCreator comp) {
        return comp == rootComponent;
    }

    // ��������¼�e��������λ����Ը������λ�ù�ϵ
    public Direction getLoc2Root(MouseEvent e) {
        int x = e.getX() + formArea.getHorizontalValue();
        int y = e.getY() + formArea.getVerticalValue();
        int width = rootComponent.getWidth();
        int height = rootComponent.getHeight();

        Insets insets = getOutlineInsets();
        if (x < width) {
            if ((y >= height) && (y <= (height + insets.bottom))) {
                return RootResizeDirection.BOTTOM_RESIZE;
            } else {
                return Location.outer;
            }
        } else if (x <= (width + insets.right)) {
            if ((y >= 0) && (y < height)) {
                return RootResizeDirection.RIGHT_RESIZE;
            } else if ((y >= height) && (y <= (height + insets.bottom))) {
                return RootResizeDirection.RIGHT_BOTTOM_RESIZE;
            } else {
                return Location.outer;
            }
        } else {
            return Location.outer;
        }
    }

    protected void setRootComponent(XLayoutContainer component) {
        this.rootComponent = component;
        component.setOpaque(true);
        component.setBackground(XCreatorConstants.FORM_BG);
        selectionModel.reset();
        // Ĭ�ϻ���ѡ��RootPane��
        selectionModel.setSelectedCreator(rootComponent);
        invalidateLayout();
    }

    public StateModel getStateModel() {
        return stateModel;
    }

    public AddingModel getAddingModel() {
        return addingModel;
    }

    public XCreator getComponentAt(MouseEvent e) {
        return getComponentAt(e.getX(), e.getY());
    }

    @Override
    public XCreator getComponentAt(Point p) {
        return getComponentAt(p.x, p.y);
    }

    @Override
    public XCreator getComponentAt(int x, int y) {
        return getComponentAt(x, y, null);
    }

    public XCreator getComponentAt(int x, int y, XCreator[] except) {
        XLayoutContainer container = y < paraHeight - formArea.getVerticalValue() ? paraComponent : rootComponent;
        XCreator comp = xCreatorAt(x + formArea.getHorizontalValue(), y + formArea.getVerticalValue(), container,
                except);
        return comp == null ? container : comp;
    }

    public SelectionModel getSelectionModel() {
        if (paraComponent!=null){
            paraComponent.setSize(paraComponent.getWidth(),getParaHeight());
            Rectangle rec = rootComponent.getBounds();
            rootComponent.setBounds(rec.x,getParaHeight(),rec.width,rec.height);
        }
        return selectionModel;
    }

    /**
     * �Ƴ�ѡ��״̬
     */
    public void removeSelection() {
        selectionModel.reset();
        this.repaint();
    }

    /**
     * ��ק׼��
     * @param xCreator ���
     */
    public void startDraggingBean(XCreator xCreator) {
        // ������ѡ��������BeanInfo������Ӧ��AddingModel
        // AddingModel��StateModel��һ�����ʺϵ�ǰѡ��������ص�
        addingModel = new AddingModel(this, xCreator);
        this.setDropTarget(new FormCreatorDropTarget(this));
        // ����״̬���ģʽ�¼�
        repaint();
    }

    /**
     *  ��קʱ��ش���
     * @param xCreator ���
     * @param lastPressEvent ����¼�
     * @param x ����x
     * @param y ����y
     */
    public void startDraggingComponent(XCreator xCreator, MouseEvent lastPressEvent, int x, int y) {
        // ������ѡ��������BeanInfo������Ӧ��AddingModel
        // AddingModel��StateModel��һ�����ʺϵ�ǰѡ��������ص�
    	int creatorWidth = xCreator.getWidth();
    	int creatorHeight = xCreator.getHeight();
        this.addingModel = new AddingModel(xCreator, x, y);
        TransferHandler handler = new DesignerTransferHandler(this, addingModel);
        setTransferHandler(handler);
        handler.exportAsDrag(this, lastPressEvent, TransferHandler.COPY);
        XCreator parent = XCreatorUtils.getParentXLayoutContainer(xCreator);
        selectionModel.removeCreator(xCreator, creatorWidth, creatorHeight);
        selectionModel.setSelectedCreator(parent);
        this.setDropTarget(new FormCreatorDropTarget(this));
        // ����״̬���ģʽ�¼�
        repaint();
    }

    /**
     * �ı����ֵ
     * @param e ���ѡ���¼�
     */
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        ComponentTree tree = (ComponentTree) e.getSource();
        TreePath[] paths = tree.getSelectionPaths();

        if (paths != null) {
            ArrayList<XCreator> selected = new ArrayList<XCreator>();

            for (TreePath path : paths) {
                selected.add((XCreator) path.getLastPathComponent());
            }

            if (!BaseUtils.isAuthorityEditing()) {

                selectionModel.setSelectedCreators(selected);

                TreePath path = e.getNewLeadSelectionPath();
                XCreator comp = (XCreator) path.getLastPathComponent();
                formArea.scrollPathToVisible(comp);
            } else {
                showAuthorityEditPane();
            }
            //��ѡ���ټ��
            setToolbarButtons(paths.length == 1 && tree.getSelectionPath().getParentPath() == null);
        }
    }

	/**
	 * ��ʾȨ�ޱ༭����
	 */
    public void showAuthorityEditPane() {
        HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().setAuthorityMode(true);
        if (isSupportAuthority()) {
            AuthorityPropertyPane authorityPropertyPane = new AuthorityPropertyPane(this);
            authorityPropertyPane.populate();
            EastRegionContainerPane.getInstance().replaceUpPane(authorityPropertyPane);
        } else {
            EastRegionContainerPane.getInstance().replaceUpPane(new NoSupportAuthorityEdit());
        }
        EastRegionContainerPane.getInstance().replaceDownPane(RolesAlreadyEditedPane.getInstance());
    }

    /**
	 * �Ƿ�֧��Ȩ�ޱ༭
	 * @return ���򷵻�true
	 */
    public boolean isSupportAuthority() {
        int size = getSelectionModel().getSelection().size();
        XCreator creator = size == 0 ? getRootComponent() : getSelectionModel().getSelection()
                .getSelectedCreator();
        return !(creator instanceof XLayoutContainer) && !(creator instanceof XChartEditor);
    }


    protected void setToolbarButtons(boolean flag) {
        //����Ӧ���ֺ͵ײ㶼����ɾ��
          DesignerContext.getDesignerFrame().checkCombineUp(!(isRoot(getSelectionModel().getSelection().getSelectedCreator()) || flag), NAME_ARRAY_LIST);
    }

    private void invalidateLayout() {
        Container parent = this.getArea();
        if (parent != null) {
            parent.doLayout();
            parent.repaint();
        }
    }

    /**
     *  �Ƿ���action��
     * @param name action��
     * @return ���򷵻�true
     */
    public boolean isRootRelatedAction(String name) {
        return NAME_ARRAY_LIST.contains(name);
    }

    /**
     * ��ʾ���
     * @param comp ���
     */
    public void makeVisible(XCreator comp) {
        XCreator parent = AdapterBus.getFirstInvisibleParent(comp);
        if (isRoot(parent)) {
            return;
        }
        while (parent != null) {
            XLayoutContainer parentContainer = XCreatorUtils.getParentXLayoutContainer(parent);

            if (parentContainer == null) {
                // parent.setVisible(true);
                break;
            } else {
                parentContainer.getLayoutAdapter().showComponent(parent);
                parent = AdapterBus.getFirstInvisibleParent(parent);
            }
        }
    }

    /**
     * ˢ�½�����Ⱦ����
     */
    public void refreshDesignerUI() {
        LayoutUtils.layoutRootContainer(getRootComponent());
        repaint();
    }

    /**
     * ���ظ���ճ��ɾ���ȶ���
     * @return ͬ��
     */
    public Action[] getActions() {
        if (designer_actions == null) {
           //�ȰѸ���ճ����ťȥ����ֻ����ɾ��
//            designer_actions = new Action[]{new CutAction(this), new CopyAction(this), new PasteAction(this),
//                    new FormDeleteAction(this)};
            designer_actions = new Action[]{new FormDeleteAction(this)};
        }
        return designer_actions;
    }

    protected Border getOuterBorder() {
        return XCreatorConstants.AREA_BORDER;
    }

    protected Rectangle getOutlineBounds() {
        Insets insets = getOuterBorder().getBorderInsets(this);
        int w = rootComponent.getWidth() + insets.left + insets.right;
        int h = rootComponent.getHeight() + insets.top + insets.bottom;
        return new Rectangle(0, 0, w, h);
    }

    /**
     * ͬ��
     */
    public void populateRootSize() {

    }
   
    /**
     * ���ر�����
     * @return ������
     */
    public FormArea getArea() {
        return formArea;
    }

    /**
     * �����ϲ�����
     * @param formArea ������
     */
    public void setParent(FormArea formArea) {
        this.formArea = formArea;
    }

    /**
     * ����������ڵ�
     * @param clipg ͼ��
     */
    public void paintContent(Graphics clipg) {
        rootComponent.paint(clipg);
    }

    public void paintPara(Graphics clipg){
        if(paraComponent != null){
            paraComponent.paint(clipg);
        }
    }

    /**
     * ��������߽�
     */
    public void resetEditorComponentBounds() {
        editingMouseListener.resetEditorComponentBounds();
    }

    /**
     * ����������
     * @return ConnectorHelper��
     */
    public ConnectorHelper getDrawLineHelper() {
        return ConnectorHelper;
    }

    /**
     *  �Ƿ���ģʽ
     * @return ���򷵻�true
     */
    public boolean isDrawLineMode() {
        return this.drawLineMode;
    }

    /**
     * ����DrawLineMode
     * @param mode ��or��
     */
    public void setDrawLineMode(boolean mode) {
        this.drawLineMode = mode;
    }

    /**
     * ��갴��λ��(evtX, evtY).
     *
     * @param evtX event x position ����
     * @param evtY event y position ����
     */
    public void doMousePress(double evtX, double evtY) {
        dispatchEvent(new MouseEvent(this, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, (int) evtX,
                (int) evtY, 1, false));
    }

    /**
     * TODO ALEX_SEP ��ʱ����ʲô��
     */
    @Override
    public void stopEditing() {
    }

    /**
     * ���ر��ؼ�Ȩ�ޱ༭pane
     * @return ͬ��
     */
    public AuthorityEditPane createAuthorityEditPane() {
        return new FormWidgetAuthorityEditPane(this);
    }

    public JPanel getEastUpPane() {
        return WidgetPropertyPane.getInstance(this);
    }

    public JPanel getEastDownPane() {
        final JPanel pane = new JPanel();
        if (EastRegionContainerPane.getInstance().getDownPane() == null) {
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        FRLogger.getLogger().error(e.getMessage(), e);
                    }

                    pane.setLayout(new BorderLayout());
                    pane.add(FormWidgetDetailPane.getInstance(FormDesigner.this), BorderLayout.CENTER);
                    EastRegionContainerPane.getInstance().replaceDownPane(pane);
                }
            }.start();
        } else {
            pane.setLayout(new BorderLayout());
            pane.add(FormWidgetDetailPane.getInstance(this), BorderLayout.CENTER);
            EastRegionContainerPane.getInstance().replaceDownPane(pane);
        }

        return pane;
    }


    public ToolBarMenuDockPlus getToolBarMenuDockPlus() {
    	return StableFactory.getMarkedInstanceObjectFromClass(BaseJForm.XML_TAG, ToolBarMenuDockPlus.class);
    }

    /**
     * ����
     */
    @Override
    public void copy() {
        selectionModel.copySelectedCreator2ClipBoard();
    }

    /**
     * ճ��
     * @return ��
     */
    @Override
    public boolean paste() {
        selectionModel.pasteFromClipBoard();
        return false;
    }

    /**
     * ����
     * @return ��
     */
    @Override
    public boolean cut() {
        selectionModel.cutSelectedCreator2ClipBoard();
        return false;
    }

    // ////////////////////////////////////////////////////////////////////
    // ////////////////for toolbarMenuAdapter//////////////////////////////
    // ////////////////////////////////////////////////////////////////////

    /**
     * �������˵�
     * @return ͬ��
     */
    @Override
    public MenuDef[] menus4Target() {
        return new MenuDef[0];
    }

    public int getMenuState() {
        return DesignState.JFORM;
    }

    /**
     * ģ��˵�
     * @return ͬ��
     */
    @Override
    public ShortCut[] shortcut4TemplateMenu() {
        return new ShortCut[0];
    }

    /**
     * Ȩ�޲˵�
     * @return ͬ��
     */
    public ShortCut[] shortCuts4Authority() {
        return new ShortCut[0];

    }

    /**
     * ����ToolBarDef
     * @return ͬ��
     */
    @Override
    public ToolBarDef[] toolbars4Target() {
        return new ToolBarDef[0];
    }

    /**
     * ���ع�������ť���
     * @return ͬ��
     */
    public JComponent[] toolBarButton4Form() {
        return new JComponent[0];
    }


    private class FormWidgetAuthorityEditAdapter implements DesignerEditListener {

        @Override
        public void fireCreatorModified(DesignerEvent evt) {
            if (!BaseUtils.isAuthorityEditing()) {
                return;
            }
            if (evt.getCreatorEventID() == DesignerEvent.CREATOR_EDITED
                    || evt.getCreatorEventID() == DesignerEvent.CREATOR_DELETED
                    || evt.getCreatorEventID() == DesignerEvent.CREATOR_SELECTED) {

                TreePath paths = getSelectedTreePath();

                if (paths == null) {
                    return;
                }
                if (BaseUtils.isAuthorityEditing()) {
                    showAuthorityEditPane();
                }

            } else if (evt.getCreatorEventID() == DesignerEvent.CREATOR_RESIZED) {
                repaint();
            }
        }


        public TreePath getSelectedTreePath() {
            XCreator creator = selectionModel.getSelection().getSelectedCreator();
            return buildTreePath(creator);
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof FormWidgetAuthorityEditAdapter;
        }

        private TreePath buildTreePath(Component comp) {
            ArrayList<Component> path = new ArrayList<Component>();
            Component parent = comp;

            while (parent != null) {
                path.add(0, parent);
                parent = parent.getParent();
            }

            Object[] components = path.toArray();
            if (components.length == 0) {
                return null;
            }

            return new TreePath(components);
        }

    }
}
