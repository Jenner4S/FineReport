// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.chart.base.MapSvgAttr;
import com.fr.chart.base.MapSvgXMLHelper;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.series.PlotSeries.*;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import java.awt.BorderLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MapEditPane extends BasicBeanPane
{

    private UITabbedPane tabbedPane;
    private MapCustomPane areaPane;
    private MapDefiAreaNamePane namedPane;
    private String currentMapName;
    private AbstrctMapAttrEditPane editingPane;
    private ChangeListener tabbedChangeListener;

    public MapEditPane()
    {
        tabbedChangeListener = new ChangeListener() {

            final MapEditPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                switch(tabbedPane.getSelectedIndex())
                {
                case 1: // '\001'
                    namedPane.populateMapAttr(editingPane.updateCurrentAttr());
                    editingPane = namedPane;
                    break;

                default:
                    areaPane.populateMapAttr(editingPane.updateCurrentAttr());
                    editingPane = areaPane;
                    break;
                }
            }

            
            {
                this$0 = MapEditPane.this;
                super();
            }
        }
;
        initTabbedPane();
        setLayout(new BorderLayout());
        add(tabbedPane, "Center");
    }

    private void initTabbedPane()
    {
        tabbedPane = new UITabbedPane();
        areaPane = new MapCustomPane(false);
        namedPane = new MapDefiAreaNamePane(false);
        areaPane.setImageSelectType(1);
        tabbedPane.add(Inter.getLocText("FR-Chart-Map_ImageArea"), areaPane);
        tabbedPane.add(Inter.getLocText("FR-Chart-Map_Corresponding_Fields"), namedPane);
        editingPane = areaPane;
    }

    protected String title4PopupWindow()
    {
        return null;
    }

    public void populateBean(MapSvgAttr mapsvgattr)
    {
        if(!StringUtils.isEmpty(mapsvgattr.getName()) && !MapSvgXMLHelper.getInstance().containsMapName(mapsvgattr.getName()))
        {
            MapSvgAttr mapsvgattr1 = new MapSvgAttr();
            mapsvgattr1.setFilePath((new StringBuilder()).append(MapSvgXMLHelper.customMapPath()).append("/").append(mapsvgattr.getName()).append(".svg").toString());
            MapSvgXMLHelper.getInstance().addNewSvgMaps(mapsvgattr.getName(), mapsvgattr1);
        }
        currentMapName = mapsvgattr.getName();
        if(editingPane == null)
            editingPane = areaPane;
        editingPane.populateMapAttr(mapsvgattr);
        tabbedPane.addChangeListener(tabbedChangeListener);
    }

    public String getCurrentMapName()
    {
        return currentMapName;
    }

    public void setCurrentMapName(String s)
    {
        currentMapName = s;
    }

    public void dealWidthMap(String s)
    {
        areaPane.setTypeNameAndMapName(s, currentMapName);
    }

    public MapSvgAttr updateBean()
    {
        MapSvgAttr mapsvgattr = editingPane.updateCurrentAttr();
        currentMapName = mapsvgattr == null ? currentMapName : mapsvgattr.getName();
        MapSvgAttr mapsvgattr1 = MapSvgXMLHelper.getInstance().getMapAttr(currentMapName);
        if(mapsvgattr1 != null)
        {
            MapSvgXMLHelper.getInstance().removeNewMapAttr(currentMapName);
            MapSvgXMLHelper.getInstance().pushMapAttr(currentMapName, mapsvgattr);
            return mapsvgattr;
        } else
        {
            return MapSvgXMLHelper.getInstance().getNewMapAttr(currentMapName);
        }
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((MapSvgAttr)obj);
    }





}
