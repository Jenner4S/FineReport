/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.actions.help;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.actions.UpdateAction;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-3-18
 * Time: ����9:21
 */
public class ForumAction extends UpdateAction {
    public ForumAction() {
        this.setMenuKeySet(FORUM);
        this.setName(getMenuKeySet().getMenuName());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_help/product_forum.png"));
    }


    /**
     * ����
     * @param e �¼�
     */
    public void actionPerformed(ActionEvent e) {
        String url = "http://bbs.finereport.com/";
        if (StringUtils.isEmpty(url)) {
            FRContext.getLogger().info("The URL is empty!");
            return;
        }
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException exp) {
            JOptionPane.showMessageDialog(null, Inter.getLocText("Set_default_browser"));
            FRContext.getLogger().errorWithServerLevel(exp.getMessage(), exp);
        } catch (URISyntaxException exp) {
            FRContext.getLogger().errorWithServerLevel(exp.getMessage(), exp);
        } catch (Exception exp) {
            FRContext.getLogger().errorWithServerLevel(exp.getMessage(), exp);
            FRContext.getLogger().error("Can not open the browser for URL:  " + url);
        }
    }

    public static final MenuKeySet FORUM = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 0;
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("Forum");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };
}
