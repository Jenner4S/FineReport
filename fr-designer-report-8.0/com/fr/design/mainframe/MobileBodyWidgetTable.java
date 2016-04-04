// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itable.HeaderRenderer;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WFitLayout;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.*;

// Referenced classes of package com.fr.design.mainframe:
//            FormDesigner, FormSelection

public class MobileBodyWidgetTable extends JTable
{
    public class BeanTableModel extends DefaultTableModel
    {

        final MobileBodyWidgetTable this$0;

        public int getRowCount()
        {
            return cellData.length;
        }

        public int getColumnCount()
        {
            return 1;
        }

        public Object getValueAt(int i, int j)
        {
            if(i >= getRowCount() || j >= getColumnCount())
                return null;
            else
                return cellData[i][0];
        }

        public String getColumnName(int i)
        {
            return headers[0];
        }

        public void setValueAt(Object obj, int i, int j)
        {
            if(i >= getRowCount() || j >= getColumnCount())
                return;
            if(obj == null)
            {
                cellData[i] = null;
                return;
            } else
            {
                cellData[i][0] = obj.toString();
                return;
            }
        }

        public boolean isCellEditable(int i, int j)
        {
            return false;
        }

        public BeanTableModel()
        {
            this$0 = MobileBodyWidgetTable.this;
            super(cellData, headers);
        }
    }

    private class MobileWidgetTableCellRenderer extends DefaultTableCellRenderer
    {

        final MobileBodyWidgetTable this$0;

        public Component getTableCellRendererComponent(JTable jtable, Object obj, boolean flag, boolean flag1, int i, int j)
        {
            if(getCursor().getType() == 13 && selectedRow > -1 && selectedRow < getRowCount())
                getInstance().setRowSelectionInterval(selectedRow, selectedRow);
            super.getTableCellRendererComponent(jtable, obj, flag, flag1, i, j);
            return this;
        }

        private MobileWidgetTableCellRenderer()
        {
            this$0 = MobileBodyWidgetTable.this;
            super();
        }

    }


    private FormDesigner designer;
    protected TableModel defaultmodel;
    private String cellData[][];
    private String headers[] = {
        Inter.getLocText("Form-Widget_Name")
    };
    public static final int WIDGET_TABLE_ROW_HEIGHT = 22;
    private UILabel moveComponent;
    private int selectedRow;
    private int GAP;
    private boolean draging;
    private MouseAdapter mouseAdapter;

    public MobileBodyWidgetTable(FormDesigner formdesigner)
    {
        moveComponent = new UILabel();
        selectedRow = -1;
        GAP = 10;
        draging = false;
        mouseAdapter = new MouseAdapter() {

            final MobileBodyWidgetTable this$0;

            public void mouseClicked(MouseEvent mouseevent)
            {
                if(getSelectedRow() != -1)
                {
                    String s = cellData[getSelectedRow()][0];
                    if(StringUtils.isNotEmpty(s))
                    {
                        XLayoutContainer xlayoutcontainer = getEditingDesigner().getRootComponent();
                        int i = xlayoutcontainer.getXCreatorCount();
                        for(int j = 0; j < i; j++)
                        {
                            XCreator xcreator = xlayoutcontainer.getXCreator(j).getEditingChildCreator();
                            Widget widget = xcreator.toData();
                            if(ComparatorUtils.equals(s, widget.getWidgetName()))
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
                int i = 0;
                for(int j = 0; j < getRowCount(); j++)
                    if(mouseevent.getY() > j * 22 && mouseevent.getY() <= (j + 1) * 22)
                        i = j;

                if(i == getSelectedRow())
                    setCursor(Cursor.getPredefinedCursor(13));
                else
                    setCursor(Cursor.getPredefinedCursor(0));
            }

            public void mouseDragged(MouseEvent mouseevent)
            {
                int i = getColumnModel().getColumn(0).getWidth();
                if(getCursor().getType() == 13)
                {
                    draging = true;
                    getInstance().setRowSelectionInterval(selectedRow, selectedRow);
                    moveComponent.setText(getValueAt(getSelectedRow(), getSelectedColumn()).toString());
                    moveComponent.setLocation(0, mouseevent.getY() - GAP);
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
                    ((WFitLayout)designer.getRootComponent().toData()).adjustOrder(getSelectedRow(), i);
                    getInstance().setRowSelectionInterval(0, getRowCount() - 1);
                    refresh();
                    getInstance().repaint();
                    designer.fireTargetModified();
                    return;
                }
            }

            
            {
                this$0 = MobileBodyWidgetTable.this;
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
        HeaderRenderer headerrenderer = new HeaderRenderer();
        headerrenderer.setHorizontalAlignment(0);
        jtableheader.setDefaultRenderer(headerrenderer);
        setGridColor(new Color(212, 208, 200));
        setSelectionMode(0);
        setColumnSelectionAllowed(false);
        setRowSelectionAllowed(false);
        setFillsViewportHeight(true);
        defaultmodel = new BeanTableModel();
        setModel(defaultmodel);
        setAutoResizeMode(4);
        repaint();
        setDefaultRenderer(java/lang/Object, new MobileWidgetTableCellRenderer());
        refresh();
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        add(moveComponent);
    }

    public MobileBodyWidgetTable getInstance()
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
            int j = 0;
            do
            {
                if(j >= cellData.length)
                    break;
                if(ComparatorUtils.equals(s, cellData[j][0]))
                {
                    i = j;
                    break;
                }
                j++;
            } while(true);
            selectedRow = i;
            changeSelection(i, 0, false, false);
            if(i == -1)
                clearSelection();
        }
    }

    private String[][] getData()
    {
        if(designer.isFormParaDesigner())
            return new String[0][0];
        XLayoutContainer xlayoutcontainer = designer.getRootComponent();
        if(xlayoutcontainer == null || !xlayoutcontainer.acceptType(new Class[] {
    com/fr/form/ui/container/WFitLayout
}))
            return new String[0][0];
        WFitLayout wfitlayout = (WFitLayout)(WFitLayout)xlayoutcontainer.toData();
        ArrayList arraylist = wfitlayout.getMobileWidgetList();
        String as[][] = new String[arraylist.size()][2];
        for(int i = 0; i < arraylist.size(); i++)
            as[i][0] = (String)arraylist.get(i);

        return as;
    }









}
