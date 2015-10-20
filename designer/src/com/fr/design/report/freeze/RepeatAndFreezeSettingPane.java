package com.fr.design.report.freeze;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.page.ReportPageAttrProvider;
import com.fr.stable.ColumnRow;
import com.fr.stable.FT;
import com.fr.stable.bridge.StableFactory;

/**
 * Sets Report Page Attributes
 */
public class RepeatAndFreezeSettingPane extends BasicPane {
	// �ظ�������
	private RepeatRowPane repeatTitleRowPane;
	// �ظ�������
	private RepeatColPane repeatTitleColPane;
	// �ظ���β��
	private RepeatRowPane repeatFinisRowPane;
	// �ظ���β��
	private RepeatColPane repeatFinisColPane;
	// ��ҳ������
	private FreezePagePane freezePageRowPane;
	// ��ҳ������
	private FreezePagePane freezePageColPane;
	// �������
	private FreezeWriteRowPane freezeWriteRowPane;
	// �������
	private FreezeWriteColPane freezeWriteColPane;
	// �ظ�����
	private UICheckBox useRepeatTitleRCheckBox;
	private UICheckBox useRepeatTitleCCheckBox;
	// �ظ���β
	private UICheckBox useRepeatFinisRCheckBox;
	private UICheckBox useRepeatFinisCCheckBox;

	// ��ҳ����
	private UICheckBox usePageFrozenCCheckBox;
	private UICheckBox usePageFrozenRCheckBox;
	// �����
	private UICheckBox useWriteFrozenCCheckBox;
	private UICheckBox useWriteFrozenRCheckBox;

	/**
	 * �ظ�������
	 */
	private JPanel initRowStartPane(){
		JPanel soverlapRowStartPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		useRepeatTitleRCheckBox = new UICheckBox();
		soverlapRowStartPane.add(useRepeatTitleRCheckBox);
		soverlapRowStartPane.add(new UILabel(Inter.getLocText(new String[]{"PageSetup-Title_Start_Row", "From"})));
		repeatTitleRowPane = new RepeatRowPane();
		soverlapRowStartPane.add(repeatTitleRowPane);
		
		return soverlapRowStartPane;
	}
	
	/**
	 * �ظ�������
	 */
	private JPanel initColStartPane(){
		// �ظ���ӡ�������ʼ��
		JPanel soverlapColStartPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		useRepeatTitleCCheckBox = new UICheckBox();
		soverlapColStartPane.add(useRepeatTitleCCheckBox);
		soverlapColStartPane.add(new UILabel(Inter.getLocText(new String[]{"PageSetup-Title_Start_Column", "From"})));
		// �ظ���ӡ����Ľ�����
		repeatTitleColPane = new RepeatColPane();
		soverlapColStartPane.add(repeatTitleColPane);
		
		return soverlapColStartPane;
	}
	
	/**
	 * �ظ���β��
	 */
	private JPanel initFootRowStarPane(){
		// �ظ���ӡ��β����ʼ��
		JPanel foverlapRowStartPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		useRepeatFinisRCheckBox = new UICheckBox();
		foverlapRowStartPane.add(useRepeatFinisRCheckBox);
		foverlapRowStartPane.add(new UILabel(Inter.getLocText(new String[]{"PageSetup-Finis_Start_Row", "From"})));
		repeatFinisRowPane = new RepeatRowPane();
		foverlapRowStartPane.add(repeatFinisRowPane);
		
		return foverlapRowStartPane;
	}
	
	/**
	 * �ظ���β��
	 */
	private JPanel initFootColStartPane(){
		// �ظ���ӡ��β����ʼ��
		JPanel foverlapColStartPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		useRepeatFinisCCheckBox = new UICheckBox();
		foverlapColStartPane.add(useRepeatFinisCCheckBox);
		foverlapColStartPane.add(new UILabel(Inter.getLocText(new String[]{"PageSetup-Finis_Start_Column", "From"})));
		repeatFinisColPane = new RepeatColPane();
		foverlapColStartPane.add(repeatFinisColPane);
		
		return foverlapColStartPane;
	}
	
	/**
	 * ��ȡ��ҳ����ı���(���в���Ҫд��ҳ����)
	 * 
	 * @return ��ҳ����ı���
	 * 
	 *
	 * @date 2014-11-14-����1:32:08
	 * 
	 */
	protected String getPageFrozenTitle(){
		return Inter.getLocText("FR-Engine_Page-Frozen");
	}
	
	/**
	 * ��ҳ����Pane
	 */
	private JPanel initPageFrozenPane(){
		JPanel pagePanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
		// ��ҳ����
		UILabel pageLabel = new UILabel(getPageFrozenTitle());
		JPanel pageLabelPanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		pageLabelPanel.add(pageLabel);
		pagePanel.add(pageLabelPanel, BorderLayout.NORTH);
		JPanel pagecon = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
		pagecon.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
		pagePanel.add(pagecon, BorderLayout.CENTER);
		UILabel warningx = new UILabel("(" + Inter.getLocText("FR-Engine_Please_Set_Repeat_First") + ")");
		warningx.setForeground(Color.red);
		pageLabelPanel.add(warningx);
		
		JPanel pageRowGridPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		pageRowGridPane.add(this.usePageFrozenRCheckBox = new UICheckBox());
		pagecon.add(pageRowGridPane);
		// ��ʾ�ж�����Ϣ��panel
		freezePageRowPane = new FreezePagePane(true);
		pageRowGridPane.add(freezePageRowPane);
		addPageFrozenCol(pagecon);
		
		return pagePanel;
	}
	
	/**
	 * ��ҳ������
	 */
	private void addPageFrozenCol(JPanel pagecon){
		// ��ʾ�ж�����Ϣ��panel
		freezePageColPane = new FreezePagePane(false);
		JPanel pageColGridPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		pageColGridPane.add(this.usePageFrozenCCheckBox = new UICheckBox());
		pagecon.add(pageColGridPane);
		pageColGridPane.add(freezePageColPane);
	}
	
	/**
	 * �����Pane
	 */
	private JPanel initWriteFrozenPane(){
		JPanel writePanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
		// �����
		UILabel writeLabel = new UILabel(Inter.getLocText(new String[]{"Face_Write", "Frozen"}) + ":");
		JPanel writeLabelPanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		writeLabelPanel.add(writeLabel);
		writePanel.add(writeLabelPanel, BorderLayout.NORTH);
		JPanel writecon = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
		writecon.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
		writePanel.add(writecon, BorderLayout.CENTER);

		// �ж���
		JPanel writeRowPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		writeRowPane.add(this.useWriteFrozenRCheckBox = new UICheckBox());
		// �ж���
		JPanel writeColPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		writeColPane.add(this.useWriteFrozenCCheckBox = new UICheckBox());
		writecon.add(writeRowPane);
		freezeWriteRowPane = new FreezeWriteRowPane();
		writeRowPane.add(freezeWriteRowPane);
		writecon.add(writeColPane);
		freezeWriteColPane = new FreezeWriteColPane();
		writeColPane.add(freezeWriteColPane);
		
		return writePanel;
	}
	
	public RepeatAndFreezeSettingPane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		JPanel outrepeatPanel = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("FR-Engine_Repeat"));
		JPanel cenrepeatPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
		outrepeatPanel.add(cenrepeatPanel);
		JPanel outfreezePanel = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("FR-Engine_Frozen"));
		this.add(outrepeatPanel, BorderLayout.NORTH);
		this.add(outfreezePanel, BorderLayout.CENTER);
		JPanel repeatPanel = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
		repeatPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
		JPanel freezePanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
		outfreezePanel.add(freezePanel);
		// �ظ���ӡ����
		// �ظ���ӡ�������ʼ��
		JPanel labelPanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		labelPanel.add(new UILabel(Inter.getLocText("FR-Engine_Select_the_repeated_row_and_column") + ":"));
		UILabel warning = new UILabel("(" + Inter.getLocText("FR-Engine_FreezeWarning1") + ")");
		warning.setForeground(Color.red);
		labelPanel.add(warning);
		cenrepeatPanel.add(labelPanel, BorderLayout.NORTH);
		cenrepeatPanel.add(repeatPanel, BorderLayout.CENTER);

		repeatPanel.add(initRowStartPane());
		addColStart(repeatPanel);
		repeatPanel.add(initFootRowStarPane());
		addFootColStart(repeatPanel);

		freezePanel.add(initPageFrozenPane(), BorderLayout.NORTH);
		addWriteFrozen(freezePanel);

		initPageRwoListener();
		initPageColListener();
		initWriteListener();
	}
	
	protected void initWriteListener(){
		// ��ظ�������
		useWriteFrozenCCheckBox.addChangeListener(useWriteFrozenCListener);
		// ��ظ�������
		useWriteFrozenRCheckBox.addChangeListener(useWriteFrozenRListener);
	}
	
	private void initPageRwoListener(){
		repeatTitleRowPane.addListener(freezePageRowListener);
		// ��ҳ�ظ�������
		usePageFrozenRCheckBox.addChangeListener(usePageFrozenRListener);
		//�ظ�������
		useRepeatTitleRCheckBox.addChangeListener(useRepeatTitleRListener);
		//�ظ���β��
		useRepeatFinisRCheckBox.addChangeListener(useRepeatFinisRListener);
	}
	
	protected void initPageColListener(){
		repeatTitleColPane.addListener(freezePageColListener);
		// ��ҳ�ظ�������
		usePageFrozenCCheckBox.addChangeListener(usePageFrozenCListener);
		//�ظ�������
		useRepeatTitleCCheckBox.addChangeListener(useRepeatTitleCListener);
		//�ظ���β��
		useRepeatFinisCCheckBox.addChangeListener(useRepeatFinisCListener);
	}
	
	protected void addWriteFrozen(JPanel freezePanel) {
		freezePanel.add(initWriteFrozenPane(), BorderLayout.CENTER);
	}
	
	protected void addFootColStart(JPanel repeatPanel){
		repeatPanel.add(initFootColStartPane());
	}
	
	protected void addColStart(JPanel repeatPanel){
		repeatPanel.add(initColStartPane());
	}
	
	ChangeListener useRepeatFinisCListener = new ChangeListener() {

		@Override
		public void stateChanged(ChangeEvent e) {
			repeatFinisColPane.setEnabled(useRepeatFinisCCheckBox.isSelected());

		}
	};
	
	ChangeListener useRepeatFinisRListener = new ChangeListener() {

		@Override
		public void stateChanged(ChangeEvent e) {
			repeatFinisRowPane.setEnabled(useRepeatFinisRCheckBox.isSelected());

		}
	};
	
	ChangeListener useRepeatTitleCListener = new ChangeListener() {

		@Override
		public void stateChanged(ChangeEvent e) {
			boolean flag = useRepeatTitleCCheckBox.isSelected();
			repeatTitleColPane.setEnabled(flag);
			if (!flag) {
				usePageFrozenCCheckBox.setSelected(false);
				usePageFrozenCCheckBox.setEnabled(false);
			} else {
				usePageFrozenCCheckBox.setEnabled(true);
			}
		}
	};
	
	ChangeListener useRepeatTitleRListener = new ChangeListener() {

		@Override
		public void stateChanged(ChangeEvent e) {
			boolean flag = useRepeatTitleRCheckBox.isSelected();
			repeatTitleRowPane.setEnabled(flag);
			if (!flag) {
				usePageFrozenRCheckBox.setSelected(false);
				usePageFrozenRCheckBox.setEnabled(false);
			} else {
				usePageFrozenRCheckBox.setEnabled(true);
			}
		}
	};
	
	ChangeListener useWriteFrozenRListener = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			boolean flag = useWriteFrozenRCheckBox.isSelected();
			freezeWriteRowPane.setEnabled(flag);

		}
	};
	
	ChangeListener useWriteFrozenCListener = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			boolean flag = useWriteFrozenCCheckBox.isSelected();
			freezeWriteColPane.setEnabled(flag);

		}
	};
	
	ChangeListener usePageFrozenCListener = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			freezePageColPane.setEnabled(usePageFrozenCCheckBox.isSelected());
		}
	};
	
	ChangeListener usePageFrozenRListener = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			freezePageRowPane.setEnabled(usePageFrozenRCheckBox.isSelected());
		}
	};
	
	// ��ҳ�ظ���������������
	ChangeListener freezePageRowListener = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			FT ft = repeatTitleRowPane.updateBean();
			int to = ft.getTo();
			freezePageRowPane.populateBean(new FT(to > -1 ? 0 : -1, to));
		}
	};
	
	// ��ҳ�ظ���������������
	ChangeListener freezePageColListener = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			FT ft = repeatTitleColPane.updateBean();
			int to = ft.getTo();
			freezePageColPane.populateBean(new FT(to > -1 ? 0 : -1, to));
		}
	};

	/**
	 * ���ڱ���
	 * 
	 *
	 * @date 2014-11-14-����2:30:58
	 * 
	 */
	protected String title4PopupWindow() {
		return Inter.getLocText("FR-Engine_Repeat-Freeze");
	}

	public void populate(ReportPageAttrProvider attribute) {

		if (attribute == null) {
			attribute = (ReportPageAttrProvider) StableFactory.createXmlObject(ReportPageAttrProvider.XML_TAG);
		}
		FT defaultFT = new FT(0, 0);
		populatColPane(attribute, defaultFT);
		populateRowPane(attribute, defaultFT);
	}
	
	protected void populateRowPane(ReportPageAttrProvider attribute, FT defaultFT){
		FT ft = new FT(new Integer(attribute.getRepeatHeaderRowFrom()), new Integer(attribute.getRepeatHeaderRowTo()));
		if (isDefalut(ft)) {
			this.repeatTitleRowPane.populateBean(defaultFT);
			this.repeatTitleRowPane.setEnabled(false);
			usePageFrozenRCheckBox.setEnabled(false);
		} else {
			this.repeatTitleRowPane.populateBean(ft);
			useRepeatTitleRCheckBox.setSelected(true);
		}

		ft = new FT(new Integer(attribute.getRepeatFooterRowFrom()), new Integer(attribute.getRepeatFooterRowTo()));
		if (isDefalut(ft)) {
			this.repeatFinisRowPane.populateBean(defaultFT);
			this.repeatFinisRowPane.setEnabled(false);
			useRepeatFinisRCheckBox.setSelected(false);
		} else {
			this.repeatFinisRowPane.populateBean(ft);
			useRepeatFinisRCheckBox.setSelected(true);
		}
		
		this.usePageFrozenRCheckBox.setSelected(attribute.isUsePageFrozenRow());
		this.freezePageRowPane.setEnabled(attribute.isUsePageFrozenRow());
	}
	
	protected void populatColPane(ReportPageAttrProvider attribute, FT defaultFT){
		FT ft = new FT(new Integer(attribute.getRepeatHeaderColumnFrom()), new Integer(attribute.getRepeatHeaderColumnTo()));
		if (isDefalut(ft)) {
			this.repeatTitleColPane.populateBean(defaultFT);
			this.repeatTitleColPane.setEnabled(false);
			usePageFrozenCCheckBox.setEnabled(false);
		} else {
			this.repeatTitleColPane.populateBean(ft);
			useRepeatTitleCCheckBox.setSelected(true);
		}
		
		ft = new FT(new Integer(attribute.getRepeatFooterColumnFrom()), new Integer(attribute.getRepeatFooterColumnTo()));
		if (isDefalut(ft)) {
			this.repeatFinisColPane.populateBean(defaultFT);
			this.repeatFinisColPane.setEnabled(false);
			useRepeatFinisCCheckBox.setSelected(false);
		} else {
			this.repeatFinisColPane.populateBean(ft);
			useRepeatFinisCCheckBox.setSelected(true);
		}
		
		this.usePageFrozenCCheckBox.setSelected(attribute.isUsePageFrozenColumn());
		this.freezePageColPane.setEnabled(attribute.isUsePageFrozenColumn());
	}

	private boolean isDefalut(FT ob) {
		return ob.getFrom() == -1 && ob.getTo() == -1;
	}

    /**
	 * ��ʼ�������pane
	 * 
	 * @param writeFrozenColumnRow ��������
	 * 
	 *
	 * @date 2014-11-14-����2:30:15
	 * 
	 */
	public void populateWriteFrozenColumnRow(ColumnRow writeFrozenColumnRow) {
		if (writeFrozenColumnRow != null) {
			int col = writeFrozenColumnRow.getColumn();
			int row = writeFrozenColumnRow.getRow();
			if (col > 0) {
				freezeWriteColPane.populateBean(new FT(1, col - 1));
			}
			if (row > 0) {
				freezeWriteRowPane.populateBean(new FT(1, row - 1));
			}

			useWriteFrozenCCheckBox.setSelected(col > 0);
			useWriteFrozenRCheckBox.setSelected(row > 0);
			freezeWriteColPane.setEnabled(col > 0);
			freezeWriteRowPane.setEnabled(row > 0);
		} else {
			useWriteFrozenCCheckBox.setSelected(false);
			useWriteFrozenRCheckBox.setSelected(false);
			freezeWriteRowPane.setEnabled(false);
			freezeWriteColPane.setEnabled(false);
		}
	}

	public ReportPageAttrProvider update() {
		ReportPageAttrProvider attribute = (ReportPageAttrProvider) StableFactory.createXmlObject(ReportPageAttrProvider.XML_TAG);

		updateRowPane(attribute);
		updateColPane(attribute);

		return attribute;
	}
	
	protected void updateRowPane(ReportPageAttrProvider attribute){
		// �ظ�������
		int titleFrom = valid(useRepeatTitleRCheckBox, this.repeatTitleRowPane.updateBean().getFrom());
		int titleTo = valid(useRepeatTitleRCheckBox, this.repeatTitleRowPane.updateBean().getTo());
		attribute.setRepeatHeaderRowFrom(titleFrom);
		attribute.setRepeatHeaderRowTo(titleTo);

		int finishFrom = valid(useRepeatFinisRCheckBox, this.repeatFinisRowPane.updateBean().getFrom());
		int finishTo = valid(useRepeatFinisRCheckBox, this.repeatFinisRowPane.updateBean().getTo());
		attribute.setRepeatFooterRowFrom(finishFrom);
		attribute.setRepeatFooterRowTo(finishTo);
		
		attribute.setUsePageFrozenRow(this.usePageFrozenRCheckBox.isSelected());
	}
	
	private int valid(UICheckBox checkBox, int num){
		return checkBox.isSelected() ? num : -1;
	}
	
	protected void updateColPane(ReportPageAttrProvider attribute){
		int titleFrom = valid(useRepeatTitleCCheckBox, this.repeatTitleColPane.updateBean().getFrom());
		int titleTo = valid(useRepeatTitleCCheckBox, this.repeatTitleColPane.updateBean().getTo());
		attribute.setRepeatHeaderColumnFrom(titleFrom);
		attribute.setRepeatHeaderColumnTo(titleTo);
		
		int finishFrom = valid(useRepeatFinisCCheckBox, this.repeatFinisColPane.updateBean().getFrom());
		int finishTo = valid(useRepeatFinisCCheckBox, this.repeatFinisColPane.updateBean().getTo());
		attribute.setRepeatFooterColumnFrom(finishFrom);
		attribute.setRepeatFooterColumnTo(finishTo);
		
		attribute.setUsePageFrozenColumn(this.usePageFrozenCCheckBox.isSelected());
	}
	
    /**
	 * ����WriteFrozenColumnRow
	 * 
	 * @return ����
	 * 
	 *
	 * @date 2014-11-14-����2:29:45
	 * 
	 */
	public ColumnRow updateWriteFrozenColumnRow() {
		if (useWriteFrozenCCheckBox.isSelected() || useWriteFrozenRCheckBox.isSelected()) {
			int col = useWriteFrozenCCheckBox.isSelected() ? freezeWriteColPane.updateBean().getTo() + 1 : 0;
			int row = useWriteFrozenRCheckBox.isSelected() ? freezeWriteRowPane.updateBean().getTo() + 1 : 0;
            return ColumnRow.valueOf(col, row);
        }
		return null;
	}

}
