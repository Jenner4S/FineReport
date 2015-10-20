package com.fr.design.chart.series.SeriesCondition;

import com.fr.general.Inter;
import com.fr.chart.chartglyph.CustomAttr;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;

import java.awt.*;

public class DataSeriesAttrCustomPane extends DataSeriesAttrPane {
	private static final long serialVersionUID = -9046019835977910412L;

	public DataSeriesAttrCustomPane() {
		super();

		// �����趨��С. JControlPane�е�(450, 450) ��С  @ChartSize
		this.setPreferredSize(new Dimension(640,450));
	}
	
	public NameableCreator[] createNameableCreators() {
		return new NameableCreator[] {
				new NameObjectCreator(Inter.getLocText("Condition_Attributes"), CustomAttr.class, DataSeriesCustomConditionPane.class)
		};
	}
}
