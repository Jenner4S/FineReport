package com.fr.design.widget.btn;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.widget.IconDefinePane;
import com.fr.form.ui.Button;
import com.fr.general.Inter;
import com.fr.stable.StableUtils;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-15
 * Time   : 下午6:22
 */
public abstract class ButtonWithHotkeysDetailPane<T extends Button> extends ButtonDetailPane<T> {
    private UITextField hotkeysTextField;
    private UITextField buttonNameTextField;
    private IconDefinePane iconPane;

    public ButtonWithHotkeysDetailPane() {
        initComponents();
    }

	private void initComponents() {
		this.setLayout(new BorderLayout());
		double p = TableLayout.PREFERRED;
        double rowSize[] = {p, p, p, p};
        double columnSize[] = {p, p};
		JPanel labelPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		iconPane = new IconDefinePane();
        labelPane.add(iconPane);
    	Component[][] n_components = {
        		{new UILabel(Inter.getLocText("FR-Designer_Button-Name") + ":"), buttonNameTextField = new UITextField(20)},
        		{new UILabel(Inter.getLocText("FR-Designer_Button-Icon") + ":"), labelPane},
        		{new UILabel(Inter.getLocText("FR-Designer_Button-Type") + ":"), createButtonTypeComboBox()},
        		{new UILabel(Inter.getLocText("FR-Designer_Button-Hotkeys") + ":"), hotkeysTextField = new UITextField(40)}
        };
    	hotkeysTextField.setToolTipText(StableUtils.join(ButtonConstants.HOTKEYS, ","));
		JPanel panel = TableLayoutHelper.createGapTableLayoutPane(n_components, rowSize, columnSize, 0, 4);
		add(panel,BorderLayout.NORTH);
		Component comp = createCenterPane();
		if(comp != null	) {
			add(comp,BorderLayout.CENTER);
		}
	}

    protected abstract Component createCenterPane();

	@Override
	public void populate(Button button) {
        if (button == null) {
            return;
        }
        iconPane.populate(button.getIconName());
        buttonNameTextField.setText(button.getText());
        hotkeysTextField.setText(button.getHotkeys());
    }

    @Override
	public T update() {
        T button = createButton();
        button.setIconName(iconPane.update());
        button.setText(buttonNameTextField.getText());
        button.setHotkeys(hotkeysTextField.getText());
        return button;
    }
}