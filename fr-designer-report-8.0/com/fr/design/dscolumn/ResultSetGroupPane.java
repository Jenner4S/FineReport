// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.dscolumn;

import com.fr.design.data.DesignTableDataManager;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.mainframe.*;
import com.fr.general.Inter;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.core.group.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.dscolumn:
//            SpecifiedGroupAttrPane

public abstract class ResultSetGroupPane extends JPanel
{

    protected static final int COMMON = 0;
    protected static final int CONTINUUM = 1;
    protected static final int ADVANCED = 2;
    protected TemplateCellElement cellElement;
    protected RecordGrouper recordGrouper;
    protected UIComboBox groupComboBox;
    ActionListener groupAdvancedListener;

    protected ResultSetGroupPane()
    {
        groupAdvancedListener = new ActionListener() {

            final ResultSetGroupPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(cellElement == null)
                    return;
                Object obj = cellElement.getValue();
                if(obj == null || !(obj instanceof DSColumn))
                {
                    return;
                } else
                {
                    final DSColumn dSColumn = (DSColumn)obj;
                    String as[] = DesignTableDataManager.getSelectedColumnNames(DesignTableDataManager.getEditingTableDataSource(), dSColumn.getDSName());
                    final SpecifiedGroupAttrPane specifiedGroupAttrPane = new SpecifiedGroupAttrPane(as);
                    specifiedGroupAttrPane.populate(recordGrouper);
                    specifiedGroupAttrPane.showWindow(SwingUtilities.getWindowAncestor(ResultSetGroupPane.this), new DialogActionAdapter() {

                        final SpecifiedGroupAttrPane val$specifiedGroupAttrPane;
                        final DSColumn val$dSColumn;
                        final _cls1 this$1;

                        public void doOk()
                        {
                            RecordGrouper recordgrouper = specifiedGroupAttrPane.update(cellElement, recordGrouper);
                            if(!isNPE(cellElement))
                                dSColumn.setGrouper(recordgrouper);
                            setRecordGrouper(recordgrouper);
                            fireTargetChanged();
                            JTemplate jtemplate = DesignerContext.getDesignerFrame().getSelectedJTemplate();
                            if(jtemplate != null)
                            {
                                jtemplate.fireTargetModified();
                                jtemplate.requestGridFocus();
                            }
                        }

                    
                    {
                        this$1 = _cls1.this;
                        specifiedGroupAttrPane = specifiedgroupattrpane;
                        dSColumn = dscolumn;
                        super();
                    }
                    }
).setVisible(true);
                    return;
                }
            }

            
            {
                this$0 = ResultSetGroupPane.this;
                super();
            }
        }
;
        groupComboBox = new UIComboBox(new String[] {
            Inter.getLocText("Common"), Inter.getLocText("Continuum"), Inter.getLocText("Advanced")
        });
    }

    abstract void populate(TemplateCellElement templatecellelement);

    abstract void update();

    abstract void setRecordGrouper(RecordGrouper recordgrouper);

    void fireTargetChanged()
    {
    }

    protected RecordGrouper updateGroupCombox()
    {
        if(groupComboBox.getSelectedIndex() == 0)
        {
            FunctionGrouper functiongrouper = new FunctionGrouper();
            functiongrouper.setDivideMode(0);
            functiongrouper.setCustom(false);
            recordGrouper = functiongrouper;
        } else
        if(groupComboBox.getSelectedIndex() == 1)
        {
            FunctionGrouper functiongrouper1 = new FunctionGrouper();
            functiongrouper1.setDivideMode(2);
            functiongrouper1.setCustom(false);
            recordGrouper = functiongrouper1;
        } else
        if(groupComboBox.getSelectedIndex() == 2)
        {
            if(recordGrouper == null)
                recordGrouper = new CustomGrouper();
            if(!(recordGrouper instanceof CustomGrouper) && (!(recordGrouper instanceof FunctionGrouper) || !((FunctionGrouper)recordGrouper).isCustom()))
                recordGrouper = new CustomGrouper();
        }
        return recordGrouper;
    }

    protected boolean isNPE(CellElement cellelement)
    {
        if(cellelement == null)
            return true;
        if(cellelement.getValue() == null)
            return true;
        return !(cellelement.getValue() instanceof DSColumn);
    }
}
