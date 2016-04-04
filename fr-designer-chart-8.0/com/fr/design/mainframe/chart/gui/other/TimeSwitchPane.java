// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.other;

import com.fr.base.*;
import com.fr.chart.base.TimeSwitchAttr;
import com.fr.chart.chartattr.Axis;
import com.fr.chart.chartattr.Plot;
import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.mainframe.JTemplate;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class TimeSwitchPane extends JPanel
    implements UIObserver
{
    private class TimeTickBox extends JPanel
    {

        private UITextField mainUnitField;
        private UIComboBox mainType;
        private UIButton delButton;
        final TimeSwitchPane this$0;

        private void initListeners()
        {
            mainUnitField.addMouseListener(new MouseAdapter() {

                final TimeTickBox this$1;

                public void mousePressed(MouseEvent mouseevent)
                {
                    showFormulaPane(mainUnitField);
                }

                
                {
                    this$1 = TimeTickBox.this;
                    super();
                }
            }
);
            delButton.addActionListener(new ActionListener() {

                final TimeTickBox this$1;

                public void actionPerformed(ActionEvent actionevent)
                {
                    deleteTimeTick(TimeTickBox.this);
                }

                
                {
                    this$1 = TimeTickBox.this;
                    super();
                }
            }
);
            mainType.addItemListener(new ItemListener() {

                final TimeTickBox this$1;

                public void itemStateChanged(ItemEvent itemevent)
                {
                    fireChange();
                }

                
                {
                    this$1 = TimeTickBox.this;
                    super();
                }
            }
);
        }

        public void setEnabled(boolean flag)
        {
            delButton.setEnabled(flag);
        }

        private void showFormulaPane(final UITextField jTextField)
        {
            final UIFormula formulaPane = FormulaFactory.createFormulaPane();
            final String original = jTextField.getText();
            formulaPane.populate(new Formula(original));
            BasicDialog basicdialog = formulaPane.showLargeWindow(SwingUtilities.getWindowAncestor(TimeSwitchPane.this), new DialogActionAdapter() {

                final UIFormula val$formulaPane;
                final UITextField val$jTextField;
                final String val$original;
                final TimeTickBox this$1;

                public void doOk()
                {
                    String s = Utils.objectToString(formulaPane.update());
                    jTextField.setText(s);
                    if(!ComparatorUtils.equals(original, s))
                        fireChange();
                }

                
                {
                    this$1 = TimeTickBox.this;
                    formulaPane = uiformula;
                    jTextField = uitextfield;
                    original = s;
                    super();
                }
            }
);
            basicdialog.setVisible(true);
        }




        public TimeTickBox(Formula formula, int i)
        {
            this$0 = TimeSwitchPane.this;
            super();
            setLayout(new FlowLayout(1, 4, 0));
            mainUnitField = new UITextField(formula.toString());
            mainUnitField.setEditable(false);
            mainUnitField.setPreferredSize(new Dimension(84, 20));
            mainType = new UIComboBox(TimeSwitchPane.TYPES);
            mainType.setSelectedItem(TimeSwitchPane.INTS.get(Integer.valueOf(i)));
            delButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/toolbarbtn/close.png"));
            initListeners();
            add(mainUnitField);
            add(mainType);
            add(delButton);
        }
    }

    private class Layout
        implements LayoutManager
    {

        final TimeSwitchPane this$0;

        public void addLayoutComponent(String s, Component component)
        {
        }

        public void removeLayoutComponent(Component component)
        {
        }

        public Dimension preferredLayoutSize(Container container)
        {
            int i = addButton.getPreferredSize().height + tablePane.getPreferredSize().height;
            return new Dimension(container.getWidth(), i + 2);
        }

        public Dimension minimumLayoutSize(Container container)
        {
            return preferredLayoutSize(container);
        }

        public void layoutContainer(Container container)
        {
            int i = container.getWidth();
            int j = 0;
            tablePane.setBounds(0, j, i, tablePane.getPreferredSize().height);
            j += tablePane.getPreferredSize().height + 2;
            addButton.setBounds(5, j, i - 10, addButton.getPreferredSize().height);
        }

        private Layout()
        {
            this$0 = TimeSwitchPane.this;
            super();
        }

    }

    private class TableLayout
        implements LayoutManager
    {

        final TimeSwitchPane this$0;

        public void addLayoutComponent(String s, Component component)
        {
        }

        public void removeLayoutComponent(Component component)
        {
        }

        public Dimension preferredLayoutSize(Container container)
        {
            return new Dimension(container.getWidth(), 25 * container.getComponentCount());
        }

        public Dimension minimumLayoutSize(Container container)
        {
            return preferredLayoutSize(container);
        }

        public void layoutContainer(Container container)
        {
            int i = 0;
            int j = container.getComponents().length;
            Component acomponent[] = container.getComponents();
            int k = acomponent.length;
            for(int l = 0; l < k; l++)
            {
                Component component = acomponent[l];
                component.setEnabled(j > 1);
                component.setBounds(0, i, container.getWidth(), 20);
                i += 25;
            }

        }

        private TableLayout()
        {
            this$0 = TimeSwitchPane.this;
            super();
        }

    }


    private static final int TICK_WIDTH = 84;
    private static final int TICK_HEIGHT = 20;
    private static final int COM_GAP = 5;
    private static final String YEAR = Inter.getLocText("Year");
    private static final String MONTH = Inter.getLocText("Month");
    private static final String DAY = Inter.getLocText("Sun");
    private static final String HOUR = Inter.getLocText("Sche-Hour");
    private static final String MINUTE = Inter.getLocText("Sche-Minute");
    private static final String SECOND = Inter.getLocText("Sche-Second");
    private static String TYPES[] = {
        Inter.getLocText("Year"), Inter.getLocText("Month"), Inter.getLocText("Sun"), Inter.getLocText("Sche-Hour"), Inter.getLocText("Sche-Minute"), Inter.getLocText("Sche-Second")
    };
    private static Map VALUES;
    private static Map INTS;
    private UIButton addButton;
    private JPanel tablePane;
    private UIObserverListener observerListener;

    public TimeSwitchPane()
    {
        initTablePane();
        initAddButton();
        setLayout(new Layout());
        add(addButton);
        add(tablePane);
        initSelfListener(tablePane);
        initSelfListener(addButton);
    }

    private void initTablePane()
    {
        tablePane = new JPanel(new TableLayout());
        tablePane.add(new TimeTickBox(new Formula("1"), 2));
        tablePane.add(new TimeTickBox(new Formula("3"), 2));
        tablePane.add(new TimeTickBox(new Formula("6"), 2));
        tablePane.add(new TimeTickBox(new Formula("1"), 3));
        tablePane.revalidate();
    }

    protected void initAddButton()
    {
        addButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/add.png")) {

            final TimeSwitchPane this$0;

            public boolean shouldResponseChangeListener()
            {
                return false;
            }

            
            {
                this$0 = TimeSwitchPane.this;
                super(icon);
            }
        }
;
        addButton.setBorderType(1);
        addButton.setOtherBorder(UIConstants.BS, UIConstants.LINE_COLOR);
        addButton.addActionListener(getAddButtonListener());
    }

    protected ActionListener getAddButtonListener()
    {
        return new ActionListener() {

            final TimeSwitchPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                tablePane.add(new TimeTickBox(new Formula(), 3));
                tablePane.revalidate();
                fireChange();
            }

            
            {
                this$0 = TimeSwitchPane.this;
                super();
            }
        }
;
    }

    protected void deleteTimeTick(TimeTickBox timetickbox)
    {
        tablePane.remove(timetickbox);
        tablePane.revalidate();
        fireChange();
    }

    public void populate(Plot plot)
    {
        ArrayList arraylist = plot.getxAxis().getTimeSwitchMap();
        if(arraylist == null || arraylist.isEmpty())
            return;
        tablePane.removeAll();
        TimeSwitchAttr timeswitchattr;
        for(Iterator iterator = arraylist.iterator(); iterator.hasNext(); tablePane.add(new TimeTickBox(timeswitchattr.getTimeUnit(), timeswitchattr.getUnit())))
            timeswitchattr = (TimeSwitchAttr)iterator.next();

        tablePane.revalidate();
        initSelfListener(tablePane);
    }

    public void update(Plot plot)
    {
        ArrayList arraylist = plot.getxAxis().getTimeSwitchMap();
        if(arraylist == null)
            return;
        arraylist.clear();
        Component acomponent[] = tablePane.getComponents();
        int i = acomponent.length;
        for(int j = 0; j < i; j++)
        {
            Component component = acomponent[j];
            if(component instanceof TimeTickBox)
            {
                TimeTickBox timetickbox = (TimeTickBox)component;
                arraylist.add(new TimeSwitchAttr(new Formula(timetickbox.mainUnitField.getText()), ((Integer)VALUES.get(timetickbox.mainType.getSelectedItem())).intValue()));
            }
        }

    }

    private void fireChange()
    {
        observerListener.doChange();
        HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().fireTargetModified();
    }

    public void registerChangeListener(UIObserverListener uiobserverlistener)
    {
        observerListener = uiobserverlistener;
    }

    public boolean shouldResponseChangeListener()
    {
        return true;
    }

    private void initSelfListener(Container container)
    {
        for(int i = 0; i < container.getComponentCount(); i++)
        {
            Component component = container.getComponent(i);
            if(component instanceof Container)
                initSelfListener((Container)component);
            if(component instanceof UIObserver)
                ((UIObserver)component).registerChangeListener(observerListener);
        }

    }

    static 
    {
        VALUES = new HashMap();
        VALUES.put(YEAR, Integer.valueOf(3));
        VALUES.put(MONTH, Integer.valueOf(2));
        VALUES.put(DAY, Integer.valueOf(1));
        VALUES.put(HOUR, Integer.valueOf(4));
        VALUES.put(MINUTE, Integer.valueOf(5));
        VALUES.put(SECOND, Integer.valueOf(6));
        INTS = new HashMap();
        INTS.put(Integer.valueOf(3), Inter.getLocText("Year"));
        INTS.put(Integer.valueOf(2), Inter.getLocText("Month"));
        INTS.put(Integer.valueOf(1), Inter.getLocText("Sun"));
        INTS.put(Integer.valueOf(4), Inter.getLocText("Sche-Hour"));
        INTS.put(Integer.valueOf(5), Inter.getLocText("Sche-Minute"));
        INTS.put(Integer.valueOf(6), Inter.getLocText("Sche-Second"));
    }





}
