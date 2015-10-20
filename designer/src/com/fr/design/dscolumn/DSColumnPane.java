package com.fr.design.dscolumn;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.FRContext;
import com.fr.data.TableDataSource;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;

public class DSColumnPane extends BasicPane {

    private TableDataSource tplEC;
    private UITabbedPane tabbedPane;
    private DSColumnBasicPane basicPane = null;
    private DSColumnConditionsPane conditionPane = null;
    private DSColumnAdvancedPane advancedPane = null;
    private TemplateCellElement cellElement;
    protected Component lastSelectedComponent;
    
    public static final int SETTING_ALL = 2;
    public static final int SETTING_DSRELATED = 1;
    
    public DSColumnPane() {
    	this.initComponents(SETTING_ALL);
    }
    
    public DSColumnPane(int setting) {
    	this.initComponents(setting);
    }

    protected void initComponents(int setting) {
        JPanel contentPane = this;
        contentPane.setLayout(FRGUIPaneFactory.createBorderLayout());

        //peter:����Panel.
        tabbedPane = new UITabbedPane();
        tabbedPane.addChangeListener(appliedWizardTabChangeListener);

        contentPane.add(tabbedPane, BorderLayout.CENTER);

        //_denny: ���������
        basicPane = new DSColumnBasicPane(setting);
        basicPane.addPropertyChangeListener("cellElement", myPropertyChangeListener);
        tabbedPane.addTab(Inter.getLocText("Basic"), basicPane);

        conditionPane = new DSColumnConditionsPane(setting);
        tabbedPane.addTab(Inter.getLocText("Filter"), conditionPane);

        advancedPane = new DSColumnAdvancedPane(setting);
        tabbedPane.addTab(Inter.getLocText("Advanced"), advancedPane);

        this.setPreferredSize(new Dimension(610, 400));
    }
    
    @Override
    protected String title4PopupWindow() {
    	return Inter.getLocText("ExpandD-Data_Column");
    }

    /*
     * populate
     */
    public void populate(TableDataSource tds, TemplateCellElement cellElement) throws Exception {
    	this.tplEC = tds;
    	
        if (tds == null || cellElement == null) {
            // _denny: �Ҳ���Ϊ�������Ӧ�ó��֣��Է���һ
            this.cellElement = new DefaultTemplateCellElement();
            return;
        }
        // _denny: �����Ҫ��¡һ�£���Ϊ������ʱ�����ܸı��ֶ�cellElement�����ı���ʵֵ�ǲ���������
        try {
            this.cellElement = (TemplateCellElement) cellElement.clone();
        } catch (CloneNotSupportedException ce) {
        }
        this.basicPane.populate(tds, this.cellElement);
        this.conditionPane.populate(tds, this.cellElement);
        this.advancedPane.populate(this.cellElement);
    }

    /*
     * update
     */
    public CellElement update() {
        this.basicPane.update(cellElement);
        this.conditionPane.update(cellElement);
        this.advancedPane.update(cellElement);
        return cellElement;
    }
    public ChangeListener appliedWizardTabChangeListener = new ChangeListener() {

        public void stateChanged(ChangeEvent evt) {
            try {
                if (lastSelectedComponent == null) {
                    lastSelectedComponent = basicPane;
                }
                //selectTabComponent����Ҫ�л������Ǹ�Pane
                Component selectTabComponent = tabbedPane.getSelectedComponent();
                // _denny: ����л�Tabʱ��һ��Pane��basicPane, ��ˢ��һ������Pane��
                // ��Ϊѡ��������п��ܸı�, ���º�����˺�ʹ�ù�ʽ�õ���������ı�
                if (lastSelectedComponent == basicPane) {
                    basicPane.update(cellElement);

                    // denny_GUI: ˢ���������
                    refrushOtherTabs();
                }
                // �л���ǩ��ʱ��ͣ�ȷ���Ƿ���û����ӵ��б��е�����
                lastSelectedComponent = selectTabComponent;
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }

        }
    };
    // cellElement �ı�ʱ��ˢ��һ��
    // ���磺�ϱ��л�Tabʱ��basicPane Update��һ�£����ܻ�ı�Field cellElement��ֵ
    PropertyChangeListener myPropertyChangeListener = new PropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent evt) {
            refrushOtherTabs();
        }
    };

    //_denny:������tab�е����ݷ����仯��ʱ��ˢ�º����tab
    public void refrushOtherTabs() {
        // ����deny:��JTabPane�м���һ��Paneʱ�������Pane���ܻ�û�г�ʼ��
        if (conditionPane == null || advancedPane == null) {
            return;
        }
        this.conditionPane.populate(tplEC, cellElement);
        this.advancedPane.populate(cellElement);
    }
    public void putElementcase(ElementCasePane t){
    	basicPane.putElementcase(t);
    }

	public void putCellElement(TemplateCellElement tplEC2) {
		basicPane.putCellElement(tplEC2);
	}
}
