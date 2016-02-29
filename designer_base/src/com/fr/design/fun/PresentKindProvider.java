package com.fr.design.fun;

import com.fr.base.present.Present;
import com.fr.design.beans.FurtherBasicBeanPane;

/**
 * @author richie
 * @date 2015-05-22
 * @since 8.0
 * 形态类型接口
 */
public interface PresentKindProvider {

    String MARK_STRING = "PresentKindProvider";

    /**
     * 形态设置界面
     * @return 形态设置界面
     */
    FurtherBasicBeanPane<? extends Present> appearanceForPresent();

    /**
     * 在形态设置面板上显示的名字
     * @return 名字
     */
    String title();

    /**
     * 该形态对应的类
     * @return 类
     */
    Class<? extends Present> kindOfPresent();

    /**
     * 菜单快捷键
     * @return 快捷点对应的字符
     */
    char mnemonic();
}