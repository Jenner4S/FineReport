package com.fr.design.data.datapane;

import java.awt.*;

import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.data.datapane.preview.PreviewLabel;
import com.fr.design.data.datapane.preview.PreviewLabel.Previewable;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerBean;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.Inter;
import com.fr.design.utils.gui.GUICoreUtils;

public class VerticalChoosePane extends ChoosePane implements DesignerBean{

	public VerticalChoosePane(Previewable previewable) {
		this(previewable, -1);
	}

	public VerticalChoosePane(Previewable previewable, int labelSize) {
		super(previewable, labelSize);
		DesignerContext.setDesignerBean("databasename", this);
	}

	@Override
	protected void initComponentsLayout(PreviewLabel previewLabel, int labelSize) {
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { p, f };
		double[] rowSize = { p, p, p };

        JPanel rs = new JPanel(new BorderLayout(0, 0));
        rs.add(tableNameComboBox, BorderLayout.CENTER);
        rs.add(GUICoreUtils.createFlowPane(new Component[]{new RefreshLabel(this), previewLabel}, FlowLayout.LEFT, LayoutConstants.HGAP_LARGE), BorderLayout.EAST);

        UILabel l1 = new UILabel(Inter.getLocText("Database") + ":", UILabel.RIGHT);
        UILabel l2 = new UILabel(Inter.getLocText("Model") + ":", UILabel.RIGHT);
        UILabel l3 = new UILabel(Inter.getLocText(new String[]{"Database", "Table"}) + ":", UILabel.RIGHT);
        
        if (labelSize > 0) {
        	Dimension pSize = new Dimension(labelSize, 20);
        	l1.setPreferredSize(pSize);
        	l2.setPreferredSize(pSize);
        	l3.setPreferredSize(pSize);
        }
        
        Component[][] components = new Component[][]{
                new Component[]{l1, dsNameComboBox},
                new Component[]{l2, schemaBox},
                new Component[]{l3, rs}
        };

        JPanel content = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
        this.setLayout(new BorderLayout());
        this.add(content,BorderLayout.CENTER);
	}



	@Override
	public void refreshBeanElement() {
		initDsNameComboBox();
	}

}
