package com.fr.design.mainframe.actions;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;
import com.fr.start.StartServer;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * ͼ��������ò�Ʒ��ʾ
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-27
 * Time: ����9:01
 */
public class ChartWebAction  extends UpdateAction {
    public ChartWebAction() {
        this.setMenuKeySet(getSelfMenuKeySet());
        this.setName(getMenuKeySet().getMenuName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_help/demo.png"));
    }

    private MenuKeySet getSelfMenuKeySet(){
        return new MenuKeySet() {
            @Override
            public char getMnemonic() {
                return 'D';
            }

            @Override
            public String getMenuName() {
                return Inter.getLocText("FR-Chart-Product_Demo");
            }

            @Override
            public KeyStroke getKeyStroke() {
                return null;
            }
        };
    }


    /**
     * ����
     * @param e �¼�
     */
    public void actionPerformed(ActionEvent e) {
        StartServer.browerURLWithLocalEnv("http://www.vancharts.com/demo.html");
        return;
    }
}
