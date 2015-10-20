package com.fr.design.mainframe.widget.editors;

import java.awt.event.ActionListener;

/**
 * ������ʾֵ��ʵ��ֵ����ʾ��
 * 
 * @since 6.5.2
 */
public interface ITextComponent {

    public void setText(String text);

    public String getText();

    public void setEditable(boolean editable);

    public void addActionListener(ActionListener l);

    public void selectAll();

    public void setValue(Object v);
}
