package com.fr.design.widget.ui.btn;

import com.fr.base.IconManager;
import com.fr.general.Inter;
import com.fr.report.web.button.write.AppendRowButton;
import com.fr.design.widget.btn.ButtonWithHotkeysDetailPane;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-15
 * Time   : ����8:02
 */
public class AppendRowButtonDefinePane<T extends AppendRowButton> extends ButtonWithHotkeysDetailPane<AppendRowButton> {
    private DefineAppendColumnRowPane defineColumnRowPane;

//	@Override
//	protected void initComponents() {
//        super.initComponents();
//        defineColumnRowPane = new DefineAppendColumnRowPane();
//        add(defineColumnRowPane, BorderLayout.SOUTH);
//    }

    @Override
    protected Component createCenterPane() {
        return defineColumnRowPane = new DefineAppendColumnRowPane();
    }

    @Override
    public AppendRowButton createButton() {
        AppendRowButton button = new AppendRowButton();
        button.setText(Inter.getLocText("Utils-Insert_Row"));
        button.setIconName(IconManager.ADD.getName());
        return button;
    }

    @Override
    public void populate(com.fr.form.ui.Button btn) {
        super.populate(btn);
        if (btn instanceof AppendRowButton) {
            defineColumnRowPane.populate((AppendRowButton) btn);
        }
    }

    @Override
    public AppendRowButton update() {
        AppendRowButton btn = super.update();
        defineColumnRowPane.update(btn);
        return btn;
    }

    @Override
    public Class classType() {
        return AppendRowButton.class;
    }
}
