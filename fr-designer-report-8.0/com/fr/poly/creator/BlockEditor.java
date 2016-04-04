// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly.creator;

import com.fr.base.ScreenResolution;
import com.fr.design.beans.location.Absorptionline;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.general.ComparatorUtils;
import com.fr.poly.PolyDesigner;
import com.fr.poly.hanlder.*;
import com.fr.report.poly.TemplateBlock;
import com.fr.stable.core.PropertyChangeAdapter;
import com.fr.stable.unit.UNIT;
import com.fr.stable.unit.UnitRectangle;
import java.awt.*;
import javax.swing.JComponent;

// Referenced classes of package com.fr.poly.creator:
//            BlockEditorLayout, BlockCreator

public abstract class BlockEditor extends JComponent
{

    protected PolyDesigner designer;
    protected BlockCreator creator;
    protected int resolution;
    protected JComponent editComponent;
    private JComponent addHeightTool;
    private JComponent addWidthTool;
    private JComponent moveTool;
    private boolean isDragging;
    private Absorptionline lineInX;
    private Absorptionline lineInY;
    private BlockForbiddenWindow forbiddenWindow;

    public BlockEditor(PolyDesigner polydesigner, BlockCreator blockcreator)
    {
        resolution = ScreenResolution.getScreenResolution();
        designer = polydesigner;
        creator = blockcreator;
        initComponets();
        addColumnRowListeners();
        addBoundsListener();
        initDataChangeListener();
    }

    protected void initComponets()
    {
        setBackground(Color.white);
        setOpaque(false);
        setLayout(new BlockEditorLayout());
        editComponent = createEffective();
        add("center", editComponent);
        addHeightTool = new UIButton();
        add("leftbottom", addHeightTool);
        addHeightTool.setPreferredSize(getAddHeigthPreferredSize());
        addWidthTool = new UIButton();
        add("righttop", addWidthTool);
        addWidthTool.setPreferredSize(getAddWidthPreferredSize());
        moveTool = new UIButton();
        add("bottomcorner", moveTool);
        forbiddenWindow = new BlockForbiddenWindow();
    }

    public abstract void resetSelectionAndChooseState();

    protected abstract JComponent createEffective();

    protected abstract Dimension getAddHeigthPreferredSize();

    protected abstract Dimension getAddWidthPreferredSize();

    protected abstract void initDataChangeListener();

    protected abstract RowOperationMouseHandler createRowOperationMouseHandler();

    protected abstract ColumnOperationMouseHandler createColumnOperationMouseHandler();

    protected void addColumnRowListeners()
    {
        RowOperationMouseHandler rowoperationmousehandler = createRowOperationMouseHandler();
        addHeightTool.addMouseListener(rowoperationmousehandler);
        addHeightTool.addMouseMotionListener(rowoperationmousehandler);
        addHeightTool.setCursor(Cursor.getPredefinedCursor(9));
        ColumnOperationMouseHandler columnoperationmousehandler = createColumnOperationMouseHandler();
        addWidthTool.addMouseListener(columnoperationmousehandler);
        addWidthTool.addMouseMotionListener(columnoperationmousehandler);
        addWidthTool.setCursor(Cursor.getPredefinedCursor(11));
        BottomCornerMouseHanlder bottomcornermousehanlder = new BottomCornerMouseHanlder(designer, this);
        moveTool.addMouseListener(bottomcornermousehanlder);
        moveTool.addMouseMotionListener(bottomcornermousehanlder);
        moveTool.setCursor(Cursor.getPredefinedCursor(13));
    }

    protected void addBoundsListener()
    {
        TemplateBlock templateblock = getValue();
        templateblock.addPropertyListener(new PropertyChangeAdapter() {

            final BlockEditor this$0;

            public void propertyChange()
            {
                initSize();
                LayoutUtils.layoutRootContainer(BlockEditor.this);
            }

            public boolean equals(Object obj)
            {
                return ComparatorUtils.equals(obj.getClass().getName(), getClass().getName());
            }

            
            {
                this$0 = BlockEditor.this;
                super();
            }
        }
);
    }

    public void setDragging(boolean flag)
    {
        isDragging = flag;
    }

    public boolean isDragging()
    {
        return isDragging;
    }

    public void setXAbsorptionline(Absorptionline absorptionline)
    {
        lineInX = absorptionline;
    }

    public void setYAbsorptionline(Absorptionline absorptionline)
    {
        lineInY = absorptionline;
    }

    public void showForbiddenWindow(int i, int j)
    {
        forbiddenWindow.showWindow(i, j);
    }

    public void hideForbiddenWindow()
    {
        forbiddenWindow.hideWindow();
    }

    public void paintAbsorptionline(Graphics g)
    {
        if(lineInX != null)
            lineInX.paint(g, designer);
        if(lineInY != null)
            lineInY.paint(g, designer);
    }

    protected void initSize()
    {
        Dimension dimension = getCornerSize();
        TemplateBlock templateblock = getValue();
        UnitRectangle unitrectangle = templateblock.getBounds();
        int i = unitrectangle.x.toPixI(resolution) - dimension.width - designer.getHorizontalValue();
        int j = unitrectangle.y.toPixI(resolution) - dimension.height - designer.getVerticalValue();
        int k = unitrectangle.width.toPixI(resolution) + dimension.width + 15;
        int l = unitrectangle.height.toPixI(resolution) + dimension.height + 15;
        setBounds(new Rectangle(i, j, k, l));
    }

    public TemplateBlock getValue()
    {
        return creator.getValue();
    }

    public Dimension getCornerSize()
    {
        return new Dimension();
    }
}
