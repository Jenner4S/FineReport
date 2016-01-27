/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe.toolbar;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.DesignState;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.actions.UpdateAction;
import com.fr.design.actions.file.*;
import com.fr.design.actions.help.*;
import com.fr.design.actions.server.*;
import com.fr.design.file.NewTemplatePane;
import com.fr.design.fun.MenuHandler;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.imenu.UIMenu;
import com.fr.design.gui.imenu.UIMenuBar;
import com.fr.design.gui.itoolbar.UILargeToolbar;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.menu.*;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.ProductConstants;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * @author richer
 * @since 6.5.5 ������2011-6-13
 */
/*
 * TODO ALEX_SEP ��sheet1�л���sheet2,����õ���Docking��һ����,����λ�ò�Ҫ�����䶯,sheet1ʱ������ʾ���ĸ�docking��tab,��sheet2ʱҲһ��
 * �о���docking�Լ�ȷ����λ�õķ�ʽ�Ƚ�����ʵ��
 * ����docking��״̬�ı���,�´δ������,ҲӦ����������
 */
public abstract class ToolBarMenuDock {
    private static final String FINEREPORT = "FineReport";
    private static final int MENUBAR_HEIGHT = 22;
    public static final int PANLE_HEIGNT = 26;
    private MenuDef[] menus;
    private ToolBarDef toolBarDef;

    /**
     * ���²˵�
     */
    public void updateMenuDef() {
        for (int i = 0, count = ArrayUtils.getLength(menus); i < count; i++) {
            menus[i].updateMenu();
        }
    }

    /**
     * ����toolbar
     */
    public void updateToolBarDef() {
        if (toolBarDef == null) {
            return;
        }
        for (int j = 0, cc = toolBarDef.getShortCutCount(); j < cc; j++) {
            ShortCut shortCut = toolBarDef.getShortCut(j);
            if (shortCut instanceof UpdateAction) {
                ((UpdateAction) shortCut).update();
            }
        }


        refreshLargeToolbarState();
    }

    /**
     * ���ɲ˵���
     *
     * @param plus ����
     * @return �˵���
     */
    public final JMenuBar createJMenuBar(ToolBarMenuDockPlus plus) {
        UIMenuBar jMenuBar = new UIMenuBar() {
            @Override
            public Dimension getPreferredSize() {
                Dimension dim = super.getPreferredSize();
                dim.height = MENUBAR_HEIGHT;
                return dim;
            }
        };

        this.menus = menus(plus);
        for (int i = 0; i < menus.length; i++) {
            UIMenu subMenu = menus[i].createJMenu();
            jMenuBar.add(subMenu);
            menus[i].updateMenu();
        }
        return jMenuBar;
    }

    /**
     * ���ɱ�����ƺͱ���Ƶı༭����
     *
     * @return ģ��
     */
    public JTemplate<?, ?> createNewTemplate() {
        return null;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////menu below/////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    private MenuDef[] menus(final ToolBarMenuDockPlus plus) {
        java.util.List<MenuDef> menuList = new java.util.ArrayList<MenuDef>();
        // ����ļ��˵�
        menuList.add(createFileMenuDef(plus));

        MenuDef[] menuDefs = createTemplateShortCuts(plus);
        insertTemplateExtendMenu(plus, menuDefs);

        // ���ģ��˵�
        menuList.addAll(Arrays.asList(menuDefs));

        // ��ӷ������˵�
        menuList.add(createServerMenuDef(plus));
        // ��Ӱ����˵�
        menuList.add(createHelpMenuDef());

        return menuList.toArray(new MenuDef[menuList.size()]);
    }

    private void insertTemplateExtendMenu(ToolBarMenuDockPlus plus, MenuDef[] menuDefs) {
        // ���˵��Ӳ�����
        for (MenuDef m : menuDefs) {
            switch (m.getAnchor()) {
                case MenuHandler.TEMPLATE :
                    insertMenu(m, MenuHandler.TEMPLATE, new TemplateTargetAction(plus));
                    break;
                case MenuHandler.INSERT :
                    insertMenu(m, MenuHandler.INSERT);
                    break;
                case MenuHandler.CELL :
                    insertMenu(m, MenuHandler.CELL);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * �����½�ģ��Ĳ˵�
     *
     * @param plus ����
     * @return �˵�
     */
    public MenuDef[] createTemplateShortCuts(ToolBarMenuDockPlus plus) {
        return plus.menus4Target();
    }

    private MenuDef createFileMenuDef(ToolBarMenuDockPlus plus) {
        MenuDef menuDef = new MenuDef(Inter.getLocText("FR-Designer_File"), 'F');

        ShortCut[] scs = new ShortCut[0];
        if (!BaseUtils.isAuthorityEditing()) {
            scs = createNewFileShortCuts();
        }
        if (!ArrayUtils.isEmpty(scs)) {
            menuDef.addShortCut(scs);
        }

        menuDef.addShortCut(openTemplateAction());

        menuDef.addShortCut(new OpenRecentReportMenuDef());

        addCloseCurrentTemplateAction(menuDef);

        scs = plus.shortcut4FileMenu();
        if (!ArrayUtils.isEmpty(scs)) {
            menuDef.addShortCut(SeparatorDef.DEFAULT);
            menuDef.addShortCut(scs);
            menuDef.addShortCut(SeparatorDef.DEFAULT);
        }

        addPreferenceAction(menuDef);

        addSwitchExistEnvAction(menuDef);

        menuDef.addShortCut( new ExitDesignerAction());

        insertMenu(menuDef, MenuHandler.FILE);
        return menuDef;
    }

    protected void addCloseCurrentTemplateAction(MenuDef menuDef) {
        if (!BaseUtils.isAuthorityEditing()) {
            menuDef.addShortCut(new CloseCurrentTemplateAction());
        }
    }

    protected void addPreferenceAction(MenuDef menuDef) {
        if (!BaseUtils.isAuthorityEditing()) {
            menuDef.addShortCut(new PreferenceAction());
        }
    }

    protected void addSwitchExistEnvAction(MenuDef menuDef) {
        menuDef.addShortCut(new SwitchExistEnv());
    }

    protected ShortCut openTemplateAction(){
        return new OpenTemplateAction();
    }

    /**
     * �����½��ļ��Ĳ˵�
     *
     * @return �˵�
     */
    public abstract ShortCut[] createNewFileShortCuts();
    
    /**
	 * ������̳��¼���, chart�Ǳ߲���Ҫ
	 * 
	 * @return ������
	 * 
	 */
    public Component createBBSLoginPane(){
    	return new UILabel();
    }


    protected MenuDef createServerMenuDef(ToolBarMenuDockPlus plus) {
        MenuDef menuDef = new MenuDef(Inter.getLocText("FR-Designer_M-Server"), 'S');

        if (FRContext.getCurrentEnv() == null || !FRContext.getCurrentEnv().isRoot()) {
            menuDef.addShortCut(new ConnectionListAction());
            return menuDef;
        }

        if (!BaseUtils.isAuthorityEditing()) {
            menuDef.addShortCut(
                    new ConnectionListAction(),
                    new GlobalTableDataAction()
            );
        }


        menuDef.addShortCut(
                new PlatformManagerAction()
        );

        if (!BaseUtils.isAuthorityEditing()) {
            if (FRContext.isChineseEnv()){
                menuDef.addShortCut(
                        new PluginManagerAction()
                );
            }
            menuDef.addShortCut(
                    new FunctionManagerAction(),
                    new GlobalParameterAction()
            );
        }


        return menuDef;
    }

    /**
     * ���������Ӳ˵�
     * @return ����˵����Ӳ˵�
     */
    public ShortCut[] createHelpShortCuts() {
        java.util.List<ShortCut> shortCuts = new ArrayList<ShortCut>();
        shortCuts.add(new WebDemoAction());
        shortCuts.add(SeparatorDef.DEFAULT);
        shortCuts.add(new TutorialAction());
        shortCuts.add(SeparatorDef.DEFAULT);
        if (ComparatorUtils.equals(ProductConstants.APP_NAME,FINEREPORT)) {
            shortCuts.add(new FeedBackAction());
            shortCuts.add(SeparatorDef.DEFAULT);
            shortCuts.add(new SupportQQAction());
            shortCuts.add(SeparatorDef.DEFAULT);
            shortCuts.add(new ForumAction());
        }
        shortCuts.add(SeparatorDef.DEFAULT);
        shortCuts.add(new AboutAction());

        return shortCuts.toArray(new ShortCut[shortCuts.size()]);
    }


    private MenuDef createHelpMenuDef() {
        MenuDef menuDef = new MenuDef(Inter.getLocText("FR-Designer_Help"), 'H');
        ShortCut[] otherHelpShortCuts = createHelpShortCuts();
        for (ShortCut shortCut : otherHelpShortCuts) {
            menuDef.addShortCut(shortCut);
        }
        insertMenu(menuDef, MenuHandler.HELP);
        return menuDef;
    }

    /**
     * ���ɹ�����
     *
     * @param toolbarComponent ������
     * @param plus             ����
     * @return ������
     */
    public JComponent resetToolBar(JComponent toolbarComponent, ToolBarMenuDockPlus plus) {
        ToolBarDef[] plusToolBarDefs = plus.toolbars4Target();
        UIToolbar toolBar;
        if (toolbarComponent instanceof UIToolbar) {
            toolBar = (UIToolbar) toolbarComponent;
            toolBar.removeAll();
        } else {
            toolBar = ToolBarDef.createJToolBar();
        }

        toolBar.setFocusable(true);
        toolBarDef = new ToolBarDef();

        if (plusToolBarDefs != null) {
            for (int i = 0; i < plusToolBarDefs.length; i++) {
                ToolBarDef def = plusToolBarDefs[i];
                for (int di = 0, dlen = def.getShortCutCount(); di < dlen; di++) {
                    toolBarDef.addShortCut(def.getShortCut(di));
                }
                toolBarDef.addShortCut(SeparatorDef.DEFAULT);
            }
            UIManager.getDefaults().put("ToolTip.hideAccelerator", Boolean.TRUE);
            toolBarDef.updateToolBar(toolBar);
            return toolBar;

        } else {
            return polyToolBar(Inter.getLocText(new String[]{"Polybolck", "Edit"}));
        }
    }


    protected JPanel polyToolBar(String text) {
        JPanel panel = new JPanel(new BorderLayout()) {
            public Dimension getPreferredSize() {
                Dimension dim = super.getPreferredSize();
                dim.height = PANLE_HEIGNT;
                return dim;
            }
        };
        UILabel uiLabel = new UILabel(text);
        uiLabel.setHorizontalAlignment(SwingConstants.CENTER);
        uiLabel.setFont(new Font(Inter.getLocText("FR-Designer-All_MSBold"), 0, 14));
        uiLabel.setForeground(new Color(150, 150, 150));
        panel.add(uiLabel, BorderLayout.CENTER);
        return panel;
    }


    /**
     * ��������Ĺ�����
     *
     * @param plus ����
     * @return ������
     */
    public JComponent[] resetUpToolBar(ToolBarMenuDockPlus plus) {
        return plus.toolBarButton4Form();
    }


    /**
     * ������Ĺ��߰�ť
     *
     * @return ��Ĺ��߰�ť
     */
    public UILargeToolbar createLargeToolbar() {
        return new UILargeToolbar(FlowLayout.LEFT);
    }

    /**
     * ��������İ�ť
     *
     * @return ��ť
     */
    public UIButton[] createUp() {
        return new UIButton[0];
    }


    protected void refreshLargeToolbarState() {

    }

    public static final ToolBarMenuDockPlus NULLAVOID = new ToolBarMenuDockPlus() {

        @Override
        public ToolBarDef[] toolbars4Target() {
            return new ToolBarDef[0];
        }


        @Override
        public ShortCut[] shortcut4FileMenu() {
            return new ShortCut[0];
        }

        @Override
        public MenuDef[] menus4Target() {
            return new MenuDef[0];
        }

        @Override
        public JPanel[] toolbarPanes4Form() {
            return new JPanel[0];
        }

        public JComponent[] toolBarButton4Form() {
            return new JComponent[0];
        }

        public JComponent toolBar4Authority() {
            return new JPanel();
        }

        @Override
        public int getMenuState() {
            return DesignState.WORK_SHEET;
        }
        public int getToolBarHeight(){
            return PANLE_HEIGNT;
        }

        /**
         * �����˵����Ӳ˵� ��Ŀǰ����ͼ�������
         *
         * @return �Ӳ˵�
         */
    	public ShortCut[] shortcut4ExportMenu(){
            return new ShortCut[0];
        }

    };

    public NewTemplatePane getNewTemplatePane(){
       return new NewTemplatePane() {
           @Override
           public Icon getNew() {
               return BaseUtils.readIcon("/com/fr/design/images/buttonicon/addicon.png");
           }

           @Override
           public Icon getMouseOverNew() {
               return BaseUtils.readIcon("/com/fr/design/images/buttonicon/add_press.png");
           }

           @Override
           public Icon getMousePressNew() {
               return BaseUtils.readIcon("/com/fr/design/images/buttonicon/add_press.png");
           }
       };
    }

    protected void insertMenu(MenuDef menuDef, String anchor) {
        insertMenu(menuDef, anchor, new NoTargetAction());
    }

    protected void insertMenu(MenuDef menuDef, String anchor, ShortCutMethodAction action) {
        // �����ǲ���ӿڽ����
        MenuHandler[] menuHandlers = ExtraDesignClassManager.getInstance().getMenuHandlers(anchor);
        for (MenuHandler handler : menuHandlers) {
            int insertPosition = handler.insertPosition(menuDef.getShortCutCount());
            ShortCut shortCut = action.methodAction(handler);
            if (shortCut == null){
                continue;
            }

            if (insertPosition == MenuHandler.LAST) {
                if (handler.insertSeparatorBefore()) {
                    menuDef.addShortCut(SeparatorDef.DEFAULT);
                }
                menuDef.addShortCut(shortCut);
            } else {
                menuDef.insertShortCut(insertPosition, shortCut);
                if (handler.insertSeparatorBefore()) {
                    menuDef.insertShortCut(insertPosition, SeparatorDef.DEFAULT);
                    insertPosition ++;
                }
                if (handler.insertSeparatorAfter()) {
                    insertPosition ++;
                    menuDef.insertShortCut(insertPosition, SeparatorDef.DEFAULT);
                }
            }
        }
    }
    
    /**
	 * ������˳�ʱ, ����һЩ����.
	 * 
	 */
    public void shutDown(){
    	
    }

    private interface ShortCutMethodAction{

        public ShortCut methodAction(MenuHandler handler);
    }

    private abstract class AbstractShortCutMethodAction implements ShortCutMethodAction{

        public ShortCut methodAction(MenuHandler handler){
           return handler.shortcut();
        }
    }

    //����Ҫ�༭����Ĳ˵�, �����ļ�, ������, ����
    private class NoTargetAction extends AbstractShortCutMethodAction{

    }

    //ģ��Ϊ����Ĳ˵�, ����ģ��, ���������Ԫ��ҲҪ, ֱ�ӼӸ�CellTargetAction����.
    //��methodAction����handler.shortcut(cell), ����Ҫ�޸�handler��ԭ�нӿ�, �Ӹ�shortcut(cell).
    private class TemplateTargetAction extends AbstractShortCutMethodAction{

        private ToolBarMenuDockPlus plus;

        public TemplateTargetAction(ToolBarMenuDockPlus plus){
            this.plus = plus;
        }

        public ShortCut methodAction(MenuHandler handler) {
            return handler.shortcut(plus);
        }
    }
}
