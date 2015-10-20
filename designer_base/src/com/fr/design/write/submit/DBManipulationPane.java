package com.fr.design.write.submit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import com.fr.base.BaseUtils;
import com.fr.base.Formula;
import com.fr.base.Parameter;
import com.fr.data.DataConstants;
import com.fr.data.condition.JoinCondition;
import com.fr.data.condition.ListCondition;
import com.fr.data.core.db.dml.Table;
import com.fr.design.actions.UpdateAction;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.DSColumnLiteConditionPane;
import com.fr.design.constants.UIConstants;
import com.fr.design.data.datapane.ChoosePaneSupportFormula;
import com.fr.design.data.datapane.DataBaseItems;
import com.fr.design.data.tabledata.tabledatapane.FormatExplanationPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.editor.editor.Editor;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ilist.CheckBoxList;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.javascript.JavaScriptActionPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.scrollruler.ModLineBorder;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.DateUtils;
import com.fr.general.Inter;
import com.fr.general.data.Condition;
import com.fr.stable.ColumnRow;
import com.fr.stable.ColumnRowGroup;
import com.fr.write.DBManipulation;
import com.fr.write.NameSubmitJob;
import com.fr.write.config.ColumnConfig;
import com.fr.write.config.DMLConfig;
import com.fr.write.config.DeleteConfig;
import com.fr.write.config.InsertConfig;
import com.fr.write.config.IntelliDMLConfig;
import com.fr.write.config.UpdateConfig;

//august���������Ӧ�÷ֳ����࣬һ�����е�Ԫ��������һ����û�е�Ԫ������
public class DBManipulationPane extends BasicBeanPane<DBManipulation> {
    private static final Image ICODS_IMAGE = BaseUtils.readImage("/com/fr/web/core/css/images/icons.png");
    private static final Icon HEIP_ICON = BaseUtils.createIcon(ICODS_IMAGE, 193, 1, 14, 14);
    private static final int DEFAULT_RETURN_VALUE = 4;
	public KeyColumnNameValueTable keyColumnValuesTable;
	private UIComboBox columnsComboBox; // ���ڱ༭ColumnName��Editor
    private UICheckBox UpdateCheckBox;
    private JPanel checkBoxUpdatePane;
	/*
	 * ��¼��ǰѡȡ��DS & Table��Ӧ��ColumnName[]
	 * alex:��ǰÿ��tableNameComboBox�ı��ˢ��columnsComboBox,����ÿ����һ�����־ͻ�ˢһ��,�ܲ���
	 */
	private ColumnName[] currentColumnNames = null;

	// ֧�ֹ�ʽ��������ݱ�ѡ�����
	private ChoosePaneSupportFormula chooseTable;

	private UIComboBox dmlConfigComboBox = null;

	// �ύ�¼�
	private NameSubmitJob[] jobs = null;

	// �ύ����
	private Condition condition = null;

	private JTree conditionsTree;

	private Editor<?>[] v_Types;

	protected JavaScriptActionPane parentPane;

	private int keyColumnWidth = 100;
	private int resizeColumnCount = 4;
	private int btnWidth = 110;
	private int btnHeight = 20;
    private String subMitName;

	private static final String[] DML_CONFIG_TYPES = new String[] {
            Inter.getLocText(new String[]{"Smart", "Submit"}),
            Inter.getLocText(new String[]{"Delete", "Submit"}),
			Inter.getLocText(new String[]{"Insert", "Submit"}),
            Inter.getLocText(new String[]{"Update", "Submit"})};

	/**
	 * �޵�Ԫ��û��������ӵ�Ԫ��Ȱ�ť
	 * �е�Ԫ��Ĳμ�������SmartInsertDBManipulationPane
	 */
	public DBManipulationPane() {
		this(ValueEditorPaneFactory.extendedCellGroupEditors());
	}

	public DBManipulationPane(Editor<?>[] v_Types) {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		this.v_Types = v_Types;

		JPanel northPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		this.add(northPane, BorderLayout.NORTH);

		dmlConfigComboBox = new UIComboBox(DML_CONFIG_TYPES);

		JPanel typePane = GUICoreUtils.createFlowPane(new Component[] { new UILabel(Inter.getLocText(new String[]{"Choose", "Type"}) + ":"), dmlConfigComboBox },
				FlowLayout.LEFT, 10);
		typePane.setBorder(BorderFactory.createTitledBorder(new ModLineBorder(ModLineBorder.TOP), Inter.getLocText(new String[]{"Submit", "Type"})));
		northPane.add(typePane, BorderLayout.NORTH);

		chooseTable = new ChoosePaneSupportFormula();
		chooseTable.setBorder(BorderFactory.createTitledBorder(new ModLineBorder(ModLineBorder.TOP), Inter.getLocText("FR-Base_Table")));
		chooseTable.setTableNameComboBoxPopSize(160, 320);

		northPane.add(chooseTable, BorderLayout.CENTER);

		// peter:�༭��TablePane
		JPanel editTablePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		this.add(editTablePane, BorderLayout.CENTER);
		editTablePane.setBorder(BorderFactory.createTitledBorder(new ModLineBorder(ModLineBorder.TOP), Inter.getLocText("FR-Base_Value")));

		keyColumnValuesTable = new KeyColumnNameValueTable();
		editTablePane.add(new JScrollPane(this.keyColumnValuesTable), BorderLayout.CENTER);
		keyColumnValuesTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		keyColumnValuesTable.setPreferredScrollableViewportSize(new Dimension(280, 180));
		keyColumnValuesTable.setShowHorizontalLines(true);

		initJTableColumn();

		addButtons(editTablePane);

		addBottomPane();

		addListeners();
	}

    public void setSubMitName(String subMitName){
        this.subMitName = subMitName;
    }

    public String getSubMitName(){
        return this.subMitName;
    }

	private void addButtons(JPanel editTablePane) {
		// alex:��Ӳ�����ť
		UpdateAction[] actions = this.getActions();
		if (actions != null && actions.length > 0) {
			JPanel controlBtnPane = new JPanel(new GridLayout(actions.length + 1, 1, 4, 4));
			editTablePane.add(GUICoreUtils.createBorderPane(controlBtnPane, BorderLayout.NORTH), BorderLayout.EAST);

			for (int i = 0; i < actions.length; i++) {
				controlBtnPane.add(new UIButton(actions[i]));
			}
            checkBoxUpdatePane = new JPanel(new BorderLayout(0, 0));
            checkBoxUpdatePane.setPreferredSize(new Dimension(120,20));
            controlBtnPane.add(checkBoxUpdatePane);

            UpdateCheckBox = new UICheckBox(Inter.getLocText("RWA-NotChange_Unmodified"));
            UIButton  helpButton = new UIButton(HEIP_ICON);
            helpButton.setToolTipText(Inter.getLocText("FR-Base_Help"));
            helpButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    FormatExplanationPane formatExplanation = new FormatExplanationPane(Inter.getLocText("FR-Base_Help"),Inter.getLocText("FR-Designer_RWA-Help"), 12f);
                    BasicDialog dlg = formatExplanation.showMediumWindow(SwingUtilities.getWindowAncestor(DBManipulationPane.this),
                            new DialogActionAdapter(){});
                    dlg.setVisible(true);
                }
            });
            helpButton.set4ToolbarButton();
            checkBoxUpdatePane.add(UpdateCheckBox,BorderLayout.WEST);
            checkBoxUpdatePane.add(helpButton,BorderLayout.EAST);
		}
	}

    protected void updateUpdateCheckBoxEnable(){
        KeyColumnTableModel model = (KeyColumnTableModel)keyColumnValuesTable.getModel();
        if (model.getRowCount() == 0){
            setUpdateCheckBoxEnable(false);
            UpdateCheckBox.setSelected(true);
            return;
        }
        ArrayList columnObjects = new ArrayList();
        for (int i = 0; i < model.getRowCount(); i++) {
            columnObjects.add(model.getKeyColumnNameValue(i).cv.getObj());
        }
        for (int i = 0;i < columnObjects.size();i++){
            Object ob = columnObjects.get(i) ;
            if (!( ob instanceof ColumnRow || ob instanceof ColumnRowGroup)){
                setUpdateCheckBoxEnable(false);
                UpdateCheckBox.setSelected(false);
                return;
            }
        }
        setUpdateCheckBoxEnable(true);
    }

    private void setUpdateCheckBoxEnable( boolean b){
        UpdateCheckBox.setEnabled(b);
    }

	private void addBottomPane() {
		JPanel eventPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		eventPane.add(addEventButton());

		JPanel conditionPane = this.createConditionPane();

		JPanel btPane =new JPanel( FRGUIPaneFactory.createBorderLayout());
        btPane.add(eventPane,BorderLayout.CENTER);
        btPane.add(conditionPane,BorderLayout.NORTH);
		this.add(btPane, BorderLayout.SOUTH);
	}

	private UIButton addEventButton() {
		UIButton addSubmitEventButton = new UIButton(Inter.getLocText("FR-Designer_Set_Submit_Event"));
		addSubmitEventButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				final SubmitJobListPane jobsPane = createSubmitJobListPane();

				jobsPane.populate(jobs);
				BasicDialog dialog = jobsPane.showWindow(SwingUtilities.getWindowAncestor(DBManipulationPane.this), new DialogActionAdapter() {
					@Override
					public void doOk() {
						super.doOk();
						jobs = jobsPane.updateDBManipulation();
					}
				});
				dialog.setVisible(true);
			}

		});
		return addSubmitEventButton;
	}

	private JPanel createConditionPane() {
		JPanel conditionPane = new JPanel();
		conditionPane.setPreferredSize(createConditionPanePreferredSize());

        setBorderAndLayout(conditionPane);

		conditionsTree = new JTree(new DefaultTreeModel(new ExpandMutableTreeNode()));
		conditionsTree.setRootVisible(false);
		conditionsTree.setShowsRootHandles(true);
		conditionsTree.setBackground(UIConstants.NORMAL_BACKGROUND);
		conditionsTree.setForeground(UIConstants.NORMAL_BACKGROUND);
		DefaultTreeCellRenderer cr = (DefaultTreeCellRenderer) conditionsTree.getCellRenderer();
		cr.setForeground(UIConstants.NORMAL_BACKGROUND);
		JScrollPane jp = new JScrollPane(conditionsTree);
		addComponent(conditionPane,jp);

		UIButton addSubmitConditionButton = new UIButton(Inter.getLocText("FR-Designer_Set_Submit_Condition"));
		addSubmitConditionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final DSColumnLiteConditionPane conditionPane = new DSColumnLiteConditionPane();
				String[] columns = chooseTable.currentColumnNames();
				if (columns != null && columns.length > 0) {
					conditionPane.populateColumns(chooseTable.currentColumnNames());
				}

				conditionPane.populateBean(condition);
				BasicDialog dialog = conditionPane.showWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
					@Override
					public void doOk() {
						super.doOk();
						condition = conditionPane.updateBean();
						refreshConditionList();
					}
				});
				dialog.setVisible(true);
			}
		});

		JPanel controlBtnPane = new JPanel(new GridLayout(1, 1, 4, 4));
        controlBtnPane.setPreferredSize(createControlBtnPanePreferredSize());
        conditionPane.add(GUICoreUtils.createBorderPane(controlBtnPane, BorderLayout.NORTH),setControlBtnPanePosition());
		controlBtnPane.add(addSubmitConditionButton);

		return conditionPane;
	}

    protected void setBorderAndLayout(JPanel jPanel){
        jPanel.setLayout(FRGUIPaneFactory.createBorderLayout());
        jPanel.setBorder(BorderFactory.createTitledBorder(
                new ModLineBorder(ModLineBorder.TOP), Inter.getLocText(new String[]{"Submit", "Condition"})));
    }

    protected void addComponent(JPanel mainPane,JScrollPane addPane){
        mainPane.add(addPane,BorderLayout.CENTER);
    }


    protected Dimension createConditionPanePreferredSize(){
        return new Dimension(454, 80);
    }

    protected Dimension createControlBtnPanePreferredSize(){
        return new Dimension(110, 20);
    }

    protected String setControlBtnPanePosition(){
       return  BorderLayout.EAST;
    }

	private void refreshConditionList() {
		DefaultTreeModel defaultTreeModel = (DefaultTreeModel) conditionsTree.getModel();
		ExpandMutableTreeNode rootTreeNode = (ExpandMutableTreeNode) defaultTreeModel.getRoot();
		rootTreeNode.setUserObject(new JoinCondition(DataConstants.AND, new ListCondition()));
		rootTreeNode.removeAllChildren();
		Condition liteCondition = this.condition == null ? new ListCondition() : this.condition;
		if (liteCondition instanceof ListCondition) {
			ListCondition listCondition = (ListCondition) liteCondition;
			int joinConditionCount = listCondition.getJoinConditionCount();
			for (int i = 0; i < joinConditionCount; i++) {
				addLiteConditionToListCondition(rootTreeNode, listCondition.getJoinCondition(i));
			}
		} else {
			ExpandMutableTreeNode newTreeNode = new ExpandMutableTreeNode(new JoinCondition(DataConstants.AND, liteCondition));
			rootTreeNode.add(newTreeNode);
		}
		defaultTreeModel.reload(rootTreeNode);
		rootTreeNode.expandCurrentTreeNode(conditionsTree);
	}

	private void addLiteConditionToListCondition(ExpandMutableTreeNode parentTreeNode, JoinCondition joinCondition) {
		ExpandMutableTreeNode newTreeNode = new ExpandMutableTreeNode(joinCondition);
		parentTreeNode.add(newTreeNode);
		Condition liteCondition = joinCondition.getCondition();
		if (liteCondition instanceof ListCondition) {
			ListCondition listCondition = (ListCondition) liteCondition;
			int joinConditionCount = listCondition.getJoinConditionCount();
			for (int i = 0; i < joinConditionCount; i++) {
				addLiteConditionToListCondition(newTreeNode, listCondition.getJoinCondition(i));
			}
		}
	}

	private void addListeners() {
		dmlConfigComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
                    Object ob = e.getItem();
					if ((ComparatorUtils.equals(ob,DML_CONFIG_TYPES[0])) || ComparatorUtils.equals(ob,DML_CONFIG_TYPES[3])) {
                        checkBoxUpdatePane.setVisible(true);
					}  else if ((ComparatorUtils.equals(ob,DML_CONFIG_TYPES[1])) || ComparatorUtils.equals(ob,DML_CONFIG_TYPES[2])){
                        checkBoxUpdatePane.setVisible(false);
                    }
				}
			}
		});

		/*
		 * ��DS & Table�ı�ʱҪˢ��ColumnsComboBox.model,������ItemListener,
		 * ��ΪtableNameComboBox�ǿ��Ա༭��,ÿдһ�����־�Ҫ��,̫Ƶ����
		 * Ҳ������tableNameComboBox.focusLost�¼�ʱ,����û��
		 */
		keyColumnValuesTable.addFocusListener(new FocusAdapter() {

			public void focusGained(FocusEvent e) {
				refreshColumnsComboBox();
			}

		});
	}

	protected UpdateAction[] getActions() {
		return new UpdateAction[] { new SmartAddFieldsAction(), new AddFieldAction(), new RemoveFieldAction() };
	}

	protected SubmitJobListPane createSubmitJobListPane() {
		return new SubmitJobListPane();
	}

	public void setParentJavaScriptActionPane(JavaScriptActionPane jsPane) {
		this.parentPane = jsPane;
	}

	@Override
	protected String title4PopupWindow() {
		return "DB";
	}

	protected class SmartAddFieldsAction extends UpdateAction {
		public SmartAddFieldsAction() {
			this.setName(Inter.getLocText("RWA-Smart_Add_Fields"));
		}

		public void actionPerformed(ActionEvent evt) {

			BasicPane bPane = new BasicPane() {
				@Override
				protected String title4PopupWindow() {
					return Inter.getLocText("RWA-Smart_Add_Fields");
				}
			};
			bPane.setLayout(FRGUIPaneFactory.createBorderLayout());

			final CheckBoxList list = new CheckBoxList(currentColumnNames(), CheckBoxList.SelectedState.ALL, Inter.getLocText("FR-Designer_Chart_Field_Name")) {
				public String value2Text(Object value) {
					if (value instanceof ColumnName) {
						return ((ColumnName)value).name;
					}

					return super.value2Text(value);
				}
			};
			bPane.add(new JScrollPane(list), BorderLayout.CENTER);

			BasicDialog dlg = bPane.showSmallWindow(SwingUtilities.getWindowAncestor(DBManipulationPane.this), new DialogActionAdapter() {
				public void doOk() {
					addFields(list);
                    updateUpdateCheckBoxEnable();
				}
			});
			dlg.setVisible(true);
		}
	}

    private void addFields (CheckBoxList list) {
        KeyColumnTableModel model = (KeyColumnTableModel)keyColumnValuesTable.getModel();
        List<KeyColumnNameValue> keyColumnNameValueList = new ArrayList<KeyColumnNameValue>();
        keyColumnNameValueList.clear();
        for (int i = 0; i < model.getRowCount(); i++) {
            keyColumnNameValueList.add(model.getKeyColumnNameValue(i));
        }
        model.removeAllKeyColumnNameValue();
        Object[] selected = list.getSelectedValues();
        // Richie:���˴洢�µ�KeyColumnNameValue��List.
        List<KeyColumnNameValue> newKeyColumnNameValueList = new ArrayList<KeyColumnNameValue>();
        if (!keyColumnNameValueList.isEmpty()) {
            for (int i = 0; i < selected.length; i++) {
                // Richie:�����Ͽյ�.
                newKeyColumnNameValueList.add(new KeyColumnNameValue(false, (ColumnName)selected[i], new ColumnValue(""), false));
            }
        }
        // Richie:��ʼ��
        int returnValue = DEFAULT_RETURN_VALUE;
        int coverNumber = 0;
        for (int i = 0; i < selected.length; i++) {
            if (returnValue == 0 || returnValue == 3) {
                break;
            }
            for (int j = 0; j < keyColumnNameValueList.size(); j++) {
                if (ComparatorUtils.equals(selected[i], keyColumnNameValueList.get(j).cn)) {
                    Object[] options = { Inter.getLocText("FR-Designer_Covered_All"), Inter.getLocText("FR-Base_Yes"), Inter.getLocText("FR-Base_No"), Inter.getLocText("FR-Designer_Cover_None") };
                    returnValue = JOptionPane.showOptionDialog(DBManipulationPane.this, keyColumnNameValueList.get(j).cn.name
                            + Inter.getLocText( new String[] {"Has_Existed", "Want_To_Cover_It"}, new String[] {",",  "��"}),
                            "", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                    // Richie:ȫ������
                    if (returnValue == 0) {
                        break;
                        // Richie:����ָ����
                    } else if (returnValue == 1) {
                        coverNumber = i;
                        // Richie:�������ƶ���
                    } else if (returnValue == 2) {
                        coverNumber = i;
                        newKeyColumnNameValueList.remove(i);
                        newKeyColumnNameValueList.add(i, keyColumnNameValueList.get(j));
                        // Richie:ȫ��������
                    } else if (returnValue == 3) {
                        coverNumber = i;
                        break;
                    }
                }
            }
        }
        checkTableModel(returnValue, coverNumber, model, selected, keyColumnNameValueList, newKeyColumnNameValueList);
    }

    private void checkTableModel (int returnValue, int coverNumber, KeyColumnTableModel model, Object[] selected, List<KeyColumnNameValue> keyColumnNameValueList, List<KeyColumnNameValue> newKeyColumnNameValueList) {
        if (returnValue == 0) {
            model.removeAllKeyColumnNameValue();
            // Richie:ȫ������,��selected�ĳ������Ĭ�ϵ���
            for (int i = 0; i < selected.length; i++) {
                model.addKeyColumnNameValue(newKeyColumnNameValueList.get(i));
            }

        } else if (returnValue == 3) {
            model.removeAllKeyColumnNameValue();
            // Richie:ȫ��������,�Ѿ����ڵľͱ���,�����ڵ����Ĭ����
            for (int i = coverNumber; i < selected.length; i++) {
                for (int j = 0; j < keyColumnNameValueList.size(); j++) {
                    if (ComparatorUtils.equals(selected[i], keyColumnNameValueList.get(j).cn)) {
                        newKeyColumnNameValueList.remove(i);
                        newKeyColumnNameValueList.add(i, keyColumnNameValueList.get(j));
                    }
                }

            }

            for (int i = 0; i < selected.length; i++) {
                model.addKeyColumnNameValue(newKeyColumnNameValueList.get(i));
            }

        } else if (returnValue == 1 || returnValue == 2) {
            for (int i = 0; i < selected.length; i++) {
                model.addKeyColumnNameValue(newKeyColumnNameValueList.get(i));
            }
        }

        // Richie:��ʼ��
        if (keyColumnNameValueList.isEmpty()) {
            model.removeAllKeyColumnNameValue();
            for (int i = 0; i < selected.length; i++) {
                model.addKeyColumnNameValue(new KeyColumnNameValue(false, (ColumnName)selected[i], new ColumnValue(""), false));
            }
        }
        model.fireTableDataChanged();
        keyColumnValuesTable.validate();
    }

	protected class AddFieldAction extends UpdateAction {
		public AddFieldAction() {
			this.setName(Inter.getLocText("RWA-Add_Field"));
		}

		public void actionPerformed(ActionEvent e) {

			KeyColumnTableModel model = (KeyColumnTableModel)keyColumnValuesTable.getModel();

			model.addKeyColumnNameValue(new KeyColumnNameValue(false, new ColumnName(""), new ColumnValue(""), false));
            updateUpdateCheckBoxEnable();
			model.fireTableDataChanged();

			keyColumnValuesTable.getSelectionModel().setSelectionInterval(model.getRowCount() - 1, model.getRowCount() - 1);
		}
	}

	protected class RemoveFieldAction extends UpdateAction {
		public RemoveFieldAction() {
			this.setName(Inter.getLocText("RWA-Remove_Field"));
		}

		public void actionPerformed(ActionEvent evt) {
			// DBManipulationPane target = this.getDBManipulationPane();

			int[] selectedRows = keyColumnValuesTable.getSelectedRows();
			if (selectedRows == null || selectedRows.length == 0) {
				return;
			}

			int returnVal = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(DBManipulationPane.this),
					Inter.getLocText("FR-Base_sure_remove_item") + "?", Inter.getLocText("FR-Base_Remove"), JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			if (returnVal == JOptionPane.OK_OPTION) {
				KeyColumnTableModel keyColumnNameValueTableModel = (KeyColumnTableModel)keyColumnValuesTable.getModel();

				// ��selectedRows��С�����Ÿ���,��ɾ�����ɾС��
				java.util.Arrays.sort(selectedRows);
				for (int i = selectedRows.length - 1; i >= 0; i--) {
					keyColumnNameValueTableModel.removeKeyColumnNameValue(selectedRows[i]);
				}
                updateUpdateCheckBoxEnable();
				keyColumnNameValueTableModel.fireTableDataChanged();

				// select other one.
				if (keyColumnNameValueTableModel.getRowCount() > selectedRows[0]) {
					keyColumnValuesTable.getSelectionModel().setSelectionInterval(selectedRows[0], selectedRows[0]);
				} else if (keyColumnNameValueTableModel.getRowCount() > 0) {
					keyColumnValuesTable.getSelectionModel().setSelectionInterval(0, 0);
				}
			}
		}
	}

	/*
	 * ˢ��columnsComboBox
	 */
	private void refreshColumnsComboBox() {
		DefaultComboBoxModel model = (DefaultComboBoxModel)this.columnsComboBox.getModel();
		model.removeAllElements();

		ColumnName[] columnNames = currentColumnNames();
		for (int i = 0; i < columnNames.length; i++) {
			model.addElement(columnNames[i]);
		}
	}

	// �õ���ǰ��ColumnName[]
	private ColumnName[] currentColumnNames() {
		// ben:���ԭ�е�
		if (currentColumnNames != null) {
			currentColumnNames = null;
		}
		String[] colNames = this.chooseTable.currentColumnNames();
		int len = colNames.length;
		currentColumnNames = new ColumnName[len];
		for (int i = 0; i < len; i++) {
			currentColumnNames[i] = new ColumnName(colNames[i]);
		}
		if (currentColumnNames == null) {
			currentColumnNames = new ColumnName[0];
		}
		return currentColumnNames;
	}

	/*
	 * ����JTable��Column
	 */
	private void initJTableColumn() {
		TableColumn column0 = this.keyColumnValuesTable.getColumnModel().getColumn(0);
		column0.setMaxWidth(50);

		TableColumn column1 = this.keyColumnValuesTable.getColumnModel().getColumn(1);
		column1.setCellRenderer(new ColumnNameTableCellRenderer());

		TableColumn column2 = this.keyColumnValuesTable.getColumnModel().getColumn(2);
		column2.setCellRenderer(new ColumnValueTableCellRenderer());

		// ����column1��editor
		columnsComboBox = new UIComboBox(new DefaultComboBoxModel());
		columnsComboBox.setRenderer(new UIComboBoxRenderer() {

			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

				if (value instanceof ColumnName) {
					this.setText(((ColumnName)value).name);
				}

				return this;
			}

		});
		column1.setCellEditor(new DefaultCellEditor(columnsComboBox) {
			public boolean stopCellEditing() {
				if (super.stopCellEditing()) {
					return true;
				}

				return false;
			}
		});
		((DefaultCellEditor)column1.getCellEditor()).setClickCountToStart(2);

		//����Column 2��Editor
		column2.setCellEditor(new ColumnValueEditor());
	}

	public void populateBean(DBManipulation dbManipulation) {
		if (dbManipulation == null) {
			dbManipulation = new DBManipulation();
		}
        subMitName = dbManipulation.getName();
		DMLConfig dmlConfig = dbManipulation.getDmlConfig();

		if (dmlConfig != null) {
			if (dmlConfig instanceof IntelliDMLConfig) {
				dmlConfigComboBox.setSelectedIndex(0);
			} else if (dmlConfig instanceof DeleteConfig) {
				dmlConfigComboBox.setSelectedIndex(1);
			} else if (dmlConfig instanceof InsertConfig) {
				dmlConfigComboBox.setSelectedIndex(2);
			} else if (dmlConfig instanceof UpdateConfig) {
				dmlConfigComboBox.setSelectedIndex(3);
			} else {
				dmlConfigComboBox.setSelectedIndex(0);
			}
			jobs = new NameSubmitJob[dmlConfig == null ? 0 : dmlConfig.getSubmitJobCount()];
			for (int i = 0; i < jobs.length; i++) {
				jobs[i] = dmlConfig.getSubmitJob(i);
			}
			condition = dmlConfig.getCondition();
            UpdateCheckBox.setSelected(dmlConfig.isUpdateSelected());
		} else {
			jobs = null;
			condition = null;
			dmlConfigComboBox.setSelectedIndex(0);
            UpdateCheckBox.setSelected(false);
		}

		String schema = null;
		String tableName = null;
        Table table = null;
		if (dmlConfig != null && dmlConfig.getOriTable() != null) {
            table = dmlConfig.getOriTable();
            if (table != null) {
                schema = table.getSchema();
                tableName = table.getName();
            }
        }
		chooseTable.populateBean(new DataBaseItems(dbManipulation.getDBName(), schema, tableName));

		populateKeyColumnValueTable(dmlConfig);

        updateUpdateCheckBoxEnable();
		refreshConditionList();
	}

	private void populateKeyColumnValueTable(DMLConfig dmlConfig) {
		KeyColumnTableModel keyColumnNameValueTableModel = (KeyColumnTableModel)this.keyColumnValuesTable.getModel();
		keyColumnNameValueTableModel.removeAllKeyColumnNameValue();

		if (dmlConfig != null) {
			boolean acceptPara = false;
			for (int i = 0; i < v_Types.length; i++) {
				if (v_Types[i].accept(new Parameter())) {
					acceptPara = true;
					break;
				}
			}
			for (int i = 0; i < dmlConfig.getColumnConfigCount(); i++) {
				ColumnConfig column = dmlConfig.getColumnConfig(i);
				String columnName = column.getColumnName();
				if (!acceptPara && column.getColumnValue() instanceof Parameter) {
					// ����,����ǰ�Ĳ���ת��Ϊ��ʽ
					column.setColumnValue(new Formula(((Parameter)column.getColumnValue()).getName()));
				}

				KeyColumnNameValue newColumnNameValue = new KeyColumnNameValue(column.isKey(), new ColumnName(columnName), new ColumnValue(column.getColumnValue()),
						column.isSkipUnmodified());
				keyColumnNameValueTableModel.addKeyColumnNameValue(newColumnNameValue);
			}
		}
		keyColumnNameValueTableModel.fireTableDataChanged();

		// ѡ���һ��
		if (keyColumnNameValueTableModel.getRowCount() > 0) {
			keyColumnValuesTable.getSelectionModel().setSelectionInterval(0, 0);
		}
	}

	public DBManipulation updateBean() {
		DataBaseItems para = chooseTable.updateBean(true);
		DBManipulation dbMani = new DBManipulation();
        dbMani.setName(subMitName);
		dbMani.setDBName(para.getDatabaseName());

		DMLConfig dmlConfig = new IntelliDMLConfig();
		if (dmlConfigComboBox.getSelectedIndex() == 1) {
			dmlConfig = new DeleteConfig();
		} else if (dmlConfigComboBox.getSelectedIndex() == 2) {
			dmlConfig = new InsertConfig();
		} else if (dmlConfigComboBox.getSelectedIndex() == 3) {
			dmlConfig = new UpdateConfig();
		}

		dbMani.setDmlConfig(dmlConfig);

		dmlConfig.setTable(new Table(para.getSchemaName(), para.getTableName()));

		KeyColumnTableModel keyColumnNameValueTableModel = (KeyColumnTableModel)this.keyColumnValuesTable.getModel();
		int rowCount = keyColumnNameValueTableModel.keyColumnNameValueList.size();
		for (int i = 0; i < rowCount; i++) {
			KeyColumnNameValue newKeyColumnNameValue = keyColumnNameValueTableModel.keyColumnNameValueList.get(i);
			// peter:�����key column name.

			dmlConfig.addColumnConfig(new ColumnConfig(newKeyColumnNameValue.cn.name, newKeyColumnNameValue.cv.obj, newKeyColumnNameValue.isKey,false));
		}
        dmlConfig.setUpdateSelected(UpdateCheckBox.isSelected());

		if (jobs != null) {
			for (int i = 0; i < jobs.length; i++) {
				dmlConfig.addSubmitJob(jobs[i]);
			}
		}
		dmlConfig.setCondition(condition);

		return dbMani;
	}

	protected class ValuePane extends BasicBeanPane<Object> {
		ValueEditorPane vPane;

		public ValuePane() {
			this(v_Types);
		}

		public ValuePane(Editor<?>[] types) {
			vPane = new ValueEditorPane(types);
			this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 45));
			this.add(vPane);
			vPane.setPreferredSize(new Dimension(220, 25));
		}

		@Override
		protected String title4PopupWindow() {
			return Inter.getLocText("FR-Designer_Values-Editor");
		}

		public void populateBean(Object object) {
			vPane.populate(object);
		}

		@Override
		public Object updateBean() {
			return vPane.update();
		}

	}

	/*
	 * alex:ColumnValue�ı༭��,�����Ի������༭...���˾��ò����
	 */
	protected class ColumnValueEditor extends AbstractCellEditor implements TableCellEditor {
		/** The Swing component being edited. */
		private UILabel textLabel;
		private ValuePane vPane;
		private BasicDialog vPaneDLG;

		protected ColumnValueEditor() {
			this(v_Types);
		}

		protected ColumnValueEditor(Editor<?>[] types) {
			textLabel = new UILabel();
			textLabel.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					vPaneDLG.setVisible(true);

				}
			});

			vPane = new ValuePane(types);
			vPaneDLG = vPane.showSmallWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
				public void doOk() {
					fireEditingStopped(); // Make the renderer
					// reappear.
                    updateUpdateCheckBoxEnable();
				}

				public void doCancel() {
					fireEditingCanceled();
				}
			});
		}

		/*
		 * ˫���Ա༭
		 */
		public boolean isCellEditable(EventObject anEvent) {
			if (anEvent instanceof MouseEvent) {
				return ((MouseEvent)anEvent).getClickCount() >= 2;
			}
			return true;
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			if (value instanceof ColumnValue) {
				vPane.populateBean(((ColumnValue)value).obj);

				if (((ColumnValue)value).obj != null) {
					textLabel.setText(((ColumnValue)value).obj.toString());
				} else {
					textLabel.setText("");
				}
			}

			return textLabel;
		}

		public Object getCellEditorValue() {
			return new ColumnValue(vPane.updateBean());
		}
	}

	public static class ColumnName {
		public String name;

		public ColumnName(String name) {
			this.name = name;
		}

		public boolean equals(Object obj) {
			if (!(obj instanceof ColumnName)) {
				return false;
			}

			return ComparatorUtils.equals(this.name, ((ColumnName)obj).name);
		}
	}

	public static class ColumnValue {
		public Object obj;

		public ColumnValue(Object obj) {
			this.obj = obj;
		}

        public Object getObj(){
            return obj;
        }

		public boolean equals(Object obj) {
			if (!(obj instanceof ColumnValue)) {
				return false;
			}

			return ComparatorUtils.equals(this.obj, ((ColumnValue)obj).obj);
		}
	}

	protected static class KeyColumnNameValueTable extends JTable {

		public KeyColumnNameValueTable() {
			super(new KeyColumnValueTableModel(null));
		}


		public KeyColumnTableModel getTableModel4SmartAddCell() {
			return new KeyColumnValueTableModel((KeyColumnTableModel)this.getModel());
		}

	}

	protected abstract static class KeyColumnTableModel extends AbstractTableModel {
        public static final String RAW_KEY = Inter.getLocText("FR-Base_RWA-Key");
        public static final String COLUMN = Inter.getLocText("FR-Base_Column");
        public static final String VALUE = Inter.getLocText("FR-Base_Value");

        public static final String[] COLUMN_NAMES = new String[]{RAW_KEY, COLUMN, VALUE};

		protected java.util.List<KeyColumnNameValue> keyColumnNameValueList = new ArrayList<KeyColumnNameValue>();

		public KeyColumnTableModel(KeyColumnTableModel model) {
			if (model != null) {
				this.keyColumnNameValueList.addAll(model.keyColumnNameValueList);
			}
		}

		public String getColumnName(int col) {
			return COLUMN_NAMES[col];
		}

		public int getColumnCount() {
			return COLUMN_NAMES.length;
		}

		public int getRowCount() {
			return keyColumnNameValueList.size();
		}

		public void addKeyColumnNameValue(KeyColumnNameValue keyColumnNameValue) {
			this.keyColumnNameValueList.add(keyColumnNameValue);
		}

		public void removeKeyColumnNameValue(int index) {
			this.keyColumnNameValueList.remove(index);
		}

		public KeyColumnNameValue getKeyColumnNameValue(int index) {
			return this.keyColumnNameValueList.get(index);
		}

		public void removeAllKeyColumnNameValue() {
			this.keyColumnNameValueList.clear();
		}
	}

	protected static class KeyColumnValueTableModel extends KeyColumnTableModel {

		public KeyColumnValueTableModel(KeyColumnTableModel model) {
			super(model);
		}

		public Object getValueAt(int row, int col) {
			KeyColumnNameValue knv = keyColumnNameValueList.get(row);

			switch (col) {
			case 0:
				return knv.isKey;
			case 1:
				return knv.cn;
			case 2:
				return knv.cv;
			}
			return null;
		}

		public void setValueAt(Object value, int row, int col) {
			KeyColumnNameValue knv = keyColumnNameValueList.get(row);

			if (col == 0 && value instanceof Boolean) {
				knv.isKey = ((Boolean)value).booleanValue();
			} else if (col == 1 && value instanceof ColumnName) {
				knv.cn = (ColumnName)value;
			} else if (col == 2 && value instanceof ColumnValue) {
				knv.cv = (ColumnValue)value;
			}
		}

		public Class<?> getColumnClass(int c) {
			switch (c) {
			case 0:
				return Boolean.class;
			case 1:
				return ColumnName.class;
			case 2:
				return ColumnValue.class;
			}
			return String.class;
		}

		public boolean isCellEditable(int row, int col) {
			return true;
		}
	}



	public static class KeyColumnNameValue {
		private boolean isKey = false;
		private ColumnName cn;
		public ColumnValue cv;

		public KeyColumnNameValue(boolean isKey, ColumnName cn, ColumnValue cv, boolean skip) {
			this.isKey = isKey;
			this.cn = cn;
			this.cv = cv;
		}

		/**
		 * �ַ���
		 * @return �ַ���z
		 */
		public String toString() {
			return (isKey ? "* " : "") + cn + ":" + cv;
		}
	}

	/*
	 * ColumnNameTableCellRenderer
	 */
	public class ColumnNameTableCellRenderer extends DefaultTableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if (value instanceof ColumnName) {
				this.setText(((ColumnName)value).name);
			}

			return this;
		}
	}

	/*
	 * ColumnValueTableCellRenderer
	 */
	private class ColumnValueTableCellRenderer extends DefaultTableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if (value instanceof ColumnValue) {
				if (((ColumnValue)value).obj != null) {
					if (((ColumnValue)value).obj instanceof Date) {
						this.setText(DateUtils.DATEFORMAT2.format(((ColumnValue)value).obj));
					} else {
						this.setText(((ColumnValue)value).obj.toString());
					}
				} else {
					this.setText("");
				}
			}

			return this;
		}
	}
}
