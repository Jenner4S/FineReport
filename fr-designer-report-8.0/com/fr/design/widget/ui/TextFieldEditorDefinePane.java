// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.design.gui.frpane.RegPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.FieldEditor;
import com.fr.form.ui.TextEditor;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.widget.ui:
//            FieldEditorDefinePane, WaterMarkDictPane

public class TextFieldEditorDefinePane extends FieldEditorDefinePane
{

    protected RegPane regPane;
    private WaterMarkDictPane waterMarkDictPane;

    public TextFieldEditorDefinePane()
    {
        initComponents();
    }

    protected JPanel setFirstContentPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
        JPanel jpanel1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel1.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
        jpanel.add(jpanel1, "North");
        regPane = createRegPane();
        final com.fr.design.gui.frpane.RegPane.RegChangeListener rl = new com.fr.design.gui.frpane.RegPane.RegChangeListener() {

            final TextFieldEditorDefinePane this$0;

            public void regChangeAction()
            {
                waterMarkDictPane.setWaterMark("");
                regPane.removeRegChangeListener(this);
            }

            
            {
                this$0 = TextFieldEditorDefinePane.this;
                super();
            }
        }
;
        final com.fr.design.gui.frpane.RegPane.PhoneRegListener pl = new com.fr.design.gui.frpane.RegPane.PhoneRegListener() {

            final com.fr.design.gui.frpane.RegPane.RegChangeListener val$rl;
            final TextFieldEditorDefinePane this$0;

            public void phoneRegChangeAction(com.fr.design.gui.frpane.RegPane.PhoneRegEvent phoneregevent)
            {
                if(StringUtils.isNotEmpty(phoneregevent.getPhoneRegString()) && StringUtils.isEmpty(waterMarkDictPane.getWaterMark()))
                {
                    waterMarkDictPane.setWaterMark((new StringBuilder()).append(Inter.getLocText("Example")).append(":").append(phoneregevent.getPhoneRegString()).toString());
                    regPane.addRegChangeListener(rl);
                }
            }

            
            {
                this$0 = TextFieldEditorDefinePane.this;
                rl = regchangelistener;
                super();
            }
        }
;
        regPane.addPhoneRegListener(pl);
        waterMarkDictPane = new WaterMarkDictPane();
        waterMarkDictPane.addInputKeyListener(new KeyAdapter() {

            final com.fr.design.gui.frpane.RegPane.PhoneRegListener val$pl;
            final com.fr.design.gui.frpane.RegPane.RegChangeListener val$rl;
            final TextFieldEditorDefinePane this$0;

            public void keyTyped(KeyEvent keyevent)
            {
                regPane.removePhoneRegListener(pl);
                regPane.removeRegChangeListener(rl);
                waterMarkDictPane.removeInputKeyListener(this);
            }

            
            {
                this$0 = TextFieldEditorDefinePane.this;
                pl = phonereglistener;
                rl = regchangelistener;
                super();
            }
        }
);
        jpanel1.add(regPane, "North");
        jpanel1.add(waterMarkDictPane, "Center");
        return jpanel;
    }

    protected RegPane createRegPane()
    {
        return new RegPane();
    }

    protected String title4PopupWindow()
    {
        return "text";
    }

    protected void populateSubFieldEditorBean(TextEditor texteditor)
    {
        regPane.populate(texteditor.getRegex());
        waterMarkDictPane.populate(texteditor);
    }

    protected TextEditor updateSubFieldEditorBean()
    {
        TextEditor texteditor = newTextEditorInstance();
        texteditor.setRegex(regPane.update());
        waterMarkDictPane.update(texteditor);
        return texteditor;
    }

    protected TextEditor newTextEditorInstance()
    {
        return new TextEditor();
    }

    protected volatile FieldEditor updateSubFieldEditorBean()
    {
        return updateSubFieldEditorBean();
    }

    protected volatile void populateSubFieldEditorBean(FieldEditor fieldeditor)
    {
        populateSubFieldEditorBean((TextEditor)fieldeditor);
    }

}
