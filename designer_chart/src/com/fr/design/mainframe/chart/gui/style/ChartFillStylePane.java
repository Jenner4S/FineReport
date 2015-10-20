package com.fr.design.mainframe.chart.gui.style;

import com.fr.base.ChartPreStyleManagerProvider;
import com.fr.base.ChartPreStyleServerManager;
import com.fr.base.Utils;
import com.fr.chart.base.AttrFillStyle;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.base.ChartPreStyle;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.background.gradient.GradientBar;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * ͼ����ɫ(����ϵ��, Ԥ����������) ��ɫ����.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version ����ʱ�䣺2013-8-20 ����05:10:54
 */
public class ChartFillStylePane extends BasicBeanPane<AttrFillStyle>{
	
	protected UIComboBox styleSelectBox;
	protected JPanel customPane;
    protected JPanel changeColorSetPane;
	protected GradientBar colorGradient;

    protected CardLayout cardLayout;

    protected UIButton accButton;
    protected UIButton gradientButton;

    protected ChartAccColorPane  colorAcc;
	
	public ChartFillStylePane() {
		this.setLayout(new BorderLayout());
		
		styleSelectBox = new UIComboBox(getNameObj());
		customPane = new JPanel(FRGUIPaneFactory.createBorderLayout());
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonPane.add(accButton = new UIButton(Inter.getLocText("FR-Designer_Chart_Acc_Set")));
		buttonPane.add(gradientButton = new UIButton(Inter.getLocText("FR-Designer_Gradient-Color")));
		customPane.add(buttonPane, BorderLayout.NORTH);
		
		changeColorSetPane = new JPanel(cardLayout = new CardLayout());
		changeColorSetPane.add(colorGradient = new GradientBar(4, 130), "gradient");
		changeColorSetPane.add(colorAcc = new ChartAccColorPane(), "acc");
		cardLayout.show(changeColorSetPane, "acc");
		customPane.add(changeColorSetPane, BorderLayout.CENTER);
		
		customPane.setVisible(false);
		accButton.setSelected(true);
		
		initListener();
		initLayout();
	}
	
	private void initListener() {
		styleSelectBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				customPane.setVisible(styleSelectBox.getSelectedIndex() == styleSelectBox.getItemCount() - 1);
			}
		});
		accButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				accButton.setSelected(true);
				gradientButton.setSelected(false);
				cardLayout.show(changeColorSetPane, "acc");
			}
		});
		
		gradientButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gradientButton.setSelected(true);
				accButton.setSelected(false);
				cardLayout.show(changeColorSetPane, "gradient");
			}
		});
	}
	
	protected void initLayout() {
		customPane.setPreferredSize(new Dimension(200, 130));
		colorGradient.setPreferredSize(new Dimension(120, 30));
		
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { f };
		double[] rowSize = { p, p};
        Component[][] components = new Component[][]{
                new Component[]{styleSelectBox},
                new Component[]{customPane}
        } ;
        JPanel panel = TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"ColorMatch"},components,rowSize,columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);
	}
	
	@Override
	public Dimension getPreferredSize() {
		if(styleSelectBox.getSelectedIndex() != styleSelectBox.getItemCount() - 1) {
			return new Dimension(styleSelectBox.getPreferredSize().width, 50);
		}
		return super.getPreferredSize();
	}

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText(new String[]{"Chart", "Color"});
	}

    private String[] getNameObj() {
        ChartPreStyleManagerProvider manager = ChartPreStyleServerManager.getProviderInstance();
        ArrayList<String> nameArr = new ArrayList<String>();
        nameArr.add(Inter.getLocText("FR-Designer_DEFAULT"));
        Iterator keys = manager.names();
        while (keys.hasNext()) {
            Object key = keys.next();
            nameArr.add(Utils.objectToString(key));
        }
        nameArr.add(Inter.getLocText("FR-Designer_Custom"));
        return nameArr.toArray(new String[nameArr.size()]);
    }

	@Override
	public void populateBean(AttrFillStyle condition) {
        String fillStyleName = condition == null? "" : condition.getFillStyleName();
		if(StringUtils.isBlank(fillStyleName)){//���ݴ���
            if(condition == null || condition.getColorStyle() == ChartConstants.COLOR_DEFAULT) {
                styleSelectBox.setSelectedIndex(0);//Ĭ��

                colorAcc.populateBean(ChartConstants.CHART_COLOR_ARRAY);// �½�ʱ ����Ĭ����ʽ
                accButton.setSelected(true);
                gradientButton.setSelected(false);
                cardLayout.show(changeColorSetPane, "acc");

                colorGradient.getSelectColorPointBtnP1().setColorInner(Color.white);
                colorGradient.getSelectColorPointBtnP2().setColorInner(Color.black);// �ؼ��е�λ����Ч.
            } else {
                styleSelectBox.setSelectedIndex(styleSelectBox.getItemCount() - 1);

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
        }else{
            styleSelectBox.setSelectedItem(fillStyleName);
        }
	}

	@Override
	public AttrFillStyle updateBean() {
		AttrFillStyle condition = new AttrFillStyle();
		condition.clearColors();
		
		if(styleSelectBox.getSelectedIndex() == 0) {
			condition.setColorStyle(ChartConstants.COLOR_DEFAULT);
		} else if(styleSelectBox.getSelectedIndex() < styleSelectBox.getItemCount() - 1){
            ChartPreStyleManagerProvider manager = ChartPreStyleServerManager.getProviderInstance();
            Object preStyle = manager.getPreStyle(styleSelectBox.getSelectedItem());
            if(preStyle instanceof ChartPreStyle) {
                AttrFillStyle def = ((ChartPreStyle) preStyle).getAttrFillStyle();
                def.setFillStyleName(Utils.objectToString(styleSelectBox.getSelectedItem()));
                return def;
            }else{
                condition.setColorStyle(ChartConstants.COLOR_DEFAULT);
            }
        } else {
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
		}
		
		return condition;
	}

}
