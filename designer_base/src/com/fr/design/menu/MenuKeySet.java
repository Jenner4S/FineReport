package com.fr.design.menu;


import javax.swing.*;

/**
 * ��ݼ����ýӿ�
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-3-13
 * Time: ����10:50
 */
public abstract class MenuKeySet {
    private String menuName;
    private KeyStroke menuKeyStore;
    private char mnemonic;

    public MenuKeySet() {
        menuName = getMenuName();
        menuKeyStore = getKeyStroke();
        mnemonic = getMnemonic();
    }

    public abstract char getMnemonic();

    public abstract String getMenuName();

    public String getMenuKeySetName(){
        return getMenuName()+ "(" +getMnemonic() + ")";
    }

    public abstract KeyStroke getKeyStroke();
}
