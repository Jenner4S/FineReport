package com.fr.design.menu;

import com.fr.design.DesignState;
import com.fr.design.actions.MenuAction;
import com.fr.file.XMLFileManager;
import com.fr.general.FRLogger;
import com.fr.general.GeneralUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLableReader;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 13-12-23
 * Time: ����5:25
 */
public class MenuManager extends XMLFileManager {


    private static MenuManager menuManager = null;
    //�ļ���ģ�塢���롢��Ԫ�񡢷���������������˵�
    public static final int FILE = 0;
    public static final int TEMPLATE = 1;
    public static final int INSERT = 2;
    public static final int CELL = 3;
    public static final int SERVER = 4;
    public static final int HELP = 5;
    private static final boolean[] DEFAULT_TOP_MENUS = new boolean[]{true, true, true, true, true, true};

    private boolean[] topMenuVisibleGroup = DEFAULT_TOP_MENUS;

    private ArrayList<MenuDef> menus = new ArrayList<MenuDef>();


    public synchronized static MenuManager getInstance() {
        if (menuManager == null) {
            menuManager = new MenuManager();
        }
        return menuManager;
    }

    public MenuManager() {

    }

    public ArrayList<MenuDef> getXMLMenus() {
        menus.clear();
        menuManager.readXMLFile();
        return menus;
    }

    public boolean[] getTopMenuVisibleGroup() {
        return topMenuVisibleGroup;
    }


    //�˵���16�����
    public void setMenus4Designer(DesignState state) {
        topMenuVisibleGroup = DEFAULT_TOP_MENUS;
        int designSate = state.getDesignState();
        boolean isFormLayout = designSate == DesignState.PARAMETER_PANE || designSate == DesignState.JFORM;
        if (isFormLayout || (designSate == DesignState.POLY_SHEET)) {
            topMenuVisibleGroup[INSERT] = false;
            topMenuVisibleGroup[CELL] = false;
        }

        //Ȩ�ޱ༭״̬�£�������͵�Ԫ��˵�����
        if (state.isAuthority()) {
            topMenuVisibleGroup[INSERT] = false;
            topMenuVisibleGroup[CELL] = false;
        }


        //��ͨ�û���¼��������������
        topMenuVisibleGroup[SERVER] = state.isRoot();

    }


    /**
     * �ļ���
     * @return  �ļ���
     */
    public String fileName() {
        return "menu.xml";
    }

    @Override
    public void readXML(XMLableReader reader) {
        if (reader.getTagName().equals("MenuBar")) {
            reader.readXMLObject(new XMLReadable() {
                @Override
                public void readXML(XMLableReader reader) {
                    readTopMenus(reader);
                }
            });
        }
    }

    private void readTopMenus(XMLableReader reader) {
        if (reader.isChildNode()) {
            if (reader.getTagName().equals("Menu")) {
                //��˵�Menus
                final MenuDef topMenu = new MenuDef();
                String tmpVal;
                if ((tmpVal = reader.getAttrAsString("name", StringUtils.EMPTY)) != StringUtils.EMPTY) {
                    topMenu.setName(tmpVal);
                }
                if ((tmpVal = reader.getAttrAsString("mnemonic", StringUtils.EMPTY)) != StringUtils.EMPTY) {
                    topMenu.setMnemonic(tmpVal.charAt(0));
                }
                if ((tmpVal = reader.getAttrAsString("iconPath", StringUtils.EMPTY)) != StringUtils.EMPTY) {
                    topMenu.setIconPath(tmpVal);
                }
                menus.add(topMenu);
                reader.readXMLObject(new XMLReadable() {
                    @Override
                    public void readXML(XMLableReader reader) {
                        readSubMenus(reader, topMenu);
                    }
                });
            }
        }
    }

    private void readSubMenus(XMLableReader reader, MenuDef menu) {
        if (reader.isChildNode()) {
            if (reader.getTagName().equals("Action")) {
                String name = StringUtils.EMPTY, tmpVal = StringUtils.EMPTY;
                if ((tmpVal = reader.getAttrAsString("class", StringUtils.EMPTY)) != StringUtils.EMPTY) {
                    name = tmpVal;
                }
                //��ȡģ�����ݼ��˵�
                if (name.isEmpty()) {
                    return;
                }
                try {
                    MenuAction action = (MenuAction) GeneralUtils.classForName(name).newInstance();
                    menu.addShortCut(action);
                } catch (Exception exp) {
                    FRLogger.getLogger().error(exp.getMessage(), exp);
                }
            } else if (reader.getTagName().equals("Menu")) {
                final MenuDef submenu = new MenuDef();
                String tmpVal = StringUtils.EMPTY;
                if ((tmpVal = reader.getAttrAsString("name", StringUtils.EMPTY)) != StringUtils.EMPTY) {
                    submenu.setName(tmpVal);
                }
                if ((tmpVal = reader.getAttrAsString("mnemonic", StringUtils.EMPTY)) != StringUtils.EMPTY) {
                    submenu.setMnemonic(tmpVal.charAt(0));
                }
                if ((tmpVal = reader.getAttrAsString("iconPath", StringUtils.EMPTY)) != StringUtils.EMPTY) {
                    submenu.setIconPath(tmpVal);
                }
                menu.addShortCut(submenu);
                reader.readXMLObject(new XMLReadable() {
                    @Override
                    public void readXML(XMLableReader reader) {
                        readSubMenus(reader, submenu);
                    }
                });
            }
        }
    }


    @Override
    public void writeXML(XMLPrintWriter writer) {
    }
}
