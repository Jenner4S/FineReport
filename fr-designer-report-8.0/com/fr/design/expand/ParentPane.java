// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.expand;

import com.fr.base.BaseUtils;
import com.fr.design.cell.smartaction.*;
import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.columnrow.ColumnRowPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.present.ColumnRowTableModel;
import com.fr.design.selection.SelectionEvent;
import com.fr.design.selection.SelectionListener;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.grid.Grid;
import com.fr.grid.GridUtils;
import com.fr.grid.selection.*;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellExpandAttr;
import com.fr.stable.ColumnRow;
import com.fr.stable.StableUtils;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class ParentPane extends BasicPane
{
    private class SmartJTablePane4CellExpandAttr extends SmartJTablePane
    {
        private class RowTableCellRenderer extends DefaultTableCellRenderer
        {

            final SmartJTablePane4CellExpandAttr this$1;

            public Component getTableCellRendererComponent(JTable jtable, Object obj, boolean flag, boolean flag1, int i, int j)
            {
                super.getTableCellRendererComponent(jtable, obj, flag, flag1, i, j);
                setText((new StringBuilder()).append(((Integer)obj).intValue() + 1).append("").toString());
                setBackground(Color.cyan);
                return this;
            }

            private RowTableCellRenderer()
            {
                this$1 = SmartJTablePane4CellExpandAttr.this;
                super();
            }

        }

        private class ColumnTableCellRenderer extends DefaultTableCellRenderer
        {

            final SmartJTablePane4CellExpandAttr this$1;

            public Component getTableCellRendererComponent(JTable jtable, Object obj, boolean flag, boolean flag1, int i, int j)
            {
                super.getTableCellRendererComponent(jtable, obj, flag, flag1, i, j);
                setText(StableUtils.convertIntToABC(((Integer)obj).intValue() + 1));
                setBackground(Color.cyan);
                return this;
            }

            private ColumnTableCellRenderer()
            {
                this$1 = SmartJTablePane4CellExpandAttr.this;
                super();
            }

        }


        private SmartJTablePaneAction a;
        private SelectionListener listener;
        final ParentPane this$0;

        public void setCellRenderer()
        {
            TableColumn tablecolumn = table.getColumnModel().getColumn(0);
            tablecolumn.setCellRenderer(new ColumnTableCellRenderer());
            TableColumn tablecolumn1 = table.getColumnModel().getColumn(1);
            tablecolumn1.setCellRenderer(new RowTableCellRenderer());
        }

        protected String title4PopupWindow()
        {
            return Inter.getLocText("RWA-Smart_Add_Cells");
        }



        public SmartJTablePane4CellExpandAttr(ColumnRowTableModel columnrowtablemodel, ElementCasePane elementcasepane)
        {
            this$0 = ParentPane.this;
            super(columnrowtablemodel, elementcasepane);
            a = new AbstractSmartJTablePaneAction(this, ParentPane.this) {

                final SmartJTablePane4CellExpandAttr this$1;

                public void doOk()
                {
                    GridUtils.doSelectCell(ePane, oldCellElement.getColumn(), oldCellElement.getRow());
                    ePane.getGrid().setNotShowingTableSelectPane(true);
                    ePane.repaint(10L);
                    cardLayout.show(parentCardPane, "Custom");
                    customParentColumnRowPane.populate(((ColumnRowTableModel)SmartJTablePane4CellExpandAttr.this.this$1.getModel()).getColumnRow(0));
                }

                
                {
                    this$1 = SmartJTablePane4CellExpandAttr.this;
                    super(smartjtablepane, container);
                }
            }
;
            listener = new SelectionListener() {

                final SmartJTablePane4CellExpandAttr this$1;

                public void selectionChanged(SelectionEvent selectionevent)
                {
                    ElementCasePane elementcasepane1 = (ElementCasePane)selectionevent.getSource();
                    Selection selection = elementcasepane1.getSelection();
                    if(selection instanceof FloatSelection)
                    {
                        return;
                    } else
                    {
                        CellSelection cellselection = (CellSelection)selection;
                        ColumnRowTableModel columnrowtablemodel1 = (ColumnRowTableModel)SmartJTablePane4CellExpandAttr.this.this$1.getModel();
                        ColumnRow columnrow = ColumnRow.valueOf(cellselection.getColumn(), cellselection.getRow());
                        columnrowtablemodel1.setColumnRow(columnrow, 0);
                        columnrowtablemodel1.fireTableDataChanged();
                        return;
                    }
                }

                
                {
                    this$1 = SmartJTablePane4CellExpandAttr.this;
                    super();
                }
            }
;
            changeSmartJTablePaneAction(a);
            changeGridSelectionChangeListener(listener);
        }
    }


    public static final int LEFT = 0;
    public static final int UP = 1;
    private CardLayout cardLayout;
    private JPanel parentCardPane;
    private UITextField noneParentText;
    private UITextField defaultParentText;
    private ColumnRowPane customParentColumnRowPane;
    private ElementCasePane ePane;
    private int leftOrUp;
    private ActionListener cellAttrPaneListener;
    private transient TemplateCellElement oldCellElement;

    public ParentPane(int i, ActionListener actionlistener)
    {
        leftOrUp = 0;
        cellAttrPaneListener = null;
        leftOrUp = i;
        cellAttrPaneListener = actionlistener;
        initComponents();
    }

    private void initComponents()
    {
        setLayout(new BorderLayout());
        final UIButton arrowButton = new UIButton(UIConstants.ARROW_DOWN_ICON);
        arrowButton.setRoundBorder(true, 2);
        add(arrowButton, "East");
        cardLayout = new CardLayout();
        parentCardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        parentCardPane.setLayout(cardLayout);
        customParentColumnRowPane = new ColumnRowPane();
        UIButton uibutton = new UIButton(BaseUtils.readIcon("com/fr/design/images/buttonicon/select.png")) {

            final ParentPane this$0;

            public Dimension getPreferredSize()
            {
                return new Dimension(24, 24);
            }

            
            {
                this$0 = ParentPane.this;
                super(icon);
            }
        }
;
        uibutton.addActionListener(new ActionListener() {

            final ParentPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(ePane == null || oldCellElement == null)
                    return;
                ePane.getGrid().setNotShowingTableSelectPane(false);
                ColumnRowTableModel columnrowtablemodel = new ColumnRowTableModel();
                columnrowtablemodel.addColumnRow(ColumnRow.valueOf(0, 0));
                SmartJTablePane4CellExpandAttr smartjtablepane4cellexpandattr = new SmartJTablePane4CellExpandAttr(columnrowtablemodel, ePane);
                Object obj = ParentPane.this;
                do
                {
                    if(obj == null)
                        break;
                    if(obj instanceof Dialog)
                    {
                        ((Container) (obj)).setVisible(false);
                        break;
                    }
                    obj = ((Container) (obj)).getParent();
                } while(true);
                com.fr.design.mainframe.DesignerFrame designerframe = DesignerContext.getDesignerFrame();
                BasicDialog basicdialog = smartjtablepane4cellexpandattr.showWindow(designerframe);
                ePane.setEditable(false);
                basicdialog.setModal(false);
                basicdialog.setVisible(true);
            }

            
            {
                this$0 = ParentPane.this;
                super();
            }
        }
);
        noneParentText = new UITextField(Inter.getLocText("None"));
        noneParentText.setRectDirection(4);
        parentCardPane.add("None", noneParentText);
        defaultParentText = new UITextField(Inter.getLocText("Default"));
        defaultParentText.setRectDirection(4);
        parentCardPane.add("Default", defaultParentText);
        parentCardPane.add("Custom", GUICoreUtils.createFlowPane(new JComponent[] {
            customParentColumnRowPane, uibutton
        }, 1));
        add(parentCardPane, "Center");
        final UIPopupMenu popup = createPopMenu();
        noneParentText.addMouseListener(new MouseAdapter() {

            final UIPopupMenu val$popup;
            final UIButton val$arrowButton;
            final ParentPane this$0;

            public void mousePressed(MouseEvent mouseevent)
            {
                Rectangle rectangle = parentCardPane.getBounds();
                popup.setPopupSize(rectangle.width + arrowButton.getSize().width, popup.getPreferredSize().height);
                popup.show(parentCardPane, 0, rectangle.height);
            }

            
            {
                this$0 = ParentPane.this;
                popup = uipopupmenu;
                arrowButton = uibutton;
                super();
            }
        }
);
        defaultParentText.addMouseListener(new MouseAdapter() {

            final UIPopupMenu val$popup;
            final UIButton val$arrowButton;
            final ParentPane this$0;

            public void mousePressed(MouseEvent mouseevent)
            {
                Rectangle rectangle = parentCardPane.getBounds();
                popup.setPopupSize(rectangle.width + arrowButton.getSize().width, popup.getPreferredSize().height);
                popup.show(parentCardPane, 0, rectangle.height);
            }

            
            {
                this$0 = ParentPane.this;
                popup = uipopupmenu;
                arrowButton = uibutton;
                super();
            }
        }
);
        arrowButton.addActionListener(new ActionListener() {

            final UIPopupMenu val$popup;
            final UIButton val$arrowButton;
            final ParentPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                Rectangle rectangle = parentCardPane.getBounds();
                popup.setPopupSize(rectangle.width + arrowButton.getSize().width, popup.getPreferredSize().height);
                popup.show(parentCardPane, 0, rectangle.height);
            }

            
            {
                this$0 = ParentPane.this;
                popup = uipopupmenu;
                arrowButton = uibutton;
                super();
            }
        }
);
    }

    protected String title4PopupWindow()
    {
        return "parent";
    }

    public void putElementcase(ElementCasePane elementcasepane)
    {
        ePane = elementcasepane;
    }

    public void putCellElement(TemplateCellElement templatecellelement)
    {
        oldCellElement = templatecellelement;
    }

    private UIPopupMenu createPopMenu()
    {
        UIPopupMenu uipopupmenu = new UIPopupMenu();
        uipopupmenu.setOnlyText(true);
        UIMenuItem uimenuitem = new UIMenuItem(Inter.getLocText("None"));
        if(cellAttrPaneListener != null)
            uimenuitem.addActionListener(cellAttrPaneListener);
        uimenuitem.addActionListener(new ActionListener() {

            final ParentPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                cardLayout.show(parentCardPane, "None");
            }

            
            {
                this$0 = ParentPane.this;
                super();
            }
        }
);
        UIMenuItem uimenuitem1 = new UIMenuItem(Inter.getLocText("Default"));
        if(cellAttrPaneListener != null)
            uimenuitem1.addActionListener(cellAttrPaneListener);
        uimenuitem1.addActionListener(new ActionListener() {

            final ParentPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                cardLayout.show(parentCardPane, "Default");
            }

            
            {
                this$0 = ParentPane.this;
                super();
            }
        }
);
        UIMenuItem uimenuitem2 = new UIMenuItem(Inter.getLocText("Custom"));
        if(cellAttrPaneListener != null)
            uimenuitem2.addActionListener(cellAttrPaneListener);
        uimenuitem2.addActionListener(new ActionListener() {

            final ParentPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                cardLayout.show(parentCardPane, "Custom");
            }

            
            {
                this$0 = ParentPane.this;
                super();
            }
        }
);
        uipopupmenu.add(uimenuitem);
        uipopupmenu.addSeparator();
        uipopupmenu.add(uimenuitem1);
        uipopupmenu.addSeparator();
        uipopupmenu.add(uimenuitem2);
        return uipopupmenu;
    }

    private boolean isLeft()
    {
        return leftOrUp == 0;
    }

    public void populate(CellExpandAttr cellexpandattr)
    {
        if(cellexpandattr == null)
            cellexpandattr = new CellExpandAttr();
        ColumnRow columnrow = isLeft() ? cellexpandattr.getLeftParentColumnRow() : cellexpandattr.getUpParentColumnRow();
        if(isLeft() ? cellexpandattr.isLeftParentDefault() : cellexpandattr.isUpParentDefault())
        {
            cardLayout.show(parentCardPane, "Default");
            customParentColumnRowPane.populate(ColumnRow.valueOf(0, 0));
            if(ColumnRow.validate(columnrow))
                defaultParentText.setText((new StringBuilder()).append(Inter.getLocText("Default")).append(":").append(BaseUtils.convertColumnRowToCellString(columnrow)).toString());
            else
                defaultParentText.setText(Inter.getLocText("Default"));
        } else
        if(ColumnRow.validate(columnrow))
        {
            cardLayout.show(parentCardPane, "Custom");
            customParentColumnRowPane.populate(columnrow);
        } else
        {
            cardLayout.show(parentCardPane, "None");
            customParentColumnRowPane.populate(ColumnRow.valueOf(0, 0));
        }
    }

    public void update(CellExpandAttr cellexpandattr)
    {
        if(cellexpandattr == null)
            cellexpandattr = new CellExpandAttr();
        if(noneParentText.isVisible())
        {
            if(isLeft())
            {
                cellexpandattr.setLeftParentDefault(false);
                cellexpandattr.setLeftParentColumnRow(null);
            } else
            {
                cellexpandattr.setUpParentDefault(false);
                cellexpandattr.setUpParentColumnRow(null);
            }
        } else
        if(defaultParentText.isVisible())
        {
            if(isLeft())
            {
                cellexpandattr.setLeftParentDefault(true);
                cellexpandattr.setLeftParentColumnRow(null);
            } else
            {
                cellexpandattr.setUpParentDefault(true);
                cellexpandattr.setUpParentColumnRow(null);
            }
        } else
        if(customParentColumnRowPane.isVisible())
            if(isLeft())
            {
                cellexpandattr.setLeftParentDefault(false);
                cellexpandattr.setLeftParentColumnRow(customParentColumnRowPane.update());
            } else
            {
                cellexpandattr.setUpParentDefault(false);
                cellexpandattr.setUpParentColumnRow(customParentColumnRowPane.update());
            }
    }





}
