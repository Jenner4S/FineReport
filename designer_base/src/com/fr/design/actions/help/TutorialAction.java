package com.fr.design.actions.help;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Locale;

import javax.swing.KeyStroke;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.Utils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.general.http.HttpClient;
import com.fr.stable.OperatingSystem;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;

public class TutorialAction extends UpdateAction {
	
    public TutorialAction() {
        this.setMenuKeySet(HELP_TUTORIAL);
        this.setName(getMenuKeySet().getMenuName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_help/help.png"));
        this.setAccelerator(getMenuKeySet().getKeyStroke());
    }

    private void nativeExcuteMacInstallHomePrograms(String appName) {
        String installHome = StableUtils.getInstallHome();
        if(installHome == null) {
            FRContext.getLogger().error("Can not find the install home, please check it.");
        } else {
            String appPath = StableUtils.pathJoin(new String[]{installHome, "bin", appName});
            if(!(new File(appPath)).exists()) {
                FRContext.getLogger().error(appPath + " can not be found.");
            }

            String cmd = "open " + appPath;
            Runtime runtime = Runtime.getRuntime();

            try {
                runtime.exec(cmd);
            } catch (IOException e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }

        }
    }
    /**
     * ����
     * @param evt �¼�
     */
    public void actionPerformed(ActionEvent evt) {
        Locale locale = FRContext.getLocale();
        if (ComparatorUtils.equals(locale, Locale.CHINA) || ComparatorUtils.equals(locale, Locale.TAIWAN)){
            HttpClient client = new HttpClient(ProductConstants.HELP_URL);
            if(client.getResponseCode() != -1) {
                try {
                    Desktop.getDesktop().browse(new URI(ProductConstants.HELP_URL));
                    return;
                } catch (Exception e) {
                    //�����쳣�Ļ�, ��Ȼ�򿪱��ؽ̳�
                }
            }
        }
        if (OperatingSystem.isMacOS()) {
            nativeExcuteMacInstallHomePrograms("helptutorial.app");
        }
        else {
            Utils.nativeExcuteInstallHomePrograms("helptutorial.exe");
        }
    }

    public static final MenuKeySet HELP_TUTORIAL = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'T';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_Help-Tutorial");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);
        }
    };

}