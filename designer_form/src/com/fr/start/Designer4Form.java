package com.fr.start;

import com.fr.design.mainframe.actions.NewFormAction;
import com.fr.design.menu.ShortCut;
import com.fr.design.module.FormDesignerModule;


public class Designer4Form extends BaseDesigner {

    /**
     * ������
     * @param args ��ڲ���
     */
	public static void main(String[] args) {
		new Designer4Form(args);
	}

	public Designer4Form(String[] args) {
		super(args);
	}

	@Override
	protected String module2Start() {
		return FormDesignerModule.class.getName();
	}

    /**
     * �����½��ļ��˵�
     * @return �˵�
     */
	public ShortCut[] createNewFileShortCuts() {
		return new ShortCut[]{
				new NewFormAction()
		};
	}
}
