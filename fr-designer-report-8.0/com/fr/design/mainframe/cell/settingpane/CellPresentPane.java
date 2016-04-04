// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.cell.settingpane;

import com.fr.base.present.Present;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.present.PresentPane;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.cell.settingpane:
//            AbstractCellAttrPane

public class CellPresentPane extends AbstractCellAttrPane
{

    private PresentPane presentPane;

    public CellPresentPane()
    {
    }

    public JPanel createContentPane()
    {
        presentPane = new PresentPane();
        JPanel jpanel = new JPanel(new BorderLayout());
        jpanel.add(presentPane, "Center");
        presentPane.addTabChangeListener(new ItemListener() {

            final CellPresentPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                adjustValues();
            }

            
            {
                this$0 = CellPresentPane.this;
                super();
            }
        }
);
        return jpanel;
    }

    public String getIconPath()
    {
        return "com/fr/design/images/data/source/dataDictionary.png";
    }

    public void updateBean(TemplateCellElement templatecellelement)
    {
        templatecellelement.setPresent((Present)presentPane.updateBean());
    }

    public void updateBeans()
    {
        Present present = (Present)presentPane.updateBean();
        TemplateElementCase templateelementcase = elementCasePane.getEditingElementCase();
        int i = cs.getCellRectangleCount();
        for(int j = 0; j < i; j++)
        {
            Rectangle rectangle = cs.getCellRectangle(j);
            for(int k = 0; k < rectangle.height; k++)
            {
                for(int l = 0; l < rectangle.width; l++)
                {
                    int i1 = l + rectangle.x;
                    int j1 = k + rectangle.y;
                    Object obj = templateelementcase.getTemplateCellElement(i1, j1);
                    if(obj == null)
                    {
                        obj = new DefaultTemplateCellElement(i1, j1);
                        templateelementcase.addCellElement(((TemplateCellElement) (obj)));
                    }
                    ((TemplateCellElement) (obj)).setPresent(present);
                }

            }

        }

    }

    protected void populateBean()
    {
        Present present = getSelectCellPresent();
        presentPane.populateBean(present);
    }

    private Present getSelectCellPresent()
    {
        TemplateElementCase templateelementcase = elementCasePane.getEditingElementCase();
        int i = cs.getCellRectangleCount();
        for(int j = 0; j < i; j++)
        {
            Rectangle rectangle = cs.getCellRectangle(j);
            for(int k = 0; k < rectangle.height; k++)
            {
                for(int l = 0; l < rectangle.width; l++)
                {
                    int i1 = l + rectangle.x;
                    int j1 = k + rectangle.y;
                    TemplateCellElement templatecellelement = templateelementcase.getTemplateCellElement(i1, j1);
                    if(templatecellelement == null || templatecellelement.getPresent() == null)
                        return null;
                }

            }

        }

        return cellElement.getPresent();
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("FR-Chart-Style_Present");
    }

    public transient void setSelectedByIds(int i, String as[])
    {
        presentPane.setSelectedByName(as[i]);
    }

}
