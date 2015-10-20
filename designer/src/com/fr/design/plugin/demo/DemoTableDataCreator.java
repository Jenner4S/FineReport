package com.fr.design.plugin.demo;

import com.fr.base.TableData;
import com.fr.data.impl.UnionTableData;
import com.fr.design.data.tabledata.tabledatapane.AbstractTableDataPane;
import com.fr.design.data.tabledata.tabledatapane.MultiTDTableDataPane;
import com.fr.design.fun.TableDataDefineProvider;

/**
 * @author richie
 * @date 15/1/12
 * @since 8.0
 */
public class DemoTableDataCreator implements TableDataDefineProvider {
    @Override
    public Class<? extends TableData> classForTableData() {
        return UnionTableData.class;
    }

    @Override
    public Class<? extends TableData> classForInitTableData() {
        return UnionTableData.class;
    }

    @Override
    public Class<? extends AbstractTableDataPane> appearanceForTableData() {
        return MultiTDTableDataPane.class;
    }

    @Override
    public String nameForTableData() {
        return "demo";
    }

    @Override
    public String prefixForTableData() {
        return "demo";
    }

    @Override
    public String iconPathForTableData() {
        return "/com/fr/design/images/data/cube.png";
    }
}
