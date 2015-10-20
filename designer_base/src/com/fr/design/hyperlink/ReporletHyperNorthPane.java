package com.fr.design.hyperlink;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import com.fr.design.actions.UpdateAction;
import com.fr.design.gui.itree.filetree.ReportletPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.general.Inter;
import com.fr.js.ReportletHyperlink;
import com.fr.stable.StringUtils;
import com.fr.design.utils.gui.GUICoreUtils;

/**
 * �ȵ����Ӳ��� �Ϸ� �������� ��ʽ ���� �����ԵĽ���.
 *
 * @author kunsnat
 */
public class ReporletHyperNorthPane extends AbstractHyperlinkPane<ReportletHyperlink> {
    private UITextField itemNameTextField;
    private boolean needRenamePane = false;
    private UITextField reportPathTextField;
    private UICheckBox showParameterInterface;
    private UIButton browserButton;

    // richer:�������ݷ�ʽ
    private UIComboBox postComboBox;

    public ReporletHyperNorthPane(boolean needRenamePane) {
        this.needRenamePane = needRenamePane;
        this.inits();
    }

    public ReporletHyperNorthPane() {
        this.inits();
    }

    /**
     * ��ʼ�����
     */
    public void inits() {
        super.initComponents();
    }

    @Override
    protected JPanel setHeaderPanel() {
        JPanel headerPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p};
        double[] columnSize = {p, TableLayout.FILL};
        // Reportlet.
        JPanel reportletNamePane = FRGUIPaneFactory.createBorderLayout_S_Pane();

        reportPathTextField = new UITextField(20);
        reportletNamePane.add(reportPathTextField, BorderLayout.CENTER);

        browserButton = new UIButton(Inter.getLocText("Select"));
        browserButton.setPreferredSize(new Dimension(browserButton.getPreferredSize().width, 20));
        reportletNamePane.add(browserButton, BorderLayout.EAST);
        browserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                final ReportletPane reportletPane = new ReportletPane();
                reportletPane.setSelectedReportletPath(reportPathTextField.getText());
                BasicDialog reportletDialog = reportletPane.showWindow(SwingUtilities.getWindowAncestor(ReporletHyperNorthPane.this));

                reportletDialog.addDialogActionListener(new DialogActionAdapter() {
                    public void doOk() {
                        reportPathTextField.setText(reportletPane.getSelectedReportletPath());
                    }
                });
                reportletDialog.setVisible(true);
            }
        });

        Component[][] components;
        if(!this.needRenamePane){
            components = new Component[][]{
                    {new UILabel(" " + Inter.getLocText("Reportlet") + ":"), reportletNamePane},
            };
        }else{
            itemNameTextField = new UITextField();
            components = new Component[][]{
                    {new UILabel(" " + Inter.getLocText("Name") + ":"), itemNameTextField},
                    {new UILabel(" " + Inter.getLocText("Reportlet") + ":"), reportletNamePane},
            };
        }
        JPanel northPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        headerPane.add(northPane, BorderLayout.NORTH);
        return headerPane;
    }

    @Override
    protected String title4PopupWindow() {
        return "reportlet";
    }

    @Override
    protected void populateSubHyperlinkBean(ReportletHyperlink link) {
        if(itemNameTextField != null){
            this.itemNameTextField.setText(link.getItemName());
        }
        this.reportPathTextField.setText(link.getReportletPath());
        this.showParameterInterface.setSelected(link.isShowParameterInterface());
        this.postComboBox.setSelectedIndex(link.isByPost() ? 1 : 0);
    }

    @Override
    protected ReportletHyperlink updateSubHyperlinkBean() {
        ReportletHyperlink reportletHyperlink = new ReportletHyperlink();
        updateSubHyperlinkBean(reportletHyperlink);

        return reportletHyperlink;
    }

    @Override
    protected void updateSubHyperlinkBean(ReportletHyperlink reportletHyperlink) {
        if(itemNameTextField != null){
            reportletHyperlink.setItemName(this.itemNameTextField.getText());
        }
        reportletHyperlink.setReportletPath(this.reportPathTextField.getText());
        reportletHyperlink.setShowParameterInterface(this.showParameterInterface.isSelected());
        reportletHyperlink.setByPost(postComboBox.getSelectedIndex() == 1 ? true : false);
    }

    public String getReportletName() {
        return StringUtils.isBlank(this.reportPathTextField.getText()) ? "" : this.reportPathTextField.getText().substring(1);
    }

    /**
     * ��ȡ��ť����
     */
    public void requestButtonFocus() {
        this.browserButton.requestFocus();
        JPopupMenu popup = new JPopupMenu();
        FakeTipAction tip = new FakeTipAction();
        tip.setEnabled(false);
        popup.add(tip);
        GUICoreUtils.showPopupCloseMenu(popup, this.browserButton);
    }

    private class FakeTipAction extends UpdateAction {
        public FakeTipAction() {
            this.setName(Inter.getLocText(new String[]{"Choose", "Reportlet"}));
        }

        public void actionPerformed(ActionEvent e) {

        }
    }

    @Override
    protected JPanel setFootPanel() {
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p};
        double[] columnSize = {p, TableLayout.FILL};
        showParameterInterface = new UICheckBox(Inter.getLocText(new String[]{"Display", "Reportlet", "ParameterD-Parameter_Interface"}));
        JPanel showParameterPanel = new JPanel();
        showParameterPanel.add(new UILabel());
        showParameterPanel.add(showParameterInterface);

        postComboBox = new UIComboBox(new String[]{"GET", "POST"});
        JPanel postPanel = new JPanel();
        postPanel.add(new UILabel(Inter.getLocText("Reportlet-Parameter_Type")));
        postPanel.add(postComboBox);
        Component[][] components = {{postPanel},
                {showParameterPanel},
                {new UILabel(" ")}
        };

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

}
