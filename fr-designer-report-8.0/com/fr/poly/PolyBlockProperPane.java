// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly;

import com.fr.design.mainframe.DockingView;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import javax.swing.Icon;

// Referenced classes of package com.fr.poly:
//            PolyBlockProperTable, PolyDesigner

public class PolyBlockProperPane extends DockingView
{
    private static class HOLDER
    {

        private static PolyBlockProperPane singleton = new PolyBlockProperPane();



        private HOLDER()
        {
        }
    }


    private PolyDesigner designer;
    private PolyBlockProperTable polyBlockProperTable;

    public static PolyBlockProperPane getInstance(PolyDesigner polydesigner)
    {
        HOLDER.singleton.setEditingPolyDesigner(polydesigner);
        return HOLDER.singleton;
    }

    private PolyBlockProperPane()
    {
        polyBlockProperTable = new PolyBlockProperTable();
        setLayout(new BorderLayout());
        add(polyBlockProperTable, "Center");
    }

    private void setEditingPolyDesigner(PolyDesigner polydesigner)
    {
        designer = polydesigner;
    }

    public void refreshDockingView()
    {
        polyBlockProperTable.populate(designer);
    }

    public String getViewTitle()
    {
        return Inter.getLocText("Form-Widget_Property_Table");
    }

    public Icon getViewIcon()
    {
        return null;
    }

    public com.fr.design.mainframe.DockingView.Location preferredLocation()
    {
        return com.fr.design.mainframe.DockingView.Location.WEST_BELOW;
    }

}
