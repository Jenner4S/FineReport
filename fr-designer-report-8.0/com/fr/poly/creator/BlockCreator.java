// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly.creator;

import com.fr.base.ScreenResolution;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.menu.*;
import com.fr.design.selection.SelectableElement;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.general.ComparatorUtils;
import com.fr.poly.PolyDesigner;
import com.fr.report.poly.TemplateBlock;
import com.fr.stable.core.PropertyChangeAdapter;
import com.fr.stable.unit.*;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.Serializable;
import javax.swing.JComponent;

// Referenced classes of package com.fr.poly.creator:
//            BlockEditor, PolyElementCasePane

public abstract class BlockCreator extends JComponent
    implements Serializable, SelectableElement
{

    protected PolyDesigner designer;
    protected TemplateBlock block;
    protected int resolution;

    public BlockCreator()
    {
        resolution = ScreenResolution.getScreenResolution();
    }

    public BlockCreator(TemplateBlock templateblock)
    {
        resolution = ScreenResolution.getScreenResolution();
        cal(templateblock);
    }

    protected void cal(TemplateBlock templateblock)
    {
        if(block != templateblock)
        {
            block = templateblock;
            templateblock.addPropertyListener(new PropertyChangeAdapter() {

                final BlockCreator this$0;

                public void propertyChange()
                {
                    calculateMonitorSize();
                }

                public boolean equals(Object obj)
                {
                    return ComparatorUtils.equals(obj.getClass().getName(), getClass().getName());
                }

            
            {
                this$0 = BlockCreator.this;
                super();
            }
            }
);
            removeAll();
            setLayout(FRGUIPaneFactory.createBorderLayout());
            add(initMonitor(), "Center");
        }
        calculateMonitorSize();
    }

    protected void calculateMonitorSize()
    {
        UnitRectangle unitrectangle = block.getBounds();
        if(unitrectangle == null)
        {
            unitrectangle = getDefaultBlockBounds();
            block.setBounds(unitrectangle);
        }
        setBounds(unitrectangle.x.toPixI(resolution), unitrectangle.y.toPixI(resolution), unitrectangle.width.toPixI(resolution) + UNITConstants.DELTA.toPixI(resolution), unitrectangle.height.toPixI(resolution) + UNITConstants.DELTA.toPixI(resolution));
        LayoutUtils.layoutContainer(this);
    }

    public abstract UnitRectangle getDefaultBlockBounds();

    protected abstract JComponent initMonitor();

    public abstract PolyElementCasePane getEditingElementCasePane();

    public abstract BlockEditor getEditor();

    public abstract void checkButtonEnable();

    public PolyDesigner getDesigner()
    {
        return designer;
    }

    public void setDesigner(PolyDesigner polydesigner)
    {
        designer = polydesigner;
    }

    public Rectangle getEditorBounds()
    {
        Rectangle rectangle = getBounds();
        Dimension dimension = getEditor().getCornerSize();
        rectangle.x -= dimension.width + designer.getHorizontalValue();
        rectangle.y -= dimension.height + designer.getVerticalValue();
        rectangle.width += dimension.width + 15;
        rectangle.height += dimension.height + 15;
        return rectangle;
    }

    public TemplateBlock getValue()
    {
        return block;
    }

    public abstract void setValue(TemplateBlock templateblock);

    public abstract ToolBarDef[] toolbars4Target();

    public abstract JComponent[] toolBarButton4Form();

    public abstract MenuDef[] menus4Target();

    public abstract int getMenuState();

    public abstract ShortCut[] shortcut4TemplateMenu();
}
