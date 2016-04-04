// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.base.BaseUtils;
import com.fr.base.IconManager;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.fun.ExportToolBarProvider;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.javascript.JavaScriptActionPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.style.background.BackgroundPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.design.widget.IconDefinePane;
import com.fr.form.ui.Button;
import com.fr.form.ui.CustomToolBarButton;
import com.fr.form.ui.Widget;
import com.fr.form.ui.WidgetManager;
import com.fr.form.ui.WidgetManagerProvider;
import com.fr.general.Background;
import com.fr.general.Inter;
import com.fr.js.JavaScript;
import com.fr.report.web.button.Export;
import com.fr.report.web.button.PDFPrint;
import com.fr.report.web.button.Print;
import com.fr.report.web.button.write.AppendColumnRow;
import com.fr.report.web.button.write.Submit;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

// Referenced classes of package com.fr.design.webattr:
//            ToolBarButton, FToolBar

public class EditToolBar extends BasicPane
{
    public class ButtonPane extends BasicPane
    {

        private CardLayout card;
        private JPanel centerPane;
        private UICheckBox icon;
        private UICheckBox text;
        private UICheckBox pdf;
        private UICheckBox excelP;
        private UICheckBox excelO;
        private UICheckBox excelS;
        private UICheckBox image;
        private UICheckBox word;
        private UICheckBox flashPrint;
        private UICheckBox pdfPrint;
        private UICheckBox appletPrint;
        private UICheckBox serverPrint;
        private UICheckBox isPopup;
        private UICheckBox isVerify;
        private UICheckBox failSubmit;
        private UIBasicSpinner count;
        private Widget widget;
        private UITextField nameField;
        private IconDefinePane iconPane;
        private UIButton button;
        private JavaScriptActionPane javaScriptPane;
        private ExportToolBarProvider exportToolBarProviders[];
        private ChangeListener changeListener;
        ActionListener l;
        final EditToolBar this$0;

        public void initComponents()
        {
            exportToolBarProviders = ExtraDesignClassManager.getInstance().getExportToolBarProviders();
            setLayout(FRGUIPaneFactory.createBorderLayout());
            JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
            icon = new UICheckBox(Inter.getLocText("FR-Designer_Show_Icon"));
            text = new UICheckBox(Inter.getLocText("FR-Designer_Show_Text"));
            jpanel.add(icon, "North");
            jpanel.add(text, "Center");
            nameField = new UITextField(8);
            iconPane = new IconDefinePane();
            javaScriptPane = JavaScriptActionPane.createDefault();
            double d = -2D;
            double ad[] = {
                d, d
            };
            double ad1[] = {
                d, d
            };
            Component acomponent[][] = {
                {
                    new UILabel((new StringBuilder()).append(Inter.getLocText(new String[] {
                        "Widget", "Printer-Alias"
                    })).append(":").toString()), nameField
                }, {
                    new UILabel((new StringBuilder()).append(Inter.getLocText(new String[] {
                        "Widget", "Icon"
                    })).append(":").toString()), iconPane
                }
            };
            JPanel jpanel1 = TableLayoutHelper.createTableLayoutPane(acomponent, ad, ad1);
            jpanel.add(jpanel1, "South");
            jpanel.setBorder(BorderFactory.createTitledBorder(Inter.getLocText(new String[] {
                "Form-Button", "Property", "Set"
            })));
            add(jpanel, "North");
            JPanel jpanel2 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            centerPane = FRGUIPaneFactory.createCardLayout_S_Pane();
            card = new CardLayout();
            centerPane.setLayout(card);
            centerPane.add("custom", getCustomPane());
            centerPane.add("export", getExport());
            centerPane.add("print", getPrint());
            centerPane.add("none", jpanel2);
            centerPane.add("pdfprint", getPdfPrintSetting());
            centerPane.add(getCpane(), "appendcount");
            centerPane.add(getSubmitPane(), "submit");
            add(centerPane, "Center");
        }

        private JPanel getCustomPane()
        {
            JPanel jpanel = FRGUIPaneFactory.createCenterFlowInnerContainer_S_Pane();
            button = new UIButton(Inter.getLocText("FR-Designer_User_Defined_Event"));
            jpanel.add(button);
            jpanel.setBorder(GUICoreUtils.createTitledBorder((new StringBuilder()).append(Inter.getLocText("FR-Designer_Edit")).append("JS").toString(), null));
            button.addActionListener(l);
            return jpanel;
        }

        private JPanel getExport()
        {
            JPanel jpanel = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
            pdf = new UICheckBox(Inter.getLocText("FR-Designer_Output_PDF"));
            excelP = new UICheckBox(Inter.getLocText("FR-Designer-Output_Excel_Page"));
            excelO = new UICheckBox(Inter.getLocText("FR-Designer-Output_Excel_Simple"));
            excelS = new UICheckBox(Inter.getLocText("FR-Designer-Output_Excel_Sheet"));
            word = new UICheckBox(Inter.getLocText("FR-Designer_Output_Word"));
            image = new UICheckBox(Inter.getLocText("FR-Designer_Image"));
            jpanel.add(pdf);
            jpanel.add(Box.createVerticalStrut(2));
            jpanel.add(excelP);
            jpanel.add(Box.createVerticalStrut(2));
            jpanel.add(excelO);
            jpanel.add(Box.createVerticalStrut(2));
            jpanel.add(excelS);
            jpanel.add(Box.createVerticalStrut(2));
            jpanel.add(word);
            jpanel.add(Box.createVerticalStrut(2));
            jpanel.add(image);
            for(int i = 0; i < ArrayUtils.getLength(exportToolBarProviders); i++)
                jpanel = exportToolBarProviders[i].updateCenterPane(jpanel);

            jpanel.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText(new String[] {
                "Form-Button", "Property", "Set"
            }), null));
            return jpanel;
        }

        private JPanel getPrint()
        {
            JPanel jpanel = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
            flashPrint = new UICheckBox(Inter.getLocText("FR-Designer_Flash_Print"));
            pdfPrint = new UICheckBox(Inter.getLocText("FR-Designer_PDF_Print"));
            appletPrint = new UICheckBox(Inter.getLocText("FR-Designer_Applet_Print"));
            serverPrint = new UICheckBox(Inter.getLocText("FR-Designer_Server_Print"));
            jpanel.add(flashPrint);
            jpanel.add(pdfPrint);
            jpanel.add(appletPrint);
            jpanel.add(serverPrint);
            jpanel.setBorder(BorderFactory.createTitledBorder(Inter.getLocText(new String[] {
                "Form-Button", "Property", "Set"
            })));
            return jpanel;
        }

        private JPanel getCpane()
        {
            JPanel jpanel = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
            count = new UIBasicSpinner(new SpinnerNumberModel(1, 0, 0x7fffffff, 1));
            UILabel uilabel = new UILabel((new StringBuilder()).append(Inter.getLocText(new String[] {
                "Add", "Row", "Column", "Numbers"
            })).append(":").toString());
            JPanel jpanel1 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            jpanel1.add(uilabel);
            jpanel1.add(count);
            jpanel.add(jpanel1);
            return jpanel1;
        }

        private JPanel getPdfPrintSetting()
        {
            JPanel jpanel = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
            isPopup = new UICheckBox(Inter.getLocText("PDF-Print_isPopup"));
            jpanel.add(isPopup);
            jpanel.setBorder(BorderFactory.createTitledBorder(Inter.getLocText("PDF-Print_Setting")));
            return jpanel;
        }

        private JPanel getSubmitPane()
        {
            isVerify = new UICheckBox(Inter.getLocText("Verify-Data_Verify"));
            failSubmit = new UICheckBox(Inter.getLocText(new String[] {
                "Verify_Fail", "Still", "Submit"
            }));
            JPanel jpanel = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
            jpanel.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText(new String[] {
                "Form-Button", "Property", "Set"
            }), null));
            jpanel.add(isVerify);
            jpanel.add(failSubmit);
            isVerify.addChangeListener(changeListener);
            return jpanel;
        }

        protected String title4PopupWindow()
        {
            return "Button";
        }

        public void populate(Widget widget1)
        {
            widget = widget1;
            card.show(centerPane, "none");
            if(widget1 instanceof Button)
                populateDefault();
            if(widget1 instanceof Export)
                populateExport();
            else
            if(widget1 instanceof Print)
                populatePrint();
            else
            if(widget1 instanceof PDFPrint)
                populatePDFPrint();
            else
            if(widget1 instanceof AppendColumnRow)
                populateAppendColumnRow();
            else
            if(widget1 instanceof Submit)
                populateSubmit();
            else
            if(widget1 instanceof CustomToolBarButton)
                populateCustomToolBarButton();
        }

        private void populateAppendColumnRow()
        {
            card.show(centerPane, "appendcount");
            count.setValue(Integer.valueOf(((AppendColumnRow)widget).getCount()));
        }

        private void populateExport()
        {
            card.show(centerPane, "export");
            Export export = (Export)widget;
            pdf.setSelected(export.isPdfAvailable());
            excelP.setSelected(export.isExcelPAvailable());
            excelO.setSelected(export.isExcelOAvailable());
            excelS.setSelected(export.isExcelSAvailable());
            word.setSelected(export.isWordAvailable());
            image.setSelected(export.isImageAvailable());
            if(exportToolBarProviders != null)
            {
                for(int i = 0; i < exportToolBarProviders.length; i++)
                    exportToolBarProviders[i].populate();

            }
        }

        private void populateCustomToolBarButton()
        {
            card.show(centerPane, "custom");
            CustomToolBarButton customtoolbarbutton = (CustomToolBarButton)widget;
            if(customtoolbarbutton.getJSImpl() != null)
                javaScriptPane.populateBean(customtoolbarbutton.getJSImpl());
        }

        private void populateSubmit()
        {
            card.show(centerPane, "submit");
            Submit submit = (Submit)widget;
            isVerify.setSelected(submit.isVerify());
            failSubmit.setSelected(submit.isFailVerifySubmit());
        }

        private void populatePDFPrint()
        {
            card.show(centerPane, "pdfprint");
            PDFPrint pdfprint = (PDFPrint)widget;
            isPopup.setSelected(pdfprint.isPopup());
        }

        private void populatePrint()
        {
            card.show(centerPane, "print");
            Print print = (Print)widget;
            pdfPrint.setSelected(print.isPDFPrint());
            appletPrint.setSelected(print.isAppletPrint());
            flashPrint.setSelected(print.isFlashPrint());
            serverPrint.setSelected(print.isServerPrint());
        }

        private void populateDefault()
        {
            Button button1 = (Button)widget;
            icon.setSelected(button1.isShowIcon());
            text.setSelected(button1.isShowText());
            nameField.setText(button1.getText());
            iconPane.populate(((Button)widget).getIconName());
        }

        public Widget update()
        {
            if(widget instanceof Export)
                updateExport();
            else
            if(widget instanceof Print)
                updatePrint();
            else
            if(widget instanceof PDFPrint)
            {
                PDFPrint pdfprint = (PDFPrint)widget;
                pdfprint.setPopup(isPopup.isSelected());
            } else
            if(widget instanceof AppendColumnRow)
                ((AppendColumnRow)widget).setCount(((Integer)count.getValue()).intValue());
            else
            if(widget instanceof Submit)
                updateSubmit();
            else
            if(widget instanceof CustomToolBarButton)
                ((CustomToolBarButton)widget).setJSImpl((JavaScript)javaScriptPane.updateBean());
            if(widget instanceof Button)
                updateDefault();
            return widget;
        }

        private void updateDefault()
        {
            ((Button)widget).setShowIcon(icon.isSelected());
            ((Button)widget).setShowText(text.isSelected());
            ((Button)widget).setText(nameField.getText());
            ((Button)widget).setIconName(iconPane.update());
        }

        private void updateSubmit()
        {
            Submit submit = (Submit)widget;
            submit.setVerify(isVerify.isSelected());
            submit.setFailVerifySubmit(failSubmit.isSelected());
        }

        private void updatePrint()
        {
            Print print = (Print)widget;
            print.setAppletPrint(appletPrint.isSelected());
            print.setFlashPrint(flashPrint.isSelected());
            print.setPDFPrint(pdfPrint.isSelected());
            print.setServerPrint(serverPrint.isSelected());
        }

        private void updateExport()
        {
            Export export = (Export)widget;
            export.setPdfAvailable(pdf.isSelected());
            export.setExcelPAvailable(excelP.isSelected());
            export.setExcelOAvailable(excelO.isSelected());
            export.setExcelSAvailable(excelS.isSelected());
            export.setWordAvailable(word.isSelected());
            export.setImageAvailable(image.isSelected());
            if(exportToolBarProviders != null)
            {
                for(int i = 0; i < exportToolBarProviders.length; i++)
                    exportToolBarProviders[i].update();

            }
        }










        public ButtonPane()
        {
            this$0 = EditToolBar.this;
            super();
            changeListener = new ChangeListener() {

                final ButtonPane this$1;

                public void stateChanged(ChangeEvent changeevent)
                {
                    if(isVerify.isSelected())
                        failSubmit.setVisible(true);
                    else
                        failSubmit.setVisible(false);
                }

                
                {
                    this$1 = ButtonPane.this;
                    super();
                }
            }
;
            l = new ActionListener() {

                final ButtonPane this$1;

                public void actionPerformed(ActionEvent actionevent)
                {
                    if(!(widget instanceof CustomToolBarButton))
                        return;
                    if(javaScriptPane == null || ((CustomToolBarButton)widget).getJSImpl() == null)
                        javaScriptPane = JavaScriptActionPane.createDefault();
                    javaScriptPane.setPreferredSize(new Dimension(750, 500));
                    BasicDialog basicdialog = javaScriptPane.showWindow(SwingUtilities.getWindowAncestor(ButtonPane.this));
                    basicdialog.addDialogActionListener(new DialogActionAdapter() {

                        final _cls2 this$2;

                        public void doOk()
                        {
                            ((CustomToolBarButton)widget).setJSImpl((JavaScript)javaScriptPane.updateBean());
                        }

                        
                        {
                            this$2 = _cls2.this;
                            super();
                        }
                    }
);
                    basicdialog.setVisible(true);
                }

                
                {
                    this$1 = ButtonPane.this;
                    super();
                }
            }
;
            initComponents();
        }
    }

    public class RemoveAction extends UpdateAction
    {

        final EditToolBar this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            int i = list.getSelectedIndex();
            if(i < 0 || !(listModel.getElementAt(i) instanceof ToolBarButton))
                return;
            int j = JOptionPane.showConfirmDialog(EditToolBar.this, (new StringBuilder()).append(Inter.getLocText("FR-Designer_Are_You_Sure_To_Delete_The_Data")).append("?").toString(), "Message", 0);
            if(j != 0)
                return;
            listModel.removeElementAt(i);
            list.validate();
            if(listModel.size() > 0)
                list.setSelectedIndex(0);
            else
                card.show(right, "none");
        }

        public RemoveAction()
        {
            this$0 = EditToolBar.this;
            super();
            setName(Inter.getLocText("FR-Designer_Delete"));
            setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/cell/control/remove.png"));
        }
    }

    private class MoveDownItemAction extends UpdateAction
    {

        final EditToolBar this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            int i = list.getSelectedIndex();
            if(i == -1)
                return;
            if(i == -1)
                return;
            if(i < list.getModel().getSize() - 1)
            {
                DefaultListModel defaultlistmodel = (DefaultListModel)list.getModel();
                Object obj = defaultlistmodel.get(i + 1);
                defaultlistmodel.set(i + 1, defaultlistmodel.get(i));
                defaultlistmodel.set(i, obj);
                list.setSelectedIndex(i + 1);
                list.ensureIndexIsVisible(i + 1);
                list.validate();
            }
        }

        public MoveDownItemAction()
        {
            this$0 = EditToolBar.this;
            super();
            setName(Inter.getLocText("Utils-Move_Down"));
            setMnemonic('D');
            setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/down.png"));
        }
    }

    private class MoveUpItemAction extends UpdateAction
    {

        final EditToolBar this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            int i = list.getSelectedIndex();
            if(i == -1)
                return;
            if(i > 0)
            {
                DefaultListModel defaultlistmodel = (DefaultListModel)list.getModel();
                Object obj = defaultlistmodel.get(i - 1);
                defaultlistmodel.set(i - 1, defaultlistmodel.get(i));
                defaultlistmodel.set(i, obj);
                list.setSelectedIndex(i - 1);
                list.ensureIndexIsVisible(i - 1);
                list.validate();
            }
        }

        public MoveUpItemAction()
        {
            this$0 = EditToolBar.this;
            super();
            setName(Inter.getLocText("Utils-Move_Up"));
            setMnemonic('U');
            setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/up.png"));
        }
    }


    private JWorkBook jwb;
    private JList list;
    private DefaultListModel listModel;
    private JPanel right;
    private CardLayout card;
    private ButtonPane bp;
    private ToolBarButton lastButton;
    private Background background;
    private UICheckBox defaultCheckBox;
    private ListSelectionListener listSelectionListener;
    private ActionListener actioner;
    ListCellRenderer render;

    public EditToolBar()
    {
        background = null;
        listSelectionListener = new ListSelectionListener() {

            final EditToolBar this$0;

            public void valueChanged(ListSelectionEvent listselectionevent)
            {
                if(lastButton != null && (lastButton.getWidget() instanceof Button))
                    if(!(list.getSelectedValue() instanceof ToolBarButton) || !(((ToolBarButton)(ToolBarButton)list.getSelectedValue()).getWidget() instanceof CustomToolBarButton))
                    {
                        lastButton.setWidget(bp.update());
                    } else
                    {
                        ((Button)lastButton.getWidget()).setShowIcon(bp.icon.isSelected());
                        ((Button)lastButton.getWidget()).setShowText(bp.text.isSelected());
                        ((Button)lastButton.getWidget()).setText(bp.nameField.getText());
                        ((Button)lastButton.getWidget()).setIconName(bp.iconPane.update());
                    }
                if(list.getSelectedValue() instanceof ToolBarButton)
                {
                    lastButton = (ToolBarButton)list.getSelectedValue();
                    if(lastButton.getWidget() instanceof Button)
                    {
                        card.show(right, "button");
                        bp.populate(lastButton.getWidget());
                    } else
                    {
                        bp.populate(lastButton.getWidget());
                        card.show(right, "none");
                    }
                }
            }

            
            {
                this$0 = EditToolBar.this;
                super();
            }
        }
;
        actioner = new ActionListener() {

            final EditToolBar this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                final BackgroundPane backgroundPane = new BackgroundPane();
                BasicDialog basicdialog = backgroundPane.showWindow(DesignerContext.getDesignerFrame());
                backgroundPane.populate(background);
                basicdialog.addDialogActionListener(new DialogActionAdapter() {

                    final BackgroundPane val$backgroundPane;
                    final _cls2 this$1;

                    public void doOk()
                    {
                        background = backgroundPane.update();
                        if(background != null)
                            defaultCheckBox.setSelected(false);
                    }

                    
                    {
                        this$1 = _cls2.this;
                        backgroundPane = backgroundpane;
                        super();
                    }
                }
);
                basicdialog.setVisible(true);
            }

            
            {
                this$0 = EditToolBar.this;
                super();
            }
        }
;
        render = new DefaultListCellRenderer() {

            final EditToolBar this$0;

            public Component getListCellRendererComponent(JList jlist, Object obj, int i, boolean flag, boolean flag1)
            {
                super.getListCellRendererComponent(jlist, obj, i, flag, flag1);
                if(obj instanceof ToolBarButton)
                {
                    ToolBarButton toolbarbutton = (ToolBarButton)obj;
                    setText(toolbarbutton.getNameOption().optionName());
                    setIcon(toolbarbutton.getNameOption().optionIcon());
                }
                return this;
            }

            
            {
                this$0 = EditToolBar.this;
                super();
            }
        }
;
        initComponent();
    }

    public void initComponent()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        listModel = new DefaultListModel();
        list = new JList(listModel);
        list.setCellRenderer(render);
        jpanel.add(new JScrollPane(list), "Center");
        if(listModel.getSize() > 0)
            list.setSelectedIndex(0);
        ToolBarDef toolbardef = new ToolBarDef();
        toolbardef.addShortCut(new ShortCut[] {
            new MoveUpItemAction()
        });
        toolbardef.addShortCut(new ShortCut[] {
            new MoveDownItemAction()
        });
        toolbardef.addShortCut(new ShortCut[] {
            new RemoveAction()
        });
        com.fr.design.gui.itoolbar.UIToolbar uitoolbar = ToolBarDef.createJToolBar();
        toolbardef.updateToolBar(uitoolbar);
        jpanel.add(uitoolbar, "North");
        right = FRGUIPaneFactory.createCardLayout_S_Pane();
        card = new CardLayout();
        right.setLayout(card);
        bp = new ButtonPane();
        right.add("none", FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane());
        right.add("button", bp);
        JSplitPane jsplitpane = new JSplitPane(1, true, jpanel, right);
        jsplitpane.setDividerLocation(120);
        add(jsplitpane);
        list.addListSelectionListener(listSelectionListener);
        JPanel jpanel1 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        UIButton uibutton = new UIButton(Inter.getLocText(new String[] {
            "Background", "Set"
        }));
        defaultCheckBox = new UICheckBox(Inter.getLocText(new String[] {
            "Default", "Background"
        }));
        uibutton.addActionListener(actioner);
        jpanel1.add(defaultCheckBox);
        jpanel1.add(uibutton);
        jpanel1.setBorder(BorderFactory.createTitledBorder(Inter.getLocText(new String[] {
            "Background", "Set"
        })));
        add(jpanel1, "South");
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("FR-Designer_Edit");
    }

    public void populate(FToolBar ftoolbar)
    {
        populate(ftoolbar, null);
    }

    public void populate(FToolBar ftoolbar, ToolBarButton toolbarbutton)
    {
        if(ftoolbar == null)
            return;
        for(int i = 0; i < ftoolbar.getButtonlist().size(); i++)
            listModel.addElement(ftoolbar.getButtonlist().get(i));

        list.validate();
        list.repaint();
        if(ftoolbar.getButtonlist().size() > 0)
            list.setSelectedIndex(0);
        if(toolbarbutton != null)
            list.setSelectedValue(toolbarbutton, true);
        background = ftoolbar.getBackground();
        defaultCheckBox.setSelected(ftoolbar.isDefault());
    }

    public FToolBar update()
    {
        if(list.getSelectedIndex() > -1)
        {
            for(int i = 0; i < listModel.getSize(); i++)
            {
                list.setSelectedIndex(i);
                ToolBarButton toolbarbutton = (ToolBarButton)list.getSelectedValue();
                Widget widget = bp.update();
                toolbarbutton.setWidget(widget);
                if(!(widget instanceof Button))
                    continue;
                String s = ((Button)widget).getIconName();
                if(!StringUtils.isBlank(s))
                {
                    java.awt.Image image = WidgetManager.getProviderInstance().getIconManager().getIconImage(s);
                    toolbarbutton.setIcon(new ImageIcon(image));
                }
            }

        }
        ArrayList arraylist = new ArrayList();
        for(int j = 0; j < listModel.size(); j++)
            arraylist.add((ToolBarButton)listModel.get(j));

        FToolBar ftoolbar = new FToolBar();
        ftoolbar.setButtonlist(arraylist);
        ftoolbar.setDefault(defaultCheckBox.isSelected());
        if(!ftoolbar.isDefault())
            ftoolbar.setBackground(background);
        return ftoolbar;
    }










}
