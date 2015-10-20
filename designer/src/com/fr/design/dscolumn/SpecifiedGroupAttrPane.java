package com.fr.design.dscolumn;

import com.fr.base.Formula;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.DSColumnLiteConditionPane;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.CustomVariableResolver;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.controlpane.ObjectJControlPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ilist.ModNameActionListener;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.cellattr.core.group.ConditionGroup;
import com.fr.report.cell.cellattr.core.group.CustomGrouper;
import com.fr.report.cell.cellattr.core.group.FunctionGrouper;
import com.fr.report.cell.cellattr.core.group.RecordGrouper;
import com.fr.stable.Nameable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * SpecifiedGroupAttrDialog
 */
public class SpecifiedGroupAttrPane extends BasicPane {
	private CardLayout cardLayout;
	private JPanel centerCardPane;
	private JPanel conditionsGroupPane;
	private FormulaGroupPane formulaGroupPane;
	
	private UIComboBox specifiedComboBox;
	private SpecifiedGroupControlPane specifiedControlPane;
	
    //����ѡ��������Ҫ��
    private  String[] displayNames;

    //other pane�Ŀؼ�
    private UICheckBox forceCheckBox;
    private UICheckBox moreCheckBox;
    private UIComboBox otherComboBox;
    private UITextField otherTextField;  
    
    public class SpecifiedGroupControlPane extends ObjectJControlPane {
    	public SpecifiedGroupControlPane(String[] displayNames) {
    		super(displayNames);
    		this.addModNameActionListener(new ModNameActionListener() {
    			public void nameModed(int index, String oldName, String newName) {
    				populateSelectedValue();
    			}
            });
    	}

		@Override
		public NameableCreator[] createNameableCreators() {
			return new NameableCreator[] {
					new NameObjectCreator(Inter.getLocText("Condition"), ConditionGroup.class, ConditionGroupDetailsPane.class)
			};
		}
		
		@Override
		protected String title4PopupWindow() {
			return Inter.getLocText("SpecifiedG-Specified_Group");
		}
    }

    public SpecifiedGroupAttrPane(String[] displayNames) {
    	this.displayNames = displayNames;
        this.initComponents();
    }

    protected void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        specifiedComboBox = new UIComboBox(new String[] {
        		Inter.getLocText(new String[]{"Condition", "Group"}), Inter.getLocText(new String[]{"Formula", "Group"})
        });
        
        specifiedComboBox.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent e) {
        		if (specifiedComboBox.getSelectedIndex() == 0) {
        			cardLayout.show(centerCardPane, "Condition");
        		} else {
        			cardLayout.show(centerCardPane, "Formula");
        		}
        	}
        });
        
        JPanel northPane = GUICoreUtils.createFlowPane(
        		new JComponent[]{new UILabel(Inter.getLocText("Select_Specified_Grouping") + ":"),
        				specifiedComboBox}, FlowLayout.LEFT);
        this.add(northPane, BorderLayout.NORTH);
        
        cardLayout = new CardLayout();
        centerCardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        centerCardPane.setLayout(cardLayout);
        
        // ��������
        conditionsGroupPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        specifiedControlPane = new SpecifiedGroupControlPane(displayNames);

        conditionsGroupPane.add(specifiedControlPane, BorderLayout.CENTER);
        
        JPanel southPane = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
        conditionsGroupPane.add(southPane, BorderLayout.SOUTH);
        forceCheckBox = new UICheckBox(Inter.getLocText("SpecifiedG-Force_Group"));
        moreCheckBox = new UICheckBox(Inter.getLocText("one_record_exists_in_many_groups"));
        southPane.add(forceCheckBox); southPane.add(moreCheckBox); 
        otherComboBox = new UIComboBox(new String[] {Inter.getLocText("SpecifiedG-Discard_all_others"),
        		Inter.getLocText("SpecifiedG-Leave_in_their_own_groups"), Inter.getLocText("SpecifiedG-Put_all_others_together")});
        otherComboBox.addItemListener(otherItemListener);
        UILabel label = new UILabel(Inter.getLocText("OtherGroup_Name") + ":");
        otherTextField = new UITextField(8);
        southPane.add(otherComboBox);
        southPane.add(GUICoreUtils.createFlowPane(new Component[]{label, otherTextField}, FlowLayout.LEFT));
        
        // ��ʽ����
        formulaGroupPane = new FormulaGroupPane();
        
        
        centerCardPane.add("Condition", conditionsGroupPane);
        centerCardPane.add("Formula", formulaGroupPane);
        this.add(centerCardPane, BorderLayout.CENTER);
    }
    
    @Override
    protected String title4PopupWindow() {
    	return Inter.getLocText("SpecifiedG-Specified_Group");
    }

    /**
     * radio Select Action Listener
     */
    ItemListener otherItemListener = new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
            if (otherComboBox.getSelectedIndex() == 2) {
                otherTextField.setEnabled(true);
            } else {
                otherTextField.setEnabled(false);
            }
        }
    };

    /**
     * check Valid
     */
    @Override
	public void checkValid() throws Exception {
    	
    }


	// denny_DS
    public void populate(RecordGrouper grouper) {
    	if(grouper == null){
		    return;
		}
		
        if (grouper instanceof CustomGrouper) {
        	this.specifiedComboBox.setSelectedIndex(0);
        	cardLayout.show(centerCardPane, "Condition");
        	CustomGrouper customGrouper = (CustomGrouper)grouper;
        	boolean force = customGrouper.isForce();
        	ConditionGroup[] conditionGroups = customGrouper.getConditionGroups();
        	boolean more = customGrouper.isMore();
        	int other = customGrouper.getOther();
        	String odisplay = customGrouper.getOtherdisplay();
        	
        	this.forceCheckBox.setSelected(force);
        	
        	this.moreCheckBox.setSelected(more);
        	
        	if (other == CustomGrouper.TOGETHER) {
        		this.otherComboBox.setSelectedIndex(2);
        		this.otherTextField.setEnabled(true);
        	} else if (other == CustomGrouper.DISCARD) {
        		this.otherComboBox.setSelectedIndex(0);
        		this.otherTextField.setEnabled(false);
        	} else {
        		this.otherComboBox.setSelectedIndex(1);
        		this.otherTextField.setEnabled(false);
        	}
        	
        	if (odisplay != null) {
        		this.otherTextField.setText(odisplay);
        	}
        	
        	if (conditionGroups != null){
        		java.util.List<NameObject> list = new ArrayList<NameObject>();
        		for(int i = 0; i < conditionGroups.length; i++) {
        			list.add(new NameObject(conditionGroups[i].getDisplay(), conditionGroups[i]));
        		}
            	
        		specifiedControlPane.populate(list.toArray(new NameObject[list.size()]));
        	}
        } else if (grouper instanceof FunctionGrouper 
        		&& ((FunctionGrouper)grouper).isCustom()) {
        	this.specifiedComboBox.setSelectedIndex(1);
        	cardLayout.show(centerCardPane, "Formula");
        	formulaGroupPane.populate(grouper);
        }
        
        if (otherComboBox.getSelectedIndex() == 2) {
            otherTextField.setEnabled(true);
        } else {
            otherTextField.setEnabled(false);
        }
    }

    public RecordGrouper update(CellElement cellElement, RecordGrouper recordGrouper) {
    	if (this.specifiedComboBox.getSelectedIndex() == 0) {
    		CustomGrouper customGroup = new CustomGrouper();
            
            if (forceCheckBox.isSelected()) {
            	customGroup.setForce(true);
            }
            
            if (!moreCheckBox.isSelected()) {
            	customGroup.setMore(false);
            }
            
            if (otherComboBox.getSelectedIndex() == 2) {
            	customGroup.setOther(CustomGrouper.TOGETHER);
            } else if (otherComboBox.getSelectedIndex() == 0) {
            	customGroup.setOther(CustomGrouper.DISCARD);
            } else {
            	customGroup.setOther(CustomGrouper.LEAVE);
            }
            
            customGroup.setOdisplay(this.otherTextField.getText());
           	
            // Nameable[]��Ȼ����ǿת��NameObject[],һ��Ҫ��ôд...
        	Nameable[] res = specifiedControlPane.update();
        	NameObject[] nameObject_array = new NameObject[res.length];
    		java.util.Arrays.asList(res).toArray(nameObject_array);
    		ConditionGroup[] res_array = new ConditionGroup[res.length];
    		for (int i = 0; i < res.length; i ++) {
    			res_array[i] = (ConditionGroup)nameObject_array[i].getObject();
    			res_array[i].setDisplay(nameObject_array[i].getName());
    		}

    		customGroup.setConditionGroups(res_array);
    		recordGrouper =  customGroup;
    	} else {    		
    		recordGrouper = formulaGroupPane.update();
    	}
    	
    	return recordGrouper;
    }
    
    public static class ConditionGroupDetailsPane extends BasicBeanPane<ConditionGroup> {
    	private ConditionGroup editing;
    	
    	private DSColumnLiteConditionPane liteConditionPane;
    	public ConditionGroupDetailsPane(String[] displayNames) {
    		//alex:�Ҳ�����ڵ�����ϸ��
            this.setLayout(FRGUIPaneFactory.createBorderLayout());
            this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

            liteConditionPane = new DSColumnLiteConditionPane();
            this.add(liteConditionPane, BorderLayout.CENTER);
            if (displayNames != null) {
                liteConditionPane.populateColumns(displayNames);
            }
    	}
    	
    	@Override
    	protected String title4PopupWindow() {
    		return Inter.getLocText("SpecifiedG-Specified_Group");
    	}

        @Override
    	public ConditionGroup updateBean() {
        	editing.setCondition(liteConditionPane.updateBean());
        	
        	return editing;
    	}

        @Override
    	public void populateBean(ConditionGroup ob) {
        	editing = ob;
    		liteConditionPane.populateBean(ob.getCondition());
    	}
    }
    
    private class FormulaGroupPane extends JPanel {

	    private String[] displayModeNames = {Inter.getLocText("GROUPING_MODE"), Inter.getLocText("LIST_MODE"), 
	    		Inter.getLocText("CONTINUUM_MODE")};
	    
	    private String InsertText = "    ";
	    
	    private UIComboBox modeComboBox;
    	private UITextField valueField;
    	private JPanel southPane;
    	
    	public FormulaGroupPane() {
    		this.setBorder(BorderFactory.createTitledBorder(Inter.getLocText("D-Dispaly_Divide_Result_Set_into_Groups")));
    		this.setLayout(FRGUIPaneFactory.createM_BorderLayout());
    		JPanel contentPane = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
    		this.add(contentPane, BorderLayout.NORTH);
    		
    		JPanel northPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
    		northPane.setLayout(FRGUIPaneFactory.createBorderLayout());
    		contentPane.add(northPane);
    		modeComboBox = new UIComboBox(displayModeNames);

    		northPane.add(GUICoreUtils.createFlowPane(new JComponent[] { new UILabel(InsertText), new UILabel(Inter.getLocText("Display_Modes") + ":  "),
    				modeComboBox }, FlowLayout.LEFT), BorderLayout.WEST);
    		
    		UILabel label = new UILabel("=");
            label.setFont(new Font("Dialog", Font.BOLD, 12));
            valueField = new UITextField(16);
            valueField.setText("$$$");

            UIButton formulaButton = new UIButton("...");
            formulaButton.setToolTipText(Inter.getLocText("Formula") + "...");
            formulaButton.setPreferredSize(new Dimension(25, valueField.getPreferredSize().height));
            formulaButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                	Formula valueFormula = new Formula();
                    String text = valueField.getText();
                    if (text == null || text.length() <= 0) {
                        valueFormula.setContent("$$$");
                    } else {
                        valueFormula.setContent(text);
                    }

                    final UIFormula formulaPane = FormulaFactory.createFormulaPane();
            	    
            	    formulaPane.populate(valueFormula, new CustomVariableResolver(displayNames == null? new String[0] : displayNames, true));
    				formulaPane.showLargeWindow(SwingUtilities.getWindowAncestor(FormulaGroupPane.this), new DialogActionAdapter(){
    					@Override
						public void doOk() {
    						Formula valueFormula = formulaPane.update();
                            if (valueFormula.getContent().length() <= 1) {
                                valueField.setText("$$$");
                            } else {
                                valueField.setText(valueFormula.getContent().substring(1));
                            }
    					}
    				}).setVisible(true);
                }
            });
            
            southPane = GUICoreUtils.createFlowPane(new JComponent[] {new UILabel(InsertText), //new UILabel(Inter.getLocText("I-Message_FunctionGrouper_2")),
            		new UILabel(Inter.getLocText(new String[]{"Custom", "Value"}) + ": "), label, valueField, formulaButton}, FlowLayout.LEFT);
            contentPane.add(southPane);
    	}
    	
    	// populate
    	public void populate(RecordGrouper grouper) {    		
    		if (grouper instanceof FunctionGrouper) {
    			int mode = ((FunctionGrouper)grouper).getDivideMode();
    			if (mode == FunctionGrouper.GROUPING_MODE) {
    				this.modeComboBox.setSelectedIndex(0);
    			} else if (mode == FunctionGrouper.LIST_MODE) {
    				this.modeComboBox.setSelectedIndex(1);
    			} else {
    				this.modeComboBox.setSelectedIndex(2);
    			}
    			String formulaContent = ((FunctionGrouper)grouper).getFormulaContent();
    			if(formulaContent == null) {
    				this.valueField.setText("$$$");
    			} else {
    				this.valueField.setText(formulaContent);
    			}
    		}
    	}
    	
    	public RecordGrouper update() {
    		FunctionGrouper grouper = new FunctionGrouper();
    		grouper.setCustom(true);
    		if (this.modeComboBox.getSelectedIndex() == 0) {
    			grouper.setDivideMode(FunctionGrouper.GROUPING_MODE);
    		} else if (this.modeComboBox.getSelectedIndex() == 1) {
    			grouper.setDivideMode(FunctionGrouper.LIST_MODE);
    		} else if (this.modeComboBox.getSelectedIndex() == 2) {
    			grouper.setDivideMode(FunctionGrouper.CONTINUUM_MODE);
    		}
    		grouper.setFormulaContent(this.valueField.getText());
    		
    		return grouper;
    	}
    }
}
