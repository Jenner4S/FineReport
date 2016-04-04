// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.PlotSeries;

import com.fr.base.BaseUtils;
import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.base.MapHelper;
import com.fr.base.MapXMLHelper;
import com.fr.base.Utils;
import com.fr.chart.base.MapSvgAttr;
import com.fr.chart.base.MapSvgXMLHelper;
import com.fr.chart.chartattr.MapPlot;
import com.fr.design.DesignerEnvManager;
import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;

// Referenced classes of package com.fr.design.chart.series.PlotSeries:
//            UIGroupExtensionPane, MapDefiAreaNamePane, MapCustomPane

public class MapGroupExtensionPane extends BasicPane
    implements UIObserver
{

    private static final String TYPE_NAMES[] = {
        Inter.getLocText("FR-Chart-World_Map"), Inter.getLocText("FR-Chart-State_Map"), Inter.getLocText("FR-Chart-Province_Map"), Inter.getLocText("FR-Chart-Custom_Map")
    };
    private static final int WORD = 0;
    private static final int NATION = 1;
    private static final int STATE = 2;
    private static final int USER = 3;
    private static final int OFFSET_X = 800;
    private static final int OFFSET_Y = 200;
    private UIGroupExtensionPane groupExtensionPane;
    private UIButton addButton;
    private JPopupMenu popupMenu;
    private ArrayList changeListeners;
    private boolean hasPopulated;

    protected String title4PopupWindow()
    {
        return "Map";
    }

    public MapGroupExtensionPane()
    {
        changeListeners = new ArrayList();
        hasPopulated = false;
        setLayout(new BorderLayout());
        groupExtensionPane = new UIGroupExtensionPane(TYPE_NAMES) {

            final MapGroupExtensionPane this$0;

            protected void dealNewAddedDataIndex(Object obj)
            {
                String s = (String)obj;
                MapSvgXMLHelper mapsvgxmlhelper = MapSvgXMLHelper.getInstance();
                if(mapsvgxmlhelper.getNewMapAttr(s) != null)
                {
                    return;
                } else
                {
                    MapSvgAttr mapsvgattr = new MapSvgAttr();
                    mapsvgattr.setFilePath((new StringBuilder()).append(MapSvgXMLHelper.customMapPath()).append("/").append(s).append(".svg").toString());
                    mapsvgxmlhelper.addNewSvgMaps(mapsvgattr.getName(), mapsvgattr);
                    return;
                }
            }

            protected boolean isRespondToValueChange(ListSelectionEvent listselectionevent)
            {
                return !listselectionevent.getValueIsAdjusting() && !isPressOnDelete() && hasPopulated;
            }

            
            {
                this$0 = MapGroupExtensionPane.this;
                super(as);
            }
        }
;
        groupExtensionPane.addSelectionChangeListener(new com.fr.design.event.ChangeListener() {

            final MapGroupExtensionPane this$0;

            public void fireChanged(com.fr.design.event.ChangeEvent changeevent)
            {
                fireStateChange();
            }

            
            {
                this$0 = MapGroupExtensionPane.this;
                super();
            }
        }
);
        groupExtensionPane.addItemEditListener(new com.fr.design.event.ChangeListener() {

            final MapGroupExtensionPane this$0;

            public void fireChanged(com.fr.design.event.ChangeEvent changeevent)
            {
                doEdit(changeevent);
            }

            
            {
                this$0 = MapGroupExtensionPane.this;
                super();
            }
        }
);
        groupExtensionPane.addDeleteListener(new com.fr.design.event.ChangeListener() {

            final MapGroupExtensionPane this$0;

            public void fireChanged(com.fr.design.event.ChangeEvent changeevent)
            {
                String s = Utils.objectToString(groupExtensionPane.getSelectedObject());
                saveMapInfo(s);
            }

            
            {
                this$0 = MapGroupExtensionPane.this;
                super();
            }
        }
);
        setPreferredSize(new Dimension(400, 210));
        add(groupExtensionPane, "Center");
        addButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/add.png")) {

            final MapGroupExtensionPane this$0;

            protected void paintBorder(Graphics g)
            {
                Graphics2D graphics2d = (Graphics2D)g;
                graphics2d.setStroke(UIConstants.BS);
                java.awt.geom.RoundRectangle2D.Float float1 = new java.awt.geom.RoundRectangle2D.Float(0.5F, 0.5F, getWidth() - 1, getHeight() - 1, 0.0F, 0.0F);
                graphics2d.setColor(UIConstants.LINE_COLOR);
                graphics2d.draw(float1);
            }

            
            {
                this$0 = MapGroupExtensionPane.this;
                super(icon);
            }
        }
;
        addButton.addActionListener(new ActionListener() {

            final MapGroupExtensionPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                popupMenu.show(MapGroupExtensionPane.this, addButton.getX() + 1, addButton.getY() + addButton.getHeight());
            }

            
            {
                this$0 = MapGroupExtensionPane.this;
                super();
            }
        }
);
        add(addButton, "South");
        initPopupMenu();
    }

    private void initPopupMenu()
    {
        popupMenu = new JPopupMenu() {

            final MapGroupExtensionPane this$0;

            public Dimension getPreferredSize()
            {
                Dimension dimension = new Dimension();
                dimension.height = super.getPreferredSize().height;
                dimension.width = addButton.getWidth() - 2;
                return dimension;
            }

            
            {
                this$0 = MapGroupExtensionPane.this;
                super();
            }
        }
;
        JMenuItem jmenuitem = new JMenuItem(TYPE_NAMES[0]);
        popupMenu.add(jmenuitem);
        jmenuitem.addActionListener(new ActionListener() {

            final MapGroupExtensionPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                groupExtensionPane.addData(MapGroupExtensionPane.TYPE_NAMES[0], MapGroupExtensionPane.TYPE_NAMES[0], true);
            }

            
            {
                this$0 = MapGroupExtensionPane.this;
                super();
            }
        }
);
        JMenuItem jmenuitem1 = new JMenuItem(TYPE_NAMES[1]);
        popupMenu.add(jmenuitem1);
        jmenuitem1.addActionListener(new ActionListener() {

            final MapGroupExtensionPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                groupExtensionPane.addData(MapGroupExtensionPane.TYPE_NAMES[1], MapGroupExtensionPane.TYPE_NAMES[1], true);
            }

            
            {
                this$0 = MapGroupExtensionPane.this;
                super();
            }
        }
);
        JMenuItem jmenuitem2 = new JMenuItem(TYPE_NAMES[2]);
        popupMenu.add(jmenuitem2);
        jmenuitem2.addActionListener(new ActionListener() {

            final MapGroupExtensionPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                groupExtensionPane.addData(MapGroupExtensionPane.TYPE_NAMES[2], MapGroupExtensionPane.TYPE_NAMES[2], true);
            }

            
            {
                this$0 = MapGroupExtensionPane.this;
                super();
            }
        }
);
        JMenuItem jmenuitem3 = new JMenuItem(TYPE_NAMES[3]);
        popupMenu.add(jmenuitem3);
        jmenuitem3.addActionListener(new ActionListener() {

            final MapGroupExtensionPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                groupExtensionPane.addData(MapGroupExtensionPane.TYPE_NAMES[3], MapGroupExtensionPane.TYPE_NAMES[3], true);
            }

            
            {
                this$0 = MapGroupExtensionPane.this;
                super();
            }
        }
);
    }

    private void doEdit(com.fr.design.event.ChangeEvent changeevent)
    {
        MouseEvent mouseevent = (MouseEvent)changeevent.getSource();
        JPopupMenu jpopupmenu = new JPopupMenu();
        String s = Utils.objectToString(groupExtensionPane.getSelectedObject());
        jpopupmenu.add(createAreaItem(s));
        jpopupmenu.add(createMarkerItem(s));
        jpopupmenu.add(createLayerItem(s));
        jpopupmenu.add(createRenameItem());
        jpopupmenu.show(this, mouseevent.getXOnScreen() - 800, mouseevent.getYOnScreen() - 200);
    }

    private void mapCheckBeforeEdit(String s)
    {
        if(MapSvgXMLHelper.getInstance().containsMapName(s) || MapSvgXMLHelper.getInstance().getNewMapAttr(s) != null)
        {
            return;
        } else
        {
            MapSvgAttr mapsvgattr = new MapSvgAttr();
            mapsvgattr.setFilePath((new StringBuilder()).append(MapSvgXMLHelper.customMapPath()).append("/").append(s).append(".svg").toString());
            MapSvgXMLHelper.getInstance().addNewSvgMaps(s, mapsvgattr);
            return;
        }
    }

    private JMenuItem createAreaItem(final String oldName)
    {
        JMenuItem jmenuitem = new JMenuItem(Inter.getLocText(new String[] {
            "Edit", "Image", "Filed"
        }));
        jmenuitem.addActionListener(new ActionListener() {

            final String val$oldName;
            final MapGroupExtensionPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                final MapCustomPane image = new MapCustomPane();
                image.setImageSelectType(1);
                image.populateBean(oldName);
                image.setTypeNameAndMapName(groupExtensionPane.getSelectedType(), Utils.objectToString(groupExtensionPane.getSelectedObject()));
                mapCheckBeforeEdit(oldName);
                final Image oldImage = getMapImage(oldName);
                BasicDialog basicdialog = image.showMediumWindow(SwingUtilities.getWindowAncestor(MapGroupExtensionPane.this), new DialogActionAdapter() {

                    final MapCustomPane val$image;
                    final Image val$oldImage;
                    final _cls12 this$1;

                    public void doOk()
                    {
                        image.updateBean();
                        Image image1 = getMapImage(oldName);
                        if(!ComparatorUtils.equals(oldImage, image1))
                            fireStateChange();
                        MapSvgAttr mapsvgattr = MapSvgXMLHelper.getInstance().getMapAttr(oldName);
                        if(mapsvgattr != null)
                            mapsvgattr.addVersionID();
                        saveMapInfo(oldName);
                        refresh();
                    }

                    
                    {
                        this$1 = _cls12.this;
                        image = mapcustompane;
                        oldImage = image1;
                        super();
                    }
                }
);
                basicdialog.setVisible(true);
            }

            
            {
                this$0 = MapGroupExtensionPane.this;
                oldName = s;
                super();
            }
        }
);
        return jmenuitem;
    }

    private Image getMapImage(String s)
    {
        if(MapSvgXMLHelper.getInstance().containsMapName(s))
        {
            MapSvgAttr mapsvgattr = MapSvgXMLHelper.getInstance().getMapAttr(s);
            if(mapsvgattr == null)
                return null;
            else
                return mapsvgattr.getMapImage();
        } else
        {
            return null;
        }
    }

    private JMenuItem createMarkerItem(final String oldName)
    {
        JMenuItem jmenuitem = new JMenuItem(Inter.getLocText(new String[] {
            "Edit", "Image", "Marker"
        }));
        jmenuitem.addActionListener(new ActionListener() {

            final String val$oldName;
            final MapGroupExtensionPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                final MapCustomPane image = new MapCustomPane();
                image.setImageSelectType(0);
                image.populateBean(oldName);
                mapCheckBeforeEdit(oldName);
                final Image oldImage = getMapImage(oldName);
                BasicDialog basicdialog = image.showMediumWindow(SwingUtilities.getWindowAncestor(image), new DialogActionAdapter() {

                    final MapCustomPane val$image;
                    final Image val$oldImage;
                    final _cls13 this$1;

                    public void doOk()
                    {
                        image.updateBean();
                        Image image1 = getMapImage(oldName);
                        if(!ComparatorUtils.equals(oldImage, image1))
                            fireStateChange();
                        MapSvgAttr mapsvgattr = MapSvgXMLHelper.getInstance().getMapAttr(oldName);
                        if(mapsvgattr != null)
                            mapsvgattr.addVersionID();
                        saveMapInfo(oldName);
                        refresh();
                    }

                    
                    {
                        this$1 = _cls13.this;
                        image = mapcustompane;
                        oldImage = image1;
                        super();
                    }
                }
);
                basicdialog.setVisible(true);
            }

            
            {
                this$0 = MapGroupExtensionPane.this;
                oldName = s;
                super();
            }
        }
);
        return jmenuitem;
    }

    private JMenuItem createLayerItem(final String oldName)
    {
        JMenuItem jmenuitem = new JMenuItem(Inter.getLocText(new String[] {
            "Filed", "Corresponding_Fields"
        }));
        jmenuitem.addActionListener(new ActionListener() {

            final String val$oldName;
            final MapGroupExtensionPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                final MapDefiAreaNamePane namedPane = new MapDefiAreaNamePane();
                namedPane.populateBean(oldName);
                mapCheckBeforeEdit(oldName);
                BasicDialog basicdialog = namedPane.showMediumWindow(SwingUtilities.getWindowAncestor(namedPane), new DialogActionAdapter() {

                    final MapDefiAreaNamePane val$namedPane;
                    final _cls14 this$1;

                    public void doOk()
                    {
                        namedPane.updateBean();
                        MapSvgAttr mapsvgattr = MapSvgXMLHelper.getInstance().getMapAttr(oldName);
                        if(mapsvgattr != null)
                            mapsvgattr.addVersionID();
                        saveMapInfo(oldName);
                    }

                    
                    {
                        this$1 = _cls14.this;
                        namedPane = mapdefiareanamepane;
                        super();
                    }
                }
);
                basicdialog.setVisible(true);
                refresh();
            }

            
            {
                this$0 = MapGroupExtensionPane.this;
                oldName = s;
                super();
            }
        }
);
        return jmenuitem;
    }

    private void showRenameWaring(String s)
    {
        JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), (new StringBuilder()).append("\"").append(s).append("\"").append(Inter.getLocText("Utils-has_been_existed")).append("!").toString(), Inter.getLocText("FR-Designer_Alert"), 2);
    }

    private JMenuItem createRenameItem()
    {
        JMenuItem jmenuitem = new JMenuItem(Inter.getLocText("FR-Chart-Map_Rename"));
        jmenuitem.addActionListener(new ActionListener() {

            final MapGroupExtensionPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                String s;
                String s1;
                s = JOptionPane.showInputDialog(DesignerContext.getDesignerFrame().getContentPane(), Inter.getLocText("FR-Chart-Map_Rename"), groupExtensionPane.getSelectedObject());
                if(!StringUtils.isNotBlank(s))
                    break MISSING_BLOCK_LABEL_356;
                s1 = Utils.objectToString(groupExtensionPane.getSelectedObject());
                if(ComparatorUtils.equals(s1, s))
                    return;
                if(MapSvgXMLHelper.getInstance().getNewMapAttr(s) != null)
                {
                    showRenameWaring(s);
                    return;
                }
                if(FRContext.getCurrentEnv().fileExists(StableUtils.pathJoin(new String[] {
            MapSvgXMLHelper.relativeDefaultMapPath(), (new StringBuilder()).append(s).append(".svg").toString()
        })))
                {
                    showRenameWaring(s);
                    return;
                }
                if(FRContext.getCurrentEnv().fileExists(StableUtils.pathJoin(new String[] {
            MapSvgXMLHelper.relativeCustomMapPath(), (new StringBuilder()).append(s).append(".svg").toString()
        })))
                {
                    showRenameWaring(s);
                    return;
                }
                MapSvgAttr mapsvgattr;
                mapsvgattr = MapSvgXMLHelper.getInstance().getMapAttr(s1);
                if(mapsvgattr == null)
                    mapsvgattr = MapSvgXMLHelper.getInstance().getNewMapAttr(s1);
                if(mapsvgattr == null)
                    return;
                try
                {
                    mapsvgattr.renameMap(s);
                    groupExtensionPane.setValueAtCurrentSelectIndex(s);
                    fireStateChange();
                    saveMapInfo(s);
                    FRContext.getCurrentEnv().deleteFile(StableUtils.pathJoin(new String[] {
                        MapSvgXMLHelper.relativeDefaultMapPath(), (new StringBuilder()).append(s1).append(".svg").toString()
                    }));
                    FRContext.getCurrentEnv().deleteFile(StableUtils.pathJoin(new String[] {
                        MapSvgXMLHelper.relativeCustomMapPath(), (new StringBuilder()).append(s1).append(".svg").toString()
                    }));
                    refresh();
                }
                catch(Exception exception)
                {
                    FRLogger.getLogger().error(exception.getMessage());
                }
            }

            
            {
                this$0 = MapGroupExtensionPane.this;
                super();
            }
        }
);
        return jmenuitem;
    }

    private void refresh()
    {
        validate();
        repaint();
        DesignerFrame designerframe = DesignerContext.getDesignerFrame();
        if(designerframe != null)
            designerframe.repaint();
    }

    private void saveMapInfo(final String mapName)
    {
        SwingWorker swingworker = new SwingWorker() {

            final String val$mapName;
            final MapGroupExtensionPane this$0;

            protected Integer doInBackground()
                throws Exception
            {
                MapSvgAttr mapsvgattr = MapSvgXMLHelper.getInstance().getMapAttr(mapName);
                if(mapsvgattr != null)
                    mapsvgattr.writeBack(mapName);
                return Integer.valueOf(0);
            }

            protected void done()
            {
                FRLogger.getLogger().info(Inter.getLocText("FR-Chart-Map_Saved"));
            }

            protected volatile Object doInBackground()
                throws Exception
            {
                return doInBackground();
            }

            
            {
                this$0 = MapGroupExtensionPane.this;
                mapName = s;
                super();
            }
        }
;
        swingworker.execute();
        DesignerEnvManager.addWorkers(swingworker);
    }

    private void fireStateChange()
    {
        for(int i = changeListeners.size(); i > 0; i--)
            ((ChangeListener)changeListeners.get(i - 1)).stateChanged(new ChangeEvent(this));

    }

    public String updateBean(MapPlot mapplot)
    {
        if(!hasPopulated)
        {
            populateBean(mapplot);
            hasPopulated = true;
        }
        Object obj = mapplot.isSvgMap() ? ((Object) (MapSvgXMLHelper.getInstance())) : ((Object) (MapXMLHelper.getInstance()));
        ((MapHelper) (obj)).clearCateNames();
        String as[] = TYPE_NAMES;
        int i = as.length;
        for(int j = 0; j < i; j++)
        {
            String s = as[j];
            Object aobj[] = groupExtensionPane.getData(s);
            Object aobj1[] = aobj;
            int k = aobj1.length;
            for(int l = 0; l < k; l++)
            {
                Object obj1 = aobj1[l];
                ((MapHelper) (obj)).addCateNames(s, obj1);
            }

        }

        return Utils.objectToString(groupExtensionPane.getSelectedObject());
    }

    public void populateBean(MapPlot mapplot)
    {
        hasPopulated = false;
        groupExtensionPane.clearData();
        String as[] = TYPE_NAMES;
        int i = as.length;
        for(int j = 0; j < i; j++)
        {
            String s = as[j];
            Object obj = mapplot.isSvgMap() ? ((Object) (MapSvgXMLHelper.getInstance())) : ((Object) (MapXMLHelper.getInstance()));
            java.util.List list = ((MapHelper) (obj)).getNamesListWithCateName(s);
            Object obj1;
            for(Iterator iterator = list.iterator(); iterator.hasNext(); groupExtensionPane.addData(obj1, s))
                obj1 = iterator.next();

        }

        groupExtensionPane.setSelectedObject(mapplot.getMapName());
        hasPopulated = true;
    }

    public void registerChangeListener(final UIObserverListener listener)
    {
        changeListeners.add(new ChangeListener() {

            final UIObserverListener val$listener;
            final MapGroupExtensionPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                listener.doChange();
            }

            
            {
                this$0 = MapGroupExtensionPane.this;
                listener = uiobserverlistener;
                super();
            }
        }
);
    }

    public boolean shouldResponseChangeListener()
    {
        return true;
    }

    public void setEnabled(boolean flag)
    {
        super.setEnabled(flag);
        addButton.setEnabled(flag);
        popupMenu.setEnabled(flag);
        groupExtensionPane.setEnabled(flag);
    }













}
