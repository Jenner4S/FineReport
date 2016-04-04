// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.*;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itable.HeaderRenderer;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.form.ui.Label;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WParameterLayout;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import javax.swing.text.Document;

// Referenced classes of package com.fr.design.mainframe:
//            FormDesigner, FormSelection

public class MobileWidgetTable extends JTable
{
    private class UITableTextField extends UITextField
    {

        final MobileWidgetTable this$0;

        protected void paintBorder(Graphics g)
        {
            Graphics2D graphics2d = (Graphics2D)g;
            graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if(isFocusOwner())
                graphics2d.setStroke(new BasicStroke(1.5F));
            else
                graphics2d.setStroke(new BasicStroke(1.0F));
            java.awt.geom.RoundRectangle2D.Double double1 = new java.awt.geom.RoundRectangle2D.Double(0.0D, 0.0D, getWidth() - 2, getHeight() - 2, 4D, 4D);
            graphics2d.setColor(Color.orange);
            graphics2d.draw(double1);
        }

        public UITableTextField()
        {
            this$0 = MobileWidgetTable.this;
            super();
        }

        public UITableTextField(String s)
        {
            this$0 = MobileWidgetTable.this;
            super(s);
        }
    }

    public class BeanTableModel extends DefaultTableModel
    {

        final MobileWidgetTable this$0;

        public int getRowCount()
        {
            return cellData.length;
        }

        public int getColumnCount()
        {
            return 2;
        }

        public Object getValueAt(int i, int j)
        {
            if(i >= getRowCount() || j >= getColumnCount())
                return null;
            String as[] = cellData[i];
            if(j > -1 && j < as.length)
                return cellData[i][j];
            else
                return null;
        }

        public String getColumnName(int i)
        {
            if(i == 0)
                return headers[0];
            else
                return headers[1];
        }

        public void setValueAt(Object obj, int i, int j)
        {
            if(i >= getRowCount() || j >= getColumnCount())
                return;
            if(obj == null)
            {
                cellData[i][j] = null;
                return;
            } else
            {
                cellData[i][j] = obj.toString();
                return;
            }
        }

        public boolean isCellEditable(int i, int j)
        {
            return j != 1;
        }

        public BeanTableModel()
        {
            this$0 = MobileWidgetTable.this;
            super(cellData, headers);
        }
    }

    private class MobileCellEditor extends AbstractCellEditor
        implements TableCellEditor
    {

        UITableTextField uiTableTextField;
        final MobileWidgetTable this$0;

        public void firePropertyChange()
        {
            ((WParameterLayout)designer.getParaComponent().toData()).add2NameTagMap(uiTableTextField.getText(), cellData[getSelectedRow()][1]);
        }

        public Object getCellEditorValue()
        {
            return uiTableTextField.getText();
        }

        public boolean isCellEditable(EventObject eventobject)
        {
            if(eventobject instanceof MouseEvent)
                return ((MouseEvent)eventobject).getClickCount() >= 2;
            else
                return true;
        }

        public Component getTableCellEditorComponent(JTable jtable, Object obj, boolean flag, int i, int j)
        {
            uiTableTextField.setText(obj.toString());
            return uiTableTextField;
        }

        MobileCellEditor()
        {
            this$0 = MobileWidgetTable.this;
            super();
            uiTableTextField = new UITableTextField();
            uiTableTextField.addFocusListener(new FocusAdapter() {

                final MobileWidgetTable val$this$0;
                final MobileCellEditor this$1;

                public void focusLost(FocusEvent focusevent)
                {
                    stopCellEditing();
                    designer.fireTargetModified();
                }


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            uiTableTextField.getDocument().addDocumentListener(new DocumentListener() {

                final MobileWidgetTable val$this$0;
                final MobileCellEditor this$1;

                public void insertUpdate(DocumentEvent documentevent)
                {
                    firePropertyChange();
                }

                public void removeUpdate(DocumentEvent documentevent)
                {
                    firePropertyChange();
                }

                public void changedUpdate(DocumentEvent documentevent)
                {
                    firePropertyChange();
                }


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
        }
    }

    private class MobileWidgetTableCellRenderer extends DefaultTableCellRenderer
    {

        final MobileWidgetTable this$0;

        public Component getTableCellRendererComponent(JTable jtable, Object obj, boolean flag, boolean flag1, int i, int j)
        {
            if(getCursor().getType() == 13 && selectedRow > -1 && selectedRow < getRowCount())
                getInstance().setRowSelectionInterval(selectedRow, selectedRow);
            super.getTableCellRendererComponent(jtable, obj, flag, flag1, i, j);
            if(j == 0)
            {
                Object obj1;
                if(getSelectedColumn() == j && getSelectedRow() == i)
                    obj1 = new UITableTextField(obj.toString());
                else
                    obj1 = new UITextField(obj.toString());
                return ((Component) (obj1));
            } else
            {
                return this;
            }
        }

        private MobileWidgetTableCellRenderer()
        {
            this$0 = MobileWidgetTable.this;
            super();
        }

    }


    private FormDesigner designer;
    protected TableModel defaultmodel;
    private String cellData[][];
    private String headers[] = {
        Inter.getLocText("FR-Utils_Label"), Inter.getLocText("Form-Widget_Name")
    };
    public static final int WIDGET_TABLE_ROW_HEIGHT = 22;
    private UILabel moveComponent;
    private int selectedRow;
    private int GAP;
    private boolean draging;
    private MouseAdapter mouseAdapter;

    public MobileWidgetTable(FormDesigner formdesigner)
    {
        moveComponent = new UILabel();
        selectedRow = -1;
        GAP = 10;
        draging = false;
        mouseAdapter = new MouseAdapter() {

            final MobileWidgetTable this$0;

            public void mouseClicked(MouseEvent mouseevent)
            {
                if(getSelectedRow() != -1 && getSelectedColumn() == 1)
                {
                    String s = cellData[getSelectedRow()][getSelectedColumn()];
                    if(StringUtils.isNotEmpty(s))
                    {
                        int i = getEditingDesigner().getParaComponent().getComponentCount();
                        for(int j = 0; j < i; j++)
                        {
                            XCreator xcreator = (XCreator)getEditingDesigner().getParaComponent().getComponent(j);
                            Widget widget = xcreator.toData();
                            if(!widget.acceptType(new Class[] {
            com/fr/form/ui/Label
        }) && ComparatorUtils.equals(s, widget.getWidgetName()))
                            {
                                getEditingDesigner().getSelectionModel().setSelectedCreator(xcreator);
                                setCursor(Cursor.getPredefinedCursor(13));
                                selectedRow = getSelectedRow();
                            }
                        }

                    }
                }
            }

            public void mouseExited(MouseEvent mouseevent)
            {
                draging = false;
                moveComponent.setVisible(false);
                setCursor(Cursor.getPredefinedCursor(0));
            }

            public void mouseMoved(MouseEvent mouseevent)
            {
                int i = mouseevent.getX() >= getColumnModel().getColumn(0).getWidth() ? 1 : 0;
                int j = -1;
                for(int k = 0; k < getRowCount(); k++)
                    if(mouseevent.getY() > k * 22 && mouseevent.getY() <= (k + 1) * 22)
                        j = k;

                if(j == getSelectedRow() && i == getSelectedColumn() && i != 0)
                    setCursor(Cursor.getPredefinedCursor(13));
                else
                    setCursor(Cursor.getPredefinedCursor(0));
            }

            public void mouseDragged(MouseEvent mouseevent)
            {
                if(mouseevent.getX() < getColumnModel().getColumn(0).getWidth())
                {
                    draging = false;
                    moveComponent.setVisible(false);
                    setCursor(Cursor.getPredefinedCursor(0));
                }
                int i = getColumnModel().getColumn(1).getWidth();
                if(getCursor().getType() == 13)
                {
                    draging = true;
                    getInstance().setRowSelectionInterval(selectedRow, selectedRow);
                    moveComponent.setText(getValueAt(getSelectedRow(), getSelectedColumn()).toString());
                    moveComponent.setLocation(getColumnModel().getColumn(0).getWidth(), mouseevent.getY() - GAP);
                    moveComponent.setPreferredSize(new Dimension(i, 22));
                    moveComponent.setSize(new Dimension(i, 22));
                    moveComponent.setVisible(true);
                    moveComponent.setForeground(Color.lightGray);
                    moveComponent.setBorder(BorderFactory.createLineBorder(Color.lightGray));
                }
            }

            public void mouseReleased(MouseEvent mouseevent)
            {
                if(!draging)
                {
                    return;
                } else
                {
                    draging = false;
                    moveComponent.setVisible(false);
                    int i = mouseevent.getY() >= GAP ? (int)Math.rint((mouseevent.getY() - GAP) / 22) + 1 : 0;
                    ((WParameterLayout)designer.getParaComponent().toData()).adjustOrder(getSelectedRow(), i);
                    getInstance().setRowSelectionInterval(0, getRowCount() - 1);
                    refresh();
                    getInstance().repaint();
                    designer.fireTargetModified();
                    return;
                }
            }

            
            {
                this$0 = MobileWidgetTable.this;
                super();
            }
        }
;
        designer = formdesigner;
        cellData = getData();
        setRowHeight(22);
        JTableHeader jtableheader = getTableHeader();
        jtableheader.setReorderingAllowed(false);
        jtableheader.setPreferredSize(new Dimension(0, 22));
        jtableheader.setDefaultRenderer(new HeaderRenderer());
        setGridColor(new Color(212, 208, 200));
        setSelectionMode(0);
        setColumnSelectionAllowed(false);
        setRowSelectionAllowed(false);
        setFillsViewportHeight(true);
        setDefaultEditor(java/lang/Object, new MobileCellEditor());
        defaultmodel = new BeanTableModel();
        setModel(defaultmodel);
        setAutoResizeMode(4);
        TableColumn tablecolumn = getColumn(getColumnName(0));
        tablecolumn.setPreferredWidth(30);
        repaint();
        setDefaultRenderer(java/lang/Object, new MobileWidgetTableCellRenderer());
        refresh();
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        add(moveComponent);
    }

    public MobileWidgetTable getInstance()
    {
        return this;
    }

    public FormDesigner getEditingDesigner()
    {
        return designer;
    }

    public void refresh()
    {
        XCreator xcreator = designer.getSelectionModel().getSelection().getSelectedCreator();
        cellData = getData();
        if(xcreator != null)
        {
            String s = xcreator.toData().getWidgetName();
            int i = -1;
            byte byte0 = -1;
            int j = 0;
            do
            {
                if(j >= cellData.length)
                    break;
                if(ComparatorUtils.equals(s, cellData[j][0]))
                {
                    i = j;
                    byte0 = 0;
                    break;
                }
                if(ComparatorUtils.equals(s, cellData[j][1]))
                {
                    i = j;
                    byte0 = 1;
                    break;
                }
                j++;
            } while(true);
            selectedRow = i;
            changeSelection(i, byte0, false, false);
            if(i == -1)
                clearSelection();
        }
    }

    private String[][] getData()
    {
        XLayoutContainer xlayoutcontainer = designer.getParaComponent();
        if(xlayoutcontainer == null || !xlayoutcontainer.acceptType(new Class[] {
    com/fr/design/designer/creator/XWParameterLayout
}))
        {
            return new String[0][0];
        } else
        {
            WParameterLayout wparameterlayout = (WParameterLayout)(WParameterLayout)xlayoutcontainer.toData();
            return wparameterlayout.getWidgetNameTag();
        }
    }









}
