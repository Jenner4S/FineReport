/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.form.util;

import java.awt.BasicStroke;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * @author richer
 * @since 6.5.3
 */
public class XCreatorConstants {

    private XCreatorConstants() {
    }
    public static final String WIDGETNAME = "widgetName";
    // �������Եķ���
    public static final String PROPERTY_CATEGORY = "category";
    public static final String DEFAULT_GROUP_NAME = "Form-Basic_Properties";
    public static final Color FORM_BG = new Color(252, 252, 254);
    // ��ק��ʶ��Ĵ�С
    public static final int RESIZE_BOX_SIZ = 5;
    // ��ק��С������ڲ���ɫ
    public static final Color RESIZE_BOX_INNER_COLOR = Color.white;
    // ��ק��С����ı߿���ɫ
    public static final Color RESIZE_BOX_BORDER_COLOR = new Color(143, 171, 196);
    // ��ǰѡȡ������ı߿�����ɫ
	public static final Color SELECTION_COLOR = new Color(179, 209, 236);
    // �����������߿����ɫ�ʹ�ϸ
    public static final Border AREA_BORDER = BorderFactory.createLineBorder(new Color(224, 224, 255), 0);
    // ������קʱ����ɫ
    public static final Color LAYOUT_HOTSPOT_COLOR = new Color(64, 240, 0);
    public static final Color LAYOUT_FORBIDDEN_COLOR = new Color(254, 0, 0);
    //����Ӧ������ק��ɫ
    public static final Color FIT_LAYOUT_HOTSPOT_COLOR = new Color(154, 195, 233);
    // ����Ӧ���ֵĽ������Ⱦ��ɫ
    public static final Color FIT_LAYOUT_POINT_COLOR = new Color(106, 168, 222);
    // ���Ӳ��ֵķָ���
    public static final Color LAYOUT_SEP_COLOR = new Color(210, 210, 210);
    
    // ����������������ɫ
    public static final Color OP_COLOR = new Color(157,228,245);
    
    // ��ͬ��ϸ����
    public static final BasicStroke STROKE = new BasicStroke(2);
}
