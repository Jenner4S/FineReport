// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.insert.flot;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.base.chart.BaseChartCollection;
import com.fr.design.actions.ElementCaseAction;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.chart.MiddleChartDialog;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.module.DesignModuleFactory;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.grid.selection.FloatSelection;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.bridge.StableFactory;
import com.fr.stable.unit.OLDPIX;
import java.awt.Color;
import javax.swing.KeyStroke;

public class ChartFloatAction extends ElementCaseAction
{

    public static final MenuKeySet FLOAT_INSERT_CHART = new MenuKeySet() {

        public char getMnemonic()
        {
            return 'C';
        }

        public String getMenuName()
        {
            return Inter.getLocText("M_Insert-Chart");
        }

        public KeyStroke getKeyStroke()
        {
            return null;
        }

    }
;

    public ChartFloatAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setMenuKeySet(FLOAT_INSERT_CHART);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_insert/chart.png"));
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        final ElementCasePane reportPane = (ElementCasePane)getEditingComponent();
        if(reportPane == null)
        {
            return false;
        } else
        {
            reportPane.stopEditing();
            BaseChartCollection basechartcollection = (BaseChartCollection)StableFactory.createXmlObject("CC");
            basechartcollection.removeAllNameObject();
            final MiddleChartDialog chartDialog = DesignModuleFactory.getChartDialog(DesignerContext.getDesignerFrame());
            chartDialog.populate(basechartcollection);
            chartDialog.addDialogActionListener(new DialogActionAdapter() {

                final MiddleChartDialog val$chartDialog;
                final ElementCasePane val$reportPane;
                final ChartFloatAction this$0;

                public void doOk()
                {
                    try
                    {
                        FloatElement floatelement = new FloatElement(chartDialog.getChartCollection().clone());
                        floatelement.setLeftDistance(new OLDPIX(20F));
                        floatelement.setTopDistance(new OLDPIX(20F));
                        floatelement.setWidth(new OLDPIX(507F));
                        floatelement.setHeight(new OLDPIX(340F));
                        Style style = floatelement.getStyle();
                        if(style != null)
                            floatelement.setStyle(style.deriveBorder(0, Color.black, 0, Color.black, 0, Color.black, 0, Color.black));
                        reportPane.getEditingElementCase().addFloatElement(floatelement);
                        reportPane.setSelection(new FloatSelection(floatelement.getName()));
                        reportPane.fireTargetModified();
                        reportPane.fireSelectionChangeListener();
                    }
                    catch(CloneNotSupportedException clonenotsupportedexception)
                    {
                        FRLogger.getLogger().error("Error in Float");
                    }
                }

            
            {
                this$0 = ChartFloatAction.this;
                chartDialog = middlechartdialog;
                reportPane = elementcasepane;
                super();
            }
            }
);
            chartDialog.setVisible(true);
            return true;
        }
    }

}
