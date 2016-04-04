package com.fr.plugin.chart.base;

import com.fr.chart.base.AttrBackground;

/**
 * 系列填充图片
 */
//这个只是为了区分AttrBackground。因为条件属性包含两个一样的class会失效一个
public class AttrSeriesImageBackground extends AttrBackground {
    public static final String XML_TAG = "AttrSeriesImageBackground";

    public boolean equals(Object ob) {
        return ob instanceof AttrSeriesImageBackground
                && super.equals(ob);
    }

    public String getConditionType() {
        return XML_TAG;
    }
}
