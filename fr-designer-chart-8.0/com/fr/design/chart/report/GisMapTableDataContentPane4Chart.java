// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.report;

import com.fr.base.Utils;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartdata.GisMapTableDefinition;
import com.fr.chart.chartdata.SeriesDefinition;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itable.UITableEditor;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableModel;

public class GisMapTableDataContentPane4Chart extends FurtherBasicBeanPane
    implements UIObserver
{
    private class InnerTableEditor extends UITableEditor
    {

        private JComponent editorComponent;
        final GisMapTableDataContentPane4Chart this$0;

        public Object getCellEditorValue()
        {
            if(editorComponent instanceof UITextField)
            {
                UITextField uitextfield = (UITextField)editorComponent;
                return uitextfield.getText();
            }
            if(editorComponent instanceof UIComboBox)
            {
                UIComboBox uicombobox = (UIComboBox)editorComponent;
                return uicombobox.getSelectedItem();
            } else
            {
                return super.getCellEditorValue();
            }
        }

        public Component getTableCellEditorComponent(JTable jtable, Object obj, boolean flag, int i, int j)
        {
            if(j == jtable.getModel().getColumnCount())
                return null;
            if(j == 0)
            {
                UITextField uitextfield = new UITextField();
                if(obj != null)
                    uitextfield.setText(Utils.objectToString(obj));
                uitextfield.addFocusListener(new FocusAdapter() {

                    final InnerTableEditor this$1;

                    public void focusLost(FocusEvent focusevent)
                    {
                        titleValuePane.stopCellEditing();
                        titleValuePane.fireTargetChanged();
                    }

                
                {
                    this$1 = InnerTableEditor.this;
                    super();
                }
                }
);
                editorComponent = uitextfield;
            } else
            {
                UIComboBox uicombobox = new UIComboBox(initNames);
                uicombobox.addItemListener(new ItemListener() {

                    final InnerTableEditor this$1;

                    public void itemStateChanged(ItemEvent itemevent)
                    {
                        titleValuePane.fireTargetChanged();
                        titleValuePane.stopCellEditing();
                    }

                
                {
                    this$1 = InnerTableEditor.this;
                    super();
                }
                }
);
                if(obj != null && StringUtils.isNotEmpty(obj.toString()))
                    uicombobox.setSelectedItem(obj);
                else
                    uicombobox.setSelectedItem(obj);
                editorComponent = uicombobox;
            }
            return editorComponent;
        }

        private InnerTableEditor()
        {
            this$0 = GisMapTableDataContentPane4Chart.this;
            super();
        }

    }


    private ArrayList changeListeners;
    private String initNames[] = {
        ""
    };
    private UIButtonGroup addressType;
    private UIButtonGroup lnglatOrder;
    private UIComboBox addressBox;
    private UIComboBox addressNameBox;
    private UICorrelationPane titleValuePane;
    private JPanel orderPane;
    private TableDataWrapper tableDataWrapper;

    public GisMapTableDataContentPane4Chart()
    {
        changeListeners = new ArrayList();
        setLayout(new BorderLayout());
        addressType = new UIButtonGroup(new String[] {
            Inter.getLocText("Chart-Gis_Address"), Inter.getLocText("Chart-Gis_LatLng")
        });
        lnglatOrder = new UIButtonGroup(new String[] {
            Inter.getLocText("Chart-Lng_First"), Inter.getLocText("Chart-Lat_First")
        });
        addressBox = new UIComboBox();
        addressNameBox = new UIComboBox();
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d
        };
        orderPane = new JPanel(new BorderLayout(6, 0)) {

            final GisMapTableDataContentPane4Chart this$0;

            public Dimension getPreferredSize()
            {
                if(isVisible())
                    return super.getPreferredSize();
                else
                    return new Dimension(0, 0);
            }

            
            {
                this$0 = GisMapTableDataContentPane4Chart.this;
                super(layoutmanager);
            }
        }
;
        orderPane.add(new UILabel(Inter.getLocText("Chart-LatLng_Order")), "West");
        orderPane.add(lnglatOrder, "Center");
        orderPane.setVisible(false);
        lnglatOrder.setSelectedIndex(0);
        addressType.setSelectedIndex(0);
        addressNameBox.removeAllItems();
        addressNameBox.addItem(Inter.getLocText("Chart-Use_None"));
        Component acomponent[][] = {
            {
                addressType, addressBox
            }, {
                orderPane, null
            }, {
                new UILabel((new StringBuilder()).append(Inter.getLocText("Chart-Address_Name")).append(":").toString(), 4), addressNameBox
            }
        };
        JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        JPanel jpanel1 = new JPanel();
        add(jpanel1, "Center");
        jpanel1.setLayout(new BorderLayout());
        jpanel1.add(jpanel, "North");
        String as[] = {
            Inter.getLocText("Chart-Area_Title"), Inter.getLocText("Chart-Area_Value")
        };
        titleValuePane = new UICorrelationPane(as) {

            final GisMapTableDataContentPane4Chart this$0;

            public UITableEditor createUITableEditor()
            {
                return new InnerTableEditor();
            }

            transient 
            {
                this$0 = GisMapTableDataContentPane4Chart.this;
                super(as);
            }
        }
;
        jpanel1.add(titleValuePane, "Center");
    }

    private void refresh2ComboBox()
    {
        TableDataWrapper tabledatawrapper = tableDataWrapper;
        if(tabledatawrapper == null)
            return;
        java.util.List list = tabledatawrapper.calculateColumnNameList();
        initNames = (String[])list.toArray(new String[list.size()]);
        addressBox.removeAllItems();
        addressNameBox.removeAllItems();
        addressNameBox.addItem(Inter.getLocText("Chart-Use_None"));
        int i = 0;
        for(int j = initNames.length; i < j; i++)
        {
            addressBox.addItem(initNames[i]);
            addressNameBox.addItem(initNames[i]);
        }

        if(initNames.length > 0)
            addressBox.setSelectedIndex(0);
        addressNameBox.setSelectedIndex(0);
        stopEditing();
    }

    public boolean accept(Object obj)
    {
        return false;
    }

    public void reset()
    {
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("Chart-DS_TableData");
    }

    private void stopEditing()
    {
    }

    public void populateBean(GisMapTableDefinition gismaptabledefinition)
    {
        stopEditing();
        if(gismaptabledefinition instanceof GisMapTableDefinition)
        {
            GisMapTableDefinition gismaptabledefinition1 = gismaptabledefinition;
            if(gismaptabledefinition.isAddress())
            {
                addressType.setSelectedIndex(0);
                orderPane.setVisible(false);
            } else
            {
                addressType.setSelectedIndex(1);
                orderPane.setVisible(true);
            }
            if(gismaptabledefinition.isLngFirst())
                lnglatOrder.setSelectedIndex(0);
            else
                lnglatOrder.setSelectedIndex(1);
            addressBox.setSelectedItem(gismaptabledefinition1.getAddress());
            if(StringUtils.isEmpty(gismaptabledefinition1.getAddressName()))
                addressNameBox.setSelectedItem(Inter.getLocText("Chart-Use_None"));
            else
                addressNameBox.setSelectedItem(gismaptabledefinition1.getAddressName());
            ArrayList arraylist = new ArrayList();
            int i = gismaptabledefinition1.getTittleValueSize();
            for(int j = 0; j < i; j++)
            {
                SeriesDefinition seriesdefinition = gismaptabledefinition1.getTittleValueWithIndex(j);
                if(seriesdefinition != null && seriesdefinition.getSeriesName() != null && seriesdefinition.getValue() != null)
                    arraylist.add(((Object) (new Object[] {
                        seriesdefinition.getSeriesName(), seriesdefinition.getValue()
                    })));
            }

            if(!arraylist.isEmpty())
                titleValuePane.populateBean(arraylist);
        }
    }

    public GisMapTableDefinition updateBean()
    {
        stopEditing();
        GisMapTableDefinition gismaptabledefinition = new GisMapTableDefinition();
        TableDataWrapper tabledatawrapper = tableDataWrapper;
        if(tabledatawrapper == null || addressBox.getSelectedItem() == null)
            return null;
        gismaptabledefinition.setTableData(tableDataWrapper.getTableData());
        gismaptabledefinition.setAddress(Utils.objectToString(addressBox.getSelectedItem()));
        if(addressType.getSelectedIndex() == 0)
        {
            gismaptabledefinition.setAddressType(true);
            lnglatOrder.setVisible(false);
        } else
        {
            gismaptabledefinition.setAddressType(false);
            lnglatOrder.setVisible(true);
        }
        if(lnglatOrder.getSelectedIndex() == 0)
            gismaptabledefinition.setLnglatOrder(true);
        else
            gismaptabledefinition.setLnglatOrder(false);
        if(addressNameBox.getSelectedItem() != null)
        {
            String s = Utils.objectToString(addressNameBox.getSelectedItem());
            if(ArrayUtils.contains(ChartConstants.NONE_KEYS, s))
                gismaptabledefinition.setAddressName("");
            else
                gismaptabledefinition.setAddressName(s);
        }
        java.util.List list = titleValuePane.updateBean();
        int i = 0;
        for(int j = list.size(); i < j; i++)
        {
            Object aobj[] = (Object[])(Object[])list.get(i);
            if(aobj.length == 2)
            {
                SeriesDefinition seriesdefinition = new SeriesDefinition();
                seriesdefinition.setSeriesName(aobj[0]);
                seriesdefinition.setValue(aobj[1]);
                gismaptabledefinition.addTittleValue(seriesdefinition);
            }
        }

        return gismaptabledefinition;
    }

    public void registerChangeListener(final UIObserverListener listener)
    {
        changeListeners.add(new ChangeListener() {

            final UIObserverListener val$listener;
            final GisMapTableDataContentPane4Chart this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                listener.doChange();
            }

            
            {
                this$0 = GisMapTableDataContentPane4Chart.this;
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

    public void fireTableDataChange(TableDataWrapper tabledatawrapper)
    {
        tableDataWrapper = tabledatawrapper;
        refresh2ComboBox();
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((GisMapTableDefinition)obj);
    }


}
