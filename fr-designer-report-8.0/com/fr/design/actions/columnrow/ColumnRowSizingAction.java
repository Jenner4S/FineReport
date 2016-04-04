// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.columnrow;

import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.frpane.UnitInputPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.report.elementcase.ElementCase;
import com.fr.stable.ArrayUtils;
import com.fr.stable.unit.*;

// Referenced classes of package com.fr.design.actions.columnrow:
//            AbstractColumnRowIndexAction

public abstract class ColumnRowSizingAction extends AbstractColumnRowIndexAction
{

    protected ColumnRowSizingAction(ElementCasePane elementcasepane, int i)
    {
        super(elementcasepane, i);
    }

    protected boolean executeActionReturnUndoRecordNeededWithCellSelection(CellSelection cellselection)
    {
        final ElementCasePane ePane = (ElementCasePane)getEditingComponent();
        final com.fr.report.elementcase.TemplateElementCase report = ePane.getEditingElementCase();
        final UnitInputPane uPane = new UnitInputPane(3, title4UnitInputPane()) {

            final ColumnRowSizingAction this$0;

            protected String title4PopupWindow()
            {
                return title4UnitInputPane();
            }

            
            {
                this$0 = ColumnRowSizingAction.this;
                super(i, s);
            }
        }
;
        UNIT unit = getShowLen(report, cellselection);
        populateNumberDialog(uPane, unit);
        final CellSelection finalCS = cellselection;
        uPane.showSmallWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

            final UnitInputPane val$uPane;
            final ElementCase val$report;
            final CellSelection val$finalCS;
            final ElementCasePane val$ePane;
            final ColumnRowSizingAction this$0;

            public void doOk()
            {
                try
                {
                    float f = (float)uPane.update();
                    short word0 = DesignerEnvManager.getEnvManager().getReportLengthUnit();
                    Object obj;
                    if(word0 == 1)
                        obj = new CM(f);
                    else
                    if(word0 == 2)
                        obj = new INCH(f);
                    else
                    if(word0 == 3)
                        obj = new PT(f);
                    else
                        obj = new MM(f);
                    updateAction(report, ((UNIT) (obj)), finalCS);
                }
                catch(com.fr.design.gui.frpane.UnitInputPane.ValueNotChangeException valuenotchangeexception) { }
                ePane.fireTargetModified();
            }

            
            {
                this$0 = ColumnRowSizingAction.this;
                uPane = unitinputpane;
                report = elementcase;
                finalCS = cellselection;
                ePane = elementcasepane;
                super();
            }
        }
).setVisible(true);
        return false;
    }

    protected void populateNumberDialog(UnitInputPane unitinputpane, UNIT unit)
    {
        short word0 = DesignerEnvManager.getEnvManager().getReportLengthUnit();
        float f;
        if(word0 == 1)
        {
            f = unit.toCMValue4Scale2();
            unitinputpane.setUnitText(Inter.getLocText("Unit_CM"));
        } else
        if(word0 == 2)
        {
            f = unit.toINCHValue4Scale3();
            unitinputpane.setUnitText(Inter.getLocText("Unit_INCH"));
        } else
        if(word0 == 3)
        {
            f = unit.toPTValue4Scale2();
            unitinputpane.setUnitText(Inter.getLocText("Unit_PT"));
        } else
        {
            f = unit.toMMValue4Scale2();
            unitinputpane.setUnitText(Inter.getLocText("Unit_MM"));
        }
        unitinputpane.populate(f);
    }

    protected abstract String title4UnitInputPane();

    protected abstract void updateAction(ElementCase elementcase, UNIT unit, CellSelection cellselection);

    protected abstract UNIT getShowLen(ElementCase elementcase, CellSelection cellselection);

    protected abstract UNIT getIndexLen(int i, ElementCase elementcase);

    protected UNIT getSelectedCellsLen(int ai[], ElementCase elementcase)
    {
        int i = ArrayUtils.getLength(ai);
        UNIT unit = null;
        for(int j = 0; j < i; j++)
        {
            UNIT unit1 = getIndexLen(ai[j], elementcase);
            if(unit == null)
            {
                unit = unit1;
                continue;
            }
            if(unit.subtract(unit1).not_equal_zero())
                return UNIT.ZERO;
        }

        return getIndexLen(getIndex(), elementcase);
    }
}
