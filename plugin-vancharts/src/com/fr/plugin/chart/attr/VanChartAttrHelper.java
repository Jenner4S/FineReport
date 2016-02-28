package com.fr.plugin.chart.attr;

import com.fr.base.CoreDecimalFormat;
import com.fr.base.Utils;
import com.fr.base.background.ColorBackground;
import com.fr.base.background.GradientBackground;
import com.fr.base.background.ImageBackground;
import com.fr.chart.base.ChartBaseUtils;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartglyph.DataPoint;
import com.fr.chart.chartglyph.GeneralInfo;
import com.fr.general.Background;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.attr.axis.AxisTickLineType;
import com.fr.plugin.chart.attr.axis.TimeType;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.attr.axis.VanChartValueAxis;
import com.fr.plugin.chart.base.AttrSeriesImageBackground;
import com.fr.plugin.chart.base.AttrTooltipContent;
import com.fr.plugin.chart.base.VanChartConstants;
import com.fr.stable.Constants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.web.Repository;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitisky on 15/8/18.
 */
public class VanChartAttrHelper {
    public static final double PERCENT = 100.0;
    public static final String STRING_PERCENT = "%";
    public static final DecimalFormat PERCENT_FORMAT = new CoreDecimalFormat(new DecimalFormat("#.##%"), "#.##%");
    public static final DecimalFormat VALUE_FORMAT = new CoreDecimalFormat(new DecimalFormat("#.##"), "#.##");
    private static final String BREAK = ",";
    private static final String COLON = ":";
    public static final String NEWLINE = "\n";
    public static final double HALF_ALPHA = 0.5;
    public static final String PT = "pt";
    public static final String BOLD = "bold";
    public static final String ITALIC = "italic";
    //标签设置为自动时。仅用于计算，不用于画。
    public static final FRFont DEFAULT_LABEL_FONT = FRFont.getInstance("verdana", Font.BOLD, 9, Color.black);
    public static final FRFont DEFAULT_AXIS_TITLE_LABLE_FONT = FRFont.getInstance("verdana", Font.PLAIN, 11, new Color(102,102,102));
    public static final String TRANSPARENT_COLOR = "rgba(255,255,255,0)";
    private static final double DEFAULT_GRADIENT_VALUE_MIN = 0;
    private static final double DEFAULT_GRADIENT_VALUE_MAX = 1;
    public static final String X_AXIS_PREFIX = Inter.getLocText("ChartF-X_Axis");
    public static final String Y_AXIS_PREFIX = Inter.getLocText("ChartF-Y_Axis");
    public static final Color DEFAULT_MAIN_GRID_COLOR = new Color(196,196,196);
    public static final FRFont DEFAULT_RADAR_AXIS_LABLE_FONT = FRFont.getInstance("verdana", Font.PLAIN, 9, new Color(102,102,102));

    public static Format getNotNullValueFormat(Format format) {
        return format == null ? VALUE_FORMAT : format;
    }

    public static Background getNotNullBackground(Background background) {
        return background == null ? ColorBackground.getInstance(Color.WHITE) : background;
    }

    public static String getStringColor(Color color) {
        return color == null ? TRANSPARENT_COLOR : StableUtils.javaColorToCSSColor(color);
    }

    public static String getRGBAColorWithColorAndAlpha(Color color, double alpha){
        return getRGBAColorWithColorAndAlpha(color, (float)alpha);
    }

    public static String getRGBAColorWithColorAndAlpha(Color color, float alpha){
        color = color == null ? new Color(255,255,255,0) : new Color(color.getRed(),color.getGreen(),color.getBlue(),(int)(alpha * 255));
        return StableUtils.javaColor2JSColorWithAlpha(color);
    }

    public static JSONObject getGradientBackgroundJSON(GradientBackground gradientBackground, float alpha) throws JSONException{
        JSONObject js = new JSONObject();
        js.put("x1", DEFAULT_GRADIENT_VALUE_MIN);
        js.put("y1", DEFAULT_GRADIENT_VALUE_MIN);
        if(gradientBackground.getDirection() == 0){
            //横向
            js.put("x2", DEFAULT_GRADIENT_VALUE_MAX);
            js.put("y2", DEFAULT_GRADIENT_VALUE_MIN);
        } else {
            //纵向
            js.put("x2", DEFAULT_GRADIENT_VALUE_MIN);
            js.put("y2", DEFAULT_GRADIENT_VALUE_MAX);
        }
        js.put("startColor", VanChartAttrHelper.getRGBAColorWithColorAndAlpha(gradientBackground.getStartColor(), alpha));
        js.put("endColor", VanChartAttrHelper.getRGBAColorWithColorAndAlpha(gradientBackground.getEndColor(), alpha));
        return js;
    }

    public static String getImageBackground(ImageBackground imageBackground, Repository repo) {
        Image image = imageBackground.getImage();
        if(image == null) {
            return StringUtils.EMPTY;
        }
        String embID = ChartBaseUtils.addImageAsEmb(image);
        return repo.getServletURL() + "?op=fr_attach&cmd=ah_image&id=" + embID;
    }

    /**
     * 添加base64 image、width、height到json。
     * @param js json对象
     * @param attrSeriesImageBackground 图片
     * @param repo 待说明
     * @throws JSONException 抛错
     */
    public static void addImageBackgroundAndWidthAndHeight(JSONObject js, AttrSeriesImageBackground attrSeriesImageBackground, Repository repo) throws JSONException{
        ImageBackground imageBackground = (ImageBackground)attrSeriesImageBackground.getSeriesBackground();
        if(imageBackground == null || imageBackground.getImage() == null){
            return;
        }
        int width = imageBackground.getImage().getWidth(new JPanel());
        int height = imageBackground.getImage().getHeight(new JPanel());
        js.put("image",VanChartAttrHelper.getImageBackground(imageBackground, repo));
        js.put("imageWidth", width);
        js.put("imageHeight", height);
    }

    public static JSONObject getCSSFontJSONWithFont(FRFont font) throws JSONException{
        JSONObject js = new JSONObject();
        if(font == null){
            return js;
        }
        js.put("fontFamily", font.getFamily());
        js.put("fontSize", font.getSize() + PT);
        js.put("color", StableUtils.javaColor2JSColorWithAlpha(font.getForeground()));
        if(StringUtils.contains(font.getStyleName(), Inter.getLocText("FR-Designer-FRFont_Bold"))){
            js.put("fontWeight", BOLD);
        } else {
            js.put("fontWeight", StringUtils.EMPTY);
        }
        if(StringUtils.contains(font.getStyleName(), Inter.getLocText("FR-Designer-FRFont_Italic"))){
            js.put("fontStyle", ITALIC);
        }
        return js;
    }

    public static int getAxisLineStyle(int lineStyle) {
        switch (lineStyle) {
            case Constants.LINE_THICK:
                return 3;
            case Constants.LINE_CHART_THIN_ARROW:
                return 1;
            case Constants.LINE_CHART_MED_ARROW:
                return 2;
            case Constants.LINE_CHART_THICK_ARROW:
                return 3;
            default:
                return lineStyle;
        }
    }

    /**
     * 添加线型宽度和虚线属性到js
     * @param js json对象
     * @param lineStyle 线型
     * @throws JSONException 抛错
     */
    public static void addDashLineStyleJSON(JSONObject js, int lineStyle) throws JSONException{
        int width = lineStyle;
        boolean dash = false;
        switch (lineStyle){
            case Constants.LINE_THICK:
                width = 3;
                break;
            case Constants.LINE_DOT:
                width = 1;
                dash = true;
                break;
            case Constants.LINE_DASH:
                width = 2;
                dash = true;
                break;
            case Constants.LINE_MEDIUM_DASH:
                width = 3;
                dash = true;
                break;
        }
        js.put("width", width);
        if(dash){
            js.put("dashStyle", "Dash");
        }
    }

    public static String getPlotStyle(int plotStyle) {
        switch (plotStyle){

            case ChartConstants.STYLE_SHADE:
                return "gradual";
            case ChartConstants.STYLE_NONE:
                return "normal";
            default:
                return "normal";
        }
    }

    public static String getLabelText(AttrTooltipContent tooltipContent, DataPoint dataPoint) {
        if(tooltipContent.isCommon()){
            return getCateAndSeries(tooltipContent, dataPoint) + getValueAndPercent(tooltipContent, dataPoint);
        }
        //自定义，设计器这边不解析，不画标签
        return StringUtils.EMPTY;
    }

    public static String getCateAndSeries(AttrTooltipContent tooltipContent, DataPoint dataPoint) {
        String cateAndSeriesText = StringUtils.EMPTY;
        if(tooltipContent.isCategoryName()){
            cateAndSeriesText += dataPoint.getCategoryName();
        }
        if(tooltipContent.isSeriesName()){
            cateAndSeriesText += tooltipContent.isCategoryName() ? StringUtils.BLANK : StringUtils.EMPTY;
            cateAndSeriesText += dataPoint.getSeriesName();
        }
        return cateAndSeriesText;
    }

    public static String getValueAndPercent(AttrTooltipContent tooltipContent, DataPoint dataPoint) {
        String valueAndPercentText = StringUtils.EMPTY;
        if(tooltipContent.isValue()){
            valueAndPercentText += (tooltipContent.isCategoryName() || tooltipContent.isSeriesName()) ? NEWLINE : StringUtils.EMPTY;
            valueAndPercentText += getValueStringWithFormat(dataPoint.getValue(), tooltipContent.getFormat());
        }
        if(tooltipContent.isPercentValue()){
            if(tooltipContent.isValue()){
                valueAndPercentText += StringUtils.BLANK;
            } else {
                valueAndPercentText += tooltipContent.isCategoryName() || tooltipContent.isSeriesName() ? NEWLINE :StringUtils.EMPTY;
            }
            valueAndPercentText += getValueStringWithFormat(dataPoint.getPercentValue(), tooltipContent.getPercentFormat());
        }
        return valueAndPercentText;
    }


    public static String getCategoryName(AttrTooltipContent tooltipContent, DataPoint dataPoint) {
        if(tooltipContent.isCommon() && tooltipContent.isCategoryName()){
            return dataPoint.getCategoryName();
        }
        return StringUtils.EMPTY;
    }

    public static String getPercent(AttrTooltipContent tooltipContent, DataPoint dataPoint) {
        if(tooltipContent.isCommon() && tooltipContent.isPercentValue()){
            return getValueStringWithFormat(dataPoint.getPercentValue(), tooltipContent.getPercentFormat());
        }
        return StringUtils.EMPTY;
    }

    public static String getLabelWithOutCategory(AttrTooltipContent tooltipContent, DataPoint dataPoint) {
        String str = StringUtils.EMPTY;
        if(tooltipContent.isCommon()){
            if(tooltipContent.isSeriesName()){
                str += dataPoint.getSeriesName();
            }
            if(tooltipContent.isValue()){
                str += tooltipContent.isSeriesName() ?  COLON: StringUtils.EMPTY;
                str += getValueStringWithFormat(dataPoint.getValue(), tooltipContent.getFormat());
            }
            if(tooltipContent.isPercentValue()){
                str += (tooltipContent.isSeriesName() || tooltipContent.isValue()) ? StringUtils.BLANK : StringUtils.EMPTY;
                str += getValueStringWithFormat(dataPoint.getPercentValue(), tooltipContent.getPercentFormat());
            }
        }
        return str;
    }

    public static String getCateAndValue(AttrTooltipContent tooltipContent, DataPoint dataPoint) {
        String cateAndValue = StringUtils.EMPTY;
        if(tooltipContent.isCommon()){
            if(tooltipContent.isCategoryName()){
                cateAndValue += dataPoint.getCategoryName();
            }
            if(tooltipContent.isValue()){
                cateAndValue += tooltipContent.isCategoryName() ? COLON : StringUtils.EMPTY;
                cateAndValue += getValueStringWithFormat(dataPoint.getValue(), tooltipContent.getFormat());
            }
        }
        return cateAndValue;
    }

    public static String getSlotCateAndValue(AttrTooltipContent tooltipContent, DataPoint dataPoint) {
        String cateAndValue = StringUtils.EMPTY;
        if(tooltipContent.isCommon()){
            if(tooltipContent.isCategoryName()){
                cateAndValue += dataPoint.getCategoryName();
            }
            if(tooltipContent.isValue()){
                cateAndValue += tooltipContent.isCategoryName() ? NEWLINE : StringUtils.EMPTY;
                cateAndValue += getValueStringWithFormat(dataPoint.getValue(), tooltipContent.getFormat());
            }
        }
        return cateAndValue;
    }

    public static String getValueStringWithFormat(double value, Format format) {
        Object valueString;
        format = getNotNullValueFormat(format);
        valueString = format.format(value);

        return Utils.objectToString(valueString);
    }

    /**
     * 数据点提示默认背景
     * @return 数据点提示默认背景
     */
    public static GeneralInfo createDefaultTooltipBackground() {
        GeneralInfo generalInfo = new GeneralInfo();
        ColorBackground colorBackground = ColorBackground.getInstance(Color.black);
        generalInfo.setBackground(colorBackground);
        generalInfo.setAlpha((float)HALF_ALPHA);
        generalInfo.setRoundRadius(2);
        return generalInfo;
    }

    /**
     * 是否是X坐标轴
     * @param name 坐标轴名称
     * @return 是否是X坐标轴
     */
    public static boolean isXAxis(String name) {
        return StringUtils.contains(name, VanChartAttrHelper.X_AXIS_PREFIX);
    }

    /**
     * 是否是默认坐标轴
     * @param name 坐标轴名称
     * @return 是否是默认坐标轴
     */
    public static boolean isDefaultAxis(String name) {
        return ComparatorUtils.equals(name, VanChartAttrHelper.X_AXIS_PREFIX) || ComparatorUtils.equals(name, VanChartAttrHelper.Y_AXIS_PREFIX);
    }

    /**
     * 创建默认X坐标轴
     * @return 创建默认X坐标轴
     */
    public static List<VanChartAxis> createDefaultXAxisList() {
        List<VanChartAxis> xList = new ArrayList<VanChartAxis>();
        VanChartAxis axis = new VanChartAxis(VanChartAttrHelper.X_AXIS_PREFIX, VanChartConstants.AXIS_BOTTOM);
        xList.add(axis);
        return xList;
    }

    /**
     * 创建默认Y坐标
     * @return 创建默认Y坐标
     */
    public static List<VanChartAxis> createDefaultYAxisList() {
        List<VanChartAxis> yList = new ArrayList<VanChartAxis>();
        VanChartValueAxis axis = new VanChartValueAxis(VanChartAttrHelper.Y_AXIS_PREFIX, VanChartConstants.AXIS_LEFT);
        axis.setAxisStyle(Constants.LINE_NONE);
        axis.setMainTickLine(AxisTickLineType.TICK_LINE_NONE);
        yList.add(axis);
        return yList;
    }

    /**
     * 创建雷达图Y坐标轴数组
     * @return 创建默认Y坐标
     */
    public static List<VanChartAxis> createRadarYAxisList() {
        List<VanChartAxis> yList = new ArrayList<VanChartAxis>();
        VanChartValueAxis axis = new VanChartValueAxis(VanChartAttrHelper.Y_AXIS_PREFIX, VanChartConstants.AXIS_LEFT);
        axis.setMainGridColor(DEFAULT_MAIN_GRID_COLOR);
        axis.getTextAttr().setFRFont(DEFAULT_RADAR_AXIS_LABLE_FONT);
        yList.add(axis);
        return yList;
    }

    /**
     * 创建雷达图X坐标轴数组
     * @return 创建默认X坐标
     */
    public static List<VanChartAxis> createRadarXAxisList() {
        List<VanChartAxis> xList = new ArrayList<VanChartAxis>();
        VanChartAxis axis = new VanChartAxis(VanChartAttrHelper.X_AXIS_PREFIX, VanChartConstants.AXIS_BOTTOM);
        axis.getTextAttr().setFRFont(DEFAULT_RADAR_AXIS_LABLE_FONT);
        xList.add(axis);
        return xList;
    }

    /**
     * 创建自定义柱形图的默认y轴2
     * @return 轴
     */
    public static VanChartAxis createDefaultY2Axis() {
        VanChartValueAxis axis = new VanChartValueAxis(VanChartAttrHelper.Y_AXIS_PREFIX + "2", VanChartConstants.AXIS_RIGHT);
        axis.setAxisStyle(Constants.LINE_NONE);
        axis.setMainTickLine(AxisTickLineType.TICK_LINE_NONE);
        return axis;
    }

    /**
     * 创建条形图默认X坐标轴
     * @return 创建默认X坐标轴
     */
    public static List<VanChartAxis> createDefaultBarXAxisList() {
        List<VanChartAxis> xList = new ArrayList<VanChartAxis>();
        VanChartValueAxis axis = new VanChartValueAxis(VanChartAttrHelper.X_AXIS_PREFIX, VanChartConstants.AXIS_BOTTOM);
        axis.setAxisStyle(Constants.LINE_NONE);
        axis.setMainTickLine(AxisTickLineType.TICK_LINE_NONE);
        xList.add(axis);
        return xList;
    }

    /**
     * 创建条形图默认Y坐标
     * @return 创建默认Y坐标
     */
    public static List<VanChartAxis> createDefaultBarYAxisList() {
        List<VanChartAxis> yList = new ArrayList<VanChartAxis>();
        VanChartAxis axis = new VanChartAxis(VanChartAttrHelper.Y_AXIS_PREFIX, VanChartConstants.AXIS_LEFT);
        yList.add(axis);
        return yList;
    }

    /**
     * 创建自定义条形图的默认x轴2
     * @return 轴
     */
    public static VanChartAxis createDefaultBarX2Axis() {
        VanChartValueAxis axis = new VanChartValueAxis(VanChartAttrHelper.X_AXIS_PREFIX + "2", VanChartConstants.AXIS_TOP);
        axis.setAxisStyle(Constants.LINE_NONE);
        axis.setMainTickLine(AxisTickLineType.TICK_LINE_NONE);
        return axis;
    }


    public static String[] getAllAxisNames(List<VanChartAxis> xAxisList, List<VanChartAxis> yAxisList) {
        int xSize = xAxisList.size(), ySize = yAxisList.size();
        String[] axisNames = new String[xSize + ySize];
        for(int i = 0; i < xSize; i++){
            axisNames[i] = xAxisList.get(i).getAxisName();
        }
        for(int i = 0; i < ySize; i++){
            axisNames[i + xSize] = yAxisList.get(i).getAxisName();
        }
        return axisNames;
    }

    public static String[] getAxisNames(List<VanChartAxis> xAxisList){
        int xSize = xAxisList.size();
        String[] axisNames = new String[xSize];
        for(int i = 0; i < xSize; i++){
            axisNames[i] = xAxisList.get(i).getAxisName();
        }
        return axisNames;
    }

    public static long getTimeTypeSecond(TimeType timeType){
        return getTimeTypeMilliSecond(timeType) / ChartConstants.SECOND_2_MILLISECOND;
    }

    public static long getTimeTypeMilliSecond(TimeType timeType) {
        switch (timeType){
            case TIME_SECOND:
                return ChartConstants.SECOND_2_MILLISECOND;
            case TIME_YEAR:
                return ChartConstants.YEAR_2_MILLISECOND;
            case TIME_MINUTE:
                return ChartConstants.MINUTE_2_MILLISECOND;
            case TIME_HOUR:
                return ChartConstants.HOUR_2_MILLISECOND;
            case TIME_MONTH:
                return ChartConstants.MONTH_2_MILLISECOND;
            case TIME_DAY:
                return ChartConstants.DAY_2_MILLISECOND;
            default:
                return ChartConstants.DAY_2_MILLISECOND;
        }
    }

    //最大最小值和主要刻度单位就是以主要刻度类型（没有设置的时候默认为秒）为标准确认值的。主要刻度类型为天。月、年时，以天为标准。
    public static long getTimeTypeMilliSecond4MinMax(TimeType timeType) {
        switch (timeType){
            case TIME_SECOND:
                return ChartConstants.SECOND_2_MILLISECOND;
            case TIME_MINUTE:
                return ChartConstants.MINUTE_2_MILLISECOND;
            case TIME_HOUR:
                return ChartConstants.HOUR_2_MILLISECOND;
            default:
                return ChartConstants.DAY_2_MILLISECOND;
        }
    }

    public static String getMarkerType(MarkerType markerType) {
        switch (markerType){
            case MARKER_NULL:
                return "null_marker";
            case MARKER_CIRCLE:
                return "circle";
            case MARKER_CIRCLE_HOLLOW:
                return "circle_hollow";
            case MARKER_DIAMOND:
                return "diamond";
            case MARKER_DIAMOND_HOLLOW:
                return "diamond_hollow";
            case MARKER_SQUARE:
                return "square";
            case MARKER_SQUARE_HOLLOW:
                return "square_hollow";
            case MARKER_TRIANGLE:
                return "triangle";
            case MARKER_TRIANGLE_HOLLOW:
                return "triangle_hollow";
            default:
                return "null_marker";
        }
    }

}
