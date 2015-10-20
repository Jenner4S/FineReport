package com.fr.design.mainframe.form;

import com.fr.design.event.TargetModifiedListener;
import com.fr.form.FormElementCaseProvider;

public interface FormECCompositeProvider {
	
	public static final String XML_TAG = "FormReportComponentComposite";

    public void setSelectedWidget(FormElementCaseProvider fc);
    /**
     *  ���Ŀ��ı�ļ���
     * @param targetModifiedListener     Ŀ��ı��¼�
     */
	public void addTargetModifiedListener(TargetModifiedListener targetModifiedListener);

}
