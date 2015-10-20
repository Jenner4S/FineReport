package com.fr.design;

import com.fr.base.BaseUtils;
import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.env.RemoteEnv;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 13-12-24
 * Time: ����9:36
 * ��¼��������������״̬
 */
public class DesignState {

    //�˵��ļ������
    //Jwrok

    //worksheet
    public static final int WORK_SHEET = 0;
    //polyDesogner
    public static final int POLY_SHEET = 1;
    //�������
    public static final int PARAMETER_PANE = 2;


    //From
    public static final int JFORM = 4;


    //�ǲ�����Զ��
    public static final int REMOTE = 8;

    //���״̬
    private int designState = -1;
    private boolean isRoot = true;//Ĭ���ǹ���Ա��½
    private boolean isAuthority = false;

    public DesignState(ToolBarMenuDockPlus plus) {
        designState = plus.getMenuState();
        Env env = FRContext.getCurrentEnv();
        if (env != null && env instanceof RemoteEnv) {
            designState += REMOTE;
        }
        isRoot = env != null && env.isRoot();
        isAuthority = BaseUtils.isAuthorityEditing();
    }

    public int getDesignState() {
        return designState;
    }

    /**
     * �Ƿ��ǹ���Ա
     * @return �ǹ���Ա����true
     */
    public boolean isRoot() {
        return this.isRoot;
    }

    /**
     * �Ƿ���Ȩ�ޱ༭״̬
     * @return  ���򷵻�true
     */
    public boolean isAuthority() {
        return isAuthority;
    }


}
