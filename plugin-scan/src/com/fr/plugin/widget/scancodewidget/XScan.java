package com.fr.plugin.widget.scancodewidget;

import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWScaleLayout;
import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.Widget;
import com.fr.plugin.ExtraClassManager;
import com.fr.stable.fun.FunctionProcessor;

import javax.swing.*;
import java.awt.*;

/**
 * Created by joyxu on 2016/3/7.
 */
public class XScan extends XWidgetCreator {
    XWidgetCreator.LimpidButton btn;

    public XScan(Widget var1, Dimension var2) {
        super(var1, var2);
        FunctionProcessor processor= ExtraClassManager.getInstance().getFunctionProcessor();
        if(processor!=null){
            processor.recordFunction(ScanImpl.ONEFUNCTION);
        }
    }


    protected JComponent initEditor() {
        if(this.editor == null) {
            this.editor = FRGUIPaneFactory.createBorderLayout_S_Pane();
            this.editor.setLocation(300,300);
            UITextField var1 = new UITextField(5);
            var1.setOpaque(false);
            this.editor.add(var1, "Center");
            this.btn = new LimpidButton( "", this.getIconPath(), this.toData().isVisible()?FULL_OPACITY:HALF_OPACITY);
            this.btn.setPreferredSize(new Dimension(21, 21));
            this.btn.setOpaque(true);
            this.editor.add(this.btn, "East");
            this.editor.setBackground(Color.WHITE);
        }

        return this.editor;
    }

    public String getIconPath(){
        return "/com/fr/plugin/widget/scancodewidget/images/scancodeIcon.png";
    }

    protected void makeVisible(boolean var1) {
        this.btn.makeVisible(var1);
    }

    protected XLayoutContainer getCreatorWrapper(String var1) {
        return new XWScaleLayout();
    }

    protected void addToWrapper(XLayoutContainer var1, int var2, int var3) {
        this.setSize(var2, var3);
        var1.add(this);
    }

    public boolean shouldScaleCreator() {
        return true;
    }
}