package com.fr.plugin.chart.base;

import com.fr.chart.base.AttrColor;

/**
 * Created by Mitisky on 15/8/24.
 */
//���ֻ��Ϊ������AttrColor����Ϊ�������԰�������һ����class��ʧЧһ��

public class AttrFloatColor extends AttrColor {
    public static final String XML_TAG = "AttrFloatColor";

    public boolean equals(Object ob) {
        return ob instanceof AttrFloatColor
                && super.equals(ob);
    }

    public String getConditionType() {
        return XML_TAG;
    }
}
