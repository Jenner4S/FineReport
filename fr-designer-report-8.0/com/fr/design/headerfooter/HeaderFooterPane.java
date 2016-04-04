// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.headerfooter;

import com.fr.base.*;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.page.ReportSettingsProvider;
import com.fr.report.core.ReportHF;
import com.fr.stable.unit.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Hashtable;
import javax.swing.*;
import javax.swing.event.*;

// Referenced classes of package com.fr.design.headerfooter:
//            HeaderFooterEditPane

public abstract class HeaderFooterPane extends BasicPane
{

    private Hashtable reportHFHash;
    private JList reportHFYypeList;
    private int lastSelectedHFType;
    private UICheckBox defineCheckBox;
    private HeaderFooterEditPane headerFooterEditPane;
    private boolean isHeader;
    ChangeListener defineChangeListener;
    ListSelectionListener reportHFTypeSelectionListener;
    UIComboBoxRenderer reportPageTypeRenderer;

    public HeaderFooterPane()
    {
        reportHFHash = new Hashtable();
        lastSelectedHFType = -1;
        defineChangeListener = new ChangeListener() {

            final HeaderFooterPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                Object obj = reportHFYypeList.getSelectedValue();
                if(obj == null || !(obj instanceof Integer))
                    return;
                int i = ((Integer)obj).intValue();
                if(defineCheckBox.isSelected())
                {
                    GUICoreUtils.setEnabled(headerFooterEditPane, true);
                    reportHFHash.put(Integer.valueOf(i), headerFooterEditPane.update());
                } else
                {
                    GUICoreUtils.setEnabled(headerFooterEditPane, false);
                    reportHFHash.remove(new Integer(i));
                }
                reportHFYypeList.repaint();
            }

            
            {
                this$0 = HeaderFooterPane.this;
                super();
            }
        }
;
        reportHFTypeSelectionListener = new ListSelectionListener() {

            final HeaderFooterPane this$0;

            public void valueChanged(ListSelectionEvent listselectionevent)
            {
                Object obj = reportHFYypeList.getSelectedValue();
                if(obj == null || !(obj instanceof Integer))
                    return;
                int i = ((Integer)obj).intValue();
                if(i != lastSelectedHFType)
                {
                    if(lastSelectedHFType != -1)
                        if(defineCheckBox.isSelected())
                            reportHFHash.put(Integer.valueOf(lastSelectedHFType), headerFooterEditPane.update());
                        else
                            reportHFHash.remove(new Integer(lastSelectedHFType));
                    lastSelectedHFType = i;
                    Object obj1 = reportHFHash.get(new Integer(i));
                    if(obj1 == null)
                        defineCheckBox.setSelected(false);
                    else
                        defineCheckBox.setSelected(true);
                    headerFooterEditPane.populate((ReportHF)obj1, FU.getInstance(PaperSize.PAPERSIZE_A4.getWidth().toFU() - (new INCH(0.75F)).toFU() - (new INCH(0.75F)).toFU()).toPixD(ScreenResolution.getScreenResolution()), 0.53000000000000003D * (double)ScreenResolution.getScreenResolution());
                    if(i == 0)
                    {
                        defineCheckBox.setSelected(true);
                        defineCheckBox.setEnabled(false);
                    } else
                    {
                        defineCheckBox.setEnabled(true);
                    }
                }
            }

            
            {
                this$0 = HeaderFooterPane.this;
                super();
            }
        }
;
        reportPageTypeRenderer = new UIComboBoxRenderer() {

            final HeaderFooterPane this$0;

            public Component getListCellRendererComponent(JList jlist, Object obj, int i, boolean flag, boolean flag1)
            {
                super.getListCellRendererComponent(jlist, obj, i, flag, flag1);
                if(obj != null && (obj instanceof Integer))
                {
                    int j = ((Integer)obj).intValue();
                    setIcon(BaseUtils.readIcon("/com/fr/base/images/oem/logo.png"));
                    if(j == 0)
                        setText(Inter.getLocText("HF-Default_Page"));
                    else
                    if(j == 1)
                        setText(Inter.getLocText("HF-First_Page"));
                    else
                    if(j == 2)
                        setText(Inter.getLocText("Utils-Last_Page"));
                    else
                    if(j == 3)
                        setText(Inter.getLocText("HF-Odd_Page"));
                    else
                    if(j == 4)
                        setText(Inter.getLocText("HF-Even_Page"));
                    if(reportHFHash != null)
                    {
                        Object obj1 = reportHFHash.get(new Integer(j));
                        if(obj1 == null)
                            setEnabled(false);
                    }
                }
                return this;
            }

            
            {
                this$0 = HeaderFooterPane.this;
                super();
            }
        }
;
        setLayout(new BorderLayout(0, 4));
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        add(jpanel, "West");
        DefaultListModel defaultlistmodel = new DefaultListModel();
        reportHFYypeList = new JList(defaultlistmodel);
        reportHFYypeList.setCellRenderer(reportPageTypeRenderer);
        reportHFYypeList.addListSelectionListener(reportHFTypeSelectionListener);
        JScrollPane jscrollpane = new JScrollPane(reportHFYypeList);
        jpanel.add(jscrollpane, "Center");
        defaultlistmodel.addElement(new Integer(0));
        defaultlistmodel.addElement(new Integer(1));
        defaultlistmodel.addElement(new Integer(2));
        defaultlistmodel.addElement(new Integer(3));
        defaultlistmodel.addElement(new Integer(4));
        JPanel jpanel1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        add(jpanel1, "Center");
        JPanel jpanel2 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        jpanel1.add(jpanel2, "North");
        defineCheckBox = new UICheckBox(Inter.getLocText("HF-Whether_to_define_the_selected_type"), true);
        jpanel2.add(defineCheckBox);
        defineCheckBox.addChangeListener(defineChangeListener);
        headerFooterEditPane = new HeaderFooterEditPane();
        jpanel1.add(headerFooterEditPane, "Center");
        headerFooterEditPane.setBorder(BorderFactory.createLineBorder(GUICoreUtils.getTitleLineBorderColor()));
    }

    public void populate(ReportSettingsProvider reportsettingsprovider, boolean flag)
    {
        headerFooterEditPane.populateReportSettings(reportsettingsprovider, flag);
    }

    public UNIT updateReportSettings()
    {
        return headerFooterEditPane.updateReportSettings();
    }

    public void populate(Hashtable hashtable)
    {
        reportHFHash = hashtable;
        reportHFYypeList.setSelectedIndex(0);
    }

    public Hashtable update()
    {
        Object obj = reportHFYypeList.getSelectedValue();
        if(obj != null && (obj instanceof Integer))
            if(defineCheckBox.isSelected())
                reportHFHash.put(Integer.valueOf(lastSelectedHFType), headerFooterEditPane.update());
            else
                reportHFHash.remove(new Integer(lastSelectedHFType));
        return reportHFHash;
    }






}
