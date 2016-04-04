// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.report;

import com.fr.base.MapXMLHelper;
import com.fr.base.Utils;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.base.MapSvgAttr;
import com.fr.chart.base.MapSvgXMLHelper;
import com.fr.chart.chartglyph.MapAttr;
import com.fr.design.DesignerEnvManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.dialog.UIDialog;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

// Referenced classes of package com.fr.design.chart.report:
//            MapCubeSetDataPane

public class MapCubeLayerPane extends BasicBeanPane
{

    private JTree mapTree;
    private DefaultMutableTreeNode root;
    private String editingMap;
    private Set editedMap;
    private java.util.List fireWhenTreeChange;
    private java.util.List hasDealNames;
    private boolean isSvg;
    MouseListener mapListener;

    public MapCubeLayerPane()
    {
        editingMap = "";
        editedMap = new HashSet();
        fireWhenTreeChange = new ArrayList();
        hasDealNames = new ArrayList();
        isSvg = true;
        mapListener = new MouseAdapter() {

            final MapCubeLayerPane this$0;

            public void mouseClicked(MouseEvent mouseevent)
            {
                TreePath treepath = mapTree.getSelectionPath();
                if(treepath == null)
                    return;
                String s = Utils.objectToString(((DefaultMutableTreeNode)treepath.getLastPathComponent()).getUserObject());
                if(SwingUtilities.isRightMouseButton(mouseevent))
                {
                    if(!isSvg)
                    {
                        popBitMapDialog(mouseevent, s);
                        return;
                    }
                    final MapSvgAttr editingAttr = MapSvgXMLHelper.getInstance().getMapAttr(editingMap);
                    editedMap.add(editingMap);
                    final MapCubeSetDataPane setDataPane = new MapCubeSetDataPane();
                    setDataPane.freshComboxNames();
                    MapSvgAttr mapsvgattr = MapSvgXMLHelper.getInstance().getMapAttr(s);
                    editedMap.add(s);
                    if(mapsvgattr != null)
                    {
                        ArrayList arraylist = new ArrayList();
                        Object obj;
                        String s1;
                        for(Iterator iterator = mapsvgattr.shapeValuesIterator(); iterator.hasNext(); arraylist.add(((Object) (new Object[] {
            obj, s1
        }))))
                        {
                            obj = iterator.next();
                            s1 = editingAttr.getLayerTo(Utils.objectToString(obj));
                            if(ArrayUtils.contains(ChartConstants.NONE_KEYS, s1))
                                s1 = "";
                        }

                        setDataPane.populateBean(arraylist);
                    }
                    int i = (int)(mapTree.getLocationOnScreen().getX() + (double)mapTree.getWidth());
                    int j = (int)mouseevent.getLocationOnScreen().getY();
                    UIDialog uidialog = setDataPane.showUnsizedWindow(SwingUtilities.getWindowAncestor(setDataPane), new DialogActionAdapter() {

                        final MapCubeSetDataPane val$setDataPane;
                        final MapSvgAttr val$editingAttr;
                        final _cls1 this$1;

                        public void doOk()
                        {
                            java.util.List list = setDataPane.updateBean();
                            for(int k = 0; k < list.size(); k++)
                            {
                                Object aobj[] = (Object[])(Object[])list.get(k);
                                editingAttr.putLayerTo(Utils.objectToString(aobj[0]), Utils.objectToString(aobj[1]));
                            }

                            initRootTree(editingMap);
                            saveMapInfo();
                        }

                    
                    {
                        this$1 = _cls1.this;
                        setDataPane = mapcubesetdatapane;
                        editingAttr = mapsvgattr;
                        super();
                    }
                    }
);
                    uidialog.setSize(300, 300);
                    GUICoreUtils.centerWindow(uidialog);
                    uidialog.setVisible(true);
                }
            }

            
            {
                this$0 = MapCubeLayerPane.this;
                super();
            }
        }
;
        initCom();
    }

    private void initCom()
    {
        setLayout(new BorderLayout());
        root = new DefaultMutableTreeNode();
        mapTree = new JTree(root);
        mapTree.setRootVisible(false);
        mapTree.addMouseListener(mapListener);
        JScrollPane jscrollpane = new JScrollPane(mapTree);
        jscrollpane.setPreferredSize(new Dimension(100, 100));
        add(jscrollpane, "Center");
    }

    public void setSvg(boolean flag)
    {
        isSvg = flag;
    }

    public int getTreeDepth()
    {
        return root.getDepth();
    }

    public void initRootTree(String s)
    {
        editingMap = s;
        root.removeAllChildren();
        DefaultMutableTreeNode defaultmutabletreenode = new DefaultMutableTreeNode(s);
        root.add(defaultmutabletreenode);
        if(isSvg)
        {
            MapSvgAttr mapsvgattr = MapSvgXMLHelper.getInstance().getMapAttr(s);
            hasDealNames.clear();
            add4Node(mapsvgattr, defaultmutabletreenode, s);
        } else
        {
            MapAttr mapattr = (MapAttr)MapXMLHelper.getInstance().getMapAttr(s);
            hasDealNames.clear();
            addBitMap4Node(mapattr, defaultmutabletreenode, s);
        }
        mapTree.doLayout();
        mapTree.validate();
        ((DefaultTreeModel)mapTree.getModel()).reload();
        for(int i = 0; i < fireWhenTreeChange.size(); i++)
            ((ChangeListener)fireWhenTreeChange.get(i)).stateChanged(new ChangeEvent(this));

    }

    public void addChangeListener(ChangeListener changelistener)
    {
        fireWhenTreeChange.add(changelistener);
    }

    private void addBitMap4Node(MapAttr mapattr, DefaultMutableTreeNode defaultmutabletreenode, String s)
    {
        MapAttr mapattr1 = (MapAttr)MapXMLHelper.getInstance().getMapAttr(s);
        if(mapattr1 != null)
        {
            Iterator iterator = mapattr1.shapeValuesIterator();
            do
            {
                if(!iterator.hasNext())
                    break;
                Object obj = iterator.next();
                String s1 = Utils.objectToString(mapattr.getLayerTo(Utils.objectToString(obj)));
                MapAttr mapattr2 = (MapAttr)MapXMLHelper.getInstance().getMapAttr(s1);
                if(mapattr2 != null)
                {
                    DefaultMutableTreeNode defaultmutabletreenode1 = new DefaultMutableTreeNode(obj);
                    defaultmutabletreenode.add(defaultmutabletreenode1);
                    if(!hasDealNames.contains(Utils.objectToString(defaultmutabletreenode1.getUserObject())))
                    {
                        hasDealNames.add(Utils.objectToString(defaultmutabletreenode1.getUserObject()));
                        addBitMap4Node(mapattr, defaultmutabletreenode1, s1);
                    }
                }
            } while(true);
        }
    }

    private void add4Node(MapSvgAttr mapsvgattr, DefaultMutableTreeNode defaultmutabletreenode, String s)
    {
        MapSvgAttr mapsvgattr1 = MapSvgXMLHelper.getInstance().getMapAttr(s);
        if(mapsvgattr1 != null)
        {
            Iterator iterator = mapsvgattr1.shapeValuesIterator();
            do
            {
                if(!iterator.hasNext())
                    break;
                Object obj = iterator.next();
                String s1 = Utils.objectToString(mapsvgattr.getLayerTo(Utils.objectToString(obj)));
                MapSvgAttr mapsvgattr2 = MapSvgXMLHelper.getInstance().getMapAttr(s1);
                if(mapsvgattr2 != null)
                {
                    DefaultMutableTreeNode defaultmutabletreenode1 = new DefaultMutableTreeNode(obj);
                    defaultmutabletreenode.add(defaultmutabletreenode1);
                    if(!hasDealNames.contains(Utils.objectToString(defaultmutabletreenode1.getUserObject())))
                    {
                        hasDealNames.add(Utils.objectToString(defaultmutabletreenode1.getUserObject()));
                        add4Node(mapsvgattr, defaultmutabletreenode1, s1);
                    }
                }
            } while(true);
        }
    }

    private void popBitMapDialog(MouseEvent mouseevent, String s)
    {
        final MapAttr editingAttr = (MapAttr)MapXMLHelper.getInstance().getMapAttr(editingMap);
        editedMap.add(editingMap);
        final MapCubeSetDataPane setDataPane = new MapCubeSetDataPane();
        setDataPane.freshBitMapComboxNames();
        MapAttr mapattr = (MapAttr)MapXMLHelper.getInstance().getMapAttr(s);
        if(mapattr != null)
        {
            ArrayList arraylist = new ArrayList();
            Object obj;
            String s1;
            for(Iterator iterator = mapattr.shapeValuesIterator(); iterator.hasNext(); arraylist.add(((Object) (new Object[] {
    obj, s1
}))))
            {
                obj = iterator.next();
                s1 = editingAttr.getLayerTo(Utils.objectToString(obj));
                if(ArrayUtils.contains(ChartConstants.NONE_KEYS, s1))
                    s1 = "";
            }

            setDataPane.populateBean(arraylist);
        }
        int i = (int)(mapTree.getLocationOnScreen().getX() + (double)mapTree.getWidth());
        int j = (int)mouseevent.getLocationOnScreen().getY();
        UIDialog uidialog = setDataPane.showUnsizedWindow(SwingUtilities.getWindowAncestor(setDataPane), new DialogActionAdapter() {

            final MapCubeSetDataPane val$setDataPane;
            final MapAttr val$editingAttr;
            final MapCubeLayerPane this$0;

            public void doOk()
            {
                java.util.List list = setDataPane.updateBean();
                for(int k = 0; k < list.size(); k++)
                {
                    Object aobj[] = (Object[])(Object[])list.get(k);
                    editingAttr.putLayerTo(aobj[0], aobj[1]);
                }

                initRootTree(editingMap);
                saveMapInfo();
            }

            
            {
                this$0 = MapCubeLayerPane.this;
                setDataPane = mapcubesetdatapane;
                editingAttr = mapattr;
                super();
            }
        }
);
        uidialog.setSize(300, 300);
        GUICoreUtils.centerWindow(uidialog);
        uidialog.setVisible(true);
    }

    private void saveMapInfo()
    {
        final String mapNames[] = (String[])editedMap.toArray(new String[0]);
        if(isSvg)
            editedMap.clear();
        SwingWorker swingworker = new SwingWorker() {

            final String val$mapNames[];
            final MapCubeLayerPane this$0;

            protected Integer doInBackground()
                throws Exception
            {
                if(isSvg)
                    MapSvgXMLHelper.getInstance().saveEditedMaps(mapNames);
                else
                    MapXMLHelper.getInstance().writerMapSourceWhenEditMap();
                return Integer.valueOf(0);
            }

            protected void done()
            {
                FRLogger.getLogger().info("Map Save End");
            }

            protected volatile Object doInBackground()
                throws Exception
            {
                return doInBackground();
            }

            
            {
                this$0 = MapCubeLayerPane.this;
                mapNames = as;
                super();
            }
        }
;
        swingworker.execute();
        DesignerEnvManager.addWorkers(swingworker);
    }

    public void populateBean(String s)
    {
        initRootTree(s);
    }

    public void updateBean(String s)
    {
    }

    public String updateBean()
    {
        return "";
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("FR-Chart-Map_Drill");
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((String)obj);
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((String)obj);
    }






}
