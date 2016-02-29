/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.insert.cell;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;

import javax.swing.*;

/**
 *
 */
public class GeneralCellAction extends AbstractCellAction {
	public GeneralCellAction(ElementCasePane t) {
    	super(t);
        this.setMenuKeySet(INSERT_TEXT);
        this.setName(getMenuKeySet().getMenuKeySetName()+ "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_insert/text.png"));
    }

    public static final MenuKeySet INSERT_TEXT = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'T';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_Insert-Text");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    @Override
	public Class getCellValueClass() {
        return String.class;
    }
}