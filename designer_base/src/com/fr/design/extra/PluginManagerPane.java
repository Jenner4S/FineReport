package com.fr.design.extra;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.general.Inter;

import java.awt.*;

/**
 * @author richie
 * @date 2015-03-09
 * @since 8.0
 */
public class PluginManagerPane extends BasicPane {


    public PluginManagerPane() {
        setLayout(new BorderLayout());
        UITabbedPane tabbedPane = new UITabbedPane();
        add(tabbedPane, BorderLayout.CENTER);
        PluginInstalledPane installedPane = new PluginInstalledPane();
        tabbedPane.addTab(installedPane.tabTitle(), installedPane);
        tabbedPane.addTab(Inter.getLocText("FR-Designer-Plugin_Update"), new PluginUpdatePane(tabbedPane));
        tabbedPane.addTab(Inter.getLocText("FR-Designer-Plugin_All_Plugins"), new PluginFromStorePane(tabbedPane));
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Designer-Plugin_Manager");
    }
}