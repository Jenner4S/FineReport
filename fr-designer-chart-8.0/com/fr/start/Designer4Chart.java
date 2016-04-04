// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.start;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.file.WebPreviewUtils;
import com.fr.design.actions.help.AboutAction;
import com.fr.design.constants.UIConstants;
import com.fr.design.file.*;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.itoolbar.UILargeToolbar;
import com.fr.design.mainframe.*;
import com.fr.design.mainframe.actions.*;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.*;
import com.fr.design.module.ChartStartModule;
import com.fr.general.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.MatteBorder;

// Referenced classes of package com.fr.start:
//            BaseDesigner, ChartSplashPane, SplashPane

public class Designer4Chart extends BaseDesigner
{

    private static final int TOOLBAR_HEIGHT = 53;
    private static final int TOOLBAR_WIDTH = 109;
    private static final int GAP = 7;
    private static final int EAST_WIDTH = 292;
    private static final int MESSAGEPORT = 51460;
    private UIButton saveButton;
    private UIButton undo;
    private UIButton redo;
    private UIButton run;
    private UIButton copy;
    public static final MenuKeySet PREVIEW = new MenuKeySet() {

        public char getMnemonic()
        {
            return 'P';
        }

        public String getMenuName()
        {
            return Inter.getLocText("FR-Chart-Template_Preview");
        }

        public KeyStroke getKeyStroke()
        {
            return KeyStroke.getKeyStroke(80, 2);
        }

    }
;

    public static void main(String args[])
    {
        new Designer4Chart(args);
    }

    public Designer4Chart(String as[])
    {
        super(as);
    }

    protected String module2Start()
    {
        EastRegionContainerPane.getInstance().setDownPaneVisible(false);
        EastRegionContainerPane.getInstance().setContainerWidth(292);
        return com/fr/design/module/ChartStartModule.getName();
    }

    protected void initLanguage()
    {
        FRContext.setLanguage(2);
    }

    protected void initDefaultFont()
    {
        FRContext.getDefaultValues().setFRFont(FRFont.getInstance("Meiryo", 0, 9F));
    }

    public String buildPropertiesPath()
    {
        return "/com/fr/chart/base/build.properties";
    }

    public ShortCut[] createNewFileShortCuts()
    {
        ArrayList arraylist = new ArrayList();
        arraylist.add(new NewChartAction());
        return (ShortCut[])arraylist.toArray(new ShortCut[arraylist.size()]);
    }

    public JTemplate createNewTemplate()
    {
        return new JChart();
    }

    protected void resetToolTips()
    {
        copy.setToolTipText((new StringBuilder()).append(Inter.getLocText("FR-Chart-Action_Copy")).append("JS").toString());
        run.setToolTipText(PREVIEW.getMenuKeySetName());
    }

    public UILargeToolbar createLargeToolbar()
    {
        UILargeToolbar uilargetoolbar = new UILargeToolbar(0) {

            final Designer4Chart this$0;

            public Dimension getPreferredSize()
            {
                return new Dimension(109, 53);
            }

            
            {
                this$0 = Designer4Chart.this;
                super(i);
            }
        }
;
        uilargetoolbar.setLayout(new FlowLayout(1, 0, 4));
        uilargetoolbar.add(new JPanel() {

            final Designer4Chart this$0;

            public Dimension getPreferredSize()
            {
                Dimension dimension = super.getPreferredSize();
                dimension.width = 1;
                return dimension;
            }

            
            {
                this$0 = Designer4Chart.this;
                super();
            }
        }
);
        createRunButton();
        uilargetoolbar.add(run);
        uilargetoolbar.add(new JPanel() {

            final Designer4Chart this$0;

            public Dimension getPreferredSize()
            {
                Dimension dimension = super.getPreferredSize();
                dimension.width = 7;
                return dimension;
            }

            
            {
                this$0 = Designer4Chart.this;
                super();
            }
        }
);
        uilargetoolbar.addSeparator(new Dimension(2, 42));
        uilargetoolbar.add(new JPanel() {

            final Designer4Chart this$0;

            public Dimension getPreferredSize()
            {
                Dimension dimension = super.getPreferredSize();
                dimension.width = 7;
                return dimension;
            }

            
            {
                this$0 = Designer4Chart.this;
                super();
            }
        }
);
        createCopyButton();
        uilargetoolbar.add(copy);
        uilargetoolbar.add(new JPanel() {

            final Designer4Chart this$0;

            public Dimension getPreferredSize()
            {
                Dimension dimension = super.getPreferredSize();
                dimension.width = 7;
                return dimension;
            }

            
            {
                this$0 = Designer4Chart.this;
                super();
            }
        }
);
        uilargetoolbar.addSeparator(new Dimension(2, 42));
        uilargetoolbar.setBorder(new MatteBorder(new Insets(0, 0, 1, 0), UIConstants.LINE_COLOR));
        return uilargetoolbar;
    }

    protected int getStartPort()
    {
        return 51460;
    }

    protected DesignerFrame createDesignerFrame()
    {
        return new DesignerFrame4Chart(this);
    }

    public UIButton[] createUp()
    {
        return (new UIButton[] {
            createSaveButton(), createUndoButton(), createRedoButton()
        });
    }

    private UIButton createRunButton()
    {
        run = new UIButton(BaseUtils.readIcon("com/fr/design/images/buttonicon/pageb24.png")) {

            final Designer4Chart this$0;

            public Dimension getPreferredSize()
            {
                return new Dimension(34, 43);
            }

            
            {
                this$0 = Designer4Chart.this;
                super(icon);
            }
        }
;
        run.setToolTipText(PREVIEW.getMenuKeySetName());
        run.set4ChartLargeToolButton();
        run.addActionListener(new ActionListener() {

            final Designer4Chart this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                if(jtemplate == null)
                {
                    return;
                } else
                {
                    Designer4Chart.onChartPreview(jtemplate);
                    return;
                }
            }

            
            {
                this$0 = Designer4Chart.this;
                super();
            }
        }
);
        return run;
    }

    public static void onChartPreview(JTemplate jtemplate)
    {
        WebPreviewUtils.actionPerformed(jtemplate, null, "chartlet");
    }

    private UIButton createCopyButton()
    {
        copy = new UIButton(BaseUtils.readIcon("com/fr/design/images/copyjs.png")) {

            final Designer4Chart this$0;

            public Dimension getPreferredSize()
            {
                return new Dimension(34, 43);
            }

            
            {
                this$0 = Designer4Chart.this;
                super(icon);
            }
        }
;
        copy.setToolTipText((new StringBuilder()).append(Inter.getLocText("FR-Chart-Action_Copy")).append("JS").toString());
        copy.set4ChartLargeToolButton();
        copy.addActionListener(new ActionListener() {

            final Designer4Chart this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                if(jtemplate == null)
                    return;
                DesignerContext.getDesignerFrame().refreshToolbar();
                jtemplate.stopEditing();
                if(!jtemplate.isSaved() && !jtemplate.saveTemplate2Env())
                {
                    return;
                } else
                {
                    jtemplate.copyJS();
                    return;
                }
            }

            
            {
                this$0 = Designer4Chart.this;
                super();
            }
        }
);
        return copy;
    }

    private UIButton createSaveButton()
    {
        saveButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/save.png"));
        saveButton.setToolTipText(KeySetUtils.SAVE_TEMPLATE.getMenuKeySetName());
        saveButton.set4ToolbarButton();
        saveButton.addActionListener(new ActionListener() {

            final Designer4Chart this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                jtemplate.stopEditing();
                jtemplate.saveTemplate();
                jtemplate.requestFocus();
            }

            
            {
                this$0 = Designer4Chart.this;
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

            final Designer4Chart this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                if(jtemplate != null)
                    jtemplate.undo();
            }

            
            {
                this$0 = Designer4Chart.this;
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

            final Designer4Chart this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                if(jtemplate != null)
                    jtemplate.redo();
            }

            
            {
                this$0 = Designer4Chart.this;
                super();
            }
        }
);
        return redo;
    }

    public JComponent resetToolBar(JComponent jcomponent, ToolBarMenuDockPlus toolbarmenudockplus)
    {
        return toolbarmenudockplus.toolBar4Authority();
    }

    public NewTemplatePane getNewTemplatePane()
    {
        return new NewTemplatePane() {

            final Designer4Chart this$0;

            public Icon getNew()
            {
                return BaseUtils.readIcon("/com/fr/design/images/newchart_normal.png");
            }

            public Icon getMouseOverNew()
            {
                return BaseUtils.readIcon("/com/fr/design/images/newchart_over.png");
            }

            public Icon getMousePressNew()
            {
                return BaseUtils.readIcon("/com/fr/design/images/newchart_press.png");
            }

            
            {
                this$0 = Designer4Chart.this;
                super();
            }
        }
;
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
    }

    protected SplashPane createSplashPane()
    {
        return new ChartSplashPane();
    }

    public void updateToolBarDef()
    {
        refreshLargeToolbarState();
    }

    protected void addCloseCurrentTemplateAction(MenuDef menudef)
    {
    }

    protected void addPreferenceAction(MenuDef menudef)
    {
    }

    protected void addSwitchExistEnvAction(MenuDef menudef)
    {
    }

    public MenuDef[] createTemplateShortCuts(ToolBarMenuDockPlus toolbarmenudockplus)
    {
        MenuDef menudef = new MenuDef(KeySetUtils.EXPORT_CHART.getMenuKeySetName(), KeySetUtils.EXPORT_CHART.getMnemonic());
        menudef.addShortCut(toolbarmenudockplus.shortcut4ExportMenu());
        return (new MenuDef[] {
            menudef
        });
    }

    public ShortCut[] createHelpShortCuts()
    {
        resetToolTips();
        return (new ShortCut[] {
            new ChartWebAction(), SeparatorDef.DEFAULT, new ChartFeedBackAciton(), SeparatorDef.DEFAULT, new UpdateOnlineAction(), new AboutAction()
        });
    }

    protected ShortCut openTemplateAction()
    {
        return new OpenChartAction();
    }

    protected String[] startFileSuffix()
    {
        return (new String[] {
            ".crt"
        });
    }

}
