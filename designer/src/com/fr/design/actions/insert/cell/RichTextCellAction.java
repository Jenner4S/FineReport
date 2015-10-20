package com.fr.design.actions.insert.cell;

import javax.swing.KeyStroke;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.core.RichText;

public class RichTextCellAction extends AbstractCellAction {

	public RichTextCellAction(ElementCasePane t) {
    	super(t);
        this.setMenuKeySet(INSERT_RICHTEXT);
        this.setName(getMenuKeySet().getMenuKeySetName()+ "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon(
                "/com/fr/design/images/m_insert/richtext.png"));
	}

	@Override
	public Class getCellValueClass() {
		return RichText.class;
	}

	/**
	 * equals �Ƚ�
	 * @param object
	 * @return true false
	 */
	@Override
	public boolean equals(Object object) {
		if (this == object){
			return true;
		}
		if (!(object instanceof RichTextCellAction)){
			return false;
		}

		return ComparatorUtils.equals(this.getEditingComponent(),((RichTextCellAction)object).getEditingComponent());
	}

    private static final MenuKeySet INSERT_RICHTEXT = new MenuKeySet() {
    	@Override
    	public char getMnemonic() {
    		return 'R';
    	}
    	
    	@Override
    	public String getMenuName() {
    		return Inter.getLocText("FR-Designer_RichText");
    	}
    	
    	@Override
    	public KeyStroke getKeyStroke() {
    		return null;
    	}
    };
}
