// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report.share;

import com.fr.data.TableDataSource;
import com.fr.data.impl.EmbeddedTableData;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.controlpane.*;
import com.fr.design.mainframe.JTemplate;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import java.util.*;

// Referenced classes of package com.fr.design.report.share:
//            ConfusionInfo, ConfusionTableDataPane

public class ConfusionManagerPane extends JControlPane
{

    public ConfusionManagerPane()
    {
    }

    public NameableCreator[] createNameableCreators()
    {
        NameObjectCreator nameobjectcreator = new NameObjectCreator(Inter.getLocText("FR-Engine_DS-TableData"), "/com/fr/design/images/data/dock/serverdatatable.png", com/fr/design/report/share/ConfusionInfo, com/fr/design/report/share/ConfusionTableDataPane);
        return (new NameableCreator[] {
            nameobjectcreator
        });
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("FR-Designer_Data-confusion");
    }

    public boolean populateTabledataManager()
    {
        List list = initNameObjectList();
        if(list.isEmpty())
        {
            return false;
        } else
        {
            populate((com.fr.stable.Nameable[])list.toArray(new NameObject[list.size()]));
            return true;
        }
    }

    private List initNameObjectList()
    {
        JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        TableDataSource tabledatasource = (TableDataSource)jtemplate.getTarget();
        Iterator iterator = tabledatasource.getTableDataNameIterator();
        ArrayList arraylist = new ArrayList();
        String s;
        ConfusionInfo confusioninfo;
        for(; iterator.hasNext(); arraylist.add(new NameObject(s, confusioninfo)))
        {
            s = (String)iterator.next();
            EmbeddedTableData embeddedtabledata = (EmbeddedTableData)tabledatasource.getTableData(s);
            confusioninfo = new ConfusionInfo(embeddedtabledata, s);
        }

        return arraylist;
    }

    protected ShortCut4JControlPane[] createShortcuts()
    {
        return (new ShortCut4JControlPane[] {
            moveUpItemShortCut(), moveDownItemShortCut(), sortItemShortCut()
        });
    }
}
