package com.fr.design.chart.series.SeriesCondition.dlp;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import com.fr.base.Utils;
import com.fr.chart.base.AttrContents;
import com.fr.chart.base.ChartConstants;
import com.fr.design.chart.series.SeriesCondition.DataLabelStylePane;
import com.fr.design.chart.series.SeriesCondition.TooltipContentsPane;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.stable.Constants;
import com.fr.stable.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-29
 * Time   : ����9:12
 * 
 * ϵ�б�ǩ����
 */
public class DataLabelPane extends TooltipContentsPane {
	private static final int SPACE = 4;
	
    protected UICheckBox showSeriesNameCB;
    protected UICheckBox showCategoryNameCB;
    protected UIComboBox delimiterBox;
    private DataLabelStylePane stylePane;
    private UIRadioButton bottomButton;
    private UIRadioButton leftButton;
    private UIRadioButton rigtButton;
    private UIRadioButton topButton;
    protected UIRadioButton centerButton;

    public DataLabelPane() {
    	setLayout(FRGUIPaneFactory.createBorderLayout());
		contentPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
		add(contentPane, BorderLayout.CENTER);
		contentPane.add(createJPanel4Label());
    	contentPane.add(createJPanel4Delimiter());
    	JPanel jPanel4FontStyle = createJPanel4FontStyle();
    	if (jPanel4FontStyle != null) {
    		contentPane.add(jPanel4FontStyle);
    	}
    	JPanel jPanel4Position = createJPanel4Position();
    	if (jPanel4Position != null) {
    		contentPane.add(jPanel4Position);

    	}
    }

    private JPanel createJPanel4Label() {
    	return createTableLayoutPane(new Component[][]{
    			createComponents4ShowSeriesName(),
    			createComponents4ShowCategoryName(),
    			createComponents4Value(),
    			createComponents4PercentValue(),
    			createComponents4ShowGuidLine()
    	});

    }

    protected Component[] createComponents4ShowSeriesName() {
        if (showSeriesNameCB == null) {
            showSeriesNameCB = new UICheckBox(Inter.getLocText(new String[]{"ChartF-Series", "WF-Name"}));
        }
        return new Component[]{new UILabel(Inter.getLocText(new String[]{"Label", "Include"}) + ":"), showSeriesNameCB};
    }

    protected Component[] createComponents4ShowCategoryName() {
        if (showCategoryNameCB == null) {
            showCategoryNameCB = new UICheckBox(Inter.getLocText(new String[]{"StyleFormat-Category", "WF-Name"}));
        }
        return new Component[]{null, showCategoryNameCB};
    }

   

    protected Component[] createComponents4ShowGuidLine() {
        return new Component[0];
    }

    private JPanel createJPanel4Delimiter() {
        if (delimiterBox == null) {
            delimiterBox = new UIComboBox(ChartConstants.DELIMITERS);
        }
        delimiterBox.setPreferredSize(new Dimension(70, 20));
        JPanel boxPane = FRGUIPaneFactory.createBoxFlowInnerContainer_S_Pane();
        boxPane.add(new UILabel(Inter.getLocText("Form-Delimiter") + ":"));
        boxPane.add(delimiterBox);
        return boxPane;
    }

    private JPanel createJPanel4FontStyle() {
        JPanel labelStylePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        JPanel westPane = FRGUIPaneFactory.createBoxFlowInnerContainer_S_Pane();
        labelStylePane.add(westPane, BorderLayout.WEST);
        westPane.add(new UILabel(Inter.getLocText(new String[]{"Label", "FRFont"}) + ":"));
        labelStylePane.add(stylePane = new DataLabelStylePane(), BorderLayout.CENTER);
        return labelStylePane;
    }

    protected JPanel createJPanel4Position() {
        // ��ǩλ��:����������.
        bottomButton = new UIRadioButton(Inter.getLocText("StyleAlignment-Bottom"));
        leftButton = new UIRadioButton(Inter.getLocText("StyleAlignment-Left"));
        rigtButton = new UIRadioButton(Inter.getLocText("StyleAlignment-Right"));
        topButton = new UIRadioButton(Inter.getLocText("StyleAlignment-Top"));
        centerButton = new UIRadioButton(Inter.getLocText("Center"));

        ButtonGroup bg = new ButtonGroup();
        bg.add(bottomButton);
        bg.add(leftButton);
        bg.add(rigtButton);
        bg.add(topButton);
        bg.add(centerButton);
        topButton.setSelected(true);
        
        JPanel buttonPane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        buttonPane.add(new UILabel(Inter.getLocText(new String[]{"Label", "Layout"}) + ":"));
        buttonPane.add(bottomButton);
        buttonPane.add(leftButton);
        buttonPane.add(rigtButton);
        buttonPane.add(topButton);
        buttonPane.add(centerButton);

        return buttonPane;
    }

   

    public void populate(AttrContents seriesAttrContents) {
    	super.populate(seriesAttrContents);
        String dataLabel = seriesAttrContents.getSeriesLabel();
        if (dataLabel != null) {
            for (int i = 0; i < ChartConstants.DELIMITERS.length; i++) {
                String delimiter = ChartConstants.DELIMITERS[i];
                if (delimiterBox != null && dataLabel.contains(delimiter)) {
                    delimiterBox.setSelectedItem(delimiter);
                    break;
                }
            }
            // ��ǰ�Ļ��з� ${BR}
            if (delimiterBox != null && dataLabel.contains(ChartConstants.BREAKLINE_PARA)) {
                delimiterBox.setSelectedItem(ChartConstants.DELIMITERS[3]);
            }

            if (showCategoryNameCB != null) {
                showCategoryNameCB.setSelected(dataLabel.contains(ChartConstants.CATEGORY_PARA));
            }
            if (showSeriesNameCB != null) {
                showSeriesNameCB.setSelected(dataLabel.contains(ChartConstants.SERIES_PARA));
            }
        } else {
            if (showCategoryNameCB != null) {
                showCategoryNameCB.setSelected(false);
            }
            if (showSeriesNameCB != null) {
                showSeriesNameCB.setSelected(false);
            }
        }

        int position = seriesAttrContents.getPosition();
        if (bottomButton != null && position == Constants.BOTTOM) {
            bottomButton.setSelected(true);
        } else if (topButton != null && position == Constants.TOP) {
            topButton.setSelected(true);
        } else if (rigtButton != null && position == Constants.RIGHT) {
            rigtButton.setSelected(true);
        } else if (leftButton != null && position == Constants.LEFT) {
            leftButton.setSelected(true);
        } else if (centerButton != null && position == Constants.CENTER) {
            centerButton.setSelected(true);
        }

        if (stylePane != null) {
            stylePane.populate(seriesAttrContents);
        }
    }
    
    

    public void update(AttrContents seriesAttrContents) {
    	String contents = StringUtils.EMPTY;
    	String delString = Utils.objectToString(delimiterBox.getSelectedItem());
    	if (delString.contains(ChartConstants.DELIMITERS[3])) {
    		delString = ChartConstants.BREAKLINE_PARA;
    	} else if (delString.contains(ChartConstants.DELIMITERS[SPACE])) {
    		delString = StringUtils.BLANK;
    	}
    	// �������� ��label�����
    	if ((showCategoryNameCB != null && showCategoryNameCB.isSelected())) {
    		contents += ChartConstants.CATEGORY_PARA + delString;
    	}
    	if (showSeriesNameCB != null && showSeriesNameCB.isSelected()) {
    		contents += ChartConstants.SERIES_PARA + delString;
    	}
    	if (showValueCB != null && showValueCB.isSelected()) {
    		contents += ChartConstants.VALUE_PARA + delString;
    	}
    	if (showPercent != null && showPercent.isSelected()) {
    		contents += ChartConstants.PERCENT_PARA + delString;
    	}
    	if (contents.contains(delString)) {
    		contents = contents.substring(0, contents.lastIndexOf(delString));
    	}

    	if (topButton != null && topButton.isSelected()) {
    		seriesAttrContents.setPosition(Constants.TOP);
    	} else if (leftButton != null && leftButton.isSelected()) {
    		seriesAttrContents.setPosition(Constants.LEFT);
    	} else if (bottomButton != null && bottomButton.isSelected()) {
    		seriesAttrContents.setPosition(Constants.BOTTOM);
    	} else if (rigtButton != null && rigtButton.isSelected()) {
    		seriesAttrContents.setPosition(Constants.RIGHT);
    	} else if (centerButton != null && centerButton.isSelected()) {
    		seriesAttrContents.setPosition(Constants.CENTER);
    	}

    	if (stylePane != null) {
    		stylePane.update(seriesAttrContents);
    	}

    	seriesAttrContents.setSeriesLabel(contents);
    	seriesAttrContents.setFormat(format);
    	seriesAttrContents.setPercentFormat(percentFormat);
    }

    public void checkGuidBox() {

    }
}
