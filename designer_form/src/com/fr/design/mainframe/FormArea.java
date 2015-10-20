package com.fr.design.mainframe;

import java.awt.AWTEvent;
import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWBorderLayout;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.frpane.UINumberSlidePane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.scrollruler.BaseRuler;
import com.fr.design.scrollruler.HorizontalRuler;
import com.fr.design.scrollruler.RulerLayout;
import com.fr.design.scrollruler.ScrollRulerComponent;
import com.fr.design.scrollruler.VerticalRuler;
import com.fr.design.utils.ComponentUtils;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.general.FRScreen;
import com.fr.general.Inter;

public class FormArea extends JComponent implements ScrollRulerComponent {

	private static final double SLIDER_FLOAT = 120.0;
	private static final double SLIDER_MIN = 60.0;
	public static final double DEFAULT_SLIDER = 100.0;
    private static final int ROTATIONS = 50;
    private FormDesigner designer;
    private int horizontalValue = 0;
    private int verticalValue = 0;
    private int verticalMax = 0;
    private int horicalMax = 0;
    private FormScrollBar verScrollBar;
	private FormScrollBar horScrollBar;
    //��ʾ�����ñ������С�Ŀؼ�
    private UINumberField widthPane;
    private UINumberField heightPane;
    private UINumberSlidePane slidePane;
    private boolean isValid = true;
    // ��ʼʱ����ֵΪ100���ж����ֵ��ΪSTART_VALUE;
    private double START_VALUE = DEFAULT_SLIDER;
    private double screenValue;
    
    public FormScrollBar getHorScrollBar() {
		return horScrollBar;
	}

	public void setHorScrollBar(FormScrollBar horScrollBar) {
		this.horScrollBar = horScrollBar;
	}

    public FormArea(FormDesigner designer) {
        this(designer, true);
    }

    public FormArea(FormDesigner designer, boolean useScrollBar) {
        this.designer = designer;
        this.designer.setParent(this);
        isValid = useScrollBar;
        verScrollBar = new FormScrollBar(Adjustable.VERTICAL, this);
        horScrollBar = new FormScrollBar(Adjustable.HORIZONTAL, this);
        if (useScrollBar) {
            this.setLayout(new FormRulerLayout());
            designer.setBorder(new LineBorder(new Color(198,198,198)));
            this.add(FormRulerLayout.CENTER, designer);
            addFormSize();
            this.add(FormRulerLayout.VERTICAL, verScrollBar);
            this.add(FormRulerLayout.HIRIZONTAL, horScrollBar);
            enableEvents(AWTEvent.MOUSE_WHEEL_EVENT_MASK);
        }  else {
        	// �����������ֻҪ��ߺ�����pane
            this.setLayout(new RulerLayout());
            this.add(RulerLayout.CENTER, designer);
            addFormRuler();
        }
        this.setFocusTraversalKeysEnabled(false);
    }
    
    /**
     * ���ӱ���ҳ���С���ƽ��棬�����ֶ��޸ĺͻ����϶�
     */
    private void addFormSize() {
    	double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		double[] rowSize = {f};
		double[] columnSize = { p, f, p, p, p, p, p, f, p};
		UILabel tipsPane = new UILabel("form");
		tipsPane.setPreferredSize(new Dimension(200, 0));
    	widthPane = new UINumberField();
    	widthPane.setPreferredSize(new Dimension(60, 0));
    	heightPane = new UINumberField();
    	heightPane.setPreferredSize(new Dimension(60, 0));
    	slidePane = new UINumberSlidePane(SLIDER_MIN, SLIDER_FLOAT);
    	slidePane.setPreferredSize(new Dimension(200,0));
    	JPanel resizePane =TableLayoutHelper.createCommonTableLayoutPane(new JComponent[][]{
                {tipsPane, new UILabel(), widthPane, new UILabel(Inter.getLocText("FR-Designer_Indent-Pixel")), new UILabel("x"),
                heightPane, new UILabel(Inter.getLocText("FR-Designer_Indent-Pixel")), new UILabel(), slidePane}},
                rowSize, columnSize, 8);
    	this.add(FormRulerLayout.BOTTOM, resizePane);
    	setWidgetsConfig();
    	// �ȳ�ʼ�����鼰��Ӧ�¼���Ȼ���ȡ�ֱ��ʵ�����������ʾ��С
    	slidePane.setEnabled(false);
    	slidePane.setVisible(false);
//    	initTransparent();
    	initCalculateSize();
    }
    
    private void setWidgetsConfig() {
    	widthPane.setHorizontalAlignment(widthPane.CENTER);
    	heightPane.setHorizontalAlignment(heightPane.CENTER);
    	widthPane.setMaxDecimalLength(0);
    	heightPane.setMaxDecimalLength(0);
    	//�ؼ���ʼֵ���Ǹ��ڵ������ʼ�Ŀ�͸�
    	widthPane.setValue(designer.getRootComponent().getWidth());
    	heightPane.setValue(designer.getRootComponent().getHeight());
    	addWidthPaneListener();
    	addHeightPaneListener();
    }
    
    private void initTransparent() {
    	initCalculateSize();
    	slidePane.addChangeListener(new ChangeListener() {
    		public void stateChanged(ChangeEvent e) {
    			double value = ((UINumberSlidePane) e.getSource()).getValue();
    			reCalculateRoot(value, true);
    			JTemplate form = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
    			if(form != null){
    				form.fireTargetModified();
    			}
    		}
    	});
    }
    
    /**
     *  ���ص�ǰ����Ļ�ֱ��ʶ�Ӧ�İٷֱ�ֵ
     * @return ���ŵİٷֱ�ֵ
     */
    public double getScreenValue() {
		return screenValue;
	}

    /**
     * ������Ļ�ֱ��ʶ�Ӧ�İٷֱ�ֵ
     * @param screenValue �ٷֱ�ֵ
     */
	public void setScreenValue(double screenValue) {
		this.screenValue = screenValue;
	}
    
    private void initCalculateSize() {
    	Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension scrnsize = toolkit.getScreenSize();
        this.screenValue = FRScreen.getByDimension(scrnsize).getValue();
        XLayoutContainer root = FormArea.this.designer.getRootComponent();
        // 7.1.1�������Ż��飬������С�԰���Ļ�ֱ��ʵ���
//        slidePane.populateBean(screenValue);
        if (root.acceptType(XWFitLayout.class)) {
	    	XWFitLayout layout = (XWFitLayout) root;
	        if (screenValue != DEFAULT_SLIDER ) {
	        	reCalculateRoot(screenValue, true);
	        } else {
	        	// ��������
	        	int val = layout.getAcualInterval();
	        	layout.addCompInterval(val);
	        }
		}
		LayoutUtils.layoutContainer(root);
    }
    
    //���ÿ�ȵĿؼ�����Ӧ�¼�
    private void addWidthPaneListener() {
    	widthPane.addActionListener(
    			new ActionListener() { 
    				public void actionPerformed(ActionEvent evt) {
    					int width = (int) ((UINumberField) evt.getSource()).getValue();
    					changeWidthPaneValue(width);
			        }
			});
    	widthPane.addFocusListener(
    			new FocusAdapter(){
		    	    public void focusLost(FocusEvent e){
		    	         // ʧȥ����ʱ��������Ϊ�������
		    	    	int width = (int) ((UINumberField) e.getSource()).getValue();
		    	    	changeWidthPaneValue(width);
		    	    }
    	});
    }
    
    private void changeWidthPaneValue(int width) {
    	XWFitLayout layout = (XWFitLayout) designer.getRootComponent();
    	if (width != layout.toData().getContainerWidth()) {
    		reCalculateWidth(width);
	    	designer.getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_EDITED);
    	}
    }
    
    private void addHeightPaneListener() {
    	heightPane.addActionListener(
    			new ActionListener() { 
    				public void actionPerformed(ActionEvent evt) {
    					int height = (int) ((UINumberField) evt.getSource()).getValue();
    					changeHeightPaneValue(height);
			        }
			});
    	heightPane.addFocusListener(
    			new FocusAdapter(){
		    	    public void focusLost(FocusEvent e){
		    	         // ʧȥ����ʱ��������Ϊ�������
		    	    	int height = (int) ((UINumberField) e.getSource()).getValue();
		    	    	changeHeightPaneValue(height);
		    	    }
    	});
    }
    
    private void changeHeightPaneValue(int height) {
    	XWFitLayout layout = (XWFitLayout) designer.getRootComponent();
    	if (height != layout.toData().getContainerHeight()) {
    		reCalculateHeight(height);
	    	designer.getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_EDITED);
    	}
    }
    
    private void reCalculateWidth(int width) {
    	XLayoutContainer root = FormArea.this.designer.getRootComponent();
		if (root.acceptType(XWFitLayout.class)) {
			XWFitLayout layout = (XWFitLayout) root;
			Dimension d = new Dimension(layout.toData().getContainerWidth(), layout.toData().getContainerHeight());
			Rectangle rec = new Rectangle(d);
			// ������С�ı�ʱ������backupBoundΪ��֮ǰ��ʵ�ʴ�С,�Ա�������߽�����
			layout.setBackupBound(rec);
			int dW = width-rec.width;
			if (dW == 0) {
				return;
			}
			double percent = (double) dW/rec.width;
			if (percent < 0 && !layout.canReduce(percent)) {
				widthPane.setValue(rec.width);
				return;
			}
			// �������������СΪʵ�ʵĸߺ͵�ǰ�Ŀ�Ȼ�󰴴˵����ڲ������
			layout.setSize(width, rec.height);
			layout.adjustCreatorsWidth(percent);
			if (layout.getNeedAddWidth() > 0) {
				widthPane.setValue(layout.getWidth());
				// ����֮������������������ٴ����ŵ���Сֵ���������
				layout.setNeedAddWidth(0);
			}
			doReCalculateRoot(width, rec.height, layout);
		}
    }
    
    private void reCalculateHeight(int height) {
    	XLayoutContainer root = FormArea.this.designer.getRootComponent();
		if (root.acceptType(XWFitLayout.class)) {
			XWFitLayout layout = (XWFitLayout) root;
			Dimension d = new Dimension(layout.toData().getContainerWidth(), layout.toData().getContainerHeight());
			Rectangle rec = new Rectangle(d);
			// ������С�ı�ʱ������backupBoundΪ��֮ǰ��ʵ�ʴ�С
			layout.setBackupBound(rec);
			int dH = height - rec.height;
			if (dH == 0) {
				return;
			}
			double percent = (double) dH/rec.height;
			if (percent < 0 && !layout.canReduce(percent)) {
				heightPane.setValue(rec.height);
				return;
			}
			layout.setSize(rec.width, height);
			layout.adjustCreatorsHeight(percent);
			if (layout.getNeedAddHeight() > 0) {
				heightPane.setValue(layout.getHeight());
				// ����֮������������������ٴ����ŵ���Сֵ���������
				layout.setNeedAddHeight(0);
			}
			doReCalculateRoot(rec.width, height, layout);
		}
    }
    
    /**
     * �޸Ĵ�С���ٸ�����Ļ�ֱ��ʵ�����
     */
    private void doReCalculateRoot(int width, int height, XWFitLayout layout) {
//    	double value = slidePane.updateBean();
    	//���û����ֵΪĬ��ֵ100
    	START_VALUE = DEFAULT_SLIDER;
		if (screenValue == DEFAULT_SLIDER) {
			layout.getParent().setSize(width, height+designer.getParaHeight());
			FormArea.this.validate();
		} else {
			layout.setBackupGap(screenValue/DEFAULT_SLIDER);
			reCalculateRoot(screenValue, false);
		}
    }
    
    /**
     * ���ս����С�İٷֱ�ֵ����root��С
     * @param needCalculateParaHeight �Ƿ���Ҫ������������߶�
     * @param value
     */
    private void reCalculateRoot(double value, boolean needCalculateParaHeight) {
    	if (value == START_VALUE) {
			return;
		}
		double percent = (value - START_VALUE) / START_VALUE;
		XLayoutContainer root = FormArea.this.designer.getRootComponent();
		if (root.acceptType(XWFitLayout.class)) {
			XWFitLayout layout = (XWFitLayout) root;
			layout.setContainerPercent(value/DEFAULT_SLIDER);
			traverAndAdjust(layout, percent);
			layout.adjustCreatorsWhileSlide(percent);
			
			// �϶����飬�Ƚ��ڲ�����ٷֱȴ�С���㣬�ټ���������С

			Dimension d = new Dimension(layout.getWidth(), layout.getHeight());
			// ����Ӧ���ֵĸ�����border
			if (layout.getParent() != null) {
				int paraHeight = designer.getParaHeight();
				if (needCalculateParaHeight && paraHeight > 0) {
					// ������������Ĵ�С
					paraHeight += (int) (paraHeight*percent) ;
					designer.setParaHeight(paraHeight);
					XWBorderLayout parent =  (XWBorderLayout) layout.getParent();
					parent.toData().setNorthSize(paraHeight);
					parent.removeAll();
					parent.add(designer.getParaComponent(),WBorderLayout.NORTH);
					parent.add(designer.getRootComponent(),WBorderLayout.CENTER);
				}
				layout.getParent().setSize(d.width, d.height+paraHeight);
				// ��������Ӧ���ִ�С��ͬ���������������border��С����ʱˢ����formArea
				FormArea.this.validate();
			}
			START_VALUE = value;
		}
    }
    
    //ѭ���������֣����ٷֱȵ����������С
    private void  traverAndAdjust(XCreator creator,double percent){
		for(int i=0; i<creator.getComponentCount(); i++){
			Object object = creator.getComponent(i);
			if(object instanceof XCreator){
				XCreator temp = (XCreator)object;
    			temp.adjustCompSize(percent);
    			traverAndAdjust(temp,percent);
			}
		}
	
    }

    /**
     * ���ӿ̶���
     */
    public void addFormRuler() {
        BaseRuler vRuler = new VerticalRuler(this);
        BaseRuler hRuler = new HorizontalRuler(this);
        this.add(RulerLayout.VRULER, vRuler);
        this.add(RulerLayout.HRULER, hRuler);
    }
    
    /**
     * �������¼�
     * ���ڱ���ƽ���Ҫ�� ������С���ڽ���ʱ���������ſ����϶������Բ�֧�ֹ����������¹�
     */
    @Override
    protected void processMouseWheelEvent(java.awt.event.MouseWheelEvent evt) {
        int id = evt.getID();
        switch (id) {
            case MouseEvent.MOUSE_WHEEL: {
                int rotations = evt.getWheelRotation();
                int value = this.verScrollBar.getValue() + rotations * ROTATIONS ; 
                value = Math.min(value, verticalMax);
                value = Math.max(0, value);
                doLayout(); //��dolayout����Ϊÿ�ι�����Ҫ���� Max�Ĵ�С
                this.verScrollBar.setValue(value);
                break;
            }
        }
    }

    /**
     * ���ر�����������designer
     * getFormEditor.
     */
    public FormDesigner getFormEditor() {
        return designer;
    }
    
    private boolean shouldSetScrollValue(XCreator creator) {
          return !isValid || designer.isRoot(creator) || getDesignerWidth() >= designer.getRootComponent().getWidth();
    }

    /**
     * ���ý����ڵ�����ɼ��Լ�ˮƽ��ֱ��������ֵ
     * �����˸������������������ʱ�����С������ǰ�����С���������ù�����ֵ�����������Ĭ�ϲ���ʾ��
     *
     * @param creator   �ؼ�
     */
    public void scrollPathToVisible(XCreator creator) {
    	creator.seleteRelatedComponent(creator);
    	
        if (!ComponentUtils.isComponentVisible(creator) && !designer.isRoot(creator) && (creator.toData()).isVisible()) {
            designer.makeVisible(creator);
        } 

        if (shouldSetScrollValue(creator)) {
        	return;
        }
        //��ȡ�������ľ���λ��
        Rectangle rec = ComponentUtils.getRelativeBounds(creator);
        int dWidth = getDesignerWidth();
        if (rec.width <= dWidth&&rec.x < getHorizontalValue()) {
        	//�ڱ߽��ڲ���xλ��С��ˮƽ��������ֵ
        	horScrollBar.setValue(rec.x);
        } else if (rec.x+rec.width > dWidth) {
        	//�����߽���
        	horScrollBar.setValue(rec.x+rec.width-dWidth);
        }
        int dHeight = getDesignerHeight();
        if (rec.height < dHeight && rec.y < getVerticalValue()) {
        	//�ڱ߽��ڲ���yλ��С����ֱ��������ֵ
        	 verScrollBar.setValue(rec.y);
        } else if (rec.y+rec.height > dHeight) {
        	//�����߽�߶�
        	verScrollBar.setValue(rec.y+rec.height-dHeight);
        }
    }
    
    
    /**
     * ��������
     */
    public void doLayout() {
    	layout();
    	if (isValid) {
    		XLayoutContainer root = designer.getRootComponent();
    		setScrollBarProperties(root.getWidth()-designer.getWidth(), horScrollBar);
            //���������ֵ��ʱ��Ӧ�����ϲ������ĸ߶�
            setScrollBarProperties(designer.getParaHeight() + root.getHeight()-designer.getHeight(), verScrollBar);
    	}
    }
    
    /**
     * ���ù�����������
     */
    private void setScrollBarProperties(int value, FormScrollBar bar) {
    	if (value == 0 && isScrollNotVisible(bar)) {
    		return;
    	}
    	if (value <= 0) {
    		// �����й�����ʱ���ֶ���С������ȵ������ڣ����ù�����ֵ��max
    		setScrollBarMax(0, bar);
    		bar.setMaximum(0);
    		bar.setValue(0);
    		bar.setEnabled(false);
    	} else {
            //���������ק������valueһֱΪ��ǰvalue
            int oldValue = verticalValue;
    		setScrollBarMax(value, bar);
    		bar.setEnabled(true);
    		bar.setMaximum(value);
    		bar.setValue(value);
            bar.setValue(oldValue);
    	}
    }
    
    private boolean isScrollNotVisible(FormScrollBar bar) {
    	if (bar.getOrientation() == Adjustable.VERTICAL ) {
    		return verticalMax == 0;
    	} else {
    		return horicalMax == 0;
    	}
    }
    
    private void setScrollBarMax( int max, FormScrollBar bar) {
    	if (bar.getOrientation() == Adjustable.VERTICAL ) {
    		verticalMax = max;
    	} else {
    		horicalMax = max;
    	}
    }
    
    /**
     *����designer����С�߶�
     *
     * @return int
     */
    public int getMinHeight() {
        return designer.getDesignerMode().getMinDesignHeight();
    }

    /**
     *����designer����С���
     *
     * @return int
     */
    public int getMinWidth() {
        return designer.getDesignerMode().getMinDesignWidth();
    }

    /**
     * getRulerLengthUnit
     *
     * @return short
     */
    public short getRulerLengthUnit() {
        return -1;
    }

    /**
     * ����ˮƽ��������value
     *
     * @return int
     */
    public int getHorizontalValue() {
        return horizontalValue;
    }

    /**
     * ����ˮƽ��������value
     *
     * @param newValue
     */
    public void setHorizontalValue(int newValue) {
        this.horizontalValue = newValue;
    }

    /**
     * ������ֱ��������value
     *
     * @return
     */
    public int getVerticalValue() {
        return verticalValue;
    }

    /**
     * ��ֱ��������ֵ
     *
     * @param newValue
     */
    public void setVerticalValue(int newValue) {
        this.verticalValue = newValue;
    }

    /**
     * ���ص�ǰdesigner�ĸ߶�
     *
     * @return height
     */
    public int getDesignerHeight() {
        return designer.getHeight();
    }

    /**
     * ���ص�ǰdesigner�Ŀ��
     *
     * @return
     */
    public int getDesignerWidth() {
        return designer.getWidth();
    }
    
    /**
     * ���ؿ�ȿؼ���value
     *
     * @return ���
     */
    public double getWidthPaneValue() {
        return widthPane.getValue();
    }
    
    /**
     * ���ÿ��ֵ
     * @param value ֵ
     */
    public void setWidthPaneValue(int value) {
    	widthPane.setValue(value);
    }
    
    /**
     * ���ø߶�ֵ
     * @param value ֵ
     */
    public void setHeightPaneValue(int value) {
    	heightPane.setValue(value);
    }
    
    /**
     * ���ظ߶ȿؼ���value
     *
     * @return �߶�
     */
    public double getHeightPaneValue() {
        return heightPane.getValue();
    }
    
    /**
     * ���ؽ����С�İٷֱ�ֵ
     *
     * @return �ٷֱ�ֵ
     */
    public double getSlideValue() {
//    	return slidePane.updateBean();
    	//7.1.1�������Ż���
    	return this.screenValue;
    }

    /**
     * ���ؽ��������С
     *
     * @return Dimension
     */
    public Dimension getAreaSize() {
        return new Dimension(horScrollBar.getMaximum(), verScrollBar.getMaximum());
    }
    
    /**
     * setAreaSize
     *
     * @param totalSize
     * @param horizontalValue
     * @param verticalValue
     */
    public void setAreaSize(Dimension totalSize, int horizontalValue, int verticalValue, double width, double height, double slide) {
    	this.verticalMax = (int) totalSize.getHeight();
    	this.horicalMax = (int) totalSize.getHeight();
        // ����ʱ��refreshRoot������layout��С��ΪĬ�ϴ�С
        // ����֮ǰ���õĿ�ߺͰٷֱ�����������size
        if (width != widthPane.getValue()) {
        	widthPane.setValue(width);
        	reCalculateWidth((int) width);
        }
        if (height != heightPane.getValue()) {
        	heightPane.setValue(height);
        	reCalculateHeight((int) height);
        }
        if (designer.getRootComponent().acceptType(XWFitLayout.class) && slide == DEFAULT_SLIDER) {
        	XWFitLayout layout = (XWFitLayout) designer.getRootComponent();
        	// ����ʱ��refreshRoot�ˣ��˴�ȥ���ڱ߾������Ӽ��
        	layout.moveContainerMargin();
        	layout.addCompInterval(layout.getAcualInterval());
        } else if (designer.getRootComponent().acceptType(XWFitLayout.class)){
        	START_VALUE = DEFAULT_SLIDER;
        	reCalculateRoot(slide, true);
//    		slidePane.populateBean(slide);
        }
    }
    
    /**
     * �����������ֵ��max
     * @param oldmax ֮ǰ���ֵ
     * @param max ��ǰ���ֵ
     * @param newValue ��ǰvalue
     * @param oldValue ֮ǰvalue
     * @param visi designer�Ĵ�С
     * @param orientation ����������
     * @return ������ֵ��max
     */
    @Override
    public Point calculateScroll(int oldmax, int max, int newValue, int oldValue, int visi, int orientation) {
    	int scrollMax = orientation==1 ? verticalMax : horicalMax;
    	//��ֹ����������Ͷ˻����Լ�������ƶ�(���������Χ����ʱ��newValueҪ�ڷ�Χ֮��)
    	if ( oldmax == scrollMax+visi && newValue>scrollMax ) {
    		return new Point(oldValue, oldmax);
    	}
		return new Point(newValue, max);
	}

	private class FormRulerLayout extends RulerLayout{
        private int DESIGNERWIDTH = 960;
        private int DESIGNERHEIGHT =540;
        private int TOPGAP = 8;
        
        public FormRulerLayout(){
            super();
        }
        
        /**
         * ���õ�layout����ǰ����Ҫ���
         */
        public void layoutContainer(Container target) {
            synchronized (target.getTreeLock()) {
                Insets insets = target.getInsets();
                int top = insets.top;
                int left = insets.left;
                int bottom = target.getHeight() - insets.bottom;
                int right = target.getWidth() - insets.right;
                Dimension resize = resizePane.getPreferredSize();
                Dimension hbarPreferredSize = null;
                Dimension vbarPreferredSize = null;
                
                resizePane.setBounds(left, bottom - resize.height, right, resize.height);
                if(horScrollBar != null) {
                    hbarPreferredSize = horScrollBar.getPreferredSize();
                    vbarPreferredSize = verScrollBar.getPreferredSize();
                    horScrollBar.setBounds(left , bottom - hbarPreferredSize.height-resize.height, right - BARSIZE, hbarPreferredSize.height);
                    verScrollBar.setBounds(right - vbarPreferredSize.width, top, vbarPreferredSize.width, bottom - BARSIZE-resize.height);
                }
                FormDesigner dg = ((FormDesigner) designer);
                XLayoutContainer root = dg.getRootComponent();
                if (root.acceptType(XWFitLayout.class)) {
                	DESIGNERWIDTH  = root.getWidth();
                	DESIGNERHEIGHT = dg.hasWAbsoluteLayout() ? root.getHeight()+dg.getParaHeight() :  root.getHeight();
            	}
                Rectangle rec = new Rectangle(left+(right - DESIGNERWIDTH)/2, TOPGAP, right, bottom);
                //�Ƿ�Ϊ��
                if (isValid ){
            		int maxHeight = bottom - hbarPreferredSize.height - resize.height -TOPGAP*2;
            		int maxWidth =  right - vbarPreferredSize.width;
            		DESIGNERWIDTH  = DESIGNERWIDTH> maxWidth ? maxWidth : DESIGNERWIDTH;
            		DESIGNERHEIGHT = DESIGNERHEIGHT > maxHeight ? maxHeight : DESIGNERHEIGHT;
            		int designerLeft = left+(verScrollBar.getX() - DESIGNERWIDTH)/2;
            		rec = new Rectangle(designerLeft, TOPGAP, DESIGNERWIDTH, DESIGNERHEIGHT);
                } 
                // designer����������ƽ����е���岿�֣�Ŀǰֻ������Ӧ���ֺͲ������档
                designer.setBounds(rec);
            }
        }
        
    }

}
