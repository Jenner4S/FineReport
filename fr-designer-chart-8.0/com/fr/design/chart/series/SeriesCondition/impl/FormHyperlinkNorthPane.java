// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.design.DesignModelAdapter;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.Widget;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.js.FormHyperlinkProvider;
import com.fr.stable.bridge.StableFactory;
import java.awt.Component;
import javax.swing.*;

public class FormHyperlinkNorthPane extends BasicBeanPane
{

    private UITextField itemNameTextField;
    private boolean needRenamePane;
    private Widget formHyperlinkEditors[];
    private UIComboBox targetFrameComboBox;

    public FormHyperlinkNorthPane(boolean flag)
    {
        needRenamePane = false;
        needRenamePane = flag;
        initComponents();
    }

    protected void initComponents()
    {
        setLayout(FRGUIPaneFactory.createM_BorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_L_Pane();
        add(jpanel, "Center");
        formHyperlinkEditors = getFormHyperlinkEditors();
        targetFrameComboBox = formHyperlinkEditors != null ? new UIComboBox(getFormHyperlinkEditNames()) : new UIComboBox();
        targetFrameComboBox.setRenderer(new DefaultListCellRenderer() {

            final FormHyperlinkNorthPane this$0;

            public Component getListCellRendererComponent(JList jlist, Object obj, int i, boolean flag, boolean flag1)
            {
                super.getListCellRendererComponent(jlist, obj, i, flag, flag1);
                return this;
            }

            
            {
                this$0 = FormHyperlinkNorthPane.this;
                super();
            }
        }
);
        double d = -2D;
        double ad[] = {
            d, d, d
        };
        double ad1[] = {
            d, -1D
        };
        Component acomponent[][];
        if(!needRenamePane)
        {
            acomponent = (new Component[][] {
                new Component[] {
                    new UILabel(Inter.getLocText("FR-Designer_Form-Object")), targetFrameComboBox
                }
            });
        } else
        {
            itemNameTextField = new UITextField();
            acomponent = (new Component[][] {
                new Component[] {
                    new UILabel(Inter.getLocText("FR-Designer_Name_has_Colon")), itemNameTextField
                }, new Component[] {
                    new UILabel(Inter.getLocText("FR-Designer_Form-Object")), targetFrameComboBox
                }
            });
        }
        JPanel jpanel1 = TableLayoutHelper.createTableLayoutPane(acomponent, ad, ad1);
        jpanel.add(jpanel1, "North");
    }

    public Widget getEditingEditor()
    {
        if(formHyperlinkEditors == null)
            return null;
        String s = (String)targetFrameComboBox.getSelectedItem();
        Widget awidget[] = formHyperlinkEditors;
        int i = awidget.length;
        for(int j = 0; j < i; j++)
        {
            Widget widget = awidget[j];
            if(ComparatorUtils.equals(s, widget.getWidgetName()))
                return widget;
        }

        return null;
    }

    private Widget[] getFormHyperlinkEditors()
    {
        return DesignModelAdapter.getCurrentModelAdapter().getLinkableWidgets();
    }

    private String[] getFormHyperlinkEditNames()
    {
        String as[] = new String[formHyperlinkEditors.length];
        int i = 0;
        Widget awidget[] = formHyperlinkEditors;
        int j = awidget.length;
        for(int k = 0; k < j; k++)
        {
            Widget widget = awidget[k];
            as[i] = widget.getWidgetName();
            i++;
        }

        return as;
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("Hyperlink-Form_link");
    }

    public void populateBean(FormHyperlinkProvider formhyperlinkprovider)
    {
        if(itemNameTextField != null)
            itemNameTextField.setText(formhyperlinkprovider.getItemName());
        String s = formhyperlinkprovider.getRelateEditorName();
        if(s == null)
        {
            return;
        } else
        {
            targetFrameComboBox.setSelectedItem(s);
            return;
        }
    }

    public FormHyperlinkProvider updateBean()
    {
        FormHyperlinkProvider formhyperlinkprovider = (FormHyperlinkProvider)StableFactory.getMarkedInstanceObjectFromClass("FormHyperlink", com/fr/js/FormHyperlinkProvider);
        updateBean(formhyperlinkprovider);
        return formhyperlinkprovider;
    }

    public void updateBean(FormHyperlinkProvider formhyperlinkprovider)
    {
        if(itemNameTextField != null)
            formhyperlinkprovider.setItemName(itemNameTextField.getText());
        formhyperlinkprovider.setRelateEditorName((String)targetFrameComboBox.getSelectedItem());
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((FormHyperlinkProvider)obj);
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((FormHyperlinkProvider)obj);
    }
}
