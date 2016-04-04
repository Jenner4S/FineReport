// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.adapters.component;

import com.fr.base.FRContext;
import com.fr.design.actions.UpdateAction;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.actions.ChangeNameAction;
import com.fr.design.designer.beans.events.DesignerEditor;
import com.fr.design.designer.creator.*;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.gui.xtable.PropertyGroupModel;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.utils.ComponentUtils;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.Button;
import com.fr.form.ui.Widget;
import com.fr.general.FRLogger;
import com.fr.stable.StringUtils;
import com.fr.stable.core.PropertyChangeAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.beans.IntrospectionException;
import java.util.*;
import javax.swing.Action;
import javax.swing.JPopupMenu;

public class CompositeComponentAdapter
    implements ComponentAdapter
{

    protected FormDesigner designer;
    protected DesignerEditor editorComponent;
    protected XCreator xCreator;
    private ChangeNameAction changeVarNameAction;

    public CompositeComponentAdapter(FormDesigner formdesigner, Component component)
    {
        designer = formdesigner;
        xCreator = (XCreator)component;
    }

    public void initialize()
    {
        initButtonText();
        java.awt.Dimension dimension = xCreator.getPreferredSize();
        xCreator.setSize(dimension);
        LayoutUtils.layoutContainer(xCreator);
    }

    private void initButtonText()
    {
        Widget widget = xCreator.toData();
        if((xCreator instanceof XButton) && StringUtils.isEmpty(((Button)widget).getText()))
        {
            ((Button)xCreator.toData()).setText(widget.getWidgetName());
            ((XButton)xCreator).setButtonText(widget.getWidgetName());
        }
    }

    public void paintComponentMascot(Graphics g)
    {
        Graphics2D graphics2d = (Graphics2D)g;
        AlphaComposite alphacomposite = AlphaComposite.getInstance(3, 0.5F);
        graphics2d.setComposite(alphacomposite);
        xCreator.paint(graphics2d);
        g.setColor(XCreatorConstants.RESIZE_BOX_BORDER_COLOR);
        g.drawRect(0, 0, xCreator.getWidth() - 1, xCreator.getHeight() - 1);
    }

    public JPopupMenu getContextPopupMenu(MouseEvent mouseevent)
    {
        JPopupMenu jpopupmenu = new JPopupMenu();
        if(changeVarNameAction == null)
            changeVarNameAction = new ChangeNameAction(designer);
        boolean flag = ComponentUtils.isRootComponent(xCreator) || designer.isRoot(xCreator);
        changeVarNameAction.setEnabled(!flag);
        jpopupmenu.add(changeVarNameAction);
        Action aaction[] = designer.getActions();
        Action aaction1[] = aaction;
        int i = aaction1.length;
        for(int j = 0; j < i; j++)
        {
            Action action = aaction1[j];
            action.setEnabled(!designer.isRootRelatedAction(((UpdateAction)action).getName()) || !flag);
            jpopupmenu.add(action);
        }

        return jpopupmenu;
    }

    private ArrayList createPropertyGroupModels(CRPropertyDescriptor acrpropertydescriptor[])
    {
        HashMap hashmap = new HashMap();
        ArrayList arraylist = new ArrayList();
        CRPropertyDescriptor acrpropertydescriptor1[] = acrpropertydescriptor;
        int i = acrpropertydescriptor1.length;
        for(int j = 0; j < i; j++)
        {
            CRPropertyDescriptor crpropertydescriptor = acrpropertydescriptor1[j];
            String s1 = (String)crpropertydescriptor.getValue("category");
            if(StringUtils.isEmpty(s1))
                s1 = "Form-Basic_Properties";
            ArrayList arraylist3 = (ArrayList)hashmap.get(s1);
            if(arraylist3 == null)
            {
                arraylist3 = new ArrayList();
                hashmap.put(s1, arraylist3);
                arraylist.add(s1);
            }
            arraylist3.add(crpropertydescriptor);
        }

        ArrayList arraylist1 = new ArrayList();
        PropertyGroupModel propertygroupmodel;
        for(Iterator iterator = arraylist.iterator(); iterator.hasNext(); arraylist1.add(propertygroupmodel))
        {
            String s = (String)iterator.next();
            ArrayList arraylist2 = (ArrayList)hashmap.get(s);
            propertygroupmodel = new PropertyGroupModel(s, xCreator, (CRPropertyDescriptor[])arraylist2.toArray(new CRPropertyDescriptor[0]), designer);
        }

        return arraylist1;
    }

    public ArrayList getXCreatorPropertyModel()
    {
        ArrayList arraylist = new ArrayList();
        CRPropertyDescriptor acrpropertydescriptor[] = getCalculateCreatorProperties();
        ArrayList arraylist1 = createPropertyGroupModels(acrpropertydescriptor);
        Collections.sort(arraylist1);
        arraylist.addAll(arraylist1);
        return arraylist;
    }

    private CRPropertyDescriptor[] getCalculateCreatorProperties()
    {
        try
        {
            return xCreator.getPropertyDescriptorCreator().supportedDescriptor();
        }
        catch(IntrospectionException introspectionexception)
        {
            FRContext.getLogger().error(introspectionexception.getMessage(), introspectionexception);
        }
        return new CRPropertyDescriptor[0];
    }

    public DesignerEditor getDesignerEditor()
    {
        if(editorComponent == null)
        {
            editorComponent = xCreator.getDesignerEditor();
            if(editorComponent != null)
                editorComponent.addPropertyChangeListener(new PropertyChangeAdapter() {

                    final CompositeComponentAdapter this$0;

                    public void propertyChange()
                    {
                        designer.fireTargetModified();
                    }

            
            {
                this$0 = CompositeComponentAdapter.this;
                super();
            }
                }
);
        }
        if(editorComponent != null)
            editorComponent.reset();
        return editorComponent;
    }
}
