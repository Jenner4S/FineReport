// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.report;

import com.fr.base.Formula;
import com.fr.base.Utils;
import com.fr.chart.chartdata.BaseSeriesDefinition;
import com.fr.chart.chartdata.GisMapReportDefinition;
import com.fr.chart.chartdata.SeriesDefinition;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.constants.UIConstants;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itable.UITableEditor;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableModel;

public class GisMapReportDataContentPane extends FurtherBasicBeanPane
    implements UIObserver
{
    private class InnerTableEditor extends UITableEditor
    {

        private JComponent editorComponent;
        final GisMapReportDataContentPane this$0;

        public Object getCellEditorValue()
        {
            if(editorComponent instanceof TinyFormulaPane)
                return ((TinyFormulaPane)editorComponent).getUITextField().getText();
            if(editorComponent instanceof UITextField)
                return ((UITextField)editorComponent).getText();
            else
                return super.getCellEditorValue();
        }

        public Component getTableCellEditorComponent(JTable jtable, Object obj, boolean flag, int i, int j)
        {
            if(j == jtable.getModel().getColumnCount())
                return null;
            else
                return getEditorComponent(j, obj);
        }

        private JComponent getEditorComponent(int i, Object obj)
        {
            if(i == 0)
            {
                UITextField uitextfield = new UITextField();
                addListener4UITextFiled(uitextfield);
                if(obj != null)
                    uitextfield.setText(Utils.objectToString(obj));
                editorComponent = uitextfield;
            } else
            {
                TinyFormulaPane tinyformulapane = new TinyFormulaPane() {

                    final InnerTableEditor this$1;

                    public void okEvent()
                    {
                        seriesPane.stopCellEditing();
                        seriesPane.fireTargetChanged();
                    }

                
                {
                    this$1 = InnerTableEditor.this;
                    super();
                }
                }
;
                tinyformulapane.setBackground(UIConstants.FLESH_BLUE);
                addListener4UITextFiled(tinyformulapane.getUITextField());
                if(obj != null)
                    tinyformulapane.getUITextField().setText(Utils.objectToString(obj));
                editorComponent = tinyformulapane;
            }
            return editorComponent;
        }

        private void addListener4UITextFiled(UITextField uitextfield)
        {
            uitextfield.addFocusListener(new FocusAdapter() {

                final InnerTableEditor this$1;

                public void focusLost(FocusEvent focusevent)
                {
                    seriesPane.fireTargetChanged();
                }

                
                {
                    this$1 = InnerTableEditor.this;
                    super();
                }
            }
);
        }

        private InnerTableEditor()
        {
            this$0 = GisMapReportDataContentPane.this;
            super();
        }

    }


    private UIButtonGroup addressType;
    private UIButtonGroup lnglatOrder;
    private TinyFormulaPane addressPane;
    private TinyFormulaPane addressNamePane;
    private UICorrelationPane seriesPane;
    private JPanel orderPane;
    private ArrayList changeListeners;

    public GisMapReportDataContentPane()
    {
        changeListeners = new ArrayList();
        initCom();
    }

    private void initCom()
    {
        setLayout(new BorderLayout(0, 0));
        addressType = new UIButtonGroup(new String[] {
            Inter.getLocText("Chart-Address"), Inter.getLocText("Chart-LngLat")
        });
        lnglatOrder = new UIButtonGroup(new String[] {
            Inter.getLocText("Chart-LngFirst"), Inter.getLocText("Chart-LatFirst")
        });
        addressPane = new TinyFormulaPane();
        addressNamePane = new TinyFormulaPane();
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d
        };
        orderPane = new JPanel(new BorderLayout(6, 0)) {

            final GisMapReportDataContentPane this$0;

            public Dimension getPreferredSize()
            {
                if(isVisible())
                    return super.getPreferredSize();
                else
                    return new Dimension(0, 0);
            }

            
            {
                this$0 = GisMapReportDataContentPane.this;
                super(layoutmanager);
            }
        }
;
        orderPane.add(new UILabel(Inter.getLocText("Chart-LatLngOrder")), "West");
        orderPane.add(lnglatOrder, "Center");
        orderPane.setVisible(false);
        lnglatOrder.setSelectedIndex(0);
        addressType.setSelectedIndex(0);
        Component acomponent[][] = {
            {
                addressType, addressPane
            }, {
                orderPane, null
            }, {
                new UILabel((new StringBuilder()).append(" ").append(Inter.getLocText("Chart-Address-Name")).append(":").toString(), 4), addressNamePane
            }
        };
        JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        add(jpanel, "North");
        String as[] = {
            Inter.getLocText(new String[] {
                "Filed", "Title"
            }), Inter.getLocText("Area_Value")
        };
        seriesPane = new UICorrelationPane(as) {

            final GisMapReportDataContentPane this$0;

            public UITableEditor createUITableEditor()
            {
                return new InnerTableEditor();
            }

            transient 
            {
                this$0 = GisMapReportDataContentPane.this;
                super(as);
            }
        }
;
        add(seriesPane, "Center");
    }

    public boolean accept(Object obj)
    {
        return true;
    }

    public void reset()
    {
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("Cell");
    }

    public void populateBean(GisMapReportDefinition gismapreportdefinition)
    {
        if(gismapreportdefinition.getCategoryName() != null)
        {
            if(gismapreportdefinition.isAddress())
            {
                addressType.setSelectedIndex(0);
                orderPane.setVisible(false);
            } else
            {
                addressType.setSelectedIndex(1);
                orderPane.setVisible(true);
            }
            if(gismapreportdefinition.isLngFirst())
                lnglatOrder.setSelectedIndex(0);
            else
                lnglatOrder.setSelectedIndex(1);
            addressPane.populateBean(Utils.objectToString(gismapreportdefinition.getCategoryName()));
            if(gismapreportdefinition.getAddressName() != null)
                addressNamePane.populateBean(Utils.objectToString(gismapreportdefinition.getAddressName()));
            int i = gismapreportdefinition.getTitleValueSize();
            ArrayList arraylist = new ArrayList();
            for(int j = 0; j < i; j++)
            {
                BaseSeriesDefinition baseseriesdefinition = gismapreportdefinition.getTitleValueWithIndex(j);
                if(baseseriesdefinition != null && baseseriesdefinition.getSeriesName() != null && baseseriesdefinition.getValue() != null)
                    arraylist.add(((Object) (new Object[] {
                        baseseriesdefinition.getSeriesName(), baseseriesdefinition.getValue()
                    })));
            }

            if(!arraylist.isEmpty())
                seriesPane.populateBean(arraylist);
        }
    }

    public GisMapReportDefinition updateBean()
    {
        GisMapReportDefinition gismapreportdefinition = new GisMapReportDefinition();
        if(addressType.getSelectedIndex() == 0)
        {
            gismapreportdefinition.setAddressType(true);
            orderPane.setVisible(false);
        } else
        {
            gismapreportdefinition.setAddressType(false);
            orderPane.setVisible(true);
        }
        if(lnglatOrder.getSelectedIndex() == 0)
            gismapreportdefinition.setLnglatOrder(true);
        else
            gismapreportdefinition.setLnglatOrder(false);
        String s = addressPane.updateBean();
        if(StringUtils.isBlank(s))
            return null;
        if(StableUtils.canBeFormula(s))
            gismapreportdefinition.setCategoryName(new Formula(s));
        else
            gismapreportdefinition.setCategoryName(s);
        String s1 = addressNamePane.updateBean();
        if(s1 != null && !StringUtils.isBlank(s1))
            if(StableUtils.canBeFormula(s1))
                gismapreportdefinition.setAddressName(s1);
            else
                gismapreportdefinition.setAddressName(s1);
        java.util.List list = seriesPane.updateBean();
        if(list != null && !list.isEmpty())
        {
            int i = 0;
            for(int j = list.size(); i < j; i++)
            {
                Object aobj[] = (Object[])(Object[])list.get(i);
                Object obj = aobj[0];
                Object obj1 = aobj[1];
                if(StableUtils.canBeFormula(obj1))
                    obj1 = new Formula(Utils.objectToString(obj1));
                SeriesDefinition seriesdefinition = new SeriesDefinition(obj, obj1);
                gismapreportdefinition.addTitleValue(seriesdefinition);
            }

        }
        return gismapreportdefinition;
    }

    public void registerChangeListener(final UIObserverListener listener)
    {
        changeListeners.add(new ChangeListener() {

            final UIObserverListener val$listener;
            final GisMapReportDataContentPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                listener.doChange();
            }

            
            {
                this$0 = GisMapReportDataContentPane.this;
                listener = uiobserverlistener;
                super();
            }
        }
);
    }

    public boolean shouldResponseChangeListener()
    {
        return true;
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((GisMapReportDefinition)obj);
    }

}
