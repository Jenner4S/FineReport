package com.fr.plugin.chart.pie;

import com.fr.base.GraphHelper;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.glyph.VanChartDataPoint;
import com.fr.stable.Constants;

import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Created by Mitisky on 15/8/20.
 */
public class VanChartPieDataPoint extends VanChartDataPoint {

    private static final long serialVersionUID = 5540426467114703655L;
    private GeneralPath leadLine;

    /**
     * 设置饼图系列点的牵引线.
     */
    public void setLeadLine(GeneralPath leadLine) {
        this.leadLine = leadLine;
    }

    /**
     * 返回饼图系列点的牵引线
     */
    public GeneralPath getLeadLine() {
        return leadLine;
    }

    /**
     * 画出系列点的标签
     */
    public void drawLabel(Graphics g, int resolution) {
        super.drawLabel(g, resolution);

        // 画牵引线
        if(this.getDataLabel() != null && this.getLeadLine() != null) {
            Color oldColor = g.getColor();
            Color backgroundColor = getDataPointBackgroundColor();
            if(backgroundColor != null){
                g.setColor(backgroundColor);
            }
            GraphHelper.draw(g, getLeadLine(), Constants.LINE_THIN);
            g.setColor(oldColor);
        }
    }

    protected void addXYJSON(JSONObject js) throws JSONException {
        js.put("x", getSeriesName());
        js.put("y", isValueIsNull() ? "-" : getValue());
    }
}
