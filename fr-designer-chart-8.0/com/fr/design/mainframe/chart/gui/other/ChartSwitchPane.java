// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.other;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.chart.ChartControlPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ChartSwitchPane extends AbstractAttrNoScrollPane
{

    private UIButton changeButton;
    private ChartCollection editingChartCollection;
    private ChartEditPane currentChartEditPane;

    public ChartSwitchPane()
    {
    }

    protected JPanel createContentPane()
    {
        JPanel jpanel = new JPanel();
        jpanel.setLayout(new BorderLayout());
        changeButton = new UIButton(Inter.getLocText("Switch"));
        jpanel.add(changeButton, "North");
        changeButton.addActionListener(new ActionListener() {

            final ChartSwitchPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                final ChartControlPane chartTypeManager = new ChartControlPane();
                chartTypeManager.populate(editingChartCollection);
                BasicDialog basicdialog = chartTypeManager.showWindow4ChartType(SwingUtilities.getWindowAncestor(new JPanel()), new DialogActionAdapter() {

                    final ChartControlPane val$chartTypeManager;
                    final _cls1 this$1;

                    public void doOk()
                    {
                        chartTypeManager.update(editingChartCollection);
                        if(currentChartEditPane != null)
                        {
                            currentChartEditPane.populate(editingChartCollection);
                            currentChartEditPane.GoToPane(new String[] {
                                PaneTitleConstants.CHART_TYPE_TITLE
                            });
                            currentChartEditPane.GoToPane(new String[] {
                                PaneTitleConstants.CHART_OTHER_TITLE, PaneTitleConstants.CHART_OTHER_TITLE_CHANGE
                            });
                            currentChartEditPane.fire();
                        }
                    }

                    
                    {
                        this$1 = _cls1.this;
                        chartTypeManager = chartcontrolpane;
                        super();
                    }
                }
);
                basicdialog.setVisible(true);
            }

            
            {
                this$0 = ChartSwitchPane.this;
                super();
            }
        }
);
        return jpanel;
    }

    public void registerChartEditPane(ChartEditPane charteditpane)
    {
        currentChartEditPane = charteditpane;
    }

    public void populateBean(ChartCollection chartcollection)
    {
        editingChartCollection = chartcollection;
    }

    public void updateBean(ChartCollection chartcollection)
    {
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("Chart-Switch");
    }

    public String getIconPath()
    {
        return null;
    }


}
