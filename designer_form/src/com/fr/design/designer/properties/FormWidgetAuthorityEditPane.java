package com.fr.design.designer.properties;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.AuthorityEditPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.toolbar.AuthorityEditToolBarComponent;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;
import com.fr.form.ui.Widget;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;

/**
 * Author : daisy
 * Date: 13-9-16
 * Time: ����10:45
 */
public class FormWidgetAuthorityEditPane extends AuthorityEditPane {
    private FormDesigner designer;
    private Widget[] widgets = null;
    private UICheckBox widgetVisible = new UICheckBox(Inter.getLocText("FR-Designer_Visible"));
    private UICheckBox widgetAvailable = new UICheckBox(Inter.getLocText("FR-Designer_Enabled"));
    private ItemListener visibleItemListener = new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
            String selectedRoles = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
            if (selectedRoles == null) {
                return;
            }
            if (widgets != null && widgets.length > 0) {
                for (int i = 0; i < widgets.length; i++) {
                    widgets[i].changeVisibleAuthorityState(selectedRoles, widgetVisible.isSelected());
                }
            }
            doAfterAuthority();
        }
    };


    private ItemListener usableItemListener = new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
            String selectedRoles = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
            if (ComparatorUtils.equals(selectedRoles, Inter.getLocText("FR-Engine_Role"))) {
                return;
            }
            if (selectedRoles == null) {
                return;
            }
            if (widgets != null && widgets.length > 0) {
                for (int i = 0; i < widgets.length; i++) {
                    widgets[i].changeUsableAuthorityState(selectedRoles, widgetAvailable.isSelected());
                }
            }
            doAfterAuthority();
        }
    };

    public FormWidgetAuthorityEditPane(FormDesigner designer) {
        super(designer);
        this.designer = designer;
        widgetAvailable.addItemListener(usableItemListener);
        widgetVisible.addItemListener(visibleItemListener);
    }


    private void doAfterAuthority() {
        designer.repaint();
        HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().fireTargetModified();
        RolesAlreadyEditedPane.getInstance().refreshDockingView();
        RolesAlreadyEditedPane.getInstance().setReportAndFSSelectedRoles();
        RolesAlreadyEditedPane.getInstance().repaint();
        checkCheckBoxes();
    }

    /**
	 * �����������
	 * 
	 *
	 * @date 2014-12-21-����6:19:43
	 * 
	 */
    public void populateType() {
        type.setText(Inter.getLocText("Widget-Form_Widget_Config"));
    }

    /**
	 * �����������
	 * 
	 *
	 * @date 2014-12-21-����7:12:27
	 * 
	 */
    public void populateName() {
        String nameText = "";
        if (widgets == null || widgets.length <= 0) {
            return;
        }
        for (int i = 0; i < widgets.length; i++) {
            nameText += "," + widgets[i].getClass().getSimpleName();
        }
        name.setText(nameText.substring(1));
    }

    /**
	 * ����checkbox���ڵ����
	 * 
	 * @return ���
	 * 
	 *
	 * @date 2014-12-21-����6:19:03
	 * 
	 */
    public JPanel populateCheckPane() {
        checkPane.add(populateWidgetCheckPane(), BorderLayout.WEST);
        return checkPane;
    }

    private JPanel populateWidgetCheckPane() {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Widget"), SwingConstants.LEFT), widgetVisible, widgetAvailable}
        };
        double[] rowSize = {p};
        double[] columnSize = {p, p, f};
        int[][] rowCount = {{1, 1, 1}};
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_MEDIUM, LayoutConstants.VGAP_MEDIUM);
    }


    /**
     * �Ե�Ԫ��������в���ʱ��Ȩ�ޱ༭ҳ��
     */
    public void populateDetials() {
        //����˵��Ҫ��JWorkBook�Ļ���˵����꽹���ָı���
        HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().setAuthorityMode(true);
        signelSelection();

        refreshCreator();
        //����ǲ���ѡ�в�֧�ֵ�Ԫ������ʾ����Ԫ�ز�֧��Ȩ�޿��ơ�
        populateType();
        populateName();
        checkPane.removeAll();
        populateCheckPane();
        checkPane.setBorder(BorderFactory.createEmptyBorder(ALIGNMENT_GAP, 0, 0, 0));
        checkCheckBoxes();
    }

    private void checkCheckBoxes() {
        String selected = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        widgetVisible.removeItemListener(visibleItemListener);
        widgetAvailable.removeItemListener(usableItemListener);
        populateWidgetButton(selected);
        widgetVisible.addItemListener(visibleItemListener);
        widgetAvailable.addItemListener(usableItemListener);
    }
    
    private void populateWidgetButton(String selected){
        if (widgets == null || widgets.length == 0) {
        	return;
        }
        
        //ѡ�ж��, ������ֻȡ��һ��
        Widget widget = widgets[0];
    	
        if(widget.isVisible()){
        	widgetVisible.setSelected(!widget.isDoneVisibleAuthority(selected));
        }else{
        	widgetVisible.setSelected(widget.isVisibleAuthority(selected));
        }
        
        if(widget.isEnabled()){
	        widgetAvailable.setSelected(!widget.isDoneUsableAuthority(selected));
        }else{
        	widgetAvailable.setSelected(widget.isUsableAuthority(selected));
        }
    }


    //ʵ�ֵ�ѡ

    private void signelSelection() {
        if (HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().isJWorkBook()) {
            //�幤����
            JComponent component = DesignerContext.getDesignerFrame().getToolbarComponent();
            if (component instanceof AuthorityEditToolBarComponent) {
                ((AuthorityEditToolBarComponent) component).removeSelection();
            }

            //��ձ�������ĵ�Ԫ��ѡ��
            HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().removeTemplateSelection();
        }
    }

    private void refreshCreator() {
        int size = designer.getSelectionModel().getSelection().size();
        widgets = size == 0 ? null : designer.getSelectionModel().getSelection().getSelectedWidgets();
    }


}
