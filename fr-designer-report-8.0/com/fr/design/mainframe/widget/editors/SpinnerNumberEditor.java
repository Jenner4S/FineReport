// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.editors;

import com.fr.design.Exception.ValidationException;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.form.ui.NumberEditor;
import java.awt.Component;
import javax.swing.SpinnerNumberModel;

// Referenced classes of package com.fr.design.mainframe.widget.editors:
//            AbstractPropertyEditor

public class SpinnerNumberEditor extends AbstractPropertyEditor
{

    protected NumberEditor widget;
    protected UIBasicSpinner spinner;
    protected SpinnerNumberModel valueModel;

    public SpinnerNumberEditor(Object obj)
    {
        widget = (NumberEditor)obj;
        valueModel = new SpinnerNumberModel(0.0D, -1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.0D);
        spinner = new UIBasicSpinner(valueModel);
        spinner.registerChangeListener(new UIObserverListener() {

            final SpinnerNumberEditor this$0;

            public void doChange()
            {
                firePropertyChanged();
            }

            
            {
                this$0 = SpinnerNumberEditor.this;
                super();
            }
        }
);
    }

    protected Double getDefaultLimit()
    {
        return Double.valueOf(1.7976931348623157E+308D);
    }

    protected Double getLimitValue()
    {
        return Double.valueOf(widget.getMaxValue());
    }

    public void validateValue()
        throws ValidationException
    {
    }

    public void setValue(Object obj)
    {
        spinner.setValue(obj);
    }

    public Object getValue()
    {
        return spinner.getValue();
    }

    public Component getCustomEditor()
    {
        return spinner;
    }
}
