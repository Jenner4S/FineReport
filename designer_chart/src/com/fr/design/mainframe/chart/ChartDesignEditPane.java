/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe.chart;

import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.mainframe.chart.gui.*;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-15
 * Time: ����5:47
 */
public class ChartDesignEditPane extends ChartEditPane {

    private static ChartDesignEditPane instance;

    private boolean isFromToolBar = false;

    public synchronized static ChartEditPane getInstance() {
        if (instance == null) {
            instance = new ChartDesignEditPane();
        }
        return instance;
    }

    public ChartDesignEditPane() {
        paneList = new ArrayList<AbstractChartAttrPane>();
        dataPane4SupportCell = new ChartDesignerDataPane(listener);
        paneList.add(dataPane4SupportCell);
        paneList.add(new StylePane4Chart(listener, false));
        paneList.add(new ChartDesignerOtherPane());
        createTabsPane();
    }

    protected void dealWithStyleChange(){
        if(!isFromToolBar){
            HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().styleChange();
        }
    }

    /**
     *��Ҫ����ͼ����������ж���ʽ�ı��Ƿ����Թ�������ȫ����ʽ��ť
     * @param isFromToolBar �Ƿ����Թ�����
     */
    public void styleChange(boolean isFromToolBar){
        this.isFromToolBar = isFromToolBar;
    }

}

