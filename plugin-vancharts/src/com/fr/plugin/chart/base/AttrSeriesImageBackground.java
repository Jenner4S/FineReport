package com.fr.plugin.chart.base;

import com.fr.chart.base.AttrBackground;

/**
 * ϵ�����ͼƬ
 */
//���ֻ��Ϊ������AttrBackground����Ϊ�������԰�������һ����class��ʧЧһ��
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
