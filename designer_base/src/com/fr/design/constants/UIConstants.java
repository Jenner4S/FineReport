/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.constants;

import com.fr.base.BaseUtils;
import com.fr.general.Inter;
import com.fr.stable.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This class defines the constants used in the designer.
 */
public interface UIConstants {


    public static final Icon BLACK_ICON = BaseUtils.readIcon("/com/fr/base/images/cell/blank.gif");


    public static final int SIZE = 17;

    /**
     * Cell default cursor.
     */
    public static final Cursor CELL_DEFAULT_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
            BaseUtils.readImage("/com/fr/base/images/cell/cursor/cell_default.png"),
            new Point(16, 16), "CellDefaultCursor");
    public static final Cursor DRAW_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
            BaseUtils.readImage("/com/fr/base/images/cell/cursor/cursor_draw.png"),
            new Point(16, 16), "DrawCursor");


    public static final Cursor FORMAT_BRUSH_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
            BaseUtils.readImage("/com/fr/base/images/cell/cursor/brush_cursor0.png"),
            new Point(16, 16), "formatBrushCursor");

    /**
     * Border style array.
     */
    public final static int[] BORDER_LINE_STYLE_ARRAY = new int[]{
            Constants.LINE_THIN, //Thin border.
            Constants.LINE_MEDIUM, //Medium border
            Constants.LINE_DASH, //dash border
            Constants.LINE_HAIR, //hair-line border
            Constants.LINE_HAIR2, //hair-line border
            Constants.LINE_THICK, //Thick border
            Constants.LINE_DOUBLE, //double-line border
            Constants.LINE_DOT, //dot border
            Constants.LINE_MEDIUM_DASH, //Medium dashed border
            Constants.LINE_DASH_DOT, //dash-dot border
            Constants.LINE_MEDIUM_DASH_DOT, //medium dash-dot border
            Constants.LINE_DASH_DOT_DOT, //dash-dot-dot border
            Constants.LINE_MEDIUM_DASH_DOT_DOT, //medium dash-dot-dot border
            Constants.LINE_SLANTED_DASH_DOT, //slanted dash-dot border
    };
    public static final Color LINE_COLOR = new Color(153, 153, 153);
    public static final Color FONT_COLOR = new Color(51, 51, 51);
    public static final Color LIGHT_BLUE = new Color(182, 217, 253);
    public static final Color SKY_BLUE = new Color(164, 192, 220);
    public static final Color OCEAN_BLUE = new Color(141, 179, 217);
    public static final Color DARK_BLUE = new Color(0, 88, 144);
    public static final Color NORMAL_BACKGROUND = new Color(240, 240, 240);
    public static final Color SHADOW_GREY = new Color(0xe2e2e2);
    public static final Color SHADOW_CENTER = new Color(200, 200, 200);
    public static final Color SHADOW_PURPLE = new Color(255, 0, 255);
    public static final Color FLESH_BLUE = new Color(168, 180, 202);
    public static final Color HOVER_BLUE = new Color(0xd2d2d2);
    public static final Color DOTTED_LINE_COLOR = new Color(35, 108, 184);
    public static final Color AUTHORITY_COLOR = new Color(88, 125, 153);
    public static final Color AUTHORITY_BLUE = new Color(0xe2e2e2);
    public static final Color AUTHORITY_DARK_BLUE = new Color(136, 164, 186);
    public static final Color AUTHORITY_PRESS_BLUE = new Color(131, 159, 181);
    public static final Color AUTHORITY_LINE_COLOR = new Color(0, 124, 229);
    public static final Color AUTHORITY_SHEET_DARK = new Color(86, 120, 143);
    public static final Color AUTHORITY_SHEET_LIGHT = new Color(156, 204, 238);
    public static final Color AUTHORITY_SHEET_UNSELECTED = new Color(146, 192, 225);
    public static final Color ATTRIBUTE_PRESS = new Color(0xdfecfc);
    public static final Color ATTRIBUTE_NORMAL = new Color(0xe2e2e2);
    public static final Color ATTRIBUTE_HOVER = new Color(0xd3d3d3);
    public static final Color CHECKBOX_HOVER_SELECTED = new Color(0x3394f0);
    public static final Color TEXT_FILED_BORDER_SELECTED = new Color(0x3384f0);
    public static final Color SHEET_NORMAL = new Color(0xc8c8ca);
    public static final Color SELECTED_BACKGROUND = new Color(0xdeedfe);
    public static final Color SELECTED_BORDER_LINE_COLOR = new Color(0x3384f0);
    public static final Color DEFAULT_BG_RULER = new Color(0xf7f7f7);
    public static final Color RULER_LINE_COLOR = new Color(0xababab);
    public static final Color RULER_SCALE_COLOR = new Color(0x4e504f);



    public static final BufferedImage DRAG_BAR = BaseUtils.readImage("com/fr/design/images/control/bar.png");
    public static final BufferedImage DRAG_BAR_RIGHT = BaseUtils.readImage("com/fr/design/images/control/barm.png");
    public static final BufferedImage DRAG_BAR_LEFT = BaseUtils.readImage("com/fr/design/images/control/barl.png");
    public static final BufferedImage DRAG_UP_NORMAL = BaseUtils.readImage("com/fr/design/images/control/upnor.png");
    public static final BufferedImage DRAG_UP_PRESS = BaseUtils.readImage("com/fr/design/images/control/uppre.png");
    public static final BufferedImage DRAG_DOWN_NORMAL = BaseUtils.readImage("com/fr/design/images/control/downnor.png");
    public static final BufferedImage DRAG_DOWN_PRESS = BaseUtils.readImage("com/fr/design/images/control/downpre.png");
    public static final BufferedImage DRAG_RIGHT_NORMAL = BaseUtils.readImage("com/fr/design/images/control/rightnor.png");
    public static final BufferedImage DRAG_RIGHT_PRESS = BaseUtils.readImage("com/fr/design/images/control/rightpre.png");
    public static final BufferedImage DRAG_LEFT_NORMAL = BaseUtils.readImage("com/fr/design/images/control/leftnor.png");
    public static final BufferedImage DRAG_LEFT_PRESS = BaseUtils.readImage("com/fr/design/images/control/leftpre.png");
    public static final BufferedImage DRAG_DOT = BaseUtils.readImage("com/fr/design/images/control/dot.png");
    public static final BufferedImage DRAG_DOT_VERTICAL = BaseUtils.readImage("com/fr/design/images/control/dotv.png");
    public static final int MODEL_NORMAL = 0;
    public static final int MODEL_PRESS = 1;
    public static final Icon ARROW_DOWN_ICON = BaseUtils.readIcon("com/fr/design/images/buttonicon/arrowdown.png");
    public static final Icon ARROW_UP_ICON = BaseUtils.readIcon("com/fr/design/images/buttonicon/arrowup.png");
    public static final Icon YES_ICON = BaseUtils.readIcon("com/fr/design/images/buttonicon/yes.png");
    public static final Icon CHOOSEN_ICON = BaseUtils.readIcon("com/fr/design/images/buttonicon/select_item.png");
    public static final Icon PRE_WIDGET_ICON = BaseUtils.readIcon("com/fr/design/images/buttonicon/prewidget.png");
    public static final Icon EDIT_NORMAL_ICON = BaseUtils.readIcon("com/fr/design/images/buttonicon/editn.png");
    public static final Icon EDIT_PRESSED_ICON = BaseUtils.readIcon("com/fr/design/images/buttonicon/editp.png");
    public static final Icon HIDE_NORMAL_ICON = BaseUtils.readIcon("com/fr/design/images/buttonicon/hiden.png");
    public static final Icon HIDE_PRESSED_ICON = BaseUtils.readIcon("com/fr/design/images/buttonicon/hidep.png");
    public static final Icon VIEW_NORMAL_ICON = BaseUtils.readIcon("com/fr/design/images/buttonicon/viewn.png");
    public static final Icon VIEW_PRESSED_ICON = BaseUtils.readIcon("com/fr/design/images/buttonicon/viewp.png");
    public static final Icon RUN_BIG_ICON = BaseUtils.readIcon("com/fr/design/images/buttonicon/run24.png");
    public static final Icon RUN_SMALL_ICON = BaseUtils.readIcon("com/fr/design/images/buttonicon/runs.png");
    public static final Icon PAGE_BIG_ICON = BaseUtils.readIcon("com/fr/design/images/buttonicon/pageb24.png");
    public static final Icon WRITE_BIG_ICON = BaseUtils.readIcon("com/fr/design/images/buttonicon/writeb24.png");
    public static final Icon ANA_BIG_ICON = BaseUtils.readIcon("com/fr/design/images/buttonicon/anab24.png");
    public static final Icon PAGE_SMALL_ICON = BaseUtils.readIcon("com/fr/design/images/buttonicon/pages.png");
    public static final Icon WRITE_SMALL_ICON = BaseUtils.readIcon("com/fr/design/images/buttonicon/writes.png");
    public static final Icon ANA_SMALL_ICON = BaseUtils.readIcon("com/fr/design/images/buttonicon/anas.png");
    public static final Icon REFRESH_ICON = BaseUtils.readIcon("com/fr/design/images/buttonicon/refresh.png");
    public static final Icon FONT_ICON = BaseUtils.readIcon("/com/fr/design/images/gui/color/foreground.png");
    public static final Icon HISTORY_ICON = BaseUtils.readIcon("com/fr/design/images/buttonicon/history.png");
    public static final Icon DELETE_ICON = BaseUtils.readIcon("com/fr/design/images/m_file/close.png");
    public static final Icon EDIT_ICON = BaseUtils.readIcon("com/fr/design/images/m_file/edit.png");
    public static final Icon SEARCH_ICON = BaseUtils.readIcon("/com/fr/design/images/data/search.png");
    public static final Icon CLEAR_ICON = BaseUtils.readIcon("/com/fr/design/images/data/source/delete.png");
    public static final Color PRESSED_DARK_GRAY = new Color(127, 127, 127);
    public static final Color GRDIENT_DARK_GRAY = new Color(45, 45, 45);
    public static final Color BARNOMAL = new Color(153, 153, 153);
    public static final int ARC = 0;
    public static final int LARGEARC = 0;
    public static final Stroke BS = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 2f, new float[]{3, 1}, 0);
    public static final Icon PREVIEW_DOWN = BaseUtils.readIcon("com/fr/design/images/buttonicon/prevew_down_icon.png");
    public static final Icon CLOSE_OF_AUTHORITY = BaseUtils.readIcon("/com/fr/design/images/m_report/close.png");
    public static final Icon CLOSE_OVER_AUTHORITY = BaseUtils.readIcon("/com/fr/design/images/m_report/close_over.png");
    public static final Icon CLOSE_PRESS_AUTHORITY = BaseUtils.readIcon("/com/fr/design/images/m_report/close_press.png");
    public static final int CLOSE_AUTHORITY_HEIGHT_AND_WIDTH = 24;


    /**
     * 正在加载的界面
     */
    public static final Object PENDING = new Object() {

        @Override
        public String toString() {
            return Inter.getLocText("Loading") + "...";
        }
    };
    /**
     * 数据库连接失败的界面
     */
    public static final Object CONNECTION_FAILED = new Object() {

        public String toString() {
            return Inter.getLocText(new String[]{"Database", "Datasource-Connection_failed"}) + "!";
        }
    };

    /**
     * 自动补全的默认快捷键，一般来说是 alt + /.
     */
    public static final String DEFAULT_AUTO_COMPLETE = "alt + SLASH";
}