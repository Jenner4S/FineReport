package com.fr.design.mainframe.widget.editors;

import java.awt.Color;

import com.fr.design.gui.itextfield.UITextField;

/**
 * ������ʾ��ֱ�ӱ༭�Ŀؼ�������ֵ�Ŀؼ�
 * @since 6.5.2
 */
public class TextField extends UITextField implements ITextComponent {

    public TextField() {
        setBorder(null);
        setOpaque(false);
    }

    @Override
    public void setValue(Object v) {
    }

    @Override
    public void setEditable(boolean b) {
        super.setEditable(b);
        setBackground(Color.white);
    }
}
