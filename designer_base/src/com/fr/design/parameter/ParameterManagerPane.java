package com.fr.design.parameter;

import com.fr.base.FRContext;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.base.ConfigManagerProvider;
import com.fr.stable.project.ProjectConstants;

import javax.swing.*;
import java.awt.*;
import java.io.File;


public class ParameterManagerPane extends BasicPane {
    private UITextField parameterTextField;
    private ParameterArrayPane parameterArrayPane;

    public ParameterManagerPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        JPanel parameterPathPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        this.add(parameterPathPane, BorderLayout.NORTH);


        parameterPathPane.add(new UILabel(Inter.getLocText("FR-Designer_Save_Path") + ":"), BorderLayout.WEST);
        this.parameterTextField = new UITextField();
        parameterPathPane.add(parameterTextField, BorderLayout.CENTER);
        this.parameterTextField.setEditable(false);

        parameterArrayPane = new ParameterArrayPane();
        this.add(parameterArrayPane, BorderLayout.CENTER);
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("M_Server-Global_Parameters");
    }

    public void populate(ConfigManagerProvider configManager) {
        this.parameterTextField.setText(FRContext.getCurrentEnv().getPath() + File.separator +
                ProjectConstants.RESOURCES_NAME +
                File.separator + configManager.fileName());
        this.parameterArrayPane.populate(configManager.getGlobal_Parameters());
    }

    public void update(ConfigManagerProvider configManager) {
        configManager.setGlobal_Parameters(parameterArrayPane.updateParameters());
    }

    /**
     * 是否重命名
     *
     * @return 是则返回true
     */
    public boolean isContainsRename() {
        return parameterArrayPane.isContainsRename() || parameterArrayPane.isNameRepeated();
    }
}