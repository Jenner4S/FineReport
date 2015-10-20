package com.fr.design.mainframe.form;

import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.BaseJForm;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.form.FormElementCaseContainerProvider;
import com.fr.form.FormElementCaseProvider;
import com.fr.report.worksheet.FormElementCase;

import javax.swing.*;
import java.awt.*;

/**
 * ����FormElementCase�༭���� �������������м��grid���߾ۺϿ顢�����sheetTab
 */
public class FormReportComponentComposite extends JComponent implements TargetModifiedListener, FormECCompositeProvider{
	
	private FormElementCaseDesigner elementCaseDesigner;
    private BaseJForm jForm;

    private FormTabPane sheetNameTab;
    private JPanel hbarContainer;



    public FormReportComponentComposite(BaseJForm jform, FormElementCaseDesigner elementCaseDesign, FormElementCaseContainerProvider ecContainer) {
        this.jForm = jform;
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.elementCaseDesigner = elementCaseDesign;
        this.add(elementCaseDesigner, BorderLayout.CENTER);
        sheetNameTab = new FormTabPane(ecContainer, jform);
        this.add(createSouthControlPane(), BorderLayout.SOUTH);
        
        elementCaseDesigner.addTargetModifiedListener(this);
    }

    private java.util.List<TargetModifiedListener> targetModifiedList = new java.util.ArrayList<TargetModifiedListener>();

    /**
     *  ���Ŀ��ı�ļ���
     * @param targetModifiedListener     Ŀ��ı��¼�
     */
    public void addTargetModifiedListener(TargetModifiedListener targetModifiedListener) {
    	targetModifiedList.add(targetModifiedListener);
    }

    /**
     * Ŀ��ı�
     * @param e      �¼�
     */
	public void targetModified(TargetModifiedEvent e) {
        for (TargetModifiedListener l : targetModifiedList) {
            l.targetModified(e);
        }
	}
    public void setEditingElementCase(FormElementCase formElementCase){
        elementCaseDesigner.setTarget(formElementCase);
        fireTargetModified();
    }

    private JComponent createSouthControlPane() {
        hbarContainer = FRGUIPaneFactory.createBorderLayout_S_Pane();
        hbarContainer.add(elementCaseDesigner.getHorizontalScrollBar());
        JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sheetNameTab, hbarContainer);
        splitpane.setBorder(null);
        splitpane.setDividerSize(3);
        splitpane.setResizeWeight(0.6);
        return splitpane;
    }

    /**
     * ֹͣ�༭
     */
    public void stopEditing() {
    	elementCaseDesigner.stopEditing();
    }

    public void setComposite() {
        DesignerContext.getDesignerFrame().resetToolkitByPlus((ToolBarMenuDockPlus) jForm);
        this.validate();
        this.repaint(40);
    }
    
	public void setSelectedWidget(FormElementCaseProvider fc) {
        if (fc != null){
            elementCaseDesigner.setTarget(fc);
        }
	}

    /**
     * ģ�����
     */
    public void fireTargetModified() {
        jForm.fireTargetModified();
    }


}
