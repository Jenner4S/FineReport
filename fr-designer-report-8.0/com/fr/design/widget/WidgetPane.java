// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.widget.btn.ButtonConstants;
import com.fr.form.ui.*;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.*;

// Referenced classes of package com.fr.design.widget:
//            CellWidgetCardPane

public class WidgetPane extends BasicPane
    implements ItemListener
{
    public static class Item
    {

        private String name;
        private Object value;

        public Object getValue()
        {
            return value;
        }

        public String getName()
        {
            return name;
        }

        public String toString()
        {
            return name;
        }

        public boolean equals(Object obj)
        {
            return (obj instanceof Item) && ComparatorUtils.equals(((Item)obj).value, value) && ComparatorUtils.equals(((Item)obj).name, name);
        }

        public Item(String s, Object obj)
        {
            name = s;
            value = obj;
        }
    }

    private static class EditorTypeComboBox extends UIComboBox
    {

        private Item item;

        public void setSelectedItemByWidgetClass(Class class1)
        {
            WidgetOption awidgetoption[] = getWidgetOptions();
            int i = 0;
            for(int j = getModel().getSize(); i < j; i++)
            {
                Item item1 = (Item)getModel().getElementAt(i);
                if((item1.getValue() instanceof Integer) && awidgetoption[((Integer)item1.getValue()).intValue()].widgetClass() == class1)
                    setSelectedItem(item1);
            }

        }

        public void setSelectedItem(Object obj)
        {
            if(obj == item)
            {
                return;
            } else
            {
                super.setSelectedItem(obj);
                return;
            }
        }

        private Vector getWidgetsName(boolean flag)
        {
            WidgetOption awidgetoption[] = getWidgetOptions();
            Vector vector = new Vector();
            int i = 0;
            for(int j = awidgetoption.length; i < j; i++)
                vector.add(new Item(awidgetoption[i].optionName(), Integer.valueOf(i)));

            WidgetManagerProvider widgetmanagerprovider = WidgetManager.getProviderInstance();
            Iterator iterator = widgetmanagerprovider.getWidgetConfigNameIterator();
            if(flag && iterator.hasNext())
            {
                vector.add(item);
                String s;
                for(; iterator.hasNext(); vector.add(new Item(s, s)))
                    s = (String)iterator.next();

            }
            return vector;
        }

        public Widget getCellWidget()
        {
            Item item1 = (Item)getSelectedItem();
            if(item1.getValue() instanceof Integer)
                return getWidgetOptions()[((Integer)item1.getValue()).intValue()].createWidget();
            if(item1.getValue() instanceof String)
                return new NameWidget((String)item1.getValue());
            else
                return null;
        }

        private WidgetOption[] getWidgetOptions()
        {
            return (WidgetOption[])(WidgetOption[])ArrayUtils.addAll(WidgetOption.getReportWidgetInstance(), ExtraDesignClassManager.getInstance().getCellWidgetOptions());
        }


        public EditorTypeComboBox(boolean flag)
        {
            item = new Item(Inter.getLocText("Widget-User_Defined"), Inter.getLocText("Widget-User_Defined"));
            setEditable(false);
            setModel(new DefaultComboBoxModel(getWidgetsName(flag)));
            setRenderer(new UIComboBoxRenderer() {

                final EditorTypeComboBox this$0;

                public Component getListCellRendererComponent(JList jlist, Object obj, int i, boolean flag1, boolean flag2)
                {
                    if(obj == item)
                    {
                        UILabel uilabel = new UILabel((new StringBuilder()).append(Inter.getLocText("Widget-User_Defined")).append(" ！！！！").toString());
                        uilabel.setEnabled(false);
                        return uilabel;
                    } else
                    {
                        return super.getListCellRendererComponent(jlist, obj, i, flag1, flag2);
                    }
                }

                
                {
                    this$0 = EditorTypeComboBox.this;
                    super();
                }
            }
);
            setPreferredSize(new Dimension(100, 20));
        }
    }


    private EditorTypeComboBox editorTypeComboBox;
    private CellWidgetCardPane cellEditorCardPane;
    private boolean shouldFireSelectedEvent;
    protected JPanel northPane;

    public WidgetPane()
    {
        this(null);
    }

    public WidgetPane(ElementCasePane elementcasepane)
    {
        initComponents(elementcasepane);
    }

    protected void initComponents(ElementCasePane elementcasepane)
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        northPane = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
        add(northPane, "North");
        northPane.add(new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Designer_Type")).append(":").toString()));
        editorTypeComboBox = new EditorTypeComboBox(elementcasepane != null);
        editorTypeComboBox.setPreferredSize(new Dimension(150, 30));
        editorTypeComboBox.setMaximumRowCount(16);
        northPane.add(editorTypeComboBox);
        editorTypeComboBox.addItemListener(this);
        cellEditorCardPane = new CellWidgetCardPane(elementcasepane);
        add(cellEditorCardPane, "Center");
    }

    public void itemStateChanged(ItemEvent itemevent)
    {
        if(itemevent.getStateChange() == 1)
        {
            if((itemevent.getItem() instanceof Item) && (((Item)itemevent.getItem()).getValue() instanceof WidgetConfig))
            {
                populate(editorTypeComboBox.getCellWidget());
                return;
            }
            if(shouldFireSelectedEvent)
            {
                Widget widget = editorTypeComboBox.getCellWidget();
                populateWidgetConfig(widget);
            }
        }
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("FR-Designer_Widget");
    }

    public void populate(Widget widget)
    {
        if(widget == null)
            widget = new TextEditor();
        if(widget instanceof NameWidget)
        {
            String s = ((NameWidget)widget).getName();
            shouldFireSelectedEvent = false;
            editorTypeComboBox.setSelectedItem(new Item(s, s));
            shouldFireSelectedEvent = true;
            cellEditorCardPane.populate(widget);
            return;
        }
        Object obj = widget.getClass();
        if(ArrayUtils.contains(ButtonConstants.CLASSES4BUTTON, obj))
            obj = com/fr/form/ui/Button;
        shouldFireSelectedEvent = false;
        editorTypeComboBox.setSelectedItemByWidgetClass(((Class) (obj)));
        shouldFireSelectedEvent = true;
        cellEditorCardPane.populate(widget);
    }

    public Widget update()
    {
        return cellEditorCardPane.update();
    }

    protected void populateWidgetConfig(Widget widget)
    {
        cellEditorCardPane.populate(widget);
    }

    public void checkValid()
        throws Exception
    {
        cellEditorCardPane.checkValid();
    }
}
