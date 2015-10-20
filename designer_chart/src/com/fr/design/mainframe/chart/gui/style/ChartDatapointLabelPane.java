package com.fr.design.mainframe.chart.gui.style;

import com.fr.base.CoreDecimalFormat;
import com.fr.base.FRContext;
import com.fr.base.Style;
import com.fr.base.Utils;
import com.fr.chart.base.AttrContents;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartattr.Plot;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.gui.frpane.UIBubbleFloatPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.style.FormatPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.dialog.BasicPane;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.stable.Constants;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.HashMap;
import java.util.Map;

public class ChartDatapointLabelPane extends BasicPane{
	protected static final int SPACE = 4;
	protected static final int NEWLIEN = 3;

	private static Map<String, Integer> nameValueMap = new HashMap<String, Integer>();
	private static Map<Integer, String> valueNameMap = new HashMap<Integer, String>();
	
	protected ChartTextAttrPane textFontPane;
	
	protected UICheckBox isLabelShow;

	protected UIComboBox positionBox;

	protected UICheckBox isCategory;
	protected UICheckBox isSeries;
	protected UICheckBox isValue;
	protected UIButton valueFormatButton;
	protected UICheckBox isValuePercent;
	protected UIButton valuePercentFormatButton;
	
	protected FormatPane valueFormatPane;
	protected FormatPane percentFormatPane;
	protected Format valueFormat;
	protected Format percentFormat;
	
	protected ChartStylePane parent;

	protected UICheckBox isGuid;

	protected UIComboBox divideComoBox;
	
	protected JPanel labelPane;
	
	public ChartDatapointLabelPane() {
		// do nothing
	}
	
	public ChartDatapointLabelPane(String[] locationNameArray, Integer[] locationValueArray, Plot plot, ChartStylePane parent) {
		this.parent = parent;
		
		isLabelShow = new UICheckBox(Inter.getLocText("FR-Chart-Chart_Label"));
		if(locationNameArray != null && locationNameArray.length > 0 
				&& locationValueArray != null && locationValueArray.length > 0) {
			nameValueMap.clear();
			valueNameMap.clear();
			positionBox = new UIComboBox(locationNameArray);
			positionBox.setSelectedItem(locationValueArray[0]);
			for(int i = 0; i < locationNameArray.length; i++) {
				nameValueMap.put(locationNameArray[i], locationValueArray[i]);
				valueNameMap.put(locationValueArray[i], locationNameArray[i]);
			}
		}

        boolean isGuidline = plot.isSupportLeadLine();

        if(plot.isSupportCategoryFilter()) {
		    isCategory = new UICheckBox(Inter.getLocText("Chart-Category_Name"));
        }
		isSeries = new UICheckBox(Inter.getLocText("Chart-Series_Name"));
		isValue = new UICheckBox(Inter.getLocText("Chart-Use_Value"));
		isValue.setSelected(true);
		valueFormatButton = new UIButton(Inter.getLocText("Chart-Use_Format"));

        if(plot.isSupportValuePercent()) {
		    isValuePercent = new UICheckBox(Inter.getLocText("Chart-Value_Percent"));
		    valuePercentFormatButton = new UIButton(Inter.getLocText("Chart-Use_Format"));
			if (plot.isShowAllDataPointLabel()) {
				isValuePercent.setText(Inter.getLocText("Chart-Value_Conversion"));
			}
        }

        if (plot.isSupportDelimiter()) {
		    divideComoBox = new UIComboBox(ChartConstants.DELIMITERS);
        }
		textFontPane = new ChartTextAttrPane();
		
		if(isGuidline) {
			isGuid = new UICheckBox(Inter.getLocText("ChartF-Show_GuidLine"));
		}

        if (plot.isShowAllDataPointLabel()) {
            isSeries.setSelected(true);
            isGuid.setSelected(true);
        }
		
		initFormatListener();

		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;

		double[] columnSize = { p, f };
		double[] rowSize1 = { p,p,p,p,p,p,p,p};
        double[] rowSize2 = { p,p,p,p,p,p,p};

        Component[][] components = new Component[8][3];

        JPanel panel = null;

		if(positionBox != null) {
			JPanel positionPane = new JPanel(new BorderLayout(LayoutConstants.VGAP_MEDIUM, LayoutConstants.VGAP_MEDIUM));
			positionPane.add(new BoldFontTextLabel(Inter.getLocText("Chart-Layout_Position")), BorderLayout.WEST);
			positionPane.add(positionBox, BorderLayout.CENTER);
			components[0] = new Component[]{positionPane, null};
            if(isGuidline) {
            	positionBox.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						checkLeadLineWhenPositionChange();
					}
				});
            }
		}
		
		if(isGuidline) {
			components[1] = new Component[]{isSeries, null};
			components[2] = new Component[]{null, null};
		} else {
			components[1] = new Component[]{isCategory, null};
			components[2] = new Component[]{isSeries, null};
		}
        
        components[3] = new Component[]{isValue,valueFormatButton};
        components[4] = new Component[]{isValuePercent,valuePercentFormatButton} ;

        JPanel delimiterPane = new JPanel(new BorderLayout(LayoutConstants.VGAP_MEDIUM, LayoutConstants.VGAP_MEDIUM));
        if (plot.isSupportDelimiter()) {
            delimiterPane.add(new BoldFontTextLabel(Inter.getLocText("FR-Chart-Delimiter_Symbol")), BorderLayout.WEST);
            delimiterPane.add(divideComoBox, BorderLayout.CENTER);
        }
		
		if(isGuidline) {
            components[5] = new Component[]{isGuid,null};
            components[6] = new Component[]{delimiterPane,null};
            components[7] = new Component[]{textFontPane,null};
            labelPane = TableLayoutHelper.createTableLayoutPane(components,rowSize1,columnSize);
		} else {
			components[5] = new Component[]{delimiterPane,null};
			components[6] = new Component[]{textFontPane,null};
            labelPane = TableLayoutHelper.createTableLayoutPane(components,rowSize2,columnSize);
		}
		
		double[] row = {p,p};
		double[] col = {LayoutConstants.CHART_ATTR_TOMARGIN, f};
		panel = TableLayoutHelper.createTableLayoutPane(new Component[][]{
										new Component[]{isLabelShow,null},new Component[]{null, labelPane}}, row, col);
		
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER) ;

        isLabelShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkBoxUse();
			}
		});
	}

    // ��Ӳ㼶 ��ϵ
    private void getLabelPositionPane() {

    }

    private void getLabelContentPane() {

    }

    private void getShowGuidPane() {

    }

    private void getDelimPane() {

    }

    private void getLabelFontPane() {

    }

	private void checkLeadLineWhenPositionChange() {
		if(isGuid != null && positionBox != null) {
			isGuid.setSelected(positionBox.getSelectedIndex() != 0);
            isGuid.setEnabled(positionBox.getSelectedIndex() != 0);
		}
	}

    private void initValueFormat() {
        if(valueFormatButton != null) {
            valueFormatButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (!valueFormatButton.isEnabled()) {
                        return;
                    }

                    if(valueFormatPane == null) {
                        valueFormatPane =  new FormatPane();
                    }
                    Point comPoint = valueFormatButton.getLocationOnScreen();
                    Point arrowPoint = new Point(comPoint.x + valueFormatButton.getWidth(), comPoint.y + valueFormatButton.getHeight());
                    UIBubbleFloatPane<Style> pane = new UIBubbleFloatPane(Constants.LEFT, arrowPoint, valueFormatPane, 258, 209) {

                        @Override
                        public void updateContentPane() {
                            valueFormat = valueFormatPane.update();
                            parent.attributeChanged();
                        }
                    };
                    pane.show(ChartDatapointLabelPane.this, Style.getInstance(valueFormat));
                    super.mouseReleased(e);
                }
            });
        }
    }

    private void initPercentFormat() {
        if(valuePercentFormatButton != null) {
            valuePercentFormatButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (!valuePercentFormatButton.isEnabled()) {
                        return;
                    }

                    if(percentFormatPane == null) {
                        percentFormatPane =  new FormatPane();
                    }
                    Point comPoint = valuePercentFormatButton.getLocationOnScreen();
                    Point arrowPoint = new Point(comPoint.x + valuePercentFormatButton.getWidth(), comPoint.y + valuePercentFormatButton.getHeight());
                    UIBubbleFloatPane<Style> pane = new UIBubbleFloatPane(Constants.LEFT, arrowPoint, percentFormatPane, 258, 209) {
                        @Override
                        public void updateContentPane() {
                            percentFormat = percentFormatPane.update();
                            parent.attributeChanged();
                        }
                    };
                    pane.show(ChartDatapointLabelPane.this, Style.getInstance(percentFormat));
                    super.mouseReleased(e);
                    percentFormatPane.justUsePercentFormat();
                }
            });
        }
    }
	
	protected void initFormatListener() {
        initValueFormat();
        initPercentFormat();
	}

	@Override
	protected String title4PopupWindow() {
		return null;
	}

	public void populate(AttrContents attrContents) {
		isLabelShow.setSelected(true);
		String dataLabel = attrContents.getSeriesLabel();
		if (dataLabel != null) {
			for (int i = 0; i < ChartConstants.DELIMITERS.length; i++) {
				String delimiter = ChartConstants.DELIMITERS[i];
				if (divideComoBox != null && dataLabel.contains(delimiter)) {
					divideComoBox.setSelectedItem(delimiter);
					break;
				}
			}
			if (divideComoBox != null && dataLabel.contains(ChartConstants.BREAKLINE_PARA)) {
				divideComoBox.setSelectedItem(ChartConstants.DELIMITERS[3]);// ��ǰ�Ļ��з� ${BR}
			}

			if (isCategory != null) {
				isCategory.setSelected(dataLabel.contains(ChartConstants.CATEGORY_PARA));
			}
			if (isSeries != null) {
				isSeries.setSelected(dataLabel.contains(ChartConstants.SERIES_PARA));
			}
			if(isValue != null) {
				isValue.setSelected(dataLabel.contains(ChartConstants.VALUE_PARA));
			}
			if(isValuePercent != null) {
				isValuePercent.setSelected(dataLabel.contains(ChartConstants.PERCENT_PARA));
			}
		} else {
			noSelected();
		}

		int position = attrContents.getPosition();
		if(positionBox != null && valueNameMap.containsKey(position)) {
			positionBox.setSelectedItem(valueNameMap.get(position));
		}

		if (isGuid != null) {
			isGuid.setSelected(attrContents.isShowGuidLine());
            if(positionBox != null) {
                isGuid.setEnabled(positionBox.getSelectedIndex() != 0);
            }
		}

        valueFormat = attrContents.getFormat();
        percentFormat = attrContents.getPercentFormat();
	}
	
	private void noSelected() {
		if (isCategory != null) {
			isCategory.setSelected(false);
		}
		if (isSeries != null) {
			isSeries.setSelected(false);
		}
		if (isValue != null) {
			isValue.setSelected(false);
		}
		if (isValuePercent != null) {
			isValuePercent.setSelected(false);
		}
	}

    /**
     * ���boxʹ��.
     */
	public void checkBoxUse() {
		labelPane.setVisible(isLabelShow.isSelected());
        this.checkLeadLineWhenPositionChange();
	}

	public void populate(DataSeriesCondition attr) {
		if(attr == null) {
			isLabelShow.setSelected(false);
		}else if (attr instanceof AttrContents) {
			AttrContents attrContents = (AttrContents) attr;
			populate(attrContents);
		}
		
		if(textFontPane != null) {
			if(attr != null) {
				textFontPane.populate(((AttrContents)attr).getTextAttr());
			} else {
				FRFont font = FRContext.getDefaultValues().getFRFont();
				textFontPane.populate(font == null ? FRFont.getInstance() : font);
			}
		}

        labelPane.setVisible(isLabelShow.isSelected());
	}

	public AttrContents update() {
		if(!isLabelShow.isSelected()) {
			return null;
		}
		AttrContents attrContents = new AttrContents();
		String contents = StringUtils.EMPTY;
		String delString = StringUtils.BLANK;
		if (divideComoBox != null) {
			delString = Utils.objectToString(divideComoBox.getSelectedItem());
		}
		if (delString.contains(ChartConstants.DELIMITERS[3])) {
			delString = ChartConstants.BREAKLINE_PARA;
		} else if (delString.contains(ChartConstants.DELIMITERS[SPACE])) {
			delString = StringUtils.BLANK;
		}
		if ((isCategory != null && isCategory.isSelected())) {
			contents += ChartConstants.CATEGORY_PARA + delString;
		}
		if (isSeries != null && isSeries.isSelected()) {
			contents += ChartConstants.SERIES_PARA + delString;
		}
		if (isValue != null && isValue.isSelected()) {
			contents += ChartConstants.VALUE_PARA + delString;
		}
		if (isValuePercent != null && isValuePercent.isSelected()) {
			contents += ChartConstants.PERCENT_PARA + delString;
		}
		if (contents.contains(delString)) {
			contents = contents.substring(0, contents.lastIndexOf(delString));
		}
		if(positionBox != null && positionBox.getSelectedItem() != null) {
			if(nameValueMap.containsKey(positionBox.getSelectedItem())) {
				attrContents.setPosition(nameValueMap.get(positionBox.getSelectedItem()));
			}
		}
		attrContents.setSeriesLabel(contents);
		if(isGuid != null) {
			attrContents.setShowGuidLine(isGuid.isSelected());
		}
		if(valueFormat != null) {
			attrContents.setFormat(valueFormat);
		}
		if(percentFormat != null) {
			attrContents.setPercentFormat(percentFormat);
		}
		if(textFontPane != null){
			attrContents.setTextAttr(textFontPane.update());
		}
		updatePercentFormatpane(); //��ʽ����populate��ȷ
		return attrContents;
	}
	
	protected void updatePercentFormatpane(){
		if(isValuePercent!= null && isValuePercent.isSelected()){
            if(this.percentFormatPane == null){
                this.percentFormatPane = new FormatPane();
            }
            if(this.percentFormat == null){
                DecimalFormat defaultFormat = new CoreDecimalFormat(new DecimalFormat(), "#.##%");
                percentFormatPane.populateBean(defaultFormat);
                this.percentFormat = defaultFormat;
            }
		}
	}
}
