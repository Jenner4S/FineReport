// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.base.Parameter;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.ui.ElementCaseEditorProvider;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;
import com.fr.js.FormHyperlinkProvider;
import com.fr.stable.bridge.StableFactory;
import java.util.List;
import javax.swing.BorderFactory;

// Referenced classes of package com.fr.design.chart.series.SeriesCondition.impl:
//            FormHyperlinkNorthPane

public class FormHyperlinkPane extends BasicBeanPane
{
    public static class CHART_METER extends FormHyperlinkPane
    {

        protected int getChartParaType()
        {
            return 10;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((FormHyperlinkProvider)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((FormHyperlinkProvider)obj);
        }

        public CHART_METER()
        {
        }
    }

    public static class CHART_GANTT extends FormHyperlinkPane
    {

        protected int getChartParaType()
        {
            return 9;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((FormHyperlinkProvider)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((FormHyperlinkProvider)obj);
        }

        public CHART_GANTT()
        {
        }
    }

    public static class CHART_STOCK extends FormHyperlinkPane
    {

        protected int getChartParaType()
        {
            return 6;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((FormHyperlinkProvider)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((FormHyperlinkProvider)obj);
        }

        public CHART_STOCK()
        {
        }
    }

    public static class CHART_BUBBLE extends FormHyperlinkPane
    {

        protected int getChartParaType()
        {
            return 5;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((FormHyperlinkProvider)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((FormHyperlinkProvider)obj);
        }

        public CHART_BUBBLE()
        {
        }
    }

    public static class CHART_XY extends FormHyperlinkPane
    {

        protected int getChartParaType()
        {
            return 4;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((FormHyperlinkProvider)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((FormHyperlinkProvider)obj);
        }

        public CHART_XY()
        {
        }
    }

    public static class CHART_PIE extends FormHyperlinkPane
    {

        protected int getChartParaType()
        {
            return 3;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((FormHyperlinkProvider)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((FormHyperlinkProvider)obj);
        }

        public CHART_PIE()
        {
        }
    }

    public static class CHART_GIS extends FormHyperlinkPane
    {

        protected int getChartParaType()
        {
            return 8;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((FormHyperlinkProvider)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((FormHyperlinkProvider)obj);
        }

        public CHART_GIS()
        {
        }
    }

    public static class CHART_MAP extends FormHyperlinkPane
    {

        protected int getChartParaType()
        {
            return 2;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((FormHyperlinkProvider)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((FormHyperlinkProvider)obj);
        }

        public CHART_MAP()
        {
        }
    }

    public static class CHART extends FormHyperlinkPane
    {

        protected int getChartParaType()
        {
            return 1;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((FormHyperlinkProvider)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((FormHyperlinkProvider)obj);
        }

        public CHART()
        {
        }
    }

    public static class CHART_NO_RENAME extends FormHyperlinkPane
    {

        protected boolean needRenamePane()
        {
            return false;
        }

        protected int getChartParaType()
        {
            return 1;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((FormHyperlinkProvider)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((FormHyperlinkProvider)obj);
        }

        public CHART_NO_RENAME()
        {
        }
    }


    private ReportletParameterViewPane parameterViewPane;
    private FormHyperlinkNorthPane northPane;

    public FormHyperlinkPane()
    {
        initComponents();
    }

    protected void initComponents()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        northPane = new FormHyperlinkNorthPane(needRenamePane());
        add(northPane, "North");
        parameterViewPane = new ReportletParameterViewPane(getChartParaType());
        add(parameterViewPane, "Center");
        parameterViewPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FR-Designer_Parameters"), null));
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("Hyperlink-Form_link");
    }

    protected int getChartParaType()
    {
        return 0;
    }

    protected boolean needRenamePane()
    {
        return getChartParaType() != 0;
    }

    protected int getHyperlinkType()
    {
        return northPane.getEditingEditor() == null || !northPane.getEditingEditor().acceptType(new Class[] {
            com/fr/form/ui/ElementCaseEditorProvider
        }) ? 0 : 1;
    }

    public void populateBean(FormHyperlinkProvider formhyperlinkprovider)
    {
        northPane.populateBean(formhyperlinkprovider);
        List list = parameterViewPane.update();
        list.clear();
        com.fr.stable.ParameterProvider aparameterprovider[] = formhyperlinkprovider.getParameters();
        parameterViewPane.populate(aparameterprovider);
    }

    public FormHyperlinkProvider updateBean()
    {
        FormHyperlinkProvider formhyperlinkprovider = (FormHyperlinkProvider)StableFactory.getMarkedInstanceObjectFromClass("FormHyperlink", com/fr/js/FormHyperlinkProvider);
        formhyperlinkprovider.setType(getHyperlinkType());
        updateBean(formhyperlinkprovider);
        return formhyperlinkprovider;
    }

    public void updateBean(FormHyperlinkProvider formhyperlinkprovider)
    {
        formhyperlinkprovider.setType(getHyperlinkType());
        northPane.updateBean(formhyperlinkprovider);
        List list = parameterViewPane.update();
        if(!list.isEmpty())
        {
            Parameter aparameter[] = new Parameter[list.size()];
            list.toArray(aparameter);
            formhyperlinkprovider.setParameters(aparameter);
        }
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((FormHyperlinkProvider)obj);
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((FormHyperlinkProvider)obj);
    }
}
