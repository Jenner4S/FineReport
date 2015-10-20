/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.start;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.file.WebPreviewUtils;
import com.fr.design.actions.help.AboutAction;
import com.fr.design.constants.UIConstants;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.file.MutilTempalteTabPane;
import com.fr.design.file.NewTemplatePane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.itoolbar.UILargeToolbar;
import com.fr.design.mainframe.*;
import com.fr.design.mainframe.actions.*;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.*;
import com.fr.design.module.ChartStartModule;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.general.web.ParameterConsts;
import com.fr.stable.Constants;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-13
 * Time: ����11:02
 */
public class Designer4Chart extends BaseDesigner {
    private static final int TOOLBAR_HEIGHT = 53;
   	private static final int TOOLBAR_WIDTH =60+34+7+1+7 ;
    private static final int GAP = 7;
    private static final int EAST_WIDTH = 292;
    private static final int MESSAGEPORT = 51460;
    private UIButton saveButton;
    private UIButton undo;
    private UIButton redo;
    private UIButton run;
    private UIButton copy;

    /**
     * ������
     *
     * @param args ��ڲ���
     */
    public static void main(String[] args) {
        new Designer4Chart(args);
    }

    public Designer4Chart(String[] args) {
        super(args);
    }

    @Override
    protected String module2Start() {
        EastRegionContainerPane.getInstance().setDownPaneVisible(false);
        EastRegionContainerPane.getInstance().setContainerWidth(EAST_WIDTH);
        return ChartStartModule.class.getName();
    }

    protected void initLanguage() {
        //�������λ�ò�������������Ϊ��Ӱ�������л�������
        FRContext.setLanguage(Constants.LANGUAGE_ENGLISH);
    }

    protected void initDefaultFont(){
        FRContext.getDefaultValues().setFRFont(FRFont.getInstance("Meiryo", Font.PLAIN, 9));
    }

    /**
     * build��·��
     * @return  build��·��
     */
    public String buildPropertiesPath() {
        return "/com/fr/chart/base/build.properties";
    }

    /**
     * �����ļ��˵���
     * @return �˵���
     */
    public ShortCut[] createNewFileShortCuts() {
        ArrayList<ShortCut> shortCuts = new ArrayList<ShortCut>();
        shortCuts.add(new NewChartAction());
        return shortCuts.toArray(new ShortCut[shortCuts.size()]);
    }

    /**
     * ������ģ��
     * @return ģ��
     */
    public JTemplate<?, ?> createNewTemplate() {
        return new JChart();
    }

    protected void resetToolTips(){
        copy.setToolTipText(Inter.getLocText("FR-Chart-Action_Copy")+"JS");
        run.setToolTipText(PREVIEW.getMenuKeySetName());
    }

    /**
     * ����������ϼ����Ƚϴ��ͼ�꣺�½�cpt�����棬ǰ�������ˣ����С�
     *
     * @return ���ش�ͼ���Ӧ�Ĺ�����
     */
    public UILargeToolbar createLargeToolbar() {

        UILargeToolbar largeToolbar = new UILargeToolbar(FlowLayout.LEFT){
            public Dimension getPreferredSize() {
           		return new Dimension(TOOLBAR_WIDTH ,TOOLBAR_HEIGHT);
           	}
        };
        largeToolbar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 4));
        largeToolbar.add(new JPanel() {
            public Dimension getPreferredSize() {
                Dimension dim = super.getPreferredSize();
                dim.width = 1;
                return dim;
            }
        });
        createRunButton();
        largeToolbar.add(run);
        largeToolbar.add(new JPanel() {
            public Dimension getPreferredSize() {
                Dimension dim = super.getPreferredSize();
                dim.width = GAP;
                return dim;
            }
        });
        largeToolbar.addSeparator(new Dimension(2, 42));
        largeToolbar.add(new JPanel() {
            public Dimension getPreferredSize() {
                Dimension dim = super.getPreferredSize();
                dim.width = GAP;
                return dim;
            }
        });
        createCopyButton();
        largeToolbar.add(copy);
        largeToolbar.add(new JPanel() {
              public Dimension getPreferredSize() {
                  Dimension dim = super.getPreferredSize();
                  dim.width = GAP;
                  return dim;
              }
          });
        largeToolbar.addSeparator(new Dimension(2, 42));

        largeToolbar.setBorder(new MatteBorder(new Insets(0, 0, 1, 0), UIConstants.LINE_COLOR));
        return largeToolbar;
    }

    protected int getStartPort(){
        return MESSAGEPORT;
    }

    protected DesignerFrame createDesignerFrame(){

        return new DesignerFrame4Chart(this);
    }

    /**
     * ��������һ�ŵĹ�������ť
     *
     * @return ��ť
     */
    public UIButton[] createUp() {
        return new UIButton[]{createSaveButton(), createUndoButton(), createRedoButton()};
    }

    private UIButton createRunButton() {
        run = new UIButton(BaseUtils.readIcon("com/fr/design/images/buttonicon/pageb24.png")) {
                    public Dimension getPreferredSize() {
                        return new Dimension(34, 43);
                    }
                };
        run.setToolTipText(PREVIEW.getMenuKeySetName());
        run.set4ChartLargeToolButton();
        run.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTemplate<?, ?> jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                if (jt == null) {
                    return;
                }
                onChartPreview(jt);
            }
        });
        return run;
    }

    public static void onChartPreview(JTemplate<?, ?> jt) {
        WebPreviewUtils.actionPerformed(jt, null, ParameterConsts.CHARTLET);
    }

    public static final MenuKeySet PREVIEW = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'P';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Chart-Template_Preview");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK);
        }
    };


    private UIButton createCopyButton(){
        copy = new UIButton(BaseUtils.readIcon("com/fr/design/images/copyjs.png")) {
            public Dimension getPreferredSize() {
                return new Dimension(34, 43);
            }
        };
        copy.setToolTipText(Inter.getLocText("FR-Chart-Action_Copy")+"JS");
        copy.set4ChartLargeToolButton();
        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTemplate<?, ?> jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                if (jt == null) {
                    return;
                }
                DesignerContext.getDesignerFrame().refreshToolbar();

              	jt.stopEditing();
              	if (!jt.isSaved() && !jt.saveTemplate2Env()) {
              		return;
              	}
                //���ƴ���
                jt.copyJS();
            }
        });
        return copy;
    }

    private UIButton createSaveButton() {
        saveButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/save.png"));
        saveButton.setToolTipText(KeySetUtils.SAVE_TEMPLATE.getMenuKeySetName());
        saveButton.set4ToolbarButton();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTemplate<?, ?> jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                jt.stopEditing();
                jt.saveTemplate();
                jt.requestFocus();
            }
        });
        return saveButton;
    }

    private UIButton createUndoButton() {
        undo = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/undo.png"));
        undo.setToolTipText(KeySetUtils.UNDO.getMenuKeySetName());
        undo.set4ToolbarButton();
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTemplate<?, ?> jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                if (jt != null) {
                    jt.undo();
                }
            }
        });
        return undo;
    }

    private UIButton createRedoButton() {
        redo = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/redo.png"));
        redo.setToolTipText(KeySetUtils.REDO.getMenuKeySetName());
        redo.set4ToolbarButton();
        redo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTemplate<?, ?> jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                if (jt != null) {
                    jt.redo();
                }
            }
        });
        return redo;
    }

    /**
     * ���ù�����
     * @param toolbarComponent ������
     * @param plus             ����
     * @return   ����������
     */
    public JComponent resetToolBar(JComponent toolbarComponent, ToolBarMenuDockPlus plus) {
        return plus.toolBar4Authority();
    }

    public NewTemplatePane getNewTemplatePane(){
       return new NewTemplatePane() {
           @Override
           public Icon getNew() {
               return BaseUtils.readIcon("/com/fr/design/images/newchart_normal.png");
           }

           @Override
           public Icon getMouseOverNew() {
               return BaseUtils.readIcon("/com/fr/design/images/newchart_over.png");
           }

           @Override
           public Icon getMousePressNew() {
               return BaseUtils.readIcon("/com/fr/design/images/newchart_press.png");
           }
       };
    };

    @Override
    protected void refreshLargeToolbarState() {
        JTemplate<?, ?> jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if (jt == null) {
            return;
        }
        saveButton.setEnabled(!jt.isSaved());
        MutilTempalteTabPane.getInstance().refreshOpenedTemplate(HistoryTemplateListPane.getInstance().getHistoryList());
        MutilTempalteTabPane.getInstance().repaint();
        if (DesignerEnvManager.getEnvManager().isSupportUndo()) {
            undo.setEnabled(jt.canUndo());
            redo.setEnabled(jt.canRedo());
        } else {
            undo.setEnabled(false);
            redo.setEnabled(false);
        }
    }

    protected SplashPane createSplashPane() {
        return new ChartSplashPane();
    }

    /**
     *���¹�����
     */
    public void updateToolBarDef() {
        refreshLargeToolbarState();
    }

    protected void addCloseCurrentTemplateAction(MenuDef menuDef) {

    }

    protected void addPreferenceAction(MenuDef menuDef) {

    }

    protected void addSwitchExistEnvAction(MenuDef menuDef) {

    }

    @Override
    public MenuDef[] createTemplateShortCuts(ToolBarMenuDockPlus plus) {
        MenuDef menuDef = new MenuDef(KeySetUtils.EXPORT_CHART.getMenuKeySetName(), KeySetUtils.EXPORT_CHART.getMnemonic());
        menuDef.addShortCut(plus.shortcut4ExportMenu());
        return new MenuDef[] {menuDef};
    }

    /**
     * ���������˵��ò˵���
     * @return �˵���
     */
    public ShortCut[] createHelpShortCuts() {
        resetToolTips();
        return new ShortCut[]{
                new ChartWebAction(),
                SeparatorDef.DEFAULT,
                new ChartFeedBackAciton(),
                SeparatorDef.DEFAULT,
                new UpdateOnlineAction(),
                new AboutAction()
        };
    }

    protected ShortCut openTemplateAction(){
        return new OpenChartAction();
    }

    protected String[] startFileSuffix(){
        return new String[]{".crt"};
    }

}
