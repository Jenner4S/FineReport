// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.cell.settingpane;

import com.fr.base.Style;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.cell.settingpane.style.StylePane;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Referenced classes of package com.fr.design.mainframe.cell.settingpane:
//            AbstractCellAttrPane

public class CellStylePane extends AbstractCellAttrPane
{

    private StylePane stylePane;

    public CellStylePane()
    {
    }

    public JPanel createContentPane()
    {
        JPanel jpanel = new JPanel(new BorderLayout());
        stylePane = new StylePane();
        jpanel.add(stylePane, "Center");
        stylePane.addPredefinedChangeListener(new ChangeListener() {

            final CellStylePane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                attributeChanged();
            }

            
            {
                this$0 = CellStylePane.this;
                super();
            }
        }
);
        stylePane.addCustomTabChangeListener(new ChangeListener() {

            final CellStylePane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                adjustValues();
            }

            
            {
                this$0 = CellStylePane.this;
                super();
            }
        }
);
        return jpanel;
    }

    public String getIconPath()
    {
        return "com/fr/design/images/m_format/cell.png";
    }

    public void updateBean(TemplateCellElement templatecellelement)
    {
        templatecellelement.setStyle((Style)stylePane.updateBean());
    }

    public void updateBeans()
    {
        if(stylePane.getSelectedIndex() == 1)
        {
            Style style = (Style)stylePane.updateBean();
            TemplateElementCase templateelementcase1 = elementCasePane.getEditingElementCase();
            int j = cs.getCellRectangleCount();
            for(int l = 0; l < j; l++)
            {
                Rectangle rectangle1 = cs.getCellRectangle(l);
                for(int j1 = 0; j1 < rectangle1.height; j1++)
                {
                    for(int l1 = 0; l1 < rectangle1.width; l1++)
                    {
                        int j2 = l1 + rectangle1.x;
                        int l2 = j1 + rectangle1.y;
                        Object obj1 = templateelementcase1.getTemplateCellElement(j2, l2);
                        if(obj1 == null)
                        {
                            obj1 = new DefaultTemplateCellElement(j2, l2);
                            templateelementcase1.addCellElement(((TemplateCellElement) (obj1)));
                        }
                        ((TemplateCellElement) (obj1)).setStyle(style);
                    }

                }

            }

        } else
        {
            TemplateElementCase templateelementcase = elementCasePane.getEditingElementCase();
            int i = cs.getCellRectangleCount();
            for(int k = 0; k < i; k++)
            {
                Rectangle rectangle = cs.getCellRectangle(k);
                for(int i1 = 0; i1 < rectangle.height; i1++)
                {
                    for(int k1 = 0; k1 < rectangle.width; k1++)
                    {
                        int i2 = k1 + rectangle.x;
                        int k2 = i1 + rectangle.y;
                        Object obj = templateelementcase.getTemplateCellElement(i2, k2);
                        if(obj == null)
                        {
                            obj = new DefaultTemplateCellElement(i2, k2);
                            templateelementcase.addCellElement(((TemplateCellElement) (obj)));
                        }
                        Style style1 = ((TemplateCellElement) (obj)).getStyle();
                        if(style1 == null)
                        {
                            Style _tmp = style1;
                            style1 = Style.DEFAULT_STYLE;
                        }
                        style1 = stylePane.updateStyle(style1);
                        ((TemplateCellElement) (obj)).setStyle(style1);
                    }

                }

            }

            stylePane.updateBorder();
        }
    }

    protected void populateBean()
    {
        stylePane.populateBean(cellElement.getStyle());
        stylePane.dealWithBorder(elementCasePane);
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("Style");
    }

    public transient void setSelectedByIds(int i, String as[])
    {
        stylePane.setSelctedByName(as[i]);
    }

}
