package com.fr.design.data.datapane.preview;

import javax.swing.table.AbstractTableModel;

import com.fr.base.FRContext;
import com.fr.data.AbstractDataModel;
import com.fr.data.impl.EmbeddedTableData.EmbeddedTDDataModel;
import com.fr.data.impl.storeproc.ProcedureDataModel;
import com.fr.env.RemoteEnv;
import com.fr.general.Inter;
import com.fr.general.data.DataModel;
import com.fr.general.data.TableDataException;
import com.fr.cache.list.IntList;
import com.fr.stable.StringUtils;
import com.fr.design.utils.DesignUtils;

/**
 * ���TableModel��Ҫ��Ԥ�����ݵ�. �ֶ�TableData����ת��Ϊ���õ�
 */
public class PreviewTableModel extends AbstractTableModel {
    private DataModel dataModel;
    private String erroMessage = null;

    public IntList dateIndexs = new IntList(4);

    public PreviewTableModel(int maxRowCount) {
        // peter:Ĭ�ϱ�����ʾ���������Դ.
        this(new ErrorResultSet(), maxRowCount);
    }

    public PreviewTableModel(DataModel sourceResultSet, int maxRowCount) {
        if (sourceResultSet instanceof ProcedureDataModel) {
            ProcedureDataModel rs = (ProcedureDataModel) sourceResultSet;
            try {
                this.dataModel = createRowDataModel(rs, maxRowCount);
            } catch (TableDataException e) {
                // TODO Auto-generated catch block
                FRContext.getLogger().error(e.getMessage(), e);
            }
        } else {
            this.dataModel = sourceResultSet;
        }
    }

    public static DataModel createRowDataModel(final ProcedureDataModel rs, int maxRowCount) throws TableDataException {
        int rowCount = rs.getRowCount();
        if (maxRowCount == 0) {
            maxRowCount = rowCount;
        } else if (maxRowCount > rowCount) {
            maxRowCount = rowCount;
        }
        final int finalRowCount = maxRowCount;
        DataModel dm = new DataModel() {

            @Override
            public void release() throws Exception {
                rs.release();
            }

            @Override
            public boolean hasRow(int rowIndex) throws TableDataException {
                return rowIndex <= finalRowCount - 1 && rowIndex >= 0;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex)
                    throws TableDataException {
                return rs.getValueAt(rowIndex, columnIndex);
            }

            @Override
            public int getRowCount() throws TableDataException {
                return finalRowCount;
            }

            @Override
            public String getColumnName(int columnIndex) throws TableDataException {
                return rs.getColumnName(columnIndex);
            }

            @Override
            public int getColumnCount() throws TableDataException {
                return rs.getColumnCount();
            }
        };
        return dm;
    }

    public String getErrMessage() {
        return this.erroMessage;
    }

    public void setErrMessage(String err) {
        this.erroMessage = err;
    }

    public String getColumnName(int column) {
        try {
            return Integer.toString(column + 1) + ". " + dataModel.getColumnName(column) + checkType(column);
        } catch (TableDataException e) {
            FRContext.getLogger().error(e.getMessage(), e);
            DesignUtils.errorMessage(e.getMessage());
            return Inter.getLocText("Error");
        }
    }

    public int getRowCount() {
        try {
            return this.dataModel.getRowCount();
        } catch (TableDataException e) {
            FRContext.getLogger().error(e.getMessage(), e);
            return 0;
        }
    }

    public int getColumnCount() {
        try {
            if (dataModel == null) {
                return 0;
            }
            return dataModel.getColumnCount();
        } catch (TableDataException e) {
            FRContext.getLogger().error(e.getMessage(), e);
            DesignUtils.errorMessage(e.getMessage());
            return 0;
        }
    }

    public Object getValueAt(int row, int column) {
        try {
            return dataModel.getValueAt(row, column);
        } catch (TableDataException e) {
            FRContext.getLogger().error(e.getMessage(), e);
            DesignUtils.errorMessage(e.getMessage());
            return "";
        }
    }

    /*
     * peter:�������Ԥ����TableData��������,�����е���������һ��,�ΰ�.
     * ��������Ԥ����JTable�ڲ�ͣ��getRowCount����ʾ����.
     */
    private static class ErrorResultSet extends AbstractDataModel {
        public ErrorResultSet() {
        }

        public int getRowCount() {
            return 0;
        }

        public String getColumnName(int column) {
            return Inter.getLocText("Error");
        }

        public int getColumnCount() {
            return 1;
        }

        public Object getValueAt(int row, int column) {
            return "";
        }

        public void release() throws Exception {
        }
    }

    private String checkType(int column) {
        if (dateIndexs.contain(column)) {
            String s = Inter.getLocText("Date");
            return ("(" + s + ")");
        }

        String s = StringUtils.EMPTY;
        Object o = null;
        try {
            for (int i = 0; i < dataModel.getRowCount(); i++) {
                o = dataModel.getRowCount() <= 0 ? null : dataModel.getValueAt(i, column);

                if (o != null && StringUtils.isNotEmpty(o.toString())) {
                    break;
                }
            }
        } catch (TableDataException e) {
            return ("(?)");
        }

        if (o == null) {
            s = "?";
        } else if (o instanceof String) {
            s = Inter.getLocText("Parameter-String");
            if (FRContext.getCurrentEnv() instanceof RemoteEnv && dataModel instanceof EmbeddedTDDataModel) {
                Class clzz = ((EmbeddedTDDataModel) dataModel).getColumnClass(column);
                if (Number.class.isAssignableFrom(clzz)) {
                    s = Inter.getLocText("Number");//bigdecimal
                } else if (java.sql.Date.class.isAssignableFrom(clzz)) {
                    s = Inter.getLocText("Date");
                }
            }
        } else if (o instanceof Integer) {
            s = Inter.getLocText("Integer");
        } else if (o instanceof Double || o instanceof Float) {
            s = Inter.getLocText("Double");
        } else if (o instanceof java.sql.Date || o instanceof java.util.Date) {
            s = Inter.getLocText("Date");
        } else if (o instanceof Number) {
            s = Inter.getLocText("Number");//bigdecimal
        } else {
            s = "?";
        }
        return ("(" + s + ")");
    }
}
