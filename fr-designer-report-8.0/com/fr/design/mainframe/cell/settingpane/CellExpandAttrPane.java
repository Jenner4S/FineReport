// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.cell.settingpane;

import com.fr.base.BaseUtils;
import com.fr.design.expand.*;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellExpandAttr;
import com.fr.report.elementcase.TemplateElementCase;
import java.awt.Component;
import java.awt.Rectangle;
import javax.swing.*;

// Referenced classes of package com.fr.design.mainframe.cell.settingpane:
//            AbstractCellAttrPane

public class CellExpandAttrPane extends AbstractCellAttrPane
{

    private UIButtonGroup expandDirectionButton;
    private ExpandLeftFatherPane leftFatherPane;
    private ExpandUpFatherPane rightFatherPane;
    private UICheckBox horizontalExpandableCheckBox;
    private UICheckBox verticalExpandableCheckBox;
    private SortExpandAttrPane sortAfterExpand;

    public CellExpandAttrPane()
    {
    }

    public JPanel createContentPane()
    {
        String as[] = {
            Inter.getLocText("ExpandD-Not_Expand"), Inter.getLocText("Utils-Top_to_Bottom"), Inter.getLocText("Utils-Left_to_Right")
        };
        Icon aicon[] = {
            BaseUtils.readIcon("/com/fr/design/images/expand/none16x16.png"), BaseUtils.readIcon("/com/fr/design/images/expand/vertical.png"), BaseUtils.readIcon("/com/fr/design/images/expand/landspace.png")
        };
        Byte abyte[] = {
            Byte.valueOf((byte)2), Byte.valueOf((byte)0), Byte.valueOf((byte)1)
        };
        expandDirectionButton = new UIButtonGroup(aicon, abyte);
        expandDirectionButton.setAllToolTips(as);
        leftFatherPane = new ExpandLeftFatherPane();
        rightFatherPane = new ExpandUpFatherPane();
        horizontalExpandableCheckBox = new UICheckBox(Inter.getLocText("ExpandD-Horizontal_Extendable"));
        verticalExpandableCheckBox = new UICheckBox(Inter.getLocText("ExpandD-Vertical_Extendable"));
        sortAfterExpand = new SortExpandAttrPane();
        initAllNames();
        return layoutPane();
    }

    private void initAllNames()
    {
        expandDirectionButton.setGlobalName(Inter.getLocText("ExpandD-Expand_Direction"));
        leftFatherPane.setGlobalName(Inter.getLocText("LeftParent"));
        rightFatherPane.setGlobalName(Inter.getLocText("ExpandD-Up_Father_Cell"));
        horizontalExpandableCheckBox.setGlobalName(Inter.getLocText("ExpandD-Expandable"));
        verticalExpandableCheckBox.setGlobalName(Inter.getLocText("ExpandD-Expandable"));
    }

    private JPanel layoutPane()
    {
        double d = -1D;
        double d1 = -2D;
        Component acomponent[][] = {
            {
                new UILabel((new StringBuilder()).append(Inter.getLocText("ExpandD-Expand_Direction")).append(":").toString(), 4), expandDirectionButton
            }, {
                new UILabel((new StringBuilder()).append(Inter.getLocText("LeftParent")).append(":").toString(), 4), leftFatherPane
            }, {
                new UILabel((new StringBuilder()).append(Inter.getLocText("ExpandD-Up_Father_Cell")).append(":").toString(), 4), rightFatherPane
            }, {
                new JSeparator(), null
            }, {
                new UILabel((new StringBuilder()).append(Inter.getLocText("ExpandD-Expandable")).append(":").toString(), 4), horizontalExpandableCheckBox
            }, {
                null, verticalExpandableCheckBox
            }, {
                new UILabel((new StringBuilder()).append(Inter.getLocText("ExpandD-Sort_After_Expand")).append(":").toString(), 4), sortAfterExpand
            }
        };
        double ad[] = {
            d1, d1, d1, d1, d1, d1, d1, d1, d1, d1, 
            d1, d1, d1
        };
        double ad1[] = {
            d1, d
        };
        int ai[][] = {
            {
                1, 1
            }, {
                1, 3
            }, {
                1, 3
            }, {
                1, 1
            }, {
                1, 1
            }, {
                1, 1
            }, {
                1, 3
            }
        };
        return TableLayoutHelper.createGapTableLayoutPane(acomponent, ad, ad1, ai, 6D, 6D);
    }

    protected void populateBean()
    {
        leftFatherPane.setElementCasePane(elementCasePane);
        rightFatherPane.setElementCasePane(elementCasePane);
        CellExpandAttr cellexpandattr = cellElement.getCellExpandAttr();
        if(cellexpandattr == null)
        {
            cellexpandattr = new CellExpandAttr();
            cellElement.setCellExpandAttr(cellexpandattr);
        }
        expandDirectionButton.setSelectedItem(Byte.valueOf(cellexpandattr.getDirection()));
        leftFatherPane.populate(cellexpandattr);
        rightFatherPane.populate(cellexpandattr);
        switch(cellexpandattr.getExtendable())
        {
        case 0: // '\0'
            horizontalExpandableCheckBox.setSelected(true);
            verticalExpandableCheckBox.setSelected(true);
            break;

        case 1: // '\001'
            horizontalExpandableCheckBox.setSelected(false);
            verticalExpandableCheckBox.setSelected(true);
            break;

        case 2: // '\002'
            horizontalExpandableCheckBox.setSelected(true);
            verticalExpandableCheckBox.setSelected(false);
            break;

        default:
            horizontalExpandableCheckBox.setSelected(false);
            verticalExpandableCheckBox.setSelected(false);
            break;
        }
        sortAfterExpand.populate(cellexpandattr);
    }

    public String getIconPath()
    {
        return "com/fr/design/images/expand/cellAttr.gif";
    }

    public void updateBean(TemplateCellElement templatecellelement)
    {
        CellExpandAttr cellexpandattr = templatecellelement.getCellExpandAttr();
        if(cellexpandattr == null)
        {
            cellexpandattr = new CellExpandAttr();
            templatecellelement.setCellExpandAttr(cellexpandattr);
        }
        if(ComparatorUtils.equals(getGlobalName(), Inter.getLocText("ExpandD-Expand_Direction")))
            cellexpandattr.setDirection(((Byte)expandDirectionButton.getSelectedItem()).byteValue());
        if(ComparatorUtils.equals(getGlobalName(), Inter.getLocText("LeftParent")))
            leftFatherPane.update(cellexpandattr);
        if(ComparatorUtils.equals(getGlobalName(), Inter.getLocText("ExpandD-Up_Father_Cell")))
            rightFatherPane.update(cellexpandattr);
        if(ComparatorUtils.equals(getGlobalName(), Inter.getLocText("ExpandD-Expandable")))
            if(horizontalExpandableCheckBox.isSelected())
            {
                if(verticalExpandableCheckBox.isSelected())
                    cellexpandattr.setExtendable((byte)0);
                else
                    cellexpandattr.setExtendable((byte)2);
            } else
            if(verticalExpandableCheckBox.isSelected())
                cellexpandattr.setExtendable((byte)1);
            else
                cellexpandattr.setExtendable((byte)3);
        if(ComparatorUtils.equals(getGlobalName(), Inter.getLocText("ExpandD-Sort_After_Expand")))
            sortAfterExpand.update(cellexpandattr);
    }

    public void updateBeans()
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
                    Object obj = templateelementcase.getTemplateCellElement(i1, j1);
                    if(obj == null)
                    {
                        obj = new DefaultTemplateCellElement(i1, j1);
                        templateelementcase.addCellElement(((TemplateCellElement) (obj)));
                    }
                    updateBean(((TemplateCellElement) (obj)));
                }

            }

        }

    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("ExpandD-Expand_Attribute");
    }
}
