package com.fr.design.mainframe;

import com.fr.chart.base.MapSvgAttr;
import com.fr.chart.base.MapSvgXMLHelper;
import com.fr.chart.chartglyph.MapShapeValue;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.series.PlotSeries.AbstrctMapAttrEditPane;
import com.fr.design.chart.series.PlotSeries.MapCustomPane;
import com.fr.design.chart.series.PlotSeries.MapDefiAreaNamePane;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.general.Inter;
import com.fr.stable.CoreConstants;
import com.fr.stable.StringUtils;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 * Date: 14/12/2
 * Time: ����7:17
 */
public class MapEditPane extends BasicBeanPane<MapSvgAttr>{

    private UITabbedPane tabbedPane;
    private MapCustomPane areaPane ;
//    private MapCustomPane pointPane;
    private MapDefiAreaNamePane namedPane;
    private String currentMapName;
    private AbstrctMapAttrEditPane editingPane;

    private ChangeListener tabbedChangeListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            switch ( tabbedPane.getSelectedIndex()){
                case 1:
                    namedPane.populateMapAttr(editingPane.updateCurrentAttr());
                    editingPane = namedPane;
                    break;
                default:
                    areaPane.populateMapAttr(editingPane.updateCurrentAttr());
                    editingPane = areaPane;
            }
        }
    } ;

    public MapEditPane(){
        initTabbedPane();
        this.setLayout(new BorderLayout());
        this.add(tabbedPane,BorderLayout.CENTER);
    }

    private void initTabbedPane(){
        tabbedPane = new UITabbedPane();
        areaPane = new MapCustomPane(false);
//        pointPane = new MapCustomPane(false);
        namedPane= new MapDefiAreaNamePane(false);
        areaPane.setImageSelectType(MapShapeValue.AREA);
//        pointPane.setImageSelectType(MapShapeValue.POINT);
        tabbedPane.add(Inter.getLocText("FR-Chart-Map_ImageArea"),areaPane);
//        tabbedPane.add(Inter.getLocText("FR-Chart-Map_ImagePoint"),pointPane);
        tabbedPane.add(Inter.getLocText("FR-Chart-Map_Corresponding_Fields"),namedPane);
        editingPane = areaPane;
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }

    @Override
    public void populateBean(MapSvgAttr ob) {
        if(!StringUtils.isEmpty(ob.getName()) && !MapSvgXMLHelper.getInstance().containsMapName(ob.getName())){
            MapSvgAttr mapSvgAttr = new MapSvgAttr();
            mapSvgAttr.setFilePath(MapSvgXMLHelper.customMapPath()+ CoreConstants.SEPARATOR+ob.getName()+".svg");
            MapSvgXMLHelper.getInstance().addNewSvgMaps(ob.getName(), mapSvgAttr);
        }

        currentMapName = ob.getName();
        if(editingPane == null){
            editingPane = areaPane;
        }
        editingPane.populateMapAttr(ob);
        tabbedPane.addChangeListener(tabbedChangeListener);
    }

    public String getCurrentMapName(){
        return currentMapName;
    }

    public void setCurrentMapName(String currentMapName){
        this.currentMapName = currentMapName;

    }

    /**
   	 * ��ͼƬ����������ͺ�����
   	 * @param mapType ��ͼ����
   	 */
    public void dealWidthMap(String mapType){
        areaPane.setTypeNameAndMapName(mapType,currentMapName);
//        pointPane.setTypeNameAndMapName(mapType,currentMapName);
   	}

    @Override
    public MapSvgAttr updateBean() {
        MapSvgAttr currentAttr = editingPane.updateCurrentAttr();
        currentMapName =currentAttr != null ? currentAttr.getName() : currentMapName;
        MapSvgAttr attr =  MapSvgXMLHelper.getInstance().getMapAttr(currentMapName);
        if(attr != null ){
            MapSvgXMLHelper.getInstance().removeNewMapAttr(currentMapName);
            MapSvgXMLHelper.getInstance().pushMapAttr(currentMapName,currentAttr);
            return currentAttr;
        }else{
            return  MapSvgXMLHelper.getInstance().getNewMapAttr(currentMapName);
        }
    }
}
