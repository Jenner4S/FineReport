package com.fr.design.menu;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;

import com.fr.design.gui.itoolbar.UIToolbar;

/**
 * Define toolbar..
 */
public class ToolBarDef  {
	
    // item List.
    private List<ShortCut> shortcutList = new ArrayList<ShortCut>();
    
    /*
     * 一个static的方法生成一个JToolBar
     */
    public static UIToolbar createJToolBar() {
    	UIToolbar toolbar = new UIToolbar(FlowLayout.LEFT);
    	toolbar.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
    	return toolbar;
    }

    public ToolBarDef() {
    }

    public int getShortCutCount() {
        return this.shortcutList.size();
    }

    public ShortCut getShortCut(int index) {
        return this.shortcutList.get(index);
    }

//  用可变参数，方便添加数组
    public void addShortCut(ShortCut... shortcut) {
    	for(ShortCut i : shortcut){
    		this.shortcutList.add(i);
    	}
    }
    
    public void clearShortCuts() {
        this.shortcutList.clear();
    }

    /*
     * 根据当前的ToolBarDef,更新toolBar
     */
    public void updateToolBar(UIToolbar toolBar) {
        toolBar.removeAll();
        
        int actionCount = this.getShortCutCount();
        for (int i = 0; i < actionCount; i++) {
        	ShortCut shortcut = this.getShortCut(i);
        	shortcut.intoJToolBar(toolBar);
        }
    }

}