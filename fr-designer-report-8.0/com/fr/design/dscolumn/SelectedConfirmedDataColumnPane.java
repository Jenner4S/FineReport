// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.dscolumn;

import com.fr.data.TableDataSource;
import com.fr.design.data.datapane.TableDataComboBox;
import com.fr.design.data.tabledata.wrapper.TemplateTableDataWrapper;
import com.fr.design.gui.icombobox.LazyComboBox;
import com.fr.main.impl.WorkBook;
import com.fr.report.cell.TemplateCellElement;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;
import javax.swing.ComboBoxModel;

// Referenced classes of package com.fr.design.dscolumn:
//            SelectedDataColumnPane

public class SelectedConfirmedDataColumnPane extends SelectedDataColumnPane
{

    public SelectedConfirmedDataColumnPane()
    {
        super(false);
    }

    protected void initTableNameComboBox()
    {
        tableNameComboBox = new TableDataComboBox(new WorkBook());
        tableNameComboBox.addItemListener(new ItemListener() {

            final SelectedConfirmedDataColumnPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                columnNameComboBox.setLoaded(false);
            }

            
            {
                this$0 = SelectedConfirmedDataColumnPane.this;
                super();
            }
        }
);
        tableNameComboBox.setPreferredSize(new Dimension(100, 20));
    }

    public void populate(TableDataSource tabledatasource, TemplateCellElement templatecellelement)
    {
        tableNameComboBox.refresh(tabledatasource);
        tableNameComboBox.setEditable(false);
        tableNameComboBox.setEnabled(false);
        super.populate(tabledatasource, templatecellelement);
        try
        {
            Iterator iterator = tabledatasource.getTableDataNameIterator();
            String s = (String)iterator.next();
            TemplateTableDataWrapper templatetabledatawrapper = new TemplateTableDataWrapper(tabledatasource.getTableData(s), s);
            tableNameComboBox.setSelectedItem(templatetabledatawrapper);
            tableNameComboBox.getModel().setSelectedItem(templatetabledatawrapper);
        }
        catch(Exception exception) { }
    }
}
