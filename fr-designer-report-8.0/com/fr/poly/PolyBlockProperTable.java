// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly;

import com.fr.design.gui.itable.AbstractPropertyTable;
import com.fr.design.gui.itable.PropertyGroup;
import com.fr.poly.group.PolyBoundsGroup;
import com.fr.poly.group.PolyNameGroup;
import com.fr.report.poly.PolyWorkSheet;
import com.fr.report.poly.TemplateBlock;
import java.util.ArrayList;

// Referenced classes of package com.fr.poly:
//            PolyDesigner

public class PolyBlockProperTable extends AbstractPropertyTable
{

    private PolyDesigner designer;

    public PolyBlockProperTable()
    {
    }

    public void initPropertyGroups(Object obj)
    {
        groups = new ArrayList();
        if(obj instanceof TemplateBlock)
        {
            TemplateBlock templateblock = (TemplateBlock)obj;
            PolyNameGroup polynamegroup = new PolyNameGroup(templateblock);
            groups.add(new PropertyGroup(polynamegroup));
            PolyBoundsGroup polyboundsgroup = new PolyBoundsGroup(templateblock, (PolyWorkSheet)designer.getTarget());
            groups.add(new PropertyGroup(polyboundsgroup));
        }
        com.fr.design.gui.itable.AbstractPropertyTable.BeanTableModel beantablemodel = new com.fr.design.gui.itable.AbstractPropertyTable.BeanTableModel(this);
        setModel(beantablemodel);
        repaint();
    }

    public void firePropertyEdit()
    {
        designer.fireTargetModified();
    }

    public void populate(PolyDesigner polydesigner)
    {
        designer = polydesigner;
        initPropertyGroups(designer.getEditingTarget());
    }
}
