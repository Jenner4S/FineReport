package com.fr.design.designer.properties.items;

import com.fr.general.Inter;
import com.fr.report.stable.FormConstants;

/**
 * Created with IntelliJ IDEA.
 * User: zx
 * Date: 14-8-13
 * Time: ����2:13
 */
public class WidgetDisplayPositionItems implements ItemProvider{
    //����Ϊ�˺�web��һ�£�ֻ�����ó�012��
    private static Item[] VALUE_ITEMS = {
            new Item(Inter.getLocText("StyleAlignment-Left"), FormConstants.LEFTPOSITION),
            new Item(Inter.getLocText("Center"), FormConstants.CENTERPOSITION),
            new Item(Inter.getLocText("StyleAlignment-Right"), FormConstants.RIGHTPOSITION)
    };

    @Override
    public Item[] getItems() {
        return VALUE_ITEMS;
    }
}
