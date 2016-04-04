// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.base.Parameter;
import com.fr.design.designer.TargetComponent;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.Painter;
import com.fr.design.designer.beans.actions.FormDeleteAction;
import com.fr.design.designer.beans.adapters.layout.FRParameterLayoutAdapter;
import com.fr.design.designer.beans.events.CreatorEventListenerTable;
import com.fr.design.designer.beans.events.DesignerEditListener;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.designer.beans.location.Location;
import com.fr.design.designer.beans.location.RootResizeDirection;
import com.fr.design.designer.beans.models.AddingModel;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.beans.models.StateModel;
import com.fr.design.designer.creator.XChartEditor;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWBorderLayout;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.design.designer.properties.FormWidgetAuthorityEditPane;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.parameter.ParaDefinitePane;
import com.fr.design.parameter.ParameterPropertyPane;
import com.fr.design.parameter.ParameterToolBarPane;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;
import com.fr.design.utils.ComponentUtils;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.FormElementCaseContainerProvider;
import com.fr.form.FormElementCaseProvider;
import com.fr.form.main.Form;
import com.fr.form.parameter.FormSubmitButton;
import com.fr.form.ui.EditorHolder;
import com.fr.form.ui.Label;
import com.fr.form.ui.PaddingMargin;
import com.fr.form.ui.Widget;
import com.fr.form.ui.WidgetValue;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.form.ui.container.WFitLayout;
import com.fr.form.ui.container.WLayout;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.ExtraClassManager;
import com.fr.stable.ArrayUtils;
import com.fr.stable.bridge.StableFactory;
import edu.emory.mathcs.backport.java.util.Arrays;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

// Referenced classes of package com.fr.design.mainframe:
//            FormDesignerDropTarget, FormTargetMode, FormEditorKeyListener, EditingMouseListener, 
//            FormDesignerUI, FormCreatorDropTarget, DesignerTransferHandler, ComponentTree, 
//            AuthorityPropertyPane, NoSupportAuthorityEdit, BaseFormDesigner, DesignerFrame, 
//            EastRegionContainerPane, DesignerContext, ConnectorHelper, FormArea, 
//            FormParaWidgetPane, JTemplate, FormSelection, WidgetPropertyPane, 
//            FormWidgetDetailPane, FormDesignerModeForSpecial, AuthorityEditPane

public class FormDesigner extends TargetComponent
    implements TreeSelectionListener, InvocationHandler, BaseFormDesigner, ParaDefinitePane
{
    private class FormWidgetAuthorityEditAdapter
        implements DesignerEditListener
    {

        final FormDesigner this$0;

        public void fireCreatorModified(DesignerEvent designerevent)
        {
            if(!BaseUtils.isAuthorityEditing())
                return;
            if(designerevent.getCreatorEventID() == 5 || designerevent.getCreatorEventID() == 2 || designerevent.getCreatorEventID() == 7)
            {
                TreePath treepath = getSelectedTreePath();
                if(treepath == null)
                    return;
                if(BaseUtils.isAuthorityEditing())
                    showAuthorityEditPane();
            } else
            if(designerevent.getCreatorEventID() == 6)
                repaint();
        }

        public TreePath getSelectedTreePath()
        {
            XCreator xcreator = selectionModel.getSelection().getSelectedCreator();
            return buildTreePath(xcreator);
        }

        public boolean equals(Object obj)
        {
            return obj instanceof FormWidgetAuthorityEditAdapter;
        }

        private TreePath buildTreePath(Component component)
        {
            ArrayList arraylist = new ArrayList();
            for(Object obj = component; obj != null; obj = ((Component) (obj)).getParent())
                arraylist.add(0, obj);

            Object aobj[] = arraylist.toArray();
            if(aobj.length == 0)
                return null;
            else
                return new TreePath(aobj);
        }

        private FormWidgetAuthorityEditAdapter()
        {
            this$0 = FormDesigner.this;
            super();
        }

    }


    protected static final ArrayList NAME_ARRAY_LIST = new ArrayList(Arrays.asList(new String[] {
        Inter.getLocText("M_Edit-Cut"), Inter.getLocText("M_Edit-Copy"), Inter.getLocText("M_Edit-Delete")
    }));
    private static final int BORDER_WIDTH = 6;
    protected static final Dimension LARGE_PREFERRED_SIZE = new Dimension(960, 540);
    private int paraHeight;
    private XLayoutContainer rootComponent;
    private XLayoutContainer paraComponent;
    private boolean drawLineMode;
    private FormArea formArea;
    private ConnectorHelper ConnectorHelper;
    private boolean isReportBlockEditing;
    private transient EditingMouseListener editingMouseListener;
    private transient StateModel stateModel;
    private transient AddingModel addingModel;
    private transient Painter painter;
    private transient SelectionModel selectionModel;
    private CreatorEventListenerTable edit;
    protected Action designer_actions[];
    private FormDesignerModeForSpecial desigerMode;
    private Action switchAction;
    private FormElementCaseContainerProvider elementCaseContainer;
    private Parameter parameterArray[];
    private int currentIndex;
    private static final int NUM_IN_A_LINE = 4;
    private static final int H_COMPONENT_GAP = 165;
    private static final int V_COMPONENT_GAP = 25;
    private static final int FIRST_V_LOCATION = 35;
    private static final int FIRST_H_LOCATION = 90;
    private static final int SECOND_H_LOCATION = 170;
    private static final int ADD_HEIGHT = 20;
    private static final int H_GAP = 105;

    public FormDesigner(Form form)
    {
        this(form, null);
    }

    public FormDesigner(Form form, Action action)
    {
        super(form);
        paraHeight = 0;
        isReportBlockEditing = false;
        setDoubleBuffered(true);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setOpaque(true);
        setBackground(Color.WHITE);
        edit = new CreatorEventListenerTable();
        selectionModel = new SelectionModel(this);
        stateModel = new StateModel(this);
        desigerMode = createFormDesignerTargetMode();
        updateUI();
        refreshRoot();
        initializeListener();
        new FormDesignerDropTarget(this);
        switchAction = action;
        populateParameterPropertyPane();
    }

    public void populateParameterPropertyPane()
    {
        setParameterArray(getNoRepeatParas(((Form)getTarget()).getParameters()));
        refreshParameter();
        ParameterPropertyPane.getInstance().populateBean(this);
    }

    public Parameter[] getNoRepeatParas(Parameter aparameter[])
    {
        ArrayList arraylist = new ArrayList();
        HashSet hashset = new HashSet();
        Parameter aparameter1[] = aparameter;
        int i = aparameter1.length;
        for(int j = 0; j < i; j++)
        {
            Parameter parameter = aparameter1[j];
            if(!hashset.contains(parameter.getName().toLowerCase()))
            {
                arraylist.add(parameter);
                hashset.add(parameter.getName().toLowerCase());
            }
        }

        return (Parameter[])arraylist.toArray(new Parameter[arraylist.size()]);
    }

    public void setParameterArray(Parameter aparameter[])
    {
        parameterArray = aparameter;
    }

    public Parameter[] getParameterArray()
    {
        return parameterArray;
    }

    public void refreshParameter()
    {
        XLayoutContainer xlayoutcontainer = getParaComponent();
        if(xlayoutcontainer != null)
        {
            ArrayList arraylist = new ArrayList();
            xlayoutcontainer.getAllXCreatorNameList(xlayoutcontainer, arraylist);
            Parameter aparameter[] = getParameterArray();
            if(aparameter != null)
                removeSame(aparameter, arraylist);
        }
        ParameterPropertyPane.getInstance().getParameterToolbarPane().populateBean(getParameterArray() != null ? getParameterArray() : new Parameter[0]);
        ParameterPropertyPane.getInstance().repaintContainer();
    }

    private void removeSame(Parameter aparameter[], java.util.List list)
    {
        Parameter aparameter1[] = aparameter;
        int i = aparameter1.length;
label0:
        for(int j = 0; j < i; j++)
        {
            Parameter parameter = aparameter1[j];
            Iterator iterator = list.iterator();
            do
            {
                if(!iterator.hasNext())
                    continue label0;
                String s = (String)iterator.next();
                if(s.equalsIgnoreCase(parameter.getName()))
                    setParameterArray((Parameter[])(Parameter[])ArrayUtils.removeElement(getParameterArray(), parameter));
            } while(true);
        }

    }

    public boolean isWithQueryButton()
    {
        XLayoutContainer xlayoutcontainer = getParaComponent();
        return xlayoutcontainer != null && xlayoutcontainer.SearchQueryCreators(xlayoutcontainer);
    }

    public void addingParameter2Editor(Parameter parameter)
    {
        if(getParaComponent() == null)
        {
            addParaPaneTooltips();
            return;
        }
        Label label = new Label();
        String s = parameter.getName();
        label.setWidgetName((new StringBuilder()).append("Label").append(s).toString());
        label.setWidgetValue(new WidgetValue((new StringBuilder()).append(s).append(":").toString()));
        XCreator xcreator = XCreatorUtils.createXCreator(label);
        EditorHolder editorholder = new EditorHolder(parameter);
        XCreator xcreator1 = XCreatorUtils.createXCreator(editorholder);
        if(!autoAddComponent(xcreator, 165 * (currentIndex % 4) + 90, 35 + 25 * (currentIndex / 4)))
            return;
        if(!autoAddComponent(xcreator1, 165 * (currentIndex % 4) + 170, 35 + 25 * (currentIndex / 4)))
        {
            return;
        } else
        {
            currentIndex++;
            parameterArray = (Parameter[])(Parameter[])ArrayUtils.removeElement(parameterArray, parameter);
            refreshParameter();
            EastRegionContainerPane.getInstance().refreshDownPane();
            return;
        }
    }

    public void addingParameter2EditorWithQueryButton(Parameter parameter)
    {
        if(getParaComponent() == null)
        {
            addParaPaneTooltips();
            return;
        }
        Label label = new Label();
        String s = parameter.getName();
        label.setWidgetName((new StringBuilder()).append("Label").append(s).toString());
        label.setWidgetValue(new WidgetValue((new StringBuilder()).append(s).append(":").toString()));
        XCreator xcreator = XCreatorUtils.createXCreator(label);
        EditorHolder editorholder = new EditorHolder(parameter);
        editorholder.setWidgetName(s);
        XCreator xcreator1 = XCreatorUtils.createXCreator(editorholder);
        if(!autoAddComponent(xcreator, 90, 35 + 25 * (currentIndex / 4)))
            return;
        if(!autoAddComponent(xcreator1, 170, 35 + 25 * (currentIndex / 4)))
            return;
        FormSubmitButton formsubmitbutton = new FormSubmitButton();
        formsubmitbutton.setWidgetName("Search");
        formsubmitbutton.setText(Inter.getLocText("FR-Designer_Query"));
        xcreator1 = XCreatorUtils.createXCreator(formsubmitbutton);
        if(!autoAddComponent(xcreator1, 270, 35 + 25 * (currentIndex / 4)))
        {
            return;
        } else
        {
            currentIndex = (currentIndex + 4) - currentIndex % 4;
            parameterArray = (Parameter[])(Parameter[])ArrayUtils.removeElement(parameterArray, parameter);
            refreshParameter();
            EastRegionContainerPane.getInstance().refreshDownPane();
            return;
        }
    }

    public void addingAllParameter2Editor()
    {
        if(getParaComponent() == null)
        {
            addParaPaneTooltips();
            return;
        }
        if(parameterArray == null)
            return;
        int i = 0;
        do
        {
            if(i >= parameterArray.length)
                break;
            Label label = new Label();
            label.setWidgetName((new StringBuilder()).append("Label").append(parameterArray[i].getName()).toString());
            label.setWidgetValue(new WidgetValue((new StringBuilder()).append(parameterArray[i].getName()).append(":").toString()));
            XCreator xcreator1 = XCreatorUtils.createXCreator(label);
            EditorHolder editorholder = new EditorHolder(parameterArray[i]);
            editorholder.setWidgetName(parameterArray[i].getName());
            XCreator xcreator2 = XCreatorUtils.createXCreator(editorholder);
            if(!autoAddComponent(xcreator1, 165 * (currentIndex % 4) + 90, 35 + 25 * (currentIndex / 4)) || !autoAddComponent(xcreator2, 165 * (currentIndex % 4) + 170, 35 + 25 * (currentIndex / 4)))
                break;
            currentIndex++;
            i++;
        } while(true);
        if(!isWithQueryButton())
        {
            FormSubmitButton formsubmitbutton = new FormSubmitButton();
            formsubmitbutton.setWidgetName("Search");
            formsubmitbutton.setText(Inter.getLocText("FR-Designer_Query"));
            XCreator xcreator = XCreatorUtils.createXCreator(formsubmitbutton);
            if(!autoAddComponent(xcreator, 600, 35 + 25 * (currentIndex / 4)))
                return;
        }
        parameterArray = null;
        refreshParameter();
        EastRegionContainerPane.getInstance().refreshDownPane();
    }

    private void addParaPaneTooltips()
    {
        JOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("FR-Designer-Form-Please_Drag_ParaPane"), Inter.getLocText("FR-Designer_Tooltips"), 2, 2);
    }

    public boolean autoAddComponent(XCreator xcreator, int i, int j)
    {
        XWParameterLayout xwparameterlayout = (XWParameterLayout)getParaComponent();
        FRParameterLayoutAdapter frparameterlayoutadapter = (FRParameterLayoutAdapter)xwparameterlayout.getLayoutAdapter();
        if(prepareForAdd(xcreator, i, j, xwparameterlayout))
            frparameterlayoutadapter.addBean(xcreator, i, j);
        getSelectionModel().setSelectedCreator(xcreator);
        repaint();
        return true;
    }

    public boolean prepareForAdd(XCreator xcreator, int i, int j, XWParameterLayout xwparameterlayout)
    {
        Dimension dimension = xwparameterlayout.getSize();
        Boolean boolean1 = Boolean.valueOf(false);
        if(xcreator.getWidth() / 2 + i > xwparameterlayout.getWidth())
        {
            dimension.width = xcreator.getWidth() / 2 + i + 20;
            boolean1 = Boolean.valueOf(true);
        }
        if(xcreator.getHeight() / 2 + j > xwparameterlayout.getHeight())
        {
            dimension.height = xcreator.getHeight() / 2 + j + 20;
            boolean1 = Boolean.valueOf(true);
        }
        if(boolean1.booleanValue())
        {
            xwparameterlayout.setSize(dimension);
            setParaHeight(dimension.height);
        }
        return true;
    }

    public void addParaComponent()
    {
        if(paraComponent != null)
        {
            return;
        } else
        {
            paraHeight = 65;
            paraComponent = new XWParameterLayout();
            paraComponent.toData().setWidgetName("para");
            paraComponent.setSize(paraComponent.initEditorSize());
            XWBorderLayout xwborderlayout = (XWBorderLayout)rootComponent.getParent();
            xwborderlayout.toData().setNorthSize(paraHeight);
            xwborderlayout.add(paraComponent, "North");
            xwborderlayout.setSize(xwborderlayout.getWidth(), xwborderlayout.getHeight() + paraHeight);
            selectionModel.reset();
            selectionModel.setSelectedCreator(paraComponent);
            invalidateLayout();
            populateParameterPropertyPane();
            return;
        }
    }

    public Component getTopContainer()
    {
        if(rootComponent != null)
            return LayoutUtils.getTopContainer(rootComponent);
        else
            return XCreatorUtils.createXCreator(((Form)getTarget()).getContainer());
    }

    public int getParaHeight()
    {
        return paraHeight;
    }

    public void setParaHeight(int i)
    {
        XWBorderLayout xwborderlayout = (XWBorderLayout)getTopContainer();
        xwborderlayout.toData().setNorthSize(i);
        xwborderlayout.setSize(xwborderlayout.getWidth(), (xwborderlayout.getHeight() + i) - getParaHeight());
        paraHeight = i;
    }

    public void removeParaComponent()
    {
        XWBorderLayout xwborderlayout = (XWBorderLayout)getTopContainer();
        xwborderlayout.toData().removeWidget(paraComponent.toData());
        paraHeight = 0;
        paraComponent = null;
        xwborderlayout.setSize(rootComponent.getWidth(), rootComponent.getHeight());
        EastRegionContainerPane.getInstance().replaceDownPane(getEastDownPane());
        invalidateLayout();
    }

    public void switchTab(FormElementCaseContainerProvider formelementcasecontainerprovider)
    {
        if(switchAction == null)
        {
            return;
        } else
        {
            elementCaseContainer = formelementcasecontainerprovider;
            switchAction.actionPerformed(null);
            return;
        }
    }

    public void setElementCaseContainer(FormElementCaseContainerProvider formelementcasecontainerprovider)
    {
        elementCaseContainer = formelementcasecontainerprovider;
    }

    public FormElementCaseProvider getElementCase()
    {
        return elementCaseContainer.getElementCase();
    }

    public String getElementCaseContainerName()
    {
        return elementCaseContainer.getElementCaseContainerName();
    }

    public void setElementCase(FormElementCaseProvider formelementcaseprovider)
    {
        elementCaseContainer.setElementCase(formelementcaseprovider);
    }

    public void setElementCaseBackground(BufferedImage bufferedimage)
    {
        elementCaseContainer.setBackground(bufferedimage);
    }

    public Dimension getElementCaseContainerSize()
    {
        return elementCaseContainer.getSize();
    }

    public FormElementCaseContainerProvider getElementCaseContainer()
    {
        return elementCaseContainer;
    }

    protected FormDesignerModeForSpecial createFormDesignerTargetMode()
    {
        return new FormTargetMode(this);
    }

    public FormDesignerModeForSpecial getDesignerMode()
    {
        return desigerMode;
    }

    public CreatorEventListenerTable getEditListenerTable()
    {
        return edit;
    }

    public void addDesignerEditListener(DesignerEditListener designereditlistener)
    {
        getEditListenerTable().addListener(designereditlistener);
    }

    public void cancelFormat()
    {
    }

    public boolean hasWAbsoluteLayout()
    {
        return paraComponent != null && paraComponent.acceptType(new Class[] {
            com/fr/design/designer/creator/XWParameterLayout
        });
    }

    public void setReportBlockEditing(boolean flag)
    {
        isReportBlockEditing = flag;
    }

    public boolean isReportBlockEditing()
    {
        return isReportBlockEditing;
    }

    public boolean renameCreator(XCreator xcreator, String s)
    {
        if(ComparatorUtils.equals(xcreator.toData().getWidgetName(), s))
            return false;
        if(((Form)getTarget()).isNameExist(s))
        {
            JOptionPane.showMessageDialog(this, (new StringBuilder()).append("\"").append(s).append("\"").append(Inter.getLocText("Utils-has_been_existed")).append("!").toString(), Inter.getLocText("FR-Designer_Alert"), 2);
            return false;
        } else
        {
            xcreator.resetCreatorName(s);
            getEditListenerTable().fireCreatorModified(xcreator, 5);
            return true;
        }
    }

    public void updateWidth(int i)
    {
    }

    public Object invoke(Object obj, Method method, Object aobj[])
        throws Throwable
    {
        if(rootComponent != null)
            LayoutUtils.layoutRootContainer(rootComponent);
        if(paraComponent != null)
            LayoutUtils.layoutRootContainer(paraComponent);
        repaint();
        return null;
    }

    private void initializeListener()
    {
        addKeyListener(new FormEditorKeyListener(this));
        editingMouseListener = new EditingMouseListener(this);
        addMouseMotionListener(editingMouseListener);
        addMouseListener(editingMouseListener);
        addInvocationHandler(this);
        getEditListenerTable().addListener(new DesignerEditListener() {

            final FormDesigner this$0;

            public void fireCreatorModified(DesignerEvent designerevent)
            {
                if(designerevent.getCreatorEventID() != 7)
                    fireTargetModified();
            }

            
            {
                this$0 = FormDesigner.this;
                super();
            }
        }
);
    }

    public void addInvocationHandler(InvocationHandler invocationhandler)
    {
        ClassLoader classloader = getClass().getClassLoader();
        Class aclass[] = {
            com/fr/design/designer/beans/events/DesignerEditListener
        };
        Object obj = Proxy.newProxyInstance(classloader, aclass, invocationhandler);
        addDesignerEditListener((DesignerEditListener)obj);
        addDesignerEditListener(new FormWidgetAuthorityEditAdapter());
    }

    public void stopAddingState()
    {
        addingModel = null;
        painter = null;
        new FormDesignerDropTarget(this);
        repaint();
    }

    public void updateUI()
    {
        setUI(new FormDesignerUI());
    }

    public void selectComponents(MouseEvent mouseevent)
    {
        stateModel.selectCreators(mouseevent);
        stateModel.reset();
        repaint();
    }

    private XCreator xCreatorAt(int i, int j, XCreator xcreator, XCreator axcreator[])
    {
        if(xcreator == null || !xcreator.isVisible())
            return null;
        i -= xcreator.getX();
        j -= xcreator.getY();
        if(xcreator instanceof XLayoutContainer)
        {
            XLayoutContainer xlayoutcontainer = (XLayoutContainer)xcreator;
            int k = xlayoutcontainer.getXCreatorCount();
            for(int l = 0; l < k; l++)
            {
                XCreator xcreator1 = xlayoutcontainer.getXCreator(l);
                if(ArrayUtils.contains(axcreator, xcreator1))
                    continue;
                XCreator xcreator2 = xCreatorAt(i, j, xcreator1, axcreator);
                if(xcreator2 != null && !ArrayUtils.contains(axcreator, xcreator1))
                    return xcreator2;
            }

        }
        Rectangle rectangle = ComponentUtils.computeVisibleRect(xcreator);
        if(isIntersectArea(i, j, rectangle))
            return xcreator;
        else
            return null;
    }

    private boolean isIntersectArea(int i, int j, Rectangle rectangle)
    {
        return (double)i >= rectangle.getX() && (double)i <= rectangle.getX() + rectangle.getWidth() && (double)j >= rectangle.getY() && (double)j <= rectangle.getY() + rectangle.getHeight();
    }

    public void updateDrawLineMode(MouseEvent mouseevent)
    {
        Point point = ConnectorHelper.getNearWidgetPoint(mouseevent);
        if(point == null)
        {
            XCreator xcreator = getComponentAt(mouseevent);
            if(xcreator == rootComponent)
                point = new Point(mouseevent.getX() + formArea.getHorizontalValue(), mouseevent.getY() + formArea.getVerticalValue());
        }
        stateModel.startDrawLine(point);
    }

    public void refreshRoot()
    {
        XLayoutContainer xlayoutcontainer = (XLayoutContainer)XCreatorUtils.createXCreator(((Form)getTarget()).getContainer());
        if(ExtraClassManager.getInstance().getDebugLogProviders().length != 0)
            formDesignerDebug();
        ((Form)getTarget()).getContainer().setMargin(new PaddingMargin(0, 0, 0, 0));
        xlayoutcontainer.setBorder(null);
        if(xlayoutcontainer.acceptType(new Class[] {
    com/fr/design/designer/creator/XWBorderLayout
}))
        {
            WBorderLayout wborderlayout = (WBorderLayout)xlayoutcontainer.toData();
            Widget widget = wborderlayout.getLayoutWidget("North");
            Widget widget1 = wborderlayout.getLayoutWidget("Center");
            xlayoutcontainer.removeAll();
            refreshNorth(widget, xlayoutcontainer);
            refreshCenter(widget1, xlayoutcontainer);
        } else
        {
            xlayoutcontainer.setSize(LARGE_PREFERRED_SIZE);
            setRootComponent(xlayoutcontainer);
        }
    }

    private void refreshNorth(Widget widget, XLayoutContainer xlayoutcontainer)
    {
        if(widget == null)
        {
            paraComponent = null;
            paraHeight = 0;
            FormParaWidgetPane.getInstance(this);
            return;
        } else
        {
            XLayoutContainer xlayoutcontainer1 = (XLayoutContainer)XCreatorUtils.createXCreator(widget);
            paraHeight = ((XWBorderLayout)xlayoutcontainer).toData().getNorthSize();
            paraComponent = xlayoutcontainer1;
            xlayoutcontainer1.setSize(0, paraHeight);
            xlayoutcontainer.add(xlayoutcontainer1, "North");
            return;
        }
    }

    private void refreshCenter(Widget widget, XLayoutContainer xlayoutcontainer)
    {
        if(widget == null)
        {
            XLayoutContainer xlayoutcontainer1 = (XLayoutContainer)XCreatorUtils.createXCreator(new WFitLayout("body"));
            xlayoutcontainer1.setSize(LARGE_PREFERRED_SIZE);
            setRootComponent(xlayoutcontainer1);
            xlayoutcontainer.add(rootComponent, "Center");
            return;
        }
        XLayoutContainer xlayoutcontainer2 = (XLayoutContainer)XCreatorUtils.createXCreator(widget);
        Dimension dimension = new Dimension();
        dimension.width = ((WFitLayout)widget).getContainerWidth();
        dimension.height = ((WFitLayout)widget).getContainerHeight();
        xlayoutcontainer2.setSize(dimension);
        xlayoutcontainer.setSize(dimension.width, dimension.height + paraHeight);
        setRootComponent(xlayoutcontainer2);
        LayoutUtils.layoutContainer(xlayoutcontainer2);
        xlayoutcontainer.add(rootComponent, "Center");
        if(ExtraClassManager.getInstance().getDebugLogProviders().length != 0)
            formDesignerDebug();
    }

    protected Insets getOutlineInsets()
    {
        return new Insets(10, 10, 10, 10);
    }

    public Painter getPainter()
    {
        return painter;
    }

    public void setPainter(Painter painter1)
    {
        painter = painter1;
    }

    public XLayoutContainer getRootComponent()
    {
        return rootComponent;
    }

    public XLayoutContainer getParaComponent()
    {
        return paraComponent;
    }

    public boolean isFormParaDesigner()
    {
        return false;
    }

    public boolean isRoot(XCreator xcreator)
    {
        return xcreator == rootComponent;
    }

    public Direction getLoc2Root(MouseEvent mouseevent)
    {
        int i = mouseevent.getX() + formArea.getHorizontalValue();
        int j = mouseevent.getY() + formArea.getVerticalValue();
        int k = rootComponent.getWidth();
        int l = rootComponent.getHeight();
        Insets insets = getOutlineInsets();
        if(i < k)
            if(j >= l && j <= l + insets.bottom)
                return RootResizeDirection.BOTTOM_RESIZE;
            else
                return Location.outer;
        if(i <= k + insets.right)
        {
            if(j >= 0 && j < l)
                return RootResizeDirection.RIGHT_RESIZE;
            if(j >= l && j <= l + insets.bottom)
                return RootResizeDirection.RIGHT_BOTTOM_RESIZE;
            else
                return Location.outer;
        } else
        {
            return Location.outer;
        }
    }

    protected void setRootComponent(XLayoutContainer xlayoutcontainer)
    {
        rootComponent = xlayoutcontainer;
        xlayoutcontainer.setOpaque(true);
        xlayoutcontainer.setBackground(XCreatorConstants.FORM_BG);
        selectionModel.reset();
        selectionModel.setSelectedCreator(rootComponent);
        invalidateLayout();
    }

    public StateModel getStateModel()
    {
        return stateModel;
    }

    public AddingModel getAddingModel()
    {
        return addingModel;
    }

    public XCreator getComponentAt(MouseEvent mouseevent)
    {
        return getComponentAt(mouseevent.getX(), mouseevent.getY());
    }

    public XCreator getComponentAt(Point point)
    {
        return getComponentAt(point.x, point.y);
    }

    public XCreator getComponentAt(int i, int j)
    {
        return getComponentAt(i, j, null);
    }

    public XCreator getComponentAt(int i, int j, XCreator axcreator[])
    {
        XLayoutContainer xlayoutcontainer = j >= paraHeight - formArea.getVerticalValue() ? rootComponent : paraComponent;
        XCreator xcreator = xCreatorAt(i + formArea.getHorizontalValue(), j + formArea.getVerticalValue(), xlayoutcontainer, axcreator);
        return ((XCreator) (xcreator != null ? xcreator : xlayoutcontainer));
    }

    public SelectionModel getSelectionModel()
    {
        if(paraComponent != null)
        {
            paraComponent.setSize(paraComponent.getWidth(), getParaHeight());
            Rectangle rectangle = rootComponent.getBounds();
            rootComponent.setBounds(rectangle.x, getParaHeight(), rectangle.width, rectangle.height);
        }
        return selectionModel;
    }

    public void removeSelection()
    {
        selectionModel.reset();
        repaint();
    }

    public void startDraggingBean(XCreator xcreator)
    {
        addingModel = new AddingModel(this, xcreator);
        setDropTarget(new FormCreatorDropTarget(this));
        repaint();
    }

    public void startDraggingComponent(XCreator xcreator, MouseEvent mouseevent, int i, int j)
    {
        int k = xcreator.getWidth();
        int l = xcreator.getHeight();
        addingModel = new AddingModel(xcreator, i, j);
        DesignerTransferHandler designertransferhandler = new DesignerTransferHandler(this, addingModel);
        setTransferHandler(designertransferhandler);
        designertransferhandler.exportAsDrag(this, mouseevent, 1);
        XLayoutContainer xlayoutcontainer = XCreatorUtils.getParentXLayoutContainer(xcreator);
        selectionModel.removeCreator(xcreator, k, l);
        selectionModel.setSelectedCreator(xlayoutcontainer);
        setDropTarget(new FormCreatorDropTarget(this));
        repaint();
    }

    public void valueChanged(TreeSelectionEvent treeselectionevent)
    {
        ComponentTree componenttree = (ComponentTree)treeselectionevent.getSource();
        TreePath atreepath[] = componenttree.getSelectionPaths();
        if(atreepath != null)
        {
            ArrayList arraylist = new ArrayList();
            TreePath atreepath1[] = atreepath;
            int i = atreepath1.length;
            for(int j = 0; j < i; j++)
            {
                TreePath treepath1 = atreepath1[j];
                arraylist.add((XCreator)treepath1.getLastPathComponent());
            }

            if(!BaseUtils.isAuthorityEditing())
            {
                selectionModel.setSelectedCreators(arraylist);
                TreePath treepath = treeselectionevent.getNewLeadSelectionPath();
                XCreator xcreator = (XCreator)treepath.getLastPathComponent();
                formArea.scrollPathToVisible(xcreator);
            } else
            {
                showAuthorityEditPane();
            }
            setToolbarButtons(atreepath.length == 1 && componenttree.getSelectionPath().getParentPath() == null);
        }
    }

    public void showAuthorityEditPane()
    {
        HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().setAuthorityMode(true);
        if(isSupportAuthority())
        {
            AuthorityPropertyPane authoritypropertypane = new AuthorityPropertyPane(this);
            authoritypropertypane.populate();
            EastRegionContainerPane.getInstance().replaceUpPane(authoritypropertypane);
        } else
        {
            EastRegionContainerPane.getInstance().replaceUpPane(new NoSupportAuthorityEdit());
        }
        EastRegionContainerPane.getInstance().replaceDownPane(RolesAlreadyEditedPane.getInstance());
    }

    public boolean isSupportAuthority()
    {
        int i = getSelectionModel().getSelection().size();
        Object obj = i != 0 ? ((Object) (getSelectionModel().getSelection().getSelectedCreator())) : ((Object) (getRootComponent()));
        return !(obj instanceof XLayoutContainer) && !(obj instanceof XChartEditor);
    }

    protected void setToolbarButtons(boolean flag)
    {
        DesignerContext.getDesignerFrame().checkCombineUp(!isRoot(getSelectionModel().getSelection().getSelectedCreator()) && !flag, NAME_ARRAY_LIST);
    }

    private void invalidateLayout()
    {
        FormArea formarea = getArea();
        if(formarea != null)
        {
            formarea.doLayout();
            formarea.repaint();
        }
    }

    public boolean isRootRelatedAction(String s)
    {
        return NAME_ARRAY_LIST.contains(s);
    }

    public void makeVisible(XCreator xcreator)
    {
        XCreator xcreator1 = AdapterBus.getFirstInvisibleParent(xcreator);
        if(isRoot(xcreator1))
            return;
        do
        {
            if(xcreator1 == null)
                break;
            XLayoutContainer xlayoutcontainer = XCreatorUtils.getParentXLayoutContainer(xcreator1);
            if(xlayoutcontainer == null)
                break;
            xlayoutcontainer.getLayoutAdapter().showComponent(xcreator1);
            xcreator1 = AdapterBus.getFirstInvisibleParent(xcreator1);
        } while(true);
    }

    public void refreshDesignerUI()
    {
        LayoutUtils.layoutRootContainer(getRootComponent());
        if(ExtraClassManager.getInstance().getDebugLogProviders().length != 0)
            formDesignerDebug();
        repaint();
    }

    private void formDesignerDebug()
    {
        if(((Form)getTarget()).getContainer() instanceof WBorderLayout)
        {
            Widget widget = ((WBorderLayout)((Form)getTarget()).getContainer()).getLayoutWidget("Center");
            if(widget != null)
                ExtraClassManager.getInstance().sendDebugLog((new StringBuilder()).append(widget.getClass().getName()).append("@").append(Integer.toHexString(widget.hashCode())).toString());
            else
                ExtraClassManager.getInstance().sendDebugLog("Target.center is null");
        }
        if(getRootComponent() != null && getRootComponent().toData() != null)
            ExtraClassManager.getInstance().sendDebugLog((new StringBuilder()).append(getRootComponent().toData().getClass().getName()).append("@").append(Integer.toHexString(getRootComponent().toData().getClass().hashCode())).toString());
        else
            ExtraClassManager.getInstance().sendDebugLog("RootComponent or rootComponent.data is null");
    }

    public Action[] getActions()
    {
        if(designer_actions == null)
            designer_actions = (new Action[] {
                new FormDeleteAction(this)
            });
        return designer_actions;
    }

    protected Border getOuterBorder()
    {
        return XCreatorConstants.AREA_BORDER;
    }

    protected Rectangle getOutlineBounds()
    {
        Insets insets = getOuterBorder().getBorderInsets(this);
        int i = rootComponent.getWidth() + insets.left + insets.right;
        int j = rootComponent.getHeight() + insets.top + insets.bottom;
        return new Rectangle(0, 0, i, j);
    }

    public void populateRootSize()
    {
    }

    public FormArea getArea()
    {
        return formArea;
    }

    public void setParent(FormArea formarea)
    {
        formArea = formarea;
    }

    public void paintContent(Graphics g)
    {
        rootComponent.paint(g);
    }

    public void paintPara(Graphics g)
    {
        if(paraComponent != null)
            paraComponent.paint(g);
    }

    public void resetEditorComponentBounds()
    {
        editingMouseListener.resetEditorComponentBounds();
    }

    public ConnectorHelper getDrawLineHelper()
    {
        return ConnectorHelper;
    }

    public boolean isDrawLineMode()
    {
        return drawLineMode;
    }

    public void setDrawLineMode(boolean flag)
    {
        drawLineMode = flag;
    }

    public void doMousePress(double d, double d1)
    {
        dispatchEvent(new MouseEvent(this, 501, System.currentTimeMillis(), 0, (int)d, (int)d1, 1, false));
    }

    public void stopEditing()
    {
    }

    public AuthorityEditPane createAuthorityEditPane()
    {
        return new FormWidgetAuthorityEditPane(this);
    }

    public JPanel getEastUpPane()
    {
        return WidgetPropertyPane.getInstance(this);
    }

    public JPanel getEastDownPane()
    {
        final JPanel pane = new JPanel();
        if(EastRegionContainerPane.getInstance().getDownPane() == null)
        {
            (new Thread() {

                final JPanel val$pane;
                final FormDesigner this$0;

                public void run()
                {
                    try
                    {
                        Thread.sleep(1500L);
                    }
                    catch(InterruptedException interruptedexception)
                    {
                        FRLogger.getLogger().error(interruptedexception.getMessage(), interruptedexception);
                    }
                    pane.setLayout(new BorderLayout());
                    pane.add(FormWidgetDetailPane.getInstance(FormDesigner.this), "Center");
                    EastRegionContainerPane.getInstance().replaceDownPane(pane);
                }

            
            {
                this$0 = FormDesigner.this;
                pane = jpanel;
                super();
            }
            }
).start();
        } else
        {
            pane.setLayout(new BorderLayout());
            pane.add(FormWidgetDetailPane.getInstance(this), "Center");
            EastRegionContainerPane.getInstance().replaceDownPane(pane);
        }
        return pane;
    }

    public ToolBarMenuDockPlus getToolBarMenuDockPlus()
    {
        return (ToolBarMenuDockPlus)StableFactory.getMarkedInstanceObjectFromClass("JForm", com/fr/design/mainframe/toolbar/ToolBarMenuDockPlus);
    }

    public void copy()
    {
        selectionModel.copySelectedCreator2ClipBoard();
    }

    public boolean paste()
    {
        selectionModel.pasteFromClipBoard();
        return false;
    }

    public boolean cut()
    {
        selectionModel.cutSelectedCreator2ClipBoard();
        return false;
    }

    public MenuDef[] menus4Target()
    {
        return new MenuDef[0];
    }

    public int getMenuState()
    {
        return 4;
    }

    public ShortCut[] shortcut4TemplateMenu()
    {
        return new ShortCut[0];
    }

    public ShortCut[] shortCuts4Authority()
    {
        return new ShortCut[0];
    }

    public ToolBarDef[] toolbars4Target()
    {
        return new ToolBarDef[0];
    }

    public JComponent[] toolBarButton4Form()
    {
        return new JComponent[0];
    }

    public volatile Component getComponentAt(Point point)
    {
        return getComponentAt(point);
    }

    public volatile Component getComponentAt(int i, int j)
    {
        return getComponentAt(i, j);
    }


}
