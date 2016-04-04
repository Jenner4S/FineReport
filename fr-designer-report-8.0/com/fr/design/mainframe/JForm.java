// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.DesignModelAdapter;
import com.fr.design.actions.file.WebPreviewUtils;
import com.fr.design.cell.FloatElementsProvider;
import com.fr.design.constants.UIConstants;
import com.fr.design.designer.beans.actions.FormDeleteAction;
import com.fr.design.designer.beans.events.*;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.*;
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
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.*;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.file.FILE;
import com.fr.form.FormElementCaseContainerProvider;
import com.fr.form.FormElementCaseProvider;
import com.fr.form.main.Form;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.form.ui.container.WLayout;
import com.fr.general.*;
import com.fr.stable.ArrayUtils;
import com.fr.stable.bridge.StableFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.undo.UndoManager;

// Referenced classes of package com.fr.design.mainframe:
//            JTemplate, FormDesigner, TabChangeAction, FormArea, 
//            NoSupportAuthorityEdit, FormUndoState, FormModelAdapter, AuthorityPropertyPane, 
//            BaseJForm, EastRegionContainerPane, WidgetToolBarPane, FormHierarchyTreePane, 
//            FormSelectionUtils, FormParaWidgetPane, DesignerContext, DesignerFrame, 
//            WidgetPropertyPane, FormWidgetDetailPane, FormSelection, AuthorityEditPane, 
//            BaseUndoState

public class JForm extends JTemplate
    implements BaseJForm
{

    private static final String FORM_CARD = "FORM";
    private static final String ELEMENTCASE_CARD = "ELEMENTCASE";
    private static final String CARDNAME[] = {
        "FORM", "ELEMENTCASE"
    };
    private static final int TOOLBARPANEDIMHEIGHT_FORM = 60;
    private FormDesigner formDesign;
    private FormECDesignerProvider elementCaseDesign;
    private JPanel tabCenterPane;
    private CardLayout cardLayout;
    private JComponent editingComponent;
    private FormECCompositeProvider reportComposite;
    protected int index;

    public JForm()
    {
        super(new Form(new WBorderLayout("form")), "Form");
        index = 0;
    }

    public JForm(Form form, FILE file)
    {
        super(form, file);
        index = 0;
    }

    public int getMenuState()
    {
        return 4;
    }

    protected boolean accept(Object obj)
    {
        return !(obj instanceof FloatElementsProvider);
    }

    public boolean isJWorkBook()
    {
        return false;
    }

    public HyperlinkGroupPane getHyperLinkPane()
    {
        return new FormHyperlinkGroupPane();
    }

    public int getEditingReportIndex()
    {
        return index;
    }

    public void setAuthorityMode(boolean flag)
    {
    }

    public int getToolBarHeight()
    {
        return 60;
    }

    public ShortCut[] shortcut4FileMenu()
    {
        return (ShortCut[])(ShortCut[])ArrayUtils.addAll(super.shortcut4FileMenu(), new ShortCut[] {
            createWorkBookExportMenu()
        });
    }

    private MenuDef createWorkBookExportMenu()
    {
        MenuDef menudef = new MenuDef(KeySetUtils.EXPORT.getMenuName());
        menudef.setIconPath("/com/fr/design/images/m_file/export.png");
        menudef.addShortCut(new ShortCut[] {
            new EmbeddedFormExportExportAction(this)
        });
        return menudef;
    }

    public void cancelFormat()
    {
    }

    public void doResize()
    {
        formDesign.getRootComponent().setSize(formDesign.getSize());
        LayoutUtils.layoutRootContainer(formDesign.getRootComponent());
    }

    protected JPanel createCenterPane()
    {
        tabCenterPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        cardLayout = (CardLayout)tabCenterPane.getLayout();
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, new Color(85, 85, 85)));
        formDesign = new FormDesigner((Form)getTarget(), new TabChangeAction(1, this));
        WidgetToolBarPane.getInstance(formDesign);
        FormArea formarea = new FormArea(formDesign);
        jpanel.add(formarea, "Center");
        tabCenterPane.add(jpanel, "FORM", 0);
        add(tabCenterPane, "Center");
        formDesign.addTargetModifiedListener(new TargetModifiedListener() {

            final JForm this$0;

            public void targetModified(TargetModifiedEvent targetmodifiedevent)
            {
                fireTargetModified();
            }

            
            {
                this$0 = JForm.this;
                super();
            }
        }
);
        formDesign.addDesignerEditListener(new DesignerEditListener() {

            final JForm this$0;

            public void fireCreatorModified(DesignerEvent designerevent)
            {
                if(designerevent.getCreatorEventID() == 3 || designerevent.getCreatorEventID() == 2)
                    setPropertyPaneChange(formDesign.getRootComponent());
                else
                if(designerevent.getCreatorEventID() == 7)
                    setPropertyPaneChange(designerevent.getAffectedCreator());
            }

            
            {
                this$0 = JForm.this;
                super();
            }
        }
);
        return tabCenterPane;
    }

    public FormDesigner getFormDesign()
    {
        return formDesign;
    }

    public void setFormDesign(FormDesigner formdesigner)
    {
        formDesign = formdesigner;
    }

    public void removeTemplateSelection()
    {
    }

    public void setSheetCovered(boolean flag)
    {
    }

    public void refreshContainer()
    {
    }

    public void removeParameterPaneSelection()
    {
    }

    public AuthorityEditPane createAuthorityEditPane()
    {
        FormWidgetAuthorityEditPane formwidgetauthorityeditpane = new FormWidgetAuthorityEditPane(formDesign);
        formwidgetauthorityeditpane.populateDetials();
        return formwidgetauthorityeditpane;
    }

    private void setPropertyPaneChange(XComponent xcomponent)
    {
        if(xcomponent == null)
            return;
        editingComponent = xcomponent.createToolPane(this, formDesign);
        if(BaseUtils.isAuthorityEditing())
            EastRegionContainerPane.getInstance().replaceUpPane(((JComponent) (ComparatorUtils.equals(editingComponent.getClass(), com/fr/design/mainframe/NoSupportAuthorityEdit) ? editingComponent : ((JComponent) (createAuthorityEditPane())))));
        else
            EastRegionContainerPane.getInstance().replaceUpPane(editingComponent);
    }

    public JComponent getEditingPane()
    {
        return editingComponent;
    }

    public ToolBarMenuDockPlus getToolBarMenuDockPlus()
    {
        return this;
    }

    public void requestFocus()
    {
        super.requestFocus();
        formDesign.requestFocus();
    }

    public void requestGridFocus()
    {
        super.requestFocus();
        formDesign.requestFocus();
    }

    public String suffix()
    {
        return ".frm";
    }

    public void refreshRoot()
    {
        formDesign.setTarget(getTarget());
        formDesign.refreshRoot();
        FormHierarchyTreePane.getInstance().refreshRoot();
    }

    public void refreshAllNameWidgets()
    {
        if(formDesign.getParaComponent() != null)
            XCreatorUtils.refreshAllNameWidgets(formDesign.getParaComponent());
        XCreatorUtils.refreshAllNameWidgets(formDesign.getRootComponent());
    }

    public void refreshSelectedWidget()
    {
        formDesign.getEditListenerTable().fireCreatorModified(7);
    }

    public void copy()
    {
        formDesign.copy();
    }

    public boolean paste()
    {
        return formDesign.paste();
    }

    public boolean cut()
    {
        return formDesign.cut();
    }

    public MenuDef[] menus4Target()
    {
        return index != 0 ? (MenuDef[])(MenuDef[])ArrayUtils.addAll(super.menus4Target(), elementCaseDesign.menus4Target()) : (MenuDef[])(MenuDef[])ArrayUtils.addAll(super.menus4Target(), formDesign.menus4Target());
    }

    public ToolBarDef[] toolbars4Target()
    {
        return index != 0 ? elementCaseDesign.toolbars4Target() : formDesign.toolbars4Target();
    }

    public ShortCut[] shortcut4TemplateMenu()
    {
        return index != 0 ? elementCaseDesign.shortcut4TemplateMenu() : new ShortCut[0];
    }

    public ShortCut[] shortCuts4Authority()
    {
        return new ShortCut[0];
    }

    protected FormUndoState createUndoState()
    {
        FormUndoState formundostate = new FormUndoState(this, formDesign.getArea());
        if(formDesign.isReportBlockEditing())
            formundostate.setFormReportType(3);
        return formundostate;
    }

    public void applyUndoState4Form(BaseUndoState baseundostate)
    {
        applyUndoState((FormUndoState)baseundostate);
    }

    public boolean canUndo()
    {
        boolean flag = ((FormUndoState)undoState).getFormReportType() == 4 && formDesign.isReportBlockEditing();
        return !flag && getUndoManager().canUndo();
    }

    protected void applyUndoState(FormUndoState formundostate)
    {
        try
        {
            setTarget((Form)formundostate.getForm().clone());
            if(index == 0)
            {
                refreshRoot();
                formDesign.getArea().setAreaSize(formundostate.getAreaSize(), formundostate.getHorizontalValue(), formundostate.getVerticalValue(), formundostate.getWidthValue(), formundostate.getHeightValue(), formundostate.getSlideValue());
                formDesign.getSelectionModel().setSelectedCreators(FormSelectionUtils.rebuildSelection(formDesign.getRootComponent(), formundostate.getSelectWidgets()));
            } else
            {
                String s = formDesign.getElementCaseContainerName();
                FormElementCaseProvider formelementcaseprovider = ((Form)getTarget()).getElementCaseByName(s);
                reportComposite.setSelectedWidget(formelementcaseprovider);
                formDesign.setElementCase(formelementcaseprovider);
            }
        }
        catch(CloneNotSupportedException clonenotsupportedexception)
        {
            throw new RuntimeException(clonenotsupportedexception);
        }
        if(BaseUtils.isAuthorityEditing())
            authorityUndoState = formundostate;
        else
            undoState = formundostate;
    }

    protected FormModelAdapter createDesignModel()
    {
        return new FormModelAdapter(this);
    }

    public JPanel[] toolbarPanes4Form()
    {
        return index != 0 ? new JPanel[0] : (new JPanel[] {
            FormParaWidgetPane.getInstance(formDesign)
        });
    }

    public JComponent[] toolBarButton4Form()
    {
        return index != 0 ? elementCaseDesign.toolBarButton4Form() : (new JComponent[] {
            (new FormDeleteAction(formDesign)).createToolBarComponent()
        });
    }

    public JComponent toolBar4Authority()
    {
        JPanel jpanel = new JPanel(new BorderLayout()) {

            final JForm this$0;

            public Dimension getPreferredSize()
            {
                Dimension dimension = super.getPreferredSize();
                dimension.height = 26;
                return dimension;
            }

            
            {
                this$0 = JForm.this;
                super(layoutmanager);
            }
        }
;
        UILabel uilabel = new UILabel(Inter.getLocText(new String[] {
            "DashBoard-FormBook", "Privilege", "Edit"
        }));
        uilabel.setHorizontalAlignment(0);
        uilabel.setFont(new Font(Inter.getLocText("FR-Designer-All_MSBold"), 0, 14));
        uilabel.setForeground(new Color(150, 150, 150));
        jpanel.add(uilabel, "Center");
        return jpanel;
    }

    public JPanel getEastUpPane()
    {
        if(BaseUtils.isAuthorityEditing())
            if(formDesign.isSupportAuthority())
                return new AuthorityPropertyPane(this);
            else
                return new NoSupportAuthorityEdit();
        if(editingComponent == null)
            editingComponent = formDesign.getRootComponent().createToolPane(this, formDesign);
        return (JPanel)editingComponent;
    }

    public JPanel getEastDownPane()
    {
        return formDesign.getEastDownPane();
    }

    public Icon getPreviewLargeIcon()
    {
        return UIConstants.RUN_BIG_ICON;
    }

    public UIMenuItem[] createMenuItem4Preview()
    {
        UIMenuItem uimenuitem = new UIMenuItem(Inter.getLocText("M-Form_Preview"), UIConstants.RUN_SMALL_ICON);
        uimenuitem.addActionListener(new ActionListener() {

            final JForm this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                WebPreviewUtils.onFormPreview(JForm.this);
            }

            
            {
                this$0 = JForm.this;
                super();
            }
        }
);
        return (new UIMenuItem[] {
            uimenuitem
        });
    }

    public void populateParameter()
    {
        formDesign.populateParameterPropertyPane();
    }

    public void refreshToolArea()
    {
        populateParameter();
        DesignerContext.getDesignerFrame().resetToolkitByPlus(this);
        WidgetToolBarPane.getInstance(formDesign);
        if(BaseUtils.isAuthorityEditing())
        {
            if(formDesign.isSupportAuthority())
                EastRegionContainerPane.getInstance().replaceUpPane(new AuthorityPropertyPane(this));
            else
                EastRegionContainerPane.getInstance().replaceUpPane(new NoSupportAuthorityEdit());
            EastRegionContainerPane.getInstance().replaceDownPane(RolesAlreadyEditedPane.getInstance());
            return;
        }
        if(formDesign.isReportBlockEditing() && elementCaseDesign != null)
        {
            EastRegionContainerPane.getInstance().replaceDownPane(elementCaseDesign.getEastDownPane());
            EastRegionContainerPane.getInstance().replaceUpPane(elementCaseDesign.getEastUpPane());
            return;
        }
        EastRegionContainerPane.getInstance().replaceUpPane(WidgetPropertyPane.getInstance(formDesign));
        if(EastRegionContainerPane.getInstance().getDownPane() == null)
        {
            (new Thread() {

                final JForm this$0;

                public void run()
                {
                    try
                    {
                        Thread.sleep(1500L);
                    }
                    catch(InterruptedException interruptedexception)
                    {
                        FRLogger.getLogger().error(interruptedexception.getMessage(), interruptedexception);
                    }
                    JPanel jpanel1 = new JPanel();
                    jpanel1.setLayout(new BorderLayout());
                    jpanel1.add(FormWidgetDetailPane.getInstance(formDesign), "Center");
                    EastRegionContainerPane.getInstance().replaceDownPane(jpanel1);
                }

            
            {
                this$0 = JForm.this;
                super();
            }
            }
).start();
        } else
        {
            JPanel jpanel = new JPanel();
            jpanel.setLayout(new BorderLayout());
            jpanel.add(FormWidgetDetailPane.getInstance(formDesign), "Center");
            EastRegionContainerPane.getInstance().replaceDownPane(jpanel);
        }
    }

    public String getEditingCreatorName()
    {
        return formDesign.getSelectionModel().getSelection().getSelectedCreator().toData().getWidgetName();
    }

    public WLayout getRootLayout()
    {
        return formDesign.getRootComponent().toData();
    }

    public boolean isSelectRootPane()
    {
        return formDesign.getRootComponent() == formDesign.getSelectionModel().getSelection().getSelectedCreator();
    }

    public void tabChanged(int i)
    {
        if(i == 1)
        {
            formDesign.setReportBlockEditing(true);
            ecTabAction();
        } else
        {
            formDesign.setReportBlockEditing(false);
            formTabAction();
        }
        index = i;
        refreshToolArea();
        cardLayout.show(tabCenterPane, CARDNAME[i]);
        if(elementCaseDesign != null && i == 1)
        {
            elementCaseDesign.requestFocus();
            fireTargetModified();
        }
    }

    public void tabChanged(int i, FormElementCaseContainerProvider formelementcasecontainerprovider)
    {
        if(i == 2)
        {
            saveImage();
            formDesign.setElementCaseContainer(formelementcasecontainerprovider);
            return;
        } else
        {
            tabChanged(i);
            return;
        }
    }

    private FormECDesignerProvider initElementCaseDesign()
    {
        HashMap hashmap = new HashMap();
        hashmap.put("0", com/fr/form/FormElementCaseProvider);
        Object aobj[] = {
            formDesign.getElementCase()
        };
        return (FormECDesignerProvider)StableFactory.getMarkedInstanceObjectFromClass("FormElementCaseDesigner", aobj, hashmap, com/fr/design/mainframe/form/FormECDesignerProvider);
    }

    private FormECCompositeProvider initComposite()
    {
        Object aobj[] = {
            this, elementCaseDesign, formDesign.getElementCaseContainer()
        };
        HashMap hashmap = new HashMap();
        hashmap.put("0", com/fr/design/mainframe/BaseJForm);
        hashmap.put("2", com/fr/form/FormElementCaseContainerProvider);
        return (FormECCompositeProvider)StableFactory.getMarkedInstanceObjectFromClass("FormReportComponentComposite", aobj, hashmap, com/fr/design/mainframe/form/FormECCompositeProvider);
    }

    private void ecTabAction()
    {
        elementCaseDesign = initElementCaseDesign();
        reportComposite = initComposite();
        tabCenterPane.add((Component)reportComposite, "ELEMENTCASE", 1);
        reportComposite.addTargetModifiedListener(new TargetModifiedListener() {

            final JForm this$0;

            public void targetModified(TargetModifiedEvent targetmodifiedevent)
            {
                fireTargetModified();
                FormElementCaseProvider formelementcaseprovider = elementCaseDesign.getEditingElementCase();
                formDesign.setElementCase(formelementcaseprovider);
            }

            
            {
                this$0 = JForm.this;
                super();
            }
        }
);
    }

    private void saveImage()
    {
        fireTargetModified();
        java.awt.image.BufferedImage bufferedimage = elementCaseDesign.getElementCaseImage(formDesign.getSize());
        formDesign.setElementCaseBackground(bufferedimage);
    }

    private void formTabAction()
    {
        saveImage();
    }

    public Icon getIcon()
    {
        return BaseUtils.readIcon("/com/fr/web/images/form/new_form3.png");
    }

    protected volatile void applyUndoState(BaseUndoState baseundostate)
    {
        applyUndoState((FormUndoState)baseundostate);
    }

    protected volatile BaseUndoState createUndoState()
    {
        return createUndoState();
    }

    protected volatile DesignModelAdapter createDesignModel()
    {
        return createDesignModel();
    }

    protected volatile JComponent createCenterPane()
    {
        return createCenterPane();
    }




}
