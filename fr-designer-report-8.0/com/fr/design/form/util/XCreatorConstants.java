// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.form.util;

import java.awt.BasicStroke;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class XCreatorConstants
{

    public static final String WIDGETNAME = "widgetName";
    public static final String PROPERTY_CATEGORY = "category";
    public static final String DEFAULT_GROUP_NAME = "Form-Basic_Properties";
    public static final Color FORM_BG = new Color(252, 252, 254);
    public static final int RESIZE_BOX_SIZ = 5;
    public static final Color RESIZE_BOX_INNER_COLOR;
    public static final Color RESIZE_BOX_BORDER_COLOR = new Color(143, 171, 196);
    public static final Color SELECTION_COLOR = new Color(179, 209, 236);
    public static final Border AREA_BORDER = BorderFactory.createLineBorder(new Color(224, 224, 255), 0);
    public static final Color LAYOUT_HOTSPOT_COLOR = new Color(64, 240, 0);
    public static final Color LAYOUT_FORBIDDEN_COLOR = new Color(254, 0, 0);
    public static final Color FIT_LAYOUT_HOTSPOT_COLOR = new Color(154, 195, 233);
    public static final Color FIT_LAYOUT_POINT_COLOR = new Color(106, 168, 222);
    public static final Color LAYOUT_SEP_COLOR = new Color(210, 210, 210);
    public static final Color OP_COLOR = new Color(157, 228, 245);
    public static final BasicStroke STROKE = new BasicStroke(2.0F);

    private XCreatorConstants()
    {
    }

    static 
    {
        RESIZE_BOX_INNER_COLOR = Color.white;
    }
}
