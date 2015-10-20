package com.fr.design.mainframe.chart.gui.style;

import java.awt.*;

import com.fr.chart.base.AttrFillStyle;
import com.fr.chart.base.ChartConstants;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;

/**
 * Ԥ�����ͼ����ɫ����, ���к����Ա���ChartFillStylePane ��Ҫ�Ĳ�ͬ���Ǳ�ǩ��λ��.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2013-8-21 ����03:16:27
 */
public class ChartPreFillStylePane extends ChartFillStylePane {
	
	public ChartPreFillStylePane() {
		
	}
	
	protected void initLayout() {// �����Ƿ�����Ԥ�� �����沼��, �����Ա� ������ͬ.
		customPane.setPreferredSize(new Dimension(200, 200));
		colorGradient.setPreferredSize(new Dimension(120, 30));
		
		double p = TableLayout.PREFERRED;
		double[] columnSize = {p, p };
		double[] rowSize = { p, p, p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(" " + Inter.getLocText("ColorMatch")), null},
                new Component[]{null, customPane},
        };
        
        this.add(TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize), BorderLayout.WEST);
	}

    public void populateBean(AttrFillStyle condition) {
        styleSelectBox.setSelectedIndex(styleSelectBox.getItemCount()-1);

        if(condition == null || condition.getColorStyle() == ChartConstants.COLOR_DEFAULT) {
            colorAcc.populateBean(ChartConstants.CHART_COLOR_ARRAY);// �½�ʱ ����Ĭ����ʽ
            accButton.setSelected(true);
            gradientButton.setSelected(false);
            cardLayout.show(changeColorSetPane, "acc");

            colorGradient.getSelectColorPointBtnP1().setColorInner(Color.white);
            colorGradient.getSelectColorPointBtnP2().setColorInner(Color.black);// �ؼ��е�λ����Ч.
        } else {
            int colorStyle = condition.getColorStyle();
            gradientButton.setSelected(colorStyle == ChartConstants.COLOR_GRADIENT);
            accButton.setSelected(colorStyle == ChartConstants.COLOR_ACC);

            int colorSize = condition.getColorSize();
            if(colorSize == 2 && gradientButton.isSelected() ) {
                cardLayout.show(changeColorSetPane, "gradient");

                Color endColor = condition.getColorIndex(1);
                Color startColor = condition.getColorIndex(0);
                colorGradient.getSelectColorPointBtnP1().setColorInner(startColor);
                colorGradient.getSelectColorPointBtnP2().setColorInner(endColor);
                colorGradient.repaint();
            } else if(colorSize > 2 && accButton.isSelected()){
                cardLayout.show(changeColorSetPane, "acc");

                Color[] colors = new Color[colorSize];
                for(int i = 0; i < colorSize; i++) {
                    colors[i] = condition.getColorIndex(i);
                }
                colorAcc.populateBean(colors);
            }
        }
    }

    @Override
    public AttrFillStyle updateBean() {
        AttrFillStyle condition = new AttrFillStyle();
        condition.clearColors();

        if(gradientButton.isSelected()) {
            condition.setColorStyle(ChartConstants.COLOR_GRADIENT);
            Color start = colorGradient.getSelectColorPointBtnP1().getColorInner();
            Color end = colorGradient.getSelectColorPointBtnP2().getColorInner();
            condition.addFillColor(start);
            condition.addFillColor(end);
        } else {
            condition.setColorStyle(ChartConstants.COLOR_ACC);

            Color[] colors = colorAcc.updateBean();
            for(int i = 0, length = colors.length; i < length; i++) {
                condition.addFillColor(colors[i]);
            }
        }

        return condition;
    }
}
