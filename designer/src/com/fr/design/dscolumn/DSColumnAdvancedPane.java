package com.fr.design.dscolumn;

import com.fr.base.Formula;
import com.fr.data.util.SortOrder;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.*;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellExpandAttr;
import com.fr.report.cell.cellattr.core.group.DSColumn;
import com.fr.report.cell.cellattr.core.group.SelectCount;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DSColumnAdvancedPane extends BasicPane {

    private static final String InsetText = " ";
    private SortPane sortPane;
    private SelectCountPane selectCountPane;
    private ValuePane valuePane;
    private UICheckBox horizontalExtendableCheckBox;
    private UICheckBox verticalExtendableCheckBox;
//    private UICheckBox isCoverCheckBox;
    private UICheckBox useMultiplyNumCheckBox;
    private UISpinner multiNumSpinner;
    
    public DSColumnAdvancedPane() {
    	this(DSColumnPane.SETTING_ALL);
    }

    public DSColumnAdvancedPane(int setting) {
        this.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        sortPane = new SortPane();
        sortPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("Sort-Sort_Order"),null));

        if (setting > DSColumnPane.SETTING_DSRELATED) {
	        selectCountPane = new SelectCountPane();
	        selectCountPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("BindColumn-Results_Filter"),null));
        }
        
        valuePane = new ValuePane();
        valuePane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("BindColumn-Custom_Data_Appearance"),null));


        JPanel extendablePane = null;
        if (setting > DSColumnPane.SETTING_DSRELATED) {
            // extendableDirectionPane
            JPanel extendableDirectionPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();

            extendableDirectionPane.add(horizontalExtendableCheckBox = new UICheckBox(Inter.getLocText("ExpandD-Horizontal_Extendable")));
            extendableDirectionPane.add(verticalExtendableCheckBox = new UICheckBox(Inter.getLocText("ExpandD-Vertical_Extendable")));

	        extendablePane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("ExpandD-Expandable"));
	        extendablePane.setLayout(new BorderLayout());
	        extendablePane.add(extendableDirectionPane, BorderLayout.CENTER);
        }

        JPanel multiNumPane = null;
        if (setting > DSColumnPane.SETTING_DSRELATED) {
	        multiNumPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("Fill_blank_Data"));
	        useMultiplyNumCheckBox = new UICheckBox(Inter.getLocText("Column_Multiple"));
	        multiNumPane.add(useMultiplyNumCheckBox);
	        multiNumPane.add(new UILabel(InsetText));
	        
	        multiNumSpinner = new UISpinner(1, 10000, 1, 1);
	        multiNumPane.add(multiNumSpinner);
	        
	        useMultiplyNumCheckBox.addActionListener(new ActionListener() {

	            public void actionPerformed(ActionEvent e) {
	                checkButtonEnabled();
	            }
	        });
        }

        double[] rowSize = {TableLayout.PREFERRED, TableLayout.PREFERRED,
            TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED};
        double[] columnSize = {TableLayout.FILL};
        
        Component[][] components = null;
        if (setting > DSColumnPane.SETTING_DSRELATED) {
        	components = new Component[][]{
                    {sortPane},
                    {selectCountPane},
                    {valuePane},
                    {extendablePane},
                    {multiNumPane}
                };
        } else {
        	components = new Component[][]{
                    {sortPane},
                    {valuePane}
                };
        }
        this.add(TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize), BorderLayout.CENTER);
    }
    
    @Override
    protected String title4PopupWindow() {
    	return Inter.getLocText("Advanced");
    }

    public void populate(TemplateCellElement cellElement) {
        if (cellElement == null) {
            return;
        }

        sortPane.populate(cellElement);
        valuePane.populate(cellElement);
        
        if (selectCountPane != null) {
        	selectCountPane.populate(cellElement);
        	
        	CellExpandAttr cellExpandAttr = cellElement.getCellExpandAttr();
    		if (cellExpandAttr == null) {
    		    cellExpandAttr = new CellExpandAttr();
    		    cellElement.setCellExpandAttr(cellExpandAttr);
    		}
            
            // extendable
            switch (cellExpandAttr.getExtendable()) {
                case CellExpandAttr.Both_EXTENDABLE:
                    horizontalExtendableCheckBox.setSelected(true);
                    verticalExtendableCheckBox.setSelected(true);
                    break;
                case CellExpandAttr.Vertical_EXTENDABLE:
                    horizontalExtendableCheckBox.setSelected(false);
                    verticalExtendableCheckBox.setSelected(true);
                    break;
                case CellExpandAttr.Horizontal_EXTENDABLE:
                    horizontalExtendableCheckBox.setSelected(true);
                    verticalExtendableCheckBox.setSelected(false);
                    break;
                default: {
                    horizontalExtendableCheckBox.setSelected(false);
                    verticalExtendableCheckBox.setSelected(false);
                }

            }

            if (cellExpandAttr.getMultipleNumber() == -1) {
                this.useMultiplyNumCheckBox.setSelected(false);
            } else {
                this.useMultiplyNumCheckBox.setSelected(true);
                this.multiNumSpinner.setValue(cellExpandAttr.getMultipleNumber());
            }
            
            this.checkButtonEnabled();
        }
    }
    
    public void update(TemplateCellElement cellElement) {
        if (cellElement == null) {
            return;
        }

        sortPane.update(cellElement);
        valuePane.update(cellElement);
        
        if (selectCountPane != null) {
        	selectCountPane.update(cellElement);
        	
        	CellExpandAttr cellExpandAttr = cellElement.getCellExpandAttr();
            if (cellExpandAttr == null) {
                cellExpandAttr = new CellExpandAttr();
                cellElement.setCellExpandAttr(cellExpandAttr);
            }

            // extendable
            if (horizontalExtendableCheckBox.isSelected()) {
                if (verticalExtendableCheckBox.isSelected()) {
                    cellExpandAttr.setExtendable(CellExpandAttr.Both_EXTENDABLE);
                } else {
                    cellExpandAttr.setExtendable(CellExpandAttr.Horizontal_EXTENDABLE);
                }
            } else {
                if (verticalExtendableCheckBox.isSelected()) {
                    cellExpandAttr.setExtendable(CellExpandAttr.Vertical_EXTENDABLE);
                } else {
                    cellExpandAttr.setExtendable(CellExpandAttr.None_EXTENDABLE);
                }
            }

            if (this.useMultiplyNumCheckBox.isSelected()) {
                cellExpandAttr.setMultipleNumber((int) this.multiNumSpinner.getValue());
            } else {
                cellExpandAttr.setMultipleNumber(-1);
            }
        }
    }

    private static class SortPane extends SortFormulaPane {
    	private CellElement cellElement;
    	
    	@Override
		public void formulaAction() {            
            if (cellElement == null) {
                return;
            }
            Object value = cellElement.getValue();
            if (value == null || !(value instanceof DSColumn)) {
                return;
            }

            String[] displayNames = DesignTableDataManager.getSelectedColumnNames(
            		DesignTableDataManager.getEditingTableDataSource(), ((DSColumn) value).getDSName());
            
            showFormulaDialog(displayNames);
    	}

        void populate(CellElement cellElement) {
            if (cellElement == null) {
                return;
            }
            this.cellElement = cellElement;

            Object value = cellElement.getValue();
            if (value == null || !(value instanceof DSColumn)) {
                return;
            }
            DSColumn dSColumn = (DSColumn) value;

            int sort = dSColumn.getOrder();
            this.sortOrderComboBox.setSortOrder(new SortOrder(sort));

            String sortFormula = dSColumn.getSortFormula();
            if (sortFormula != null && sortFormula.length() >= 1) {
                sortFormulaTextField.setText(sortFormula);
            } else {
                sortFormulaTextField.setText(sortFormula);
            }
        }

        public void update(CellElement cellElement) {
            if (cellElement == null) {
                return;
            }
            Object value = cellElement.getValue();
            if (value == null || !(value instanceof DSColumn)) {
                return;
            }
            DSColumn dSColumn = (DSColumn) (cellElement.getValue());

            dSColumn.setOrder(this.sortOrderComboBox.getSortOrder().getOrder());
            //lance:sort formula

            String sText = null;
            if (!(sortFormulaTextField.getText() == null || sortFormulaTextField.getText().trim().equals("") || sortFormulaTextField.getText().trim().equals("$$$"))) {
                sText = new String(sortFormulaTextField.getText());
            }
            if (!(sText == null || sText.length() < 1)) {
                dSColumn.setSortFormula(sText);
            } else {
                dSColumn.setSortFormula(null);
            }
        }
    }

    private static class SelectCountPane extends JPanel {

        CellElement cellElement;
        //  private Comparator sortComparator;
        private UIComboBox selectCountComboBox;
        private JPanel selectCountCardPane;
        private UITextField serialTextField;
        
        JFormulaField topFormulaPane;
        JFormulaField bottomFormulaPane;

        public SelectCountPane() {
            this.setLayout(FRGUIPaneFactory.createBorderLayout());

            selectCountComboBox = new UIComboBox(new String[]{
                    Inter.getLocText("Undefined"),
                    Inter.getLocText("BindColumn-Top_N"),
                    Inter.getLocText("BindColumn-Bottom_N"),
                    Inter.getLocText("Odd"),
                    Inter.getLocText("Even"),
                    Inter.getLocText("Specify"),});
            selectCountComboBox.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    int selectIndex = selectCountComboBox.getSelectedIndex();
                    CardLayout c1 = (CardLayout) selectCountCardPane.getLayout();
                    if (selectIndex == 1) {
                        c1.show(selectCountCardPane, "TOP");
                    } else if (selectIndex == 2) {
                        c1.show(selectCountCardPane, "BOTTOM");
                    } else if (selectIndex == 3) {
                        c1.show(selectCountCardPane, "ODD");
                    } else if (selectIndex == 4) {
                        c1.show(selectCountCardPane, "EVEN");
                    } else if (selectIndex == 5) {
                        c1.show(selectCountCardPane, "SPECIFY");
                    } else {
                        c1.show(selectCountCardPane, "UNDEFINE");
                    }
                }
            });

            selectCountCardPane =FRGUIPaneFactory.createCardLayout_S_Pane();
            this.add(GUICoreUtils.createFlowPane(new JComponent[]{new UILabel(InsetText), selectCountComboBox,
                    new UILabel(InsetText), selectCountCardPane}, FlowLayout.LEFT), BorderLayout.WEST);
//            selectCountCardPane.setLayout(new CardLayout());

            //not define pane

            JPanel undefinedPane = GUICoreUtils.createFlowPane(new UILabel(Inter.getLocText("Undefined")), FlowLayout.LEFT);
            topFormulaPane = new JFormulaField("-1");
            bottomFormulaPane = new JFormulaField("-1");
            serialTextField = new UITextField(18);
            JPanel oddPane = GUICoreUtils.createFlowPane(new UILabel(Inter.getLocText("BindColumn-Result_Serial_Number_Start_From_1")
                + "  " + Inter.getLocText("BindColumn-Odd_Selected_(1,3,5...)")), FlowLayout.LEFT);
            JPanel evenPane = GUICoreUtils.createFlowPane(new UILabel(Inter.getLocText("BindColumn-Result_Serial_Number_Start_From_1")
                + "  " + Inter.getLocText("BindColumn-Even_Selected_(2,4,6...)")), FlowLayout.LEFT);
            JPanel specifyPane = GUICoreUtils.createFlowPane(new JComponent[]{
                    serialTextField, new UILabel(
                    Inter.getLocText(new String[]{
                            "Format", "BindColumn-Result_Serial_Number_Start_From_1", "Inner_Parameter", "Group_Count"},
                            new String[]{": 1,2-3,5,8  ", ",", "$__count__"})
            )
                }, FlowLayout.LEFT);
            serialTextField.setToolTipText(Inter.getLocText("StyleFormat-Sample") + ":=JOINARRAY(GREPARRAY(RANGE($__count__), item!=4), \",\")");
            selectCountCardPane.add(undefinedPane, "UNDEFINE");
            selectCountCardPane.add(topFormulaPane, "TOP");
            selectCountCardPane.add(bottomFormulaPane, "BOTTOM");
            //odd
            selectCountCardPane.add(oddPane, "ODD");
            //even
            selectCountCardPane.add(evenPane, "EVEN");
            //specify
            selectCountCardPane.add(specifyPane, "SPECIFY");
        }

        public void populate(CellElement cellElement) {
            if (cellElement == null) {
                return;
            }
            this.cellElement = cellElement;

            Object value = cellElement.getValue();
            if (value == null || !(value instanceof DSColumn)) {
                return;
            }
            DSColumn dSColumn = (DSColumn) (cellElement.getValue());
            SelectCount selectCount = dSColumn.getSelectCount();
            this.topFormulaPane.populateElement(cellElement);
            this.bottomFormulaPane.populateElement(cellElement);
            if (selectCount != null) {
                int selectCountType = selectCount.getType();
                this.selectCountComboBox.setSelectedIndex(selectCountType);
                if (selectCountType == SelectCount.TOP) {
                	this.topFormulaPane.populate(selectCount.getFormulaCount());
                } else if (selectCountType == SelectCount.BOTTOM) {
                	this.bottomFormulaPane.populate(selectCount.getFormulaCount());
                } else if (selectCountType == SelectCount.SPECIFY) {
                    this.serialTextField.setText(selectCount.getSerial());
                }
            }
        }

        public void update(CellElement cellElement) {
            if (cellElement == null) {
                return;
            }
            Object value = cellElement.getValue();
            if (value == null || !(value instanceof DSColumn)) {
                return;
            }
            DSColumn dSColumn = (DSColumn) (cellElement.getValue());

            //alex:SelectCount
            int selectCountSelectIndex = this.selectCountComboBox.getSelectedIndex();
            if (selectCountSelectIndex == 0) {
                dSColumn.setSelectCount(null);
            } else {
                SelectCount selectCount = new SelectCount();
                dSColumn.setSelectCount(selectCount);
                selectCount.setType(selectCountSelectIndex);
                if (selectCountSelectIndex == SelectCount.TOP) {
                    selectCount.setFormulaCount(this.topFormulaPane.getFormulaText());
                } else if (selectCountSelectIndex == SelectCount.BOTTOM) {
                    selectCount.setFormulaCount(this.bottomFormulaPane.getFormulaText());
                } else if (selectCountSelectIndex == SelectCount.SPECIFY) {
                    selectCount.setSerial(this.serialTextField.getText());
                }
            }
        }

        private JFormattedTextField getTextField(JSpinner spinner) {
            JComponent editor = spinner.getEditor();
            if (editor instanceof JSpinner.DefaultEditor) {
                return ((JSpinner.DefaultEditor) editor).getTextField();
            } else {
                System.err.println("Unexpected editor type: "
                    + spinner.getEditor().getClass()
                    + " isn't a descendant of DefaultEditor");
                return null;
            }
        }
    }
    
    private static class JFormulaField extends JPanel {
        private CellElement cellElement;
    	private UITextField formulaTextField;
    	private String defaultValue;
    	
    	public JFormulaField(String defaultValue) {
    		this.defaultValue = defaultValue;
    		
    		this.setLayout(FRGUIPaneFactory.createBoxFlowLayout());
            UILabel bottomLabel = new UILabel("=");
            bottomLabel.setFont(new Font("Dialog", Font.BOLD, 12));
            this.add(bottomLabel);
            formulaTextField = new UITextField(24);
            this.add(formulaTextField);
            formulaTextField.setText(defaultValue);

            UIButton bottomFrmulaButton = new UIButton("...");
            this.add(bottomFrmulaButton);
            bottomFrmulaButton.setToolTipText(Inter.getLocText("Formula") + "...");
            bottomFrmulaButton.setPreferredSize(new Dimension(25, formulaTextField.getPreferredSize().height));
            bottomFrmulaButton.addActionListener(formulaButtonActionListener);
    	}
    	
    	public void populate(String formulaContent) {
    		this.formulaTextField.setText(formulaContent);
    	}
    	public void populateElement(CellElement cellElement) {
    		this.cellElement = cellElement;
    	}
    	
    	public String getFormulaText() {
    		return this.formulaTextField.getText();
    	}
    	
    	private ActionListener formulaButtonActionListener = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                Formula valueFormula = new Formula();
                String text = formulaTextField.getText();
                if (text == null || text.length() <= 0) {
                    valueFormula.setContent(defaultValue);
                } else {
                    valueFormula.setContent(text);
                }

                final UIFormula formulaPane = FormulaFactory.createFormulaPaneWhenReserveFormula();

                if (cellElement == null) {
                    return;
                }
                Object value = cellElement.getValue();
                if (value == null || !(value instanceof DSColumn)) {
                    return;
                }
                DSColumn dsColumn = (DSColumn) value;

                String[] displayNames = DesignTableDataManager.getSelectedColumnNames(DesignTableDataManager.getEditingTableDataSource(), dsColumn.getDSName());

                formulaPane.populate(valueFormula, new CustomVariableResolver(displayNames, true));
                formulaPane.showLargeWindow(SwingUtilities.getWindowAncestor(JFormulaField.this), new DialogActionAdapter() {
                    @Override
					public void doOk() {
                        Formula valueFormula = formulaPane.update();
                        if (valueFormula.getContent().length() <= 1) {
                            formulaTextField.setText(defaultValue);
                        } else {
                            formulaTextField.setText(valueFormula.getContent().substring(1));
                        }
                    }
                }).setVisible(true);
            }
        };
    }

    private static class ValuePane extends JPanel {
        private JFormulaField formulaField;

        public ValuePane() {
            this.setLayout(FRGUIPaneFactory.createBoxFlowLayout());

            this.add(new UILabel(InsetText + Inter.getLocText("Value") + ":"));
            this.add(Box.createHorizontalStrut(2));
            this.add((formulaField = new JFormulaField("$$$")));
        }

        public void populate(CellElement cellElement) {
            if (cellElement == null) {
                return;
            }

            Object value = cellElement.getValue();
            if (value == null || !(value instanceof DSColumn)) {
                return;
            }
            DSColumn dSColumn = (DSColumn) value;

            //formula
            String valueFormula = dSColumn.getResult();
            if (valueFormula == null) {
                valueFormula = "$$$";
            }
            formulaField.populateElement(cellElement);
            formulaField.populate(valueFormula);
        }

        public void update(CellElement cellElement) {
            if (cellElement == null) {
                return;
            }
            Object value = cellElement.getValue();
            if (value == null || !(value instanceof DSColumn)) {
                return;
            }
            DSColumn dSColumn = (DSColumn) (cellElement.getValue());

            //formula
            dSColumn.setResult(this.formulaField.getFormulaText());
        }
    }

    private void checkButtonEnabled() {
        if (useMultiplyNumCheckBox.isSelected()) {
            multiNumSpinner.setEnabled(true);
        } else {
            multiNumSpinner.setEnabled(false);
        }
    }
}