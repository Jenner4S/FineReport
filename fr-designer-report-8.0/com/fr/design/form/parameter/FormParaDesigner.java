// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.form.parameter;

import com.fr.base.BaseUtils;
import com.fr.base.Parameter;
import com.fr.base.parameter.ParameterUI;
import com.fr.design.DesignModelAdapter;
import com.fr.design.designer.beans.actions.CopyAction;
import com.fr.design.designer.beans.actions.CutAction;
import com.fr.design.designer.beans.actions.FormDeleteAction;
import com.fr.design.designer.beans.actions.PasteAction;
import com.fr.design.designer.beans.adapters.layout.FRAbsoluteLayoutAdapter;
import com.fr.design.designer.beans.events.CreatorEventListenerTable;
import com.fr.design.designer.beans.events.DesignerEditListener;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWAbsoluteLayout;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.design.designer.properties.FormWidgetAuthorityEditPane;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.mainframe.AuthorityEditPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.mainframe.EastRegionContainerPane;
import com.fr.design.mainframe.FormArea;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormDesignerModeForSpecial;
import com.fr.design.mainframe.FormParaPane;
import com.fr.design.mainframe.FormWidgetDetailPane;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.mainframe.WidgetToolBarPane;
import com.fr.design.parameter.ParaDefinitePane;
import com.fr.design.parameter.ParameterBridge;
import com.fr.design.parameter.ParameterDesignerProvider;
import com.fr.design.parameter.ParameterPropertyPane;
import com.fr.design.parameter.ParameterToolBarPane;
import com.fr.form.main.Form;
import com.fr.form.main.parameter.FormParameterUI;
import com.fr.form.parameter.FormSubmitButton;
import com.fr.form.ui.EditorHolder;
import com.fr.form.ui.Label;
import com.fr.form.ui.Widget;
import com.fr.form.ui.WidgetValue;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.form.ui.container.WLayout;
import com.fr.form.ui.container.WParameterLayout;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.form.parameter:
//            FormParaTargetMode, XFormSubmit

public class FormParaDesigner extends FormDesigner
    implements ParameterDesignerProvider
{

    private static final int NUM_IN_A_LINE = 4;
    private static final int H_COMPONENT_GAP = 165;
    private static final int V_COMPONENT_GAP = 25;
    private static final int FIRST_V_LOCATION = 35;
    private static final int FIRST_H_LOCATION = 90;
    private static final int SECOND_H_LOCATION = 170;
    private static final int ADD_HEIGHT = 20;
    private static final int H_GAP = 105;
    private static Image paraImage = BaseUtils.readImage("/com/fr/design/images/form/parameter.png");

    public FormParaDesigner()
    {
        this(new FormParameterUI());
    }

    public FormParaDesigner(FormParameterUI formparameterui)
    {
        super(gen(formparameterui));
    }

    private static Form gen(Form form)
    {
        Object obj = form.getContainer();
        if(obj == null)
            obj = new WParameterLayout();
        ((WLayout) (obj)).setWidgetName("para");
        form.setContainer(((WLayout) (obj)));
        return form;
    }

    protected FormDesignerModeForSpecial createFormDesignerTargetMode()
    {
        return new FormParaTargetMode(this);
    }

    public void initBeforeUpEdit()
    {
        WidgetToolBarPane.getInstance(this);
        EastRegionContainerPane.getInstance().replaceDownPane(FormWidgetDetailPane.getInstance(this));
        if(!BaseUtils.isAuthorityEditing())
            EastRegionContainerPane.getInstance().replaceUpPane(WidgetPropertyPane.getInstance(this));
        else
            showAuthorityEditPane();
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
        return FormWidgetDetailPane.getInstance(this);
    }

    public AuthorityEditPane getAuthorityEditPane()
    {
        FormWidgetAuthorityEditPane formwidgetauthorityeditpane = new FormWidgetAuthorityEditPane(this);
        formwidgetauthorityeditpane.populateDetials();
        return formwidgetauthorityeditpane;
    }

    public void addListener(final ParaDefinitePane paraDefinitePane)
    {
        getEditListenerTable().addListener(new DesignerEditListener() {

            final ParaDefinitePane val$paraDefinitePane;
            final FormParaDesigner this$0;

            public void fireCreatorModified(DesignerEvent designerevent)
            {
                if(designerevent.getCreatorEventID() == 1 || designerevent.getCreatorEventID() == 3 || designerevent.getCreatorEventID() == 4 || designerevent.getCreatorEventID() == 2 || designerevent.getCreatorEventID() == 5 || designerevent.getCreatorEventID() == 8)
                {
                    paraDefinitePane.setParameterArray(paraDefinitePane.getNoRepeatParas(DesignModelAdapter.getCurrentModelAdapter().getParameters()));
                    paraDefinitePane.refreshParameter();
                }
            }

            
            {
                this$0 = FormParaDesigner.this;
                paraDefinitePane = paradefinitepane;
                super();
            }
        }
);
    }

    public Component createWrapper()
    {
        FormArea formarea = new FormArea(this, false);
        formarea.setBorder(BorderFactory.createEmptyBorder(0, 13, 0, 0));
        return formarea;
    }

    public void refreshAllNameWidgets()
    {
        XCreatorUtils.refreshAllNameWidgets(getRootComponent());
    }

    public void refresh4TableData(String s, String s1)
    {
        ((Form)getTarget()).renameTableData(s, s1);
        getEditListenerTable().fireCreatorModified(7);
    }

    public void refreshParameter(ParaDefinitePane paradefinitepane)
    {
        XLayoutContainer xlayoutcontainer = getRootComponent();
        java.util.List list = getAllXCreatorNameList(xlayoutcontainer);
        Parameter aparameter[] = paradefinitepane.getParameterArray();
        if(aparameter != null)
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
                        paradefinitepane.setParameterArray((Parameter[])(Parameter[])ArrayUtils.removeElement(paradefinitepane.getParameterArray(), parameter));
                } while(true);
            }

        }
        ParameterPropertyPane.getInstance().getParameterToolbarPane().populateBean(paradefinitepane.getParameterArray() != null ? paradefinitepane.getParameterArray() : new Parameter[0]);
        ParameterPropertyPane.getInstance().repaintContainer();
    }

    public boolean isBlank()
    {
        XLayoutContainer xlayoutcontainer = getRootComponent();
        java.util.List list = getAllXCreatorNameList(xlayoutcontainer);
        return list.isEmpty();
    }

    protected void setToolbarButtons(boolean flag)
    {
        DesignerContext.getDesignerFrame().checkCombineUp(!flag, NAME_ARRAY_LIST);
    }

    public boolean isWithoutParaXCreator(Parameter aparameter[])
    {
        XLayoutContainer xlayoutcontainer = getRootComponent();
        java.util.List list = getAllXCreatorNameList(xlayoutcontainer);
        Parameter aparameter1[] = aparameter;
        int i = aparameter1.length;
        int j = 0;
        do
        {
            if(j >= i)
                break;
            Parameter parameter = aparameter1[j];
            for(Iterator iterator = list.iterator(); iterator.hasNext();)
            {
                String s = (String)iterator.next();
                if(s.equalsIgnoreCase(parameter.getName()))
                    return false;
            }

            j++;
        } while(true);
        return true;
    }

    public java.util.List getAllXCreatorNameList()
    {
        XLayoutContainer xlayoutcontainer = getRootComponent();
        ArrayList arraylist = new ArrayList();
        for(int i = 0; i < xlayoutcontainer.getXCreatorCount(); i++)
            if(xlayoutcontainer.getXCreator(i) instanceof XLayoutContainer)
                arraylist.addAll(getAllXCreatorNameList((XLayoutContainer)xlayoutcontainer.getXCreator(i)));
            else
                arraylist.add(xlayoutcontainer.getXCreator(i).toData().getWidgetName());

        return arraylist;
    }

    private java.util.List getAllXCreatorNameList(XLayoutContainer xlayoutcontainer)
    {
        ArrayList arraylist = new ArrayList();
        for(int i = 0; i < xlayoutcontainer.getXCreatorCount(); i++)
            if(xlayoutcontainer.getXCreator(i) instanceof XLayoutContainer)
                arraylist.addAll(getAllXCreatorNameList((XLayoutContainer)xlayoutcontainer.getXCreator(i)));
            else
                arraylist.add(xlayoutcontainer.getXCreator(i).toData().getWidgetName());

        return arraylist;
    }

    public boolean isWithQueryButton()
    {
        XLayoutContainer xlayoutcontainer = getRootComponent();
        return SearchQueryCreators(xlayoutcontainer);
    }

    public Action[] getActions()
    {
        if(designer_actions == null)
            designer_actions = (new Action[] {
                new CutAction(this), new CopyAction(this), new PasteAction(this), new FormDeleteAction(this)
            });
        return designer_actions;
    }

    private boolean SearchQueryCreators(XLayoutContainer xlayoutcontainer)
    {
        boolean flag = false;
        for(int i = 0; i < xlayoutcontainer.getXCreatorCount(); i++)
        {
            if(xlayoutcontainer.getXCreator(i) instanceof XLayoutContainer)
            {
                flag = SearchQueryCreators((XLayoutContainer)xlayoutcontainer.getXCreator(i));
                continue;
            }
            if(xlayoutcontainer.getXCreator(i) instanceof XFormSubmit)
                flag = true;
        }

        return flag;
    }

    public ParameterUI getParaTarget()
    {
        return (FormParameterUI)super.getTarget();
    }

    public void populateParameterPropertyPane(ParaDefinitePane paradefinitepane)
    {
        ParameterPropertyPane.getInstance().populateBean(paradefinitepane);
    }

    public void initWidgetToolbarPane()
    {
        WidgetToolBarPane.getInstance(this);
    }

    public void populate(ParameterUI parameterui)
    {
        if(parameterui == null)
            return;
        if(getTarget() == parameterui)
        {
            repaint();
            return;
        } else
        {
            setTarget((FormParameterUI)parameterui.convert());
            refreshRoot();
            return;
        }
    }

    public boolean hasWAbsoluteLayout()
    {
        return ((Form)getTarget()).getContainer() instanceof WAbsoluteLayout;
    }

    public void refreshRoot()
    {
        Object obj = (XLayoutContainer)XCreatorUtils.createXCreator(((Form)getTarget()).getContainer());
        if(obj == null)
            obj = new XWParameterLayout();
        ((XLayoutContainer) (obj)).setSize(LARGE_PREFERRED_SIZE);
        setRootComponent(((XLayoutContainer) (obj)));
    }

    public boolean isFormParaDesigner()
    {
        return true;
    }

    public XLayoutContainer getParaComponent()
    {
        return getRootComponent();
    }

    private void paintLinkParameters(Graphics g)
    {
        Parameter aparameter[] = DesignModelAdapter.getCurrentModelAdapter().getParameters();
        if(aparameter == null || aparameter.length == 0)
            return;
        Graphics g1 = g.create();
        g1.setColor(Color.RED);
        if(!(getRootComponent() instanceof XWAbsoluteLayout))
            return;
        XWAbsoluteLayout xwabsolutelayout = (XWAbsoluteLayout)getRootComponent();
label0:
        for(int i = 0; i < xwabsolutelayout.getXCreatorCount(); i++)
        {
            XCreator xcreator = xwabsolutelayout.getXCreator(i);
            if(!xcreator.isVisible())
                continue;
            Parameter aparameter1[] = aparameter;
            int j = aparameter1.length;
            int k = 0;
            do
            {
                if(k >= j)
                    continue label0;
                Parameter parameter = aparameter1[k];
                if(parameter.getName().equalsIgnoreCase(xcreator.toData().getWidgetName()))
                {
                    g1.drawImage(paraImage, xcreator.getX() - 4, xcreator.getY() + 2, null);
                    continue label0;
                }
                k++;
            } while(true);
        }

    }

    public Dimension getPreferredSize()
    {
        return getDesignSize();
    }

    public Dimension getDesignSize()
    {
        return ((FormParameterUI)getTarget()).getDesignSize();
    }

    public void setDesignHeight(int i)
    {
        Dimension dimension = getPreferredSize();
        dimension.height = i;
        ((FormParameterUI)getTarget()).setDesignSize(dimension);
    }

    public void paintContent(Graphics g)
    {
        Dimension dimension = ((FormParameterUI)getTarget()).getDesignSize();
        getRootComponent().setSize(dimension);
        getRootComponent().paint(g);
        paintLinkParameters(g);
        paintOp(g, getOutlineBounds());
    }

    private void paintOp(Graphics g, Rectangle rectangle)
    {
        Color color = g.getColor();
        Insets insets = getOutlineInsets();
        g.setColor(XCreatorConstants.OP_COLOR);
        g.fillRect(rectangle.x, rectangle.y + rectangle.height, rectangle.width + insets.right, insets.bottom);
        g.fillRect(rectangle.x + rectangle.width, rectangle.y, insets.right, rectangle.height);
        g.setColor(color);
    }

    protected void setRootComponent(XLayoutContainer xlayoutcontainer)
    {
        xlayoutcontainer.setDirections(new int[] {
            2, 4
        });
        super.setRootComponent(xlayoutcontainer);
    }

    public void populateRootSize()
    {
        ((FormParameterUI)getTarget()).setDesignSize(getRootComponent().getSize());
        if(getParaComponent().acceptType(new Class[] {
    com/fr/design/designer/creator/XWParameterLayout
}))
        {
            WParameterLayout wparameterlayout = (WParameterLayout)getParaComponent().toData();
            wparameterlayout.setDesignWidth(getRootComponent().getWidth());
        }
    }

    public void updateWidth(int i)
    {
        FormParameterUI formparameterui = (FormParameterUI)getTarget();
        formparameterui.setDesignSize(new Dimension(i, formparameterui.getDesignSize().height));
    }

    public void updateHeight(int i)
    {
        FormParameterUI formparameterui = (FormParameterUI)getTarget();
        formparameterui.setDesignSize(new Dimension(formparameterui.getDesignSize().width, i));
    }

    public boolean prepareForAdd(XCreator xcreator, int i, int j, XWAbsoluteLayout xwabsolutelayout)
    {
        if(!isRoot(xwabsolutelayout))
            return false;
        Dimension dimension = xwabsolutelayout.getSize();
        Boolean boolean1 = Boolean.valueOf(false);
        if(xcreator.getWidth() / 2 + i > xwabsolutelayout.getWidth())
        {
            dimension.width = xcreator.getWidth() / 2 + i + 20;
            boolean1 = Boolean.valueOf(true);
        }
        if(xcreator.getHeight() / 2 + j > xwabsolutelayout.getHeight())
        {
            dimension.height = xcreator.getHeight() / 2 + j + 20;
            boolean1 = Boolean.valueOf(true);
        }
        if(boolean1.booleanValue())
        {
            xwabsolutelayout.setSize(dimension);
            populateRootSize();
        }
        return true;
    }

    public boolean addingParameter2Editor(Parameter parameter, int i)
    {
        Label label = new Label();
        String s = parameter.getName();
        label.setWidgetName((new StringBuilder()).append("Label").append(s).toString());
        label.setWidgetValue(new WidgetValue((new StringBuilder()).append(s).append(":").toString()));
        XCreator xcreator = XCreatorUtils.createXCreator(label);
        if(!autoAddComponent(xcreator, 165 * (i % 4) + 90, 35 + 25 * (i / 4)))
            return false;
        EditorHolder editorholder = new EditorHolder(parameter);
        xcreator = XCreatorUtils.createXCreator(editorholder);
        return autoAddComponent(xcreator, 165 * (i % 4) + 170, 35 + 25 * (i / 4));
    }

    public boolean addingParameter2EditorWithQueryButton(Parameter parameter, int i)
    {
        Label label = new Label();
        String s = parameter.getName();
        label.setWidgetName((new StringBuilder()).append("Label").append(s).toString());
        label.setWidgetValue(new WidgetValue((new StringBuilder()).append(s).append(":").toString()));
        XCreator xcreator = XCreatorUtils.createXCreator(label);
        if(!autoAddComponent(xcreator, 90, 35 + 25 * (i / 4)))
            return false;
        EditorHolder editorholder = new EditorHolder(parameter);
        editorholder.setWidgetName(s);
        xcreator = XCreatorUtils.createXCreator(editorholder);
        if(!autoAddComponent(xcreator, 170, 35 + 25 * (i / 4)))
            return false;
        FormSubmitButton formsubmitbutton = new FormSubmitButton();
        formsubmitbutton.setWidgetName("Search");
        formsubmitbutton.setText(Inter.getLocText("FR-Designer_Query"));
        xcreator = XCreatorUtils.createXCreator(formsubmitbutton);
        return autoAddComponent(xcreator, 270, 35 + 25 * (i / 4));
    }

    public void addingAllParameter2Editor(Parameter aparameter[], int i)
    {
        int j = 0;
        do
        {
            if(j >= aparameter.length)
                break;
            Label label = new Label();
            label.setWidgetName((new StringBuilder()).append("Label").append(aparameter[j].getName()).toString());
            label.setWidgetValue(new WidgetValue((new StringBuilder()).append(aparameter[j].getName()).append(":").toString()));
            XCreator xcreator1 = XCreatorUtils.createXCreator(label);
            if(!autoAddComponent(xcreator1, 165 * (i % 4) + 90, 35 + 25 * (i / 4)))
                break;
            EditorHolder editorholder = new EditorHolder(aparameter[j]);
            editorholder.setWidgetName(aparameter[j].getName());
            xcreator1 = XCreatorUtils.createXCreator(editorholder);
            if(!autoAddComponent(xcreator1, 165 * (i % 4) + 170, 35 + 25 * (i / 4)))
                break;
            i++;
            j++;
        } while(true);
        if(!isWithQueryButton())
        {
            FormSubmitButton formsubmitbutton = new FormSubmitButton();
            formsubmitbutton.setWidgetName("Search");
            formsubmitbutton.setText(Inter.getLocText("FR-Designer_Query"));
            XCreator xcreator = XCreatorUtils.createXCreator(formsubmitbutton);
            if(!autoAddComponent(xcreator, 600, 35 + 25 * (i / 4)))
                return;
        }
    }

    public boolean autoAddComponent(XCreator xcreator, int i, int j)
    {
        XWAbsoluteLayout xwabsolutelayout = (XWAbsoluteLayout)getRootComponent();
        FRAbsoluteLayoutAdapter frabsolutelayoutadapter = (FRAbsoluteLayoutAdapter)xwabsolutelayout.getLayoutAdapter();
        if(prepareForAdd(xcreator, i, j, xwabsolutelayout))
            frabsolutelayoutadapter.addBean(xcreator, i, j);
        getSelectionModel().setSelectedCreator(xcreator);
        repaint();
        return true;
    }

    public JPanel[] toolbarPanes4Form()
    {
        return (new JPanel[] {
            FormParaPane.getInstance(this)
        });
    }

    public JComponent[] toolBarButton4Form()
    {
        return (new JComponent[] {
            (new CutAction(this)).createToolBarComponent(), (new CopyAction(this)).createToolBarComponent(), (new PasteAction(this)).createToolBarComponent(), (new FormDeleteAction(this)).createToolBarComponent()
        });
    }

    public volatile ParameterBridge getParaComponent()
    {
        return getParaComponent();
    }

}
