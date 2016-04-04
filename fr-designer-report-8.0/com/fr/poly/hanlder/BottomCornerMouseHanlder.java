// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly.hanlder;

import com.fr.base.BaseUtils;
import com.fr.base.ScreenResolution;
import com.fr.design.beans.location.Absorptionline;
import com.fr.design.beans.location.MoveUtils;
import com.fr.general.ComparatorUtils;
import com.fr.poly.PolyDesigner;
import com.fr.poly.creator.BlockEditor;
import com.fr.report.block.Block;
import com.fr.report.poly.PolyWorkSheet;
import com.fr.report.poly.TemplateBlock;
import com.fr.stable.ArrayUtils;
import com.fr.stable.unit.UnitRectangle;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;

public class BottomCornerMouseHanlder extends MouseInputAdapter
{

    private PolyDesigner designer;
    private BlockEditor editor;
    private Point pressed;
    private UnitRectangle oldBounds;
    private int resolution;
    private com.fr.design.beans.location.MoveUtils.RectangleDesigner rectDesigner;

    public BottomCornerMouseHanlder(PolyDesigner polydesigner, BlockEditor blockeditor)
    {
        resolution = ScreenResolution.getScreenResolution();
        rectDesigner = new com.fr.design.beans.location.MoveUtils.RectangleDesigner() {

            final BottomCornerMouseHanlder this$0;

            public void setXAbsorptionline(Absorptionline absorptionline)
            {
                editor.setXAbsorptionline(absorptionline);
            }

            public void setYAbsorptionline(Absorptionline absorptionline)
            {
                editor.setYAbsorptionline(absorptionline);
            }

            public com.fr.design.beans.location.MoveUtils.RectangleIterator createRectangleIterator()
            {
                return getRectangleIt();
            }

            public int[] getVerticalLine()
            {
                return editor.getValue().getVerticalLine();
            }

            public int[] getHorizontalLine()
            {
                return editor.getValue().getHorizontalLine();
            }

            
            {
                this$0 = BottomCornerMouseHanlder.this;
                super();
            }
        }
;
        designer = polydesigner;
        editor = blockeditor;
    }

    public void mousePressed(MouseEvent mouseevent)
    {
        if(BaseUtils.isAuthorityEditing())
            designer.noAuthorityEdit();
        pressed = mouseevent.getPoint();
        oldBounds = editor.getValue().getBounds();
        designer.setChooseType(com.fr.poly.PolyDesigner.SelectionType.BLOCK);
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
        if(BaseUtils.isAuthorityEditing())
            designer.noAuthorityEdit();
        editor.setDragging(false);
        if(designer.intersectsAllBlock(editor.getValue()))
        {
            editor.getValue().setBounds(oldBounds);
            return;
        }
        if(!ComparatorUtils.equals(editor.getValue().getBounds(), oldBounds))
            designer.fireTargetModified();
        designer.repaint();
    }

    public void mouseDragged(MouseEvent mouseevent)
    {
        if(BaseUtils.isAuthorityEditing())
        {
            designer.noAuthorityEdit();
            return;
        } else
        {
            editor.setDragging(true);
            Point point = mouseevent.getPoint();
            point.x -= pressed.x;
            point.y -= pressed.y;
            TemplateBlock templateblock = editor.getValue();
            Rectangle rectangle = templateblock.getBounds().toRectangle(resolution);
            Point point1 = MoveUtils.sorption(rectangle.x + point.x >= 0 ? rectangle.x + point.x : 0, rectangle.y + point.y >= 0 ? rectangle.y + point.y : 0, rectangle.width, rectangle.height, rectDesigner);
            templateblock.setBounds(new UnitRectangle(new Rectangle(point1.x, point1.y, rectangle.width, rectangle.height), resolution));
            designer.repaint();
            return;
        }
    }

    private com.fr.design.beans.location.MoveUtils.RectangleIterator getRectangleIt()
    {
        return new com.fr.design.beans.location.MoveUtils.RectangleIterator() {

            private int i;
            final BottomCornerMouseHanlder this$0;

            public boolean hasNext()
            {
                boolean flag = i >= ((PolyWorkSheet)designer.getTarget()).getBlockCount();
                if(flag)
                    return false;
                boolean flag1 = ((PolyWorkSheet)designer.getTarget()).getBlock(i) == editor.getValue();
                if(!flag1)
                {
                    return true;
                } else
                {
                    boolean flag2 = ++i < ((PolyWorkSheet)designer.getTarget()).getBlockCount();
                    return flag2;
                }
            }

            public int[] getHorizontalLine()
            {
                TemplateBlock templateblock = (TemplateBlock)((PolyWorkSheet)designer.getTarget()).getBlock(i - 1);
                if(templateblock == null)
                    return ArrayUtils.EMPTY_INT_ARRAY;
                else
                    return templateblock.getHorizontalLine();
            }

            public int[] getVerticalLine()
            {
                TemplateBlock templateblock = (TemplateBlock)((PolyWorkSheet)designer.getTarget()).getBlock(i - 1);
                if(templateblock == null)
                    return ArrayUtils.EMPTY_INT_ARRAY;
                else
                    return templateblock.getVerticalLine();
            }

            public Rectangle nextRectangle()
            {
                UnitRectangle unitrectangle = ((PolyWorkSheet)designer.getTarget()).getBlock(i++).getBounds();
                return unitrectangle.toRectangle(resolution);
            }

            
            {
                this$0 = BottomCornerMouseHanlder.this;
                super();
            }
        }
;
    }




}
