package com.fr.design.mainframe;


public abstract class FormDockView extends DockingView {
	
	private FormDesigner editor;
	
	public void setEditingFormDesigner(FormDesigner  editor) {
		this.editor = editor;
	}
    
    // TODO ALEX_SEP dockingView.enabled��֪�ܷ������������ & ��ʱ��ʵ��
    public FormDesigner getEditingFormDesigner() {
    	return editor;
    }
    
}
