// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report.freeze;

import com.fr.design.gui.itextfield.UINumberField;
import java.awt.Toolkit;
import javax.swing.text.*;

public class UIIntNumberField extends UINumberField
{
    class NumberDocument extends PlainDocument
    {

        final UIIntNumberField this$0;

        public void insertString(int i, String s, AttributeSet attributeset)
            throws BadLocationException
        {
            String s1 = getText(0, getLength());
            if(!s.matches("^[0-9]+$"))
            {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            String s2 = (new StringBuilder()).append(s1.substring(0, i)).append(s).append(s1.substring(i, getLength())).toString();
            if(isOverMaxOrMinValue(s2))
            {
                Toolkit.getDefaultToolkit().beep();
                return;
            } else
            {
                setisContentChanged(true);
                super.insertString(i, s, attributeset);
                return;
            }
        }

        private boolean isOverMaxOrMinValue(String s)
        {
            return Double.parseDouble(s) < getMinValue() || Double.parseDouble(s) > getMaxValue();
        }

        public NumberDocument()
        {
            this$0 = UIIntNumberField.this;
            super();
        }
    }


    public UIIntNumberField()
    {
    }

    public void setFieldDocument()
    {
        setDocument(new NumberDocument());
    }
}
