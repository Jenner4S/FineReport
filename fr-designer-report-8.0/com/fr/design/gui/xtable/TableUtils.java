// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.gui.xtable;

import com.fr.base.Formula;
import com.fr.design.mainframe.widget.editors.*;
import com.fr.design.mainframe.widget.renderer.*;
import com.fr.third.com.lowagie.text.Rectangle;
import java.awt.*;
import java.util.HashMap;
import javax.swing.table.DefaultTableCellRenderer;

public class TableUtils
{

    private static HashMap propertyEditorClasses;
    private static HashMap cellRendererClasses;

    public TableUtils()
    {
    }

    public static Class getPropertyEditorClass(Class class1)
    {
        return (Class)propertyEditorClasses.get(class1);
    }

    public static Class getTableCellRendererClass(Class class1)
    {
        return (Class)cellRendererClasses.get(class1);
    }

    static 
    {
        propertyEditorClasses = new HashMap();
        propertyEditorClasses.put(java/lang/String, com/fr/design/mainframe/widget/editors/StringEditor);
        propertyEditorClasses.put(Boolean.TYPE, com/fr/design/mainframe/widget/editors/BooleanEditor);
        propertyEditorClasses.put(java/awt/Color, com/fr/design/mainframe/widget/editors/ColorEditor);
        propertyEditorClasses.put(java/awt/Font, com/fr/design/mainframe/widget/editors/FontEditor);
        propertyEditorClasses.put(java/awt/Dimension, com/fr/design/mainframe/widget/editors/DimensionEditor);
        propertyEditorClasses.put(Integer.TYPE, com/fr/design/mainframe/widget/editors/IntegerPropertyEditor);
        propertyEditorClasses.put(java/lang/Integer, com/fr/design/mainframe/widget/editors/IntegerPropertyEditor);
        propertyEditorClasses.put(Long.TYPE, com/fr/design/mainframe/widget/editors/LongEditor);
        propertyEditorClasses.put(java/lang/Long, com/fr/design/mainframe/widget/editors/IntegerPropertyEditor);
        propertyEditorClasses.put(Float.TYPE, com/fr/design/mainframe/widget/editors/FloatEditor);
        propertyEditorClasses.put(java/lang/Float, com/fr/design/mainframe/widget/editors/IntegerPropertyEditor);
        propertyEditorClasses.put(Double.TYPE, com/fr/design/mainframe/widget/editors/DoubleEditor);
        propertyEditorClasses.put(java/lang/Double, com/fr/design/mainframe/widget/editors/IntegerPropertyEditor);
        propertyEditorClasses.put(com/fr/base/Formula, com/fr/design/mainframe/widget/editors/FormulaEditor);
        cellRendererClasses = new HashMap();
        cellRendererClasses.put(java/awt/Color, com/fr/design/mainframe/widget/renderer/ColorCellRenderer);
        cellRendererClasses.put(java/awt/Font, com/fr/design/mainframe/widget/renderer/FontCellRenderer);
        cellRendererClasses.put(java/lang/String, javax/swing/table/DefaultTableCellRenderer);
        cellRendererClasses.put(Integer.TYPE, javax/swing/table/DefaultTableCellRenderer);
        cellRendererClasses.put(java/lang/Integer, javax/swing/table/DefaultTableCellRenderer);
        cellRendererClasses.put(Long.TYPE, javax/swing/table/DefaultTableCellRenderer);
        cellRendererClasses.put(java/lang/Long, javax/swing/table/DefaultTableCellRenderer);
        cellRendererClasses.put(Float.TYPE, javax/swing/table/DefaultTableCellRenderer);
        cellRendererClasses.put(java/lang/Float, javax/swing/table/DefaultTableCellRenderer);
        cellRendererClasses.put(Double.TYPE, javax/swing/table/DefaultTableCellRenderer);
        cellRendererClasses.put(java/lang/Double, javax/swing/table/DefaultTableCellRenderer);
        cellRendererClasses.put(java/awt/Point, com/fr/design/mainframe/widget/renderer/PointCellRenderer);
        cellRendererClasses.put(java/awt/Dimension, com/fr/design/mainframe/widget/renderer/DimensionCellRenderer);
        cellRendererClasses.put(com/fr/third/com/lowagie/text/Rectangle, com/fr/design/mainframe/widget/renderer/RectangleCellRenderer);
    }
}
