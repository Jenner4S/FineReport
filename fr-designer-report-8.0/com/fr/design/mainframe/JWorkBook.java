// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.base.Parameter;
import com.fr.design.DesignModelAdapter;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.actions.AllowAuthorityEditAction;
import com.fr.design.actions.ExitAuthorityEditAction;
import com.fr.design.actions.TemplateComponentAction;
import com.fr.design.actions.file.WebPreviewUtils;
import com.fr.design.actions.file.export.CSVExportAction;
import com.fr.design.actions.file.export.EmbeddedExportExportAction;
import com.fr.design.actions.file.export.ExcelExportAction;
import com.fr.design.actions.file.export.PDFExportAction;
import com.fr.design.actions.file.export.PageExcelExportAction;
import com.fr.design.actions.file.export.PageToSheetExcelExportAction;
import com.fr.design.actions.file.export.SVGExportAction;
import com.fr.design.actions.file.export.TextExportAction;
import com.fr.design.actions.file.export.WordExportAction;
import com.fr.design.actions.report.ReportExportAttrAction;
import com.fr.design.actions.report.ReportParameterAction;
import com.fr.design.actions.report.ReportWebAttrAction;
import com.fr.design.constants.UIConstants;
import com.fr.design.data.datapane.TableDataTreePane;
import com.fr.design.designer.EditingState;
import com.fr.design.designer.TargetComponent;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.file.MutilTempalteTabPane;
import com.fr.design.fun.PreviewProvider;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIModeControlContainer;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.mainframe.cell.QuickEditorRegion;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.menu.NameSeparator;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.parameter.HierarchyTreePane;
import com.fr.design.parameter.ParameterDefinitePane;
import com.fr.design.parameter.ParameterDesignerProvider;
import com.fr.design.parameter.ParameterInputPane;
import com.fr.design.preview.PagePreview;
import com.fr.design.preview.ViewPreview;
import com.fr.design.preview.WritePreview;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import com.fr.design.roleAuthority.RoleTree;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;
import com.fr.design.selection.QuickEditor;
import com.fr.design.write.submit.DBManipulationPane;
import com.fr.design.write.submit.SmartInsertDBManipulationInWidgetEventPane;
import com.fr.design.write.submit.SmartInsertDBManipulationPane;
import com.fr.env.RemoteEnv;
import com.fr.file.FILE;
import com.fr.file.FileNodeFILE;
import com.fr.file.filetree.FileNode;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.general.ModuleContext;
import com.fr.grid.Grid;
import com.fr.io.exporter.EmbeddedTableDataExporter;
import com.fr.main.TemplateWorkBook;
import com.fr.main.impl.WorkBook;
import com.fr.poly.PolyDesigner;
import com.fr.privilege.finegrain.WorkSheetPrivilegeControl;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.report.TemplateReport;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StableUtils;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe:
//            JTemplate, ReportComponentComposite, ElementCasePane, SheetAuthorityEditPane, 
//            AuthoritySheetEditedPane, NoSupportAuthorityEdit, AuthorityPropertyPane, ReportComponent, 
//            WorkBookUndoState, WorkBookModelAdapter, AuthorityToolBarPane, SheetNameTabPane, 
//            DesignerFrame, ReportComponentCardPane, DesignerContext, CellElementPropertyPane, 
//            EastRegionContainerPane, AuthorityEditPane, BaseUndoState

public class JWorkBook extends JTemplate
{

    private static final String SHARE_SUFFIX = "_share";
    private static final String SHARE_FOLDER = "share";
    private static final int TOOLBARPANEDIMHEIGHT = 26;
    private UIModeControlContainer centerPane;
    private ReportComponentComposite reportComposite;
    private ParameterDefinitePane parameterPane;

    public JWorkBook()
    {
        super(new WorkBook(new WorkSheet()), "WorkBook");
        populateReportParameterAttr();
    }

    public JWorkBook(WorkBook workbook, String s)
    {
        super(workbook, s);
        populateReportParameterAttr();
    }

    public JWorkBook(WorkBook workbook, FILE file)
    {
        super(workbook, file);
        populateReportParameterAttr();
    }

    protected UIModeControlContainer createCenterPane()
    {
        parameterPane = ModuleContext.isModuleStarted("com.fr.form.module.FormModule") ? new ParameterDefinitePane() : null;
        centerPane = new UIModeControlContainer(parameterPane, reportComposite = new ReportComponentComposite(this)) {

            final JWorkBook this$0;

            protected void onModeChanged()
            {
                refreshToolArea();
            }

            protected void onResize(int i)
            {
                if(hasParameterPane())
                {
                    parameterPane.setDesignHeight(i);
                    fireTargetModified();
                }
            }

            
            {
                this$0 = JWorkBook.this;
                super(jcomponent, jcomponent1);
            }
        }
;
        reportComposite.addTargetModifiedListener(new TargetModifiedListener() {

            final JWorkBook this$0;

            public void targetModified(TargetModifiedEvent targetmodifiedevent)
            {
                fireTargetModified();
            }

            
            {
                this$0 = JWorkBook.this;
                super();
            }
        }
);
        reportComposite.setParentContainer(centerPane);
        return centerPane;
    }

    public void judgeSheetAuthority(String s)
    {
        boolean flag = reportComposite.getEditingTemplateReport().getWorkSheetPrivilegeControl().checkInvisible(s);
        centerPane.setSheeetCovered(flag);
        centerPane.refreshContainer();
    }

    public void doConditionCancelFormat()
    {
        if(ComparatorUtils.equals(reportComposite.centerCardPane.editingComponet.elementCasePane, DesignerContext.getReferencedElementCasePane()))
            cancelFormat();
    }

    public void cancelFormat()
    {
        DesignerContext.setFormatState(0);
        reportComposite.centerCardPane.editingComponet.elementCasePane.getGrid().setCursor(UIConstants.CELL_DEFAULT_CURSOR);
        ((ElementCasePane)DesignerContext.getReferencedElementCasePane()).getGrid().setCursor(UIConstants.CELL_DEFAULT_CURSOR);
        ((ElementCasePane)DesignerContext.getReferencedElementCasePane()).getGrid().setNotShowingTableSelectPane(true);
        DesignerContext.setReferencedElementCasePane(null);
        DesignerContext.setReferencedIndex(0);
        repaint();
    }

    public int getEditingReportIndex()
    {
        return reportComposite.getEditingIndex();
    }

    public AuthorityEditPane createAuthorityEditPane()
    {
        if(centerPane.isUpEditMode())
            return parameterPane.getParaDesigner().getAuthorityEditPane();
        WorkSheetPrivilegeControl worksheetprivilegecontrol = reportComposite.getEditingTemplateReport().getWorkSheetPrivilegeControl();
        if(worksheetprivilegecontrol.checkInvisible(ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName()))
        {
            SheetAuthorityEditPane sheetauthorityeditpane = new SheetAuthorityEditPane(reportComposite.getEditingWorkBook(), getEditingReportIndex());
            sheetauthorityeditpane.populateDetials();
            return sheetauthorityeditpane;
        } else
        {
            return reportComposite.getEditingReportComponent().createAuthorityEditPane();
        }
    }

    public ToolBarMenuDockPlus getToolBarMenuDockPlus()
    {
        if(getEditingElementCasePane() == null)
            return this;
        getEditingElementCasePane().getGrid().setEditable(!BaseUtils.isAuthorityEditing());
        centerPane.needToShowCoverAndHidPane();
        if(centerPane.isUpEditMode())
            return parameterPane;
        else
            return this;
    }

    private boolean hasParameterPane()
    {
        return parameterPane != null;
    }

    public void setAutoHeightForCenterPane()
    {
        centerPane.setUpPaneHeight(hasParameterPane() ? parameterPane.getPreferredSize().height : 0);
    }

    public void setComposite()
    {
        super.setComposite();
        reportComposite.setComponents();
    }

    public JPanel getEastUpPane()
    {
        if(BaseUtils.isAuthorityEditing())
            return allowAuthorityUpPane();
        else
            return exitEastUpPane();
    }

    private JPanel allowAuthorityUpPane()
    {
        boolean flag = centerPane.isUpEditMode() && !parameterPane.getParaDesigner().isSupportAuthority();
        boolean flag1 = (reportComposite.getEditingReportComponent() instanceof PolyDesigner) && !((PolyDesigner)reportComposite.getEditingReportComponent()).isSelectedECBolck();
        WorkSheetPrivilegeControl worksheetprivilegecontrol = reportComposite.getEditingTemplateReport().getWorkSheetPrivilegeControl();
        if(!centerPane.isUpEditMode() && worksheetprivilegecontrol.checkInvisible(ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName()))
        {
            AuthoritySheetEditedPane authoritysheeteditedpane = new AuthoritySheetEditedPane(reportComposite.getEditingWorkBook(), getEditingReportIndex());
            authoritysheeteditedpane.populate();
            return authoritysheeteditedpane;
        }
        boolean flag2 = !centerPane.isUpEditMode() && flag1;
        if(flag || flag2)
            return new NoSupportAuthorityEdit();
        else
            return new AuthorityPropertyPane(this);
    }

    private JPanel exitEastUpPane()
    {
        if(centerPane.isUpEditMode())
            return parameterPane.getParaDesigner().getEastUpPane();
        if(delegate4ToolbarMenuAdapter() instanceof PolyDesigner)
            return ((PolyDesigner)delegate4ToolbarMenuAdapter()).getEastUpPane();
        ElementCasePane elementcasepane = ((ReportComponent)delegate4ToolbarMenuAdapter()).elementCasePane;
        if(elementcasepane != null)
            return elementcasepane.getEastUpPane();
        else
            return new JPanel();
    }

    public JPanel getEastDownPane()
    {
        if(centerPane.isUpEditMode())
            return parameterPane.getParaDesigner().getEastDownPane();
        if(delegate4ToolbarMenuAdapter() instanceof PolyDesigner)
            if(((PolyDesigner)delegate4ToolbarMenuAdapter()).getSelectionType() == com.fr.poly.PolyDesigner.SelectionType.NONE)
                return new JPanel();
            else
                return ((PolyDesigner)delegate4ToolbarMenuAdapter()).getEastDownPane();
        ElementCasePane elementcasepane = ((ReportComponent)delegate4ToolbarMenuAdapter()).elementCasePane;
        if(elementcasepane != null)
            return elementcasepane.getEastDownPane();
        else
            return new JPanel();
    }

    public void removeTemplateSelection()
    {
        reportComposite.removeSelection();
    }

    public void setSheetCovered(boolean flag)
    {
        centerPane.setSheeetCovered(flag);
    }

    public void refreshContainer()
    {
        centerPane.refreshContainer();
    }

    public void removeParameterPaneSelection()
    {
        parameterPane.getParaDesigner().removeSelection();
    }

    public int getToolBarHeight()
    {
        return 26;
    }

    public void populateReportParameterAttr()
    {
        if(hasParameterPane())
        {
            parameterPane.populate(this);
            setAutoHeightForCenterPane();
        }
    }

    public void updateReportParameterAttr()
    {
        if(hasParameterPane())
        {
            com.fr.main.parameter.ReportParameterAttr reportparameterattr = parameterPane.update(((WorkBook)getTarget()).getReportParameterAttr());
            ((WorkBook)getTarget()).setReportParameterAttr(reportparameterattr);
        }
    }

    public void checkHasSubmitButton()
    {
        if(parameterPane != null)
            parameterPane.checkSubmitButton();
    }

    public void setTarget(WorkBook workbook)
    {
        if(workbook == null)
            return;
        if(workbook.getReportCount() == 0)
            workbook.addReport(new WorkSheet());
        super.setTarget(workbook);
    }

    private TargetComponent delegate4ToolbarMenuAdapter()
    {
        return reportComposite.getEditingReportComponent();
    }

    public void copy()
    {
        delegate4ToolbarMenuAdapter().copy();
    }

    public boolean cut()
    {
        return delegate4ToolbarMenuAdapter().cut();
    }

    public boolean paste()
    {
        return delegate4ToolbarMenuAdapter().paste();
    }

    public void stopEditing()
    {
        reportComposite.stopEditing();
        if(!isSaved())
        {
            updateReportParameterAttr();
            delegate4ToolbarMenuAdapter().stopEditing();
        }
    }

    public String suffix()
    {
        return ".cpt";
    }

    public ShortCut[] shortcut4FileMenu()
    {
        return (ShortCut[])(ShortCut[])ArrayUtils.addAll(super.shortcut4FileMenu(), !BaseUtils.isAuthorityEditing() && !(FRContext.getCurrentEnv() instanceof RemoteEnv) ? ((Object []) (new ShortCut[] {
            createWorkBookExportMenu()
        })) : ((Object []) (new ShortCut[0])));
    }

    public MenuDef[] menus4Target()
    {
        return (MenuDef[])(MenuDef[])ArrayUtils.addAll(super.menus4Target(), delegate4ToolbarMenuAdapter().menus4Target());
    }

    public int getMenuState()
    {
        return delegate4ToolbarMenuAdapter().getMenuState();
    }

    private MenuDef createWorkBookExportMenu()
    {
        MenuDef menudef = new MenuDef(KeySetUtils.EXCEL_EXPORT.getMenuKeySetName(), KeySetUtils.EXCEL_EXPORT.getMnemonic());
        menudef.setIconPath("/com/fr/design/images/m_file/excel.png");
        menudef.addShortCut(new ShortCut[] {
            new PageExcelExportAction(this), new ExcelExportAction(this), new PageToSheetExcelExportAction(this)
        });
        MenuDef menudef1 = new MenuDef(KeySetUtils.EXPORT.getMenuName());
        menudef1.setIconPath("/com/fr/design/images/m_file/export.png");
        menudef1.addShortCut(new ShortCut[] {
            menudef, new PDFExportAction(this), new WordExportAction(this), new SVGExportAction(this), new CSVExportAction(this), new TextExportAction(this), new EmbeddedExportExportAction(this)
        });
        return menudef1;
    }

    public ShortCut[] shortCuts4Authority()
    {
        return (new ShortCut[] {
            new NameSeparator(Inter.getLocText(new String[] {
                "DashBoard-Potence", "Edit"
            })), BaseUtils.isAuthorityEditing() ? new ExitAuthorityEditAction(this) : new AllowAuthorityEditAction(this)
        });
    }

    public ShortCut[] shortcut4TemplateMenu()
    {
        return (ShortCut[])(ShortCut[])ArrayUtils.addAll(new ShortCut[] {
            new ReportWebAttrAction(this), new ReportExportAttrAction(this), new ReportParameterAction(this), new NameSeparator(Inter.getLocText("Utils-Current_Sheet"))
        }, reportComposite.getEditingReportComponent().shortcut4TemplateMenu());
    }

    public ToolBarDef[] toolbars4Target()
    {
        return delegate4ToolbarMenuAdapter().toolbars4Target();
    }

    protected WorkBookUndoState createUndoState()
    {
        return new WorkBookUndoState(this, reportComposite.getSelectedIndex(), reportComposite.getEditingReportComponent().createEditingState());
    }

    protected void applyUndoState(WorkBookUndoState workbookundostate)
    {
        try
        {
            setTarget((WorkBook)workbookundostate.getWorkBook().clone());
            if(BaseUtils.isAuthorityEditing())
                break MISSING_BLOCK_LABEL_117;
            if(workbookundostate.getAuthorityType() != 0)
            {
                applyAll(workbookundostate);
                undoState = workbookundostate;
                return;
            }
        }
        catch(CloneNotSupportedException clonenotsupportedexception)
        {
            throw new RuntimeException(clonenotsupportedexception);
        }
        if(centerPane.isUpEditMode())
        {
            if(hasParameterPane())
            {
                parameterPane.populate((JWorkBook)workbookundostate.getApplyTarget());
                DesignModuleFactory.getFormHierarchyPane().refreshRoot();
            }
        } else
        {
            reportComposite.setSelectedIndex(workbookundostate.getSelectedReportIndex());
            workbookundostate.getSelectedEditingState().revert();
            TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter()).refreshDockingView();
        }
        undoState = workbookundostate;
        break MISSING_BLOCK_LABEL_140;
        applyAll(workbookundostate);
        authorityUndoState = workbookundostate;
        break MISSING_BLOCK_LABEL_140;
    }

    private void applyAll(WorkBookUndoState workbookundostate)
    {
        if(hasParameterPane())
        {
            parameterPane.populate((JWorkBook)workbookundostate.getApplyTarget());
            DesignModuleFactory.getFormHierarchyPane().refreshRoot();
        }
        reportComposite.setSelectedIndex(workbookundostate.getSelectedReportIndex());
        workbookundostate.getSelectedEditingState().revert();
        TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter()).refreshDockingView();
        DesignerContext.getDesignerFrame().resetToolkitByPlus(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate());
    }

    public void requestFocus()
    {
        super.requestFocus();
        ReportComponent reportcomponent = reportComposite.getEditingReportComponent();
        reportcomponent.requestFocus();
    }

    public final TemplateElementCase getEditingElementCase()
    {
        return reportComposite.getEditingReportComponent().getEditingElementCasePane().getEditingElementCase();
    }

    public final ElementCasePane getEditingElementCasePane()
    {
        return reportComposite.getEditingReportComponent().getEditingElementCasePane();
    }

    public void refreshAllNameWidgets()
    {
        if(parameterPane != null)
            parameterPane.refreshAllNameWidgets();
    }

    public void refreshParameterPane4TableData(String s, String s1)
    {
        if(parameterPane != null)
            parameterPane.refresh4TableData(s, s1);
    }

    public void revert()
    {
        ElementCasePane elementcasepane = reportComposite.getEditingReportComponent().elementCasePane;
        if(elementcasepane == null)
            return;
        if(delegate4ToolbarMenuAdapter() instanceof PolyDesigner)
        {
            PolyDesigner polydesigner = (PolyDesigner)delegate4ToolbarMenuAdapter();
            if(polydesigner.getSelectionType() == com.fr.poly.PolyDesigner.SelectionType.NONE || polydesigner.getSelection() == null)
                QuickEditorRegion.getInstance().populate(QuickEditor.DEFAULT_EDITOR);
            else
                QuickEditorRegion.getInstance().populate(elementcasepane.getCurrentEditor());
        } else
        {
            QuickEditorRegion.getInstance().populate(elementcasepane.getCurrentEditor());
        }
        CellElementPropertyPane.getInstance().populate(elementcasepane);
    }

    protected WorkBookModelAdapter createDesignModel()
    {
        return new WorkBookModelAdapter(this);
    }

    public JPanel[] toolbarPanes4Form()
    {
        if(centerPane.isUpEditMode() && hasParameterPane())
            return parameterPane.toolbarPanes4Form();
        else
            return new JPanel[0];
    }

    public JComponent[] toolBarButton4Form()
    {
        centerPane.needToShowCoverAndHidPane();
        if(centerPane.isUpEditMode() && hasParameterPane())
            return parameterPane.toolBarButton4Form();
        else
            return delegate4ToolbarMenuAdapter().toolBarButton4Form();
    }

    public JComponent toolBar4Authority()
    {
        return new AuthorityToolBarPane();
    }

    public PreviewProvider[] supportPreview()
    {
        return (PreviewProvider[])(PreviewProvider[])ArrayUtils.addAll(new PreviewProvider[] {
            new PagePreview(), new WritePreview(), new ViewPreview()
        }, ExtraDesignClassManager.getInstance().getPreviewProviders());
    }

    public UIMenuItem[] createMenuItem4Preview()
    {
        ArrayList arraylist = new ArrayList();
        PreviewProvider apreviewprovider[] = supportPreview();
        PreviewProvider apreviewprovider1[] = apreviewprovider;
        int i = apreviewprovider1.length;
        for(int j = 0; j < i; j++)
        {
            final PreviewProvider provider = apreviewprovider1[j];
            UIMenuItem uimenuitem = new UIMenuItem(provider.nameForPopupItem(), BaseUtils.readIcon(provider.iconPathForPopupItem()));
            uimenuitem.addActionListener(new java.awt.event.ActionListener() {

                final PreviewProvider val$provider;
                final JWorkBook this$0;

                public void actionPerformed(java.awt.event.ActionEvent actionevent)
                {
                    provider.onClick(JWorkBook.this);
                }

            
            {
                this$0 = JWorkBook.this;
                provider = previewprovider;
                super();
            }
            }
);
            arraylist.add(uimenuitem);
        }

        return (UIMenuItem[])arraylist.toArray(new UIMenuItem[arraylist.size()]);
    }

    public void previewMenuActionPerformed(PreviewProvider previewprovider)
    {
        setPreviewType(previewprovider);
        WebPreviewUtils.actionPerformed(this, previewprovider.parametersForPreview(), "reportlet");
    }

    public boolean isJWorkBook()
    {
        return true;
    }

    public void setAuthorityMode(boolean flag)
    {
        centerPane.setAuthorityMode(flag);
    }

    public boolean isUpMode()
    {
        return centerPane.isUpEditMode();
    }

    public void refreshToolArea()
    {
        populateReportParameterAttr();
        if(centerPane.isUpEditMode())
        {
            if(hasParameterPane())
            {
                DesignerContext.getDesignerFrame().resetToolkitByPlus(parameterPane);
                parameterPane.initBeforeUpEdit();
            }
        } else
        {
            DesignerContext.getDesignerFrame().resetToolkitByPlus(this);
            if(delegate4ToolbarMenuAdapter() instanceof PolyDesigner)
            {
                PolyDesigner polydesigner = (PolyDesigner)delegate4ToolbarMenuAdapter();
                if(polydesigner.getSelectionType() == com.fr.poly.PolyDesigner.SelectionType.NONE || polydesigner.getSelection() == null)
                {
                    EastRegionContainerPane.getInstance().replaceDownPane(new JPanel());
                    QuickEditorRegion.getInstance().populate(QuickEditor.DEFAULT_EDITOR);
                } else
                {
                    EastRegionContainerPane.getInstance().replaceDownPane(CellElementPropertyPane.getInstance());
                }
                EastRegionContainerPane.getInstance().replaceUpPane(QuickEditorRegion.getInstance());
            } else
            {
                ElementCasePane elementcasepane = ((ReportComponent)delegate4ToolbarMenuAdapter()).elementCasePane;
                if(elementcasepane != null)
                    elementcasepane.fireSelectionChangeListener();
            }
        }
        if(BaseUtils.isAuthorityEditing())
        {
            EastRegionContainerPane.getInstance().replaceUpPane(allowAuthorityUpPane());
            EastRegionContainerPane.getInstance().replaceDownPane(RolesAlreadyEditedPane.getInstance());
        }
        centerPane.needToShowCoverAndHidPane();
    }

    public Icon getPreviewLargeIcon()
    {
        PreviewProvider previewprovider = getPreviewType();
        String s = previewprovider.iconPathForLarge();
        return BaseUtils.readIcon(s);
    }

    public Parameter[] getParameters()
    {
        Parameter aparameter[] = parameterPane.getParameterArray();
        Parameter aparameter1[] = parameterPane.getAllParameters();
        for(int i = 0; i < aparameter.length; i++)
        {
            for(int j = 0; j < aparameter1.length; j++)
                if(ComparatorUtils.equals(aparameter[i].getName(), aparameter1[j].getName()))
                    aparameter[i].setValue(aparameter1[j].getValue());

        }

        return aparameter;
    }

    public void requestGridFocus()
    {
        reportComposite.centerCardPane.requestGrifFocus();
    }

    public DBManipulationPane createDBManipulationPane()
    {
        ElementCasePane elementcasepane = getEditingElementCasePane();
        return new SmartInsertDBManipulationPane(elementcasepane);
    }

    public DBManipulationPane createDBManipulationPaneInWidget()
    {
        ElementCasePane elementcasepane = getEditingElementCasePane();
        return new SmartInsertDBManipulationInWidgetEventPane(elementcasepane);
    }

    public Icon getIcon()
    {
        return BaseUtils.readIcon("/com/fr/design/images/buttonicon/newcpts.png");
    }

    public SheetNameTabPane createSheetNameTabPane(ReportComponentComposite reportcomponentcomposite)
    {
        return new SheetNameTabPane(reportcomponentcomposite);
    }

    public boolean saveShareFile()
    {
        FILE file = createNewEmptyFile();
        MutilTempalteTabPane.getInstance().closeFileTemplate(file);
        TemplateWorkBook templateworkbook = (TemplateWorkBook)getTarget();
        Map map = inputParameters(templateworkbook);
        try
        {
            String s = StableUtils.pathJoin(new String[] {
                FRContext.getCurrentEnv().getPath(), file.getPath()
            });
            FileOutputStream fileoutputstream = new FileOutputStream(s);
            EmbeddedTableDataExporter embeddedtabledataexporter = new EmbeddedTableDataExporter();
            embeddedtabledataexporter.export(fileoutputstream, (WorkBook)templateworkbook, map);
        }
        catch(Exception exception)
        {
            FRContext.getLogger().error(exception.getMessage());
        }
        DesignerContext.getDesignerFrame().openTemplate(file);
        return true;
    }

    private FILE createNewEmptyFile()
    {
        String s = getEditingFILE().getName();
        s = s.replaceAll(".cpt", "");
        String s1 = (new StringBuilder()).append(s).append("_share").toString();
        String s2 = StableUtils.pathJoin(new String[] {
            "reportlets", "share", s1, (new StringBuilder()).append(s1).append(".cpt").toString()
        });
        FileNode filenode = new FileNode(s2, false);
        FileNodeFILE filenodefile = new FileNodeFILE(filenode);
        mkNewFile(filenodefile);
        return filenodefile;
    }

    private Map inputParameters(TemplateWorkBook templateworkbook)
    {
        final HashMap parameterMap = new HashMap();
        DesignerFrame designerframe = DesignerContext.getDesignerFrame();
        Parameter aparameter[] = templateworkbook.getParameters();
        if(!ArrayUtils.isEmpty(aparameter))
        {
            final ParameterInputPane pPane = new ParameterInputPane(aparameter);
            pPane.showSmallWindow(designerframe, new DialogActionAdapter() {

                final Map val$parameterMap;
                final ParameterInputPane val$pPane;
                final JWorkBook this$0;

                public void doOk()
                {
                    parameterMap.putAll(pPane.update());
                }

            
            {
                this$0 = JWorkBook.this;
                parameterMap = map;
                pPane = parameterinputpane;
                super();
            }
            }
).setVisible(true);
        }
        return parameterMap;
    }

    public UIButton[] createShareButton()
    {
        return new UIButton[0];
    }

    protected volatile void applyUndoState(BaseUndoState baseundostate)
    {
        applyUndoState((WorkBookUndoState)baseundostate);
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

    public volatile void setTarget(Object obj)
    {
        setTarget((WorkBook)obj);
    }


}
