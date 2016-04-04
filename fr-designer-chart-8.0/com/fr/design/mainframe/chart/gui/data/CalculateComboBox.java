// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data;

import com.fr.data.util.function.*;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.general.*;

public class CalculateComboBox extends UIComboBox
{

    public static final String CALCULATE_ARRAY[] = {
        Inter.getLocText("DataFunction-None"), Inter.getLocText("DataFunction-Sum"), Inter.getLocText("DataFunction-Average"), Inter.getLocText("DataFunction-Max"), Inter.getLocText("DataFunction-Min"), Inter.getLocText("DataFunction-Count")
    };
    public static final Class CLASS_ARRAY[] = {
        com/fr/data/util/function/NoneFunction, com/fr/data/util/function/SumFunction, com/fr/data/util/function/AverageFunction, com/fr/data/util/function/MaxFunction, com/fr/data/util/function/MinFunction, com/fr/data/util/function/CountFunction
    };

    public CalculateComboBox()
    {
        super(CALCULATE_ARRAY);
        setSelectedIndex(0);
    }

    public void reset()
    {
        setSelectedItem(Inter.getLocText("DataFunction-None"));
    }

    public void populateBean(AbstractDataFunction abstractdatafunction)
    {
        int i = 0;
        do
        {
            if(i >= CLASS_ARRAY.length)
                break;
            if(abstractdatafunction != null && ComparatorUtils.equals(abstractdatafunction.getClass(), CLASS_ARRAY[i]))
            {
                setSelectedIndex(i);
                break;
            }
            i++;
        } while(true);
    }

    public AbstractDataFunction updateBean()
    {
        try
        {
            int i = getSelectedIndex();
            if(i >= 0 && i < CLASS_ARRAY.length)
                return (AbstractDataFunction)CLASS_ARRAY[i].newInstance();
        }
        catch(InstantiationException instantiationexception)
        {
            FRLogger.getLogger().error("Function Error");
            return null;
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            FRLogger.getLogger().error("Function Error");
            return null;
        }
        return null;
    }

}
