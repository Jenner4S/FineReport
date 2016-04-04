// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.javascript.ListenerEditPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.design.widget.EventCreator;
import com.fr.form.event.Listener;
import com.fr.general.Inter;
import com.fr.js.JavaScriptImpl;
import com.fr.report.web.WebContent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

public class EventPane extends BasicPane
{
    public class AddMenuDef extends MenuDef
    {

        private String menuName[];
        final EventPane this$0;

        public void showMenu()
        {
            for(int i = 0; i < menuName.length; i++)
            {
                final int j = i;
                addShortCut(new ShortCut[] {
                    new UpdateAction() {

                        final int val$j;
                        final AddMenuDef this$1;

                        public void actionPerformed(ActionEvent actionevent)
                        {
                            String as[] = WebContent.getDefaultArg(menuName[j]);
                            final ListenerEditPane listenerPane = as != null ? new ListenerEditPane(as) : new ListenerEditPane();
                            Listener listener = new Listener(menuName[j], new JavaScriptImpl());
                            listenerPane.populateBean(listener);
                            BasicDialog basicdialog = listenerPane.showWindow(SwingUtilities.getWindowAncestor(_fld0));
                            basicdialog.addDialogActionListener(new DialogActionAdapter() {

                                final ListenerEditPane val$listenerPane;
                                final _cls1 this$2;

                                public void doOk()
                                {
                                    listModel.addElement(listenerPane.updateBean());
                                    eventList.validate();
                                }

                        
                        {
                            this$2 = _cls1.this;
                            listenerPane = listenereditpane;
                            super();
                        }
                            }
);
                            basicdialog.setVisible(true);
                        }

                
                {
                    this$1 = AddMenuDef.this;
                    j = i;
                    super();
                    setName(EventCreator.switchLang(menuName[j]));
                }
                    }

                });
            }

        }


        public AddMenuDef(String as[])
        {
            this$0 = EventPane.this;
            super();
            setName(Inter.getLocText("Add"));
            setTooltip(Inter.getLocText("Add"));
            setMnemonic('A');
            setIconPath("/com/fr/design/images/control/addPopup.png");
            menuName = as;
            showMenu();
        }
    }

    public class EditAction extends UpdateAction
    {

        final EventPane this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            if(eventList.getSelectedIndex() < 0)
            {
                return;
            } else
            {
                edit();
                return;
            }
        }

        public EditAction()
        {
            this$0 = EventPane.this;
            super();
            setName(Inter.getLocText("Edit"));
            setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/edit.png"));
        }
    }

    public class RemoveAction extends UpdateAction
    {

        final EventPane this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            int i = eventList.getSelectedIndex();
            if(i < 0 || !(listModel.getElementAt(i) instanceof Listener))
                return;
            int j = JOptionPane.showConfirmDialog(EventPane.this, (new StringBuilder()).append(Inter.getLocText("Are_You_Sure_To_Delete_The_Data")).append("?").toString(), "Message", 0);
            if(j != 0)
            {
                return;
            } else
            {
                listModel.removeElementAt(i);
                checkEnableState();
                eventList.validate();
                return;
            }
        }

        public RemoveAction()
        {
            this$0 = EventPane.this;
            super();
            setName(Inter.getLocText("Delete"));
            setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/cell/control/remove.png"));
        }
    }


    private DefaultListModel listModel;
    private JList eventList;
    private AddMenuDef addAction;
    private EditAction editAction;
    private RemoveAction removeAction;
    private String eventName[];
    private int itemHeight;
    public static ListCellRenderer render = new DefaultListCellRenderer() {

        public Component getListCellRendererComponent(JList jlist, Object obj, int i, boolean flag, boolean flag1)
        {
            super.getListCellRendererComponent(jlist, obj, i, flag, flag1);
            if(obj instanceof Listener)
            {
                Listener listener = (Listener)obj;
                setText(EventCreator.switchLang(listener.getEventName()));
            }
            return this;
        }

    }
;
    public MouseListener editListener;

    public EventPane(String as[])
    {
        itemHeight = 20;
        editListener = new MouseAdapter() {

            final EventPane this$0;

            public void mouseReleased(MouseEvent mouseevent)
            {
                if(eventList.getSelectedIndex() < 0)
                    return;
                checkEnableState();
                if(mouseevent.getClickCount() >= 2 && SwingUtilities.isLeftMouseButton(mouseevent))
                    edit();
                if(SwingUtilities.isRightMouseButton(mouseevent))
                {
                    int i = mouseevent.getY();
                    eventList.setSelectedIndex((int)Math.floor(i / itemHeight));
                    JPopupMenu jpopupmenu = new JPopupMenu();
                    jpopupmenu.add(editAction);
                    jpopupmenu.add(removeAction);
                    GUICoreUtils.showPopupMenu(jpopupmenu, eventList, mouseevent.getX() - 1, mouseevent.getY() - 1);
                }
            }

            
            {
                this$0 = EventPane.this;
                super();
            }
        }
;
        initComponents(as);
    }

    private void initComponents(String as[])
    {
        int i = as.length;
        eventName = (String[])Arrays.copyOf(as, i);
        setLayout(FRGUIPaneFactory.createBorderLayout());
        listModel = new DefaultListModel();
        eventList = new JList(listModel);
        eventList.setCellRenderer(render);
        eventList.addMouseListener(editListener);
        add(new UIScrollPane(eventList), "Center");
        addAction = new AddMenuDef(eventName);
        editAction = new EditAction();
        removeAction = new RemoveAction();
        ToolBarDef toolbardef = new ToolBarDef();
        toolbardef.addShortCut(new ShortCut[] {
            addAction
        });
        toolbardef.addShortCut(new ShortCut[] {
            editAction
        });
        toolbardef.addShortCut(new ShortCut[] {
            removeAction
        });
        UIToolbar uitoolbar = ToolBarDef.createJToolBar();
        toolbardef.updateToolBar(uitoolbar);
        uitoolbar.setPreferredSize(new Dimension(uitoolbar.getWidth(), 26));
        add(uitoolbar, "North");
    }

    public void setEnabled(boolean flag)
    {
        super.setEnabled(flag);
        eventList.setEnabled(flag);
        addAction.setEnabled(flag);
        editAction.setEnabled(flag);
        removeAction.setEnabled(flag);
        checkEnableState();
    }

    private void checkEnableState()
    {
        if(listModel.size() == 0 || eventList.getSelectedIndex() < 0)
            setEditEnabled(false);
        else
            setEditEnabled(true);
    }

    private void setEditEnabled(boolean flag)
    {
        editAction.setEnabled(flag);
        removeAction.setEnabled(flag);
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("Event_Set");
    }

    public void populate(java.util.List list)
    {
        listModel.removeAllElements();
        for(int i = 0; i < list.size(); i++)
            listModel.addElement(list.get(i));

        eventList.validate();
    }

    public java.util.List update()
    {
        ArrayList arraylist = new ArrayList();
        for(int i = 0; i < listModel.getSize(); i++)
            arraylist.add((Listener)listModel.get(i));

        return arraylist;
    }

    public void setMenu(String as[])
    {
        eventName = as;
    }

    public void edit()
    {
        final int i = eventList.getSelectedIndex();
        if(!(listModel.getElementAt(i) instanceof Listener))
        {
            return;
        } else
        {
            Listener listener = (Listener)listModel.getElementAt(i);
            final ListenerEditPane jsPane = new ListenerEditPane(WebContent.getDefaultArg(listener.getEventName()));
            jsPane.populateBean(listener);
            BasicDialog basicdialog = jsPane.showWindow(SwingUtilities.getWindowAncestor(this));
            basicdialog.addDialogActionListener(new DialogActionAdapter() {

                final ListenerEditPane val$jsPane;
                final int val$i;
                final EventPane this$0;

                public void doOk()
                {
                    listModel.setElementAt(jsPane.updateBean(), i);
                    eventList.validate();
                }

            
            {
                this$0 = EventPane.this;
                jsPane = listenereditpane;
                i = j;
                super();
            }
            }
);
            basicdialog.setVisible(true);
            return;
        }
    }







}
