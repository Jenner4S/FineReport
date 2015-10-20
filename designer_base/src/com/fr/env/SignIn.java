package com.fr.env;


import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.general.ComparatorUtils;
import com.fr.design.utils.DesignUtils;
import com.fr.general.GeneralContext;


public class SignIn {
    public static Env lastSelectedEnv;// ��¼����¼��Env

    /**
     * ע���뻷��
     * @param selectedEnv ѡ��Ļ���
     * @throws Exception �쳣
     */
    public static void signIn(Env selectedEnv) throws Exception {
        boolean validServer;
        signOutOldEnv(selectedEnv);
        selectedEnv.signIn();
        validServer = true;
        if (validServer) {
            DesignUtils.switchToEnv(selectedEnv);
            lastSelectedEnv = selectedEnv;
        }
    }

    private static void signOutOldEnv(Env newEnv) {
        // ������ֱͬ�ӷ��أ������˷ѹ���ʱ��
        if (lastSelectedEnv == null || ComparatorUtils.equals(lastSelectedEnv, newEnv)) {
            return;
        }
        try {
        	GeneralContext.fireEnvSignOutListener();
            lastSelectedEnv.signOut();
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
    }
}
