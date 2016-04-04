// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.constants.UIConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.gui.core.UserDefinedWidgetOption;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.UserDefinedWidgetConfig;
import com.fr.form.ui.Widget;
import com.fr.form.ui.WidgetConfig;
import com.fr.form.ui.WidgetManager;
import com.fr.form.ui.WidgetManagerProvider;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.Icon;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe:
//            ToolBarButton, DesignerContext, DesignerFrame, DesktopCardPane, 
//            FormDesigner, FormDesignerModeForSpecial, FormWidgetPopWindow

public class FormParaPane extends JPanel
{

    private static final int PANE_HEIGHT = 24;
    private static final int TOOLTIP_X = 5;
    private static final int TOOLTIP_Y = 10;
    private static Dimension originalSize;
    private static FormParaPane THIS;
    private java.util.List predifinedwidgeList;
    private UIButton predefineButton;
    private FormWidgetPopWindow predifinedWindow;
    private FormDesigner designer;
    private ArrayList componentsList4Para;

    public static final FormParaPane getInstance(FormDesigner formdesigner)
    {
        if(THIS == null)
            THIS = new FormParaPane();
        THIS.designer = formdesigner;
        THIS.setTarget(formdesigner);
        if(originalSize != null)
            THIS.setPreferredSize(originalSize);
        return THIS;
    }

    public FormParaPane()
    {
        predifinedwidgeList = new ArrayList();
        componentsList4Para = new ArrayList();
        predefineButton = new UIButton(UIConstants.PRE_WIDGET_ICON) {

            final FormParaPane this$0;

            public Dimension getPreferredSize()
            {
                Dimension dimension = super.getPreferredSize();
                dimension.width = 24;
                return dimension;
            }

            
            {
                this$0 = FormParaPane.this;
                super(icon);
            }
        }
;
        predefineButton.set4ToolbarButton();
        predefineButton.setToolTipText(Inter.getLocText("Widget-User_Defined_Widget_Config"));
        predefineButton.addMouseListener(new MouseAdapter() {

            final FormParaPane this$0;

            public void mouseClicked(MouseEvent mouseevent)
            {
                if(predifinedWindow == null)
                    predifinedWindow = new FormWidgetPopWindow();
                loadPredifinedWidget();
                predifinedWindow.showToolTip(predefineButton.getLocationOnScreen().x + predefineButton.getWidth() + 5, predefineButton.getLocationOnScreen().y - 10, (WidgetOption[])(WidgetOption[])predifinedwidgeList.toArray(new WidgetOption[predifinedwidgeList.size()]));
            }

            
            {
                this$0 = FormParaPane.this;
                super();
            }
        }
);
        setLayout(new FlowLayout(0));
        DesignerContext.getDesignerFrame().getCenterTemplateCardPane().addComponentListener(new ComponentAdapter() {

            final FormParaPane this$0;

            public void componentResized(ComponentEvent componentevent)
            {
                if(getParent() != null)
                {
                    JPanel jpanel = (JPanel)getParent();
                    Dimension dimension = jpanel.getSize();
                    int i = 0;
                    for(int j = 0; j < jpanel.getComponentCount() - 1; j++)
                        i += jpanel.getComponent(j).getWidth();

                    setPreferredSize(new Dimension(dimension.width - i, dimension.height));
                    LayoutUtils.layoutContainer(jpanel);
                }
            }

            
            {
                this$0 = FormParaPane.this;
                super();
            }
        }
);
        initParaComponent();
    }

    private void initParaComponent()
    {
        removeAll();
        initParaButtons();
        WidgetOption widgetoption;
        for(Iterator iterator = componentsList4Para.iterator(); iterator.hasNext(); add(new ToolBarButton(widgetoption)))
            widgetoption = (WidgetOption)iterator.next();

        add(new UILabel("|"));
        add(predefineButton);
    }

    private void loadPredifinedWidget()
    {
        predifinedwidgeList.clear();
        if(designer != null)
        {
            WidgetOption awidgetoption[] = designer.getDesignerMode().getPredefinedWidgetOptions();
            for(int i = 0; i < awidgetoption.length; i++)
                predifinedwidgeList.add(awidgetoption[i]);

        }
        WidgetManagerProvider widgetmanagerprovider = WidgetManager.getProviderInstance();
        Iterator iterator = widgetmanagerprovider.getWidgetConfigNameIterator();
        do
        {
            if(!iterator.hasNext())
                break;
            String s = (String)iterator.next();
            WidgetConfig widgetconfig = widgetmanagerprovider.getWidgetConfig(s);
            if(widgetconfig instanceof UserDefinedWidgetConfig)
            {
                Widget widget = ((UserDefinedWidgetConfig)widgetconfig).getWidget();
                String s1 = widget.getClass().getName();
                if(!isButtonWidget(s1) && XCreatorUtils.createXCreator(widget).canEnterIntoParaPane())
                    predifinedwidgeList.add(new UserDefinedWidgetOption(s));
            }
        } while(true);
    }

    private boolean isButtonWidget(String s)
    {
        return s.endsWith("DeleteRowButton") || s.endsWith("AppendRowButton") || s.endsWith("TreeNodeToogleButton");
    }

    private void setTarget(FormDesigner formdesigner)
    {
        if(formdesigner == null)
        {
            return;
        } else
        {
            initParaComponent();
            return;
        }
    }

    private void initParaButtons()
    {
        if(componentsList4Para.isEmpty())
        {
            WidgetOption awidgetoption[] = WidgetOption.getReportParaWidgetIntance();
            WidgetOption awidgetoption1[] = (WidgetOption[])(WidgetOption[])ArrayUtils.addAll(awidgetoption, ExtraDesignClassManager.getInstance().getParameterWidgetOptions());
            WidgetOption awidgetoption2[] = awidgetoption1;
            int i = awidgetoption2.length;
            for(int j = 0; j < i; j++)
            {
                WidgetOption widgetoption = awidgetoption2[j];
                componentsList4Para.add(widgetoption);
            }

        }
    }





}
