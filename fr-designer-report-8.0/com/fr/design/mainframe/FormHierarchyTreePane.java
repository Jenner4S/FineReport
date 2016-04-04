// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.design.designer.treeview.ComponentTreeModel;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.parameter.HierarchyTreePane;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.tree.TreePath;

// Referenced classes of package com.fr.design.mainframe:
//            FormDockView, ComponentTree, FormDesigner, DockingView

public class FormHierarchyTreePane extends FormDockView
    implements HierarchyTreePane
{
    private class ForWardAction extends UpdateAction
    {

        final FormHierarchyTreePane this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            searchResult.next();
        }

        public ForWardAction()
        {
            this$0 = FormHierarchyTreePane.this;
            super();
            setName(Inter.getLocText("Form-Hierarchy_Tree_Next"));
            setSmallIcon(BaseUtils.readIcon("com/fr/design/images/m_help/forward.png"));
            setEnabled(false);
        }
    }

    private class BackAction extends UpdateAction
    {

        final FormHierarchyTreePane this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            searchResult.last();
        }

        public BackAction()
        {
            this$0 = FormHierarchyTreePane.this;
            super();
            setName(Inter.getLocText("Form-Hierarchy_Tree_Last"));
            setSmallIcon(BaseUtils.readIcon("com/fr/design/images/m_help/back.png"));
            setEnabled(false);
        }
    }

    private class SearchResultPane extends JPanel
    {

        private UILabel resultLabel;
        private BackAction backAction;
        private ForWardAction forwardAction;
        private TreePath tree[];
        private int number;
        final FormHierarchyTreePane this$0;

        private void addButtonToJPanel(JPanel jpanel, JComponent jcomponent)
        {
            jpanel.add(jcomponent);
            if(jcomponent instanceof UIButton)
                jcomponent.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        }

        public void populate(TreePath atreepath[])
        {
            tree = atreepath;
            resultLabel.setText((new StringBuilder()).append(Inter.getLocText("FR-Designer_Total")).append(":").append(tree.length).toString());
            number = 0;
            check();
        }

        public void next()
        {
            if(number < tree.length - 1)
                componentTree.setAndScrollSelectionPath(tree[++number]);
            check();
        }

        public void last()
        {
            if(number > 0)
                componentTree.setAndScrollSelectionPath(tree[--number]);
            check();
        }

        public void check()
        {
            if(tree.length < 1)
            {
                backAction.setEnabled(false);
                forwardAction.setEnabled(false);
            } else
            {
                backAction.setEnabled(number > 0);
                forwardAction.setEnabled(number < tree.length - 1);
            }
        }

        SearchResultPane()
        {
            this$0 = FormHierarchyTreePane.this;
            super();
            resultLabel = new UILabel();
            backAction = new BackAction();
            forwardAction = new ForWardAction();
            number = 0;
            setLayout(FRGUIPaneFactory.createBorderLayout());
            JPanel jpanel = FRGUIPaneFactory.createCenterFlowInnerContainer_S_Pane();
            addButtonToJPanel(jpanel, backAction.createToolBarComponent());
            addButtonToJPanel(jpanel, forwardAction.createToolBarComponent());
            add(jpanel, "East");
            add(resultLabel, "West");
        }
    }

    private static class HOLDER
    {

        private static FormHierarchyTreePane singleton = new FormHierarchyTreePane();



        private HOLDER()
        {
        }
    }


    public static final int NODE_LENGTH = 2;
    public static final int PARA = 0;
    public static final int BODY = 1;
    private ComponentTree componentTree;
    private UITextField searchTextField;
    private SearchResultPane searchResult;

    public static FormHierarchyTreePane getInstance()
    {
        return HOLDER.singleton;
    }

    public static FormHierarchyTreePane getInstance(FormDesigner formdesigner)
    {
        HOLDER.singleton.setEditingFormDesigner(formdesigner);
        HOLDER.singleton.refreshDockingView();
        return HOLDER.singleton;
    }

    private FormHierarchyTreePane()
    {
        setLayout(new BorderLayout(0, 6));
    }

    public String getViewTitle()
    {
        return Inter.getLocText("Form-Hierarchy_Tree");
    }

    public Icon getViewIcon()
    {
        return BaseUtils.readIcon("/com/fr/design/images/m_report/tree.png");
    }

    public ComponentTree getComponentTree()
    {
        return componentTree;
    }

    public void clearDockingView()
    {
        componentTree = null;
        searchTextField = null;
        searchResult = null;
        add(new JScrollPane(), "Center");
    }

    public void refreshDockingView()
    {
        FormDesigner formdesigner = getEditingFormDesigner();
        removeAll();
        if(formdesigner == null)
        {
            clearDockingView();
            return;
        }
        componentTree = new ComponentTree(formdesigner);
        ComponentTreeModel componenttreemodel = (ComponentTreeModel)componentTree.getModel();
        XCreator xcreator = (XCreator)componenttreemodel.getRoot();
        int i = componenttreemodel.getChildCount(xcreator);
        if(i == 2)
            adjustPosition(componenttreemodel, formdesigner);
        UIScrollPane uiscrollpane = new UIScrollPane(componentTree);
        uiscrollpane.setBorder(null);
        add(uiscrollpane, "Center");
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        add(jpanel, "North");
        jpanel.add(new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Designer_Search")).append(":").toString(), 0), "West");
        searchTextField = new UITextField();
        jpanel.add(searchTextField, "Center");
        searchTextField.getDocument().addDocumentListener(new DocumentListener() {

            final FormHierarchyTreePane this$0;

            public void insertUpdate(DocumentEvent documentevent)
            {
                search();
            }

            public void removeUpdate(DocumentEvent documentevent)
            {
                search();
            }

            public void changedUpdate(DocumentEvent documentevent)
            {
                search();
            }

            private void search()
            {
                String s = searchTextField.getText();
                if(StringUtils.isEmpty(s))
                    removeSearchResult();
                else
                    populate(componentTree.search(s));
            }

            
            {
                this$0 = FormHierarchyTreePane.this;
                super();
            }
        }
);
    }

    private void adjustPosition(ComponentTreeModel componenttreemodel, FormDesigner formdesigner)
    {
        XCreator xcreator = (XCreator)componenttreemodel.getRoot();
        if(componenttreemodel.getChild(xcreator, 0) instanceof XWParameterLayout)
        {
            return;
        } else
        {
            xcreator.add((Component)(Component)componenttreemodel.getChild(xcreator, 0), 1);
            componenttreemodel.setRoot(xcreator);
            componentTree = new ComponentTree(formdesigner, componenttreemodel);
            return;
        }
    }

    public void refreshRoot()
    {
        if(componentTree == null)
        {
            return;
        } else
        {
            componentTree.refreshTreeRoot();
            return;
        }
    }

    public void removeSearchResult()
    {
        componentTree.setSelectionPath(null);
        if(searchResult != null)
            remove(searchResult);
    }

    public void populate(TreePath atreepath[])
    {
        if(searchResult == null)
            searchResult = new SearchResultPane();
        if(((BorderLayout)getLayout()).getLayoutComponent("South") == null)
            add(searchResult, "South");
        searchResult.populate(atreepath);
    }

    public DockingView.Location preferredLocation()
    {
        return DockingView.Location.WEST_BELOW;
    }




}
