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

    private boolean hidden = true;//ͼ���Ƿ������ڲ˵���ť�ڣ�Ĭ��true
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
     * ����ͼƬ
     * @return ����ͼƬ
     */
    public boolean isExport() {
        return export;
    }

    /**
     * ȫ��
     * @return ȫ��
     */
    public boolean isFullScreen() {
        return fullScreen;
    }

    /**
     * ����˵�
     * @return ����˵�
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * ����
     * @return ����
     */
    public boolean isSort() {
        return sort;
    }

    /**
     *  дXML
     * @param writer XML�������
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
     * ��ȡXML����
     * @param reader XML��ȡ��
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
     * �ȽϺ�Object�Ƿ����
     * @param ob �� �ڱȽϵ�Object
     * @return һ��booleanֵ
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
     * ����json����
     * @return js����
     * @throws JSONException �״�
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
