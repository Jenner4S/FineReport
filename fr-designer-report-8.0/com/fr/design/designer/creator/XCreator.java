// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.base.BaseUtils;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.events.DesignerEditor;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.AuthorityPropertyPane;
import com.fr.design.mainframe.BaseJForm;
import com.fr.design.mainframe.EditingMouseListener;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.NoSupportAuthorityEdit;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WLayout;
import com.fr.stable.StableUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.IntrospectionException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

// Referenced classes of package com.fr.design.designer.creator:
//            XWTitleLayout, XLayoutContainer, XComponent, XCreatorTools, 
//            XCreatorUtils, CRPropertyDescriptor

public abstract class XCreator extends JPanel
    implements XComponent, XCreatorTools
{

    protected static final Border DEFALUTBORDER = BorderFactory.createLineBorder(new Color(210, 210, 210), 1);
    public static final Dimension SMALL_PREFERRED_SIZE = new Dimension(80, 21);
    protected static final Dimension MIDDLE_PREFERRED_SIZE = new Dimension(80, 50);
    protected static final Dimension BIG_PREFERRED_SIZE = new Dimension(80, 80);
    protected Dimension backupSize;
    protected XLayoutContainer backupParent;
    protected Widget data;
    protected JComponent editor;
    private int directions[];
    private Rectangle backupBound;

    public XCreator(Widget widget, Dimension dimension)
    {
        data = widget;
        initEditor();
        if(editor != null && editor != this)
        {
            setLayout(FRGUIPaneFactory.createBorderLayout());
            add(editor, "Center");
        }
        if(dimension.width == 0)
            dimension.width = initEditorSize().width;
        if(dimension.height == 0)
            dimension.height = initEditorSize().height;
        setPreferredSize(dimension);
        setSize(dimension);
        setMaximumSize(dimension);
        initXCreatorProperties();
    }

    public int[] getDirections()
    {
        return directions;
    }

    public void setDirections(int ai[])
    {
        directions = ai;
    }

    public void useBackupSize()
    {
        if(backupSize != null)
            setSize(backupSize);
    }

    public void backupCurrentSize()
    {
        backupSize = getSize();
    }

    public XLayoutContainer getBackupParent()
    {
        return backupParent;
    }

    public void setBackupParent(XLayoutContainer xlayoutcontainer)
    {
        backupParent = xlayoutcontainer;
    }

    public void backupParent()
    {
        setBackupParent(XCreatorUtils.getParentXLayoutContainer(this));
    }

    protected XLayoutContainer getCreatorWrapper(String s)
    {
        return new XWTitleLayout();
    }

    protected void addToWrapper(XLayoutContainer xlayoutcontainer, int i, int j)
    {
        xlayoutcontainer.add(this, "Body");
    }

    protected void setWrapperName(XLayoutContainer xlayoutcontainer, String s)
    {
        xlayoutcontainer.toData().setWidgetName(s);
    }

    public XLayoutContainer initCreatorWrapper(int i)
    {
        String s = toData().getWidgetName();
        XLayoutContainer xlayoutcontainer = getCreatorWrapper(s);
        int j = getWidth();
        int k = getHeight();
        xlayoutcontainer.setLocation(getX(), getY());
        xlayoutcontainer.setSize(j, k);
        setWrapperName(xlayoutcontainer, s);
        setLocation(0, 0);
        addToWrapper(xlayoutcontainer, j, i);
        LayoutUtils.layoutRootContainer(xlayoutcontainer);
        return xlayoutcontainer;
    }

    public void rebuid()
    {
        initXCreatorProperties();
    }

    public abstract CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException;

    public Widget toData()
    {
        return data;
    }

    protected abstract JComponent initEditor();

    protected abstract void initXCreatorProperties();

    public Dimension initEditorSize()
    {
        return SMALL_PREFERRED_SIZE;
    }

    protected String getIconName()
    {
        return "";
    }

    public String getIconPath()
    {
        return (new StringBuilder()).append("/com/fr/web/images/form/resources/").append(getIconName()).toString();
    }

    public String createDefaultName()
    {
        String s = getClass().getSimpleName();
        return (new StringBuilder()).append(Character.toLowerCase(s.charAt(1))).append(s.substring(2)).toString();
    }

    public void setBounds(Rectangle rectangle)
    {
        Dimension dimension = getMinimumSize();
        if(rectangle.getWidth() < (double)dimension.width)
        {
            rectangle.width = dimension.width;
            rectangle.x = getX();
        }
        if(rectangle.getHeight() < (double)dimension.height)
        {
            rectangle.height = dimension.height;
            rectangle.y = getY();
        }
        super.setBounds(rectangle);
    }

    public DesignerEditor getDesignerEditor()
    {
        return null;
    }

    public JComponent createToolPane(BaseJForm basejform, FormDesigner formdesigner)
    {
        if(!BaseUtils.isAuthorityEditing())
            if(isDedicateContainer())
            {
                XCreator xcreator = ((XLayoutContainer)this).getXCreator(0);
                return xcreator.createToolPane(basejform, formdesigner);
            } else
            {
                return WidgetPropertyPane.getInstance(formdesigner);
            }
        if(formdesigner.isSupportAuthority())
        {
            AuthorityPropertyPane authoritypropertypane = new AuthorityPropertyPane(formdesigner);
            authoritypropertypane.populate();
            return authoritypropertypane;
        } else
        {
            return new NoSupportAuthorityEdit();
        }
    }

    public Dimension getMinimumSize()
    {
        return new Dimension(0, 0);
    }

    public boolean isReport()
    {
        return false;
    }

    public boolean canEnterIntoParaPane()
    {
        return true;
    }

    public boolean canEnterIntoAdaptPane()
    {
        return true;
    }

    public boolean isSupportDrag()
    {
        return true;
    }

    public java.util.List getAllXCreatorNameList(XCreator xcreator, java.util.List list)
    {
        list.add(xcreator.toData().getWidgetName());
        return list;
    }

    public boolean SearchQueryCreators(XCreator xcreator)
    {
        return false;
    }

    public Rectangle getBackupBound()
    {
        return backupBound;
    }

    public void setBackupBound(Rectangle rectangle)
    {
        backupBound = rectangle;
    }

    public void notShowInComponentTree(ArrayList arraylist)
    {
    }

    public void resetCreatorName(String s)
    {
        toData().setWidgetName(s);
    }

    public XCreator getEditingChildCreator()
    {
        return this;
    }

    public XCreator getPropertyDescriptorCreator()
    {
        return this;
    }

    public void updateChildBound(int i)
    {
    }

    public boolean isComponentTreeLeaf()
    {
        return true;
    }

    public boolean isDedicateContainer()
    {
        return false;
    }

    public transient boolean acceptType(Class aclass[])
    {
        Class aclass1[] = aclass;
        int i = aclass1.length;
        for(int j = 0; j < i; j++)
        {
            Class class1 = aclass1[j];
            if(StableUtils.classInstanceOf(getClass(), class1))
                return true;
        }

        return false;
    }

    public boolean shouldScaleCreator()
    {
        return false;
    }

    public boolean hasTitleStyle()
    {
        return false;
    }

    public void respondClick(EditingMouseListener editingmouselistener, MouseEvent mouseevent)
    {
        FormDesigner formdesigner = editingmouselistener.getDesigner();
        SelectionModel selectionmodel = editingmouselistener.getSelectionModel();
        if(mouseevent.getClickCount() <= 1)
            selectionmodel.selectACreatorAtMouseEvent(mouseevent);
        if(editingmouselistener.stopEditing() && this != formdesigner.getRootComponent())
        {
            ComponentAdapter componentadapter = AdapterBus.getComponentAdapter(formdesigner, this);
            editingmouselistener.startEditing(this, componentadapter.getDesignerEditor(), componentadapter);
        }
    }

    public void deleteRelatedComponent(XCreator xcreator, FormDesigner formdesigner)
    {
    }

    public void seleteRelatedComponent(XCreator xcreator)
    {
    }

    public XCreator getXCreator()
    {
        return this;
    }

    public void adjustCompSize(double d)
    {
    }

    public ArrayList getTargetChildrenList()
    {
        return new ArrayList();
    }

    public XLayoutContainer getOuterLayout()
    {
        return getBackupParent();
    }

    public void recalculateChildWidth(int i)
    {
    }

    public void recalculateChildHeight(int i)
    {
    }

}
