// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.base.*;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.widget.editors.NameWidgetComboboxEditor;
import com.fr.form.ui.*;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.stable.core.PropertyChangeAdapter;
import java.awt.*;
import java.beans.IntrospectionException;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.designer.creator:
//            XWidgetCreator, CRPropertyDescriptor, XWScaleLayout, XWTitleLayout, 
//            XCreator, DedicateLayoutContainer, XCreatorUtils, XLayoutContainer

public class XNameWidget extends XWidgetCreator
{

    public XNameWidget(NameWidget namewidget, Dimension dimension)
    {
        super(namewidget, dimension);
    }

    public NameWidget toData()
    {
        return (NameWidget)data;
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if(editor == null)
        {
            Graphics2D graphics2d = (Graphics2D)g.create();
            BaseUtils.drawStringStyleInRotation(graphics2d, getWidth(), getHeight(), Inter.getLocText("FR-Engine_NameWidget-Invalid"), Style.getInstance().deriveHorizontalAlignment(0).deriveVerticalAlignment(0).deriveFRFont(FRFont.getInstance().applyForeground(Color.RED)), ScreenResolution.getScreenResolution());
        }
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return (new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("widgetName", data.getClass())).setI18NName(Inter.getLocText("Form-Widget_Name")), (new CRPropertyDescriptor("name", data.getClass())).setI18NName(Inter.getLocText("FR-Engine_NameWidget-Name")).setEditorClass(com/fr/design/mainframe/widget/editors/NameWidgetComboboxEditor).setPropertyChangeListener(new PropertyChangeAdapter() {

                final XNameWidget this$0;

                public void propertyChange()
                {
                    rebuild();
                }

            
            {
                this$0 = XNameWidget.this;
                super();
            }
            }
)
        });
    }

    protected JComponent initEditor()
    {
        if(editor == null)
        {
            WidgetManagerProvider widgetmanagerprovider = WidgetManager.getProviderInstance();
            WidgetConfig widgetconfig = widgetmanagerprovider.getWidgetConfig(toData().getName());
            Widget widget;
            if(widgetconfig != null && (widget = widgetconfig.toWidget()) != null)
            {
                editor = XCreatorUtils.createXCreator(widget);
                toData().setVisible(widget.isVisible());
                setBorder(null);
            } else
            {
                setBorder(DEFALUTBORDER);
            }
        }
        return editor;
    }

    protected String getIconName()
    {
        return "user_widget.png";
    }

    public void rebuild()
    {
        editor = null;
        removeAll();
        initEditor();
        if(editor != null)
        {
            setLayout(FRGUIPaneFactory.createBorderLayout());
            add(editor, "Center");
            setVisible(toData().isVisible());
        }
    }

    protected XLayoutContainer getCreatorWrapper(String s)
    {
        return ((XLayoutContainer) (shouldScaleCreator() ? new XWScaleLayout() : new XWTitleLayout()));
    }

    protected void addToWrapper(XLayoutContainer xlayoutcontainer, int i, int j)
    {
        if(!shouldScaleCreator())
        {
            super.addToWrapper(xlayoutcontainer, i, j);
            return;
        } else
        {
            setSize(i, j);
            xlayoutcontainer.add(this);
            return;
        }
    }

    public boolean shouldScaleCreator()
    {
        if(editor == null)
        {
            return false;
        } else
        {
            XCreator xcreator = (XCreator)editor;
            return xcreator.shouldScaleCreator();
        }
    }

    public volatile Widget toData()
    {
        return toData();
    }
}
