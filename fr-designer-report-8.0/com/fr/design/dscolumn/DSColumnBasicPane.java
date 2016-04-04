// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.dscolumn;

import com.fr.data.TableDataSource;
import com.fr.design.dialog.BasicPane;
import com.fr.design.expand.ConditionParentPane;
import com.fr.design.expand.ExpandDirectionPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellExpandAttr;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;

// Referenced classes of package com.fr.design.dscolumn:
//            SelectedDataColumnPane, SelectedConfirmedDataColumnPane, ResultSetGroupPopUpPane

public class DSColumnBasicPane extends BasicPane
{

    private SelectedDataColumnPane selectDataColumnPane;
    private ConditionParentPane conditionParentPane;
    private ResultSetGroupPopUpPane resultSetGroupPane;
    private ExpandDirectionPane expandDirectionPane;
    private CellElement cellElement;
    ActionListener summary_direction_ActionListener = new ActionListener() {

        final DSColumnBasicPane this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            if(expandDirectionPane != null)
                expandDirectionPane.setNoneRadioButtonSelected(true);
        }

            
            {
                this$0 = DSColumnBasicPane.this;
                super();
            }
    }
;
    ActionListener otherGroup_direction_ActionListener = new ActionListener() {

        final DSColumnBasicPane this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            if(expandDirectionPane != null)
                expandDirectionPane.setNoneRadioButtonSelected(false);
        }

            
            {
                this$0 = DSColumnBasicPane.this;
                super();
            }
    }
;
    ActionListener sdcUpdate_ActionListener = new ActionListener() {

        final DSColumnBasicPane this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            selectDataColumnPane.update(cellElement);
        }

            
            {
                this$0 = DSColumnBasicPane.this;
                super();
            }
    }
;

    public DSColumnBasicPane()
    {
        this(2);
    }

    public DSColumnBasicPane(int i)
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        if(i > 1)
            selectDataColumnPane = new SelectedDataColumnPane();
        else
            selectDataColumnPane = new SelectedConfirmedDataColumnPane();
        selectDataColumnPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("Select_DataColumn"), null));
        if(i > 1)
        {
            conditionParentPane = new ConditionParentPane();
            conditionParentPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("ParentCell_Setting"), null));
        }
        resultSetGroupPane = new ResultSetGroupPopUpPane(i > 1);
        resultSetGroupPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("Data_Setting"), null));
        if(i > 1)
        {
            expandDirectionPane = new ExpandDirectionPane();
            expandDirectionPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("ExpandD-Expand_Direction"), null));
        }
        double ad[] = {
            -2D, -2D, -2D, -2D
        };
        double ad1[] = {
            -1D
        };
        Component acomponent[][] = (Component[][])null;
        if(i > 1)
            acomponent = (new Component[][] {
                new Component[] {
                    selectDataColumnPane
                }, new Component[] {
                    conditionParentPane
                }, new Component[] {
                    resultSetGroupPane
                }, new Component[] {
                    expandDirectionPane
                }
            });
        else
            acomponent = (new Component[][] {
                new Component[] {
                    selectDataColumnPane
                }, new Component[] {
                    resultSetGroupPane
                }
            });
        add(TableLayoutHelper.createTableLayoutPane(acomponent, ad, ad1), "Center");
        resultSetGroupPane.addListeners(summary_direction_ActionListener, otherGroup_direction_ActionListener, sdcUpdate_ActionListener);
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("Basic");
    }

    public void populate(TableDataSource tabledatasource, TemplateCellElement templatecellelement)
    {
        if(templatecellelement == null)
            return;
        cellElement = templatecellelement;
        selectDataColumnPane.populate(tabledatasource, templatecellelement);
        CellExpandAttr cellexpandattr = templatecellelement.getCellExpandAttr();
        if(conditionParentPane != null)
            conditionParentPane.populate(cellexpandattr);
        if(expandDirectionPane != null)
            expandDirectionPane.populate(cellexpandattr);
        resultSetGroupPane.populate(templatecellelement);
        if(expandDirectionPane != null && resultSetGroupPane.isSummaryRadioButtonSelected())
            expandDirectionPane.setNoneRadioButtonSelected(true);
    }

    public void update(TemplateCellElement templatecellelement)
    {
        if(templatecellelement == null)
            return;
        selectDataColumnPane.update(templatecellelement);
        CellExpandAttr cellexpandattr = templatecellelement.getCellExpandAttr();
        if(cellexpandattr == null)
        {
            cellexpandattr = new CellExpandAttr();
            templatecellelement.setCellExpandAttr(cellexpandattr);
        }
        if(conditionParentPane != null)
            conditionParentPane.update(cellexpandattr);
        if(expandDirectionPane != null)
            expandDirectionPane.update(cellexpandattr);
        resultSetGroupPane.update();
    }

    public void putElementcase(ElementCasePane elementcasepane)
    {
        if(conditionParentPane != null)
            conditionParentPane.putElementcase(elementcasepane);
    }

    public void putCellElement(TemplateCellElement templatecellelement)
    {
        if(conditionParentPane != null)
            conditionParentPane.putCellElement(templatecellelement);
    }



}
