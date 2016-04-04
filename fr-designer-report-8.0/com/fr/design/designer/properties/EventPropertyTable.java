// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.actions.UpdateAction;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.dialog.*;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.form.javascript.FormEmailPane;
import com.fr.design.gui.frpane.ListenerUpdatePane;
import com.fr.design.gui.ilist.JNameEdList;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.javascript.EmailPane;
import com.fr.design.javascript.JavaScriptActionPane;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormSelection;
import com.fr.design.menu.*;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.design.write.submit.DBManipulationPane;
import com.fr.form.event.Listener;
import com.fr.form.ui.Widget;
import com.fr.general.*;
import com.fr.js.JavaScriptImpl;
import com.fr.stable.Nameable;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

public class EventPropertyTable extends BasicPane
{
    public static class DnDListItem
        implements Transferable
    {

        private NameObject no;
        public static final DataFlavor FLAVOR = new DataFlavor(com/fr/design/designer/properties/EventPropertyTable$DnDListItem, "DnDListItem.class");

        public DataFlavor[] getTransferDataFlavors()
        {
            return (new DataFlavor[] {
                FLAVOR
            });
        }

        public boolean isDataFlavorSupported(DataFlavor dataflavor)
        {
            return ComparatorUtils.equals(dataflavor, FLAVOR);
        }

        public Object getTransferData(DataFlavor dataflavor)
            throws UnsupportedFlavorException, IOException
        {
            if(ComparatorUtils.equals(dataflavor, FLAVOR))
                return no;
            else
                return null;
        }


        public DnDListItem(NameObject nameobject)
        {
            no = nameobject;
        }
    }

    private class DnDTransferHandler extends TransferHandler
    {

        private int action;
        final EventPropertyTable this$0;

        public int getSourceActions(JComponent jcomponent)
        {
            return action;
        }

        public Transferable createTransferable(JComponent jcomponent)
        {
            int i = nameableList.getSelectedIndex();
            if(i < 0 || i >= nameableList.getModel().getSize())
                return null;
            else
                return new DnDListItem((NameObject)nameableList.getSelectedValue());
        }

        public boolean canImport(javax.swing.TransferHandler.TransferSupport transfersupport)
        {
            if(!transfersupport.isDrop())
                return false;
            if(!transfersupport.isDataFlavorSupported(DnDListItem.FLAVOR))
                return false;
            boolean flag = (action & transfersupport.getSourceDropActions()) == action;
            if(flag)
            {
                transfersupport.setDropAction(action);
                return true;
            } else
            {
                return false;
            }
        }

        public boolean importData(javax.swing.TransferHandler.TransferSupport transfersupport)
        {
            if(!canImport(transfersupport))
                return false;
            javax.swing.JList.DropLocation droplocation = (javax.swing.JList.DropLocation)transfersupport.getDropLocation();
            int i = droplocation.getIndex();
            try
            {
                NameObject nameobject = (NameObject)transfersupport.getTransferable().getTransferData(DnDListItem.FLAVOR);
                JList jlist = (JList)transfersupport.getComponent();
                DefaultListModel defaultlistmodel = (DefaultListModel)jlist.getModel();
                int j = 0;
                do
                {
                    if(j > i)
                        break;
                    if(ComparatorUtils.equals(nameobject, defaultlistmodel.getElementAt(j)))
                    {
                        i--;
                        break;
                    }
                    j++;
                } while(true);
                defaultlistmodel.removeElement(nameobject);
                defaultlistmodel.insertElementAt(nameobject, i);
                updateWidgetListener(creator);
            }
            catch(UnsupportedFlavorException unsupportedflavorexception)
            {
                return false;
            }
            catch(IOException ioexception)
            {
                return false;
            }
            return true;
        }

        public DnDTransferHandler()
        {
            this(2);
        }

        public DnDTransferHandler(int i)
        {
            this$0 = EventPropertyTable.this;
            super();
            action = i;
        }
    }

    private class CopyItemAction extends UpdateAction
    {

        final EventPropertyTable this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            NameObject nameobject = (NameObject)nameableList.getSelectedValue();
            try
            {
                NameObject nameobject1 = (NameObject)BaseUtils.cloneObject(nameobject);
                nameobject1.setName((new StringBuilder()).append("CopyOf").append(nameobject.getName()).toString());
                addNameObject(nameobject1, nameableList.getSelectedIndex() + 1);
            }
            catch(Exception exception)
            {
                FRContext.getLogger().error(exception.getMessage(), exception);
            }
            updateWidgetListener(creator);
        }

        public CopyItemAction()
        {
            this$0 = EventPropertyTable.this;
            super();
            setName(Inter.getLocText("Copy"));
            setMnemonic('C');
            setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/cell/control/copy.png"));
        }
    }

    private class RemoveItemAction extends UpdateAction
    {

        final EventPropertyTable this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            GUICoreUtils.removeJListSelectedNodes(SwingUtilities.getWindowAncestor(EventPropertyTable.this), nameableList);
            updateWidgetListener(creator);
        }

        public RemoveItemAction()
        {
            this$0 = EventPropertyTable.this;
            super();
            setName(Inter.getLocText("Remove"));
            setMnemonic('R');
            setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/cell/control/remove.png"));
        }
    }

    protected class EditItemMenuDef extends UpdateAction
    {

        final EventPropertyTable this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            if(nameableList.getSelectedValue() instanceof NameObject)
                showEventPane((NameObject)nameableList.getSelectedValue());
        }

        public EditItemMenuDef()
        {
            this$0 = EventPropertyTable.this;
            super();
            setName(Inter.getLocText("Edit"));
            setMnemonic('E');
            setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/edit.png"));
        }
    }

    protected class AddItemMenuDef extends MenuDef
    {

        final EventPropertyTable this$0;

        public void populate(String as[])
        {
            clearShortCuts();
            for(int i = 0; i < as.length; i++)
            {
                final String eventname = as[i];
                addShortCut(new ShortCut[] {
                    new UpdateAction() {

                        final String val$eventname;
                        final AddItemMenuDef this$1;

                        public void actionPerformed(ActionEvent actionevent)
                        {
                            NameObject nameobject = new NameObject(createUnrepeatedName(switchLang(eventname)), new Listener(eventname, new JavaScriptImpl()));
                            addNameObject(nameobject, nameableList.getModel().getSize());
                            updateWidgetListener(creator);
                        }

                
                {
                    this$1 = AddItemMenuDef.this;
                    eventname = s;
                    super();
                    setName(switchLang(eventname));
                }
                    }

                });
            }

        }

        public AddItemMenuDef()
        {
            this$0 = EventPropertyTable.this;
            super();
            setName(Inter.getLocText("Add"));
            setMnemonic('A');
            setIconPath("/com/fr/design/images/control/addPopup.png");
        }
    }

    private class NameableListCellRenderer extends DefaultListCellRenderer
    {

        final EventPropertyTable this$0;

        public Component getListCellRendererComponent(JList jlist, Object obj, int i, boolean flag, boolean flag1)
        {
            super.getListCellRendererComponent(jlist, obj, i, flag, flag1);
            if(obj instanceof NameObject)
            {
                NameObject nameobject = (NameObject)obj;
                setText(nameobject.getName());
            }
            return this;
        }

        private NameableListCellRenderer()
        {
            this$0 = EventPropertyTable.this;
            super();
        }

    }


    private ShortCut shorts[];
    private XCreator creator;
    private JNameEdList nameableList;
    private ToolBarDef toolbarDef;
    private AddItemMenuDef itemMenu;
    private ShortCut editItemAction;
    private ShortCut copyItemAction;
    private ShortCut removeItemAction;
    private UIToolbar toolbar;
    private ListenerUpdatePane listenerPane;
    private FormDesigner designer;
    private MouseListener listMouseListener;

    public EventPropertyTable(FormDesigner formdesigner)
    {
        listMouseListener = new MouseAdapter() {

            final EventPropertyTable this$0;

            public void mouseReleased(MouseEvent mouseevent)
            {
                checkButtonEnabled();
                if(mouseevent.getClickCount() >= 2 && SwingUtilities.isLeftMouseButton(mouseevent))
                {
                    NameObject nameobject = (NameObject)nameableList.getSelectedValue();
                    showEventPane(nameobject);
                }
            }

            
            {
                this$0 = EventPropertyTable.this;
                super();
            }
        }
;
        designer = formdesigner;
        initComponents();
    }

    protected void initComponents()
    {
        setLayout(new BorderLayout());
        nameableList = new JNameEdList(new DefaultListModel());
        add(new JScrollPane(nameableList), "Center");
        nameableList.setCellRenderer(new NameableListCellRenderer());
        nameableList.setSelectionMode(1);
        nameableList.addMouseListener(listMouseListener);
        nameableList.setTransferHandler(new DnDTransferHandler());
        nameableList.setDropMode(DropMode.INSERT);
        nameableList.setDragEnabled(true);
        toolbarDef = new ToolBarDef();
        shorts = (new ShortCut[] {
            itemMenu = new AddItemMenuDef(), editItemAction = new EditItemMenuDef(), copyItemAction = new CopyItemAction(), removeItemAction = new RemoveItemAction()
        });
        ShortCut ashortcut[] = shorts;
        int i = ashortcut.length;
        for(int j = 0; j < i; j++)
        {
            ShortCut shortcut = ashortcut[j];
            toolbarDef.addShortCut(new ShortCut[] {
                shortcut
            });
        }

        toolbar = ToolBarDef.createJToolBar();
        toolbarDef.updateToolBar(toolbar);
        add(toolbar, "North");
    }

    public void addNameObject(NameObject nameobject, int i)
    {
        DefaultListModel defaultlistmodel = (DefaultListModel)nameableList.getModel();
        defaultlistmodel.add(i, nameobject);
        nameableList.setSelectedIndex(i);
        nameableList.ensureIndexIsVisible(i);
        nameableList.repaint();
    }

    public void refreshNameableCreator(String as[])
    {
        itemMenu.populate(as);
        toolbarDef.updateToolBar(toolbar);
        toolbar.validate();
        toolbar.repaint();
        repaint();
    }

    private void checkButtonEnabled()
    {
        boolean flag = nameableList.getSelectedValue() instanceof NameObject;
        itemMenu.setEnabled(creator != null && itemMenu.getShortCutCount() > 0);
        editItemAction.setEnabled(flag);
        copyItemAction.setEnabled(flag);
        removeItemAction.setEnabled(flag);
    }

    private void showEventPane(final NameObject object)
    {
        if(listenerPane == null)
            listenerPane = new ListenerUpdatePane() {

                final EventPropertyTable this$0;

                protected JavaScriptActionPane createJavaScriptActionPane()
                {
                    return new JavaScriptActionPane() {

                        final _cls2 this$1;

                        protected DBManipulationPane createDBManipulationPane()
                        {
                            return new DBManipulationPane(ValueEditorPaneFactory.formEditors());
                        }

                        protected String title4PopupWindow()
                        {
                            return Inter.getLocText("Set_Callback_Function");
                        }

                        protected EmailPane initEmaiPane()
                        {
                            return new FormEmailPane();
                        }

                        public boolean isForm()
                        {
                            return true;
                        }

                        protected String[] getDefaultArgs()
                        {
                            return new String[0];
                        }

                    
                    {
                        this$1 = _cls2.this;
                        super();
                    }
                    }
;
                }

                protected boolean supportCellAction()
                {
                    return false;
                }

            
            {
                this$0 = EventPropertyTable.this;
                super();
            }
            }
;
        listenerPane.populateBean((Listener)object.getObject());
        BasicDialog basicdialog = listenerPane.showWindow(SwingUtilities.getWindowAncestor(this));
        basicdialog.addDialogActionListener(new DialogActionAdapter() {

            final NameObject val$object;
            final EventPropertyTable this$0;

            public void doOk()
            {
                object.setObject(listenerPane.updateBean());
                updateWidgetListener(creator);
            }

            
            {
                this$0 = EventPropertyTable.this;
                object = nameobject;
                super();
            }
        }
);
        basicdialog.setVisible(true);
    }

    private String switchLang(String s)
    {
        return Inter.getLocText((new StringBuilder()).append("Event-").append(s).toString());
    }

    public void refresh()
    {
        int i = designer.getSelectionModel().getSelection().size();
        if(i == 0 || i == 1)
        {
            creator = ((XCreator) (i != 0 ? designer.getSelectionModel().getSelection().getSelectedCreator() : ((XCreator) (designer.getRootComponent()))));
        } else
        {
            creator = null;
            ((DefaultListModel)nameableList.getModel()).removeAllElements();
            checkButtonEnabled();
            return;
        }
        Widget widget = creator.toData();
        refreshNameableCreator(widget.supportedEvents());
        ((DefaultListModel)nameableList.getModel()).removeAllElements();
        int j = 0;
        for(int k = widget.getListenerSize(); j < k; j++)
        {
            Listener listener = widget.getListener(j);
            if(!listener.isDefault())
                addNameObject(new NameObject((new StringBuilder()).append(switchLang(listener.getEventName())).append(j + 1).toString(), listener), j);
        }

        checkButtonEnabled();
        repaint();
    }

    public void updateWidgetListener(XCreator xcreator)
    {
        DefaultListModel defaultlistmodel = (DefaultListModel)nameableList.getModel();
        xcreator.toData().clearListeners();
        int i = 0;
        for(int j = defaultlistmodel.getSize(); i < j; i++)
            xcreator.toData().addListener((Listener)((NameObject)defaultlistmodel.getElementAt(i)).getObject());

        designer.fireTargetModified();
        checkButtonEnabled();
    }

    protected String title4PopupWindow()
    {
        return "Event";
    }

    public String createUnrepeatedName(String s)
    {
        DefaultListModel defaultlistmodel = (DefaultListModel)nameableList.getModel();
        Nameable anameable[] = new Nameable[defaultlistmodel.getSize()];
        for(int i = 0; i < defaultlistmodel.size(); i++)
            anameable[i] = (Nameable)defaultlistmodel.get(i);

        int j = 1;
        do
        {
            String s1 = (new StringBuilder()).append(s).append(j).toString();
            boolean flag = false;
            int k = 0;
            int l = defaultlistmodel.size();
            do
            {
                if(k >= l)
                    break;
                Nameable nameable = anameable[k];
                if(ComparatorUtils.equals(nameable.getName(), s1))
                {
                    flag = true;
                    break;
                }
                k++;
            } while(true);
            if(!flag)
                return s1;
            j++;
        } while(true);
    }






}
