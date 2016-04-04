// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report;

import com.fr.data.ClassVerifyJob;
import com.fr.design.gui.frpane.ObjectProperiesPane;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.write.submit.CustomJobPane;

public class CustomVerifyJobPane extends CustomJobPane
{

    public CustomVerifyJobPane()
    {
    }

    public ClassVerifyJob updateBean()
    {
        ClassVerifyJob classverifyjob = new ClassVerifyJob(classNameTextField.getText());
        classverifyjob.setPropertyMap(objectProperiesPane.updateBean());
        checkAddButtonEnable();
        return classverifyjob;
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }
}
