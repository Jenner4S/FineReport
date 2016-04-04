package com.fr.plugin.chart.attr;

import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLable;
import com.fr.stable.xml.XMLableReader;

/**
 * Created by Mitisky on 15/8/24.
 */
public class VanChartTools implements XMLable {

    private static final long serialVersionUID = -3005147918987340938L;
    public static final String XML_TAG = "tools";

    private boolean hidden = true;//图标是否隐藏在菜单按钮内，默认true
    private boolean sort = true;
    private boolean export = true;
    private boolean fullScreen = true;

    public void setExport(boolean export) {
        this.export = export;
    }

    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setSort(boolean sort) {
        this.sort = sort;
    }

    /**
     * 导出图片
     * @return 导出图片
     */
    public boolean isExport() {
        return export;
    }

    /**
     * 全屏
     * @return 全屏
     */
    public boolean isFullScreen() {
        return fullScreen;
    }

    /**
     * 抽屉菜单
     * @return 抽屉菜单
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * 排序
     * @return 排序
     */
    public boolean isSort() {
        return sort;
    }

    /**
     *  写XML
     * @param writer XML属性输出
     */
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(XML_TAG);
        writer.attr("hidden", hidden)
                .attr("sort", sort)
                .attr("export", export)
                .attr("fullScreen", fullScreen)
                .end();

    }

    /**
     * 读取XML属性
     * @param reader XML读取器
     */
    public void readXML(XMLableReader reader) {
        if (reader.isAttr()){
            hidden = reader.getAttrAsBoolean("hidden", true);
            sort = reader.getAttrAsBoolean("sort", true);
            export = reader.getAttrAsBoolean("export", true);
            fullScreen = reader.getAttrAsBoolean("fullScreen", true);
        }

    }

    /**
     * 比较和Object是否相等
     * @param ob 用 于比较的Object
     * @return 一个boolean值
     */
    public boolean equals(Object ob) {
        return ob instanceof VanChartTools
                && ((VanChartTools) ob).isSort() == isSort()
                && ((VanChartTools) ob).isExport() == isExport()
                && ((VanChartTools) ob).isFullScreen() == isFullScreen()
                && ((VanChartTools) ob).isHidden() == isHidden();
    }

    public Object clone() throws CloneNotSupportedException {
        VanChartTools newTools = (VanChartTools)super.clone();
        newTools.setExport(isExport());
        newTools.setSort(isSort());
        newTools.setHidden(isHidden());
        newTools.setFullScreen(isFullScreen());

        return newTools;
    }

    /**
     * 返回json对象
     * @return js对象
     * @throws JSONException 抛错
     */
    public JSONObject toJSONObject() throws JSONException {
        JSONObject js = new JSONObject();
        boolean enabled = sort || export || fullScreen;
        if(enabled){
            js.put("hidden", hidden);
            js.put("enabled", true);

            js.put("sort", new JSONObject().put("enabled", sort));
            js.put("toImage", new JSONObject().put("enabled", export));
            js.put("fullScreen", new JSONObject().put("enabled", fullScreen));
        }
        return js;
    }
}
