// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly.creator;

import com.fr.design.designer.TargetComponent;
import com.fr.design.gui.chart.MiddleChartComponent;
import com.fr.design.menu.*;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.selection.QuickEditor;
import com.fr.quickeditor.ChartQuickEditor;
import com.fr.report.poly.PolyChartBlock;
import com.fr.report.poly.TemplateBlock;
import com.fr.stable.unit.*;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

// Referenced classes of package com.fr.poly.creator:
//            BlockCreator, ChartBlockEditor, BlockEditor, PolyElementCasePane

public class ChartBlockCreator extends BlockCreator
{

    private MiddleChartComponent cpm;
    private ChartBlockEditor editor;
    private static final UNIT DEFAULT_WIDTH = FU.getInstance(0xbfd948L);
    private static final UNIT DEFAULT_HEIGHT = FU.getInstance(0x8b86c0L);

    public ChartBlockCreator()
    {
    }

    public ChartBlockCreator(PolyChartBlock polychartblock)
    {
        super(polychartblock);
    }

    public JComponent initMonitor()
    {
        cpm = DesignModuleFactory.getChartComponent(getValue().getChartCollection());
        cpm.setBorder(BorderFactory.createLineBorder(Color.lightGray));
        return cpm;
    }

    public UnitRectangle getDefaultBlockBounds()
    {
        return new UnitRectangle(UNIT.ZERO, UNIT.ZERO, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public BlockEditor getEditor()
    {
        if(editor == null)
            editor = new ChartBlockEditor(designer, this);
        return editor;
    }

    public void checkButtonEnable()
    {
        if(editor == null)
            editor = new ChartBlockEditor(designer, this);
        editor.checkChartButtonsEnable();
    }

    public PolyChartBlock getValue()
    {
        return (PolyChartBlock)block;
    }

    public void setValue(PolyChartBlock polychartblock)
    {
        block = polychartblock;
        cpm.populate(((PolyChartBlock)block).getChartCollection());
    }

    public ToolBarDef[] toolbars4Target()
    {
        return new ToolBarDef[0];
    }

    public JComponent[] toolBarButton4Form()
    {
        return new JComponent[0];
    }

    public MenuDef[] menus4Target()
    {
        return new MenuDef[0];
    }

    public int getMenuState()
    {
        return 1;
    }

    public ShortCut[] shortcut4TemplateMenu()
    {
        return new ShortCut[0];
    }

    public PolyElementCasePane getEditingElementCasePane()
    {
        return null;
    }

    public QuickEditor getQuickEditor(TargetComponent targetcomponent)
    {
        ChartQuickEditor chartquickeditor = ChartQuickEditor.getInstance();
        chartquickeditor.populate(targetcomponent);
        return chartquickeditor;
    }

    public volatile void setValue(TemplateBlock templateblock)
    {
        setValue((PolyChartBlock)templateblock);
    }

    public volatile TemplateBlock getValue()
    {
        return getValue();
    }

}
