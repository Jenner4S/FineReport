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

    //表单设计器
    private FormDesigner formDesign;
    //格子设计器
    private FormECDesignerProvider elementCaseDesign;

    //中间编辑区域, carllayout布局
    private JPanel tabCenterPane;
    private CardLayout cardLayout;
    //当前编辑的组件对象
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
     * 是否是报表
     *
     * @return 否
     */
    public boolean isJWorkBook() {
        return false;
    }

    /**
     * 返回当前支持的超链界面pane
     *
     * @return 超链连接界面
     */
    public HyperlinkGroupPane getHyperLinkPane() {
        return new FormHyperlinkGroupPane();
    }

    //表单返回 FORM_TAB or ELEMENTCASE_TAB
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
     * 菜单栏上的文件按钮
     *
     * @return 菜单数组
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
     * 取消格式
     */
    public void cancelFormat() {
        return;
    }

    /**
     * 重新计算大小
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
                JForm.this.fireTargetModified();// 调用保存*, 调用刷新界面, 刷新工具栏按钮
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
     * 去除选择
     */
    public void removeTemplateSelection() {
        return;
    }

    public void setSheetCovered(boolean isCovered) {

    }

    /**
     * 刷新容器
     */
    public void refreshContainer() {

    }

    /**
     * 去除参数面板选择
     */
    public void removeParameterPaneSelection() {
        return;
    }

    /**
     * 创建权限细粒度编辑面板
     *
     * @return 权限细粒度编辑面板
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
     *焦点放到JForm
     */
    public void requestFocus() {
        super.requestFocus();
        formDesign.requestFocus();
    }

    /**
     * 焦点放到JForm
     */
    public void requestGridFocus() {
        super.requestFocus();
        formDesign.requestFocus();
    }

    @Override
    /**
     * 保存文件的后缀名
     *
     * @return 返回后缀名
     */
    public String suffix() {
        // daniel改成三个字
        return ".frm";
    }

    /**
     * 刷新
     */
    public void refreshRoot() {
        // formDesign子类的target重置
        this.formDesign.setTarget(this.getTarget());
        this.formDesign.refreshRoot();
        FormHierarchyTreePane.getInstance().refreshRoot();
    }

    /**
     * 刷新s
     */
    public void refreshAllNameWidgets() {
        if (formDesign.getParaComponent() != null) {
            XCreatorUtils.refreshAllNameWidgets(formDesign.getParaComponent());
        }
        XCreatorUtils.refreshAllNameWidgets(formDesign.getRootComponent());
    }

    /**
     * 刷新
     */
    public void refreshSelectedWidget() {
        formDesign.getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_SELECTED);
    }

    @Override
    /**
     *复制  f
     */
    public void copy() {
        this.formDesign.copy();
    }

    @Override
    /**
     *
     * 粘贴
     * @return 是否成功
     */
    public boolean paste() {
        return this.formDesign.paste();
    }

    @Override
    /**
     *
     * 剪切
     * @return 是否成功
     */
    public boolean cut() {
        return this.formDesign.cut();
    }

    // ////////////////////////////////////////////////////////////////////
    // ////////////////for toolbarMenuAdapter//////////////////////////////
    // ////////////////////////////////////////////////////////////////////


    @Override
    /**
     * 目标菜单
     *
     * @return 菜单
     */
    public MenuDef[] menus4Target() {
        return this.index == FORM_TAB ?
                (MenuDef[]) ArrayUtils.addAll(super.menus4Target(), this.formDesign.menus4Target()) :
                (MenuDef[]) ArrayUtils.addAll(super.menus4Target(), this.elementCaseDesign.menus4Target());
    }

    @Override
    /**
     *  模板的工具
     *
     * @return 工具
     */
    public ToolBarDef[] toolbars4Target() {
        return this.index == FORM_TAB ?
                this.formDesign.toolbars4Target() :
                this.elementCaseDesign.toolbars4Target();
    }

    @Override
    /**
     * 模板菜单
     *
     * @return 返回菜单
     */
    public ShortCut[] shortcut4TemplateMenu() {
        return this.index == FORM_TAB ? new ShortCut[0] :
                this.elementCaseDesign.shortcut4TemplateMenu();
    }

    /**
     * 权限细粒度模板菜单
     * 表单中去掉此菜单项
     *
     * @return 菜单
     */
    public ShortCut[] shortCuts4Authority() {
        return new ShortCut[0];
    }

    @Override
    /**
     * undo的表单state
     *
     *  @return 表单State
     */
    protected FormUndoState createUndoState() {
        FormUndoState cur = new FormUndoState(this, this.formDesign.getArea());
        if (this.formDesign.isReportBlockEditing()) {
            cur.setFormReportType(BaseUndoState.STATE_FORM_REPORT);
        }
        return cur;
    }

    /**
     * 应用UndoState
     *
     * @param o undo的状态
     */
    public void applyUndoState4Form(BaseUndoState o) {
        this.applyUndoState((FormUndoState) o);
    }

    /**
     * 可以撤销
     *
     * @return 是则返回true
     */
    public boolean canUndo() {
        //报表块最多撤销至编辑报表块的第一步，不能撤销表单中的操作
        boolean inECUndoForm = undoState.getFormReportType() == BaseUndoState.STATE_BEFORE_FORM_REPORT && formDesign.isReportBlockEditing();
        return !inECUndoForm && this.getUndoManager().canUndo();
    }

    @Override
    /**
     * 应用undoState的表单数据
     */
    protected void applyUndoState(FormUndoState u) {
        try {
            //JForm的target重置
            this.setTarget((Form) u.getForm().clone());
            if (this.index == FORM_TAB) {
                JForm.this.refreshRoot();
                this.formDesign.getArea().setAreaSize(u.getAreaSize(), u.getHorizontalValue(), u.getVerticalValue(), u.getWidthValue(), u.getHeightValue(), u.getSlideValue());
                this.formDesign.getSelectionModel().setSelectedCreators(FormSelectionUtils.rebuildSelection(formDesign.getRootComponent(), u.getSelectWidgets()));
            } else {
                String widgetName = this.formDesign.getElementCaseContainerName();
                //这儿太坑了，u.getForm() 与 getTarget内容不一样
                FormElementCaseProvider dataTable = getTarget().getElementCaseByName(widgetName);
                this.reportComposite.setSelectedWidget(dataTable);
                //下面这句话是防止撤销之后直接退出编辑再编辑撤销的东西会回来,因为撤销不会保存EC
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
     * 表单的工具栏
     *
     * @return 表单工具栏
     */
    public JPanel[] toolbarPanes4Form() {
        return this.index == FORM_TAB ?
                new JPanel[]{FormParaWidgetPane.getInstance(formDesign)} :
                new JPanel[0];
    }

    /**
     * 表单的工具按钮
     *
     * @return 工具按钮
     */
    public JComponent[] toolBarButton4Form() {
        return this.index == FORM_TAB ?
                new JComponent[]{
                        //自适应布局里的复制粘贴意义不大, 先屏蔽掉
//        		new CutAction(formDesign).createToolBarComponent(), 
//        		new CopyAction(formDesign).createToolBarComponent(), 
//        		new PasteAction(formDesign).createToolBarComponent(),
                        new FormDeleteAction(formDesign).createToolBarComponent()} :
                elementCaseDesign.toolBarButton4Form();
    }

    /**
     * 权限细粒度状态下的工具面板
     *
     * @return 工具面板
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
     * 创建菜单项Preview
     *
     * @return 菜单
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
     * 刷新参数
     */
    public void populateParameter() {
        formDesign.populateParameterPropertyPane();
    }

    @Override
    /**
     * 刷新工具区域
     */
    public void refreshToolArea() {
        populateParameter();
        DesignerContext.getDesignerFrame().resetToolkitByPlus(JForm.this);
        //表单切换后拖不进去组件是因为找不到designer
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
     * 选择的是否是表单主体
     *
     * @return 是则返回true
     */
    public boolean isSelectRootPane() {
        return formDesign.getRootComponent() == formDesign.getSelectionModel().getSelection().getSelectedCreator();

    }

    /**
     * 只在Form和ElementCase之间切换
     *
     * @param index 切换位置
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
            //报表块编辑失焦，进入报表块可直接编辑A1
            elementCaseDesign.requestFocus();
            //进入编辑报表块，触发一次保存，记住编辑报表块前的表单状态
            //防止报表块中撤销到表单
            JForm.this.fireTargetModified();
        }
    }

    /**
     * 在Form和ElementCase, 以及ElementCase和ElementCase之间切换
     *
     * @param index       切换位置
     * @param ecContainer ElementCase所在container
     */
    public void tabChanged(int index, FormElementCaseContainerProvider ecContainer) {
        if (index == ELEMENTCASE_CHANGE_TAB) {
            saveImage();
            //更新FormDesign中的控件容器
            formDesign.setElementCaseContainer(ecContainer);
            //如果只是内部ElementCase之间的切换, 那么不需要下面的界面变动
            return;
        }

        tabChanged(index);
    }

    /**
     * 格子编辑组件
     */
    private FormECDesignerProvider initElementCaseDesign() {
        HashMap<String, Class> designerClass = new HashMap<String, Class>();
        designerClass.put(Constants.ARG_0, FormElementCaseProvider.class);

        Object[] designerArg = new Object[]{formDesign.getElementCase()};
        return StableFactory.getMarkedInstanceObjectFromClass(FormECDesignerProvider.XML_TAG, designerArg, designerClass, FormECDesignerProvider.class);
    }

    /**
     * 整个报表块编辑区域
     */
    private FormECCompositeProvider initComposite() {
        Object[] compositeArg = new Object[]{this, elementCaseDesign, formDesign.getElementCaseContainer()};
        HashMap<String, Class> compoClass = new HashMap<String, Class>();
        compoClass.put(Constants.ARG_0, BaseJForm.class);
        compoClass.put(Constants.ARG_2, FormElementCaseContainerProvider.class);
        return StableFactory.getMarkedInstanceObjectFromClass(FormECCompositeProvider.XML_TAG, compositeArg, compoClass, FormECCompositeProvider.class);
    }

    /**
     * 切换格子编辑
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
        //触发一次保存, 把缩略图保存起来
        JForm.this.fireTargetModified();
        //用formDesign的size是为了当报表块被拉伸时, 它对应的背景图片需要足够大才不会显示成空白
        BufferedImage image = elementCaseDesign.getElementCaseImage(formDesign.getSize());
        formDesign.setElementCaseBackground(image);
    }

    /**
     * 切换form编辑
     */
    private void formTabAction() {
        saveImage();
    }

    /**
     * 取小图标，主要用于多TAB标签栏
     *
     * @return 图表
     */
    public Icon getIcon() {
        return BaseUtils.readIcon("/com/fr/web/images/form/new_form3.png");
    }

}