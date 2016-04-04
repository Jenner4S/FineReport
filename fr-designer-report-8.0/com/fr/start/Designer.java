// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.start;

import com.fr.base.*;
import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.core.ActionUtils;
import com.fr.design.actions.file.WebPreviewUtils;
import com.fr.design.actions.file.newReport.NewPolyReportAction;
import com.fr.design.actions.file.newReport.NewWorkBookAction;
import com.fr.design.actions.server.*;
import com.fr.design.constants.UIConstants;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.file.MutilTempalteTabPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIPreviewButton;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.gui.itoolbar.UILargeToolbar;
import com.fr.design.mainframe.*;
import com.fr.design.mainframe.bbs.UserInfoLabel;
import com.fr.design.mainframe.bbs.UserInfoPane;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.*;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.module.DesignerModule;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.*;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.xml.XMLTools;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.MatteBorder;

// Referenced classes of package com.fr.start:
//            BaseDesigner, ReportSplashPane, CollectUserInformationDialog, SplashPane

public class Designer extends BaseDesigner
{

    private static final int TOOLBARPANEVGAP = -4;
    private static final int PREVIEW_DOWN_X_GAP = 92;
    private static final int GAP = 7;
    private static final String OLD_ENV_FOLDER_71 = ".FineReport71";
    private static final String OLD_ENV_FOLDER_70 = ".FineReport70";
    private UserInfoPane userInfoPane;
    private UIButton saveButton;
    private UIButton undo;
    private UIButton redo;
    private UIPreviewButton run;

    public static void main(String args[])
    {
        new Designer(args);
    }

    public Designer(String as[])
    {
        super(as);
    }

    protected String module2Start()
    {
        return com/fr/design/module/DesignerModule.getName();
    }

    public ShortCut[] createNewFileShortCuts()
    {
        ArrayList arraylist = new ArrayList();
        arraylist.add(new NewWorkBookAction());
        arraylist.add(new NewPolyReportAction());
        try
        {
            if(DesignModuleFactory.getNewFormAction() != null)
                arraylist.add((ShortCut)DesignModuleFactory.getNewFormAction().newInstance());
        }
        catch(Exception exception)
        {
            FRContext.getLogger().error(exception.getMessage());
        }
        return (ShortCut[])arraylist.toArray(new ShortCut[arraylist.size()]);
    }

    protected MenuDef createServerMenuDef(ToolBarMenuDockPlus toolbarmenudockplus)
    {
        MenuDef menudef = super.createServerMenuDef(toolbarmenudockplus);
        if(FRContext.getCurrentEnv() == null)
            return menudef;
        if(!BaseUtils.isAuthorityEditing())
        {
            menudef.addShortCut(new ShortCut[] {
                SeparatorDef.DEFAULT
            });
            if(FRContext.getCurrentEnv().isRoot())
            {
                menudef.addShortCut(new ShortCut[] {
                    new ServerConfigManagerAction(), new StyleListAction(), new WidgetManagerAction()
                });
                if(ActionUtils.getChartPreStyleAction() != null)
                    menudef.addShortCut(new ShortCut[] {
                        ActionUtils.getChartPreStyleAction()
                    });
            }
            insertMenu(menudef, "server");
        }
        return menudef;
    }

    public UILargeToolbar createLargeToolbar()
    {
        UILargeToolbar uilargetoolbar = super.createLargeToolbar();
        uilargetoolbar.setLayout(new FlowLayout(1, 0, 4));
        uilargetoolbar.add(new JPanel() {

            final Designer this$0;

            public Dimension getPreferredSize()
            {
                Dimension dimension = super.getPreferredSize();
                dimension.width = 1;
                return dimension;
            }

            
            {
                this$0 = Designer.this;
                super();
            }
        }
);
        createRunButton(uilargetoolbar);
        uilargetoolbar.add(run);
        uilargetoolbar.add(new JPanel() {

            final Designer this$0;

            public Dimension getPreferredSize()
            {
                Dimension dimension = super.getPreferredSize();
                dimension.width = 7;
                return dimension;
            }

            
            {
                this$0 = Designer.this;
                super();
            }
        }
);
        uilargetoolbar.addSeparator(new Dimension(2, 42));
        uilargetoolbar.setBorder(new MatteBorder(new Insets(0, 0, 1, 0), UIConstants.LINE_COLOR));
        return uilargetoolbar;
    }

    public UIButton[] createUp()
    {
        return (new UIButton[] {
            createSaveButton(), createUndoButton(), createRedoButton()
        });
    }

    private UIButton createSaveButton()
    {
        saveButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/save.png"));
        saveButton.setToolTipText(KeySetUtils.SAVE_TEMPLATE.getMenuKeySetName());
        saveButton.set4ToolbarButton();
        saveButton.addActionListener(new ActionListener() {

            final Designer this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                jtemplate.stopEditing();
                jtemplate.saveTemplate();
                jtemplate.requestFocus();
            }

            
            {
                this$0 = Designer.this;
                super();
            }
        }
);
        return saveButton;
    }

    private UIButton createUndoButton()
    {
        undo = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/undo.png"));
        undo.setToolTipText(KeySetUtils.UNDO.getMenuKeySetName());
        undo.set4ToolbarButton();
        undo.addActionListener(new ActionListener() {

            final Designer this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                if(jtemplate != null)
                    jtemplate.undo();
            }

            
            {
                this$0 = Designer.this;
                super();
            }
        }
);
        return undo;
    }

    private UIButton createRedoButton()
    {
        redo = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/redo.png"));
        redo.setToolTipText(KeySetUtils.REDO.getMenuKeySetName());
        redo.set4ToolbarButton();
        redo.addActionListener(new ActionListener() {

            final Designer this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                if(jtemplate != null)
                    jtemplate.redo();
            }

            
            {
                this$0 = Designer.this;
                super();
            }
        }
);
        return redo;
    }

    private void createRunButton(UILargeToolbar uilargetoolbar)
    {
        run = new UIPreviewButton(new UIButton(UIConstants.PAGE_BIG_ICON) {

            final Designer this$0;

            public Dimension getPreferredSize()
            {
                return new Dimension(34, 34);
            }

            
            {
                this$0 = Designer.this;
                super(icon);
            }
        }
, new UIButton(UIConstants.PREVIEW_DOWN) {

            final Designer this$0;

            public Dimension getPreferredSize()
            {
                return new Dimension(34, 10);
            }

            
            {
                this$0 = Designer.this;
                super(icon);
            }
        }
) {

            final Designer this$0;

            protected void upButtonClickEvent()
            {
                JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                if(jtemplate == null)
                    return;
                if(jtemplate instanceof JWorkBook)
                    WebPreviewUtils.onWorkbookPreview(jtemplate);
                else
                if(jtemplate instanceof BaseJForm)
                    WebPreviewUtils.onFormPreview(jtemplate);
            }

            protected void downButtonClickEvent()
            {
                JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                if(jtemplate == null)
                    return;
                UIPopupMenu uipopupmenu = new UIPopupMenu();
                com.fr.design.gui.imenu.UIMenuItem auimenuitem[] = jtemplate.createMenuItem4Preview();
                for(int i = 0; i < auimenuitem.length; i++)
                    uipopupmenu.add(auimenuitem[i]);

                GUICoreUtils.showPopupMenu(uipopupmenu, MutilTempalteTabPane.getInstance(), MutilTempalteTabPane.getInstance().getX() - 92, (MutilTempalteTabPane.getInstance().getY() - 1) + MutilTempalteTabPane.getInstance().getHeight());
            }

            public Dimension getPreferredSize()
            {
                return new Dimension(34, 46);
            }

            
            {
                this$0 = Designer.this;
                super(uibutton, uibutton1);
            }
        }
;
        run.setExtraPainted(false);
        run.set4Toolbar();
        run.getUpButton().setToolTipText(Inter.getLocText("FR-Designer_Preview"));
        run.getDownButton().setToolTipText(Inter.getLocText("FR-Designer_Dropdown-More-Preview"));
    }

    protected void refreshLargeToolbarState()
    {
        JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if(jtemplate == null)
            return;
        saveButton.setEnabled(!jtemplate.isSaved());
        MutilTempalteTabPane.getInstance().refreshOpenedTemplate(HistoryTemplateListPane.getInstance().getHistoryList());
        MutilTempalteTabPane.getInstance().repaint();
        if(DesignerEnvManager.getEnvManager().isSupportUndo())
        {
            undo.setEnabled(jtemplate.canUndo());
            redo.setEnabled(jtemplate.canRedo());
        } else
        {
            undo.setEnabled(false);
            redo.setEnabled(false);
        }
        run.getUpButton().setIcon(jtemplate.getPreviewLargeIcon());
    }

    public JComponent resetToolBar(JComponent jcomponent, ToolBarMenuDockPlus toolbarmenudockplus)
    {
        if(BaseUtils.isAuthorityEditing())
            if((toolbarmenudockplus instanceof JWorkBook) && toolbarmenudockplus.toolbars4Target() == null)
                return super.polyToolBar(Inter.getLocText(new String[] {
                    "Polybolck", "DashBoard-Potence", "Edit"
                }));
            else
                return toolbarmenudockplus.toolBar4Authority();
        if(toolbarmenudockplus.toolbarPanes4Form().length == 0)
            return super.resetToolBar(jcomponent, toolbarmenudockplus);
        JPanel jpanel = new JPanel(new FlowLayout(0, 0, -4));
        Dimension dimension = new Dimension();
        dimension.height = toolbarmenudockplus.getToolBarHeight();
        jpanel.setPreferredSize(dimension);
        jpanel.setFocusable(true);
        JPanel ajpanel[] = toolbarmenudockplus.toolbarPanes4Form();
        for(int i = 0; i < ajpanel.length; i++)
            jpanel.add(ajpanel[i]);

        return jpanel;
    }

    public JTemplate createNewTemplate()
    {
        return new JWorkBook();
    }

    public Component createBBSLoginPane()
    {
        if(userInfoPane == null)
            userInfoPane = new UserInfoPane();
        return userInfoPane;
    }

    protected SplashPane createSplashPane()
    {
        return new ReportSplashPane();
    }

    protected void collectUserInformation()
    {
        if(!ComparatorUtils.equals(ProductConstants.APP_NAME, "FineReport"))
            return;
        DesignerEnvManager designerenvmanager = DesignerEnvManager.getEnvManager();
        String s = designerenvmanager.getActivationKey();
        if(ActiveKeyGenerator.localVerify(s))
        {
            onLineVerify(designerenvmanager, s);
            UserInfoLabel.showBBSDialog();
            return;
        }
        if(StableUtils.checkDesignerActive(readOldKey()))
        {
            String s1 = ActiveKeyGenerator.generateActiveKey();
            designerenvmanager.setActivationKey(s1);
            UserInfoLabel.showBBSDialog();
            return;
        } else
        {
            CollectUserInformationDialog collectuserinformationdialog = new CollectUserInformationDialog(DesignerContext.getDesignerFrame());
            collectuserinformationdialog.setVisible(true);
            return;
        }
    }

    private void onLineVerify(DesignerEnvManager designerenvmanager, final String key)
    {
        int i = designerenvmanager.getActiveKeyStatus();
        if(i != 0)
        {
            Thread thread = new Thread(new Runnable() {

                final String val$key;
                final Designer this$0;

                public void run()
                {
                    ActiveKeyGenerator.onLineVerify(key);
                }

            
            {
                this$0 = Designer.this;
                key = s;
                super();
            }
            }
);
            thread.start();
        }
    }

    private File getOldEnvFile(String s)
    {
        String s1 = System.getProperty("user.home");
        if(s1 == null)
            s1 = System.getProperty("userHome");
        String s2 = StableUtils.pathJoin(new String[] {
            s1, s, (new StringBuilder()).append(ProductConstants.APP_NAME).append("Env.xml").toString()
        });
        return new File(s2);
    }

    private String getOldActiveKeyFromFile(File file)
    {
        if(!file.exists())
            return "";
        DesignerEnvManager designerenvmanager = new DesignerEnvManager();
        try
        {
            XMLTools.readFileXML(designerenvmanager, file);
        }
        catch(Exception exception)
        {
            FRContext.getLogger().error(exception.getMessage(), exception);
        }
        return designerenvmanager.getActivationKey();
    }

    private String readOldKey()
    {
        File file = getOldEnvFile(".FineReport71");
        if(!file.exists())
        {
            File file1 = getOldEnvFile(".FineReport70");
            return getOldActiveKeyFromFile(file1);
        } else
        {
            return getOldActiveKeyFromFile(file);
        }
    }

    public void shutDown()
    {
        InformationCollector informationcollector = InformationCollector.getInstance();
        informationcollector.collectStopTime();
        informationcollector.saveXMLFile();
    }
}
