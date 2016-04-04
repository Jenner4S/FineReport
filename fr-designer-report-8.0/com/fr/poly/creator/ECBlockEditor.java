// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly.creator;

import com.fr.base.BaseUtils;
import com.fr.design.constants.UIConstants;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.mainframe.*;
import com.fr.design.mainframe.cell.QuickEditorRegion;
import com.fr.grid.Grid;
import com.fr.grid.GridUtils;
import com.fr.poly.PolyDesigner;
import com.fr.poly.hanlder.ColumnOperationMouseHandler;
import com.fr.poly.hanlder.RowOperationMouseHandler;
import com.fr.report.poly.PolyECBlock;
import com.fr.stable.unit.*;
import java.awt.Dimension;
import javax.swing.JComponent;

// Referenced classes of package com.fr.poly.creator:
//            BlockEditor, ECBlockPane, BlockCreator, ECBlockCreator

public class ECBlockEditor extends BlockEditor
{

    private static final int HEIGHT_MORE = 5;

    public ECBlockEditor(PolyDesigner polydesigner, ECBlockCreator ecblockcreator)
    {
        super(polydesigner, ecblockcreator);
    }

    protected void initDataChangeListener()
    {
        ((ECBlockPane)editComponent).addTargetModifiedListener(new TargetModifiedListener() {

            final ECBlockEditor this$0;

            public void targetModified(TargetModifiedEvent targetmodifiedevent)
            {
                designer.fireTargetModified();
            }

            
            {
                this$0 = ECBlockEditor.this;
                super();
            }
        }
);
    }

    public ECBlockPane createEffective()
    {
        PolyECBlock polyecblock = (PolyECBlock)creator.getValue();
        if(editComponent == null)
            editComponent = new ECBlockPane(designer, polyecblock, this);
        if(DesignerContext.getFormatState() == 0)
            ((ECBlockPane)editComponent).getGrid().setCursor(UIConstants.CELL_DEFAULT_CURSOR);
        return (ECBlockPane)editComponent;
    }

    protected void initSize()
    {
        Dimension dimension = getCornerSize();
        PolyECBlock polyecblock = (PolyECBlock)getValue();
        UnitRectangle unitrectangle = polyecblock.getBounds();
        int i = unitrectangle.x.toPixI(resolution) - dimension.width - designer.getHorizontalValue();
        int j = unitrectangle.y.toPixI(resolution) - dimension.height - designer.getVerticalValue();
        int k = unitrectangle.width.toPixI(resolution) + dimension.width + 15 + UNITConstants.DELTA.toPixI(resolution);
        int l = unitrectangle.height.toPixI(resolution) + dimension.height + 15 + UNITConstants.DELTA.toPixI(resolution);
        setBounds(i, j, k, l);
        ((ECBlockPane)editComponent).getGrid().setVerticalExtent(GridUtils.getExtentValue(0, polyecblock.getRowHeightList_DEC(), ((ECBlockPane)editComponent).getGrid().getHeight(), resolution));
        ((ECBlockPane)editComponent).getGrid().setHorizontalExtent(GridUtils.getExtentValue(0, polyecblock.getColumnWidthList_DEC(), ((ECBlockPane)editComponent).getGrid().getWidth(), resolution));
    }

    public void setBounds(int i, int j, int k, int l)
    {
        int i1 = l + 5;
        super.setBounds(i, j, k, i1);
    }

    protected Dimension getAddHeigthPreferredSize()
    {
        Dimension dimension = getCornerSize();
        dimension.height = 15;
        return dimension;
    }

    protected Dimension getAddWidthPreferredSize()
    {
        Dimension dimension = getCornerSize();
        dimension.width = 15;
        return dimension;
    }

    protected RowOperationMouseHandler createRowOperationMouseHandler()
    {
        return new com.fr.poly.hanlder.RowOperationMouseHandler.ECBlockRowOperationMouseHandler(designer, this);
    }

    protected ColumnOperationMouseHandler createColumnOperationMouseHandler()
    {
        return new com.fr.poly.hanlder.ColumnOperationMouseHandler.ECBlockColumnOperationMouseHandler(designer, this);
    }

    public Dimension getCornerSize()
    {
        return ((ECBlockPane)editComponent).getCornerSize();
    }

    public void resetSelectionAndChooseState()
    {
        designer.setChooseType(com.fr.poly.PolyDesigner.SelectionType.INNER);
        if(BaseUtils.isAuthorityEditing())
        {
            JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
            if(jtemplate.isJWorkBook())
                jtemplate.removeParameterPaneSelection();
            designer.noAuthorityEdit();
            return;
        } else
        {
            QuickEditorRegion.getInstance().populate(((ECBlockPane)editComponent).getCurrentEditor());
            CellElementPropertyPane.getInstance().populate((ElementCasePane)editComponent);
            EastRegionContainerPane.getInstance().replaceDownPane(CellElementPropertyPane.getInstance());
            return;
        }
    }

    public volatile JComponent createEffective()
    {
        return createEffective();
    }
}
