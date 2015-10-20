package com.fr.design.actions.file;

import com.fr.base.BaseUtils;
import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.tabledata.ResponseDataSourceChange;
import com.fr.dav.LocalEnv;
import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.UpdateAction;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.TemplatePane;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.SeparatorDef;
import com.fr.design.dialog.InformationWarnPane;
import com.fr.env.RemoteEnv;
import com.fr.env.SignIn;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.GeneralContext;
import com.fr.general.Inter;
import com.fr.stable.LicUtils;
import com.fr.stable.ProductConstants;
import com.fr.stable.EnvChangedListener;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;


public class SwitchExistEnv extends MenuDef {

    public SwitchExistEnv() {
        this.setMenuKeySet(KeySetUtils.SWITCH_ENV);
        this.setName(getMenuKeySet().getMenuName());
        this.setHasScrollSubMenu(true);
        initMenuDef();
        GeneralContext.addEnvWillChangedListener(new EnvChangedListener() {
            public void envChanged() {
                SwitchExistEnv.this.clearShortCuts();
                initMenuDef();
            }
        });
    }

    private void initMenuDef() {
        //ButtonGroup group = new ButtonGroup();
        Iterator<String> nameIt = DesignerEnvManager.getEnvManager().getEnvNameIterator();
        while (nameIt.hasNext()) {
            String name = nameIt.next();
            this.setIconPath("com/fr/design/images/m_file/switch.png");
            this.addShortCut(new GetExistEnvAction(name));
        }
        this.addShortCut(SeparatorDef.DEFAULT);
        this.addShortCut(new EditEnvAction());
    }

    public static class GetExistEnvAction extends UpdateAction implements ResponseDataSourceChange {
        public GetExistEnvAction() {

        }

        public GetExistEnvAction(String envName) {
            this.setName(envName);
            Env env = DesignerEnvManager.getEnvManager().getEnv(envName);
            if (env instanceof LocalEnv) {
                this.setSmallIcon(BaseUtils.readIcon("com/fr/design/images/data/bind/localconnect.png"));
            } else if (env instanceof RemoteEnv) {
                this.setSmallIcon(BaseUtils.readIcon("com/fr/design/images/data/bind/distanceconnect.png"));
            }
        }

        /**
         * ��Ӧ���ݼ��ı�
         */
        public void fireDSChanged() {
            fireDSChanged(new HashMap<String, String>());
        }

        /**
         * ��Ӧ���ݼ��ı�
         *
         * @param map ���ݼ�
         */
        public void fireDSChanged(Map<String, String> map) {
            DesignTableDataManager.fireDSChanged(map);
        }

        /**
         * ����
         *
         * @param e �¼�
         */
        public void actionPerformed(ActionEvent e) {
            DesignerEnvManager envManager = DesignerEnvManager.getEnvManager();
            Env selectedEnv = envManager.getEnv(this.getName());
            try {
                if (selectedEnv instanceof RemoteEnv && !((RemoteEnv) selectedEnv).testServerConnection()) {
                    JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText(new String[]{"M-SwitchWorkspace", "Failed"}));
                    return;
                }
                String remoteVersion = selectedEnv.getDesignerVersion();
                if (StringUtils.isBlank(remoteVersion) || ComparatorUtils.compare(remoteVersion, ProductConstants.DESIGNER_VERSION) < 0) {
                    String infor = Inter.getLocText("Server-version-tip");
                    String moreInfo = Inter.getLocText("Server-version-tip-moreInfo");
                    FRLogger.getLogger().log(Level.WARNING, infor);
                    new InformationWarnPane(infor, moreInfo, Inter.getLocText("Tooltips")).show();
                    return;
                }
                SignIn.signIn(selectedEnv);
                LicUtils.resetBytes();
                HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().refreshToolArea();
                fireDSChanged();
            } catch (Exception em) {
                FRContext.getLogger().error(em.getMessage(), em);
                JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText(new String[]{"M-SwitchWorkspace", "Failed"}));
                TemplatePane.getInstance().editItems();
            }
        }
    }
}
