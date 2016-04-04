// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.module;

import com.fr.chart.web.ChartHyperPoplink;
import com.fr.chart.web.ChartHyperRelateCellLink;
import com.fr.design.chart.series.SeriesCondition.impl.*;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.frpane.BaseHyperlinkGroup;
import com.fr.general.Inter;
import com.fr.js.FormHyperlinkProvider;
import com.fr.stable.ArrayUtils;
import com.fr.stable.bridge.StableFactory;

public class FormHyperlinkGroup extends BaseHyperlinkGroup
{

    public FormHyperlinkGroup()
    {
    }

    public NameableCreator[] getHyperlinkCreators()
    {
        FormHyperlinkProvider formhyperlinkprovider = (FormHyperlinkProvider)StableFactory.getMarkedInstanceObjectFromClass("FormHyperlink", com/fr/js/FormHyperlinkProvider);
        NameableCreator anameablecreator[] = {
            new NameObjectCreator(Inter.getLocText("FR-Designer_Chart_Float_chart"), com/fr/chart/web/ChartHyperPoplink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperPoplinkPane$CHART_NO_RENAME), new NameObjectCreator(Inter.getLocText("FR-Designer_Chart_Cell"), com/fr/chart/web/ChartHyperRelateCellLink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperRelateCellLinkPane$CHART_NO_RENAME), new NameObjectCreator(Inter.getLocText("FR-Designer_Hyperlink-Form_link"), formhyperlinkprovider.getClass(), com/fr/design/chart/series/SeriesCondition/impl/FormHyperlinkPane)
        };
        return (NameableCreator[])(NameableCreator[])ArrayUtils.addAll(super.getHyperlinkCreators(), anameablecreator);
    }
}
