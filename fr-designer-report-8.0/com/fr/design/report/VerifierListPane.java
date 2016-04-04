// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report;

import com.fr.data.Verifier;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.controlpane.*;
import com.fr.design.gui.ilist.JNameEdList;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.stable.Nameable;
import com.fr.stable.bridge.StableFactory;
import com.fr.write.*;
import java.util.*;
import javax.swing.DefaultListModel;

// Referenced classes of package com.fr.design.report:
//            CustomVerifyJobPane, ValueVerifierEditPane

public class VerifierListPane extends ObjectJControlPane
{
    public static class CustomVerifierPane extends BasicBeanPane
    {

        private CustomVerifyJobPane pane;

        public void populateBean(WClassVerifierProvider wclassverifierprovider)
        {
            pane.populateBean(wclassverifierprovider.getClassVerifyJob());
        }

        public WClassVerifierProvider updateBean()
        {
            WClassVerifierProvider wclassverifierprovider = (WClassVerifierProvider)StableFactory.getMarkedInstanceObjectFromClass("WClassVerifier", com/fr/write/WClassVerifierProvider);
            wclassverifierprovider.setClassVerifyJob(pane.updateBean());
            return wclassverifierprovider;
        }

        protected String title4PopupWindow()
        {
            return "custom";
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((WClassVerifierProvider)obj);
        }

        public CustomVerifierPane()
        {
            setLayout(FRGUIPaneFactory.createBorderLayout());
            pane = new CustomVerifyJobPane();
            add(pane, "Center");
        }
    }

    public static class BuildInVerifierPane extends BasicBeanPane
    {

        private ValueVerifierEditPane valueVerifierEditPane;

        public void populateBean(ReportWriteAttrProvider reportwriteattrprovider)
        {
            valueVerifierEditPane.populate(reportwriteattrprovider);
        }

        public ReportWriteAttrProvider updateBean()
        {
            ReportWriteAttrProvider reportwriteattrprovider = (ReportWriteAttrProvider)StableFactory.getMarkedInstanceObjectFromClass("ReportWriteAttr", com/fr/write/ReportWriteAttrProvider);
            valueVerifierEditPane.update(reportwriteattrprovider, VerifierListPane.valueVerifyName);
            return reportwriteattrprovider;
        }

        protected String title4PopupWindow()
        {
            return null;
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ReportWriteAttrProvider)obj);
        }

        public BuildInVerifierPane()
        {
            setLayout(FRGUIPaneFactory.createBorderLayout());
            valueVerifierEditPane = new ValueVerifierEditPane();
            add(valueVerifierEditPane, "Center");
        }
    }


    private static String valueVerifyName = Inter.getLocText("Verify-Data_Verify");

    public VerifierListPane(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
    }

    public NameableCreator[] createNameableCreators()
    {
        return (new NameableCreator[] {
            new NameObjectCreator(Inter.getLocText(new String[] {
                "BuildIn", "Verify"
            }), "/com/fr/web/images/reportlet.png", StableFactory.getRegisteredClass("ReportWriteAttr"), com/fr/design/report/VerifierListPane$BuildInVerifierPane), new NameObjectCreator(Inter.getLocText(new String[] {
                "Custom", "Verify"
            }), "/com/fr/web/images/reportlet.png", StableFactory.getRegisteredClass("WClassVerifier"), com/fr/design/report/VerifierListPane$CustomVerifierPane)
        });
    }

    protected String title4PopupWindow()
    {
        return null;
    }

    public void populate(ReportWriteAttrProvider reportwriteattrprovider)
    {
        if(reportwriteattrprovider == null)
            return;
        ArrayList arraylist = new ArrayList();
        int i = reportwriteattrprovider.getVerifierCount();
        boolean flag = false;
        for(int j = 0; j < i; j++)
        {
            Verifier verifier = reportwriteattrprovider.getVerifier(j);
            String s = reportwriteattrprovider.getVerifierNameList(j);
            if(verifier instanceof ValueVerifierProvider)
            {
                if(!flag)
                {
                    arraylist.add(new NameObject(s, reportwriteattrprovider));
                    flag = true;
                }
            } else
            {
                arraylist.add(new NameObject(s, verifier));
            }
        }

        populate((Nameable[])arraylist.toArray(new NameObject[arraylist.size()]));
    }

    public void updateReportWriteAttr(ReportWriteAttrProvider reportwriteattrprovider)
    {
        JNameEdList jnameedlist = nameableList;
        DefaultListModel defaultlistmodel = (DefaultListModel)jnameedlist.getModel();
        int i = 0;
        do
        {
            if(i >= defaultlistmodel.size())
                break;
            NameObject nameobject = (NameObject)((ListModelElement)defaultlistmodel.get(i)).wrapper;
            if(nameobject.getObject() instanceof ReportWriteAttrProvider)
            {
                valueVerifyName = nameobject.getName();
                break;
            }
            i++;
        } while(true);
        Nameable anameable[] = update();
        NameObject anameobject[] = new NameObject[anameable.length];
        Arrays.asList(anameable).toArray(anameobject);
        reportwriteattrprovider.clearVerifiers();
        for(int j = 0; j < anameobject.length; j++)
        {
            NameObject nameobject1 = anameobject[j];
            if(nameobject1.getObject() instanceof Verifier)
            {
                reportwriteattrprovider.addVerifier(nameobject1.getName(), (Verifier)nameobject1.getObject());
                continue;
            }
            if(!(nameobject1.getObject() instanceof ReportWriteAttrProvider))
                continue;
            ReportWriteAttrProvider reportwriteattrprovider1 = (ReportWriteAttrProvider)nameobject1.getObject();
            for(int k = 0; k < reportwriteattrprovider1.getValueVerifierCount(); k++)
                reportwriteattrprovider.addVerifier(nameobject1.getName(), reportwriteattrprovider1.getVerifier(k));

        }

    }

    public void addNameable(Nameable nameable, int i)
    {
        JNameEdList jnameedlist = nameableList;
        DefaultListModel defaultlistmodel = (DefaultListModel)jnameedlist.getModel();
        if(((NameObject)nameable).getObject() instanceof ReportWriteAttrProvider)
        {
            setToolbarDefEnable(0, 0, false);
            for(int j = 0; j < defaultlistmodel.size(); j++)
                if(isBuildInVerifier(((NameObject)((ListModelElement)defaultlistmodel.get(j)).wrapper).getObject()))
                    nameableList.setSelectedIndex(j);

        }
        ListModelElement listmodelelement = new ListModelElement(nameable);
        defaultlistmodel.add(i, listmodelelement);
        nameableList.setSelectedIndex(i);
        nameableList.ensureIndexIsVisible(i);
        jnameedlist.repaint();
    }

    public void checkButtonEnabled()
    {
        super.checkButtonEnabled();
        if(!hasBuildInVerifier())
            setToolbarDefEnable(0, 0, true);
    }

    private boolean hasBuildInVerifier()
    {
        JNameEdList jnameedlist = nameableList;
        DefaultListModel defaultlistmodel = (DefaultListModel)jnameedlist.getModel();
        for(int i = 0; i < defaultlistmodel.size(); i++)
            if(isBuildInVerifier(((NameObject)((ListModelElement)defaultlistmodel.get(i)).wrapper).getObject()))
                return true;

        return false;
    }

    private boolean isBuildInVerifier(Object obj)
    {
        return obj instanceof ReportWriteAttrProvider;
    }


}
