// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.design.DesignModelAdapter;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.form.main.Form;
import com.fr.form.main.WidgetGatherAdapter;
import com.fr.form.ui.*;
import com.fr.stable.js.WidgetName;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe:
//            BaseJForm, JForm, WidgetToolBarPane

public class FormModelAdapter extends DesignModelAdapter
{

    public FormModelAdapter(BaseJForm basejform)
    {
        super(basejform);
    }

    public void envChanged()
    {
        WidgetToolBarPane.refresh();
        ((BaseJForm)jTemplate).refreshAllNameWidgets();
    }

    public void parameterChanged()
    {
        ((BaseJForm)jTemplate).populateParameter();
    }

    public void widgetConfigChanged()
    {
        WidgetToolBarPane.refresh();
        ((BaseJForm)jTemplate).refreshAllNameWidgets();
    }

    public boolean renameTableData(String s, String s1)
    {
        if(super.renameTableData(s, s1))
        {
            ((BaseJForm)jTemplate).refreshSelectedWidget();
            return true;
        } else
        {
            return false;
        }
    }

    public List getWidgetsName()
    {
        final ArrayList list = new ArrayList();
        Form.traversalFormWidget(((Form)getBook()).getContainer(), new WidgetGatherAdapter() {

            final List val$list;
            final FormModelAdapter this$0;

            public void dealWith(Widget widget)
            {
                if((widget instanceof DataControl) || (widget instanceof MultiFileEditor))
                    list.add(new WidgetName(widget.getWidgetName()));
            }

            
            {
                this$0 = FormModelAdapter.this;
                list = list1;
                super();
            }
        }
);
        return list;
    }

    public Widget[] getLinkableWidgets()
    {
        final ArrayList linkAbleList = new ArrayList();
        JForm jform = (JForm)HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        Form.traversalWidget(jform.getRootLayout(), new WidgetGatherAdapter() {

            final ArrayList val$linkAbleList;
            final FormModelAdapter this$0;

            public boolean dealWithAllCards()
            {
                return true;
            }

            public void dealWith(Widget widget)
            {
                boolean flag = widget.acceptType(new Class[] {
                    com/fr/form/ui/ElementCaseEditor
                }) || widget.acceptType(new Class[] {
                    com/fr/form/ui/ChartEditorProvider
                });
                if(flag)
                    linkAbleList.add(widget);
            }

            
            {
                this$0 = FormModelAdapter.this;
                linkAbleList = arraylist;
                super();
            }
        }
, com/fr/form/ui/Widget);
        return (Widget[])linkAbleList.toArray(new Widget[linkAbleList.size()]);
    }
}
