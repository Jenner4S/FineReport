package com.fr.design.gui.xpane;

import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.frpane.HyperlinkGroupPane;
import com.fr.design.form.javascript.FormEmailPane;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.js.EmailJavaScript;

public class FormHyperlinkGroupPane extends  HyperlinkGroupPane{
	
	/**
     * ������Ӱ�ť��NameableCreator
     * ���ڱ������ĵ�Ԫ�����͵�Ԫ���������Գ����е�emailPane��Ҫ�ñ���emailPane�����������
     *
     * @return ����Nameable��ť����.
     */
	 public NameableCreator[] createNameableCreators() {
		 NameableCreator[] creators = super.createNameableCreators();
		 for (int i=0; i<creators.length; i++) {
			 if (ComparatorUtils.equals(creators[i].menuName(), Inter.getLocText("FR-Designer_Email"))) {
				 creators[i] = new NameObjectCreator(Inter.getLocText("FR-Designer_Email"), EmailJavaScript.class, FormEmailPane.class);
				 break;
			 }
		 }
		 return creators;
	 }

}
