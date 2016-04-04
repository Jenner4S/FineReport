// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.scrollruler.*;
import com.fr.form.ui.ChartBook;
import com.fr.general.FRScreen;
import com.fr.general.Inter;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

// Referenced classes of package com.fr.design.mainframe:
//            FormScrollBar, ChartDesigner

public class ChartArea extends JComponent
    implements ScrollRulerComponent
{
    private class FormRulerLayout extends RulerLayout
    {

        final ChartArea this$0;

        public void layoutContainer(Container container)
        {
            synchronized(container.getTreeLock())
            {
                Insets insets = container.getInsets();
                int i = insets.top;
                int j = insets.left;
                int k = container.getHeight() - insets.bottom;
                int l = container.getWidth() - insets.right;
                Dimension dimension = resizePane.getPreferredSize();
                Dimension dimension1 = null;
                Dimension dimension2 = null;
                resizePane.setBounds(j, k - dimension.height, l, dimension.height);
                if(horScrollBar != null)
                {
                    dimension1 = horScrollBar.getPreferredSize();
                    dimension2 = verScrollBar.getPreferredSize();
                    horScrollBar.setBounds(j, k - dimension1.height - dimension.height, l - BARSIZE, dimension1.height);
                    verScrollBar.setBounds(l - dimension2.width, i, dimension2.width, k - BARSIZE - dimension.height);
                }
                ChartDesigner chartdesigner = (ChartDesigner)designer;
                Rectangle rectangle = new Rectangle(j + (l - designerwidth) / 2, 8, l, k);
                if(isValid)
                {
                    int i1 = k - dimension1.height - dimension.height - 16;
                    int j1 = l - dimension2.width;
                    designerwidth = designerwidth <= j1 ? designerwidth : j1;
                    designerheight = designerheight <= i1 ? designerheight : i1;
                    int k1 = j + (verScrollBar.getX() - designerwidth) / 2;
                    rectangle = new Rectangle(k1, 8, designerwidth, designerheight);
                }
                designer.setBounds(rectangle);
            }
        }

        public FormRulerLayout()
        {
            this$0 = ChartArea.this;
            super();
        }
    }


    private static final int TOPGAP = 8;
    private static final int MIN_WIDTH = 36;
    private static final int MIN_HEIGHT = 21;
    private static final double SLIDER_FLOAT = 120D;
    private static final double SLIDER_MIN = 60D;
    private static final double DEFAULT_SLIDER = 100D;
    private static final int ROTATIONS = 50;
    private int designerwidth;
    private int designerheight;
    private int customWidth;
    private int customHeight;
    private ChartDesigner designer;
    private int horizontalValue;
    private int verticalValue;
    private int verticalMax;
    private int horicalMax;
    private FormScrollBar verScrollBar;
    private FormScrollBar horScrollBar;
    private UINumberField widthPane;
    private UINumberField heightPane;
    private boolean isValid;
    private double START_VALUE;

    public ChartArea(ChartDesigner chartdesigner)
    {
        this(chartdesigner, true);
    }

    public ChartArea(ChartDesigner chartdesigner, boolean flag)
    {
        designerwidth = 810;
        designerheight = 500;
        customWidth = 810;
        customHeight = 500;
        horizontalValue = 0;
        verticalValue = 0;
        verticalMax = 0;
        horicalMax = 0;
        isValid = true;
        START_VALUE = 100D;
        designer = chartdesigner;
        designer.setParent(this);
        customWidth = ((ChartBook)chartdesigner.getTarget()).getWidth();
        customHeight = ((ChartBook)chartdesigner.getTarget()).getHeight();
        designerwidth = customWidth;
        designerheight = customHeight;
        isValid = flag;
        verScrollBar = new FormScrollBar(1, this);
        horScrollBar = new FormScrollBar(0, this);
        if(flag)
        {
            setLayout(new FormRulerLayout());
            chartdesigner.setBorder(new LineBorder(new Color(198, 198, 198)));
            add("Center", chartdesigner);
            addFormSize();
            add("Vertical", verScrollBar);
            add("Horizontal", horScrollBar);
            enableEvents(0x20000L);
        } else
        {
            setLayout(new RulerLayout());
            add("Center", chartdesigner);
            addFormRuler();
        }
        setFocusTraversalKeysEnabled(false);
    }

    private void addFormSize()
    {
        double d = -1D;
        double d1 = -2D;
        double ad[] = {
            d
        };
        double ad1[] = {
            d1, d, d1, d1, d1, d1, d1, d, d1
        };
        UILabel uilabel = new UILabel("chart");
        uilabel.setPreferredSize(new Dimension(200, 0));
        widthPane = new UINumberField();
        widthPane.setPreferredSize(new Dimension(60, 0));
        heightPane = new UINumberField();
        heightPane.setPreferredSize(new Dimension(60, 0));
        JPanel jpanel = new JPanel() {

            final ChartArea this$0;

            public Dimension getPreferredSize()
            {
                return new Dimension(200, 0);
            }

            
            {
                this$0 = ChartArea.this;
                super();
            }
        }
;
        JPanel jpanel1 = TableLayoutHelper.createCommonTableLayoutPane(new JComponent[][] {
            new JComponent[] {
                uilabel, new UILabel(), widthPane, new UILabel(Inter.getLocText("Indent-Pixel")), new UILabel("x"), heightPane, new UILabel(Inter.getLocText("Indent-Pixel")), new UILabel(), jpanel
            }
        }, ad, ad1, 8D);
        add("Bottom", jpanel1);
        setWidgetsConfig();
        initCalculateSize();
    }

    private void setWidgetsConfig()
    {
        UINumberField _tmp = widthPane;
        widthPane.setHorizontalAlignment(0);
        UINumberField _tmp1 = heightPane;
        heightPane.setHorizontalAlignment(0);
        widthPane.setMaxDecimalLength(0);
        heightPane.setMaxDecimalLength(0);
        widthPane.setValue(designerwidth);
        heightPane.setValue(designerheight);
        addWidthPaneListener();
        addHeightPaneListener();
    }

    private void initCalculateSize()
    {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        double d = FRScreen.getByDimension(dimension).getValue().doubleValue();
        if(d != 100D)
            reCalculateRoot(d, true);
    }

    private void addWidthPaneListener()
    {
        widthPane.addActionListener(new ActionListener() {

            final ChartArea this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                reCalculateWidth((int)((UINumberField)actionevent.getSource()).getValue());
            }

            
            {
                this$0 = ChartArea.this;
                super();
            }
        }
);
        widthPane.addFocusListener(new FocusAdapter() {

            final ChartArea this$0;

            public void focusLost(FocusEvent focusevent)
            {
                reCalculateWidth((int)((UINumberField)focusevent.getSource()).getValue());
            }

            
            {
                this$0 = ChartArea.this;
                super();
            }
        }
);
    }

    private void addHeightPaneListener()
    {
        heightPane.addActionListener(new ActionListener() {

            final ChartArea this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                reCalculateHeight((int)((UINumberField)actionevent.getSource()).getValue());
            }

            
            {
                this$0 = ChartArea.this;
                super();
            }
        }
);
        heightPane.addFocusListener(new FocusAdapter() {

            final ChartArea this$0;

            public void focusLost(FocusEvent focusevent)
            {
                reCalculateHeight((int)((UINumberField)focusevent.getSource()).getValue());
            }

            
            {
                this$0 = ChartArea.this;
                super();
            }
        }
);
    }

    private void reCalculateWidth(int i)
    {
        int j = i - designerwidth;
        if(j == 0)
        {
            return;
        } else
        {
            designer.setSize(i, designerheight);
            designerwidth = i;
            customWidth = i;
            ((ChartBook)designer.getTarget()).setWidth(i);
            validate();
            designer.fireTargetModified();
            return;
        }
    }

    private void reCalculateHeight(int i)
    {
        int j = i - designerwidth;
        if(j == 0)
        {
            return;
        } else
        {
            designer.setSize(designerwidth, i);
            designerheight = i;
            customHeight = i;
            ((ChartBook)designer.getTarget()).setHeight(i);
            validate();
            designer.fireTargetModified();
            return;
        }
    }

    private void reCalculateRoot(double d, boolean flag)
    {
        if(d == START_VALUE)
        {
            return;
        } else
        {
            double d1 = (d - START_VALUE) / START_VALUE;
            Dimension dimension = new Dimension(designerwidth, designerheight);
            validate();
            START_VALUE = d;
            return;
        }
    }

    public void addFormRuler()
    {
        VerticalRuler verticalruler = new VerticalRuler(this);
        HorizontalRuler horizontalruler = new HorizontalRuler(this);
        add("VRuler", verticalruler);
        add("HRuler", horizontalruler);
    }

    protected void processMouseWheelEvent(MouseWheelEvent mousewheelevent)
    {
        int i = mousewheelevent.getID();
        switch(i)
        {
        case 507: 
            int j = mousewheelevent.getWheelRotation();
            int k = verScrollBar.getValue() + j * 50;
            k = Math.min(k, verticalMax);
            k = Math.max(0, k);
            doLayout();
            verScrollBar.setValue(k);
            break;
        }
    }

    public void doLayout()
    {
        layout();
        if(isValid)
        {
            setScrollBarProperties(customWidth - designer.getWidth(), horScrollBar);
            setScrollBarProperties(customHeight - designer.getHeight(), verScrollBar);
        }
    }

    private void setScrollBarProperties(int i, FormScrollBar formscrollbar)
    {
        if(i <= 0)
        {
            setScrollBarMax(0, formscrollbar);
            formscrollbar.setMaximum(0);
            formscrollbar.setValue(0);
            formscrollbar.setEnabled(false);
        } else
        {
            int j = verticalValue;
            setScrollBarMax(i, formscrollbar);
            formscrollbar.setEnabled(true);
            formscrollbar.setMaximum(i);
            formscrollbar.setValue(i);
            formscrollbar.setValue(j);
        }
    }

    private boolean isScrollNotVisible(FormScrollBar formscrollbar)
    {
        if(formscrollbar.getOrientation() == 1)
            return verticalMax == 0;
        else
            return horicalMax == 0;
    }

    private void setScrollBarMax(int i, FormScrollBar formscrollbar)
    {
        if(formscrollbar.getOrientation() == 1)
            verticalMax = i;
        else
            horicalMax = i;
    }

    public int getMinHeight()
    {
        return 21;
    }

    public int getMinWidth()
    {
        return 36;
    }

    public short getRulerLengthUnit()
    {
        return -1;
    }

    public int getHorizontalValue()
    {
        return horizontalValue;
    }

    public void setHorizontalValue(int i)
    {
        horizontalValue = i;
    }

    public int getVerticalValue()
    {
        return verticalValue;
    }

    public void setVerticalValue(int i)
    {
        verticalValue = i;
    }

    public int getDesignerHeight()
    {
        return designer.getHeight();
    }

    public int getDesignerWidth()
    {
        return designer.getWidth();
    }

    public double getWidthPaneValue()
    {
        return widthPane.getValue();
    }

    public void setWidthPaneValue(int i)
    {
        widthPane.setValue(i);
    }

    public void setHeightPaneValue(int i)
    {
        heightPane.setValue(i);
    }

    public double getHeightPaneValue()
    {
        return heightPane.getValue();
    }

    public Dimension getAreaSize()
    {
        return new Dimension(horScrollBar.getMaximum(), verScrollBar.getMaximum());
    }

    public void setAreaSize(Dimension dimension, int i, int j, double d, double d1, 
            double d2)
    {
        horScrollBar.setMaximum((int)dimension.getWidth());
        verScrollBar.setMaximum((int)dimension.getHeight());
        horScrollBar.setValue(i);
        verScrollBar.setValue(j);
        if(d != widthPane.getValue())
        {
            widthPane.setValue(d);
            reCalculateWidth((int)d);
        }
        if(d1 != heightPane.getValue())
        {
            heightPane.setValue(d1);
            reCalculateHeight((int)d1);
        }
        START_VALUE = 100D;
        reCalculateRoot(d2, true);
    }

    public int getCustomWidth()
    {
        return customWidth;
    }

    public int getCustomHeight()
    {
        return customHeight;
    }

    public Point calculateScroll(int i, int j, int k, int l, int i1, int j1)
    {
        int k1 = j1 != 1 ? horicalMax : verticalMax;
        if(i == k1 + i1 && k > k1)
            return new Point(l, i);
        else
            return new Point(k, j);
    }







}
