// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.bridge.DesignToolbarProvider;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.core.FormWidgetOption;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.module.DesignModuleFactory;
import com.fr.general.Inter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JComponent;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe:
//            ToolBarButton, FormDesigner, FormDesignerModeForSpecial, FormWidgetPopWindow

public class WidgetToolBarPane extends BasicPane
    implements DesignToolbarProvider
{
    private class WidgetToolBarPaneLayout
        implements LayoutManager
    {

        private int max_Y;
        final WidgetToolBarPane this$0;

        public void addLayoutComponent(String s, Component component)
        {
        }

        public void removeLayoutComponent(Component component)
        {
        }

        public Dimension preferredLayoutSize(Container container)
        {
            return new Dimension(56, max_Y);
        }

        public Dimension minimumLayoutSize(Container container)
        {
            return new Dimension(56, max_Y);
        }

        public void layoutContainer(Container container)
        {
            Insets insets = container.getInsets();
            int i = container.getWidth() - insets.left - insets.right - 8;
            int j = insets.left;
            int k = insets.top;
            boolean flag = true;
            for(int l = 0; l < container.getComponentCount(); l++)
            {
                Component component = container.getComponent(l);
                if(component instanceof TitleLabel)
                {
                    if(flag)
                    {
                        component.setBounds(j, k, i + 8, 20);
                        k = k + 20 + 4;
                    } else
                    {
                        k = k + 20 + 4;
                        component.setBounds(j, k, i + 8, 20);
                        flag = true;
                        k = k + 20 + 4;
                    }
                    continue;
                }
                if(component instanceof ToolBarButton)
                {
                    if(flag)
                    {
                        component.setBounds(j, k, i / 2, 20);
                        flag = false;
                    } else
                    {
                        component.setBounds(j + i / 2, k, i / 2, 20);
                        k = k + 20 + 4;
                        flag = true;
                    }
                } else
                {
                    component.setBounds(j + 13, k, i / 2, 20);
                    flag = true;
                    k = k + 20 + 4;
                }
            }

            max_Y = k;
            if(!flag)
                max_Y += 24;
        }

        private WidgetToolBarPaneLayout()
        {
            this$0 = WidgetToolBarPane.this;
            super();
            max_Y = 0;
        }

    }

    public class TitleLabel extends UILabel
    {

        final WidgetToolBarPane this$0;

        public void paintComponent(Graphics g)
        {
            Dimension dimension = getPreferredSize();
            Graphics2D graphics2d = (Graphics2D)g;
            graphics2d.setColor(new Color(215, 215, 215));
            graphics2d.fillRect(0, 0, dimension.width, dimension.height);
            super.paintComponent(g);
            graphics2d.setColor(Color.black);
            BasicStroke basicstroke = new BasicStroke(2.0F, 0, 1, 3.5F, new float[] {
                3F, 1.0F
            }, 0.0F);
            graphics2d.setStroke(basicstroke);
            graphics2d.drawLine(0, dimension.height, dimension.width, dimension.height);
        }

        public Dimension getPreferredSize()
        {
            return new Dimension(56, 20);
        }

        public TitleLabel(String s)
        {
            this$0 = WidgetToolBarPane.this;
            super(s);
            setBackground(Color.gray);
            setHorizontalAlignment(0);
        }
    }


    private static final int WIDTH = 56;
    private static final int BUTTON_GAP = 4;
    private static final int BUTTON_HEIGHT = 20;
    private static final int WIDTH_GAP = 13;
    private ArrayList componentsList4Form;
    private FormDesigner designer;
    private JPanel formWidgetPanel;
    private FormWidgetPopWindow chartWindow;
    private static WidgetToolBarPane singleton = new WidgetToolBarPane();

    public static WidgetToolBarPane getInstance()
    {
        return singleton;
    }

    public static WidgetToolBarPane getInstance(FormDesigner formdesigner)
    {
        singleton.setTarget(formdesigner);
        singleton.checkEnable();
        return singleton;
    }

    public void refreshToolbar()
    {
        reset();
    }

    public static void refresh()
    {
        singleton.reset();
    }

    private WidgetToolBarPane()
    {
        componentsList4Form = new ArrayList();
        setLayout(new BorderLayout());
    }

    private void reset()
    {
        if(formWidgetPanel != null)
            formWidgetPanel.removeAll();
        if(designer == null || !isShowing())
            return;
        if(!designer.getDesignerMode().isFormParameterEditor())
        {
            initFormComponent();
            formWidgetPanel.doLayout();
        }
        doLayout();
        repaint();
    }

    private void setTarget(FormDesigner formdesigner)
    {
        if(formdesigner == null)
            return;
        FormDesigner formdesigner1 = designer;
        designer = formdesigner;
        if(isChangeToOrInitFormEditor(formdesigner1))
            initFormComponent();
    }

    private boolean isChangeToOrInitFormEditor(FormDesigner formdesigner)
    {
        return !designer.getDesignerMode().isFormParameterEditor() && (formdesigner == null || formdesigner.getDesignerMode().isFormParameterEditor());
    }

    private void initFormComponent()
    {
        if(formWidgetPanel == null)
        {
            initFormButtons();
            formWidgetPanel = new JPanel();
            formWidgetPanel.setLayout(new WidgetToolBarPaneLayout());
        }
        if(formWidgetPanel.getComponentCount() == 0)
        {
            JComponent jcomponent;
            for(Iterator iterator = componentsList4Form.iterator(); iterator.hasNext(); formWidgetPanel.add(jcomponent))
                jcomponent = (JComponent)iterator.next();

        }
        removeAll();
        add(formWidgetPanel, "Center");
    }

    private void checkEnable()
    {
        JComponent jcomponent;
        for(Iterator iterator = componentsList4Form.iterator(); iterator.hasNext(); jcomponent.setEnabled(!BaseUtils.isAuthorityEditing()))
            jcomponent = (JComponent)iterator.next();

    }

    public static FormDesigner getTarget()
    {
        return singleton.designer;
    }

    private void initFormButtons()
    {
        if(componentsList4Form.isEmpty())
        {
            componentsList4Form.add(new TitleLabel(Inter.getLocText("Form-Layout")));
            WidgetOption awidgetoption[] = FormWidgetOption.getFormContainerInstance();
            final WidgetOption wo[] = awidgetoption;
            int i = wo.length;
            for(int j = 0; j < i; j++)
            {
                WidgetOption widgetoption = wo[j];
                componentsList4Form.add(new ToolBarButton(widgetoption));
            }

            wo = DesignModuleFactory.getExtraWidgetOptions();
            if(wo != null && wo.length > 0)
            {
                componentsList4Form.add(new TitleLabel(Inter.getLocText("Chart")));
                UIButton uibutton = new UIButton(BaseUtils.readIcon("com/fr/design/images/toolbarbtn/chart.png"));
                uibutton.setToolTipText(Inter.getLocText("Click-Me"));
                uibutton.addMouseListener(new MouseAdapter() {

                    final WidgetOption val$wo[];
                    final WidgetToolBarPane this$0;

                    public void mouseClicked(MouseEvent mouseevent)
                    {
                        if(BaseUtils.isAuthorityEditing())
                            return;
                        if(chartWindow == null)
                            chartWindow = new FormWidgetPopWindow();
                        chartWindow.showToolTip(mouseevent.getXOnScreen() + 20, mouseevent.getYOnScreen() - 20, wo);
                    }

            
            {
                this$0 = WidgetToolBarPane.this;
                wo = awidgetoption;
                super();
            }
                }
);
                componentsList4Form.add(uibutton);
            }
        }
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(56, 2);
    }

    protected String title4PopupWindow()
    {
        return null;
    }



}
