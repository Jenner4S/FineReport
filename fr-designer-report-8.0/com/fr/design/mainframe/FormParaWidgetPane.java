// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.constants.UIConstants;
import com.fr.design.designer.beans.events.DesignerEditListener;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.gui.core.FormWidgetOption;
import com.fr.design.gui.core.UserDefinedWidgetOption;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.UserDefinedWidgetConfig;
import com.fr.form.ui.Widget;
import com.fr.form.ui.WidgetConfig;
import com.fr.form.ui.WidgetManager;
import com.fr.form.ui.WidgetManagerProvider;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.OperatingSystem;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JWindow;

// Referenced classes of package com.fr.design.mainframe:
//            ToolBarButton, DesignerContext, DesignerFrame, DesktopCardPane, 
//            FormDesigner, FormDesignerModeForSpecial, JTemplate, FormWidgetDetailPane, 
//            EastRegionContainerPane

public class FormParaWidgetPane extends JPanel
{
    private class EditorChoosePane extends JPanel
    {

        final FormParaWidgetPane this$0;

        public void paintComponent(Graphics g)
        {
            Rectangle rectangle = getBounds();
            g.setColor(UIConstants.NORMAL_BACKGROUND);
            g.fillRoundRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height, 0, 0);
            g.setColor(UIConstants.LINE_COLOR);
            g.drawLine(rectangle.x, rectangle.y, rectangle.x, rectangle.y + rectangle.height);
            g.drawLine(rectangle.x, (rectangle.y + rectangle.height) - 1, (rectangle.x + rectangle.width) - 1, (rectangle.y + rectangle.height) - 1);
            g.drawLine((rectangle.x + rectangle.width) - 1, rectangle.y, (rectangle.x + rectangle.width) - 1, (rectangle.y + rectangle.height) - 1);
        }

        public EditorChoosePane()
        {
            this$0 = FormParaWidgetPane.this;
            super();
            ((FlowLayout)getLayout()).setVgap(1);
        }
    }

    private class PopUpWindow extends JWindow
    {

        private JPanel northPane;
        private String typeName;
        private int LineWidth;
        private int BarWidth;
        private AWTEventListener awt;
        final FormParaWidgetPane this$0;

        protected JPanel initComponents()
        {
            EditorChoosePane editorchoosepane = new EditorChoosePane();
            JPanel jpanel = new JPanel();
            jpanel.setLayout(new BorderLayout(17, 0));
            jpanel.add(northPane, "Center");
            JPanel jpanel1 = new JPanel(new BorderLayout());
            jpanel1.add(new UILabel(typeName, 0), "Center");
            UIButton uibutton = createPopDownButton();
            uibutton.addMouseListener(new MouseAdapter() {

                final PopUpWindow this$1;

                public void mouseClicked(MouseEvent mouseevent)
                {
                    setVisible(false);
                }

                
                {
                    this$1 = PopUpWindow.this;
                    super();
                }
            }
);
            jpanel1.add(uibutton, "East");
            jpanel.add(jpanel1, "South");
            editorchoosepane.add(jpanel, "Center");
            return editorchoosepane;
        }




        public PopUpWindow(JPanel jpanel, String s)
        {
            this$0 = FormParaWidgetPane.this;
            super();
            LineWidth = 5;
            BarWidth = 10;
            awt = new AWTEventListener() {

                final PopUpWindow this$1;

                public void eventDispatched(AWTEvent awtevent)
                {
                    if(awtevent instanceof MouseEvent)
                    {
                        MouseEvent mouseevent = (MouseEvent)awtevent;
                        Point point = mouseevent.getLocationOnScreen();
                        double d = getX() + northPane.getWidth() + LineWidth;
                        double d1 = d - (double)BarWidth;
                        double d2 = getY() + northPane.getY();
                        double d3 = d2 + (double)northPane.getHeight();
                        boolean flag = d1 < point.getX() && d > point.getX() && d3 > point.getY();
                        if(!flag && mouseevent.getClickCount() > 0 && mouseevent.getID() != 502 && !ComparatorUtils.equals(mouseevent.getSource(), PopUpWindow.this) && !OperatingSystem.isMacOS())
                            setVisible(false);
                    }
                }

                
                {
                    this$1 = PopUpWindow.this;
                    super();
                }
            }
;
            northPane = jpanel;
            typeName = s;
            getContentPane().add(initComponents());
            doLayout();
            Toolkit.getDefaultToolkit().addAWTEventListener(awt, 16L);
        }
    }

    public class paraButtonDesignerAdapter
        implements DesignerEditListener
    {

        ToolBarButton button;
        final FormParaWidgetPane this$0;

        public void fireCreatorModified(DesignerEvent designerevent)
        {
            button.setEnabled(designer.getParaComponent() == null);
        }

        public paraButtonDesignerAdapter(ToolBarButton toolbarbutton)
        {
            this$0 = FormParaWidgetPane.this;
            super();
            button = toolbarbutton;
        }
    }

    private class paraButton extends ToolBarButton
    {

        final FormParaWidgetPane this$0;

        public void mouseDragged(MouseEvent mouseevent)
        {
            if(designer.getParaComponent() != null)
                return;
            designer.addParaComponent();
            FormWidgetDetailPane formwidgetdetailpane = FormWidgetDetailPane.getInstance(designer);
            EastRegionContainerPane.getInstance().replaceDownPane(formwidgetdetailpane);
            setEnabled(false);
            designer.addDesignerEditListener(new paraButtonDesignerAdapter(this));
            JTemplate jtemplate = DesignerContext.getDesignerFrame().getSelectedJTemplate();
            if(jtemplate != null)
                jtemplate.fireTargetModified();
        }

        public void setEnabled(boolean flag)
        {
            super.setEnabled(flag);
            paraLabel.setForeground(flag ? Color.BLACK : new Color(198, 198, 198));
        }

        public paraButton(WidgetOption widgetoption)
        {
            this$0 = FormParaWidgetPane.this;
            super(widgetoption);
            setDisabledIcon(BaseUtils.readIcon("/com/fr/web/images/form/resources/layout_parameter2.png"));
            if(designer != null)
                setEnabled(designer.getParaComponent() == null);
        }
    }


    private static FormParaWidgetPane THIS;
    private java.util.List predifinedwidgeList;
    private JWindow chartTypeWindow;
    private JWindow widgetTypeWindow;
    private WidgetOption widgetOptions[];
    private WidgetOption chartOptions[];
    private WidgetOption layoutOptions[];
    private int widgetButtonWidth;
    private int widgetButtonHeight;
    private int smallGAP;
    private int jsparatorWidth;
    private int jsparatorHeight;
    private int preWidgetShowMaxNum;
    private int preWidgetShowMaxRow;
    private int commonChartNum;
    private int commonWidgetNum;
    private JSeparator jSeparatorPara;
    private JSeparator jSeparatorChart;
    private JSeparator jSeparatorLayout;
    private UILabel paraLabel;
    private FormDesigner designer;

    public static final FormParaWidgetPane getInstance(FormDesigner formdesigner)
    {
        if(THIS == null)
            THIS = new FormParaWidgetPane();
        THIS.designer = formdesigner;
        THIS.setTarget(formdesigner);
        return THIS;
    }

    public FormParaWidgetPane()
    {
        predifinedwidgeList = new ArrayList();
        widgetOptions = null;
        chartOptions = null;
        layoutOptions = null;
        widgetButtonWidth = 22;
        widgetButtonHeight = 20;
        smallGAP = 6;
        jsparatorWidth = 2;
        jsparatorHeight = 50;
        preWidgetShowMaxNum = 3;
        preWidgetShowMaxRow = 20;
        commonChartNum = 8;
        commonWidgetNum = 10;
        setLayout(new FlowLayout(0));
        DesignerContext.getDesignerFrame().getCenterTemplateCardPane().addComponentListener(new ComponentAdapter() {

            final FormParaWidgetPane this$0;

            public void componentResized(ComponentEvent componentevent)
            {
                if(getParent() != null)
                {
                    JPanel jpanel = (JPanel)getParent();
                    int i = 0;
                    for(int j = 0; j < jpanel.getComponentCount() - 1; j++)
                        i += jpanel.getComponent(j).getWidth();

                    if(i == 0)
                        return;
                    Dimension dimension = jpanel.getSize();
                    setPreferredSize(new Dimension(dimension.width - i, dimension.height));
                    LayoutUtils.layoutContainer(jpanel);
                }
            }

            
            {
                this$0 = FormParaWidgetPane.this;
                super();
            }
        }
);
        initFormParaComponent();
    }

    private void initFormParaComponent()
    {
        removeAll();
        JPanel jpanel = new JPanel(new FlowLayout());
        jpanel.add(new ToolBarButton(FormWidgetOption.ELEMENTCASE));
        add(createNormalCombinationPane(jpanel, Inter.getLocText("FR-Designer-Form-ToolBar_Report")));
        add(createJSeparator());
        JPanel jpanel1 = new JPanel(new FlowLayout());
        paraButton parabutton = new paraButton(FormWidgetOption.PARAMETERCONTAINER);
        jpanel1.add(parabutton);
        add(createNormalCombinationPane(jpanel1, Inter.getLocText("FR-Designer_Parameter")));
        jSeparatorPara = createJSeparator();
        add(jSeparatorPara);
        JPanel jpanel2 = new JPanel(new FlowLayout());
        WidgetOption awidgetoption[] = loadLayoutOptions();
        int j = awidgetoption.length;
        for(int k = 0; k < j; k++)
        {
            WidgetOption widgetoption = awidgetoption[k];
            jpanel2.add(new ToolBarButton(widgetoption));
        }

        add(createNormalCombinationPane(jpanel2, Inter.getLocText("FR-Designer_Layout")));
        jSeparatorLayout = createJSeparator();
        add(jSeparatorLayout);
        int i = loadChartOptions().length;
        if(i > 0)
        {
            commonChartNum = ++i / 2;
            JPanel jpanel3 = new JPanel(new FlowLayout());
            for(int l = 0; l < commonChartNum; l++)
                jpanel3.add(new ToolBarButton(loadChartOptions()[l]));

            add(createChartCombinationPane(jpanel3, Inter.getLocText("FR-Designer-Form-ToolBar_Chart")));
            jSeparatorChart = createJSeparator();
            add(jSeparatorChart);
        }
        JPanel jpanel4 = new JPanel(new FlowLayout());
        for(int i1 = 0; i1 < commonWidgetNum; i1++)
            jpanel4.add(new ToolBarButton(loadWidgetOptions()[i1]));

        jpanel4.add(createJSeparator(20D));
        loadPredefinedWidget();
        int j1 = Math.min(predifinedwidgeList.size(), preWidgetShowMaxNum);
        for(int k1 = 0; k1 < j1; k1++)
            jpanel4.add(new ToolBarButton((WidgetOption)predifinedwidgeList.get(k1)));

        add(createWidgetCombinationPane(jpanel4, Inter.getLocText("FR-Designer-Form-ToolBar_Widget")));
        add(createJSeparator());
    }

    private void loadPredefinedWidget()
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
            initFormParaComponent();
            return;
        }
    }

    private JPanel createNormalCombinationPane(JComponent jcomponent, String s)
    {
        JPanel jpanel = new JPanel(new BorderLayout(17, 5));
        jpanel.add(jcomponent, "Center");
        JPanel jpanel1 = new JPanel(new BorderLayout());
        UILabel uilabel = new UILabel(s, 0);
        if(ComparatorUtils.equals(Inter.getLocText("FR-Designer_Parameter"), s))
            paraLabel = uilabel;
        jpanel1.add(uilabel, "Center");
        jpanel.add(jpanel1, "South");
        jpanel.setPreferredSize(new Dimension((int)jcomponent.getPreferredSize().getWidth(), (int)jpanel.getPreferredSize().getHeight()));
        return jpanel;
    }

    private JPanel createChartCombinationPane(JComponent jcomponent, String s)
    {
        JPanel jpanel = new JPanel(new BorderLayout(17, 5));
        jpanel.add(jcomponent, "Center");
        JPanel jpanel1 = new JPanel(new BorderLayout());
        jpanel1.add(new UILabel(s, 0), "Center");
        UIButton uibutton = createPopUpButton();
        uibutton.addMouseListener(new MouseAdapter() {

            final FormParaWidgetPane this$0;

            public void mouseClicked(MouseEvent mouseevent)
            {
                if(chartTypeWindow == null)
                {
                    JPanel jpanel2 = new JPanel(new FlowLayout(0));
                    WidgetOption awidgetoption[] = loadChartOptions();
                    int j = awidgetoption.length;
                    for(int k = 0; k < j; k++)
                    {
                        WidgetOption widgetoption = awidgetoption[k];
                        jpanel2.add(new ToolBarButton(widgetoption));
                    }

                    int i = commonChartNum * (widgetButtonWidth + smallGAP);
                    j = (int)Math.ceil((double)loadWidgetOptions().length / (double)commonChartNum) * (widgetButtonHeight + smallGAP);
                    jpanel2.setPreferredSize(new Dimension(i, j));
                    chartTypeWindow = new PopUpWindow(jpanel2, Inter.getLocText("FR-Designer-Form-ToolBar_Chart"));
                    chartTypeWindow.setLocation((int)jSeparatorLayout.getLocationOnScreen().getX() + 1, (int)jSeparatorLayout.getLocationOnScreen().getY());
                    chartTypeWindow.setSize(chartTypeWindow.getPreferredSize());
                }
                chartTypeWindow.setVisible(true);
            }

            
            {
                this$0 = FormParaWidgetPane.this;
                super();
            }
        }
);
        jpanel1.add(uibutton, "East");
        jpanel.add(jpanel1, "South");
        return jpanel;
    }

    private JPanel createWidgetCombinationPane(JComponent jcomponent, String s)
    {
        JPanel jpanel = new JPanel(new BorderLayout(17, 5));
        jpanel.add(jcomponent, "Center");
        JPanel jpanel1 = new JPanel(new BorderLayout());
        jpanel1.add(new UILabel(s, 0), "Center");
        UIButton uibutton = createPopUpButton();
        uibutton.addMouseListener(new MouseAdapter() {

            final FormParaWidgetPane this$0;

            public void mouseClicked(MouseEvent mouseevent)
            {
                JPanel jpanel2 = new JPanel(new FlowLayout(0, 0, 0));
                loadPredefinedWidget();
                int i = calculateWidgetWindowRowNum();
                JPanel jpanel3 = new JPanel(new FlowLayout(0));
                WidgetOption awidgetoption[] = loadWidgetOptions();
                int k = awidgetoption.length;
                for(int l = 0; l < k; l++)
                {
                    WidgetOption widgetoption = awidgetoption[l];
                    jpanel3.add(new ToolBarButton(widgetoption));
                }

                int j = commonWidgetNum * (widgetButtonWidth + smallGAP);
                jpanel3.setPreferredSize(new Dimension(j, (int)((double)i * jpanel3.getPreferredSize().getHeight())));
                JPanel jpanel4 = new JPanel(new FlowLayout(0));
                WidgetOption widgetoption1;
                for(Iterator iterator = predifinedwidgeList.iterator(); iterator.hasNext(); jpanel4.add(new ToolBarButton(widgetoption1)))
                    widgetoption1 = (WidgetOption)iterator.next();

                int i1 = preWidgetShowMaxNum * (widgetButtonWidth + smallGAP);
                int j1 = predifinedwidgeList.size() < preWidgetShowMaxNum ? (int)jpanel4.getPreferredSize().getWidth() : i1;
                jpanel4.setPreferredSize(new Dimension(j1, (int)((double)i * jpanel4.getPreferredSize().getHeight())));
                UIScrollPane uiscrollpane = new UIScrollPane(jpanel4);
                uiscrollpane.setBorder(null);
                int k1 = preWidgetShowMaxRow * (widgetButtonHeight + smallGAP);
                int l1 = predifinedwidgeList.size() < preWidgetShowMaxNum * preWidgetShowMaxRow ? (int)jpanel4.getPreferredSize().getHeight() : k1;
                j1 = predifinedwidgeList.size() < preWidgetShowMaxNum * preWidgetShowMaxRow ? (int)jpanel4.getPreferredSize().getWidth() : (int)jpanel4.getPreferredSize().getWidth() + smallGAP + jsparatorWidth;
                uiscrollpane.setPreferredSize(new Dimension(j1, l1));
                jpanel2.add(jpanel3);
                jpanel2.add(createJSeparator(l1));
                jpanel2.add(uiscrollpane);
                widgetTypeWindow = new PopUpWindow(jpanel2, Inter.getLocText("FR-Designer-Form-ToolBar_Widget"));
                widgetTypeWindow.setSize(widgetTypeWindow.getPreferredSize());
                if(jSeparatorChart != null)
                    widgetTypeWindow.setLocation((int)jSeparatorChart.getLocationOnScreen().getX() + 1, (int)jSeparatorChart.getLocationOnScreen().getY());
                widgetTypeWindow.setVisible(true);
            }

            
            {
                this$0 = FormParaWidgetPane.this;
                super();
            }
        }
);
        jpanel1.add(uibutton, "East");
        jpanel.add(jpanel1, "South");
        return jpanel;
    }

    private int calculateWidgetWindowRowNum()
    {
        int i = (int)Math.ceil((double)predifinedwidgeList.size() / (double)preWidgetShowMaxNum);
        i = Math.max(i, 2);
        i = Math.min(i, preWidgetShowMaxRow);
        return i;
    }

    private JSeparator createJSeparator()
    {
        JSeparator jseparator = new JSeparator(1);
        jseparator.setPreferredSize(new Dimension(jsparatorWidth, jsparatorHeight));
        return jseparator;
    }

    private JSeparator createJSeparator(double d)
    {
        JSeparator jseparator = new JSeparator(1);
        jseparator.setPreferredSize(new Dimension(jsparatorWidth, (int)d));
        return jseparator;
    }

    private UIButton createPopUpButton()
    {
        UIButton uibutton = new UIButton(BaseUtils.readIcon("com/fr/design/images/buttonicon/arrowdown.png"));
        uibutton.set4ToolbarButton();
        return uibutton;
    }

    private UIButton createPopDownButton()
    {
        UIButton uibutton = new UIButton(BaseUtils.readIcon("com/fr/design/images/buttonicon/arrowup.png"));
        uibutton.set4ToolbarButton();
        return uibutton;
    }

    private WidgetOption[] loadWidgetOptions()
    {
        if(widgetOptions == null)
            widgetOptions = (WidgetOption[])(WidgetOption[])ArrayUtils.addAll(WidgetOption.getFormWidgetIntance(), ExtraDesignClassManager.getInstance().getFormWidgetOptions());
        return widgetOptions;
    }

    private WidgetOption[] loadLayoutOptions()
    {
        if(layoutOptions == null)
            layoutOptions = (WidgetOption[])(WidgetOption[])ArrayUtils.addAll(FormWidgetOption.getFormLayoutInstance(), ExtraDesignClassManager.getInstance().getFormWidgetContainerOptions());
        return layoutOptions;
    }

    private WidgetOption[] loadChartOptions()
    {
        if(chartOptions == null)
            chartOptions = DesignModuleFactory.getExtraWidgetOptions();
        return chartOptions;
    }























}
