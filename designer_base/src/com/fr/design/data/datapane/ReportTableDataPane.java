/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.data.datapane;

import com.fr.data.TableDataSource;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.frpane.LoadingBasicPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * @author richer
 * @since 6.5.5
 * ������2011-6-14
 */
public class ReportTableDataPane extends LoadingBasicPane {
    private TableDataListPane tdListPane;

    @Override
    protected void initComponents(JPanel container) {
        container.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.tdListPane = new TableDataListPane() {
            @Override
            public NameableCreator[] createNameableCreators() {

                return TableDataCreatorProducer.getInstance().createReportTableDataCreator();
            }
        };
        container.add(tdListPane, BorderLayout.CENTER);
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("DS-Report_TableData");
    }

    public void populate(TableDataSource tds) {
        tdListPane.populate(tds);
    }

    public void update(TableDataSource tds) {
        tdListPane.update(tds);
    }

    /**
     * ���tdListPane��Ч��check Valid
     *
     * @throws Exception �쳣
     */
    public void checkValid() throws Exception {
        this.tdListPane.checkValid();
    }

    public Map<String, String> getDsNameChangedMap() {
        return tdListPane.getDsNameChangedMap();
    }
}

