package com.fr.design.formula;

import com.fr.base.FRContext;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.data.tabledata.tabledatapane.ClassNameSelectPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.controlpane.JControlPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextarea.DescriptionTextArea;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.file.FunctionManagerProvider;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.stable.Nameable;
import com.fr.stable.project.ProjectConstants;
import com.fr.stable.script.FunctionDef;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FunctionManagerPane extends BasicPane {

    private UITextField functionTextField;
    private FunctionControlPane functionControlPane;

    public FunctionManagerPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        //path pane
        JPanel datasourcePathPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        this.add(datasourcePathPane, BorderLayout.NORTH);


        datasourcePathPane.add(new UILabel(Inter.getLocText(
            "Function-Function_File") + ":"), BorderLayout.WEST);
        this.functionTextField = new UITextField();
        datasourcePathPane.add(this.functionTextField, BorderLayout.CENTER);
        this.functionTextField.setEditable(false);

        functionControlPane = new FunctionControlPane();
        this.add(functionControlPane, BorderLayout.CENTER);
    }
    
    @Override
    protected String title4PopupWindow() {
    	return Inter.getLocText("Function-Function_Manager");
    }

    /**
     * Populate.
     */
    public void populate(FunctionManagerProvider functionManager) {
        this.functionTextField.setText(FRContext.getCurrentEnv().getPath() + File.separator
            + ProjectConstants.RESOURCES_NAME
            + File.separator + functionManager.fileName());

        List<NameObject> nameObjectList = new ArrayList<NameObject>();
        for (int i = 0; i < functionManager.getFunctionDefCount(); i++) {
            String name = functionManager.getFunctionDef(i).getName();
            nameObjectList.add(new NameObject(name, functionManager.getFunctionDef(i)));
        }
        functionControlPane.populate(nameObjectList.toArray(new NameObject[nameObjectList.size()]));

    }

    /**
     * Update.
     */
    public void update(FunctionManagerProvider functionManager) {
        // Nameable[]居然不能强转成NameObject[],一定要这么写...
        Nameable[] res = this.functionControlPane.update();
        NameObject[] res_array = new NameObject[res.length];
        java.util.Arrays.asList(res).toArray(res_array);

        functionManager.clearAllFunctionDef();

        for (int i = 0; i < res_array.length; i++) {
            FunctionDef fd = (FunctionDef) res_array[i].getObject();
            fd.setName(res_array[i].getName());
            functionManager.addFunctionDef(fd);
        }
    }

    /**
     * CellRenderer.
     */
    class FunctionControlPane extends JControlPane {

        public FunctionControlPane() {
            super();
        }

        @Override
        public NameableCreator[] createNameableCreators() {
            NameableCreator funcDef = new NameObjectCreator(Inter.getLocText("FR-Designer_Function"), FunctionDef.class,
            		FunctionContentPane.class);
            return new NameableCreator[]{funcDef};
        }
        
        @Override
        protected String title4PopupWindow() {
        	return FunctionManagerPane.this.title4PopupWindow();
        }
    }

    public static class FunctionContentPane extends BasicBeanPane<FunctionDef> {
        private FunctionDef editing;
        
        private UITextField classNameTextField;
        private UITextArea descriptionTextArea;

        public FunctionContentPane() {
            this.initComponents();
        }

        protected void initComponents() {
            this.setLayout(FRGUIPaneFactory.createBorderLayout());
            this.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
            JPanel northPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
            this.add(northPane, BorderLayout.NORTH);
            JPanel reportletNamePane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            classNameTextField = new UITextField(28);
            reportletNamePane.add(classNameTextField);
            UIButton browserButton = new UIButton(Inter.getLocText("FR-Designer_Select"));
            browserButton.setPreferredSize(new Dimension(browserButton.getPreferredSize().width,  classNameTextField.getPreferredSize().height));
            reportletNamePane.add(browserButton);
            browserButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    final ClassNameSelectPane bPane = new ClassNameSelectPane();
                    bPane.setClassPath(classNameTextField.getText());
                    bPane.showWindow( (Dialog) SwingUtilities.getWindowAncestor(FunctionContentPane.this),
                            new DialogActionAdapter() {
		                        public void doOk() {
		                            classNameTextField.setText(bPane.getClassPath());
		                        }
	                    }).setVisible(true);
                }
            });
            UIButton editorButton = new UIButton(Inter.getLocText("FR-Designer_Edit"));
            editorButton.setPreferredSize(new Dimension(editorButton.getPreferredSize().width, classNameTextField.getPreferredSize().height));
            reportletNamePane.add(editorButton);
            editorButton.addActionListener(createEditorButtonActionListener());
            northPane.add(GUICoreUtils.createFlowPane(new Component[]{new UILabel(Inter.getLocText("DS-Class_Name") + ":"),
                    reportletNamePane}, FlowLayout.LEFT));
            DescriptionTextArea descriptionArea = new DescriptionTextArea();
            descriptionArea.setWrapStyleWord(true);
            descriptionArea.setLineWrap(true);
            northPane.add(descriptionArea);
            descriptionArea.setText(Inter.getLocText(new String[]{"Function-The_class_must_inherit","Fuction-The_compiled_class_should_be_copied_to",
            		"Function-J2EE_server", "Directory", "Function-Source_Code", "Example"}, 
            		new String[]{" \"com.fr.script.AbstractFunction\".", "\n", " \"" + File.separator + "WEB-INF" + File.separator + "classes\"",
            		",", ".\n", ":" + FRContext.getCurrentEnv().getPath() + File.separator + "classes"}));
            JPanel descriptionPane = FRGUIPaneFactory.createBorderLayout_S_Pane();  //Description Pane
            this.add(descriptionPane, BorderLayout.SOUTH);
            descriptionPane.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            descriptionPane.add(new UILabel(Inter.getLocText("FR-Designer_Description") + ":"), BorderLayout.NORTH);
            this.descriptionTextArea = new UITextArea(6, 24);
            descriptionPane.add(new JScrollPane(this.descriptionTextArea), BorderLayout.CENTER);
        }
        
        private ActionListener createEditorButtonActionListener() {
        	return new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    final JavaEditorPane javaEditorPane = new JavaEditorPane(classNameTextField.getText(), JavaEditorPane.DEFAULT_FUNCTION_STRING);
                    final BasicDialog dlg = javaEditorPane.showMediumWindow(SwingUtilities.getWindowAncestor(FunctionContentPane.this),
                            new DialogActionAdapter() {
                                public void doOk() {
                                    classNameTextField.setText(javaEditorPane.getClassText());
                                }
                            });
                    javaEditorPane.addSaveActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            dlg.doOK();
                        }
                    });
                    dlg.setVisible(true);
                } 
        	};
        }
        
        @Override
        protected String title4PopupWindow() {
        	return "Function";
        }

        public String getReportletPath() {
            return this.classNameTextField.getText();
        }

        @Override
        public void populateBean(FunctionDef ob) {
        	editing = ob;
        	
            this.classNameTextField.setText(ob.getClassName());
            this.descriptionTextArea.setText(ob.getDescription());
        }

        @Override
        public FunctionDef updateBean() {
        	editing.setClassName(this.classNameTextField.getText());
            editing.setDescription(this.descriptionTextArea.getText());
            
            return editing;
        }
    }
}