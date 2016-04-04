// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.expand;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellExpandAttr;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.expand:
//            ParentPane

public class ConditionParentPane extends JPanel
{

    private ParentPane leftParentPane;
    private ParentPane upParentPane;

    public ConditionParentPane()
    {
        initComponents(null);
    }

    public ConditionParentPane(ActionListener actionlistener)
    {
        initComponents(actionlistener);
    }

    public void initComponents(ActionListener actionlistener)
    {
        JPanel jpanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        add(jpanel);
        JPanel jpanel1 = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
        jpanel.add(jpanel1);
        jpanel1.add(GUICoreUtils.createFlowPane(new JComponent[] {
            new UILabel((new StringBuilder()).append(Inter.getLocText("LeftParent")).append(": ").toString()), leftParentPane = new ParentPane(0, actionlistener)
        }, 0));
        jpanel1.add(GUICoreUtils.createFlowPane(new JComponent[] {
            new UILabel((new StringBuilder()).append(Inter.getLocText("UpParent")).append(": ").toString()), upParentPane = new ParentPane(1, actionlistener)
        }, 0));
    }

    public void putElementcase(ElementCasePane elementcasepane)
    {
        leftParentPane.putElementcase(elementcasepane);
        upParentPane.putElementcase(elementcasepane);
    }

    public void putCellElement(TemplateCellElement templatecellelement)
    {
        leftParentPane.putCellElement(templatecellelement);
        upParentPane.putCellElement(templatecellelement);
    }

    public void populate(CellExpandAttr cellexpandattr)
    {
        if(cellexpandattr == null)
            cellexpandattr = new CellExpandAttr();
        leftParentPane.populate(cellexpandattr);
        upParentPane.populate(cellexpandattr);
    }

    public void update(CellExpandAttr cellexpandattr)
    {
        if(cellexpandattr == null)
            cellexpandattr = new CellExpandAttr();
        leftParentPane.update(cellexpandattr);
        upParentPane.update(cellexpandattr);
    }
}
