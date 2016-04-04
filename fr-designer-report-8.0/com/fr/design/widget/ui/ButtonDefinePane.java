// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.widget.btn.ButtonDetailPane;
import com.fr.design.widget.ui.btn.ButtonDetailPaneFactory;
import com.fr.form.ui.Button;
import com.fr.form.ui.FreeButton;
import javax.swing.BorderFactory;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Referenced classes of package com.fr.design.widget.ui:
//            AbstractDataModify

public class ButtonDefinePane extends AbstractDataModify
{

    private ButtonDetailPane detailPane;

    public ButtonDefinePane()
    {
        initComponent();
    }

    private void initComponent()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 13, 0, 0));
    }

    protected String title4PopupWindow()
    {
        return "Button";
    }

    private void resetDetailPane(Button button, Class class1)
    {
        if(detailPane != null)
            remove(detailPane);
        detailPane = ButtonDetailPaneFactory.createButtonDetailPane(class1, button);
        add(detailPane, "Center");
        detailPane.addTypeChangeListener(new ChangeListener() {

            final ButtonDefinePane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                resetDetailPane(null, (Class)changeevent.getSource());
            }

            
            {
                this$0 = ButtonDefinePane.this;
                super();
            }
        }
);
        updateUI();
    }

    public void populateBean(Button button)
    {
        resetDetailPane(button, !(button instanceof FreeButton) || ((FreeButton)button).isCustomStyle() ? null : com/fr/form/ui/Button);
    }

    public Button updateBean()
    {
        return detailPane.update();
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((Button)obj);
    }

}
