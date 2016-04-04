// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly.hanlder;

import com.fr.base.BaseUtils;
import com.fr.base.ScreenResolution;
import com.fr.general.ComparatorUtils;
import com.fr.poly.PolyDesigner;
import com.fr.poly.creator.*;
import com.fr.report.poly.*;
import com.fr.stable.unit.*;
import java.awt.Point;
import java.awt.event.MouseEvent;

// Referenced classes of package com.fr.poly.hanlder:
//            BlockOperationMouseHandler

public abstract class RowOperationMouseHandler extends BlockOperationMouseHandler
{
    public static class ChartBlockRowOperationMouseHandler extends RowOperationMouseHandler
    {

        public void mousePressed(MouseEvent mouseevent)
        {
            ((ChartBlockEditor)getTargetEditor()).refreshChartComponent();
            mousePressed(mouseevent);
        }

        public void mouseReleased(MouseEvent mouseevent)
        {
            if(isEditorResized())
                designer.fireTargetModified();
        }

        public ChartBlockRowOperationMouseHandler(PolyDesigner polydesigner, ChartBlockEditor chartblockeditor)
        {
            super(polydesigner, chartblockeditor);
        }
    }

    public static class ECBlockRowOperationMouseHandler extends RowOperationMouseHandler
    {

        public void mouseReleased(MouseEvent mouseevent)
        {
            if(isEditorResized())
            {
                ((PolyECBlock)((ECBlockEditor)getTargetEditor()).getValue()).reCalculateBlockSize();
                designer.fireTargetModified();
            }
        }

        public ECBlockRowOperationMouseHandler(PolyDesigner polydesigner, ECBlockEditor ecblockeditor)
        {
            super(polydesigner, ecblockeditor);
        }
    }


    protected PolyDesigner designer;
    private BlockEditor editor;
    private Point startPoint;
    private UnitRectangle oldBounds;
    private int resolution;

    public RowOperationMouseHandler(PolyDesigner polydesigner, BlockEditor blockeditor)
    {
        resolution = ScreenResolution.getScreenResolution();
        designer = polydesigner;
        editor = blockeditor;
    }

    protected BlockEditor getTargetEditor()
    {
        return editor;
    }

    protected boolean isEditorResized()
    {
        return !ComparatorUtils.equals(editor.getValue().getBounds(), oldBounds);
    }

    public void mousePressed(MouseEvent mouseevent)
    {
        if(BaseUtils.isAuthorityEditing())
            designer.noAuthorityEdit();
        startPoint = mouseevent.getPoint();
        designer.setChooseType(com.fr.poly.PolyDesigner.SelectionType.BLOCK);
        oldBounds = editor.getValue().getBounds();
    }

    public void mouseDragged(MouseEvent mouseevent)
    {
        if(BaseUtils.isAuthorityEditing())
        {
            return;
        } else
        {
            TemplateBlock templateblock = editor.getValue();
            Point point = mouseevent.getPoint();
            int i = point.y - startPoint.y;
            UnitRectangle unitrectangle = templateblock.getBounds();
            FU fu = FU.valueOfPix(i, resolution);
            unitrectangle.setHeight(unitrectangle.getHeight().add(fu));
            templateblock.setBounds(unitrectangle, getTarget());
            return;
        }
    }

    protected PolyWorkSheet getTarget()
    {
        return (PolyWorkSheet)designer.getTarget();
    }
}
