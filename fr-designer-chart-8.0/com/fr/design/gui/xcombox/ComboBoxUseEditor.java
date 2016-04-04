// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.gui.xcombox;

import com.fr.design.editor.editor.Editor;
import com.fr.design.gui.icombobox.FRTreeComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.*;
import java.util.Enumeration;
import javax.swing.ComboBoxEditor;
import javax.swing.JTree;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.tree.*;

public class ComboBoxUseEditor extends Editor
{

    private FRTreeComboBox comBox;
    private String items[];
    private PopupMenuListener popupMenuListener;
    TreeCellRenderer treeRenderer;

    public ComboBoxUseEditor(String as[])
    {
        items = new String[0];
        popupMenuListener = new PopupMenuListener() {

            final ComboBoxUseEditor this$0;

            public void popupMenuWillBecomeVisible(PopupMenuEvent popupmenuevent)
            {
                (new Thread() {

                    final _cls1 this$1;

                    public void run()
                    {
                        calculateComboBoxNames(items);
                    }

                    
                    {
                        this$1 = _cls1.this;
                        super();
                    }
                }
).start();
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent popupmenuevent)
            {
            }

            public void popupMenuCanceled(PopupMenuEvent popupmenuevent)
            {
            }

            
            {
                this$0 = ComboBoxUseEditor.this;
                super();
            }
        }
;
        treeRenderer = new DefaultTreeCellRenderer() {

            final ComboBoxUseEditor this$0;

            public Component getTreeCellRendererComponent(JTree jtree, Object obj, boolean flag, boolean flag1, boolean flag2, int i, boolean flag3)
            {
                super.getTreeCellRendererComponent(jtree, obj, flag, flag1, flag2, i, flag3);
                if(obj instanceof DefaultMutableTreeNode)
                {
                    DefaultMutableTreeNode defaultmutabletreenode = (DefaultMutableTreeNode)obj;
                    Object obj1 = defaultmutabletreenode.getUserObject();
                    if(obj1 instanceof String)
                        setIcon(null);
                }
                return this;
            }

            
            {
                this$0 = ComboBoxUseEditor.this;
                super();
            }
        }
;
        setLayout(new BorderLayout(0, 0));
        comBox = new FRTreeComboBox() {

            final ComboBoxUseEditor this$0;

            protected void dealSamePath(TreePath treepath, TreeNode treenode, UITextField uitextfield)
            {
                Enumeration enumeration = treenode.children();
                do
                {
                    if(!enumeration.hasMoreElements())
                        break;
                    TreeNode treenode1 = (TreeNode)enumeration.nextElement();
                    TreePath treepath1 = treepath.pathByAddingChild(treenode1);
                    if(!pathToString(treepath1).toUpperCase().startsWith(uitextfield.getText().toUpperCase()))
                        continue;
                    tree.scrollPathToVisible(treepath1);
                    tree.setSelectionPath(treepath1);
                    break;
                } while(true);
            }

            
            {
                this$0 = ComboBoxUseEditor.this;
                super();
            }
        }
;
        comBox.getTree().setCellRenderer(treeRenderer);
        comBox.setEditable(true);
        comBox.setEnabled(true);
        items = as;
        comBox.addPopupMenuListener(popupMenuListener);
        comBox.setRenderer(new UIComboBoxRenderer());
        add(comBox, "Center");
        setName("");
        comBox.setBorder(null);
        comBox.addItemListener(new ItemListener() {

            final ComboBoxUseEditor this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                fireStateChanged();
            }

            
            {
                this$0 = ComboBoxUseEditor.this;
                super();
            }
        }
);
        comBox.getEditor().getEditorComponent().addFocusListener(new FocusListener() {

            final ComboBoxUseEditor this$0;

            public void focusLost(FocusEvent focusevent)
            {
                fireStateChanged();
            }

            public void focusGained(FocusEvent focusevent)
            {
            }

            
            {
                this$0 = ComboBoxUseEditor.this;
                super();
            }
        }
);
    }

    public boolean accept(Object obj)
    {
        return obj instanceof String;
    }

    public String getValue()
    {
        if(comBox.getSelectedItem() != null)
            return comBox.getSelectedItem().toString();
        else
            return "";
    }

    public void setValue(String s)
    {
        comBox.setSelectedItem(s);
    }

    private void calculateComboBoxNames(String as[])
    {
        JTree jtree = comBox.getTree();
        if(jtree == null)
            return;
        DefaultMutableTreeNode defaultmutabletreenode = (DefaultMutableTreeNode)jtree.getModel().getRoot();
        defaultmutabletreenode.removeAllChildren();
        if(as.length == 0)
            return;
        for(int i = 0; i < as.length; i++)
        {
            ExpandMutableTreeNode expandmutabletreenode = new ExpandMutableTreeNode(as[i]);
            defaultmutabletreenode.add(expandmutabletreenode);
        }

        ((DefaultTreeModel)jtree.getModel()).reload();
        TreeNode treenode = (TreeNode)jtree.getModel().getRoot();
        TreePath treepath = new TreePath(treenode);
        TreeNode treenode1 = (TreeNode)treepath.getLastPathComponent();
        TreePath treepath1;
        for(Enumeration enumeration = treenode1.children(); enumeration.hasMoreElements(); jtree.expandPath(treepath1))
        {
            TreeNode treenode2 = (TreeNode)enumeration.nextElement();
            treepath1 = treepath.pathByAddingChild(treenode2);
        }

    }

    public volatile void setValue(Object obj)
    {
        setValue((String)obj);
    }

    public volatile Object getValue()
    {
        return getValue();
    }




}
