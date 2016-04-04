// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.dscolumn;

import com.fr.base.Parameter;
import com.fr.data.SimpleDSColumn;
import com.fr.data.TableDataSource;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.datapane.TableDataComboBox;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.LazyComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.design.gui.itableeditorpane.UITableEditorPane;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.Inter;
import com.fr.general.data.TableDataColumn;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.core.group.DSColumn;
import com.fr.stable.StringUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectedDataColumnPane extends BasicPane
{

    protected UITableEditorPane editorPane;
    protected Parameter ps[];
    protected TableDataComboBox tableNameComboBox;
    protected LazyComboBox columnNameComboBox;
    private ItemListener itemListener;
    private UIButton paramButton;

    public SelectedDataColumnPane()
    {
        this(true);
    }

    public SelectedDataColumnPane(boolean flag)
    {
        initComponent(flag);
    }

    public void initComponent(boolean flag)
    {
        initTableNameComboBox();
        if(flag)
            initWithParameterButton();
        columnNameComboBox = new LazyComboBox() {

            final SelectedDataColumnPane this$0;

            public Object[] load()
            {
                java.util.List list = calculateColumnNameList();
                return list.toArray(new String[list.size()]);
            }

            
            {
                this$0 = SelectedDataColumnPane.this;
                super();
            }
        }
;
        columnNameComboBox.setEditable(true);
        double d = -2D;
        UILabel uilabel = new UILabel((new StringBuilder()).append(Inter.getLocText("TableData")).append(":").toString());
        UILabel uilabel1 = new UILabel((new StringBuilder()).append(Inter.getLocText("DataColumn")).append(":").toString());
        if(flag)
        {
            uilabel.setPreferredSize(new Dimension(200, 25));
            uilabel1.setPreferredSize(new Dimension(200, 25));
        }
        if(flag)
        {
            Component acomponent[][] = {
                {
                    uilabel, null, uilabel1
                }, {
                    tableNameComboBox, paramButton, columnNameComboBox
                }
            };
            add(TableLayoutHelper.createTableLayoutPane(acomponent, new double[] {
                d, d
            }, new double[] {
                d, d, d
            }));
        } else
        {
            double d1 = -1D;
            double ad[] = {
                d, d1
            };
            double ad1[] = {
                d, d
            };
            Component acomponent1[][] = {
                {
                    uilabel, tableNameComboBox
                }, {
                    uilabel1, columnNameComboBox
                }
            };
            javax.swing.JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent1, ad1, ad);
            setLayout(new BorderLayout());
            add(jpanel, "Center");
        }
    }

    protected void initTableNameComboBox()
    {
        tableNameComboBox = new TableDataComboBox(DesignTableDataManager.getEditingTableDataSource());
        tableNameComboBox.addItemListener(new ItemListener() {

            final SelectedDataColumnPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                columnNameComboBox.setLoaded(false);
            }

            
            {
                this$0 = SelectedDataColumnPane.this;
                super();
            }
        }
);
        tableNameComboBox.setPreferredSize(new Dimension(100, 20));
    }

    private void initWithParameterButton()
    {
        editorPane = new UITableEditorPane(new ParameterTableModel());
        paramButton = new UIButton(Inter.getLocText("TableData_Dynamic_Parameter_Setting"));
        paramButton.addActionListener(new ActionListener() {

            final SelectedDataColumnPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                BasicDialog basicdialog = editorPane.showSmallWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

                    final _cls3 this$1;

                    public void doOk()
                    {
                        java.util.List list = editorPane.update();
                        ps = (Parameter[])list.toArray(new Parameter[list.size()]);
                    }

                    
                    {
                        this$1 = _cls3.this;
                        super();
                    }
                }
);
                editorPane.populate(ps != null ? ((Object []) (ps)) : ((Object []) (new Parameter[0])));
                basicdialog.setVisible(true);
            }

            
            {
                this$0 = SelectedDataColumnPane.this;
                super();
            }
        }
);
    }

    protected String title4PopupWindow()
    {
        return "DSColumn";
    }

    public void populate(TableDataSource tabledatasource, TemplateCellElement templatecellelement)
    {
        if(templatecellelement == null)
            return;
        if(itemListener != null)
            removeListener(itemListener);
        Object obj = templatecellelement.getValue();
        if(!(obj instanceof DSColumn))
        {
            return;
        } else
        {
            DSColumn dscolumn = (DSColumn)obj;
            String s = dscolumn.getDSName();
            tableNameComboBox.setSelectedTableDataByName(s);
            columnNameComboBox.setSelectedItem(TableDataColumn.getColumnName(dscolumn.getColumn()));
            ps = dscolumn.getParameters();
            addListener(itemListener);
            return;
        }
    }

    public void update(CellElement cellelement)
    {
        if(cellelement == null)
            return;
        Object obj = cellelement.getValue();
        if(tableNameComboBox.getSelectedItem() == null && columnNameComboBox.getSelectedItem() == null)
            return;
        DSColumn dscolumn = null;
        if(obj == null || !(obj instanceof DSColumn))
        {
            dscolumn = new DSColumn();
            cellelement.setValue(dscolumn);
        }
        dscolumn = (DSColumn)cellelement.getValue();
        SimpleDSColumn simpledscolumn = updateColumnPane();
        dscolumn.setDSName(simpledscolumn.getDsName());
        dscolumn.setColumn(simpledscolumn.getColumn());
        dscolumn.setParameters(ps == null || ps.length <= 0 ? null : ps);
    }

    public SimpleDSColumn updateColumnPane()
    {
        SimpleDSColumn simpledscolumn = new SimpleDSColumn();
        TableDataWrapper tabledatawrapper = tableNameComboBox.getSelectedItem();
        if(tabledatawrapper == null)
            return null;
        simpledscolumn.setDsName(tabledatawrapper.getTableDataName());
        String s = (String)columnNameComboBox.getSelectedItem();
        TableDataColumn tabledatacolumn;
        if(isColumnName(s))
        {
            String s1 = s.substring(1);
            Pattern pattern = Pattern.compile("[^\\d]");
            if(pattern.matcher(s1).find())
            {
                tabledatacolumn = TableDataColumn.createColumn(s);
            } else
            {
                int i = Integer.parseInt(s.substring(1));
                tabledatacolumn = TableDataColumn.createColumn(i);
            }
        } else
        {
            tabledatacolumn = TableDataColumn.createColumn(s);
        }
        simpledscolumn.setColumn(tabledatacolumn);
        return simpledscolumn;
    }

    private boolean isColumnName(String s)
    {
        return StringUtils.isNotBlank(s) && s.length() > 0 && s.charAt(0) == '#' && !s.endsWith("#");
    }

    public void addListener(ItemListener itemlistener)
    {
        itemListener = itemlistener;
        tableNameComboBox.addItemListener(itemlistener);
        columnNameComboBox.addItemListener(itemlistener);
    }

    public void removeListener(ItemListener itemlistener)
    {
        tableNameComboBox.removeItemListener(itemlistener);
        columnNameComboBox.removeItemListener(itemlistener);
    }

    private java.util.List calculateColumnNameList()
    {
        if(tableNameComboBox.getSelectedItem() != null)
            return tableNameComboBox.getSelectedItem().calculateColumnNameList();
        else
            return new ArrayList();
    }

}
