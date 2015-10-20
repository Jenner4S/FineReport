/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.cell.editor;

import com.fr.base.Style;
import com.fr.base.TextFormat;
import com.fr.report.ReportHelper;
import com.fr.report.cell.FloatElement;

/**
 * FloatEditor used to edit general object.
 */
public class GeneralFloatEditor extends TextFloatEditor {
    /**
     * Gets the value of the FloatEditor.
     */
    @Override
	public Object getFloatEditorValue()  throws Exception {
        Object textValue = super.getFloatEditorValue();

        //�����ʽ��TextFormat���ͷ�����ͨ�ı�.
        //TODO, peter ����ط���Ҫ�������,�Ҹо������ convertGeneralStringAccordingToExcel��������Foramt��ʵ�֡�
        FloatElement floatElement = this.getFloatElement();
        //peter:ֻ����ʽ���Style.
        Style style = floatElement.getStyle();
        if(style != null &&
                style.getFormat() != null && style.getFormat() == TextFormat.getInstance()) {
            return textValue;
        }

        return ReportHelper.convertGeneralStringAccordingToExcel(textValue);
    }
}
