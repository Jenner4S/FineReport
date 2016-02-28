package com.fr.plugin.chart.attr;

import com.fr.chart.chartglyph.Marker;
import com.fr.general.ComparatorUtils;
import com.fr.plugin.chart.glyph.marker.*;

/**
 * Created by Mitisky on 15/11/12.
 */
public enum MarkerType {
    MARKER_NULL(new VanChartNullMarker()),
    MARKER_CIRCLE(new VanChartRoundFilledMarker()),
    MARKER_SQUARE(new VanChartSquareFilledMarker()),
    MARKER_DIAMOND(new VanChartDiamondFilledMarker()),
    MARKER_TRIANGLE(new VanChartTriangleFilledMarker()),
    MARKER_CIRCLE_HOLLOW(new VanChartRoundMarker()),
    MARKER_SQUARE_HOLLOW(new VanChartSquareMarker()),
    MARKER_DIAMOND_HOLLOW(new VanChartDiamondMarker()),
    MARKER_TRIANGLE_HOLLOW(new VanChartTriangleMarker())
    ;

    private Marker marker;

    private MarkerType(Marker marker){
        this.marker = marker;
    }

    public String getType() {
        return this.marker.getMarkerType();
    }

    private static MarkerType[] types;

    private static MarkerType[] getTypes() {
        if(types == null){
            types = MarkerType.values();
        }
        return types;
    }

    public static MarkerType parse(String type){

        for(MarkerType markerType : MarkerType.getTypes()){
            if(ComparatorUtils.equals(markerType.marker.getMarkerType(), type)){
                return markerType;
            }
        }
        return MARKER_NULL;
    }

    private static Marker[] markers = null;

    public static Marker[] getMarkers() {
        if(markers == null){
            markers = new Marker[getTypes().length];
            int i = 0;
            for(MarkerType markerType : getTypes()){
                markers[i++] = markerType.getMarker();
            }
        }
        return markers;
    }

    public Marker getMarker(){
        try{
            return (Marker)this.marker.clone();
        }catch (CloneNotSupportedException e){
            return new VanChartNullMarker();
        }
    }
}
