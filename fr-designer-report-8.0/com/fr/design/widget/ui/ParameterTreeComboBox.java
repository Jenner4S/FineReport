// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.base.BaseUtils;
import com.fr.base.ConfigManager;
import com.fr.base.ConfigManagerProvider;
import com.fr.base.Parameter;
import com.fr.base.TableData;
import com.fr.design.DesignModelAdapter;
import com.fr.design.gui.icombobox.FRTreeComboBox;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.parameter.ParameterGroup;
import com.fr.file.DatasourceManager;
import com.fr.file.DatasourceManagerProvider;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.script.Calculator;
import com.fr.stable.ArrayUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class ParameterTreeComboBox extends FRTreeComboBox
{

    public ParameterTreeComboBox()
    {
        super(new JTree(), new DefaultTreeCellRenderer() {

            public java.awt.Component getTreeCellRendererComponent(JTree jtree, Object obj, boolean flag, boolean flag1, boolean flag2, int i, boolean flag3)
            {
                super.getTreeCellRendererComponent(jtree, obj, flag, flag1, flag2, i, flag3);
                if(obj instanceof DefaultMutableTreeNode)
                {
                    DefaultMutableTreeNode defaultmutabletreenode = (DefaultMutableTreeNode)obj;
                    Object obj1 = defaultmutabletreenode.getUserObject();
                    if(obj1 instanceof String)
                        setIcon(BaseUtils.readIcon("com/fr/design/images/m_insert/expandCell.gif"));
                    else
                    if(obj1 instanceof Parameter)
                    {
                        Parameter parameter = (Parameter)obj1;
                        setText(parameter.getName());
                    }
                }
                return this;
            }

        }
);
        addItemListener(new java.awt.event.ItemListener() {

            final ParameterTreeComboBox this$0;

            public void itemStateChanged(java.awt.event.ItemEvent itemevent)
            {
                if(itemevent.getStateChange() == 1 && (itemevent.getItem() instanceof TreePath))
                {
                    DefaultMutableTreeNode defaultmutabletreenode = (DefaultMutableTreeNode)((TreePath)itemevent.getItem()).getLastPathComponent();
                    if(defaultmutabletreenode.getUserObject() instanceof Parameter)
                        getEditor().setItem(((Parameter)defaultmutabletreenode.getUserObject()).getName());
                    else
                        getEditor().setItem(null);
                }
            }

            
            {
                this$0 = ParameterTreeComboBox.this;
                super();
            }
        }
);
        setEditable(true);
    }

    public String getSelectedParameterName()
    {
        Object obj = getSelectedItem();
        if(obj instanceof TreePath)
            return ((Parameter)((ExpandMutableTreeNode)((TreePath)obj).getLastPathComponent()).getUserObject()).getName();
        else
            return (String)obj;
    }

    public void setSelectedItem(Object obj)
    {
        if(obj instanceof String)
        {
            setSelectedItemString((String)obj);
            return;
        } else
        {
            tree.setSelectionPath((TreePath)obj);
            getModel().setSelectedItem(obj);
            return;
        }
    }

    public void setSelectedParameterName(String s)
    {
        DefaultTreeModel defaulttreemodel = (DefaultTreeModel)tree.getModel();
        DefaultMutableTreeNode defaultmutabletreenode = (DefaultMutableTreeNode)tree.getModel().getRoot();
        DefaultMutableTreeNode defaultmutabletreenode1 = defaultmutabletreenode.getFirstLeaf();
        do
        {
            if(!(defaultmutabletreenode1.getUserObject() instanceof Parameter) || !ComparatorUtils.equals(((Parameter)defaultmutabletreenode1.getUserObject()).getName(), s))
                continue;
            TreePath treepath = new TreePath(defaulttreemodel.getPathToRoot(defaultmutabletreenode1));
            tree.setSelectionPath(treepath);
            setSelectedItem(treepath);
            break;
        } while((defaultmutabletreenode1 = defaultmutabletreenode1.getNextLeaf()) != null);
        if(getSelectedItem() == null)
            ((ComboBoxModel)defaulttreemodel).setSelectedItem(s);
    }

    public void refreshTree()
    {
        DefaultMutableTreeNode defaultmutabletreenode = (DefaultMutableTreeNode)tree.getModel().getRoot();
        defaultmutabletreenode.removeAllChildren();
        addParameterTreeNode(defaultmutabletreenode);
        DefaultTreeModel defaulttreemodel = (DefaultTreeModel)tree.getModel();
        if(defaulttreemodel != null)
            defaulttreemodel.reload();
    }

    private void addParameterTreeNode(DefaultMutableTreeNode defaultmutabletreenode)
    {
        ParameterGroup aparametergroup[] = getParameterGroup();
        ParameterGroup aparametergroup1[] = aparametergroup;
        int i = aparametergroup1.length;
        for(int j = 0; j < i; j++)
        {
            ParameterGroup parametergroup = aparametergroup1[j];
            ExpandMutableTreeNode expandmutabletreenode = new ExpandMutableTreeNode(parametergroup.getGroupName());
            defaultmutabletreenode.add(expandmutabletreenode);
            Parameter aparameter[] = parametergroup.getParameter();
            int k = aparameter.length;
            for(int l = 0; l < k; l++)
            {
                Parameter parameter = aparameter[l];
                if(parameter != null)
                {
                    ExpandMutableTreeNode expandmutabletreenode1 = new ExpandMutableTreeNode(parameter);
                    expandmutabletreenode.add(expandmutabletreenode1);
                }
            }

        }

    }

    private ParameterGroup[] getParameterGroup()
    {
        ArrayList arraylist = new ArrayList();
        DesignModelAdapter designmodeladapter = DesignModelAdapter.getCurrentModelAdapter();
        if(designmodeladapter != null)
        {
            Parameter aparameter[] = designmodeladapter.getReportParameters();
            if(!ArrayUtils.isEmpty(aparameter))
                arraylist.add(new ParameterGroup(Inter.getLocText("ParameterD-Report_Parameter"), aparameter));
            aparameter = designmodeladapter.getTableDataParameters();
            if(!ArrayUtils.isEmpty(aparameter))
                arraylist.add(new ParameterGroup(Inter.getLocText("FR-Designer_Datasource-Parameter"), aparameter));
        }
        Parameter aparameter1[] = ConfigManager.getProviderInstance().getGlobal_Parameters();
        if(!ArrayUtils.isEmpty(aparameter1))
            arraylist.add(new ParameterGroup(Inter.getLocText("M_Server-Global_Parameters"), aparameter1));
        aparameter1 = new Parameter[0];
        Calculator calculator = Calculator.createCalculator();
        DatasourceManagerProvider datasourcemanagerprovider = DatasourceManager.getProviderInstance();
        Iterator iterator = datasourcemanagerprovider.getTableDataNameIterator();
        do
        {
            if(!iterator.hasNext())
                break;
            TableData tabledata = datasourcemanagerprovider.getTableData((String)iterator.next());
            com.fr.stable.ParameterProvider aparameterprovider[] = tabledata.getParameters(calculator);
            if(!ArrayUtils.isEmpty(aparameterprovider))
                aparameter1 = (Parameter[])(Parameter[])ArrayUtils.addAll(aparameter1, aparameterprovider);
        } while(true);
        if(!ArrayUtils.isEmpty(aparameter1))
            arraylist.add(new ParameterGroup(Inter.getLocText(new String[] {
                "Server", "Datasource-Datasource", "Parameter"
            }), aparameter1));
        return (ParameterGroup[])arraylist.toArray(new ParameterGroup[0]);
    }
}
