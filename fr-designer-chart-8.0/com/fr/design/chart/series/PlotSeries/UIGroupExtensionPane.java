// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.PlotSeries;

import com.fr.chart.base.MapSvgXMLHelper;
import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.dialog.UIDialog;
import com.fr.design.event.ChangeEvent;
import com.fr.design.event.ChangeListener;
import com.fr.design.gui.frpane.UIExtensionPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UISearchTextField;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.GeneralUtils;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;

public class UIGroupExtensionPane extends BasicPane
{

    public static final String EDIT = "edit";
    public static final String DELETE = "delete";
    private static final int BUTTONWIDTH = 16;
    private static final int DIALOG_WIDTH = 140;
    private static final int DIALOG_HEIGHT = 100;
    private UISearchTextField searchTextFiled;
    private JList contentViews[];
    private Component components[][];
    private String titles[];
    private java.util.List selectionListeners;
    private java.util.List editListeners;
    private java.util.List deleteListeners;
    private boolean isPressOnDelete;
    private DefaultListCellRenderer listCellRenderer;
    private ListSelectionListener listSelectionListener;
    private MouseListener mouseListener;

    protected String title4PopupWindow()
    {
        return "group";
    }

    public UIGroupExtensionPane(String as[])
    {
        selectionListeners = new ArrayList();
        editListeners = new ArrayList();
        deleteListeners = new ArrayList();
        isPressOnDelete = false;
        listCellRenderer = new DefaultListCellRenderer() {

            final UIGroupExtensionPane this$0;

            public Component getListCellRendererComponent(JList jlist, Object obj, int i, boolean flag, boolean flag1)
            {
                JComponent jcomponent = (JComponent)super.getListCellRendererComponent(jlist, obj, i, flag, flag1);
                if(!hasEditOperation(jlist))
                    return jcomponent;
                Border border = null;
                jcomponent.setBorder(border);
                UILabel uilabel = new UILabel(UIConstants.EDIT_ICON);
                UILabel uilabel1 = new UILabel(UIConstants.DELETE_ICON);
                JPanel jpanel = GUICoreUtils.createFlowPane(new Component[] {
                    uilabel, uilabel1
                }, 0, 4);
                jpanel.setBackground(flag ? jcomponent.getBackground() : null);
                jpanel.setBorder(border);
                JPanel jpanel1 = GUICoreUtils.createBorderLayoutPane(new Object[] {
                    jcomponent, "Center", jpanel, "East"
                });
                if(shouldFilter(obj))
                    jpanel1.setPreferredSize(new Dimension(0, 0));
                return jpanel1;
            }

            
            {
                this$0 = UIGroupExtensionPane.this;
                super();
            }
        }
;
        listSelectionListener = new ListSelectionListener() {

            final UIGroupExtensionPane this$0;

            public void valueChanged(ListSelectionEvent listselectionevent)
            {
                if(listselectionevent.getValueIsAdjusting())
                    isPressOnDelete = false;
                if(!isRespondToValueChange(listselectionevent))
                    return;
                fireSelectionChangeListener(new ChangeEvent(listselectionevent.getSource()));
                if(((JList)listselectionevent.getSource()).getSelectedIndex() == -1)
                    return;
                JList ajlist[] = contentViews;
                int i = ajlist.length;
                for(int j = 0; j < i; j++)
                {
                    JList jlist = ajlist[j];
                    if(jlist.getSelectedIndex() == -1 || ComparatorUtils.equals(jlist, listselectionevent.getSource()))
                        continue;
                    try
                    {
                        jlist.setSelectedIndices(null);
                    }
                    catch(Exception exception) { }
                }

            }

            
            {
                this$0 = UIGroupExtensionPane.this;
                super();
            }
        }
;
        mouseListener = new MouseAdapter() {

            final UIGroupExtensionPane this$0;

            public void mousePressed(final MouseEvent e)
            {
                isPressOnDelete = false;
                final JList list = (JList)e.getSource();
                Point point = e.getPoint();
                final int index = list.locationToIndex(point);
                int i = list.getWidth();
                if(hasEditOperation(list))
                    if(point.x > i - 20)
                    {
                        BasicPane basicpane = new BasicPane() {

                            final _cls5 this$1;

                            protected String title4PopupWindow()
                            {
                                return "";
                            }

                    
                    {
                        this$1 = _cls5.this;
                        super();
                    }
                        }
;
                        isPressOnDelete = true;
                        basicpane.setLayout(new BorderLayout());
                        basicpane.add(new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText(new String[] {
                            "Delete", "Chart-Map"
                        })).append("?").toString(), 0));
                        clearLastListSelection(list);
                        final String selectedType = getSelectedType();
                        UIDialog uidialog = basicpane.showUnsizedWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

                            final JList val$list;
                            final int val$index;
                            final String val$selectedType;
                            final MouseEvent val$e;
                            final _cls5 this$1;

                            public void doOk()
                            {
                                Object obj = getSelectedObject();
                                ((DefaultListModel)list.getModel()).removeElementAt(index);
                                MapSvgXMLHelper.getInstance().removeMapAttr(GeneralUtils.objectToString(obj));
                                MapSvgXMLHelper.getInstance().removeCateNames(selectedType, GeneralUtils.objectToString(obj));
                                fireDeleteListener(new ChangeEvent(e));
                            }

                    
                    {
                        this$1 = _cls5.this;
                        list = jlist;
                        index = i;
                        selectedType = s;
                        e = mouseevent;
                        super();
                    }
                        }
);
                        uidialog.setResizable(true);
                        uidialog.setSize(140, 100);
                        uidialog.setResizable(false);
                        GUICoreUtils.centerWindow(uidialog);
                        uidialog.setVisible(true);
                    } else
                    if(point.x > i - 40 && point.x < i - 20)
                        fireItemEditListener(new ChangeEvent(e));
            }

            public void mouseReleased(MouseEvent mouseevent)
            {
                isPressOnDelete = false;
            }

            
            {
                this$0 = UIGroupExtensionPane.this;
                super();
            }
        }
;
        titles = as;
        if(ArrayUtils.isEmpty(as))
        {
            return;
        } else
        {
            initComponents(as);
            return;
        }
    }

    private void initComponents(String as[])
    {
        setBackground(null);
        searchTextFiled = initSearchTextField();
        int i = as.length;
        contentViews = new JList[i];
        components = new Component[i + 1][];
        double d = -2D;
        double d1 = -1D;
        double ad[] = new double[i + 1];
        double ad1[] = {
            d1
        };
        for(int j = 0; j < i + 1; j++)
        {
            ad[j] = d;
            if(j == 0)
            {
                components[j] = (new Component[] {
                    searchTextFiled
                });
                continue;
            }
            if(j > 0 && j < i + 1)
            {
                JList jlist = new JList(new DefaultListModel());
                jlist.addListSelectionListener(listSelectionListener);
                jlist.addMouseListener(mouseListener);
                jlist.setCellRenderer(listCellRenderer);
                jlist.setBackground(null);
                contentViews[j - 1] = jlist;
                components[j] = (new UIExtensionPane[] {
                    new UIExtensionPane(as[j - 1], jlist, false)
                });
            }
        }

        JPanel jpanel = TableLayoutHelper.createGapTableLayoutPane(components, ad, ad1, 0.0D, 0.0D);
        setLayout(new BorderLayout());
        add(new UIScrollPane(jpanel), "Center");
    }

    private UISearchTextField initSearchTextField()
    {
        UISearchTextField uisearchtextfield = new UISearchTextField() {

            final UIGroupExtensionPane this$0;

            public Dimension getPreferredSize()
            {
                return new Dimension(120, 22);
            }

            
            {
                this$0 = UIGroupExtensionPane.this;
                super();
            }
        }
;
        uisearchtextfield.setIconPosition(4);
        uisearchtextfield.getDocument().addDocumentListener(new DocumentListener() {

            final UIGroupExtensionPane this$0;

            public void insertUpdate(DocumentEvent documentevent)
            {
                doFilter();
            }

            public void removeUpdate(DocumentEvent documentevent)
            {
                doFilter();
            }

            public void changedUpdate(DocumentEvent documentevent)
            {
                doFilter();
            }

            
            {
                this$0 = UIGroupExtensionPane.this;
                super();
            }
        }
);
        return uisearchtextfield;
    }

    public Object[] getData(int i)
    {
        if(i < 0 || i > contentViews.length)
            return ArrayUtils.EMPTY_OBJECT_ARRAY;
        ListModel listmodel = contentViews[i].getModel();
        Object aobj[] = new Object[listmodel.getSize()];
        int j = 0;
        for(int k = listmodel.getSize(); j < k; j++)
            aobj[j] = listmodel.getElementAt(j);

        return aobj;
    }

    public Object[] getData(String s)
    {
        int i = ArrayUtils.indexOf(titles, s);
        if(i != -1)
            return getData(i);
        else
            return ArrayUtils.EMPTY_OBJECT_ARRAY;
    }

    private void doFilter()
    {
        JList ajlist[] = contentViews;
        int j = ajlist.length;
        for(int l = 0; l < j; l++)
        {
            JList jlist = ajlist[l];
            ListDataListener alistdatalistener[] = ((DefaultListModel)jlist.getModel()).getListDataListeners();
            ListDataListener alistdatalistener1[] = alistdatalistener;
            int i1 = alistdatalistener1.length;
            for(int j1 = 0; j1 < i1; j1++)
            {
                ListDataListener listdatalistener = alistdatalistener1[j1];
                listdatalistener.contentsChanged(new ListDataEvent(listdatalistener, 0, 0, jlist.getModel().getSize()));
            }

        }

        int i = 1;
        for(int k = components.length; i < k; i++)
            ((UIExtensionPane)components[i][0]).setExpand(true);

    }

    public Object getSelectedObject()
    {
        JList ajlist[] = contentViews;
        int i = ajlist.length;
        for(int j = 0; j < i; j++)
        {
            JList jlist = ajlist[j];
            if(jlist.getSelectedValue() != null)
                return jlist.getSelectedValue();
        }

        return null;
    }

    public String getSelectedType()
    {
        int i = 0;
        for(int j = contentViews.length; i < j; i++)
            if(contentViews[i].getSelectedValue() != null)
                return titles[i];

        return "";
    }

    public void setSelectedObject(Object obj)
    {
        int i = 0;
        for(int j = contentViews.length; i < j; i++)
        {
            UIExtensionPane uiextensionpane = (UIExtensionPane)components[i + 1][0];
            JList jlist = contentViews[i];
            DefaultListModel defaultlistmodel = (DefaultListModel)jlist.getModel();
            uiextensionpane.setExpand(defaultlistmodel.contains(obj));
            if(defaultlistmodel.contains(obj))
                jlist.setSelectedValue(obj, true);
        }

    }

    public void setValueAtCurrentSelectIndex(Object obj)
    {
        JList ajlist[] = contentViews;
        int i = ajlist.length;
        for(int j = 0; j < i; j++)
        {
            JList jlist = ajlist[j];
            if(jlist.getSelectedIndex() != -1)
                ((DefaultListModel)jlist.getModel()).setElementAt(obj, jlist.getSelectedIndex());
        }

    }

    public void addData(Object obj, int i)
    {
        addData(obj, i, false);
    }

    public void addData(Object obj, int i, boolean flag)
    {
        if(contentViews == null || i < 0 || i > contentViews.length - 1)
            return;
        JList jlist = contentViews[i];
        DefaultListModel defaultlistmodel = (DefaultListModel)jlist.getModel();
        if(obj instanceof String)
            defaultlistmodel.addElement(createUnrepeatedName(defaultlistmodel, (String)obj, flag));
        if(flag)
        {
            int j = 1;
            for(int k = components.length; j < k; j++)
                ((UIExtensionPane)components[j][0]).setExpand(false);

            ((UIExtensionPane)components[i + 1][0]).setExpand(true);
            j = jlist.getModel().getSize() - 1;
            jlist.setSelectedIndex(j);
            dealNewAddedDataIndex(((DefaultListModel)jlist.getModel()).elementAt(j));
        }
    }

    protected void dealNewAddedDataIndex(Object obj)
    {
    }

    public void addData(Object obj, String s)
    {
        addData(obj, s, false);
    }

    public void addData(Object obj, String s, boolean flag)
    {
        int i = ArrayUtils.indexOf(titles, s);
        if(i != -1)
            addData(obj, i, flag);
    }

    public void clearData()
    {
        JList ajlist[] = contentViews;
        int i = ajlist.length;
        for(int j = 0; j < i; j++)
        {
            JList jlist = ajlist[j];
            ((DefaultListModel)jlist.getModel()).clear();
        }

    }

    private String createUnrepeatedName(DefaultListModel defaultlistmodel, String s, boolean flag)
    {
        if(!flag)
            return s;
        int i = defaultlistmodel.getSize();
        int j = 1;
        String s1 = (new StringBuilder()).append(s).append(i + j).toString();
        boolean flag1 = false;
        do
        {
            flag1 = false;
            String s2 = (new StringBuilder()).append(s).append(i + j).toString();
            for(int k = 0; k < i; k++)
                if(ComparatorUtils.equals(defaultlistmodel.getElementAt(k), s2))
                {
                    flag1 = true;
                    j++;
                }

        } while(flag1);
        return (new StringBuilder()).append(s).append(i + j).toString();
    }

    private boolean hasEditOperation(JList jlist)
    {
        return true;
    }

    protected boolean isRespondToValueChange(ListSelectionEvent listselectionevent)
    {
        return true;
    }

    private boolean shouldFilter(Object obj)
    {
        return !GeneralUtils.objectToString(obj).toLowerCase().contains(searchTextFiled.getText().toLowerCase());
    }

    public void clearLastListSelection(JList jlist)
    {
        JList ajlist[] = contentViews;
        int i = ajlist.length;
        for(int j = 0; j < i; j++)
        {
            JList jlist1 = ajlist[j];
            if(jlist1.getSelectedIndex() == -1 || ComparatorUtils.equals(jlist1, jlist))
                continue;
            try
            {
                jlist1.setSelectedIndices(null);
            }
            catch(Exception exception)
            {
                FRLogger.getLogger().error(exception.getMessage());
            }
        }

    }

    public boolean isPressOnDelete()
    {
        return isPressOnDelete;
    }

    public void addSelectionChangeListener(ChangeListener changelistener)
    {
        selectionListeners.add(changelistener);
    }

    private void fireSelectionChangeListener(ChangeEvent changeevent)
    {
        for(int i = selectionListeners.size(); i > 0; i--)
            ((ChangeListener)selectionListeners.get(i - 1)).fireChanged(changeevent);

    }

    public void addItemEditListener(ChangeListener changelistener)
    {
        editListeners.add(changelistener);
    }

    private void fireItemEditListener(ChangeEvent changeevent)
    {
        for(int i = editListeners.size(); i > 0; i--)
            ((ChangeListener)editListeners.get(i - 1)).fireChanged(changeevent);

    }

    public void addDeleteListener(ChangeListener changelistener)
    {
        deleteListeners.add(changelistener);
    }

    private void fireDeleteListener(ChangeEvent changeevent)
    {
        for(int i = deleteListeners.size(); i > 0; i--)
            ((ChangeListener)deleteListeners.get(i - 1)).fireChanged(changeevent);

    }

    public void setEnabled(boolean flag)
    {
        super.setEnabled(flag);
        if(searchTextFiled != null)
            searchTextFiled.setEnabled(flag);
        if(components != null)
        {
            for(int i = 0; i < components.length; i++)
            {
                Component acomponent[] = components[i];
                for(int j = 0; j < acomponent.length; j++)
                    acomponent[j].setEnabled(flag);

            }

        }
    }

    public static void main(String args[])
    {
        JFrame jframe = new JFrame();
        jframe.setDefaultCloseOperation(3);
        Container container = jframe.getContentPane();
        container.setBackground(Color.WHITE);
        container.setLayout(new BoxLayout(container, 1));
        UIGroupExtensionPane uigroupextensionpane = new UIGroupExtensionPane(new String[] {
            "title1", "title2", "title3"
        });
        container.add(uigroupextensionpane, "Center");
        JPanel jpanel = new JPanel(new FlowLayout());
        container.add(jpanel, "South");
        UIButton uibutton = new UIButton("add1");
        uibutton.addActionListener(new ActionListener(uigroupextensionpane) {

            final UIGroupExtensionPane val$g;

            public void actionPerformed(ActionEvent actionevent)
            {
                g.addData("test111", 0);
            }

            
            {
                g = uigroupextensionpane;
                super();
            }
        }
);
        jpanel.add(uibutton);
        UIButton uibutton1 = new UIButton("add2");
        uibutton1.addActionListener(new ActionListener(uigroupextensionpane) {

            final UIGroupExtensionPane val$g;

            public void actionPerformed(ActionEvent actionevent)
            {
                g.addData("test222", 1);
            }

            
            {
                g = uigroupextensionpane;
                super();
            }
        }
);
        jpanel.add(uibutton1);
        jframe.setSize(360, 500);
        jframe.setLocation(200, 100);
        jframe.setVisible(true);
    }








}
