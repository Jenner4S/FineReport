package com.fr.design.mainframe.chart.gui.style;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JPanel;

import com.fr.base.BaseUtils;
import com.fr.base.Utils;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.gui.ibutton.UIColorButton;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.FRFont;
import com.fr.design.utils.gui.GUICoreUtils;

/**
 * �����ʽ����, �������С����.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2013-1-21 ����03:35:47
 */
public class ChartTextAttrNoFontSizePane extends ChartTextAttrPane {
	
	private static final long serialVersionUID = 4890526255627852602L;

	public ChartTextAttrNoFontSizePane() {
		super();
	}
	
    protected void initComponents() {
        fontNameComboBox = new UIComboBox(Utils.getAvailableFontFamilyNames4Report());
        fontColor = new UIColorButton();
        fontSizeComboBox = new UIComboBox(Font_Sizes);
        bold = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/bold.png"));
        italic = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/italic.png"));

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;

        Component[] components1 = new Component[]{
                fontColor, italic, bold
        };
        JPanel buttonPane = new JPanel(new BorderLayout());
        buttonPane.add(GUICoreUtils.createFlowPane(components1, FlowLayout.LEFT, LayoutConstants.HGAP_LARGE), BorderLayout.CENTER);

        double[] columnSize = {f};
        double[] rowSize = {p, p};
        Component[][] components = new Component[][]{
                new Component[]{fontNameComboBox},
                new Component[]{buttonPane}
        };

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);

        populate(FRFont.getInstance());
    }
}
