// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data;

import com.fr.base.present.*;
import com.fr.data.Dictionary;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.present.FormulaPresentPane;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.general.Inter;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class PresentComboBox extends UIComboBox
{

    private Present present;
    private String ITEMS[] = {
        Inter.getLocText("DS-Dictionary"), Inter.getLocText("Present-Formula_Present"), Inter.getLocText("Present-No_Present")
    };

    public PresentComboBox()
    {
        addItem(ITEMS[0]);
        addItem(ITEMS[1]);
        addItem(ITEMS[2]);
        setSelectedItem(ITEMS[2]);
        addPopupMenuListener(new PopupMenuListener() {

            final PresentComboBox this$0;

            public void popupMenuWillBecomeVisible(PopupMenuEvent popupmenuevent)
            {
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent popupmenuevent)
            {
                if(getSelectedIndex() == 0)
                {
                    final DictionaryPane pp = new DictionaryPane();
                    if(present instanceof DictPresent)
                        pp.populateBean(((DictPresent)present).getDictionary());
                    BasicDialog basicdialog = pp.showWindow(SwingUtilities.getWindowAncestor(new JPanel()), new DialogActionAdapter() {

                        final DictionaryPane val$pp;
                        final _cls1 this$1;

                        public void doOk()
                        {
                            populate(new DictPresent((Dictionary)pp.updateBean()));
                            fireChange();
                        }

                    
                    {
                        this$1 = _cls1.this;
                        pp = dictionarypane;
                        super();
                    }
                    }
);
                    basicdialog.setVisible(true);
                } else
                if(getSelectedIndex() == 1)
                {
                    final FormulaPresentPane pp = new FormulaPresentPane();
                    if(present instanceof FormulaPresent)
                        pp.populateBean((FormulaPresent)present);
                    BasicDialog basicdialog1 = pp.showSmallWindow(SwingUtilities.getWindowAncestor(new JPanel()), new DialogActionAdapter() {

                        final FormulaPresentPane val$pp;
                        final _cls1 this$1;

                        public void doOk()
                        {
                            populate(pp.updateBean());
                            fireChange();
                        }

                    
                    {
                        this$1 = _cls1.this;
                        pp = formulapresentpane;
                        super();
                    }
                    }
);
                    basicdialog1.setVisible(true);
                } else
                {
                    present = null;
                    fireChange();
                }
            }

            public void popupMenuCanceled(PopupMenuEvent popupmenuevent)
            {
            }

            
            {
                this$0 = PresentComboBox.this;
                super();
            }
        }
);
    }

    protected void fireChange()
    {
    }

    public void populate(Present present1)
    {
        if(present1 instanceof DictPresent)
            setSelectedIndex(0);
        else
        if(present1 instanceof FormulaPresent)
            setSelectedIndex(1);
        else
        if(present1 == null)
            setSelectedIndex(2);
        present = present1;
    }

    public Present update()
    {
        return present;
    }


}
