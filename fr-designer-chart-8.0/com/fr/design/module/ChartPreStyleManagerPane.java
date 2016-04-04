// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.module;

import com.fr.base.*;
import com.fr.chart.base.ChartPreStyle;
import com.fr.design.gui.controlpane.*;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.stable.Nameable;
import java.util.ArrayList;
import java.util.Iterator;

// Referenced classes of package com.fr.design.module:
//            ChartPreStylePane

public class ChartPreStyleManagerPane extends JControlPane
{

    public ChartPreStyleManagerPane()
    {
    }

    public NameableCreator[] createNameableCreators()
    {
        return (new NameableCreator[] {
            new NameObjectCreator(Inter.getLocText("FR-Designer_PreStyle"), com/fr/chart/base/ChartPreStyle, com/fr/design/module/ChartPreStylePane)
        });
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("FR-Designer_Chart-PreStyle");
    }

    public void populateBean()
    {
        ChartPreStyleManagerProvider chartprestylemanagerprovider = ChartPreStyleServerManager.getProviderInstance();
        ArrayList arraylist = new ArrayList();
        Object obj;
        ChartPreStyle chartprestyle;
        for(Iterator iterator = chartprestylemanagerprovider.names(); iterator.hasNext(); arraylist.add(new NameObject(Utils.objectToString(obj), chartprestyle)))
        {
            obj = iterator.next();
            chartprestyle = (ChartPreStyle)chartprestylemanagerprovider.getPreStyle(obj);
        }

        Nameable anameable[] = (Nameable[])(Nameable[])arraylist.toArray(new Nameable[arraylist.size()]);
        populate(anameable);
        if(chartprestylemanagerprovider.containsName(chartprestylemanagerprovider.getCurrentStyle()))
            setSelectedName(chartprestylemanagerprovider.getCurrentStyle());
    }

    public void updateBean()
    {
        ChartPreStyleManagerProvider chartprestylemanagerprovider = ChartPreStyleServerManager.getProviderInstance();
        chartprestylemanagerprovider.clearPreStyles();
        Nameable anameable[] = update();
        chartprestylemanagerprovider.setCurrentStyle(getSelectedName());
        for(int i = 0; i < anameable.length; i++)
        {
            Nameable nameable = anameable[i];
            chartprestylemanagerprovider.putPreStyle(nameable.getName(), ((NameObject)nameable).getObject());
        }

        chartprestylemanagerprovider.writerPreChartStyle();
        DesignerFrame designerframe = DesignerContext.getDesignerFrame();
        if(designerframe != null)
            designerframe.repaint();
    }
}
