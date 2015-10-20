/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.gui.itextarea;


/**
 * peter:����ؼ���Ҫ�������ڽ�����ʾ������Ϣ��.
 */
public class DescriptionTextArea extends UITextArea {
    public DescriptionTextArea() {
        this(3);
    }

    public DescriptionTextArea(int row) {
        this(row, 8);
    }

    public DescriptionTextArea(int rows, int columns) {
        super(rows, columns);
        this.setBackground(null);
        this.setLineWrap(true);

        this.setWrapStyleWord(true);
        this.setEditable(false);
    }
}