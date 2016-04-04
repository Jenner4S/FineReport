// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.chart.base.MapSvgAttr;
import com.fr.chart.base.MapSvgXMLHelper;
import com.fr.design.DesignerEnvManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.controlpane.JControlPane;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.controlpane.NameableSelfCreator;
import com.fr.design.gui.controlpane.ShortCut4JControlPane;
import com.fr.design.gui.controlpane.UnrepeatedNameHelper;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilist.JNameEdList;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.stable.Nameable;
import com.fr.stable.StringUtils;
import com.fr.stable.core.PropertyChangeAdapter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

// Referenced classes of package com.fr.design.mainframe:
//            MapEditPane, ChartDesigner, MapPlotPane4ToolBar

public class MapArrayPane extends JControlPane
{
    private class NameableListCellRenderer extends DefaultListCellRenderer
    {

        final MapArrayPane this$0;

        public Component getListCellRendererComponent(JList jlist, Object obj, int i, boolean flag, boolean flag1)
        {
            super.getListCellRendererComponent(jlist, obj, i, flag, flag1);
            if(obj instanceof ListModelElement)
            {
                Nameable nameable = ((ListModelElement)obj).wrapper;
                setText(((ListModelElement)obj).wrapper.getName());
                NameableCreator anameablecreator[] = creators();
                int j = anameablecreator.length;
                int k = 0;
                do
                {
                    if(k >= j)
                        break;
                    NameableCreator nameablecreator = anameablecreator[k];
                    if(nameablecreator.menuIcon() != null && nameablecreator.acceptObject2Populate(nameable) != null)
                    {
                        setIcon(nameablecreator.menuIcon());
                        setToolTipText(nameablecreator.createTooltip());
                        break;
                    }
                    k++;
                } while(true);
            }
            return this;
        }

        private NameableListCellRenderer()
        {
            this$0 = MapArrayPane.this;
            super();
        }

    }


    private static final int LEFT_WIDTH = 180;
    private static final Color LINE_COLOR = new Color(176, 176, 176);
    private static final int TOP_GAP = 5;
    private static final String TYPE_NAMES[] = {
        Inter.getLocText("FR-Chart-World_Map"), Inter.getLocText("FR-Chart-State_Map"), Inter.getLocText("FR-Chart-Province_Map"), Inter.getLocText("FR-Chart-Custom_Map")
    };
    private String mapType;
    private String mapDetailName;
    MapPlotPane4ToolBar toolBar;
    UIComboBox mapTypeBox;
    private ArrayList editedNames;
    private ItemListener typeListener;
    private ArrayList removeNames;
    private MapEditPane mapEditPane;
    private ChartDesigner chartDesigner;

    public MapArrayPane(String s, String s1, ChartDesigner chartdesigner)
    {
        editedNames = new ArrayList();
        typeListener = new ItemListener() {

            final MapArrayPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                updateBeans();
                mapType = mapTypeBox.getSelectedItem().toString();
                populate(MapSvgXMLHelper.getInstance().getAllMapObjects4Cate(mapType));
            }

            
            {
                this$0 = MapArrayPane.this;
                super();
            }
        }
;
        removeNames = new ArrayList();
        mapDetailName = s1;
        mapType = s;
        if(mapTypeBox != null)
            mapTypeBox.setSelectedItem(s);
        chartDesigner = chartdesigner;
        mapTypeBox.addItemListener(typeListener);
        setBorder(new EmptyBorder(5, 0, 0, 0));
        addEditingListner(new PropertyChangeAdapter() {

            final MapArrayPane this$0;

            public void propertyChange()
            {
                dealPropertyChange();
            }

            
            {
                this$0 = MapArrayPane.this;
                super();
            }
        }
);
    }

    public void setToolBarPane(MapPlotPane4ToolBar mapplotpane4toolbar)
    {
        toolBar = mapplotpane4toolbar;
    }

    protected void doWhenPopulate(BasicBeanPane basicbeanpane)
    {
        mapEditPane = (MapEditPane)basicbeanpane;
        mapEditPane.dealWidthMap(mapType);
        String s = ((MapEditPane)basicbeanpane).getCurrentMapName();
        if(!editedNames.contains(s))
            editedNames.add(s);
    }

    protected JPanel getLeftPane()
    {
        JPanel jpanel = super.getLeftPane();
        mapTypeBox = new UIComboBox(TYPE_NAMES);
        JPanel jpanel1 = new JPanel();
        jpanel1.setLayout(new BorderLayout());
        jpanel1.setBorder(new EmptyBorder(3, 0, 0, 0));
        jpanel1.add(mapTypeBox, "North");
        jpanel1.add(jpanel, "Center");
        return jpanel1;
    }

    private void dealPropertyChange()
    {
        MapSvgXMLHelper mapsvgxmlhelper = MapSvgXMLHelper.getInstance();
        java.util.List list = mapsvgxmlhelper.getNamesListWithCateName(mapType);
        String as[] = nameableList.getAllNames();
        as[nameableList.getSelectedIndex()] = "";
        String s = getEditingName();
        if(StringUtils.isEmpty(s))
        {
            String as1[] = {
                "NOT_NULL_Des", "Please_Rename"
            };
            String as2[] = {
                ",", "!"
            };
            nameableList.stopEditing();
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), Inter.getLocText(as1, as2));
            setWarnigText(editingIndex);
            return;
        }
        if(!ComparatorUtils.equals(s, selectedName) && isNameRepeted(new java.util.List[] {
    list, Arrays.asList(as)
}, s))
        {
            nameableList.stopEditing();
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), Inter.getLocText(new String[] {
                "FR-Chart-Map_NameAlreadyExist", "Please_Rename"
            }, new String[] {
                ",", "!"
            }));
            setWarnigText(editingIndex);
            return;
        }
        String s1 = mapEditPane.getCurrentMapName();
        mapEditPane.setCurrentMapName(s);
        mapEditPane.dealWidthMap(mapType);
        if(editedNames.contains(s1))
        {
            editedNames.remove(s1);
            editedNames.add(s);
        }
        if(mapsvgxmlhelper.getNewMapAttr(s1) != null)
        {
            MapSvgAttr mapsvgattr = mapsvgxmlhelper.getNewMapAttr(s1);
            mapsvgattr.renameMap(s);
            mapsvgxmlhelper.removeNewMapAttr(s1);
            mapsvgxmlhelper.addNewSvgMaps(s, mapsvgattr);
        }
        toolBar.fireTargetModified();
        saveMapInfo(s);
    }

    protected void doAfterRemove()
    {
        String s;
        for(Iterator iterator = removeNames.iterator(); iterator.hasNext(); MapSvgXMLHelper.getInstance().removeNewMapAttr(s))
        {
            s = (String)iterator.next();
            MapSvgXMLHelper.getInstance().removeMapAttr(s);
        }

        update4AllType();
    }

    protected void doBeforeRemove()
    {
        removeNames.clear();
        int ai[] = nameableList.getSelectedIndices();
        int i = ai.length;
        for(int j = 0; j < i; j++)
        {
            int k = ai[j];
            removeNames.add(nameableList.getNameAt(k));
        }

    }

    private void saveMapInfo(final String mapName)
    {
        SwingWorker swingworker = new SwingWorker() {

            final String val$mapName;
            final MapArrayPane this$0;

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
                this$0 = MapArrayPane.this;
                mapName = s;
                super();
            }
        }
;
        swingworker.execute();
        DesignerEnvManager.addWorkers(swingworker);
    }

    private void update4AllType()
    {
        MapSvgXMLHelper mapsvgxmlhelper = MapSvgXMLHelper.getInstance();
        mapsvgxmlhelper.clearNames4Cate(mapType);
        String as[] = nameableList.getAllNames();
        int i = as.length;
        for(int j = 0; j < i; j++)
        {
            String s = as[j];
            MapSvgAttr mapsvgattr = mapsvgxmlhelper.getMapAttr(s);
            if(mapsvgattr != null)
                mapsvgxmlhelper.addCateNames(mapsvgattr.getMapType(), mapsvgattr.getName());
        }

    }

    public NameableCreator[] createNameableCreators()
    {
        return (new NameableCreator[] {
            new NameableSelfCreator(Inter.getLocText("FR-Chart-Custom_Map"), com/fr/chart/base/MapSvgAttr, com/fr/design/mainframe/MapEditPane) {

                final MapArrayPane this$0;

                public MapSvgAttr createNameable(UnrepeatedNameHelper unrepeatednamehelper)
                {
                    MapSvgAttr mapsvgattr = new MapSvgAttr();
                    mapsvgattr.setFilePath((new StringBuilder()).append(MapSvgXMLHelper.customMapPath()).append("/").append(unrepeatednamehelper.createUnrepeatedName(Inter.getLocText("FR-Chart-Custom_Map"))).append(".svg").toString());
                    MapSvgXMLHelper.getInstance().addNewSvgMaps(mapsvgattr.getName(), mapsvgattr);
                    update4Edited(mapsvgattr.getName());
                    return mapsvgattr;
                }

                public String createTooltip()
                {
                    return null;
                }

                public void saveUpdatedBean(ListModelElement listmodelelement, Object obj)
                {
                    listmodelelement.wrapper = (Nameable)obj;
                }

                public volatile Nameable createNameable(UnrepeatedNameHelper unrepeatednamehelper)
                {
                    return createNameable(unrepeatednamehelper);
                }

            
            {
                this$0 = MapArrayPane.this;
                super(s, class1, class2);
            }
            }

        });
    }

    protected boolean isCreatorNeedIocn()
    {
        return false;
    }

    protected ShortCut4JControlPane[] createShortcuts()
    {
        return (new ShortCut4JControlPane[] {
            addItemShortCut(), removeItemShortCut()
        });
    }

    protected int getLeftPreferredSize()
    {
        return 180;
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText(new String[] {
            "FR-Chart-Map_Map", "FR-Chart-Data_Edit"
        });
    }

    public void updateBeans()
    {
        super.update();
        update4AllType();
        updateAllEditedAttrMaps();
        MapSvgXMLHelper.getInstance().clearTempAttrMaps();
        toolBar.fireTargetModified();
        saveMapInfo(selectedName);
    }

    public JNameEdList createJNameList()
    {
        JNameEdList jnameedlist = new JNameEdList(new DefaultListModel()) {

            final MapArrayPane this$0;

            public Rectangle createRect(Rectangle rectangle, int i)
            {
                return rectangle;
            }

            protected void doAfterLostFocus()
            {
                updateControlUpdatePane();
            }

            public void setNameAt(String s, int i)
            {
                super.setNameAt(s, i);
                update4Edited(s);
            }

            
            {
                this$0 = MapArrayPane.this;
                super(listmodel);
            }
        }
;
        jnameedlist.setCellRenderer(new NameableListCellRenderer());
        return jnameedlist;
    }

    protected void update4Edited(String s)
    {
    }

    private void updateAllEditedAttrMaps()
    {
        MapSvgXMLHelper mapsvgxmlhelper = MapSvgXMLHelper.getInstance();
        Iterator iterator = editedNames.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            String s = (String)iterator.next();
            if(mapsvgxmlhelper.getMapAttr(s) != null)
                mapsvgxmlhelper.getMapAttr(s).writeBack(s);
            else
            if(mapsvgxmlhelper.getNewMapAttr(s) != null)
                mapsvgxmlhelper.getNewMapAttr(s).writeBack(s);
        } while(true);
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




}
