// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.axis;

import com.fr.chart.chartattr.ChartAlertValue;
import com.fr.design.gui.controlpane.*;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import java.util.*;

// Referenced classes of package com.fr.design.chart.axis:
//            ChartAlertValuePane

public class ChartAlertLinePane extends JControlPane
{

    public ChartAlertLinePane()
    {
    }

    public NameableCreator[] createNameableCreators()
    {
        return (new NameableCreator[] {
            new NameObjectCreator(Inter.getLocText("ChartF-Alert-Line"), com/fr/chart/chartattr/ChartAlertValue, com/fr/design/chart/axis/ChartAlertValuePane)
        });
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText(new String[] {
            "Edit", "ChartF-Alert-Line"
        });
    }

    public void populate(ChartAlertValue achartalertvalue[])
    {
        if(achartalertvalue == null)
            achartalertvalue = new ChartAlertValue[0];
        ArrayList arraylist = new ArrayList();
        for(int i = 0; i < achartalertvalue.length; i++)
        {
            ChartAlertValue chartalertvalue = achartalertvalue[i];
            arraylist.add(new NameObject(chartalertvalue.getAlertPaneSelectName(), chartalertvalue));
        }

        if(!arraylist.isEmpty())
            populate((com.fr.stable.Nameable[])arraylist.toArray(new NameObject[arraylist.size()]));
    }

    public ChartAlertValue[] updateAlertValues()
    {
        com.fr.stable.Nameable anameable[] = update();
        NameObject anameobject[] = new NameObject[anameable.length];
        Arrays.asList(anameable).toArray(anameobject);
        if(anameobject.length < 1)
            return new ChartAlertValue[0];
        ArrayList arraylist = new ArrayList();
        for(int i = 0; i < anameobject.length; i++)
        {
            NameObject nameobject = anameobject[i];
            ChartAlertValue chartalertvalue = (ChartAlertValue)nameobject.getObject();
            chartalertvalue.setAlertPaneSelectName(nameobject.getName());
            arraylist.add(chartalertvalue);
        }

        return (ChartAlertValue[])(ChartAlertValue[])arraylist.toArray(new ChartAlertValue[arraylist.size()]);
    }
}
