// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.base.BaseUtils;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.GeneralUtils;
import com.fr.general.Inter;
import com.fr.report.web.Printer;
import com.fr.stable.StringUtils;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ServerPrinterPane extends BasicPane
{
    public class PrintersPane extends BasicPane
    {

        private UIComboBox printerCombo;
        final ServerPrinterPane this$0;

        protected void initComponents()
        {
            setLayout(FRGUIPaneFactory.createBorderLayout());
            setBorder(BorderFactory.createEmptyBorder(20, 5, 0, 0));
            JPanel jpanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            jpanel.add(new UILabel((new StringBuilder()).append(Inter.getLocText("Printer")).append(":").toString()), "West");
            DefaultComboBoxModel defaultcomboboxmodel = new DefaultComboBoxModel();
            printerCombo = new UIComboBox(defaultcomboboxmodel);
            jpanel.add(printerCombo);
            String as[] = GeneralUtils.getSystemPrinterNameArray();
            for(int i = 0; i < as.length; i++)
                defaultcomboboxmodel.addElement(as[i]);

            add(jpanel);
        }

        public void checkValid()
            throws Exception
        {
            String s = printerCombo.getSelectedItem().toString();
            if(StringUtils.isBlank(s))
                throw new Exception((new StringBuilder()).append(Inter.getLocText("ReportServerP-The_name_of_printer_cannot_be_null")).append(".").toString());
            else
                return;
        }

        protected boolean isShowHelpButton()
        {
            return false;
        }

        protected String title4PopupWindow()
        {
            return Inter.getLocText("Printer");
        }

        public void populate(String s)
        {
            printerCombo.setSelectedItem(s);
        }

        public String update()
        {
            return printerCombo.getSelectedItem().toString();
        }

        public PrintersPane()
        {
            this$0 = ServerPrinterPane.this;
            super();
            initComponents();
        }
    }


    private JList printerList;
    private UIButton addButton;
    private UIButton editButton;
    private UIButton removeButton;
    private UIButton moveUpButton;
    private UIButton moveDownButton;
    ActionListener addActionListener;
    ActionListener editActionListener;
    ActionListener removeActionListener;
    ActionListener moveUpActionListener;
    ActionListener moveDownActionListener;
    ListSelectionListener printerSelectionListener;
    MouseAdapter mouseClickedListener;

    public ServerPrinterPane()
    {
        addActionListener = new ActionListener() {

            final ServerPrinterPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                final PrintersPane printersPane = new PrintersPane();
                BasicDialog basicdialog = printersPane.showSmallWindow(SwingUtilities.getWindowAncestor(ServerPrinterPane.this), new DialogActionAdapter() {

                    final PrintersPane val$printersPane;
                    final _cls1 this$1;

                    public void doOk()
                    {
                        String s = printersPane.update();
                        if(StringUtils.isNotBlank(s))
                        {
                            DefaultListModel defaultlistmodel = (DefaultListModel)printerList.getModel();
                            defaultlistmodel.addElement(s);
                            printerList.setSelectedIndex(defaultlistmodel.size() - 1);
                        }
                    }

                    
                    {
                        this$1 = _cls1.this;
                        printersPane = printerspane;
                        super();
                    }
                }
);
                basicdialog.setTitle((new StringBuilder()).append(Inter.getLocText("ReportServerP-Add_Printer")).append("...").toString());
                basicdialog.setVisible(true);
            }

            
            {
                this$0 = ServerPrinterPane.this;
                super();
            }
        }
;
        editActionListener = new ActionListener() {

            final ServerPrinterPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                editPrinterList();
            }

            
            {
                this$0 = ServerPrinterPane.this;
                super();
            }
        }
;
        removeActionListener = new ActionListener() {

            final ServerPrinterPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                int i = printerList.getSelectedIndex();
                if(i == -1)
                    return;
                int j = JOptionPane.showConfirmDialog(ServerPrinterPane.this, (new StringBuilder()).append(Inter.getLocText("ReportServerP-Are_you_sure_to_delete_the_selected_printer")).append("?").toString(), Inter.getLocText("Remove"), 2, 3);
                if(j == 0)
                {
                    ((DefaultListModel)printerList.getModel()).remove(i);
                    if(printerList.getModel().getSize() > 0)
                        if(i < printerList.getModel().getSize())
                            printerList.setSelectedIndex(i);
                        else
                            printerList.setSelectedIndex(printerList.getModel().getSize() - 1);
                    checkButtonEnabled();
                }
            }

            
            {
                this$0 = ServerPrinterPane.this;
                super();
            }
        }
;
        moveUpActionListener = new ActionListener() {

            final ServerPrinterPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                int i = printerList.getSelectedIndex();
                if(i > 0)
                {
                    DefaultListModel defaultlistmodel = (DefaultListModel)printerList.getModel();
                    Object obj = defaultlistmodel.get(i - 1);
                    defaultlistmodel.set(i - 1, defaultlistmodel.get(i));
                    defaultlistmodel.set(i, obj);
                    printerList.setSelectedIndex(i - 1);
                    checkButtonEnabled();
                }
            }

            
            {
                this$0 = ServerPrinterPane.this;
                super();
            }
        }
;
        moveDownActionListener = new ActionListener() {

            final ServerPrinterPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                int i = printerList.getSelectedIndex();
                if(i == -1)
                    return;
                if(i < printerList.getModel().getSize() - 1)
                {
                    DefaultListModel defaultlistmodel = (DefaultListModel)printerList.getModel();
                    Object obj = defaultlistmodel.get(i + 1);
                    defaultlistmodel.set(i + 1, defaultlistmodel.get(i));
                    defaultlistmodel.set(i, obj);
                    printerList.setSelectedIndex(i + 1);
                    checkButtonEnabled();
                }
            }

            
            {
                this$0 = ServerPrinterPane.this;
                super();
            }
        }
;
        printerSelectionListener = new ListSelectionListener() {

            final ServerPrinterPane this$0;

            public void valueChanged(ListSelectionEvent listselectionevent)
            {
                checkButtonEnabled();
            }

            
            {
                this$0 = ServerPrinterPane.this;
                super();
            }
        }
;
        mouseClickedListener = new MouseAdapter() {

            final ServerPrinterPane this$0;

            public void mouseClicked(MouseEvent mouseevent)
            {
                int i = mouseevent.getClickCount();
                if(i >= 2)
                    editPrinterList();
            }

            
            {
                this$0 = ServerPrinterPane.this;
                super();
            }
        }
;
        initComponents();
    }

    protected void initComponents()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        setBorder(BorderFactory.createEmptyBorder(6, 2, 4, 2));
        JToolBar jtoolbar = new JToolBar();
        add(jtoolbar, "North");
        Dimension dimension = new Dimension(24, 24);
        addButton = new UIButton(BaseUtils.readIcon("/com/fr/base/images/cell/control/add.png"));
        addButton.addActionListener(addActionListener);
        addButton.setToolTipText(Inter.getLocText("Add"));
        addButton.setPreferredSize(dimension);
        editButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/control/edit.png"));
        editButton.addActionListener(editActionListener);
        editButton.setToolTipText(Inter.getLocText("Edit"));
        editButton.setPreferredSize(dimension);
        removeButton = new UIButton(BaseUtils.readIcon("/com/fr/base/images/cell/control/remove.png"));
        removeButton.addActionListener(removeActionListener);
        removeButton.setToolTipText(Inter.getLocText("Remove"));
        removeButton.setPreferredSize(dimension);
        moveUpButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/control/up.png"));
        moveUpButton.addActionListener(moveUpActionListener);
        moveUpButton.setToolTipText(Inter.getLocText("Utils-Move_Up"));
        moveUpButton.setPreferredSize(dimension);
        moveDownButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/control/down.png"));
        moveDownButton.addActionListener(moveDownActionListener);
        moveDownButton.setToolTipText(Inter.getLocText("Utils-Move_Down"));
        moveDownButton.setPreferredSize(dimension);
        jtoolbar.add(addButton);
        jtoolbar.add(editButton);
        jtoolbar.add(removeButton);
        jtoolbar.add(moveUpButton);
        jtoolbar.add(moveDownButton);
        printerList = new JList(new DefaultListModel());
        printerList.addListSelectionListener(printerSelectionListener);
        printerList.addMouseListener(mouseClickedListener);
        add(new JScrollPane(printerList), "Center");
        checkButtonEnabled();
    }

    protected String title4PopupWindow()
    {
        return "printer";
    }

    private void checkButtonEnabled()
    {
        editButton.setEnabled(false);
        removeButton.setEnabled(false);
        moveUpButton.setEnabled(false);
        moveDownButton.setEnabled(false);
        int i = printerList.getSelectedIndex();
        if(i >= 0)
        {
            editButton.setEnabled(true);
            removeButton.setEnabled(true);
            if(i > 0)
                moveUpButton.setEnabled(true);
            if(i < printerList.getModel().getSize() - 1)
                moveDownButton.setEnabled(true);
        }
    }

    public void editPrinterList()
    {
        final int index = printerList.getSelectedIndex();
        final PrintersPane printersPane = new PrintersPane();
        BasicDialog basicdialog = printersPane.showSmallWindow(SwingUtilities.getWindowAncestor(this), new DialogActionAdapter() {

            final PrintersPane val$printersPane;
            final int val$index;
            final ServerPrinterPane this$0;

            public void doOk()
            {
                String s = printersPane.update();
                if(StringUtils.isNotBlank(s))
                {
                    DefaultListModel defaultlistmodel = (DefaultListModel)printerList.getModel();
                    defaultlistmodel.remove(index);
                    defaultlistmodel.add(index, s);
                    printerList.setSelectedIndex(index);
                }
            }

            
            {
                this$0 = ServerPrinterPane.this;
                printersPane = printerspane;
                index = i;
                super();
            }
        }
);
        printersPane.populate(printerList.getSelectedValue().toString());
        basicdialog.setTitle((new StringBuilder()).append(Inter.getLocText("ReportServerP-Edit_Printer")).append("...").toString());
        basicdialog.setVisible(true);
    }

    public void populate(Printer printer)
    {
        if(printer == null)
            return;
        if(printer.getPrinters() != null)
        {
            String as[] = printer.getPrinters();
            DefaultListModel defaultlistmodel = (DefaultListModel)printerList.getModel();
            defaultlistmodel.removeAllElements();
            for(int i = 0; i < as.length; i++)
                defaultlistmodel.addElement(as[i]);

            if(defaultlistmodel.size() > 0)
                printerList.setSelectedIndex(0);
        }
    }

    public Printer update()
    {
        Printer printer = new Printer();
        ArrayList arraylist = new ArrayList();
        DefaultListModel defaultlistmodel = (DefaultListModel)printerList.getModel();
        for(int i = 0; i < defaultlistmodel.size(); i++)
            arraylist.add(defaultlistmodel.get(i));

        if(arraylist.size() > 0)
        {
            int j = arraylist.size();
            String as[] = new String[j];
            for(int k = 0; k < j; k++)
                as[k] = arraylist.get(k).toString();

            printer.setPrinters(as);
        } else
        {
            printer.setPrinters(null);
        }
        return printer;
    }


}
