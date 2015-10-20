package com.fr.design.gui.style;

import com.fr.base.background.GradientBackground;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.backgroundpane.BackgroundSettingPane;
import com.fr.general.Background;
import com.fr.general.Inter;
import com.fr.design.style.background.gradient.GradientBar;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * @author zhou
 * @since 2012-5-30����10:36:21
 */
public class GradientPane extends BackgroundSettingPane {
	private static final long serialVersionUID = -6854603990673031897L;

    private static final int DEFAULT_GRADIENT_WIDTH = 185;

    private int gradientBarWidth = DEFAULT_GRADIENT_WIDTH;

	private GradientBar gradientBar;
	private UIButtonGroup<Integer> directionPane;

	public GradientPane() {
        constructPane();
	}

    public GradientPane(int gradientBarWidth) {
        this.gradientBarWidth = gradientBarWidth;
        constructPane();
    }

    private void constructPane(){
        String[] textArray = {Inter.getLocText("Utils-Left_to_Right"), Inter.getLocText("Utils-Top_to_Bottom")};
        Integer[] valueArray = {GradientBackground.LEFT2RIGHT, GradientBackground.TOP2BOTTOM};
        directionPane = new UIButtonGroup<Integer>(textArray, valueArray);
        directionPane.setSelectedIndex(0);
        gradientBar = new GradientBar(4, this.gradientBarWidth);

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p, p,};

        Component[][] components = new Component[][]{
                new Component[]{gradientBar, null},
                new Component[]{new UILabel(Inter.getLocText("Gradient-Direction") + ":"), directionPane}
        };
        JPanel Gradient = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.add(Gradient, BorderLayout.CENTER);
    }

	public void populateBean(Background background) {
		GradientBackground bg = (GradientBackground) background;
		this.gradientBar.getSelectColorPointBtnP1().setColorInner(bg.getStartColor());
		this.gradientBar.getSelectColorPointBtnP2().setColorInner(bg.getEndColor());
		directionPane.setSelectedItem(bg.getDirection());
		if (bg.isUseCell()) {
			return;
		}
		double startValue = (double) bg.getBeginPlace();
		double endValue = (double) bg.getFinishPlace();
		gradientBar.setStartValue(startValue);
		gradientBar.setEndValue(endValue);
        if(this.gradientBar.getSelectColorPointBtnP1() != null && this.gradientBar.getSelectColorPointBtnP2() != null){
            this.gradientBar.getSelectColorPointBtnP1().setX(startValue);
            this.gradientBar.getSelectColorPointBtnP2().setX(endValue);
        }
		this.gradientBar.repaint();
	}

	public GradientBackground updateBean() {
		GradientBackground gb = new GradientBackground(gradientBar.getSelectColorPointBtnP1().getColorInner(), gradientBar.getSelectColorPointBtnP2().getColorInner());
		gb.setDirection(directionPane.getSelectedItem());
		if (gradientBar.isOriginalPlace()) {
			gb.setUseCell(true);
		} else {
			gb.setUseCell(false);
			gb.setBeginPlace((float) gradientBar.getStartValue());
			gb.setFinishPlace((float) gradientBar.getEndValue());
		}
		return gb;
	}

	/**
	 * ������Ǽ�һ���۲��߼����¼�
	 *
	 * @param listener �۲��߼����¼�
	 */
	public void registerChangeListener(final UIObserverListener listener) {
	   gradientBar.addChangeListener(new ChangeListener() {
		   public void stateChanged(ChangeEvent e) {
			   listener.doChange();
		   }
	   });
	   directionPane.addChangeListener(new ChangeListener() {
		   public void stateChanged(ChangeEvent e) {
			   listener.doChange();
		   }
	   });
	}

	@Override
	public boolean accept(Background background) {
		return background instanceof GradientBackground;
	}

	@Override
	public String title4PopupWindow() {
		return Inter.getLocText("Gradient-Color");
	}

}
