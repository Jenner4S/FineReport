package com.fr.design.mainframe.chart.gui.style.series;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.Formula;
import com.fr.chart.base.AreaColor;
import com.fr.chart.base.ChartBaseUtils;
import com.fr.chart.chartglyph.MapHotAreaColor;
import com.fr.design.constants.UIConstants;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ipoppane.PopupHider;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import com.fr.design.style.color.ColorControlWindow;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.design.utils.gui.GUICoreUtils;

public class UIColorPickerPane extends BasicPane implements UIObserver {
	private static final int MARGIN_TOP = 10;
	private static final int OFF_HEIGHT = 6;
	private static final int COLOR_REC_HEIGHT = 40;
	private static final int COLOR_REC_WIDTH = 30;
	protected static final int TEXTFIELD_HEIGHT = 20;
	protected static final int TEXTFIELD_WIDTH = 120;
	private static final int LAYOUR_DET = 6;
	private static final double VALUE = 100;

	protected ArrayList<JComponent> textFieldList;
	private List<ColorRecButton> colorRecList = new ArrayList<ColorRecButton>();

	private JPanel upControlPane;
	private TextFieldGroupPane textGroup;
	private ColorGroupPane colorGroup;

	private UIButtonGroup<Integer> designTypeButtonGroup;
	private ColorSelectBox fillStyleCombox;
	private UINumberDragPane regionNumPane;
	private JPanel stagePanel = null;
	private ChangeListener changeListener;

	public UIColorPickerPane() {
		fillStyleCombox = this.getColorSelectBox();
		fillStyleCombox.setSelectObject(Color.BLUE);
		fillStyleCombox.addSelectChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				UIColorPickerPane.this.refreshGroupPane(getColorArray(fillStyleCombox.getSelectObject(),
						regionNumPane.updateBean().intValue()), getValueArray(regionNumPane.updateBean().intValue()));
			}
		});
		designTypeButtonGroup = new UIButtonGroup<Integer>(new String[]{Inter.getLocText("FR-Chart-Mode_Auto"), Inter.getLocText("FR-Chart-Mode_Custom")}, new Integer[]{0, 1});
		designTypeButtonGroup.setSelectedIndex(0);
		designTypeButtonGroup.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (designTypeButtonGroup.getSelectedIndex() == 0) {
					UIColorPickerPane.this.remove(textGroup);
					UIColorPickerPane.this.remove(colorGroup);
				} else {
					UIColorPickerPane.this.add(textGroup);
					UIColorPickerPane.this.add(colorGroup);
					int value = regionNumPane.updateBean().intValue();
					UIColorPickerPane.this.refreshGroupPane(getColorArray(fillStyleCombox.getSelectObject(), (int) value), getValueArray((int) value));
					UIColorPickerPane.this.initContainerLister();
                }
				refreshPane();
			}
		});

		regionNumPane = new UINumberDragPane(1, 6) {
			@Override
			public void userEvent(double value) {
				UIColorPickerPane.this.refreshGroupPane(getColorArray(fillStyleCombox.getSelectObject(), (int) value), getValueArray((int) value));
				UIColorPickerPane.this.initContainerLister();
			}
		};

		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = {p, f};
		double[] rowSize = {p, p, p};
		Component[][] components = new Component[][]{
				new Component[]{new BoldFontTextLabel(Inter.getLocText("FR_Chart-Data_Range_configuration")), designTypeButtonGroup},
				new Component[]{new BoldFontTextLabel(Inter.getLocText(new String[]{"FR-Chart-Color_Subject", "FR-Chart-Color_Color"})), fillStyleCombox},
				new Component[]{new BoldFontTextLabel(Inter.getLocText("FR-Chart-Value_Divided_stage")), regionNumPane},
		};
		upControlPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);

		this.textFieldList = this.getTextFieldList();
		this.textGroup = new TextFieldGroupPane();
		this.colorGroup = new ColorGroupPane();
		this.setLayout(layout);
		this.add(upControlPane);
		int number = regionNumPane.updateBean().intValue();
		Color[] colors = getColorArray(fillStyleCombox.getSelectObject(), number);

		refreshGroupPane(colors, getValueArray(number));
	}
	
	public UIColorPickerPane(String meterString){

		fillStyleCombox = this.getColorSelectBox();
		fillStyleCombox.setSelectObject(Color.BLUE);
		fillStyleCombox.addSelectChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				UIColorPickerPane.this.refreshGroupPane(getColorArray(fillStyleCombox.getSelectObject(),
						regionNumPane.updateBean().intValue()), getValueArray(regionNumPane.updateBean().intValue()));
			}
		});
		designTypeButtonGroup = new UIButtonGroup<Integer>(new String[]{Inter.getLocText("FR-Chart-Mode_Auto"), Inter.getLocText("FR-Chart-Mode_Custom")}, new Integer[]{0, 1});
		designTypeButtonGroup.setSelectedIndex(0);
		designTypeButtonGroup.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (designTypeButtonGroup.getSelectedIndex() == 0) {
					UIColorPickerPane.this.remove(textGroup);
					UIColorPickerPane.this.remove(colorGroup);
				} else {
					UIColorPickerPane.this.add(textGroup);
					UIColorPickerPane.this.add(colorGroup);
					int value = regionNumPane.updateBean().intValue();
					UIColorPickerPane.this.refreshGroupPane(getColorArray(fillStyleCombox.getSelectObject(), (int) value), getValueArray((int) value));
                    UIColorPickerPane.this.initContainerLister();
                }
				refreshPane();
			}
		});

		regionNumPane = new UINumberDragPane(1, 6) {
			@Override
			public void userEvent(double value) {
				UIColorPickerPane.this.refreshGroupPane(getColorArray(fillStyleCombox.getSelectObject(), (int) value), getValueArray((int) value));
				UIColorPickerPane.this.initContainerLister();
			}
		};

		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = {p, f};
		double[] rowSize = {p};
		Component[][] tmpComp = new Component[][]{new Component[]{new BoldFontTextLabel(Inter.getLocText("FR-Chart-Value_Divided_stage")), regionNumPane}};
		
		stagePanel = TableLayoutHelper.createTableLayoutPane(tmpComp, rowSize, columnSize);
		
		Component[][] components = new Component[][]{
				new Component[]{new BoldFontTextLabel(Inter.getLocText("FR-Chart-Value_Tick_And_Color")), designTypeButtonGroup},
		};
		upControlPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);

		this.textGroup = new TextFieldGroupPane();
		this.colorGroup = new ColorGroupPane();
		this.setLayout(layoutMeter);
		this.add(upControlPane);
		this.add(stagePanel);
		int number = regionNumPane.updateBean().intValue();
		Color[] colors = getColorArray(fillStyleCombox.getSelectObject(), number);

		this.textFieldList = this.getTextFieldList();
		refreshGroupPane(colors, getValueArray(number));
	
	}

	protected ArrayList getTextFieldList(){
		return new ArrayList<ColorPickerPaneNumFiled>();
	}

	protected void setTextValue4Index(int index,String value){
		((ColorPickerPaneNumFiled)textFieldList.get(index)).setText(StringUtils.cutStringStartWith(value,"="));
	}

	protected String getValue4Index(int i){
		return ((ColorPickerPaneNumFiled)textFieldList.get(i)).getText();
	}

	protected JComponent getNewTextFieldComponent(int i,String value){
		ColorPickerPaneNumFiled textField = new ColorPickerPaneNumFiled();
		textField.setBounds(0, i * 2 * TEXTFIELD_HEIGHT, TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT);
		textField.setText(StringUtils.cutStringStartWith(value,"="));
		return textField;
	}

	/**
	 * ���ñ仯�ı�����ɫ
	 * */
	protected void setBackgroundUIColor(int index,Color color) {
		((ColorPickerPaneNumFiled)textFieldList.get(index)).setBackgroundUIColor(color);
	}


	private void refreshPane() {
		this.validate();
		this.repaint();
		this.revalidate();
	}

	protected ColorSelectBox getColorSelectBox(){
		return new ColorSelectBox(100);
	}

	/**
	 * ����Ԥ��Ľ����С.
	 */
	public Dimension getPreferredSize() {
		if (designTypeButtonGroup.getSelectedIndex() == 0) {
			return new Dimension(colorGroup.getPreferredSize().width + textGroup.getPreferredSize().width, upControlPane.getPreferredSize().height);
		} else {
			int extra = stagePanel == null ? 0 : stagePanel.getPreferredSize().height + this.MARGIN_TOP;
			return new Dimension(colorGroup.getPreferredSize().width + textGroup.getPreferredSize().width,
					extra + textGroup.getPreferredSize().height + upControlPane.getPreferredSize().height + OFF_HEIGHT);
		}
	}

	/**
	 * ������Ǽ�һ���۲��߼����¼�
	 *
	 * @param listener �۲��߼����¼�
	 */
	public void registerChangeListener(final UIObserverListener listener) {
		changeListener = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				listener.doChange();
			}
		};
		designTypeButtonGroup.addChangeListener(changeListener);
		fillStyleCombox.addSelectChangeListener(changeListener);
		regionNumPane.addChangeListener(changeListener);
		colorGroup.addSelectionChangeListener(changeListener);
	}

	/**
	 * ����Ƿ���Ҫ��Ӧ��ӵĹ۲����¼�
	 *
	 * @return �����Ҫ��Ӧ�۲����¼��򷵻�true�����򷵻�false
	 */
	public boolean shouldResponseChangeListener() {
		return true;
	}

	private class ColorRecButton extends JLabel implements PopupHider {
		private Color color;
		private boolean isLast;
		private boolean isRollover;
		private ColorControlWindow popupWin;
		private List<ChangeListener> changeListenerList = new ArrayList<ChangeListener>();

		public ColorRecButton(Color color, boolean isLast) {
			super(StringUtils.BLANK);
			this.setFocusable(true);
			this.setHorizontalAlignment(SwingConstants.CENTER);
			this.setVerticalAlignment(SwingConstants.CENTER);
			this.color = color;
			this.isLast = isLast;

			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					if (!isColorArea(e.getPoint())) {
						return;
					}
					requestFocus();
					isRollover = true;
					repaint();
				}

				@Override
				public void mouseExited(MouseEvent e) {
					isRollover = false;
					repaint();
				}

				public void mouseReleased(MouseEvent e) {
					if (!isColorArea(e.getPoint())) {
						return;
					}
					popupWin = ColorRecButton.this.getColorControlWindow();
					GUICoreUtils.showPopupMenu(popupWin, ColorRecButton.this, 0, ColorRecButton.this.getSize().height);
				}
			});
		}

		private boolean isColorArea(Point point) {
			return point.getX() <= COLOR_REC_WIDTH;
		}

		private ColorControlWindow getColorControlWindow() {
			if (this.popupWin == null) {
				this.popupWin = new ColorControlWindow(false, ColorRecButton.this) {
					@Override
					protected void colorChanged() {
						ColorRecButton.this.color = this.getColor();
						hidePopupMenu();
						fireChangeListener();
						ColorRecButton.this.repaint();
					}

				};
			}

			return popupWin;
		}

		public Color getPaintColor() {
			return this.color;
		}

		public void hidePopupMenu() {
			if (popupWin != null) {
				popupWin.setVisible(false);
			}
			popupWin = null;
		}

		public void addSelectChangeListener(ChangeListener changeListener) {
			this.changeListenerList.add(changeListener);
		}

		public void removeSelectChangeListener(ChangeListener changeListener) {
			this.changeListenerList.remove(changeListener);
		}

		public void fireChangeListener() {
			if (!changeListenerList.isEmpty()) {
				ChangeEvent evt = new ChangeEvent(this);
				for (int i = 0; i < changeListenerList.size(); i++) {
					this.changeListenerList.get(i).stateChanged(evt);
				}
			}
		}

		@Override
		public void paint(Graphics g) {
			int width = this.getWidth();
			int height = this.getHeight();
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(color);
			g2d.fillRect(1, 1, COLOR_REC_WIDTH - 1, COLOR_REC_HEIGHT - 1);
			if (isRollover) {
				g2d.setColor(UIConstants.FLESH_BLUE);
				g2d.drawRect(0, 0, COLOR_REC_WIDTH, COLOR_REC_HEIGHT);
				g2d.setColor(UIConstants.LINE_COLOR);
				g2d.drawLine(COLOR_REC_WIDTH, 0, width, 0);
				g2d.drawLine(COLOR_REC_WIDTH, COLOR_REC_HEIGHT, width, height);
				if (isLast) {
					g2d.drawLine(COLOR_REC_WIDTH, COLOR_REC_HEIGHT - 1, width, height - 1);
				}
			} else {
				g2d.setColor(UIConstants.LINE_COLOR);
				g2d.drawLine(0, 0, width, 0);
				g2d.drawLine(0, 0, 0, height);
				g2d.drawLine(COLOR_REC_WIDTH, 0, COLOR_REC_WIDTH, height);
				if (isLast) {
					g2d.drawLine(0, height - 1, width, height - 1);
				}
			}
			super.paint(g);
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(COLOR_REC_HEIGHT + COLOR_REC_WIDTH, COLOR_REC_HEIGHT);
		}
	}

	private class ColorGroupPane extends JPanel {
		private static final long serialVersionUID = 9222766209646008907L;

		public void refreshColorGroupPane(Color[] values) {
			for (int i = 0; i < colorRecList.size(); i++) {
				remove(colorRecList.get(i));
			}
			colorRecList.clear();

			for (int i = 0; i < values.length; i++) {
				ColorRecButton rec = new ColorRecButton(values[i], i == values.length - 1);
				rec.setBounds(0, i * rec.getPreferredSize().height, rec.getPreferredSize().width, rec.getPreferredSize().height);
				this.add(rec);
				colorRecList.add(rec);
			}
			this.repaint();
		}

		public ColorGroupPane() {
			this.setLayout(null);
		}

		@Override
		public Dimension getPreferredSize() {
			Dimension dim = new Dimension();
			dim.width = COLOR_REC_WIDTH + COLOR_REC_HEIGHT;
			dim.height = colorRecList.size() * COLOR_REC_HEIGHT;
			return dim;
		}

		public void addSelectionChangeListener(ChangeListener l) {
			for (int i = 0; i < colorRecList.size(); i++) {
				colorRecList.get(i).addSelectChangeListener(l);
			}
		}

		public void removeSelectionChangeListener(ChangeListener l) {
			for (int i = 0; i < colorRecList.size(); i++) {
				colorRecList.get(i).removeSelectChangeListener(l);
			}
		}
	}

	private class TextFieldGroupPane extends JPanel {
		private static final long serialVersionUID = -8390474551829486013L;

		public void refreshTextGroupPane(Formula[] values) {

			if (values.length == textFieldList.size()) {
				for (int i = 0; i < textFieldList.size(); i++) {
					setTextValue4Index(i,values[i].toString());
				}
			} else {
				for (int i = 0; i < textFieldList.size(); i++) {
					remove(textFieldList.get(i));
				}
				textFieldList.clear();

				for (int i = 0; i < values.length; i++) {
					JComponent com = getNewTextFieldComponent(i, values[i].toString());
					textFieldList.add(com);
					this.add(com);
				}
			}
			this.repaint();
		}
		
		/**
		 * ������Щ ȷ��ÿ��Field�������Сֵ. ���Ҹı䱳����ɫ.
		 */
		public void checkEveryFiledMinMax() {
			
			double forValue = Double.MAX_VALUE;
			double backValue = -Double.MAX_VALUE;
			for(int i = 0, size = textFieldList.size(); i < size; i++) {// check �Ƿ�ϸ�, Ȼ���� �Ƿ�ı���ɫ,
				if(i == size - 1) {
					backValue = -Double.MAX_VALUE;
				} else {
					Number backNumber = ChartBaseUtils.formula2Number(new Formula(getValue4Index(i+1)));
					if(backNumber != null){
						backValue = backNumber.doubleValue();
					}
				}
				
				Number number =  ChartBaseUtils.formula2Number(new Formula(getValue4Index(i)));
				
				if(number != null) {
					double value = number.doubleValue();
					if(value < forValue && value > backValue) {
						setBackgroundUIColor(i,Color.white);
					} else {
						setBackgroundUIColor(i, Color.red);
					}
					forValue = value;
				}else{
					//��ʽ����
					setBackgroundUIColor(i,Color.white);
				}
			}
			this.repaint();
		}

		public TextFieldGroupPane() {
			this.setLayout(null);
		}

		@Override
		public Dimension getPreferredSize() {
			Dimension dim = new Dimension();
			dim.width = TEXTFIELD_WIDTH;
			dim.height = (textFieldList.size() * 2 - 1) * TEXTFIELD_HEIGHT;
			return dim;
		}


	}

	@Override
	protected String title4PopupWindow() {
		// TODO Auto-generated method stub
		return null;
	}

	private LayoutManager layoutMeter = new LayoutManager() {
		@Override
		public void removeLayoutComponent(Component comp) {

		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return getPreferredSize();
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return null;
		}

		@Override
		public void layoutContainer(Container parent) {
			upControlPane.setBounds(0, 0, parent.getPreferredSize().width, upControlPane.getPreferredSize().height);
			stagePanel.setBounds(0,upControlPane.getPreferredSize().height + LAYOUR_DET, parent.getPreferredSize().width, stagePanel.getPreferredSize().height);
			colorGroup.setBounds(0, MARGIN_TOP + upControlPane.getPreferredSize().height + stagePanel.getPreferredSize().height + 2 * LAYOUR_DET, colorGroup.getPreferredSize().width, colorGroup.getPreferredSize().height + upControlPane.getPreferredSize().height);
			textGroup.setBounds(colorGroup.getPreferredSize().width, upControlPane.getPreferredSize().height+ stagePanel.getPreferredSize().height + 2 * LAYOUR_DET, textGroup.getPreferredSize().width, textGroup.getPreferredSize().height);
		}

		@Override
		public void addLayoutComponent(String name, Component comp) {

		}
	};

	private LayoutManager layout = new LayoutManager() {
		@Override
		public void removeLayoutComponent(Component comp) {

		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return getPreferredSize();
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return null;
		}

		@Override
		public void layoutContainer(Container parent) {
			upControlPane.setBounds(0, 0, parent.getPreferredSize().width, upControlPane.getPreferredSize().height);
			colorGroup.setBounds(0, MARGIN_TOP + upControlPane.getPreferredSize().height + LAYOUR_DET, colorGroup.getPreferredSize().width, colorGroup.getPreferredSize().height + upControlPane.getPreferredSize().height);
			textGroup.setBounds(colorGroup.getPreferredSize().width, upControlPane.getPreferredSize().height + LAYOUR_DET, textGroup.getPreferredSize().width, textGroup.getPreferredSize().height);
		}

		@Override
		public void addLayoutComponent(String name, Component comp) {

		}
	};


	/**
	 *ˢ����ɫѡȡ��
	 * @param colorArray ��ɫֵ
	 * @param valueArray ֵ����
	 */
	public void refreshGroupPane(Color[] colorArray, Formula[] valueArray) {
		colorGroup.refreshColorGroupPane(colorArray);
		textGroup.refreshTextGroupPane(valueArray);

		if (changeListener != null) {
			colorGroup.addSelectionChangeListener(changeListener);
		}
		refreshPane();
	}

	public int getDesignType(){
		return this.designTypeButtonGroup.getSelectedIndex();
	}
	
	public void populateBean(MapHotAreaColor hotAreaColor) {
		Color mainColor = hotAreaColor.getMainColor();

		fillStyleCombox.setSelectObject(mainColor);
		designTypeButtonGroup.setSelectedIndex(hotAreaColor.getUseType());

		double value = (double) hotAreaColor.getAreaNumber();
		UIColorPickerPane.this.add(textGroup);
		UIColorPickerPane.this.add(colorGroup);
		Color[] colors = hotAreaColor.initColor();
		Formula[] values = hotAreaColor.initValues();
		refreshGroupPane(colors, values);
		this.initContainerLister();
		regionNumPane.populateBean(value);
		refreshPane();
	}
	
	private void initContainerLister(){
		Container container = UIColorPickerPane.this;
        while (!(container instanceof ChartStylePane)) {
            if (container.getParent() == null) {
                break;
            }
            container = container.getParent();
        }
        ((ChartStylePane)container).initAllListeners();
	}

	public void updateBean(MapHotAreaColor hotAreaColor) {
		hotAreaColor.setMainColor(fillStyleCombox.getSelectObject());
		hotAreaColor.setUseType(designTypeButtonGroup.getSelectedIndex());
		hotAreaColor.setAreaNumber(regionNumPane.updateBean().intValue());

		if (hotAreaColor.getUseType() == MapHotAreaColor.CUSTOM) {
			if(!checkInOrder()) {// �ȼ��˳��,  ������в���ȷ, �����������.
				return;
			}
			hotAreaColor.clearColor();
			Color[] colors = getColors4Custom(fillStyleCombox.getSelectObject(), regionNumPane.updateBean().intValue());
			Formula[] value = getValueArray(regionNumPane.updateBean().intValue());

			for (int i = 0; i < colors.length; i++) {
				hotAreaColor.addAreaColor(new AreaColor(value[i], value[i + 1], colors[i]));
			}
		}
	}

	// ��� ����˳��.
	private boolean checkInOrder() {
		textGroup.checkEveryFiledMinMax();
		
		boolean allInOrder = true;

		double maxValue = Double.MAX_VALUE;
		for (int i = 0, size = textFieldList.size(); i < size; i++) {
			if(StringUtils.isEmpty(getValue4Index(i))){
				return false;
			}
			Number number =  ChartBaseUtils.formula2Number(new Formula(getValue4Index(i)));
			if(number != null) {
				double value = number.doubleValue();
				if(value > maxValue) {
					allInOrder = false;
					break;
				} else {
					maxValue = value;
				}
			}
		}
		return allInOrder;
	}

	private Color[] getColors4Custom(Color color, int sum) {
		Color[] autos = getColorArray(color, sum);

		Color[] colors = new Color[sum];
		for (int i = 0; i < sum; i++) {
			if (i >= colorRecList.size()) {
				colors[i] = autos[i];
			} else {

				colors[i] = colorRecList.get(i).getPaintColor();
			}
		}

		return colors;
	}

	private Color[] getColorArray(Color color, int sum) {
		return ChartBaseUtils.createColorsWithAlpha(color, sum);
	}

	private Formula[] getValueArray(int count) {
		Formula[] valueArray = new Formula[count + 1];
		for (int i = 0; i < valueArray.length; i++) {
			if (i >= textFieldList.size()) {
				valueArray[i] = new Formula(new Double((count + 1 - i) * VALUE).toString());
			} else {
				valueArray[i] = new Formula(getValue4Index(i));
			}
		}
		return valueArray;
	}


	/**
	 * ���Գ���������
	 * @param args ����
	 */
	public static void main(String... args) {
		JFrame jf = new JFrame("test");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel content = (JPanel) jf.getContentPane();
		content.setLayout(new BorderLayout());
		UIColorPickerPane pp = new UIColorPickerPane();
		content.add(pp, BorderLayout.CENTER);
		GUICoreUtils.centerWindow(jf);
		jf.setSize(400, 400);
		jf.setVisible(true);
	}

}
