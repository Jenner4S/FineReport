// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.condition;

import com.fr.design.DesignerEnvManager;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.highlight.HighlightAction;
import com.fr.stable.unit.*;
import java.math.BigDecimal;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

// Referenced classes of package com.fr.design.condition:
//            ConditionAttrSingleConditionPane, ConditionAttributesPane

public abstract class WHPane extends ConditionAttrSingleConditionPane
{

    protected UILabel unitLabel;
    private UIBasicSpinner spinner;
    private String locString;

    protected WHPane(ConditionAttributesPane conditionattributespane, String s)
    {
        super(conditionattributespane);
        add(new UILabel((new StringBuilder()).append(Inter.getLocText(s)).append(":").toString()));
        add(spinner = new UIBasicSpinner(new SpinnerNumberModel(0, 0, 0x7fffffff, 1)));
        add(unitLabel = new UILabel(getUnitString()));
        GUICoreUtils.setColumnForSpinner(spinner, 5);
        spinner.setValue(new Integer(0));
        locString = s;
    }

    public String nameForPopupMenuItem()
    {
        return locString;
    }

    protected String title4PopupWindow()
    {
        return nameForPopupMenuItem();
    }

    public void populate(HighlightAction highlightaction, JSpinner jspinner)
    {
        short word0 = DesignerEnvManager.getEnvManager().getReportLengthUnit();
        UNIT unit = getUnit(highlightaction);
        double d;
        if(word0 == 1)
        {
            d = unit.toCMValue4Scale2();
            unitLabel.setText(Inter.getLocText("FR-Designer_Unit_CM"));
        } else
        if(word0 == 2)
        {
            d = unit.toINCHValue4Scale3();
            unitLabel.setText(Inter.getLocText("FR-Designer_Unit_INCH"));
        } else
        if(word0 == 3)
        {
            d = unit.toPTValue4Scale2();
            unitLabel.setText(Inter.getLocText("FR-Designer_Unit_PT"));
        } else
        {
            d = unit.toMMValue4Scale2();
            unitLabel.setText(Inter.getLocText("FR-Designer_Unit_MM"));
        }
        Float float1 = new Float((new BigDecimal((new StringBuilder()).append(d).append("").toString())).setScale(2, 1).floatValue());
        jspinner.setValue(float1);
    }

    protected abstract UNIT getUnit(HighlightAction highlightaction);

    protected String getUnitString()
    {
        short word0 = DesignerEnvManager.getEnvManager().getReportLengthUnit();
        if(word0 == 1)
            return Inter.getLocText("FR-Designer_Unit_CM");
        if(word0 == 2)
            return Inter.getLocText("FR-Designer_Unit_INCH");
        if(word0 == 3)
            return Inter.getLocText("FR-Designer_Unit_PT");
        else
            return Inter.getLocText("FR-Designer_Unit_MM");
    }

    public HighlightAction update(UIBasicSpinner uibasicspinner)
    {
        float f = ((Number)uibasicspinner.getValue()).floatValue();
        f = (new Float((new BigDecimal((new StringBuilder()).append(f).append("").toString())).setScale(2, 1).floatValue())).floatValue();
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
        return returnAction(((UNIT) (obj)));
    }

    public void populate(HighlightAction highlightaction)
    {
        populate(highlightaction, ((JSpinner) (spinner)));
    }

    public HighlightAction update()
    {
        return update(spinner);
    }

    protected abstract HighlightAction returnAction(UNIT unit);

    public volatile Object update()
    {
        return update();
    }

    public volatile void populate(Object obj)
    {
        populate((HighlightAction)obj);
    }
}
