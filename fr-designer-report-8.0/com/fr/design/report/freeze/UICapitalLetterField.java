// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report.freeze;

import com.fr.design.gui.itextfield.UINumberField;
import com.fr.stable.StableUtils;
import java.awt.Toolkit;
import javax.swing.text.*;

public class UICapitalLetterField extends UINumberField
{
    class NumberDocument extends PlainDocument
    {

        final UICapitalLetterField this$0;

        public void insertString(int i, String s, AttributeSet attributeset)
            throws BadLocationException
        {
            if(s.matches("^[a-z]+$"))
                s = s.toUpperCase();
            if(!s.matches("^[A-Z]+$"))
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

        public NumberDocument()
        {
            this$0 = UICapitalLetterField.this;
            super();
        }
    }


    public UICapitalLetterField()
    {
    }

    public void setValue(double d)
    {
        setText(StableUtils.convertIntToABC((int)d));
    }

    public double getValue()
        throws NumberFormatException
    {
        try
        {
            if(getText().length() == 0)
                return 0.0D;
        }
        catch(NumberFormatException numberformatexception)
        {
            return 1.7976931348623157E+308D;
        }
        return (double)StableUtils.convertABCToInt(getText());
    }

    public void setFieldDocument()
    {
        setDocument(new NumberDocument());
    }
}
