package com.fr.design.formula;

import com.fr.base.BaseUtils;
import com.fr.base.Formula;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 
 * @author zhou
 * @since 2012-6-1下午3:50:37
 */
public class TinyFormulaPane extends BasicBeanPane<String> implements UIObserver{
	
	private static final long serialVersionUID = 1L;
	protected UITextField formulaTextField;
	protected UIButton formulaTextFieldButton;

	public TinyFormulaPane() {
		this.initComponents();
	}

	protected void initComponents() {

		formulaTextField = new UITextField();
		formulaTextField.setGlobalName(Inter.getLocText("ExpandD-Sort_After_Expand"));

		// 添加一公式编辑器按钮
		formulaTextFieldButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/m_insert/formula.png"));
		formulaTextFieldButton.setToolTipText(Inter.getLocText("Formula") + "...");
		formulaTextFieldButton.setPreferredSize(new Dimension(24, 20));
		formulaTextFieldButton.setOpaque(false);
		formulaTextFieldButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		formulaTextFieldButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String text = formulaTextField.getText();

				final UIFormula formulaPane = FormulaFactory.createFormulaPane();
				formulaPane.populate(new Formula(text));
				formulaPane.showLargeWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
					@Override
					public void doOk() {
						Formula fm = formulaPane.update();
						if (fm.getContent().length() <= 1) {
							formulaTextField.setText("$$$");
						} else {
							formulaTextField.setText(fm.getContent());
						}
						okEvent();
					}
				}).setVisible(true);
			}
		});
		initLayout();
	}

	protected void initLayout() {
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = {f };
		double[] rowSize = { p };

        Component[] components1 = new Component[]{
               formulaTextFieldButton
        } ;
        JPanel pane = new JPanel(new BorderLayout(0,0));
        pane.add(formulaTextField,BorderLayout.CENTER);
        pane.add(GUICoreUtils.createFlowPane(components1,FlowLayout.LEFT, LayoutConstants.HGAP_LARGE),BorderLayout.EAST);

        Component[][] components2 = new Component[][]{
               new Component[]{pane}
        };

        JPanel panel= TableLayoutHelper.createTableLayoutPane(components2,rowSize,columnSize) ;
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER) ;
	}
	
	/**
	 * 公式窗口点击确定后的事件接口
	 */
	public void okEvent() {
		
	}
	
	public UITextField getUITextField() {
		return formulaTextField;
	}

	@Override
	public void populateBean(String ob) {
		this.formulaTextField.setText(ob);
	}

	@Override
	public String updateBean() {
		return formulaTextField.getText().trim();
	}

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("Present-Formula_Present");
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(super.getPreferredSize().width, 20);
	}

	@Override
	public void registerChangeListener(UIObserverListener listener) {
		if(formulaTextField != null) {
			formulaTextField.registerChangeListener(listener);
		}
		if(formulaTextFieldButton != null) {
			formulaTextFieldButton.registerChangeListener(listener);
		}
	}

	@Override
	public boolean shouldResponseChangeListener() {
		return true;
	}

}