// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.base.Formula;
import com.fr.chart.base.AreaColor;
import com.fr.chart.base.ChartBaseUtils;
import com.fr.chart.chartglyph.MapHotAreaColor;
import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ipoppane.PopupHider;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.style.color.ColorControlWindow;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.series:
//            ColorPickerPaneNumFiled

public class UIColorPickerPane extends BasicPane
    implements UIObserver
{
    private class TextFieldGroupPane extends JPanel
    {

        private static final long serialVersionUID = 0x8b8f0bd9893fa643L;
        final UIColorPickerPane this$0;

        public void refreshTextGroupPane(Formula aformula[])
        {
            if(aformula.length == textFieldList.size())
            {
                for(int i = 0; i < textFieldList.size(); i++)
                    setTextValue4Index(i, aformula[i].toString());

            } else
            {
                for(int j = 0; j < textFieldList.size(); j++)
                    remove((Component)textFieldList.get(j));

                textFieldList.clear();
                for(int k = 0; k < aformula.length; k++)
                {
                    JComponent jcomponent = getNewTextFieldComponent(k, aformula[k].toString());
                    textFieldList.add(jcomponent);
                    initMoveOnListener(jcomponent);
                    add(jcomponent);
                }

            }
            repaint();
        }

        private void initMoveOnListener(Container container)
        {
            Component acomponent[] = container.getComponents();
            int i = acomponent.length;
            for(int j = 0; j < i; j++)
            {
                Component component = acomponent[j];
                if(component instanceof Container)
                    initMoveOnListener((Container)component);
                if(component instanceof UIObserver)
                    component.addMouseListener(new MouseAdapter() {

                        final TextFieldGroupPane this$1;

                        public void mouseEntered(MouseEvent mouseevent)
                        {
                            moveOnColorOrTextPane = true;
                        }

                        public void mouseExited(MouseEvent mouseevent)
                        {
                            moveOnColorOrTextPane = false;
                        }

                
                {
                    this$1 = TextFieldGroupPane.this;
                    super();
                }
                    }
);
            }

        }

        public void checkEveryFiledMinMax()
        {
            double d = 1.7976931348623157E+308D;
            double d1 = -1.7976931348623157E+308D;
            int i = 0;
            for(int j = textFieldList.size(); i < j; i++)
            {
                if(i == j - 1)
                {
                    d1 = -1.7976931348623157E+308D;
                } else
                {
                    Number number = ChartBaseUtils.formula2Number(new Formula(getValue4Index(i + 1)));
                    if(number != null)
                        d1 = number.doubleValue();
                }
                Number number1 = ChartBaseUtils.formula2Number(new Formula(getValue4Index(i)));
                if(number1 != null)
                {
                    double d2 = number1.doubleValue();
                    if(d2 < d && d2 > d1)
                        setBackgroundUIColor(i, Color.white);
                    else
                        setBackgroundUIColor(i, Color.red);
                    d = d2;
                } else
                {
                    setBackgroundUIColor(i, Color.white);
                }
            }

            repaint();
        }

        public Dimension getPreferredSize()
        {
            Dimension dimension = new Dimension();
            dimension.width = 120;
            dimension.height = (textFieldList.size() * 2 - 1) * 20;
            return dimension;
        }

        public TextFieldGroupPane()
        {
            this$0 = UIColorPickerPane.this;
            super();
            setLayout(null);
        }
    }

    private class ColorGroupPane extends JPanel
    {

        private static final long serialVersionUID = 0x7ffdd900dc6d1a4bL;
        final UIColorPickerPane this$0;

        public void refreshColorGroupPane(Color acolor[])
        {
            for(int i = 0; i < colorRecList.size(); i++)
                remove((Component)colorRecList.get(i));

            colorRecList.clear();
            for(int j = 0; j < acolor.length; j++)
            {
                ColorRecButton colorrecbutton = new ColorRecButton(acolor[j], j == acolor.length - 1);
                colorrecbutton.setBounds(0, j * colorrecbutton.getPreferredSize().height, colorrecbutton.getPreferredSize().width, colorrecbutton.getPreferredSize().height);
                add(colorrecbutton);
                colorRecList.add(colorrecbutton);
            }

            repaint();
        }

        public Dimension getPreferredSize()
        {
            Dimension dimension = new Dimension();
            dimension.width = 70;
            dimension.height = colorRecList.size() * 40;
            return dimension;
        }

        public void addSelectionChangeListener(ChangeListener changelistener)
        {
            for(int i = 0; i < colorRecList.size(); i++)
                ((ColorRecButton)colorRecList.get(i)).addSelectChangeListener(changelistener);

        }

        public void removeSelectionChangeListener(ChangeListener changelistener)
        {
            for(int i = 0; i < colorRecList.size(); i++)
                ((ColorRecButton)colorRecList.get(i)).removeSelectChangeListener(changelistener);

        }

        public ColorGroupPane()
        {
            this$0 = UIColorPickerPane.this;
            super();
            setLayout(null);
        }
    }

    private class ColorRecButton extends JLabel
        implements PopupHider
    {

        private Color color;
        private boolean isLast;
        private boolean isRollover;
        private ColorControlWindow popupWin;
        private java.util.List changeListenerList;
        final UIColorPickerPane this$0;

        private boolean isColorArea(Point point)
        {
            return point.getX() <= 30D;
        }

        private ColorControlWindow getColorControlWindow()
        {
            if(popupWin == null)
                popupWin = new ColorControlWindow(false, this) {

                    final ColorRecButton this$1;

                    protected void colorChanged()
                    {
                        color = getColor();
                        hidePopupMenu();
                        fireChangeListener();
                        repaint();
                    }

                
                {
                    this$1 = ColorRecButton.this;
                    super(flag, popuphider);
                }
                }
;
            return popupWin;
        }

        public Color getPaintColor()
        {
            return color;
        }

        public void hidePopupMenu()
        {
            if(popupWin != null)
                popupWin.setVisible(false);
            popupWin = null;
        }

        public void addSelectChangeListener(ChangeListener changelistener)
        {
            changeListenerList.add(changelistener);
        }

        public void removeSelectChangeListener(ChangeListener changelistener)
        {
            changeListenerList.remove(changelistener);
        }

        public void fireChangeListener()
        {
            if(!changeListenerList.isEmpty())
            {
                ChangeEvent changeevent = new ChangeEvent(this);
                for(int i = 0; i < changeListenerList.size(); i++)
                    ((ChangeListener)changeListenerList.get(i)).stateChanged(changeevent);

            }
        }

        public void paint(Graphics g)
        {
            int i = getWidth();
            int j = getHeight();
            Graphics2D graphics2d = (Graphics2D)g;
            graphics2d.setColor(color);
            graphics2d.fillRect(1, 1, 29, 39);
            if(isRollover)
            {
                graphics2d.setColor(UIConstants.FLESH_BLUE);
                graphics2d.drawRect(0, 0, 30, 40);
                graphics2d.setColor(UIConstants.LINE_COLOR);
                graphics2d.drawLine(30, 0, i, 0);
                graphics2d.drawLine(30, 40, i, j);
                if(isLast)
                    graphics2d.drawLine(30, 39, i, j - 1);
            } else
            {
                graphics2d.setColor(UIConstants.LINE_COLOR);
                graphics2d.drawLine(0, 0, i, 0);
                graphics2d.drawLine(0, 0, 0, j);
                graphics2d.drawLine(30, 0, 30, j);
                if(isLast)
                    graphics2d.drawLine(0, j - 1, i, j - 1);
            }
            super.paint(g);
        }

        public Dimension getPreferredSize()
        {
            return new Dimension(70, 40);
        }







        public ColorRecButton(Color color1, boolean flag)
        {
            this$0 = UIColorPickerPane.this;
            super(" ");
            changeListenerList = new ArrayList();
            setFocusable(true);
            setHorizontalAlignment(0);
            setVerticalAlignment(0);
            color = color1;
            isLast = flag;
            addMouseListener(new MouseAdapter() {

                final UIColorPickerPane val$this$0;
                final ColorRecButton this$1;

                public void mouseEntered(MouseEvent mouseevent)
                {
                    if(!isColorArea(mouseevent.getPoint()))
                    {
                        return;
                    } else
                    {
                        requestFocus();
                        moveOnColorOrTextPane = true;
                        isRollover = true;
                        repaint();
                        return;
                    }
                }

                public void mouseExited(MouseEvent mouseevent)
                {
                    moveOnColorOrTextPane = false;
                    isRollover = false;
                    repaint();
                }

                public void mouseReleased(MouseEvent mouseevent)
                {
                    if(!isColorArea(mouseevent.getPoint()))
                    {
                        return;
                    } else
                    {
                        popupWin = getColorControlWindow();
                        GUICoreUtils.showPopupMenu(popupWin, ColorRecButton.this, 0, getSize().height);
                        return;
                    }
                }


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
        }
    }


    private static final int MARGIN_TOP = 10;
    private static final int OFF_HEIGHT = 6;
    private static final int COLOR_REC_HEIGHT = 40;
    private static final int COLOR_REC_WIDTH = 30;
    protected static final int TEXTFIELD_HEIGHT = 20;
    protected static final int TEXTFIELD_WIDTH = 120;
    private static final int LAYOUR_DET = 6;
    private static final double VALUE = 100D;
    protected ArrayList textFieldList;
    private java.util.List colorRecList;
    private JPanel upControlPane;
    private TextFieldGroupPane textGroup;
    private ColorGroupPane colorGroup;
    private UIButtonGroup designTypeButtonGroup;
    private ColorSelectBox fillStyleCombox;
    private UINumberDragPane regionNumPane = new UINumberDragPane(1.0D, 6D) {

        final UIColorPickerPane this$0;

        public void userEvent(double d2)
        {
            if(!moveOnColorOrTextPane)
            {
                refreshGroupPane(getColorArray(fillStyleCombox.getSelectObject(), (int)d2), getValueArray((int)d2));
                initContainerLister();
            }
        }

            
            {
                this$0 = UIColorPickerPane.this;
                super(d, d1);
            }
    }
;
    private JPanel stagePanel;
    private ChangeListener changeListener;
    private boolean moveOnColorOrTextPane;
    private LayoutManager layoutMeter = new LayoutManager() {

        final UIColorPickerPane this$0;

        public void removeLayoutComponent(Component component)
        {
        }

        public Dimension preferredLayoutSize(Container container)
        {
            return getPreferredSize();
        }

        public Dimension minimumLayoutSize(Container container)
        {
            return null;
        }

        public void layoutContainer(Container container)
        {
            upControlPane.setBounds(0, 0, container.getPreferredSize().width, upControlPane.getPreferredSize().height);
            stagePanel.setBounds(0, upControlPane.getPreferredSize().height + 6, container.getPreferredSize().width, stagePanel.getPreferredSize().height);
            colorGroup.setBounds(0, 10 + upControlPane.getPreferredSize().height + stagePanel.getPreferredSize().height + 12, colorGroup.getPreferredSize().width, colorGroup.getPreferredSize().height + upControlPane.getPreferredSize().height);
            textGroup.setBounds(colorGroup.getPreferredSize().width, upControlPane.getPreferredSize().height + stagePanel.getPreferredSize().height + 12, textGroup.getPreferredSize().width, textGroup.getPreferredSize().height);
        }

        public void addLayoutComponent(String s, Component component)
        {
        }

            
            {
                this$0 = UIColorPickerPane.this;
                super();
            }
    }
;
    private LayoutManager layout = new LayoutManager() {

        final UIColorPickerPane this$0;

        public void removeLayoutComponent(Component component)
        {
        }

        public Dimension preferredLayoutSize(Container container)
        {
            return getPreferredSize();
        }

        public Dimension minimumLayoutSize(Container container)
        {
            return null;
        }

        public void layoutContainer(Container container)
        {
            upControlPane.setBounds(0, 0, container.getPreferredSize().width, upControlPane.getPreferredSize().height);
            colorGroup.setBounds(0, 10 + upControlPane.getPreferredSize().height + 6, colorGroup.getPreferredSize().width, colorGroup.getPreferredSize().height + upControlPane.getPreferredSize().height);
            textGroup.setBounds(colorGroup.getPreferredSize().width, upControlPane.getPreferredSize().height + 6, textGroup.getPreferredSize().width, textGroup.getPreferredSize().height);
        }

        public void addLayoutComponent(String s, Component component)
        {
        }

            
            {
                this$0 = UIColorPickerPane.this;
                super();
            }
    }
;

    public UIColorPickerPane()
    {
        colorRecList = new ArrayList();
        stagePanel = null;
        fillStyleCombox = getColorSelectBox();
        fillStyleCombox.setSelectObject(Color.BLUE);
        fillStyleCombox.addSelectChangeListener(new ChangeListener() {

            final UIColorPickerPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                refreshGroupPane(getColorArray(fillStyleCombox.getSelectObject(), regionNumPane.updateBean().intValue()), getValueArray(regionNumPane.updateBean().intValue()));
            }

            
            {
                this$0 = UIColorPickerPane.this;
                super();
            }
        }
);
        designTypeButtonGroup = new UIButtonGroup(new String[] {
            Inter.getLocText("FR-Chart-Mode_Auto"), Inter.getLocText("FR-Chart-Mode_Custom")
        }, new Integer[] {
            Integer.valueOf(0), Integer.valueOf(1)
        });
        designTypeButtonGroup.setSelectedIndex(0);
        designTypeButtonGroup.addChangeListener(new ChangeListener() {

            final UIColorPickerPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                if(designTypeButtonGroup.getSelectedIndex() == 0)
                {
                    remove(textGroup);
                    remove(colorGroup);
                } else
                {
                    add(textGroup);
                    add(colorGroup);
                    int j = regionNumPane.updateBean().intValue();
                    refreshGroupPane(getColorArray(fillStyleCombox.getSelectObject(), j), getValueArray(j));
                    initContainerLister();
                }
                refreshPane();
            }

            
            {
                this$0 = UIColorPickerPane.this;
                super();
            }
        }
);
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d
        };
        Component acomponent[][] = createComponents();
        upControlPane = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        textFieldList = getTextFieldList();
        textGroup = new TextFieldGroupPane();
        colorGroup = new ColorGroupPane();
        setLayout(layout);
        add(upControlPane);
        int i = regionNumPane.updateBean().intValue();
        Color acolor[] = getColorArray(fillStyleCombox.getSelectObject(), i);
        refreshGroupPane(acolor, getValueArray(i));
    }

    protected UIButtonGroup getDesignTypeButtonGroup()
    {
        return designTypeButtonGroup;
    }

    protected ColorSelectBox getFillStyleCombox()
    {
        return fillStyleCombox;
    }

    protected UINumberDragPane getRegionNumPane()
    {
        return regionNumPane;
    }

    protected Component[][] createComponents()
    {
        return (new Component[][] {
            new Component[] {
                new BoldFontTextLabel(Inter.getLocText("FR_Chart-Data_Range_Configuration")), designTypeButtonGroup
            }, new Component[] {
                new BoldFontTextLabel(Inter.getLocText(new String[] {
                    "FR-Chart-Color_Subject", "FR-Chart-Color_Color"
                })), fillStyleCombox
            }, new Component[] {
                new BoldFontTextLabel(Inter.getLocText("FR-Chart-Value_Divided_stage")), regionNumPane
            }
        });
    }

    public UIColorPickerPane(String s)
    {
        colorRecList = new ArrayList();
        stagePanel = null;
        fillStyleCombox = getColorSelectBox();
        fillStyleCombox.setSelectObject(Color.BLUE);
        fillStyleCombox.addSelectChangeListener(new ChangeListener() {

            final UIColorPickerPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                refreshGroupPane(getColorArray(fillStyleCombox.getSelectObject(), regionNumPane.updateBean().intValue()), getValueArray(regionNumPane.updateBean().intValue()));
            }

            
            {
                this$0 = UIColorPickerPane.this;
                super();
            }
        }
);
        designTypeButtonGroup = new UIButtonGroup(new String[] {
            Inter.getLocText("FR-Chart-Mode_Auto"), Inter.getLocText("FR-Chart-Mode_Custom")
        }, new Integer[] {
            Integer.valueOf(0), Integer.valueOf(1)
        });
        designTypeButtonGroup.setSelectedIndex(0);
        designTypeButtonGroup.addChangeListener(new ChangeListener() {

            final UIColorPickerPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                if(designTypeButtonGroup.getSelectedIndex() == 0)
                {
                    remove(textGroup);
                    remove(colorGroup);
                } else
                {
                    add(textGroup);
                    add(colorGroup);
                    int j = regionNumPane.updateBean().intValue();
                    refreshGroupPane(getColorArray(fillStyleCombox.getSelectObject(), j), getValueArray(j));
                    initContainerLister();
                }
                refreshPane();
            }

            
            {
                this$0 = UIColorPickerPane.this;
                super();
            }
        }
);
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d
        };
        Component acomponent[][] = {
            {
                new BoldFontTextLabel(Inter.getLocText("FR-Chart-Value_Divided_stage")), regionNumPane
            }
        };
        stagePanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        Component acomponent1[][] = {
            {
                new BoldFontTextLabel(Inter.getLocText("FR-Chart-Value_Tick_And_Color")), designTypeButtonGroup
            }
        };
        upControlPane = TableLayoutHelper.createTableLayoutPane(acomponent1, ad1, ad);
        textGroup = new TextFieldGroupPane();
        colorGroup = new ColorGroupPane();
        setLayout(layoutMeter);
        add(upControlPane);
        add(stagePanel);
        int i = regionNumPane.updateBean().intValue();
        Color acolor[] = getColorArray(fillStyleCombox.getSelectObject(), i);
        textFieldList = getTextFieldList();
        refreshGroupPane(acolor, getValueArray(i));
    }

    protected ArrayList getTextFieldList()
    {
        return new ArrayList();
    }

    protected void setTextValue4Index(int i, String s)
    {
        ((ColorPickerPaneNumFiled)textFieldList.get(i)).setText(StringUtils.cutStringStartWith(s, "="));
    }

    protected String getValue4Index(int i)
    {
        return ((ColorPickerPaneNumFiled)textFieldList.get(i)).getText();
    }

    protected JComponent getNewTextFieldComponent(int i, String s)
    {
        ColorPickerPaneNumFiled colorpickerpanenumfiled = new ColorPickerPaneNumFiled();
        colorpickerpanenumfiled.setBounds(0, i * 2 * 20, 120, 20);
        colorpickerpanenumfiled.setText(StringUtils.cutStringStartWith(s, "="));
        return colorpickerpanenumfiled;
    }

    protected void setBackgroundUIColor(int i, Color color)
    {
        ((ColorPickerPaneNumFiled)textFieldList.get(i)).setBackgroundUIColor(color);
    }

    private void refreshPane()
    {
        validate();
        repaint();
        revalidate();
    }

    protected ColorSelectBox getColorSelectBox()
    {
        return new ColorSelectBox(100);
    }

    public Dimension getPreferredSize()
    {
        if(designTypeButtonGroup.getSelectedIndex() == 0)
        {
            return new Dimension(colorGroup.getPreferredSize().width + textGroup.getPreferredSize().width, upControlPane.getPreferredSize().height);
        } else
        {
            int i = stagePanel != null ? stagePanel.getPreferredSize().height + 10 : 0;
            return new Dimension(colorGroup.getPreferredSize().width + textGroup.getPreferredSize().width, i + textGroup.getPreferredSize().height + upControlPane.getPreferredSize().height + 6);
        }
    }

    public void registerChangeListener(final UIObserverListener listener)
    {
        changeListener = new ChangeListener() {

            final UIObserverListener val$listener;
            final UIColorPickerPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                listener.doChange();
            }

            
            {
                this$0 = UIColorPickerPane.this;
                listener = uiobserverlistener;
                super();
            }
        }
;
        designTypeButtonGroup.addChangeListener(changeListener);
        fillStyleCombox.addSelectChangeListener(changeListener);
        regionNumPane.addChangeListener(changeListener);
        colorGroup.addSelectionChangeListener(changeListener);
    }

    public boolean shouldResponseChangeListener()
    {
        return true;
    }

    protected String title4PopupWindow()
    {
        return null;
    }

    public void refreshGroupPane(Color acolor[], Formula aformula[])
    {
        colorGroup.refreshColorGroupPane(acolor);
        textGroup.refreshTextGroupPane(aformula);
        if(changeListener != null)
            colorGroup.addSelectionChangeListener(changeListener);
        refreshPane();
    }

    public int getDesignType()
    {
        return designTypeButtonGroup.getSelectedIndex();
    }

    public void populateBean(MapHotAreaColor maphotareacolor)
    {
        Color color = maphotareacolor.getMainColor();
        fillStyleCombox.setSelectObject(color);
        designTypeButtonGroup.setSelectedIndex(maphotareacolor.getUseType());
        double d = maphotareacolor.getAreaNumber();
        add(textGroup);
        add(colorGroup);
        Color acolor[] = maphotareacolor.initColor();
        Formula aformula[] = maphotareacolor.initValues();
        refreshGroupPane(acolor, aformula);
        initContainerLister();
        regionNumPane.populateBean(Double.valueOf(d));
        refreshPane();
    }

    private void initContainerLister()
    {
        Object obj;
        for(obj = this; !(obj instanceof ChartStylePane) && ((Container) (obj)).getParent() != null; obj = ((Container) (obj)).getParent());
        ((ChartStylePane)obj).initAllListeners();
    }

    public void updateBean(MapHotAreaColor maphotareacolor)
    {
        maphotareacolor.setMainColor(fillStyleCombox.getSelectObject());
        maphotareacolor.setUseType(designTypeButtonGroup.getSelectedIndex());
        maphotareacolor.setAreaNumber(regionNumPane.updateBean().intValue());
        if(maphotareacolor.getUseType() == 1)
        {
            if(!checkInOrder())
                return;
            maphotareacolor.clearColor();
            Color acolor[] = getColors4Custom(fillStyleCombox.getSelectObject(), regionNumPane.updateBean().intValue());
            Formula aformula[] = getValueArray(regionNumPane.updateBean().intValue());
            for(int i = 0; i < acolor.length; i++)
                maphotareacolor.addAreaColor(new AreaColor(aformula[i], aformula[i + 1], acolor[i]));

        }
    }

    private boolean checkInOrder()
    {
        textGroup.checkEveryFiledMinMax();
        boolean flag = true;
        double d = 1.7976931348623157E+308D;
        int i = 0;
        for(int j = textFieldList.size(); i < j; i++)
        {
            if(StringUtils.isEmpty(getValue4Index(i)))
                return false;
            Number number = ChartBaseUtils.formula2Number(new Formula(getValue4Index(i)));
            if(number == null)
                continue;
            double d1 = number.doubleValue();
            if(d1 > d)
            {
                flag = false;
                break;
            }
            d = d1;
        }

        return flag;
    }

    private Color[] getColors4Custom(Color color, int i)
    {
        Color acolor[] = getColorArray(color, i);
        Color acolor1[] = new Color[i];
        for(int j = 0; j < i; j++)
            if(j >= colorRecList.size())
                acolor1[j] = acolor[j];
            else
                acolor1[j] = ((ColorRecButton)colorRecList.get(j)).getPaintColor();

        return acolor1;
    }

    private Color[] getColorArray(Color color, int i)
    {
        return ChartBaseUtils.createColorsWithAlpha(color, i);
    }

    private Formula[] getValueArray(int i)
    {
        Formula aformula[] = new Formula[i + 1];
        for(int j = 0; j < aformula.length; j++)
            if(j >= textFieldList.size())
                aformula[j] = new Formula((new Double((double)((i + 1) - j) * 100D)).toString());
            else
                aformula[j] = new Formula(getValue4Index(j));

        return aformula;
    }

    public static transient void main(String args[])
    {
        JFrame jframe = new JFrame("test");
        jframe.setDefaultCloseOperation(3);
        JPanel jpanel = (JPanel)jframe.getContentPane();
        jpanel.setLayout(new BorderLayout());
        UIColorPickerPane uicolorpickerpane = new UIColorPickerPane();
        jpanel.add(uicolorpickerpane, "Center");
        GUICoreUtils.centerWindow(jframe);
        jframe.setSize(400, 400);
        jframe.setVisible(true);
    }















    // Unreferenced inner class com/fr/design/mainframe/chart/gui/style/series/UIColorPickerPane$6

/* anonymous class */
    class _cls6 extends UINumberDragPane
    {

        final UIColorPickerPane this$0;

        public void userEvent(double d2)
        {
            if(!moveOnColorOrTextPane)
            {
                refreshGroupPane(getColorArray(fillStyleCombox.getSelectObject(), (int)d2), getValueArray((int)d2));
                initContainerLister();
            }
        }

            
            {
                this$0 = UIColorPickerPane.this;
                super(d, d1);
            }
    }

}
