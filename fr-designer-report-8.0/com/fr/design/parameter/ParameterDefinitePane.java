// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.parameter;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.Parameter;
import com.fr.base.parameter.ParameterUI;
import com.fr.design.actions.AllowAuthorityEditAction;
import com.fr.design.actions.ExitAuthorityEditAction;
import com.fr.design.actions.edit.RedoAction;
import com.fr.design.actions.edit.UndoAction;
import com.fr.design.actions.file.SaveAsTemplateAction;
import com.fr.design.actions.file.SaveTemplateAction;
import com.fr.design.actions.report.ReportParameterAction;
import com.fr.design.designer.TargetComponent;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.AuthorityToolBarPane;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.menu.NameSeparator;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.style.background.BackgroundPane;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.main.impl.WorkBook;
import com.fr.main.parameter.ReportParameterAttr;
import com.fr.stable.ArrayUtils;
import com.fr.stable.bridge.StableFactory;
import com.fr.stable.core.PropertyChangeAdapter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

// Referenced classes of package com.fr.design.parameter:
//            ParaDefinitePane, ParameterBridge, ParameterDesignerProvider, HierarchyTreePane

public class ParameterDefinitePane extends JPanel
    implements ToolBarMenuDockPlus, ParaDefinitePane
{

    private static final int NUM_IN_A_LINE = 4;
    private Parameter parameterArray[];
    private ParameterDesignerProvider paraDesignEditor;
    private PropertyChangeAdapter propertyChangeListener;
    private int currentIndex;
    private Parameter allParameters[];
    private UIButtonGroup bg;
    private UIButton setButton;
    private JCheckBoxMenuItem isshowWindowItem;
    private JCheckBoxMenuItem isdelayItem;
    private JPopupMenu jPopupMenu;
    private BackgroundPane bgPane;
    private boolean isEditing;
    private static final int TOOLBARPANEDIMHEIGHT = 26;
    private JWorkBook workBook;

    public ParameterDefinitePane()
    {
        setBorder(null);
        setLayout(FRGUIPaneFactory.createBorderLayout());
        setComponentBg(this);
        paraDesignEditor = DesignModuleFactory.getFormParaDesigner();
        if(paraDesignEditor == null)
        {
            return;
        } else
        {
            paraDesignEditor.initWidgetToolbarPane();
            ((TargetComponent)paraDesignEditor).addTargetModifiedListener(new TargetModifiedListener() {

                final ParameterDefinitePane this$0;

                public void targetModified(TargetModifiedEvent targetmodifiedevent)
                {
                    if(isEditing)
                    {
                        workBook.updateReportParameterAttr();
                        workBook.fireTargetModified();
                    }
                }

            
            {
                this$0 = ParameterDefinitePane.this;
                super();
            }
            }
);
            paraDesignEditor.addListener(this);
            propertyChangeListener = new PropertyChangeAdapter() {

                final ParameterDefinitePane this$0;

                public void propertyChange()
                {
                    if(isEditing)
                    {
                        workBook.updateReportParameterAttr();
                        workBook.fireTargetModified();
                    }
                }

            
            {
                this$0 = ParameterDefinitePane.this;
                super();
            }
            }
;
            add(paraDesignEditor.createWrapper(), "Center");
            setButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/toolbarbtn/parametersetting.png"));
            setButton.set4ToolbarButton();
            isshowWindowItem = new JCheckBoxMenuItem(Inter.getLocText("ParameterD-Show_Parameter_Window"));
            isshowWindowItem.addItemListener(new ItemListener() {

                final ParameterDefinitePane this$0;

                public void itemStateChanged(ItemEvent itemevent)
                {
                    if(propertyChangeListener != null)
                        propertyChangeListener.propertyChange();
                }

            
            {
                this$0 = ParameterDefinitePane.this;
                super();
            }
            }
);
            isdelayItem = new JCheckBoxMenuItem(Inter.getLocText("ParameterD-Delay_Playing"));
            isdelayItem.addItemListener(new ItemListener() {

                final ParameterDefinitePane this$0;

                public void itemStateChanged(ItemEvent itemevent)
                {
                    if(propertyChangeListener != null)
                        propertyChangeListener.propertyChange();
                }

            
            {
                this$0 = ParameterDefinitePane.this;
                super();
            }
            }
);
            setButton.addActionListener(new ActionListener() {

                final ParameterDefinitePane this$0;

                public void actionPerformed(ActionEvent actionevent)
                {
                    jPopupMenu.show(setButton, 0, 20);
                }

            
            {
                this$0 = ParameterDefinitePane.this;
                super();
            }
            }
);
            return;
        }
    }

    public void initBeforeUpEdit()
    {
        paraDesignEditor.initBeforeUpEdit();
    }

    public void setComponentBg(Container container)
    {
        Component acomponent[] = container.getComponents();
        int i = acomponent.length;
        for(int j = 0; j < i; j++)
        {
            Component component = acomponent[j];
            component.setBackground(new Color(240, 240, 240));
            if(component instanceof Container)
                setComponentBg((Container)component);
        }

    }

    public ParameterDesignerProvider getParaDesigner()
    {
        return paraDesignEditor;
    }

    public Dimension getPreferredSize()
    {
        return paraDesignEditor.getPreferredSize();
    }

    public void setDesignHeight(int i)
    {
        paraDesignEditor.setDesignHeight(i);
    }

    public Dimension getDesignSize()
    {
        return paraDesignEditor.getDesignSize();
    }

    public void setParameterArray(Parameter aparameter[])
    {
        parameterArray = aparameter;
    }

    public Parameter[] getParameterArray()
    {
        return parameterArray;
    }

    public int getToolBarHeight()
    {
        return 26;
    }

    public ShortCut[] shortcut4ExportMenu()
    {
        return new ShortCut[0];
    }

    public void populate(JWorkBook jworkbook)
    {
        isEditing = false;
        workBook = jworkbook;
        ReportParameterAttr reportparameterattr = ((WorkBook)jworkbook.getTarget()).getReportParameterAttr();
        if(reportparameterattr == null)
        {
            reportparameterattr = new ReportParameterAttr();
            reportparameterattr.setShowWindow(true);
        }
        ParameterUI parameterui = reportparameterattr.getParameterUI();
        if(parameterui == null)
            try
            {
                parameterui = (ParameterUI)StableFactory.getMarkedInstanceObjectFromClass("FormParameterUI", com/fr/base/parameter/ParameterUI);
                parameterui.setDefaultSize();
            }
            catch(Exception exception)
            {
                FRContext.getLogger().error(exception.getMessage());
            }
        paraDesignEditor.populate(parameterui);
        parameterArray = getNoRepeatParas(getTargetParameter(jworkbook));
        refreshParameter();
        allParameters = reportparameterattr.getParameters();
        paraDesignEditor.populateParameterPropertyPane(this);
        isdelayItem.setSelected(reportparameterattr.isDelayPlaying());
        isshowWindowItem.setSelected(reportparameterattr.isShowWindow());
        isEditing = true;
        ParameterBridge parameterbridge = paraDesignEditor.getParaComponent();
        if(parameterui != null)
        {
            parameterbridge.setDelayDisplayContent(reportparameterattr.isDelayPlaying());
            parameterbridge.setPosition(reportparameterattr.getAlign());
            parameterbridge.setDisplay(reportparameterattr.isShowWindow());
            parameterbridge.setBackground(reportparameterattr.getBackground());
        }
    }

    private Parameter[] getTargetParameter(JWorkBook jworkbook)
    {
        return ((WorkBook)jworkbook.getTarget()).getParameters();
    }

    public void refreshAllNameWidgets()
    {
        if(paraDesignEditor != null)
            paraDesignEditor.refreshAllNameWidgets();
    }

    public void refresh4TableData(String s, String s1)
    {
        if(paraDesignEditor != null)
            paraDesignEditor.refresh4TableData(s, s1);
    }

    public Parameter[] getNoRepeatParas(Parameter aparameter[])
    {
        ArrayList arraylist = new ArrayList();
        HashSet hashset = new HashSet();
        Parameter aparameter1[] = aparameter;
        int i = aparameter1.length;
        for(int j = 0; j < i; j++)
        {
            Parameter parameter = aparameter1[j];
            if(!hashset.contains(parameter.getName().toLowerCase()))
            {
                arraylist.add(parameter);
                hashset.add(parameter.getName().toLowerCase());
            }
        }

        return (Parameter[])arraylist.toArray(new Parameter[arraylist.size()]);
    }

    public void refreshParameter()
    {
        if(paraDesignEditor != null)
            paraDesignEditor.refreshParameter(this);
    }

    public java.util.List getAllXCreatorNameList()
    {
        return paraDesignEditor.getAllXCreatorNameList();
    }

    private boolean isWithoutParaXCreator()
    {
        return paraDesignEditor.isWithoutParaXCreator(allParameters);
    }

    public Parameter[] getAllParameters()
    {
        return allParameters;
    }

    private boolean isBlank()
    {
        return paraDesignEditor.isBlank();
    }

    public boolean isWithQueryButton()
    {
        return paraDesignEditor.isWithQueryButton();
    }

    public ReportParameterAttr update(ReportParameterAttr reportparameterattr)
    {
        if(reportparameterattr == null)
            reportparameterattr = new ReportParameterAttr();
        ParameterUI parameterui = isBlank() ? null : paraDesignEditor.getParaTarget();
        ParameterBridge parameterbridge = paraDesignEditor.getParaComponent();
        if(parameterui != null)
        {
            reportparameterattr.setWindowPosition(1);
            reportparameterattr.setDelayPlaying(parameterbridge.isDelayDisplayContent());
            reportparameterattr.setShowWindow(parameterbridge.isDisplay());
            reportparameterattr.setAlign(parameterbridge.getPosition());
            reportparameterattr.setBackground(parameterbridge.getDataBackground());
        }
        paraDesignEditor.getParaTarget().setDesignSize(new Dimension(parameterbridge.getDesignWidth(), (int)paraDesignEditor.getParaTarget().getDesignSize().getHeight()));
        reportparameterattr.setParameterUI(parameterui);
        return reportparameterattr;
    }

    public void addingParameter2Editor(Parameter parameter)
    {
        if(isWithoutParaXCreator())
            currentIndex = 0;
        if(!paraDesignEditor.addingParameter2Editor(parameter, currentIndex))
            return;
        currentIndex++;
        parameterArray = (Parameter[])(Parameter[])ArrayUtils.removeElement(parameterArray, parameter);
        refreshParameter();
        DesignModuleFactory.getFormHierarchyPane().refreshDockingView();
        if(propertyChangeListener != null)
            propertyChangeListener.propertyChange();
    }

    public void addingParameter2EditorWithQueryButton(Parameter parameter)
    {
        currentIndex = isWithoutParaXCreator() ? 0 : currentIndex + 4;
        if(!paraDesignEditor.addingParameter2EditorWithQueryButton(parameter, currentIndex))
            return;
        currentIndex = (currentIndex + 4) - currentIndex % 4;
        parameterArray = (Parameter[])(Parameter[])ArrayUtils.removeElement(parameterArray, parameter);
        refreshParameter();
        DesignModuleFactory.getFormHierarchyPane().refreshDockingView();
        if(propertyChangeListener != null)
            propertyChangeListener.propertyChange();
    }

    public void addingAllParameter2Editor()
    {
        if(isWithoutParaXCreator())
            currentIndex = 0;
        if(parameterArray == null)
            return;
        paraDesignEditor.addingAllParameter2Editor(parameterArray, currentIndex);
        parameterArray = null;
        refreshParameter();
        DesignModuleFactory.getFormHierarchyPane().refreshDockingView();
        if(propertyChangeListener != null)
            propertyChangeListener.propertyChange();
        workBook.setAutoHeightForCenterPane();
    }

    public boolean checkSubmitButton()
    {
        return true;
    }

    public ToolBarDef[] toolbars4Target()
    {
        return new ToolBarDef[0];
    }

    public ShortCut[] shortcut4FileMenu()
    {
        return (ShortCut[])(ShortCut[])ArrayUtils.addAll(BaseUtils.isAuthorityEditing() ? ((Object []) (new ShortCut[] {
            new SaveTemplateAction(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate()), new UndoAction(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate()), new RedoAction(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate())
        })) : ((Object []) (new ShortCut[] {
            new SaveTemplateAction(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate()), new SaveAsTemplateAction(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate()), new UndoAction(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate()), new RedoAction(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate())
        })), new ShortCut[0]);
    }

    public MenuDef[] menus4Target()
    {
        MenuDef menudef = new MenuDef(KeySetUtils.TEMPLATE.getMenuKeySetName(), KeySetUtils.TEMPLATE.getMnemonic());
        if(!BaseUtils.isAuthorityEditing())
        {
            menudef.addShortCut(new ShortCut[] {
                new NameSeparator(Inter.getLocText("FR-Utils_WorkBook"))
            });
            menudef.addShortCut(new ShortCut[] {
                new ReportParameterAction(workBook)
            });
            menudef.addShortCut(new ShortCut[] {
                new NameSeparator(Inter.getLocText(new String[] {
                    "DashBoard-Potence", "Edit"
                }))
            });
            menudef.addShortCut(new ShortCut[] {
                new AllowAuthorityEditAction(workBook)
            });
        } else
        {
            menudef.addShortCut(new ShortCut[] {
                new ExitAuthorityEditAction(workBook)
            });
        }
        return (new MenuDef[] {
            menudef
        });
    }

    public JPanel[] toolbarPanes4Form()
    {
        return paraDesignEditor.toolbarPanes4Form();
    }

    public JComponent[] toolBarButton4Form()
    {
        return paraDesignEditor.toolBarButton4Form();
    }

    public JComponent toolBar4Authority()
    {
        return new AuthorityToolBarPane();
    }

    public int getMenuState()
    {
        return 2;
    }





}
