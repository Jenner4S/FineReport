// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.write.submit;

import com.fr.data.SubmitJob;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.fun.SubmitProvider;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.controlpane.ObjectJControlPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.scrollruler.ModLineBorder;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.report.write.SubmitVisitor;
import com.fr.stable.ArrayUtils;
import com.fr.stable.bridge.StableFactory;
import com.fr.write.BuiltInSQLSubmiterProvider;
import com.fr.write.DBManipulation;
import com.fr.write.ReportWriteAttrProvider;
import com.fr.write.WClassSubmiterProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.write.submit:
//            DefaultSubmit, SmartInsertDBManipulationPane, DBManipulationPane

public class SubmiterListPane extends ObjectJControlPane
{
    public static class CustomPane extends BasicBeanPane
    {

        private UIComboBox csjConfigComboBox;
        private JPanel customCardPane;
        private Map customSubmitPanes;
        private final Map comboItemsMap = new HashMap();
        private List configTypes;
        private WClassSubmiterProvider editing;
        private static final String DEFAULT_PANE_TYPE = "submitnormal";

        public void populateBean(WClassSubmiterProvider wclasssubmiterprovider)
        {
            editing = wclasssubmiterprovider;
            SubmitJob submitjob = wclasssubmiterprovider.getSubmitJob();
            if(submitjob == null)
            {
                csjConfigComboBox.setSelectedItem(comboItemsMap.get("submitnormal"));
                java.util.Map.Entry entry;
                for(Iterator iterator = customSubmitPanes.entrySet().iterator(); iterator.hasNext(); ((BasicBeanPane)entry.getValue()).populateBean(submitjob))
                    entry = (java.util.Map.Entry)iterator.next();

                return;
            }
            String s = submitjob.getJobType();
            BasicBeanPane basicbeanpane = (BasicBeanPane)customSubmitPanes.get(s);
            if(basicbeanpane != null)
            {
                csjConfigComboBox.setSelectedItem(comboItemsMap.get(s));
                basicbeanpane.populateBean(submitjob);
            }
        }

        public WClassSubmiterProvider updateBean()
        {
            Iterator iterator = customSubmitPanes.entrySet().iterator();
            do
            {
                if(!iterator.hasNext())
                    break;
                java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
                BasicBeanPane basicbeanpane = (BasicBeanPane)entry.getValue();
                if(basicbeanpane != null && basicbeanpane.isVisible())
                    editing.setSubmitJob((SubmitJob)basicbeanpane.updateBean());
            } while(true);
            return editing;
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
            populateBean((WClassSubmiterProvider)obj);
        }




        public CustomPane()
        {
            csjConfigComboBox = null;
            customCardPane = null;
            customSubmitPanes = null;
            configTypes = null;
            setLayout(FRGUIPaneFactory.createBorderLayout());
            customCardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
            customSubmitPanes = new HashMap();
            SubmitProvider asubmitprovider[] = ExtraDesignClassManager.getInstance().getSubmitProviders();
            asubmitprovider = (SubmitProvider[])(SubmitProvider[])ArrayUtils.add(asubmitprovider, new DefaultSubmit());
            SubmitProvider asubmitprovider1[] = asubmitprovider;
            int i = asubmitprovider1.length;
            for(int j = 0; j < i; j++)
            {
                SubmitProvider submitprovider = asubmitprovider1[j];
                customSubmitPanes.put(submitprovider.keyForSubmit(), submitprovider.appearanceForSubmit());
                comboItemsMap.put(submitprovider.keyForSubmit(), submitprovider.dataForSubmit());
            }

            configTypes = new ArrayList();
            java.util.Map.Entry entry;
            String s;
            for(Iterator iterator = customSubmitPanes.entrySet().iterator(); iterator.hasNext(); customCardPane.add((java.awt.Component)entry.getValue(), s))
            {
                entry = (java.util.Map.Entry)iterator.next();
                s = (String)entry.getKey();
                configTypes.add(comboItemsMap.get(s));
            }

            csjConfigComboBox = new UIComboBox(configTypes.toArray());
            JPanel jpanel = GUICoreUtils.createFlowPane(new java.awt.Component[] {
                new UILabel((new StringBuilder()).append(Inter.getLocText(new String[] {
                    "Choose", "Type"
                })).append(":").toString()), csjConfigComboBox
            }, 0, 10);
            jpanel.setBorder(BorderFactory.createTitledBorder(new ModLineBorder(1), Inter.getLocText(new String[] {
                "Submit", "Type"
            })));
            add(jpanel, "North");
            add(customCardPane, "Center");
            csjConfigComboBox.addItemListener(new java.awt.event.ItemListener() {

                final CustomPane this$0;

                public void itemStateChanged(java.awt.event.ItemEvent itemevent)
                {
                    if(itemevent.getStateChange() == 1)
                    {
                        Object obj = itemevent.getItem();
                        java.awt.CardLayout cardlayout = (java.awt.CardLayout)customCardPane.getLayout();
                        Iterator iterator1 = customSubmitPanes.keySet().iterator();
                        do
                        {
                            if(!iterator1.hasNext())
                                break;
                            String s1 = (String)iterator1.next();
                            String s2 = (String)comboItemsMap.get(s1);
                            if(ComparatorUtils.equals(obj, s2))
                                cardlayout.show(customCardPane, s1);
                        } while(true);
                    }
                }

                
                {
                    this$0 = CustomPane.this;
                    super();
                }
            }
);
        }
    }

    public static class BuildInSQLPane extends BasicBeanPane
    {

        protected DBManipulationPane dbManipulationPane;
        private BuiltInSQLSubmiterProvider editing;

        protected String title4PopupWindow()
        {
            return "builtinsql";
        }

        public void populateBean(BuiltInSQLSubmiterProvider builtinsqlsubmiterprovider)
        {
            editing = builtinsqlsubmiterprovider;
            DBManipulation dbmanipulation = builtinsqlsubmiterprovider.getDBManipulation();
            dbManipulationPane.populateBean(dbmanipulation);
        }

        public BuiltInSQLSubmiterProvider updateBean()
        {
            DBManipulation dbmanipulation = dbManipulationPane.updateBean();
            try
            {
                editing = (BuiltInSQLSubmiterProvider)editing.clone();
            }
            catch(Exception exception)
            {
                FRLogger.getLogger().error(exception.getMessage());
            }
            editing.setDBManipulation(dbmanipulation);
            return editing;
        }

        public void checkValid()
            throws Exception
        {
            dbManipulationPane.checkValid();
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((BuiltInSQLSubmiterProvider)obj);
        }

        public BuildInSQLPane()
        {
        }

        public BuildInSQLPane(ElementCasePane elementcasepane)
        {
            setLayout(FRGUIPaneFactory.createBorderLayout());
            dbManipulationPane = new SmartInsertDBManipulationPane(elementcasepane);
            add(dbManipulationPane, "Center");
        }
    }


    public SubmiterListPane(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
    }

    public NameableCreator[] createNameableCreators()
    {
        return (new NameableCreator[] {
            new NameObjectCreator(Inter.getLocText("RWA-BuildIn_SQL"), "/com/fr/web/images/reportlet.png", StableFactory.getRegisteredClass("BuiltInSQLSubmiter"), com/fr/design/write/submit/SubmiterListPane$BuildInSQLPane), new NameObjectCreator(Inter.getLocText(new String[] {
                "Custom", "RWA-Submit"
            }), "/com/fr/web/images/reportlet.png", StableFactory.getRegisteredClass("WClassSubmiter"), com/fr/design/write/submit/SubmiterListPane$CustomPane)
        });
    }

    protected String title4PopupWindow()
    {
        return "write";
    }

    public void populate(ReportWriteAttrProvider reportwriteattrprovider)
    {
        if(reportwriteattrprovider == null)
            return;
        ArrayList arraylist = new ArrayList();
        int i = reportwriteattrprovider.getSubmitVisitorCount();
        for(int j = 0; j < i; j++)
        {
            SubmitVisitor submitvisitor = reportwriteattrprovider.getSubmitVisitor(j);
            String s = reportwriteattrprovider.getSubmitVisitorNameList(j);
            arraylist.add(new NameObject(s, submitvisitor));
        }

        populate((com.fr.stable.Nameable[])arraylist.toArray(new NameObject[arraylist.size()]));
    }

    public void updateReportWriteAttr(ReportWriteAttrProvider reportwriteattrprovider)
    {
        com.fr.stable.Nameable anameable[] = update();
        NameObject anameobject[] = new NameObject[anameable.length];
        Arrays.asList(anameable).toArray(anameobject);
        reportwriteattrprovider.clearSubmitVisitors();
        for(int i = 0; i < anameobject.length; i++)
        {
            NameObject nameobject = anameobject[i];
            if(nameobject.getObject() instanceof SubmitVisitor)
                reportwriteattrprovider.addSubmitVisitor(nameobject.getName(), (SubmitVisitor)nameobject.getObject());
        }

    }
}
