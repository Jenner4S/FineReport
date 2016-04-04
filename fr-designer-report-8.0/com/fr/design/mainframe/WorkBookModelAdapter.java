// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.Parameter;
import com.fr.base.TableData;
import com.fr.data.TableDataSource;
import com.fr.design.DesignModelAdapter;
import com.fr.design.bridge.DesignToolbarProvider;
import com.fr.form.ui.Widget;
import com.fr.main.impl.WorkBook;
import com.fr.main.parameter.ReportParameterAttr;
import com.fr.report.cell.*;
import com.fr.report.elementcase.ElementCase;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.report.Report;
import com.fr.script.Calculator;
import com.fr.stable.StringUtils;
import com.fr.stable.bridge.StableFactory;
import com.fr.stable.js.WidgetName;
import java.util.*;

// Referenced classes of package com.fr.design.mainframe:
//            JWorkBook

public class WorkBookModelAdapter extends DesignModelAdapter
{

    public WorkBookModelAdapter(JWorkBook jworkbook)
    {
        super(jworkbook);
    }

    public Parameter[] getParameters()
    {
        return ((WorkBook)getBook()).getParameters();
    }

    public Parameter[] getReportParameters()
    {
        ReportParameterAttr reportparameterattr = ((WorkBook)getBook()).getReportParameterAttr();
        return reportparameterattr != null ? reportparameterattr.getParameters() : new Parameter[0];
    }

    public Parameter[] getTableDataParameters()
    {
        com.fr.base.io.IOFile iofile = getBook();
        Calculator calculator = Calculator.createCalculator();
        calculator.setAttribute(com/fr/data/TableDataSource, iofile);
        ArrayList arraylist = new ArrayList();
        Iterator iterator = ((WorkBook)getBook()).getTableDataNameIterator();
        do
        {
            if(!iterator.hasNext())
                break;
            TableData tabledata = iofile.getTableData((String)iterator.next());
            if(tabledata.getParameters(calculator) != null)
                arraylist.addAll(Arrays.asList(tabledata.getParameters(calculator)));
        } while(true);
        return (Parameter[])arraylist.toArray(new Parameter[arraylist.size()]);
    }

    public boolean renameTableData(String s, String s1)
    {
        if(super.renameTableData(s, s1))
        {
            if(((WorkBook)getBook()).getTableData(s) == null)
                ((JWorkBook)jTemplate).refreshParameterPane4TableData(s, s1);
            return true;
        } else
        {
            return false;
        }
    }

    public void envChanged()
    {
        DesignToolbarProvider designtoolbarprovider = (DesignToolbarProvider)StableFactory.getMarkedObject("DesignToolbarProvider", com/fr/design/bridge/DesignToolbarProvider);
        if(designtoolbarprovider != null)
            designtoolbarprovider.refreshToolbar();
        ((JWorkBook)jTemplate).refreshAllNameWidgets();
    }

    public void parameterChanged()
    {
        ((JWorkBook)jTemplate).updateReportParameterAttr();
        ((JWorkBook)jTemplate).populateReportParameterAttr();
    }

    public void widgetConfigChanged()
    {
        DesignToolbarProvider designtoolbarprovider = (DesignToolbarProvider)StableFactory.getMarkedObject("DesignToolbarProvider", com/fr/design/bridge/DesignToolbarProvider);
        if(designtoolbarprovider != null)
            designtoolbarprovider.refreshToolbar();
        ((JWorkBook)jTemplate).refreshAllNameWidgets();
    }

    public List getWidgetsName()
    {
        ArrayList arraylist = new ArrayList();
        WorkBook workbook = (WorkBook)getBook();
        int i = 0;
        int j = workbook.getReportCount();
        do
        {
            if(i >= j)
                break;
            Report report = workbook.getReport(i);
            for(Iterator iterator = report.iteratorOfElementCase(); iterator.hasNext();)
            {
                ElementCase elementcase = (ElementCase)iterator.next();
                Iterator iterator1 = elementcase.cellIterator();
                while(iterator1.hasNext()) 
                {
                    CellElement cellelement = (CellElement)iterator1.next();
                    if(cellelement instanceof DefaultTemplateCellElement)
                    {
                        Widget widget = ((DefaultTemplateCellElement)cellelement).getWidget();
                        if(widget != null && StringUtils.isNotEmpty(widget.getWidgetName()))
                            arraylist.add(new WidgetName(widget.getWidgetName()));
                    }
                }
            }

            i++;
        } while(true);
        return arraylist;
    }

    public String[] getFloatNames()
    {
        TemplateElementCase templateelementcase = ((JWorkBook)jTemplate).getEditingElementCase();
        ArrayList arraylist = new ArrayList();
        for(Iterator iterator = templateelementcase.floatIterator(); iterator.hasNext(); arraylist.add(((FloatElement)iterator.next()).getName()));
        return (String[])arraylist.toArray(new String[arraylist.size()]);
    }

    public Widget[] getLinkableWidgets()
    {
        return new Widget[0];
    }
}
