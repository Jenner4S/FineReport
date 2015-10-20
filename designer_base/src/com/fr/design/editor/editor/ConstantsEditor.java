package com.fr.design.editor.editor;

import com.fr.base.Formula;


public class ConstantsEditor extends FormulaEditor {

	public ConstantsEditor(String name, Formula formula) {
		super(name, formula);
	}
	
	protected void showFormulaPane() {
		// do nothing ��ֹ�޸�...
	}
	
	public void setValue(Formula value) {
		// do nothing ��ֹ�޸�...
    }
	
	public boolean accept(Object object) {
        return object instanceof Formula && object.equals(this.getValue());
    }
}
