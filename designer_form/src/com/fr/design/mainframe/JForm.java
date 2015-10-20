package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.DesignState;
import com.fr.design.actions.file.WebPreviewUtils;
import com.fr.design.cell.FloatElementsProvider;
import com.fr.design.constants.UIConstants;
import com.fr.design.designer.beans.actions.FormDeleteAction;
import com.fr.design.designer.beans.events.DesignerEditListener;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XComponent;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.properties.FormWidgetAuthorityEditPane;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.gui.frpane.HyperlinkGroupPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.gui.xpane.FormHyperlinkGroupPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.actions.EmbeddedFormExportExportAction;
import com.fr.design.mainframe.form.FormECCompositeProvider;
import com.fr.design.mainframe.form.FormECDesignerProvider;
import com.fr.design.mainframe.toolbar.ToolBarMenuDock;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.file.FILE;
import com.fr.form.FormElementCaseContainerProvider;
import com.fr.form.FormElementCaseProvider;
import com.fr.form.main.Form;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.form.ui.container.WLayout;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Constants;
import com.fr.stable.bridge.StableFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class JForm extends JTemplate<Form, FormUndoState> implements BaseJForm {
    private static final String FORM_CARD = "FORM";
    private static final String ELEMENTCASE_CARD = "ELEMENTCASE";

    private static final String[] CARDNAME = new String[]{FORM_CARD, ELEMENTCASE_CARD};
    private static final int TOOLBARPANEDIMHEIGHT_FORM = 60;

    //�������
    private FormDesigner formDesign;
    //���������
    private FormECDesignerProvider elementCaseDesign;

    //�м�༭����, carllayout����
    private JPanel tabCenterPane;
    private CardLayout cardLayout;
    //��ǰ�༭���������
    private JComponent editingComponent;
    private FormECCompositeProvider reportComposite;

    protected int index = FORM_TAB;

    public JForm() {
        super(new Form(new WBorderLayout("form")), "Form");
    }

    public JForm(Form form, FILE file) {
        super(form, file);
    }

    public int getMenuState() {

        return DesignState.JFORM;
    }

    @Override
    protected boolean accept(Object o) {
        return !(o instanceof FloatElementsProvider);
    }

    /**
     * �Ƿ��Ǳ���
     *
     * @return ��
     */
    public boolean isJWorkBook() {
        return false;
    }

    /**
     * ���ص�ǰ֧�ֵĳ�������pane
     *
     * @return �������ӽ���
     */
    public HyperlinkGroupPane getHyperLinkPane() {
        return new FormHyperlinkGroupPane();
    }

    //������ FORM_TAB or ELEMENTCASE_TAB
    public int getEditingReportIndex() {
        return this.index;
    }

    public void setAuthorityMode(boolean isUpMode) {
        return;
    }

    public int getToolBarHeight() {
        return TOOLBARPANEDIMHEIGHT_FORM;
    }

    /**
     * �˵����ϵ��ļ���ť
     *
     * @return �˵�����
     */
    public ShortCut[] shortcut4FileMenu() {
        return (ShortCut[]) ArrayUtils.addAll(
                super.shortcut4FileMenu(), new ShortCut[]{this.createWorkBookExportMenu()}
        );
    }

    private MenuDef createWorkBookExportMenu() {
        MenuDef exportMenuDef = new MenuDef(KeySetUtils.EXPORT.getMenuName());
        exportMenuDef.setIconPath("/com/fr/design/images/m_file/export.png");
        exportMenuDef.addShortCut(new EmbeddedFormExportExportAction(this));

        return exportMenuDef;
    }

    /**
     * ȡ����ʽ
     */
    public void cancelFormat() {
        return;
    }

    /**
     * ���¼����С
     */
    public void doResize() {
        formDesign.getRootComponent().setSize(formDesign.getSize());
        LayoutUtils.layoutRootContainer(formDesign.getRootComponent());
    }

    @Override
    protected JPanel createCenterPane() {
        tabCenterPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        cardLayout = (CardLayout) tabCenterPane.getLayout();

        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        centerPane.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, new Color(85, 85, 85)));
        formDesign = new FormDesigner(this.getTarget(), new TabChangeAction(BaseJForm.ELEMENTCASE_TAB, this));
        WidgetToolBarPane.getInstance(formDesign);
        FormArea area = new FormArea(formDesign);
        centerPane.add(area, BorderLayout.CENTER);
        tabCenterPane.add(centerPane, FORM_CARD, FORM_TAB);

        this.add(tabCenterPane, BorderLayout.CENTER);

        formDesign.addTargetModifiedListener(new TargetModifiedListener() {

            @Override
            public void targetModified(TargetModifiedEvent e) {
                JForm.this.fireTargetModified();// ���ñ���*, ����ˢ�½���, ˢ�¹�������ť
            }
        });
        formDesign.addDesignerEditListener(new DesignerEditListener() {

            @Override
            public void fireCreatorModified(DesignerEvent evt) {
                if (evt.getCreatorEventID() == DesignerEvent.CREATOR_CUTED
                        || evt.getCreatorEventID() == DesignerEvent.CREATOR_DELETED) {
                    setPropertyPaneChange(formDesign.getRootComponent());
                } else if (evt.getCreatorEventID() == DesignerEvent.CREATOR_SELECTED) {
                    setPropertyPaneChange(evt.getAffectedCreator());
                }
            }
        });
        return tabCenterPane;
    }

    public FormDesigner getFormDesign() {
        return formDesign;
    }

    public void setFormDesign(FormDesigner formDesign) {
        this.formDesign = formDesign;
    }

    /**
     * ȥ��ѡ��
     */
    public void removeTemplateSelection() {
        return;
    }

    public void setSheetCovered(boolean isCovered) {

    }

    /**
     * ˢ������
     */
    public void refreshContainer() {

    }

    /**
     * ȥ���������ѡ��
     */
    public void removeParameterPaneSelection() {
        return;
    }

    /**
     * ����Ȩ��ϸ���ȱ༭���
     *
     * @return Ȩ��ϸ���ȱ༭���
     */
    public AuthorityEditPane createAuthorityEditPane() {
        FormWidgetAuthorityEditPane formWidgetAuthorityEditPane = new FormWidgetAuthorityEditPane(formDesign);
        formWidgetAuthorityEditPane.populateDetials();
        return formWidgetAuthorityEditPane;
    }


    private void setPropertyPaneChange(XComponent comp) {
        if (comp == null) {
            return;
        }
        editingComponent = comp.createToolPane(this, formDesign);
        if (BaseUtils.isAuthorityEditing()) {
            EastRegionContainerPane.getInstance().replaceUpPane(
                    ComparatorUtils.equals(editingComponent.getClass(), NoSupportAuthorityEdit.class) ? editingComponent : createAuthorityEditPane());
        } else {
            EastRegionContainerPane.getInstance().replaceUpPane(editingComponent);
        }
    }

    public JComponent getEditingPane() {
        return editingComponent;
    }


    public ToolBarMenuDockPlus getToolBarMenuDockPlus() {
        return this;
    }


    @Override
    /**
     *����ŵ�JForm
     */
    public void requestFocus() {
        super.requestFocus();
        formDesign.requestFocus();
    }

    /**
     * ����ŵ�JForm
     */
    public void requestGridFocus() {
        super.requestFocus();
        formDesign.requestFocus();
    }

    @Override
    /**
     * �����ļ��ĺ�׺��
     *
     * @return ���غ�׺��
     */
    public String suffix() {
        // daniel�ĳ�������
        return ".frm";
    }

    /**
     * ˢ��
     */
    public void refreshRoot() {
        // formDesign�����target����
        this.formDesign.setTarget(this.getTarget());
        this.formDesign.refreshRoot();
        FormHierarchyTreePane.getInstance().refreshRoot();
    }

    /**
     * ˢ��s
     */
    public void refreshAllNameWidgets() {
        if (formDesign.getParaComponent() != null) {
            XCreatorUtils.refreshAllNameWidgets(formDesign.getParaComponent());
        }
        XCreatorUtils.refreshAllNameWidgets(formDesign.getRootComponent());
    }

    /**
     * ˢ��
     */
    public void refreshSelectedWidget() {
        formDesign.getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_SELECTED);
    }

    @Override
    /**
     *����  f
     */
    public void copy() {
        this.formDesign.copy();
    }

    @Override
    /**
     *
     * ճ��
     * @return �Ƿ�ɹ�
     */
    public boolean paste() {
        return this.formDesign.paste();
    }

    @Override
    /**
     *
     * ����
     * @return �Ƿ�ɹ�
     */
    public boolean cut() {
        return this.formDesign.cut();
    }

    // ////////////////////////////////////////////////////////////////////
    // ////////////////for toolbarMenuAdapter//////////////////////////////
    // ////////////////////////////////////////////////////////////////////


    @Override
    /**
     * Ŀ��˵�
     *
     * @return �˵�
     */
    public MenuDef[] menus4Target() {
        return this.index == FORM_TAB ?
                (MenuDef[]) ArrayUtils.addAll(super.menus4Target(), this.formDesign.menus4Target()) :
                (MenuDef[]) ArrayUtils.addAll(super.menus4Target(), this.elementCaseDesign.menus4Target());
    }

    @Override
    /**
     *  ģ��Ĺ���
     *
     * @return ����
     */
    public ToolBarDef[] toolbars4Target() {
        return this.index == FORM_TAB ?
                this.formDesign.toolbars4Target() :
                this.elementCaseDesign.toolbars4Target();
    }

    @Override
    /**
     * ģ��˵�
     *
     * @return ���ز˵�
     */
    public ShortCut[] shortcut4TemplateMenu() {
        return this.index == FORM_TAB ? new ShortCut[0] :
                this.elementCaseDesign.shortcut4TemplateMenu();
    }

    /**
     * Ȩ��ϸ����ģ��˵�
     * ����ȥ���˲˵���
     *
     * @return �˵�
     */
    public ShortCut[] shortCuts4Authority() {
        return new ShortCut[0];
    }

    @Override
    /**
     * undo�ı�state
     *
     *  @return ��State
     */
    protected FormUndoState createUndoState() {
        FormUndoState cur = new FormUndoState(this, this.formDesign.getArea());
        if (this.formDesign.isReportBlockEditing()) {
            cur.setFormReportType(BaseUndoState.STATE_FORM_REPORT);
        }
        return cur;
    }

    /**
     * Ӧ��UndoState
     *
     * @param o undo��״̬
     */
    public void applyUndoState4Form(BaseUndoState o) {
        this.applyUndoState((FormUndoState) o);
    }

    /**
     * ���Գ���
     *
     * @return ���򷵻�true
     */
    public boolean canUndo() {
        //�������೷�����༭�����ĵ�һ�������ܳ������еĲ���
        boolean inECUndoForm = undoState.getFormReportType() == BaseUndoState.STATE_BEFORE_FORM_REPORT && formDesign.isReportBlockEditing();
        return !inECUndoForm && this.getUndoManager().canUndo();
    }

    @Override
    /**
     * Ӧ��undoState�ı�����
     */
    protected void applyUndoState(FormUndoState u) {
        try {
            //JForm��target����
            this.setTarget((Form) u.getForm().clone());
            if (this.index == FORM_TAB) {
                JForm.this.refreshRoot();
                this.formDesign.getArea().setAreaSize(u.getAreaSize(), u.getHorizontalValue(), u.getVerticalValue(), u.getWidthValue(), u.getHeightValue(), u.getSlideValue());
                this.formDesign.getSelectionModel().setSelectedCreators(FormSelectionUtils.rebuildSelection(formDesign.getRootComponent(), u.getSelectWidgets()));
            } else {
                String widgetName = this.formDesign.getElementCaseContainerName();
                //���̫���ˣ�u.getForm() �� getTarget���ݲ�һ��
                FormElementCaseProvider dataTable = getTarget().getElementCaseByName(widgetName);
                this.reportComposite.setSelectedWidget(dataTable);
                //������仰�Ƿ�ֹ����֮��ֱ���˳��༭�ٱ༭�����Ķ��������,��Ϊ�������ᱣ��EC
                formDesign.setElementCase(dataTable);
            }
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        if (BaseUtils.isAuthorityEditing()) {
            this.authorityUndoState = u;
        } else {
            this.undoState = u;
        }

    }

    @Override
    /**
     *
     */
    protected FormModelAdapter createDesignModel() {
        return new FormModelAdapter(this);
    }

    @Override
    /**
     * ���Ĺ�����
     *
     * @return ��������
     */
    public JPanel[] toolbarPanes4Form() {
        return this.index == FORM_TAB ?
                new JPanel[]{FormParaWidgetPane.getInstance(formDesign)} :
                new JPanel[0];
    }

    /**
     * ���Ĺ��߰�ť
     *
     * @return ���߰�ť
     */
    public JComponent[] toolBarButton4Form() {
        return this.index == FORM_TAB ?
                new JComponent[]{
                        //����Ӧ������ĸ���ճ�����岻��, �����ε�
//        		new CutAction(formDesign).createToolBarComponent(), 
//        		new CopyAction(formDesign).createToolBarComponent(), 
//        		new PasteAction(formDesign).createToolBarComponent(),
                        new FormDeleteAction(formDesign).createToolBarComponent()} :
                elementCaseDesign.toolBarButton4Form();
    }

    /**
     * Ȩ��ϸ����״̬�µĹ������
     *
     * @return �������
     */
    public JComponent toolBar4Authority() {
        JPanel panel = new JPanel(new BorderLayout()) {
            public Dimension getPreferredSize() {
                Dimension dim = super.getPreferredSize();
                dim.height = ToolBarMenuDock.PANLE_HEIGNT;
                return dim;
            }
        };
        UILabel uiLabel = new UILabel(Inter.getLocText(new String[]{"DashBoard-FormBook", "Privilege", "Edit"}));
        uiLabel.setHorizontalAlignment(SwingConstants.CENTER);
        uiLabel.setFont(new Font(Inter.getLocText("FR-Designer-All_MSBold"), 0, 14));
        uiLabel.setForeground(new Color(150, 150, 150));
        panel.add(uiLabel, BorderLayout.CENTER);
        return panel;
    }


    public JPanel getEastUpPane() {
        if (BaseUtils.isAuthorityEditing()) {
            if (formDesign.isSupportAuthority()) {
                return new AuthorityPropertyPane(this);
            } else {
                return new NoSupportAuthorityEdit();
            }
        } else {
            if (editingComponent == null) {
                editingComponent = formDesign.getRootComponent().createToolPane(this, formDesign);
            }
            return (JPanel) editingComponent;
        }
    }

    public JPanel getEastDownPane() {
        return formDesign.getEastDownPane();
    }

    @Override
    /**
     *
     */
    public Icon getPreviewLargeIcon() {
        return UIConstants.RUN_BIG_ICON;
    }

    @Override
    /**
     * �����˵���Preview
     *
     * @return �˵�
     */
    public UIMenuItem[] createMenuItem4Preview() {
        UIMenuItem form = new UIMenuItem(Inter.getLocText("M-Form_Preview"), UIConstants.RUN_SMALL_ICON);
        form.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WebPreviewUtils.onFormPreview(JForm.this);
            }
        });
        return new UIMenuItem[]{form};
    }

    /**
     * ˢ�²���
     */
    public void populateParameter() {
        formDesign.populateParameterPropertyPane();
    }

    @Override
    /**
     * ˢ�¹�������
     */
    public void refreshToolArea() {
        populateParameter();
        DesignerContext.getDesignerFrame().resetToolkitByPlus(JForm.this);
        //���л����ϲ���ȥ�������Ϊ�Ҳ���designer
        WidgetToolBarPane.getInstance(formDesign);
        if (BaseUtils.isAuthorityEditing()) {
            if (formDesign.isSupportAuthority()) {
                EastRegionContainerPane.getInstance().replaceUpPane(new AuthorityPropertyPane(this));
            } else {
                EastRegionContainerPane.getInstance().replaceUpPane(new NoSupportAuthorityEdit());
            }
            EastRegionContainerPane.getInstance().replaceDownPane(RolesAlreadyEditedPane.getInstance());
            return;
        }
        if (formDesign.isReportBlockEditing()) {
            if (elementCaseDesign != null) {
                EastRegionContainerPane.getInstance().replaceDownPane(elementCaseDesign.getEastDownPane());
                EastRegionContainerPane.getInstance().replaceUpPane(elementCaseDesign.getEastUpPane());
                return;
            }
        }

        EastRegionContainerPane.getInstance().replaceUpPane(WidgetPropertyPane.getInstance(formDesign));

        if (EastRegionContainerPane.getInstance().getDownPane() == null) {
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        FRLogger.getLogger().error(e.getMessage(), e);
                    }
                    JPanel pane = new JPanel();
                    pane.setLayout(new BorderLayout());
                    pane.add(FormWidgetDetailPane.getInstance(formDesign), BorderLayout.CENTER);
                    EastRegionContainerPane.getInstance().replaceDownPane(pane);
                }
            }.start();
        } else {
            JPanel pane = new JPanel();
            pane.setLayout(new BorderLayout());
            pane.add(FormWidgetDetailPane.getInstance(formDesign), BorderLayout.CENTER);
            EastRegionContainerPane.getInstance().replaceDownPane(pane);
        }
    }

    public String getEditingCreatorName() {
        return formDesign.getSelectionModel().getSelection().getSelectedCreator().toData().getWidgetName();
    }

    public WLayout getRootLayout() {
        return formDesign.getRootComponent().toData();
    }

    /**
     * ѡ����Ƿ��Ǳ�����
     *
     * @return ���򷵻�true
     */
    public boolean isSelectRootPane() {
        return formDesign.getRootComponent() == formDesign.getSelectionModel().getSelection().getSelectedCreator();

    }

    /**
     * ֻ��Form��ElementCase֮���л�
     *
     * @param index �л�λ��
     */
    public void tabChanged(int index) {
        if (index == ELEMENTCASE_TAB) {
            formDesign.setReportBlockEditing(true);
            ecTabAction();
        } else {
            formDesign.setReportBlockEditing(false);
            formTabAction();
        }
        this.index = index;
        refreshToolArea();
        this.cardLayout.show(tabCenterPane, CARDNAME[index]);
        if (elementCaseDesign != null && index == ELEMENTCASE_TAB) {
            //�����༭ʧ�������뱨����ֱ�ӱ༭A1
            elementCaseDesign.requestFocus();
            //����༭����飬����һ�α��棬��ס�༭�����ǰ�ı�״̬
            //��ֹ������г�������
            JForm.this.fireTargetModified();
        }
    }

    /**
     * ��Form��ElementCase, �Լ�ElementCase��ElementCase֮���л�
     *
     * @param index       �л�λ��
     * @param ecContainer ElementCase����container
     */
    public void tabChanged(int index, FormElementCaseContainerProvider ecContainer) {
        if (index == ELEMENTCASE_CHANGE_TAB) {
            saveImage();
            //����FormDesign�еĿؼ�����
            formDesign.setElementCaseContainer(ecContainer);
            //���ֻ���ڲ�ElementCase֮����л�, ��ô����Ҫ����Ľ���䶯
            return;
        }

        tabChanged(index);
    }

    /**
     * ���ӱ༭���
     */
    private FormECDesignerProvider initElementCaseDesign() {
        HashMap<String, Class> designerClass = new HashMap<String, Class>();
        designerClass.put(Constants.ARG_0, FormElementCaseProvider.class);

        Object[] designerArg = new Object[]{formDesign.getElementCase()};
        return StableFactory.getMarkedInstanceObjectFromClass(FormECDesignerProvider.XML_TAG, designerArg, designerClass, FormECDesignerProvider.class);
    }

    /**
     * ���������༭����
     */
    private FormECCompositeProvider initComposite() {
        Object[] compositeArg = new Object[]{this, elementCaseDesign, formDesign.getElementCaseContainer()};
        HashMap<String, Class> compoClass = new HashMap<String, Class>();
        compoClass.put(Constants.ARG_0, BaseJForm.class);
        compoClass.put(Constants.ARG_2, FormElementCaseContainerProvider.class);
        return StableFactory.getMarkedInstanceObjectFromClass(FormECCompositeProvider.XML_TAG, compositeArg, compoClass, FormECCompositeProvider.class);
    }

    /**
     * �л����ӱ༭
     */
    private void ecTabAction() {
        elementCaseDesign = initElementCaseDesign();
        reportComposite = initComposite();

        tabCenterPane.add((Component) reportComposite, ELEMENTCASE_CARD, 1);
        reportComposite.addTargetModifiedListener(new TargetModifiedListener() {

            @Override
            public void targetModified(TargetModifiedEvent e) {
                JForm.this.fireTargetModified();
                FormElementCaseProvider te = elementCaseDesign.getEditingElementCase();
                formDesign.setElementCase(te);
            }
        });
    }

    private void saveImage() {
        //����һ�α���, ������ͼ��������
        JForm.this.fireTargetModified();
        //��formDesign��size��Ϊ�˵�����鱻����ʱ, ����Ӧ�ı���ͼƬ��Ҫ�㹻��Ų�����ʾ�ɿհ�
        BufferedImage image = elementCaseDesign.getElementCaseImage(formDesign.getSize());
        formDesign.setElementCaseBackground(image);
    }

    /**
     * �л�form�༭
     */
    private void formTabAction() {
        saveImage();
    }

    /**
     * ȡСͼ�꣬��Ҫ���ڶ�TAB��ǩ��
     *
     * @return ͼ��
     */
    public Icon getIcon() {
        return BaseUtils.readIcon("/com/fr/web/images/form/new_form3.png");
    }

}
