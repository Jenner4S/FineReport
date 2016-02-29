package com.fr.plugin.designer;

import com.fr.general.ComparatorUtils;

import java.awt.event.KeyEvent;

/**
 * Created by vito on 2015/9/23
 */
public class ActionTreeSearch {
    /**
     * 快捷键
     * @param e 按键事件
     */
        public static void click(KeyEvent e) {
        String newkeypresscode = (e.isControlDown() ? "Ctrl+" :
                "") + (
                e.isAltDown() ? "Alt+" : "") + (
                e.isShiftDown() ? "Shift+" : "") +
                KeyEvent.getKeyText(e.getKeyCode());
        if (ComparatorUtils.equals(newkeypresscode,"Ctrl+R")) {
            DataConfig(e);
        }
    }

    /**
     *搜索按钮的快捷键响应
     * @param e 按键事件
     */
    public static void DataConfig(KeyEvent e) {
        new SearchTemplateAction().actionPerformed(null);
    }
}