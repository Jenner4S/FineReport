package com.fr.design.data.datapane.connect;

import com.fr.base.FRContext;
import com.fr.design.gui.frpane.LoadingBasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.file.DatasourceManagerProvider;
import com.fr.general.Inter;
import com.fr.stable.project.ProjectConstants;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;

public class ConnectionManagerPane extends LoadingBasicPane {
    private UITextField connectionTextField;
    private ConnectionListPane connectionListPane;

    protected void initComponents(JPanel container) {
        container.setLayout(FRGUIPaneFactory.createBorderLayout());

        JPanel connectionPathPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        container.add(connectionPathPane, BorderLayout.NORTH);

        connectionPathPane.setBorder(BorderFactory.createEmptyBorder(6, 2, 2, 2));

        connectionPathPane.add(new UILabel(Inter.getLocText("FR-Designer_Save_Path") + ":"), BorderLayout.WEST);
        this.connectionTextField = new UITextField();
        connectionPathPane.add(connectionTextField, BorderLayout.CENTER);
        this.connectionTextField.setEditable(false);
        connectionListPane = new ConnectionListPane(){
            protected void rename(String oldName,String newName) {
                super.rename(oldName,newName);
                renameConnection(oldName,newName);
            }
        };
        container.add(connectionListPane, BorderLayout.CENTER);
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("Server-Define_Data_Connection");
    }

    public HashMap<String, String> getRenameMap() {
        return connectionListPane.getRenameMap();
    }

    public void populate(DatasourceManagerProvider datasourceManager) {
        this.connectionTextField.setText(FRContext.getCurrentEnv().getPath() + File.separator + ProjectConstants.RESOURCES_NAME
                + File.separator + datasourceManager.fileName());
        this.connectionListPane.populate(datasourceManager);
    }

    public void update(DatasourceManagerProvider datasourceManager) {
        this.connectionListPane.update(datasourceManager);
    }

    /**
     * ����ѡ����
     *
     * @param index ѡ��������к�
     */
    public void setSelectedIndex(int index) {
        this.connectionListPane.setSelectedIndex(index);
    }

    /**
     * �����Ƿ�����
     * @return ������true
     */
    public boolean isNamePermitted() {
        return connectionListPane.isNamePermitted();
    }

}
