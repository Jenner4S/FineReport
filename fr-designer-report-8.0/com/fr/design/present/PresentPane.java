// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.present;

import com.fr.base.present.DictPresent;
import com.fr.base.present.FormulaPresent;
import com.fr.base.present.Present;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.fun.PresentKindProvider;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.icombobox.DictionaryComboBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.BarcodePresent;
import com.fr.report.cell.cellattr.CurrencyLinePresent;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.fr.design.present:
//            NonePresentPane, DictPresentPane, BarCodePane, FormulaPresentPane, 
//            CurrencyLinePane

public class PresentPane extends UIComboBoxPane
{

    private DictPresentPane dictPresentPane;
    private List keys;
    private List displays;

    public PresentPane()
    {
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("FR-Designer_Present");
    }

    public void setSelectedByName(String s)
    {
        jcb.setSelectedItem(s);
    }

    public void addTabChangeListener(java.awt.event.ItemListener itemlistener)
    {
        super.addTabChangeListener(itemlistener);
        dictPresentPane.addTabChangeListener(itemlistener);
    }

    public void populateBean(Present present)
    {
        if(present == null)
            dictPresentPane.reset();
        super.populateBean(present);
    }

    protected List initPaneList()
    {
        if(keys == null)
            keys = new ArrayList();
        if(displays == null)
            displays = new ArrayList();
        ArrayList arraylist = new ArrayList();
        NonePresentPane nonepresentpane = new NonePresentPane();
        arraylist.add(nonepresentpane);
        keys.add("NOPRESENT");
        displays.add(nonepresentpane.title4PopupWindow());
        arraylist.add(dictPresentPane = new DictPresentPane());
        keys.add(com/fr/base/present/DictPresent.getName());
        displays.add(dictPresentPane.title4PopupWindow());
        BarCodePane barcodepane = new BarCodePane();
        arraylist.add(barcodepane);
        keys.add(com/fr/report/cell/cellattr/BarcodePresent.getName());
        displays.add(barcodepane.title4PopupWindow());
        FormulaPresentPane formulapresentpane = new FormulaPresentPane();
        arraylist.add(formulapresentpane);
        keys.add(com/fr/base/present/FormulaPresent.getName());
        displays.add(formulapresentpane.title4PopupWindow());
        CurrencyLinePane currencylinepane = new CurrencyLinePane();
        arraylist.add(currencylinepane);
        keys.add(com/fr/report/cell/cellattr/CurrencyLinePresent.getName());
        displays.add(currencylinepane.title4PopupWindow());
        PresentKindProvider apresentkindprovider[] = ExtraDesignClassManager.getInstance().getPresentKindProviders();
        PresentKindProvider apresentkindprovider1[] = apresentkindprovider;
        int i = apresentkindprovider1.length;
        for(int j = 0; j < i; j++)
        {
            PresentKindProvider presentkindprovider = apresentkindprovider1[j];
            FurtherBasicBeanPane furtherbasicbeanpane = presentkindprovider.appearanceForPresent();
            arraylist.add(furtherbasicbeanpane);
            keys.add(presentkindprovider.kindOfPresent().getName());
            displays.add(furtherbasicbeanpane.title4PopupWindow());
        }

        return arraylist;
    }

    protected UIComboBox createComboBox()
    {
        return new DictionaryComboBox(keys.toArray(new String[keys.size()]), (String[])displays.toArray(new String[displays.size()]));
    }

    protected void addComboBoxItem(List list, int i)
    {
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((Present)obj);
    }
}
