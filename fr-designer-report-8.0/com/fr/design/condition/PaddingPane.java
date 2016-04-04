// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.condition;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.fun.IndentationUnitProcessor;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.gui.style.DefaultIndentationUnitProcessor;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.highlight.HighlightAction;
import com.fr.report.cell.cellattr.highlight.PaddingHighlightAction;
import javax.swing.SpinnerNumberModel;

// Referenced classes of package com.fr.design.condition:
//            ConditionAttrSingleConditionPane, ConditionAttributesPane

public class PaddingPane extends ConditionAttrSingleConditionPane
{

    private UILabel paddingLeft;
    private UIBasicSpinner paddingLeftSpinner;
    private UILabel paddingRight;
    private UIBasicSpinner paddingRightSpinner;
    private UIComboBox paddingScopeComboBox;
    private IndentationUnitProcessor indentationUnitProcessor;

    public PaddingPane(ConditionAttributesPane conditionattributespane)
    {
        super(conditionattributespane);
        indentationUnitProcessor = null;
        paddingLeft = new UILabel((new StringBuilder()).append(Inter.getLocText("Style-Left_Indent")).append(":").toString());
        paddingLeftSpinner = new UIBasicSpinner(new SpinnerNumberModel(2, 0, 0x7fffffff, 1));
        GUICoreUtils.setColumnForSpinner(paddingLeftSpinner, 5);
        paddingRight = new UILabel((new StringBuilder()).append(Inter.getLocText("Style-Right_Indent")).append(":").toString());
        paddingRightSpinner = new UIBasicSpinner(new SpinnerNumberModel(2, 0, 0x7fffffff, 1));
        GUICoreUtils.setColumnForSpinner(paddingRightSpinner, 5);
        add(paddingLeft);
        add(paddingLeftSpinner);
        add(paddingRight);
        add(paddingRightSpinner);
        paddingScopeComboBox = new UIComboBox(new String[] {
            Inter.getLocText("Utils-Current_Cell"), Inter.getLocText("Utils-Current_Row"), Inter.getLocText("Utils-Current_Column")
        });
        add(paddingScopeComboBox);
        paddingLeftSpinner.setValue(new Integer(0));
        paddingRightSpinner.setValue(new Integer(0));
        indentationUnitProcessor = ExtraDesignClassManager.getInstance().getIndentationUnitEditor();
        if(null == indentationUnitProcessor)
            indentationUnitProcessor = new DefaultIndentationUnitProcessor();
    }

    public String nameForPopupMenuItem()
    {
        return Inter.getLocText("FR-Designer_Sytle-Indentation");
    }

    protected String title4PopupWindow()
    {
        return nameForPopupMenuItem();
    }

    public void populate(HighlightAction highlightaction)
    {
        int i = indentationUnitProcessor.paddingUnitProcessor(((PaddingHighlightAction)highlightaction).getPaddingLeft());
        int j = indentationUnitProcessor.paddingUnitProcessor(((PaddingHighlightAction)highlightaction).getPaddingRight());
        paddingLeftSpinner.setValue(new Integer(i));
        paddingRightSpinner.setValue(new Integer(j));
        paddingScopeComboBox.setSelectedIndex(((PaddingHighlightAction)highlightaction).getScope());
    }

    public HighlightAction update()
    {
        int i = indentationUnitProcessor.paddingUnitGainFromSpinner(((Integer)paddingLeftSpinner.getValue()).intValue());
        int j = indentationUnitProcessor.paddingUnitGainFromSpinner(((Integer)paddingRightSpinner.getValue()).intValue());
        return new PaddingHighlightAction(Integer.valueOf(i).intValue(), Integer.valueOf(j).intValue(), paddingScopeComboBox.getSelectedIndex());
    }

    public volatile Object update()
    {
        return update();
    }

    public volatile void populate(Object obj)
    {
        populate((HighlightAction)obj);
    }
}
