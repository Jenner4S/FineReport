// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style;

import com.fr.chart.chartattr.CategoryAxis;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.util.ArrayList;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style:
//            DateAxisValuePane

public class ChartAxisValueTypePane extends BasicPane
{
    private class TextAxisValueTypePane extends FurtherBasicBeanPane
    {

        final ChartAxisValueTypePane this$0;

        public boolean accept(Object obj)
        {
            return obj instanceof CategoryAxis;
        }

        public void reset()
        {
        }

        public String title4PopupWindow()
        {
            return Inter.getLocText("Chart_Text_Axis");
        }

        public void populateBean(CategoryAxis categoryaxis)
        {
        }

        public CategoryAxis updateBean()
        {
            return null;
        }

        public void updateBean(CategoryAxis categoryaxis)
        {
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((CategoryAxis)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((CategoryAxis)obj);
        }

        private TextAxisValueTypePane()
        {
            this$0 = ChartAxisValueTypePane.this;
            super();
        }

    }


    private UIComboBoxPane boxPane;
    private DateAxisValuePane dateAxisPane;
    private TextAxisValueTypePane textAxisPane;

    public ChartAxisValueTypePane()
    {
        setLayout(new BorderLayout());
        boxPane = new UIComboBoxPane() {

            final ChartAxisValueTypePane this$0;

            protected java.util.List initPaneList()
            {
                ArrayList arraylist = new ArrayList();
                arraylist.add(dateAxisPane = new DateAxisValuePane());
                arraylist.add(textAxisPane = new TextAxisValueTypePane());
                return arraylist;
            }

            protected String title4PopupWindow()
            {
                return "";
            }

            
            {
                this$0 = ChartAxisValueTypePane.this;
                super();
            }
        }
;
        add(boxPane, "North");
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("AxisValue");
    }

    public void populateBean(CategoryAxis categoryaxis)
    {
        if(categoryaxis != null && categoryaxis.isDate())
        {
            boxPane.setSelectedIndex(0);
            dateAxisPane.populateBean(categoryaxis);
        } else
        {
            boxPane.setSelectedIndex(1);
        }
    }

    public void updateBean(CategoryAxis categoryaxis)
    {
        if(boxPane.getSelectedIndex() == 0)
        {
            dateAxisPane.updateBean(categoryaxis);
            categoryaxis.setDate(true);
        } else
        {
            categoryaxis.setDate(false);
            textAxisPane.updateBean(categoryaxis);
        }
    }

    public void removeTextAxisPane()
    {
        if(boxPane.getUIComboBox().getItemCount() > 1)
        {
            boxPane.getUIComboBox().removeItemAt(1);
            boxPane.getCards().remove(1);
        }
    }


}
