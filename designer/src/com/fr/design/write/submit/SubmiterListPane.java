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
import com.fr.report.write.Submiter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Nameable;
import com.fr.stable.bridge.StableFactory;
import com.fr.write.BuiltInSQLSubmiterProvider;
import com.fr.write.DBManipulation;
import com.fr.write.ReportWriteAttrProvider;
import com.fr.write.WClassSubmiterProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubmiterListPane extends ObjectJControlPane {

    public SubmiterListPane(ElementCasePane ePane) {
        super(ePane);
    }

    /**
     * ����ѡ��
     *
     * @return ѡ��
     */
    public NameableCreator[] createNameableCreators() {
        return new NameableCreator[]{
                new NameObjectCreator(Inter.getLocText("RWA-BuildIn_SQL"),
                        "/com/fr/web/images/reportlet.png",
//				BuiltInSQLSubmiter.class,
                        StableFactory.getRegisteredClass(BuiltInSQLSubmiterProvider.TAG),
                        BuildInSQLPane.class),
                new NameObjectCreator(
                        Inter.getLocText(new String[]{"Custom", "RWA-Submit"}),
                        "/com/fr/web/images/reportlet.png",
//				WClassSubmiter.class,
                        StableFactory.getRegisteredClass(WClassSubmiterProvider.TAG),
                        CustomPane.class)
        };
    }

    @Override
    protected String title4PopupWindow() {
        return "write";
    }

    public void populate(ReportWriteAttrProvider reportWriteAttr) {
        if (reportWriteAttr == null) {
            return;
        }

        List<NameObject> nameObjectList = new ArrayList<NameObject>();

        int submiterCount = reportWriteAttr.getSubmiterCount();
        for (int i = 0; i < submiterCount; i++) {
            Submiter submiter = reportWriteAttr.getSubmiter(i);
            String name = reportWriteAttr.getSubmiterNameList(i);
            nameObjectList.add(new NameObject(name, submiter));
        }

        this.populate(nameObjectList.toArray(new NameObject[nameObjectList.size()]));
    }

    /**
     * ���������
     *
     * @param reportWriteAttr ���������
     */
    public void updateReportWriteAttr(ReportWriteAttrProvider reportWriteAttr) {
        // Nameable[]��Ȼ����ǿת��NameObject[],һ��Ҫ��ôд...
        Nameable[] res = this.update();
        NameObject[] res_array = new NameObject[res.length];
        java.util.Arrays.asList(res).toArray(res_array);

        reportWriteAttr.clearSubmiters();

        for (int i = 0; i < res_array.length; i++) {
            NameObject nameObject = res_array[i];
            if (nameObject.getObject() instanceof Submiter) {
                reportWriteAttr.addSubmiter(nameObject.getName(), (Submiter) nameObject.getObject());
            }
        }
    }

    public static class BuildInSQLPane extends BasicBeanPane<BuiltInSQLSubmiterProvider> {
        protected DBManipulationPane dbManipulationPane;
        private BuiltInSQLSubmiterProvider editing;

        public BuildInSQLPane() {

        }

        public BuildInSQLPane(ElementCasePane ePane) {
            this.setLayout(FRGUIPaneFactory.createBorderLayout());
            dbManipulationPane = new SmartInsertDBManipulationPane(ePane);
            this.add(dbManipulationPane, BorderLayout.CENTER);
        }

        @Override
        protected String title4PopupWindow() {
            return "builtinsql";
        }

        @Override
        public void populateBean(BuiltInSQLSubmiterProvider ob) {
            editing = ob;

            DBManipulation dbManipulation = ob.getDBManipulation();
            dbManipulationPane.populateBean(dbManipulation);
        }

        @Override
        public BuiltInSQLSubmiterProvider updateBean() {
            DBManipulation dbManipulation = dbManipulationPane.updateBean();

            //���ƶ������ڳ���
            try{
                editing = (BuiltInSQLSubmiterProvider)editing.clone();
            }catch (Exception e){
                FRLogger.getLogger().error(e.getMessage());
            }
            editing.setDBManipulation(dbManipulation);

            return editing;
        }

        /**
         * ����Ƿ���Ϲ淶
         *
         * @throws Exception
         */
        public void checkValid() throws Exception {
            this.dbManipulationPane.checkValid();
        }
    }

    public static class CustomPane extends BasicBeanPane<WClassSubmiterProvider> {
        private UIComboBox csjConfigComboBox = null;
        private JPanel customCardPane = null;
        private Map<String, BasicBeanPane> customSubmitPanes = null;
        private final Map<String, String> comboItemsMap;

        private List<String> configTypes = null;

        private WClassSubmiterProvider editing;

        private static final String DEFAULT_PANE_TYPE = "submitnormal";

        public CustomPane() {
            this.setLayout(FRGUIPaneFactory.createBorderLayout());
            customCardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
            customSubmitPanes = new HashMap<String, BasicBeanPane>();
            comboItemsMap = new HashMap<String, String>();

            SubmitProvider[] providers = ExtraDesignClassManager.getInstance().getSubmitProviders();
            providers = (SubmitProvider[])ArrayUtils.add(providers, new DefaultSubmit());
            for (SubmitProvider provider : providers) {
                customSubmitPanes.put(provider.keyForSubmit(), provider.appearanceForSubmit());
                comboItemsMap.put(provider.keyForSubmit(), provider.dataForSubmit());
            }

            configTypes = new ArrayList<String>();
            for (Map.Entry<String, BasicBeanPane> entry : customSubmitPanes.entrySet()) {
                String key = entry.getKey();
                configTypes.add(comboItemsMap.get(key));
                customCardPane.add(entry.getValue(), key);
            }
            csjConfigComboBox = new UIComboBox(configTypes.toArray());

            JPanel typePane = GUICoreUtils.createFlowPane(new Component[]{new UILabel(Inter.getLocText(new String[]{"Choose", "Type"}) + ":"), csjConfigComboBox},
                    FlowLayout.LEFT, 10);
            typePane.setBorder(BorderFactory.createTitledBorder(new ModLineBorder(ModLineBorder.TOP), Inter.getLocText(new String[]{"Submit", "Type"})));
            this.add(typePane, BorderLayout.NORTH);

            this.add(customCardPane, BorderLayout.CENTER);

            csjConfigComboBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        Object ob = e.getItem();
                        CardLayout c1 = (CardLayout) customCardPane.getLayout();
                        for (String key : customSubmitPanes.keySet()) {
                            String item = comboItemsMap.get(key);
                            if ((ComparatorUtils.equals(ob, item))) {
                                c1.show(customCardPane, key);
                            }
                        }
                    }
                }
            });
        }

        @Override
        public void populateBean(WClassSubmiterProvider ob) {
            editing = ob;
            SubmitJob submitJob = ob.getSubmitJob();
            if (submitJob == null) {
                csjConfigComboBox.setSelectedItem(comboItemsMap.get(DEFAULT_PANE_TYPE));

                for (Map.Entry<String, BasicBeanPane> entry : customSubmitPanes.entrySet()) {
                    entry.getValue().populateBean(submitJob);
                }
                return;
            }
            String pantype=submitJob.getJobType();
            BasicBeanPane pane = customSubmitPanes.get(pantype);
            if (pane != null) {
                csjConfigComboBox.setSelectedItem(comboItemsMap.get(pantype));
                pane.populateBean(submitJob);
            }


        }

        @Override
        public WClassSubmiterProvider updateBean() {
            for (Map.Entry<String, BasicBeanPane> entry : customSubmitPanes.entrySet()) {
                BasicBeanPane pane = entry.getValue();
                if (pane != null && pane.isVisible()) {
                    editing.setSubmitJob((SubmitJob) pane.updateBean());
                }
            }
            return editing;
        }

        @Override
        protected String title4PopupWindow() {
            return "custom";
        }
    }
}
