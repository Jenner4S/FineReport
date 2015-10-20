/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe.widget.wrappers;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;
import com.fr.design.gui.xpane.LayoutBorderPane;
import com.fr.form.ui.LayoutBorderStyle;

public class LayoutBorderStyleWrapper implements Encoder, Decoder {
    public LayoutBorderStyleWrapper() {
        
    }

    /**
     * ������ת�����ַ���
     * @param v    ���Զ���
     * @return      �ַ���
     */
    public String encode(Object v) {
       if (v == null) {
           return null;
       }
       LayoutBorderStyle style = (LayoutBorderStyle)v;
       return LayoutBorderPane.BORDER_TYPE[style.getType()];
    }

    /**
     * ���ַ���ת��������
     * @param txt  �ַ���
     * @return  ���Զ���
     */
    public Object decode(String txt) {
        return null;
    }

    /**
     *  ���Ϲ���
     * @param txt   �ַ���
     * @throws ValidationException    �״�
     */
    public void validate(String txt) throws ValidationException {
        
    }

}
