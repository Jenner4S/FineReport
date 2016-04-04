// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.editor.editor.IntegerEditor;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.FRExplainLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.Inter;
import com.fr.report.core.ReportUtils;
import com.fr.report.stable.LayerReportAttr;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.ProductConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class LayerReportPane extends BasicBeanPane
{

    private static final int LABEL_HEIGHT = 55;
    private UICheckBox isLayerReportBox;
    private UICheckBox isPageQueryBox;
    private IntegerEditor countPerPageEditor;
    private CardLayout card;
    private JPanel cardPane;
    private WorkSheet worksheet;

    public LayerReportPane(WorkSheet worksheet1)
    {
        worksheet = worksheet1;
        initComponents();
    }

    protected void initComponents()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText(new String[] {
            "Report_Engine", "Attribute"
        }));
        JPanel jpanel1 = FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
        add(jpanel);
        jpanel.add(jpanel1);
        JPanel jpanel2 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        isLayerReportBox = new UICheckBox(Inter.getLocText("Execute_Report_by_Layer_Engine"));
        isLayerReportBox.setSelected(false);
        jpanel2.add(isLayerReportBox);
        jpanel1.add(jpanel2);
        card = new CardLayout();
        cardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        cardPane.setLayout(card);
        cardPane.add(FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane(), "none");
        JPanel jpanel3 = createPageQueryPane();
        cardPane.add(jpanel3, "page");
        jpanel1.add(cardPane);
        isLayerReportBox.addActionListener(new ActionListener() {

            final LayerReportPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(isLayerReportBox.isSelected())
                    card.show(cardPane, "page");
                else
                    card.show(cardPane, "none");
            }

            
            {
                this$0 = LayerReportPane.this;
                super();
            }
        }
);
        JPanel jpanel4 = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("Attention"));
        FRExplainLabel frexplainlabel = new FRExplainLabel(Inter.getLocText("Layer_Report_Warnning_info"));
        frexplainlabel.setPreferredSize(new Dimension(frexplainlabel.getPreferredSize().width, 55));
        jpanel4.add(frexplainlabel);
        jpanel1.add(jpanel4);
    }

    private JPanel createPageQueryPane()
    {
        double d = -2D;
        double ad[] = {
            d, d
        };
        double ad1[] = {
            d, d
        };
        isPageQueryBox = new UICheckBox();
        isPageQueryBox.setSelected(false);
        isPageQueryBox.addActionListener(new ActionListener() {

            final LayerReportPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(isPageQueryBox.isSelected())
                    countPerPageEditor.setEnabled(true);
                else
                    countPerPageEditor.setEnabled(false);
            }

            
            {
                this$0 = LayerReportPane.this;
                super();
            }
        }
);
        countPerPageEditor = new IntegerEditor(new Integer(30));
        countPerPageEditor.setPreferredSize(new Dimension(120, 20));
        countPerPageEditor.setEnabled(false);
        Component acomponent[][] = {
            {
                new UILabel((new StringBuilder()).append(Inter.getLocText("LayerPageReport_PageEngine")).append(":").toString()), isPageQueryBox
            }, {
                new UILabel((new StringBuilder()).append(Inter.getLocText("LayerPageReport_CountPerPage")).append(":").toString()), countPerPageEditor
            }
        };
        JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad, ad1);
        jpanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 0, 0));
        return jpanel;
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText(new String[] {
            "Report_Engine", "Attribute"
        });
    }

    public void populateBean(LayerReportAttr layerreportattr)
    {
        isLayerReportBox.setSelected(layerreportattr != null);
        if(layerreportattr != null)
        {
            card.show(cardPane, "page");
            countPerPageEditor.setValue(new Integer(layerreportattr.getCountPerPage()));
            if(layerreportattr.isPageQuery())
            {
                isPageQueryBox.setSelected(true);
                countPerPageEditor.setEnabled(true);
            } else
            {
                isPageQueryBox.setSelected(false);
                countPerPageEditor.setEnabled(false);
            }
        } else
        {
            card.show(cardPane, "none");
        }
    }

    public LayerReportAttr updateBean()
    {
        LayerReportAttr layerreportattr = null;
        if(isLayerReportBox.isSelected())
        {
            layerreportattr = new LayerReportAttr();
            layerreportattr.setPageQuery(isPageQueryBox.isSelected());
            layerreportattr.setCountPerPage(Math.min(500, countPerPageEditor.getValue().intValue()));
        }
        return layerreportattr;
    }

    public void checkValid()
        throws Exception
    {
        if(isLayerReportBox.isSelected() && !ReportUtils.isLayerReportUsable(worksheet))
        {
            int i = JOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(), (new StringBuilder()).append(Inter.getLocText("After_Changed_Some_Attributes_Are_Different")).append("?").toString(), ProductConstants.PRODUCT_NAME, 0);
            if(i != 0)
                isLayerReportBox.setSelected(false);
        }
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((LayerReportAttr)obj);
    }





}
