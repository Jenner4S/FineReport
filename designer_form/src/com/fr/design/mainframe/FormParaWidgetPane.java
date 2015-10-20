package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.constants.UIConstants;
import com.fr.design.designer.beans.events.DesignerEditListener;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.gui.core.FormWidgetOption;
import com.fr.design.gui.core.UserDefinedWidgetOption;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.*;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FormParaWidgetPane extends JPanel{
	private static FormParaWidgetPane THIS;
	private List<WidgetOption> predifinedwidgeList = new ArrayList<WidgetOption>();

    private JWindow chartTypeWindow;
    private JWindow widgetTypeWindow;
    private WidgetOption[] widgetOptions = null;
    private WidgetOption[] chartOptions =  null;
    private WidgetOption[] layoutOptions = null;
    private int widgetButtonWidth = 22;
    private int widgetButtonHeight = 20;
    private int smallGAP = 6;
    private int jsparatorWidth = 2;
    private int jsparatorHeight = 50;
    //Ԥ����ؼ�ÿ�������ʾ3��
    private int preWidgetShowMaxNum = 3;
    //Ԥ����ؼ������ʾ20��
    private int preWidgetShowMaxRow = 20;
    //��ʾ8��ͼ�����
    private int commonChartNum = 8;
    //��ʾ10����ͨ�ؼ�
    private int commonWidgetNum = 10;
    private JSeparator jSeparatorPara;
    private JSeparator jSeparatorChart;
    private JSeparator jSeparatorLayout;

    private UILabel paraLabel ;

	private FormDesigner designer;
	
	public static final FormParaWidgetPane getInstance(FormDesigner designer) {
		if(THIS == null) {
			THIS = new FormParaWidgetPane();
		}
		THIS.designer = designer;
		THIS.setTarget(designer);
		return THIS;
	}

	public FormParaWidgetPane() {
		setLayout(new FlowLayout(FlowLayout.LEFT));
        DesignerContext.getDesignerFrame().getCenterTemplateCardPane().addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
				if (FormParaWidgetPane.this.getParent() != null) {
                    JPanel fother = (JPanel)FormParaWidgetPane.this.getParent();
                    int delta_wdith = 0;
                    for (int i = 0; i < fother.getComponentCount() - 1; i ++) {
                        delta_wdith += fother.getComponent(i).getWidth();
                    }
                    
                    if(delta_wdith == 0){
                    	return;
                    }
                    
                    Dimension d = fother.getSize();
					setPreferredSize(new Dimension(d.width - delta_wdith, d.height));
					LayoutUtils.layoutContainer(fother);
				}
            }
        });
        initFormParaComponent();
	}


    private void initFormParaComponent() {
        this.removeAll();
        // �˵��еĲ�����ע�͵�

        JPanel reportPane = new JPanel(new FlowLayout());
        reportPane.add(new ToolBarButton(FormWidgetOption.ELEMENTCASE));
        add(createNormalCombinationPane(reportPane,Inter.getLocText("FR-Designer-Form-ToolBar_Report")));
        add(createJSeparator());

        JPanel paraPane = new JPanel(new FlowLayout());
        ToolBarButton paraButton = new paraButton(FormWidgetOption.PARAMETERCONTAINER);
        paraPane.add(paraButton);
        add(createNormalCombinationPane(paraPane,Inter.getLocText("FR-Designer_Parameter")));
        jSeparatorPara = createJSeparator();
        add(jSeparatorPara);
        
        JPanel layoutPane = new JPanel(new FlowLayout());
        for(WidgetOption option : loadLayoutOptions()){
            layoutPane.add(new ToolBarButton(option));
        }
        add(createNormalCombinationPane(layoutPane,Inter.getLocText("FR-Designer_Layout")));
        jSeparatorLayout = createJSeparator();
        add(jSeparatorLayout);

        // ��ʼ����ʱ�����ͼ����ܸ�����õ�����ʾͼ��ĸ���
        int totalChartNums = loadChartOptions().length;
        if (totalChartNums > 0) {
        	commonChartNum = ++totalChartNums/2;
            JPanel chartTypePane = new JPanel(new FlowLayout());
            for (int i = 0;i < commonChartNum ;i++) {
                chartTypePane.add( new ToolBarButton(loadChartOptions()[i]));
            }
            add(createChartCombinationPane(chartTypePane, Inter.getLocText("FR-Designer-Form-ToolBar_Chart")));
            jSeparatorChart = createJSeparator();
            add(jSeparatorChart);
        }

        JPanel widgetPane = new JPanel(new FlowLayout());
        for (int i = 0;i < commonWidgetNum;i++) {
            widgetPane.add(new ToolBarButton(loadWidgetOptions()[i]));
        }
        widgetPane.add(createJSeparator(20));
        loadPredefinedWidget();
        int num = Math.min(predifinedwidgeList.size(),preWidgetShowMaxNum);
        for (int i = 0 ;i < num ;i++) {
            widgetPane.add(new ToolBarButton(predifinedwidgeList.get(i)));
        }
        add(createWidgetCombinationPane(widgetPane, Inter.getLocText("FR-Designer-Form-ToolBar_Widget")));
        add(createJSeparator());
    }
    
	private void loadPredefinedWidget() {
		predifinedwidgeList.clear();
		if(designer != null) {
			WidgetOption[] designerPre = designer.getDesignerMode().getPredefinedWidgetOptions();
			for(int i = 0; i < designerPre.length; i++) {
				predifinedwidgeList.add(designerPre[i]);
			}
		}
		WidgetManagerProvider mgr = WidgetManager.getProviderInstance();
		Iterator<String> nameIt = mgr.getWidgetConfigNameIterator();
		while (nameIt.hasNext()) {
			String name = nameIt.next();
			WidgetConfig widgetConfig = mgr.getWidgetConfig(name);
			if (widgetConfig instanceof UserDefinedWidgetConfig) {
				Widget widget = ((UserDefinedWidgetConfig) widgetConfig).getWidget();
				String widgetClassName = widget.getClass().getName();
				if (isButtonWidget(widgetClassName)) {
					// ...
					continue;
				}
                if (!XCreatorUtils.createXCreator(widget).canEnterIntoParaPane()){
                    //Ԥ����ؼ��������������ʾ��������û�е�Ԥ����ؼ�
                    continue;
                }
				predifinedwidgeList.add(new UserDefinedWidgetOption(name));
			}
		}
	}

    private boolean isButtonWidget(String widgetClassName) {
        return widgetClassName.endsWith("DeleteRowButton") || widgetClassName.endsWith("AppendRowButton") || widgetClassName.endsWith("TreeNodeToogleButton");
    }

	private void setTarget(FormDesigner designer) {
		if (designer == null) {
			return;
		}
        initFormParaComponent();

	}

    private JPanel createNormalCombinationPane(JComponent jComponent,String typeName){
        JPanel reportPane = new JPanel(new BorderLayout(17,5));
        reportPane.add(jComponent,BorderLayout.CENTER);
        JPanel labelPane = new JPanel(new BorderLayout());
        UILabel label = new UILabel(typeName,UILabel.CENTER);
        if(ComparatorUtils.equals(Inter.getLocText("FR-Designer_Parameter"),typeName )){
            paraLabel = label;
        }
        labelPane.add(label,BorderLayout.CENTER);
        reportPane.add(labelPane,BorderLayout.SOUTH);
        reportPane.setPreferredSize(new Dimension((int)jComponent.getPreferredSize().getWidth(),(int)reportPane.getPreferredSize().getHeight()));
        return reportPane;
    }


    private JPanel createChartCombinationPane(JComponent jComponent,String typeName){
        JPanel chartPane = new JPanel(new BorderLayout(17,5));
        chartPane.add(jComponent, BorderLayout.CENTER);
        JPanel labelPane = new JPanel(new BorderLayout());
        labelPane.add(new UILabel(typeName,UILabel.CENTER),BorderLayout.CENTER);
        UIButton chartPopUpButton = createPopUpButton();
        chartPopUpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(chartTypeWindow == null) {
                    JPanel componentsPara = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    for (WidgetOption chartOption : loadChartOptions()) {
                        componentsPara.add( new ToolBarButton(chartOption));
                    }
                    int x = commonChartNum * (widgetButtonWidth + smallGAP);
                    int y = (int)Math.ceil(((double)loadWidgetOptions().length/(double)commonChartNum)) * (widgetButtonHeight + smallGAP);
                    componentsPara.setPreferredSize(new Dimension(x,y));
                    chartTypeWindow = new PopUpWindow(componentsPara, Inter.getLocText("FR-Designer-Form-ToolBar_Chart"));
                    chartTypeWindow.setLocation((int)jSeparatorLayout.getLocationOnScreen().getX() + 1, (int)jSeparatorLayout.getLocationOnScreen().getY());
                    chartTypeWindow.setSize(chartTypeWindow.getPreferredSize());
                }
                chartTypeWindow.setVisible(true);
            }
        });
        labelPane.add(chartPopUpButton,BorderLayout.EAST);
        chartPane.add(labelPane,BorderLayout.SOUTH);
        return chartPane;
    }

    private JPanel createWidgetCombinationPane(JComponent jComponent,String typeName){
        JPanel widgetPane = new JPanel(new BorderLayout(17,5));
        widgetPane.add(jComponent,BorderLayout.CENTER);
        JPanel labelPane = new JPanel(new BorderLayout());
        labelPane.add(new UILabel(typeName,UILabel.CENTER),BorderLayout.CENTER);
        UIButton chartPopUpButton = createPopUpButton();
        chartPopUpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel widgetPane = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
                loadPredefinedWidget();
                int rowNum = calculateWidgetWindowRowNum();
                JPanel westPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                for (WidgetOption o : loadWidgetOptions()) {
                    westPanel.add(new ToolBarButton(o));
                }
                int x = commonWidgetNum * (widgetButtonWidth + smallGAP);
                westPanel.setPreferredSize(new Dimension(x,(int)(rowNum * westPanel.getPreferredSize().getHeight())));
                JPanel eastPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
                for (WidgetOption no : predifinedwidgeList) {
                    eastPane.add(new ToolBarButton(no));
                }
                int maxWidth = preWidgetShowMaxNum * (widgetButtonWidth + smallGAP);
                int width = predifinedwidgeList.size() >= preWidgetShowMaxNum ? maxWidth:(int)eastPane.getPreferredSize().getWidth();
                eastPane.setPreferredSize(new Dimension(width,(int)(rowNum*eastPane.getPreferredSize().getHeight())));

                UIScrollPane eastScrollPane = new UIScrollPane(eastPane);
                eastScrollPane.setBorder(null);
                int maxHeight = preWidgetShowMaxRow * (widgetButtonHeight + smallGAP);
                int height = predifinedwidgeList.size() >= preWidgetShowMaxNum * preWidgetShowMaxRow ? maxHeight : (int)eastPane.getPreferredSize().getHeight();
                width =  predifinedwidgeList.size() >= preWidgetShowMaxNum * preWidgetShowMaxRow ? (int)eastPane.getPreferredSize().getWidth() + smallGAP + jsparatorWidth : (int)eastPane.getPreferredSize().getWidth();
                eastScrollPane.setPreferredSize(new Dimension(width,height));

                widgetPane.add(westPanel);
                widgetPane.add(createJSeparator(height));
                widgetPane.add(eastScrollPane);

                widgetTypeWindow = new PopUpWindow( widgetPane, Inter.getLocText("FR-Designer-Form-ToolBar_Widget"));
                widgetTypeWindow.setSize(widgetTypeWindow.getPreferredSize());
                if (jSeparatorChart != null) {
                    widgetTypeWindow.setLocation((int) jSeparatorChart.getLocationOnScreen().getX() + 1, (int) jSeparatorChart.getLocationOnScreen().getY());
                }
                widgetTypeWindow.setVisible(true);
            }

        });
        labelPane.add(chartPopUpButton,BorderLayout.EAST);
        widgetPane.add(labelPane,BorderLayout.SOUTH);
        return widgetPane;
    }

    private int calculateWidgetWindowRowNum(){
        //����ȡ��
        int rowNum = (int)Math.ceil((double)predifinedwidgeList.size()/(double)preWidgetShowMaxNum);
        rowNum = Math.max(rowNum,2);
        rowNum = Math.min(rowNum,preWidgetShowMaxRow);
        return rowNum;
    }


    private JSeparator createJSeparator(){
        JSeparator jSeparator = new JSeparator(SwingConstants.VERTICAL );
        jSeparator.setPreferredSize(new Dimension(jsparatorWidth,jsparatorHeight));
        return  jSeparator;
    }

    private JSeparator createJSeparator(double height){
        JSeparator jSeparator = new JSeparator(SwingConstants.VERTICAL );
        jSeparator.setPreferredSize(new Dimension(jsparatorWidth,(int)height));
        return  jSeparator;
    }

    private UIButton createPopUpButton(){
        UIButton popUpButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/buttonicon/arrowdown.png"));
        popUpButton.set4ToolbarButton();
        return popUpButton;
    }

    private UIButton createPopDownButton(){
        UIButton popUpButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/buttonicon/arrowup.png"));
        popUpButton.set4ToolbarButton();
        return popUpButton;
    }

    private class paraButton extends ToolBarButton{
        public paraButton(WidgetOption no){
            super(no);
            this.setDisabledIcon(BaseUtils.readIcon("/com/fr/web/images/form/resources/layout_parameter2.png"));
            if(designer != null){
                this.setEnabled(designer.getParaComponent() == null);
            }
        }
        public void mouseDragged(MouseEvent e) {
            if (designer.getParaComponent() != null){
            	return;
            }

            designer.addParaComponent();
            JPanel pane =FormWidgetDetailPane.getInstance(designer);
            EastRegionContainerPane.getInstance().replaceDownPane(pane);
            this.setEnabled(false);

            designer.addDesignerEditListener(new paraButtonDesignerAdapter(this));

            JTemplate targetComponent = DesignerContext.getDesignerFrame().getSelectedJTemplate();
            if (targetComponent != null) {
                targetComponent.fireTargetModified();
            }
        }

        public void setEnabled(boolean b) {
            super.setEnabled(b);
            paraLabel.setForeground(b ? Color.BLACK : new Color(198,198,198));
        }

    }

    public class paraButtonDesignerAdapter implements DesignerEditListener {
        ToolBarButton button;

        public paraButtonDesignerAdapter(ToolBarButton button) {
            this.button = button;
        }

        /**
         *  ��Ӧ����ı��¼�
         * @param evt  �¼�
         */
        public void fireCreatorModified(DesignerEvent evt) {
            button.setEnabled(designer.getParaComponent() == null);
        }
    }



    private class PopUpWindow extends JWindow {
        private JPanel northPane;
        private String typeName;
        private int LineWidth = 5;
        private int BarWidth = 10;

        public PopUpWindow(JPanel northPane,String typeName){
            super();
            this.northPane = northPane;
            this.typeName = typeName;
            this.getContentPane().add(initComponents());
            this.doLayout();
            Toolkit.getDefaultToolkit().addAWTEventListener(awt, AWTEvent.MOUSE_EVENT_MASK);
        }

        private AWTEventListener awt = new AWTEventListener() {
            public void eventDispatched(AWTEvent event) {
                if (event instanceof MouseEvent) {
                    MouseEvent mv = (MouseEvent) event;
                    Point point = mv.getLocationOnScreen();
                    double endX = PopUpWindow.this.getX() + northPane.getWidth() + LineWidth;
                    double startX = endX - BarWidth;
                    double startY = PopUpWindow.this.getY() + northPane.getY();
                    double endY = startY + northPane.getHeight();
                    boolean dragBar = startX < point.getX() && endX > point.getX() && endY > point.getY();
                    if (!dragBar && mv.getClickCount() > 0 && mv.getID() != MouseEvent.MOUSE_RELEASED) {
                        if(!ComparatorUtils.equals(mv.getSource(), PopUpWindow.this)) {
                            PopUpWindow.this.setVisible(false);
                        }
                    }
                }
            }
        };


        protected JPanel initComponents() {
            JPanel rootPane = new EditorChoosePane();
            JPanel contentPane = new JPanel();
            contentPane.setLayout(new BorderLayout(17, 0));
            contentPane.add(northPane,BorderLayout.CENTER);
            JPanel labelPane = new JPanel(new BorderLayout());
            labelPane.add(new UILabel(typeName,UILabel.CENTER),BorderLayout.CENTER);
            JButton popUpButton = createPopDownButton();
            popUpButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    PopUpWindow.this.setVisible(false);
                }
            });
            labelPane.add(popUpButton, BorderLayout.EAST);
            contentPane.add(labelPane,BorderLayout.SOUTH);
            rootPane.add(contentPane,BorderLayout.CENTER);
            return rootPane;

        }


    }

    private class EditorChoosePane extends JPanel {
        public EditorChoosePane() {
            super();
            ((FlowLayout)this.getLayout()).setVgap(1);
        }

        @Override
        public void paintComponent(Graphics g) {
            Rectangle r = this.getBounds();
            g.setColor(UIConstants.NORMAL_BACKGROUND);
            g.fillRoundRect(r.x, r.y, r.width, r.height,0,0);
            g.setColor(UIConstants.LINE_COLOR);
            g.drawLine(r.x, r.y, r.x, r.y+r.height);
            g.drawLine(r.x, r.y+r.height-1, r.x+r.width-1, r.y+r.height-1);
            g.drawLine(r.x+r.width-1, r.y, r.x+r.width-1, r.y+r.height-1);
        }
    }

    private WidgetOption[] loadWidgetOptions() {
        if (widgetOptions == null) {
            widgetOptions = (WidgetOption[])ArrayUtils.addAll(WidgetOption.getFormWidgetIntance(), ExtraDesignClassManager.getInstance().getFormWidgetOptions());
        }
        return widgetOptions;
    }

    private WidgetOption[] loadLayoutOptions() {
        if (layoutOptions == null) {
            layoutOptions = (WidgetOption[])ArrayUtils.addAll(FormWidgetOption.getFormLayoutInstance(), ExtraDesignClassManager.getInstance().getFormWidgetContainerOptions());
        }
        return layoutOptions;
    }

    private WidgetOption[] loadChartOptions() {
        if (chartOptions == null) {
            chartOptions = DesignModuleFactory.getExtraWidgetOptions();
        }
        return chartOptions;
    }
}
