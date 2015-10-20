package com.fr.design.parameter;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.Parameter;
import com.fr.base.parameter.ParameterUI;
import com.fr.design.DesignState;
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
import com.fr.design.menu.*;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.style.background.BackgroundPane;
import com.fr.general.Inter;
import com.fr.main.parameter.ReportParameterAttr;
import com.fr.main.parameter.TemplateParameterAttr;
import com.fr.stable.ArrayUtils;
import com.fr.stable.bridge.StableFactory;
import com.fr.stable.core.PropertyChangeAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

/**
 * ����Ĳ���������
 *
 * @editor zhou
 * @since 2012-3-23����3:36:52
 */

//TODO:һ�й���setbutton��ɾ����
public class ParameterDefinitePane extends JPanel implements ToolBarMenuDockPlus, ParaDefinitePane {
    private static final int NUM_IN_A_LINE = 4;
    private Parameter[] parameterArray;
    //    private FormParaDesigner formParaDesignEditor;
    private ParameterDesignerProvider paraDesignEditor;
    private PropertyChangeAdapter propertyChangeListener;
    // �������ʱ��סλ��,ÿ������
    private int currentIndex;
    private Parameter[] allParameters;
    private UIButtonGroup<Integer> bg;
    private UIButton setButton;
    private JCheckBoxMenuItem isshowWindowItem;
    private JCheckBoxMenuItem isdelayItem;
    private JPopupMenu jPopupMenu;
    private BackgroundPane bgPane;

    private boolean isEditing;

    private static final int TOOLBARPANEDIMHEIGHT = 26;

    private JWorkBook workBook;

    public ParameterDefinitePane() {
        this.setBorder(null);
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        setComponentBg(this);
//		formParaDesignEditor = new FormParaDesigner(new FormParameterUI());
        paraDesignEditor = DesignModuleFactory.getFormParaDesigner();
        if (paraDesignEditor == null) {
            return;
        }
        paraDesignEditor.initWidgetToolbarPane();
        ((TargetComponent) paraDesignEditor).addTargetModifiedListener(new TargetModifiedListener() {
            @Override
            public void targetModified(TargetModifiedEvent e) {
                if (isEditing) {
                    workBook.updateReportParameterAttr();
                    workBook.fireTargetModified();
                }
            }
        });
        paraDesignEditor.addListener(this);

        propertyChangeListener = new PropertyChangeAdapter() {

            @Override
            public void propertyChange() {
                if (isEditing) {
                    workBook.updateReportParameterAttr();
                    workBook.fireTargetModified();
                }
            }
        };
        this.add(paraDesignEditor.createWrapper(), BorderLayout.CENTER);
//        WidgetToolBarPane.getInstance(formParaDesignEditor);

        setButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/toolbarbtn/parametersetting.png"));
        setButton.set4ToolbarButton();
        isshowWindowItem = new JCheckBoxMenuItem(Inter.getLocText("ParameterD-Show_Parameter_Window"));
        isshowWindowItem.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (propertyChangeListener != null) {
                    propertyChangeListener.propertyChange();
                }
            }
        });
        isdelayItem = new JCheckBoxMenuItem(Inter.getLocText("ParameterD-Delay_Playing"));
        isdelayItem.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (propertyChangeListener != null) {
                    propertyChangeListener.propertyChange();
                }

            }
        });


        setButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jPopupMenu.show(setButton, 0, 20);
            }
        });
    }

    /**
     * ��ʼ��
     */
    public void initBeforeUpEdit() {
        paraDesignEditor.initBeforeUpEdit();
    }

    /**
     * set Component Background
     *
     * @param cc
     */
    public void setComponentBg(Container cc) {
        for (Component com : cc.getComponents()) {
            com.setBackground(new Color(240, 240, 240));
            if (com instanceof Container) {
                setComponentBg((Container) com);
            }
        }
    }

    /**
     * get formParaDesignEditor
     *
     * @return
     */
    public ParameterDesignerProvider getParaDesigner() {
        return paraDesignEditor;
    }

    /**
     * ��ȡĬ�ϴ�С
     *
     * @return
     */
    public Dimension getPreferredSize() {
        return paraDesignEditor.getPreferredSize();
    }

    /**
     * set height
     *
     * @param height
     */
    public void setDesignHeight(int height) {
        paraDesignEditor.setDesignHeight(height);
    }

    public Dimension getDesignSize() {
        return paraDesignEditor.getDesignSize();
    }

    public void setParameterArray(Parameter[] ps) {
        parameterArray = ps;
    }

    public Parameter[] getParameterArray() {
        return parameterArray;
    }

    public int getToolBarHeight(){
        return TOOLBARPANEDIMHEIGHT;
    }

    /**
     * �����˵������ͼ�������
     * @return �˵���
     */
    public ShortCut[] shortcut4ExportMenu() {
        return new ShortCut[0];
    }

    /**
     * populate
     *
     * @param workBook
     */
    public void populate(final JWorkBook workBook) {
        isEditing = false;
        this.workBook = workBook;
        ReportParameterAttr reportParameterAttr = workBook.getTarget().getReportParameterAttr();
        if (reportParameterAttr == null) {
            reportParameterAttr = new ReportParameterAttr();
            reportParameterAttr.setShowWindow(true);
        }

        // formParaDesignEditor.populate()��Ҫ��refreshParameter()֮ǰִ��,��Ȼ��ʹrefreshParameter���Ѿ���ӵĲ����ж�������
        ParameterUI parameterUI = reportParameterAttr.getParameterUI();
        if (parameterUI == null) {
            try {
                parameterUI = StableFactory.getMarkedInstanceObjectFromClass(ParameterUI.FORM_XML_TAG, ParameterUI.class);
                parameterUI.setDefaultSize();
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage());
            }
        }

        paraDesignEditor.populate(parameterUI);

        parameterArray = getNoRepeatParas(getTargetParameter(workBook));
        refreshParameter();
        allParameters = reportParameterAttr.getParameters();

        paraDesignEditor.populateParameterPropertyPane(this);
        isdelayItem.setSelected(reportParameterAttr.isDelayPlaying());
        isshowWindowItem.setSelected(reportParameterAttr.isShowWindow());
        isEditing = true;

        ParameterBridge bridge = paraDesignEditor.getParaComponent();
        if (parameterUI != null) {
            bridge.setDelayDisplayContent(reportParameterAttr.isDelayPlaying());
            bridge.setPosition(reportParameterAttr.getAlign());
            bridge.setDisplay(reportParameterAttr.isShowWindow());
            bridge.setBackground(reportParameterAttr.getBackground());
        }
    }


    private Parameter[] getTargetParameter(JWorkBook workBook) {
        return workBook.getTarget().getParameters();
    }

    /**
     * ˢ�����еĿؼ�
     */
    public void refreshAllNameWidgets() {
        if (paraDesignEditor != null) {
            paraDesignEditor.refreshAllNameWidgets();
        }
    }


    /**
     * ˢ�����ݼ�
     *
     * @param oldName �ɵ�����
     * @param newName �µ�����
     */
    public void refresh4TableData(String oldName, String newName) {
        if (paraDesignEditor != null) {
            paraDesignEditor.refresh4TableData(oldName, newName);
        }
    }

    public Parameter[] getNoRepeatParas(Parameter[] paras) {
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

    /**
     * ˢ�²���
     */
    public void refreshParameter() {
        if (paraDesignEditor != null) {
            paraDesignEditor.refreshParameter(this);
        }
    }

    /**
     * // ��ȡ��������������пؼ��������б�
     *
     * @return
     */
    public List<String> getAllXCreatorNameList() {
        return paraDesignEditor.getAllXCreatorNameList();
    }

    private boolean isWithoutParaXCreator() {
        return paraDesignEditor.isWithoutParaXCreator(allParameters);
    }

    /**
     * get allParameters
     *
     * @return
     */
    public Parameter[] getAllParameters() {
        return allParameters;
    }

    private boolean isBlank() {
        return paraDesignEditor.isBlank();
    }

    /**
     * �Ƿ��в�ѯ��ť
     *
     * @return ���򷵻�true
     */
    public boolean isWithQueryButton() {
        return paraDesignEditor.isWithQueryButton();
    }

    /**
     * update
     *
     * @param reportParameterAttr
     * @return
     */
    public ReportParameterAttr update(ReportParameterAttr reportParameterAttr) {
        if (reportParameterAttr == null) {
            reportParameterAttr = new ReportParameterAttr();
        }

        ParameterUI parameterUI = (isBlank() ? null : paraDesignEditor.getParaTarget());
        ParameterBridge bridge = paraDesignEditor.getParaComponent();
        if (parameterUI != null) {
            reportParameterAttr.setWindowPosition(TemplateParameterAttr.EMBED);
            reportParameterAttr.setDelayPlaying(bridge.isDelayDisplayContent());
            reportParameterAttr.setShowWindow(bridge.isDisplay());
            reportParameterAttr.setAlign(bridge.getPosition());
            reportParameterAttr.setBackground(bridge.getDataBackground());
        }
        //���ﲻ�� parameterUI ��ԭ���ǿ��ǵ�û�пؼ���ʱ�����ÿ����Ч���������棬ֻ�к��пؼ��ű�������
        paraDesignEditor.getParaTarget().setDesignSize(new Dimension(bridge.getDesignWidth(),
                (int)paraDesignEditor.getParaTarget().getDesignSize().getHeight()));
        reportParameterAttr.setParameterUI(parameterUI);
        return reportParameterAttr;
    }

    /**
     * ���������ӵ��༭����
     *
     * @param parameter ����
     */
    public void addingParameter2Editor(Parameter parameter) {
        if (isWithoutParaXCreator()) {
            currentIndex = 0;
        }
        if (!paraDesignEditor.addingParameter2Editor(parameter, currentIndex)) {
            return;
        }
        currentIndex++;
        parameterArray = (Parameter[]) ArrayUtils.removeElement(parameterArray, parameter);
        refreshParameter();
//        FormHierarchyTreePane.getInstance().refreshDockingView();
        DesignModuleFactory.getFormHierarchyPane().refreshDockingView();
        if (propertyChangeListener != null) {
            propertyChangeListener.propertyChange();
        }
    }

    /**
     * ���������ӵ�����ѯ��ť�ı༭����
     *
     * @param parameter ����
     */
    public void addingParameter2EditorWithQueryButton(Parameter parameter) {
        currentIndex = (isWithoutParaXCreator()) ? 0 : (currentIndex + NUM_IN_A_LINE);
        if (!paraDesignEditor.addingParameter2EditorWithQueryButton(parameter, currentIndex)) {
            return;
        }
        currentIndex = currentIndex + NUM_IN_A_LINE - currentIndex % NUM_IN_A_LINE;
        parameterArray = (Parameter[]) ArrayUtils.removeElement(parameterArray, parameter);
        refreshParameter();
//        FormHierarchyTreePane.getInstance().refreshDockingView();
        DesignModuleFactory.getFormHierarchyPane().refreshDockingView();
        if (propertyChangeListener != null) {
            propertyChangeListener.propertyChange();
        }
    }

    /**
     * �����еĲ������ӵ��༭����
     */
    public void addingAllParameter2Editor() {
        if (isWithoutParaXCreator()) {
            currentIndex = 0;
        }
        if (parameterArray == null) {
            return;
        }

        paraDesignEditor.addingAllParameter2Editor(parameterArray, currentIndex);

        parameterArray = null;
        refreshParameter();
//        FormHierarchyTreePane.getInstance().refreshDockingView();
        DesignModuleFactory.getFormHierarchyPane().refreshDockingView();
        if (propertyChangeListener != null) {
            propertyChangeListener.propertyChange();
        }
        workBook.setAutoHeightForCenterPane();
    }

    /**
     * ����ύ��ť
     *
     * @return ����true
     */
    public boolean checkSubmitButton() {
        return true;
    }


    /**
     * ��Զ���Ĺ�����
     *
     * @return �޹���
     */
    public ToolBarDef[] toolbars4Target() {
        return new ToolBarDef[0];
    }

    /**
     * ���������ļ��˵����Ӳ˵�
     *
     * @return �ļ��˵����Ӳ˵�
     */
    public ShortCut[] shortcut4FileMenu() {
        return (ShortCut[]) ArrayUtils.addAll(BaseUtils.isAuthorityEditing() ?
                new ShortCut[]{new SaveTemplateAction(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate()),
                        new UndoAction(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate()),
                        new RedoAction(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate())} :
                new ShortCut[]{new SaveTemplateAction(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate()),
                        new SaveAsTemplateAction(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate()),
                        new UndoAction(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate()),
                        new RedoAction(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate())},
                new ShortCut[0]
        );
    }

    /**
     * ��������ģ��˵�
     *
     * @return ģ��˵�
     */
    public MenuDef[] menus4Target() {
        MenuDef tplMenu = new MenuDef(KeySetUtils.TEMPLATE.getMenuKeySetName(),KeySetUtils.TEMPLATE.getMnemonic());
        if (!BaseUtils.isAuthorityEditing()) {
            tplMenu.addShortCut(new NameSeparator(Inter.getLocText("FR-Utils_WorkBook")));
            tplMenu.addShortCut(new ReportParameterAction(workBook));
            tplMenu.addShortCut(new NameSeparator(Inter.getLocText(new String[]{"DashBoard-Potence", "Edit"})));
            tplMenu.addShortCut(new AllowAuthorityEditAction(workBook));
        } else {
            tplMenu.addShortCut(new ExitAuthorityEditAction(workBook));
        }


        return new MenuDef[]{tplMenu};
    }

    /**
     * ����������FROM�Ĺ�����
     *
     * @return ���ع���
     */
    public JPanel[] toolbarPanes4Form() {
        return paraDesignEditor.toolbarPanes4Form();
    }

    /**
     * ����������FORM�Ĺ��ܰ�ť
     *
     * @return ���ع��߰�ť
     */
    public JComponent[] toolBarButton4Form() {
        return paraDesignEditor.toolBarButton4Form();
    }

    /**
     * ����������Ȩ��ϸ���ȵĹ�����
     *
     * @return ������
     */
    public JComponent toolBar4Authority() {
        return new AuthorityToolBarPane();
    }

    @Override
    public int getMenuState() {
        return DesignState.PARAMETER_PANE;
    }


}
